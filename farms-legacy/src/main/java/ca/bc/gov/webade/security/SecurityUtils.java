/**
 * @(#)SecurityUtils.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.security;

/**
 * @author jross
 */
public final class SecurityUtils {
    /**
     * Checks the given stack trace to make sure the calling class is an
     * instance of one of the permitted classes.
     * 
     * @param stack The stack trace to analyze.
     * @param acceptedClassNames The array of acceptable calling classes.
     * @return True if the calling class is one of the accepted set.
     */
    public static final boolean checkStackCallAccess(StackTraceElement[] stack, String[] acceptedClassNames) {
        boolean result = false;
        if (stack.length <= 1) {
            throw new SecurityException("Stack trace is too small to determine caller.");
        }
        for (int i = 0; i < acceptedClassNames.length; i++) {
            if (stack[1].getClassName().equals(acceptedClassNames[i])) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Checks the given stack trace to make sure the calling class is an
     * instance of one of the permitted classes.
     * 
     * @param stack
     *            The stack trace to analyze.
     * @param acceptedClasses
     *            The array of acceptable calling classes.
     * @return True if the calling class is one of the accepted set.
     */
    public static final boolean checkStackCallAccess(StackTraceElement[] stack, Class<?>[] acceptedClasses) {
        boolean result = false;
        if (stack.length <= 1) {
            throw new SecurityException("Stack trace is too small to determine caller.");
        }
        for (int i = 0; i < acceptedClasses.length; i++) {
            if (stack[1].getClassName().equals(acceptedClasses[i].getName())) {
                result = true;
                break;
            }
        }
        return result;
    }
}
