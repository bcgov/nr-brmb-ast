package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface InventoryItemDetail extends Serializable {

    public Long getInventoryItemDetailId();
    public void setInventoryItemDetailId(Long inventoryItemDetailId);

    public Integer getProgramYear();
    public void setProgramYear(Integer programYear);

    public String getEligibilityInd();
    public void setEligibilityInd(String eligibilityInd);

    public Integer getLineItem();
    public void setLineItem(Integer lineItem);

    public BigDecimal getInsurableValue();
    public void setInsurableValue(BigDecimal insurableValue);

    public BigDecimal getPremiumRate();
    public void setPremiumRate(BigDecimal premiumRate);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getCommodityTypeCode();
    public void setCommodityTypeCode(String commodityTypeCode);

    public String getCommodityTypeDesc();
    public void setCommodityTypeDesc(String commodityTypeDesc);

    public String getFruitVegTypeCode();
    public void setFruitVegTypeCode(String fruitVegTypeCode);

    public String getFruitVegTypeDesc();
    public void setFruitVegTypeDesc(String fruitVegTypeDesc);

    public String getMultiStageCommdtyCode();
    public void setMultiStageCommdtyCode(String multiStageCommdtyCode);

    public String getMultiStageCommdtyDesc();
    public void setMultiStageCommdtyDesc(String multiStageCommdtyDesc);

    public Long getUrlId();
    public void setUrlId(Long urlId);

    public String getUrl();
    public void setUrl(String url);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
