package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface CropUnitConversion extends Serializable {

    public Long getCropUnitConversionFactorId();
    public void setCropUnitConversionFactorId(Long cropUnitConversionFactorId);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getCropUnitCode();
    public void setCropUnitCode(String cropUnitCode);

    public String getCropUnitDesc();
    public void setCropUnitDesc(String cropUnitDesc);

    public BigDecimal getConversionFactor();
    public void setConversionFactor(BigDecimal conversionFactor);

    public String getTargetCropUnitCode();
    public void setTargetCropUnitCode(String targetCropUnitCode);

    public String getTargetCropUnitDesc();
    public void setTargetCropUnitDesc(String targetCropUnitDesc);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
