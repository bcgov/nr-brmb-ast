package ca.bc.gov.farms.domain.staging;

/**
 * Z40PrtcpntRefSuplDtl identifies reference year crop and livestock inventory
 * data, after it has been adjusted. Includes AARM price over-rides used during
 * 2007 processing. Reference year Purchased Inputs, Deferred Income &
 * Receivables, and Accounts Payable The Inventory Type code describes which
 * part of the form each row came from. This file will have multiple rows per
 * participant and farming operation. This file is created by FIPD. This is a
 * staging object used to load temporary data set before being merged into the
 * operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:16 PM
 */
public final class Z40PrtcpntRefSuplDtl {

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
     * priorYearSupplementalKey is the primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     */
    private java.lang.Integer priorYearSupplementalKey;

    /**
     * quantityStart is the Start of year quantity of inventory. For livestock
     * this will always be # of Head.
     */
    private java.lang.Double quantityStart;

    /**
     * startingPrice is the opening price used when calculating the benefit for
     * the reference year.
     */
    private java.lang.Double startingPrice;

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
     * endYearProducerPrice identifies the end of year price supplied by the
     * participant.
     */
    private java.lang.Double endYearProducerPrice;

    /**
     * isAcceptProducerPrice iIndicates if the P2 Producer price was used, even if
     * it was outside FMV bands for the reference year. Allowable values are Y -
     * Yes, N - No.
     */
    private java.lang.Boolean isAcceptProducerPrice;

    /**
     * endYearPrice is the actual opening price used by when calcuting the
     * reference year benefit.
     */
    private java.lang.Double endYearPrice;

    /**
     * aarmReferenceP1Price identifies that when processing 2007 payments, the
     * start of year prices for each reference year could be manipulated for AARM
     * purposes, to adjust the calculated margin for that year. This would affect
     * processing of the ProgramYear (2007), but not affect processing of the year
     * being adjusted. This field will only be populated if the start of year
     * price has been over- ridden.
     */
    private java.lang.Double aarmReferenceP1Price;

    /**
     * aarmReferenceP2Price identifies that when processing 2007 payments, the end
     * of year prices for each reference year could be manipulated for AARM
     * purposes, to adjust the calculated margin for that year. This would affect
     * processing of the ProgramYear (2007), but not affect processing of the year
     * being adjusted. This field will only be populated if the end of year price
     * has been over-ridden.
     */
    private java.lang.Double aarmReferenceP2Price;

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

    /** productionUnit is the unit of measure code. */
    private java.lang.Integer productionUnit;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */
    public Z40PrtcpntRefSuplDtl() {

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
     * PriorYearSupplementalKey is the primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getPriorYearSupplementalKey() {
        return priorYearSupplementalKey;
    }

    /**
     * PriorYearSupplementalKey is the primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     *
     * @param newVal The new value for this property
     */
    public void setPriorYearSupplementalKey(final java.lang.Integer newVal) {
        priorYearSupplementalKey = newVal;
    }

    /**
     * QuantityStart is the Start of year quantity of inventory. For livestock
     * this will always be # of Head.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getQuantityStart() {
        return quantityStart;
    }

    /**
     * QuantityStart is the Start of year quantity of inventory. For livestock
     * this will always be # of Head.
     *
     * @param newVal The new value for this property
     */
    public void setQuantityStart(final java.lang.Double newVal) {
        quantityStart = newVal;
    }

    /**
     * StartingPrice is the opening price used when calculating the benefit for
     * the reference year.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getStartingPrice() {
        return startingPrice;
    }

    /**
     * StartingPrice is the opening price used when calculating the benefit for
     * the reference year.
     *
     * @param newVal The new value for this property
     */
    public void setStartingPrice(final java.lang.Double newVal) {
        startingPrice = newVal;
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
     * EndYearProducerPrice identifies the end of year price supplied by the
     * participant.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getEndYearProducerPrice() {
        return endYearProducerPrice;
    }

    /**
     * EndYearProducerPrice identifies the end of year price supplied by the
     * participant.
     *
     * @param newVal The new value for this property
     */
    public void setEndYearProducerPrice(final java.lang.Double newVal) {
        endYearProducerPrice = newVal;
    }

    /**
     * IsAcceptProducerPrice iIndicates if the P2 Producer price was used, even if
     * it was outside FMV bands for the reference year. Allowable values are Y -
     * Yes, N - No.
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isAcceptProducerPrice() {
        return isAcceptProducerPrice;
    }

    /**
     * IsAcceptProducerPrice iIndicates if the P2 Producer price was used, even if
     * it was outside FMV bands for the reference year. Allowable values are Y -
     * Yes, N - No.
     *
     * @param newVal The new value for this property
     */
    public void setIsAcceptProducerPrice(final java.lang.Boolean newVal) {
        isAcceptProducerPrice = newVal;
    }

    /**
     * EndYearPrice is the actual opening price used by when calcuting the
     * reference year benefit.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getEndYearPrice() {
        return endYearPrice;
    }

    /**
     * EndYearPrice is the actual opening price used by when calcuting the
     * reference year benefit.
     *
     * @param newVal The new value for this property
     */
    public void setEndYearPrice(final java.lang.Double newVal) {
        endYearPrice = newVal;
    }

    /**
     * AarmReferenceP1Price identifies that when processing 2007 payments, the
     * start of year prices for each reference year could be manipulated for AARM
     * purposes, to adjust the calculated margin for that year. This would affect
     * processing of the ProgramYear (2007), but not affect processing of the year
     * being adjusted. This field will only be populated if the start of year
     * price has been over- ridden.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getAarmReferenceP1Price() {
        return aarmReferenceP1Price;
    }

    /**
     * AarmReferenceP1Price identifies that when processing 2007 payments, the
     * start of year prices for each reference year could be manipulated for AARM
     * purposes, to adjust the calculated margin for that year. This would affect
     * processing of the ProgramYear (2007), but not affect processing of the year
     * being adjusted. This field will only be populated if the start of year
     * price has been over- ridden.
     *
     * @param newVal The new value for this property
     */
    public void setAarmReferenceP1Price(final java.lang.Double newVal) {
        aarmReferenceP1Price = newVal;
    }

    /**
     * AarmReferenceP2Price identifies that when processing 2007 payments, the end
     * of year prices for each reference year could be manipulated for AARM
     * purposes, to adjust the calculated margin for that year. This would affect
     * processing of the ProgramYear (2007), but not affect processing of the year
     * being adjusted. This field will only be populated if the end of year price
     * has been over-ridden.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getAarmReferenceP2Price() {
        return aarmReferenceP2Price;
    }

    /**
     * AarmReferenceP2Price identifies that when processing 2007 payments, the end
     * of year prices for each reference year could be manipulated for AARM
     * purposes, to adjust the calculated margin for that year. This would affect
     * processing of the ProgramYear (2007), but not affect processing of the year
     * being adjusted. This field will only be populated if the end of year price
     * has been over-ridden.
     *
     * @param newVal The new value for this property
     */
    public void setAarmReferenceP2Price(final java.lang.Double newVal) {
        aarmReferenceP2Price = newVal;
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
     * ProductionUnit is the unit of measure code.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProductionUnit() {
        return productionUnit;
    }

    /**
     * ProductionUnit is the unit of measure code.
     *
     * @param newVal The new value for this property
     */
    public void setProductionUnit(final java.lang.Integer newVal) {
        productionUnit = newVal;
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
