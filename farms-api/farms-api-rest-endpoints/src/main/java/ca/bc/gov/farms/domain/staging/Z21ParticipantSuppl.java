package ca.bc.gov.farms.domain.staging;

/**
 * Z21ParticipantSuppl identifies Inventory information from parts 7, 8, 10, 11,
 * 12 of the AgriStability Supplementary Form. Totals are not included, but can
 * be calculated. The Inventory Type code describes which part of the form each
 * row came from. This file will have multiple rows per participant and farming
 * operation, for the current (latest) program year. This file is created by
 * FIPD. This is a staging object used to load temporary data set before being
 * merged into the operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:12 PM
 */
public final class Z21ParticipantSuppl {

    /**
     * participantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     */
    private java.lang.Integer participantPin;

    /** programYear is the stabilization year for this record. */
    private java.lang.Integer programYear;

    /**
     * operationNumber identifies each operation a participant reports for a given
     * stab year. Operations may have different statement numbers in different
     * program years.
     */
    private java.lang.Integer operationNumber;

    /**
     * inventoryKey is the primary key for the file. Provides each row with a
     * unique identifier over the whole file.
     */
    private java.lang.Integer inventoryKey;

    /**
     * cropUnitType is a numeric value assigned to a unit type for the crop
     * inventory, section 8 column c. Foreign key to file 28.
     */
    private java.lang.Integer cropUnitType;

    /**
     * cropOnFarmAcres is the number of acres grown of the crop - section 8 column
     * d.
     */
    private java.lang.Double cropOnFarmAcres;

    /**
     * cropQtyProduced is the quantity of a crop produced - section 8 column e.
     */
    private java.lang.Double cropQtyProduced;

    /**
     * quantityEnd is the ending inventory for livestock (section 7 column c) or
     * crop (section 8 column f).
     */
    private java.lang.Double quantityEnd;

    /**
     * endOfYearPrice is the endOfYearPrice for crop or livestock inventory
     * entries.
     */
    private java.lang.Double endOfYearPrice;

    /**
     * endOfYearAmount is the the end of year dollar amount for Purchased Inputs,
     * Defferred income/receivables or Accounts Payable.
     */
    private java.lang.Double endOfYearAmount;

    /**
     * inventoryCode is a numeric code used to uniquely identify an inventory
     * item.
     */
    private java.lang.Integer inventoryCode;

    /**
     * inventoryTypeCode is a numeric code indicating an inventory type. Valid
     * values are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased
     * Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.
     */
    private java.lang.Integer inventoryTypeCode;

    /**
     * cropUnseedableAcres is the number of unseedable acres of the crop - section 8
     * column e.
     */
    private java.lang.Double cropUnseedableAcres;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */
    public Z21ParticipantSuppl() {

    }

    /**
     * ParticipantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getParticipantPin() {
        return participantPin;
    }

    /**
     * ParticipantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantPin(final java.lang.Integer newVal) {
        participantPin = newVal;
    }

    /**
     * ProgramYear is the stabilization year for this record.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProgramYear() {
        return programYear;
    }

    /**
     * ProgramYear is the stabilization year for this record.
     *
     * @param newVal The new value for this property
     */
    public void setProgramYear(final java.lang.Integer newVal) {
        programYear = newVal;
    }

    /**
     * OperationNumber identifies each operation a participant reports for a given
     * stab year. Operations may have different statement numbers in different
     * program years.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getOperationNumber() {
        return operationNumber;
    }

    /**
     * OperationNumber identifies each operation a participant reports for a given
     * stab year. Operations may have different statement numbers in different
     * program years.
     *
     * @param newVal The new value for this property
     */
    public void setOperationNumber(final java.lang.Integer newVal) {
        operationNumber = newVal;
    }

    /**
     * InventoryKey is the primary key for the file. Provides each row with a
     * unique identifier over the whole file.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getInventoryKey() {
        return inventoryKey;
    }

    /**
     * InventoryKey is the primary key for the file. Provides each row with a
     * unique identifier over the whole file.
     *
     * @param newVal The new value for this property
     */
    public void setInventoryKey(final java.lang.Integer newVal) {
        inventoryKey = newVal;
    }

    /**
     * CropUnitType is a numeric value assigned to a unit type for the crop
     * inventory, section 8 column c. Foreign key to file 28.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getCropUnitType() {
        return cropUnitType;
    }

    /**
     * CropUnitType is a numeric value assigned to a unit type for the crop
     * inventory, section 8 column c. Foreign key to file 28.
     *
     * @param newVal The new value for this property
     */
    public void setCropUnitType(final java.lang.Integer newVal) {
        cropUnitType = newVal;
    }

    /**
     * CropOnFarmAcres is the number of acres grown of the crop - section 8 column
     * d.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getCropOnFarmAcres() {
        return cropOnFarmAcres;
    }

    /**
     * CropOnFarmAcres is the number of acres grown of the crop - section 8 column
     * d.
     *
     * @param newVal The new value for this property
     */
    public void setCropOnFarmAcres(final java.lang.Double newVal) {
        cropOnFarmAcres = newVal;
    }

    /**
     * CropQtyProduced is the quantity of a crop produced - section 8 column e.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getCropQtyProduced() {
        return cropQtyProduced;
    }

    /**
     * CropQtyProduced is the quantity of a crop produced - section 8 column e.
     *
     * @param newVal The new value for this property
     */
    public void setCropQtyProduced(final java.lang.Double newVal) {
        cropQtyProduced = newVal;
    }

    /**
     * QuantityEnd is the ending inventory for livestock (section 7 column c) or
     * crop (section 8 column f).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getQuantityEnd() {
        return quantityEnd;
    }

    /**
     * QuantityEnd is the ending inventory for livestock (section 7 column c) or
     * crop (section 8 column f).
     *
     * @param newVal The new value for this property
     */
    public void setQuantityEnd(final java.lang.Double newVal) {
        quantityEnd = newVal;
    }

    /**
     * EndOfYearPrice is the endOfYearPrice for crop or livestock inventory
     * entries.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getEndOfYearPrice() {
        return endOfYearPrice;
    }

    /**
     * EndOfYearPrice is the endOfYearPrice for crop or livestock inventory
     * entries.
     *
     * @param newVal The new value for this property
     */
    public void setEndOfYearPrice(final java.lang.Double newVal) {
        endOfYearPrice = newVal;
    }

    /**
     * EndOfYearAmount is the the end of year dollar amount for Purchased Inputs,
     * Defferred income/receivables or Accounts Payable.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getEndOfYearAmount() {
        return endOfYearAmount;
    }

    /**
     * EndOfYearAmount is the the end of year dollar amount for Purchased Inputs,
     * Defferred income/receivables or Accounts Payable.
     *
     * @param newVal The new value for this property
     */
    public void setEndOfYearAmount(final java.lang.Double newVal) {
        endOfYearAmount = newVal;
    }

    /**
     * InventoryCode is a numeric code used to uniquely identify an inventory
     * item.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getInventoryCode() {
        return inventoryCode;
    }

    /**
     * InventoryCode is a numeric code used to uniquely identify an inventory
     * item.
     *
     * @param newVal The new value for this property
     */
    public void setInventoryCode(final java.lang.Integer newVal) {
        inventoryCode = newVal;
    }

    /**
     * InventoryTypeCode is a numeric code indicating an inventory type. Valid
     * values are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased
     * Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getInventoryTypeCode() {
        return inventoryTypeCode;
    }

    /**
     * InventoryTypeCode is a numeric code indicating an inventory type. Valid
     * values are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased
     * Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.
     *
     * @param newVal The new value for this property
     */
    public void setInventoryTypeCode(final java.lang.Integer newVal) {
        inventoryTypeCode = newVal;
    }

    /**
     * cropUnseedableAcres is the number of unseedable acres of the crop - section 8
     * column e.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getCropUnseedableAcres() {
        return cropUnseedableAcres;
    }

    /**
     * cropUnseedableAcres is the number of unseedable acres of the crop - section 8
     * column e.
     *
     * @param newVal The new value for this property
     */
    public void setCropUnseedableAcres(java.lang.Double cropUnseedableAcres) {
        this.cropUnseedableAcres = cropUnseedableAcres;
    }

    /**
     * RevisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getRevisionCount() {
        return revisionCount;
    }

    /**
     * RevisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     *
     * @param newVal The new value for this property
     */
    public void setRevisionCount(final java.lang.Integer newVal) {
        revisionCount = newVal;
    }

}
