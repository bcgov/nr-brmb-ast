package ca.bc.gov.farms.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class InventoryItemAttributeDto extends BaseDto<InventoryItemAttributeDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemAttributeDto.class);

    private Long inventoryItemAttributeId;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String rollupInventoryItemCode;
    private String rollupInventoryItemDesc;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public InventoryItemAttributeDto() {
    }

    public InventoryItemAttributeDto(InventoryItemAttributeDto dto) {
        this.inventoryItemAttributeId = dto.inventoryItemAttributeId;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.rollupInventoryItemCode = dto.rollupInventoryItemCode;
        this.rollupInventoryItemDesc = dto.rollupInventoryItemDesc;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public InventoryItemAttributeDto copy() {
        return new InventoryItemAttributeDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(InventoryItemAttributeDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(InventoryItemAttributeDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("inventoryItemAttributeId", this.inventoryItemAttributeId,
                    other.inventoryItemAttributeId);
            result = result && dtoUtils.equals("inventoryItemCode", this.inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", this.inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("rollupInventoryItemCode", this.rollupInventoryItemCode,
                    other.rollupInventoryItemCode);
            result = result && dtoUtils.equals("rollupInventoryItemDesc", this.rollupInventoryItemDesc,
                    other.rollupInventoryItemDesc);
        }

        return result;
    }

    public Long getInventoryItemAttributeId() {
        return inventoryItemAttributeId;
    }

    public void setInventoryItemAttributeId(Long inventoryItemAttributeId) {
        this.inventoryItemAttributeId = inventoryItemAttributeId;
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

    public String getRollupInventoryItemCode() {
        return rollupInventoryItemCode;
    }

    public void setRollupInventoryItemCode(String rollupInventoryItemCode) {
        this.rollupInventoryItemCode = rollupInventoryItemCode;
    }

    public String getRollupInventoryItemDesc() {
        return rollupInventoryItemDesc;
    }

    public void setRollupInventoryItemDesc(String rollupInventoryItemDesc) {
        this.rollupInventoryItemDesc = rollupInventoryItemDesc;
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
