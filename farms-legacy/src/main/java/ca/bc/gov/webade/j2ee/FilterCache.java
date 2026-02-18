/**
 * @(#)FilterCache.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;

/**
 * @author jross
 */
final class FilterCache {
    private HashMap<FilterConfig,Filter> filterMap = new HashMap<FilterConfig,Filter>();
    
    void addFilter(FilterConfig config, Filter filter) {
        this.filterMap.put(config, filter);
    }
    
    void removeFilter(FilterConfig config) {
        this.filterMap.remove(config);
    }
    
    Filter getFilter(FilterConfig config) {
        return this.filterMap.get(config);
    }
}
