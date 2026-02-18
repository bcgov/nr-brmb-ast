/**
 * @(#)DefaultGovernmentUserSearchQuery.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

import ca.bc.gov.webade.user.UserTypeCode;

/**
 * @author jross
 */
public class DefaultGovernmentUserSearchQuery extends AbstractUserSearchQuery implements GovernmentUserSearchQuery {
    private static final long serialVersionUID = 5921244574185985118L;
    /** The city search attribute. */
    private TextSearchAttribute city = new DefaultTextSearchAttribute();

    /**
     * Default Constructor.
     */
    public DefaultGovernmentUserSearchQuery() {
        super();
    }

    /**
     * @return Returns the directory user type.
     */
    @Override
	public UserTypeCode getDirectoryUserType() {
        return UserTypeCode.GOVERNMENT;
    }

    /**
     * @see ca.bc.gov.webade.user.search.GovernmentUserSearchQuery#getCity()
     */
    @Override
	public TextSearchAttribute getCity() {
        return city;
    }

}
