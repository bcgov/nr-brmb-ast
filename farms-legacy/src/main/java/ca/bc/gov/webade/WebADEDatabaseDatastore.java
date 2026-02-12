package ca.bc.gov.webade;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.webade.config.ApplicationConfig;
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
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.provider.WebADEUserProvider;
import ca.bc.gov.webade.user.provider.WebADEUserProviderException;

public abstract class WebADEDatabaseDatastore implements WebADEDatastore, Serializable {

    private JsonApplicationConfiguration applicationConfiguration = new JsonApplicationConfiguration();

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
}
