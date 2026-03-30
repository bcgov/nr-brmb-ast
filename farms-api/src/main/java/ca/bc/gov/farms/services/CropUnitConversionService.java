package ca.bc.gov.farms.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.CropUnitConversionResourceAssembler;
import ca.bc.gov.farms.data.entities.ConversionUnitEntity;
import ca.bc.gov.farms.data.entities.CropUnitConversionEntity;
import ca.bc.gov.farms.data.mappers.CropUnitConversionMapper;
import ca.bc.gov.farms.data.models.CropUnitConversionListModel;
import ca.bc.gov.farms.data.models.CropUnitConversionModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CropUnitConversionService {

    @Autowired
    private CropUnitConversionMapper cropUnitConversionMapper;

    @Autowired
    private CropUnitConversionResourceAssembler cropUnitConversionResourceAssembler;

    @Autowired
    private Validator validator;

    public CropUnitConversionListModel getAllCropUnitConversions() throws ServiceException {

        CropUnitConversionListModel result = null;

        try {
            List<CropUnitConversionEntity> entities = cropUnitConversionMapper.fetchAll();

            result = cropUnitConversionResourceAssembler.getCropUnitConversionList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public CropUnitConversionListModel getCropUnitConversionsByInventoryItemCode(String inventoryItemCode)
            throws ServiceException {

        CropUnitConversionListModel result = null;

        try {
            List<CropUnitConversionEntity> entities = cropUnitConversionMapper
                    .fetchByInventoryItemCode(inventoryItemCode);

            result = cropUnitConversionResourceAssembler.getCropUnitConversionList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public CropUnitConversionModel getCropUnitConversion(Long cropUnitDefaultId)
            throws ServiceException, NotFoundException {

        CropUnitConversionModel result = null;

        try {
            CropUnitConversionEntity entity = cropUnitConversionMapper.fetch(cropUnitDefaultId);

            if (entity == null) {
                throw new NotFoundException("Did not find the crop unit conversion: " + cropUnitDefaultId);
            }

            result = cropUnitConversionResourceAssembler.getCropUnitConversion(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public CropUnitConversionModel createCropUnitConversion(CropUnitConversionModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<CropUnitConversionModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        CropUnitConversionModel result = null;
        String userId = resource.getUserEmail();

        try {

            CropUnitConversionEntity dto = new CropUnitConversionEntity();

            cropUnitConversionResourceAssembler.updateCropUnitConversion(resource, dto);
            cropUnitConversionMapper.insertCropUnitDefault(dto, userId);
            for (ConversionUnitEntity conversionUnitDto : dto.getConversionUnits()) {
                int count = cropUnitConversionMapper.insertCropUnitConversionFactor(conversionUnitDto, userId);
                if (count == 0) {
                    throw new ServiceException("Record not inserted: " + count);
                }
            }

            dto = cropUnitConversionMapper.fetch(dto.getCropUnitDefaultId());
            result = cropUnitConversionResourceAssembler.getCropUnitConversion(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public CropUnitConversionModel updateCropUnitConversion(Long cropUnitDefaultId, CropUnitConversionModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<CropUnitConversionModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        CropUnitConversionModel result = null;
        String userId = resource.getUserEmail();

        try {

            CropUnitConversionEntity dto = cropUnitConversionMapper.fetch(cropUnitDefaultId);

            if (dto == null) {
                throw new NotFoundException("Did not find the crop unit conversion: " + cropUnitDefaultId);
            }

            cropUnitConversionResourceAssembler.updateCropUnitConversion(resource, dto);
            int count = cropUnitConversionMapper.updateCropUnitDefault(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            Set<Long> cropUnitConversionFactorIds = new HashSet<>();
            for (ConversionUnitEntity conversionUnitDto : dto.getConversionUnits()) {
                // insert
                if (conversionUnitDto.getCropUnitConversionFactorId() == null) {
                    count = cropUnitConversionMapper.insertCropUnitConversionFactor(conversionUnitDto, userId);
                    if (count == 0) {
                        throw new ServiceException("Record not inserted: " + count);
                    }
                    cropUnitConversionFactorIds.add(conversionUnitDto.getCropUnitConversionFactorId());
                }
                // update
                else {
                    count = cropUnitConversionMapper.updateCropUnitConversionFactor(conversionUnitDto, userId);
                    if (count == 0) {
                        throw new ServiceException("Record not updated: " + count);
                    }
                    cropUnitConversionFactorIds.add(conversionUnitDto.getCropUnitConversionFactorId());
                }
            }

            // delete
            dto = cropUnitConversionMapper.fetch(cropUnitDefaultId);
            for (ConversionUnitEntity conversionUnit : dto.getConversionUnits()) {
                Long cropUnitConversionFactorId = conversionUnit.getCropUnitConversionFactorId();
                if (!cropUnitConversionFactorIds.contains(cropUnitConversionFactorId)) {
                    count = cropUnitConversionMapper.deleteCropUnitConversionFactor(cropUnitConversionFactorId);
                    if (count == 0) {
                        throw new ServiceException("Record not deleted: " + count);
                    }
                }
            }

            dto = cropUnitConversionMapper.fetch(cropUnitDefaultId);
            result = cropUnitConversionResourceAssembler.getCropUnitConversion(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteCropUnitConversion(Long cropUnitDefaultId) throws ServiceException, NotFoundException {

        try {
            CropUnitConversionEntity entity = cropUnitConversionMapper.fetch(cropUnitDefaultId);

            if (entity == null) {
                throw new NotFoundException("Did not find the crop unit conversion: " + cropUnitDefaultId);
            }

            for (ConversionUnitEntity conversionUnitEntity : entity.getConversionUnits()) {
                int count = cropUnitConversionMapper
                        .deleteCropUnitConversionFactor(conversionUnitEntity.getCropUnitConversionFactorId());
                if (count == 0) {
                    throw new ServiceException("Record not deleted: " + count);
                }
            }

            int count = cropUnitConversionMapper.deleteCropUnitDefault(cropUnitDefaultId);
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
