package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;

import ca.bc.gov.farms.model.v1.ConversionUnit;

public class ConversionUnitRsrc implements ConversionUnit {

    private static final long serialVersionUID = 1L;

    private Long cropUnitConversionFactorId;
    private BigDecimal conversionFactor;
    private String targetCropUnitCode;
    private String targetCropUnitDesc;

    @Override
    public Long getCropUnitConversionFactorId() {
        return cropUnitConversionFactorId;
    }

    @Override
    public void setCropUnitConversionFactorId(Long cropUnitConversionFactorId) {
        this.cropUnitConversionFactorId = cropUnitConversionFactorId;
    }

    @Override
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public String getTargetCropUnitCode() {
        return targetCropUnitCode;
    }

    @Override
    public void setTargetCropUnitCode(String targetCropUnitCode) {
        this.targetCropUnitCode = targetCropUnitCode;
    }

    @Override
    public String getTargetCropUnitDesc() {
        return targetCropUnitDesc;
    }

    @Override
    public void setTargetCropUnitDesc(String targetCropUnitDesc) {
        this.targetCropUnitDesc = targetCropUnitDesc;
    }
}
