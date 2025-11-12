package ca.bc.gov.farms.persistence.v1.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportBPUDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer programYear;
    private String municipalityCode;
    private String inventoryItemCode;
    private String unitComment;
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
    private String fileLocation;

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
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

    public String getUnitComment() {
        return unitComment;
    }

    public void setUnitComment(String unitComment) {
        this.unitComment = unitComment;
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

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
