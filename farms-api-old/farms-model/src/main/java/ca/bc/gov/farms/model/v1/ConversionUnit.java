package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface ConversionUnit extends Serializable {

    public Long getCropUnitConversionFactorId();
    public void setCropUnitConversionFactorId(Long cropUnitConversionFactorId);

    public BigDecimal getConversionFactor();
    public void setConversionFactor(BigDecimal conversionFactor);

    public String getTargetCropUnitCode();
    public void setTargetCropUnitCode(String targetCropUnitCode);

    public String getTargetCropUnitDesc();
    public void setTargetCropUnitDesc(String targetCropUnitDesc);
}
