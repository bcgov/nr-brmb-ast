package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.InventoryItemDetail;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_ITEM_DETAIL_NAME)
@XmlSeeAlso({ InventoryItemDetailRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryItemDetailRsrc extends BaseResource implements InventoryItemDetail {

    private static final long serialVersionUID = 1L;

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
    private String userEmail;

    @Override
    public Long getInventoryItemDetailId() {
        return inventoryItemDetailId;
    }

    @Override
    public void setInventoryItemDetailId(Long inventoryItemDetailId) {
        this.inventoryItemDetailId = inventoryItemDetailId;
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
    public String getEligibilityInd() {
        return eligibilityInd;
    }

    @Override
    public void setEligibilityInd(String eligibilityInd) {
        this.eligibilityInd = eligibilityInd;
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
    public BigDecimal getInsurableValue() {
        return insurableValue;
    }

    @Override
    public void setInsurableValue(BigDecimal insurableValue) {
        this.insurableValue = insurableValue;
    }

    @Override
    public BigDecimal getPremiumRate() {
        return premiumRate;
    }

    @Override
    public void setPremiumRate(BigDecimal premiumRate) {
        this.premiumRate = premiumRate;
    }

    @Override
    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    @Override
    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    @Override
    public String getInventoryItemDesc() {
        return inventoryItemDesc;
    }

    @Override
    public void setInventoryItemDesc(String inventoryItemDesc) {
        this.inventoryItemDesc = inventoryItemDesc;
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
    public String getCommodityTypeDesc() {
        return commodityTypeDesc;
    }

    @Override
    public void setCommodityTypeDesc(String commodityTypeDesc) {
        this.commodityTypeDesc = commodityTypeDesc;
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
    public String getFruitVegTypeDesc() {
        return fruitVegTypeDesc;
    }

    @Override
    public void setFruitVegTypeDesc(String fruitVegTypeDesc) {
        this.fruitVegTypeDesc = fruitVegTypeDesc;
    }

    @Override
    public String getMultiStageCommdtyCode() {
        return multiStageCommdtyCode;
    }

    @Override
    public void setMultiStageCommdtyCode(String multiStageCommdtyCode) {
        this.multiStageCommdtyCode = multiStageCommdtyCode;
    }

    @Override
    public String getMultiStageCommdtyDesc() {
        return multiStageCommdtyDesc;
    }

    @Override
    public void setMultiStageCommdtyDesc(String multiStageCommdtyDesc) {
        this.multiStageCommdtyDesc = multiStageCommdtyDesc;
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
