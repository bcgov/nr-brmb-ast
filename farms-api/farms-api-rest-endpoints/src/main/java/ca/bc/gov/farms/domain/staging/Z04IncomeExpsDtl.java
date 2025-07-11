package ca.bc.gov.farms.domain.staging;

/**
 * Z04IncomeExpsDtl identifies all the income(sales) and expense(purchases)
 * listed by the producer on each statement A/B form. There can be multiple rows
 * for a single income or expense code, if this is how the producer filled out
 * the form. This file is sent to the provinces by FIPD. This is a staging
 * object used to load temporary data set before being merged into the
 * operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:08 PM
 */
public final class Z04IncomeExpsDtl {

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
     * incomeExpenseKey is th Primary key for the file. Provides each row with a
     * unique identifier over the whole file.
     */
    private java.lang.Integer incomeExpenseKey;

    /**
     * isIe is the Income / Expense indicator. Allowable values are I - Income, E
     * - Expense.
     */
    private String ie;

    /** amount is the Income / Expense Amount (not adjusted for prshp pct). */
    private java.lang.Double amount;

    /** lineCode is the Income/Expense code. */
    private java.lang.Integer lineCode;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */

    public Z04IncomeExpsDtl() {

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
     * IncomeExpenseKey is th Primary key for the file. Provides each row with a
     * unique identifier over the whole file.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getIncomeExpenseKey() {
        return incomeExpenseKey;
    }

    /**
     * IncomeExpenseKey is th Primary key for the file. Provides each row with a
     * unique identifier over the whole file.
     *
     * @param newVal The new value for this property
     */
    public void setIncomeExpenseKey(final java.lang.Integer newVal) {
        incomeExpenseKey = newVal;
    }

    /**
     * isIe is the Income / Expense indicator. Allowable values are I - Income, E
     * - Expense.
     *
     * @return java.lang.Boolean
     */
    public String getIe() {
        return ie;
    }

    /**
     * isIe is the Income / Expense indicator. Allowable values are I - Income, E
     * - Expense.
     *
     * @param newVal The new value for this property
     */
    public void setIe(final String newVal) {
        ie = newVal;
    }

    /**
     * Amount is the Income / Expense Amount (not adjusted for prshp pct).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getAmount() {
        return amount;
    }

    /**
     * Amount is the Income / Expense Amount (not adjusted for prshp pct).
     *
     * @param newVal The new value for this property
     */
    public void setAmount(final java.lang.Double newVal) {
        amount = newVal;
    }

    /**
     * LineCode is the Income/Expense code.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getLineCode() {
        return lineCode;
    }

    /**
     * LineCode is the Income/Expense code.
     *
     * @param newVal The new value for this property
     */
    public void setLineCode(final java.lang.Integer newVal) {
        lineCode = newVal;
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
