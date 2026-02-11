/**
 * @(#)IndividualUserInfo.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

import java.util.Date;

/**
 * @author jross
 */
public interface BcServicesCardUserInfo extends SiteMinderUserInfo {
	
    /**
     * The reserved attribute name for the date of birth attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String DATE_OF_BIRTH = "bc.services.card.user.date.of.birth";
	
    /**
     * The reserved attribute name for the sex attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String SEX = "bc.services.card.user.sex";
	
    /**
     * The reserved attribute name for the transaction identifier attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String TRANSACTION_IDENTIFIER = "bc.services.card.user.transaction.identifier";
	
    /**
     * The reserved attribute name for the transaction identifier attribute. Used by the
     * <code>getAttributeValue</code>, <code>getAttributeNames</code>, and
     * <code>hasAttribute</code> methods.
     */
    public static final String IDENTITY_ASSURANCE_LEVEL = "bc.services.card.user.identity.assurance.level";

    /**
     * @return Returns the dateOfBirth.
     */
    public Date getDateOfBirth();

    /**
     * @return Returns the sex.
     */
    public String getSex();

    /**
     * @return Returns the transactionIdentifier.
     */
    public String getTransactionIdentifier();

    /**
     * @return Returns the identityAssuranceLevel.
     */
    public String getIdentityAssuranceLevel();
}
