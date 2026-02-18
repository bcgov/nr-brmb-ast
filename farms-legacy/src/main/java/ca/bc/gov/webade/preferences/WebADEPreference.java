/**
 * @(#)WebADEPreference.java
 * Copyright (c) 2006, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

/**
 * @author jross
 */
public interface WebADEPreference {

    /**
     * @return Returns the preferenceName.
     */
    public String getPreferenceName();

    /**
     * @return Returns the preference value assigned to the preference name.
     */
    public String getPreferenceValue();

    /**
     * Sets the given value as the preference value, erasing all other
     * preference values that may already be set for this preference.
     * @param preferenceValue
     *            The preference value to set.
     */
    public void setPreferenceValue(String preferenceValue);

    /**
     * see java.lang.Object#clone()
     */
    public Object clone();
}
