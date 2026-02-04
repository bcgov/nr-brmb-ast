/**
 * @(#)BusinessPartnerUserInfo.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

/**
 * @author jross
 */
public interface BusinessPartnerUserInfo extends IndividualUserInfo {
    /**
     * The reserved attribute name for the business GUID attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_GUID = "business.user.business.GUID";

    /**
     * The reserved attribute name for the business LUID attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_LUID = "business.user.business.LUID";
    
    /**
     * The reserved attribute name for the business legal name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_LEGAL_NAME = "business.user.business.legal.name";

    /**
     * The reserved attribute name for the business trading name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_TRADING_NAME = "business.user.business.trading.name";

    /**
     * The reserved attribute name for the business activation code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ACTIVATION_CODE = "business.user.business.activation.code";

    /**
     * The reserved attribute name for the business number attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_NUMBER = "business.user.business.number";

    /**
     * The reserved attribute name for the incorporation number attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String INCORPORATION_NUMBER = "business.user.incorporation.number";

    /**
     * The reserved attribute name for the extra provincial registration number
     * attribute. Used by the <code>getAttributeValue</code>,
     * <code>getAttributeNames</code>, and <code>hasAttribute</code>
     * methods.
     */
    public static final String EXTRA_PROVINCIAL_REGISTRATION_NUMBER = "business.user.extra.provincial.registration.number";

    /**
     * The reserved attribute name for the business type code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_TYPE_CODE = "business.user.type.code";
    
    /**
     * The reserved attribute name for the business type other attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_TYPE_OTHER = "business.user.type.other";
    
    /**
     * The reserved attribute name for the bn hub business type code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BN_HUB_BUSINESS_TYPE_CODE = "business.user.bn.hub.business.type.code";
    
    /**
     * The reserved attribute name for the business number verified attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_NUMBER_VERIFIED = "business.user.business.number.verified.flag";
    
    /**
     * The reserved attribute name for the statement of registration attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String STATEMENT_OF_REGISTRATION_NUMBER = "business.user.statement.of.registration.number";
    
    /**
     * The reserved attribute name for the jurisdiction of incorporation attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String JURISDICTION_OF_INCORPORATION = "business.user.jurisdiction.of.incorporation";
    
    /**
     * The reserved attribute name for the doing business as attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String DOING_BUSINESS_AS = "business.user.doing.business.as";
    
    /**
     * The reserved attribute name for the is suspended attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String IS_SUSPENDED = "business.user.is.suspended";

    /**
     * The reserved attribute name for the business address line 1 attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_LINE_1 = "business.user.business.address.line.1";
    
    /**
     * The reserved attribute name for the business address line 2 attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_LINE_2 = "business.user.business.address.line.2";

    /**
     * The reserved attribute name for the business address city attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_CITY = "business.user.business.address.city";
    
    /**
     * The reserved attribute name for the business address province attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_PROVINCE = "business.user.business.address.province";
    
    /**
     * The reserved attribute name for the business address country attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_COUNTRY = "business.user.business.address.country";
    
    /**
     * The reserved attribute name for the business address postal code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_POSTAL_CODE = "business.user.business.address.postal.code";
    
    /**
     * The reserved attribute name for the business address unstructured attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String BUSINESS_ADDRESS_UNSTRUCTURED = "business.user.business.address.unstructured";

    /**
     * @return The user's associated business' GUID.
     */
    public GUID getBusinessGUID();
    
    /**
     * @return The user's associated business' LUID.
     */
    public String getBusinessLUID();

    /**
     * @return The user's associated business' legal name.
     */
    public String getBusinessLegalName();

    /**
     * @return The user's associated business' trading name.
     */
    public String getBusinessTradingName();

    /**
     * @return The user's associated business' activation code.
     */
    public String getBusinessActivationCode();

    /**
     * @return The user's associated business' business number.
     */
    public String getBusinessNumber();
    
    /**
     * @return The user's associated business' business number verified.
     */
    public boolean isBusinessNumberVerified();

    /**
     * @return The user's associated business' incorporation number.
     */
    public String getIncorporationNumber();

    /**
     * @return The user's associated business' extra provincial registration number.
     */
    public String getExtraProvincialRegistrationNumber();
    
    /**
     * @return The user's associated business' business type code.
     */
    public String getBusinessTypeCode();
    
    /**
     * @return The user's associated business' business type other.
     */
    public String getBusinessTypeOther();
    
    /**
     * @return The user's associated business' BN hub business type code.
     */
    public String getBnHubBusinessTypeCode();
    
    /**
     * @return The user's associated business' statement of registration number.
     */
    public String getStatementOfRegistrationNumber();
    
    /**
     * @return The user's associated business' jurisdiction of incorporation.
     */
    public String getJurisdictionOfIncorporation();
    
    /**
     * @return The user's associated business' doing business as.
     */
    public String getDoingBusinessAs();
    
    /**
     * @return The user's associated business' is suspended.
     */
    public boolean isSuspended();
    
    /**
     * @return The user's business address line 1
     */
    public String getBusinessAddressLine1();
    
    /**
     * @return The user's business address line 2
     */
    public String getBusinessAddressLine2();
    
    /**
     * @return The user's business address city
     */
    public String getBusinessAddressCity();
    
    /**
     * @return The user's business address province
     */
    public String getBusinessAddressProvince();
    
    /**
     * @return The user's business address country
     */
    public String getBusinessAddressCountry();
    
    /**
     * @return The user's business address postal code
     */
    public String getBusinessAddressPostalCode();
    
    /**
     * @return The user's unstructured business address
     */
    public String getBusinessAddressUnstructured();
}
