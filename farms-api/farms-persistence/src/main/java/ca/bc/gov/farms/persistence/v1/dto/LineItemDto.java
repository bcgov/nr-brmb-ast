package ca.bc.gov.farms.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class LineItemDto extends BaseDto<LineItemDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(LineItemDto.class);

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

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public LineItemDto() {
    }

    public LineItemDto(LineItemDto dto) {
        this.lineItemId = dto.lineItemId;
        this.programYear = dto.programYear;
        this.lineItem = dto.lineItem;
        this.description = dto.description;
        this.province = dto.province;
        this.eligibilityInd = dto.eligibilityInd;
        this.eligibilityForRefYearsInd = dto.eligibilityForRefYearsInd;
        this.yardageInd = dto.yardageInd;
        this.programPaymentInd = dto.programPaymentInd;
        this.contractWorkInd = dto.contractWorkInd;
        this.supplyManagedCommodityInd = dto.supplyManagedCommodityInd;
        this.manualExpenseInd = dto.manualExpenseInd;
        this.excludeFromRevenueCalcInd = dto.excludeFromRevenueCalcInd;
        this.industryAverageExpenseInd = dto.industryAverageExpenseInd;
        this.commodityTypeCode = dto.commodityTypeCode;
        this.fruitVegTypeCode = dto.fruitVegTypeCode;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public LineItemDto copy() {
        return new LineItemDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(LineItemDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(LineItemDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("lineItemId", this.lineItemId, other.lineItemId);
            result = result && dtoUtils.equals("programYear", this.programYear, other.programYear);
            result = result && dtoUtils.equals("lineItem", this.lineItem, other.lineItem);
            result = result && dtoUtils.equals("description", this.description, other.description);
            result = result && dtoUtils.equals("province", this.province, other.province);
            result = result && dtoUtils.equals("eligibilityInd", this.eligibilityInd, other.eligibilityInd);
            result = result && dtoUtils.equals("eligibilityForRefYearsInd", this.eligibilityForRefYearsInd,
                    other.eligibilityForRefYearsInd);
            result = result && dtoUtils.equals("yardageInd", this.yardageInd, other.yardageInd);
            result = result && dtoUtils.equals("programPaymentInd", this.programPaymentInd, other.programPaymentInd);
            result = result && dtoUtils.equals("contractWorkInd", this.contractWorkInd, other.contractWorkInd);
            result = result && dtoUtils.equals("supplyManagedCommodityInd", this.supplyManagedCommodityInd,
                    other.supplyManagedCommodityInd);
            result = result && dtoUtils.equals("manualExpenseInd", this.manualExpenseInd, other.manualExpenseInd);
            result = result && dtoUtils.equals("excludeFromRevenueCalcInd", this.excludeFromRevenueCalcInd,
                    other.excludeFromRevenueCalcInd);
            result = result && dtoUtils.equals("industryAverageExpenseInd", this.industryAverageExpenseInd,
                    other.industryAverageExpenseInd);
            result = result && dtoUtils.equals("commodityTypeCode", this.commodityTypeCode, other.commodityTypeCode);
            result = result && dtoUtils.equals("fruitVegTypeCode", this.fruitVegTypeCode, other.fruitVegTypeCode);
        }

        return result;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    public Integer getLineItem() {
        return lineItem;
    }

    public void setLineItem(Integer lineItem) {
        this.lineItem = lineItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getEligibilityInd() {
        return eligibilityInd;
    }

    public void setEligibilityInd(String eligibilityInd) {
        this.eligibilityInd = eligibilityInd;
    }

    public String getEligibilityForRefYearsInd() {
        return eligibilityForRefYearsInd;
    }

    public void setEligibilityForRefYearsInd(String eligibilityForRefYearsInd) {
        this.eligibilityForRefYearsInd = eligibilityForRefYearsInd;
    }

    public String getYardageInd() {
        return yardageInd;
    }

    public void setYardageInd(String yardageInd) {
        this.yardageInd = yardageInd;
    }

    public String getProgramPaymentInd() {
        return programPaymentInd;
    }

    public void setProgramPaymentInd(String programPaymentInd) {
        this.programPaymentInd = programPaymentInd;
    }

    public String getContractWorkInd() {
        return contractWorkInd;
    }

    public void setContractWorkInd(String contractWorkInd) {
        this.contractWorkInd = contractWorkInd;
    }

    public String getSupplyManagedCommodityInd() {
        return supplyManagedCommodityInd;
    }

    public void setSupplyManagedCommodityInd(String supplyManagedCommodityInd) {
        this.supplyManagedCommodityInd = supplyManagedCommodityInd;
    }

    public String getManualExpenseInd() {
        return manualExpenseInd;
    }

    public void setManualExpenseInd(String manualExpenseInd) {
        this.manualExpenseInd = manualExpenseInd;
    }

    public String getExcludeFromRevenueCalcInd() {
        return excludeFromRevenueCalcInd;
    }

    public void setExcludeFromRevenueCalcInd(String excludeFromRevenueCalcInd) {
        this.excludeFromRevenueCalcInd = excludeFromRevenueCalcInd;
    }

    public String getIndustryAverageExpenseInd() {
        return industryAverageExpenseInd;
    }

    public void setIndustryAverageExpenseInd(String industryAverageExpenseInd) {
        this.industryAverageExpenseInd = industryAverageExpenseInd;
    }

    public String getCommodityTypeCode() {
        return commodityTypeCode;
    }

    public void setCommodityTypeCode(String commodityTypeCode) {
        this.commodityTypeCode = commodityTypeCode;
    }

    public String getFruitVegTypeCode() {
        return fruitVegTypeCode;
    }

    public void setFruitVegTypeCode(String fruitVegTypeCode) {
        this.fruitVegTypeCode = fruitVegTypeCode;
    }

    public Integer getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(Integer revisionCount) {
        this.revisionCount = revisionCount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
