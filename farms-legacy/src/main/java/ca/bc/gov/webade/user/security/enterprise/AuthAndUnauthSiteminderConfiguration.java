/**
 * @(#)AuthAndUnauthSiteminderConfiguration.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.security.enterprise;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.user.UserCredentials;

/**
 * This configuration describes a web application that has SiteMinder managing
 * security for all requests to the application via one URL, while a second URL
 * is configured without SiteMinder security to allow anonymous access. When a
 * user accesses any part of the web application via the SiteMinder-controlled
 * URL for the first time, they will be redirected to the BC Government Common
 * Log-in Page and are forced to log-in before accessing the application
 * directly. A user will be logged in to SiteMinder for the duration of their
 * session with the web application.
 * 
 * @author jross
 */
public final class AuthAndUnauthSiteminderConfiguration extends AbstractSiteminderConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(AuthAndUnauthSiteminderConfiguration.class);

	public AuthAndUnauthSiteminderConfiguration(Properties properties) {
		super(properties);
	}
	
	/**
	 * @see ca.bc.gov.webade.user.security.enterprise.SecurityConfiguration#initialize(java.util.Properties)
	 */
	@Override
	public void initialize() throws SecurityException {
		log.debug("Initializing the AuthAndUnauthSiteminderConfiguration...");
	}

	@Override
	protected boolean credentialsMatch(UserCredentials requestCreds, UserCredentials sessionCreds) {
		boolean credsMatch;
		if (UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.compareCredentials(sessionCreds)) {
			credsMatch = UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.compareCredentials(requestCreds);
		} else {
			credsMatch = sessionCreds.compareCredentials(requestCreds);
		}
		return credsMatch;
	}

	@Override
	protected UserCredentials loadCredentialsFromRequest(HttpServletRequest request) throws SecurityException {
		// Default to unauthenticated
		UserCredentials credentials = UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS;

		if (mustUseRemoteUser()) {
			
			String remoteUser = request.getRemoteUser();
			if (remoteUser != null && isUserValid(remoteUser)) {
				
				log.debug("Found remote user " + remoteUser);
				String userName = remoteUser;
				credentials = extractCredentials(userName);
			} else {
				
				log.warn("Remote user " + remoteUser + " does not contain enough information to set user credentials.");
			}
		} else if (isNoSiteMinderHeadersPresent(request)) {
			
			log.debug("No Siteminder attributes found in request headers.");
			if (canUseRemoteUser()) {
				
				String remoteUser = request.getRemoteUser();
				if (remoteUser != null) {
					
					if (isUserValid(remoteUser)) {
						
						log.debug("Found remote user " + remoteUser);
						String userName = remoteUser;
						credentials = extractCredentials(userName);
					} else {
						
						log.warn("No Siteminder attributes found in request and remote user " + remoteUser + " does not contain enough information to set user credentials.");
					}
				}
			} else {
				
				log.debug("No Siteminder attributes found in request headers and cannot use remote user.");
			}
		} else if (isSiteMinderHeadersPresent(request)) {
			
			credentials = extractCredentials(request);
		} else {
			
			reportAllHeadersNotPresent(request);
		}
		return credentials;
	}

}
