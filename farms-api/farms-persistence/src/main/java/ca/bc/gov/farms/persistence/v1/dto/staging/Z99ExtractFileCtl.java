package ca.bc.gov.farms.persistence.v1.dto.staging;

/**
 * Z99ExtractFileCtl identifies row counts for every other file in the extract.
 * There will be one row per file. This file is created by FIPD. If both the
 * AgriStability t1273 extract files and the AgriStability supplemental data
 * files are extracted, there will be a single file 99 with counts for all
 * files. This is a staging object used to load temporary data set before being
 * merged into the operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:19 PM
 */
public final class Z99ExtractFileCtl {

    /** extractFileNumber the 2 digit file number of each file (01, 02, �). */
    private java.lang.Integer extractFileNumber;

    /** extractDate is the ProgramYear for this record. */
    private String extractDate;

    /** rowCount is the number of rows each extract file should have. */
    private java.lang.Integer rowCount;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */
    public Z99ExtractFileCtl() {

    }

    /**
     * ExtractFileNumber the 2 digit file number of each file (01, 02, �).
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getExtractFileNumber() {
        return extractFileNumber;
    }

    /**
     * ExtractFileNumber the 2 digit file number of each file (01, 02, �).
     *
     * @param newVal The new value for this property
     */
    public void setExtractFileNumber(final java.lang.Integer newVal) {
        extractFileNumber = newVal;
    }

    /**
     * ExtractDate is the ProgramYear for this record.
     *
     * @return String
     */
    public String getExtractDate() {
        return extractDate;
    }

    /**
     * ExtractDate is the ProgramYear for this record.
     *
     * @param newVal The new value for this property
     */
    public void setExtractDate(final String newVal) {
        extractDate = newVal;
    }

    /**
     * RowCount is the number of rows each extract file should have.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getRowCount() {
        return rowCount;
    }

    /**
     * RowCount is the number of rows each extract file should have.
     *
     * @param newVal The new value for this property
     */
    public void setRowCount(final java.lang.Integer newVal) {
        rowCount = newVal;
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
