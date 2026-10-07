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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * DAO used by the webapp for the COB report.
 */
public class CobDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARMS_WEBAPP_PKG";

  private static final String INSERT_PROC = "INSERT_COB";
  
  private static final String UPDATE_PROC = "UPDATE_COB";
  
  private static final String DELETE_PROC = "DELETE_COB";

  private static final String GET_BLOB_PROC = "GET_COB_BLOB";

  private static final String UPDATE_DOCUMENT_PROC = "UPDATE_COB_DOCUMENT";

  /**
   * Persist a completed Jasper PDF within the caller's transaction. Unlike the legacy
   * helpers, this operation never commits an empty document or clears an existing PDF
   * in a separate transaction. The service must commit, or roll back on failure.
   */
  public void saveJasperCob(Transaction transaction, Integer scenarioId, byte[] document,
      boolean isInsert, String userId) throws DataAccessException {
    if (scenarioId == null || document == null || document.length == 0) {
      throw new IllegalArgumentException("A scenario and non-empty COB document are required.");
    }
    Connection connection = getConnection(transaction);
    try {
      if (connection.getAutoCommit()) {
        throw new SQLException("Saving a Jasper COB requires an active transaction.");
      }
      // Serialize new Jasper saves for a scenario; there is no unique document/scenario constraint.
      try (PreparedStatement lock = connection.prepareStatement(
          "SELECT agristability_scenario_id FROM farms.farm_agristability_scenarios "
              + "WHERE agristability_scenario_id = ? FOR UPDATE")) {
        lock.setLong(1, scenarioId.longValue());
        try (ResultSet rows = lock.executeQuery()) {
          if (!rows.next()) {
            throw new SQLException("COB scenario no longer exists: " + scenarioId);
          }
        }
      }
      try (PreparedStatement check = connection.prepareStatement(
          "SELECT count(*) FROM farms.farm_benefit_calc_documents WHERE agristability_scenario_id = ?")) {
        check.setLong(1, scenarioId.longValue());
        try (ResultSet rows = check.executeQuery()) {
          rows.next();
          int count = rows.getInt(1);
          if ((isInsert && count != 0) || (!isInsert && count != 1)) {
            throw new SQLException("COB document state changed for scenario " + scenarioId
                + "; reload the scenario before generating or reprinting.");
          }
        }
      }
      String procedure = PACKAGE_NAME + "." + (isInsert ? INSERT_PROC : UPDATE_PROC);
      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procedure, isInsert ? 3 : 2, false)) {
        int index = 1;
        if (isInsert) {
          proc.registerOutParameter(index, Types.BIGINT);
          proc.setLong(index++, (Long) null);
        }
        proc.setLong(index++, scenarioId.longValue());
        proc.setString(index, userId);
        proc.execute();
      }
      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection,
          PACKAGE_NAME + "." + UPDATE_DOCUMENT_PROC, 3, false)) {
        proc.setLong(1, scenarioId.longValue());
        proc.setBytes(2, document);
        proc.setString(3, userId);
        proc.execute();
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }
  


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
    boolean originalAutoCommit = true;

    final int paramCount = 3;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

        int index = 1;
        proc.registerOutParameter(index, Types.BIGINT);

        proc.setLong(index++, (Long) null);
        proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
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
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;

    final int paramCount = 2;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

        int index = 1;
        proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
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
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }



  /**
   * @param   connection  connection
   * @param   scenarioId  scenarioId
   *
   * @return  the document content, or null if there is no COB record for the scenario
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public byte[] getDocument(
  	final Connection connection,
    final Integer scenarioId)
    throws DataAccessException {
    byte[] document = null;
    DAOStoredProcedure proc = null;
    ResultSet resultSet = null;
    final int paramCount = 1;
    String procName = PACKAGE_NAME + "." + GET_BLOB_PROC;

    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      proc.setLong(paramCount, scenarioId == null ? null : scenarioId.longValue());
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        document = resultSet.getBytes(1);
      }

      connection.commit();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        ex.addSuppressed(rollbackEx);
      }
      throw new DataAccessException(ex);
    } finally {
      close(resultSet, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw new DataAccessException(ex);
      }
    }

    return document;
  }



  /**
   * Write the generated document content onto the COB record for the scenario.
   *
   * @param   transaction  transaction
   * @param   scenarioId   scenarioId
   * @param   document     document content
   * @param   userId       userId
   *
   * @throws  DataAccessException  on exception
   */
  public final void saveDocument(
    final Transaction transaction,
    final Integer scenarioId,
    final byte[] document,
    final String userId)
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + UPDATE_DOCUMENT_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    final int paramCount = 3;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

        int index = 1;
        proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
        proc.setBytes(index++, document);
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
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
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
    boolean originalAutoCommit = true;

    final int paramCount = 1;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

        int index = 1;
        proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
        proc.execute();
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
  }
}
