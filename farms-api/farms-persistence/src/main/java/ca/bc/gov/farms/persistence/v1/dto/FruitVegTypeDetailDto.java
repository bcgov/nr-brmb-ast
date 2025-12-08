package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class FruitVegTypeDetailDto extends BaseDto<FruitVegTypeDetailDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(FruitVegTypeDetailDto.class);

    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private LocalDate establishedDate;
    private LocalDate expiryDate;
    private BigDecimal revenueVarianceLimit;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public FruitVegTypeDetailDto() {
    }

    public FruitVegTypeDetailDto(FruitVegTypeDetailDto dto) {
        this.fruitVegTypeCode = dto.fruitVegTypeCode;
        this.fruitVegTypeDesc = dto.fruitVegTypeDesc;
        this.establishedDate = dto.establishedDate;
        this.expiryDate = dto.expiryDate;
        this.revenueVarianceLimit = dto.revenueVarianceLimit;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public FruitVegTypeDetailDto copy() {
        return new FruitVegTypeDetailDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(FruitVegTypeDetailDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(FruitVegTypeDetailDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("fruitVegTypeCode", this.fruitVegTypeCode, other.fruitVegTypeCode);
            result = result && dtoUtils.equals("fruitVegTypeDesc", this.fruitVegTypeDesc, other.fruitVegTypeDesc);
            result = result && dtoUtils.equals("establishedDate", this.establishedDate, other.establishedDate);
            result = result && dtoUtils.equals("expiryDate", this.expiryDate, other.expiryDate);
            result = result
                    && dtoUtils.equals("revenueVarianceLimit", this, other);
        }

        return result;
    }

    public String getFruitVegTypeCode() {
        return fruitVegTypeCode;
    }

    public void setFruitVegTypeCode(String fruitVegTypeCode) {
        this.fruitVegTypeCode = fruitVegTypeCode;
    }

    public String getFruitVegTypeDesc() {
        return fruitVegTypeDesc;
    }

    public void setFruitVegTypeDesc(String fruitVegTypeDesc) {
        this.fruitVegTypeDesc = fruitVegTypeDesc;
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getRevenueVarianceLimit() {
        return revenueVarianceLimit;
    }

    public void setRevenueVarianceLimit(BigDecimal revenueVarianceLimit) {
        this.revenueVarianceLimit = revenueVarianceLimit;
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
