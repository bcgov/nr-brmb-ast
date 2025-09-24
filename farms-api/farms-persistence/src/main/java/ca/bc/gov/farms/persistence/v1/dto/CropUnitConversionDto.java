package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class CropUnitConversionDto extends BaseDto<CropUnitConversionDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CropUnitConversionDto.class);

    private Long cropUnitConversionFactorId;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    private BigDecimal conversionFactor;
    private String targetCropUnitCode;
    private String targetCropUnitDesc;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public CropUnitConversionDto() {
    }

    public CropUnitConversionDto(CropUnitConversionDto dto) {
        this.cropUnitConversionFactorId = dto.cropUnitConversionFactorId;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.cropUnitCode = dto.cropUnitCode;
        this.cropUnitDesc = dto.cropUnitDesc;
        this.conversionFactor = dto.conversionFactor;
        this.targetCropUnitCode = dto.targetCropUnitCode;
        this.targetCropUnitDesc = dto.targetCropUnitDesc;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public CropUnitConversionDto copy() {
        return new CropUnitConversionDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(CropUnitConversionDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(CropUnitConversionDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("cropUnitConversionFactorId", this.cropUnitConversionFactorId,
                    other.cropUnitConversionFactorId);
            result = result && dtoUtils.equals("inventoryItemCode", this.inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", this.inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("cropUnitCode", this.cropUnitCode, other.cropUnitCode);
            result = result && dtoUtils.equals("cropUnitDesc", this.cropUnitDesc, other.cropUnitDesc);
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

    public String getCropUnitCode() {
        return cropUnitCode;
    }

    public void setCropUnitCode(String cropUnitCode) {
        this.cropUnitCode = cropUnitCode;
    }

    public String getCropUnitDesc() {
        return cropUnitDesc;
    }

    public void setCropUnitDesc(String cropUnitDesc) {
        this.cropUnitDesc = cropUnitDesc;
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
