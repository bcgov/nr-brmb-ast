/**
 * @(#)WebADEUtils.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

import java.util.Date;


/**
 * @author jross
 */
public final class WebADEUtils {
    /**
     * The default encoding scheme for URL encoding and decoding.
     */
    public static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * Helper constant for the '\' character.
     */
    public static final String BACKSLASH = "\\";
    /**
     * Helper constant for the '/' character.
     */
    public static final String FORWARDSLASH = "/";

    /**
     * @return The version of this WebADE library in the format 00_00_00.
     */
    public static final String getWebADEVersion() {
        return "04_07_00";
    }

    /**
     * Looks for the given String in the array.
     * @param s
     *            The target String.
     * @param sa
     *            The String array.
     * @return <code>true</code> if the string s is in the String array sa
     */
    public static final boolean isInStringArray(String s, String[] sa) {
        if (sa == null) {
            return false;
        }
        for (int i = 0; i < sa.length; i++) {
            if (sa[i].equals(s))
                return true;
        }
        return false;
    }

    /**
     * Looks for the given Integer in the array.
     * @param i
     *            The target Integer.
     * @param ia
     *            The Integer array.
     * @return <code>true</code> if the integer i is in the String array ia
     */
    public static final boolean isInIntegerArray(Integer i, Integer[] ia) {
        if (ia == null) {
            return false;
        }
        for (int j = 0; j < ia.length; j++) {
            if (ia[j].equals(i))
                return true;
        }
        return false;
    }

    /**
     * Strips all non-numeric characters from the given String, returning only
     * the numeric string.
     * @param inputString
     * @return The given string without non-numeric characters.
     */
    public static final String stripNonNumericCharacters(String inputString) {
        if (inputString == null) {
            return inputString;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inputString.length(); i++) {
            if (Character.isDigit(inputString.charAt(i))) {
                sb.append(inputString.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * Checks to see if the given string value is an empty string or a null
     * value.
     * @param stringValue
     *            The string to test.
     * @return True if the given string is blank or null.
     */
    public static boolean isBlankOrNull(String stringValue) {
        return (stringValue == null || stringValue.equals(""));
    }

    /**
     * Compares two strings, returning true if they are both null, or are
     * equivalent strings.
     * @param value1
     *            The first string to compare.
     * @param value2
     *            The second string to compare.
     * @return True if the strings are equal.
     */
    public static boolean areEqual(String value1, String value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2));
    }

    /**
     * Compares two dates, returning true if they are both null, or are
     * equivalent dates.
     * @param value1
     *            The first date to compare.
     * @param value2
     *            The second date to compare.
     * @return True if the dates are equal.
     */
    public static boolean areEqual(Date value1, Date value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value2 != null && value1.getTime() == value2
                        .getTime());
    }

    /**
     * Compares two objects, returning true if they are both null, or the
     * equals() Object method returns true.
     * @param value1
     *            The first object to compare.
     * @param value2
     *            The second object to compare.
     * @return True if the objects are equal.
     */
    public static boolean areEqual(Object value1, Object value2) {
        return (value1 == null && value2 == null)
                || (value1 != null && value1.equals(value2));
    }
}
