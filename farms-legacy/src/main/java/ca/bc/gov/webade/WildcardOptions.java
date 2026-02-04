/**
 * @(#)WildcardOptions.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

import java.io.Serializable;

/**
 * @author jross
 */
public final class WildcardOptions implements Serializable {
    private static final long serialVersionUID = 502916169766241929L;
    /** Exact match wildcard option. No wildcard searching. */
    public static final int EXACT_MATCH = 1;
    /** Wildcard to the left of the search value only when searching. */
    public static final int WILDCARD_LEFT = 2;
    /** Wildcard to the right of the search value only when searching. */
    public static final int WILDCARD_RIGHT = 3;
    /** Wildcard to both sides of the search value when searching. */
    public static final int WILDCARD_BOTH = 4;

    private WildcardOptions() {
    }

    /**
     * Verifies that the given value is one of the valid wildcard options.
     * 
     * @param value
     *            The given wildcard value.
     * @return True is the value equals on of the defined static values.
     */
    public static boolean isValidValue(int value) {
        return (value == EXACT_MATCH || value == WILDCARD_LEFT || value == WILDCARD_RIGHT || value == WILDCARD_BOTH);
    }
}