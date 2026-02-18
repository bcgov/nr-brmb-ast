/**
 * @(#)AbstractSearchAttribute.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

import java.io.Serializable;

/**
 * @author jross
 */
public abstract class AbstractSearchAttribute implements SearchAttribute, Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isSupported;

    /**
     * @see ca.bc.gov.webade.user.search.SearchAttribute#isSupported()
     */
    @Override
	public boolean isSupported() {
        return isSupported;
    }

    /**
     * @see ca.bc.gov.webade.user.search.SearchAttribute#setSupported(boolean)
     */
    @Override
	public void setSupported(boolean isSupported) {
        this.isSupported = isSupported;
    }
}