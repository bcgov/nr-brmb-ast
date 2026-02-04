/**
 * @(#)MinistryOrganizationInfo.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

/**
 * Returns the ministry specific organization code value as one field. Classes
 * implementing this interface should be able to be cast to their actual class,
 * in order to provide any ministry-specific data.
 * 
 * @author jross
 */
public interface MinistryOrganizationInfo {
    /**
     * Returns the ministry-specific unique organization code as a String.
     * 
     * @return The organization code.
     */
    public String getOrganizationCode();

    /**
     * Returns the ministry-specific unique organization code as an int.
     * 
     * @return The organization code.
     * @throws
     *         UnsupportedOperationException Thrown if this code cannot be expressed as a number.
     */
    public int getOrganizationCodeAsInt() throws UnsupportedOperationException;

    /**
     * Returns the ministry-specific unique organization code as a long.
     * 
     * @return The organization code.
     * @throws
     *         UnsupportedOperationException Thrown if this code cannot be expressed as a number.
     */
    public long getOrganizationCodeAsLong() throws UnsupportedOperationException;
}