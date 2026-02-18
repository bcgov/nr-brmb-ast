package ca.bc.gov.webade;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.webade.config.ApplicationConfig;
import ca.bc.gov.webade.database.DatabaseUserCredentials;
import ca.bc.gov.webade.json.JsonAction;
import ca.bc.gov.webade.json.JsonApplicationConfiguration;
import ca.bc.gov.webade.json.JsonApplicationPreference;
import ca.bc.gov.webade.json.JsonRole;
import ca.bc.gov.webade.json.JsonWdePreference;
import ca.bc.gov.webade.preferences.DefaultWebADEPreference;
import ca.bc.gov.webade.preferences.DefaultWebADEPreferenceSet;
import ca.bc.gov.webade.preferences.DefaultWebADEPreferences;
import ca.bc.gov.webade.preferences.MultiValueWebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreferenceTypeFactory;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.security.WebADESecurityManager;
import ca.bc.gov.webade.user.DefaultGovernmentUserInfo;
import ca.bc.gov.webade.user.DefaultWebADEUserPermissions;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.user.WebADEUserUtils;
import ca.bc.gov.webade.user.provider.WebADEUserProvider;
import ca.bc.gov.webade.user.provider.WebADEUserProviderException;
import ca.bc.gov.webade.user.security.enterprise.AuthAndUnauthSiteminderConfiguration;
import ca.bc.gov.webade.user.security.enterprise.SecurityConfiguration;

public abstract class WebADEDatabaseDatastore implements WebADEDatastore, Serializable {

    private JsonApplicationConfiguration applicationConfiguration = new JsonApplicationConfiguration();

    protected static final long NULL_EUSER_ID = 0;

    private static final Logger log = LoggerFactory.getLogger(WebADEDatabaseDatastore.class);

    private String appCode;
    private Map<UserTypeCode, WebADEUserProvider> userProviderMapByUserType;
    private Map<String, WebADEUserProvider> userProviderMapBySourceDirectory;
    private ApplicationConfig appConfig;
    private ArrayList<Role> roles;

    public WebADEDatabaseDatastore() {
        try (InputStream is = WebADEDatabaseDatastore.class
                .getResourceAsStream("/config/applicationConfiguration.json")) {
            if (is != null) {
                applicationConfiguration = JsonUtils.getJsonObjectMapper()
                        .readValue(is, JsonApplicationConfiguration.class);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load applicationConfiguration.json", e);
        }
    }

    void init(String applicationCode) throws WebADEException {
        this.appCode = applicationCode.trim().toUpperCase();

        this.roles = null; // Reset the roles cache.
        this.userProviderMapByUserType = new HashMap<UserTypeCode, WebADEUserProvider>();
        this.userProviderMapBySourceDirectory = new HashMap<String, WebADEUserProvider>();
        WebADEPreferences wdePrefs = getWebADEPreferences();
        this.appConfig = new ApplicationConfig(wdePrefs);

        List<WebADEPreferenceSet> providerPrefs = this.appConfig
                .getUserProviderWebADEPreferenceSets();

        for (Iterator<WebADEPreferenceSet> i = providerPrefs.iterator(); i.hasNext();) {
            WebADEPreferenceSet currentSettings = i
                    .next();

            WebADEPreference classPref = currentSettings
                    .getPreference(WebADEPreferences.PROVIDER_CLASS_NAME);
            if (classPref == null) {
                log.error("User provider '"
                        + currentSettings.getPreferenceSetName()
                        + "' missing mandatory preference '"
                        + WebADEPreferences.PROVIDER_CLASS_NAME + "'");
                // Skip this provider if there is no class defined
                continue;
            } else if (classPref instanceof MultiValueWebADEPreference) {
                log.error("User provider '"
                        + currentSettings.getPreferenceSetName()
                        + "' has multiple values for "
                        + "mandatory preference '"
                        + WebADEPreferences.PROVIDER_CLASS_NAME + "'");
                // Skip this provider if there is more than one class
                // defined
                continue;
            }

            boolean isEnabled = true;
            WebADEPreference enabledPref = currentSettings
                    .getPreference(WebADEPreferences.PROVIDER_ENABLED);
            if (enabledPref != null
                    && !(enabledPref instanceof MultiValueWebADEPreference)) {
                String isEnabledString = enabledPref.getPreferenceValue();
                if (isEnabledString != null
                        && isEnabledString.equals("false")) {
                    isEnabled = false;
                }
            }
            if (!isEnabled) {
                log.info("UserInfoProvider '"
                        + currentSettings.getPreferenceSetName()
                        + "' is disabled.");
            } else {

                WebADEUserProvider webADEUserProvider = loadUserProvider(currentSettings);
                if (webADEUserProvider != null) {
                    try {
                        UserTypeCode[] userTypes = webADEUserProvider
                                .getSupportedUserTypes();

                        for (int j = 0; j < userTypes.length; ++j) {

                            log.debug("checking " + userTypes[j]);
                            if (this.userProviderMapByUserType.get(userTypes[j]) == null) {
                                log.debug(" adding " + userTypes[j] + " provider " + webADEUserProvider);
                                this.userProviderMapByUserType.put(userTypes[j], webADEUserProvider);
                            } else {
                                throw new WebADEException(
                                        "Multiple user providers are configured "
                                                + "in the WebADE database for "
                                                + userTypes[j]
                                                + ".  Only one "
                                                + "user provider is allowed per user type.  A single provider can support multiple user types.");
                            }

                            String sourceDirectory = webADEUserProvider.getSourceDirectoryForUserType(userTypes[j]);
                            log.debug("checking " + sourceDirectory);
                            if (this.userProviderMapBySourceDirectory.get(sourceDirectory) == null) {
                                this.userProviderMapBySourceDirectory.put(sourceDirectory, webADEUserProvider);
                            } else {
                                throw new WebADEException(
                                        "Multiple user types are configured "
                                                + "in the WebADE database for "
                                                + sourceDirectory
                                                + ".  Only one "
                                                + "user type is allowed per source directory.  A single provider can support multiple source directories.");
                            }
                        }
                    } catch (WebADEUserProviderException e) {
                        throw new WebADEException(e);
                    }
                }
            }
        }
    }

    private WebADEUserProvider loadUserProvider(
            WebADEPreferenceSet currentSettings) {
        WebADEUserProvider provider = null;

        WebADEPreference classPref = currentSettings.getPreference(WebADEPreferences.PROVIDER_CLASS_NAME);
        String className = classPref.getPreferenceValue();
        try {
            Class<?> providerClass = Class.forName(className);
            Object instance = providerClass.getConstructor(new Class[0])
                    .newInstance(new Object[0]);
            if (instance instanceof WebADEUserProvider) {
                WebADEUserProvider providerInstance = (WebADEUserProvider) instance;
                providerInstance.init(currentSettings.getPreferencesProperties());
                provider = providerInstance;
            } else {
                throw new WebADEException("Unsupported UserProvider type: " + className);
            }
        } catch (ClassNotFoundException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (IllegalArgumentException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (SecurityException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (InstantiationException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (IllegalAccessException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (InvocationTargetException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (NoSuchMethodException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (WebADEException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        } catch (WebADEUserProviderException e) {
            log.error("Error occurred while initializing "
                    + "UserInfoProvider from class '" + className + "'", e);
        }
        return provider;
    }

    @Override
    public WebADEPreferences getWebADEApplicationPreferences()
            throws WebADEException {
        List<JsonApplicationPreference> applicationPreferences = applicationConfiguration.getApplicationPreferences();
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.APPLICATION);

        // loop through the application preferences and populate the WebADEPreferences
        // object
        for (JsonApplicationPreference applicationPreference : applicationPreferences) {

            // extract the relevant information from the applicationPreference object
            String preferenceSubType = applicationPreference.getSubTypeCode();
            String preferenceSetName = applicationPreference.getSetName();
            String prefName = applicationPreference.getName();
            WebADEPreference preference = new DefaultWebADEPreference(prefName);
            String prefValue = applicationPreference.getValue();
            preference.setPreferenceValue(prefValue);

            // add the preference to the appropriate preference set in the WebADEPreferences
            // object
            WebADEPreferenceSet prefSet = preferences.getPreferenceSet(preferenceSubType, preferenceSetName);
            if (prefSet == null) {
                prefSet = new DefaultWebADEPreferenceSet(preferenceSetName);
                preferences.addPreferenceSet(preferenceSubType, prefSet);
            }
            prefSet.addPreference(preference);
        }

        return preferences;
    }

    @Override
    public WebADEPreferences getWebADEUserPreferences(
            UserCredentials userCredentials) throws WebADEException {
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.APPLICATION);
        return preferences;
    }

    @Override
    public WebADEPreferences getWebADEGlobalPreferences()
            throws WebADEException {
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.GLOBAL);
        return preferences;
    }

    WebADEPreferences getWebADEPreferences() throws WebADEException {
        List<JsonWdePreference> wdePreferences = applicationConfiguration.getWdePreferences();
        WebADEPreferences preferences = new DefaultWebADEPreferences(WebADEPreferenceTypeFactory.WEBADE);

        // loop through the application preferences and populate the WebADEPreferences
        // object
        for (JsonWdePreference wdePreference : wdePreferences) {

            // extract the relevant information from the wdePreference object
            String preferenceSubType = wdePreference.getSubTypeCode();
            String preferenceSetName = wdePreference.getSetName();
            String prefName = wdePreference.getName();
            WebADEPreference preference = new DefaultWebADEPreference(prefName);
            String prefValue = wdePreference.getValue();
            preference.setPreferenceValue(prefValue);

            // add the preference to the appropriate preference set in the WebADEPreferences
            // object
            if (preferenceSetName == null) {
                preferences.addPreference(preferenceSubType, preference);
            } else {
                WebADEPreferenceSet prefSet = preferences.getPreferenceSet(preferenceSubType, preferenceSetName);
                if (prefSet == null) {
                    prefSet = new DefaultWebADEPreferenceSet(preferenceSetName);
                    preferences.addPreferenceSet(preferenceSubType, prefSet);
                }
                prefSet.addPreference(preference);
            }
        }
        return preferences;
    }

    @Override
    public final String getApplicationCode() {
        return this.appCode;
    }

    @Override
    public final Role[] getApplicationRoles() throws WebADEException {
        List<JsonAction> jsonActions = applicationConfiguration.getActions();
        Map<String, Action> actionMap = new HashMap<>();

        // loop through the jsonActions and populate the actionMap
        for (JsonAction jsonAction : jsonActions) {
            String actionName = jsonAction.getName();
            Boolean isPriviledged = jsonAction.getPrivilegedInd();
            Action currentAction = new Action(actionName, isPriviledged);
            actionMap.put(actionName, currentAction);
        }

        List<JsonRole> jsonRoles = applicationConfiguration.getRoles();
        this.roles = new ArrayList<Role>();

        // loop through the jsonRoles and populate the roles list
        for (JsonRole jsonRole : jsonRoles) {
            String roleName = jsonRole.getName();
            List<Action> actions = new ArrayList<>();
            for (String actionName : jsonRole.getActionNames()) {
                if (actionMap.containsKey(actionName)) {
                    Action currentAction = actionMap.get(actionName);
                    actions.add(currentAction);
                }
            }
            Role role = new Role(roleName, actions.toArray(new Action[actions.size()]));
            this.roles.add(role);
        }

        return this.roles.toArray(new Role[this.roles.size()]);
    }

    @Override
    public final WebADEUserPermissions getWebADEUserPermissions(
            UserCredentials givenCredentials) throws WebADEException {
        return getWebADEUserPermissions(givenCredentials, false, false);
    }

    @Override
    public WebADEUserPermissions getWebADEUserPermissions(UserCredentials givenCredentials, boolean ignoreSessionCache)
            throws WebADEException {
        return getWebADEUserPermissions(givenCredentials, ignoreSessionCache, false);
    }

    public WebADEUserPermissions getWebADEUserPermissions(UserCredentials givenCredentials, boolean ignoreSessionCache,
            boolean isAnonymous) throws WebADEException {
        log.trace("> getWebADEUserPermissions");
        isInitialized();

        WebADEUserPermissions perms;
        // Don't compare creds, if instructed to ignore the session cache object.
        boolean credsMatch = (ignoreSessionCache) ? false : compareCredsToCurrentUser(givenCredentials);
        UserCredentials currentUser = fetchCurrentUserCredentials();
        if (credsMatch && currentUser != null) {
            perms = WebADESecurityManager.getWebADESecurityManager(this.appCode).getCurrentUserPermissions();
        } else if (UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.equals(givenCredentials)) {
            perms = getPublicWebADEPermissions();
        } else {
            UserCredentials loadedCredentials = loadUserCredentialsFromDatabase(givenCredentials);
            if (loadedCredentials == null) {
                log.trace("Could not locate user for given credentials '"
                        + givenCredentials + "'.");
                perms = null;
            } else {
                log.trace("Loading user '" + loadedCredentials
                        + "' permissions from database.");
                perms = loadWebADEUserPermissions(loadedCredentials);
            }
        }
        log.trace("< getWebADEUserPermissions");
        return perms;
    }

    protected final boolean isInitialized() throws WebADEException {
        if (this.appCode == null) {
            throw new WebADEException(
                    "WebADEDatabaseDatastore class not properly initialized.  Missing application code value.");
        }
        return true;
    }

    private boolean compareCredsToCurrentUser(UserCredentials givenCredentials) {
        boolean credsMatch;
        UserCredentials currentCreds = fetchCurrentUserCredentials();
        if (givenCredentials == null || currentCreds == null) {
            credsMatch = false;
        } else {
            credsMatch = currentCreds.equals(givenCredentials);
        }
        return credsMatch;
    }

    private UserCredentials fetchCurrentUserCredentials() {
        WebADESecurityManager manager = WebADESecurityManager.getWebADESecurityManager(this.appCode);
        UserCredentials requestingCreds = manager.getCurrentUserCredentials();
        return requestingCreds;
    }

    @Override
    public final WebADEUserPermissions getPublicWebADEPermissions()
            throws WebADEException {
        isInitialized();
        WebADEUserPermissions permissions = new DefaultWebADEUserPermissions(
                UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS,
                new ArrayList<Role>(), new HashMap<Organization, ArrayList<Role>>());
        return permissions;
    }

    private UserCredentials loadUserCredentialsFromDatabase(UserCredentials credentials)
            throws WebADEException {
        DatabaseUserCredentials databaseUserCredentials = new DatabaseUserCredentials();
        databaseUserCredentials.setUserTypeCode(credentials.getUserTypeCode());
        databaseUserCredentials.setEUserId(NULL_EUSER_ID);
        databaseUserCredentials.setAccountName(credentials.getAccountName());
        databaseUserCredentials.setSourceDirectory(credentials.getSourceDirectory());
        databaseUserCredentials.setUserGuid(credentials.getUserGuid());
        databaseUserCredentials.setUpdatedDate(new Date());

        GUID userGuid = databaseUserCredentials.getUserGuid();
        if (userGuid.equals(new GUID("AHOPKINS000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(6L);
            databaseUserCredentials.setAccountName("AHOPKINS");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("BGRABLE0000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(7L);
            databaseUserCredentials.setAccountName("BGRABLE");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("BPITT000000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(8L);
            databaseUserCredentials.setAccountName("BPITT");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("CEASTWOOD00000000000000000000000"))) {
            databaseUserCredentials.setEUserId(9L);
            databaseUserCredentials.setAccountName("CEASTWOOD");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("DLETTERMAN0000000000000000000000"))) {
            databaseUserCredentials.setEUserId(10L);
            databaseUserCredentials.setAccountName("DLETTERMAN");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("DMOORE00000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(11L);
            databaseUserCredentials.setAccountName("DMOORE");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("ETAYLOR0000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(12L);
            databaseUserCredentials.setAccountName("ETAYLOR");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("FASTAIRE000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(13L);
            databaseUserCredentials.setAccountName("FASTAIRE");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("GGARBO00000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(14L);
            databaseUserCredentials.setAccountName("GGARBO");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("HFORD000000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(15L);
            databaseUserCredentials.setAccountName("HFORD");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("JCHAN000000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(17L);
            databaseUserCredentials.setAccountName("JCHAN");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("UTHRUMAN000000000000000000000000"))) {
            databaseUserCredentials.setEUserId(30L);
            databaseUserCredentials.setAccountName("UTHURMAN");
            databaseUserCredentials.setSourceDirectory("IDIR");
        } else if (userGuid.equals(new GUID("OAUTHGOV100000000000000000000000"))) {
            databaseUserCredentials.setEUserId(503L);
        } else if (userGuid.equals(new GUID("OAUTHGOV200000000000000000000000"))) {
            databaseUserCredentials.setEUserId(504L);
        }

        return databaseUserCredentials;
    }

    private WebADEUserPermissions loadWebADEUserPermissions(
            UserCredentials credentials) throws WebADEException {
        ArrayList<WebADEUserPermissions> userAuths = new ArrayList<WebADEUserPermissions>();

        if (credentials instanceof DatabaseUserCredentials
                && ((DatabaseUserCredentials) credentials).getEUserId() != NULL_EUSER_ID) {
            WebADEUserPermissions personalAuths = loadUserPersonalPermissions((DatabaseUserCredentials) credentials);
            userAuths.add(personalAuths);
        } else {
            // Adding an empty permissions object ensures that the user's
            // credentials get added to the merged permissions object.
            WebADEUserPermissions personalAuths = new DefaultWebADEUserPermissions(
                    credentials, new ArrayList<Role>(), new HashMap<Organization, ArrayList<Role>>());
            userAuths.add(personalAuths);
        }

        WebADEUserPermissions auths = WebADEUserUtils
                .mergeWebADEUserPermissions(userAuths
                        .toArray(new WebADEUserPermissions[userAuths.size()]));
        auths.getUserCredentials().setReadOnly();
        return auths;
    }

    private WebADEUserPermissions loadUserPersonalPermissions(
            DatabaseUserCredentials credentials) throws WebADEException {
        // build role map
        Role[] appRoles = getApplicationRoles();
        Map<String, Role> roleMap = new HashMap<>();
        for (Role role : appRoles) {
            roleMap.put(role.getName(), role);
        }

        // build user role map
        Map<Long, ArrayList<Role>> userRoleMap = new HashMap<Long, ArrayList<Role>>() {
            {
                put(6L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("ADMIN"));
                        add(roleMap.get("TIP_REPORT_ADMIN"));
                    }
                });
                put(7L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("INBOX_VIEWER"));
                        add(roleMap.get("SENIOR_VERIFIER"));
                    }
                });
                put(8L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("VERIFIER"));
                        add(roleMap.get("CLIENT"));
                        add(roleMap.get("TIP_REPORT_ADMIN"));
                        add(roleMap.get("INBOX_VIEWER"));
                        add(roleMap.get("SENIOR_VERIFIER"));
                    }
                });
                put(9L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("VERIFIER"));
                        add(roleMap.get("INBOX_VIEWER"));
                    }
                });
                put(10L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("VERIFIER"));
                        add(roleMap.get("INBOX_VIEWER"));
                    }
                });
                put(11L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("TIP_REPORT_USER"));
                    }
                });
                put(12L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("PROGRAM_ANALYST"));
                        add(roleMap.get("NEW_PARTICIPANT_ADMIN"));
                    }
                });
                put(13L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("INBOX_VIEWER"));
                        add(roleMap.get("DATA_ADMIN"));
                    }
                });
                put(14L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("STAFF"));
                        add(roleMap.get("INBOX_VIEWER"));
                    }
                });
                put(15L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("STAFF"));
                        add(roleMap.get("INBOX_VIEWER"));
                    }
                });
                put(17L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("TIP_REPORT_ADMIN"));
                    }
                });
                put(30L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("STAFF"));
                    }
                });
                put(503L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("CLIENT"));
                    }
                });
                put(504L, new ArrayList<Role>() {
                    {
                        add(roleMap.get("CLIENT"));
                    }
                });
            }
        };

        ArrayList<Role> rolesNonSecured = userRoleMap.get(credentials.getEUserId());
        HashMap<Organization, ArrayList<Role>> userRolesWithOrgs = new HashMap<Organization, ArrayList<Role>>();
        WebADEUserPermissions auths = new DefaultWebADEUserPermissions(
                credentials, rolesNonSecured, userRolesWithOrgs);
        return auths;
    }

    @Override
    public SecurityConfiguration getSecurityConfiguration() throws WebADEException {

        SecurityConfiguration result = null;

        WebADEPreferences wdePrefs = this.getWebADEPreferences();
        Properties properties = wdePrefs.getPreferencePropertiesBySubType(WebADEPreferences.SECURITY_PROVIDER_SUB_TYPE);

        result = new AuthAndUnauthSiteminderConfiguration(properties);
        result.initialize();

        return result;
    }

    @Override
    public final boolean isApplicationEnabled() throws WebADEException {
        return true;
    }

    @Override
    public final WebADEUserInfo getWebADEUserInfo(UserCredentials givenCredentials)
            throws WebADEException {
        return getWebADEUserInfo(givenCredentials, false, false);
    }

    @Override
    public final WebADEUserInfo getWebADEUserInfo(UserCredentials givenCredentials, boolean ignoreSessionCache)
            throws WebADEException {
        return getWebADEUserInfo(givenCredentials, ignoreSessionCache, false);
    }

    private final WebADEUserInfo getWebADEUserInfo(UserCredentials givenCredentials, boolean ignoreSessionCache,
            boolean isAnonymous)
            throws WebADEException {
        if (givenCredentials instanceof DatabaseUserCredentials) {
            Long eUserId = ((DatabaseUserCredentials) givenCredentials).getEUserId();
            DefaultGovernmentUserInfo info = new DefaultGovernmentUserInfo();
            if (eUserId == 6L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Anthony");
                info.setLastName("Hopkins");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 7L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Betty");
                info.setLastName("Grable");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 8L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Brad");
                info.setLastName("Pitt");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 9L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Clint");
                info.setLastName("Eastwood");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 10L) {
                info.setEmployeeId("N");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("David");
                info.setLastName("Letterman");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 11L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Demi");
                info.setLastName("Moore");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 12L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Elizabeth");
                info.setLastName("Taylor");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 13L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Fred");
                info.setLastName("Astaire");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 14L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Greta");
                info.setLastName("Garbo");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 15L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Harrison");
                info.setLastName("Ford");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 17L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Jackie");
                info.setLastName("Chan");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 30L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Uma");
                info.setLastName("Thurman");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 503L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Oauth");
                info.setLastName("Gov");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            } else if (eUserId == 504L) {
                info.setEmployeeId("Y");
                info.setUserCredentials(givenCredentials);
                info.setFirstName("Oauth");
                info.setLastName("Gov");
                info.setMiddleInitial(null);
                info.setEmailAddress(null);
                info.setPhoneNumber(null);
            }
            return info;
        }

        return null;
    }
}
