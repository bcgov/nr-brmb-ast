package ca.bc.gov.farms.persistence.v1.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportFMVDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer programYear;
    private Integer period;
    private BigDecimal averagePrice;
    private BigDecimal percentVariance;
    private String municipalityCode;
    private String cropUnitCode;
    private String inventoryItemCode;

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getPercentVariance() {
        return percentVariance;
    }

    public void setPercentVariance(BigDecimal percentVariance) {
        this.percentVariance = percentVariance;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getCropUnitCode() {
        return cropUnitCode;
    }

    public void setCropUnitCode(String cropUnitCode) {
        this.cropUnitCode = cropUnitCode;
    }

    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }
}
