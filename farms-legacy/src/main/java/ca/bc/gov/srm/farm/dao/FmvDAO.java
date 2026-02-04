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

import ca.bc.gov.srm.farm.domain.staging.FmvCsvRow;
import ca.bc.gov.srm.farm.exception.DataAccessException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;


/**
 * Used for importing an FMV CSV import.
 */
public class FmvDAO extends OracleDAO {

  /** DOCUMENT ME! */
  private static final String PACKAGE_NAME = "FARM_FMV_PKG";

  /** INSERT_PROC. */
  private static final String INSERT_PROC = "INSERT_STAGING_ROW";

  /** CLEAR_PROC. */
  private static final String CLEAR_PROC = "CLEAR_STAGING";

  /** DELETE_ERROR_PROC. */
  private static final String DELETE_ERRORS_PROC = "DELETE_STAGING_ERRORS";

  /** VALIDATE_PROC. */
  private static final String VALIDATE_PROC = "VALIDATE_STAGING";

  /** GET_ERRORS_PROC. */
  private static final String GET_ERRORS_PROC = "GET_STAGING_ERRORS";

  /** OPERATIONAL_PROC. */
  private static final String OPERATIONAL_PROC = "STAGING_TO_OPERATIONAL";


  /** cached for repetative use. */
  private DAOStoredProcedure insertProc = null;

  /** document me.* */
  private Connection connection;

  /**
   * @param  conn  Connection
   */
  public FmvDAO(Connection conn) {
    connection = conn;
  }


  /** close. */
  public final void close() {
    close(insertProc);
  }


  /**
   * clearStaging.
   *
   * @throws  SQLException  On exception.
   */
  public final void clearStaging() throws SQLException {
    DAOStoredProcedure proc = null;
    final int paramCount = 0;
    String procName = PACKAGE_NAME + "." + CLEAR_PROC;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);
      proc.execute();
    } finally {
      close(proc);
    }
  }


  /**
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  DOCUMENT ME!
   * @param   rowNum  DOCUMENT ME!
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(final FmvCsvRow obj, final String userId,
    final int rowNum) throws SQLException {

    if (insertProc == null) {
      final int paramCount = 9;
      String procName = PACKAGE_NAME + "." + INSERT_PROC;

      insertProc = new DAOStoredProcedure(connection,
          procName,
          paramCount,
          false);
    } else {
      insertProc.clearParameters();
    }

    int index = 1;

    insertProc.setInt(index++, rowNum);
    insertProc.setInt(index++, obj.getProgramYear());
    insertProc.setInt(index++, obj.getPeriod());
    insertProc.setDouble(index++, obj.getAveragePrice());
    insertProc.setDouble(index++, obj.getPercentVariance());
    insertProc.setString(index++, obj.getMunicipalityCode());
    insertProc.setString(index++, obj.getUnitCode());
    insertProc.setString(index++, obj.getInventoryCode());
    insertProc.setString(index++, userId);

    insertProc.execute();
  }


  /**
   * validateStaging.
   *
   * @param   importVersionId  importVersionId
   *
   * @throws  SQLException  On exception.
   */
  public final void validateStaging(final Integer importVersionId)
    throws SQLException {
    DAOStoredProcedure proc = null;
    final int paramCount = 1;
    String procName = PACKAGE_NAME + "." + VALIDATE_PROC;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
    } finally {
      close(proc);
    }
  }


  /**
   * deleteStagingErrors.
   *
   * @param   importVersionId  importVersionId
   *
   * @throws  SQLException  On exception.
   */
  public final void deleteStagingErrors(final Integer importVersionId)
    throws SQLException {
    DAOStoredProcedure proc = null;
    final int paramCount = 1;
    String procName = PACKAGE_NAME + "." + DELETE_ERRORS_PROC;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
    } finally {
      close(proc);
    }
  }


  /**
   * getStagingErrors.
   *
   * @param   importVersionId  importVersionId
   *
   * @return  List of XML formatted errors
   *
   * @throws  DataAccessException  On exception.
   */
  public final List<String> getStagingErrors(final Integer importVersionId)
    throws DataAccessException {
    String procName = PACKAGE_NAME + "." + GET_ERRORS_PROC;
    ArrayList<String> errors = new ArrayList<>();
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        errors.add(resultSet.getString("LOG_MESSAGE"));
      }
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return errors;
  }


  /**
   * performImport.
   *
   * @param   importVersionId  importVersionId
   * @param   userId           userId
   *
   * @throws  SQLException  On exception.
   */
  public final void performImport(final Integer importVersionId,
    final String userId) throws SQLException {
    DAOStoredProcedure proc = null;
    final int paramCount = 2;
    String procName = PACKAGE_NAME + "." + OPERATIONAL_PROC;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.setString(index++, userId);

      proc.execute();
    } finally {
      close(proc);
    }
  }

}
