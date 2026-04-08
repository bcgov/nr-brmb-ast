package ca.bc.gov.farms.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class InventoryTypeXrefDto extends BaseDto<InventoryTypeXrefDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(InventoryTypeXrefDto.class);

    private Long agristabilityCommodityXrefId;
    private String marketCommodityInd;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String inventoryGroupCode;
    private String inventoryGroupDesc;
    private String inventoryClassCode;
    private String inventoryClassDesc;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public InventoryTypeXrefDto() {
    }

    public InventoryTypeXrefDto(InventoryTypeXrefDto dto) {
        this.agristabilityCommodityXrefId = dto.agristabilityCommodityXrefId;
        this.marketCommodityInd = dto.marketCommodityInd;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.inventoryGroupCode = dto.inventoryGroupCode;
        this.inventoryGroupDesc = dto.inventoryGroupDesc;
        this.inventoryClassCode = dto.inventoryClassCode;
        this.inventoryClassDesc = dto.inventoryClassDesc;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public InventoryTypeXrefDto copy() {
        return new InventoryTypeXrefDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(InventoryTypeXrefDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(InventoryTypeXrefDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("agristabilityCommodityXrefId", agristabilityCommodityXrefId,
                    other.agristabilityCommodityXrefId);
            result = result && dtoUtils.equals("marketCommodityInd", marketCommodityInd, other.marketCommodityInd);
            result = result && dtoUtils.equals("inventoryItemCode", inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("inventoryGroupCode", inventoryGroupCode, other.inventoryGroupCode);
            result = result && dtoUtils.equals("inventoryGroupDesc", inventoryGroupDesc, other.inventoryGroupDesc);
            result = result && dtoUtils.equals("inventoryClassCode", inventoryClassCode, other.inventoryClassCode);
            result = result && dtoUtils.equals("inventoryClassDesc", inventoryClassDesc, other.inventoryClassDesc);
        }

        return result;
    }

    public Long getAgristabilityCommodityXrefId() {
        return agristabilityCommodityXrefId;
    }

    public void setAgristabilityCommodityXrefId(Long agristabilityCommodityXrefId) {
        this.agristabilityCommodityXrefId = agristabilityCommodityXrefId;
    }

    public String getMarketCommodityInd() {
        return marketCommodityInd;
    }

    public void setMarketCommodityInd(String marketCommodityInd) {
        this.marketCommodityInd = marketCommodityInd;
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

    public String getInventoryGroupCode() {
        return inventoryGroupCode;
    }

    public void setInventoryGroupCode(String inventoryGroupCode) {
        this.inventoryGroupCode = inventoryGroupCode;
    }

    public String getInventoryGroupDesc() {
        return inventoryGroupDesc;
    }

    public void setInventoryGroupDesc(String inventoryGroupDesc) {
        this.inventoryGroupDesc = inventoryGroupDesc;
    }

    public String getInventoryClassCode() {
        return inventoryClassCode;
    }

    public void setInventoryClassCode(String inventoryClassCode) {
        this.inventoryClassCode = inventoryClassCode;
    }

    public String getInventoryClassDesc() {
        return inventoryClassDesc;
    }

    public void setInventoryClassDesc(String inventoryClassDesc) {
        this.inventoryClassDesc = inventoryClassDesc;
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
}
