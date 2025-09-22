/**
 *
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author awilkiinson
 */
public class EmptyUnitTest {

  private static Logger logger = LoggerFactory.getLogger(EmptyUnitTest.class);

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    // tear down
  }

  @Test
  public void test() throws Exception {
    char c = 'A';
    int i = c;
    logger.debug("", i);
  }

}
