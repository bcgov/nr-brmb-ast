package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author jross
 */
public class DefaultIndividualUserInfo extends AbstractSiteMinderUserInfo implements IndividualUserInfo {
    private static final long serialVersionUID = 3816595592958283791L;

	private static final String[] USER_ATTRIBUTES = new String[] {
			DATE_OF_BIRTH, RESIDENTIAL_ADDRESS_LINE_1,
			RESIDENTIAL_ADDRESS_LINE_2, RESIDENTIAL_ADDRESS_CITY,
			RESIDENTIAL_ADDRESS_PROVINCE, RESIDENTIAL_ADDRESS_COUNTRY,
			RESIDENTIAL_ADDRESS_POSTAL_CODE, RESIDENTIAL_ADDRESS_UNSTRUCTURED,
			MAILING_ADDRESS_LINE_1, MAILING_ADDRESS_LINE_2,
			MAILING_ADDRESS_CITY, MAILING_ADDRESS_PROVINCE,
			MAILING_ADDRESS_COUNTRY, MAILING_ADDRESS_POSTAL_CODE,
			MAILING_ADDRESS_UNSTRUCTURED };

	private HashMap<String,Object> userAttributes = new HashMap<String,Object>();

    /**
     * Default Constructor.
     */
    public DefaultIndividualUserInfo() {
        super(new UserCredentials(UserTypeCode.INDIVIDUAL));
    }

    /**
     * Copy constructor. Copies all user details from the given object to this
     * instance.
     * 
     * @param user
     *            The user object to copy.
     */
    @SuppressWarnings("unchecked")
	public DefaultIndividualUserInfo(DefaultIndividualUserInfo user) {
        super(user);
		this.userAttributes = (HashMap<String,Object>) user.userAttributes.clone();
    }

    /**
     * Basic constructor. Subclasses should pass in an instance of the
     * appropriate credentials class.
     * 
     * @param credentials
     *            The appropriate user credentials class.
     */
    protected DefaultIndividualUserInfo(UserCredentials credentials) {
        super(credentials);
    }

    /**
     * Clones the WebADEUserInfo object. The cloned object will have the
     * isReadOnly() flag set to false, making it editable.
     */
    @Override
	public Object clone() {
        DefaultIndividualUserInfo info = new DefaultIndividualUserInfo(this);
        return info;
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
     * @see ca.bc.gov.webade.user.WebADEUserInfo#getInitials()
     */
    @Override
	public Date getDateOfBirth() {
        return (Date) this.userAttributes.get(DATE_OF_BIRTH);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressLine1()
	 */
	@Override
	public String getResidentialAddressLine1() {
		return (String) this.userAttributes.get(RESIDENTIAL_ADDRESS_LINE_1);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressLine2()
	 */
	@Override
	public String getResidentialAddressLine2() {
		return (String) this.userAttributes.get(RESIDENTIAL_ADDRESS_LINE_2);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressCity()
	 */
	@Override
	public String getResidentialAddressCity() {
		return (String) this.userAttributes.get(RESIDENTIAL_ADDRESS_CITY);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressProvince()
	 */
	@Override
	public String getResidentialAddressProvince() {
		return (String) this.userAttributes.get(RESIDENTIAL_ADDRESS_PROVINCE);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressCountry()
	 */
	@Override
	public String getResidentialAddressCountry() {
		return (String) this.userAttributes.get(RESIDENTIAL_ADDRESS_COUNTRY);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressPostalCode()
	 */
	@Override
	public String getResidentialAddressPostalCode() {
		return (String) this.userAttributes
				.get(RESIDENTIAL_ADDRESS_POSTAL_CODE);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getResidentialAddressUnstructured()
	 */
	@Override
	public String getResidentialAddressUnstructured() {
		return (String) this.userAttributes
				.get(RESIDENTIAL_ADDRESS_UNSTRUCTURED);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressLine1()
	 */
	@Override
	public String getMailingAddressLine1() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_LINE_1);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressLine2()
	 */
	@Override
	public String getMailingAddressLine2() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_LINE_2);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressCity()
	 */
	@Override
	public String getMailingAddressCity() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_CITY);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressProvince()
	 */
	@Override
	public String getMailingAddressProvince() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_PROVINCE);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressCountry()
	 */
	@Override
	public String getMailingAddressCountry() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_COUNTRY);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressPostalCode()
	 */
	@Override
	public String getMailingAddressPostalCode() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_POSTAL_CODE);
	}

	/**
	 * @see ca.bc.gov.webade.user.VerifiedIndividualUserInfo#getMailingAddressUnstructured()
	 */
	@Override
	public String getMailingAddressUnstructured() {
		return (String) this.userAttributes.get(MAILING_ADDRESS_UNSTRUCTURED);
	}

	public final void setDateOfBirth(Date dateOfBirth) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
        	this.userAttributes.put(DATE_OF_BIRTH, dateOfBirth);
        }
	}

	/**
	 * Sets this user's residentialAddressLine1 attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressLine1
	 *            The residentialAddressLine1 to set.
	 */
	public final void setResidentialAddressLine1(String residentialAddressLine1) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_LINE_1,
					residentialAddressLine1);
		}
	}

	/**
	 * Sets this user's residentialAddressLine2 attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressLine2
	 *            The residentialAddressLine2 to set.
	 */
	public final void setResidentialAddressLine2(String residentialAddressLine2) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_LINE_2,
					residentialAddressLine2);
		}
	}

	/**
	 * Sets this user's residentialAddressCity attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressCity
	 *            The residentialAddressCity to set.
	 */
	public final void setResidentialAddressCity(String residentialAddressCity) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_CITY,
					residentialAddressCity);
		}
	}

	/**
	 * Sets this user's residentialAddressProvince attribute to the given value.
	 * If this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressProvince
	 *            The residentialAddressProvince to set.
	 */
	public final void setResidentialAddressProvince(
			String residentialAddressProvince) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_PROVINCE,
					residentialAddressProvince);
		}
	}

	/**
	 * Sets this user's residentialAddressCountry attribute to the given value.
	 * If this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressCountry
	 *            The residentialAddressCountry to set.
	 */
	public final void setResidentialAddressCountry(
			String residentialAddressCountry) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_COUNTRY,
					residentialAddressCountry);
		}
	}

	/**
	 * Sets this user's residentialAddressPostalCode attribute to the given
	 * value. If this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressPostalCode
	 *            The residentialAddressPostalCode to set.
	 */
	public final void setResidentialAddressPostalCode(
			String residentialAddressPostalCode) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_POSTAL_CODE,
					residentialAddressPostalCode);
		}
	}

	/**
	 * Sets this user's residentialAddressUnstructured attribute to the given
	 * value. If this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param residentialAddressUnstructured
	 *            The residentialAddressUnstructured to set.
	 */
	public final void setResidentialAddressUnstructured(
			String residentialAddressUnstructured) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(RESIDENTIAL_ADDRESS_UNSTRUCTURED,
					residentialAddressUnstructured);
		}
	}

	/**
	 * Sets this user's mailingAddressLine1 attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressLine1
	 *            The mailingAddressLine1 to set.
	 */
	public final void setMailingAddressLine1(String mailingAddressLine1) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes
					.put(MAILING_ADDRESS_LINE_1, mailingAddressLine1);
		}
	}

	/**
	 * Sets this user's mailingAddressLine2 attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressLine2
	 *            The mailingAddressLine2 to set.
	 */
	public final void setMailingAddressLine2(String mailingAddressLine2) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes
					.put(MAILING_ADDRESS_LINE_2, mailingAddressLine2);
		}
	}

	/**
	 * Sets this user's mailingAddressCity attribute to the given value. If this
	 * user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressCity
	 *            The mailingAddressCity to set.
	 */
	public final void setMailingAddressCity(String mailingAddressCity) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(MAILING_ADDRESS_CITY, mailingAddressCity);
		}
	}

	/**
	 * Sets this user's mailingAddressProvince attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressProvince
	 *            The mailingAddressProvince to set.
	 */
	public final void setMailingAddressProvince(String mailingAddressProvince) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(MAILING_ADDRESS_PROVINCE,
					mailingAddressProvince);
		}
	}

	/**
	 * Sets this user's mailingAddressCountry attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressCountry
	 *            The mailingAddressCountry to set.
	 */
	public final void setMailingAddressCountry(String mailingAddressCountry) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(MAILING_ADDRESS_COUNTRY,
					mailingAddressCountry);
		}
	}

	/**
	 * Sets this user's mailingAddressPostalCode attribute to the given value.
	 * If this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressPostalCode
	 *            The mailingAddressPostalCode to set.
	 */
	public final void setMailingAddressPostalCode(
			String mailingAddressPostalCode) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(MAILING_ADDRESS_POSTAL_CODE,
					mailingAddressPostalCode);
		}
	}

	/**
	 * Sets this user's mailingAddressUnstructured attribute to the given value.
	 * If this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param mailingAddressUnstructured
	 *            The mailingAddressUnstructured to set.
	 */
	public final void setMailingAddressUnstructured(
			String mailingAddressUnstructured) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(MAILING_ADDRESS_UNSTRUCTURED,
					mailingAddressUnstructured);
		}
	}

	@Override
	public String toString() {
		String newLine = "\n";
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString()).append(newLine);
		sb.append("Date of Birth: ").append(getDateOfBirth()).append(newLine);
		sb.append("Residential Address Line 1: ")
				.append(getResidentialAddressLine1()).append(newLine);
		sb.append("Residential Address Line 2: ")
				.append(getResidentialAddressLine2()).append(newLine);
		sb.append("Residential Address City: ")
				.append(getResidentialAddressCity()).append(newLine);
		sb.append("Residential Address Province: ")
				.append(getResidentialAddressProvince()).append(newLine);
		sb.append("Residential Address Country: ")
				.append(getResidentialAddressCountry()).append(newLine);
		sb.append("Residential Address Postal Code: ")
				.append(getResidentialAddressPostalCode()).append(newLine);
		sb.append("Residential Address Unstructured: ")
				.append(getResidentialAddressUnstructured()).append(newLine);
		sb.append("Mailing Address Line 1: ").append(getMailingAddressLine1())
				.append(newLine);
		sb.append("Mailing Address Line 2: ").append(getMailingAddressLine2())
				.append(newLine);
		sb.append("Mailing Address City: ").append(getMailingAddressCity())
				.append(newLine);
		sb.append("Mailing Address Province: ")
				.append(getMailingAddressProvince()).append(newLine);
		sb.append("Mailing Address Country: ")
				.append(getMailingAddressCountry()).append(newLine);
		sb.append("Mailing Address Postal Code: ")
				.append(getMailingAddressPostalCode()).append(newLine);
		sb.append("Mailing Address Unstructured: ")
				.append(getMailingAddressUnstructured()).append(newLine);
		return sb.toString();
	}
}
