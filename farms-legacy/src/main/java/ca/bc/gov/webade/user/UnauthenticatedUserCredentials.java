/**
 * @(#)UnauthenticatedUserCredentials.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

/**
 * @author jross
 */
final class UnauthenticatedUserCredentials extends UserCredentials {
    private static final long serialVersionUID = 8230596022575725934L;

    /**
     * Default Constructor
     */
    UnauthenticatedUserCredentials() {
        super();
        setAccountName(null);
        setSourceDirectory(null);
        setUserGuid(null);
        setUserTypeCode(null);
        setReadOnly();
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#areCredentialsComplete()
     */
    @Override
	public boolean areCredentialsComplete() {
        return true;
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#compareCredentials(ca.bc.gov.webade.user.UserCredentials)
     */
    @Override
	public boolean compareCredentials(UserCredentials credentials) {
        return this.equals(credentials);
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        return obj == this;
    }

    /**
     * @see ca.bc.gov.webade.user.UserCredentials#mergeCredentials(ca.bc.gov.webade.user.UserCredentials)
     */
    @Override
	public void mergeCredentials(UserCredentials credentials) throws RuntimeException {
        throw new RuntimeException("Cannot merge unauthenticated user credentials.");
    }

}
