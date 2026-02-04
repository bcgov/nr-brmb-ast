/**
 * @(#)WebADEPreferenceSet.java
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
public interface WebADEPreferenceSet {
    /**
     * @return Returns the name.
     */
    public String getPreferenceSetName();

    /**
     * Returns the set of preferences.
     * @return An ArrayList of WebADEPreference objects.
     */
    public List<WebADEPreference> getPreferences();

    /**
     * Returns the preference names of the preference set.
     * @return An ArrayList of String objects.
     */
    public List<String> getPreferenceNames();

    /**
     * Returns the corresponding value for the given preference name, or null if
     * not found.
     * @param preferenceName
     *            The unique name of the target preference in the set.
     * @return A String value.
     */
    public WebADEPreference getPreference(String preferenceName);

    /**
     * Returns all preferences in the set as name value pairs in a Properties
     * object. Assumes that all preference objects in the set have at most one
     * value assigned.
     * @return A Properties object of String name-value pairs.
     * @throws WebADEException
     *             Thrown if any of the WebADEPreference object within the set have
     *             more than one value assigned.
     */
    public Properties getPreferencesProperties() throws WebADEException;

    /**
     * Returns all preferences in the set as name value pairs in a Properties
     * object. Assumes that all preference objects in the set have at most one
     * value assigned.
     * @param convertMultiValuesToDelimitedString
     *            Flag to convert multi-value preferences to a single
     *            comma-delimited value in the properties file (if set to true),
     *            or whether to throw an exception (if set to false).
     * @return A Properties object of String name-value pairs.
     * @throws WebADEException
     *             Thrown if any of the WebADEPreference object within the set
     *             have more than one value assigned, unless the
     *             <code>convertMultiValuesToDelimitedString</code> is set to
     *             true.
     */
    public Properties getPreferenceProperties(
            boolean convertMultiValuesToDelimitedString) throws WebADEException;

    /**
     * Returns all preferences in the set as name value pairs in a Properties
     * object. Assumes that all preference objects in the set have at most one
     * value assigned.
     * @param convertMultiValuesToDelimitedString
     *            Flag to convert multi-value preferences to a single delimited
     *            value in the properties file (if set to true), or whether to
     *            throw an exception (if set to false).
     * @param delimiterString
     *            The string used to delimit the multi-values string. The
     *            default value is a comma.
     * @return A Properties object of String name-value pairs.
     * @throws WebADEException
     *             Thrown if any of the WebADEPreference object within the set
     *             have more than one value assigned, unless the
     *             <code>convertMultiValuesToDelimitedString</code> is set to
     *             true.
     */
    public Properties getPreferenceProperties(
            boolean convertMultiValuesToDelimitedString, String delimiterString)
            throws WebADEException;

    /**
     * Adds the given preference to the current set, removing the previous one, if found.
     * @param preference
     *            The target preference.
     */
    public void addPreference(WebADEPreference preference);

    /**
     * Removes the given preference from the current set.
     * @param preferenceName
     *            The name of the target preference.
     */
    public void removePreference(String preferenceName);

    /**
     * Removes all preferences from the current set.
     */
    public void clearPreferences();

    /**
     * see java.lang.Object#clone()
     */
    public Object clone();

}
