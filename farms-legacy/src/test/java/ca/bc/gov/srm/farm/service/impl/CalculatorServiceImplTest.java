/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.ADMINISTRATIVE_ADJUSTMENT;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.FINAL;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.INTERIM;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.NOL;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.PRODUCER_ADJUSTMENT;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.IN_PROGRESS;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.VERIFIED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CalculatorServiceImplTest {

  @Test
  public void shouldGeneratePyPlusTwoEnrolmentForEligibleVerifiedScenarios() {
    assertTrue(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(true, VERIFIED, FINAL));
    assertTrue(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(
        true, VERIFIED, PRODUCER_ADJUSTMENT));
    assertTrue(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(
        true, VERIFIED, ADMINISTRATIVE_ADJUSTMENT));
  }


  @Test
  public void shouldNotGeneratePyPlusTwoEnrolmentForIneligibleScenarioChanges() {
    assertFalse(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(false, VERIFIED, FINAL));
    assertFalse(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(true, IN_PROGRESS, FINAL));
    assertFalse(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(true, VERIFIED, NOL));
    assertFalse(CalculatorServiceImpl.shouldGeneratePyPlusTwoEnrolment(true, VERIFIED, INTERIM));
  }
}
