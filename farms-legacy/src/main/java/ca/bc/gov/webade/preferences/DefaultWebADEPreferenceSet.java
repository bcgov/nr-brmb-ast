/**
 * @(#)DefaultWebADEPreferenceSet.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import ca.bc.gov.webade.WebADEException;


/**
 * @author jross
 */
public final class DefaultWebADEPreferenceSet implements WebADEPreferenceSet, Serializable {
    private static final long serialVersionUID = 4821756353533607096L;
    /**
     * The default String delimiter used to convert a multi-value preference to
     * a single delimited property string in the
     * <code>getPreferenceProperties</code> methods.
     */
    private static final String DEFAULT_DELIMITER = ",";
    private String preferenceSetName;
    private ArrayList<WebADEPreference> preferences = new ArrayList<WebADEPreference>();

    /**
     * Creates a new preference set with the given set name.
     * @param preferenceSetName
     *            The name of the preference set.
     * @throws NullPointerException
     *             Thrown if the preference set name parameter was null.
     */
    public DefaultWebADEPreferenceSet(String preferenceSetName) throws NullPointerException {
        if (preferenceSetName == null) {
            throw new NullPointerException(
                    "Given preference set name was null.");
        }
        this.preferenceSetName = preferenceSetName;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreferenceSetName()
     */
    @Override
	public final String getPreferenceSetName() {
        return preferenceSetName;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreferences()
     */
    @Override
	@SuppressWarnings("unchecked")
	public final List<WebADEPreference> getPreferences() {
        return (List<WebADEPreference>)this.preferences.clone();
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreferenceNames()
     */
    @Override
	public final List<String> getPreferenceNames() {
        ArrayList<String> prefNames = new ArrayList<String>();
        for (Iterator<WebADEPreference> iter = this.preferences.iterator(); iter.hasNext();) {
            WebADEPreference current = iter.next();
            prefNames.add(current.getPreferenceName());
        }
        return prefNames;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreference(java.lang.String)
     */
    @Override
	public final WebADEPreference getPreference(String preferenceName) {
        WebADEPreference pref = null;
        for (Iterator<WebADEPreference> iter = this.preferences.iterator(); iter.hasNext();) {
            WebADEPreference current = iter.next();
            if (current.getPreferenceName().equals(preferenceName)) {
                pref = current;
                break;
            }
        }
        return pref;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreferencesProperties()
     */
    @Override
	public final Properties getPreferencesProperties() throws WebADEException {
        return getPreferenceProperties(false, DEFAULT_DELIMITER);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreferenceProperties(boolean)
     */
    @Override
	public Properties getPreferenceProperties(
            boolean convertMultiValuesToDelimitedString) throws WebADEException {
        return getPreferenceProperties(convertMultiValuesToDelimitedString, DEFAULT_DELIMITER);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#getPreferenceProperties(boolean, java.lang.String)
     */
    @Override
	public Properties getPreferenceProperties(
            boolean convertMultiValuesToDelimitedString, String delimiterString)
            throws WebADEException {
        if (delimiterString == null || delimiterString.equals("")) {
            delimiterString = DEFAULT_DELIMITER;
        }
        Properties prefs = new Properties();
        for (Iterator<WebADEPreference> i = this.preferences.iterator(); i.hasNext();) {
        	WebADEPreference obj = i.next();
            if (obj instanceof MultiValueWebADEPreference) {
                if (convertMultiValuesToDelimitedString) {
                    //Converts the pref values into a single, delimited string value.
                    MultiValueWebADEPreference multiValPref = (MultiValueWebADEPreference)obj;
                    StringBuffer value = new StringBuffer();
                    for (Iterator<String> j = multiValPref.getPreferenceValues().iterator(); j.hasNext();) {
                        String currentValue = j.next();
                        if (value.length() > 0) {
                            value.append(delimiterString);
                        }
                        value.append(currentValue);
                    }
                    prefs.put(multiValPref.getPreferenceName(), value.toString());
                } else {
                    throw new WebADEException("Found preferences "
                            + "in the set with multiple values.");
                }
            } else {
                WebADEPreference current = obj;
                prefs.put(current.getPreferenceName(), current
                        .getPreferenceValue());
            }
        }
        return prefs;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#addPreference(ca.bc.gov.webade.preferences.WebADEPreference)
     */
    @Override
	public final void addPreference(WebADEPreference pref) {
        if (pref == null) {
            return;
        }
        this.preferences.remove(pref);
        this.preferences.add(pref);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#removePreference(java.lang.String)
     */
    @Override
	public final void removePreference(String preferenceName) {
        WebADEPreference preference = getPreference(preferenceName);
        if (preference != null) {
            this.preferences.remove(preference);
        }
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferenceSet#clearPreferences()
     */
    @Override
	public final void clearPreferences() {
        this.preferences.clear();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof WebADEPreferenceSet) {
            WebADEPreferenceSet pref = (WebADEPreferenceSet) obj;
            equals = getPreferenceSetName().equals(pref.getPreferenceSetName());
        }
        return equals;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getPreferenceSetName();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return toString().hashCode();
    }

    /**
     * see java.lang.Object#clone()
     */
    @Override
	public Object clone() {
        DefaultWebADEPreferenceSet clone = new DefaultWebADEPreferenceSet(new String(
                getPreferenceSetName()));
        for (Iterator<WebADEPreference> i = this.preferences.iterator(); i.hasNext();) {
            WebADEPreference currentPref = i.next();
            clone.preferences.add((WebADEPreference) currentPref.clone());
        }
        return clone;
    }
}
