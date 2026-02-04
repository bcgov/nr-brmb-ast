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

import ca.bc.gov.srm.farm.domain.staging.AarmCsvRow;

/**
 * Used for importing an AARM CSV import. This file followed the
 * same pattern as the FMV DAO, so there's some duplicate code.
 * Refactor when time permits.
 */
public class AarmDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_AARM_PKG";

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

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
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
  		final AarmCsvRow obj, 
  		final String userId,
  		final int rowNum)
    throws SQLException {

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
    insertProc.setInt(index++, obj.getAarmKey());
    insertProc.setInt(index++, obj.getParticipantPin());
    insertProc.setInt(index++, obj.getProgramYear());
    insertProc.setInt(index++, obj.getOperationNumber());
    insertProc.setDouble(index++, obj.getPartnerPercent());
    insertProc.setInt(index++, obj.getInventoryTypeCode());
    insertProc.setInt(index++, obj.getInventoryCode());
    insertProc.setString(index++, obj.getInventoryDescription());
    insertProc.setInt(index++, obj.getProductionUnit());
    insertProc.setDouble(index++, obj.getAarmReferenceP1Price());
    insertProc.setDouble(index++, obj.getAarmReferenceP2Price());
    insertProc.setDouble(index++, obj.getQuantityStart());
    insertProc.setDouble(index++, obj.getQuantityEnd());
    insertProc.setString(index++, userId);

    insertProc.execute();
  }
  
  
  public final void performImport(
  	final Integer importVersionId,
  	final String userId) 
  throws SQLException {
    final int paramCount = 2;
  	String procName = PACKAGE_NAME + "." + OPERATIONAL_PROC;

    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {
      
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.setString(index++, userId);
      
      proc.execute();
    }
  }

}
