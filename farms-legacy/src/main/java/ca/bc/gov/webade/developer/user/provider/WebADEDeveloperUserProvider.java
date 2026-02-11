package ca.bc.gov.webade.developer.user.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.developer.DeveloperConstants;
import ca.bc.gov.webade.developer.WebADEDeveloperException;
import ca.bc.gov.webade.developer.util.IOUtils;
import ca.bc.gov.webade.developer.xml.XMLFileBindingCache;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.provider.WebADEUserProvider;
import ca.bc.gov.webade.user.provider.WebADEUserProviderException;
import ca.bc.gov.webade.user.search.BusinessPartnerUserSearchQuery;
import ca.bc.gov.webade.user.search.DefaultBusinessPartnerUserSearchQuery;
import ca.bc.gov.webade.user.search.DefaultGovernmentUserSearchQuery;
import ca.bc.gov.webade.user.search.DefaultVerifiedIndividualUserSearchQuery;
import ca.bc.gov.webade.user.search.GovernmentUserSearchQuery;
import ca.bc.gov.webade.user.search.UserSearchQuery;
import ca.bc.gov.webade.user.search.VerifiedIndividualUserSearchQuery;
import ca.bc.gov.webade.user.search.WildcardOptions;

/**
 * The WebADEDeveloperUserProvider implements the WebADE UserInfoProvider
 * interface and returns user information. In the standard WebADE configuration
 * a UserInfoProvider retrieves information from government webservices and
 * passes this information back to the application. By altering the WebADE
 * preferences we can substitute this WebADEDeveloperUserProvider for the
 * standard Web Services UserInfoProvider. This enables the webade-developer
 * module to emulate the responses from web-services. This is useful when
 * developing WebADE applications outside the government extranet where the web
 * services are unavailable.
 * 
 * @author Vivid Solutions Inc
 */
public final class WebADEDeveloperUserProvider implements WebADEUserProvider {

  public static final String USER_TYPE_DOMAIN_SUFFIX = "-user-type-domain";
  public static final String RESPONSE_DELAY = "user-info-response-delay";
  public static final String USER_INFO_FILE_LOCATION = "user-info-file-location";
  public static final String ENABLED = "enabled";

  private static String userInfoFileLocation;
  private static String responseDelay;
  private static String tempFile;
	
	private static final Logger logger = LoggerFactory.getLogger(WebADEDeveloperUserProvider.class);
  public static final String USER_TYPE_DELINIATOR = ",";

  private Properties properties;
  private XMLFileBindingCache bindingCache;

  /**
   * Default Constructor.
   */
  public WebADEDeveloperUserProvider() {
  }

  /**
   * @see ca.bc.gov.webade.UserInfoProvider#init(ca.bc.gov.webade.PreferenceSet)
   */
  @Override
public final void init(Properties props) throws WebADEUserProviderException {
    this.properties = props;
    try {
      validateSupportedTypes(props);
      configureUserInfo(props);
      configureResponseDelay(props);
    }
    catch (WebADEDeveloperException e) {
      logger.error(e.getMessage(), e);
      throw new WebADEUserProviderException(e.getMessage(), e);
    }

  }

  @Override
public WebADEUserInfo getUser(UserCredentials requestor, UserCredentials credentials) throws WebADEUserProviderException {
    WebADEUserInfo user = null;
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("getUser: UserCredentials " + credentials);
      }

      if (requestor == null) {
        logger.warn("Requestor UserCredentials is null.");
      }
      if (credentials == null) {
        throw new IllegalArgumentException("Credentials parameter cannot be null.");
      }
      if (credentials.getUserGuid() == null && credentials.getAccountName() == null) {
        throw new IllegalArgumentException("Credentials parameter must have either a GUID or account name set.");
      }
      
      // We need the user type code in order to enforce the rule
      // that only IDIR accounts can search for IDIR accounts.
      // However, if the requestor is null or the requestor is unauthenticated
      // the system user will be the requestor.
      String requestorUserType = null;
      if (requestor != null) {
        UserTypeCode userTypeCode = getUserTypeCode(requestor);
        if(!UserCredentials.areUnauthenticated(requestor)) {
          requestorUserType = userTypeCode.getCodeValue();
        }
      }
      if (credentials.getSourceDirectory() == null && credentials.getUserTypeCode() == null) {
        if (logger.isDebugEnabled()) {
          logger.debug("Loading user: " + credentials);
        }

        logger.warn("The UserCredentials provided have resulted in an inefficient call to getUser(). " + "To optimize, set the UserCredentials sourceDirectory and/or userTypeCode. "
            + "UserCredentials=[" + credentials + "]");
        addDelay();

        WebADEUserInfo userInfo = getUntypedUser(credentials, requestorUserType);
        if (userInfo != null && UserTypeCode.GOVERNMENT.equals(userInfo.getUserCredentials().getUserTypeCode())) {
          if (credentials.getUserGuid() != null && !UserTypeCode.GOVERNMENT.getCodeValue().equals(requestorUserType)) {
            return null;
          }
        }
        return userInfo;
      }

      UserTypeCode userType = getUserTypeCode(credentials);
      String userTypeCode = userType.getCodeValue();
      String sourceDirectory = credentials.getSourceDirectory();
      credentials.setSourceDirectory(sourceDirectory);
      if (logger.isDebugEnabled()) {
        logger.debug("Loading " + userType + " user: " + credentials);
      }

      if (UserTypeCode.GOVERNMENT.getCodeValue().equals(userTypeCode)) {
        user = bindingCache.loadBinding().getGovernmentUser(credentials, requestorUserType);
      }
      else if (UserTypeCode.BUSINESS_PARTNER.getCodeValue().equals(userTypeCode)) {
        user = bindingCache.loadBinding().getBusinessPartnerUser(credentials);
      }
      else if (UserTypeCode.VERIFIED_INDIVIDUAL.getCodeValue().equals(userTypeCode)) {
        user = bindingCache.loadBinding().getVerifiedIndividualUser(credentials);
      }
      else if (UserTypeCode.INDIVIDUAL.getCodeValue().equals(userTypeCode)) {
        user = bindingCache.loadBinding().getIndividualUser(credentials);
      }

      if (logger.isDebugEnabled()) {
        logger.debug("Loaded user: " + user);
      }
    }
    catch (WebADEDeveloperException e) {
      throw new WebADEUserProviderException(e.getMessage(), e);
    }
    addDelay();
    return user;
  }

  /**
   * Note that I'm just returning the first source directory in the list. The
   * assumption in the interface is that there will only be one source directory
   * per user type. (Which isn't always true but I've got to implement the
   * interface)
   */
  @Override
public String getSourceDirectoryForUserType(UserTypeCode userTypeCode) {
    String[] sourceDirectories = getSourceDirectories(userTypeCode.getCodeValue());
    return sourceDirectories[0];
  }

  @Override
public List<WebADEUserInfo> findUsers(UserCredentials requestor, UserSearchQuery userSearchQuery) throws WebADEUserProviderException {
    if (logger.isDebugEnabled()) {
      logger.debug("findUsers: " + userSearchQuery);
    }
    List<WebADEUserInfo> results = new ArrayList<WebADEUserInfo>();
    try {
      if (UserTypeCode.GOVERNMENT.equals(userSearchQuery.getDirectoryUserType())) {
        // We need the user type code in order to enforce the rule
        // that only IDIR accounts can search for IDIR accounts.
        // However, if the requestor is null or the requestor is unauthenticated
        // the system user will be the requestor.
        if (requestor != null && !UserCredentials.areUnauthenticated(requestor)) {
          if (!UserTypeCode.GOVERNMENT.getCodeValue().equals(requestor.getUserTypeCode().getCodeValue())) {
            return results;
          }
        }
        results = bindingCache.loadBinding().findGovernmentUsers(userSearchQuery);
      }
      else if (UserTypeCode.BUSINESS_PARTNER.equals(userSearchQuery.getDirectoryUserType())) {
        results = bindingCache.loadBinding().findBusinessPartnerUsers(userSearchQuery);
      } else if (UserTypeCode.VERIFIED_INDIVIDUAL.equals(userSearchQuery.getDirectoryUserType())) {
        results = bindingCache.loadBinding().findVerifiedIndividualUsers(userSearchQuery);
      }

    }
    catch (WebADEDeveloperException e) {
      throw new WebADEUserProviderException(e.getMessage(), e);
    }
    if (logger.isInfoEnabled()) {
      StringBuffer sb = new StringBuffer();
      sb.append("Found Users:");
      Iterator<WebADEUserInfo> i = results.iterator();
      while (i.hasNext()) {
        sb.append(" ");
        sb.append(i.next());
      }
      logger.info(sb.toString());
    }
    addDelay();
    return results;
  }

  @Override
public UserSearchQuery createUserSearchQuery(UserTypeCode userTypeCode) throws WebADEUserProviderException {
    UserSearchQuery query = null;
    String typeCode = null;
    if (userTypeCode != null) {
      typeCode = userTypeCode.getCodeValue();
    }
    if (UserTypeCode.GOVERNMENT.getCodeValue().equals(typeCode)) {
      GovernmentUserSearchQuery govQuery = new DefaultGovernmentUserSearchQuery();
      govQuery.getMiddleInitial().setSupported(true);
      govQuery.getMiddleInitial().setWildcardOption(WildcardOptions.EXACT_MATCH);
      govQuery.getEmailAddress().setSupported(true);
      govQuery.getEmailAddress().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
      govQuery.getPhoneNumber().setSupported(true);
      govQuery.getPhoneNumber().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
      govQuery.getCity().setSupported(true);
      govQuery.getCity().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
      query = govQuery;
    }
    else if (UserTypeCode.BUSINESS_PARTNER.getCodeValue().equals(typeCode)) {
      BusinessPartnerUserSearchQuery bupQuery = new DefaultBusinessPartnerUserSearchQuery();
      bupQuery.getMiddleInitial().setSupported(true);
      bupQuery.getMiddleInitial().setWildcardOption(WildcardOptions.EXACT_MATCH);
      bupQuery.getEmailAddress().setSupported(true);
      bupQuery.getEmailAddress().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
      bupQuery.getPhoneNumber().setSupported(true);
      bupQuery.getPhoneNumber().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
      bupQuery.getBusinessGUID().setSupported(true);
      bupQuery.getBusinessGUID().setWildcardOption(WildcardOptions.EXACT_MATCH);
      query = bupQuery;
    }
    else if (UserTypeCode.VERIFIED_INDIVIDUAL.getCodeValue().equals(typeCode)) {
    	VerifiedIndividualUserSearchQuery vinQuery = new DefaultVerifiedIndividualUserSearchQuery();
    	vinQuery.getMiddleInitial().setSupported(true);
    	vinQuery.getMiddleInitial().setWildcardOption(WildcardOptions.EXACT_MATCH);
    	vinQuery.getEmailAddress().setSupported(true);
    	vinQuery.getEmailAddress().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
    	vinQuery.getPhoneNumber().setSupported(true);
    	vinQuery.getPhoneNumber().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
        query = vinQuery;
    }
    else {
      // We'll just make an anonymous individual user search type because WebADE
      // doesn't have one yet
      query = new ca.bc.gov.webade.user.search.AbstractUserSearchQuery() {

		private static final long serialVersionUID = 1L;

		@Override
		public UserTypeCode getDirectoryUserType() {
          return UserTypeCode.INDIVIDUAL;
        }
      };
    }
    query.getUserId().setSupported(true);
    query.getUserId().setWildcardOption(WildcardOptions.EXACT_MATCH);
    query.getFirstName().setSupported(true);
    query.getFirstName().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);
    query.getLastName().setSupported(true);
    query.getLastName().setWildcardOption(WildcardOptions.WILDCARD_RIGHT);

    return query;
  }

  @Override
	public GUID[] isUserInGroups(UserCredentials requestor, UserCredentials user, GUID[] groupGuids)
			throws WebADEUserProviderException {
		Set<GUID> results = new HashSet<>();

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("bindingCache = " + bindingCache);
				logger.debug("bindingCache.loadBinding() = " + bindingCache.loadBinding());
			}
			UserTypeCode userType = getUserTypeCode(user);

			switch (userType.getCodeValue()) {
			case "GOV": {
				for (GUID guid : groupGuids) {

					if ("RULE - GOVERNMENT".equals(guid.toNativeGUIDString())) {
						results.add(guid);
					}
				}
				break;
			}
			case "BUP": {
				for (GUID guid : groupGuids) {

					if ("RULE - BUSINESS PARTNER".equals(guid.toNativeGUIDString())) {
						results.add(guid);
					}
				}
				break;
			}
			case "VIN": {
				for (GUID guid : groupGuids) {

					if ("RULE - VERIFIED INDIVIDUAL".equals(guid.toNativeGUIDString())) {
						results.add(guid);
					}
				}
				break;
			}
			case "UIN": {
				for (GUID guid : groupGuids) {

					if ("RULE - INDIVIDUAL".equals(guid.toNativeGUIDString())) {
						results.add(guid);
					}
				}
				break;
			}
			case "BCS": {
				for (GUID guid : groupGuids) {

					if ("RULE - BC SERVICES CARD".equals(guid.toNativeGUIDString())) {
						results.add(guid);
					}
				}
				break;
			}
			default:
				return new GUID[0];
			}

			GUID[] userInGroups = bindingCache.loadBinding().userInGroups(user, groupGuids);

			for (GUID guid : userInGroups) {
				results.add(guid);
			}
		} catch (WebADEDeveloperException e) {
			throw new WebADEUserProviderException(e.getMessage(), e);
		}
		if (logger.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("User " + user + " in groups:");
			for (GUID guid : results) {
				sb.append(" ");
				sb.append(guid);
			}
			logger.info(sb.toString());
		}
		addDelay();
		return results.toArray(new GUID[] {});
	}

  @Override
public UserTypeCode getUserTypeForSourceDirectory(String sourceDirectory) {
    String userType = getUserType(sourceDirectory);
    return UserTypeCode.getUserTypeCode(userType);
  }

  private static void configureResponseDelay(Properties props) {
    // check for user-info-response-delay preference
    if (responseDelay == null) {
      responseDelay = props.getProperty(WebADEDeveloperUserProvider.RESPONSE_DELAY);
    }
    // check for user.info.response.delay param
    if (responseDelay == null) {
      responseDelay = props.getProperty(DeveloperConstants.RESPONSE_DELAY_PARAM);
    }
    if (logger.isInfoEnabled()) {
      logger.debug("Response Delay set to " + (responseDelay == null ? "0" : responseDelay) + "ms");
    }
  }

  private void configureUserInfo(Properties props) throws WebADEDeveloperException {
    // check for user-info-file-location preference
    if (getUserInfoFileLocation() == null) {
      String fileLocation = props.getProperty(WebADEDeveloperUserProvider.USER_INFO_FILE_LOCATION);
      setUserInfoFileLocation(fileLocation);
    }
    // check for user.info.file.location preference
    if (getUserInfoFileLocation() == null) {
      String fileLocation = props.getProperty(DeveloperConstants.USER_INFO_FILE_LOCATION_PARAM);
      setUserInfoFileLocation(fileLocation);
    }
    // set the binding cache
    bindingCache = new XMLFileBindingCache(getUserInfoFile());
    if (logger.isInfoEnabled()) {
      logger.info("Initializing Information Provider with file " + getUserInfoFile().getPath());
    }
  }

  public static File getUserInfoFile() throws WebADEDeveloperException {
    if (getUserInfoFileLocation() != null) {
      File file = new File(getUserInfoFileLocation());
      if (file.exists() && file.canRead()) {
        return file;
      }

      {
        throw new WebADEDeveloperException("File " + getUserInfoFileLocation() + " cannot be read.");
      }
    }

    {
      // Couldn't find the user info location
      throw new WebADEDeveloperException("The user info file location has not been specified.");
    }
  }

  /**
   * @see ca.bc.gov.webade.UserInfoProvider#handlesSourceDirectory(java.lang.String)
   */
  @Override
public final boolean handlesSourceDirectory(String sourceDirectory) {
    boolean handlesSourceDirectory = getUserType(sourceDirectory) != null;
    if (logger.isInfoEnabled()) {
      logger.info("Handles Source Directory " + sourceDirectory + " = " + handlesSourceDirectory);
    }
    return handlesSourceDirectory;
  }

  /**
   * @see ca.bc.gov.webade.UserInfoProvider#handlesUserType(ca.bc.gov.webade.user.UserTypeCode)
   */
  @Override
public final boolean handlesUserType(UserTypeCode userType) {
    boolean isHandled = false;
    if (properties != null) {
      String[] userTypes = getUserTypes();
      for (int i = 0; i < userTypes.length; i++) {
        if (userTypes[i].equalsIgnoreCase(userType.toString())) {
          isHandled = true;
          break;
        }
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Handles User Type " + userType.getCodeValue() + " = " + isHandled);
      }
    }
    return isHandled;
  }

  /**
   * @see ca.bc.gov.webade.UserInfoProvider#getSupportedSourceDirectories()
   */
  @Override
public final String[] getSupportedSourceDirectories() {
    String[] sourceDirectories = getSourceDirectories();
    if (logger.isDebugEnabled()) {
      StringBuffer sb = new StringBuffer();
      sb.append("Supported Source Directories:");
      for (int i = 0; i < sourceDirectories.length; i++) {
        sb.append(" ");
        sb.append(sourceDirectories[i]);
      }
      logger.debug(sb.toString());
    }
    return sourceDirectories;
  }

  /**
   * @see ca.bc.gov.webade.UserInfoProvider#getSupportedUserTypes()
   */
  @Override
public final UserTypeCode[] getSupportedUserTypes() {
    UserTypeCode[] codes = new UserTypeCode[] {};
    String[] userTypes = getUserTypes();
    codes = new UserTypeCode[userTypes.length];
    for (int i = 0; i < userTypes.length; i++) {
      codes[i] = UserTypeCode.getUserTypeCode(userTypes[i]);
    }
    if (logger.isDebugEnabled()) {
      StringBuffer sb = new StringBuffer();
      sb.append("Supported User Types:");
      for (int i = 0; i < codes.length; i++) {
        sb.append(" ");
        sb.append(codes[i]);
      }
      logger.debug(sb.toString());
    }
    return codes;
  }

  /**
   * @see ca.bc.gov.webade.user.provider.WebADEUserProvider#getSearchableUserTypes()
   */
  @Override
public UserTypeCode[] getSearchableUserTypes()
			throws WebADEUserProviderException {
 
  	UserTypeCode[] results = new UserTypeCode[]{
  			UserTypeCode.GOVERNMENT,
  			UserTypeCode.BUSINESS_PARTNER,
  			UserTypeCode.VERIFIED_INDIVIDUAL,
  			UserTypeCode.INDIVIDUAL
  	};

      return results;
	}

  private String[] getUserTypes() {
    String[] userTypes = new String[] {};
    if (properties != null) {
      userTypes = properties.getProperty(WebADEPreferences.PROVIDER_SUPPORTED_TYPES).split(USER_TYPE_DELINIATOR);
    }
    return userTypes;
  }

  private String[] getSourceDirectories() {
    String[] result = new String[] {};
    if (properties != null) {
      Set<String> set = new HashSet<String>();
      String[] userTypes = getUserTypes();
      for (int i = 0; i < userTypes.length; i++) {
        String[] sourceDirectories = getSourceDirectories(userTypes[i]);
        for (int j = 0; j < sourceDirectories.length; j++) {
          set.add(sourceDirectories[j]);
        }
      }
      result = set.toArray(new String[set.size()]);
    }
    return result;
  }

  private String[] getSourceDirectories(String userType) {
    String[] result = new String[] {};
    if (properties != null) {
      String key = userType + USER_TYPE_DOMAIN_SUFFIX;
      result = properties.getProperty(key).split(USER_TYPE_DELINIATOR);
    }
    return result;
  }

  private WebADEUserInfo getUntypedUser(UserCredentials credentials, String requestorUserType) throws WebADEDeveloperException {
    WebADEUserInfo foundUser;
    UserCredentials newCredentials;
    newCredentials = new UserCredentials(credentials);
    newCredentials.setUserTypeCode(UserTypeCode.GOVERNMENT);
    foundUser = bindingCache.loadBinding().getGovernmentUser(newCredentials, requestorUserType);

    newCredentials = new UserCredentials(credentials);
    newCredentials.setUserTypeCode(UserTypeCode.BUSINESS_PARTNER);
    WebADEUserInfo newUser = bindingCache.loadBinding().getBusinessPartnerUser(newCredentials);
    if (foundUser == null) {
      foundUser = newUser;
    }
    else if (newUser != null) {
      throw new WebADEDeveloperException("Found multiple users with the given " + "credentials when only one is expected.");
    }

    newCredentials = new UserCredentials(credentials);
    newCredentials.setUserTypeCode(UserTypeCode.INDIVIDUAL);
    newUser = bindingCache.loadBinding().getIndividualUser(newCredentials);
    if (foundUser == null) {
      foundUser = newUser;
    }
    else if (newUser != null) {
      throw new WebADEDeveloperException("Found multiple users with the given " + "credentials when only one is expected.");
    }
    return foundUser;
  }

  private static void validateManadatory(Properties props, String key) throws WebADEDeveloperException {
    if (props.getProperty(key) == null) {
      throw new WebADEDeveloperException("WebADE Developer Module User Provider is missing the required configuration property '" + key + "'.");
    }
  }

  /**
   * Check that supported-user-types is set and a corresponding
   * X-user-type-domains exists.
   * 
   * @param props
   * @throws WebADEDeveloperException
   */
  private void validateSupportedTypes(Properties props) throws WebADEDeveloperException {
    validateManadatory(props, WebADEPreferences.PROVIDER_SUPPORTED_TYPES);
    String[] supportedTypes = getUserTypes();
    for (int x = 0; x < supportedTypes.length; x++) {
      validateManadatory(props, supportedTypes[x] + USER_TYPE_DOMAIN_SUFFIX);
    }
  }

  private String getUserType(String sourceDirectory) {
    String[] userTypes = getUserTypes();
    for (int i = 0; i < userTypes.length; i++) {
      String[] sourceDirectories = getSourceDirectories(userTypes[i]);
      for (int j = 0; j < sourceDirectories.length; j++) {
        if (sourceDirectories[j].equalsIgnoreCase(sourceDirectory)) {
          return userTypes[i];
        }
      }
    }
    return null;
  }

  private UserTypeCode getUserTypeCode(UserCredentials credentials) {
    UserTypeCode requestingUserTypeCode = null;
    if (credentials != null) {
      requestingUserTypeCode = credentials.getUserTypeCode();
    }
    if (requestingUserTypeCode == null) {
      requestingUserTypeCode = UserTypeCode.getUserTypeCode(getUserType(credentials.getSourceDirectory()));
    }
    return requestingUserTypeCode;
  }

  private static void addDelay() {
    long delay = 0;
    if (getResponseDelay() != null) {
      try {
        delay = Long.parseLong(getResponseDelay());
        if (delay > 0) {
          if (logger.isDebugEnabled()) {
            logger.debug("delaying response by " + delay + "ms");
          }
          try {
            Thread.sleep(delay);
          }
          catch (InterruptedException e) {
            // no op
          }
        }
      }
      catch (NumberFormatException e) {
        logger.error(e.getMessage(), e);
      }

    }
  }

  public static String getResponseDelay() {
    return responseDelay;
  }

  public static void setResponseDelay(String responseDelay) {
    if (logger.isDebugEnabled()) {
      logger.debug("setResponseDelay: " + responseDelay);
    }
    WebADEDeveloperUserProvider.responseDelay = responseDelay;
  }

  public static String getUserInfoFileLocation() {
    String result = userInfoFileLocation;
    // if userInfoFileLocation is 'default' then read file from jar to temp
    // location.
    if ("default".equalsIgnoreCase(result)) {
      if (tempFile == null) {
        tempFile = IOUtils.createTempFileFromClasspath(DeveloperConstants.XML_USER_INFO_FILE_NAME, DeveloperConstants.XML_FILE_SUFFIX);
        if (logger.isInfoEnabled()) {
          logger.info("WebADE will use temp file " + tempFile + " to retrieve user information.");
        }
      }
      result = tempFile;
    }
    return result;
  }

  public static void setUserInfoFileLocation(String userInfoFileLocation) {
    if (logger.isDebugEnabled()) {
      logger.debug("setUserInfoFileLocation: " + userInfoFileLocation);
    }
    WebADEDeveloperUserProvider.userInfoFileLocation = userInfoFileLocation;
  }

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		String value = "WebADEDeveloperUserProvider[ ";
        String supportedTypes = "{ ";
        
        
        String[] userTypes = getUserTypes();
        for(int x=0;x<userTypes.length;++x) {
        	 String currentType = userTypes[x];
             if (x > 0) {
                 supportedTypes += ", ";
             }
             supportedTypes += currentType;
        }
        supportedTypes += " }";
        value += "supportedTypes=" + supportedTypes;
		return value;
	}

}
