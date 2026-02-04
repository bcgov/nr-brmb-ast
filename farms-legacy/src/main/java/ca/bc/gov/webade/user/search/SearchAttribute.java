/**
 * @(#)SearchAttribute.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

/**
 * @author jross
 */
public interface SearchAttribute {
    /**
     * @return Returns the isSupported.
     */
    public boolean isSupported();
    
    /**
     * @param isSupported
     *            The isSupported to set.
     */
    public void setSupported(boolean isSupported);
}
