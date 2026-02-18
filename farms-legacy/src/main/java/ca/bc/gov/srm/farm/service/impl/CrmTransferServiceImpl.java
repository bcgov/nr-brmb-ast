/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;


import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountAnnotationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitAnnotationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitUpdateResource;
import ca.bc.gov.srm.farm.crm.transform.BenefitUpdateTransformer;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.dao.CrmTransferDAO;
import ca.bc.gov.srm.farm.dao.ImportDAO;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.domain.codes.CrmEntityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.OracleUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class CrmTransferServiceImpl extends BaseService implements CrmTransferService {
  
  private final Logger logger = LoggerFactory.getLogger(CrmTransferServiceImpl.class);
  
  private static final int FORM_USER_TYPE_INDEX = 32;
  private static final int SUBMISSIONGUID_INDEX = 33;
  private static final int CHEFS_FORM_TYPE_INDEX = 35;
  
  private final BenefitUpdateTransformer benefitTransformer = new BenefitUpdateTransformer();
  
  private final CrmRestApiDao crmDao = new CrmRestApiDao();
  
  private final CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();
  
  private final CdogsService cdogsService = ServiceFactory.getCdogsService();
  
  private ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
  
  @Override
  public void scheduleBenefitTransfer(Scenario scenario, String userEmail, String userId)
      throws Exception {
    scheduleBenefitTransfer(scenario, userEmail, userId, null);
  }

  @Override
  public void scheduleBenefitTransfer(Scenario scenario, String userEmail, String userId, Transaction transaction)
      throws Exception {
    scheduleBenefitTransfer(scenario, userEmail, userId, null, null, null, null, transaction);
  }
  
  @SuppressWarnings("resource")
  @Override
  public void scheduleBenefitTransfer(Scenario scenario, String userEmail, String userId,
      String chefsFormNotes, String formUserType, String chefsFormType, String benefitTriageResultType, Transaction transaction)
  throws Exception {
    
    Date stateChangeDate = new Date(); // now
    String csvLine = benefitTransformer.generateCsv(scenario, stateChangeDate, userEmail, chefsFormNotes, formUserType, chefsFormType, benefitTriageResultType);
    
    String transferDescriptionTemplate = "State Change Transfer for %d PIN: %d, State: %s, Category: %s";
    String transferDescription = String.format(
        transferDescriptionTemplate,
        scenario.getYear(),
        scenario.getClient().getParticipantPin(),
        scenario.getScenarioStateCodeDescription(),
        scenario.getScenarioCategoryCodeDescription());
    
    File pTempDir = IOUtils.getTempDir();

    File outFile = File.createTempFile("farm_state_transfer_", ".csv", pTempDir);
    
    try(FileOutputStream fos = new FileOutputStream(outFile);
      PrintWriter pw = new PrintWriter(fos)) {
      pw.println(csvLine);
    }

    ImportService service = ServiceFactory.getImportService();

    // create a farm_import_versions entry, and save the file to a blob
    try(FileInputStream fileInputStream = new FileInputStream(outFile)) {
      
      // we may already have started a transaction
      if(transaction == null) {
        service.createImportVersion(
            ImportClassCodes.XSTATE,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            transferDescription,
            outFile.getPath(),
            fileInputStream,
            userId);
      } else {
        Connection connection = OracleUtils.getOracleConnection(transaction);
        service.createImportVersion(
            connection,
            ImportClassCodes.XSTATE,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            transferDescription,
            outFile.getPath(),
            fileInputStream,
            userId);
      }
    }
    
  }

  
  @Override
  public void immediateBenefitTransfer(Scenario scenario, String userEmail, String userId,
      String chefsFormNotes, String formUserType, String chefsFormType, String benefitTriageResultType, Connection connection)
  throws ServiceException {
    
    try {
      ImportDAO dao = new ImportDAO();
      
      Date stateChangeDate = new Date(); // now
      String csvLine = benefitTransformer.generateCsv(scenario, stateChangeDate, userEmail, chefsFormNotes, formUserType, chefsFormType, benefitTriageResultType);
      StringReader stringReader = new StringReader(csvLine);
      CSVReader csvReader = new CSVReader(stringReader);
      String[] fields = csvReader.readNext();
      
      postBenefitTransfer(connection, fields, userId);
      
      String importClassCode = ImportClassCodes.XSTATE;
      String importStateCode = ImportStateCodes.IMPORT_COMPLETE;
      final String transferDescriptionTemplate = "State Change Transfer for %d PIN: %d, State: %s, Category: %s";
      String transferDescription = String.format(
          transferDescriptionTemplate,
          scenario.getYear(),
          scenario.getClient().getParticipantPin(),
          scenario.getScenarioStateCodeDescription(),
          scenario.getScenarioCategoryCodeDescription());
      
      dao.createEmptyTransferRecord(userId, connection, importClassCode, importStateCode, transferDescription);
      connection.commit();
    } catch (DataAccessException e) {
      logger.error("DataAccessException: ", e);
      throw new ServiceException(e);
    } catch (IOException e) {
      logger.error("IOException: ", e);
      throw new ServiceException(e);
    } catch (SQLException e) {
      logger.error("SQLException: ", e);
      throw new ServiceException(e);
    }
  }


  @Override
  public void transferBenefitUpdate(final Connection connection,
      final Integer importVersionId,
      final File transferFile,
      final String user) throws ServiceException {
    
    VersionDAO vdao = null;
    StagingDAO sdao = null;

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      connection.setAutoCommit(false);

      vdao.startImport(importVersionId, user);
      connection.commit();
      sdao.status(importVersionId, "Started");
      
      try(FileReader fileReader = new FileReader(transferFile)) {
        CSVReader reader = new CSVReader(fileReader);
        
        int lineNum = 0;
        String[] fields = reader.readNext();
        while(fields != null) {
          try {
            postBenefitTransfer(connection, fields, user);
          } catch(Exception e) {
            String message = "Error processing line " + lineNum + ": " + e;
            logger.error(message);
            throw new ServiceException(message);
          }
          fields = reader.readNext();
          lineNum++;
        }

        int numTransferred = lineNum;
        transferCompleted(connection, importVersionId, user, vdao, numTransferred);
        sdao.status(importVersionId, numTransferred + " State Change Events transferred to CRM.");
      }

    } catch (Throwable t) {
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


  private void postBenefitTransfer(final Connection connection, String[] fields, String user)
  throws ServiceException {
    logger.debug("<postBenefitTransfer");

    CrmBenefitUpdateResource resource = crmDao.createBenefitUpdate(fields);

    String submissionGuid = CrmTransferFormatUtil.getFieldValue(fields, SUBMISSIONGUID_INDEX);
    String formUserType   = CrmTransferFormatUtil.getFieldValue(fields, FORM_USER_TYPE_INDEX);
    String chefsFormType  = CrmTransferFormatUtil.getFieldValue(fields, CHEFS_FORM_TYPE_INDEX);
    String transactionBenefitId = resource.getVsi_transactionbenefitid();
    String benefitCategory = resource.getVsi_benefitcategory();
    
    if (!StringUtils.isBlank(submissionGuid) && !StringUtils.isBlank(formUserType) && transactionBenefitId != null &&
        canUploadSubmissionFormToCrm(benefitCategory, chefsFormType)) {
      
      uploadSubmissionFormToCrmUpdateBenefitNote(connection, submissionGuid, formUserType, transactionBenefitId, benefitCategory, chefsFormType, user);
    }
    
    logger.debug(">postBenefitTransfer");
  }
  
  private boolean canUploadSubmissionFormToCrm(String benefitCategory, String chefsFormType) {
    return ChefsFormTypeCodes.CM.equals(chefsFormType) ||
           ChefsFormTypeCodes.CN.equals(chefsFormType) ||
           ChefsFormTypeCodes.STA.equals(chefsFormType) ||
           ChefsFormTypeCodes.SUPP.equals(chefsFormType) ||
           benefitCategory.equals(ScenarioCategoryCodes.CHEF_CM) ||
           benefitCategory.equals(ScenarioCategoryCodes.CHEF_STA) ||
           benefitCategory.equals(ScenarioCategoryCodes.INTERIM) ||
           benefitCategory.equals(ScenarioCategoryCodes.PRODUCER_ADJUSTMENT);
  }

  @Override
  public void scheduleAccountUpdate(Integer clientId, String description, String userId, Transaction transaction) throws Exception {
  	scheduleAccountUpdate(clientId, description, userId, null, null, transaction);
  }
  
  @SuppressWarnings("resource")
  @Override
  public void scheduleAccountUpdate(Integer clientId, String description, String userId, String submissionGuid, String formUserType, Transaction transaction)
  throws Exception {
    
  	String csvLine = clientId.toString();
    
    File pTempDir = IOUtils.getTempDir();

    File outFile = File.createTempFile("farm_account_transfer_", ".csv", pTempDir);
    
    try(FileOutputStream fos = new FileOutputStream(outFile);
      PrintWriter pw = new PrintWriter(fos)) {
      pw.println(csvLine);
      if(submissionGuid != null && formUserType != null) {
        pw.println(submissionGuid);
        pw.println(formUserType);
      }
    }

    ImportService service = ServiceFactory.getImportService();

    // create a farm_import_versions entry, and save the file to a blob
    try(FileInputStream fileInputStream = new FileInputStream(outFile)) {
      
      // we may already have started a transaction
      if(transaction == null) {
        service.createImportVersion(
            ImportClassCodes.XCONTACT,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            description,
            outFile.getPath(),
            fileInputStream,
            userId);
      } else {
        Connection connection = OracleUtils.getOracleConnection(transaction);
        service.createImportVersion(
            connection,
            ImportClassCodes.XCONTACT,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            description,
            outFile.getPath(),
            fileInputStream,
            userId);
      }
    }
    
  }
  
  
  @Override
  public void transferAccountUpdate(final Connection connection,
      final Integer importVersionId,
      final File transferFile,
      final String user) throws ServiceException {
    
    VersionDAO vdao = null;
    StagingDAO sdao = null;
    CrmTransferDAO tdao = new CrmTransferDAO();

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      connection.setAutoCommit(false);

      vdao.startImport(importVersionId, user);
      connection.commit();
      sdao.status(importVersionId, "Started");

      List<Integer> clientIds = new ArrayList<>();
      Map<Integer, String> submissionGuidMap = new HashMap<>();
      Map<Integer, String> formUserTypedMap = new HashMap<>();
      try(FileReader fileReader = new FileReader(transferFile)) {
        CSVReader reader = new CSVReader(fileReader);
        String[] idStrings = reader.readNext();
        String[] submissionGuidStrings = reader.readNext();
        String[] formUserTypeStrings = reader.readNext();
        if(reader.readNext() != null) {
          throw new IllegalArgumentException("Expected no more lines");
        }
        for (int ii = 0; ii < idStrings.length; ii++) {
          Integer curId = new Integer(idStrings[ii]);
          clientIds.add(curId);
          if (submissionGuidStrings != null && ii < submissionGuidStrings.length) {
            submissionGuidMap.put(curId, submissionGuidStrings[ii]);
          }
          if (formUserTypeStrings != null && ii < formUserTypeStrings.length) {
            formUserTypedMap.put(curId, formUserTypeStrings[ii]);
          }
        }
      }
      
      List<Client> transferList = tdao.getContactInformation(connection, clientIds);
      Map<Integer, FarmingYear> farmingYears = tdao.getFarmingYears(connection, clientIds);
      
      for (Client client : transferList) {
        FarmingYear farmingYear = farmingYears.get(client.getClientId());
        handleAccountUpdate(client, farmingYear, submissionGuidMap.get(client.getClientId()),
            formUserTypedMap.get(client.getClientId()));
      }

      int numTransferred = clientIds.size();
      transferCompleted(connection, importVersionId, user, vdao, numTransferred);
      sdao.status(importVersionId,
          "Updated Contact Info transferred to CRM for " + numTransferred + " Participants.");

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


  private void handleAccountUpdate(Client client, FarmingYear farmingYear, String submissionGuid, String formUserType) throws ServiceException {
    
    CrmAccountResource crmAccount = crmDao.getAccountByPin(client.getParticipantPin());

    if(crmAccount == null) {
      crmDao.createAccount(client, farmingYear, submissionGuid, formUserType);
    } else {

      boolean updateAccount = false;
      if (StringUtils.isBlank(crmAccount.getVsi_businessnumber())) {
        crmAccount.setBusinessNumberFromClient(client);
        updateAccount |= StringUtils.isNotBlank(crmAccount.getVsi_businessnumber());
      }

      if (StringUtils.isBlank(crmAccount.getVsi_socialinsurancenumber())
          && StringUtils.isNotBlank(client.getSin())) {
        crmAccount.setVsi_socialinsurancenumber(client.getSin());
        updateAccount |= true;
      }
      
      if(updateAccount) {
        crmDao.updateAccount(crmAccount);
      }

      // If handling a CHEFS form then upload the PDF of the form.
      // If we've uploaded a PDF of the form then there is no need
      // to create an account update task because the PDF includes
      // all of that information.
      if (submissionGuid != null && formUserType != null) {
        crmDao.uploadNppFormToCrm(crmAccount, submissionGuid, formUserType);
      } else {
        crmDao.createAccountUpdateTask(client, crmAccount, submissionGuid);
      }
    }
  }

  
  @Override
  public void postEnrolment(Enrolment e, Integer importVersionId, String user)
      throws Exception {
    logger.debug("<postEnrolment");

    crmDao.createEnrolmentUpdate(e, importVersionId, user);
    
    logger.debug(">postEnrolment");
  }

  
  private void transferCompleted(
      final Connection connection,
      final Integer importVersionId,
      final String user,
      VersionDAO vdao,
      int numTransferred) throws Exception {
    String stagingXml = ImportLogFormatter.createStagingXml(numTransferred, new ArrayList<>());
    Boolean hasErrors = Boolean.FALSE;
    vdao.uploadedVersion(importVersionId, stagingXml, hasErrors, user);
    String importXml = ImportLogFormatter.createEmptyImportXml();
    vdao.importCompleted(importVersionId, importXml, user);
    connection.commit();
  }
  
  
  @SuppressWarnings("resource")
  @Override
  public void clearSuccessfulTransfers() throws ServiceException {
    
    Transaction transaction = openTransaction();
    Connection connection = OracleUtils.getOracleConnection(transaction);
    VersionDAO vdao = null;

    try {
      vdao = new VersionDAO(connection);
      connection.setAutoCommit(false);

      vdao.clearSuccessfulTransfers();
      connection.commit();

    } catch (SQLException sqle) {
      throw new ServiceException(sqle);
    }
  }
  
  private void uploadSubmissionFormToCrmUpdateBenefitNote(final Connection connection, String submissionGuid, String userType,
                                                          String transactionBenefitId,  String  benefitCategory, String chefsFormType, String user) throws ServiceException {
    logMethodStart(logger);

    String scenarioCategoryCode = null;

    ChefsSubmission submission = chefsDatabaseDao.readSubmissionByGuid(connection, submissionGuid);
    if (submission == null) {
      logger.error("Can not find submission for submissionGuid:" + submissionGuid);
    } else {
      ChefsSubmssnCrmEntity chefSubmssnCrmEntity = new ChefsSubmssnCrmEntity();
      chefSubmssnCrmEntity.setCrmEntityGuid(transactionBenefitId);
      chefSubmssnCrmEntity.setCrmEntityTypeCode(CrmEntityTypeCodes.BENEFIT_UP);
      chefSubmssnCrmEntity.setChefSubmissionId(submission.getSubmissionId());
      chefsDatabaseDao.createCrmEntity(connection, chefSubmssnCrmEntity, user);
    }
    
    CrmBenefitAnnotationResource benefitAnnotation = new CrmBenefitAnnotationResource();
    benefitAnnotation.setIsdocument(true);
    benefitAnnotation.setEntityId(transactionBenefitId);

    Map<Integer,File> participantPinFileMap = null;

    switch (benefitCategory) {
    case ScenarioCategoryCodes.PRODUCER_ADJUSTMENT:
      participantPinFileMap = cdogsService.createCdogsAdjustmentFormDocument(submissionGuid, userType);
      scenarioCategoryCode = "Adjustment";
      break;
    case ScenarioCategoryCodes.INTERIM:
      participantPinFileMap = cdogsService.createCdogsInterimFormDocument(submissionGuid, userType);
      scenarioCategoryCode = "Interim";
      break;
    case ScenarioCategoryCodes.CHEF_CM:
      participantPinFileMap = cdogsService.createCdogsCashMarginsFormDocument(submissionGuid, userType);
      scenarioCategoryCode = "Cash Margins";
      break;
    case ScenarioCategoryCodes.CHEF_STA:
      participantPinFileMap = cdogsService.createCdogsStatementAFormDocument(submissionGuid, userType);
      scenarioCategoryCode = "Statement A";
      break;
    }
    
    if (ChefsFormTypeCodes.SUPP.equals(chefsFormType)) {
      participantPinFileMap = cdogsService.createCdogsSupplementalFormDocument(submissionGuid, userType);
      scenarioCategoryCode = "Supplemental";
    }
    
    if(participantPinFileMap == null) {
      throw new IllegalArgumentException("Unexepected condition. No CDOGS document generated.");
    }
    
    Map.Entry<Integer,File> entry = participantPinFileMap.entrySet().iterator().next();
    File file = entry.getValue();

    benefitAnnotation.setSubject(scenarioCategoryCode + " Form");
    benefitAnnotation.setNotetext("Attach CHEFS form to update benefit note");
      assert file != null;
      benefitAnnotation.setFilename(file.getName());

     try {
      benefitAnnotation.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      throw new ServiceException(e);
    }

    try {
      crmDao.post(benefitAnnotation, crmConfig.getAnnotationUrl(), CrmConstants.HEADER_ENTITY_URL);
    } catch (ServiceException e) {
      logger.error("Error uploading CHEFS Interim or Adjustment form to CRM: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
  }
  
  @Override
  public void uploadFileToNote(File file, String crmEntityGuid, String formType) throws ServiceException {

      CrmBenefitAnnotationResource CrmBenefitEntity = createCrmBenefitAnnotation(file, crmEntityGuid, formType);
      crmDao.uploadFileToNote(CrmBenefitEntity);
  }

  @Override
  public void uploadFileToAccount(File file, Integer participantPin, String formType) throws ServiceException {

    CrmAccountAnnotationResource crmAccountEntity = createCrmAccountAnnotation(file, participantPin, formType);
    crmDao.uploadFileToNote(crmAccountEntity);
  }

  private CrmBenefitAnnotationResource createCrmBenefitAnnotation(File file, String crmEntityGuid, String formType) throws ServiceException {

    CrmBenefitAnnotationResource benefitEntity = new CrmBenefitAnnotationResource();
    benefitEntity.setSubject(formType + " Form");
    benefitEntity.setNotetext("Retry uploading submission form from FARM");
    benefitEntity.setFilename(file.getName());
    benefitEntity.setEntityId(crmEntityGuid);
    benefitEntity.setIsdocument(true);
    try {
      benefitEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      logger.debug(e.getMessage());
      throw new ServiceException(e);
    }
    return benefitEntity;
  }

  private CrmAccountAnnotationResource createCrmAccountAnnotation(File file, Integer participantPin, String formType) throws ServiceException {
    CrmAccountResource account = null;
    try {
      account = crmDao.getAccountByPin(participantPin);
      if (account == null) {
        throw new ServiceException("account not found for participantPin: " + participantPin);
      }
    } catch (ServiceException e) {
      logger.debug(e.getMessage());
      throw new ServiceException(e);
    }

    CrmAccountAnnotationResource accountEntity = new CrmAccountAnnotationResource();
    accountEntity.setSubject(formType + " Form");
    accountEntity.setNotetext("Retry uploading submission form from FARM");
    accountEntity.setFilename(file.getName());
    accountEntity.setEntityId(account.getAccountid());
    accountEntity.setIsdocument(true);
    try {
      accountEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      logger.debug(e.getMessage());
      throw new ServiceException(e);
    }
    return accountEntity;
  }
  
  @Override
  public CrmAccountResource accountUpdate(final Connection connection, final CrmAccountResource crmAccount, Client client, String submissionGuid, final String formUserType, String chefsFormTypeCode)
      throws ServiceException {
    
    CrmAccountResource result = crmAccount;

    if (crmAccount == null) {
      CrmTransferDAO tdao = new CrmTransferDAO();
      Map<Integer, FarmingYear> farmingYears = tdao.getFarmingYears(connection, Collections.singletonList(client.getClientId()));
      FarmingYear farmingYear = farmingYears.get(client.getClientId());

      // Note that crmDao.createAccount also calls crmDao.uploadNppFormToCrm()
      result = crmDao.createAccount(client, farmingYear, submissionGuid, formUserType);
    } else {
      boolean updateAccount = false;
      if (StringUtils.isBlank(crmAccount.getVsi_businessnumber())) {
        crmAccount.setBusinessNumberFromClient(client);
        updateAccount = StringUtils.isNotBlank(crmAccount.getVsi_businessnumber());
      }
      if (StringUtils.isBlank(crmAccount.getVsi_socialinsurancenumber())
          && StringUtils.isNotBlank(client.getSin())) {
        crmAccount.setVsi_socialinsurancenumber(client.getSin());
        updateAccount |= true;
      }
      if (chefsFormTypeCode.equals(ChefsFormTypeCodes.NPP)) {
        crmDao.uploadNppFormToCrm(crmAccount, submissionGuid, formUserType);
      }
      if (chefsFormTypeCode.equals(ChefsFormTypeCodes.CM)) { // Updates Cash Margin flag and date only
        updateAccount = true;
      }
      
      if(updateAccount) {
        result = crmDao.updateAccount(crmAccount);
      }
    }
    return result;
  }

}
