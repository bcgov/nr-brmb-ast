package ca.bc.gov.webade.developer.j2ee;

import java.io.Serializable;

import javax.servlet.FilterConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.developer.DeveloperConstants;
import ca.bc.gov.webade.developer.user.provider.WebADEDeveloperUserProvider;
import ca.bc.gov.webade.developer.util.ConfigurationUtils;
import ca.bc.gov.webade.user.UserTypeCode;

/**
 * Holder for configuration parameters related to the Developer Filter.
 * 
 * @author Vivid Solutions Inc
 */
class FilterConfigurator implements Serializable {

	private static final long serialVersionUID = 5458826464202272504L;
	
	private static final Logger logger = LoggerFactory.getLogger(FilterConfigurator.class);

	private FilterConfig filterConfig;

	FilterConfigurator(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
		if (logger.isDebugEnabled()) {
			logger.debug("filterConfig = " + filterConfig);
		}
	}

	boolean isPasswordEnabled() {
		boolean passwordEnabled = false;
		if (filterConfig != null) {
			String passwordEnabledParam = filterConfig.getInitParameter(FilterConstants.PASSWORD_ENABLED);
			passwordEnabled = "true".equalsIgnoreCase(passwordEnabledParam);
		}
		return passwordEnabled;
	}

	String getBasicRealm() {
		String realm = "WebADEDeveloperFilter";
		if (filterConfig != null) {
			String realmName = filterConfig.getInitParameter(FilterConstants.AUTHENTICATION_REALM);
			if ((realmName != null) && (!"".equals(realmName))) {
				realm = realmName;
			}
		}
		return realm;
	}

	boolean forceBasicAuth() {
		boolean force = false;
		if (filterConfig != null) {
			String authMethod = filterConfig.getInitParameter(FilterConstants.AUTHENTICATION_METHOD);
			force = FilterConstants.AUTHENTICATION_BASIC.equalsIgnoreCase(authMethod);
		}
		return force;
	}

	boolean allowBasicAuth() {
		boolean basic = false;
		if (filterConfig != null) {
			String authMethod = filterConfig.getInitParameter(FilterConstants.AUTHENTICATION_METHOD);
			basic = FilterConstants.AUTHENTICATION_BASIC.equalsIgnoreCase(authMethod) || FilterConstants.AUTHENTICATION_FORM_PLUS_BASIC.equalsIgnoreCase(authMethod);
		}
		return basic;
	}

	boolean filterUsersByAccess() {
		boolean filterUsersByAccess = false;
		if (filterConfig != null) {
			String filterUsersParam = filterConfig.getInitParameter(FilterConstants.FILTER_USERS_BY_ACCESS);
			filterUsersByAccess = "true".equalsIgnoreCase(filterUsersParam);
		}
		return filterUsersByAccess;
	}

	String getCookieDomain() {
		String cookieDomain = null;
		if (filterConfig != null) {
			String domain = filterConfig.getInitParameter(FilterConstants.COOKIE_DOMAIN);
			if ((domain != null) && (!"".equals(domain))) {
				cookieDomain = domain;
			}
		}
		return cookieDomain;
	}

	boolean useCookie() {
		boolean useCookie = false;
		if (filterConfig != null) {
			String useCookieParam = filterConfig.getInitParameter(FilterConstants.USE_COOKIE);
			useCookie = "true".equalsIgnoreCase(useCookieParam);
		}
		return useCookie;
	}

	String getUserInfoFileLocation() {
		String userInfoFileLocation = null;
		if (filterConfig != null) {
			userInfoFileLocation = ConfigurationUtils.findProperty(filterConfig, DeveloperConstants.USER_INFO_FILE_LOCATION_PARAM);
			if (userInfoFileLocation == null) {
				userInfoFileLocation = ConfigurationUtils.findProperty(filterConfig, WebADEDeveloperUserProvider.USER_INFO_FILE_LOCATION);
			}
		}
		return userInfoFileLocation;
	}

	public boolean isHeaderRequired(UserTypeCode userTypeCode, String header) {
		logger.debug("<isHeaderRequired");
		boolean result = false;
		if (filterConfig != null) {
			
			String key = userTypeCode.getCodeValue()+"-"+header+"_HEADER";
			logger.debug("key="+key);
			
			String value = filterConfig.getInitParameter(key);
			logger.debug("value="+value);
			
			if(value == null) {
				key = "ALL"+"-"+header+"_HEADER";
				logger.debug("key="+key);
				
				value = filterConfig.getInitParameter(key);
				logger.debug("value="+value);
			}
			
			result = "true".equalsIgnoreCase(value);
		}
		logger.debug(">isHeaderRequired "+result);
		return result;
	}

}
