/**
 * @(#)ApplicationMBean.java
 * Copyright (c) 2003, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

/**
 * @author jross
 */
public interface ApplicationMBean {
    /**
     * @return The application's identifying code.
     */
    public String getApplicationCode();
}
