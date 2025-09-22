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

import ca.bc.gov.srm.farm.csv.CSVParserException;
import ca.bc.gov.srm.farm.csv.FileHandle;
import ca.bc.gov.srm.farm.csv.FmvFileHandle;
import ca.bc.gov.srm.farm.dao.FmvDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.staging.FmvCsvRow;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.FmvService;

/**
 * FmvServiceImpl.
 */
final class FmvServiceImpl extends BaseService implements FmvService {

	private ArrayList stagingErrors = new ArrayList<>();

	private Logger logger = LoggerFactory.getLogger(FmvServiceImpl.class);

	/**
	 * Mainly used for unit testing.
	 * 
	 * @return a list of CSVParserException and Strings
	 */
	@Override
  public List<CSVParserException> getStagingErrors() {
		return stagingErrors;
	}

	/**
	 * Imports the CSV into the staging area.
	 * 
	 * @param  connection  connection
	 * @param importVersionId
	 *          Integer
	 * @param csvFile
	 *          File
	 * @param userId
	 *          DOCUMENT ME!
	 */
	@Override
  public void importCSV(
			final Connection connection,
			final Integer importVersionId, 
			final File csvFile, 
			final String userId) {

		VersionDAO vdao = null;
		FmvDAO fmvDao = null;

		try {
			vdao = new VersionDAO(connection);
			fmvDao = new FmvDAO(connection);

			// begin
			stagingErrors.clear();
			connection.setAutoCommit(false);
			
			fmvDao.clearStaging();
			connection.commit();
			
			vdao.startUpload(importVersionId, userId);
			connection.commit();

			// validate headers
			FileHandle fileHandle = FmvFileHandle.read(csvFile);
			String[] fileValidationErrors = fileHandle.validate();
			int numRows = 0;

			if ((fileValidationErrors == null) || (fileValidationErrors.length == 0)) {
				// upload the file
				numRows = uploadToStaging(userId, fmvDao, fileHandle);

				// validate what is in the staging table
				fmvDao.validateStaging(importVersionId);
				List<String> stagingValidationErrors = fmvDao.getStagingErrors(importVersionId);
				fmvDao.deleteStagingErrors(importVersionId);
				
				stagingErrors.addAll(stagingValidationErrors);
			} else {
				// convert the validation errors
				for (int ii = 0; ii < fileValidationErrors.length; ii++) {
					String msg = fileValidationErrors[ii];
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

				// add error to staging XML
				String xml = ImportLogFormatter.formatUploadException(e);
				vdao.uploadFailed(importVersionId, xml, userId);
				connection.commit();
			} catch (Exception e2) {
				logger.error("Error updating log XML for FMV import", e2);
			}
		} finally {
			fmvDao.close();
		}
	}

	

	
	/**
	 * 
	 * @param connection  Connection
	 * @param importVersionId Integer
	 * @param userId  String
	 */
	@Override
  public void processImport(
			final Connection connection,
			final Integer importVersionId, 
			final String userId) {
		VersionDAO vdao = null;
		FmvDAO fmvDao = null;

		try {
			vdao = new VersionDAO(connection);
			fmvDao = new FmvDAO(connection);

			connection.setAutoCommit(false);
			vdao.startImport(importVersionId, userId);
			connection.commit();

			fmvDao.performImport(importVersionId, userId);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unexpected error: ", e);

			try {
				connection.rollback();
				
				String xml = ImportLogFormatter.formatImportException(e);
				vdao.importFailed(importVersionId, xml, userId);
				connection.commit(); // saves the failure state
			} catch (Exception e1) {
				logger.error("Error updating log XML for FMV import", e1);
			}
		}
	}
	
	
	/**
	 * 
	 * @param userId String
	 * @param dao FmvDAO
	 * @param fileHandle FileHandle
	 * @return number of rows processed
	 */
	private int uploadToStaging(
			final String userId, 
			final FmvDAO dao, 
			final FileHandle fileHandle) {
    // first row is a header
  	final int startRowNum = 2;
		int rowNum = startRowNum;
		
		while (fileHandle.hasNext()) {
			try {
				FmvCsvRow row = (FmvCsvRow) fileHandle.next();
				dao.insert(row, userId, rowNum);
			} catch (Exception ex) {
				stagingErrors.add(new CSVParserException(rowNum, 0, ex));
			}
			rowNum++;
			
			if(rowNum % 1000 == 0) {
				logger.debug("rownum: " + rowNum);
			}
    }
    
    return rowNum-startRowNum;
	}
}
