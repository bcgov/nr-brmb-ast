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
package ca.bc.gov.srm.farm.service;

import ca.bc.gov.srm.farm.MessageItem;


/**
 * DataPersistenceException.
 */
public final class DataPersistenceException extends Exception {

  private static final long serialVersionUID = 6030455940758646713L;

  /**
   * @param  msg  String
   */
  public DataPersistenceException(final String msg) {
    super(msg);
  }

  /**
   * @param  msg  Throwable
   */
  public DataPersistenceException(final Throwable msg) {
    super(msg);
  }

  /**
   * @param  msg MessageItem
   */
  public DataPersistenceException(MessageItem msg) {
    super(msg.getMessage());
  }
}
