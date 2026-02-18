/**
 * @(#)Organization.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jross
 */
public final class Organization implements Serializable {
    private static final long serialVersionUID = -3384834477213346991L;
    /**
     * The organization type value for BC Government organizations.
     */
    public static final String BC_GOVERNMENT_ORG = "BCG";
    /**
     * The organization type value for non-government organizations.
     */
    public static final String NON_GOVERNMENT_ORG = "NGO";

    private long organizationId;
    private String name;
    private String description;
    private String organizationType;
    private Date effectiveDate;
    private Date expiryDate;
    private MinistryOrganizationInfo ministryInfo;

    /**
     * Default Constructor.
     */
    public Organization() {
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the organizationId.
     */
    public long getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId
     *            The organizationId to set.
     */
    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return Returns the organizationType.
     */
    public String getOrganizationType() {
        return organizationType;
    }

    /**
     * @param organizationType
     *            The organizationType to set.
     */
    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    /**
     * @return Returns the organizationCode.
     */
    public String getOrganizationCode() {
        return (ministryInfo != null) ? ministryInfo.getOrganizationCode()
                : null;
    }

    /**
     * @return Returns the organizationCode as an integer.
     * @throws UnsupportedOperationException
     *             Thrown if the organization code cannot be converted to a
     *             number.
     */
    public int getOrganizationCodeAsInt() throws UnsupportedOperationException {
        return (ministryInfo != null) ? ministryInfo.getOrganizationCodeAsInt()
                : -1;
    }

    /**
     * @return Returns the organizationCode as a long.
     * @throws UnsupportedOperationException
     *             Thrown if the organization code cannot be converted to a
     *             number.
     */
    public long getOrganizationCodeAsLong()
            throws UnsupportedOperationException {
        return (ministryInfo != null) ? ministryInfo
                .getOrganizationCodeAsLong() : -1L;
    }

    /**
     * @return Returns The organization's effective date.
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @param effectiveDate
     *            The organization's effective date to set.
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @return Returns The organization's expiry date.
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate
     *            The organization's expiry date to set.
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return Returns the ministryInfo.
     */
    public MinistryOrganizationInfo getMinistryInfo() {
        return ministryInfo;
    }

    /**
     * @param ministryInfo
     *            The ministryInfo to set.
     */
    public void setMinistryInfo(MinistryOrganizationInfo ministryInfo) {
        this.ministryInfo = ministryInfo;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        return (obj instanceof Organization && ((Organization) obj)
                .getOrganizationId() == getOrganizationId());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return new Long(organizationId).hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getName();
    }
}
