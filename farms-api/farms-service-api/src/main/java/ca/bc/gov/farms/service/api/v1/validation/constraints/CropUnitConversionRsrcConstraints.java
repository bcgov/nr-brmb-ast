package ca.bc.gov.farms.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface CropUnitConversionRsrcConstraints {

    @NotBlank(message = Errors.INVENTORY_ITEM_CODE_NOTBLANK, groups = CropUnitConversionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = CropUnitConversionRsrcConstraints.class)
    public String getInventoryItemCode();

    @NotBlank(message = Errors.CROP_UNIT_CODE_NOTBLANK, groups = CropUnitConversionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.CROP_UNIT_CODE_SIZE, groups = CropUnitConversionRsrcConstraints.class)
    public String getCropUnitCode();

}
