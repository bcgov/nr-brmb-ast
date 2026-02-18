/**
 * @(#)InternalWebADEFilterConfig.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * @author jross
 */
final class InternalWebADEFilterConfig implements FilterConfig, Serializable {
    private static final long serialVersionUID = -4108657360959193873L;

    private String filterName;
    private Hashtable<String,String> initParameters;
    private ServletContext servletContext;

    /**
     * @see javax.servlet.FilterConfig#getFilterName()
     */
    @Override
	public String getFilterName() {
        return this.filterName;
    }

    /**
     * @see javax.servlet.FilterConfig#getInitParameter(java.lang.String)
     */
    @Override
	public String getInitParameter(String key) {
        return this.initParameters.get(key);
    }

    /**
     * @see javax.servlet.FilterConfig#getInitParameterNames()
     */
    @Override
	public Enumeration<String> getInitParameterNames() {
        return this.initParameters.keys();
    }

    /**
     * @see javax.servlet.FilterConfig#getServletContext()
     */
    @Override
	public ServletContext getServletContext() {
        return this.servletContext;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        boolean areEqual = false;
        if (obj instanceof FilterConfig) {
            FilterConfig configObj = (FilterConfig)obj;
            areEqual = getFilterName().equals(configObj.getFilterName());
        }
        return areEqual;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return getFilterName();
    }

    /**
     * @param filterName The filterName to set.
     */
    void setFilterName(String filterName) {
        if (filterName == null) {
            throw new RuntimeException("Filter name value cannot be null.");
        }
        this.filterName = filterName;
    }

    /**
     * @param initParameters The initParameters to set.
     */
    void setInitParameters(Hashtable<String,String> initParameters) {
        if (initParameters == null) {
            throw new RuntimeException("Initialization parameters cannot be null.");
        }
        this.initParameters = initParameters;
    }

    /**
     * @param servletContext The servletContext to set.
     */
    void setServletContext(ServletContext servletContext) {
        if (servletContext == null) {
            throw new RuntimeException("Servlet context cannot be null.");
        }
        this.servletContext = servletContext;
    }

}
