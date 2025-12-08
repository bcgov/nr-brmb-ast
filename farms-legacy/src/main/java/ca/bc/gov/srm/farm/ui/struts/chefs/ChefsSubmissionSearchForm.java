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
package ca.bc.gov.srm.farm.ui.struts.chefs;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.list.ListView;

public class ChefsSubmissionSearchForm extends ValidatorForm {

  private static final long serialVersionUID = 1L;

  private List<ChefsSubmission> searchResults;
  private List<ListView> formTypes;

  private String formType;
  private List<CdogsTemplate> cdogsTemplates;

  public List<ChefsSubmission> getSearchResults() {
    return searchResults;
  }

  public void setSearchResults(List<ChefsSubmission> searchResults) {
    this.searchResults = searchResults;
  }

  public String getFormType() {
    return formType;
  }

  public void setFormType(String formType) {
    this.formType = formType;
  }

  public List<ListView> getFormTypes() {
    return formTypes;
  }

  public void setFormTypes(List<ListView> formTypes) {
    this.formTypes = formTypes;
  }

  public int getNumSearchResults() {
    return searchResults.size();
  }

  public List<CdogsTemplate> getCdogsTemplates() {
    return cdogsTemplates;
  }

  public void setCdogsTemplates(List<CdogsTemplate> cdogsTemplates) {
    this.cdogsTemplates = cdogsTemplates;
  }

}
