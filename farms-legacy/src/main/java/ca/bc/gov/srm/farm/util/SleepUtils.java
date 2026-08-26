/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public final class SleepUtils {

  private static final Logger logger = LoggerFactory.getLogger(SleepUtils.class);
  
  /** private constructor */
  private SleepUtils() {
  }


  public static void waitASecond() {
    waitForSeconds(1);
  }

  public static void waitForSeconds(int waitSeconds) {
    try {
      TimeUnit.SECONDS.sleep(waitSeconds);
    } catch (InterruptedException e) {
      logger.error("InterruptedException: ", e);
    }
  }
}
