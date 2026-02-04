/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.reasonability;

import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Dec 10, 2018
 */
public class ReasonabilityTestsForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645502921365172105L;
  
  private ReasonabilityTestResults results;
  private String resultsJson;
  private boolean testsDisabled;

  public ReasonabilityTestResults getResults() {
    return results;
  }

  public void setResults(ReasonabilityTestResults results) {
    this.results = results;
  }

  public String getResultsJson() {
    return resultsJson == null ? "null" : resultsJson;
  }

  public void setResultsJson(String resultsJson) {
    this.resultsJson = resultsJson;
  }

  public boolean isTestsDisabled() {
    return testsDisabled;
  }

  public void setTestsDisabled(boolean testsDisabled) {
    this.testsDisabled = testsDisabled;
  }
  
}
