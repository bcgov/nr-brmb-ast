/**
 * @(#)DefaultWebADEPreference.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

import java.io.Serializable;



/**
 * @author jross
 */
public final class DefaultWebADEPreference implements WebADEPreference, Serializable {
    private static final long serialVersionUID = -8560519066514207195L;
    private String preferenceName;
    private String preferenceValue;
    
    /**
     * Creates a new preference with the given name.
     * @param preferenceName
     *            The name of the preference.
     * @throws NullPointerException
     *             Thrown if the preference name parameter was null.
     */
    public DefaultWebADEPreference(String preferenceName) {
        if (preferenceName == null) {
            throw new NullPointerException(
                    "Given preference name was null.");
        }
        this.preferenceName = preferenceName;
    }

    /**
     * @return Returns the preferenceName.
     */
    @Override
	public final String getPreferenceName() {
        return preferenceName;
    }

    /**
     * @return Returns the preference value assigned to the preference name.
     * @throws IllegalStateException
     *             Thrown if the preference contains multiple values.
     */
    @Override
	public final String getPreferenceValue() throws IllegalStateException {
        return this.preferenceValue;
    }

    /**
     * Sets the given value as the preference value, erasing all other
     * preference values that may already be set for this preference.
     * @param preferenceValue
     *            The preference value to set.
     */
    @Override
	public final void setPreferenceValue(String preferenceValue) {
        this.preferenceValue = preferenceValue;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public final boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof WebADEPreference) {
            WebADEPreference pref = (WebADEPreference) obj;
            equals = getPreferenceName()
                    .equals(pref.getPreferenceName());
        }
        return equals;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public final String toString() {
        return getPreferenceName() + "=" + getPreferenceValue();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public final int hashCode() {
        return toString().hashCode();
    }

    /**
     * see java.lang.Object#clone()
     */
    @Override
	public Object clone() {
        DefaultWebADEPreference clone = new DefaultWebADEPreference(getPreferenceName());
        // No encryption needed
        clone.setPreferenceValue(getPreferenceValue());
        return clone;
    }
}
