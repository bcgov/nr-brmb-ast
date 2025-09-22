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

import ca.bc.gov.srm.farm.csv.BpuFileHandle;
import ca.bc.gov.srm.farm.csv.CSVParserException;
import ca.bc.gov.srm.farm.csv.FileHandle;
import ca.bc.gov.srm.farm.dao.BpuDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.staging.BpuCsvRow;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.BpuService;



/**
 * BpuServiceImpl. This class follows the same pattern as the FMV service, so it
 * could be refactored to remove duplicate code when time permits.
 */
final class BpuServiceImpl extends BaseService implements BpuService {

  private ArrayList<Object> stagingErrors = new ArrayList<>();

  private Logger logger = LoggerFactory.getLogger(BpuServiceImpl.class);

  /**
   * Mainly used for unit testing.
   *
   * @return  a list of CSVParserException
   */
  @Override
  public List<Object> getStagingErrors() {
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
    BpuDAO bpuDao = null;

    try {
      vdao = new VersionDAO(connection);
      bpuDao = new BpuDAO(connection);

      // begin
      stagingErrors.clear();
      connection.setAutoCommit(false);
      
      bpuDao.clearStaging();
      connection.commit();
      
      vdao.startUpload(importVersionId, userId);
      connection.commit();

      // validate headers
      FileHandle fileHandle = BpuFileHandle.read(csvFile);
      String[] fileErrors = fileHandle.validate();
      int numRows = 0;
      
      if ((fileErrors == null)|| (fileErrors.length == 0)) {

        // upload the file
      	numRows = uploadToStaging(userId, bpuDao, fileHandle);

        // validate what is in the staging table
        bpuDao.validateStaging(importVersionId);

        List<String> valErrors = bpuDao.getStagingErrors(importVersionId);
        bpuDao.deleteStagingErrors(importVersionId);

        stagingErrors.addAll(valErrors);
      } else {

        // convert the validation errors
        for (int ii = 0; ii < fileErrors.length; ii++) {
          String msg = fileErrors[ii];
          CSVParserException pe = new CSVParserException(1, 0, msg);
          stagingErrors.add(pe);
        }
      }

      // end
			String xml = ImportLogFormatter.createStagingXml(numRows, stagingErrors);
      Boolean hasErrors = new Boolean(stagingErrors.size() != 0);
      vdao.uploadedVersion(importVersionId, xml, hasErrors, userId);
      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);

      try {
        connection.rollback();

        if(vdao != null) {
          // add error to staging XML
          String xml = ImportLogFormatter.formatUploadException(e);
          vdao.uploadFailed(importVersionId, xml, userId);
          connection.commit();
        }
      } catch (Exception e2) {
        logger.error("Unexpected error: ", e2);
      }
    } finally {
      if(bpuDao != null) {
        bpuDao.close();
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
    BpuDAO bpuDao = null;

    try {
      vdao = new VersionDAO(connection);
      bpuDao = new BpuDAO(connection);

      // begin
      connection.setAutoCommit(false);
      vdao.startImport(importVersionId, userId);
      connection.commit();

      bpuDao.performImport(importVersionId, userId);
      connection.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);

      try {
        connection.rollback();
        
        if(vdao != null) {
          String xml = ImportLogFormatter.formatImportException(e);
          vdao.importFailed(importVersionId, xml, userId);
          connection.commit(); // saves the failure state
        }
      } catch (Exception e1) {
        logger.error("Error updating log XML for BPU import", e1);
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
  	final BpuDAO dao,
    final FileHandle fileHandle) {
  	// first row is a header
  	final int startRowNum = 2;
    int rowNum = startRowNum;
    
    while (fileHandle.hasNext()) {
      try {
        BpuCsvRow row = (BpuCsvRow) fileHandle.next();
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
