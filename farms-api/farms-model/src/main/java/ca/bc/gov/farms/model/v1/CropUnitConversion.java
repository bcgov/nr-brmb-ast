package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface CropUnitConversion extends Serializable {

    public Long getCropUnitDefaultId();
    public void setCropUnitDefaultId(Long cropUnitDefaultId);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getCropUnitCode();
    public void setCropUnitCode(String cropUnitCode);

    public String getCropUnitDesc();
    public void setCropUnitDesc(String cropUnitDesc);

    public List<ConversionUnit> getConversionUnits();
    public void setConversionUnits(List<ConversionUnit> conversionUnits);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
