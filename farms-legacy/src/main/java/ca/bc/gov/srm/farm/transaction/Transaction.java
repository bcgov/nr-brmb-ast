/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.transaction;

import ca.bc.gov.srm.farm.exception.TransactionException;


/**
 * Transaction.
 *
 * @author   $Author: hwang $
 * @version  $Revision: 6327 $
 */
public interface Transaction extends AutoCloseable {

  /**
   * begin.
   *
   * @throws  TransactionException  On exception.
   */
  void begin() throws TransactionException;

  /** close. */
  @Override
  void close();

  /**
   * commit.
   *
   * @throws  TransactionException  On exception.
   */
  void commit() throws TransactionException;

  /**
   * getDatastore.
   *
   * @return  The return value.
   */
  Object getDatastore();

  /** rollback. */
  void rollback();

  /**
   * Sets the value for datastore.
   *
   * @param  datastore  Input parameter.
   */
  void setDatastore(Object datastore);
}
