package ca.bc.gov.srm.farm.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.dao.BlobReaderWriter;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.TipBenchmarkExtractDAO;
import ca.bc.gov.srm.farm.dao.TipReportDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.tips.TipBenchmarkGroup;
import ca.bc.gov.srm.farm.domain.tips.TipFarmingOperation;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.jasper.JasperConnection;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.util.IOUtils;

public class TipReportServiceImpl extends BaseService implements TipReportService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String CSV_SUFFIX = ".csv";
  private static final String PDF_SUFFIX = ".pdf";
  private static final String ZIP_SUFFIX = ".zip";
  
  private static final String TEMP_ZIP_FILE_NAME_PREFIX = "tipReports_temp_";
  private static final String FINAL_ZIP_FILE_NAME_PREFIX = "tipReports";
  private static final String MERGE_FILE_NAME_PREFIX = "tipReports_";
  private static final String TIP_REPORT_FILE_NAME_PREFIX = "tipReport_";
  private static long MEMORY_50MB = 50 * 1024 * 1024L;
  private static long JASPER_TIME_UNTIL_NEXT_LOGIN = 9 * 60 * 1000;
  private static int REPORTS_PER_PDF_MERGE = 50;
  
  private static final int ERROR_THRESHOLD = 5;
  private static final int UPDATE_SERVICE_MESSAGE_LIMIT = 10;

  private static final String FINAL_EXPORT_ZIP_FILE_NAME_PREFIX = "tipBenchmarkExtract";
  
  /**
   * Write the RIP Report BLOB into the HTTP response
   * @param tipReportDocId tipReportDocId
   * @param response       HttpServletResponse
   * @throws Exception on error
   */
  @Override
  public void writeTipReportToResponse(Integer tipReportDocId, HttpServletResponse response)
      throws Exception {
    Transaction transaction = null;
    Blob blob = null;

    try {
      transaction = openTransaction();

      TipReportDAO dao = new TipReportDAO();
      @SuppressWarnings("resource")
      Connection dbConnection = (Connection) transaction.getDatastore();
      blob = dao.getTipReportBlob(dbConnection, tipReportDocId, null, false);

      response.reset();
      response.addHeader("content-disposition", "inline;filename=TIP_Report.pdf");
      response.setContentType(IOUtils.CONTENT_TYPE_PDF);
      response.setContentLength((int) blob.length());
      
      @SuppressWarnings("resource")
      OutputStream outputStream = response.getOutputStream();

      BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
      blobReaderWriter.readBlob(blob, outputStream);
    } finally {
      closeTransaction(transaction);
    }
  }

  @Override
  public void scheduleTipReportGeneration(String farmingOperationIdsString, Integer year, String reportState) throws IOException, ServiceException {
    
    File pTempDir = IOUtils.getTempDir();
    File tipReportInputFile = File.createTempFile("farm_tip_report_", ".csv", pTempDir);

    try (FileOutputStream fos = new FileOutputStream(tipReportInputFile);
         PrintWriter pw = new PrintWriter(fos);) {

      pw.println(farmingOperationIdsString);
      pw.flush();
      pw.close();
    }
    
    String[] farmOpIds = farmingOperationIdsString.split(",");
    
    ImportService service = ServiceFactory.getImportService();
    
    // create a farm_import_versions entry, and save the file to a blob
    try (FileInputStream importFileInputStream = new FileInputStream(tipReportInputFile);) {
      ImportVersion importVersion = service.createImportVersion(
          ImportClassCodes.TIP_REPORT,
          ImportStateCodes.SCHEDULED_FOR_STAGING,
          year + " - " + reportState + " - " + farmOpIds.length + " operations",
          tipReportInputFile.getPath(),
          importFileInputStream,
          CurrentUser.getUser().getUserId());
      
      String key = CacheKeys.CURRENT_IMPORT;
      CacheFactory.getUserCache().addItem(key, importVersion);
    }

  }

  /**
   * @param connection      connection
   * @param importVersionId importVersionId
   * @param transferFile    transferFile
   * @param user            String
   * @throws ServiceException
   */
  @Override
  public void processScheduledReportGeneration(final Connection connection, final File csvFile, final Integer importVersionId,
      final String userId) throws ServiceException {

    VersionDAO vdao = null;
    StagingDAO sdao = null;
    
    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      connection.setAutoCommit(false);

      vdao.startImport(importVersionId, userId);
      connection.commit();
      sdao.status(importVersionId, "Started");

      String line = "";
      String delimiter = ",";
      
      try (BufferedReader br = new BufferedReader(new FileReader(csvFile));) {
        while ((line = br.readLine()) != null) {

          // use comma as separator
          String[] opIds = line.split(delimiter);
          generateReportsForOpIds(opIds, userId, connection, importVersionId);
          sdao.status(importVersionId, "Generation of Tip Reports is complete.");
        }
        markGenerationComplete(connection, importVersionId, userId, vdao);
      } catch (Exception e) {
        connection.rollback();
        throw e;
      }

    } catch (Throwable t) {
      logger.error("Unexpected error: ", t);
      String xml = ImportLogFormatter.formatImportException(t);
      try {
        if (vdao != null) {
          vdao.importFailed(importVersionId, xml, userId);
          connection.commit();
        }
        if (sdao != null) {
          sdao.status(importVersionId, "Failed to generate the Tip Reports.");
        }
      } catch (SQLException | IOException ex) {
        throw new ServiceException(ex);
      }
    }
  }
  
  /**
   * Get the tipReportDocumentId for the 
   */
  @Override
  public Integer getTipReportDocumentId(Integer farmingOperationId)
      throws Exception {
    Transaction transaction = null;
    Integer tipReportDocumentId = null;

    try {
      transaction = openTransaction();

      TipReportDAO dao = new TipReportDAO();
      @SuppressWarnings("resource")
      Connection dbConnection = (Connection) transaction.getDatastore();

      tipReportDocumentId = dao.getTipReportDocumentId(dbConnection, farmingOperationId);
      
    } finally {
      closeTransaction(transaction);
    }
    
    return tipReportDocumentId;
  }

  @Override
  public void generateReports(String farmingOperationIdsString, Integer importVersionId, String userId) throws ServiceException {
    
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();

      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      generateReports(farmingOperationIdsString, importVersionId, userId, connection);
      
    } catch(Exception e) {
      logger.error("Exception generating TIP Reports: ", e);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
  }

  @Override
  public void generateReports(String farmingOperationIdsString, Integer importVersionId, String userId, Connection connection) throws Exception {
    
    String[] operationIds = farmingOperationIdsString.split(",");
    generateReportsForOpIds(operationIds, userId, connection, importVersionId);
  }

  @Override
  public void generateBenchmarkData(Integer year, String user) throws ServiceException {
    TipReportDAO dao = new TipReportDAO();
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.generateBenchmarks(transaction, year, user);
      
      transaction.commit();
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      if(transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
      throw new ServiceException(e);
    }
  }
  
  @Override
  public Boolean checkBenchmarkDataGenerated(Integer year) throws ServiceException {
    
    TipReportDAO dao = new TipReportDAO();
    Boolean isGenerated = false;
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      isGenerated = dao.checkBenchmarkDataGenerated(transaction, year);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      e.printStackTrace();
      throw new ServiceException(e);
    }
    return isGenerated;
  }
  
 
  @Override
  public List<TipFarmingOperation> getTipFarmingOperations(Integer year)  throws ServiceException {
    Transaction transaction = null;
    TipReportDAO dao = new TipReportDAO();
    
    List<TipFarmingOperation> tipFarmOps = new ArrayList<>();
    
    try {
      transaction = openTransaction();
      tipFarmOps = dao.getTipFarmingOperations(transaction, year);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return tipFarmOps;
  }

  private void generateReportsForOpIds(
      String[] opIds, 
      String userId, 
      Connection connection, 
      Integer importVersionId) throws Exception {
    LoggingUtils.logMethodStart(logger);
    
    Path tempZipPath = null;
    if(opIds.length > 1) {
      String tempZipFileName = TEMP_ZIP_FILE_NAME_PREFIX + UUID.randomUUID() + ZIP_SUFFIX;
      tempZipPath = Paths.get(IOUtils.getTempDirPath().toString(), tempZipFileName);
    }
    
    List<Path> reportFiles = new ArrayList<>();
    final int numOps = opIds.length;
    int zipEntryNumber = 1;
    
    // may be a report for one operation or multiple
    int opCount = 0;
    int errorCount = 0;
    long totalJasperExecutionTime = 0;
    long totalTimeForAllOps = 0;
    String errorMessage = "";
    
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    
    String restContextUrl = config.getValue(ConfigurationKeys.REPORTS_JASPER_URL);
    String jasperUsername = config.getValue(ConfigurationKeys.REPORTS_JASPER_USERNAME);
    String jasperUserPassword = config.getValue(ConfigurationKeys.REPORTS_JASPER_PASSWORD);
    String jasperReportsPath = config.getValue(ConfigurationKeys.REPORTS_JASPER_REPORTS_PATH);
    final String reportFormat = "pdf";
    
    JasperConnection jasperConnection = new JasperConnection(restContextUrl, jasperUsername, jasperUserPassword);
    
    StagingDAO sdao = new StagingDAO(connection);
    
    jasperConnection.jasperRestLogin();
    long timeOfJasperLogin = new Date().getTime();
    
    for (String opId : opIds) {
      opCount++;
      
      long timeAtOpStart = new Date().getTime();
      
      try {
        final String reportName = "TipReport";
        final String reportPath = jasperReportsPath + reportName;

        String fileNamePrefix = TIP_REPORT_FILE_NAME_PREFIX + opId + "_";
        Path reportFilePath = Files.createTempFile(fileNamePrefix, PDF_SUFFIX);
        String arguments = "FARMING_OPERATION_ID=" + opId;
        
        long timeSinceJasperLogin = new Date().getTime() - timeOfJasperLogin;
        if(timeSinceJasperLogin > JASPER_TIME_UNTIL_NEXT_LOGIN) {
          jasperConnection.jasperRestLogout();
          jasperConnection.jasperRestLogin();
          timeOfJasperLogin = new Date().getTime();
        }
        
        long timeBeforeJasperExecution = new Date().getTime();
        jasperConnection.fetchReport(reportPath, reportFormat, arguments, reportFilePath);
        long timeAfterJasperExecution = new Date().getTime();
        long jasperExecutionTime = timeAfterJasperExecution - timeBeforeJasperExecution;
        totalJasperExecutionTime += jasperExecutionTime;
        long averageJasperExecutionTime = totalJasperExecutionTime / opCount;
        logger.debug("Jasper Report execution time: " + jasperExecutionTime);
        logger.debug("Average execution time: " + averageJasperExecutionTime);

        reportFiles.add(reportFilePath);
        saveTipReport(connection, reportFilePath, userId, new Integer(opId));
        
        long timeAtOpEnd = new Date().getTime();
        long opTime = timeAtOpEnd - timeAtOpStart;
        totalTimeForAllOps += opTime;
        long averageOpTime = totalTimeForAllOps / opCount;
        logger.debug("Average time per operation: " + averageOpTime);
        
        if (importVersionId != null && opCount % UPDATE_SERVICE_MESSAGE_LIMIT == 0) {
          sdao.status(importVersionId, opCount + " reports generated. Average time per operation: " + averageOpTime + " ms.");
        }
        
        if(tempZipPath != null && (opCount % REPORTS_PER_PDF_MERGE == 0 || opCount == numOps)) {
          
          String zipEntryFileName = MERGE_FILE_NAME_PREFIX + String.format("%03d", zipEntryNumber) + PDF_SUFFIX;
          logger.debug("Creating merged PDF: " + zipEntryFileName);
          
          Path mergedReportsFile = mergePdfFiles(reportFiles);
          IOUtils.addFileToZip(tempZipPath, mergedReportsFile, zipEntryFileName);
          Files.delete(mergedReportsFile);
          zipEntryNumber++;
        }
        
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        
        errorMessage = errorMessage + "Farming Operation ID " + opId + ": " + e.getMessage() + "\n";
        errorCount++;
        if (errorCount == ERROR_THRESHOLD) {
          throw new ServiceException(errorMessage);
        }
      }
    }
    
    if(tempZipPath != null) {
      Path finalZip = getTipReportZipFile();
      Files.move(tempZipPath, finalZip, StandardCopyOption.REPLACE_EXISTING);
    }
    
    jasperConnection.jasperRestLogout();
    if (errorCount != 0) {
      throw new ServiceException(errorMessage);
    }
    
    LoggingUtils.logMethodEnd(logger);
  }

  private Path mergePdfFiles(List<Path> reportFiles) throws IOException {
    LoggingUtils.logMethodStart(logger);
    
    PDFMergerUtility pdfMerger = new PDFMergerUtility();
    Path mergedReportsFile = Files.createTempFile(MERGE_FILE_NAME_PREFIX, PDF_SUFFIX);
    
    for (Path curReportFile : reportFiles) {
      pdfMerger.addSource(curReportFile.toFile());
    }
    
    pdfMerger.setDestinationFileName(mergedReportsFile.toString());
    
    MemoryUsageSetting memUsageSetting = MemoryUsageSetting.setupMixed(MEMORY_50MB, -1);
    pdfMerger.mergeDocuments(memUsageSetting);
    
    for (Path curReportFile : reportFiles) {
      Files.delete(curReportFile);
    }
    
    reportFiles.clear();
    
    LoggingUtils.logMethodEnd(logger);
    return mergedReportsFile;
  }

  @Override
  public Path getTipReportZipFile() {
    String zipFileName = FINAL_ZIP_FILE_NAME_PREFIX + ZIP_SUFFIX;
    Path mergedReportsFile =  Paths.get(IOUtils.getTempDirPath().toString(), zipFileName);
    return mergedReportsFile;
  }

  @Override
  @SuppressWarnings("resource")
  public Path downloadReports(String farmingOperationIdsString) throws Exception {
    LoggingUtils.logMethodStart(logger);
    
    Transaction transaction = null;
    Path zipPath = null;
    String[] operationIds = farmingOperationIdsString.split(",");
    
    try {
      transaction = openTransaction();
      Connection connection = (Connection) transaction.getDatastore();
      
      String tempZipFileName = TEMP_ZIP_FILE_NAME_PREFIX + UUID.randomUUID() + ZIP_SUFFIX;
      zipPath = Paths.get(IOUtils.getTempDirPath().toString(), tempZipFileName);

      List<Path> reportFilePaths = new ArrayList<>();
      final int numOps = operationIds.length;
      int zipEntryNumber = 1;
      
      // may be a report for one operation or multiple
      int opCount = 0;
      
      for (String opId : operationIds) {
        opCount++;
        
        try {
          Integer farmingOperationId = Integer.valueOf(opId);
          String fileNamePrefix = TIP_REPORT_FILE_NAME_PREFIX + opId + "_";
          Path reportFilePath = Files.createTempFile(fileNamePrefix, PDF_SUFFIX);
          reportFilePaths.add(reportFilePath);
          
          try(OutputStream outputStream = Files.newOutputStream(reportFilePath, StandardOpenOption.TRUNCATE_EXISTING); ) {
          
            TipReportDAO dao = new TipReportDAO();
            Blob blob = dao.getTipReportBlob(connection, null, farmingOperationId, false);
            BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
            blobReaderWriter.readBlob(blob, outputStream);
          }
          
          if(zipPath != null && (opCount % REPORTS_PER_PDF_MERGE == 0 || opCount == numOps)) {
            
            String zipEntryFileName = MERGE_FILE_NAME_PREFIX + String.format("%03d", zipEntryNumber) + PDF_SUFFIX;
            logger.debug("Creating merged PDF: " + zipEntryFileName);
            
            Path mergedReportsFile = mergePdfFiles(reportFilePaths);
            IOUtils.addFileToZip(zipPath, mergedReportsFile, zipEntryFileName);
            Files.delete(mergedReportsFile);
            zipEntryNumber++;
          }
          
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
          
          throw new ServiceException("Farming Operation ID " + opId + ": " + e.getMessage() + "\n");
        }
      }
      
    } catch(Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    LoggingUtils.logMethodEnd(logger);
    return zipPath;
  }
  
  
  private void markGenerationComplete(
      final Connection connection,
      final Integer importVersionId,
      final String user,
      VersionDAO vdao) throws Exception {
    Boolean hasErrors = Boolean.FALSE;
    vdao.uploadedVersion(importVersionId, "", hasErrors, user);
    String importXml = ImportLogFormatter.createEmptyImportXml();
    vdao.importCompleted(importVersionId, importXml, user);
    connection.commit();
  }
  
  /**
   * Save the TIP report in a Blob in a table.
   * @param userId2 
   *
   * @param isInsert isInsert
   * @throws Exception if it could not be saved
   */
  private void saveTipReport(
      Connection connection, 
      Path reportFile, 
      String userId, 
      Integer farmingOperationId) 
          throws Exception {

    try {

      // Insert an entry into the Blob table.
      TipReportDAO dao = new TipReportDAO();
      connection.setAutoCommit(false);
      
      Integer id = dao.upsertTipReport(connection, userId, farmingOperationId);

      // put the response into a Blob
      BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
      Blob blob = dao.getTipReportBlob(connection, id, null, true);
      try (InputStream inStream = Files.newInputStream(reportFile, StandardOpenOption.READ)) {
        blobReaderWriter.writeBlob(blob, inStream);
      }

      connection.commit();
    } catch (Exception e) {
      logger.error("Error in generateTipReport", e);
      String messageErrorRecordingError = "Error recording error in import table";
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        logger.error(messageErrorRecordingError, e);
      }
      throw new ServiceException(e);
    }
  }
  
  
  @Override
  public boolean benchmarksMatchConfig(Integer year)  throws ServiceException {
    Transaction transaction = null;
    TipReportDAO dao = new TipReportDAO();
    
    boolean result;
    
    try {
      transaction = openTransaction();
      result = dao.benchmarksMatchConfig(transaction, year);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  @Override
  public void updateTipParticipantFlag(
      Collection<Integer> participantPins,
      Boolean isTipParticipant,
      String userId) throws ServiceException {
    Transaction transaction = null;
    TipReportDAO dao = new TipReportDAO();
    
    try {
      transaction = openTransaction();
      Integer[] pinArray = participantPins.toArray(new Integer[participantPins.size()]);
      dao.updateTipParticipantFlag(transaction, pinArray, isTipParticipant, userId);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> getBenchmarkGroups() throws ServiceException {
    Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> benchmarkGroupsByYear = null;
    
    Transaction transaction = null;
    TipReportDAO dao = new TipReportDAO();

    try {
      transaction = openTransaction();
      benchmarkGroupsByYear = dao.readBenchmarkGroups(transaction);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return benchmarkGroupsByYear;
  }

  @Override
  public String getBenchmarkSummaryReport(
      Integer programYearParam,
      String farmType3NameParam,
      String farmType2NameParam,
      String farmType1NameParam,
      Double incomeRangeLowParam) throws ServiceException {
    String result = null;
    
    Transaction transaction = null;
    TipReportDAO dao = new TipReportDAO();
    
    try {
      transaction = openTransaction();
      result = dao.readBenchmarkSummaryReport(transaction,
          programYearParam,
          farmType3NameParam,
          farmType2NameParam,
          farmType1NameParam,
          incomeRangeLowParam);
      
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }


  @Override
  public void generateBenchmarkExtract(
      Integer programYear,
      String farmType3Name,
      String farmType2Name,
      String farmType1Name,
      Double incomeRangeLow,
      Double incomeRangeHigh) throws ServiceException {
    LoggingUtils.logMethodStart(logger);
    
    Transaction transaction = null;
    TipBenchmarkExtractDAO dao = new TipBenchmarkExtractDAO();
    
    try {
      transaction = openTransaction();

      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      
      Path finalZip = getBenchmarkExtractZipFile();
      if(Files.exists(finalZip)) {
        Files.delete(finalZip);
      }
      
      String tempZipFileName = TEMP_ZIP_FILE_NAME_PREFIX + UUID.randomUUID() + ZIP_SUFFIX;
      Path tempZipPath = Paths.get(IOUtils.getTempDirPath().toString(), tempZipFileName);
      
      String[] extractNames = {
          TipReportService.BENCHMARK_YEARS,
          TipReportService.BENCHMARK_EXPENSES,
          TipReportService.INDIVIDUAL_DATA,
          TipReportService.INDIVIDUAL_EXPENSES
      };
      
      for(String extractName : extractNames) {
      
        logger.debug("Starting extract: " + extractName);
        String zipEntryFileName = extractName + CSV_SUFFIX;
        Path extractPath = dao.exportDataToFile(connection,
            extractName,
            programYear,
            farmType3Name,
            farmType2Name,
            farmType1Name,
            incomeRangeLow,
            incomeRangeHigh);
        
        IOUtils.addFileToZip(tempZipPath, extractPath, zipEntryFileName);
        Files.delete(extractPath);
      }
      
      Files.move(tempZipPath, finalZip, StandardCopyOption.REPLACE_EXISTING);
      
    } catch(Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    LoggingUtils.logMethodEnd(logger);
  }

  @Override
  public Path getBenchmarkExtractZipFile() {
    String zipFileName = FINAL_EXPORT_ZIP_FILE_NAME_PREFIX + ZIP_SUFFIX;
    Path mergedReportsFile =  Paths.get(IOUtils.getTempDirPath().toString(), zipFileName);
    return mergedReportsFile;
  }


  @SuppressWarnings("resource")
  @Override
  public Path generateGroupingConfigReport() throws ServiceException {
    LoggingUtils.logMethodStart(logger);
    
    Transaction transaction = null;
    TipReportDAO dao = new TipReportDAO();
    
    try {
      transaction = openTransaction();

      Connection connection = (Connection) transaction.getDatastore();
      
      Path reportFilePath = dao.readGroupingConfigReport(connection);
      
      LoggingUtils.logMethodEnd(logger);
      return reportFilePath;
      
    } catch(Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
  }

}
