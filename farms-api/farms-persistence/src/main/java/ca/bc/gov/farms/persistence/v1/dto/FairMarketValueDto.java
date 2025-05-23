package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class FairMarketValueDto extends BaseDto<FairMarketValueDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(FairMarketValueDto.class);

    private String fairMarketValueId;
    private Integer programYear;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String municipalityCode;
    private String municipalityDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    private String defaultCropUnitCode;
    private String defaultCropUnitDesc;
    private BigDecimal period01Price;
    private BigDecimal period02Price;
    private BigDecimal period03Price;
    private BigDecimal period04Price;
    private BigDecimal period05Price;
    private BigDecimal period06Price;
    private BigDecimal period07Price;
    private BigDecimal period08Price;
    private BigDecimal period09Price;
    private BigDecimal period10Price;
    private BigDecimal period11Price;
    private BigDecimal period12Price;
    private BigDecimal period01Variance;
    private BigDecimal period02Variance;
    private BigDecimal period03Variance;
    private BigDecimal period04Variance;
    private BigDecimal period05Variance;
    private BigDecimal period06Variance;
    private BigDecimal period07Variance;
    private BigDecimal period08Variance;
    private BigDecimal period09Variance;
    private BigDecimal period10Variance;
    private BigDecimal period11Variance;
    private BigDecimal period12Variance;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public FairMarketValueDto() {
    }

    public FairMarketValueDto(FairMarketValueDto dto) {
        this.fairMarketValueId = dto.fairMarketValueId;
        this.programYear = dto.programYear;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.municipalityCode = dto.municipalityCode;
        this.municipalityDesc = dto.municipalityDesc;
        this.cropUnitCode = dto.cropUnitCode;
        this.cropUnitDesc = dto.cropUnitDesc;
        this.defaultCropUnitCode = dto.defaultCropUnitCode;
        this.defaultCropUnitDesc = dto.defaultCropUnitDesc;
        this.period01Price = dto.period01Price;
        this.period02Price = dto.period02Price;
        this.period03Price = dto.period03Price;
        this.period04Price = dto.period04Price;
        this.period05Price = dto.period05Price;
        this.period06Price = dto.period06Price;
        this.period07Price = dto.period07Price;
        this.period08Price = dto.period08Price;
        this.period09Price = dto.period09Price;
        this.period10Price = dto.period10Price;
        this.period11Price = dto.period11Price;
        this.period12Price = dto.period12Price;
        this.period01Variance = dto.period01Variance;
        this.period02Variance = dto.period02Variance;
        this.period03Variance = dto.period03Variance;
        this.period04Variance = dto.period04Variance;
        this.period05Variance = dto.period05Variance;
        this.period06Variance = dto.period06Variance;
        this.period07Variance = dto.period07Variance;
        this.period08Variance = dto.period08Variance;
        this.period09Variance = dto.period09Variance;
        this.period10Variance = dto.period10Variance;
        this.period11Variance = dto.period11Variance;
        this.period12Variance = dto.period12Variance;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public FairMarketValueDto copy() {
        return new FairMarketValueDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(FairMarketValueDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(FairMarketValueDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("fairMarketValueId", this.fairMarketValueId, other.fairMarketValueId);
            result = result && dtoUtils.equals("programYear", this.programYear, other.programYear);
            result = result && dtoUtils.equals("inventoryItemCode", this.inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", this.inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("municipalityCode", this.municipalityCode, other.municipalityCode);
            result = result && dtoUtils.equals("municipalityDesc", this.municipalityDesc, other.municipalityDesc);
            result = result && dtoUtils.equals("cropUnitCode", this.cropUnitCode, other.cropUnitCode);
            result = result && dtoUtils.equals("cropUnitDesc", this.cropUnitDesc, other.cropUnitDesc);
            result = result
                    && dtoUtils.equals("defaultCropUnitCode", this.defaultCropUnitCode, other.defaultCropUnitCode);
            result = result
                    && dtoUtils.equals("defaultCropUnitDesc", this.defaultCropUnitDesc, other.defaultCropUnitDesc);
            result = result && dtoUtils.equals("period01Price", this.period01Price, other.period01Price);
            result = result && dtoUtils.equals("period02Price", this.period02Price, other.period02Price);
            result = result && dtoUtils.equals("period03Price", this.period03Price, other.period03Price);
            result = result && dtoUtils.equals("period04Price", this.period04Price, other.period04Price);
            result = result && dtoUtils.equals("period05Price", this.period05Price, other.period05Price);
            result = result && dtoUtils.equals("period06Price", this.period06Price, other.period06Price);
            result = result && dtoUtils.equals("period07Price", this.period07Price, other.period07Price);
            result = result && dtoUtils.equals("period08Price", this.period08Price, other.period08Price);
            result = result && dtoUtils.equals("period09Price", this.period09Price, other.period09Price);
            result = result && dtoUtils.equals("period10Price", this.period10Price, other.period10Price);
            result = result && dtoUtils.equals("period11Price", this.period11Price, other.period11Price);
            result = result && dtoUtils.equals("period12Price", this.period12Price, other.period12Price);
            result = result && dtoUtils.equals("period01Variance", this.period01Variance, other.period01Variance);
            result = result && dtoUtils.equals("period02Variance", this.period02Variance, other.period02Variance);
            result = result && dtoUtils.equals("period03Variance", this.period03Variance, other.period03Variance);
            result = result && dtoUtils.equals("period04Variance", this.period04Variance, other.period04Variance);
            result = result && dtoUtils.equals("period05Variance", this.period05Variance, other.period05Variance);
            result = result && dtoUtils.equals("period06Variance", this.period06Variance, other.period06Variance);
            result = result && dtoUtils.equals("period07Variance", this.period07Variance, other.period07Variance);
            result = result && dtoUtils.equals("period08Variance", this.period08Variance, other.period08Variance);
            result = result && dtoUtils.equals("period09Variance", this.period09Variance, other.period09Variance);
            result = result && dtoUtils.equals("period10Variance", this.period10Variance, other.period10Variance);
            result = result && dtoUtils.equals("period11Variance", this.period11Variance, other.period11Variance);
            result = result && dtoUtils.equals("period12Variance", this.period12Variance, other.period12Variance);
        }

        return result;
    }

    public String getFairMarketValueId() {
        return fairMarketValueId;
    }

    public void setFairMarketValueId(String fairMarketValueId) {
        this.fairMarketValueId = fairMarketValueId;
    }

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
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

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getMunicipalityDesc() {
        return municipalityDesc;
    }

    public void setMunicipalityDesc(String municipalityDesc) {
        this.municipalityDesc = municipalityDesc;
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

    public String getDefaultCropUnitCode() {
        return defaultCropUnitCode;
    }

    public void setDefaultCropUnitCode(String defaultCropUnitCode) {
        this.defaultCropUnitCode = defaultCropUnitCode;
    }

    public String getDefaultCropUnitDesc() {
        return defaultCropUnitDesc;
    }

    public void setDefaultCropUnitDesc(String defaultCropUnitDesc) {
        this.defaultCropUnitDesc = defaultCropUnitDesc;
    }

    public BigDecimal getPeriod01Price() {
        return period01Price;
    }

    public void setPeriod01Price(BigDecimal period01Price) {
        this.period01Price = period01Price;
    }

    public BigDecimal getPeriod02Price() {
        return period02Price;
    }

    public void setPeriod02Price(BigDecimal period02Price) {
        this.period02Price = period02Price;
    }

    public BigDecimal getPeriod03Price() {
        return period03Price;
    }

    public void setPeriod03Price(BigDecimal period03Price) {
        this.period03Price = period03Price;
    }

    public BigDecimal getPeriod04Price() {
        return period04Price;
    }

    public void setPeriod04Price(BigDecimal period04Price) {
        this.period04Price = period04Price;
    }

    public BigDecimal getPeriod05Price() {
        return period05Price;
    }

    public void setPeriod05Price(BigDecimal period05Price) {
        this.period05Price = period05Price;
    }

    public BigDecimal getPeriod06Price() {
        return period06Price;
    }

    public void setPeriod06Price(BigDecimal period06Price) {
        this.period06Price = period06Price;
    }

    public BigDecimal getPeriod07Price() {
        return period07Price;
    }

    public void setPeriod07Price(BigDecimal period07Price) {
        this.period07Price = period07Price;
    }

    public BigDecimal getPeriod08Price() {
        return period08Price;
    }

    public void setPeriod08Price(BigDecimal period08Price) {
        this.period08Price = period08Price;
    }

    public BigDecimal getPeriod09Price() {
        return period09Price;
    }

    public void setPeriod09Price(BigDecimal period09Price) {
        this.period09Price = period09Price;
    }

    public BigDecimal getPeriod10Price() {
        return period10Price;
    }

    public void setPeriod10Price(BigDecimal period10Price) {
        this.period10Price = period10Price;
    }

    public BigDecimal getPeriod11Price() {
        return period11Price;
    }

    public void setPeriod11Price(BigDecimal period11Price) {
        this.period11Price = period11Price;
    }

    public BigDecimal getPeriod12Price() {
        return period12Price;
    }

    public void setPeriod12Price(BigDecimal period12Price) {
        this.period12Price = period12Price;
    }

    public BigDecimal getPeriod01Variance() {
        return period01Variance;
    }

    public void setPeriod01Variance(BigDecimal period01Variance) {
        this.period01Variance = period01Variance;
    }

    public BigDecimal getPeriod02Variance() {
        return period02Variance;
    }

    public void setPeriod02Variance(BigDecimal period02Variance) {
        this.period02Variance = period02Variance;
    }

    public BigDecimal getPeriod03Variance() {
        return period03Variance;
    }

    public void setPeriod03Variance(BigDecimal period03Variance) {
        this.period03Variance = period03Variance;
    }

    public BigDecimal getPeriod04Variance() {
        return period04Variance;
    }

    public void setPeriod04Variance(BigDecimal period04Variance) {
        this.period04Variance = period04Variance;
    }

    public BigDecimal getPeriod05Variance() {
        return period05Variance;
    }

    public void setPeriod05Variance(BigDecimal period05Variance) {
        this.period05Variance = period05Variance;
    }

    public BigDecimal getPeriod06Variance() {
        return period06Variance;
    }

    public void setPeriod06Variance(BigDecimal period06Variance) {
        this.period06Variance = period06Variance;
    }

    public BigDecimal getPeriod07Variance() {
        return period07Variance;
    }

    public void setPeriod07Variance(BigDecimal period07Variance) {
        this.period07Variance = period07Variance;
    }

    public BigDecimal getPeriod08Variance() {
        return period08Variance;
    }

    public void setPeriod08Variance(BigDecimal period08Variance) {
        this.period08Variance = period08Variance;
    }

    public BigDecimal getPeriod09Variance() {
        return period09Variance;
    }

    public void setPeriod09Variance(BigDecimal period09Variance) {
        this.period09Variance = period09Variance;
    }

    public BigDecimal getPeriod10Variance() {
        return period10Variance;
    }

    public void setPeriod10Variance(BigDecimal period10Variance) {
        this.period10Variance = period10Variance;
    }

    public BigDecimal getPeriod11Variance() {
        return period11Variance;
    }

    public void setPeriod11Variance(BigDecimal period11Variance) {
        this.period11Variance = period11Variance;
    }

    public BigDecimal getPeriod12Variance() {
        return period12Variance;
    }

    public void setPeriod12Variance(BigDecimal period12Variance) {
        this.period12Variance = period12Variance;
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
