/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.service.ReportService.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.CSVWriter;
import ca.bc.gov.srm.farm.dao.ExportDAO;
import ca.bc.gov.srm.farm.service.ExportService;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * DataPersistenceService Oracle Implementation.
 * 
 * @author dzwiers
 */
public final class ExportServiceFactory implements ExportService {

  private static Logger logger = LoggerFactory.getLogger(ExportServiceFactory.class);

  private ExportDAO exportDao = null;
  
  private static boolean clean = true;
  
  /**
   * @param pConn
   *          Active Connection.
   */
  private ExportServiceFactory(final Connection pConn) {
    exportDao = new ExportDAO(pConn);
  }

  /**
   * @param conn
   *          Active Oracle connection
   * 
   * @return DataPersistenceService Oracle implementation
   */
  public static ExportService getInstance(final Connection conn) {
    return new ExportServiceFactory(conn);
  }

  private static final String CSV_SUFFIX = ".csv";
  private static final String ZIP_SUFFIX = ".zip";
  
  private static final String FILE_PREFIX = "file_";

  private static final String TEMP_ZIP_FILE_PREFIX = "farm_zip_export_";
  private static final String TEMP_DETAILED_SCENARIO_EXTRACT_FILE_PREFIX = "farm_detailed_scenario_extract_";

  public static final String ZIP_FILE_NAME_AAFM = "farm_aafm_export.zip";
  public static final String ZIP_FILE_NAME_AAFMA = "farm_analytical_data_extract.zip";
  public static final String ZIP_FILE_NAME_STATEMENT_A_FORMAT = "FROM_BC_%d_%s.zip";
  
  public static final String TODAY_DATE_STRING = "yyyyMMdd";
  
  public static final String STATEMENT_A_EXTRACT_SENT_TO = "AAFC";
  
  /**
   * Exports records for the specified year
   * @param pProgramYear Year to filter the exports on
   * @param td can be used to specify the location for the temporary file
   * 
   * @return File handle to a local temporary file
   * @throws IOException IOException 
   * @throws SQLException SQLException
   * @see ca.bc.gov.srm.farm.service.ExportService#exportNumberedFilesZip(java.lang.Integer, java.lang.String)
   */
  @Override
  public File exportNumberedFilesZip(
      Integer pProgramYear,
      String exportType,
      String userAccountName) throws IOException, SQLException {
    
    logger.debug("Start Export");
    File pTempDir = IOUtils.getTempDir();
    
    String exportDate = getTodayDateString();

    File tempZip = File.createTempFile(TEMP_ZIP_FILE_PREFIX, ZIP_SUFFIX, pTempDir);
    try(FileOutputStream fos = new FileOutputStream(tempZip);
        ZipOutputStream zos = new ZipOutputStream(fos)) {

      List<String[]> file99Records = new ArrayList<>();
      if(!exportType.equals(REPORT_STA)) {
        file99Records.add(new String[]{"PROGRAM_YEAR", "FILE_NUM", "ROW_CNT"});
      }

        exportDao.addZipEntry(zos,FILE_01, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        exportDao.addZipEntry(zos,FILE_02, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        exportDao.addZipEntry(zos,FILE_03, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        exportDao.addZipEntry(zos,FILE_04, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        exportDao.addZipEntry(zos,FILE_05, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        
        if(StringUtils.isOneOf(exportType, REPORT_STA)) {
          exportDao.addZipEntry(zos,FILE_07, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_08, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_09, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        }
        
        if(StringUtils.isOneOf(exportType, REPORT_AAFM, REPORT_AAFMA)) {
          exportDao.addZipEntry(zos,FILE_20, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_21, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_25, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_26, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_30, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_31, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_40, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
          exportDao.addZipEntry(zos,FILE_60, pProgramYear, exportType, file99Records, FILE_PREFIX, CSV_SUFFIX, userAccountName, exportDate);
        }
        
        addFile99Entry(zos, pProgramYear, file99Records, exportType, userAccountName, exportDate);
    }
    File finalZip;
    if (REPORT_AAFMA.equals(exportType)) {
      finalZip = new File(pTempDir, ZIP_FILE_NAME_AAFMA);
    } else if (REPORT_STA.equals(exportType)) {
      finalZip = new File(pTempDir, getStatementAFileName(pProgramYear));
    } else {
      finalZip = new File(pTempDir, ZIP_FILE_NAME_AAFM);
    }
    
    if(finalZip.exists()) {
      finalZip.delete();
    }
    tempZip.renameTo(finalZip);

    if(clean){
      finalZip.deleteOnExit();
    }
    
    logger.debug("Finish Export");
    return finalZip;
  }
  
  /**
   * @param zos ZipOutputStream
   * @param pProgramYear pProgramYear
   * @param file99Records List<String[]>
   * @param exportType exportType 
   * @param user user
   * @param exportDate exportDate
   * @throws IOException IOException
   */
  private void addFile99Entry(
      ZipOutputStream zos,
      Integer pProgramYear,
      List<String[]> file99Records,
      String exportType,
      String user,
      String exportDate)
  throws IOException {
    
    int recordCount = file99Records.size();
    
    if(exportType.equals(REPORT_STA)) {
      file99Records.add(new String[]{
          exportDate,
          FILE_99,
          Integer.toString(recordCount),
          user,
          STATEMENT_A_EXTRACT_SENT_TO});
    } else {
      file99Records.add(new String[]{
          pProgramYear.toString(),
          FILE_99,
          Integer.toString(recordCount)});
    }
   
    OutputStreamWriter osw = new OutputStreamWriter(zos);
    CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);

    String fileName = FILE_PREFIX + FILE_99 + CSV_SUFFIX;
    ZipEntry entry = new ZipEntry(fileName);
    zos.putNextEntry(entry);
    
    writer.writeAll(file99Records);

    writer.flush();
    osw.flush();
    
    zos.closeEntry();
  }

  /**
   * Gets clean
   *
   * @return the clean
   */
  public static boolean isClean() {
    return clean;
  }

  /**
   * Sets clean
   *
   * @param pClean the clean to set
   */
  public static void setClean(boolean pClean) {
    clean = pClean;
  }
  
  /**
   * @param pProgramYear Year to filter the exports on
   * @param td can be used to specify the location for the temporary file
   * @return File handle to a local temporary file
   * @throws IOException IOException 
   * @throws SQLException SQLException
   * @see ca.bc.gov.srm.farm.service.ExportService#exportDetailedScenarioExtract(java.lang.Integer, java.io.File)
   */
  @Override
  public File exportDetailedScenarioExtract( Integer pProgramYear, File td) throws IOException, SQLException {
    
    logger.debug("Start Export");
    File pTempDir = td;
    if(pTempDir == null){
      pTempDir = IOUtils.getTempDir(); 
    }

    File tempOutputFile = File.createTempFile(TEMP_DETAILED_SCENARIO_EXTRACT_FILE_PREFIX, CSV_SUFFIX, pTempDir);
    try(FileOutputStream fos = new FileOutputStream(tempOutputFile)) {
      exportDao.detailedScenarioExtract(fos, pProgramYear);
    }
    
    logger.debug("Finish Export");
    return tempOutputFile;
  }

  private String getStatementAFileName(Integer programYear) {
    String dateString = getTodayDateString();
    
    return String.format(ZIP_FILE_NAME_STATEMENT_A_FORMAT, programYear, dateString);
  }

  private String getTodayDateString() {
    SimpleDateFormat df = new SimpleDateFormat(TODAY_DATE_STRING);
    String dateString = df.format(new Date());
    return dateString;
  }
}
