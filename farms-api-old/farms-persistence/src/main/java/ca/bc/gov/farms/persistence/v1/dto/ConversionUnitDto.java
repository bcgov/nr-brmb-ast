package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class ConversionUnitDto extends BaseDto<ConversionUnitDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ConversionUnitDto.class);

    private Long cropUnitConversionFactorId;
    private BigDecimal conversionFactor;
    private String targetCropUnitCode;
    private String targetCropUnitDesc;

    public ConversionUnitDto() {
    }

    public ConversionUnitDto(ConversionUnitDto dto) {
        this.cropUnitConversionFactorId = dto.cropUnitConversionFactorId;
        this.conversionFactor = dto.conversionFactor;
        this.targetCropUnitCode = dto.targetCropUnitCode;
        this.targetCropUnitDesc = dto.targetCropUnitDesc;
    }

    @Override
    public ConversionUnitDto copy() {
        return new ConversionUnitDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(ConversionUnitDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(ConversionUnitDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("cropUnitConversionFactorId", this.cropUnitConversionFactorId,
                    other.cropUnitConversionFactorId);
            result = result && dtoUtils.equals("conversionFactor", this, other);
            result = result && dtoUtils.equals("targetCropUnitCode", this.targetCropUnitCode, other.targetCropUnitCode);
            result = result && dtoUtils.equals("targetCropUnitDesc", this.targetCropUnitDesc, other.targetCropUnitDesc);
        }

        return result;
    }

    public Long getCropUnitConversionFactorId() {
        return cropUnitConversionFactorId;
    }

    public void setCropUnitConversionFactorId(Long cropUnitConversionFactorId) {
        this.cropUnitConversionFactorId = cropUnitConversionFactorId;
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public String getTargetCropUnitCode() {
        return targetCropUnitCode;
    }

    public void setTargetCropUnitCode(String targetCropUnitCode) {
        this.targetCropUnitCode = targetCropUnitCode;
    }

    public String getTargetCropUnitDesc() {
        return targetCropUnitDesc;
    }

    public void setTargetCropUnitDesc(String targetCropUnitDesc) {
        this.targetCropUnitDesc = targetCropUnitDesc;
    }
}
