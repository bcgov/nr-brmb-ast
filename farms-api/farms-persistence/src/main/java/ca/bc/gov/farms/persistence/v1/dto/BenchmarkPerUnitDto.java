package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
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
    private String municipalityDesc;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String structureGroupCode;
    private String structureGroupDesc;
    private String inventoryCode;
    private String inventoryDesc;
    private BigDecimal yearMinus6Margin;
    private BigDecimal yearMinus5Margin;
    private BigDecimal yearMinus4Margin;
    private BigDecimal yearMinus3Margin;
    private BigDecimal yearMinus2Margin;
    private BigDecimal yearMinus1Margin;
    private BigDecimal yearMinus6Expense;
    private BigDecimal yearMinus5Expense;
    private BigDecimal yearMinus4Expense;
    private BigDecimal yearMinus3Expense;
    private BigDecimal yearMinus2Expense;
    private BigDecimal yearMinus1Expense;
    private Long urlId;
    private String url;

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
        this.municipalityDesc = dto.municipalityDesc;
        this.inventoryItemCode = dto.inventoryItemCode;
        this.inventoryItemDesc = dto.inventoryItemDesc;
        this.structureGroupCode = dto.structureGroupCode;
        this.structureGroupDesc = dto.structureGroupDesc;
        this.inventoryCode = dto.inventoryCode;
        this.inventoryDesc = dto.inventoryDesc;
        this.yearMinus6Margin = dto.yearMinus6Margin;
        this.yearMinus5Margin = dto.yearMinus5Margin;
        this.yearMinus4Margin = dto.yearMinus4Margin;
        this.yearMinus3Margin = dto.yearMinus3Margin;
        this.yearMinus2Margin = dto.yearMinus2Margin;
        this.yearMinus1Margin = dto.yearMinus1Margin;
        this.yearMinus6Expense = dto.yearMinus6Expense;
        this.yearMinus5Expense = dto.yearMinus5Expense;
        this.yearMinus4Expense = dto.yearMinus4Expense;
        this.yearMinus3Expense = dto.yearMinus3Expense;
        this.yearMinus2Expense = dto.yearMinus2Expense;
        this.yearMinus1Expense = dto.yearMinus1Expense;
        this.urlId = dto.urlId;
        this.url = dto.url;

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
            result = result && dtoUtils.equals("municipalityDesc", municipalityDesc, other.municipalityDesc);
            result = result && dtoUtils.equals("inventoryItemCode", inventoryItemCode, other.inventoryItemCode);
            result = result && dtoUtils.equals("inventoryItemDesc", inventoryItemDesc, other.inventoryItemDesc);
            result = result && dtoUtils.equals("structureGroupCode", structureGroupCode, other.structureGroupCode);
            result = result && dtoUtils.equals("structureGroupDesc", structureGroupDesc, other.structureGroupDesc);
            result = result && dtoUtils.equals("yearMinus6Margin", this, other);
            result = result && dtoUtils.equals("yearMinus5Margin", this, other);
            result = result && dtoUtils.equals("yearMinus4Margin", this, other);
            result = result && dtoUtils.equals("yearMinus3Margin", this, other);
            result = result && dtoUtils.equals("yearMinus2Margin", this, other);
            result = result && dtoUtils.equals("yearMinus1Margin", this, other);
            result = result && dtoUtils.equals("yearMinus6Expense", this, other);
            result = result && dtoUtils.equals("yearMinus5Expense", this, other);
            result = result && dtoUtils.equals("yearMinus4Expense", this, other);
            result = result && dtoUtils.equals("yearMinus3Expense", this, other);
            result = result && dtoUtils.equals("yearMinus2Expense", this, other);
            result = result && dtoUtils.equals("yearMinus1Expense", this, other);
            result = result && dtoUtils.equals("urlId", urlId, other.urlId);
            result = result && dtoUtils.equals("url", url, other.url);
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

    public String getMunicipalityDesc() {
        return municipalityDesc;
    }

    public void setMunicipalityDesc(String municipalityDesc) {
        this.municipalityDesc = municipalityDesc;
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

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    public String getInventoryDesc() {
        return inventoryDesc;
    }

    public void setInventoryDesc(String inventoryDesc) {
        this.inventoryDesc = inventoryDesc;
    }

    public BigDecimal getYearMinus6Margin() {
        return yearMinus6Margin;
    }

    public void setYearMinus6Margin(BigDecimal yearMinus6Margin) {
        this.yearMinus6Margin = yearMinus6Margin;
    }

    public BigDecimal getYearMinus5Margin() {
        return yearMinus5Margin;
    }

    public void setYearMinus5Margin(BigDecimal yearMinus5Margin) {
        this.yearMinus5Margin = yearMinus5Margin;
    }

    public BigDecimal getYearMinus4Margin() {
        return yearMinus4Margin;
    }

    public void setYearMinus4Margin(BigDecimal yearMinus4Margin) {
        this.yearMinus4Margin = yearMinus4Margin;
    }

    public BigDecimal getYearMinus3Margin() {
        return yearMinus3Margin;
    }

    public void setYearMinus3Margin(BigDecimal yearMinus3Margin) {
        this.yearMinus3Margin = yearMinus3Margin;
    }

    public BigDecimal getYearMinus2Margin() {
        return yearMinus2Margin;
    }

    public void setYearMinus2Margin(BigDecimal yearMinus2Margin) {
        this.yearMinus2Margin = yearMinus2Margin;
    }

    public BigDecimal getYearMinus1Margin() {
        return yearMinus1Margin;
    }

    public void setYearMinus1Margin(BigDecimal yearMinus1Margin) {
        this.yearMinus1Margin = yearMinus1Margin;
    }

    public BigDecimal getYearMinus6Expense() {
        return yearMinus6Expense;
    }

    public void setYearMinus6Expense(BigDecimal yearMinus6Expense) {
        this.yearMinus6Expense = yearMinus6Expense;
    }

    public BigDecimal getYearMinus5Expense() {
        return yearMinus5Expense;
    }

    public void setYearMinus5Expense(BigDecimal yearMinus5Expense) {
        this.yearMinus5Expense = yearMinus5Expense;
    }

    public BigDecimal getYearMinus4Expense() {
        return yearMinus4Expense;
    }

    public void setYearMinus4Expense(BigDecimal yearMinus4Expense) {
        this.yearMinus4Expense = yearMinus4Expense;
    }

    public BigDecimal getYearMinus3Expense() {
        return yearMinus3Expense;
    }

    public void setYearMinus3Expense(BigDecimal yearMinus3Expense) {
        this.yearMinus3Expense = yearMinus3Expense;
    }

    public BigDecimal getYearMinus2Expense() {
        return yearMinus2Expense;
    }

    public void setYearMinus2Expense(BigDecimal yearMinus2Expense) {
        this.yearMinus2Expense = yearMinus2Expense;
    }

    public BigDecimal getYearMinus1Expense() {
        return yearMinus1Expense;
    }

    public void setYearMinus1Expense(BigDecimal yearMinus1Expense) {
        this.yearMinus1Expense = yearMinus1Expense;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
