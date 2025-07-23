package ca.bc.gov.farms.persistence.v1.dto.staging;

/**
 * Z05PartnerInfo identifies all the partners the producer has entered in
 * section 6 of the Harmonized form. This should not include the participant.
 * Even if the participant is in a partnership, thre is no requirement to submit
 * a list of participants, so this file may not have any data for that
 * statement. This file is sent to the provinces by FIPD. This is a staging
 * object used to load temporary data set before being merged into the
 * operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:09 PM
 */
public final class Z05PartnerInfo {

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

    /** partnerInfoKey is a unique surrogate identifier for Z05PartnerInfo. */
    private java.lang.Integer partnerInfoKey;

    /**
     * partnershipPin uniquely identifies the partnership. If both the partners in
     * an operation file applications, the same partnership pin will show up under
     * both pins. partnershipPin represents the same operation if/when they are
     * used in different program years.
     */
    private java.lang.Integer partnershipPin;

    /** partnerFirstName is the partners given name. */
    private String partnerFirstName;

    /** partnerLastName is the partners surname. */
    private String partnerLastName;

    /** partnerCorpName is the Partners Corporate name. */
    private String partnerCorpName;

    /**
     * partnerSinCtnBn is the Partners SIN/CTN/BN - see participant SIN/CTN/BN for
     * valid formats.
     */
    private String partnerSinCtnBn;

    /** partnerPercent is the partners percentage share. */
    private java.lang.Double partnerPercent;

    /** partnerPin is CAIS pin of the partner, if one is available. */
    private java.lang.Integer partnerPin;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** Constructor. */
    public Z05PartnerInfo() {

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
     * PartnerInfoKey is a unique surrogate identifier for Z05PartnerInfo.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getPartnerInfoKey() {
        return partnerInfoKey;
    }

    /**
     * PartnerInfoKey is a unique surrogate identifier for Z05PartnerInfo.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerInfoKey(final java.lang.Integer newVal) {
        partnerInfoKey = newVal;
    }

    /**
     * PartnershipPin uniquely identifies the partnership. If both the partners in
     * an operation file applications, the same partnership pin will show up under
     * both pins. partnershipPin represents the same operation if/when they are
     * used in different program years.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getPartnershipPin() {
        return partnershipPin;
    }

    /**
     * PartnershipPin uniquely identifies the partnership. If both the partners in
     * an operation file applications, the same partnership pin will show up under
     * both pins. partnershipPin represents the same operation if/when they are
     * used in different program years.
     *
     * @param newVal The new value for this property
     */
    public void setPartnershipPin(final java.lang.Integer newVal) {
        partnershipPin = newVal;
    }

    /**
     * PartnerFirstName is the partners given name.
     *
     * @return String
     */
    public String getPartnerFirstName() {
        return partnerFirstName;
    }

    /**
     * PartnerFirstName is the partners given name.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerFirstName(final String newVal) {
        partnerFirstName = newVal;
    }

    /**
     * PartnerLastName is the partners surname.
     *
     * @return String
     */
    public String getPartnerLastName() {
        return partnerLastName;
    }

    /**
     * PartnerLastName is the partners surname.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerLastName(final String newVal) {
        partnerLastName = newVal;
    }

    /**
     * PartnerCorpName is the Partners Corporate name.
     *
     * @return String
     */
    public String getPartnerCorpName() {
        return partnerCorpName;
    }

    /**
     * PartnerCorpName is the Partners Corporate name.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerCorpName(final String newVal) {
        partnerCorpName = newVal;
    }

    /**
     * PartnerSinCtnBn is the Partners SIN/CTN/BN - see participant SIN/CTN/BN for
     * valid formats.
     *
     * @return String
     */
    public String getPartnerSinCtnBn() {
        return partnerSinCtnBn;
    }

    /**
     * PartnerSinCtnBn is the Partners SIN/CTN/BN - see participant SIN/CTN/BN for
     * valid formats.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerSinCtnBn(final String newVal) {
        partnerSinCtnBn = newVal;
    }

    /**
     * PartnerPercent is the partners percentage share.
     *
     * @return java.lang.Double
     */
    public java.lang.Double getPartnerPercent() {
        return partnerPercent;
    }

    /**
     * PartnerPercent is the partners percentage share.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerPercent(final java.lang.Double newVal) {
        partnerPercent = newVal;
    }

    /**
     * PartnerPin is CAIS pin of the partner, if one is available.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getPartnerPin() {
        return partnerPin;
    }

    /**
     * PartnerPin is CAIS pin of the partner, if one is available.
     *
     * @param newVal The new value for this property
     */
    public void setPartnerPin(final java.lang.Integer newVal) {
        partnerPin = newVal;
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
