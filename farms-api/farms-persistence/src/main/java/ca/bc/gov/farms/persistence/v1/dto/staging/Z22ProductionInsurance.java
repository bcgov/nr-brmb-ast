package ca.bc.gov.farms.persistence.v1.dto.staging;

/**
 * Z22ProductionInsurance identifies the Insurance contract numbers provided by
 * the participant on the supplemental page of the AgriStability application.
 * This file will have 0 to 4 rows per participant and farming operation, for
 * the current year. This file is created by FIPD. This is a staging object used
 * to load temporary data set before being merged into the operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:13 PM
 */
public final class Z22ProductionInsurance {

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
     * productionInsuranceKey is th primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     */
    private java.lang.Integer productionInsuranceKey;

    /** productionInsuranceNumber is the productionInsuranceNumber. */
    private String productionInsuranceNumber;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */
    public Z22ProductionInsurance() {

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
     * ProductionInsuranceKey is th primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProductionInsuranceKey() {
        return productionInsuranceKey;
    }

    /**
     * ProductionInsuranceKey is th primary key for the file. Provides each row
     * with a unique identifier over the whole file.
     *
     * @param newVal The new value for this property
     */
    public void setProductionInsuranceKey(final java.lang.Integer newVal) {
        productionInsuranceKey = newVal;
    }

    /**
     * ProductionInsuranceNumber is the productionInsuranceNumber.
     *
     * @return String
     */
    public String getProductionInsuranceNumber() {
        return productionInsuranceNumber;
    }

    /**
     * ProductionInsuranceNumber is the productionInsuranceNumber.
     *
     * @param newVal The new value for this property
     */
    public void setProductionInsuranceNumber(final String newVal) {
        productionInsuranceNumber = newVal;
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
