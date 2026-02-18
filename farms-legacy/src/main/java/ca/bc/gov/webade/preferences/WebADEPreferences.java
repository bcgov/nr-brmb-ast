/**
 * @(#)WebADEPreferences.java
 * Copyright (c) 2006, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

import java.util.List;
import java.util.Properties;

import ca.bc.gov.webade.WebADEException;

/**
 * @author jross
 */
public interface WebADEPreferences {

    /** The application config sub-type. */
    public static final String USER_PROVIDER_SUBTYPE = "user-provider";
    /** The application config sub-type. */
    public static final String APPLICATION_CONFIG_SUBTYPE = "app-config";
    /** The extensions preference set name. */
    public static final String EXTENSIONS_PREF_SET_NAME = "extensions";
    /** The extension preference name. */
    public static final String EXTENSION_PREF_NAME = "extension";
    /** The max user cache time preference name. */
    public static final String USER_MAX_CACHE_TIME_PREF_NAME = "user-max-cache-time";
    /** The max group cache time preference name. */
    public static final String GROUP_MAX_CACHE_TIME_PREF_NAME = "group-max-cache-time";
    /**
     * The attribute name for the application preference defining agreements
     * that users must have agreed to before being given access to the WebADE
     * application.
     */
    public static final String WEBADE_AGREEMENT_NAME = "webade-agreement";
    
    /**
     * <code>AUTHENTICATED_ONLY_SITEMINDER</code> security configuration type.
     */
    public static final String AUTHENTICATED_ONLY_SITEMINDER = "authenticated-only-siteminder";
    
    /**
     * <code>AUTHENTICATED_AND_UNAUTHENTICATED_SITEMINDER</code> security configuration type.
     */
    public static final String AUTHENTICATED_AND_UNAUTHENTICATED_SITEMINDER = "authenticated-and-unauthenticated-siteminder";
    
    /**
     * <code>PARTIALLY_AUTHENTICATED_SITEMINDER</code> security configuration type.
     */
    public static final String PARTIALLY_AUTHENTICATED_SITEMINDER = "partially-authenticated-siteminder";    
    
    /**
     * <code>SECURITY_PROVIDER_SUB_TYPE</code> defines the WebADE preference sub-type name
     * containing the security provider settings for the application.
     */
    public static final String SECURITY_PROVIDER_SUB_TYPE = "webade-security-provider";
    
    /**
     * <code>SECURITY_PROVIDER_TYPE_PREFERENCE</code> defines the WebADE preference name
     * containing the provider type for the application.
     */
    public static final String SECURITY_PROVIDER_TYPE_PREFERENCE = "webade-security-provider-type";
    
    /** The user-provider class name preference name. */
    public static final String PROVIDER_CLASS_NAME = "class-name";
    
    /** The user-provider enabled preference name. */
    public static final String PROVIDER_ENABLED = "enabled";
    /** The user-provider supported user types preference name. */
    public static final String PROVIDER_SUPPORTED_TYPES = "supported-user-types";
    /**
     * The attribute name for the web page used to display agreements that a
     * user must accept to continue using the application.
     */
    public static final String WEBADE_AGREEMENT_PAGE = "webade-agreement-page";
    /**
     * The attribute name for the preference indicating whether the application
     * should check for the application enabled flag.
     */
    public static final String WEBADE_APPLICATION_CHECK_FOR_DISABLED_FLAG = "webade-application-check-for-disabled-flag";
    /**
     * The attribute name for the web page used when an application is marked as
     * disabled.
     */
    public static final String WEBADE_APPLICATION_DISABLED_PAGE = "webade-application-disabled-page";
    /**
     * The attribute name for the preference containing the application disabled message.
     * This preference is optional.  A standard disabled message will be used if this 
     * preference is not set.
     */
    public static final String WEBADE_APPLICATION_DISABLED_MESSAGE = "webade-application-disabled-message";
    
    /**
     * The attribute name for the flag used to allow a user with multiple
     * organizations for an application to select one as his default when
     * logging in to the application.
     */
    public static final String WEBADE_DEFAULT_ORGANIZATION_ENABLED_FLAG = "webade-default-organization-enabled";
    /**
     * The attribute name for the flag used to indicate whether to forego the
     * organization selection screen when a default organization is selected.
     */
    public static final String WEBADE_USE_DEFAULT_ORGANIZATION_ENABLED_FLAG = "webade-use-default-organization-enabled";
    /**
     * The attribute name for the indicator that defines which organizations
     * the user can select from when selecting a default organization.
     */
    public static final String WEBADE_DEFAULT_ORGANIZATION_SELECT_BY_ORGANIZATION_TYPE = "webade-default-organization-select-by-organization-type";
    /**
     * The attribute name for the web page used to allow a user with multiple
     * organizations to select one as his default when logging in to the
     * application.
     */
    public static final String WEBADE_DEFAULT_ORGANIZATION_SWITCH_PAGE = "webade-default-organization-switch-page";

    /**
     * @return Returns the preferenceTypeCode.
     */
    public WebADEPreferenceType getPreferenceType();

    /**
     * Returns all subtypes in the .
     * @return A List of WebADEPreference objects matching the sub-type.
     */
    public List<String> getPreferenceSubTypes();

    /**
     * Returns the target preference, identified by sub-type and name.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @param preferenceName
     *            The target preference name.
     * @return The target WebADEPreference object, or null if not found.
     */
    public WebADEPreference getPreference(String preferenceSubType,
            String preferenceName);

    /**
     * Returns the preferences that match the given subtype.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @return A List of WebADEPreference objects matching the sub-type.
     */
    public List<WebADEPreference> getPreferencesBySubType(String preferenceSubType);

    /**
     * Returns the preferences that match the given subtype as name value pairs
     * in a Properties object. Assumes that all preference objects with the
     * given subtype have at most one value assigned.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @return A Properties object of String name-value pairs.
     * @throws WebADEException
     *             Thrown if any of the WebADEPreference object with the given
     *             subtype have more than one value assigned.
     */
    public Properties getPreferencePropertiesBySubType(String preferenceSubType)
            throws WebADEException;

    /**
     * Returns the preferences that match the given subtype as name value pairs
     * in a Properties object. Assumes that all preference objects with the
     * given subtype have at most one value assigned.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @param convertMultiValuesToDelimitedString
     *            Flag to convert multi-value preferences to a single
     *            comma-delimited value in the properties file (if set to true),
     *            or whether to throw an exception (if set to false).
     * @return A Properties object of String name-value pairs.
     * @throws WebADEException
     *             Thrown if any of the WebADEPreference object with the given
     *             subtype have more than one value assigned.
     */
    public Properties getPreferencePropertiesBySubType(
            String preferenceSubType,
            boolean convertMultiValuesToDelimitedString) throws WebADEException;

    /**
     * Returns the preferences that match the given subtype as name value pairs
     * in a Properties object. Assumes that all preference objects with the
     * given subtype have at most one value assigned.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @param convertMultiValuesToDelimitedString
     *            Flag to convert multi-value preferences to a single delimited
     *            value in the properties file (if set to true), or whether to
     *            throw an exception (if set to false).
     * @param delimiterString
     *            The string used to delimit the multi-values string. The
     *            default value is a comma.
     * @return A Properties object of String name-value pairs.
     * @throws WebADEException
     *             Thrown if any of the WebADEPreference object with the given
     *             subtype have more than one value assigned.
     */
    public Properties getPreferencePropertiesBySubType(
            String preferenceSubType,
            boolean convertMultiValuesToDelimitedString, String delimiterString)
            throws WebADEException;

    /**
     * Returns the target preference set. Assumes that there will be only one
     * match for the given sub-type/set-name combination. If more than one match
     * is found, and exception is thrown.
     * @param preferenceSubType
     *            The sub-type containing the preference set.
     * @param preferenceSetName
     *            The target preference set name.
     * @return The target WebADEPreferenceSet object, or null if not found.
     */
    public WebADEPreferenceSet getPreferenceSet(String preferenceSubType,
            String preferenceSetName);

    /**
     * Returns the preference sets that match the given subtype.
     * @param preferenceSubType
     *            The sub-type containing the preference set.
     * @return A List of WebADEPreferenceSet objects matching the sub-type.
     */
    public List<WebADEPreferenceSet> getPreferenceSetsBySubType(String preferenceSubType);

    /**
     * Returns the WebADEPreference and WebADEPreferenceSet objects that match
     * the given subtype.
     * @param preferenceSubType
     *            The sub-type containing the preferences.
     * @return A WebADEPreferences object containing all preferences and
     *         preference sets matching the sub-type.
     */
    public WebADEPreferences getWebADEPreferencesBySubType(
            String preferenceSubType);

    /**
     * Adds the given WebADEPreference object to the list of preferences.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @param pref
     *            The target preference to add.
     * @throws NullPointerException
     *             Thrown if the preference sub type parameter was null.
     */
    public void addPreference(String preferenceSubType, WebADEPreference pref)
            throws NullPointerException;

    /**
     * Removes the given preference from the list of preferences.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @param preferenceName
     *            The name of the target preference.
     */
    public void removePreference(String preferenceSubType, String preferenceName);

    /**
     * Adds the given WebADEPreference object to the target set of preferences
     * identified by the set name.
     * @param preferenceSubType
     *            The sub-type containing the preference.
     * @param prefSet
     *            The target preference set to add.
     * @throws NullPointerException
     *             Thrown if the preference sub type parameter was null.
     */
    public void addPreferenceSet(String preferenceSubType,
            WebADEPreferenceSet prefSet) throws NullPointerException;

    /**
     * Removes the given preference set from the list of preference sets.
     * @param preferenceSubType
     *            The sub-type containing the preference set.
     * @param preferenceSetName
     *            The name of the target preference set.
     */
    public void removePreferenceSet(String preferenceSubType,
            String preferenceSetName);

    /**
     * Removes all preferences and preferences sets from this instance.
     */
    public void clear();

    /**
     * see java.lang.Object#clone()
     */
    public Object clone();
}
