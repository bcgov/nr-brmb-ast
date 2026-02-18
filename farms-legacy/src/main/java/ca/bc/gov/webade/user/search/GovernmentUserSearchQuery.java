/**
 * @(#)GovernmentUserSearchQuery.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

/**
 * @author jross
 */
public interface GovernmentUserSearchQuery extends UserSearchQuery {

    /**
     * @return Returns the emailAddress.
     */
    public TextSearchAttribute getEmailAddress();

    /**
     * @return Returns the lastName.
     */
    public TextSearchAttribute getPhoneNumber();

    /**
     * @return Returns the middleInitial.
     */
    public TextSearchAttribute getMiddleInitial();

    /**
     * @return Returns the city.
     */
    public TextSearchAttribute getCity();

}
