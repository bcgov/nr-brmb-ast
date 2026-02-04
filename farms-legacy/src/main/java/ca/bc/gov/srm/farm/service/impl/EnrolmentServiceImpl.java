/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 * 
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.BenefitValidator;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.dao.EnrolmentReadDAO;
import ca.bc.gov.srm.farm.dao.EnrolmentWriteDAO;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;
import ca.bc.gov.srm.farm.enrolment.EnrolmentCalculatorFactory;
import ca.bc.gov.srm.farm.enrolment.EnwEnrolmentCalculator;
import ca.bc.gov.srm.farm.enrolment.LateParticipantEnrolmentCalculator;
import ca.bc.gov.srm.farm.enrolment.StandardEnrolmentCalculator;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.message.MessageKeys;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.dataimport.LogConverter;
import ca.bc.gov.srm.farm.ui.domain.dataimport.StagingResults;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.ERRORType;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.STAGINGLOG;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.PropertyLoader;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 3, 2010
 */
public class EnrolmentServiceImpl extends BaseService implements EnrolmentService {
  
  private Logger logger = LoggerFactory.getLogger(EnrolmentServiceImpl.class);

  private static final String CSV_SUFFIX = ".csv";

  private static final String ENROLMENT_CSV_FILE = "farm_enrolment_";
  
  private Properties messageProperties;
  
  public EnrolmentServiceImpl() {
    messageProperties = PropertyLoader.loadProperties(MessageKeys.MESSAGES_FILE_PATH);
  }

  @Override
  public List<Enrolment> getEnrolments(Integer enrolmentYear, String regionCode)
  throws ServiceException {
    
    List<Enrolment> enrolments = null;

    Transaction transaction = null;
    transaction = openTransaction();
    EnrolmentReadDAO dao = new EnrolmentReadDAO();

    try {
      enrolments = dao.getEnrolments(transaction, enrolmentYear, regionCode);

    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return enrolments;
  }
  
  @Override
  public void generateEnrolmentsStaging(final Connection connection,
      final Integer importVersionId,
      final File enrolmentFile,
      final String user)
  throws ServiceException {
    
    logger.debug("Generating enrolments");
    
    VersionDAO vdao = null;
    StagingDAO sdao = null;
    EnrolmentWriteDAO ewdao = null;

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      ewdao = new EnrolmentWriteDAO(connection);
      connection.setAutoCommit(false);

      vdao.startUpload(importVersionId, user);
      
      connection.commit();
      
      sdao.status(importVersionId, "Staging Started");
      
      String yearString;
      String pinsString;
      String createTaskInBarnString;
      try (BufferedReader reader = new BufferedReader(new FileReader(enrolmentFile));) {
        yearString = reader.readLine();
        createTaskInBarnString = reader.readLine();
        pinsString = reader.readLine();
      }
      
      Boolean createTaskInBarn = Boolean.valueOf(createTaskInBarnString);

      Integer enrolmentYear = new Integer(yearString);
      List<Integer> pins = extractPinsFromCsv(pinsString);
      
      logger.debug("Retrieving existing margins");
      List<Integer> pinsForCalculation = ewdao.generateEnrolmentsStaging(enrolmentYear, createTaskInBarn, pins, user);
      
      logger.debug("Completed retrieval of existing margins");
      sdao.status(importVersionId, "Completed retrieval of existing margins");
      
      if( ! pinsForCalculation.isEmpty() ) {
        List<EnrolmentStaging> calculatedEnrolments = calculateEnrolments(
            enrolmentYear,
            createTaskInBarn,
            pinsForCalculation,
            sdao,
            importVersionId,
            connection,
            user);
        
        ewdao.updateStaging(calculatedEnrolments, user);
      }
      
      int numPins = pins.size();
      String xml = ImportLogFormatter.createStagingXml(numPins, new ArrayList<>());
      vdao.uploadedVersion(importVersionId, xml, false, user);
      
      connection.commit();
      
      sdao.status(importVersionId, "Staging Completed");
      
    } catch (Exception e) {
      logger.error("Error in generateEnrolmentsStaging", e);
      String xml = ImportLogFormatter.formatUploadException(e);
      String messageErrorRecordingError = "Error recording error in import table";
      try {
        connection.rollback();
        if(vdao != null) {
          vdao.uploadedVersion(importVersionId, xml, Boolean.TRUE, user);
        }
      } catch (SQLException sqle) {
        logger.error(messageErrorRecordingError, e);
      } catch (IOException sqle) {
        logger.error(messageErrorRecordingError, e);
      }
      throw new ServiceException(e);
    }
    
  }


  private List<EnrolmentStaging> calculateEnrolments(
      Integer enrolmentYear,
      Boolean createTaskInBarn,
      List<Integer> pinsForCalculation,
      StagingDAO sdao,
      Integer importVersionId,
      Connection connection, final String user)
  throws Exception {
    ClientService clientService = ClientServiceFactory.getInstance(connection);
    
    StandardEnrolmentCalculator enrolmentCalculator = EnrolmentCalculatorFactory.getStandardEnrolmentCalculator();
    
    List<EnrolmentStaging> calculatedEnrolments = new ArrayList<>();
    int numPins = pinsForCalculation.size();
    int pinsCalculated = 0;

    for(Integer pin : pinsForCalculation) {
      logPin("Calculating enrolment for", pin);
      EnrolmentStaging enrolment;
      
      try {
        int programYear = enrolmentYear.intValue() - 2;
        Scenario scenario = clientService.getClientInfoWithHistory(
            pin.intValue(), programYear, null, ClientService.ENROLMENT_MODE);
        
        
        boolean isBaseDataScenario = scenario != null && scenario.isBaseData();
        
        if(isBaseDataScenario) {
          
          String failMessage = calculateBenefit(scenario, user);
          if(StringUtils.isBlank(failMessage)) {
            enrolment = enrolmentCalculator.calculateStagingEnrolment(enrolmentYear, scenario, createTaskInBarn);
          } else {
            enrolment = enrolmentCalculator.enrolmentStagingFailed(pin, enrolmentYear, false, failMessage);
          }
          
        } else {
          
          /* EnrolmentWriteDAO.generateEnrolmentsStaging() (which calls Farm_Enrolment_Write_Pkg.Generate_Enrolments
           * and returns the list of PINs passed into this method) should not return any of the following:
           * 
           *     1. PINs that have an Enrolment Notice Workflow scenario category with a state of
           *        'In Progress' or 'Enrolment Notice Complete'. 
           *     2. PINs that have a Verified Final, Administrative Adjustment, or Producer Adjustment scenario.
           *     
           * If it it does, then the logic in Farm_Enrolment_Write_Pkg.Generate_Enrolments is incorrect.
           */
          String errorMessage;
          
          if(scenario == null) {
            errorMessage = "Unexpected condition: No scenario found. Please contact the vendor for the FARM application.";
          } else {
            errorMessage = String.format(
                "Unexpected condition: Found scenario of Type: %s, Category: %s, State: %s, Number: %d."
                + " Please contact the vendor for the FARM application.",
                scenario.getScenarioTypeCode(),
                scenario.getScenarioStateCodeDescription(),
                scenario.getScenarioCategoryCodeDescription(),
                scenario.getScenarioNumber());
          }
          enrolment = enrolmentCalculator.enrolmentStagingFailed(pin, enrolmentYear, false, errorMessage);
        }
        
      } catch(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.getBuffer().toString();
        stackTrace = stackTrace.replace('\r', ' ');
        stackTrace = stackTrace.replace('\n', ' ');
        
        enrolment = enrolmentCalculator.enrolmentStagingFailed(pin, enrolmentYear, true, stackTrace);
      }
      
      calculatedEnrolments.add(enrolment);
      
      pinsCalculated++;
      final int ten = 10;
      if((pinsCalculated % ten) == 0 || pinsCalculated == numPins) {
        sdao.status(importVersionId,
            String.format("Calculated Margins for %d of %d uncalculated PINs", pinsCalculated, numPins));
      }
    }
    
    return calculatedEnrolments;
  }

  private String calculateBenefit(Scenario scenario, final String user) throws ServiceException, Exception {
    StringBuilder failMessageBuilder = new StringBuilder();
    
    Integer pin = scenario.getClient().getParticipantPin();
    AdjustmentService adjustmentService = ServiceFactory.getAdjustmentService();
    adjustmentService.makeInventoryValuationAdjustments(scenario, false, false);
    
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);
 
    ActionMessages messages = new ActionMessages();
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    boolean missingBpus = !validator.validateBpus(scenario);
    if(!missingBpus) {
      // CRA and Local Data Entry scenarios have not been calculated, so calculate.
      // Since validation is turned off, there shouldn't be any messages, but check just in case.
      BenefitService benefitService = ServiceFactory.getBenefitService();
      messages = benefitService.calculateBenefit(scenario, user, false, false, true);
    }
    
    if(missingBpus || !messages.isEmpty()) {
      
      if(missingBpus) {
        handleMissingBpus(pin, validator, failMessageBuilder);
      }
      
      for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
        ActionMessage msg = mi.next();
        failMessageBuilder.append(messageProperties.getProperty(msg.getKey()));
        if(mi.hasNext()) {
          failMessageBuilder.append(" ");
        }
      }
    }
    
    return failMessageBuilder.toString();
  }

  private void handleMissingBpus(Integer pin, BenefitValidator validator, StringBuilder errorSb) {
    List<String> invCodes = validator.getRefYearMissingBpuInventoryCodeList();
    List<String> sgCodes = validator.getRefYearMissingBpuStructureGroupCodeList();
    
    String errorMessage = Enrolment.REASON_BPU_SET_INCOMPLETE
        + (invCodes.isEmpty() ? "" : Enrolment.BPU_INVENTORY_CODES + StringUtils.toCsv(invCodes))
        + (sgCodes.isEmpty() ? "" : Enrolment.BPU_STRUCTURE_GROUP_CODES + StringUtils.toCsv(sgCodes));
    
    logPin("Failed to calculate. " + errorMessage, pin);
    errorSb.append(errorMessage);
  }

  private void logPin(String logMessage, Integer pin) {
    StringBuilder sb = new StringBuilder();
    sb.append(logMessage);
    sb.append(" PIN: ");
    sb.append(pin);
    logger.debug(sb.toString());
  }


  private List<Integer> extractPinsFromCsv(String pinsString) throws ParseException {
    List<Integer> pins = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(pinsString, ",");
    while(st.hasMoreTokens()) {
      String curPinString = st.nextToken();
      if( ! StringUtils.isBlank(curPinString) ) {
        Integer curPin = DataParseUtils.parseIntegerObject(curPinString);
        pins.add(curPin);
      }
    }
    return pins;
  }


  @Override
  public void setStagingResults(final Transaction transaction,
      final STAGINGLOG log,
      final StagingResults details)
  throws ServiceException {
    EnrolmentReadDAO rdao = new EnrolmentReadDAO();
    
    try {
      List<EnrolmentStaging> errors = getStagingErrors(log);
      details.setErrors(errors);
      
      List<EnrolmentStaging> results = rdao.getStagingResults(transaction);
      if(details.getImportVersion().getIsLatestOfClass()) {
        
        @SuppressWarnings("unchecked")
        List<EnrolmentStaging> warnings = (List<EnrolmentStaging>) details.getWarnings();
        for(EnrolmentStaging e : results) {
          if(e.getFailedToGenerate() || e.getFailedReason() != null) {
            if(e.getIsError()) {
              errors.add(e);
            } else {
              warnings.add(e);
            }
          }
        }
      }
      details.setNumberOfItems(results.size());
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }
  

  private List<EnrolmentStaging> getStagingErrors(STAGINGLOG log) {
    ArrayList<EnrolmentStaging> items = new ArrayList<>();
    
    if(log.getERRORS() != null) {
      
      @SuppressWarnings("unchecked")
      Iterator<ERRORType> iter = log.getERRORS().getERROR().iterator();
      
      while(iter.hasNext()) {
        ERRORType err = iter.next();
        EnrolmentStaging e = new EnrolmentStaging();
        
        e.setFailedReason(LogConverter.formatErrorMessage(err.getValue()));

        items.add(e);
      }
    }
    
    return items;
  }
  
  
  @Override
  public void finishGeneration(final Connection connection,
      final Integer importVersionId,
      final String user)
  throws ServiceException {
    
    VersionDAO vdao = null;
    StagingDAO sdao = null;
    EnrolmentReadDAO erdao = new EnrolmentReadDAO();
    EnrolmentWriteDAO ewdao = null;

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      ewdao = new EnrolmentWriteDAO(connection);
      connection.setAutoCommit(false);

      sdao.status(importVersionId, "Started");
      vdao.startImport(importVersionId, user);
      connection.commit();

      ewdao.copyStagingToOperational();
      List<EnrolmentStaging> enrolments = erdao.getStaging(connection);
      List<EnrolmentStaging> transferList = new ArrayList<>();
      
      for(EnrolmentStaging e : enrolments ) {
        if(!e.getFailedToGenerate()) {
          transferList.add(e);
        }
      }
      
      int numTransfer = transferList.size();

      if(numTransfer == 0) {
        logger.debug("No Enrolment Data to Transfer");
        sdao.status(importVersionId, "No Enrolment Data to Transfer to CRM");
        importCompleted(connection, importVersionId, user, vdao, true);
      } else {
        
        try {
          logger.debug("Scheduling transfer");
          sdao.status(importVersionId, "Scheduling transfer to CRM");
          
          scheduleTransfer(connection, transferList, user);

          logger.debug("Transfer scheduled");
          sdao.status(importVersionId, "Scheduled " + numTransfer + " Enrolments for Transfer to CRM");

          importCompleted(connection, importVersionId, user, vdao, true);
        } catch (Exception e) {
          String xml = ImportLogFormatter.formatImportException(e);
          connection.rollback();
          sdao.status(importVersionId, "Failed to Schedule Transfer to CRM");
          vdao.importFailed(importVersionId, xml, user);
          connection.commit();
        }
      }
      
    } catch (Exception e) {
      String xml = ImportLogFormatter.formatImportException(e);
      try {
        connection.rollback();
        if(vdao != null) {
          vdao.importFailed(importVersionId, xml, user);
          connection.commit();
        }
      } catch (SQLException | IOException ex) {
        throw new ServiceException(ex);
      }
    }
  }
  
  
  private void scheduleTransfer(Connection connection, List<EnrolmentStaging> transferList, String user)
  throws Exception {
    
    Integer enrolmentYear = transferList.get(0).getEnrolmentYear();
    StringBuilder pinsSb = new StringBuilder();

    for(Iterator<EnrolmentStaging> ti = transferList.iterator(); ti.hasNext(); ) {
      EnrolmentStaging e = ti.next();
      pinsSb.append(e.getPin().toString());
      if(ti.hasNext()) {
        pinsSb.append(",");
      }
    }

    String pinsString = pinsSb.toString();
    
    String description = "Transfer of " + transferList.size() + " Enrolments to CRM";
    
    File pTempDir = IOUtils.getTempDir(); 

    File outFile = File.createTempFile("farm_enrolment_transfer_", ".csv", pTempDir);
    FileOutputStream fos = new FileOutputStream(outFile);
    try (PrintWriter pw = new PrintWriter(fos);) {
      
      pw.println(enrolmentYear.toString());
      pw.println(pinsString);
      pw.flush();
      pw.close();
    }

    ImportService service = ServiceFactory.getImportService();

    // create a farm_import_versions entry, and save the file to a blob
    try (FileInputStream inputStream = new FileInputStream(outFile);) {
      service.createImportVersion(
          connection,
          ImportClassCodes.XENROL,
          ImportStateCodes.SCHEDULED_FOR_STAGING,
          description,
          outFile.getPath(),
          inputStream,
          user);
    }
    
  }
  
  
  private void scheduleEnrolmentTransferFromScenarioWorkflow(Connection connection, List<Enrolment> transferList, String user)
      throws Exception {
    
    Enrolment enrolment = transferList.get(0);
    Integer enrolmentYear = enrolment.getEnrolmentYear();
    StringBuilder pinsSb = new StringBuilder();
    
    for(Iterator<Enrolment> ti = transferList.iterator(); ti.hasNext(); ) {
      Enrolment e = ti.next();
      pinsSb.append(e.getPin().toString());
      if(ti.hasNext()) {
        pinsSb.append(",");
      }
    }
    
    String pinsString = pinsSb.toString();
    
    String description;
    if(transferList.size() == 1) {
      description = "Enrolment transfer for " + enrolmentYear + " PIN: " + pinsString + "";
    } else {
      description = "Transfer of " + transferList.size() + " Enrolments to CRM";
    }
    
    File pTempDir = IOUtils.getTempDir(); 
    
    File outFile = File.createTempFile("farm_enrolment_transfer_", ".csv", pTempDir);
    FileOutputStream fos = new FileOutputStream(outFile);
    try (PrintWriter pw = new PrintWriter(fos);) {
      
      pw.println(enrolmentYear.toString());
      pw.println(pinsString);
      pw.flush();
      pw.close();
    }
    
    ImportService service = ServiceFactory.getImportService();
    
    // create a farm_import_versions entry, and save the file to a blob
    try (FileInputStream inputStream = new FileInputStream(outFile);) {
      service.createImportVersion(
          connection,
          ImportClassCodes.XENROL,
          ImportStateCodes.SCHEDULED_FOR_STAGING,
          description,
          outFile.getPath(),
          inputStream,
          user);
    }
    
  }
  
  
  @Override
  public void transfer(final Connection connection,
      final Integer importVersionId,
      final File enrolmentFile,
      final String user)
  throws ServiceException {
    
    VersionDAO vdao = null;
    StagingDAO sdao = null;
    EnrolmentReadDAO erdao = new EnrolmentReadDAO();

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      connection.setAutoCommit(false);

      vdao.startImport(importVersionId, user);
      connection.commit();
      sdao.status(importVersionId, "Started");
      
      CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();
      
      if(crmConfig.isEnrolmentUrlConfigured()) {
  
        String yearString;
        String pinsString;
        try(BufferedReader reader = new BufferedReader(new FileReader(enrolmentFile));) {
          yearString = reader.readLine();
          pinsString = reader.readLine();
        }
        Integer enrolmentYear = new Integer(yearString);
    
        List<Integer> pins = extractPinsFromCsv(pinsString);
        
        List<Enrolment> transferList = erdao.getEnrolmentsForTransfer(connection, enrolmentYear, pins);
        
        CrmTransferService transferService = ServiceFactory.getCrmTransferService();
        
        for(Enrolment e : transferList) {
          transferService.postEnrolment(e, importVersionId, user);
        }

        int numTransferred = transferList.size();
        String stagingXml = ImportLogFormatter.createStagingXml(numTransferred, new ArrayList<>());
        Boolean hasErrors = Boolean.FALSE;
        vdao.uploadedVersion(importVersionId, stagingXml, hasErrors, user);
        importCompleted(connection, importVersionId, user, vdao, true);
        sdao.status(importVersionId, numTransferred + " Enrolments transferred to CRM.");
      } else {
        String xml = ImportLogFormatter.createEmptyImportXml();
        sdao.status(importVersionId,
            "CRM Web Service is not configured. No Enrolments transferred to CRM.");
        vdao.importFailed(importVersionId, xml, user);
        connection.commit();
      }

    } catch (Throwable t) {
      String message = "Error processing account update: " + t;
      logger.error(message);
      
      String xml = ImportLogFormatter.formatImportException(t);
      try {
        if(vdao != null) {
          vdao.importFailed(importVersionId, xml, user);
          connection.commit();
        }
        if(sdao != null) {
          sdao.status(importVersionId, "Failed to Transfer to CRM");
        }
      } catch (SQLException | IOException e) {
        throw new ServiceException(e);
      }
    }
  }


  private void importCompleted(final Connection connection, final Integer importVersionId, final String user,
      VersionDAO vdao, boolean commit) throws SQLException, IOException {
    String xml = ImportLogFormatter.createEmptyImportXml();
    vdao.importCompleted(importVersionId, xml, user);
    if(commit) {
      connection.commit();
    }
  }

  @Override
  public File generateCsv(Integer enrolmentYear,
      List<Integer> pins, 
      File tempDir,
      final String user)
  throws ServiceException {
    
    Transaction transaction = null;
    transaction = openTransaction();
    EnrolmentReadDAO dao = new EnrolmentReadDAO();
    
    File pTempDir = tempDir;
    if(pTempDir == null){
      pTempDir = IOUtils.getTempDir(); 
    }

    File csvFile = null;

    try {
      csvFile = File.createTempFile(ENROLMENT_CSV_FILE, CSV_SUFFIX, pTempDir);
      
      Format[] columnFormats = getCsvColumnFormats();
      
      dao.writeCsv(transaction, enrolmentYear, pins, columnFormats, csvFile);

    } catch (IOException e) {
      throw new ServiceException("IOException writing CSV file", e);
    }
    
    return csvFile;
  }

  /**
   * @return array of formats for column values
   */
  private Format[] getCsvColumnFormats() {
    DecimalFormat currencyFormat = new DecimalFormat("#0.##");
    DecimalFormat integerFormat = new DecimalFormat("#");
    DecimalFormat percentFormat = new DecimalFormat("#0.####");

    final int columnCount = 21;
    Format[] columnFormats = new Format[columnCount];
    int formatIdx = 0;
    columnFormats[formatIdx++] = integerFormat;   // PARTICIPANT_PIN
    columnFormats[formatIdx++] = integerFormat;   // ENROLMENT_YEAR
    columnFormats[formatIdx++] = currencyFormat;  // ENROLMENT_FEE
    columnFormats[formatIdx++] = currencyFormat;  // CONTRIBUTION_MARGIN
    columnFormats[formatIdx++] = currencyFormat;  // PYM_YEAR_2
    columnFormats[formatIdx++] = null;            // PYM_YEAR_2_IND
    columnFormats[formatIdx++] = currencyFormat;  // PYM_YEAR_3
    columnFormats[formatIdx++] = null;            // PYM_YEAR_3_IND
    columnFormats[formatIdx++] = currencyFormat;  // PYM_YEAR_4
    columnFormats[formatIdx++] = null;            // PYM_YEAR_4_IND
    columnFormats[formatIdx++] = currencyFormat;  // PYM_YEAR_5
    columnFormats[formatIdx++] = null;            // PYM_YEAR_5_IND
    columnFormats[formatIdx++] = currencyFormat;  // PYM_YEAR_6
    columnFormats[formatIdx++] = null;            // PYM_YEAR_6_IND
    columnFormats[formatIdx++] = null;            // GENERATED_DATE
    columnFormats[formatIdx++] = null;            // GENERATED_FROM_CRA_IND
    columnFormats[formatIdx++] = null;            // GENERATED_FROM_ENW_IND
    columnFormats[formatIdx++] = null;            // CREATE_TASK_IN_BARN_IND
    columnFormats[formatIdx++] = percentFormat;   // COMBINED_FARM_PERCENT
    columnFormats[formatIdx++] = null;            // FAILED_TO_GENERATE_IND
    columnFormats[formatIdx++] = null;            // FAILED_REASON
    
    return columnFormats;
  }


  @Override
  public Scenario getScenario(
      final int pin,
      final int programYear)
  throws ServiceException {

    Scenario scenario = null;
    Transaction transaction = null;

    try {
      transaction = openTransaction();

      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();

      ClientService service = ClientServiceFactory.getInstance(connection);

      scenario = service.getClientInfoWithHistory(pin, programYear, null,
          ClientService.ENROLMENT_MODE);
    } finally {
      closeTransaction(transaction);
    }

    return scenario;
  }
  
  
  @Override
  public void processEnrolmentFromScenarioWorkflow(Scenario scenario, boolean verifyingLatePartipant,
      boolean completingEnrolmentNotice, String user, Transaction transaction)
      throws ServiceException {
    @SuppressWarnings("resource")
    Connection connection = (Connection) transaction.getDatastore();
    processEnrolmentFromScenarioWorkflow(scenario, verifyingLatePartipant, completingEnrolmentNotice, user, connection);
  }
  
  
  
  @Override
  public void processEnrolmentFromScenarioWorkflow(Scenario scenario, boolean verifyingLatePartipant,
      boolean completingEnrolmentNotice, String user, Connection connection)
  throws ServiceException {
    
    Enrolment enrolment;
    
    if(completingEnrolmentNotice) {
      EnwEnrolmentCalculator enwEnrolmentCalculator = EnrolmentCalculatorFactory.getEnwEnrolmentCalculator();
      enrolment = enwEnrolmentCalculator.convertEnwToEnrolment(scenario, scenario.getEnwEnrolment());
    } else if(verifyingLatePartipant) {
      LateParticipantEnrolmentCalculator lateParticipantEnrolmentCalculator = EnrolmentCalculatorFactory.getLateParticipantEnrolmentCalculator();
      enrolment = lateParticipantEnrolmentCalculator.calculateEnrolment(scenario);
    } else {
      throw new IllegalStateException(
          "Expected that the scenario meets one of these criteria: "
          + " 1. Enrolment Notice Complete"
          + " 2. Verified and a Late Participant");
    }
    
    List<Enrolment> enrolments = new ArrayList<>(1);
    enrolments.add(enrolment);
    
    updateOperational(enrolments, user, connection);
    
    ImportService importService = ServiceFactory.getImportService();
    
    String pinString = scenario.getClient().getParticipantPin().toString();
    
    try {
      
      // Create empty file since we won't actually read from it.
      Path enrolmentFilePath = Files.createTempFile("farm_enrolment_", ".csv");
      
      // create a farm_import_versions entry, and save the file to a blob
      try (InputStream importFileInputStream = Files.newInputStream(enrolmentFilePath);) {
        String enrolmentMessageText;
        if(scenario.isLateParticipant()) {
          enrolmentMessageText = " - Auto-generated for Late Participant. PIN: ";
        } else {
          enrolmentMessageText = " - Auto-generated for Enrolment Notice Workflow. PIN: ";
        }
        String description = enrolment.getEnrolmentYear() + enrolmentMessageText + pinString;
        ImportVersion importVersion = importService.createImportVersion(
            connection,
            ImportClassCodes.ENROL,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            description,
            enrolmentFilePath.getFileName().toString(),
            importFileInputStream,
            "ENROLMENT_SERVICE");
        
        Integer importVersionId = importVersion.getImportVersionId();
        
        VersionDAO vdao = new VersionDAO(connection);
        StagingDAO sdao = new StagingDAO(connection);
  
        logger.debug("Enrolment generated");
        
        String statusMessage = "Scheduled " + enrolments.size() + " Enrolments for Transfer to CRM";
        sdao.statusNonAutonomous(importVersionId, statusMessage);

        importCompleted(connection, importVersionId, user, vdao, false);
        
        Files.deleteIfExists(enrolmentFilePath);
        
        scheduleEnrolmentTransferFromScenarioWorkflow(connection, enrolments, user);
      }
      
    } catch (Exception ex) {
      throw new ServiceException(ex);
    }
      
  }

  private void updateOperational(
      final List<Enrolment> enrolments,
      final String user,
      final Connection connection)
  throws ServiceException {
    
    EnrolmentWriteDAO wdao = new EnrolmentWriteDAO(connection);
    
    try {
      wdao.updateOperational(enrolments, user);
    } catch(ServiceException se) {
      throw se;
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

}
