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

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author awilkinson
 * @created Jan 9, 2019
 */
public abstract class ReasonabilityTestsAction extends CalculatorAction {

  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  protected void populateForm(ReasonabilityTestsForm form, Scenario scenario) throws JsonProcessingException {
    form.setResults(scenario.getReasonabilityTestResults());
    
    String resultsJson = jsonObjectMapper.writeValueAsString(scenario.getReasonabilityTestResults());
    form.setResultsJson(resultsJson);
    
    populateRequiredYears(form, scenario);
  }


}
