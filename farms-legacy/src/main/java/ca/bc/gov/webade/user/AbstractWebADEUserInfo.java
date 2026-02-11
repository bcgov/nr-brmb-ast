package ca.bc.gov.webade.user;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Default implementation of the WebADEUserInfo. Each setter can only be called
 * once, after which the set methods will throw a RuntimeException.
 * @author jason
 */
public abstract class AbstractWebADEUserInfo implements WebADEUserInfo, Serializable {

    private static final long serialVersionUID = -3693181911237559061L;

    protected static final Date MAX_DATE;
    static {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 9999);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        MAX_DATE = cal.getTime();
    }
    private static final String[] USER_ATTRIBUTES = new String[] {
            USER_CREDENTIALS,
            DISPLAY_NAME,
            LAST_NAME,
            FIRST_NAME,
            MIDDLE_INITIAL,
            MIDDLE_NAME,
            OTHER_MIDDLE_NAME,
            INITIALS,
            EMAIL_ADDRESS,
            PHONE_NUMBER,
            PREFERRED_NAME,
            DEPARTMENT,
            CONTACT_ADDRESS_LINE_1,
            CONTACT_ADDRESS_LINE_2,
            CONTACT_ADDRESS_CITY,
            CONTACT_ADDRESS_PROVINCE,
            CONTACT_ADDRESS_COUNTRY,
            CONTACT_ADDRESS_POSTAL_CODE,
            CONTACT_ADDRESS_UNSTRUCTURED,
            IS_VISIBLE, CONTACT_PREFERENCE_TYPE };

    private HashMap<String,Object> userAttributes = new HashMap<String,Object>();
    private boolean isReadOnly = false;

    /**
     * Default Constructor.
     */
    public AbstractWebADEUserInfo() {
    }

    /**
     * User Credentials Constructor. Creates an empty user object with the given
     * user credentials.
     * @param userCredentials
     *            The user credentials to set for this user.
     */
    public AbstractWebADEUserInfo(UserCredentials userCredentials) {
        setUserCredentials(userCredentials);
    }

    /**
     * Copy constructor. Copies all user details from the given object to this
     * instance.
     * @param user
     *            The user object to copy.
     */
    @SuppressWarnings("unchecked")
	public AbstractWebADEUserInfo(AbstractWebADEUserInfo user) {
        this.userAttributes = (HashMap<String,Object>) user.userAttributes.clone();
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getUserCredentials()
     */
    @Override
	public final UserCredentials getUserCredentials() {
        return (UserCredentials) this.userAttributes.get(USER_CREDENTIALS);
    }	

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getDisplayName()
     */
    @Override
	public final String getDisplayName() {
        return (String) this.userAttributes.get(DISPLAY_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getLastName()
     */
    @Override
	public final String getLastName() {
        return (String) this.userAttributes.get(LAST_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getFirstName()
     */
    @Override
	public final String getFirstName() {
        return (String) this.userAttributes.get(FIRST_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getMiddleInitial()
     */
    @Override
	public final String getMiddleInitial() {
        return (String) this.userAttributes.get(MIDDLE_INITIAL);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getMiddleName()
     */
    @Override
	public final String getMiddleName() {
        return (String) this.userAttributes.get(MIDDLE_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getOtherMiddleName()
     */
    @Override
	public String getOtherMiddleName() {
        return (String) this.userAttributes.get(OTHER_MIDDLE_NAME);
	}

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getInitials()
     */
    @Override
	public String getInitials() {
        return (String) this.userAttributes.get(INITIALS);
	}

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getEmailAddress()
     */
    @Override
	public final String getEmailAddress() {
        return (String) this.userAttributes.get(EMAIL_ADDRESS);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getPhoneNumber()
     */
    @Override
	public final String getPhoneNumber() {
        return (String) this.userAttributes.get(PHONE_NUMBER);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getPreferredName()
     */
    @Override
	public final String getPreferredName() {
        return (String) this.userAttributes.get(PREFERRED_NAME);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getDepartment()
     */
    @Override
	public final String getDepartment() {
        return (String) this.userAttributes.get(DEPARTMENT);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressLine1()
     */
    @Override
	public final String getContactAddressLine1() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_LINE_1);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressLine2()
     */
    @Override
	public final String getContactAddressLine2() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_LINE_2);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressCity()
     */
    @Override
	public final String getContactAddressCity() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_CITY);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressProvince()
     */
    @Override
	public final String getContactAddressProvince() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_PROVINCE);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressCountry()
     */
    @Override
	public final String getContactAddressCountry() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_COUNTRY);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressPostalCode()
     */
    @Override
	public final String getContactAddressPostalCode() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_POSTAL_CODE);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactAddressUnstructured()
     */
    @Override
	public final String getContactAddressUnstructured() {
        return (String) this.userAttributes.get(CONTACT_ADDRESS_UNSTRUCTURED);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getContactPreferenceType()
     */
    @Override
	public String getContactPreferenceType() {
        return (String) this.userAttributes.get(CONTACT_PREFERENCE_TYPE);
	}

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#isVisible()
     */
    @Override
	public final boolean isVisible() {
        return (this.userAttributes.get(IS_VISIBLE) != null) ? ((Boolean) this.userAttributes
                .get(IS_VISIBLE)).booleanValue()
                : false;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getAttributeNames()
     */
    @Override
	public String[] getAttributeNames() {
        return USER_ATTRIBUTES;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getAttributeValue(java.lang.String)
     */
    @Override
	public Object getAttributeValue(String attributeName) {
        return this.userAttributes.get(attributeName);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserInfo#hasAttribute(java.lang.String)
     */
    @Override
	public boolean hasAttribute(String attributeName) {
        boolean foundAttr = false;
        for (int i = 0; i < USER_ATTRIBUTES.length; i++) {
            if (USER_ATTRIBUTES[i].equals(attributeName)) {
                foundAttr = true;
                break;
            }
        }
        return foundAttr;
    }

    /**
     * Sets this user's displayName attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param displayName
     *            The displayName to set.
     */
    public final void setDisplayName(String displayName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(DISPLAY_NAME, displayName);
        }
    }

    /**
     * Sets this user's emailAddress attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param emailAddress
     *            The emailAddress to set.
     */
    public final void setEmailAddress(String emailAddress) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(EMAIL_ADDRESS, emailAddress);
        }
    }

    /**
     * Sets this user's firstName attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param firstName
     *            The firstName to set.
     */
    public final void setFirstName(String firstName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(FIRST_NAME, firstName);
        }
    }

    /**
     * Sets this user's lastName attribute to the given value. If this 
     * user object is read-only, a runtime exception is thrown.
     * @param lastName
     *            The lastName to set.
     */
    public final void setLastName(String lastName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(LAST_NAME, lastName);
        }
    }

    /**
     * Sets this user's middleInitial attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param middleInitial
     *            The middleInitial to set.
     */
    public final void setMiddleInitial(String middleInitial) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            String initial = middleInitial;
            if (middleInitial != null && middleInitial.length() > 1) {
                initial = middleInitial.substring(0, 1);
            }
            this.userAttributes.put(MIDDLE_INITIAL, initial);
        }
    }

    /**
     * Sets this user's middleName attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param middleName
     *            The middleName to set.
     */
    public final void setMiddleName(String middleName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
        	this.userAttributes.put(MIDDLE_NAME, middleName);
        }
    }

    /**
     * Sets this user's phoneNumber attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param phoneNumber
     *            The phoneNumber to set.
     */
    public final void setPhoneNumber(String phoneNumber) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            phoneNumber = WebADEUserInfoUtils.stripNonNumericCharacters(phoneNumber);
            if (phoneNumber != null && phoneNumber.length() > 10) {
                phoneNumber = phoneNumber.substring(0, 10);
            }
            this.userAttributes.put(PHONE_NUMBER, phoneNumber);
        }
    }
    
    /**
     * Sets this user's preferredName attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param preferredName The preferredName to set.
     */
    public final void setPreferredName(String preferredName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(PREFERRED_NAME, preferredName);
        }
    }

	public void setInitials(String initials) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(INITIALS, initials);
        }
	}

	public void setOtherMiddleName(String otherMiddleName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(OTHER_MIDDLE_NAME, otherMiddleName);
        }
	}

	public void setContactPreferenceType(String contactPreferenceType) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_PREFERENCE_TYPE, contactPreferenceType);
        }
	}
    
    /**
     * Sets this user's department attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param department The department to set.
     */
    public final void setDepartment(String department) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(DEPARTMENT, department);
        }
    }
    
    /**
     * Sets this user's contactAddressLine1 attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressLine1 The contactAddressLine1 to set.
     */
    public final void setContactAddressLine1(String contactAddressLine1) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_LINE_1, contactAddressLine1);
        }
    }
    
    /**
     * Sets this user's contactAddressLine2 attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressLine2 The contactAddressLine2 to set.
     */
    public final void setContactAddressLine2(String contactAddressLine2) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_LINE_2, contactAddressLine2);
        }
    }
    
    /**
     * Sets this user's contactAddressCity attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressCity The contactAddressCity to set.
     */
    public final void setContactAddressCity(String contactAddressCity) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_CITY, contactAddressCity);
        }
    }
    
    /**
     * Sets this user's contactAddressProvince attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressProvince The contactAddressProvince to set.
     */
    public final void setContactAddressProvince(String contactAddressProvince) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_PROVINCE, contactAddressProvince);
        }
    }
    
    /**
     * Sets this user's contactAddressCountry attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressCountry The contactAddressCountry to set.
     */
    public final void setContactAddressCountry(String contactAddressCountry) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_COUNTRY, contactAddressCountry);
        }
    }
    
    /**
     * Sets this user's contactAddressPostalCode attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressPostalCode The contactAddressPostalCode to set.
     */
    public final void setContactAddressPostalCode(String contactAddressPostalCode) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_POSTAL_CODE, contactAddressPostalCode);
        }
    }
    
    /**
     * Sets this user's contactAddressUnstructured attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param contactAddressUnstructured The contactAddressUnstructured to set.
     */
    public final void setContactAddressUnstructured(String contactAddressUnstructured) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(CONTACT_ADDRESS_UNSTRUCTURED, contactAddressUnstructured);
        }
    }

    /**
     * Sets this instance's user credentials to the given settings. If this
     * instance's credentials are already set but incomplete, the given user
     * credentials will be merged in with any existing settings. If this
     * instance's credentials are already set and complete, a runtime exception
     * is thrown.
     * @param userCredentials
     *            The user's credentials to set.
     */
    public final void setUserCredentials(UserCredentials userCredentials) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } else if (this.userAttributes.get(USER_CREDENTIALS) != null) {
            UserCredentials existingCreds = (UserCredentials) this.userAttributes
                    .get(USER_CREDENTIALS);
            userCredentials.mergeCredentials(existingCreds);
            this.userAttributes.put(USER_CREDENTIALS, userCredentials);
        } else {
            this.userAttributes.put(USER_CREDENTIALS, userCredentials);
        }
    }

    /**
     * Sets this user's visible flag attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param visible
     *            The visible to set.
     */
    public final void setVisible(boolean visible) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(IS_VISIBLE, new Boolean(visible));
        }
    }

    /**
     * Sets the attributes of this object are non-editable. Used to prevent an
     * application from editing the attributes of the session user object.
     */
    @Override
	public final void setReadOnly() {
        this.isReadOnly = true;
        UserCredentials creds = getUserCredentials();
        if (creds != null) {
            creds.setReadOnly();
        }
    }

    /**
     * @return True is the attributes of this object are non-editable.
     */
    @Override
	public final boolean isReadOnly() {
        return this.isReadOnly;
    }

    /**
     * Clones the WebADEUserInfo object. The cloned object will have the
     * isReadOnly() flag set to false, making it editable.
     */
    @Override
	public abstract Object clone();

    @Override
	public String toString() {
        String newLine = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("User Credentials: ").append(getUserCredentials().toString()).append(newLine);
        sb.append("Display Name: ").append(getDisplayName()).append(newLine);
        sb.append("Last Name: ").append(getLastName()).append(newLine);
        sb.append("First Name: ").append(getFirstName()).append(newLine);
        sb.append("Middle Initial: ").append(getMiddleInitial()).append(newLine);
        sb.append("Email Address: ").append(getEmailAddress()).append(newLine);
        sb.append("Phone Number: ").append(getPhoneNumber()).append(newLine);
        sb.append("Preferred Name: ").append(getPreferredName()).append(newLine);
        sb.append("Department: ").append(getDepartment()).append(newLine);
        sb.append("Contact Address Line 1: ").append(getContactAddressLine1()).append(newLine);
        sb.append("Contact Address Line 2: ").append(getContactAddressLine2()).append(newLine);
        sb.append("Contact Address City: ").append(getContactAddressCity()).append(newLine);
        sb.append("Contact Address Province: ").append(getContactAddressProvince()).append(newLine);
        sb.append("Contact Address Country: ").append(getContactAddressCountry()).append(newLine);
        sb.append("Contact Address Postal Code: ").append(getContactAddressPostalCode()).append(newLine);
        sb.append("Contact Address Unstructured: ").append(getContactAddressUnstructured()).append(newLine);
        sb.append("Visible: ").append(isVisible());
        return sb.toString();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        boolean areEqual = false;
        if (obj instanceof WebADEUserInfo) {
            WebADEUserInfo info = (WebADEUserInfo) obj;
            UserCredentials thisCreds = getUserCredentials();
            if (thisCreds == null) {
                areEqual = (obj == this);
            } else {
                areEqual = (thisCreds.equals(info.getUserCredentials()));
            }
        }
        return areEqual;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        UserCredentials creds = getUserCredentials();
        return (creds == null) ? super.hashCode() : creds.hashCode();
    }

}
