package ca.bc.gov.farms.persistence.v1.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class CropUnitConversionDto extends BaseDto<CropUnitConversionDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CropUnitConversionDto.class);

    private Long cropUnitDefaultId;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    private List<ConversionUnitDto> conversionUnits = new ArrayList<>();

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public CropUnitConversionDto() {
    }

    public CropUnitConversionDto(CropUnitConversionDto dto) {
        this.cropUnitDefaultId = dto.cropUnitDefaultId;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.cropUnitCode = dto.cropUnitCode;
        this.cropUnitDesc = dto.cropUnitDesc;

        // deep copy conversionUnits
        for (ConversionUnitDto conversionUnit : dto.conversionUnits) {
            this.conversionUnits.add(new ConversionUnitDto(conversionUnit));
        }

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
            result = result && dtoUtils.equals("cropUnitDefaultId", this.cropUnitDefaultId, other.cropUnitDefaultId);
            result = result && dtoUtils.equals("inventoryItemCode", this.inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", this.inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("cropUnitCode", this.cropUnitCode, other.cropUnitCode);
            result = result && dtoUtils.equals("cropUnitDesc", this.cropUnitDesc, other.cropUnitDesc);

            if (this.conversionUnits.size() == other.conversionUnits.size()) {
                for (int i = 0; i < this.conversionUnits.size(); i++) {
                    ConversionUnitDto thisConversionUnit = this.conversionUnits.get(i);
                    ConversionUnitDto otherConversionUnit = other.conversionUnits.get(i);
                    result = result && thisConversionUnit.equalsAll(otherConversionUnit);
                }
            } else {
                result = result && (this.conversionUnits.size() == other.conversionUnits.size());
            }
        }

        return result;
    }

    public Long getCropUnitDefaultId() {
        return cropUnitDefaultId;
    }

    public void setCropUnitDefaultId(Long cropUnitDefaultId) {
        this.cropUnitDefaultId = cropUnitDefaultId;
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

    public List<ConversionUnitDto> getConversionUnits() {
        return conversionUnits;
    }

    public void setConversionUnits(List<ConversionUnitDto> conversionUnits) {
        this.conversionUnits = conversionUnits;
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
