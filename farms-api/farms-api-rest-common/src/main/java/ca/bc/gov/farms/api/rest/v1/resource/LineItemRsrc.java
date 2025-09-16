package ca.bc.gov.farms.api.rest.v1.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.LineItem;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.LINE_ITEM_ATTRIBUTE_NAME)
@XmlSeeAlso({ LineItemRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class LineItemRsrc extends BaseResource implements LineItem {

    private static final long serialVersionUID = 1L;

    private Long lineItemId;
    private Integer programYear;
    private Integer lineItem;
    private String description;
    private String province;
    private String eligibilityInd;
    private String eligibilityForRefYearsInd;
    private String yardageInd;
    private String programPaymentInd;
    private String contractWorkInd;
    private String supplyManagedCommodityInd;
    private String manualExpenseInd;
    private String excludeFromRevenueCalcInd;
    private String industryAverageExpenseInd;
    private String commodityTypeCode;
    private String fruitVegTypeCode;
    private String userEmail;

    @Override
    public Long getLineItemId() {
        return lineItemId;
    }

    @Override
    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    @Override
    public Integer getProgramYear() {
        return programYear;
    }

    @Override
    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    @Override
    public Integer getLineItem() {
        return lineItem;
    }

    @Override
    public void setLineItem(Integer lineItem) {
        this.lineItem = lineItem;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getProvince() {
        return province;
    }

    @Override
    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String getEligibilityInd() {
        return eligibilityInd;
    }

    @Override
    public void setEligibilityInd(String eligibilityInd) {
        this.eligibilityInd = eligibilityInd;
    }

    @Override
    public String getEligibilityForRefYearsInd() {
        return eligibilityForRefYearsInd;
    }

    @Override
    public void setEligibilityForRefYearsInd(String eligibilityForRefYearsInd) {
        this.eligibilityForRefYearsInd = eligibilityForRefYearsInd;
    }

    @Override
    public String getYardageInd() {
        return yardageInd;
    }

    @Override
    public void setYardageInd(String yardageInd) {
        this.yardageInd = yardageInd;
    }

    @Override
    public String getProgramPaymentInd() {
        return programPaymentInd;
    }

    @Override
    public void setProgramPaymentInd(String programPaymentInd) {
        this.programPaymentInd = programPaymentInd;
    }

    @Override
    public String getContractWorkInd() {
        return contractWorkInd;
    }

    @Override
    public void setContractWorkInd(String contractWorkInd) {
        this.contractWorkInd = contractWorkInd;
    }

    @Override
    public String getSupplyManagedCommodityInd() {
        return supplyManagedCommodityInd;
    }

    @Override
    public void setSupplyManagedCommodityInd(String supplyManagedCommodityInd) {
        this.supplyManagedCommodityInd = supplyManagedCommodityInd;
    }

    @Override
    public String getManualExpenseInd() {
        return manualExpenseInd;
    }

    @Override
    public void setManualExpenseInd(String manualExpenseInd) {
        this.manualExpenseInd = manualExpenseInd;
    }

    @Override
    public String getExcludeFromRevenueCalcInd() {
        return excludeFromRevenueCalcInd;
    }

    @Override
    public void setExcludeFromRevenueCalcInd(String excludeFromRevenueCalcInd) {
        this.excludeFromRevenueCalcInd = excludeFromRevenueCalcInd;
    }

    @Override
    public String getIndustryAverageExpenseInd() {
        return industryAverageExpenseInd;
    }

    @Override
    public void setIndustryAverageExpenseInd(String industryAverageExpenseInd) {
        this.industryAverageExpenseInd = industryAverageExpenseInd;
    }

    @Override
    public String getCommodityTypeCode() {
        return commodityTypeCode;
    }

    @Override
    public void setCommodityTypeCode(String commodityTypeCode) {
        this.commodityTypeCode = commodityTypeCode;
    }

    @Override
    public String getFruitVegTypeCode() {
        return fruitVegTypeCode;
    }

    @Override
    public void setFruitVegTypeCode(String fruitVegTypeCode) {
        this.fruitVegTypeCode = fruitVegTypeCode;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
