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

import java.math.BigDecimal;
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
  private static final String PACKAGE_NAME = "FARMS_FMV_PKG";

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
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
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

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      if (insertProc == null) {
        final int paramCount = 10;
        String procName = PACKAGE_NAME + "." + INSERT_PROC;

        insertProc = new DAOStoredProcedure(connection,
            procName,
            paramCount,
            false);
      } else {
        insertProc.clearParameters();
      }

      int index = 1;

      insertProc.setLong(index++, (long)rowNum);
      insertProc.setShort(index++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      insertProc.setShort(index++, obj.getPeriod() == null ? null : obj.getPeriod().shortValue());
      insertProc.setBigDecimal(index++, obj.getAveragePrice() == null ? null : BigDecimal.valueOf(obj.getAveragePrice()));
      insertProc.setBigDecimal(index++, obj.getPercentVariance() == null ? null : BigDecimal.valueOf(obj.getPercentVariance()));
      insertProc.setString(index++, obj.getMunicipalityCode());
      insertProc.setString(index++, obj.getUnitCode());
      insertProc.setString(index++, obj.getInventoryCode());
      insertProc.setString(index++, obj.getFileLocation());
      insertProc.setString(index++, userId);

      insertProc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
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

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
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

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
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

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int index = 1;
      proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        errors.add(resultSet.getString("LOG_MESSAGE"));
      }

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
      proc.setString(index++, userId);

      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

}
