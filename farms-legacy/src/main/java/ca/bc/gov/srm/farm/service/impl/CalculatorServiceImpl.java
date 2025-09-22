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

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.struts.action.ActionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.combined.CombinedBenefitCalculator;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.CalculatorViewDAO;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.dao.ImportDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.dao.ReasonabilityWriteDAO;
import ca.bc.gov.srm.farm.dao.WriteDAO;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.CombinedFarmClient;
import ca.bc.gov.srm.farm.domain.DeductionLineItem;
import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.NewParticipant;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioBpuPurposeCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.TriageQueueCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.enrolment.EnrolmentCalculatorFactory;
import ca.bc.gov.srm.farm.enrolment.EnwEnrolmentCalculator;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.UserService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.domain.CalculatorInboxItem;
import ca.bc.gov.srm.farm.util.CacheUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.OracleUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * 
 * @author awilkinson
 *
 */
public class CalculatorServiceImpl extends BaseService implements CalculatorService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  
  @Override
  public void updateClient(final Client client, String user)
          throws ServiceException {
    
    if (client == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("Client is null");
    }
    
    Scenario scenario = client.getScenario();
    
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    CrmRestApiDao restApiDao = new CrmRestApiDao();
    CrmAccountResource accountResource = restApiDao.getAccountByPin(client.getParticipantPin());

    try {
      transaction = openTransaction();
      transaction.begin();
      
      calcDAO.updateClient(transaction, client, user);
      calcDAO.updatePerson(transaction, client.getOwner(), user);
      calcDAO.updatePerson(transaction, client.getContact(), user);
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Participant",
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);
      
      transaction.commit();

      if (accountResource != null
          && StringUtils.isBlank(accountResource.getVsi_businessnumber())) {
        accountResource.setBusinessNumberFromClient(client);
        // If we got a Business Number from the client, so it's no longer blank
        if(StringUtils.isNotBlank(accountResource.getVsi_businessnumber())) {
          restApiDao.updateAccount(accountResource);
        }
      }
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param farmingYear FarmingYear
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateFarmingYear(
      final FarmingYear farmingYear,
      String user)
  throws ServiceException {

    if (farmingYear == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("FarmingYear is null");
    }
    
    Scenario scenario = farmingYear.getReferenceScenario().getParentScenario();

    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      calcDAO.updateFarmingYear(transaction, farmingYear, user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Farming Year",
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param programYearVersionId programYearVersionId
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean programYearVersionHasVerifiedScenario(final Integer programYearVersionId)
  throws ServiceException {
    
    if (programYearVersionId == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      boolean result = dao.programYearVersionHasVerifiedScenario(transaction, programYearVersionId);
      
      return result;
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error("programYearVersionId: " + programYearVersionId);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param clientId clientId
   * @return schedule
   * @throws ServiceException On Exception
   */
  @Override
  public String getNewOperationSchedule(final Integer clientId)
  throws ServiceException {
    
    if (clientId == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      String schedule = dao.getNewOperationSchedule(transaction, clientId);
      
      return schedule;
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error("clientId: " + clientId);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param fo fo
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void createFarmingOperation(final FarmingOperation fo, final String user)
  throws ServiceException {

    if (fo == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Scenario scenario = fo.getFarmingYear().getReferenceScenario().getParentScenario();

    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createFarmingOperation(transaction, fo, user);
      dao.updateProductionInsurances(transaction, fo, user);
      
      dao.addScenarioLog(transaction,
          scenario,
          "Create Farming Operation " + fo.getSchedule(),
          user);
      
      dao.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("FarmingOperation: ", fo);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param fo fo
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateFarmingOperation(final FarmingOperation fo, final String user)
  throws ServiceException {
    
    if (fo == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Scenario scenario = fo.getFarmingYear().getReferenceScenario().getParentScenario();
    
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      calcDAO.updateFarmingOperation(transaction, fo, user);
      calcDAO.updateProductionInsurances(transaction, fo, user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Farming Operation",
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("FarmingOperation: ", fo);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param fo fo
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void deleteFarmingOperation(
      final FarmingOperation fo,
      final String user)
  throws ServiceException {
    
    if (fo == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer programYearVersionId = fo.getFarmingYear().getProgramYearVersionId();
    Scenario scenario = fo.getFarmingYear().getReferenceScenario().getParentScenario();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.deleteFarmingOperation(transaction,
          fo.getFarmingOperationId(),
          programYearVersionId,
          scenario.getScenarioId(),
          fo.getRevisionCount(),
          user);
      
      dao.addScenarioLog(transaction,
          scenario,
          "Delete Farming Operation",
          user);
      
      dao.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("FarmingOperation: ", fo);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  @Override
  public List<ActionMessage> updateScenario(
      final Scenario scenario,
      final String newStateCode,
      final String stateChangeReason,
      final String newCategoryCode,
      final String userEmail,
      final String chefsFormNotes,
      final String formUserType,
      final String chefsFormType,
      final String fifoResultType,
      final String user)
  throws ServiceException {

    if (newStateCode == null
        || scenario == null
        || user == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    List<ActionMessage> errors = new ArrayList<>();

    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    CalculatorViewDAO viewDao = new CalculatorViewDAO();
    CrmRestApiDao crmDao = new CrmRestApiDao();
    CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();

    try {
      Integer programYear = scenario.getYear();
      String oldStateCode = scenario.getScenarioStateCode();
      String oldCategoryCode = scenario.getScenarioCategoryCode();
      boolean stateChanged = !oldStateCode.equals(newStateCode);
      boolean isVerified = VERIFIED.equals(newStateCode);
      boolean isPreVerified = PRE_VERIFIED.equals(newStateCode);
      boolean isClosed = CLOSED.equals(newStateCode);
      boolean isCompleted = COMPLETED.equals(newStateCode);
      boolean isEnrolmentNoticeComplete = ENROLMENT_NOTICE_COMPLETE.equals(newStateCode);
      boolean reopened = (VERIFIED.equals(oldStateCode) || PRE_VERIFIED.equals(oldStateCode) || CLOSED.equals(oldStateCode))
          && IN_PROGRESS.equals(newStateCode);
      boolean categoryChanged = !oldCategoryCode.equals(newCategoryCode);
      boolean wasUnknownCategory = UNKNOWN.equals(oldCategoryCode);
      boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(newCategoryCode);
      boolean lateParticipant = scenario.isLateParticipant();
      boolean isPreVerification = PRE_VERIFICATION.contentEquals(newCategoryCode);
      boolean isPreVerificationZeroPaymentPass = isPreVerification
          && scenario.getPreVerificationChecklist() != null
          && scenario.getPreVerificationChecklist().getTriageQueue() != null
          && TriageQueueCodes.ZERO_PAYMENT_PASS.equals(scenario.getPreVerificationChecklist().getTriageQueue());
      boolean verifyingLateParticipant = lateParticipant && stateChanged && isVerified && isRealBenefit;
      boolean completingEnrolmentNotice = stateChanged && isEnrolmentNoticeComplete;
      boolean isNoticeOfLoss = NOL.equals(newCategoryCode);
      boolean isCoverageNotice = COVERAGE_NOTICE.equals(newCategoryCode);
      boolean reopenedCoverageNotice = COMPLETED.equals(oldStateCode) && IN_PROGRESS.equals(newStateCode);
      String crmTaskGuid = scenario.getCrmTaskGuid();
      Integer verifierUserId = scenario.getVerifierUserId();
      
      scenario.setScenarioCategoryCode(newCategoryCode);
      scenario.setScenarioStateCode(newStateCode);

      List<CombinedFarmClient> combinedFarmClients = scenario.getCombinedFarmClients();
      List<Integer> combinedFarmUpdatedPins = new ArrayList<>();

      List<Scenario> scenarios = loadFreshScenarios(scenario);
      updateScenarioFields(scenarios, newStateCode, newCategoryCode, verifierUserId);

      // For combined farm, load scenarios and build a list of updated pins.
      if(stateChanged && combinedFarmClients != null) {
        for(CombinedFarmClient client : combinedFarmClients) {
          Integer curPin = client.getParticipantPin();
          combinedFarmUpdatedPins.add(curPin);
        }
      }
      
      
      transaction = openTransaction();
      transaction.begin();
      
      Integer revisionCount = viewDao.getScenarioRevisionCount(transaction, scenario.getScenarioId());
      if(!revisionCount.equals(scenario.getRevisionCount())) {
        throw new InvalidRevisionCountException();
      }
      
      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      connection.setAutoCommit(false);
      
      if (categoryChanged && newCategoryCode.equals(FINAL) && newStateCode.equals(IN_PROGRESS)) {
        
        List<ScenarioMetaData> programYearMetadata = scenario.getScenarioMetaDataList();
        ScenarioMetaData nolScenarioMetadata = ScenarioUtils.findNolScenario(programYearMetadata, programYear);
        
        if (nolScenarioMetadata != null && nolScenarioMetadata.categoryIsOneOf(NOL)
            && nolScenarioMetadata.stateIsOneOf(IN_PROGRESS)) {

          Scenario nolScenario = loadScenario(scenario.getClient().getParticipantPin(), programYear, nolScenarioMetadata.getScenarioNumber());
          List<ActionMessage> nolErrors = updateScenario(nolScenario, COMPLETED, "", NOL, userEmail,
              chefsFormNotes, formUserType, chefsFormType, fifoResultType, user);
          errors.addAll(nolErrors);
        }
      }
      
      // update revision count(s)
      if(!combinedFarmUpdatedPins.isEmpty()) {
        
        dao.updatePinRevisionCounts(
            transaction,
            combinedFarmUpdatedPins,
            programYear,
            false,
            user);
        
        // if reopening, the scenarios are in a Verified state,
        // so they won't be updated by dao.updatePinRevisionCounts()
        if(reopened) {
          for(Scenario curScenario : scenarios) {
            dao.updateScenarioRevisionCount(
                transaction,
                curScenario.getScenarioId(),
                curScenario.getRevisionCount(),
                user);
          }
        }
      } else {
        
        dao.updateScenarioRevisionCount(
            transaction,
            scenario.getScenarioId(),
            scenario.getRevisionCount(),
            user);
      }

      for(Scenario curScenario : scenarios) {

        if(stateChanged && (isVerified || isPreVerified || isEnrolmentNoticeComplete || (isCoverageNotice && isCompleted))) {
          //
          // Save the BPUS before the scenario state changes because
          // right after that the scenario is read again from the DB which affects 
          // the logic for reading the BPUs.
          //
          saveBpuXrefs(transaction, curScenario, user);
        }

        dao.updateScenario(
            transaction,
            curScenario,
            newStateCode,
            stateChangeReason,
            user);
        
        dao.addScenarioLog(transaction,
            curScenario,
            "Update Scenario - State: " + newStateCode + ", Category: " + newCategoryCode,
            user);
      }
      
      boolean generateEnrolment = verifyingLateParticipant || completingEnrolmentNotice;
      if(generateEnrolment) {
        EnrolmentService enrolmentService = ServiceFactory.getEnrolmentService();
        for(Scenario curScenario : scenarios) {
          enrolmentService.processEnrolmentFromScenarioWorkflow(curScenario,
              verifyingLateParticipant, completingEnrolmentNotice, user, transaction);
        }
      }
      
      boolean stateTransferForStateChange = stateChanged && (isVerified || reopened || isClosed || isPreVerified)
          && isRealBenefit && !isPreVerificationZeroPaymentPass;
      boolean stateTransferForCategoryChange = categoryChanged && wasUnknownCategory && isRealBenefit; 
      if(stateTransferForStateChange || stateTransferForCategoryChange) {
        transferState(user, userEmail, scenarios, chefsFormNotes, formUserType, chefsFormType, fifoResultType, transaction);
      }
      
      transaction.commit();
      
      if(isNoticeOfLoss && isCompleted && crmTaskGuid != null) {
        crmDao.completeTask(crmConfig.getNolTaskUrl(), crmTaskGuid);
      }

      if (isCoverageNotice) {
        if (isCompleted) {
          generateCoverageNoticeReport(user, scenarios);
        } else if (reopenedCoverageNotice) {
          deleteBenefitDocuments(user, scenarios);
        }
      } else {
        if (stateChanged && isVerified) {
          generateCobs(user, scenarios);
        } else if (reopened) {
          deleteBenefitDocuments(user, scenarios);
        }
      }
      
      
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("scenarioStateCode: " + newStateCode);
      logger.error("stateChangeReason: " + stateChangeReason);
      logger.error("scenarioCategoryCode: " + newCategoryCode);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return errors;
  }


  /**
   * If the scenario belongs to a Combined Farm then all scenarios
   * of the combined farm will be loaded. Otherwise the list contains
   * only the scenario passed in.
   * 
   * @param scenario scenario
   * @param combinedFarmClients combinedFarmClients
   * @return List<Scenario>
   * @throws ServiceException ServiceException
   */
  private List<Scenario> loadCombinedFarmScenarios(
      final Scenario scenario,
      final List<CombinedFarmClient> combinedFarmClients) throws ServiceException {
    
    List<Scenario> scenarios = new ArrayList<>();
    
    if(combinedFarmClients == null) {
      scenarios.add(scenario);
    } else {
      String value = ConfigurationUtility.getInstance().getValue(ConfigurationKeys.COMBINED_FARM_LOAD_SELF_SCENARIO_FROM_DATABASE);
      boolean combinedFarmLoadSelfScenarioFromDatabase = "Y".equals(value);
      
      for(CombinedFarmClient client : combinedFarmClients) {
        Integer curScenarioId = client.getScenarioId();
        Integer curPin = client.getParticipantPin();
        Integer curScNum = client.getScenarioNumber();
        
        if(curScenarioId.equals(scenario.getScenarioId()) && !combinedFarmLoadSelfScenarioFromDatabase) {
          scenarios.add(scenario);
        } else {
          Scenario curScenario = loadScenario(
              curPin,
              scenario.getYear(),
              curScNum,
              false);
          
          scenarios.add(curScenario);
        }
      }
    }
    
    return scenarios;
  }
  
  
  /**
   * Ensure we have up to date copies of the scenarios in the combined farm.
   * If the scenario belongs to a Combined Farm then all scenarios
   * of the combined farm except the one passed in will be refreshed.
   * 
   * @param scenario scenario
   * @return List<Scenario>
   * @throws ServiceException ServiceException
   */
  private List<Scenario> loadFreshScenarios(
      final Scenario scenario) throws ServiceException {
    
    List<Scenario> refreshedScenarios = new ArrayList<>();
    
    if(scenario.isInCombinedFarm()) {
      for(Scenario curScenario : scenario.getCombinedFarm().getScenarios()) {
        Integer curScenarioId = curScenario.getScenarioId();
        Integer curPin = curScenario.getClient().getParticipantPin();
        Integer curScNum = curScenario.getScenarioNumber();
        
        if(curScenarioId.equals(scenario.getScenarioId())) {
          refreshedScenarios.add(scenario);
        } else {
          Scenario refreshedScenario = loadScenario(
              curPin,
              scenario.getYear(),
              curScNum,
              false);
          
          refreshedScenario.setCombinedFarm(scenario.getCombinedFarm());
          refreshedScenarios.add(refreshedScenario);
        }
      }
    } else {
      refreshedScenarios.add(scenario);
    }
    
    return refreshedScenarios;
  }
  
  
  /**
   * @param scenario scenario
   * @throws ServiceException ServiceException
   */
  private void loadCombinedFarm(
      final Scenario scenario) throws ServiceException {
    List<CombinedFarmClient> combinedFarmClients = scenario.getCombinedFarmClients();
    CombinedBenefitCalculator combinedCalc = CalculatorFactory.getCombinedBenefitCalculator(scenario);
    
    if(scenario.isInCombinedFarm()) {
      CombinedFarm combinedFarm = new CombinedFarm();
      scenario.setCombinedFarm(combinedFarm);
      
      List<Scenario> scenarios = loadCombinedFarmScenarios(scenario, combinedFarmClients);
      combinedFarm.setScenarios(scenarios);
      
      for(Scenario curScenario : scenarios) {
        curScenario.setCombinedFarm(combinedFarm);
      }
      
      //
      // Copy the benefit and margins from the scenario(s) to the combined farm.
      // The current scenario may not have all reference years so check them all.
      // 
      Benefit combinedBenefit = new Benefit();
      combinedFarm.setBenefit(combinedBenefit);
      boolean first = true;
      // need to add these up since they are not stored
      double combinedStandardYearBenefit = 0;
      double combinedEnhancedTotalBenefit = 0;
      double combinedEnhancedAdditionalBenefit = 0;
      double combinedTotalBenefit = 0;

      for(Scenario curScenario : scenarios) {
        Benefit scenarioBenefit = curScenario.getFarmingYear().getBenefit();
        if(scenarioBenefit != null) {
          
          if (scenarioBenefit.getTotalBenefit() != null) {
            double curTotalBenefit = scenarioBenefit.getTotalBenefit();
            combinedTotalBenefit += curTotalBenefit;
          }
          if(CalculatorConfig.hasEnhancedBenefits(scenario.getYear())) {
            if (scenarioBenefit.getStandardBenefit() != null) {
              double curStandardBenefit = scenarioBenefit.getStandardBenefit();
              combinedStandardYearBenefit += curStandardBenefit;
            }
            if (scenarioBenefit.getEnhancedTotalBenefit() != null) {
              double curEnhancedTotalBenefit = scenarioBenefit.getEnhancedTotalBenefit();
              combinedEnhancedTotalBenefit += curEnhancedTotalBenefit;
            }
            if (scenarioBenefit.getEnhancedAdditionalBenefit() != null) {
              double curEnhancedAdditionalBenefit = scenarioBenefit.getEnhancedAdditionalBenefit();
              combinedEnhancedAdditionalBenefit += curEnhancedAdditionalBenefit;
            }
          }
          if(first) {
            first = false;
            combinedCalc.copyBenefit(combinedBenefit, scenarioBenefit);
          }
        }
      }
      combinedBenefit.setTotalBenefit(combinedTotalBenefit);

      if(CalculatorConfig.hasEnhancedBenefits(scenario.getYear())) {
        combinedBenefit.setStandardBenefit(combinedStandardYearBenefit);
        combinedBenefit.setEnhancedTotalBenefit(combinedEnhancedTotalBenefit);
        combinedBenefit.setEnhancedAdditionalBenefit(combinedEnhancedAdditionalBenefit);
      }
      
      Map<Integer, MarginTotal> refYearMargins = new HashMap<>();
      combinedFarm.setYearMargins(refYearMargins);
      
      Map<Integer, Boolean> deemedFarmingYearMap = new HashMap<>();
      combinedFarm.setDeemedFarmingYearMap(deemedFarmingYearMap);
      
      //
      // Copy the MarginTotals and isDeemedFarmingYear to the combined farm.
      // Reference years may not exist for all scenarios so get the first one found
      // since the values should be the same across scenarios.
      //
      List<Integer> years = combinedFarm.getAllYears();
      for(Integer refYear : years) {
        MarginTotal combinedRefYearMargin = new MarginTotal();
        refYearMargins.put(refYear, combinedRefYearMargin);
        boolean isDeemedFarmingYear = false;
        
        for(Scenario curScenario : scenarios) {
          ReferenceScenario rs = curScenario.getReferenceScenarioByYear(refYear);
          if(rs != null) {
            isDeemedFarmingYear = rs.getIsDeemedFarmingYear();
            
            MarginTotal refYearMargin = rs.getFarmingYear().getMarginTotal();
            if(refYearMargin != null) {
              combinedCalc.copyMarginTotal(combinedRefYearMargin, refYearMargin);
              break;
            }
          }
        }
        deemedFarmingYearMap.put(refYear, isDeemedFarmingYear);
      }
    }
    
  }


  private void updateScenarioFields(
      List<Scenario> scenarios,
      String scenarioStateCode,
      String scenarioCategoryCode,
      Integer verfifiedUserId)
      throws Exception {

    String stateCodeDescription;
    switch(scenarioStateCode) {
      case VERIFIED:
        stateCodeDescription = "Verified";
        break;
      case IN_PROGRESS:
        stateCodeDescription = "In Progress";
        break;
      case CLOSED:
        stateCodeDescription = "Closed";
        break;
      case PRE_VERIFIED:
        stateCodeDescription = "Pre-Verified";
        break;
      default:
        stateCodeDescription = scenarioStateCode;
    }
    
    UserService userService = ServiceFactory.getUserService();
    FarmUser user = userService.getUserByUserId(verfifiedUserId);
    String verifierEmail = null;
    if(user != null) {
      verifierEmail = user.getEmailAddress();
    }
    
    for(Scenario curScenario : scenarios) {
      // Update the state so that it will be correct for the transfer event.
      curScenario.setScenarioStateCode(scenarioStateCode);
      // The state code description is included in the transfer event description
      curScenario.setScenarioStateCodeDescription(stateCodeDescription);
      
      // Update the category so that it will be correct for the transfer event.
      curScenario.setScenarioCategoryCode(scenarioCategoryCode);
      curScenario.setScenarioCategoryCodeDescription(getScenarioCategoryDescription(scenarioCategoryCode));
      
      curScenario.setVerifiedByEmail(verifierEmail);
    }
  }


  private void transferState(
      String user,
      String userEmail,
      List<Scenario> scenarios,
      String chefsFormNotes,
      String formUserType,
      String chefsFormType,
      String fifoResultType,
      Transaction transaction)
      throws Exception {
    CrmTransferService transferService = ServiceFactory.getCrmTransferService();
    
    for(Scenario curScenario : scenarios) {
      transferService.scheduleBenefitTransfer(curScenario, userEmail, user, chefsFormNotes, formUserType, chefsFormType, fifoResultType, transaction);
    }
  }
  
  private String getScenarioCategoryDescription(String scenarioCategoryCode) {
    ListView[] categoriesArray = ServiceFactory.getListService().getListArray(ListService.SCENARIO_CATEGORY);

    for (ListView categoryItem : categoriesArray) {
      if (categoryItem.getValue().equalsIgnoreCase(scenarioCategoryCode)) {
        return categoryItem.getLabel();
      }
    }
    logger.debug("scenarioCategoryCode is not found: " + scenarioCategoryCode);
    return "Not Found";
  }

  /**
   * @param user user
   * @param scenarios scenarios
   * @throws Exception Exception
   */
  private void generateCoverageNoticeReport(final String user, List<Scenario> scenarios) throws Exception {
    CdogsService cdogsService = ServiceFactory.getCdogsService();

    for(Scenario scenario : scenarios) {
      cdogsService.createCdogsCoverageNoticeReport(scenario, user);
      scenario.setBenefitDocCreatedDate(new java.util.Date());
  
      addScenarioLog(scenario, "Print Coverage Notice Report", user);
    }
  }
  
  /**
   * @param user user
   * @param scenarios scenarios
   * @throws Exception Exception
   */
  private void generateCobs(final String user, List<Scenario> scenarios)
      throws Exception {
    ReportService service = ServiceFactory.getReportService();
    
    String generateCobEnabled = System.getProperty("generate.cob.enabled");
    boolean generateCobReports = ! "N".equals(generateCobEnabled);

    if(generateCobReports) {
      for(Scenario curScenario : scenarios) {
        //
        // Automatically generate a new COB
        // It is important to do this after the state change because the
        // report SQL depends on the scenario being in the "COMP" state.
        //
        if(curScenario.getBenefitDocCreatedDate() == null) {
          service.saveCob(curScenario.getScenarioId(), curScenario.getYear(), user);
        } else {
          service.updateCob(curScenario.getScenarioId(), curScenario.getYear(), user);
        }
  
        curScenario.setBenefitDocCreatedDate(new java.util.Date());
  
        addScenarioLog(curScenario, "Print COB", user);
      }
    }
  }

  /**
   * @param scenarios scenarios
   * @throws Exception Exception
   */
  private void deleteBenefitDocuments(final String user, List<Scenario> scenarios)
      throws Exception {
    logger.debug("<deleteBenefitDocuments");

    ReportService service = ServiceFactory.getReportService();
    
    for(Scenario curScenario : scenarios) {
      service.deleteBenefitDocument(curScenario.getScenarioId());
      
      curScenario.setBenefitDocCreatedDate(null);
      
      addScenarioLog(curScenario, "Delete Benefit document", user);
    }

    logger.debug("<deleteBenefitDocuments");
  }
  

  /**
   * @param scenario scenario
   * @param userGuid userGuid
   * @param user user
   * @throws ServiceException On Exception 
   */
  @Override
  public void assignToUser(
      final Scenario scenario,
      final String userGuid,
      final String user)
  throws ServiceException {
    
    if (scenario == null
        || user == null
        || userGuid == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      List<Scenario> scenarios = getScenariosForAssignTo(transaction, scenario);

      for(Scenario curScenario : scenarios) {
        // updates the revision counts of all scenarios for the year
        // so we don't need to call updateScenarioRevisionCount()
        dao.assignToUser(
            transaction,
            curScenario.getScenarioId(),
            curScenario.getRevisionCount(),
            userGuid,
            user);
        
        dao.addScenarioLog(transaction,
            curScenario,
            "Check Out",
            user);
      }
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("userGuid: " + userGuid);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param transaction transaction
   * @param scenario scenario
   * @return List<Scenario>
   * @throws SQLException SQLException
   * @throws ServiceException ServiceException
   */
  private List<Scenario> getScenariosForAssignTo(final Transaction transaction,
      final Scenario scenario) throws SQLException, ServiceException {
    CalculatorViewDAO dao = new CalculatorViewDAO();
    
    List<CombinedFarmClient> combinedFarmClients = scenario.getCombinedFarmClients();
    if(combinedFarmClients == null) {
      Integer combinedFarmNumber = dao.getInProgressCombinedFarmNumber(
          transaction,
          scenario.getClient().getParticipantPin(),
          scenario.getYear());
      
      if(combinedFarmNumber != null) {
        @SuppressWarnings("resource")
        Connection connection = (Connection) transaction.getDatastore();
        ReadDAO readDao = new ReadDAO(connection);
        combinedFarmClients = readDao.readCombinedFarmClients(combinedFarmNumber);
      }
    }
    
    List<Scenario> scenarios = loadCombinedFarmScenarios(scenario, combinedFarmClients);
    
    return scenarios;
  }
  
  
  @Override
  public Integer saveScenarioAsNew(
      Integer scenarioId,
      String scenarioTypeCode,
      String scenarioCategoryCode,
      Integer programYear,
      String user)
  throws ServiceException {
    
    if (scenarioId == null
        || user == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer scenarioNumber = null;

    String appVerKey = ConfigurationKeys.APPLICATION_VERSION;
    String applicationVersion = ConfigurationUtility.getInstance().getValue(appVerKey);
    
    try {
      transaction = openTransaction();
      transaction.begin();

      scenarioNumber = dao.saveScenarioAsNew(
          transaction,
          scenarioId,
          scenarioTypeCode,
          scenarioCategoryCode,
          applicationVersion,
          user);
      
      
      // Just in case the config data was not created when
      // the program year data was created for this participant.
      Integer rowsCreated = dao.copyForwardYearConfig(transaction, programYear, user);
      
      transaction.commit();
      
      if(rowsCreated > 0) {
        CacheUtils.refreshYearConfigLists();
      }
      
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error("scenarioId: " + scenarioId);
      logger.error("user: " + user);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return scenarioNumber;
  }
  
  
  @Override
  public void createReferenceScenario(
      final Scenario forScenario,
      final Integer fromScenarioId,
      String scenarioCategoryCode,
      final String user)
  throws ServiceException {
    
    if (forScenario == null || fromScenarioId == null
        || user == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();

    String appVerKey = ConfigurationKeys.APPLICATION_VERSION;
    String applicationVersion = ConfigurationUtility.getInstance().getValue(appVerKey);
    
    try {
      transaction = openTransaction();
      transaction.begin();

      dao.createReferenceScenario(
          transaction,
          forScenario,
          fromScenarioId,
          scenarioCategoryCode,
          applicationVersion,
          user);
      
      dao.updateScenarioRevisionCount(
          transaction,
          forScenario.getScenarioId(),
          forScenario.getRevisionCount(),
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(forScenario));
      logger.error("fromScenarioId: " + fromScenarioId);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param searchType String
   * @param year Integer
   * @param userGuid String
   * @param scenarioStateCodes List
   * @return List
   * @throws ServiceException On Exception
   */
  @Override
  public List<CalculatorInboxItem> readInboxItems(
      final String searchType,
      final Integer year,
      final String userGuid,
      final List<String> scenarioStateCodes)
  throws ServiceException {
    
    List<CalculatorInboxItem> inboxItems = null;

    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();

    try {
      transaction = openTransaction();

      inboxItems = dao.readInboxItems(transaction, searchType, year, userGuid, scenarioStateCodes);

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return inboxItems;
  }


  /**
   * @param pin int
   * @param programYear int
   * @param scenarioNumber Integer
   * @return Scenario
   * @throws ServiceException On Exception
   */
  @Override
  public Scenario loadScenario(
      final int pin,
      final int programYear,
      final Integer scenarioNumber)
  throws ServiceException {
    return loadScenario(pin, programYear, scenarioNumber, true);
  }


  /**
   * @param pin int
   * @param programYear int
   * @param scenarioNumber Integer
   * @param loadCombinedFarm boolean
   * @return Scenario
   * @throws ServiceException On Exception
   */
  private Scenario loadScenario(
      final int pin,
      final int programYear,
      final Integer scenarioNumber,
      final boolean loadCombinedFarm)
  throws ServiceException {
    
    Scenario scenario = null;
    Transaction transaction = null;

    try {
      transaction = openTransaction();
      
      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      
      ClientService service = ClientServiceFactory.getInstance(connection);
      
      scenario = service.getClientInfoWithHistory(pin, programYear, scenarioNumber,
          ClientService.DEF_FIRST_MODE);
    } finally {
      closeTransaction(transaction);
    }
    
    if(loadCombinedFarm) {
      loadCombinedFarm(scenario);
    }
    
    return scenario;
  }
  
  
  /**
   * @param pin int
   * @param programYear int
   * @param scenarioNumber Integer
   * @return Scenario
   * @throws ServiceException On Exception
   */
  @Override
  public Scenario loadScenarioWithoutHistory(
      final int pin,
      final int programYear,
      final Integer scenarioNumber)
  throws ServiceException {
    
    Scenario scenario = null;
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();
      
      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();
      
      ClientService service = ClientServiceFactory.getInstance(connection);
      
      scenario = service.getClientInfoWithoutHistory(pin, programYear, scenarioNumber,
          ClientService.DEF_FIRST_MODE);
    } finally {
      closeTransaction(transaction);
    }
    
    return scenario;
  }


  @Override
  public Integer createYear(
      final Scenario scenario,
      final Integer pin,
      final Integer programYearToCreate,
      final Integer numOperations,
      final String scenarioClassCode,
      String scenarioCategoryCode, final String user) 
  throws ServiceException {
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer programYearVersionId = null;

    try {
      transaction = openTransaction();
      transaction.begin();

      programYearVersionId = dao.createYear(transaction, pin, programYearToCreate, numOperations, scenarioClassCode, scenarioCategoryCode, user);
      
      if( !scenario.getYear().equals(programYearToCreate) ) {
        String logMessage = "Create Missing Year: " + programYearToCreate;
        addScenarioLog(scenario, logMessage, user);
      }
      
      if(ScenarioTypeCodes.USER.equals(scenario.getScenarioTypeCode())) {
        dao.updateScenarioRevisionCount(
            transaction,
            scenario.getScenarioId(),
            scenario.getRevisionCount(),
            user);
      }
      
      Integer rowsCreated = dao.copyForwardYearConfig(transaction, programYearToCreate, user);

      transaction.commit();
      
      if(rowsCreated > 0) {
        CacheUtils.refreshYearConfigLists();
      }

    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("programYearToCreate: " + programYearToCreate);
      logger.error("numOperations: " + numOperations);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return programYearVersionId;
  }
  
  
  /**
   * @param scenario Scenario
   * @param updateYear the year to update
   * @param newPyvNumber the new program year version number to update the scenario to
   * @param pyvKeepOldData if true, then copy the data from the old PYV to the new PYV
   * @param opNumsKeepOldData if an operation number is in the list,
   *                          then copy the data from the old operation to the new operation
   * @param user String
   * @return Integer program year version
   * @throws ServiceException On Exception
   */
  @Override
  public Integer updateScenarioPyVersion(
      final Scenario scenario,
      final Integer updateYear,
      final Integer newPyvNumber,
      final Boolean pyvKeepOldData,
      final List<Integer> opNumsKeepOldData,
      final String user) 
  throws ServiceException {
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer newScenarioNumber = null;
    
    ReferenceScenario refScenario = scenario.getReferenceScenarioByYear(updateYear);

    StringBuffer logMessage = new StringBuffer();
    logMessage.append("Update Program Year Version Number for ");
    logMessage.append(updateYear);
    logMessage.append(" from ");
    logMessage.append(refScenario.getFarmingYear().getProgramYearVersionNumber());
    logMessage.append(" to ");
    logMessage.append(newPyvNumber);
    
    try {
      transaction = openTransaction();
      transaction.begin();

      newScenarioNumber = dao.updateScenarioPyVersion(transaction,
          refScenario.getScenarioId(),
          newPyvNumber,
          pyvKeepOldData,
          opNumsKeepOldData,
          user);
      
      dao.addScenarioLog(transaction,
          scenario,
          logMessage.toString(),
          user);
      
      dao.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("updateYear: " + updateYear);
      logger.error("newPyvNumber: " + newPyvNumber);
      logger.error("pyvKeepOldData: " + pyvKeepOldData);
      logger.error("opNumsKeepOldData: " + opNumsKeepOldData);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

    return newScenarioNumber;
  }


  /**
   * @param scenario scenario
   * @param farmingOperations List<FarmingOperation>
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateOperationAlignment(
      final Scenario scenario,
      final List<FarmingOperation> farmingOperations,
      final String user)
  throws ServiceException {

    if (farmingOperations == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      calcDAO.updateOperationAlignment(transaction, farmingOperations, user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Operation Alignment",
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  @Override
  public void saveFinalVerificationNotes(
      final Scenario scenario,
  		final String verificationNotes, 
  		final Integer programYearId,
  		final String user)
  throws ServiceException {
  	Transaction transaction = null;
    WriteDAO writeDAO = new WriteDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      
      transaction.begin();

      writeDAO.writeFinalVerificationNotes(
      		transaction,
      		verificationNotes,
      		programYearId,
      		user
      );
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Final Verification Notes",
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  @Override
  public void saveInterimVerificationNotes(
      final Scenario scenario,
      final String verificationNotes, 
      final Integer programYearId,
      final String user)
  throws ServiceException {
    Transaction transaction = null;
    WriteDAO writeDAO = new WriteDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      
      transaction.begin();

      writeDAO.writeInterimVerificationNotes(
          transaction,
          verificationNotes,
          programYearId,
          user
      );
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Interim Verification Notes",
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  
  @Override
  public void saveAdjustmentVerificationNotes(
      final Scenario scenario,
      final String verificationNotes,
      final Integer programYearId,
      final String user)
  throws ServiceException {
    Transaction transaction = null;
    WriteDAO writeDAO = new WriteDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      
      transaction.begin();

      writeDAO.writeAdjustmentVerificationNotes(
          transaction,
          verificationNotes,
          programYearId,
          user
      );
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Adjustment Verification Notes",
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  /**
   * @param scenarioId Integer
   * @throws ServiceException On Exception
   * @return revision count
   */
  @Override
  public Integer getScenarioRevisionCount(final Integer scenarioId)
  throws ServiceException {
    
    Integer revisionCount = null;

    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();

    try {
      transaction = openTransaction();

      revisionCount = dao.getScenarioRevisionCount(transaction, scenarioId);

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return revisionCount;
  }


  /**
   * @param scenario scenario
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateScenarioRevisionCount(
      final Scenario scenario,
      final String user)
  throws ServiceException {

    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      List<Scenario> scenarios;
      if(scenario.isInCombinedFarm()) {
        scenarios = scenario.getCombinedFarm().getScenarios();
      } else {
        scenarios = new ArrayList<>();
        scenarios.add(scenario);
      }
      
      for(Scenario curScenario : scenarios) {

        dao.updateScenarioRevisionCount(transaction,
            curScenario.getScenarioId(),
            curScenario.getRevisionCount(),
            user);
        
        int newCount = curScenario.getRevisionCount() + 1;
        curScenario.setRevisionCount(newCount);
      }

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param scenario scenario
   * @param logMessage logMessage
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void addScenarioLog(
      final Scenario scenario,
      final String logMessage,
      final String user)
  throws ServiceException {
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      dao.addScenarioLog(transaction,
          scenario,
          logMessage,
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error("logMessage: " + logMessage);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  
  
  /**
   * @param transaction transaction
   * @param scenario scenario
   * @param user user
   * @throws ServiceException On Exception
   */
  private void saveBpuXrefs(
      final Transaction transaction,
      final Scenario scenario,
      final String user)
  throws ServiceException {
  	logger.debug("saveBpuXrefs");
  	
    CalculatorDAO dao = new CalculatorDAO();
    
    Integer scenarioId = scenario.getScenarioId();
    Set<Integer> bpusIdSet = new TreeSet<>();
    
    addBpuIdsToSet(bpusIdSet, scenario.getInventoryBpus());
    addBpuIdsToSet(bpusIdSet, scenario.getStructureGroupBpus());
    addBpuIdsToSet(bpusIdSet, scenario.getEarlierProgramYearInventoryBpus());
    addBpuIdsToSet(bpusIdSet, scenario.getEarlierProgramYearStructureGroupBpus());
    
    List<Integer> bpuIds = new ArrayList<>();
    bpuIds.addAll(bpusIdSet);
    
    dao.deleteBpuXrefs(transaction, scenarioId);
    dao.saveBpuXrefs(transaction, scenarioId, bpuIds, ScenarioBpuPurposeCodes.STANDARD, user);
  }


  private void addBpuIdsToSet(Set<Integer> bpusIdSet, Map<String, BasePricePerUnit> bpus) {
    if(bpus != null) {
      List<Integer> bpuIds = bpus.values().stream()
          .map(b -> b.getBasePricePerUnitId())
          .collect(Collectors.toList());
      bpusIdSet.addAll(bpuIds);
    }
  }


  /**
   * @param programYear Integer
   * @param deductionType String
   * @return List<DeductionLineItem>
   * @throws ServiceException On Exception
   */
  @Override
  public List<DeductionLineItem> getDeductionLineItems(
      final Integer programYear,
      final String deductionType)
  throws ServiceException {
    
    List<DeductionLineItem> deductionLineItems = null;

    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();

    try {
      transaction = openTransaction();

      deductionLineItems = dao.getDeductionLineItems(transaction, programYear, deductionType);

    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return deductionLineItems;
  }
  
  
  /**
   * @param participantPin Integer
   * @param combinedFarmNumber Integer
   * @return Map<pin, List<scenarioNumber>>
   * @throws ServiceException On Exception
   */
  @Override
  public Map<Integer, List<Integer>> getCombinedFarmInProgressScenarioNumbers(
      final Integer participantPin,
      final Integer combinedFarmNumber)
          throws ServiceException {
    
    Map<Integer, List<Integer>> scNumMap = null;
    
    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();
    
    try {
      transaction = openTransaction();
      
      scNumMap = dao.getCombinedFarmInProgressScenarioNumbers(
          transaction,
          participantPin,
          combinedFarmNumber);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return scNumMap;
  }
  
  
  /**
   * @param pin Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean pinExists(
      final Integer pin)
          throws ServiceException {
    
    boolean exists;
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      exists = dao.pinExists(transaction, pin);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return exists;
  }
  
  
  /**
   * @param pin Integer
   * @param userGuid String
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean pinCheckedOutByUser(
      final Integer pin,
      final String userGuid)
          throws ServiceException {
    
    boolean result;
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      result = dao.pinCheckedOutByUser(transaction, pin, userGuid);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  /**
   * @param pin Integer
   * @param programYear Integer
   * @param municipalityCode municipalityCode
   * @param scenarioCategoryCode scenarioCategoryCode
   * @return true if the target PIN has an In Progress scenario with matching parameters,
   *         for the purpose adding it to a Combined Farm.
   * @throws ServiceException On Exception
   */
  @Override
  public boolean matchingScenarioExists(
      final Integer pin,
      final Integer programYear,
      final String municipalityCode,
      final String scenarioCategoryCode)
          throws ServiceException {
    
    boolean exists;
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      exists = dao.matchingScenarioExists(transaction, pin, programYear, municipalityCode, scenarioCategoryCode);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return exists;
  }
  
  
  /**
   * @param pinToAdd Integer
   * @param programYear Integer
   * @param scenarioId Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean combinedFarmHasAccountingCodeError(
      final Integer pinToAdd,
      final Integer programYear,
      final Integer scenarioId)
          throws ServiceException {
    
    boolean result;
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      result = dao.combinedFarmHasAccountingCodeError(transaction, pinToAdd, programYear, scenarioId);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  /**
   * @param pinToAdd Integer
   * @param programYear Integer
   * @param scenarioId Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean combinedFarmHasReferenceYearMismatchError(
      final Integer pinToAdd,
      final Integer programYear,
      final Integer scenarioId)
          throws ServiceException {
    
    boolean result;
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      result = dao.combinedFarmReferenceYearSetMismatchError(transaction, pinToAdd, programYear, scenarioId);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }
  
  
  /**
   * @param pin Integer
   * @param programYear Integer
   * @return Integer
   * @throws ServiceException On Exception
   */
  @Override
  public Integer getInProgressCombinedFarmNumber(
      final Integer pin,
      final Integer programYear)
          throws ServiceException {
    
    Integer combinedFarmNumber;
    
    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();
    
    try {
      transaction = openTransaction();
      
      combinedFarmNumber = dao.getInProgressCombinedFarmNumber(transaction, pin, programYear);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return combinedFarmNumber;
  }
  
  
  /**
   * @param pin Integer
   * @param programYear Integer
   * @return Integer
   * @throws ServiceException On Exception
   */
  @Override
  public Integer getVerifiedCombinedFarmNumber(
      final Integer pin,
      final Integer programYear)
          throws ServiceException {
    
    Integer combinedFarmNumber;
    
    Transaction transaction = null;
    CalculatorViewDAO dao = new CalculatorViewDAO();
    
    try {
      transaction = openTransaction();
      
      combinedFarmNumber = dao.getVerifiedCombinedFarmNumber(transaction, pin, programYear);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return combinedFarmNumber;
  }
  
  
  /**
   * @param scenarioId Integer
   * @param verifiedCombinedFarmNumber Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  @Override
  public boolean combinedFarmMatchesVerified(
      final Integer scenarioId,
      final Integer verifiedCombinedFarmNumber)
          throws ServiceException {
    
    boolean result;
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      result = dao.combinedFarmMatchesVerified(transaction, scenarioId, verifiedCombinedFarmNumber);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return result;
  }


  /**
   * @param scenario scenario
   * @param pinScenarioNumberUpdateMap pinScenarioNumberUpdateMap
   * @param updatedPins updatedPins
   * @param user user
   * @throws ServiceException On Exception 
   */
  @Override
  public void updateCombinedFarmScenarioNumbers(
      final Scenario scenario,
      final Map<Integer, Integer> pinScenarioNumberUpdateMap,
      final List<Integer> updatedPins,
      final String user)
  throws ServiceException {

    if (pinScenarioNumberUpdateMap == null
        || updatedPins == null
        || user == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer programYear = scenario.getYear();

    try {
      transaction = openTransaction();
      transaction.begin();

      updateCombinedFarmScenarioNumbers(
          transaction,
          pinScenarioNumberUpdateMap,
          scenario.getCombinedFarmNumber(),
          programYear,
          user);
      
      dao.updatePinRevisionCounts(
          transaction,
          updatedPins,
          programYear,
          true,
          user);
      
      flagReasonabilityTestsStale(scenario, user, transaction);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param scenario scenario
   * @param pinToAdd pinToAdd
   * @param pinScenarioNumberUpdateMap pinScenarioNumberUpdateMap
   * @param updatedPins updatedPins
   * @param user user
   * @throws ServiceException On Exception 
   */
  @Override
  public void addToCombinedFarm(
      final Scenario scenario,
      final Integer pinToAdd,
      final Map<Integer, Integer> pinScenarioNumberUpdateMap,
      final List<Integer> updatedPins,
      final String user)
          throws ServiceException {
    
    if (pinScenarioNumberUpdateMap == null
        || updatedPins == null
        || user == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer programYear = scenario.getYear();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      updateCombinedFarmScenarioNumbers(
          transaction,
          pinScenarioNumberUpdateMap,
          scenario.getCombinedFarmNumber(),
          programYear,
          user);
      
      dao.addToCombinedFarm(
          transaction,
          scenario.getScenarioId(),
          pinToAdd,
          programYear,
          user);
      
      dao.updatePinRevisionCounts(
          transaction,
          updatedPins,
          programYear,
          true,
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  /**
   * @param scenario scenario
   * @param scenarioIdToRemove scenarioIdToRemove
   * @param pinScenarioNumberUpdateMap pinScenarioNumberUpdateMap
   * @param updatedPins updatedPins
   * @param user user
   * @throws ServiceException On Exception 
   */
  @Override
  public void removeFromCombinedFarm(
      final Scenario scenario,
      final Integer scenarioIdToRemove,
      final Map<Integer, Integer> pinScenarioNumberUpdateMap,
      final List<Integer> updatedPins,
      final String user)
          throws ServiceException {
    
    if (pinScenarioNumberUpdateMap == null
        || updatedPins == null
        || user == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer programYear = scenario.getYear();
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      updateCombinedFarmScenarioNumbers(
          transaction,
          pinScenarioNumberUpdateMap,
          scenario.getCombinedFarmNumber(),
          programYear,
          user);
      
      dao.removeFromCombinedFarm(
          transaction, scenarioIdToRemove,
          scenario.getCombinedFarmNumber(),
          user);
      
      dao.updatePinRevisionCounts(
          transaction,
          updatedPins,
          programYear,
          true,
          user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param transaction transaction
   * @param pinScenarioNumberMap pinScenarioNumberMap
   * @param combinedFarmNumber combinedFarmNumber
   * @param programYear programYear
   * @param user user
   * @throws DataAccessException DataAccessException
   */
  private void updateCombinedFarmScenarioNumbers(
      final Transaction transaction,
      final Map<Integer, Integer> pinScenarioNumberMap,
      final Integer combinedFarmNumber,
      final Integer programYear,
      final String user)
      throws DataAccessException {
    
    CalculatorDAO dao = new CalculatorDAO();
    
    for(Iterator<?> pi = pinScenarioNumberMap.keySet().iterator(); pi.hasNext(); ) {
      Integer curPin = (Integer) pi.next();
      Integer scenarioNumber = pinScenarioNumberMap.get(curPin);
      dao.updateCombinedFarmScenario(
          transaction,
          curPin,
          programYear,
          combinedFarmNumber,
          scenarioNumber,
          user);
    }
  }
  
  
  /**
   * @param scenario scenario
   * @param scenarioRevisionCount scenarioRevisionCount
   * @param nonParticipantInd nonParticipantInd
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void saveNonParticipantInd(
      final Scenario scenario,
      final Integer scenarioRevisionCount,
      final Boolean nonParticipantInd,
      final String user)
  throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      
      transaction.begin();

      calcDAO.writeNonParticipantInd(
          transaction,
          scenario.getScenarioId(),
          scenarioRevisionCount,
          nonParticipantInd,
          user
      );
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Non-Participant to " + nonParticipantInd,
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  /**
   * @param scenario scenario
   * @param scenarioRevisionCount scenarioRevisionCount
   * @param farmType farmType
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void saveFarmType(
      final Scenario scenario,
      final String farmType,
      final String user)
  throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      
      transaction.begin();

      calcDAO.updateFarmType(
          transaction,
          scenario,
          farmType,
          user
      );

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  /**
   * @param scenario scenario
   * @param scenarioRevisionCount scenarioRevisionCount
   * @param cashMarginsInd cashMarginsInd
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void saveCashMarginsInd(
      final Scenario scenario,
      final Boolean cashMarginsInd,
      final Date cashMarginsOptInDate,
      final String user)
  throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();

    try {
      transaction = openTransaction();
      
      transaction.begin();

      calcDAO.writeCashMarginsInd(
          transaction,
          scenario.getScenarioId(),
          cashMarginsInd,
          cashMarginsOptInDate,
          user
      );
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  /**
   * @param scenario scenario
   * @param scenarioRevisionCount scenarioRevisionCount
   * @param lateParticipantInd lateParticipantInd
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void saveLateParticipantInd(
      final Scenario scenario,
      final Integer scenarioRevisionCount,
      final Boolean lateParticipantInd,
      final String user)
          throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    ImportDAO dao = new ImportDAO();
    
    try {
      transaction = openTransaction();
      
      transaction.begin();
      
      calcDAO.writeLateParticipantInd(
          transaction,
          scenario.getScenarioId(),
          scenarioRevisionCount,
          lateParticipantInd,
          user
          );
      
      @SuppressWarnings("resource")
      Connection connection = (Connection) transaction.getDatastore();

      Integer participantPin = scenario.getClient().getParticipantPin();
      Integer programYear = scenario.getYear();
      String transferDescription = String.format("Enrolment transfer for %d PIN: %d", participantPin, programYear);
      String fileName = "none";

      ImportVersion importVersion = new ImportVersion();
      importVersion.setImportClassCode(ImportClassCodes.XENROL);
      importVersion.setImportStateCode(ImportStateCodes.IMPORT_COMPLETE);
      importVersion.setDescription(transferDescription);
      importVersion.setImportedByUser(user);
      importVersion.setImportFileName(fileName);

      dao.insertImportVersion(connection , importVersion);
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Late Participant to " + lateParticipantInd,
          user);
      
      Enrolment enrolment = new Enrolment();
      enrolment.setPin(participantPin);
      enrolment.setProducerName(scenario.getClient().getOwner().getFullName());
      enrolment.setEnrolmentYear(programYear);
      enrolment.setGeneratedDate(new Date());
      enrolment.setIsLateParticipant(lateParticipantInd);
      
      if(lateParticipantInd) {
        enrolment.setEnrolmentFee(CalculatorConfig.LATE_ENROLMENT_FEE);
      } else {
        enrolment.setEnrolmentFee(0d);
      }
      
      Integer importVersionId = importVersion.getImportVersionId();
      
      CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
      crmTransferService.postEnrolment(enrolment, importVersionId, user);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  @Override
  public void updateProgramYearLocalReceivedDates(
      final Scenario scenario,
      final Date localStatementAReceivedDate,
      final Date localSupplementalReceivedDate,
      final String user,
      final String email,
      final String chefsFormNotes,
      final String formUserType, 
      final String chefsFormType,
      final String fifoResultType)
          throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      transaction.begin();
      
      calcDAO.updateProgramYearLocalReceivedDates(
          transaction,
          scenario,
          localStatementAReceivedDate,
          localSupplementalReceivedDate,
          user
          );

      transaction.commit();
      
      Integer participantPin = scenario.getClient().getParticipantPin();
      Integer programYear = scenario.getYear();
      Integer scenarioNumberForBenefitUpdate = scenario.getScenarioNumber();
     
      Scenario scenarioForBenefitUpdate = loadScenario(participantPin, programYear, scenarioNumberForBenefitUpdate);
      if (scenario.getChefsSubmissionGuid() != null) {
        scenarioForBenefitUpdate.setChefsSubmissionGuid(scenario.getChefsSubmissionGuid());
      }
      
      CrmTransferService transferService = ServiceFactory.getCrmTransferService();
      try {
        transferService.scheduleBenefitTransfer(scenarioForBenefitUpdate, email, user, chefsFormNotes, formUserType, chefsFormType,
            fifoResultType, transaction);
      } catch (Exception e) {
        throw new ServiceException(e);
      }
      
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

  }
  
  @Override
  public void updateProgramYearLocalReceivedDates(
      final Scenario scenario,
      final Date localStatementAReceivedDate,
      final Date localSupplementalReceivedDate,
      final String user,
      final String email)
          throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      
      transaction.begin();
      
      calcDAO.updateProgramYearLocalReceivedDates(
          transaction,
          scenario,
          localStatementAReceivedDate,
          localSupplementalReceivedDate,
          user
          );

      transaction.commit();
      
      Integer participantPin = scenario.getClient().getParticipantPin();
      Integer programYear = scenario.getYear();
      List<ScenarioMetaData> scenarioMetaDataList = scenario.getScenarioMetaDataList();
      
      ScenarioMetaData latestRealBenefitScenarioMetaData = scenarioMetaDataList.stream()
          .filter(y -> y.getProgramYear().equals(programYear)
              && ScenarioUtils.categoryIsRealBenefit(y.getScenarioCategoryCode())
              && y.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)
              && y.stateIsOneOf(VERIFIED, AMENDED, IN_PROGRESS)
              
              // Don't send a Verified Interim because the benefit in CRM would be
              // Processing Complete, Complete, or Closed
              // so the benefit update would be ignored.
              // Instead use the latestBaseScenarioMetaData below.
              && ! (y.stateIsOneOf(VERIFIED) && y.categoryIsOneOf(INTERIM)) 
              )
          .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
          .orElse(null);
      ScenarioMetaData latestBaseScenarioMetaData = scenarioMetaDataList.stream()
          .filter(y -> y.getProgramYear().equals(programYear)
              && y.typeIsOneOf(ScenarioTypeCodes.CRA, ScenarioTypeCodes.CHEF, ScenarioTypeCodes.GEN, ScenarioTypeCodes.LOCAL))
          .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
          .orElse(null);
      
      Integer scenarioNumberForBenefitUpdate = null;
      if(latestRealBenefitScenarioMetaData != null) {
        scenarioNumberForBenefitUpdate = latestRealBenefitScenarioMetaData.getScenarioNumber();
      } else if(latestBaseScenarioMetaData != null) {
        scenarioNumberForBenefitUpdate = latestBaseScenarioMetaData.getScenarioNumber();
      }
      
      if(scenarioNumberForBenefitUpdate != null) {
        
        Scenario scenarioForBenefitUpdate = loadScenario(participantPin, programYear, scenarioNumberForBenefitUpdate);
        
        CrmTransferService transferService = ServiceFactory.getCrmTransferService();
        try {
          transferService.scheduleBenefitTransfer(scenarioForBenefitUpdate, email, user, transaction);
        } catch (Exception e) {
          logger.error("Unexpected error: ", e);
          throw new ServiceException(e);
        }
      }
      
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }

  }
  
  @Override
  public void updateReasonabilityTests(
      final Scenario scenario,
      final ReasonabilityTestResults results,
      final String user)
          throws ServiceException {
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      List<Scenario> scenarios = loadFreshScenarios(scenario);

      transaction = openTransaction();
      
      transaction.begin();

      for(Scenario curScenario : scenarios) {
        
        ReasonabilityTestResults curResults;
        
        if(curScenario.getScenarioId().equals(scenario.getScenarioId())) {
          curResults = results;
        } else {
          curResults = curScenario.getReasonabilityTestResults();
          if(curResults == null) {
            curResults = new ReasonabilityTestResults();
            curScenario.setReasonabilityTestResults(curResults);
          }
          curResults.copy(results);
        }
        curResults.setScenario(curScenario);
      
        reasonabilityDAO.updateReasonabilityTests(transaction,
            curResults,
            user);
        
        Map<String, String> parameters = ReasonabilityConfiguration.getInstance().getParameters();
        calcDAO.upsertScenarioParameters(transaction, curScenario.getScenarioId(), parameters, user);
        
        calcDAO.addScenarioLog(transaction,
            curScenario,
            "Run Reasonability Tests",
            user);
        
        calcDAO.updateScenarioRevisionCount(
            transaction,
            curScenario.getScenarioId(),
            curScenario.getRevisionCount(),
            user);
      }
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  @Override
  public void flagReasonabilityTestsStale(
      final Scenario scenario,
      final String user)
          throws ServiceException {
    Transaction transaction = null;
    
    try {

      transaction = openTransaction();
      
      transaction.begin();

      flagReasonabilityTestsStale(scenario, user, transaction);
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  private void flagReasonabilityTestsStale(
      Scenario scenario,
      String user,
      Transaction transaction)
          throws ServiceException {
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      List<Scenario> scenarios = loadFreshScenarios(scenario);
      
      for(Scenario curScenario : scenarios) {
        
        reasonabilityDAO.flagReasonabilityTestsStale(transaction,
            curScenario.getReasonabilityTestResults(),
            user);
      }
      
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    }
  }
  
  
  @Override
  public void calculateEnwEnrolment(final Scenario scenario, final boolean benefitCalculated, final String user)
  throws ServiceException {
    
    if (scenario == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();
      transaction.begin();
      
      List<Scenario> scenarios = scenario.getParentScenarios();
      EnwEnrolmentCalculator enwEnrolmentCalculator = EnrolmentCalculatorFactory.getEnwEnrolmentCalculator();
      EnwEnrolment openScenarioEnw = scenario.getEnwEnrolment();
      
      for(Scenario curScenario : scenarios) {
        
        enwEnrolmentCalculator.initNonEditableFields(curScenario);
        
        if(curScenario.getScenarioId() != scenario.getScenarioId()) {
          enwEnrolmentCalculator.copyEditableFields(openScenarioEnw, curScenario.getEnwEnrolment());
        }
        
        enwEnrolmentCalculator.calculateEnwEnrolment(curScenario, benefitCalculated);
        EnwEnrolment enwEnrolment = curScenario.getEnwEnrolment();
        
        updateEnwEnrolment(transaction, enwEnrolment, user);
      }
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  
  private void updateEnwEnrolment(final Transaction transaction, final EnwEnrolment enwEnrolment, final String user)
      throws ServiceException {
    
    if (enwEnrolment == null) {
      logger.debug("Required object(s) are null.");
      
      throw new ServiceException("A required object is null");
    }
    
    Scenario scenario = enwEnrolment.getScenario();
    
    CalculatorDAO calcDAO = new CalculatorDAO();
    
      if(enwEnrolment.getEnwEnrolmentId() == null) {
        calcDAO.createEnwEnrolment(transaction, enwEnrolment, user);
      } else {
        calcDAO.updateEnwEnrolment(transaction, enwEnrolment, user);
      }
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Enrolment",
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);
      
      transaction.commit();
  }


  @Override
  public void createNewParticipant(NewParticipant participant,
      String scenarioTypeCode,
      String scenarioCategoryCode,
      String userEmail,
      Integer chefsSubmissionId,
      final String user)
  throws ServiceException {

    if (participant == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }

    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();

    try {
      transaction = openTransaction();
      transaction.begin();
      
      @SuppressWarnings("resource")
      Connection connection = OracleUtils.getOracleConnection(transaction);
      
      Person owner = participant.getClient().getOwner();
      Person contact = participant.getClient().getContact();
      Integer participantPin = participant.getClient().getParticipantPin();
      Integer programYear = participant.getProgramYear();
      String municipalityCode = participant.getMunicipalityCode();
      
      if(StringUtils.isNotBlank(owner.getCorpName())) {
        participant.getClient().setParticipantClassCode(ParticipantClassCodes.CORPORATION);
      } else {
        participant.getClient().setParticipantClassCode(ParticipantClassCodes.INDIVIDUAL);
      }

      Integer ownerPersonId = dao.createPerson(transaction, owner, user);
      Integer contactPersonId = dao.createPerson(transaction, contact, user);
      
      owner.setPersonId(ownerPersonId);
      contact.setPersonId(contactPersonId);
      
      Integer clientId = dao.createNewParticipant(transaction, participant, user);
      
      final int numberOfRefYears = 5;
      for(int curYear = programYear - numberOfRefYears; curYear <= programYear; curYear++) {
        
        Integer programYearId = dao.createProgramYear(transaction, clientId, curYear, user);
        Integer programYearVersionId = dao.createProgramYearVersion(transaction, programYearId, municipalityCode, user);
        Integer scenarioId = dao.createScenario(transaction, programYearVersionId,
            scenarioTypeCode, scenarioCategoryCode, user);
        
        chefsDatabaseDao.updateScenarioSubmissionId(connection, scenarioId, chefsSubmissionId, user);
        
        FarmingYear farmingYear = new FarmingYear();
        farmingYear.setProgramYearVersionId(programYearVersionId);
        
        List<FarmingOperation> farmingOperations = participant.getFarmingOperations();
        for (FarmingOperation pyOp : farmingOperations) {
          fixNulls(pyOp);
          
          FarmingOperation newOp;
          if(curYear == programYear) {
            newOp = pyOp;
          } else {
            newOp = copyFarmingOperationForYear(pyOp, programYear, curYear);
          }
          newOp.setFarmingYear(farmingYear);
          
          dao.createFarmingOperation(transaction, newOp, user);
        }
      }
      
      ClientService clientService = ClientServiceFactory.getInstance(connection);
      
      Scenario scenario = clientService.getClientInfoWithoutHistory(
          participantPin, programYear, 1, ClientService.DEF_FIRST_MODE);
      
      if( ! scenarioTypeCode.equals(ScenarioTypeCodes.CHEF) ) {
        
        CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
        String description = "Create account for PIN: " + participantPin;
        crmTransferService.scheduleAccountUpdate(clientId, description, user, transaction);
        
        // Jobs are run in order created. Sleep so that the account update runs first.
        TimeUnit.SECONDS.sleep(2);
        crmTransferService.scheduleBenefitTransfer(scenario, userEmail, user, null, null, null, null, transaction);
      }
      
      Integer rowsCreated = dao.copyForwardYearConfig(transaction, programYear, user);
      
      transaction.commit();
      
      if(rowsCreated > 0) {
        CacheUtils.refreshYearConfigLists();
      }
      
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error("Participant: ", participant);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }


  private void fixNulls(FarmingOperation pyOp) {
    
    final int asciiCodeBeforeA = 64;
    int scheduleAsciiCode = asciiCodeBeforeA + pyOp.getOperationNumber();
    pyOp.setSchedule(String.valueOf((char) scheduleAsciiCode));
    
    if(pyOp.getIsCropDisaster() == null) {
      pyOp.setIsCropDisaster(false);
    }
    if(pyOp.getIsCropShare() == null) {
      pyOp.setIsCropShare(false);
    }
    if(pyOp.getIsFeederMember() == null) {
      pyOp.setIsFeederMember(false);
    }
    if(pyOp.getIsLandlord() == null) {
      pyOp.setIsLandlord(false);
    }
    if(pyOp.getIsLivestockDisaster() == null) {
      pyOp.setIsLivestockDisaster(false);
    }
  }


  private FarmingOperation copyFarmingOperationForYear(
      FarmingOperation from,
      Integer programYear,
      Integer targetYear) {
    
    FarmingOperation to = new FarmingOperation();
    
    to.setAccountingCode(from.getAccountingCode());
    to.setBusinessUseHomeExpense(from.getBusinessUseHomeExpense());
    to.setFarmingExpenses(from.getFarmingExpenses());
    // TODO copy and create partners?
//    curOp.setFarmingOperationPartners(pyOp.getFarmingOperationPartners());
    to.setGrossIncome(from.getGrossIncome());
    to.setInventoryAdjustments(from.getInventoryAdjustments());
    to.setIsCropDisaster(from.getIsCropDisaster());
    to.setIsCropShare(from.getIsCropShare());
    to.setIsFeederMember(from.getIsFeederMember());
    to.setIsLandlord(from.getIsLandlord());
    to.setIsLivestockDisaster(from.getIsLivestockDisaster());
    to.setMargin(from.getMargin());
    to.setNetFarmIncome(from.getNetFarmIncome());
    to.setNetIncomeAfterAdj(from.getNetIncomeAfterAdj());
    to.setNetIncomeBeforeAdj(from.getNetIncomeBeforeAdj());
    to.setOperationNumber(from.getOperationNumber());
    to.setOtherDeductions(from.getOtherDeductions());
    to.setPartnershipName(from.getPartnershipName());
    to.setPartnershipPercent(from.getPartnershipPercent());
    to.setPartnershipPin(from.getPartnershipPin());
    to.setSchedule(from.getSchedule());
    
    int yearsToSubtract = programYear - targetYear;
    
    if(from.getFiscalYearStart() != null) {
      to.setFiscalYearStart(DateUtils.subtractYears(from.getFiscalYearStart(), yearsToSubtract));
    }
    if(from.getFiscalYearEnd() != null) {
      to.setFiscalYearEnd(DateUtils.subtractYears(from.getFiscalYearEnd(), yearsToSubtract));
    }
    
    return to;
  }


  @Override
  public void updateFarmingOperationPartners(
      Scenario scenario,
      List<FarmingOperationPartner> updatedPartners,
      List<FarmingOperationPartner> addedPartners,
      List<FarmingOperationPartner> removedPartners,
      String user)
  throws ServiceException {

    if (scenario == null) {
      logger.debug("Required object(s) are null.");

      throw new ServiceException("A required object is null");
    }
    
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      calcDAO.deletePartners(transaction, removedPartners);
      calcDAO.updatePartners(transaction, updatedPartners, user);
      calcDAO.createPartners(transaction, addedPartners, user);
      
      calcDAO.addScenarioLog(transaction,
          scenario,
          "Update Partners",
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(),
          user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  @Override
  public List<FarmingOperationPartner> getAllPartners() throws ServiceException {
     
    Transaction transaction = null;
    CalculatorDAO calcDAO = new CalculatorDAO();
  
    try {
      transaction = openTransaction();
      List<FarmingOperationPartner> partners;
      partners = calcDAO.readAllPartners(transaction);
      return partners;
       
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
     
  }


  @Override
  public Integer copyProgramYearVersion(
      Scenario scenario,
      String user) 
  throws ServiceException {
    
    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();
    Integer newProgramYearVersionNumber = null;

    Integer year = scenario.getYear();
    Integer programYearVersionNumber = scenario.getFarmingYear().getProgramYearVersionNumber();
    Integer programYearVersionId = scenario.getFarmingYear().getProgramYearVersionId();
    Integer scenarioId = scenario.getScenarioId();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      newProgramYearVersionNumber = dao.copyProgramYearVersion(transaction, scenarioId, user);
      
      String logMessage = String.format("Copy %d Program Year Version %d to new version: %d", year, programYearVersionNumber, newProgramYearVersionNumber);
      addScenarioLog(scenario, logMessage, user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      logger.error("year: " + year);
      logger.error("programYearVersionNumber: " + programYearVersionNumber);
      logger.error("programYearVersionId: " + programYearVersionId);
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return newProgramYearVersionNumber;
  }
  
  /**
   * @param scenario scenario
   * @param String participantDataSrcCode
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void updateScenarioParticipantDataSrcCode(final Scenario scenario, final String participantDataSrcCode, final String user)
      throws ServiceException {

    Transaction transaction = null;
    CalculatorDAO dao = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      dao.updateScenarioParticipantDataSrcCode(transaction, scenario, participantDataSrcCode, user);

      dao.updateScenarioRevisionCount(transaction, scenario.getScenarioId(), scenario.getRevisionCount(), user);
      
      flagReasonabilityTestsStale(scenario, user, transaction);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if (e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }
  
  @Override
  public List<FarmingOperationImportOption> readOperationsForProductiveUnitsImport(
      final Integer participantPin,
      final Integer year,
      final Integer excludedProgramYearVersionNumber)
      throws ServiceException {

    Transaction transaction = null;
    List<FarmingOperationImportOption> scenarioScheduleList = null;
    CalculatorDAO dao = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      scenarioScheduleList = dao.readOperationsForProductiveUnitsImport(transaction,
          participantPin, year, excludedProgramYearVersionNumber);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      if (e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return scenarioScheduleList;
  }
  
  @Override
  public List<FarmingOperationImportOption> readOperationsForInventoryImport(
      final Integer participantPin,
      final Integer year,
      final Integer excludedProgramYearVersionNumber)
      throws ServiceException {

    Transaction transaction = null;
    List<FarmingOperationImportOption> scenarioScheduleList = null;
    CalculatorDAO dao = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      scenarioScheduleList = dao.readOperationsForProductiveUnitsImport(transaction,
          participantPin, year, excludedProgramYearVersionNumber);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      if (e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return scenarioScheduleList;
  }
  
  @Override
  public List<FarmingOperationImportOption> readOperationsForAccrualImport(
      final Integer participantPin,
      final Integer year,
      final Integer excludedProgramYearVersionNumber)
      throws ServiceException {

    Transaction transaction = null;
    List<FarmingOperationImportOption> scenarioScheduleList = null;
    CalculatorDAO dao = new CalculatorDAO();

    try {
      transaction = openTransaction();
      transaction.begin();

      scenarioScheduleList = dao.readOperationsForProductiveUnitsImport(transaction,
          participantPin, year, excludedProgramYearVersionNumber);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      rollback(transaction);
      if (e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    return scenarioScheduleList;
  }

  @Override
  public boolean isAssignedToCurrentUser(Scenario scenario) {
    String guid = CurrentUser.getUser().getGuid();
    String assignedToGuid = scenario.getAssignedToUserGuid();
    boolean assignedTo = guid.equalsIgnoreCase(assignedToGuid);
    return assignedTo;
  }

}
