package ca.bc.gov.farms.service.api.v1.impl;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.model.Message;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.code.UserUtil;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.CropUnitConversion;
import ca.bc.gov.farms.model.v1.CropUnitConversionList;
import ca.bc.gov.farms.persistence.v1.dao.CropUnitConversionDao;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;
import ca.bc.gov.farms.service.api.v1.CropUnitConversionService;
import ca.bc.gov.farms.service.api.v1.model.factory.CropUnitConversionFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class CropUnitConversionServiceImpl implements CropUnitConversionService {

    private static final Logger logger = LoggerFactory.getLogger(CropUnitConversionServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private CropUnitConversionFactory cropUnitConversionFactory;

    // daos
    private CropUnitConversionDao cropUnitConversionDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setCropUnitConversionFactory(CropUnitConversionFactory cropUnitConversionFactory) {
        this.cropUnitConversionFactory = cropUnitConversionFactory;
    }

    public void setCropUnitConversionDao(CropUnitConversionDao cropUnitConversionDao) {
        this.cropUnitConversionDao = cropUnitConversionDao;
    }

    @Override
    public CropUnitConversionList<? extends CropUnitConversion> getAllCropUnitConversions(FactoryContext factoryContext)
            throws ServiceException {
        logger.debug("<getAllCropUnitConversions");

        CropUnitConversionList<? extends CropUnitConversion> result = null;

        try {
            List<CropUnitConversionDto> dtos = cropUnitConversionDao.fetchAll();

            result = cropUnitConversionFactory.getCropUnitConversionList(dtos, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllCropUnitConversions");
        return result;
    }

    @Override
    public CropUnitConversionList<? extends CropUnitConversion> getCropUnitConversionsByInventoryItemCode(
            String inventoryItemCode, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getCropUnitConversionsByInventoryItemCode");

        CropUnitConversionList<? extends CropUnitConversion> result = null;

        try {
            List<CropUnitConversionDto> dtos = cropUnitConversionDao.fetchByInventoryItemCode(inventoryItemCode);

            result = cropUnitConversionFactory.getCropUnitConversionList(dtos, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getCropUnitConversionsByInventoryItemCode");
        return result;
    }

    @Override
    public CropUnitConversion getCropUnitConversion(Long cropUnitDefaultId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getCropUnitConversion");

        CropUnitConversion result = null;

        try {
            CropUnitConversionDto dto = cropUnitConversionDao.fetch(cropUnitDefaultId);

            if (dto == null) {
                throw new NotFoundException("Did not find the crop unit conversion: " + cropUnitDefaultId);
            }

            result = cropUnitConversionFactory.getCropUnitConversion(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getCropUnitConversion");
        return result;
    }

    @Override
    public CropUnitConversion createCropUnitConversion(CropUnitConversion resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createCropUnitConversion");

        CropUnitConversion result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateCropUnitConversion(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            CropUnitConversionDto dto = new CropUnitConversionDto();

            cropUnitConversionFactory.updateCropUnitConversion(dto, resource);
            cropUnitConversionDao.insert(dto, userId);

            dto = cropUnitConversionDao.fetch(dto.getCropUnitDefaultId());
            result = cropUnitConversionFactory.getCropUnitConversion(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createCropUnitConversion");
        return result;
    }

    @Override
    public CropUnitConversion updateCropUnitConversion(Long cropUnitDefaultId,
            CropUnitConversion cropUnitConversion,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateCropUnitConversion");

        CropUnitConversion result = null;
        String userId = UserUtil.toUserId(cropUnitConversion.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateCropUnitConversion(cropUnitConversion);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            CropUnitConversionDto dto = cropUnitConversionDao.fetch(cropUnitDefaultId);

            if (dto == null) {
                throw new NotFoundException("Did not find the crop unit conversion: "
                        + cropUnitDefaultId);
            }

            cropUnitConversionFactory.updateCropUnitConversion(dto, cropUnitConversion);
            cropUnitConversionDao.update(dto, userId);

            dto = cropUnitConversionDao.fetch(cropUnitDefaultId);
            result = cropUnitConversionFactory.getCropUnitConversion(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateCropUnitConversion");
        return result;
    }

    @Override
    public void deleteCropUnitConversion(Long cropUnitDefaultId)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteCropUnitConversion");

        try {
            CropUnitConversionDto dto = cropUnitConversionDao.fetch(cropUnitDefaultId);

            if (dto == null) {
                throw new NotFoundException("Did not find the crop unit conversion: " + cropUnitDefaultId);
            }

            cropUnitConversionDao.delete(cropUnitDefaultId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteCropUnitConversion");
    }

}
