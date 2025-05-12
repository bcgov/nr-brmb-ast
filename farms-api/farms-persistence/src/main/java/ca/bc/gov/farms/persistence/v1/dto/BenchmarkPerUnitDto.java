package ca.bc.gov.farms.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class BenchmarkPerUnitDto extends BaseDto<BenchmarkPerUnitDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkPerUnitDto.class);

    private Long benchmarkPerUnitId;
    private Integer programYear;
    private String unitComment;
    private Date expiryDate;
    private String municipalityCode;
    private String inventoryItemCode;
    private String structureGroupCode;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public BenchmarkPerUnitDto() {
    }

    public BenchmarkPerUnitDto(BenchmarkPerUnitDto dto) {
        this.benchmarkPerUnitId = dto.benchmarkPerUnitId;
        this.programYear = dto.programYear;
        this.unitComment = dto.unitComment;
        this.expiryDate = dto.expiryDate;
        this.municipalityCode = dto.municipalityCode;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.structureGroupCode = dto.structureGroupCode;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public BenchmarkPerUnitDto copy() {
        return new BenchmarkPerUnitDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(BenchmarkPerUnitDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(BenchmarkPerUnitDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("benchmarkPerUnitId", benchmarkPerUnitId, other.benchmarkPerUnitId);
            result = result && dtoUtils.equals("programYear", programYear, other.programYear);
            result = result && dtoUtils.equals("unitComment", unitComment, other.unitComment);
            result = result && dtoUtils.equals("expiryDate", expiryDate, other.expiryDate);
            result = result && dtoUtils.equals("municipalityCode", municipalityCode, other.municipalityCode);
            result = result && dtoUtils.equals("inventoryItemCode", inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("structureGroupCode", structureGroupCode, other.structureGroupCode);
        }

        return result;
    }

    public Long getBenchmarkPerUnitId() {
        return benchmarkPerUnitId;
    }

    public void setBenchmarkPerUnitId(Long benchmarkPerUnitId) {
        this.benchmarkPerUnitId = benchmarkPerUnitId;
    }

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    public String getUnitComment() {
        return unitComment;
    }

    public void setUnitComment(String unitComment) {
        this.unitComment = unitComment;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    public String getStructureGroupCode() {
        return structureGroupCode;
    }

    public void setStructureGroupCode(String structureGroupCode) {
        this.structureGroupCode = structureGroupCode;
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
