/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.transaction;

import ca.bc.gov.srm.farm.exception.TransactionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * WebADETransaction.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5660 $
 */
final class WebADETransaction implements Transaction {

  /** datastore. */
  private Connection datastore;

  /** log. */
  private Logger log = LoggerFactory.getLogger(this.getClass());

  /**
   * begin.
   *
   * @throws  TransactionException  On exception.
   */
  @Override
  public void begin() throws TransactionException {

    try {
      datastore.setAutoCommit(false);
    } catch (SQLException e) {
      log.error("Error on begin(): " + e.getMessage());
      throw new TransactionException(e);
    }
  }


  /** close. */
  @Override
  public void close() {

    try {

      if (!datastore.isClosed()) {
        datastore.close();
      }
    } catch (SQLException e) {
      log.error("Error on close(): " + e.getMessage(), e);
    }
  }


  /**
   * commit.
   *
   * @throws  TransactionException  On exception.
   */
  @Override
  public void commit() throws TransactionException {

    try {
      datastore.commit();
    } catch (SQLException e) {
      log.error("Error on commit(): " + e.getMessage());
      throw new TransactionException(e);
    }
  }

  /**
   * getDatastore.
   *
   * @return  The return value.
   */
  @Override
  public Object getDatastore() {
    return datastore;
  }


  /** rollback. */
  @Override
  public void rollback() {

    try {
      datastore.rollback();
    } catch (SQLException e) {
      log.error("Error on close(): " + e.getMessage(), e);
    }
  }


  /**
   * Sets the value for datastore.
   *
   * @param  store  Input parameter.
   */
  @Override
  public void setDatastore(final Object store) {

    if (store == null) {
      throw new IllegalArgumentException("Datastore cannot be null.");
    }

    if (store instanceof Connection) {
      this.datastore = (Connection) store;
    } else {
      throw new IllegalArgumentException("Unexpected type: "
        + store.getClass().getName() + ".  Expect "
        + Connection.class.getName());
    }
  }
}
