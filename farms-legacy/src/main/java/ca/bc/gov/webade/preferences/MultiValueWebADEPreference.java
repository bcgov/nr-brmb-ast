/**
 * @(#)MultiValueWebADEPreference.java
 * Copyright (c) 2006, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

import java.util.List;

/**
 * @author jross
 */
public interface MultiValueWebADEPreference extends WebADEPreference {

    /**
     * @return Returns the preferenceValues assigned to the preference name.
     */
    public List<String> getPreferenceValues();

    /**
     * Add the given value to the preference.
     * @param preferenceValue
     *            The preference value to add.
     */
    public void addPreferenceValue(String preferenceValue);

    /**
     * Removes the given value from the preference. If the value is not found,
     * no change will occur.
     * @param preferenceValue
     *            The preference value to remove.
     */
    public void removePreferenceValue(String preferenceValue);

    /**
     * Removes all preference values for this preference, leaving this
     * preference with an empty array of values.
     */
    public void clearPreferenceValues();

}
