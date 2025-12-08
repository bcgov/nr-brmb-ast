package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface ExpectedProduction extends Serializable {

    public Long getExpectedProductionId();
    public void setExpectedProductionId(Long expectedProductionId);

    public BigDecimal getExpectedProductionPerProdUnit();
    public void setExpectedProductionPerProdUnit(BigDecimal expectedProductionPerProdUnit);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getCropUnitCode();
    public void setCropUnitCode(String cropUnitCode);

    public String getCropUnitDesc();
    public void setCropUnitDesc(String cropUnitDesc);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
