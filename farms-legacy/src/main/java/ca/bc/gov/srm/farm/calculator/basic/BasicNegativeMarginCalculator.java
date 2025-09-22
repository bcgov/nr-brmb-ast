/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator.basic;

import java.util.Collections;
import java.util.List;

import ca.bc.gov.srm.farm.calculator.NegativeMarginCalculator;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * @author awilkinson
 */
public class BasicNegativeMarginCalculator extends NegativeMarginCalculator {

  public BasicNegativeMarginCalculator(Scenario scenario) {
    super(scenario);
  }

  /**
   * Calculates the Deemed Production Insurance for the program year
   * and puts the results into the Margin and MarginTotal objects.
   */
  @Override
  public void calculateDeemedPiTotals(String user) throws ServiceException {

    MarginTotal pyMargin = scenario.getFarmingYear().getMarginTotal();
    Benefit benefit = scenario.getBenefit();
    List<Scenario> scenarios = Collections.singletonList(scenario);
    
    calculateDeemedPiTotals(pyMargin, scenarios, user);
    
    Double prodInsurDeemedTotal = pyMargin.getProdInsurDeemedTotal();
    benefit.setProdInsurDeemedBenefit(prodInsurDeemedTotal);
  }

}
