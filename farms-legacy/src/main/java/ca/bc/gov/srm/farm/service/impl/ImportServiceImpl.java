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
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.dao.BlobReaderWriter;
import ca.bc.gov.srm.farm.dao.ImportDAO;
import ca.bc.gov.srm.farm.dao.ImportXmlDAO;
import ca.bc.gov.srm.farm.dao.SearchDAO;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageResults;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TransactionException;
import ca.bc.gov.srm.farm.io.FileUtility;
import ca.bc.gov.srm.farm.service.AarmService;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.BpuService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.FmvService;
import ca.bc.gov.srm.farm.service.ImportController;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.IvprService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.dataimport.FileLineMessage;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportResults;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.domain.dataimport.LogConverter;
import ca.bc.gov.srm.farm.ui.domain.dataimport.StagingResults;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.IMPORTLOG;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.STAGINGLOG;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.webade.dbpool.WrapperConnection;

/**
 * ImportServiceImpl.
 */
final class ImportServiceImpl extends BaseService implements ImportService {

  private Logger logger = LoggerFactory.getLogger(ImportServiceImpl.class);
  
  @Override
  public List<ImportSearchResult> searchImports(final List<String> importTypes) throws ServiceException {
    Date oneYearAgo = DateUtils.oneYearAgo();
    return searchImports(importTypes, oneYearAgo);
  }

  @Override
  public List<ImportSearchResult> searchImports(List<String> importTypes, Date createdAfterDate) throws ServiceException {
    List<ImportSearchResult> results = null;
    Transaction transaction = null;
    SearchDAO dao = new SearchDAO();

    try {
      transaction = openTransaction();
      results = dao.searchImports(transaction, importTypes, createdAfterDate);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    for (ImportSearchResult importSearchResult : results) {
      String stateDescription = getTipBatchJobStateDescription(
          importSearchResult.getStateCode(), importSearchResult.getStateDescription());
      importSearchResult.setStateDescription(stateDescription);
    }

    return results;
  }

  /**
   * Only one "in progress" import is allowed at one time.
   *
   * @param searchResults results from searchImports method.
   * @param importType    importType
   * @return true if the user should be allowed to start a new import.
   */
  @Override
  public boolean getAllowNewImport(final List<ImportSearchResult> searchResults, final String importType) {
    boolean allowed = false;

    if (ImportClassCodes.ENROL.equals(importType)) {
      allowed = false;
    } else if (searchResults.size() == 0) {
      allowed = true;
    } else {

      // rely on the fact that the results are sorted newest to oldest.
      ImportSearchResult isr = searchResults.get(0);
      allowed = !ImportStateCodes.isInProgress(isr.getStateCode());
    }

    return allowed;
  }

  /**
   * @param importClassCode importClassCode
   * @param description     description
   * @param fileName        fileName
   * @param inputStream     inputStream
   * @param userId          userId
   *
   * @return New Import Version ID
   *
   * @throws ServiceException on exception
   */
  @SuppressWarnings("resource")
  @Override
  public ImportVersion createImportVersion(final String importClassCode, final String importStateCode,
      final String description, final String fileName, final InputStream inputStream, final String userId)
      throws ServiceException {

    Transaction transaction = null;
    Connection connection = null;
    ImportVersion importVersion = null;

    try {
      transaction = openTransaction();
      connection = getOracleConnection(transaction);
      connection.setAutoCommit(false);

      importVersion = createImportVersion(connection, importClassCode, importStateCode, description, fileName,
          inputStream, userId);

      connection.commit();
    } catch (Exception e) {
      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException ex) {
          logger.error("Error on commit(): " + ex.getMessage());
          throw new TransactionException(ex);
        }
      }

      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return importVersion;
  }

  /**
   * @param connection      connection
   * @param importClassCode importClassCode
   * @param description     description
   * @param fileName        fileName
   * @param inputStream     inputStream
   * @param userId          userId
   *
   * @return New Import Version ID
   *
   * @throws ServiceException on exception
   */
  @Override
  public ImportVersion createImportVersion(final Connection connection, final String importClassCode,
      final String importStateCode, final String description, final String fileName, final InputStream inputStream,
      final String userId) throws ServiceException {
    ImportVersion importVersion = null;
    ImportDAO dao = new ImportDAO();

    try {

      importVersion = new ImportVersion();
      importVersion.setImportClassCode(importClassCode);
      importVersion.setImportStateCode(importStateCode);
      importVersion.setDescription(description);
      importVersion.setImportedByUser(userId);
      importVersion.setImportFileName(fileName);

      dao.insertImportVersion(connection, importVersion);

      Integer importVersionId = importVersion.getImportVersionId();
      BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
      Blob blob = dao.getBlob(connection, importVersionId, true);
      blobReaderWriter.writeBlob(blob, inputStream);

    } catch (Exception e) {
      throw new ServiceException(e);
    }

    return importVersion;
  }

  /**
   * @param importVersionId id to use
   *
   * @throws ServiceException on exception
   */
  @Override
  public void cancelImport(final Integer importVersionId) throws ServiceException {
    Transaction transaction = null;
    ImportDAO dao = new ImportDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.cancelImport(transaction, importVersionId);

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  /**
   * @param importVersionId id to use
   *
   * @throws ServiceException on exception
   */
  @Override
  public void confirmImport(final Integer importVersionId) throws ServiceException {
    Transaction transaction = null;
    ImportDAO dao = new ImportDAO();

    try {
      transaction = openTransaction();
      dao.confirmImport(transaction, importVersionId);
    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  /**
   * @param importVersionId id to use
   * @param connection      connection
   *
   * @throws ServiceException on exception
   */
  @Override
  public void confirmImport(final Integer importVersionId, final Connection connection) throws ServiceException {
    ImportDAO dao = new ImportDAO();

    try {
      dao.confirmImport(connection, importVersionId);
    } catch (Exception e) {

      if (connection != null) {
        try {
          connection.rollback();
        } catch (SQLException e1) {
          logger.error("Error rolling back transaction", e1);
        }
      }

      throw new ServiceException(e);
    }
  }

  /**
   * @param importVersionId id to use
   *
   * @throws ServiceException on exception
   */
  @Override
  public void retryStaging(final Integer importVersionId) throws ServiceException {
    Transaction transaction = null;
    ImportDAO dao = new ImportDAO();

    try {
      transaction = openTransaction();
      dao.retryStaging(transaction, importVersionId);
    } catch (Exception e) {

      if (transaction != null) {
        transaction.rollback();
      }

      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  /**
   * @param importVersionId id to use
   *
   * @return the staging details
   *
   * @throws ServiceException on exception
   */
  @Override
  public StagingResults getStagingResults(final Integer importVersionId) throws ServiceException {
    StagingResults details = new StagingResults();
    Transaction transaction = null;
    ImportDAO dao = new ImportDAO();

    try {
      transaction = openTransaction();

      ImportVersion importVersion = dao.getImportVersion(transaction, importVersionId);
      
      updateStateDescriptionForTips(importVersion);

      details.setImportVersion(importVersion);

      String stateCode = importVersion.getImportStateCode();

      //
      // Don't try to parse the XML clob if it hasn't been populated yet
      //
      if (ImportStateCodes.isStagingDone((stateCode))) {
        ImportXmlDAO xdao = new ImportXmlDAO();

        @SuppressWarnings("resource")
        Connection connection = getOracleConnection(transaction);

        logger.debug("> getStagingLog");
        STAGINGLOG log = xdao.getStagingLog(connection, importVersionId);
        logger.debug("< getStagingLog");

        if (ImportClassCodes.ENROL.equals(importVersion.getImportClassCode())) {
          EnrolmentService enrolmentService = ServiceFactory.getEnrolmentService();
          enrolmentService.setStagingResults(transaction, log, details);
        } else {
          logger.debug("> convertStagingLog");
          LogConverter converter = new LogConverter();
          converter.convertStagingLog(log, details);
          logger.debug("< convertStagingLog");
        }
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return details;
  }

  private void updateStateDescriptionForTips(ImportVersion importVersion) {
    if(importVersion.getImportClassCode().equals(ImportClassCodes.TIP_REPORT)) {
      String stateDescription = getTipBatchJobStateDescription(
          importVersion.getImportStateCode(), importVersion.getImportStateDescription());
      importVersion.setImportStateDescription(stateDescription);
    }
  }

  /**
   * The details of what happened moving the data from the staging tables to the
   * operational tables.
   *
   * @param importVersionId id to use
   *
   * @return the import details
   *
   * @throws ServiceException on exception
   */
  @Override
  public ImportResults getImportResults(final Integer importVersionId) throws ServiceException {
    logger.debug("> getImportDetails");

    ImportResults details = new ImportResults();
    Transaction transaction = null;
    ImportDAO dao = new ImportDAO();
    Integer id = importVersionId; // checkstyle line length workaround

    try {
      transaction = openTransaction();

      logger.debug("> getImportVersion");
      ImportVersion importVersion = dao.getImportVersion(transaction, id);
      details.setImportVersion(importVersion);
      logger.debug("< getImportVersion");
      
      updateStateDescriptionForTips(importVersion);

      String stateCode = importVersion.getImportStateCode();

      if (ImportStateCodes.isImportDone(stateCode)) {
        ImportXmlDAO xdao = new ImportXmlDAO();
        String classCode = importVersion.getImportClassCode();

        if (ImportClassCodes.CRA.equals(classCode) || ImportClassCodes.BCCRA.equals(classCode)) {
          //
          // the CRA XML can be really large and the XML queries
          // can be way to slow, so use JAXB to convert the XML.
          //
          logger.debug("> getImportLog");
          @SuppressWarnings("resource")
          Connection connection = getOracleConnection(transaction);

          IMPORTLOG log = xdao.getImportLog(connection, importVersionId);

          // put the log in the session for use by the 240 screen
          Cache cache = CacheFactory.getUserCache();
          cache.addItem(CacheKeys.IMPORT_LOG, log);

          logger.debug("< getImportLog");

          logger.debug("> convertImportLog");
          LogConverter converter = new LogConverter();
          converter.convertImportLog(log, details);
          logger.debug("< convertImportLog");

        } else {
          //
          // FMV, BPU, and AARM XML is really small so keep using the XML queries.
          //
          if (! ImportClassCodes.TRIAGE.equals(classCode)) {
            
            logger.debug("> getImportTopLevelErrors");
            List<FileLineMessage> errors = xdao.getImportTopLevelErrors(transaction, id);
            details.setErrors(errors);
            logger.debug("< getImportTopLevelErrors");
  
            if (errors.size() == 0) {
              logger.debug("> getImportNumbers");
              xdao.getImportNumbers(transaction, id, details);
              logger.debug("< getImportNumbers");
            }
          }
        }
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    logger.debug("< getImportDetails");

    return details;
  }
  
  /**
   * The details of Benefit Triage data from import versions.
   *
   * @param importVersionId id to use
   *
   * @return the Benefit Triage results
   *
   * @throws ServiceException on exception
   */
  @Override
  public BenefitTriageResults getTriageResults(final ImportVersion importVersion) throws ServiceException {
    logger.debug("> getTriageResult");

    BenefitTriageResults triageResults = new BenefitTriageResults();
    try {
      ObjectMapper jsonObjectMapper = new ObjectMapper();
      if (!importVersion.getAuditInfo().isEmpty()) {
        triageResults = jsonObjectMapper.readValue(importVersion.getAuditInfo(), BenefitTriageResults.class);
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    } 
    logger.debug("< getTriageResult");

    return triageResults;
  }

  /**
   * @param importVersionId id to use
   *
   * @return the ImportVersion
   *
   * @throws ServiceException on exception
   */
  @Override
  public ImportVersion getImportVersion(final Integer importVersionId) throws ServiceException {
    ImportVersion importVersion = null;
    Transaction transaction = null;
    ImportDAO dao = new ImportDAO();

    try {
      transaction = openTransaction();
      importVersion = dao.getImportVersion(transaction, importVersionId);
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    updateStateDescriptionForTips(importVersion);

    return importVersion;
  }

  /**
   * See if we have to load the staging tables, or move the staging data into the
   * operational tables.
   *
   * @param webadeConnection must be a bare SQL connection
   *
   * @throws ServiceException on exception
   */
  @Override
  public void checkForScheduledImport(final Connection webadeConnection) throws ServiceException {
    checkForScheduledJob(webadeConnection, JOB_TYPE_IMPORT);
  }
  
  @SuppressWarnings("resource")
  @Override
  public void checkForScheduledJob(final Connection webadeConnection, final String jobType) throws ServiceException {

    try {

      //
      // If we use the webade connection then we'll run into alot of problems
      // with the clobs and blobs.
      //
      Connection connection = webadeConnection;

      if (webadeConnection instanceof WrapperConnection) {
        connection = ((WrapperConnection) webadeConnection).getWrappedConnection();
      }

      ImportDAO dao = new ImportDAO();
      ImportVersion scheduledImport = dao.getScheduledJob(connection, jobType);

      if (scheduledImport != null) {
        String stateCode = scheduledImport.getImportStateCode();

        if (ImportStateCodes.isScheduledForStaging(stateCode)) {
          String windowStartKey = ConfigurationKeys.IMPORT_STAGING_START_TIME;
          String windowEndKey = ConfigurationKeys.IMPORT_STAGING_END_TIME;

          if (isInTimeWindow(windowStartKey, windowEndKey)) {
            processStaging(scheduledImport, connection);
          }
        } else {
          String windowStartKey = ConfigurationKeys.IMPORT_START_TIME;
          String windowEndKey = ConfigurationKeys.IMPORT_END_TIME;

          if (isInTimeWindow(windowStartKey, windowEndKey)) {
            processImport(scheduledImport, connection);
          }
        }

        connection.commit();
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * See if we have to load the staging tables, or move the staging data into the
   * operational tables.
   *
   * @param connection must be a bare SQL connection
   *
   * @throws ServiceException on exception
   */
  @Override
  public void checkForInProgessImports(final Connection connection) throws ServiceException {

    try {
      ImportDAO dao = new ImportDAO();
      dao.checkForInProgessImports(connection);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
  }

  /**
   * @param iv         ImportVersion
   * @param connection connection
   *
   * @throws Exception on exception
   */
  private void processImport(ImportVersion iv, Connection connection) throws Exception {

    logger.debug("> processImport(" + iv.getImportVersionId());

    //
    // This method is called from the JMX agent, so there
    // is no "current" user, so use the ID of the user that
    // uploaded the file.
    //
    String userId = iv.getImportedByUser();
    Integer ivId = iv.getImportVersionId();

    if (ImportClassCodes.CRA.equals(iv.getImportClassCode())
        || ImportClassCodes.BCCRA.equals(iv.getImportClassCode())) {
      File tempDir = FileUtility.getInstance().getTempDirectory();
      ImportController controller = ImportControllerFactory.getInstance(connection, tempDir);

      controller.processImport(ivId, userId);
    } else if (ImportClassCodes.BPU.equals(iv.getImportClassCode())) {
      BpuService service = ServiceFactory.getBpuService();

      service.processImport(connection, ivId, userId);
    } else if (ImportClassCodes.IVPR.equals(iv.getImportClassCode())) {
      IvprService service = ServiceFactory.getIvprService();

      service.processImport(connection, ivId, userId);
    } else if (ImportClassCodes.FMV.equals(iv.getImportClassCode())) {
      FmvService service = ServiceFactory.getFmvService();

      service.processImport(connection, ivId, userId);
    } else if (ImportClassCodes.AARM.equals(iv.getImportClassCode())) {
      AarmService service = ServiceFactory.getAarmService();

      service.processImport(connection, ivId, userId);
    } else if (ImportClassCodes.ENROL.equals(iv.getImportClassCode())) {
      EnrolmentService service = ServiceFactory.getEnrolmentService();

      service.finishGeneration(connection, ivId, userId);
    }
    logger.debug("< processImport(" + iv.getImportVersionId());
  }

  /**
   * @param iv         iv
   * @param connection connection
   *
   * @throws Exception Exception
   */
  private void processStaging(ImportVersion iv, Connection connection) throws Exception {
    logger.debug("> processStaging");

    String userId = iv.getImportedByUser();
    File file = saveBlobToTempFile(iv, connection);
    Integer ivId = iv.getImportVersionId();

    if (ImportClassCodes.CRA.equals(iv.getImportClassCode())
        || ImportClassCodes.BCCRA.equals(iv.getImportClassCode())) {
      FileUtility fu = FileUtility.getInstance();
      File tempDir = fu.getTempDirectory();
      ImportController controller = ImportControllerFactory.getInstance(connection, tempDir);

      controller.importCSV(ivId, file, userId);

      //
      // The zip file contents get written to the temp dir.
      // Make sure all the files get removed.
      //
      try {
        fu.cleanDirectory();
      } catch (Exception ex) {
        //
        // We'll only get an exception if the temp dir didn't exist
        //
        logger.warn(ex.getMessage());
      }

    } else if (ImportClassCodes.BPU.equals(iv.getImportClassCode())) {
      BpuService service = ServiceFactory.getBpuService();

      service.importCSV(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.IVPR.equals(iv.getImportClassCode())) {
      IvprService service = ServiceFactory.getIvprService();

      service.importCSV(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.FMV.equals(iv.getImportClassCode())) {
      FmvService service = ServiceFactory.getFmvService();

      service.importCSV(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.AARM.equals(iv.getImportClassCode())) {
      AarmService service = ServiceFactory.getAarmService();

      service.importCSV(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.ENROL.equals(iv.getImportClassCode())) {
      EnrolmentService service = ServiceFactory.getEnrolmentService();

      service.generateEnrolmentsStaging(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.XENROL.equals(iv.getImportClassCode())) {
      EnrolmentService service = ServiceFactory.getEnrolmentService();

      service.transfer(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.XSTATE.equals(iv.getImportClassCode())) {
      CrmTransferService service = ServiceFactory.getCrmTransferService();

      service.transferBenefitUpdate(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.XCONTACT.equals(iv.getImportClassCode())) {
      CrmTransferService service = ServiceFactory.getCrmTransferService();

      service.transferAccountUpdate(connection, ivId, file, userId);
      file.delete();
    } else if (ImportClassCodes.TIP_REPORT.equals(iv.getImportClassCode())) {
       TipReportService service = ServiceFactory.getTipReportService();
 
       service.processScheduledReportGeneration(connection, file, ivId, userId); 
       file.delete();
    } else if (ImportClassCodes.TRIAGE.equals(iv.getImportClassCode())) {
      BenefitTriageService service = ServiceFactory.getBenefitTriageService();

      service.processBenefitTriage(connection, ivId, userId); 
      file.delete();
    }

    logger.debug("< processStaging");
  }

  /**
   * @param iv         ImportVersion
   * @param connection connection
   *
   * @return temp file
   *
   * @throws Exception on error
   */
  private File saveBlobToTempFile(final ImportVersion iv, final Connection connection) throws Exception {
    ImportDAO dao = new ImportDAO();
    Integer importVersionId = iv.getImportVersionId();
    String fileExt = ".csv";
    FileUtility fu = FileUtility.getInstance();

    if (ImportClassCodes.CRA.equals(iv.getImportClassCode())
        || ImportClassCodes.BCCRA.equals(iv.getImportClassCode())) {
      fileExt = ".zip";
    }

    Blob blob = dao.getBlob(connection, importVersionId, false);
    File tempFile = fu.write(blob.getBinaryStream(), fileExt);

    return tempFile;
  }

  /**
   * @param startTimeKey startTimeKey
   * @param endTimeKey   endTimeKey
   *
   * @return true if we are in an operating window
   */
  private boolean isInTimeWindow(final String startTimeKey, final String endTimeKey) {
    boolean inWindow = true;
    ConfigurationUtility cu = ConfigurationUtility.getInstance();
    String startTimeStr = cu.getValue(startTimeKey);
    String endTimeStr = cu.getValue(endTimeKey);

    if ((startTimeStr != null) && (endTimeStr != null)) {
      Date now = new Date();
      Date startTime = DateUtils.setTime(now, startTimeStr);
      Date stopTime = DateUtils.setTime(now, endTimeStr);
      inWindow = DateUtils.isBetween(now, startTime, stopTime);
    }

    return inWindow;
  }

  /**
   * Convert the import state code to one appropriate for TIP Report generation
   */
  public static String getTipBatchJobStateDescription(String stateCode, String currentStateDescription) {
    String stateDescription;
    if(stateCode.equals(ImportStateCodes.SCHEDULED_FOR_STAGING)) {
      stateDescription = "Scheduled";
    } else if(stateCode.equals(ImportStateCodes.IMPORT_IN_PROGRESS)) {
      stateDescription = "In Progress";
    } else if(stateCode.equals(ImportStateCodes.IMPORT_COMPLETE)) {
      stateDescription = "Complete";
    } else {
      stateDescription = currentStateDescription;
    }
    return stateDescription;
  }

  /**
   * 
   * @param transaction Transaction
   * @return Connection
   */
  @SuppressWarnings("resource")
  private Connection getOracleConnection(Transaction transaction) {
    Connection connection = (Connection) transaction.getDatastore();

    if (connection instanceof WrapperConnection) {
      connection = ((WrapperConnection) connection).getWrappedConnection();
    }

    return connection;
  }
}
