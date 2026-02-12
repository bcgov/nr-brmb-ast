/**
 * @(#)DatabaseUserCredentials.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.database;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;

/**
 * @author jross
 */
public final class DatabaseUserCredentials extends UserCredentials {
    private static final long serialVersionUID = 6829823839867540990L;

    /**
     * The reserved attribute name for the euser id attribute. Used by
     * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
     * and <code>hasAttribute</code> methods.
     */
    private static final String EUSER_ID = "webade.euser.id";
    
    private long eUserId;
    private Date updatedDate;
	
	private static final Logger log = LoggerFactory.getLogger(DatabaseUserCredentials.class);

    /**
     * Default Constructor.
     */
    public DatabaseUserCredentials() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param credentials
     *            The credentials to copy.
     */
    public DatabaseUserCredentials(UserCredentials credentials) {
        this(credentials, false);
    }

    /**
     * Copy constructor.
     * 
     * @param credentials
     *            The credentials to copy.
     * @param acceptsNullAttributes
     *            Flag that determines whether null values are valid values.
     */
    public DatabaseUserCredentials(UserCredentials credentials, boolean acceptsNullAttributes) {
        super(credentials, acceptsNullAttributes);
        if (credentials != null && credentials instanceof DatabaseUserCredentials) {
            DatabaseUserCredentials dbCreds = (DatabaseUserCredentials)credentials;
            this.eUserId = dbCreds.eUserId;
            this.updatedDate = dbCreds.updatedDate;
        }
    }

    /**
     * User type code constructor.
     * 
     * @param userTypeCode
     *            The user type code instance for this credentials instance.
     */
    public DatabaseUserCredentials(UserTypeCode userTypeCode) {
        super(userTypeCode);

    }

    /**
     * @return Returns the eUserId.
     */
    public final long getEUserId() {
        return eUserId;
    }

    /**
     * @param eUserId
     *            The eUserId to set.
     */
    public final void setEUserId(long eUserId) {
        if (isReadOnly()) {
            throw new RuntimeException("Attribute set error: "
                    + "user credentials are set as read-only.");
        } else {
            this.eUserId = eUserId;
        }
    }

    /**
     * @return Returns the date the user record in the database was last
     *         updated.
     */
    public final Date getUpdatedDate() {
        return this.updatedDate;
    }

    /**
     * @param updatedDate
     *            The date this database record was updated from the
     *            user-provider.
     */
    public final void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#areCredentialsComplete()
     */
    @Override
	public final boolean areCredentialsComplete() {
        return super.areCredentialsComplete() && getEUserId() > 0;
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#mergeCredentials(ca.bc.gov.webade.user.UserCredentials)
     */
    @Override
	public void mergeCredentials(UserCredentials credentials) throws RuntimeException {
        if (credentials instanceof DatabaseUserCredentials) {
            DatabaseUserCredentials dbCredentials = (DatabaseUserCredentials)credentials;
            compareSetAttributes(this.getEUserId(), dbCredentials.getEUserId(), EUSER_ID);
        }
        super.mergeCredentials(credentials);
        if (credentials instanceof DatabaseUserCredentials) {
            String readonlyErrorMessage = "Credentials merge error: user credentials are set as read-only.";
            if (credentials.getAccountName() != null) {
                if (isReadOnly() && !credentials.getAccountName().equals(this.getAccountName())) {
                    throw new RuntimeException(readonlyErrorMessage);
                } else if (!isReadOnly()) {
                    setAccountName(credentials.getAccountName());
                }
            }
        }
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#compareCredentials(ca.bc.gov.webade.user.UserCredentials)
     */
    @Override
	public boolean compareCredentials(UserCredentials credentials) {
        boolean areEqual = super.compareCredentials(credentials);
        if (areEqual && credentials instanceof DatabaseUserCredentials) {
            try {
                areEqual = false;
                DatabaseUserCredentials dbCredentials = (DatabaseUserCredentials)credentials;
                compareSetAttributes(this.getEUserId(), dbCredentials.getEUserId(), EUSER_ID);
                areEqual = true;
            } catch (RuntimeException e) {
                log.debug(e.getMessage());
            }
        }
        return areEqual;
    }

    private void compareSetAttributes(long thisAttribute,
            long otherAttribute, String attributeName)
            throws RuntimeException {
        if (thisAttribute > 0 && otherAttribute > 0 && thisAttribute != otherAttribute) {
            throw new RuntimeException("Attribute match failed: "
                    + "attribute '" + attributeName + "' is set to "
                    + "different values in each credentials instance.");
        }
    }
}
