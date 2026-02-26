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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import ca.bc.gov.srm.farm.domain.staging.BpuCsvRow;
import ca.bc.gov.srm.farm.exception.DataAccessException;

/**
 * Used for importing an Bpu CSV import. This file followed the
 * same pattern as the FMV DAO, so there's some duplicate code.
 * Refactor when time permits.
 */
public class BpuDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_BPU_PKG";

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
  
  /** document me **/
  private Connection connection;
  
  /**
   * 
   * @param conn Connection
   */
  public BpuDAO(Connection conn) {
  	connection = conn;
  }
  
  
  /**
   * close.
   */
  public final void close() {
    close(insertProc);
  }
  
  
  /**
   * clearStaging.
   *
   * @throws  SQLException  On exception.
   */
  public final void clearStaging() throws SQLException {
    final int paramCount = 0;
  	String procName = PACKAGE_NAME + "." + CLEAR_PROC;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
        proc.execute();
      }

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
   * insert.
   *
   * @param   obj     The parameter value.
   * @param   userId  userId
   * @param   rowNum  rowNum
   *
   * @throws  SQLException  On exception.
   */
  public final void insert(
  		final BpuCsvRow obj, 
  		final String userId,
  		final int rowNum)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      if (insertProc == null) {
        final int paramCount = 18;
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
      insertProc.setString(index++, obj.getMunicipalityCode());
      insertProc.setString(index++, obj.getInventoryCode());
      insertProc.setString(index++, obj.getUnitDescription());
      insertProc.setBigDecimal(index++, obj.getYearMinus6Margin() == null ? null : BigDecimal.valueOf(obj.getYearMinus6Margin()));
      insertProc.setBigDecimal(index++, obj.getYearMinus5Margin() == null ? null : BigDecimal.valueOf(obj.getYearMinus5Margin()));
      insertProc.setBigDecimal(index++, obj.getYearMinus4Margin() == null ? null : BigDecimal.valueOf(obj.getYearMinus4Margin()));
      insertProc.setBigDecimal(index++, obj.getYearMinus3Margin() == null ? null : BigDecimal.valueOf(obj.getYearMinus3Margin()));
      insertProc.setBigDecimal(index++, obj.getYearMinus2Margin() == null ? null : BigDecimal.valueOf(obj.getYearMinus2Margin()));
      insertProc.setBigDecimal(index++, obj.getYearMinus1Margin() == null ? null : BigDecimal.valueOf(obj.getYearMinus1Margin()));
      insertProc.setBigDecimal(index++, obj.getYearMinus6Expense() == null ? null : BigDecimal.valueOf(obj.getYearMinus6Expense()));
      insertProc.setBigDecimal(index++, obj.getYearMinus5Expense() == null ? null : BigDecimal.valueOf(obj.getYearMinus5Expense()));
      insertProc.setBigDecimal(index++, obj.getYearMinus4Expense() == null ? null : BigDecimal.valueOf(obj.getYearMinus4Expense()));
      insertProc.setBigDecimal(index++, obj.getYearMinus3Expense() == null ? null : BigDecimal.valueOf(obj.getYearMinus3Expense()));
      insertProc.setBigDecimal(index++, obj.getYearMinus2Expense() == null ? null : BigDecimal.valueOf(obj.getYearMinus2Expense()));
      insertProc.setBigDecimal(index++, obj.getYearMinus1Expense() == null ? null : BigDecimal.valueOf(obj.getYearMinus1Expense()));
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
   * @param importVersionId importVersionId
   *
   * @throws  SQLException  On exception.
   */
  public final void validateStaging(
  	final Integer importVersionId) 
  throws SQLException {
    final int paramCount = 1;
  	String procName = PACKAGE_NAME + "." + VALIDATE_PROC;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
        
        int index = 1;
        proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
        proc.execute();
      }

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
   * deleteStagingErrors.
   * @param importVersionId importVersionId
   *
   * @throws  SQLException  On exception.
   */
  public final void deleteStagingErrors(
  	final Integer importVersionId
  ) 
  throws SQLException {
    final int paramCount = 1;
  	String procName = PACKAGE_NAME + "." + DELETE_ERRORS_PROC;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
        
        int index = 1;
        proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
        proc.execute();
      }

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
   * getStagingErrors.
   * @param importVersionId importVersionId
   * @return List of XML formatted errors
   * @throws  DataAccessException  On exception.
   */
  public final List<String> getStagingErrors(final Integer importVersionId) throws DataAccessException {
  	String procName = PACKAGE_NAME + "." + GET_ERRORS_PROC;
    List<String> errors = new ArrayList<>();
    final int paramCount = 1;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true);) {
        
        int index = 1;
        proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
        proc.execute();
        try (ResultSet resultSet = proc.getResultSet();) {

          while (resultSet.next()) {
            errors.add(resultSet.getString("LOG_MESSAGE"));
          }
        }
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
   * @param importVersionId importVersionId
   * @param userId userId 
   *
   * @throws  SQLException  On exception.
   */
  public final void performImport(
  	final Integer importVersionId,
  	final String userId) 
  throws SQLException {
    final int paramCount = 2;
  	String procName = PACKAGE_NAME + "." + OPERATIONAL_PROC;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
        int index = 1;
        proc.setBigDecimal(index++, importVersionId == null ? null : BigDecimal.valueOf(importVersionId));
        proc.setString(index++, userId);
        
        proc.execute();
      }

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

}
