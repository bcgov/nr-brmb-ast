/**
 * @(#)DefaultWebADEPreferences.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import ca.bc.gov.webade.WebADEException;

/**
 * Contains the set of WebADEPreference objects and the set of WebADEPreferenceSet objects
 * for a particular target(Global, Application, Extension, WebADE, User).
 * @author jross
 */
public final class DefaultWebADEPreferences implements WebADEPreferences, Serializable {
    private static final long serialVersionUID = -7177619727935105625L;
    /**
     * The default String delimiter used to convert a multi-value preference to
     * a single delimited property string in the
     * <code>getPreferencePropertiesBySubType</code> methods.
     */
    private static final String DEFAULT_DELIMITER = ",";
    private WebADEPreferenceType preferenceTypeCode = null;
    private LinkedHashMap<String, ArrayList<WebADEPreference>> preferences = new LinkedHashMap<String, ArrayList<WebADEPreference>>();
    private LinkedHashMap<String, ArrayList<WebADEPreferenceSet>> preferenceSets = new LinkedHashMap<String, ArrayList<WebADEPreferenceSet>>();

    /**
     * Constructor with the preference type code for this set of preferences.
     * @param preferenceTypeCode
     *            The preference type code for all preferences in this set.
     * @throws NullPointerException
     *             Thrown if the preference type code parameter was null.
     */
    public DefaultWebADEPreferences(WebADEPreferenceType preferenceTypeCode)
            throws NullPointerException {
        if (preferenceTypeCode == null) {
            throw new NullPointerException(
                    "Given preference type code was null.");
        }
        this.preferenceTypeCode = preferenceTypeCode;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferenceType()
     */
    @Override
	public final WebADEPreferenceType getPreferenceType() {
        return preferenceTypeCode;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferenceSubTypes()
     */
    @Override
	public final List<String> getPreferenceSubTypes() {
        ArrayList<String> prefs = new ArrayList<String>();
        for (Iterator<String> i = this.preferences.keySet().iterator(); i.hasNext();) {
            String current = i.next();
            if (!prefs.contains(current)) {
                prefs.add(current);
            }
        }
        for (Iterator<String> i = this.preferenceSets.keySet().iterator(); i.hasNext();) {
            String current = i.next();
            if (!prefs.contains(current)) {
                prefs.add(current);
            }
        }
        Collections.sort(prefs);
        return prefs;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreference(java.lang.String, java.lang.String)
     */
    @Override
	public final WebADEPreference getPreference(String preferenceSubType,
            String preferenceName) {
        WebADEPreference pref = null;
        ArrayList<WebADEPreference> subTypePrefs = this.preferences.get(preferenceSubType);
        if (subTypePrefs != null) {
            for (Iterator<WebADEPreference> i = subTypePrefs.iterator(); i.hasNext();) {
                WebADEPreference current = i.next();
                if (current.getPreferenceName().equals(preferenceName)) {
                    pref = current;
                    break;
                }
            }
        }
        return pref;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferencesBySubType(java.lang.String)
     */
    @Override
	@SuppressWarnings("unchecked")
	public final List<WebADEPreference> getPreferencesBySubType(String preferenceSubType) {
        ArrayList<WebADEPreference> subTypePrefs = this.preferences.get(preferenceSubType);
        return (subTypePrefs != null) ? (ArrayList<WebADEPreference>)subTypePrefs.clone() : new ArrayList<WebADEPreference>();
    }
    
    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferencePropertiesBySubType(java.lang.String)
     */
    @Override
	public final Properties getPreferencePropertiesBySubType(String preferenceSubType)
            throws WebADEException {
        return getPreferencePropertiesBySubType(preferenceSubType, false, DEFAULT_DELIMITER);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferencePropertiesBySubType(java.lang.String, boolean)
     */
    @Override
	public final Properties getPreferencePropertiesBySubType(
            String preferenceSubType,
            boolean convertMultiValuesToDelimitedString) throws WebADEException {
        return getPreferencePropertiesBySubType(preferenceSubType, convertMultiValuesToDelimitedString, DEFAULT_DELIMITER);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferencePropertiesBySubType(java.lang.String, boolean, java.lang.String)
     */
    @Override
	public final Properties getPreferencePropertiesBySubType(
            String preferenceSubType,
            boolean convertMultiValuesToDelimitedString, String delimiterString)
            throws WebADEException {
        if (delimiterString == null || delimiterString.equals("")) {
            delimiterString = DEFAULT_DELIMITER;
        }
        Properties prefs = new Properties();
        ArrayList<WebADEPreference> subTypePrefs = this.preferences.get(preferenceSubType);
        if (subTypePrefs != null) {
            for (Iterator<WebADEPreference> i = subTypePrefs.iterator(); i.hasNext();) {
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
                                + "in the sub-type '" + preferenceSubType
                                + "' with multiple values.");
                    }
                } else {
                    WebADEPreference current = obj;
                    prefs.put(current.getPreferenceName(), current
                            .getPreferenceValue());
                }
            }
        }
        return prefs;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferenceSet(java.lang.String, java.lang.String)
     */
    @Override
	public final WebADEPreferenceSet getPreferenceSet(String preferenceSubType,
            String preferenceSetName) {
        WebADEPreferenceSet pref = null;
        ArrayList<WebADEPreferenceSet> subTypePrefSets = this.preferenceSets.get(preferenceSubType);
        if (subTypePrefSets != null) {
            for (Iterator<WebADEPreferenceSet> i = subTypePrefSets.iterator(); i.hasNext();) {
                WebADEPreferenceSet current = i.next();
                if (current.getPreferenceSetName().equals(preferenceSetName)) {
                    pref = current;
                    break;
                }
            }
        }
        return pref;
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getPreferenceSetsBySubType(java.lang.String)
     */
    @Override
	@SuppressWarnings("unchecked")
	public final List<WebADEPreferenceSet> getPreferenceSetsBySubType(String preferenceSubType) {
        ArrayList<WebADEPreferenceSet> subTypePrefs = this.preferenceSets.get(preferenceSubType);
        return (subTypePrefs != null) ? (ArrayList<WebADEPreferenceSet>)subTypePrefs.clone() : new ArrayList<WebADEPreferenceSet>();
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#getWebADEPreferencesBySubType(java.lang.String)
     */
    @Override
	public WebADEPreferences getWebADEPreferencesBySubType(String preferenceSubType) {
        WebADEPreferences prefs = new DefaultWebADEPreferences(getPreferenceType());
        List<WebADEPreference> subTypePrefs = getPreferencesBySubType(preferenceSubType);
        for (Iterator<WebADEPreference> i = subTypePrefs.iterator(); i.hasNext();) {
            WebADEPreference currentPref = i.next();
            prefs.addPreference(preferenceSubType, currentPref);
        }
        List<WebADEPreferenceSet> subTypePrefSets = getPreferenceSetsBySubType(preferenceSubType);
        for (Iterator<WebADEPreferenceSet> i = subTypePrefSets.iterator(); i.hasNext();) {
            WebADEPreferenceSet currentPrefSet = i.next();
            prefs.addPreferenceSet(preferenceSubType, currentPrefSet);
        }
        return prefs;
    }
    
    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#addPreference(java.lang.String, ca.bc.gov.webade.preferences.WebADEPreference)
     */
    @Override
	public final void addPreference(String preferenceSubType, WebADEPreference pref) throws NullPointerException {
        if (preferenceSubType == null) {
            throw new NullPointerException("Given preference sub type was null.");
        }
        ArrayList<WebADEPreference> subTypePrefs = this.preferences.get(preferenceSubType);
        if (subTypePrefs == null) {
            subTypePrefs = new ArrayList<WebADEPreference>();
            this.preferences.put(preferenceSubType, subTypePrefs);
        } else {
            subTypePrefs.remove(pref);
        }
        subTypePrefs.add(pref);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#removePreference(java.lang.String, java.lang.String)
     */
    @Override
	public final void removePreference(String preferenceSubType, String preferenceName) {
        WebADEPreference preference = getPreference(preferenceSubType, preferenceName);
        if (preference != null) {
            ArrayList<WebADEPreference> subTypePrefs = this.preferences.get(preferenceSubType);
            if (subTypePrefs != null) {
                subTypePrefs.remove(preference);
                if (subTypePrefs.size() == 0) {
                    this.preferences.remove(preferenceSubType);
                }
            }
        }
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#addPreferenceSet(java.lang.String, ca.bc.gov.webade.preferences.WebADEPreferenceSet)
     */
    @Override
	public final void addPreferenceSet(String preferenceSubType, WebADEPreferenceSet prefSet)
            throws NullPointerException {
        if (preferenceSubType == null) {
            throw new NullPointerException("Given preference sub type was null.");
        }
        ArrayList<WebADEPreferenceSet> subTypePrefSets = this.preferenceSets.get(preferenceSubType);
        if (subTypePrefSets == null) {
            subTypePrefSets = new ArrayList<WebADEPreferenceSet>();
            this.preferenceSets.put(preferenceSubType, subTypePrefSets);
        } else {
            subTypePrefSets.remove(prefSet);
        }
        subTypePrefSets.add(prefSet);
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#removePreferenceSet(java.lang.String, java.lang.String)
     */
    @Override
	public final void removePreferenceSet(String preferenceSubType, String preferenceSetName) {
        WebADEPreferenceSet preferenceSet = getPreferenceSet(preferenceSubType, preferenceSetName);
        if (preferenceSet != null) {
            ArrayList<WebADEPreferenceSet> subTypePrefs = this.preferenceSets.get(preferenceSubType);
            if (subTypePrefs != null) {
                subTypePrefs.remove(preferenceSet);
                if (subTypePrefs.size() == 0) {
                    this.preferenceSets.remove(preferenceSubType);
                }
            }
        }
    }

    /**
     * @see ca.bc.gov.webade.preferences.WebADEPreferences#clear()
     */
    @Override
	public final void clear() {
        this.preferences.clear();
        this.preferenceSets.clear();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof WebADEPreferences) {
            WebADEPreferences prefs = (WebADEPreferences) obj;
            equals = getPreferenceType().equals(prefs.getPreferenceType());
        }
        return equals;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getPreferenceType().toString();
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
        DefaultWebADEPreferences clone = new DefaultWebADEPreferences(getPreferenceType());
        for (Iterator<String> i = this.preferences.keySet().iterator(); i.hasNext();) {
            String currentSubType = i.next();
            ArrayList<WebADEPreference> currentSubTypePrefs = this.preferences.get(currentSubType);
            ArrayList<WebADEPreference> subTypePrefs = new ArrayList<WebADEPreference>();
            clone.preferences.put(currentSubType, subTypePrefs);
            for (Iterator<WebADEPreference> j = currentSubTypePrefs.iterator(); j.hasNext();) {
                WebADEPreference currentPref = j.next();
                subTypePrefs.add((WebADEPreference) currentPref.clone());
            }
        }
        for (Iterator<String> i = this.preferenceSets.keySet().iterator(); i.hasNext();) {
            String currentSubType = i.next();
            ArrayList<WebADEPreferenceSet> currentSubTypePrefSets = this.preferenceSets.get(currentSubType);
            ArrayList<WebADEPreferenceSet> subTypePrefSets = new ArrayList<WebADEPreferenceSet>();
            clone.preferenceSets.put(currentSubType, subTypePrefSets);
            for (Iterator<WebADEPreferenceSet> j = currentSubTypePrefSets.iterator(); j.hasNext();) {
                WebADEPreferenceSet currentPrefSet = j.next();
                subTypePrefSets.add((WebADEPreferenceSet) currentPrefSet.clone());
            }
        }
        return clone;
    }
}
