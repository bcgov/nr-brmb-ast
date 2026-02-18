/**
 * @(#)IndividualUserInfo.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

import java.util.Date;

/**
 * @author jross
 */
public interface IndividualUserInfo extends SiteMinderUserInfo {

    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String DATE_OF_BIRTH = "individual.user.date.of.birth";

    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_LINE_1 = "individual.user.residential.address.line.1";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_LINE_2 = "individual.user.residential.address.line.2";

    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_CITY = "individual.user.residential.address.city";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_PROVINCE = "individual.user.residential.address.province";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_COUNTRY = "individual.user.residential.address.country";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_POSTAL_CODE = "individual.user.residential.address.postal.code";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String RESIDENTIAL_ADDRESS_UNSTRUCTURED = "individual.user.residential.address.unstructured";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_LINE_1 = "individual.user.mailing.address.line.1";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_LINE_2 = "individual.user.mailing.address.line.2";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_CITY = "individual.user.mailing.address.city";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_PROVINCE = "individual.user.mailing.address.province";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_COUNTRY = "individual.user.mailing.address.country";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_POSTAL_CODE = "individual.user.mailing.address.postal.code";
    
    /**
     * The reserved attribute name for the account type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MAILING_ADDRESS_UNSTRUCTURED = "individual.user.mailing.address.unstructured";

    /**
     * @return Returns the dateOfBirth.
     */
    public Date getDateOfBirth();
    
    /**
     * @return The user's residential address line 1
     */
    public String getResidentialAddressLine1();
    
    /**
     * @return The user's residential address line 2
     */
    public String getResidentialAddressLine2();
    
    /**
     * @return The user's residential address city
     */
    public String getResidentialAddressCity();
    
    /**
     * @return The user's residential address province
     */
    public String getResidentialAddressProvince();
    
    /**
     * @return The user's residential address country
     */
    public String getResidentialAddressCountry();
    
    /**
     * @return The user's residential address postal code
     */
    public String getResidentialAddressPostalCode();
    
    /**
     * @return The user's unstructured residential address
     */
    public String getResidentialAddressUnstructured();
    
    /**
     * @return The user's mailing address line 1
     */
    public String getMailingAddressLine1();
    
    /**
     * @return The user's mailing address line 2
     */
    public String getMailingAddressLine2();
    
    /**
     * @return The user's mailing address city
     */
    public String getMailingAddressCity();
    
    /**
     * @return The user's mailing address province
     */
    public String getMailingAddressProvince();
    
    /**
     * @return The user's mailing address country
     */
    public String getMailingAddressCountry();
    
    /**
     * @return The user's mailing address postal code
     */
    public String getMailingAddressPostalCode();
    
    /**
     * @return The user's unstructured mailing address
     */
    public String getMailingAddressUnstructured();
}
