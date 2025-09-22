/**
 *
 * Copyright (c) 2018, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.reasonability;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;


/**
 *
 * @author awilkinson
 */
public abstract class ReasonabilityTest {
  
  public static final int PERCENT_DECIMAL_PLACES = 3;

  public abstract void test(Scenario scenario) throws ReasonabilityTestException;
}
