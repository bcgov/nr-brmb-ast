/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provider.
 *
 * @author   $author$
 * @version  $Revision: 5660 $, $Date: 2024-11-22 16:15:03 -0800 (Fri, 22 Nov 2024) $
 */
public abstract class Provider {

  /** initialized. */
  private boolean initialized = false;

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  public abstract void initialize(Object resource);

  /**
   * isInitialized.
   *
   * @return  The return value.
   */
  public boolean isInitialized() {
    return initialized;
  }

  /** checkInitialized. */
  protected void checkInitialized() {

    if (!isInitialized()) {
      log.error(getClass() + " not initialized.");
      throw new RuntimeException(getClass() + " not initialized.");
    }
  }

  /**
   * setInitialized.
   *
   * @param  initialized  Set initialized
   */
  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  /**
   * getLog.
   *
   * @return  The return value
   */
  public Logger getLog() {
    return log;
  }

  /**
   * setLog.
   *
   * @param  log  Set log
   */
  public void setLog(Logger log) {
    this.log = log;
  }

}
