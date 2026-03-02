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

import java.io.IOException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.dbpool.WrapperConnection;


/**
 * @author  dzwiers
 */
public class VersionDAO {

  /** Logger. */
  private Logger logger = LoggerFactory.getLogger(VersionDAO.class);

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARMS_VERSION_PKG";

  private static final String CREATE_VERSION_PROC = "CREATE_VERSION";

  private static final int CREATE_VERSION_PARAM = 3;

  private static final String UPDATE_CONTROL_FILE_PROC =
    "UPDATE_CONTROL_FILE_INFO_STG";

  private static final int UPDATE_CONTROL_FILE_PARAM = 2;

  private static final String UPLOADED_VERSION_PROC = "UPLOADED_VERSION";

  private static final int UPLOADED_VERSION_PARAM = 3;

  private static final String START_IMPORT_PROC = "START_IMPORT";

  private static final int START_IMPORT_PARAM = 2;

  private static final String START_UPLOAD_PROC = "START_UPLOAD";

  private static final int START_UPLOAD_PARAM = 2;

  private static final String PERFORM_IMPORT_PROC = "PERFORM_IMPORT";

  private static final int PERFORM_IMPORT_PARAM = 2;

  private static final String UPLOAD_FAILURE_PROC = "UPLOAD_FAILURE";

  private static final int UPLOAD_FAILURE_PARAM = 3;

  private static final String IMPORT_FAILURE_PROC = "IMPORT_FAILURE";

  private static final int IMPORT_FAILURE_PARAM = 2;
  
  private static final String IMPORT_COMPLETE_PROC = "IMPORT_COMPLETE";
  
  private static final int IMPORT_COMPLETE_PARAM = 2;
  
  private static final String CLEAR_SUCCESSFUL_TRANSFERS_PROC = "CLEAR_SUCCESSFUL_TRANSFERS";
  
  private static final int CLEAR_SUCCESSFUL_TRANSFERS_PARAM = 0;
  

  /** conn. */
  private Connection conn = null;
  
  @SuppressWarnings("unused")
  private Connection neverUse = null;

  /**
   * Creates a new StagingDAO object.
   *
   * @param  c  Input parameter to initialize class.
   */
  public VersionDAO(final Connection c) {
    neverUse = c;
    if(c instanceof WrapperConnection){
      WrapperConnection wc = (WrapperConnection)c;
      this.conn = wc.getWrappedConnection();
    }else{
      this.conn = c;
    }
  }

  /**
   * @param   importUser      importUser
   * @param   description     description
   * @param   uploadFileName  uploadFileName
   *
   * @return  New Import Version ID
   *
   * @throws  SQLException  SQLException
   */
  public final Integer createVersion(final String importUser,
    final String description, final String uploadFileName) throws SQLException {

    Integer r = null;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + CREATE_VERSION_PROC,
            CREATE_VERSION_PARAM, Types.INTEGER);) {

        int c = 1;
        proc.setString(c++, description);
        proc.setString(c++, uploadFileName);
        proc.setString(c++, importUser);

        proc.execute();

        r = proc.getInt(1);
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }

    return r;
  }

  /**
   * @param   pVersionId  pVersionId
   * @param   userId      userId
   * @throws  SQLException  SQLException
   */
  public final void updateControlFileInfo(final Integer pVersionId,
    final String userId) throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + UPDATE_CONTROL_FILE_PROC,
            UPDATE_CONTROL_FILE_PARAM, false);) {

        int c = 1;
        proc.setLong(c++, pVersionId == null ? null : pVersionId.longValue());
        proc.setString(c++, userId);

        proc.execute();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * @param   pVersionId  - the version id to Confirm or Fail.
   * @param   xml         errors Error Xml -- may be null if non exist
   * @param   hasErrors   True when errors exist
   * @param   userId      userId
   *
   * @throws  SQLException  SQLException
   * @throws  IOException   IOException
   */
  public final void uploadedVersion(final Integer pVersionId, final String xml,
    final Boolean hasErrors, final String userId) throws SQLException,
    IOException {

    Clob clob = null;

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
          PACKAGE_NAME + "." + UPLOADED_VERSION_PROC,
          UPLOADED_VERSION_PARAM, true);) {

        int c = 1;
        proc.setLong(c++, pVersionId == null ? null : pVersionId.longValue());
        proc.setString(c++, xml);
        proc.setIndicator(c++, hasErrors);
        proc.setString(c++, userId);
        proc.execute();

        try (ResultSet resultSet = proc.getResultSet();) {

          if (resultSet.next()) {
    
            // get clob from cursor
            clob = resultSet.getClob(1);
            
            try(Writer writer = clob.setCharacterStream(0);) {
              writer.write(xml);
              writer.flush();
            }
          }
        }

      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * @param   pVersionId  pVersionId
   * @param   userId      userId
   * @throws  SQLException  SQLException
   */
  public final void startUpload(final Integer pVersionId, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + START_UPLOAD_PROC,
            START_UPLOAD_PARAM, false);) {

        int c = 1;
        proc.setLong(c++, pVersionId == null ? null : pVersionId.longValue());
        proc.setString(c++, userId);

        proc.execute();

      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }


  /**
   * @param   pVersionId  pVersionId
   * @param   userId      userId
   * @throws  SQLException  SQLException
   */
  public final void startImport(final Integer pVersionId, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + START_IMPORT_PROC,
            START_IMPORT_PARAM, false);) {

        int c = 1;
        proc.setLong(c++, pVersionId == null ? null : pVersionId.longValue());
        proc.setString(c++, userId);

        proc.execute();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * @param   pVersionId  pVersionId
   * @param   userId      userId
   * @throws  SQLException  SQLException
   */
  public final void performImport(final Integer pVersionId, final String userId)
    throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + PERFORM_IMPORT_PROC,
            PERFORM_IMPORT_PARAM, false);) {

        int c = 1;
        proc.setLong(c++, pVersionId == null ? null : pVersionId.longValue());
        proc.setString(c++, userId);

        proc.execute();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }

  /**
   * This proc is for 'unexpected' failures -- meaning these failures are not
   * business rules.
   *
   * @param  pImportVersionId  version id
   * @param  pMessage          error message
   * @param  pUserId           user id
   */
  public final void uploadFailed(final Integer pImportVersionId,
    final String pMessage, final String pUserId) {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
              PACKAGE_NAME + "." + UPLOAD_FAILURE_PROC,
              UPLOAD_FAILURE_PARAM, false);) {

        int c = 1;
        proc.setLong(c++, pImportVersionId == null ? null : pImportVersionId.longValue());
        proc.setString(c++, pMessage);
        proc.setString(c++, pUserId);

        proc.execute();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      // eat it
      logger.error("Unexpected error: ", e);
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        logger.error("Unexpected error: ", ex);
      }
    }
  }
  
 

  /**
   * This proc is for 'unexpected' failures -- meaning these failures are not
   * business rules.
   *
   * @param   pImportVersionId  version id
   * @param   pMessage          error message
   * @param   pUserId           user id
   *
   * @throws  SQLException  SQLException
   */
  public final void importFailed(final Integer pImportVersionId,
    final String pMessage, final String pUserId) throws SQLException, IOException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + IMPORT_FAILURE_PROC,
            IMPORT_FAILURE_PARAM, true);) {

        int c = 1;
        proc.setLong(c++, pImportVersionId == null ? null : pImportVersionId.longValue());
        proc.setString(c++, pMessage);
        proc.setString(c++, pUserId);

        proc.execute();

        try (ResultSet resultSet = proc.getResultSet();) {

          if (resultSet.next()) {
    
            // get clob from cursor
            Clob clob = resultSet.getClob(1);
            
            try(Writer writer = clob.setCharacterStream(0);) {
              writer.write(pMessage);
              writer.flush();
            }
          }
        }
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }
  
  
  /**
   * @param   pImportVersionId  version id
   * @param   pMessage          error message
   * @param   pUserId           user id
   *
   * @throws  SQLException  SQLException
   */
  public final void importCompleted(final Integer pImportVersionId,
      final String pMessage, final String pUserId) throws SQLException, IOException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + IMPORT_COMPLETE_PROC,
            IMPORT_COMPLETE_PARAM, true);) {
        
        int c = 1;
        proc.setLong(c++, pImportVersionId == null ? null : pImportVersionId.longValue());
        proc.setString(c++, pMessage);
        proc.setString(c++, pUserId);
        
        proc.execute();

        try (ResultSet resultSet = proc.getResultSet();) {

          if (resultSet.next()) {
    
            // get clob from cursor
            Clob clob = resultSet.getClob(1);
            
            try(Writer writer = clob.setCharacterStream(0);) {
              writer.write(pMessage);
              writer.flush();
            }
          }
        }
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }
  
  
  /**
   * @throws  SQLException  SQLException
   */
  public final void clearSuccessfulTransfers() throws SQLException {

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(conn,
            PACKAGE_NAME + "." + CLEAR_SUCCESSFUL_TRANSFERS_PROC,
            CLEAR_SUCCESSFUL_TRANSFERS_PARAM, false);) {
        
        proc.execute();
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }
}
