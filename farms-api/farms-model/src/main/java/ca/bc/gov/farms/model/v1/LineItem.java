package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface LineItem extends Serializable {

    public Long getLineItemId();
    public void setLineItemId(Long lineItemId);

    public Integer getProgramYear();
    public void setProgramYear(Integer programYear);

    public Integer getLineItem();
    public void setLineItem(Integer lineItem);

    public String getDescription();
    public void setDescription(String description);

    public String getProvince();
    public void setProvince(String province);

    public String getEligibilityInd();
    public void setEligibilityInd(String eligibilityInd);

    public String getEligibilityForRefYearsInd();
    public void setEligibilityForRefYearsInd(String eligibilityForRefYearsInd);

    public String getYardageInd();
    public void setYardageInd(String yardageInd);

    public String getProgramPaymentInd();
    public void setProgramPaymentInd(String programPaymentInd);

    public String getContractWorkInd();
    public void setContractWorkInd(String contractWorkInd);

    public String getSupplyManagedCommodityInd();
    public void setSupplyManagedCommodityInd(String supplyManagedCommodityInd);

    public String getManualExpenseInd();
    public void setManualExpenseInd(String manualExpenseInd);

    public String getExcludeFromRevenueCalcInd();
    public void setExcludeFromRevenueCalcInd(String excludeFromRevenueCalcInd);

    public String getIndustryAverageExpenseInd();
    public void setIndustryAverageExpenseInd(String industryAverageExpenseInd);

    public String getCommodityTypeCode();
    public void setCommodityTypeCode(String commodityTypeCode);

    public String getFruitVegTypeCode();
    public void setFruitVegTypeCode(String fruitVegTypeCode);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
