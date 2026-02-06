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

import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * DAO used by the webapp for the import screens.
 */
public class ImportDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  private static final String GET_PROC = "GET_IMPORT_VERSION";

  private static final String CANCEL_PROC = "CANCEL_IMPORT";

  private static final String CONFIRM_PROC = "CONFIRM_IMPORT";
  
  private static final String RETRY_STAGING_PROC = "RETRY_STAGING";

  private static final String INSERT_PROC = "INSERT_IMPORT_VERSION";

  private static final String SCHEDULED_PROC = "GET_SCHEDULED_JOB";

  private static final String IN_PROGRESS_PROC = "IMPORT_IN_PROGRESS_CHECK";


  private static final String BLOB_UPDATE_PROC = "GET_IMPORT_VERSION_BLOB_UPD";

  private static final String GET_BLOB_PROC = "GET_IMPORT_VERSION_BLOB";
  
 
  


  /**
   * @param   transaction      transaction
   * @param   importVersionId  importVersionId
   *
   * @return  The state code
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public final String getStateCode(
  	final Transaction transaction,
    final Integer importVersionId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + GET_PROC;
    String stateCode = null;
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      proc.setInt(paramCount, importVersionId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        stateCode = resultSet.getString("IMPORT_STATE_CODE");
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return stateCode;
  }


  /**
   * @param   transaction      transaction
   * @param   importVersionId  importVersionId
   *
   * @return  List of ImportSearchResult
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public final ImportVersion getImportVersion(
  	final Transaction transaction,
    final Integer importVersionId) 
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    
    return getImportVersion(connection, importVersionId);
  }


  public ImportVersion getImportVersion(Connection connection, final Integer importVersionId) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + GET_PROC;
    ImportVersion iv = null;
    final int paramCount = 1;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true); ) {

      proc.setInt(paramCount, importVersionId);
      proc.execute();
      
      try (ResultSet rs = proc.getResultSet(); ) {

        if (rs.next()) {
          iv = createImportVersion(rs);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return iv;
  }


  /**
   * @param   connection  connection
   *
   * @return  ImportVersion or null
   *
   * @throws  DataAccessException  on exception
   */
  public final ImportVersion getScheduledJob(
  	final Connection connection,
  	final String jobType
  )
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + SCHEDULED_PROC;
    ImportVersion iv = null;
    ResultSet rs = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setString(index++, jobType);
      
      proc.execute();
      rs = proc.getResultSet();

      if (rs.next()) {
        iv = createImportVersion(rs);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }

    return iv;
  }


  /**
   * @param   connection  connection
   *
   * @throws  DataAccessException  on exception
   */
  public final void checkForInProgessImports(final Connection connection)
    throws DataAccessException {
    String procName = PACKAGE_NAME + "." + IN_PROGRESS_PROC;
    DAOStoredProcedure proc = null;
    final int paramCount = 0;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  /**
   * @param   rs  rs
   *
   * @return  a ImportVersion
   *
   * @throws  SQLException  on exception
   */
  private ImportVersion createImportVersion(ResultSet rs) throws SQLException {
    ImportVersion iv = new ImportVersion();

    iv.setImportVersionId(new Integer(rs.getInt("import_version_id")));
    iv.setImportedByUser(rs.getString("imported_by_user"));
    iv.setDescription(rs.getString("description"));
    iv.setImportFileName(rs.getString("import_file_name"));
    iv.setImportControlFileDate(rs.getDate("import_control_file_date"));
    iv.setImportControlFileInfo(rs.getString("import_control_file_info"));
    iv.setImportDate(rs.getDate("import_date"));
    iv.setImportStateCode(rs.getString("import_state_code"));
    iv.setImportStateDescription(rs.getString("import_state_description"));
    iv.setImportClassCode(rs.getString("import_class_code"));
    iv.setImportClassDescription(rs.getString("import_class_description"));
    iv.setLastStatusMessage(rs.getString("last_status_message"));
    iv.setLastStatusDate(rs.getTimestamp("last_status_date"));
    iv.setIsLatestOfClass(Boolean.valueOf(getIndicator(rs, "latest_of_class_ind")));
    iv.setAuditInfo(rs.getString("import_audit_info"));

    return iv;
  }


  public final void insertImportVersion(
  	final Connection connection,
    final ImportVersion importVersion) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + INSERT_PROC;
    DAOStoredProcedure proc = null;
    final int paramCount = 7;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index, Types.INTEGER);
      
      String importStateCode = importVersion.getImportStateCode() == null ? ImportStateCodes.SCHEDULED_FOR_STAGING : importVersion.getImportStateCode();

      proc.setInt(index++, (Integer) null);
      proc.setString(index++, importVersion.getImportClassCode());
      proc.setString(index++, importStateCode);
      proc.setString(index++, importVersion.getDescription());
      proc.setString(index++, importVersion.getImportFileName());
      proc.setString(index++, null);  // passwords no longer used
      proc.setString(index++, importVersion.getImportedByUser());
      proc.execute();

      index = 1;
      importVersion.setImportVersionId(new Integer(proc.getInt(index)));
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  public ImportVersion createEmptyTransferRecord(String userId, Connection connection, String importClassCode, String importStateCode,
      String transferDescription) throws DataAccessException {
    final String fileName = "none";

    ImportVersion importVersion = new ImportVersion();
    importVersion.setImportClassCode(importClassCode);
    importVersion.setImportStateCode(importStateCode);
    importVersion.setDescription(transferDescription);
    importVersion.setImportedByUser(userId);
    importVersion.setImportFileName(fileName);

    insertImportVersion(connection , importVersion);
    
    return importVersion;
  }


  public final void cancelImport(final Transaction transaction,
    final Integer importVersionId) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + CANCEL_PROC;
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);
      proc.setInt(paramCount, importVersionId);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
    }
  }


  public final void confirmImport(final Transaction transaction,
    final Integer importVersionId) throws DataAccessException {
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    confirmImport(connection, importVersionId);
  }


  public void confirmImport(final Connection connection, final Integer importVersionId) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + CONFIRM_PROC;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);
      proc.setInt(paramCount, importVersionId);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  public final void retryStaging(final Transaction transaction,
      final Integer importVersionId) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + RETRY_STAGING_PROC;
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    final int paramCount = 1;
    
    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);
      proc.setInt(paramCount, importVersionId);
      proc.execute();
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
    }
  }



  public Blob getBlob(final Connection connection,
    final Integer importVersionId, final boolean update)
    throws DataAccessException {
    Blob blob = null;
    final int paramCount = 1;
    String procName = PACKAGE_NAME + "." + GET_BLOB_PROC;

    if (update) {
      procName = PACKAGE_NAME + "." + BLOB_UPDATE_PROC;
    }

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true);) {
      proc.setInt(paramCount, importVersionId);
      proc.execute();
      
      try (ResultSet resultSet = proc.getResultSet();) {

        if (resultSet.next()) {
          blob = (resultSet.getBlob(1));
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(ex);
    }

    return blob;
  }
}
