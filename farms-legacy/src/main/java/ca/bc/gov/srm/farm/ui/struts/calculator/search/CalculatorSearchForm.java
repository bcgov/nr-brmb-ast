/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.search;

import java.util.List;

import ca.bc.gov.srm.farm.ui.domain.AccountSearchResult;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * 
 * @author awilkinson
 *
 */
public class CalculatorSearchForm extends CalculatorForm {

  private static final long serialVersionUID = -2645502921365403105L;
  
  private String searchString;
  private String searchType = "pin";
  private List<AccountSearchResult> searchResults;

  /**
   * @return the searchString
   */
  public String getSearchString() {
    return searchString;
  }

  /**
   * @param searchString the searchString to set
   */
  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  /**
   * @return the searchType
   */
  public String getSearchType() {
    return searchType;
  }

  /**
   * @param searchType the searchType to set
   */
  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  /**
   * @return  the searchResults
   */
  public List<AccountSearchResult> getSearchResults() {
    return searchResults;
  }

  /**
   * @param  searchResults  the searchResults to set
   */
  public void setSearchResults(List<AccountSearchResult> searchResults) {
    this.searchResults = searchResults;
  }

  /**
   * @return  the number of search results
   */
  public int getNumSearchResults() {
    return searchResults.size();
  }
  
}
