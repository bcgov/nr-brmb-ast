package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Default implementation of the WebADEUserInfo. Each setter can only be called
 * once, after which the set methods will throw a RuntimeException.
 * @author jason
 */
public abstract class AbstractSiteMinderUserInfo extends AbstractWebADEUserInfo implements SiteMinderUserInfo {
	
	private static final long serialVersionUID = 1L;

	private static final String[] USER_ATTRIBUTES = new String[] { 
		SITE_MINDER_USER_TYPE,
		SITE_MINDER_USER_IDENTIFIER,
		SITE_MINDER_USER_IDENTIFIER_TYPE,
		SITE_MINDER_AUTHORITATIVE_PARTY_NAME,
		SITE_MINDER_AUTHORITATIVE_PARTY_IDENTIFIER
	};

    private HashMap<String,Object> userAttributes = new HashMap<String,Object>();

    /**
     * Default Constructor.
     */
    public AbstractSiteMinderUserInfo() {
    	super();
    }

    /**
     * User Credentials Constructor. Creates an empty user object with the given
     * user credentials.
     * @param userCredentials
     *            The user credentials to set for this user.
     */
    public AbstractSiteMinderUserInfo(UserCredentials userCredentials) {
    	super(userCredentials);
    }

    /**
     * Copy constructor. Copies all user details from the given object to this
     * instance.
     * @param user
     *            The user object to copy.
     */
    @SuppressWarnings("unchecked")
	public AbstractSiteMinderUserInfo(AbstractSiteMinderUserInfo user) {
    	super(user);
        this.userAttributes = (HashMap<String,Object>) user.userAttributes.clone();
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
     * @see ca.bc.gov.webade.user.SiteMinderUserInfo#getSiteMinderUserType()
     */
    @Override
	public String getSiteMinderUserType() {
		return (String) this.userAttributes.get(SITE_MINDER_USER_TYPE);
	}

	/**
     * @see ca.bc.gov.webade.user.SiteMinderUserInfo#getSiteMinderUserIdentifer()
     */
    @Override
	public String getSiteMinderUserIdentifer() {
		return (String) this.userAttributes.get(SITE_MINDER_USER_IDENTIFIER);
	}

	/**
     * @see ca.bc.gov.webade.user.SiteMinderUserInfo#getSiteMinderUserIdentiferType()
     */
    @Override
	public String getSiteMinderUserIdentiferType() {
		return (String) this.userAttributes.get(SITE_MINDER_USER_IDENTIFIER_TYPE);
	}

	/**
     * @see ca.bc.gov.webade.user.SiteMinderUserInfo#getSiteMinderAuthoritativePartyName()
     */
    @Override
	public String getSiteMinderAuthoritativePartyName() {
		return (String) this.userAttributes.get(SITE_MINDER_AUTHORITATIVE_PARTY_NAME);
	}

	/**
     * @see ca.bc.gov.webade.user.SiteMinderUserInfo#getSiteMinderAuthoritativePartyIdentifier()
     */
    @Override
	public String getSiteMinderAuthoritativePartyIdentifier() {
		return (String) this.userAttributes.get(SITE_MINDER_AUTHORITATIVE_PARTY_IDENTIFIER);
	}

    /**
     * Sets this user's siteMinderUserType attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param siteMinderUserType
     *            The siteMinderUserType to set.
     */
    public final void setSiteMinderUserType(String siteMinderUserType) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(SITE_MINDER_USER_TYPE, siteMinderUserType);
        }
    }

    /**
     * Sets this user's siteMinderUserIdentifer attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param siteMinderUserIdentifer
     *            The siteMinderUserIdentifer to set.
     */
    public final void setSiteMinderUserIdentifer(String siteMinderUserIdentifer) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(SITE_MINDER_USER_IDENTIFIER, siteMinderUserIdentifer);
        }
    }

    /**
     * Sets this user's siteMinderUserIdentiferType attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param siteMinderUserIdentiferType
     *            The siteMinderUserIdentiferType to set.
     */
    public final void setSiteMinderUserIdentiferType(String siteMinderUserIdentiferType) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(SITE_MINDER_USER_IDENTIFIER_TYPE, siteMinderUserIdentiferType);
        }
    }

    /**
     * Sets this user's siteMinderAuthoritativePartyName attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param siteMinderAuthoritativePartyName
     *            The siteMinderAuthoritativePartyName to set.
     */
    public final void setSiteMinderAuthoritativePartyName(String siteMinderAuthoritativePartyName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(SITE_MINDER_AUTHORITATIVE_PARTY_NAME, siteMinderAuthoritativePartyName);
        }
    }

    /**
     * Sets this user's siteMinderAuthoritativePartyIdentifier attribute to the given value. If this
     * user object is read-only, a runtime exception is thrown.
     * @param siteMinderAuthoritativePartyIdentifier
     *            The siteMinderAuthoritativePartyIdentifier to set.
     */
    public final void setSiteMinderAuthoritativePartyIdentifier(String siteMinderAuthoritativePartyIdentifier) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user info is set as read-only.");
        } 

        {
            this.userAttributes.put(SITE_MINDER_AUTHORITATIVE_PARTY_IDENTIFIER, siteMinderAuthoritativePartyIdentifier);
        }
    }

}
