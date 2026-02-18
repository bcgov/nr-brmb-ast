/**
 * @(#)InternalWebADEFilterChain.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreferences;

/**
 * @author jross
 */
final class InternalWebADEFilterChain implements FilterChain, Serializable {
    private static final long serialVersionUID = 7689149642611289832L;
    static final String DYNAMIC_FILTER_SUB_TYPE = "dynamic-filter";
    static final String CLASS_NAME_PARAM = "class-name";
    static final String INIT_PARAMETERS_PREFIX = "init-param-";
    static final String CHAIN_ORDER_PARAM = "chain-order";
    static final String ENABLED_PARAM = "enabled";
    
    private transient ArrayList<Filter> filters = new ArrayList<Filter>();
    private transient InheritableThreadLocal<Integer> requestCurrentFilter = new InheritableThreadLocal<Integer>();
    private transient InheritableThreadLocal<FilterChain> requestChain = new InheritableThreadLocal<FilterChain>();
	
	private static final Logger log = LoggerFactory.getLogger(InternalWebADEFilterChain.class);
    
    InternalWebADEFilterChain(WebADEPreferences preferences, ServletContext servletContext) {
        FilterCache filterCache = (FilterCache)servletContext.getAttribute(WebADEFilter.WEBADE_FILTER_CACHE_ATTR);
        
        TreeMap<Integer,Filter> filterMap = new TreeMap<Integer,Filter>();
        List<WebADEPreferenceSet> filterPrefs = preferences.getPreferenceSetsBySubType(DYNAMIC_FILTER_SUB_TYPE);
        for (Iterator<WebADEPreferenceSet> i = filterPrefs.iterator(); i.hasNext();) {
            WebADEPreferenceSet currentSet = i.next();
            WebADEPreference enabledPref = currentSet.getPreference(ENABLED_PARAM);
            WebADEPreference chainOrderPref = currentSet.getPreference(CHAIN_ORDER_PARAM);
            
            if(enabledPref == null) {
                log.error("WebADE dynamic filter preference set '"
                        + currentSet.getPreferenceSetName()
                        + "' missing mandatory preference '"
                        + ENABLED_PARAM + "'");
                continue;
            } else {
                String enabledPreferenceValue = enabledPref.getPreferenceValue();
                boolean enabled = Boolean.valueOf(enabledPreferenceValue).booleanValue();
                if(!enabled) {
                    log.debug("WebADE dynamic filter preference set '"
                            + currentSet.getPreferenceSetName()
                            + "' is disabled. Preference '"
                            + ENABLED_PARAM + "' is set to '"
                            + enabledPreferenceValue + "'");
                    continue;
                }
            }
            
            InternalWebADEFilterConfig filterConfig = new InternalWebADEFilterConfig();
            filterConfig.setServletContext(servletContext);
            
            filterConfig.setFilterName(currentSet.getPreferenceSetName());

            Hashtable<String,String> filterParams = parseFilterParameters(currentSet);
            filterConfig.setInitParameters(filterParams);
            
            Filter filter = filterCache.getFilter(filterConfig);
            boolean filterExists = (filter != null);
            if (!filterExists) {
                filter = createFilter(currentSet);
                if (filter == null) {
                    continue;
                }
            }
            
            Integer filterChainOrder;
            if (chainOrderPref == null) {
                log.error("WebADE dynamic filter preference set '"
                        + currentSet.getPreferenceSetName()
                        + "' missing mandatory preference '"
                        + CHAIN_ORDER_PARAM + "'");
                continue;
            } else {
                try {
                    filterChainOrder = new Integer(Integer.parseInt(chainOrderPref.getPreferenceValue()));
                    if (filterMap.get(filterChainOrder) != null) {
                        log.error("WebADE dynamic filter preference set '"
                                + currentSet.getPreferenceSetName()
                                + "' preference '" + CHAIN_ORDER_PARAM
                                + "' value '" + filterChainOrder
                                + "' is already declared by another filter.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    log.error("WebADE dynamic filter preference set '"
                            + currentSet.getPreferenceSetName()
                            + "' preference '" + CHAIN_ORDER_PARAM
                            + "' value is not a number.");
                    continue;
                }
            }
            try {
                filter.init(filterConfig);
            } catch (ServletException e) {
                log.error("WebADE dynamic filter from preference set '"
                        + currentSet.getPreferenceSetName()
                        + "' is not valid.  ServletException raised "
                        + "while initializing the filter: " + e.getMessage());
                continue;
            }
            log.debug("Loaded filter: " + filterConfig.getFilterName());
            filterMap.put(filterChainOrder, filter);
            if (!filterExists) {
                filterCache.addFilter(filterConfig, filter);
            }
        }
        for (Iterator<Integer> i = filterMap.keySet().iterator(); i.hasNext();) {
            Integer currentKey = i.next();
            Filter currentFilter = filterMap.get(currentKey);
            log.debug("Adding filter: " + currentFilter + " to chain with chain order: " + currentKey);
            this.filters.add(filterMap.get(currentKey));
        }
    }
    
    public boolean isInitialized() {
        return filters != null;
    }

    /**
     * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
	public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        Integer currentFilterIndex = this.requestCurrentFilter.get();
        if (currentFilterIndex != null) {
            Filter currentFilter = this.filters.get(currentFilterIndex.intValue());
            currentFilterIndex = new Integer(currentFilterIndex.intValue() + 1);
            if (this.filters.size() == currentFilterIndex.intValue()) {
                this.requestCurrentFilter.set(null);
            } else {
                this.requestCurrentFilter.set(currentFilterIndex);
            }
            currentFilter.doFilter(request, response, this);
        } else {
            FilterChain currentFilter = this.requestChain.get();
            currentFilter.doFilter(request, response);
        }
    }

    void prepareFilterChainForRequest(FilterChain filterChain) {
        this.requestChain.set(filterChain);
        if (this.filters.size() == 0) {
            this.requestCurrentFilter.set(null);
        } else {
            this.requestCurrentFilter.set(new Integer(0));
        }
    }
    
    private Hashtable<String,String> parseFilterParameters(WebADEPreferenceSet currentSet) {
        Hashtable<String,String> parameters = new Hashtable<String,String>();
        List<String> names = currentSet.getPreferenceNames();
        for (Iterator<String> i = names.iterator(); i.hasNext();) {
            String currentPrefName = i.next();
            if (currentPrefName.startsWith(INIT_PARAMETERS_PREFIX)) {
                WebADEPreference currentPref = currentSet.getPreference(currentPrefName);
                String currentPrefValue = currentPref.getPreferenceValue();
                currentPrefName = currentPrefName.substring(INIT_PARAMETERS_PREFIX.length());
                parameters.put(currentPrefName, currentPrefValue);
            }
        }
        return parameters;
    }
    
    private Filter createFilter(WebADEPreferenceSet currentSet) {
        Filter filter = null;
        
        WebADEPreference classNamePref = currentSet.getPreference(CLASS_NAME_PARAM);
        if (classNamePref == null) {
            log.error("WebADE dynamic filter preference set '"
                    + currentSet.getPreferenceSetName()
                    + "' missing mandatory preference '"
                    + CLASS_NAME_PARAM + "'");
        } else {
            String className = classNamePref.getPreferenceValue();
            try {
                Class<?> filterClass = Class.forName(className);
                Object filterObject = filterClass.newInstance();
                if (filterObject instanceof Filter) {
                    filter = (Filter)filterObject;
                } else {
                    log.error("WebADE dynamic filter preference '"
                            + CLASS_NAME_PARAM + "' value in preference set '"
                            + currentSet.getPreferenceSetName()
                            + "' is not a valid class name.  " 
                            + "Class does not implement interface "
                            + "javax.servlet.Filter");
                }
            } catch (ClassNotFoundException e) {
                log.error("WebADE dynamic filter preference '"
                        + CLASS_NAME_PARAM + "' value in preference set '"
                        + currentSet.getPreferenceSetName()
                        + "' is not valid.  Class '" + className
                        + "' not found in classpath.");
            } catch (InstantiationException e) {
                log.error("WebADE dynamic filter preference '"
                        + CLASS_NAME_PARAM + "' value in preference set '"
                        + currentSet.getPreferenceSetName()
                        + "' is not valid.  InstantiationException raised "
                        + "while loading instance of class '" + className
                        + "': " + e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("WebADE dynamic filter preference '"
                        + CLASS_NAME_PARAM + "' value in preference set '"
                        + currentSet.getPreferenceSetName()
                        + "' is not valid.  IllegalAccessException raised "
                        + "while loading instance of class '" + className
                        + "': " + e.getMessage());
            }
        }
        return filter;
    }
}
