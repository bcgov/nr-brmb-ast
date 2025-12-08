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

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * DAO used by the webapp for the COB report.
 */
public class CobDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  private static final String INSERT_PROC = "INSERT_COB";
  
  private static final String UPDATE_PROC = "UPDATE_COB";
  
  private static final String DELETE_PROC = "DELETE_COB";

  private static final String BLOB_UPDATE_PROC = "GET_COB_BLOB_UPD";

  private static final String GET_BLOB_PROC = "GET_COB_BLOB";
  


  /**
   * @param   transaction    transaction
   * @param   scenarioId  scenarioId
   * @param   userId  userId
   * 
   * @throws  DataAccessException  on exception
   */
  public final void insertCob(
  	final Transaction transaction,
    final Integer scenarioId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + INSERT_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    final int paramCount = 3;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

      int index = 1;
      proc.registerOutParameter(index, Types.INTEGER);

      proc.setInt(index++, (Integer) null);
      proc.setInt(index++, scenarioId);
      proc.setString(index++, userId);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
  }
  
  
  
  /**
   * @param   transaction    transaction
   * @param   scenarioId  scenarioId
   * @param   userId  userId
   * 
   * @throws  DataAccessException  on exception
   */
  public final void updateCob(
  	final Transaction transaction,
    final Integer scenarioId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + UPDATE_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    final int paramCount = 2;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

      int index = 1;
      proc.setInt(index++, scenarioId);
      proc.setString(index++, userId);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
  }



  /**
   * @param   connection       connection
   * @param   scenarioId  scenarioId
   * @param   update           update
   *
   * @return  BLOB
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public Blob getBlob(
  	final Connection connection,
    final Integer scenarioId, 
    final boolean update)
    throws DataAccessException {
    Blob blob = null;
    DAOStoredProcedure proc = null;
    ResultSet resultSet = null;
    final int paramCount = 1;
    String procName = PACKAGE_NAME + "." + GET_BLOB_PROC;

    if (update) {
      procName = PACKAGE_NAME + "." + BLOB_UPDATE_PROC;
    }

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      proc.setInt(paramCount, scenarioId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        blob = resultSet.getBlob(1);
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex);
    } finally {
      close(resultSet, proc);
    }

    return blob;
  }
  
  
  
  /**
   * @param   transaction    transaction
   * @param   scenarioId  scenarioId
   * @param   userId  userId
   * 
   * @throws  DataAccessException  on exception
   */
  public final void deleteCob(
    final Transaction transaction,
    final Integer scenarioId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + DELETE_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    final int paramCount = 1;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

      int index = 1;
      proc.setInt(index++, scenarioId);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
  }
}
