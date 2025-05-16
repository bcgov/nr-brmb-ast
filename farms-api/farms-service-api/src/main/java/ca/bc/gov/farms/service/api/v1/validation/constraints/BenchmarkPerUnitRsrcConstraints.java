package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface BenchmarkPerUnitRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = BenchmarkPerUnitRsrcConstraints.class)
    public Integer getProgramYear();

    @Size(min = 0, max = 2000, message = Errors.UNIT_COMMENT_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getUnitComment();

    @NotBlank(message = Errors.MUNICIPALITY_CODE_NOTBLANK, groups = BenchmarkPerUnitRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.MUNICIPALITY_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getMunicipalityCode();

    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getInventoryItemCode();

    @Size(min = 0, max = 10, message = Errors.STRUCTURE_GROUP_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getStructureGroupCode();

    @Size(min = 0, max = 10, message = Errors.INVENTORY_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getInventoryCode();

    @Size(min = 0, max = 256, message = Errors.INVENTORY_DESC_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getInventoryDesc();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_6_MARGIN_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus6Margin();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_5_MARGIN_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus5Margin();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_4_MARGIN_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus4Margin();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_3_MARGIN_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus3Margin();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_2_MARGIN_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus2Margin();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_1_MARGIN_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus1Margin();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_6_EXPENSE_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus6Expense();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_5_EXPENSE_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus5Expense();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_4_EXPENSE_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus4Expense();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_3_EXPENSE_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus3Expense();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_2_EXPENSE_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus2Expense();

    @Digits(integer = 11, fraction = 2, message = Errors.YEAR_MINUS_1_EXPENSE_DIGITS, groups = BenchmarkPerUnitRsrcConstraints.class)
    public BigDecimal getYearMinus1Expense();

}
