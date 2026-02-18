/**
 * @(#)SiteMinderUserInfo.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;


/**
 * This interface defines the basic attributes and operations WebADE maintains
 * about any user accessible via the configured user-info providers of the
 * WebADE application.
 * @author jross
 */
public interface SiteMinderUserInfo extends WebADEUserInfo {
	
	/**
	 * The reserved attribute name for the Site Minder user type attribute. Used by
	 * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
	 * and <code>hasAttribute</code> methods.
	 */
	 public static final String SITE_MINDER_USER_TYPE = "webade.user.site.minder.user.type";
	
	 /**
	  * The reserved attribute name for the Site Minder user identifier attribute. Used by
	  * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
	  * and <code>hasAttribute</code> methods.
	  */
	  public static final String SITE_MINDER_USER_IDENTIFIER = "webade.user.site.minder.user.identifier";
		
	 /**
	  * The reserved attribute name for the Site Minder user identifier type attribute. Used by
	  * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
	  * and <code>hasAttribute</code> methods.
	  */
	  public static final String SITE_MINDER_USER_IDENTIFIER_TYPE = "webade.user.site.minder.user.identifier.type";
		
	 /**
	  * The reserved attribute name for the Site Minder authoritative party name attribute. Used by
	  * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
	  * and <code>hasAttribute</code> methods.
	  */
	  public static final String SITE_MINDER_AUTHORITATIVE_PARTY_NAME = "webade.user.site.minder.authoritative.party.name";
		
	 /**
	  * The reserved attribute name for the Site Minder authoritative party identifier attribute. Used by
	  * the <code>getAttributeValue</code>, <code>getAttributeNames</code>,
	  * and <code>hasAttribute</code> methods.
	  */
	  public static final String SITE_MINDER_AUTHORITATIVE_PARTY_IDENTIFIER = "webade.user.site.minder.authoritative.party.identifier";

    /**
     * @return Returns the Site Minder user type.
     */
    public String getSiteMinderUserType();
    
    /**
     * @return Returns the Site Minder user identifier.
     */
    public String getSiteMinderUserIdentifer();
    
    /**
     * @return Returns the Site Minder user identifier type.
     */
    public String getSiteMinderUserIdentiferType();

    /**
     * @return Returns the Site Minder authoritative party name.
     */
    public String getSiteMinderAuthoritativePartyName();

    /**
     * @return Returns the Site Minder authoritative party identifier.
     */
    public String getSiteMinderAuthoritativePartyIdentifier();
}
