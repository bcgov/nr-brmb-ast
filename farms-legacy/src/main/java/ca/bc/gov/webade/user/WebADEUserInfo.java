/**
 * @(#)WebADEUserInfo.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;


/**
 * This interface defines the basic attributes and operations WebADE maintains
 * about any user accessible via the configured user-info providers of the
 * WebADE application.
 * @author jross
 */
public interface WebADEUserInfo {
	
    /**
     * The reserved attribute name for the user credentials attribute. Used by
     * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
     * and <code>hasAttribute</code> methods.
     */
    public static final String USER_CREDENTIALS = "webade.user.credentials";

    /**
     * The reserved attribute name for the display name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String DISPLAY_NAME = "webade.user.display.name";

    /**
     * The reserved attribute name for the last name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String LAST_NAME = "webade.user.last.name";

    /**
     * The reserved attribute name for the first name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String FIRST_NAME = "webade.user.first.name";

    /**
     * The reserved attribute name for the middle initial attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MIDDLE_INITIAL = "webade.user.middle.initial";

    /**
     * The reserved attribute name for the middle name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String MIDDLE_NAME = "webade.user.middle.name";

    /**
     * The reserved attribute name for the email address attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String EMAIL_ADDRESS = "webade.user.email.address";

    /**
     * The reserved attribute name for the phone number attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String PHONE_NUMBER = "webade.user.phone.number";
    
    /**
     * The reserved attribute name for the preferred name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String PREFERRED_NAME = "webade.user.preferred.name";

    /**
     * The reserved attribute name for the contact preference type attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_PREFERENCE_TYPE = "webade.user.contact.preference.type";

    /**
     * The reserved attribute name for the other middle name attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String OTHER_MIDDLE_NAME = "webade.user.other.middle.name";

    /**
     * The reserved attribute name for the initials attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String INITIALS = "webade.user.initials";
    
    /**
     * The reserved attribute name for the department attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String DEPARTMENT = "webade.user.department";
    
    /**
     * The reserved attribute name for the contact address line 1 attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_LINE_1 = "webade.user.contact.address.line.1";
    
    /**
     * The reserved attribute name for the contact address line 2 attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_LINE_2 = "webade.user.contact.address.line.2";
    
    /**
     * The reserved attribute name for the contact address city attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_CITY = "webade.user.contact.address.city";
    
    /**
     * The reserved attribute name for the contact address province attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_PROVINCE = "webade.user.contact.address.province";
    
    /**
     * The reserved attribute name for the contact address country attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_COUNTRY = "webade.user.contact.address.country";
    
    /**
     * The reserved attribute name for the contact address postal code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_POSTAL_CODE = "webade.user.contact.address.postal.code";
    
    /**
     * The reserved attribute name for the contact address unstructured attribute (for foreign address).
     * Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String CONTACT_ADDRESS_UNSTRUCTURED = "webade.user.contact.address.unstructured";

    /**
     * The reserved attribute name for the is-visible attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String IS_VISIBLE = "webade.user.is.visible";
    
    /**
     * @return Returns the user's credentials.
     */
    public UserCredentials getUserCredentials();

    /**
     * @return Returns the user's display name.
     */
    public String getDisplayName();

    /**
     * @return Returns the lastName.
     */
    public String getLastName();

    /**
     * @return Returns the firstName.
     */
    public String getFirstName();

    /**
     * @return Returns the middleInitial.
     */
    public String getMiddleInitial();

    /**
     * @return Returns the middleName.
     */
    public String getMiddleName();

    /**
     * @return Returns the otherMiddleName.
     */
    public String getOtherMiddleName();

    /**
     * @return Returns the initials.
     */
    public String getInitials();

    /**
     * @return Returns the emailAddress.
     */
    public String getEmailAddress();

    /**
     * @return Returns the phoneNumber.
     */
    public String getPhoneNumber();
    
    /**
     * @return Returns the contactAddressLine1.
     */
    public String getContactAddressLine1();
    
    /**
     * @return Returns the contactAddressLine2.
     */
    public String getContactAddressLine2();
    
    /**
     * @return Returns the contactAddressCity.
     */
    public String getContactAddressCity();
    
    /**
     * @return Returns the contactAddressProvince.
     */
    public String getContactAddressProvince();
    
    /**
     * @return Returns the contactAddressCountry.
     */
    public String getContactAddressCountry();
    
    /**
     * @return Returns the contactAddressPostalCode.
     */
    public String getContactAddressPostalCode();
    
    /**
     * @return Returns the contactAddressUnstructured for foreign addresses (outside Canada and USA).
     */
    public String getContactAddressUnstructured();

    /**
     * @return Returns the contactPreferenceType.
     */
    public String getContactPreferenceType();
    
    /**
     * @return Returns the preferredName.
     */
    public String getPreferredName();
    
    /**
     * @return Returns the department.
     */
    public String getDepartment();

    /**
     * @return Returns true if this user object's information is visible to the
     *         requesting user.
     */
    public boolean isVisible();

    /**
     * Returns the value for the attribute with the given name.
     * @param attributeName
     *            The name constant assigned to the user attribute.
     * @return The attribute value, or null if the attribute is not found.
     */
    public Object getAttributeValue(String attributeName);

    /**
     * Returns the set of attribute names for user attributes supported by this
     * object instance.
     * @return The array of attribute name constants.
     */
    public String[] getAttributeNames();

    /**
     * Returns whether this object instance supports the attribute with the
     * given name.
     * @param attributeName
     *            The name constant assigned to the target user attribute.
     * @return True if the attribute name is supported, otherwise false.
     */
    public boolean hasAttribute(String attributeName);

    /**
     * Sets the attributes of this object are non-editable. Used to prevent an
     * application from editing the attributes of the session user object.
     */
    public void setReadOnly();

    /**
     * @return True is the attributes of this object are non-editable.
     */
    public boolean isReadOnly();

    /**
     * Clones the WebADEUserInfo object. The cloned object will have the
     * isReadOnly() flag set to false, making it editable.
     */
    public Object clone();
}
