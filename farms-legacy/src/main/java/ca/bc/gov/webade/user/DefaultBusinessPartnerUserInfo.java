package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jross
 */
public class DefaultBusinessPartnerUserInfo extends DefaultIndividualUserInfo
        implements BusinessPartnerUserInfo {
    private static final long serialVersionUID = 1296423493613045283L;
    private static final String[] USER_ATTRIBUTES = new String[] {
            BUSINESS_GUID,
            BUSINESS_LEGAL_NAME,
            BUSINESS_TRADING_NAME,
            BUSINESS_ACTIVATION_CODE,
            BUSINESS_NUMBER,
            BUSINESS_NUMBER_VERIFIED,
            INCORPORATION_NUMBER,
            EXTRA_PROVINCIAL_REGISTRATION_NUMBER,
            BUSINESS_LUID,
            BUSINESS_TYPE_CODE,
            BUSINESS_TYPE_OTHER,
            BN_HUB_BUSINESS_TYPE_CODE,
            STATEMENT_OF_REGISTRATION_NUMBER,
            JURISDICTION_OF_INCORPORATION,
            DOING_BUSINESS_AS,
            IS_SUSPENDED,
            BUSINESS_ADDRESS_LINE_1,
            BUSINESS_ADDRESS_LINE_2,
            BUSINESS_ADDRESS_CITY,
            BUSINESS_ADDRESS_PROVINCE,
            BUSINESS_ADDRESS_COUNTRY,
            BUSINESS_ADDRESS_POSTAL_CODE,
            BUSINESS_ADDRESS_UNSTRUCTURED
            };
    private HashMap<String,Object> userAttributes = new HashMap<String,Object>();

    /**
     * Default Constructor.
     */
    public DefaultBusinessPartnerUserInfo() {
        super(new UserCredentials(UserTypeCode.BUSINESS_PARTNER));
    }

    /**
     * Copy constructor. Copies all user details from the given object to this
     * instance.
     * @param user
     *            The user object to copy.
     */
    @SuppressWarnings("unchecked")
	public DefaultBusinessPartnerUserInfo(DefaultBusinessPartnerUserInfo user) {
        super(user);
        this.userAttributes = (HashMap<String,Object>) user.userAttributes.clone();
    }

    /**
     * Basic constructor. Subclasses should pass in an instance of the
     * appropriate credentials class.
     * @param credentials
     *            The appropriate user credentials class.
     */
    protected DefaultBusinessPartnerUserInfo(UserCredentials credentials) {
        super(credentials);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessGUID()
     */
    @Override
	public final GUID getBusinessGUID() {
        return (GUID) this.userAttributes.get(BUSINESS_GUID);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessLUID()
     */
    @Override
	public final String getBusinessLUID() {
        return (String) this.userAttributes.get(BUSINESS_LUID);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessLegalName()
     */
    @Override
	public final String getBusinessLegalName() {
        return (String) this.userAttributes.get(BUSINESS_LEGAL_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessTradingName()
     */
    @Override
	public String getBusinessTradingName() {
        return (String) this.userAttributes.get(BUSINESS_TRADING_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessActivationCode()
     */
    @Override
	public final String getBusinessActivationCode() {
        return (String) this.userAttributes.get(BUSINESS_ACTIVATION_CODE);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessNumber()
     */
    @Override
	public String getBusinessNumber() {
        return (String) this.userAttributes.get(BUSINESS_NUMBER);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#isBusinessNumberVerified()
     */
    @Override
	public boolean isBusinessNumberVerified() {
        Boolean businessNumberVerified =
                (Boolean) this.userAttributes.get(BUSINESS_NUMBER_VERIFIED);
        if(businessNumberVerified == null) {
            return false;
        }
        return businessNumberVerified.booleanValue();
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getIncorporationNumber()
     */
    @Override
	public String getIncorporationNumber() {
        return (String) this.userAttributes.get(INCORPORATION_NUMBER);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getExtraProvincialRegistrationNumber()
     */
    @Override
	public String getExtraProvincialRegistrationNumber() {
        return (String) this.userAttributes.get(EXTRA_PROVINCIAL_REGISTRATION_NUMBER);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessTypeCode()
     */
    @Override
	public String getBusinessTypeCode() {
        return (String) this.userAttributes.get(BUSINESS_TYPE_CODE);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessTypeOther()
     */
    @Override
	public String getBusinessTypeOther() {
        return (String) this.userAttributes.get(BUSINESS_TYPE_OTHER);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBnHubBusinessTypeCode()
     */
    @Override
	public String getBnHubBusinessTypeCode() {
        return (String) this.userAttributes.get(BN_HUB_BUSINESS_TYPE_CODE);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getStatementOfRegistrationNumber()
     */
    @Override
	public String getStatementOfRegistrationNumber() {
        return (String) this.userAttributes.get(STATEMENT_OF_REGISTRATION_NUMBER);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getJurisdictionOfIncorporation()
     */
    @Override
	public String getJurisdictionOfIncorporation() {
        return (String) this.userAttributes.get(JURISDICTION_OF_INCORPORATION);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getDoingBusinessAs()
     */
    @Override
	public String getDoingBusinessAs() {
        return (String) this.userAttributes.get(DOING_BUSINESS_AS);
    }
    
    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#isSuspended()
     */
    @Override
	public boolean isSuspended() {
        Boolean suspended = (Boolean) this.userAttributes.get(IS_SUSPENDED);
        if(suspended == null) {
            return false;
        }
        return suspended.booleanValue();
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressLine1()
     */
    @Override
	public String getBusinessAddressLine1() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_LINE_1);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressLine2()
     */
    @Override
	public String getBusinessAddressLine2() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_LINE_2);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressCity()
     */
    @Override
	public String getBusinessAddressCity() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_CITY);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressProvince()
     */
    @Override
	public String getBusinessAddressProvince() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_PROVINCE);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressCountry()
     */
    @Override
	public String getBusinessAddressCountry() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_COUNTRY);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressPostalCode()
     */
    @Override
	public String getBusinessAddressPostalCode() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_POSTAL_CODE);
    }

    /**
     * @see ca.bc.gov.webade.user.BusinessPartnerUserInfo#getBusinessAddressUnstructured()
     */
    @Override
	public String getBusinessAddressUnstructured() {
        return (String) this.userAttributes.get(BUSINESS_ADDRESS_UNSTRUCTURED);
    }

    /**
     * @see ca.bc.gov.webade.user.AbstractWebADEUserInfo#getAttributeNames()
     */
    @Override
	public String[] getAttributeNames() {
        ArrayList<String> attributeNames = new ArrayList<String>();
        String[] parentNames = super.getAttributeNames();
        for (int i = 0; i < parentNames.length; i++) {
            String currentName = parentNames[i];
            attributeNames.add(currentName);
        }
        for (int i = 0; i < USER_ATTRIBUTES.length; i++) {
            String currentName = USER_ATTRIBUTES[i];
            attributeNames.add(currentName);
        }
        return attributeNames.toArray(new String[attributeNames
                .size()]);
    }

    /**
     * @see ca.bc.gov.webade.user.AbstractWebADEUserInfo#getAttributeValue(java.lang.String)
     */
    @Override
	public Object getAttributeValue(String attributeName) {
        Object attrValue = super.getAttributeValue(attributeName);
        if (attrValue == null) {
            attrValue = this.userAttributes.get(attributeName);
        }
        return attrValue;
    }

    /**
     * @see ca.bc.gov.webade.user.AbstractWebADEUserInfo#hasAttribute(java.lang.String)
     */
    @Override
	public boolean hasAttribute(String attributeName) {
        boolean foundAttr = super.hasAttribute(attributeName);
        if (!foundAttr) {
            for (int i = 0; i < USER_ATTRIBUTES.length; i++) {
                if (USER_ATTRIBUTES[i].equals(attributeName)) {
                    foundAttr = true;
                    break;
                }
            }
        }
        return foundAttr;
    }

    /**
     * Sets this user's businessGUID attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param businessGUID
     *            The businessGUID to set.
     */
    public final void setBusinessGUID(GUID businessGUID) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_GUID, businessGUID);
        }
    }
    
    /**
     * Sets this user's businessLUID attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param businessLUID
     *            The businessLUID to set.
     */
    public final void setBusinessLUID(String businessLUID) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_LUID, businessLUID);
        }
    }

    /**
     * Sets this user's businessLegalName attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param businessLegalName
     *            The businessLegalName to set.
     */
    public final void setBusinessLegalName(String businessLegalName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_LEGAL_NAME, businessLegalName);
        }
    }

    /**
     * Sets this user's businessTradingName attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessTradingName
     *            The businessTradingName to set.
     */
    public final void setBusinessTradingName(String businessTradingName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_TRADING_NAME,
                    businessTradingName);
        }
    }

    /**
     * Sets this user's businessActivationCode attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessActivationCode
     *            The businessActivationCode to set.
     */
    public final void setBusinessActivationCode(String businessActivationCode) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ACTIVATION_CODE,
                    businessActivationCode);
        }
    }

    /**
     * Sets this user's businessNumber attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessNumber
     *            The businessNumber to set.
     */
    public final void setBusinessNumber(String businessNumber) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_NUMBER,
                    businessNumber);
        }
    }
    
    /**
     * Sets this user's businessNumberVerified attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessNumberVerified
     *            The businessNumberVerified to set.
     */
    public final void setBusinessNumberVerified(boolean businessNumberVerified) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_NUMBER_VERIFIED,
                    Boolean.valueOf(businessNumberVerified));
        }
    }

    /**
     * Sets this user's incorporationNumber attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param incorporationNumber
     *            The incorporationNumber to set.
     */
    public final void setIncorporationNumber(String incorporationNumber) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(INCORPORATION_NUMBER,
                    incorporationNumber);
        }
    }

    /**
     * Sets this user's extraProvincialRegistrationNumber attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param extraProvincialRegistrationNumber
     *            The extraProvincialRegistrationNumber to set.
     */
    public final void setExtraProvincialRegistrationNumber(String extraProvincialRegistrationNumber) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(EXTRA_PROVINCIAL_REGISTRATION_NUMBER,
                    extraProvincialRegistrationNumber);
        }
    }

    /**
     * Sets this user's businessTypeCode attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessTypeCode
     *            The businessTypeCode to set.
     */
    public final void setBusinessTypeCode(String businessTypeCode) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_TYPE_CODE,
                    businessTypeCode);
        }
    }
    
    /**
     * Sets this user's businessTypeOther attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessTypeOther
     *            The businessTypeOther to set.
     */
    public final void setBusinessTypeOther(String businessTypeOther) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_TYPE_OTHER,
                    businessTypeOther);
        }
    }
    
    /**
     * Sets this user's bnHubBusinessTypeCode attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param bnHubBusinessTypeCode
     *            The bnHubBusinessTypeCode to set.
     */
    public final void setBnHubBusinessTypeCode(String bnHubBusinessTypeCode) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BN_HUB_BUSINESS_TYPE_CODE,
                    bnHubBusinessTypeCode);
        }
    }
    
    /**
     * Sets this user's statementOfRegistrationNumber attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param statementOfRegistrationNumber
     *            The statementOfRegistrationNumber to set.
     */
    public final void setStatementOfRegistrationNumber(String statementOfRegistrationNumber) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(STATEMENT_OF_REGISTRATION_NUMBER,
                    statementOfRegistrationNumber);
        }
    }
    
    /**
     * Sets this user's jurisdictionOfIncorporation attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param jurisdictionOfIncorporation
     *            The jurisdictionOfIncorporation to set.
     */
    public final void setJurisdictionOfIncorporation(String jurisdictionOfIncorporation) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(JURISDICTION_OF_INCORPORATION,
                    jurisdictionOfIncorporation);
        }
    }
    
    /**
     * Sets this user's doingBusinessAs attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param doingBusinessAs
     *            The doingBusinessAs to set.
     */
    public final void setDoingBusinessAs(String doingBusinessAs) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(DOING_BUSINESS_AS,
                    doingBusinessAs);
        }
    }
    
    /**
     * Sets this user's suspended attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param suspended
     *            The suspended to set.
     */
    public final void setSuspended(boolean suspended) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(IS_SUSPENDED,
                    Boolean.valueOf(suspended));
        }
    }
    
    /**
     * Sets this user's businessAddressLine1 attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressLine1 The businessAddressLine1 to set.
     */
    public final void setBusinessAddressLine1(String businessAddressLine1) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_LINE_1, businessAddressLine1);
        }
    }
    
    /**
     * Sets this user's businessAddressLine2 attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressLine2 The businessAddressLine2 to set.
     */
    public final void setBusinessAddressLine2(String businessAddressLine2) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_LINE_2, businessAddressLine2);
        }
    }
    
    /**
     * Sets this user's businessAddressCity attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressCity The businessAddressCity to set.
     */
    public final void setBusinessAddressCity(String businessAddressCity) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_CITY, businessAddressCity);
        }
    }
    
    /**
     * Sets this user's businessAddressProvince attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressProvince The businessAddressProvince to set.
     */
    public final void setBusinessAddressProvince(String businessAddressProvince) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_PROVINCE, businessAddressProvince);
        }
    }
    
    /**
     * Sets this user's businessAddressCountry attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressCountry The businessAddressCountry to set.
     */
    public final void setBusinessAddressCountry(String businessAddressCountry) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_COUNTRY, businessAddressCountry);
        }
    }
    
    /**
     * Sets this user's businessAddressPostalCode attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressPostalCode The businessAddressPostalCode to set.
     */
    public final void setBusinessAddressPostalCode(String businessAddressPostalCode) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_POSTAL_CODE, businessAddressPostalCode);
        }
    }
    
    /**
     * Sets this user's businessAddressUnstructured attribute to the given value. If
     * this user object is read-only, a runtime exception is thrown.
     * @param businessAddressUnstructured The businessAddressUnstructured to set.
     */
    public final void setBusinessAddressUnstructured(String businessAddressUnstructured) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(BUSINESS_ADDRESS_UNSTRUCTURED, businessAddressUnstructured);
        }
    }

    /**
     * Clones the WebADEUserInfo object. The cloned object will have the
     * isReadOnly() flag set to false, making it editable.
     */
    @Override
	public Object clone() {
        DefaultBusinessPartnerUserInfo info = new DefaultBusinessPartnerUserInfo(this);
        return info;
    }

    @Override
	public String toString() {
        String newLine = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString()).append(newLine);
        sb.append("Business GUID: ").append(getBusinessGUID()).append(newLine);
        sb.append("Business LUID: ").append(getBusinessLUID()).append(newLine);
        sb.append("Business Legal Name: ").append(getBusinessLegalName()).append(newLine);
        sb.append("Business Trading Name: ").append(getBusinessTradingName()).append(newLine);
        sb.append("Business Activation Code: ").append(getBusinessActivationCode()).append(newLine);
        sb.append("Business Number: ").append(getBusinessNumber()).append(newLine);
        sb.append("Is Business Number Verified: ").append(isBusinessNumberVerified()).append(newLine);
        sb.append("Incorporation Number: ").append(getIncorporationNumber()).append(newLine);
        sb.append("Extra Provincial Registration Number: ").append(getExtraProvincialRegistrationNumber()).append(newLine);
        sb.append("Business Type Code: ").append(getBusinessTypeCode()).append(newLine);
        sb.append("Business Type Other: ").append(getBusinessTypeOther()).append(newLine);
        sb.append("BN Hub Business Type Code: ").append(getBnHubBusinessTypeCode()).append(newLine);
        sb.append("Statement of Registration Number: ").append(getStatementOfRegistrationNumber()).append(newLine);
        sb.append("Jurisdiction of Incorporation: ").append(getJurisdictionOfIncorporation()).append(newLine);
        sb.append("Doing Business As: ").append(getDoingBusinessAs()).append(newLine);
        sb.append("Is Suspended: ").append(isSuspended()).append(newLine);
        sb.append("Business Address Line 1: ").append(getBusinessAddressLine1()).append(newLine);
        sb.append("Business Address Line 2: ").append(getBusinessAddressLine2()).append(newLine);
        sb.append("Business Address City: ").append(getBusinessAddressCity()).append(newLine);
        sb.append("Business Address Province: ").append(getBusinessAddressProvince()).append(newLine);
        sb.append("Business Address Country: ").append(getBusinessAddressCountry()).append(newLine);
        sb.append("Business Address Postal Code: ").append(getBusinessAddressPostalCode()).append(newLine);
        sb.append("Business Address Unstructured: ").append(getBusinessAddressUnstructured()).append(newLine);
        return sb.toString();
    }
}
