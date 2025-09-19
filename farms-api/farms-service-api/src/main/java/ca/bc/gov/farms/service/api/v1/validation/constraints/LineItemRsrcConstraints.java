package ca.bc.gov.farms.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface LineItemRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = LineItemRsrcConstraints.class)
    public Integer getProgramYear();

    @NotNull(message = Errors.LINE_ITEM_NOTNULL, groups = LineItemRsrcConstraints.class)
    public Integer getLineItem();

    @NotBlank(message = Errors.DESCRIPTION_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 256, message = Errors.DESCRIPTION_SIZE, groups = LineItemRsrcConstraints.class)
    public String getDescription();

    @Size(min = 0, max = 2, message = Errors.PROVINCE_SIZE, groups = LineItemRsrcConstraints.class)
    public String getProvince();

    @NotBlank(message = Errors.ELIGIBILITY_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.ELIGIBILITY_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getEligibilityInd();

    @NotBlank(message = Errors.ELIGIBILITY_FOR_REF_YEARS_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.ELIGIBILITY_FOR_REF_YEARS_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getEligibilityForRefYearsInd();

    @NotBlank(message = Errors.YARDAGE_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.YARDAGE_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getYardageInd();

    @NotBlank(message = Errors.PROGRAM_PAYMENT_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.PROGRAM_PAYMENT_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getProgramPaymentInd();

    @NotBlank(message = Errors.CONTRACT_WORK_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.CONTRACT_WORK_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getContractWorkInd();

    @NotBlank(message = Errors.SUPPLY_MANAGED_COMMODITY_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.SUPPLY_MANAGED_COMMODITY_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getSupplyManagedCommodityInd();

    @NotBlank(message = Errors.EXCLUDE_FROM_REVENUE_CALC_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.EXCLUDE_FROM_REVENUE_CALC_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getExcludeFromRevenueCalcInd();

    @NotBlank(message = Errors.INDUSTRY_AVERAGE_EXPENSE_IND_NOTBLANK, groups = LineItemRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.INDUSTRY_AVERAGE_EXPENSE_IND_SIZE, groups = LineItemRsrcConstraints.class)
    public String getIndustryAverageExpenseInd();

    @Size(min = 0, max = 10, message = Errors.COMMODITY_TYPE_CODE_SIZE, groups = LineItemRsrcConstraints.class)
    public String getCommodityTypeCode();

    @Size(min = 0, max = 10, message = Errors.FRUIT_VEG_TYPE_CODE_SIZE, groups = LineItemRsrcConstraints.class)
    public String getFruitVegTypeCode();
}
