package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.BENCHMARK_PER_UNIT_NAME)
@XmlSeeAlso({ BenchmarkPerUnitRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class BenchmarkPerUnitRsrc extends BaseResource implements BenchmarkPerUnit {

    private static final long serialVersionUID = 1L;

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
    private String userEmail;

    @Override
    public Long getBenchmarkPerUnitId() {
        return benchmarkPerUnitId;
    }

    @Override
    public void setBenchmarkPerUnitId(Long benchmarkPerUnitId) {
        this.benchmarkPerUnitId = benchmarkPerUnitId;
    }

    @Override
    public Integer getProgramYear() {
        return programYear;
    }

    @Override
    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    @Override
    public String getUnitComment() {
        return unitComment;
    }

    @Override
    public void setUnitComment(String unitComment) {
        this.unitComment = unitComment;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getMunicipalityCode() {
        return municipalityCode;
    }

    @Override
    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    @Override
    public String getMunicipalityDesc() {
        return municipalityDesc;
    }

    @Override
    public void setMunicipalityDesc(String municipalityDesc) {
        this.municipalityDesc = municipalityDesc;
    }

    @Override
    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    @Override
    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    @Override
    public String getInventoryItemDesc() {
        return inventoryItemDesc;
    }

    @Override
    public void setInventoryItemDesc(String inventoryItemDesc) {
        this.inventoryItemDesc = inventoryItemDesc;
    }

    @Override
    public String getStructureGroupCode() {
        return structureGroupCode;
    }

    @Override
    public void setStructureGroupCode(String structureGroupCode) {
        this.structureGroupCode = structureGroupCode;
    }

    @Override
    public String getStructureGroupDesc() {
        return structureGroupDesc;
    }

    @Override
    public void setStructureGroupDesc(String structureGroupDesc) {
        this.structureGroupDesc = structureGroupDesc;
    }

    @Override
    public String getInventoryCode() {
        return inventoryCode;
    }

    @Override
    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    @Override
    public String getInventoryDesc() {
        return inventoryDesc;
    }

    @Override
    public void setInventoryDesc(String inventoryDesc) {
        this.inventoryDesc = inventoryDesc;
    }

    @Override
    public BigDecimal getYearMinus6Margin() {
        return yearMinus6Margin;
    }

    @Override
    public void setYearMinus6Margin(BigDecimal yearMinus6Margin) {
        this.yearMinus6Margin = yearMinus6Margin;
    }

    @Override
    public BigDecimal getYearMinus5Margin() {
        return yearMinus5Margin;
    }

    @Override
    public void setYearMinus5Margin(BigDecimal yearMinus5Margin) {
        this.yearMinus5Margin = yearMinus5Margin;
    }

    @Override
    public BigDecimal getYearMinus4Margin() {
        return yearMinus4Margin;
    }

    @Override
    public void setYearMinus4Margin(BigDecimal yearMinus4Margin) {
        this.yearMinus4Margin = yearMinus4Margin;
    }

    @Override
    public BigDecimal getYearMinus3Margin() {
        return yearMinus3Margin;
    }

    @Override
    public void setYearMinus3Margin(BigDecimal yearMinus3Margin) {
        this.yearMinus3Margin = yearMinus3Margin;
    }

    @Override
    public BigDecimal getYearMinus2Margin() {
        return yearMinus2Margin;
    }

    @Override
    public void setYearMinus2Margin(BigDecimal yearMinus2Margin) {
        this.yearMinus2Margin = yearMinus2Margin;
    }

    @Override
    public BigDecimal getYearMinus1Margin() {
        return yearMinus1Margin;
    }

    @Override
    public void setYearMinus1Margin(BigDecimal yearMinus1Margin) {
        this.yearMinus1Margin = yearMinus1Margin;
    }

    @Override
    public BigDecimal getYearMinus6Expense() {
        return yearMinus6Expense;
    }

    @Override
    public void setYearMinus6Expense(BigDecimal yearMinus6Expense) {
        this.yearMinus6Expense = yearMinus6Expense;
    }

    @Override
    public BigDecimal getYearMinus5Expense() {
        return yearMinus5Expense;
    }

    @Override
    public void setYearMinus5Expense(BigDecimal yearMinus5Expense) {
        this.yearMinus5Expense = yearMinus5Expense;
    }

    @Override
    public BigDecimal getYearMinus4Expense() {
        return yearMinus4Expense;
    }

    @Override
    public void setYearMinus4Expense(BigDecimal yearMinus4Expense) {
        this.yearMinus4Expense = yearMinus4Expense;
    }

    @Override
    public BigDecimal getYearMinus3Expense() {
        return yearMinus3Expense;
    }

    @Override
    public void setYearMinus3Expense(BigDecimal yearMinus3Expense) {
        this.yearMinus3Expense = yearMinus3Expense;
    }

    @Override
    public BigDecimal getYearMinus2Expense() {
        return yearMinus2Expense;
    }

    @Override
    public void setYearMinus2Expense(BigDecimal yearMinus2Expense) {
        this.yearMinus2Expense = yearMinus2Expense;
    }

    @Override
    public BigDecimal getYearMinus1Expense() {
        return yearMinus1Expense;
    }

    @Override
    public void setYearMinus1Expense(BigDecimal yearMinus1Expense) {
        this.yearMinus1Expense = yearMinus1Expense;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
