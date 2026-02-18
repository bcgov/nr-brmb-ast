/**
 * Copyright (c) 2006, B.C. Government. All rights reserved
 */
package ca.bc.gov.webade.developer.j2ee;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.developer.Version;
import ca.bc.gov.webade.developer.WebADEDeveloperException;
import ca.bc.gov.webade.developer.security.PasswordService;
import ca.bc.gov.webade.developer.user.provider.WebADEDeveloperUserProvider;
import ca.bc.gov.webade.developer.xml.XMLFileBinding;
import ca.bc.gov.webade.developer.xml.XMLFileBindingCache;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.user.BcServicesCardUserInfo;
import ca.bc.gov.webade.user.BusinessPartnerUserInfo;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.GovernmentUserInfo;
import ca.bc.gov.webade.user.IndividualUserInfo;
import ca.bc.gov.webade.user.SiteMinderUserInfo;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.VerifiedIndividualUserInfo;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.security.enterprise.SiteminderConstants;

/**
 * Challenges user for authentication and pre-configures each request before it
 * gets to the WebADE filter.
 * 
 * @author Vivid Solutions Inc
 */
public final class WebADEDeveloperFilter implements Filter, Serializable {

	private static final long serialVersionUID = 2281851109827223436L;
	
	private static final Logger logger = LoggerFactory.getLogger(WebADEDeveloperFilter.class);
	private FilterConfigurator filterConfigurator = null;
	private XMLFileBindingCache userInfoFileBindingCache = null;

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		logger.info("Initializing WebADEDeveloperFilter time=" + System.currentTimeMillis());
		logger.info("WebADE Developer Module Version: " + Version.getVersion());

		filterConfigurator = new FilterConfigurator(filterConfig);
		// Set the userInfoFileLocation if configured in the filter
		String userInfoFileLocation = filterConfigurator.getUserInfoFileLocation();
		if (userInfoFileLocation != null) {
			WebADEDeveloperUserProvider.setUserInfoFileLocation(userInfoFileLocation);
		}
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		try {

			logger.info("WebADEDeveloperFilter Filtering Request");
			if (userInfoFileBindingCache == null) {
				userInfoFileBindingCache = new XMLFileBindingCache(WebADEDeveloperUserProvider.getUserInfoFile());
			}

			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			String disableDeveloperFilter = httpRequest.getParameter("disableDeveloperFilter");
			boolean disabled = (disableDeveloperFilter != null);

			WebADEUserInfo currentUserInfo = null;

			if (!disabled) {

				// Check if we already have authenticated data in session
				currentUserInfo = HttpRequestUtils.getCurrentUserInfo(httpRequest);
				logger.debug("currentUserInfo = " + currentUserInfo);
			}

			if (disabled) {

				logger.info("WebADE Developer Filter disabled.");
				chain.doFilter(httpRequest, httpResponse);
			} else if (currentUserInfo != null && !UserCredentials.areUnauthenticated(currentUserInfo.getUserCredentials())) {

				// WebADE user is already authenticated so proceed with the
				// request

				logger.info("WebADE user is already authenticated.");
				logger.info("WebADEDeveloperFilter proceeding with request.");

				Map<String, String> headers = getRequestHeaders(currentUserInfo.getUserCredentials());
				chain.doFilter(new DeveloperRequestWrapper(httpRequest, headers), httpResponse);
			} else {

				logger.info("Authenticating User.");

				String webadeDeveloperLogonType = request.getParameter(FilterConstants.WEBADE_DEVELOPER_LOGON_TYPE);
				if (webadeDeveloperLogonType != null && FilterConstants.LOGON_TYPE_ANONYMOUS.equalsIgnoreCase(webadeDeveloperLogonType)) {

					logger.info("Anonymous Logon");
					chain.doFilter(httpRequest, httpResponse);
				} else {

					if (filterConfigurator.useCookie()) {

						currentUserInfo = getUserFromCookie(httpRequest);
					}

					if (currentUserInfo != null) {

						Map<String, String> headers = getRequestHeaders(currentUserInfo.getUserCredentials());
						DeveloperRequestWrapper developerRequestWrapper = new DeveloperRequestWrapper(httpRequest, headers);

						logger.info("User authenticated from cookie.");
						logger.info("WebADE Developer filter proceeding with request.");

						chain.doFilter(developerRequestWrapper, response);
					} else {

						// Basic Authentication
						if (filterConfigurator.forceBasicAuth()) {

							String authHeader = httpRequest.getHeader("authorization");
							if (authHeader != null) {

								logger.info("Using Basic Authentication");
								doBasicAuthentication(authHeader, httpRequest, httpResponse, chain);
							} else {

								logger.info("Forcing Basic Authentication");
								basicChallenge(httpResponse);
							}
						} else if (filterConfigurator.allowBasicAuth()) {

							String authHeader = httpRequest.getHeader("authorization");
							if (authHeader != null) {

								logger.info("Using Basic Authentication");
								doBasicAuthentication(authHeader, httpRequest, httpResponse, chain);
							} else {

								logger.info("Falling back on Form Authentication");
								doFormAuthentication(httpRequest, httpResponse, chain);
							}
						} else {

							logger.info("Defaulting to Form Authentication");
							doFormAuthentication(httpRequest, httpResponse, chain);
						}
					}
				}
			}

		} catch (WebADEDeveloperException e) {
			logger.error(e.getMessage(), e);
			throw new ServletException(e.getMessage(), e);
		}
	}

	private Map<String, String> getRequestHeaders(UserCredentials userCredentials) throws WebADEDeveloperException {
		Map<String, String> result = new HashMap<String, String>();
	
		if(userCredentials==null) {
			throw new IllegalArgumentException("userCredentials cannot be null.");
		}
	
		if(userCredentials.getUserGuid()==null) {
			throw new IllegalArgumentException("userCredentials.userGuid cannot be null.");
		}
		
		WebADEUserInfo userInfo = getUserFromFile(userCredentials.getUserGuid().toMicrosoftGUIDString());
		
		if(userInfo instanceof BcServicesCardUserInfo) {
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_USERTYPE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserType());
			}
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentiferType());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyIdentifier());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_USERDISPLAYNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERDISPLAYNAME_HEADER, userInfo.getDisplayName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_EMAIL_HEADER)) {
				result.put(SiteminderConstants.SMGOV_EMAIL_HEADER, userInfo.getEmailAddress());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_SURNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_SURNAME_HEADER, userInfo.getLastName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_GIVENNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_GIVENNAME_HEADER, userInfo.getFirstName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_GIVENNAMES_HEADER)) {
				String givenNames = "";
				if(userInfo.getFirstName()!=null&&userInfo.getFirstName().trim().length()>0) {
					givenNames += userInfo.getFirstName();
				}
				if(userInfo.getMiddleName()!=null&&userInfo.getMiddleName().trim().length()>0) {
					if(givenNames.length()>0) {
						givenNames += " ";
					}
					givenNames += userInfo.getMiddleName();
				}
				result.put(SiteminderConstants.SMGOV_GIVENNAMES_HEADER, givenNames);
			}
			
			Date dateOfBirth = ((BcServicesCardUserInfo)userInfo).getDateOfBirth();
			if(dateOfBirth!=null) {
				
				if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_BIRTHDATE_HEADER)) {
					
					DateFormat df = new SimpleDateFormat("YYYMMDD");
					String birthDate = df.format(dateOfBirth);
					result.put(SiteminderConstants.SMGOV_BIRTHDATE_HEADER, birthDate);
				}
				
				if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AGE19OROVER_HEADER)) {
					
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.YEAR, -19);
					
					boolean age19OrOver = cal.getTime().compareTo(dateOfBirth)>=0;
					result.put(SiteminderConstants.SMGOV_AGE19OROVER_HEADER, age19OrOver?"TRUE":"FALSE");
				}
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_SEX_HEADER)) {
				result.put(SiteminderConstants.SMGOV_SEX_HEADER, ((BcServicesCardUserInfo) userInfo).getSex());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_STREETADDRESS_HEADER)) {
				String streetAddress = "";
				if(userInfo.getContactAddressLine1()!=null&&userInfo.getContactAddressLine1().trim().length()>0) {
					streetAddress += userInfo.getContactAddressLine1();
				}
				if(userInfo.getContactAddressLine2()!=null&&userInfo.getContactAddressLine2().trim().length()>0) {
					if(streetAddress.length()>0) {
						streetAddress += "\n";
					}
					streetAddress += userInfo.getContactAddressLine2();
				}
				
				result.put(SiteminderConstants.SMGOV_STREETADDRESS_HEADER, streetAddress);
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_CITY_HEADER)) {
				result.put(SiteminderConstants.SMGOV_CITY_HEADER, userInfo.getContactAddressCity());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_PROVINCE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_PROVINCE_HEADER, userInfo.getContactAddressProvince());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_POSTALCODE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_POSTALCODE_HEADER, userInfo.getContactAddressPostalCode());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_COUNTRY_HEADER)) {
				result.put(SiteminderConstants.SMGOV_COUNTRY_HEADER, userInfo.getContactAddressCountry());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_ADDRESSBLOCK_HEADER)) {
				String addressBlock = "";
				if(userInfo.getContactAddressLine1()!=null&&userInfo.getContactAddressLine1().trim().length()>0) {
					addressBlock += userInfo.getContactAddressLine1();
				}
				if(userInfo.getContactAddressLine2()!=null&&userInfo.getContactAddressLine2().trim().length()>0) {
					if(addressBlock.length()>0) {
						addressBlock += "\n";
					}
					addressBlock += userInfo.getContactAddressLine2();
				}
				
				String cityLine = "";
				if(userInfo.getContactAddressPostalCode()!=null&&userInfo.getContactAddressPostalCode().trim().length()>0) {
					cityLine += userInfo.getContactAddressPostalCode();
				}
				if(userInfo.getContactAddressProvince()!=null&&userInfo.getContactAddressProvince().trim().length()>0) {
					if(cityLine.length()>0) {
						cityLine = " "+cityLine;
					}
					cityLine = userInfo.getContactAddressProvince()+cityLine;
				}
				if(userInfo.getContactAddressCity()!=null&&userInfo.getContactAddressCity().trim().length()>0) {
					if(cityLine.length()>0) {
						cityLine = ", "+cityLine;
					}
					cityLine = userInfo.getContactAddressCity()+cityLine;
				}
				
				if(addressBlock.length()>0) {
					if(addressBlock.length()>0) {
						addressBlock += "\n";
					}
					addressBlock += cityLine;
				}
				
				result.put(SiteminderConstants.SMGOV_ADDRESSBLOCK_HEADER, addressBlock);
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_TRANSACTIONIDENTIFIER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_TRANSACTIONIDENTIFIER_HEADER, ((BcServicesCardUserInfo) userInfo).getTransactionIdentifier());
			}
			
			String identityAssuranceLevelString = ((BcServicesCardUserInfo) userInfo).getIdentityAssuranceLevel();
			if(identityAssuranceLevelString!=null&&identityAssuranceLevelString.trim().length()>0) {
			
				int identityAssuranceLevel = 0;
				try {
					identityAssuranceLevel = Integer.parseInt(identityAssuranceLevelString);
				} catch(NumberFormatException e) {
					logger.warn("Invalid identityAssuranceLevel", e);
				}
				
				if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL_HEADER)) {
					result.put(SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL_HEADER, identityAssuranceLevelString);
				}
				
				if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL1_HEADER)) {
					result.put(SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL1_HEADER, identityAssuranceLevel>0?"TRUE":"FALSE");
				}
				
				if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL2_HEADER)) {
					result.put(SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL2_HEADER, identityAssuranceLevel>1?"TRUE":"FALSE");
				}
				
				if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL3_HEADER)) {
					result.put(SiteminderConstants.SMGOV_IDENTITYASSURANCELEVEL3_HEADER, identityAssuranceLevel>2?"TRUE":"FALSE");
				}
			}
			
		} else if(userInfo instanceof GovernmentUserInfo) {
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserType());
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.GOVERNMENT, SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentiferType());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyIdentifier());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.GOVERNMENT, SiteminderConstants.SMGOV_USERGUID_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERGUID_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			}
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERDISPLAYNAME_HEADER, userInfo.getDisplayName());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.GOVERNMENT, SiteminderConstants.SMGOV_EMAIL_HEADER)) {
				result.put(SiteminderConstants.SMGOV_EMAIL_HEADER, userInfo.getEmailAddress());
			}
		} else if(userInfo instanceof BusinessPartnerUserInfo) {
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserType());
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BUSINESS_PARTNER, SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentiferType());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyIdentifier());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BUSINESS_PARTNER, SiteminderConstants.SMGOV_USERGUID_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERGUID_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			}
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERDISPLAYNAME_HEADER, userInfo.getDisplayName());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BUSINESS_PARTNER, SiteminderConstants.SMGOV_EMAIL_HEADER)) {
				result.put(SiteminderConstants.SMGOV_EMAIL_HEADER, userInfo.getEmailAddress());
			}

			// Always
			GUID businessGUID = ((BusinessPartnerUserInfo) userInfo).getBusinessGUID();
			if(businessGUID!=null) {
				result.put(SiteminderConstants.SMGOV_BUSINESSGUID_HEADER, businessGUID.toMicrosoftGUIDString());
			}
			
			result.put(SiteminderConstants.SMGOV_BUSINESSLEGALNAME_HEADER, ((BusinessPartnerUserInfo) userInfo).getBusinessLegalName());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BUSINESS_PARTNER, SiteminderConstants.SMGOV_BUSINESSNUMBER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_BUSINESSNUMBER_HEADER, ((BusinessPartnerUserInfo) userInfo).getBusinessNumber());
			}
			
		} else if(userInfo instanceof VerifiedIndividualUserInfo) {
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserType());
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.VERIFIED_INDIVIDUAL, SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentiferType());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyIdentifier());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.VERIFIED_INDIVIDUAL, SiteminderConstants.SMGOV_USERGUID_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERGUID_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			}
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERDISPLAYNAME_HEADER, userInfo.getDisplayName());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.VERIFIED_INDIVIDUAL, SiteminderConstants.SMGOV_EMAIL_HEADER)) {
				result.put(SiteminderConstants.SMGOV_EMAIL_HEADER, userInfo.getEmailAddress());
			}
			
		} else if(userInfo instanceof IndividualUserInfo) {
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserType());
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.INDIVIDUAL, SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERIDENTIFIERTYPE_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentiferType());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYNAME_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyName());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.BC_SERVICES_CARD, SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER)) {
				result.put(SiteminderConstants.SMGOV_AUTHORITATIVEPARTYIDENTIFIER_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderAuthoritativePartyIdentifier());
			}
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.INDIVIDUAL, SiteminderConstants.SMGOV_USERGUID_HEADER)) {
				result.put(SiteminderConstants.SMGOV_USERGUID_HEADER, ((SiteMinderUserInfo) userInfo).getSiteMinderUserIdentifer());
			}
			
			// Always
			result.put(SiteminderConstants.SMGOV_USERDISPLAYNAME_HEADER, userInfo.getDisplayName());
			
			if(filterConfigurator.isHeaderRequired(UserTypeCode.INDIVIDUAL, SiteminderConstants.SMGOV_EMAIL_HEADER)) {
				result.put(SiteminderConstants.SMGOV_EMAIL_HEADER, userInfo.getEmailAddress());
			}			
		} else {
			throw new IllegalArgumentException("Unsupported WebADEUserInfo type: "+userInfo.getClass());
		}
		
		return result;
	}

	private WebADEUserInfo getUserFromFile(String userGuid) throws WebADEDeveloperException {
		
		WebADEUserInfo result = null;
		
		if(userGuid==null) {
			throw new IllegalArgumentException("userGuid cannot be null.");
		}
		
		XMLFileBinding binding = userInfoFileBindingCache.loadBinding();

		for(WebADEUserInfo tmp:binding.getUsers()) {
			
			if (tmp.getUserCredentials().getUserGuid().toMicrosoftGUIDString().equals(userGuid)) {
				
				result = tmp;
				logger.info("Found user matching form authentication request parameters.");
				break;
			}
		}
		
		return result;
	}

	private WebADEUserInfo getUserFromCookie(HttpServletRequest request) throws WebADEDeveloperException {
		WebADEUserInfo result = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(FilterConstants.COOKIE_NAME)) {
					// Try to locate credentials in XML file
					XMLFileBinding binding = userInfoFileBindingCache.loadBinding();
					List<WebADEUserInfo> userList = binding.getUsers();
					for (Iterator<WebADEUserInfo> iter = userList.iterator(); iter.hasNext();) {
						WebADEUserInfo userInfo = (iter.next());
						String value = cookie.getValue();
						String guid = value.substring(0, value.indexOf(":"));
						if (guid.equalsIgnoreCase(userInfo.getUserCredentials().getUserGuid().toMicrosoftGUIDString())) {
							if (filterConfigurator.isPasswordEnabled()) {
								if (value.length() > value.indexOf(":") + 1) {
									String cPassword = cookie.getValue().substring(cookie.getValue().indexOf(":") + 1);
									if (binding.isValidPassword(userInfo.getUserCredentials(), cPassword, true)) {
										result = userInfo;
										if (logger.isInfoEnabled()) {
											logger.info("Found user matching cookie " + value);
										}
										break;
									}
								}
							} else {
								result = userInfo;
								if (logger.isInfoEnabled()) {
									logger.info("Found user matching cookie " + value);
								}
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}

	private void redirectToLoginPage(HttpServletRequest request, HttpServletResponse response, List<WebADEUserInfo> users, String error) throws IOException, ServletException {
		
		logger.info("WebADEDeveloperFilter redirecting to login page.");

		try {
			
			// reset response
			response.reset();
			// set content type
			response.setContentType("text/html");
			// set status
			response.setStatus(HttpServletResponse.SC_OK);

			/*
			 * Set some cache headers before writing the response to the
			 * ServletOutputStream. These headers determine the rules by which
			 * the page content may be cached by the client and intermediate
			 * proxies. Using these values for the cache headers may help to
			 * avoid some browser problems.
			 */
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			LoginPageGenerator loginPageGenerator = new LoginPageGenerator(filterConfigurator);
			response.getWriter().write(loginPageGenerator.loginPage(request, users, error));
			
		} catch (WebADEDeveloperException e) {
			logger.error(e.getMessage(), e);
			throw new ServletException("Failed to generate login page response.", e);
		}
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// no op
	}

	private void basicChallenge(HttpServletResponse response) throws IOException {
		response.setHeader("WWW-Authenticate", "Basic realm=\"" + filterConfigurator.getBasicRealm() + "\"");
		response.sendError(401, "Authenticate");
	}

	private void doBasicAuthentication(String authHeader, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, WebADEDeveloperException {
		
		// Get supplied credentials
		byte[] creds64 = authHeader.substring(authHeader.indexOf(" ") + 1).getBytes();
		String creds = new String(Base64.decodeBase64(creds64));
		String[] userParts = creds.split(":");
		String userId = userParts[0].toLowerCase();
		String userPassword = null;
		if (userParts.length > 1) {
			userPassword = userParts[1];
		}

		// Try to locate credentials in XML file
		XMLFileBinding binding = userInfoFileBindingCache.loadBinding();

		WebADEUserInfo authenticatedUserInfo = null;
		for(WebADEUserInfo tmp:binding.getUsers()) {

			if (tmp.getUserCredentials().getUserId().toLowerCase().equals(userId) && binding.isValidPassword(tmp.getUserCredentials(), userPassword)) {
				authenticatedUserInfo = tmp;
				logger.info("Found user matching basic authentication header.");
				break;
			}
		}
		
		if (authenticatedUserInfo!=null) {
			
			Map<String, String> headers = this.getRequestHeaders(authenticatedUserInfo.getUserCredentials());
			DeveloperRequestWrapper developerRequestWrapper = new DeveloperRequestWrapper(request, headers);
			
			logger.info("WebADE Developer filter proceeding with request.");
			
			if (filterConfigurator.useCookie()) {
				addAuthenticationCookie(request, response, authenticatedUserInfo, userPassword);
			}
			
			chain.doFilter(developerRequestWrapper, response);
		} else {
			
			basicChallenge(response);
		}
	}

	private void doFormAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, WebADEDeveloperException {
		
		WebADEUserInfo currentUserInfo = null;
		boolean proceedWithRequest = false;
		String error = null;

		XMLFileBinding binding = userInfoFileBindingCache.loadBinding();
		
		String userGuid = request.getParameter(FilterConstants.WEBADE_DEVELOPER_USERGUID);
		String password = request.getParameter(FilterConstants.WEBADE_DEVELOPER_PASSWORD);

		logger.debug("webadeDeveloperUserGuid = " + userGuid);

		// check if we have been to the login page and the attributes are set
		if (userGuid != null) {

			for(WebADEUserInfo tmp:binding.getUsers()) {
				
				if (tmp.getUserCredentials().getUserGuid().toMicrosoftGUIDString().equals(userGuid)) {
					
					currentUserInfo = tmp;
					logger.info("Found user matching form authentication request parameters.");
					break;
				}
			}

			// if we found the user
			if (currentUserInfo != null) {
				
				if (!filterConfigurator.isPasswordEnabled()) {
					
					// no password security continue with request
					logger.info("User authenticated via form.");
					proceedWithRequest = true;
				} else {
					
					// check password security
					if (password != null && binding.isValidPassword(currentUserInfo.getUserCredentials(), password)) {
						
						// password is valid
						logger.info("Password check success");
						logger.info("User authenticated via form.");
						proceedWithRequest = true;
					} else {
						
						// invalid password
						logger.info("Password check failed. Redirecting to login page with error " + FilterConstants.INVALID_LOGIN_MESSAGE);
						error = FilterConstants.INVALID_LOGIN_MESSAGE;
					}
				}
			} else {
				
				// no user could be found to match given GUID.
				// This should never happen so it is raised as an exception.
				throw new WebADEDeveloperException("Parameter " + FilterConstants.WEBADE_DEVELOPER_USERGUID + " was found in request with value " + userGuid + " but matching user info record could not be found.");
			}
		}
		
		if (proceedWithRequest) {
			
			// proceed with the wrapped request
			
			Map<String, String> headers = this.getRequestHeaders(currentUserInfo.getUserCredentials());
			DeveloperRequestWrapper developerRequestWrapper = new DeveloperRequestWrapper(request, headers);
			logger.info("WebADE Developer filter proceeding with request.");
			
			if (filterConfigurator.useCookie()) {
				addAuthenticationCookie(request, response, currentUserInfo, password);
			}
			
			chain.doFilter(developerRequestWrapper, response);
		} else { // redirect to login page
			redirectToLoginPage(request, response, binding.getUsers(), error);
		}
	}

	private void addAuthenticationCookie(HttpServletRequest request, HttpServletResponse response, WebADEUserInfo userInfo, String password) throws WebADEDeveloperException {
		if (logger.isInfoEnabled()) {
			logger.info("Adding user authentication cookie to response.");
		}
		String cPassword = "";
		if (password != null) {

			cPassword = PasswordService.encrypt(password);
		}
		String value = userInfo.getUserCredentials().getUserGuid().toMicrosoftGUIDString() + ":" + cPassword;
		Cookie cookie = new Cookie(FilterConstants.COOKIE_NAME, value);
		cookie.setComment("WebADE Developer Module Single Sign-On Authentication Cookie");
		String cookieDomain = filterConfigurator.getCookieDomain();
		if (cookieDomain != null && request.getServerName().endsWith(cookieDomain)) {
			cookie.setDomain(cookieDomain);
		}
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
