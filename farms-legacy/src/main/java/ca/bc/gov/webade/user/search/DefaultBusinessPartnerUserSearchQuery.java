/**
 * @(#)DefaultBusinessPartnerUserSearchQuery.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

import ca.bc.gov.webade.user.UserTypeCode;

/**
 * @author jross
 */
public class DefaultBusinessPartnerUserSearchQuery extends AbstractUserSearchQuery implements BusinessPartnerUserSearchQuery {
    private static final long serialVersionUID = 5109850651985952530L;
    /** The business GUID search attribute. */
    private TextSearchAttribute businessGUID = new DefaultTextSearchAttribute();

    /**
     * Default Constructor.
     */
    public DefaultBusinessPartnerUserSearchQuery() {
        super();

    }

    /**
     * @return Returns the directory user type.
     */
    @Override
	public UserTypeCode getDirectoryUserType() {
        return UserTypeCode.BUSINESS_PARTNER;
    }

    /**
     * @see ca.bc.gov.webade.user.search.BusinessPartnerUserSearchQuery#getBusinessGUID()
     */
    @Override
	public TextSearchAttribute getBusinessGUID() {
        return businessGUID;
    }

}
