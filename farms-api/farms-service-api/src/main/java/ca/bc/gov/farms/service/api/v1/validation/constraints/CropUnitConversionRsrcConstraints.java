package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface CropUnitConversionRsrcConstraints {

    @NotBlank(message = Errors.INVENTORY_ITEM_CODE_NOTBLANK, groups = CropUnitConversionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = CropUnitConversionRsrcConstraints.class)
    public String getInventoryItemCode();

    @NotBlank(message = Errors.CROP_UNIT_CODE_NOTBLANK, groups = CropUnitConversionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.CROP_UNIT_CODE_SIZE, groups = CropUnitConversionRsrcConstraints.class)
    public String getCropUnitCode();

    @NotNull(message = Errors.CONVERSION_FACTOR_NOTNULL, groups = CropUnitConversionRsrcConstraints.class)
    @Digits(integer = 8, fraction = 6, message = Errors.CONVERSION_FACTOR_DIGITS, groups = CropUnitConversionRsrcConstraints.class)
    public BigDecimal getConversionFactor();

    @NotBlank(message = Errors.TARGET_CROP_UNIT_CODE_NOTBLANK, groups = CropUnitConversionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.TARGET_CROP_UNIT_CODE_SIZE, groups = CropUnitConversionRsrcConstraints.class)
    public String getTargetCropUnitCode();

}
