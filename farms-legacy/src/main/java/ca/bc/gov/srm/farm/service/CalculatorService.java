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
package ca.bc.gov.srm.farm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessage;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.DeductionLineItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.NewParticipant;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.ui.domain.CalculatorInboxItem;


/**
 * Performs the business functions related to the calculation of benefits for a
 * particular user.
 * 
 */
public interface CalculatorService {
  
  
  void updateClient(final Client client, String user)
      throws ServiceException;


  /**
   * @param farmingYear FarmingYear
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateFarmingYear(
      final FarmingYear farmingYear,
      String user)
  throws ServiceException;
  
  
  /**
   * @param programYearVersionId programYearVersionId
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean programYearVersionHasVerifiedScenario(final Integer programYearVersionId)
  throws ServiceException;
  
  
  /**
   * 
   * @param clientId clientId
   * @return schedule
   * @throws ServiceException On Exception
   */
  String getNewOperationSchedule(final Integer clientId)
  throws ServiceException;

  /**
   * 
   * @param fo fo
   * @param user user
   * @throws ServiceException On Exception
   */
  void createFarmingOperation(final FarmingOperation fo, final String user)
  throws ServiceException;


  /**
   * @param fo fo
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateFarmingOperation(final FarmingOperation fo, final String user)
  throws ServiceException;
  
  
  /**
   * @param fo fo
   * @param user user
   * @throws ServiceException On Exception
   */
  void deleteFarmingOperation(
      final FarmingOperation fo,
      final String user)
  throws ServiceException;

  
  List<ActionMessage> updateScenario(
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
  throws ServiceException;


  /**
   * @param scenario scenario
   * @param userGuid userGuid
   * @param user user
   * @throws ServiceException On Exception 
   */
  void assignToUser(
      final Scenario scenario,
      final String userGuid,
      final String user)
  throws ServiceException;  

  
  Integer saveScenarioAsNew(
      Integer scenarioId,
      String scenarioTypeCode,
      String scenarioCategoryCode,
      Integer programYear,
      String user)
  throws ServiceException;
  
  
  void createReferenceScenario(
      final Scenario forScenario,
      final Integer fromScenarioId,
      String scenarioCategoryCode, final String user)
  throws ServiceException;


  /**
   * @param searchType String
   * @param year Integer
   * @param userGuid String
   * @param scenarioStateCodes List
   * @return List
   * @throws ServiceException On Exception
   */
  List<CalculatorInboxItem> readInboxItems(
      final String searchType,
      final Integer year,
      final String userGuid,
      final List<String> scenarioStateCodes)
  throws ServiceException;


  /**
   * @param pin int
   * @param programYear int
   * @param scenarioNumber Integer
   * @return Scenario
   * @throws ServiceException On Exception
   */
  Scenario loadScenario(
      final int pin,
      final int programYear,
      final Integer scenarioNumber)
  throws ServiceException;


  /**
   * @param pin int
   * @param programYear int
   * @param scenarioNumber Integer
   * @return Scenario
   * @throws ServiceException On Exception
   */
  Scenario loadScenarioWithoutHistory(
      final int pin,
      final int programYear,
      final Integer scenarioNumber)
  throws ServiceException;


  /**
   * @param scenario scenario
   * @param pin Integer
   * @param programYearToCreate Integer
   * @param numOperations Integer
   * @param localDataEntry 
   * @param scenarioCategoryCode String
   * @param user String
   * @return Integer program year version
   * @throws ServiceException On Exception
   */
  Integer createYear(
      final Scenario scenario,
      final Integer pin,
      final Integer programYearToCreate,
      final Integer numOperations,
      final String localDataEntry,
      String scenarioCategoryCode, final String user) 
  throws ServiceException;


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
  Integer updateScenarioPyVersion(
      final Scenario scenario,
      final Integer updateYear,
      final Integer newPyvNumber,
      final Boolean pyvKeepOldData,
      final List<Integer> opNumsKeepOldData,
      final String user) 
  throws ServiceException;


  /**
   * @param scenario scenario
   * @param farmingOperations List<FarmingOperation>
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateOperationAlignment(
      final Scenario scenario,
      final List<FarmingOperation> farmingOperations,
      final String user)
  throws ServiceException;

  
  void saveFinalVerificationNotes(
      final Scenario scenario,
  		final String pyc, 
  		final Integer programYearId,
  		final String user)
  throws ServiceException;

  
  /**
   * @param scenarioId Integer
   * @throws ServiceException On Exception
   * @return revision count
   */
  Integer getScenarioRevisionCount(final Integer scenarioId)
  throws ServiceException;


  /**
   * @param scenario scenario
   * @param user user
   * @throws ServiceException On Exception
   */
  void updateScenarioRevisionCount(
      final Scenario scenario,
      final String user)
  throws ServiceException;


  /**
   * @param scenario scenario
   * @param logMessage logMessage
   * @param user user
   * @throws ServiceException On Exception
   */
  void addScenarioLog(
      final Scenario scenario,
      final String logMessage,
      final String user)
  throws ServiceException;
  

  /**
   * @param programYear Integer
   * @param deductionType String
   * @return List<DeductionLineItem>
   * @throws ServiceException On Exception
   */
  List<DeductionLineItem> getDeductionLineItems(
      final Integer programYear,
      final String deductionType)
  throws ServiceException;
  
  /**
   * @param participantPin Integer
   * @param combinedFarmNumber Integer
   * @return Map<pin, List<scenarioNumber>>
   * @throws ServiceException On Exception
   */
  Map<Integer, List<Integer>> getCombinedFarmInProgressScenarioNumbers(
      final Integer participantPin,
      final Integer combinedFarmNumber)
          throws ServiceException;
  /**
   * @param pin Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean pinExists(
      final Integer pin)
          throws ServiceException;
  
  /**
   * @param pin Integer
   * @param userGuid String
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean pinCheckedOutByUser(
      final Integer pin,
      final String userGuid)
          throws ServiceException;

  /**
   * @param pin Integer
   * @param programYear Integer
   * @param municipalityCode municipalityCode
   * @param scenarioCategoryCode scenarioCategoryCode
   * @return true if the target PIN has an In Progress scenario with matching parameters,
   *         for the purpose adding it to a Combined Farm.
   * @throws ServiceException On Exception
   */
  boolean matchingScenarioExists(
      final Integer pin,
      final Integer programYear,
      final String municipalityCode,
      final String scenarioCategoryCode)
          throws ServiceException;

  /**
   * @param pinToAdd Integer
   * @param programYear Integer
   * @param scenarioId Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean combinedFarmHasAccountingCodeError(
      final Integer pinToAdd,
      final Integer programYear,
      final Integer scenarioId)
          throws ServiceException;
  
  
  /**
   * @param pinToAdd Integer
   * @param programYear Integer
   * @param scenarioId Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean combinedFarmHasReferenceYearMismatchError(
      final Integer pinToAdd,
      final Integer programYear,
      final Integer scenarioId)
          throws ServiceException;
  
  /**
   * @param pin Integer
   * @param programYear Integer
   * @return Integer
   * @throws ServiceException On Exception
   */
  Integer getInProgressCombinedFarmNumber(
      final Integer pin,
      final Integer programYear)
          throws ServiceException;
  
  /**
   * @param pin Integer
   * @param programYear Integer
   * @return Integer
   * @throws ServiceException On Exception
   */
  Integer getVerifiedCombinedFarmNumber(
      final Integer pin,
      final Integer programYear)
          throws ServiceException;
  
  /**
   * @param scenarioId Integer
   * @param verifiedCombinedFarmNumber Integer
   * @return boolean
   * @throws ServiceException On Exception
   */
  boolean combinedFarmMatchesVerified(
      final Integer scenarioId,
      final Integer verifiedCombinedFarmNumber)
          throws ServiceException;

  /**
   * @param scenario scenario
   * @param pinScenarioNumberUpdateMap pinScenarioNumberUpdateMap
   * @param updatedPins updatedPins
   * @param user user
   * @throws ServiceException On Exception 
   */
  void updateCombinedFarmScenarioNumbers(
      final Scenario scenario,
      final Map<Integer, Integer> pinScenarioNumberUpdateMap,
      final List<Integer> updatedPins,
      final String user)
  throws ServiceException;
  
  /**
   * @param scenario scenario
   * @param pinToAdd pinToAdd
   * @param pinScenarioNumberUpdateMap pinScenarioNumberUpdateMap
   * @param updatedPins updatedPins
   * @param user user
   * @throws ServiceException On Exception 
   */
  void addToCombinedFarm(
      final Scenario scenario,
      final Integer pinToAdd,
      final Map<Integer, Integer> pinScenarioNumberUpdateMap,
      final List<Integer> updatedPins,
      final String user)
          throws ServiceException;
  
  /**
   * @param scenario scenario
   * @param scenarioIdToRemove scenarioIdToRemove
   * @param pinScenarioNumberUpdateMap pinScenarioNumberUpdateMap
   * @param updatedPins updatedPins
   * @param user user
   * @throws ServiceException On Exception 
   */
  void removeFromCombinedFarm(
      final Scenario scenario,
      final Integer scenarioIdToRemove,
      final Map<Integer, Integer> pinScenarioNumberUpdateMap,
      final List<Integer> updatedPins,
      final String user)
          throws ServiceException;
  
  void saveCashMarginsInd(
      final Scenario scenario,
      final Boolean cashMarginsInd,
      final Date cashMarginsOptInDate,
      final String user)
          throws ServiceException;

  void saveNonParticipantInd(
      final Scenario scenario,
      final Integer scenarioRevisionCount,
      final Boolean nonParticipantInd,
      final String user)
          throws ServiceException;

  void saveFarmType(
      final Scenario scenario,
      final String farmType,
      final String user)
          throws ServiceException;

  void saveLateParticipantInd(
      final Scenario scenario,
      final Integer scenarioRevisionCount,
      final Boolean lateParticipantInd,
      final String user)
          throws ServiceException;

  void updateProgramYearLocalReceivedDates(
      final Scenario scenario,
      final Date localStatementAReceivedDate,
      final Date localSupplementalReceivedDate,
      final String user,
      final String email,
      final String chefsFormNotes,
      final String formUserType, 
      final String chefsFormType,
      final String fifoResultType)
          throws ServiceException;
  
  void updateProgramYearLocalReceivedDates(
      final Scenario scenario,
      final Date localStatementAReceivedDate,
      final Date localSupplementalReceivedDate,
      final String user,
      final String email)
          throws ServiceException;
  
  public void updateReasonabilityTests(
      final Scenario scenario,
      final ReasonabilityTestResults results,
      final String user)
          throws ServiceException;
  
  public void flagReasonabilityTestsStale(
      final Scenario scenario,
      final String user)
          throws ServiceException;


  void saveInterimVerificationNotes(Scenario scenario, String pyc, Integer programYearId,
      String user) throws ServiceException;


  void saveAdjustmentVerificationNotes(Scenario scenario, String pyc, Integer programYearId,
      String user) throws ServiceException;
  
  void calculateEnwEnrolment(Scenario scenario, boolean benefitCalculated, String user)
  throws ServiceException;

  void createNewParticipant(NewParticipant participant, String scenarioTypeCode, 
  		String scenarioCategoryCode, String userEmail, Integer chefsSubmissionId, String user)
  throws ServiceException;
  
  void updateFarmingOperationPartners(
      Scenario scenario,
      List<FarmingOperationPartner> updatedPartners,
      List<FarmingOperationPartner> addedPartners,
      List<FarmingOperationPartner> removedPartners,
      String user)
  throws ServiceException;
  
  List<FarmingOperationPartner> getAllPartners() throws ServiceException;
  
  Integer copyProgramYearVersion(
      Scenario scenario,
      String user) 
  throws ServiceException;

  void updateScenarioParticipantDataSrcCode(Scenario scenario, String participantDataSrcCode, String user) throws ServiceException;

  List<FarmingOperationImportOption> readOperationsForProductiveUnitsImport(
      Integer participantPin,
      Integer year,
      Integer excludedProgramYearVersionNumber) throws ServiceException;

  List<FarmingOperationImportOption> readOperationsForInventoryImport(
      Integer participantPin,
      Integer year,
      Integer excludedProgramYearVersionNumber) throws ServiceException;

  List<FarmingOperationImportOption> readOperationsForAccrualImport(
      Integer participantPin,
      Integer year,
      Integer excludedProgramYearVersionNumber) throws ServiceException;

  boolean isAssignedToCurrentUser(Scenario scenario);

}
