package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.MarketRatePremiumResourceAssembler;
import ca.bc.gov.farms.data.entities.MarketRatePremiumEntity;
import ca.bc.gov.farms.data.mappers.MarketRatePremiumMapper;
import ca.bc.gov.farms.data.models.MarketRatePremiumListModel;
import ca.bc.gov.farms.data.models.MarketRatePremiumModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MarketRatePremiumService {

    @Autowired
    private MarketRatePremiumMapper marketRatePremiumMapper;

    @Autowired
    private MarketRatePremiumResourceAssembler marketRatePremiumResourceAssembler;

    @Autowired
    private Validator validator;

    public MarketRatePremiumListModel getAllMarketRatePremiums() throws ServiceException {

        MarketRatePremiumListModel result = null;

        try {
            List<MarketRatePremiumEntity> entities = marketRatePremiumMapper.fetchAll();

            result = marketRatePremiumResourceAssembler.getMarketRatePremiumList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public MarketRatePremiumModel getMarketRatePremium(Long marketRatePremiumId)
            throws ServiceException, NotFoundException {

        MarketRatePremiumModel result = null;

        try {
            MarketRatePremiumEntity entity = marketRatePremiumMapper.fetch(marketRatePremiumId);

            if (entity == null) {
                throw new NotFoundException("Did not find the market rate premium: " + marketRatePremiumId);
            }

            result = marketRatePremiumResourceAssembler.getMarketRatePremium(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public MarketRatePremiumModel createMarketRatePremium(MarketRatePremiumModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<MarketRatePremiumModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        MarketRatePremiumModel result = null;
        String userId = resource.getUserEmail();

        try {

            MarketRatePremiumEntity dto = new MarketRatePremiumEntity();

            marketRatePremiumResourceAssembler.updateMarketRatePremium(resource, dto);
            int count = marketRatePremiumMapper.insertMarketRatePremium(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = marketRatePremiumMapper.fetch(dto.getMarketRatePremiumId());
            result = marketRatePremiumResourceAssembler.getMarketRatePremium(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public MarketRatePremiumModel updateMarketRatePremium(Long marketRatePremiumId, MarketRatePremiumModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<MarketRatePremiumModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        MarketRatePremiumModel result = null;
        String userId = resource.getUserEmail();

        try {

            MarketRatePremiumEntity dto = marketRatePremiumMapper.fetch(marketRatePremiumId);

            if (dto == null) {
                throw new NotFoundException("Did not find the market rate premium: " + marketRatePremiumId);
            }

            marketRatePremiumResourceAssembler.updateMarketRatePremium(resource, dto);
            int count = marketRatePremiumMapper.updateMarketRatePremium(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = marketRatePremiumMapper.fetch(dto.getMarketRatePremiumId());
            result = marketRatePremiumResourceAssembler.getMarketRatePremium(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteMarketRatePremium(Long marketRatePremiumId) throws ServiceException, NotFoundException {

        try {
            MarketRatePremiumEntity entity = marketRatePremiumMapper.fetch(marketRatePremiumId);

            if (entity == null) {
                throw new NotFoundException("Did not find the market rate premium: " + marketRatePremiumId);
            }

            int count = marketRatePremiumMapper.deleteMarketRatePremium(marketRatePremiumId);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
