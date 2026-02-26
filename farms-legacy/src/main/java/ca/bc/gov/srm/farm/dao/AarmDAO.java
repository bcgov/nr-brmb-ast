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
import java.sql.SQLException;

import ca.bc.gov.srm.farm.domain.staging.AarmCsvRow;

/**
 * Used for importing an AARM CSV import. This file followed the
 * same pattern as the FMV DAO, so there's some duplicate code.
 * Refactor when time permits.
 */
public class AarmDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARMS_AARM_PKG";

  /** INSERT_PROC. */
  private static final String INSERT_PROC = "INSERT_STAGING_ROW";
  
  /** CLEAR_PROC. */
  private static final String CLEAR_PROC = "CLEAR_STAGING";
  
  /** OPERATIONAL_PROC. */
  private static final String OPERATIONAL_PROC = "STAGING_TO_OPERATIONAL";

  
  /** cached for repetative use. */
  private DAOStoredProcedure insertProc = null;
  
  private Connection connection;
  
  /**
   * 
   * @param conn Connection
   */
  public AarmDAO(Connection conn) {
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

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
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
  		final AarmCsvRow obj, 
  		final String userId,
  		final int rowNum)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      if (insertProc == null) {
        final int paramCount = 14;
        String procName = PACKAGE_NAME + "." + INSERT_PROC;
        
        insertProc = new DAOStoredProcedure(connection,
            procName,
            paramCount, 
            false);
      } else {
        insertProc.clearParameters();
      }

      int index = 1;
      insertProc.setLong(index++, obj.getAarmKey() == null ? null : obj.getAarmKey().longValue());
      insertProc.setInt(index++, obj.getParticipantPin());
      insertProc.setShort(index++, obj.getProgramYear() == null ? null : obj.getProgramYear().shortValue());
      insertProc.setShort(index++, obj.getOperationNumber() == null ? null : obj.getOperationNumber().shortValue());
      insertProc.setBigDecimal(index++, obj.getPartnerPercent() == null ? null : BigDecimal.valueOf(obj.getPartnerPercent()));
      insertProc.setShort(index++, obj.getInventoryTypeCode() == null ? null : obj.getInventoryTypeCode().shortValue());
      insertProc.setInt(index++, obj.getInventoryCode());
      insertProc.setString(index++, obj.getInventoryDescription());
      insertProc.setShort(index++, obj.getProductionUnit() == null ? null : obj.getProductionUnit().shortValue());
      insertProc.setBigDecimal(index++, obj.getAarmReferenceP1Price() == null ? null : BigDecimal.valueOf(obj.getAarmReferenceP1Price()));
      insertProc.setBigDecimal(index++, obj.getAarmReferenceP2Price() == null ? null : BigDecimal.valueOf(obj.getAarmReferenceP2Price()));
      insertProc.setBigDecimal(index++, obj.getQuantityStart() == null ? null : BigDecimal.valueOf(obj.getQuantityStart()));
      insertProc.setBigDecimal(index++, obj.getQuantityEnd() == null ? null : BigDecimal.valueOf(obj.getQuantityEnd()));
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

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
        
        int index = 1;
        proc.setLong(index++, importVersionId == null ? null : importVersionId.longValue());
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
