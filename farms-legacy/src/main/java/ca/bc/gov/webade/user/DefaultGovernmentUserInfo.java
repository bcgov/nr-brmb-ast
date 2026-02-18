package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jross
 */
public class DefaultGovernmentUserInfo extends DefaultIndividualUserInfo implements
		GovernmentUserInfo {
	private static final long serialVersionUID = 130350779745700850L;
	private static final String[] USER_ATTRIBUTES = new String[] { EMPLOYEE_ID,
			CITY, TITLE, COMPANY, ORGANIZATION_CODE, GOVERNMENT_DEPARTMENT,
			OFFICE, DESCRIPTION };
	private HashMap<String,Object> userAttributes = new HashMap<String,Object>();

	/**
	 * Default Constructor.
	 */
	public DefaultGovernmentUserInfo() {
		super(new UserCredentials(UserTypeCode.GOVERNMENT));
	}

	/**
	 * Copy constructor. Copies all user details from the given object to this
	 * instance.
	 * 
	 * @param user
	 *            The user object to copy.
	 */
	@SuppressWarnings("unchecked")
	public DefaultGovernmentUserInfo(DefaultGovernmentUserInfo user) {
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
	protected DefaultGovernmentUserInfo(UserCredentials credentials) {
		super(credentials);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getEmployeeId()
	 */
	@Override
	public final String getEmployeeId() {
		return (String) this.userAttributes.get(EMPLOYEE_ID);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#isEmployee()
	 */
	@Override
	public boolean isEmployee() {
		return (getEmployeeId() != null);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getCity()
	 */
	@Override
	public String getCity() {
		return (String) this.userAttributes.get(CITY);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getTitle()
	 */
	@Override
	public String getTitle() {
		return (String) this.userAttributes.get(TITLE);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getCompany()
	 */
	@Override
	public String getCompany() {
		return (String) this.userAttributes.get(COMPANY);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getOrganizationCode()
	 */
	@Override
	public String getOrganizationCode() {
		return (String) this.userAttributes.get(ORGANIZATION_CODE);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getGovernmentDepartment()
	 */
	@Override
	public String getGovernmentDepartment() {
		return (String) this.userAttributes.get(GOVERNMENT_DEPARTMENT);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getCity()
	 */
	@Override
	public String getOffice() {
		return (String) this.userAttributes.get(OFFICE);
	}

	/**
	 * @see ca.bc.gov.webade.user.GovernmentUserInfo#getDescription()
	 */
	@Override
	public String getDescription() {
		return (String) this.userAttributes.get(DESCRIPTION);
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
	 * Sets this user's employeeId attribute to the given value. If this user
	 * object is read-only, a runtime exception is thrown.
	 * 
	 * @param employeeId
	 *            The employeeId to set.
	 */
	public final void setEmployeeId(String employeeId) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(EMPLOYEE_ID, employeeId);
		}
	}

	/**
	 * Sets this user's city attribute to the given value. If this user object
	 * is read-only, a runtime exception is thrown.
	 * 
	 * @param city
	 *            The city to set.
	 */
	public void setCity(String city) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(CITY, city);
		}
	}

	/**
	 * Sets this user's title attribute to the given value. If this user object
	 * is read-only, a runtime exception is thrown.
	 * 
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(TITLE, title);
		}
	}

	/**
	 * Sets this user's company attribute to the given value. If this user
	 * object is read-only, a runtime exception is thrown.
	 * 
	 * @param company
	 *            The company to set.
	 */
	public void setCompany(String company) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(COMPANY, company);
		}
	}

	/**
	 * Sets this user's organizationCode attribute to the given value. If this
	 * user object is read-only, a runtime exception is thrown.
	 * 
	 * @param organizationCode
	 *            The organizationCode to set.
	 */
	public void setOrganizationCode(String organizationCode) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(ORGANIZATION_CODE, organizationCode);
		}
	}

	/**
	 * Sets this user's governmentDepartment attribute to the given value. If
	 * this user object is read-only, a runtime exception is thrown.
	 * 
	 * @param governmentDepartment
	 *            The governmentDepartment to set.
	 */
	public void setGovernmentDepartment(String governmentDepartment) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes
					.put(GOVERNMENT_DEPARTMENT, governmentDepartment);
		}
	}

	/**
	 * Sets this user's office attribute to the given value. If this user object
	 * is read-only, a runtime exception is thrown.
	 * 
	 * @param office
	 *            The office to set.
	 */
	public void setOffice(String office) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(OFFICE, office);
		}
	}

	/**
	 * Sets this user's description attribute to the given value. If this user
	 * object is read-only, a runtime exception is thrown.
	 * 
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		if (isReadOnly()) {
			throw new RuntimeException("Attribute set error: "
					+ "user info is set as read-only.");
		} 

		{
			this.userAttributes.put(DESCRIPTION, description);
		}
	}

	/**
	 * Clones the WebADEUserInfo object. The cloned object will have the
	 * isReadOnly() flag set to false, making it editable.
	 */
	@Override
	public Object clone() {
		DefaultGovernmentUserInfo info = new DefaultGovernmentUserInfo(this);
		return info;
	}

	@Override
	public String toString() {
		String newLine = "\n";
		StringBuffer sb = new StringBuffer();
		sb.append(super.toString()).append(newLine);
		sb.append("Employee Id: ").append(getEmployeeId()).append(newLine);
		sb.append("City: ").append(getCity()).append(newLine);
		sb.append("Title: ").append(getTitle()).append(newLine);
		sb.append("Company: ").append(getCompany()).append(newLine);
		sb.append("Organization Code: ").append(getOrganizationCode())
				.append(newLine);
		sb.append("Government Department: ").append(getGovernmentDepartment())
				.append(newLine);
		sb.append("Office: ").append(getOffice()).append(newLine);
		sb.append("Description: ").append(getDescription());
		return sb.toString();
	}
}
