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
package ca.bc.gov.srm.farm.service.impl;


import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.csv.AarmFileHandle;
import ca.bc.gov.srm.farm.csv.CSVParserException;
import ca.bc.gov.srm.farm.csv.FileHandle;
import ca.bc.gov.srm.farm.dao.AarmDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.staging.AarmCsvRow;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.AarmService;



/**
 * AarmServiceImpl. This class follows the same pattern as the FMV service, so it
 * could be refactored to remove duplicate code when time permits.
 */
final class AarmServiceImpl extends BaseService implements AarmService {

  private ArrayList<CSVParserException> stagingErrors = new ArrayList<>();

  private Logger logger = LoggerFactory.getLogger(BpuServiceImpl.class);

  /**
   * Mainly used for unit testing.
   *
   * @return  a list of CSVParserException
   */
  @Override
  public List<CSVParserException> getStagingErrors() {
    return stagingErrors;
  }

  /**
   * Imports the CSV into the staging area.
   *
   * @param  connection       connection
   * @param  importVersionId  Integer
   * @param  csvFile          File
   * @param  userId           userId
   */
  @Override
  public void importCSV(
  	final Connection connection,
    final Integer importVersionId, 
    final File csvFile, 
    final String userId) {

    VersionDAO vdao = null;
    AarmDAO aarmDao = null;

    try {
      vdao = new VersionDAO(connection);
      aarmDao = new AarmDAO(connection);

      // begin
      stagingErrors.clear();
      connection.setAutoCommit(false);
      
      aarmDao.clearStaging();
      connection.commit();
      
      vdao.startUpload(importVersionId, userId);
      connection.commit();

      //
      // Note that this CSV file doesn't have a header row that we can validate.
      // Also note that the staging area is not validated. All the errors will
      // have been generated when the file is parsed.
      //
      FileHandle fileHandle = AarmFileHandle.read(csvFile);
      int numRows = uploadToStaging(userId, aarmDao, fileHandle);

      // end
			String xml = ImportLogFormatter.createStagingXml(numRows, stagingErrors);
      Boolean hasErrors = new Boolean(stagingErrors.size() != 0);
      vdao.uploadedVersion(importVersionId, xml, hasErrors, userId);
      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Error importing AARM", e);

      try {
        connection.rollback();

        // add error to staging XML
        String xml = ImportLogFormatter.formatUploadException(e);
        if(vdao != null) {
          vdao.uploadFailed(importVersionId, xml, userId);
        }
        connection.commit();
      } catch (Exception e2) {
        logger.error("Error updating log XML for AARM import", e2);
      }
    } finally {
      if(aarmDao != null) {
        aarmDao.close();
      }
    }
  }


  /**
   * @param  connection       connection
   * @param  importVersionId  Integer
   * @param  userId           userId
   */
  @Override
  public void processImport(
  	final Connection connection,
    final Integer importVersionId, 
    final String userId) {
    VersionDAO vdao = null;
    AarmDAO aarmDao = null;

    try {
      vdao = new VersionDAO(connection);
      aarmDao = new AarmDAO(connection);

      // begin
      connection.setAutoCommit(false);
      vdao.startImport(importVersionId, userId);
      connection.commit();

      aarmDao.performImport(importVersionId, userId);
      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Error importing AARM", e);

      try {
        connection.rollback();
        
        String xml = ImportLogFormatter.formatImportException(e);
        if(vdao != null) {
          vdao.importFailed(importVersionId, xml, userId);
        }
        connection.commit(); // saves the failure state
      } catch (Exception e1) {
        logger.error("Error updating log XML for AARM import", e1);
      }
    }
  }


  /**
   * @param  userId      userId
   * @param  dao         dao
   * @param  fileHandle  fileHandle
   * @return number of rows processed
   */
  private int uploadToStaging(
  	final String userId, 
  	final AarmDAO dao,
    final FileHandle fileHandle) {
  	// NO HEADER ROW
  	final int startRowNum = 1;
    int rowNum = startRowNum;
    
    while (fileHandle.hasNext()) {
      try {
        AarmCsvRow row = (AarmCsvRow) fileHandle.next();
        dao.insert(row, userId, rowNum);
      } catch (Exception ex) {
        ex.printStackTrace();
        stagingErrors.add(new CSVParserException(rowNum, 0, ex));
      }
      rowNum++;
    }
    
    return rowNum-startRowNum;
  }
}
