package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DefaultBcServicesCardUserInfo extends AbstractSiteMinderUserInfo
		implements BcServicesCardUserInfo {

	private static final long serialVersionUID = -5617779585611342121L;

	private static final String[] USER_ATTRIBUTES = new String[] {DATE_OF_BIRTH,SEX,TRANSACTION_IDENTIFIER,IDENTITY_ASSURANCE_LEVEL};

	private HashMap<String,Object> userAttributes = new HashMap<String,Object>();

	/**
	 * Default Constructor.
	 */
	public DefaultBcServicesCardUserInfo() {
		super(new UserCredentials(UserTypeCode.BC_SERVICES_CARD));
	}

	/**
	 * Copy constructor. Copies all user details from the given object to this
	 * instance.
	 * 
	 * @param user
	 *            The user object to copy.
	 */
	@SuppressWarnings("unchecked")
	public DefaultBcServicesCardUserInfo(
			DefaultBcServicesCardUserInfo user) {
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
	protected DefaultBcServicesCardUserInfo(UserCredentials credentials) {
		super(credentials);
	}

	/**
	 * Clones the WebADEUserInfo object. The cloned object will have the
	 * isReadOnly() flag set to false, making it editable.
	 */
	@Override
	public Object clone() {
		DefaultBcServicesCardUserInfo info = new DefaultBcServicesCardUserInfo(
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

    /**
     * @see ca.bc.gov.webade.user.BcServicesCardUserInfo#getDateOfBirth()
     */
    @Override
	public Date getDateOfBirth() {
        return (Date) this.userAttributes.get(DATE_OF_BIRTH);
	}

    /**
     * @see ca.bc.gov.webade.user.BcServicesCardUserInfo#getSex()
     */
    @Override
	public String getSex() {
		return (String) this.userAttributes.get(SEX);
	}

    /**
     * @see ca.bc.gov.webade.user.BcServicesCardUserInfo#getTransactionIdentifier()
     */
    @Override
	public String getTransactionIdentifier() {
		return (String) this.userAttributes.get(TRANSACTION_IDENTIFIER);
	}

    /**
     * @see ca.bc.gov.webade.user.BcServicesCardUserInfo#getIdentityAssuranceLevel()
     */
    @Override
	public String getIdentityAssuranceLevel() {
		return (String) this.userAttributes.get(IDENTITY_ASSURANCE_LEVEL);
	}

    /**
     * Sets this user's dateOfBirth attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param dateOfBirth
     *            The dateOfBirth to set.
     */
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
     * Sets this user's sex attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param sex
     *            The sex to set.
     */
    public final void setSex(String sex) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(SEX, sex);
        }
    }

    /**
     * Sets this user's transactionIdentifier attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param transactionIdentifier
     *            The transactionIdentifier to set.
     */
    public final void setTransactionIdentifier(String transactionIdentifier) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(TRANSACTION_IDENTIFIER, transactionIdentifier);
        }
    }

    /**
     * Sets this user's identityAssuranceLevel attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param identityAssuranceLevel
     *            The identityAssuranceLevel to set.
     */
    public final void setIdentityAssuranceLevel(String identityAssuranceLevel) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(IDENTITY_ASSURANCE_LEVEL, identityAssuranceLevel);
        }
    }
}
