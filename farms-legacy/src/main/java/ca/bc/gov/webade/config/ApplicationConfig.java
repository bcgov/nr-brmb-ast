package ca.bc.gov.webade.config;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.preferences.MultiValueWebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.provider.WebADEUserProvider;

/**
 * @author jross
 */
public final class ApplicationConfig implements Serializable {
    private static final long serialVersionUID = -54050242548198627L;

    private long userMaxCacheTime;
    private long groupMaxCacheTime;
    private HashSet<String> extensions = new HashSet<String>();
    private WebADEPreferences internalWebADEPreferences;
	
	private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

    /**
     * Creates a new instance of the ApplicationConfig class with the given
     * preferences.
     * 
     * @param preferences
     *            The set of preferences related to the target application.
     * @throws WebADEException
     *             Thrown when the WebADE configuration is incorrect.
     */
    public ApplicationConfig(WebADEPreferences preferences) throws WebADEException {
        this.internalWebADEPreferences = (WebADEPreferences)preferences.clone();

        try {
            this.userMaxCacheTime = Long.parseLong(getPreferenceValue(internalWebADEPreferences,
            		WebADEPreferences.USER_MAX_CACHE_TIME_PREF_NAME));
        } catch (NumberFormatException e) {
            throw new WebADEException("Preference value "
                    + WebADEPreferences.USER_MAX_CACHE_TIME_PREF_NAME + " is not a number.");
        }

        try {
            this.groupMaxCacheTime = Long.parseLong(getPreferenceValue(internalWebADEPreferences,
            		WebADEPreferences.GROUP_MAX_CACHE_TIME_PREF_NAME));
        } catch (NumberFormatException e) {
            throw new WebADEException("Preference value "
                    + WebADEPreferences.GROUP_MAX_CACHE_TIME_PREF_NAME + " is not a number.");
        }

        WebADEPreferenceSet extensionsPrefSet = preferences.getPreferenceSet(
        		WebADEPreferences.APPLICATION_CONFIG_SUBTYPE, WebADEPreferences.EXTENSIONS_PREF_SET_NAME);
        if (extensionsPrefSet == null) {
            log.debug("No extensions found for application.");
        } else {
            WebADEPreference ext = extensionsPrefSet
                    .getPreference(WebADEPreferences.EXTENSION_PREF_NAME);
            String[] extensionList;
            if (ext instanceof MultiValueWebADEPreference) {
                List<String> valueList = ((MultiValueWebADEPreference)ext).getPreferenceValues();
                extensionList = valueList.toArray(new String[valueList.size()]);
            } else {
                extensionList = new String[] { ext.getPreferenceValue() };
            }
            if (extensionList.length == 0) {
                log.debug("No extensions found for application.");
            }
            for (int x = 0; x < extensionList.length; x++) {
                log.debug("Adding extension '" + extensionList[x] + "' for application.");
                this.extensions.add(extensionList[x]);
            }
        }
    }

    /**
     * Returns the set of extensions to load for this application.
     * 
     * @return A Set of String objects.
     */
    public Set<String> getExtensions() {
        return extensions;
    }

    /**
     * @return Returns the userMaxCacheTime.
     */
    public long getUserMaxCacheTime() {
        return userMaxCacheTime;
    }

    /**
     * @return Returns the groupMaxCacheTime.
     */
    public long getGroupMaxCacheTime() {
        return groupMaxCacheTime;
    }

    /**
     * @return Returns the internal WebADEPreferences.
     */
    public WebADEPreferences getInternalWebADEPreferences() {
        return internalWebADEPreferences;
    }

    /**
     * Returns the set of user-provider configuration preferences.
     * 
     * @return An array-list of WebADEPreferenceSet objects for each user-provider.
     */
    public List<WebADEPreferenceSet> getUserProviderWebADEPreferenceSets() {
        List<WebADEPreferenceSet> preferences = this.internalWebADEPreferences.getPreferenceSetsBySubType(WebADEUserProvider.WEBADE_USER_PROVIDER_SUBTYPE);
        if (preferences == null || preferences.size() == 0) {
            log.debug("No preferences found with preference sub type '"
                    + WebADEUserProvider.WEBADE_USER_PROVIDER_SUBTYPE
                    + "'. Loading user provider with legacy sub type '"
                    + WebADEPreferences.USER_PROVIDER_SUBTYPE + "'");
            preferences = this.internalWebADEPreferences
                    .getPreferenceSetsBySubType(WebADEPreferences.USER_PROVIDER_SUBTYPE);
        } else {
            log.debug("Found user provider with preference sub type '"
                    + WebADEUserProvider.WEBADE_USER_PROVIDER_SUBTYPE + "'");
        }
        return preferences;
    }

    public static final String SERVICE_CLIENT_PROVIDER_SUBTYPE = "webade-service-client-provider";

	public static final String SERVICE_CLIENT_PROVIDER_SETNAME = "webade-service-client-provider";
    
    /**
     * Returns the set of webade-service-client-provider configuration preferences.
     * 
     * @return A WebADEPreferenceSet object.
     */
    public WebADEPreferenceSet getServiceClientProviderWebADEPreferenceSet() {
    	
        WebADEPreferenceSet preferenceSet = this.internalWebADEPreferences.getPreferenceSet(SERVICE_CLIENT_PROVIDER_SUBTYPE, SERVICE_CLIENT_PROVIDER_SETNAME);
        
        return preferenceSet;
    }

    private String getPreferenceValue(WebADEPreferences preferences, String preferenceName)
            throws WebADEException {
        String prefValue = null;
        WebADEPreference pref = preferences.getPreference(WebADEPreferences.APPLICATION_CONFIG_SUBTYPE,
                preferenceName);
        if (pref == null) {
            throw new WebADEException("Could not locate " + preferenceName
                    + " application attribute.");
        } else if (pref instanceof MultiValueWebADEPreference) {
            throw new WebADEException("Multiple preference values defined for " + preferenceName
                    + " application attribute.");
        } else {
            prefValue = pref.getPreferenceValue();
            log.debug("Preference: " + preferenceName + " Value: " + prefValue);
        }
        return prefValue;
    }
}