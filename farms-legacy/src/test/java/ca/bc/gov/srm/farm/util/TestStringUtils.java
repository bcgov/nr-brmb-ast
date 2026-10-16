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
package ca.bc.gov.srm.farm.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestStringUtils {

  @Test
  public void formatBenefitsCalculatorVersion() {
    assertNull(StringUtils.formatBenefitsCalculatorVersion(null));
    assertEquals("01_04_02", StringUtils.formatBenefitsCalculatorVersion("1.4.2"));
    assertEquals("01_04_12", StringUtils.formatBenefitsCalculatorVersion("1.4.12"));
    assertEquals("10_123_05", StringUtils.formatBenefitsCalculatorVersion("10.123.5"));
    assertEquals("01_00_rc1", StringUtils.formatBenefitsCalculatorVersion("1.0.rc1"));
    assertEquals("01_02_3-rc1", StringUtils.formatBenefitsCalculatorVersion("1.2.3-rc1"));
    assertEquals("07", StringUtils.formatBenefitsCalculatorVersion("7"));
    assertEquals("local", StringUtils.formatBenefitsCalculatorVersion("local"));
  }
}
