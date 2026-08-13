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
package ca.bc.gov.srm.farm.chefs.processor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.crm.CrmConstants;

public class NppSubmissionProcessorTest {

  @Test
  public void lateParticipantSkipsEnrolmentCalculation() {
    assertFalse(NppSubmissionProcessor.shouldCalculateEnrolment(true, null));
    assertFalse(NppSubmissionProcessor.shouldCalculateEnrolment(
        true, CrmConstants.ENROLMENT_STATUS_CODE_INITIALIZED));
    assertFalse(NppSubmissionProcessor.shouldCalculateEnrolment(
        true, CrmConstants.ENROLMENT_STATUS_CODE_TO_BE_REVIEWED));
  }


  @Test
  public void nonLateParticipantCalculatesEnrolmentForEligibleStatuses() {
    assertTrue(NppSubmissionProcessor.shouldCalculateEnrolment(false, null));
    assertTrue(NppSubmissionProcessor.shouldCalculateEnrolment(
        false, CrmConstants.ENROLMENT_STATUS_CODE_INITIALIZED));
    assertTrue(NppSubmissionProcessor.shouldCalculateEnrolment(
        false, CrmConstants.ENROLMENT_STATUS_CODE_TO_BE_REVIEWED));
  }
}
