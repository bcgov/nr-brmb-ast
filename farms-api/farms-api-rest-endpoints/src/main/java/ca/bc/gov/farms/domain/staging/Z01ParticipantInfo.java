package ca.bc.gov.farms.domain.staging;

/**
 * Z01ParticipantInfo lists AgriStablity Participant information, from Tax
 * return and page 1 of the Harmonized t1273 form. There will be 1 row in this
 * file per participant. This is a staging object used to load temporary data
 * set before being merged into the operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:03 PM
 */
public final class Z01ParticipantInfo {

    /**
     * participantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     */
    private java.lang.Integer participantPin;

    private String sin;

    private String businessNumber;

    private String trustNumber;

    /**
     * sinCtnBn is the participant Social Insurance Number, Business Number or
     * Trust Number. A SIN/SBRN is 9 numbers followed by a SBRN extension of 6
     * numbers (total 15 numbers). A Trust number will be the character T followed
     * by 8 number's. The corporate tax number (CTN) is 8 numbers. A SIN for an
     * individual is 9 numbers without the extension.
     */
    private String sinCtnBn;

    /** firstName is the First name for Individuals, blank for non-individuals. */
    private String firstName;

    /** lastName is the surname for Individuals, blank for non-individuals. */
    private String lastName;

    /** corpName is the corporation/commune name. (only for non-individuals). */
    private String corpName;

    /** address1 is the Mailing Address Line 1. */
    private String address1;

    /** address2 is the Mailing Address Line 2. */
    private String address2;

    /** city is the Mailing Address City. */
    private String city;

    /** province is the Mailing Address Province. */
    private String province;

    /** postalCode is the Mailing Address zip code. */
    private String postalCode;

    /** country is the mailing address country. */
    private String country;

    /**
     * participantTypeCode identifies the type of participant. Must be either 1-
     * Individual, or 2-Entity.
     */
    private java.lang.Integer participantTypeCode;

    /** participantLanguage is the preferred language of the participant. */
    private java.lang.Integer participantLanguage;

    /** participantFax is the fax number of the participant. */
    private String participantFax;

    /**
     * participantPhoneDay is the day time telephone number of the participant.
     */
    private String participantPhoneDay;

    /**
     * participantPhoneEvening is the evening time telephone number of the
     * participant.
     */
    private String participantPhoneEvening;

    private String participantPhoneCell;

    private String participantEmail;

    /** contactFirstName is the first name of the contact. */
    private String contactFirstName;

    /** contactLastName is the last name of the contact. */
    private String contactLastName;

    /**
     * contactBusinessName is the business name of the contact as provided by the
     * participant.
     */
    private String contactBusinessName;

    /** contactAddress1 is the Contact Mailing Address line 1. */
    private String contactAddress1;

    /** contactAddress2 is the Contact Mailing Address line 2. */
    private String contactAddress2;

    /** contactCity is the Contact Mailng Address City. */
    private String contactCity;

    /** contactProvince is the contact mailing address province. */
    private String contactProvince;

    /** contactPostalCode is the postal code of the contact. */
    private String contactPostalCode;

    /** contactPhoneDay is the day time telephone number of the contact. */
    private String contactPhoneDay;

    /** contactFaxNumber is the fax number of the contact. */
    private String contactFaxNumber;

    private String contactPhoneCell;

    /**
     * isPublicOffice is the Public Office / AAFC Employee Indicator. Allowable
     * Values are 0 - No box checked, 1 - Checked Yes, 2 - Checked No.
     */
    private java.lang.Integer publicOffice;

    /**
     * identEffectiveDate is the date the address and name information was last
     * updated.
     * This may apply to the participant information or the contact.
     */
    private String identEffectiveDate;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** The Z02PartpntFarmInfo associated with this Z01ParticipantInfo. */
    private Z02PartpntFarmInfo[] z02PartpntFarmInfo;

    /** Constructor. */
    public Z01ParticipantInfo() {

    }

    /**
     * @return Z02PartpntFarmInfo[]
     */
    public Z02PartpntFarmInfo[] getZ02PartpntFarmInfo() {
        return z02PartpntFarmInfo;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ02PartpntFarmInfo(final Z02PartpntFarmInfo[] newVal) {
        z02PartpntFarmInfo = newVal;
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

    public String getSin() {
        return sin;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

    public void setBusinessNumber(String businessNumber) {
        this.businessNumber = businessNumber;
    }

    public String getTrustNumber() {
        return trustNumber;
    }

    public void setTrustNumber(String trustNumber) {
        this.trustNumber = trustNumber;
    }

    /**
     * SinCtnBn is the participant Social Insurance Number, Business Number or
     * Trust Number. A SIN/SBRN is 9 numbers followed by a SBRN extension of 6
     * numbers (total 15 numbers). A Trust number will be the character T followed
     * by 8 number's. The corporate tax number (CTN) is 8 numbers. A SIN for an
     * individual is 9 numbers without the extension.
     *
     * @return String
     */
    public String getSinCtnBn() {
        return sinCtnBn;
    }

    /**
     * SinCtnBn is the participant Social Insurance Number, Business Number or
     * Trust Number. A SIN/SBRN is 9 numbers followed by a SBRN extension of 6
     * numbers (total 15 numbers). A Trust number will be the character T followed
     * by 8 number's. The corporate tax number (CTN) is 8 numbers. A SIN for an
     * individual is 9 numbers without the extension.
     *
     * @param newVal The new value for this property
     */
    public void setSinCtnBn(final String newVal) {
        sinCtnBn = newVal;
    }

    /**
     * FirstName is the First name for Individuals, blank for non-individuals.
     *
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * FirstName is the First name for Individuals, blank for non-individuals.
     *
     * @param newVal The new value for this property
     */
    public void setFirstName(final String newVal) {
        firstName = newVal;
    }

    /**
     * LastName is the surname for Individuals, blank for non-individuals.
     *
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * LastName is the surname for Individuals, blank for non-individuals.
     *
     * @param newVal The new value for this property
     */
    public void setLastName(final String newVal) {
        lastName = newVal;
    }

    /**
     * CorpName is the corporation/commune name. (only for non-individuals).
     *
     * @return String
     */
    public String getCorpName() {
        return corpName;
    }

    /**
     * CorpName is the corporation/commune name. (only for non-individuals).
     *
     * @param newVal The new value for this property
     */
    public void setCorpName(final String newVal) {
        corpName = newVal;
    }

    /**
     * Address1 is the Mailing Address Line 1.
     *
     * @return String
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Address1 is the Mailing Address Line 1.
     *
     * @param newVal The new value for this property
     */
    public void setAddress1(final String newVal) {
        address1 = newVal;
    }

    /**
     * Address2 is the Mailing Address Line 2.
     *
     * @return String
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Address2 is the Mailing Address Line 2.
     *
     * @param newVal The new value for this property
     */
    public void setAddress2(final String newVal) {
        address2 = newVal;
    }

    /**
     * City is the Mailing Address City.
     *
     * @return String
     */
    public String getCity() {
        return city;
    }

    /**
     * City is the Mailing Address City.
     *
     * @param newVal The new value for this property
     */
    public void setCity(final String newVal) {
        city = newVal;
    }

    /**
     * Province is the Mailing Address Province.
     *
     * @return String
     */
    public String getProvince() {
        return province;
    }

    /**
     * Province is the Mailing Address Province.
     *
     * @param newVal The new value for this property
     */
    public void setProvince(final String newVal) {
        province = newVal;
    }

    /**
     * PostalCode is the Mailing Address zip code.
     *
     * @return String
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * PostalCode is the Mailing Address zip code.
     *
     * @param newVal The new value for this property
     */
    public void setPostalCode(final String newVal) {
        postalCode = newVal;
    }

    /**
     * Country is the mailing address country.
     *
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Country is the mailing address country.
     *
     * @param newVal The new value for this property
     */
    public void setCountry(final String newVal) {
        country = newVal;
    }

    /**
     * ParticipantTypeCode identifies the type of participant. Must be either 1-
     * Individual, or 2-Entity.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getParticipantTypeCode() {
        return participantTypeCode;
    }

    /**
     * ParticipantTypeCode identifies the type of participant. Must be either 1-
     * Individual, or 2-Entity.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantTypeCode(final java.lang.Integer newVal) {
        participantTypeCode = newVal;
    }

    /**
     * ParticipantLanguage is the preferred language of the participant.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getParticipantLanguage() {
        return participantLanguage;
    }

    /**
     * ParticipantLanguage is the preferred language of the participant.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantLanguage(final java.lang.Integer newVal) {
        participantLanguage = newVal;
    }

    /**
     * ParticipantFax is the fax number of the participant.
     *
     * @return String
     */
    public String getParticipantFax() {
        return participantFax;
    }

    /**
     * ParticipantFax is the fax number of the participant.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantFax(final String newVal) {
        participantFax = newVal;
    }

    /**
     * ParticipantPhoneDay is the day time telephone number of the participant.
     *
     * @return String
     */
    public String getParticipantPhoneDay() {
        return participantPhoneDay;
    }

    /**
     * ParticipantPhoneDay is the day time telephone number of the participant.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantPhoneDay(final String newVal) {
        participantPhoneDay = newVal;
    }

    /**
     * ParticipantPhoneEvening is the evening time telephone number of the
     * participant.
     *
     * @return String
     */
    public String getParticipantPhoneEvening() {
        return participantPhoneEvening;
    }

    /**
     * ParticipantPhoneEvening is the evening time telephone number of the
     * participant.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantPhoneEvening(final String newVal) {
        participantPhoneEvening = newVal;
    }

    /**
     * ContactFirstName is the first name of the contact.
     *
     * @return String
     */
    public String getContactFirstName() {
        return contactFirstName;
    }

    /**
     * ContactFirstName is the first name of the contact.
     *
     * @param newVal The new value for this property
     */
    public void setContactFirstName(final String newVal) {
        contactFirstName = newVal;
    }

    /**
     * ContactLastName is the last name of the contact.
     *
     * @return String
     */
    public String getContactLastName() {
        return contactLastName;
    }

    /**
     * ContactLastName is the last name of the contact.
     *
     * @param newVal The new value for this property
     */
    public void setContactLastName(final String newVal) {
        contactLastName = newVal;
    }

    /**
     * ContactBusinessName is the business name of the contact as provided by the
     * participant.
     *
     * @return String
     */
    public String getContactBusinessName() {
        return contactBusinessName;
    }

    /**
     * ContactBusinessName is the business name of the contact as provided by the
     * participant.
     *
     * @param newVal The new value for this property
     */
    public void setContactBusinessName(final String newVal) {
        contactBusinessName = newVal;
    }

    /**
     * ContactAddress1 is the Contact Mailing Address line 1.
     *
     * @return String
     */
    public String getContactAddress1() {
        return contactAddress1;
    }

    /**
     * ContactAddress1 is the Contact Mailing Address line 1.
     *
     * @param newVal The new value for this property
     */
    public void setContactAddress1(final String newVal) {
        contactAddress1 = newVal;
    }

    /**
     * ContactAddress2 is the Contact Mailing Address line 2.
     *
     * @return String
     */
    public String getContactAddress2() {
        return contactAddress2;
    }

    /**
     * ContactAddress2 is the Contact Mailing Address line 2.
     *
     * @param newVal The new value for this property
     */
    public void setContactAddress2(final String newVal) {
        contactAddress2 = newVal;
    }

    /**
     * ContactCity is the Contact Mailng Address City.
     *
     * @return String
     */
    public String getContactCity() {
        return contactCity;
    }

    /**
     * ContactCity is the Contact Mailng Address City.
     *
     * @param newVal The new value for this property
     */
    public void setContactCity(final String newVal) {
        contactCity = newVal;
    }

    /**
     * ContactProvince is the contact mailing address province.
     *
     * @return String
     */
    public String getContactProvince() {
        return contactProvince;
    }

    /**
     * ContactProvince is the contact mailing address province.
     *
     * @param newVal The new value for this property
     */
    public void setContactProvince(final String newVal) {
        contactProvince = newVal;
    }

    /**
     * ContactPostalCode is the postal code of the contact.
     *
     * @return String
     */
    public String getContactPostalCode() {
        return contactPostalCode;
    }

    /**
     * ContactPostalCode is the postal code of the contact.
     *
     * @param newVal The new value for this property
     */
    public void setContactPostalCode(final String newVal) {
        contactPostalCode = newVal;
    }

    /**
     * ContactPhoneDay is the day time telephone number of the contact.
     *
     * @return String
     */
    public String getContactPhoneDay() {
        return contactPhoneDay;
    }

    /**
     * ContactPhoneDay is the day time telephone number of the contact.
     *
     * @param newVal The new value for this property
     */
    public void setContactPhoneDay(final String newVal) {
        contactPhoneDay = newVal;
    }

    /**
     * ContactFaxNumber is the fax number of the contact.
     *
     * @return String
     */
    public String getContactFaxNumber() {
        return contactFaxNumber;
    }

    /**
     * ContactFaxNumber is the fax number of the contact.
     *
     * @param newVal The new value for this property
     */
    public void setContactFaxNumber(final String newVal) {
        contactFaxNumber = newVal;
    }

    /**
     * IsPublicOffice is the Public Office / AAFC Employee Indicator. Allowable
     * Values are 0 - No box checked, 1 - Checked Yes, 2 - Checked No.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getPublicOffice() {
        return publicOffice;
    }

    /**
     * IsPublicOffice is the Public Office / AAFC Employee Indicator. Allowable
     * Values are 0 - No box checked, 1 - Checked Yes, 2 - Checked No.
     *
     * @param newVal The new value for this property
     */
    public void setPublicOffice(final java.lang.Integer newVal) {
        publicOffice = newVal;
    }

    /**
     * identEffectiveDate is the date the address and name information was last
     * updated.
     * This may apply to the participant information or the contact.
     *
     * @return String
     */
    public String getIdentEffectiveDate() {
        return identEffectiveDate;
    }

    /**
     * identEffectiveDate is the date the address and name information was last
     * updated.
     * This may apply to the participant information or the contact.
     *
     * @param identEffectiveDate The new value for this property
     */
    public void setIdentEffectiveDate(String identEffectiveDate) {
        this.identEffectiveDate = identEffectiveDate;
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

    public String getParticipantEmail() {
        return participantEmail;
    }

    public void setParticipantEmail(String participantEmail) {
        this.participantEmail = participantEmail;
    }

    public String getParticipantPhoneCell() {
        return participantPhoneCell;
    }

    public void setParticipantPhoneCell(String participantPhoneCell) {
        this.participantPhoneCell = participantPhoneCell;
    }

    public String getContactPhoneCell() {
        return contactPhoneCell;
    }

    public void setContactPhoneCell(String contactPhoneCell) {
        this.contactPhoneCell = contactPhoneCell;
    }

}
