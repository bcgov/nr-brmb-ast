/**
 * Copyright (c) 2014,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.sql.Connection;

import oracle.jdbc.OracleConnection;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.webade.dbpool.WrapperConnection;

/**
 * @author awilkinson
 */
public final class OracleUtils {

  /** */
  private OracleUtils() {
  }

  /**
   * getOracleConnection.
   *
   * @param   transaction  Input parameter.
   *
   * @return  The return value.
   */
  public static OracleConnection getOracleConnection(
    final Transaction transaction) {
    Connection c = wrappedConnection(getConnection(transaction));

    if (c instanceof OracleConnection) {
      return (OracleConnection) c;
    }

    return null;
  }


  /**
   * wrappedConnection.
   *
   * @param   connection  The parameter value.
   *
   * @return  The return value.
   */
  public static Connection wrappedConnection(final Connection connection) {

    Connection inConnection = connection;

    if (inConnection instanceof WrapperConnection) {
      WrapperConnection wconn = (WrapperConnection) inConnection;
      inConnection = wconn.getWrappedConnection();
    }

    return inConnection;
  }


  /**
   * getConnection.
   *
   * @param   transaction  Input parameter.
   *
   * @return  The return value.
   */
  public static Connection getConnection(final Transaction transaction) {

    if (transaction == null) {
      throw new IllegalArgumentException("transaction cannot be null.");
    }

    if (!(transaction.getDatastore() instanceof Connection)) {
      throw new IllegalArgumentException(
        "Transaction datastore is not the expected to be type "
        + Connection.class.getName() + " not "
        + transaction.getDatastore().getClass().getName());
    }

    return (Connection) transaction.getDatastore();
  }

}
