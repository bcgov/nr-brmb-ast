/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.dataimport;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;


/**
 * ImportSearchForm.
 */
public class ImportSearchForm extends ValidatorForm {

  private static final long serialVersionUID = 9078780547622821008L;

  private List<ImportSearchResult> searchResults;

  private boolean newImportAllowed;
  
  private String jobType;

  public boolean getNewImportAllowed() {
    return newImportAllowed;
  }

  public void setNewImportAllowed(boolean newImportAllowed) {
    this.newImportAllowed = newImportAllowed;
  }

  public List<ImportSearchResult> getSearchResults() {
    return searchResults;
  }

  public void setSearchResults(List<ImportSearchResult> searchResults) {
    this.searchResults = searchResults;
  }

  public int getNumSearchResults() {
    return searchResults.size();
  }

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }
}
