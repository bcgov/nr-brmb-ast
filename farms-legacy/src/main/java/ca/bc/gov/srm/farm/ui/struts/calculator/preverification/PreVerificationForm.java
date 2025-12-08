/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.preverification;

import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author awilkinson
 */
public class PreVerificationForm extends CalculatorForm {

  private static final long serialVersionUID = 1;
  
  private String checklistJson;

  public String getChecklistJson() {
    return checklistJson;
  }

  public void setChecklistJson(String checklistJson) {
    this.checklistJson = checklistJson;
  }

}
