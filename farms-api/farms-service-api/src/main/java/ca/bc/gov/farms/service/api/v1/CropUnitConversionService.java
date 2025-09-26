package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.CropUnitConversion;
import ca.bc.gov.farms.model.v1.CropUnitConversionList;

public interface CropUnitConversionService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    CropUnitConversionList<? extends CropUnitConversion> getAllCropUnitConversions(FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    CropUnitConversionList<? extends CropUnitConversion> getCropUnitConversionsByInventoryItemCode(
            String inventoryItemCode, FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    CropUnitConversion getCropUnitConversion(Long cropUnitConversionFactorId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    CropUnitConversion createCropUnitConversion(CropUnitConversion cropUnitConversion, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    CropUnitConversion updateCropUnitConversion(Long cropUnitConversionFactorId, CropUnitConversion cropUnitConversion,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteCropUnitConversion(Long cropUnitConversionFactorId)
            throws ServiceException, NotFoundException;
}
