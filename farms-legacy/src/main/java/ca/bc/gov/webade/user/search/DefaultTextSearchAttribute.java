/**
 * @(#)DefaultTextSearchAttribute.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.search;

/**
 * A search attribute that allows any text value as a search value.
 * 
 * @author jross
 */
public final class DefaultTextSearchAttribute extends AbstractSearchAttribute implements TextSearchAttribute {
    private static final long serialVersionUID = -8862379081048925884L;
    private String searchValue;
    private int wildcardOption = WildcardOptions.EXACT_MATCH;

    /**
     * @see ca.bc.gov.webade.user.search.TextSearchAttribute#getSearchValue()
     */
    @Override
	public String getSearchValue() {
        return searchValue;
    }

    /**
     * @see ca.bc.gov.webade.user.search.TextSearchAttribute#setSearchValue(java.lang.String)
     */
    @Override
	public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    /**
     * @see ca.bc.gov.webade.user.search.TextSearchAttribute#getWildcardOption()
     */
    @Override
	public int getWildcardOption() {
        return wildcardOption;
    }

    /**
     * @see ca.bc.gov.webade.user.search.TextSearchAttribute#setWildcardOption(int)
     */
    @Override
	public void setWildcardOption(int wildcardOption) {
        if (WildcardOptions.isValidValue(wildcardOption)) {
            this.wildcardOption = wildcardOption;
        }
    }
}