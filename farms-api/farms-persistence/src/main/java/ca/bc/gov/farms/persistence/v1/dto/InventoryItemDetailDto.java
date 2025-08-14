package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class InventoryItemDetailDto extends BaseDto<InventoryItemDetailDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemDetailDto.class);

    private Long inventoryItemDetailId;
    private Integer programYear;
    private String eligibilityInd;
    private Integer lineItem;
    private BigDecimal insurableValue;
    private BigDecimal premiumRate;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String commodityTypeCode;
    private String commodityTypeDesc;
    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private String multiStageCommdtyCode;
    private String multiStageCommdtyDesc;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public InventoryItemDetailDto() {
    }

    public InventoryItemDetailDto(InventoryItemDetailDto dto) {
        this.inventoryItemDetailId = dto.inventoryItemDetailId;
        this.programYear = dto.programYear;
        this.eligibilityInd = dto.eligibilityInd;
        this.lineItem = dto.lineItem;
        this.insurableValue = dto.insurableValue;
        this.premiumRate = dto.premiumRate;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.commodityTypeCode = dto.commodityTypeCode;
        this.commodityTypeDesc = dto.commodityTypeDesc;
        this.fruitVegTypeCode = dto.fruitVegTypeCode;
        this.fruitVegTypeDesc = dto.fruitVegTypeDesc;
        this.multiStageCommdtyCode = dto.multiStageCommdtyCode;
        this.multiStageCommdtyDesc = dto.multiStageCommdtyDesc;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public InventoryItemDetailDto copy() {
        return new InventoryItemDetailDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(InventoryItemDetailDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(InventoryItemDetailDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result
                    && dtoUtils.equals("inventoryItemDetailId", inventoryItemDetailId, other.inventoryItemDetailId);
            result = result && dtoUtils.equals("programYear", programYear, other.programYear);
            result = result && dtoUtils.equals("eligibilityInd", eligibilityInd, other.eligibilityInd);
            result = result && dtoUtils.equals("lineItem", lineItem, other.lineItem);
            result = result && dtoUtils.equals("insurableValue", insurableValue, other.insurableValue);
            result = result && dtoUtils.equals("premiumRate", premiumRate, other.premiumRate);
            result = result && dtoUtils.equals("inventoryItemCode", inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("commodityTypeCode", commodityTypeCode, other.commodityTypeCode);
            result = result && dtoUtils.equals("commodityTypeDesc", commodityTypeDesc, other.commodityTypeDesc);
            result = result && dtoUtils.equals("fruitVegTypeCode", fruitVegTypeCode, other.fruitVegTypeCode);
            result = result && dtoUtils.equals("fruitVegTypeDesc", fruitVegTypeDesc, other.fruitVegTypeDesc);
            result = result
                    && dtoUtils.equals("multiStageCommdtyCode", multiStageCommdtyCode, other.multiStageCommdtyCode);
            result = result
                    && dtoUtils.equals("multiStageCommdtyDesc", multiStageCommdtyDesc, other.multiStageCommdtyDesc);
        }

        return result;
    }

    public Long getInventoryItemDetailId() {
        return inventoryItemDetailId;
    }

    public void setInventoryItemDetailId(Long inventoryItemDetailId) {
        this.inventoryItemDetailId = inventoryItemDetailId;
    }

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    public String getEligibilityInd() {
        return eligibilityInd;
    }

    public void setEligibilityInd(String eligibilityInd) {
        this.eligibilityInd = eligibilityInd;
    }

    public Integer getLineItem() {
        return lineItem;
    }

    public void setLineItem(Integer lineItem) {
        this.lineItem = lineItem;
    }

    public BigDecimal getInsurableValue() {
        return insurableValue;
    }

    public void setInsurableValue(BigDecimal insurableValue) {
        this.insurableValue = insurableValue;
    }

    public BigDecimal getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(BigDecimal premiumRate) {
        this.premiumRate = premiumRate;
    }

    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    public String getInventoryItemDesc() {
        return inventoryItemDesc;
    }

    public void setInventoryItemDesc(String inventoryItemDesc) {
        this.inventoryItemDesc = inventoryItemDesc;
    }

    public String getCommodityTypeCode() {
        return commodityTypeCode;
    }

    public void setCommodityTypeCode(String commodityTypeCode) {
        this.commodityTypeCode = commodityTypeCode;
    }

    public String getCommodityTypeDesc() {
        return commodityTypeDesc;
    }

    public void setCommodityTypeDesc(String commodityTypeDesc) {
        this.commodityTypeDesc = commodityTypeDesc;
    }

    public String getFruitVegTypeCode() {
        return fruitVegTypeCode;
    }

    public void setFruitVegTypeCode(String fruitVegTypeCode) {
        this.fruitVegTypeCode = fruitVegTypeCode;
    }

    public String getFruitVegTypeDesc() {
        return fruitVegTypeDesc;
    }

    public void setFruitVegTypeDesc(String fruitVegTypeDesc) {
        this.fruitVegTypeDesc = fruitVegTypeDesc;
    }

    public String getMultiStageCommdtyCode() {
        return multiStageCommdtyCode;
    }

    public void setMultiStageCommdtyCode(String multiStageCommdtyCode) {
        this.multiStageCommdtyCode = multiStageCommdtyCode;
    }

    public String getMultiStageCommdtyDesc() {
        return multiStageCommdtyDesc;
    }

    public void setMultiStageCommdtyDesc(String multiStageCommdtyDesc) {
        this.multiStageCommdtyDesc = multiStageCommdtyDesc;
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
