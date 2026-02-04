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
import java.io.InputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.dataimport.FileLineMessage;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportResults;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.IMPORTLOG;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.STAGINGLOG;
import oracle.jdbc.OracleResultSet;

/**
 * DAO used by the webapp for the import screens.
 */
public class ImportXmlDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_IMPORT_XML_PKG";



  private static final String IMPORT_ERRORS_PROC =
    "GET_IMPORT_TOP_LEVEL_ERRORS";
  
  private static final String IMPORT_NUMBERS_PROC = 
  	"GET_IMPORT_NUMBERS";
  
  private static final String IMPORT_LOG_PROC = 
  	"GET_IMPORT_LOG";
  
  private static final String STAGING_LOG_PROC = 
  	"GET_STAGING_LOG";

 
  
  
  /**
   * @param   transaction      transaction
   * @param   importVersionId  the ID
   *
   * @return  List of FileLineMessage
   *
   * @throws  DataAccessException  on exception
   */
  public final List<FileLineMessage> getImportTopLevelErrors(
  	final Transaction transaction,
    final Integer importVersionId) throws DataAccessException {
    return getFileLineMessages(transaction, importVersionId,
        IMPORT_ERRORS_PROC);
  }


  /**
   * @param   transaction      transaction
   * @param   importVersionId  the ID
   * @param   procName         proc name
   *
   * @return  List of FileLineMessage
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  private List<FileLineMessage> getFileLineMessages(final Transaction transaction,
    final Integer importVersionId, final String procName)
    throws DataAccessException {
    String qualifiedProcName = PACKAGE_NAME + "." + procName;
    List<FileLineMessage> items = new ArrayList<>();
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(
          connection,
          qualifiedProcName,
          paramCount,
          true);

      proc.setInt(paramCount, importVersionId);
      proc.execute();
      resultSet = proc.getResultSet();

      while (resultSet.next()) {
        FileLineMessage result = new FileLineMessage();

        result.setFileNumber(resultSet.getString("FILE_NUMBER"));
        result.setLineNumber(resultSet.getString("LINE_NUMBER"));

        //
        // We are showing the errors in a YUI datatable, and newlines
        // in the error messages screw up the javascript.
        //
        String msg = resultSet.getString("MESSAGE");
        msg = StringUtils.replace(msg, "\n", " ");
        result.setMessage(msg);

        items.add(result);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return items;
  }

  
  /**
   * Used for FMV and BPU imports to get the number of adds/updates/total
   * @param   transaction      transaction
   * @param   importVersionId  the ID
   * @param   importResults  the results
   *
   * @throws  DataAccessException  on exception
   */
  @SuppressWarnings("resource")
  public final void getImportNumbers(
  	final Transaction transaction,
    final Integer importVersionId,
    final ImportResults importResults) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + IMPORT_NUMBERS_PROC;
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
      	importResults.setNumberOfItems(resultSet.getInt("NUM_TOTAL"));
      	importResults.setNumberOfAdditions(resultSet.getInt("NUM_NEW"));
      	importResults.setNumberOfUpdates(resultSet.getInt("NUM_UPDATES"));
      	importResults.setNumberOfValueUpdates(resultSet.getInt("NUM_VALUE_UPDATES")); // BPU import only!
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }
  } 
  
  
  
  public final IMPORTLOG getImportLog(
        final Connection connection,
        final Integer importVersionId
   ) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + IMPORT_LOG_PROC;
    IMPORTLOG log = null;
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        Clob xmlClob = ((OracleResultSet) resultSet).getClob(1);
        try(InputStream stream = xmlClob.getAsciiStream();) {

          String pkg = "ca.bc.gov.srm.farm.ui.domain.resultsimport";
          JAXBContext jc = JAXBContext.newInstance(pkg);
          Unmarshaller um = jc.createUnmarshaller();
          log = (IMPORTLOG) um.unmarshal(stream);
        } 
      }
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } catch (JAXBException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } catch (IOException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return log;
  }
  
  
  /**
   * @param   connection  connection
   * @param   importVersionId  importVersionId
   *
   * @return  IMPORTLOG
   *
   * @throws  DataAccessException  on exception
   */
  public final STAGINGLOG getStagingLog(
        final Connection connection,
        final Integer importVersionId
   ) throws DataAccessException {
    String procName = PACKAGE_NAME + "." + STAGING_LOG_PROC;
    STAGINGLOG log = null;
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setInt(index++, importVersionId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        Clob xmlClob = ((OracleResultSet) resultSet).getClob(1);
        try(InputStream stream = xmlClob.getAsciiStream();) {

          String pkg = "ca.bc.gov.srm.farm.ui.domain.resultsstaging";
          JAXBContext jc = JAXBContext.newInstance(pkg);
          Unmarshaller um = jc.createUnmarshaller();
          log = (STAGINGLOG) um.unmarshal(stream);
        }
      }
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } catch (JAXBException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } catch (IOException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return log;
  }

}
