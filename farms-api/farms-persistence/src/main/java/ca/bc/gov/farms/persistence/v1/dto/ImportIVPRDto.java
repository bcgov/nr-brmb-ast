package ca.bc.gov.farms.persistence.v1.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportIVPRDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer programYear;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private BigDecimal insurableValue;
    private BigDecimal premiumRate;
    private String fileLocation;

    public Integer getProgramYear() {
        return programYear;
    }

    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
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

    public BigDecimal getInsurableValue() {
        return insurableValue;
    }

    public void setInsurableValue(BigDecimal insurableValue) {
        this.insurableValue = insurableValue;
    }

    public BigDecimal getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(BigDecimal premiumRate) {
        this.premiumRate = premiumRate;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
