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

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
      proc.execute();
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

    insertProc.setInt(index++, rowNum);
    insertProc.setInt(index++, obj.getProgramYear());
    insertProc.setString(index++, obj.getMunicipalityCode());
    insertProc.setString(index++, obj.getInventoryCode());
    insertProc.setString(index++, obj.getUnitDescription());
    insertProc.setDouble(index++, obj.getYearMinus6Margin());
    insertProc.setDouble(index++, obj.getYearMinus5Margin());
    insertProc.setDouble(index++, obj.getYearMinus4Margin());
    insertProc.setDouble(index++, obj.getYearMinus3Margin());
    insertProc.setDouble(index++, obj.getYearMinus2Margin());
    insertProc.setDouble(index++, obj.getYearMinus1Margin());
    insertProc.setDouble(index++, obj.getYearMinus6Expense());
    insertProc.setDouble(index++, obj.getYearMinus5Expense());
    insertProc.setDouble(index++, obj.getYearMinus4Expense());
    insertProc.setDouble(index++, obj.getYearMinus3Expense());
    insertProc.setDouble(index++, obj.getYearMinus2Expense());
    insertProc.setDouble(index++, obj.getYearMinus1Expense());
    insertProc.setString(index++, userId);

    insertProc.execute();
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

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
      
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
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

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
      
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
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

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true);) {
      
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
      try (ResultSet resultSet = proc.getResultSet();) {

        while (resultSet.next()) {
        	errors.add(resultSet.getString("LOG_MESSAGE"));
        }
      }
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
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

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.setString(index++, userId);
      
      proc.execute();
    }
  }

}
