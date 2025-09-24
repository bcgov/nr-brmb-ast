package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.CropUnitConversion;
import ca.bc.gov.farms.model.v1.CropUnitConversionList;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;

public interface CropUnitConversionFactory {

    CropUnitConversion getCropUnitConversion(CropUnitConversionDto dto, FactoryContext context);

    CropUnitConversionList<? extends CropUnitConversion> getCropUnitConversionList(
            List<CropUnitConversionDto> dtos, FactoryContext context);

    void updateCropUnitConversion(CropUnitConversionDto dto, CropUnitConversion model);
}
