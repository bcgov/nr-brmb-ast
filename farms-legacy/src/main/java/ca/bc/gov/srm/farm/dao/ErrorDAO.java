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
package ca.bc.gov.srm.farm.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import ca.bc.gov.srm.farm.exception.DataAccessException;

/**
 * DAO used by the webapp for the import screens.
 */
public class ErrorDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_ERROR_PKG";

  private static final String CODIFY_PROC = "Codify";


  private Connection conn = null;

  public ErrorDAO(final Connection c) {
    this.conn = c;
  }

  public final String codify(final String msg) throws DataAccessException {
    
    String procName = PACKAGE_NAME + "." + CODIFY_PROC;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(conn, procName, 1, Types.VARCHAR);) {
      proc.setString(1, msg);
      proc.execute();
      return (String) proc.getResult();

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
      return msg;
    }
  }
}
