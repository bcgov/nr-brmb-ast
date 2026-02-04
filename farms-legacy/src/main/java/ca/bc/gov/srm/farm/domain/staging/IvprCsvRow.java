/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.staging;

public class IvprCsvRow {
    /** document me * */
    private Integer programYear;

    /** document me * */
    private String inventoryItemCode;

    /** document me * */
    private String inventoryItemCodeDescription;

    /** document me * */
    private Double insurableValue;

    /** document me * */
    private Double premiumRate;

    /**
     * @return the programYear
     */
    public Integer getProgramYear() {
        return programYear;
    }

    /**
     * @param programYear the programYear to set
     */
    public void setProgramYear(final Integer programYear) {
        this.programYear = programYear;
    }

    /**
     * @return the inventoryItemCode
     */
    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    /**
     * @param inventoryItemCode the inventoryItemCode to set
     */
    public void setInventoryItemCode(final String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    /**
     * @return the inventoryItemCodeDescription
     */
    public String getInventoryItemCodeDescription() {
        return inventoryItemCodeDescription;
    }

    /**
     * @param inventoryItemCodeDescription the inventoryItemCodeDescription to set
     */
    public void setInventoryItemCodeDescription(final String inventoryItemCodeDescription) {
        this.inventoryItemCodeDescription = inventoryItemCodeDescription;
    }

    /**
     * @return the insurableValue
     */
    public Double getInsurableValue() {
        return insurableValue;
    }

    /**
     * @param insurableValue the insurableValue to set
     */
    public void setInsurableValue(final Double insurableValue) {
        this.insurableValue = insurableValue;
    }

    /**
     * @return the premiumRate
     */
    public Double getPremiumRate() {
        return premiumRate;
    }

    /**
     * @param premiumRate the premiumRate to set
     */
    public void setPremiumRate(final Double premiumRate) {
        this.premiumRate = premiumRate;
    }
}
