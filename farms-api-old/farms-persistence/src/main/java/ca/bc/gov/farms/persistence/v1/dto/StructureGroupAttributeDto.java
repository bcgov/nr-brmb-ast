package ca.bc.gov.farms.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class StructureGroupAttributeDto extends BaseDto<StructureGroupAttributeDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(StructureGroupAttributeDto.class);

    private Long structureGroupAttributeId;
    private String structureGroupCode;
    private String structureGroupDesc;
    private String rollupStructureGroupCode;
    private String rollupStructureGroupDesc;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public StructureGroupAttributeDto() {
    }

    public StructureGroupAttributeDto(StructureGroupAttributeDto dto) {
        this.structureGroupAttributeId = dto.structureGroupAttributeId;
        this.structureGroupCode = dto.structureGroupCode;
        this.structureGroupDesc = dto.structureGroupDesc;
        this.rollupStructureGroupCode = dto.rollupStructureGroupCode;
        this.rollupStructureGroupDesc = dto.rollupStructureGroupDesc;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public StructureGroupAttributeDto copy() {
        return new StructureGroupAttributeDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(StructureGroupAttributeDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(StructureGroupAttributeDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("structureGroupAttributeId", this.structureGroupAttributeId,
                    other.structureGroupAttributeId);
            result = result && dtoUtils.equals("structureGroupCode", this.structureGroupCode, other.structureGroupCode);
            result = result && dtoUtils.equals("structureGroupDesc", this.structureGroupDesc, other.structureGroupDesc);
            result = result && dtoUtils.equals("rollupStructureGroupCode", this.rollupStructureGroupCode,
                    other.rollupStructureGroupCode);
            result = result && dtoUtils.equals("rollupStructureGroupDesc", this.rollupStructureGroupDesc,
                    other.rollupStructureGroupDesc);
        }

        return result;
    }

    public Long getStructureGroupAttributeId() {
        return structureGroupAttributeId;
    }

    public void setStructureGroupAttributeId(Long structureGroupAttributeId) {
        this.structureGroupAttributeId = structureGroupAttributeId;
    }

    public String getStructureGroupCode() {
        return structureGroupCode;
    }

    public void setStructureGroupCode(String structureGroupCode) {
        this.structureGroupCode = structureGroupCode;
    }

    public String getStructureGroupDesc() {
        return structureGroupDesc;
    }

    public void setStructureGroupDesc(String structureGroupDesc) {
        this.structureGroupDesc = structureGroupDesc;
    }

    public String getRollupStructureGroupCode() {
        return rollupStructureGroupCode;
    }

    public void setRollupStructureGroupCode(String rollupStructureGroupCode) {
        this.rollupStructureGroupCode = rollupStructureGroupCode;
    }

    public String getRollupStructureGroupDesc() {
        return rollupStructureGroupDesc;
    }

    public void setRollupStructureGroupDesc(String rollupStructureGroupDesc) {
        this.rollupStructureGroupDesc = rollupStructureGroupDesc;
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
