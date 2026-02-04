/**
 * @(#)WebADEFilter.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.WebADEDatabaseApplication;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.preferences.DefaultWebADEPreference;
import ca.bc.gov.webade.preferences.DefaultWebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferences;

/**
 * @author jross
 */
public final class WebADEFilter implements Filter {
    /**
     * <code>WEBADE_FILTER_CACHE_ATTR</code> is the servlet context attribute
     * that stores the set of filter singletons for this application.
     */
    static final String WEBADE_FILTER_CACHE_ATTR = "webade.filter.cache";
    private static final String SECURITY_CONFIGURATION = "security-configuration";
    private static final String WEBADE_INTERNAL_FILTER_CHAIN = "webade-internal-filter-chain";
	
	private static final Logger log = LoggerFactory.getLogger(WebADEFilter.class);
    private FilterConfig filterConfig = null;

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
	public void init(FilterConfig config) {
        log.debug("> init()");
        this.filterConfig = config;
        FilterCache filterCache = (FilterCache)config.getServletContext().getAttribute(WEBADE_FILTER_CACHE_ATTR);
        if (filterCache == null) {
            filterCache = new FilterCache();
            config.getServletContext().setAttribute(WEBADE_FILTER_CACHE_ATTR, filterCache);
        }
        log.debug("< init()");
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            WebADEDatabaseApplication app = (WebADEDatabaseApplication)HttpRequestUtils.getApplication(filterConfig.getServletContext());
            if (app == null) {
                throw new ServletException("WebADE Application singleton not loaded "
                		+ "in the servlet context.  Please check the logs for errors.");
            }
            HttpSession session = httpRequest.getSession();
            InternalWebADEFilterChain chain = (InternalWebADEFilterChain)session.getAttribute(WEBADE_INTERNAL_FILTER_CHAIN);
            if (chain == null || !chain.isInitialized()) {
                chain = createInternalFilterChain(app);
                session.setAttribute(WEBADE_INTERNAL_FILTER_CHAIN, chain);
            }
            chain.prepareFilterChainForRequest(filterChain);
            chain.doFilter(request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private InternalWebADEFilterChain createInternalFilterChain(
            WebADEDatabaseApplication app)
            throws ServletException {
        WebADEPreferences prefs;
        try {
            prefs = app.getWebADEPreferences();
        } catch (WebADEException e) {
            throw new ServletException("WebADEException raised while loading "
                    + "the application's WebADE preferences.");
        }
        DefaultWebADEPreferenceSet prefSet;
        DefaultWebADEPreference pref;
        prefSet = new DefaultWebADEPreferenceSet("webade-filter");
        pref = new DefaultWebADEPreference(InternalWebADEFilterChain.CLASS_NAME_PARAM);
        // No encryption needed
        pref.setPreferenceValue(InternalWebADEFilter.class.getName());
        prefSet.addPreference(pref);
        pref = new DefaultWebADEPreference(InternalWebADEFilterChain.CHAIN_ORDER_PARAM);
        // No encryption needed
        pref.setPreferenceValue("0");
        prefSet.addPreference(pref);
        pref = new DefaultWebADEPreference(InternalWebADEFilterChain.ENABLED_PARAM);
        // No encryption needed
        pref.setPreferenceValue("true");
        prefSet.addPreference(pref);

        List<WebADEPreference> configPrefList = prefs.getPreferencesBySubType(SECURITY_CONFIGURATION);
        for (Iterator<WebADEPreference> i = configPrefList.iterator(); i.hasNext();) {
            WebADEPreference currentPref = i.next();
            pref = new DefaultWebADEPreference(InternalWebADEFilterChain.INIT_PARAMETERS_PREFIX + currentPref.getPreferenceName());
            pref.setPreferenceValue(currentPref.getPreferenceValue());
            prefSet.addPreference(pref);
        }
        
        prefs.addPreferenceSet(InternalWebADEFilterChain.DYNAMIC_FILTER_SUB_TYPE, prefSet);
        
        InternalWebADEFilterChain chain = new InternalWebADEFilterChain(prefs, getFilterConfig().getServletContext());
        return chain;
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
	public void destroy() {
        this.filterConfig = null;
    }

    /**
     * @return Returns the filterConfig.
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

}
