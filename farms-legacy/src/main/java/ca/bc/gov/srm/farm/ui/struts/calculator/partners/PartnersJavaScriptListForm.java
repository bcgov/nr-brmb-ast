/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.partners;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;


/**
 * @author awilkinson
 */
public class PartnersJavaScriptListForm extends CalculatorForm {

  private static final long serialVersionUID = 1;

  private List<FarmingOperationPartner> partners = new ArrayList<>();

  public List<FarmingOperationPartner> getPartners() {
    return partners;
  }

  public void setPartners(List<FarmingOperationPartner> partners) {
    this.partners = partners;
  }
  
}
