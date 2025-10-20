package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface InventoryItemDetailRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = InventoryItemDetailRsrcConstraints.class)
    public Integer getProgramYear();

    @NotBlank(message = Errors.ELIGIBILITY_IND_NOTBLANK, groups = InventoryItemDetailRsrcConstraints.class)
    @Size(min = 1, max = 1, message = Errors.ELIGIBILITY_IND_SIZE, groups = InventoryItemDetailRsrcConstraints.class)
    public String getEligibilityInd();

    @Digits(integer = 10, fraction = 3, message = Errors.INSURABLE_VALUE_DIGITS, groups = InventoryItemDetailRsrcConstraints.class)
    public BigDecimal getInsurableValue();

    @Digits(integer = 9, fraction = 4, message = Errors.PREMIUM_RATE_DIGITS, groups = InventoryItemDetailRsrcConstraints.class)
    public BigDecimal getPremiumRate();

    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = InventoryItemDetailRsrcConstraints.class)
    public String getInventoryItemCode();

    @Size(min = 0, max = 10, message = Errors.COMMODITY_TYPE_CODE_SIZE, groups = InventoryItemDetailRsrcConstraints.class)
    public String getCommodityTypeCode();

    @Size(min = 0, max = 10, message = Errors.FRUIT_VEG_TYPE_CODE_SIZE, groups = InventoryItemDetailRsrcConstraints.class)
    public String getFruitVegTypeCode();

    @Size(min = 0, max = 10, message = Errors.MULTI_STAGE_COMMDTY_CODE_SIZE, groups = InventoryItemDetailRsrcConstraints.class)
    public String getMultiStageCommdtyCode();
}
