package ca.bc.gov.farms.persistence.v1.dto.staging;

/**
 * Z42ParticipantRefYear identifies reference year data by for productive units
 * by both inventory codes and structure groups. The productive capacity for
 * each code (inventory or structure group) is displayed in column 7. This file
 * will have multiple rows per participant and farming operation. This file is
 * created by FIPD. This is a staging object used to load temporary data set
 * before being merged into the operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:17 PM
 */
public final class Z42ParticipantRefYear {

    /**
     * participantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     */
    private java.lang.Integer participantPin;

    /** programYear is the stabilization year for this record. */
    private java.lang.Integer programYear;

    /**
     * productiveCapacityKey is the primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     */
    private java.lang.Integer productiveCapacityKey;

    /**
     * refOperationNumber identifies each operation a participant reports for a
     * given stab year. Operations may have different statement numbers in
     * different program years.
     */
    private java.lang.Integer refOperationNumber;

    /**
     * productiveTypeCode iIndicates if this is an Inventory Code 1, or a
     * StructureGroupCode - 2.
     */
    private java.lang.Integer productiveTypeCode;

    /** productiveCode is the structure group or inventory code. */
    private java.lang.Integer productiveCode;

    /**
     * productiveCapacityUnits is the number of units of production FIPD has used
     * when processing future year payments. # of head for livestock, the amount
     * of acres/square meters for crop production.
     */
    private java.lang.Double productiveCapacityUnits;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */
    public Z42ParticipantRefYear() {

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
     * ProductiveCapacityKey is the primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProductiveCapacityKey() {
        return productiveCapacityKey;
    }

    /**
     * ProductiveCapacityKey is the primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     *
     * @param newVal The new value for this property
     */
    public void setProductiveCapacityKey(final java.lang.Integer newVal) {
        productiveCapacityKey = newVal;
    }

    /**
     * RefOperationNumber identifies each operation a participant reports for a
     * given stab year. Operations may have different statement numbers in
     * different program years.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getRefOperationNumber() {
        return refOperationNumber;
    }

    /**
     * RefOperationNumber identifies each operation a participant reports for a
     * given stab year. Operations may have different statement numbers in
     * different program years.
     *
     * @param newVal The new value for this property
     */
    public void setRefOperationNumber(final java.lang.Integer newVal) {
        refOperationNumber = newVal;
    }

    /**
     * ProductiveTypeCode iIndicates if this is an Inventory Code 1, or a
     * StructureGroupCode - 2.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProductiveTypeCode() {
        return productiveTypeCode;
    }

    /**
     * ProductiveTypeCode iIndicates if this is an Inventory Code 1, or a
     * StructureGroupCode - 2.
     *
     * @param newVal The new value for this property
     */
    public void setProductiveTypeCode(final java.lang.Integer newVal) {
        productiveTypeCode = newVal;
    }

    /**
     * ProductiveCode is the structure group or inventory code.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProductiveCode() {
        return productiveCode;
    }

    /**
     * ProductiveCode is the structure group or inventory code.
     *
     * @param newVal The new value for this property
     */
    public void setProductiveCode(final java.lang.Integer newVal) {
        productiveCode = newVal;
    }

    /**
     * ProductiveCapacityUnits is the number of units of production FIPD has used
     * when processing future year payments. # of head for livestock, the amount
     * of acres/square meters for crop production.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getProductiveCapacityUnits() {
        return productiveCapacityUnits;
    }

    /**
     * ProductiveCapacityUnits is the number of units of production FIPD has used
     * when processing future year payments. # of head for livestock, the amount
     * of acres/square meters for crop production.
     *
     * @param newVal The new value for this property
     */
    public void setProductiveCapacityUnits(final java.lang.Double newVal) {
        productiveCapacityUnits = newVal;
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
