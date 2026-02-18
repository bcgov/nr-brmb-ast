/**
 * @(#)UserCredentials.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

import java.io.Serializable;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jross
 */
public class UserCredentials implements Serializable {
    private static final long serialVersionUID = 8978538091420450082L;

    /**
     * The reserved attribute name for the user guid attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    private static final String USER_GUID = "webade.user.guid";

    /**
     * The reserved attribute name for the user account name attribute. Used by
     * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
     * and <code>hasAttribute</code> methods.
     */
    private static final String ACCOUNT_NAME = "webade.account.name";

    /**
     * The reserved attribute name for the user source directory attribute. Used
     * by the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
     * and <code>hasAttribute</code> methods.
     */
    private static final String SOURCE_DIRECTORY = "webade.user.source.directory";

    /**
     * The reserved attribute name for the user type code attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    private static final String USER_TYPE_CODE = "webade.user.type.code";

    /**
     * Complete empty user credentials for un-authenticated user permissions.
     */
    public static final UserCredentials UNAUTHENTICATED_USER_CREDENTIALS = new UnauthenticatedUserCredentials();
    private HashMap<String,Object> userAttributes = new HashMap<String,Object>();
    private boolean isReadOnly = false;
    private boolean acceptsNullAttributes = false;
	
	private static final Logger log = LoggerFactory.getLogger(UserCredentials.class);

    /**
     * Returns true if the given credentials match the static
     * <code>UNAUTHENTICATED_USER_CREDENTIALS</code> settings.
     * @param creds
     *            The credentials to test.
     * @return True if the given credentials are a match.
     */
    public static final boolean areUnauthenticated(UserCredentials creds) {
        return UNAUTHENTICATED_USER_CREDENTIALS.equals(creds);
    }

    /**
     * Default Constructor.
     */
    public UserCredentials() {
    }

    /**
     * Constructor indicating that this instance will accept null values as
     * valid attibutes (Default is false). Used by the WebADE to allow for
     * authenticated users that cannot be located by the WebADE.
     * @param acceptsNullAttributes
     *            Flag that determines whether null values are valid values.
     */
    public UserCredentials(boolean acceptsNullAttributes) {
        this.acceptsNullAttributes = acceptsNullAttributes;
    }

    /**
     * Copy Constructor.
     * @param credentials
     *            The credentials to copy.
     */
    public UserCredentials(UserCredentials credentials) {
        this(credentials,
                (credentials != null) ? credentials.acceptsNullAttributes
                        : false);
    }

    /**
     * Copy Constructor.
     * @param credentials
     *            The credentials to copy.
     * @param acceptsNullAttributes
     *            Flag that determines whether null values are valid values.
     */
    @SuppressWarnings("unchecked")
	public UserCredentials(UserCredentials credentials,
            boolean acceptsNullAttributes) {
        this.acceptsNullAttributes = acceptsNullAttributes;
        if (credentials != null) {
            this.userAttributes = (HashMap<String,Object>) credentials.userAttributes.clone();
        }
    }

    /**
     * User type code constructor.
     * @param userTypeCode
     *            The user type code instance for this credentials instance.
     */
    public UserCredentials(UserTypeCode userTypeCode) {
        setUserTypeCode(userTypeCode);
    }

    /**
     * @return Returns the accountName.
     */
    public final String getAccountName() {
        return (String) this.userAttributes.get(ACCOUNT_NAME);
    }

    /**
     * @param accountName
     *            The accountName to set.
     */
    public final void setAccountName(String accountName) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user credentials are set as read-only.");
        } else if (accountName != null && accountName.trim().length()>0) {
            this.userAttributes.put(ACCOUNT_NAME, accountName.toUpperCase());
        } else if (acceptsNullAttributes()) {
            this.userAttributes.put(ACCOUNT_NAME, null);
        }
    }

    /**
     * @return Returns the sourceDirectory.
     */
    public final String getSourceDirectory() {
        return (String) this.userAttributes.get(SOURCE_DIRECTORY);
    }

    /**
     * @param sourceDirectory
     *            The sourceDirectory to set.
     */
    public final void setSourceDirectory(String sourceDirectory) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user credentials are set as read-only.");
        } else if (sourceDirectory != null && sourceDirectory.trim().length()>0) {
            this.userAttributes.put(SOURCE_DIRECTORY, sourceDirectory
                    .toUpperCase());
        } else if (acceptsNullAttributes()) {
            this.userAttributes.put(SOURCE_DIRECTORY, null);
        }
    }

    /**
     * @return Returns the user's fully-qualified id, including source directory
     *         and account name.
     */
    public final String getUserId() {
    	String result = null;
    	
    	if(getAccountName() != null && getSourceDirectory() != null) {
    		result = getSourceDirectory() + WebADEUserInfoUtils.BACKSLASH + getAccountName();
    	} else if(getAccountName() != null) {
    		result = getAccountName();
    	} else if(getSourceDirectory() != null  && getUserGuid() !=null) {
    		result = getSourceDirectory() + WebADEUserInfoUtils.BACKSLASH + getUserGuid();
    	} else if(getUserTypeCode() != null  && getUserGuid() !=null) {
    		result = getUserTypeCode().getCodeValue() + WebADEUserInfoUtils.BACKSLASH + getUserGuid();
    	}
    	
    	return result;
    }

    /**
     * @return Returns the userGuid.
     */
    public final GUID getUserGuid() {
        return (GUID) this.userAttributes.get(USER_GUID);
    }

    /**
     * @param userGuid
     *            The userGuid to set.
     */
    public final void setUserGuid(GUID userGuid) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user credentials are set as read-only.");
        } else if (userGuid != null) {
            this.userAttributes.put(USER_GUID, userGuid);
        } else if (acceptsNullAttributes()) {
            this.userAttributes.put(USER_GUID, null);
        }
    }

    /**
     * @return Returns the userTypeCode.
     */
    public final UserTypeCode getUserTypeCode() {
        return (UserTypeCode) this.userAttributes.get(USER_TYPE_CODE);
    }

    /**
     * @param userTypeCode
     *            The userTypeCode to set.
     */
    public final void setUserTypeCode(UserTypeCode userTypeCode) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user credentials are set as read-only.");
        } else if (userTypeCode != null) {
            this.userAttributes.put(USER_TYPE_CODE, userTypeCode);
        } else if (acceptsNullAttributes()) {
            this.userAttributes.put(USER_TYPE_CODE, null);
        }
    }

    /**
     * @return True if all credential fields for this instance are set.
     */
    public boolean areCredentialsComplete() {
        return (UNAUTHENTICATED_USER_CREDENTIALS.equals(this) && this.isReadOnly())
                || (getAccountName() != null && getSourceDirectory() != null
                        && getUserGuid() != null && getUserTypeCode() != null);
    }

    /**
     * Merges the given credentials into this instance, adopting credential
     * attributes from the given object where no matching ones are set for this
     * object.
     * @param credentials
     *            The given credentials to merge.
     * @throws RuntimeException
     *             Thrown if the given credentials' attributes do not match any
     *             attributes already set on this object.
     */
    public void mergeCredentials(UserCredentials credentials)
            throws RuntimeException {
        if (credentials == null) {
            return;
        }
        compareSetAttributes(this.getAccountName(), credentials.getAccountName(), ACCOUNT_NAME);
        compareSetAttributes(this.getSourceDirectory(), credentials.getSourceDirectory(), SOURCE_DIRECTORY);
        compareSetAttributes(this.getUserGuid(), credentials.getUserGuid(), USER_GUID);
        compareSetAttributes(this.getUserTypeCode(), credentials.getUserTypeCode(), USER_TYPE_CODE);
        String readonlyErrorMessage = "Credentials merge error: user credentials are set as read-only.";
        if (credentials.getAccountName() != null) {
            if (isReadOnly() && !credentials.getAccountName().equals(this.getAccountName())) {
                throw new RuntimeException(readonlyErrorMessage);
            } else if (!isReadOnly()) {
                setAccountName(credentials.getAccountName());
            }
        }
        if (credentials.getSourceDirectory() != null) {
            if (isReadOnly() && !credentials.getSourceDirectory().equals(this.getSourceDirectory())) {
                throw new RuntimeException(readonlyErrorMessage);
            } else if (!isReadOnly()) {
                setSourceDirectory(credentials.getSourceDirectory());
            }
        }
        if (credentials.getUserGuid() != null) {
            if (isReadOnly() && !credentials.getUserGuid().equals(this.getUserGuid())) {
                throw new RuntimeException(readonlyErrorMessage);
            } else if (!isReadOnly()) {
                setUserGuid(credentials.getUserGuid());
            }
        }
        if (credentials.getUserTypeCode() != null) {
            if (isReadOnly() && !credentials.getUserTypeCode().equals(this.getUserTypeCode())) {
                throw new RuntimeException(readonlyErrorMessage);
            } else if (!isReadOnly()) {
                setUserTypeCode(credentials.getUserTypeCode());
            }
        }
    }
    
    /**
     * Compares the given credentials to this instance. If these objects have an
     * attribute value that does not match, this method will return false.
     * Attributes that either object does not have set are ignored.
     * @param credentials
     *            The credentials to compare.
     * @return True if the credential attributes that are set match.
     */
    public boolean compareCredentials(UserCredentials credentials) {
        boolean areEqual = false;
        if (credentials != null) {
            boolean isThisUnauthenticated = UserCredentials.areUnauthenticated(this);
            boolean isObjUnauthenticated = UserCredentials.areUnauthenticated(credentials);
            if (isThisUnauthenticated == isObjUnauthenticated) {
                try {
                    compareSetAttributes(this.getAccountName(), 
                            credentials.getAccountName(), ACCOUNT_NAME);
                    compareSetAttributes(this.getSourceDirectory(), 
                            credentials.getSourceDirectory(), SOURCE_DIRECTORY);
                    compareSetAttributes(this.getUserGuid(), 
                            credentials.getUserGuid(), USER_GUID);
                    compareSetAttributes(this.getUserTypeCode(), 
                            credentials.getUserTypeCode(), USER_TYPE_CODE);
                    areEqual = true;
                } catch (RuntimeException e) {
                    log.debug(e.getMessage());
                }
            }
        }
        return areEqual;
    }

    /**
     * Sets the attributes of this object are non-editable. Used to prevent an
     * application from editing the attributes of the session user object.
     */
    public final void setReadOnly() {
        this.isReadOnly = true;
    }

    /**
     * @return True is the attributes of this object are non-editable.
     */
    public final boolean isReadOnly() {
        return this.isReadOnly;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        boolean equals = false;
        if (obj != null && obj instanceof UserCredentials) {
            UserCredentials creds = (UserCredentials) obj;
            if (this.getUserGuid() != null && creds.getUserGuid() != null) {
                equals = this.getUserGuid().equals(creds.getUserGuid());
            } else {
                equals = compareCredentials(creds);
            }
        }
        return equals;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public final int hashCode() {
        return toString().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public final String toString() {
        String toString = "UNKNOWN";
        if (this.getSourceDirectory() != null && this.getAccountName() != null) {
            toString = this.getSourceDirectory() + WebADEUserInfoUtils.BACKSLASH
                    + this.getAccountName();
        } else if (this.getUserTypeCode() != null
                && this.getAccountName() != null) {
            toString = this.getUserTypeCode() + ":" + this.getAccountName();
        } else if (this.getUserTypeCode() != null && this.getUserGuid() != null) {
            toString = this.getUserTypeCode() + ":" + this.getUserGuid();
        } else if (this.getSourceDirectory() != null
                && this.getUserGuid() != null) {
            toString = this.getSourceDirectory() + ":" + this.getUserGuid();
        } else if (this.getUserGuid() != null) {
            toString = "GUID:" + this.getUserGuid();
        } else if (this.getAccountName() != null) {
            toString = "USERID:" + this.getAccountName();
        }
        return toString;
    }

    private static void compareSetAttributes(Object thisAttribute,
            Object otherAttribute, String attributeName)
            throws RuntimeException {
        if (thisAttribute != null && otherAttribute != null
                && !thisAttribute.equals(otherAttribute)) {
            throw new RuntimeException("Attribute match failed: "
                    + "attribute '" + attributeName + "' is set to "
                    + "different values in each credentials instance.");
        }
    }

    /**
     * @return Returns the acceptsNullAttributes flag.
     */
    private final boolean acceptsNullAttributes() {
        return acceptsNullAttributes;
    }
}
