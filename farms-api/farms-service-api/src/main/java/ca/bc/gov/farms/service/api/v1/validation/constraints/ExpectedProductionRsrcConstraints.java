package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface ExpectedProductionRsrcConstraints {

    @NotNull(message = Errors.EXPECTED_PRODUCTION_PER_PROD_UNIT_NOTNULL, groups = ExpectedProductionRsrcConstraints.class)
    @Digits(integer = 10, fraction = 3, message = Errors.EXPECTED_PRODUCTION_PER_PROD_UNIT_DIGITS, groups = ExpectedProductionRsrcConstraints.class)
    public BigDecimal getExpectedProductionPerProdUnit();

    @NotBlank(message = Errors.INVENTORY_ITEM_CODE_NOTBLANK, groups = ExpectedProductionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = ExpectedProductionRsrcConstraints.class)
    public String getInventoryItemCode();

    @NotBlank(message = Errors.CROP_UNIT_CODE_NOTBLANK, groups = ExpectedProductionRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.CROP_UNIT_CODE_SIZE, groups = ExpectedProductionRsrcConstraints.class)
    public String getCropUnitCode();

}
