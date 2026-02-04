/**
 * @(#)UserSearchQuery.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

import ca.bc.gov.webade.user.UserTypeCode;

/**
 * Search object used for user searches.
 * 
 * @author jross
 */
public interface UserSearchQuery {

    /**
     * @return Returns the directory user type.
     */
    public UserTypeCode getDirectoryUserType();

    /**
     * @return Returns the userId.
     */
    public TextSearchAttribute getUserId();

    /**
     * @return Returns the firstName.
     */
    public TextSearchAttribute getFirstName();

    /**
     * @return Returns the lastName.
     */
    public TextSearchAttribute getLastName();
}