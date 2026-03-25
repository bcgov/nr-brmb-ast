package ca.bc.gov.farms.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.ConflictException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.FairMarketValueResourceAssembler;
import ca.bc.gov.farms.data.entities.FairMarketValueEntity;
import ca.bc.gov.farms.data.mappers.FairMarketValueMapper;
import ca.bc.gov.farms.data.models.FairMarketValueListModel;
import ca.bc.gov.farms.data.models.FairMarketValueModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FairMarketValueService {

    @Autowired
    private FairMarketValueMapper fairMarketValueMapper;

    @Autowired
    private FairMarketValueResourceAssembler fairMarketValueResourceAssembler;

    @Autowired
    private Validator validator;

    public FairMarketValueListModel getFairMarketValuesByProgramYear(Integer programYear) throws ServiceException {

        FairMarketValueListModel result = null;

        try {
            List<FairMarketValueEntity> entities = fairMarketValueMapper.fetchByProgramYear(programYear);

            result = fairMarketValueResourceAssembler.getFairMarketValueList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public FairMarketValueModel getFairMarketValue(String fairMarketValueId)
            throws ServiceException, NotFoundException {

        FairMarketValueModel result = null;

        try {
            String[] parts = fairMarketValueId.split("_");
            Integer programYear = Integer.valueOf(parts[0]);
            FairMarketValueEntity entity = fairMarketValueMapper.fetch(programYear, fairMarketValueId);

            if (entity == null) {
                throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
            }

            result = fairMarketValueResourceAssembler.getFairMarketValue(entity);

        } catch (PatternSyntaxException | NumberFormatException e) {
            throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public FairMarketValueModel createFairMarketValue(FairMarketValueModel resource)
            throws ServiceException, ConstraintViolationException, ConflictException {

        Set<ConstraintViolation<FairMarketValueModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        FairMarketValueModel result = null;
        String userId = resource.getUserEmail();

        try {

            List<FairMarketValueEntity> entities = fairMarketValueMapper.fetchBy(resource.getProgramYear(),
                    resource.getInventoryItemCode(), resource.getMunicipalityCode(),
                    resource.getCropUnitCode());
            if (!entities.isEmpty()) {
                throw new ConflictException("Fair market value already exists for the given parameters.");
            }

            FairMarketValueEntity dto = new FairMarketValueEntity();

            fairMarketValueResourceAssembler.updateFairMarketValue(resource, dto);

            BigDecimal[] averagePrices = { dto.getPeriod01Price(), dto.getPeriod02Price(), dto.getPeriod03Price(),
                    dto.getPeriod04Price(), dto.getPeriod05Price(), dto.getPeriod06Price(),
                    dto.getPeriod07Price(), dto.getPeriod08Price(), dto.getPeriod09Price(),
                    dto.getPeriod10Price(), dto.getPeriod11Price(), dto.getPeriod12Price() };
            BigDecimal[] percentVariances = { dto.getPeriod01Variance(), dto.getPeriod02Variance(),
                    dto.getPeriod03Variance(), dto.getPeriod04Variance(), dto.getPeriod05Variance(),
                    dto.getPeriod06Variance(), dto.getPeriod07Variance(), dto.getPeriod08Variance(),
                    dto.getPeriod09Variance(), dto.getPeriod10Variance(), dto.getPeriod11Variance(),
                    dto.getPeriod12Variance() };

            for (int period = 1; period <= 12; period++) {
                BigDecimal averagePrice = averagePrices[period - 1];
                BigDecimal percentVariance = percentVariances[period - 1];
                int count = fairMarketValueMapper.insertFairMarketValue(dto, userId, period, averagePrice,
                        percentVariance);
                if (count == 0) {
                    throw new ServiceException("Record not inserted: " + count);
                }
            }

            dto = fairMarketValueMapper.fetch(dto.getProgramYear(), dto.getFairMarketValueId());
            result = fairMarketValueResourceAssembler.getFairMarketValue(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public FairMarketValueModel updateFairMarketValue(String fairMarketValueId, FairMarketValueModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<FairMarketValueModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        FairMarketValueModel result = null;
        String userId = resource.getUserEmail();

        try {

            String[] parts = fairMarketValueId.split("_");
            Integer programYear = Integer.valueOf(parts[0]);
            FairMarketValueEntity dto = fairMarketValueMapper.fetch(programYear, fairMarketValueId);

            if (dto == null) {
                throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
            }

            fairMarketValueResourceAssembler.updateFairMarketValue(resource, dto);

            BigDecimal[] averagePrices = { dto.getPeriod01Price(), dto.getPeriod02Price(), dto.getPeriod03Price(),
                    dto.getPeriod04Price(), dto.getPeriod05Price(), dto.getPeriod06Price(),
                    dto.getPeriod07Price(), dto.getPeriod08Price(), dto.getPeriod09Price(),
                    dto.getPeriod10Price(), dto.getPeriod11Price(), dto.getPeriod12Price() };
            BigDecimal[] percentVariances = { dto.getPeriod01Variance(), dto.getPeriod02Variance(),
                    dto.getPeriod03Variance(), dto.getPeriod04Variance(), dto.getPeriod05Variance(),
                    dto.getPeriod06Variance(), dto.getPeriod07Variance(), dto.getPeriod08Variance(),
                    dto.getPeriod09Variance(), dto.getPeriod10Variance(), dto.getPeriod11Variance(),
                    dto.getPeriod12Variance() };

            for (int period = 1; period <= 12; period++) {
                BigDecimal averagePrice = averagePrices[period - 1];
                BigDecimal percentVariance = percentVariances[period - 1];
                int count = fairMarketValueMapper.updateFairMarketValue(dto, dto.getUrlId(), userId, programYear,
                        averagePrice, percentVariance);
                if (count == 0) {
                    throw new NotFoundException("Record not updated: " + count);
                }
            }

            dto = fairMarketValueMapper.fetch(dto.getProgramYear(), dto.getFairMarketValueId());
            result = fairMarketValueResourceAssembler.getFairMarketValue(dto);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteFairMarketValue(String fairMarketValueId) throws ServiceException, NotFoundException {

        try {
            String[] parts = fairMarketValueId.split("_");
            Integer programYear = Integer.valueOf(parts[0]);
            FairMarketValueEntity entity = fairMarketValueMapper.fetch(programYear, fairMarketValueId);

            if (entity == null) {
                throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
            }

            fairMarketValueMapper.deleteFairMarketValue(programYear, fairMarketValueId);
        } catch (PatternSyntaxException | NumberFormatException e) {
            throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
