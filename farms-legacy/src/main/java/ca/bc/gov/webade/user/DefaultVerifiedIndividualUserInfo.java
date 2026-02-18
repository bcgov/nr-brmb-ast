package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultVerifiedIndividualUserInfo extends DefaultIndividualUserInfo
		implements VerifiedIndividualUserInfo {

	private static final long serialVersionUID = -5617779585611342121L;

	private static final String[] USER_ATTRIBUTES = new String[] { };

	private HashMap<String,Object> userAttributes = new HashMap<String,Object>();

	/**
	 * Default Constructor.
	 */
	public DefaultVerifiedIndividualUserInfo() {
		super(new UserCredentials(UserTypeCode.VERIFIED_INDIVIDUAL));
	}

	/**
	 * Copy constructor. Copies all user details from the given object to this
	 * instance.
	 * 
	 * @param user
	 *            The user object to copy.
	 */
	@SuppressWarnings("unchecked")
	public DefaultVerifiedIndividualUserInfo(
			DefaultVerifiedIndividualUserInfo user) {
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
	protected DefaultVerifiedIndividualUserInfo(UserCredentials credentials) {
		super(credentials);
	}

	/**
	 * Clones the WebADEUserInfo object. The cloned object will have the
	 * isReadOnly() flag set to false, making it editable.
	 */
	@Override
	public Object clone() {
		DefaultVerifiedIndividualUserInfo info = new DefaultVerifiedIndividualUserInfo(
				this);
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
}
