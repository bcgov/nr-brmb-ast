/**
 * 
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.enw.enrolment;

import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;


/**
 * Form for screen 860.
 */
public class EnwEnrolmentForm extends CalculatorSearchForm {
	
  private static final long serialVersionUID = 1;

  private String enrolmentJson;
  
  public EnwEnrolmentForm() {
  }

  public String getEnrolmentJson() {
    return enrolmentJson;
  }

  public void setEnrolmentJson(String enrolmentJson) {
    this.enrolmentJson = enrolmentJson;
  }
  
}
