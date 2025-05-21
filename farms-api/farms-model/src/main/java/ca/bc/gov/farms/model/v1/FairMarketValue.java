package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface FairMarketValue extends Serializable {

    public String getFairMarketValueId();
    public void setFairMarketValueId(String fairMarketValueId);

    public String getProgramYear();
    public void setProgramYear(String programYear);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getMunicipalityCode();
    public void setMunicipalityCode(String municipalityCode);

    public String getMunicipalityDesc();
    public void setMunicipalityDesc(String municipalityDesc);

    public String getCropUnitCode();
    public void setCropUnitCode(String cropUnitCode);

    public String getCropUnitDesc();
    public void setCropUnitDesc(String cropUnitDesc);

    public String getDefaultCropUnitCode();
    public void setDefaultCropUnitCode(String defaultCropUnitCode);

    public String getDefaultCropUnitDesc();
    public void setDefaultCropUnitDesc(String defaultCropUnitDesc);

    public BigDecimal getPeriod01Price();
    public void setPeriod01Price(BigDecimal period01Price);

    public BigDecimal getPeriod02Price();
    public void setPeriod02Price(BigDecimal period02Price);

    public BigDecimal getPeriod03Price();
    public void setPeriod03Price(BigDecimal period03Price);

    public BigDecimal getPeriod04Price();
    public void setPeriod04Price(BigDecimal period04Price);

    public BigDecimal getPeriod05Price();
    public void setPeriod05Price(BigDecimal period05Price);

    public BigDecimal getPeriod06Price();
    public void setPeriod06Price(BigDecimal period06Price);

    public BigDecimal getPeriod07Price();
    public void setPeriod07Price(BigDecimal period07Price);

    public BigDecimal getPeriod08Price();
    public void setPeriod08Price(BigDecimal period08Price);

    public BigDecimal getPeriod09Price();
    public void setPeriod09Price(BigDecimal period09Price);

    public BigDecimal getPeriod10Price();
    public void setPeriod10Price(BigDecimal period10Price);

    public BigDecimal getPeriod11Price();
    public void setPeriod11Price(BigDecimal period11Price);

    public BigDecimal getPeriod12Price();
    public void setPeriod12Price(BigDecimal period12Price);

    public BigDecimal getPeriod01Variance();
    public void setPeriod01Variance(BigDecimal period01Variance);

    public BigDecimal getPeriod02Variance();
    public void setPeriod02Variance(BigDecimal period02Variance);

    public BigDecimal getPeriod03Variance();
    public void setPeriod03Variance(BigDecimal period03Variance);

    public BigDecimal getPeriod04Variance();
    public void setPeriod04Variance(BigDecimal period04Variance);

    public BigDecimal getPeriod05Variance();
    public void setPeriod05Variance(BigDecimal period05Variance);

    public BigDecimal getPeriod06Variance();
    public void setPeriod06Variance(BigDecimal period06Variance);

    public BigDecimal getPeriod07Variance();
    public void setPeriod07Variance(BigDecimal period07Variance);

    public BigDecimal getPeriod08Variance();
    public void setPeriod08Variance(BigDecimal period08Variance);

    public BigDecimal getPeriod09Variance();
    public void setPeriod09Variance(BigDecimal period09Variance);

    public BigDecimal getPeriod10Variance();
    public void setPeriod10Variance(BigDecimal period10Variance);

    public BigDecimal getPeriod11Variance();
    public void setPeriod11Variance(BigDecimal period11Variance);

    public BigDecimal getPeriod12Variance();
    public void setPeriod12Variance(BigDecimal period12Variance);
}
