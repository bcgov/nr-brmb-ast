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
package ca.bc.gov.srm.farm.enrolment;

/**
 * @author awilkinson
 */
public final class EnrolmentCalculatorFactory {

  private EnrolmentCalculatorFactory() {
  }

  public static StandardEnrolmentCalculator getStandardEnrolmentCalculator() {
    return new StandardEnrolmentCalculator();
  }
  
  public static LateParticipantEnrolmentCalculator getLateParticipantEnrolmentCalculator() {
    return new LateParticipantEnrolmentCalculator();
  }
  
  public static EnwEnrolmentCalculator getEnwEnrolmentCalculator() {
    return new EnwEnrolmentCalculator();
  }
}
