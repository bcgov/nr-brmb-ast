/**
 * @(#)InternalWebADEFilter.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jross
 */
public final class InternalWebADEFilter implements Filter, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(InternalWebADEFilter.class);
	private String filterName;

    /**
     * Default Constructor.
     */
    InternalWebADEFilter() {
    }
    
    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
	public void init(FilterConfig config) {
        log.debug("> init()");
        
        this.filterName = config.getFilterName();
        
        log.debug("< init()");
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
	public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            HttpServletResponse httpResponse = (HttpServletResponse)response;
            boolean shouldContinue = WebAppRequestProcessingUtils.preprocessRequest(request.getServletContext(), httpRequest, httpResponse);
            try {
                if (shouldContinue) {
                    log.debug("WebADEFilter proceeding with request.");
                    chain.doFilter(httpRequest, httpResponse);
                } else {
                    log.warn("WebADEFilter determined that the request should not proceed.");
                }
            } finally {
                WebAppRequestProcessingUtils.postprocessRequest(request.getServletContext(), httpRequest, httpResponse);
            }
        }
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
	public void destroy() {
    	// do nothing
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return (this.filterName == null) ? super.hashCode()
                : this.filterName.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return (this.filterName == null) ? null : this.filterName;
    }
}
