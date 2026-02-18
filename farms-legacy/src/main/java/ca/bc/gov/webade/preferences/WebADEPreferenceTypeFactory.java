/**
 * @(#)WebADEPreferenceTypeFactory.java
 * Copyright (c) 2006, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.preferences;

import java.io.Serializable;

/**
 * @author jross
 */
public final class WebADEPreferenceTypeFactory implements Serializable {
    private static final long serialVersionUID = -465545692616836931L;

    private static final WebADEPreferenceTypeFactory FACTORY = new WebADEPreferenceTypeFactory();

    /** Global preference type constant. */
    public static final WebADEPreferenceType GLOBAL = FACTORY.new DefaultWebADEPreferenceType(
            "GLB");
    /** Application preference type constant. */
    public static final WebADEPreferenceType APPLICATION = FACTORY.new DefaultWebADEPreferenceType(
            "APP");
    /** Extension preference type constant. */
    public static final WebADEPreferenceType EXTENSION = FACTORY.new DefaultWebADEPreferenceType(
            "EXT");
    /** WebADE preference type constant. */
    public static final WebADEPreferenceType WEBADE = FACTORY.new DefaultWebADEPreferenceType(
            "WDE");
    /** User preference type constant. */
    public static final WebADEPreferenceType USER = FACTORY.new DefaultWebADEPreferenceType(
            "USR");

    /**
     * @param preferenceType
     *            The preference type string.
     * @return The matching preference type code, or null if not found.
     */
    public final static WebADEPreferenceType getPreferenceTypeCode(
            String preferenceType) {
        WebADEPreferenceType code = null;
        if (GLOBAL.getPreferenceType().equals(preferenceType)) {
            code = GLOBAL;
        } else if (APPLICATION.getPreferenceType().equals(preferenceType)) {
            code = APPLICATION;
        } else if (EXTENSION.getPreferenceType().equals(preferenceType)) {
            code = EXTENSION;
        } else if (WEBADE.getPreferenceType().equals(preferenceType)) {
            code = WEBADE;
        } else if (USER.getPreferenceType().equals(preferenceType)) {
            code = USER;
        }
        return code;
    }

    private class DefaultWebADEPreferenceType implements WebADEPreferenceType, Serializable {
        private static final long serialVersionUID = 530225330235770413L;
        private String preferenceType;

        private DefaultWebADEPreferenceType(String preferenceType) {
            this.preferenceType = preferenceType;
        }

        /**
         * @return The reserved code value for this preference type.
         */
        @Override
		public final String getPreferenceType() {
            return this.preferenceType;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
		public boolean equals(Object obj) {
            return (obj == null) ? false : getPreferenceType().equals(
                    obj.toString());
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
		public int hashCode() {
            return getPreferenceType().hashCode();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
		public String toString() {
            return getPreferenceType();
        }
    }

    private WebADEPreferenceTypeFactory() {
    }
}
