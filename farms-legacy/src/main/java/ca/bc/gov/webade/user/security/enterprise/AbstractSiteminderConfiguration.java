/**
 * @(#)StandardSiteminderConfiguration.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.security.enterprise;

import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfoUtils;

/**
 * @author jross
 */
abstract class AbstractSiteminderConfiguration implements SiteminderConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractSiteminderConfiguration.class);
	/**
	 * <code>SMGOV_USERIDENTIFIER_REGEX</code>
	 */
	public static final String SMGOV_USERIDENTIFIER_REGEX = "smgov-useridentifier-regex";
	/**
	 * <code>CAN_USE_REMOTE_USER</code>
	 */
	public static final String CAN_USE_REMOTE_USER = "can-use-remote-user";
	/**
	 * <code>MUST_USE_REMOTE_USER</code>
	 */
	public static final String MUST_USE_REMOTE_USER = "must-use-remote-user";

	private static final String TRUE = "true";

	protected Properties properties;
	
	AbstractSiteminderConfiguration(Properties properties) {
        
        this.properties = properties;
	}

	/**
	 * @see ca.bc.gov.webade.user.security.enterprise.SecurityConfiguration#identifyUser(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public UserCredentials identifyUser(HttpServletRequest request) throws SecurityException {
		UserCredentials requestCreds = loadCredentialsFromRequest(request);
		UserCredentials sessionCreds = SessionUserInfoUtils.getCurrentUserCredentials(request);
		if (sessionCreds != null) {
			boolean credsMatch = credentialsMatch(requestCreds, sessionCreds);
			if (!credsMatch) {
				throw new SecurityException(getCredentialsMismatchError(requestCreds, sessionCreds));
			}
		}
		return requestCreds;
	}

	protected abstract boolean credentialsMatch(UserCredentials requestCreds, UserCredentials sessionCreds);

	protected abstract UserCredentials loadCredentialsFromRequest(HttpServletRequest request) throws SecurityException;

	protected String getCredentialsMismatchError(UserCredentials requestCreds, UserCredentials sessionCreds) {
		return "User credentials '" + requestCreds + "' in request do not match those '" + sessionCreds + "' stored in the session.";
	}

	/**
	 * Checks if we can use the remote user to log on.
	 * 
	 * @return true if we can use the remote user to log on, false otherwise.
	 */
	protected final boolean canUseRemoteUser() {
		if (properties != null) {
			String property = properties.getProperty(CAN_USE_REMOTE_USER);
			if (property != null) {
				return TRUE.equalsIgnoreCase(property);
			}
		}
		return true;
	}

	/**
	 * Checks if we must use the remote user to log on.
	 * 
	 * @return true if we must use the remote user to log on, false otherwise.
	 */
	protected final boolean mustUseRemoteUser() {
		if (properties != null) {
			String property = properties.getProperty(MUST_USE_REMOTE_USER);
			if (property != null) {
				return TRUE.equalsIgnoreCase(property);
			}
		}
		return false;
	}

	protected final String smgovUserIdentifierRegex() {
		String result = ".+";
		if (properties != null) {
			String property = properties.getProperty(SMGOV_USERIDENTIFIER_REGEX);
			if (property != null) {
				result = property;
			}
		}
		return result;
	}

	protected final static UserCredentials extractCredentials(String userName) {
		UserCredentials credentials = new UserCredentials();
		String accountName = WebADEUserInfoUtils.extractAccountName(userName);
		String sourceDirectory = WebADEUserInfoUtils.extractSourceDirectory(userName);
		credentials.setAccountName(accountName);
		log.debug("account name = " + accountName);
		if (sourceDirectory != null) {
			log.debug("source directory = " + sourceDirectory);
			credentials.setSourceDirectory(sourceDirectory);
		}
		return credentials;
	}

	protected final UserCredentials extractCredentials(HttpServletRequest request) {
		UserCredentials credentials = new UserCredentials();
		
		String siteMinderUserType = request.getHeader(SiteminderConstants.SMGOV_USERTYPE_HEADER);
		String userIdentifier = request.getHeader(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER);

		// If the userIdentifier is null then use the deprecated userGuid
		if (userIdentifier == null) {

			String userGUID = request.getHeader(SiteminderConstants.SMGOV_USERGUID_HEADER);
			
			if(userGUID == null) {
				
				throw new IllegalArgumentException("UserIdentifier and UserGuid cannot both be null.");
			} 

			{
				
				userIdentifier = userGUID;
			}
		}
		
		String authoritativePartyName = request.getHeader(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER);			
		
		log.debug("All required Siteminder attributes found in request headers.");
		log.debug("Siteminder " + siteMinderUserType + " user found.");
		
		if (siteMinderUserType == null) {
			
			throw new IllegalArgumentException("UserType cannot be cannot be null.");
		} 

		{
			
			if (SiteminderConstants.INTERNAL_USERTYPE.equals(siteMinderUserType)) {
				
				credentials.setUserTypeCode(UserTypeCode.GOVERNMENT);
			} else if (SiteminderConstants.BUSINESS_USERTYPE.equals(siteMinderUserType)) {
				
				credentials.setUserTypeCode(UserTypeCode.BUSINESS_PARTNER);
			} else if (SiteminderConstants.INDIVIDUAL_USERTYPE.equals(siteMinderUserType)) {
				
				credentials.setUserTypeCode(UserTypeCode.INDIVIDUAL);
			} else if (SiteminderConstants.VERIFIED_INDIVIDUAL_USERTYPE.equals(siteMinderUserType)) {
				
				if(authoritativePartyName==null || authoritativePartyName.trim().length()==0) {
					log.warn("Cannot definitively identify Verified Individual source because "+SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER+" is null.");
				}
				
				if(SiteminderConstants.BC_SERVICES_CARD_AUTHORITATIVE_PARTY_NAME.equals(authoritativePartyName)) {
					
					credentials.setUserTypeCode(UserTypeCode.BC_SERVICES_CARD);
				} else {
					
					credentials.setUserTypeCode(UserTypeCode.VERIFIED_INDIVIDUAL);
				}
			}
		}

		log.info("userIdentifier = " + userIdentifier);

		String guidRegex = smgovUserIdentifierRegex();
		log.info("guidRegex = '" + guidRegex + "'");

		Pattern guidPattern = Pattern.compile(guidRegex);
		Matcher matcher = guidPattern.matcher(userIdentifier);

		String guidString;
		if (matcher.find()) {

			try {

				guidString = matcher.group(1);
			} catch (IndexOutOfBoundsException e) {
				guidString = matcher.group(0);
			}
		} else {

			throw new IllegalStateException("Failed to parse the userIdentifier to find the guid.");
		}

		GUID guid = new GUID(guidString);
		credentials.setUserGuid(guid);

		String accountName = request.getHeader(SiteminderConstants.SMGOV_EMAIL_HEADER);
		credentials.setAccountName(accountName);

		return credentials;
	}

	protected final static boolean isUserValid(String user) {
		String accountName = WebADEUserInfoUtils.extractAccountName(user);
		String sourceDirectory = WebADEUserInfoUtils.extractSourceDirectory(user);
		return sourceDirectory != null && sourceDirectory.length() > 0 && accountName != null && accountName.length() > 0;
	}

	protected final static boolean isNoSiteMinderHeadersPresent(HttpServletRequest request) {
		boolean result = true;

		for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();) {
			String headerName = headerNames.nextElement();
			if (headerName.toUpperCase().startsWith("SMGOV_")) {
				result = false;
				break;
			}
		}

		return result;
	}

	protected final static boolean isSiteMinderHeadersPresent(HttpServletRequest request) {

		String userTypeHeader = request.getHeader(SiteminderConstants.SMGOV_USERTYPE_HEADER);
		String userIdentifier = request.getHeader(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER);
		String userGUID = request.getHeader(SiteminderConstants.SMGOV_USERGUID_HEADER);

		return (userTypeHeader != null) && (userIdentifier != null || userGUID != null);
	}

	protected final static void reportAllHeadersNotPresent(HttpServletRequest request) throws SecurityException {

		String userTypeHeader = request.getHeader(SiteminderConstants.SMGOV_USERTYPE_HEADER);
		String userIdentifier = request.getHeader(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER);
		String userGUID = request.getHeader(SiteminderConstants.SMGOV_USERGUID_HEADER);

		String missingHeaders = "";
		if (userTypeHeader == null) {
			log.error("Missing Siteminder header from request: " + SiteminderConstants.SMGOV_USERTYPE_HEADER);
			missingHeaders += SiteminderConstants.SMGOV_USERTYPE_HEADER;
		}
		if (userIdentifier == null) {
			log.error("Missing Siteminder header from request: " + SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER);
			if (missingHeaders.length() > 0) {
				missingHeaders += ", ";
			}
			missingHeaders += SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER;
		}
		if (userIdentifier == null && userGUID == null) {
			log.error("Missing Siteminder header from request: " + SiteminderConstants.SMGOV_USERGUID_HEADER);
			if (missingHeaders.length() > 0) {
				missingHeaders += ", ";
			}
			missingHeaders += SiteminderConstants.SMGOV_USERGUID_HEADER;
		}
		throw new SecurityException("Missing required Siteminder attributes " + missingHeaders + " from request headers.  User's session could not be initialized.");
	}

}
