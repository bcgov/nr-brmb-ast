/**
 * @(#)TextSearchAttribute.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

/**
 * @author jross
 */
public interface TextSearchAttribute extends SearchAttribute {
    /**
     * @return Returns the searchValue.
     */
    public String getSearchValue();
    
    /**
     * @param searchValue
     *            The searchValue to set.
     */
    public void setSearchValue(String searchValue);
    
    /**
     * @return Returns one of the WildcardOptions static values.
     */
    public int getWildcardOption();
    
    /**
     * @param wildcardOption
     *            The wildcardOption to set.
     */
    public void setWildcardOption(int wildcardOption);
}
