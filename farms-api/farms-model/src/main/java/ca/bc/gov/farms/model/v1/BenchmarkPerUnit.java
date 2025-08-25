package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public interface BenchmarkPerUnit extends Serializable {

    public Long getBenchmarkPerUnitId();
    public void setBenchmarkPerUnitId(Long benchmarkPerUnitId);

    public Integer getProgramYear();
    public void setProgramYear(Integer programYear);

    public String getUnitComment();
    public void setUnitComment(String unitComment);

    public Date getExpiryDate();
    public void setExpiryDate(Date expiryDate);

    public String getMunicipalityCode();
    public void setMunicipalityCode(String municipalityCode);

    public String getMunicipalityDesc();
    public void setMunicipalityDesc(String municipalityDesc);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getStructureGroupCode();
    public void setStructureGroupCode(String structureGroupCode);

    public String getStructureGroupDesc();
    public void setStructureGroupDesc(String structureGroupDesc);

    public String getInventoryCode();
    public void setInventoryCode(String inventoryCode);

    public String getInventoryDesc();
    public void setInventoryDesc(String inventoryDesc);

    public BigDecimal getYearMinus6Margin();
    public void setYearMinus6Margin(BigDecimal yearMinus6Margin);

    public BigDecimal getYearMinus5Margin();
    public void setYearMinus5Margin(BigDecimal yearMinus5Margin);

    public BigDecimal getYearMinus4Margin();
    public void setYearMinus4Margin(BigDecimal yearMinus4Margin);

    public BigDecimal getYearMinus3Margin();
    public void setYearMinus3Margin(BigDecimal yearMinus3Margin);

    public BigDecimal getYearMinus2Margin();
    public void setYearMinus2Margin(BigDecimal yearMinus2Margin);

    public BigDecimal getYearMinus1Margin();
    public void setYearMinus1Margin(BigDecimal yearMinus1Margin);

    public BigDecimal getYearMinus6Expense();
    public void setYearMinus6Expense(BigDecimal yearMinus6Expense);

    public BigDecimal getYearMinus5Expense();
    public void setYearMinus5Expense(BigDecimal yearMinus5Expense);

    public BigDecimal getYearMinus4Expense();
    public void setYearMinus4Expense(BigDecimal yearMinus4Expense);

    public BigDecimal getYearMinus3Expense();
    public void setYearMinus3Expense(BigDecimal yearMinus3Expense);

    public BigDecimal getYearMinus2Expense();
    public void setYearMinus2Expense(BigDecimal yearMinus2Expense);

    public BigDecimal getYearMinus1Expense();
    public void setYearMinus1Expense(BigDecimal yearMinus1Expense);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
