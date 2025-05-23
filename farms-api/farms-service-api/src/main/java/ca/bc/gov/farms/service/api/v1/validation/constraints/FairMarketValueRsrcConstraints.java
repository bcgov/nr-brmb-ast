package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface FairMarketValueRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = FairMarketValueRsrcConstraints.class)
    public Integer getProgramYear();

    @NotBlank(message = Errors.INVENTORY_ITEM_CODE_NOTBLANK, groups = FairMarketValueRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = FairMarketValueRsrcConstraints.class)
    public String getInventoryItemCode();

    @NotBlank(message = Errors.MUNICIPALITY_CODE_NOTBLANK, groups = FairMarketValueRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.MUNICIPALITY_CODE_SIZE, groups = FairMarketValueRsrcConstraints.class)
    public String getMunicipalityCode();

    @NotBlank(message = Errors.CROP_UNIT_CODE_NOTBLANK, groups = FairMarketValueRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.CROP_UNIT_CODE_SIZE, groups = FairMarketValueRsrcConstraints.class)
    public String getCropUnitCode();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_01_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod01Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_02_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod02Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_03_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod03Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_04_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod04Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_05_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod05Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_06_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod06Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_07_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod07Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_08_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod08Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_09_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod09Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_10_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod10Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_11_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod11Price();

    @Digits(integer = 11, fraction = 2, message = Errors.PERIOD_12_PRICE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod12Price();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_01_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod01Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_02_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod02Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_03_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod03Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_04_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod04Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_05_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod05Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_06_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod06Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_07_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod07Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_08_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod08Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_09_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod09Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_10_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod10Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_11_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod11Variance();

    @Digits(integer = 3, fraction = 2, message = Errors.PERIOD_12_VARIANCE_DIGITS, groups = FairMarketValueRsrcConstraints.class)
    public BigDecimal getPeriod12Variance();
}
