package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class ExpectedProductionDto extends BaseDto<ExpectedProductionDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ExpectedProductionDto.class);

    private Long expectedProductionId;
    private BigDecimal expectedProductionPerProdUnit;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String cropUnitCode;
    private String cropUnitDesc;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public ExpectedProductionDto() {
    }

    public ExpectedProductionDto(ExpectedProductionDto dto) {
        this.expectedProductionId = dto.expectedProductionId;
        this.expectedProductionPerProdUnit = dto.expectedProductionPerProdUnit;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.cropUnitCode = dto.cropUnitCode;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public ExpectedProductionDto copy() {
        return new ExpectedProductionDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(ExpectedProductionDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(ExpectedProductionDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result
                    && dtoUtils.equals("expectedProductionId", this.expectedProductionId, other.expectedProductionId);
            result = result && dtoUtils.equals("expectedProductionPerProdUnit", this, other);
            result = result && dtoUtils.equals("inventoryItemCode", this.inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("cropUnitCode", this.cropUnitCode, other.cropUnitCode);
        }

        return result;
    }

    public Long getExpectedProductionId() {
        return expectedProductionId;
    }

    public void setExpectedProductionId(Long expectedProductionId) {
        this.expectedProductionId = expectedProductionId;
    }

    public BigDecimal getExpectedProductionPerProdUnit() {
        return expectedProductionPerProdUnit;
    }

    public void setExpectedProductionPerProdUnit(BigDecimal expectedProductionPerProdUnit) {
        this.expectedProductionPerProdUnit = expectedProductionPerProdUnit;
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
