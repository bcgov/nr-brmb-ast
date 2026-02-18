/**
 * @(#)AbstractUserSearchQuery.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

import java.io.Serializable;

/**
 * @author jross
 */
public abstract class AbstractUserSearchQuery implements UserSearchQuery, Serializable {
    private static final long serialVersionUID = -991974497423437967L;
    /** The full userId, including domain. */
    private TextSearchAttribute userId = new DefaultTextSearchAttribute();
    /** The user's last name. */
    private TextSearchAttribute lastName = new DefaultTextSearchAttribute();
    /** The user's first name. */
    private TextSearchAttribute firstName = new DefaultTextSearchAttribute();
    /** The user's middle initial. */
    private TextSearchAttribute middleInitial = new DefaultTextSearchAttribute();
    /** The user's phone number. */
    private TextSearchAttribute phoneNumber = new DefaultTextSearchAttribute();
    /** The user's email address. */
    private TextSearchAttribute emailAddress = new DefaultTextSearchAttribute();

    /**
     * 
     */
    public AbstractUserSearchQuery() {
        super();
    }

    /**
     * @return Returns the userId.
     */
    @Override
	public TextSearchAttribute getUserId() {
        return this.userId;
    }

    /**
     * @return Returns the emailAddress.
     */
    public TextSearchAttribute getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * @return Returns the lastName.
     */
    public TextSearchAttribute getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @return Returns the firstName.
     */
    @Override
	public TextSearchAttribute getFirstName() {
        return this.firstName;
    }

    /**
     * @return Returns the lastName.
     */
    @Override
	public TextSearchAttribute getLastName() {
        return this.lastName;
    }

    /**
     * @return Returns the middleInitial.
     */
    public TextSearchAttribute getMiddleInitial() {
        return middleInitial;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public boolean equals(Object obj) {
        return (obj instanceof UserSearchQuery)
                && getDirectoryUserType().equals(((UserSearchQuery)obj).getDirectoryUserType());
    }

	@Override
	public int hashCode() {
		return getDirectoryUserType().hashCode();
	}

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getDirectoryUserType().getDescription();
    }

}
