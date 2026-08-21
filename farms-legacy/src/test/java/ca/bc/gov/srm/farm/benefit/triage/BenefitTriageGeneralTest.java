/**
  * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.benefit.triage;

import static ca.bc.gov.srm.farm.service.BenefitTriageService.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageCalculationItem;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageItemResult;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageStatus;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public class BenefitTriageGeneralTest extends AbstractBenefitTriageTest {
  
  private static Logger logger = LoggerFactory.getLogger(BenefitTriageGeneralTest.class);
  
  @Disabled
  @Test
  public void queueAndRunBenefitTriage() {
    
    try {
      
      String triageJobDescription = "Unit Test Benefit Triage Calculation";
      Integer triageImportVersionId = benefitTriageService.queueBenefitTriage(triageJobDescription, conn, user);
      
      benefitTriageService.processBenefitTriage(conn, triageImportVersionId, user);
      
      logger.debug("triageImportVersionId = " + triageImportVersionId);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
  }


  @Disabled
  @Test
  public void runBenefitTriage() {
    
    ImportService importService = ServiceFactory.getImportService();

    try {
      importService.checkForScheduledJob(conn, ImportService.JOB_TYPE_TRIAGE);
      conn.commit();
    } catch (Exception e) {
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
        fail("Exception rolling back");
      }
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
  }


  @Disabled
  @Test
  public void runForSpecificPin() {

    Integer participantPin = 23691678;
    Integer programYear = 2024;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE and Final scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), failMessages);

      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertNotNull(itemResult.getEstimatedBenefit());
      assertNotNull(itemResult.getIsPaymentFile());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }

  @Test
  public void failNotEnoughReferenceYearsBenefitCalcFailed() {

    Integer participantPin = 22871610;
    Integer programYear = 2022;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Failed", itemResult.getScenarioStateCodeDesc());
      assertNull(itemResult.getEstimatedBenefit());
      assertNull(itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      List<String> errorMessages = itemResult.getErrorMessages();
      assertNotNull(errorMessages);
      logErrorMessages(errorMessages);
      assertEquals(1, errorMessages.size());
      
      String errorMessage = errorMessages.get(0);
      assertEquals("At least three reference years are required.", errorMessage);
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.FAILED, triageScenario.getScenarioStateCode());
      assertNull(triageScenario.getBenefit());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
    
  }


  @Test
  public void inventoryEndValuesMissing() {

    Integer participantPin = 2570547;
    Integer programYear = 2022;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
          MESSAGE_FAIL_REFERENCE_MARGIN_FAILED_AT_LOW_END,
          MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED
      }), failMessages);
      
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      assertEquals(Double.valueOf(0), itemResult.getEstimatedBenefit());
      assertEquals(Boolean.FALSE, itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertNotNull(triageScenario.getBenefit());
      assertEquals(Double.valueOf(0), triageScenario.getBenefit().getTotalBenefit());
      
      {
        assertNotNull(triageScenario.getFarmingYear());
        assertNotNull(triageScenario.getFarmingYear().getFarmingOperations());
        assertEquals(1, triageScenario.getFarmingYear().getFarmingOperations().size());
        
        FarmingOperation farmingOperation = triageScenario.getFarmingYear().getFarmingOperations().get(0);
        assertNotNull(farmingOperation);
        assertNotNull(farmingOperation.getCropItems());
        assertEquals(3, farmingOperation.getCropItems().size());
      }
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }


  @Test
  public void missingBpuTurnOffStructureChange() {

    Integer participantPin = 25762691;
    Integer programYear = 2022;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
          MESSAGE_FAIL_STRUCTURE_CHANGE_NOT_ENABLED,
          MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED,
          MESSAGE_FAIL_PAYMENT_TOO_LARGE
      }), failMessages);
      
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      assertNotNull(itemResult.getEstimatedBenefit());
      assertTrue(itemResult.getEstimatedBenefit() > 0);
      assertEquals(Boolean.TRUE, itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());

      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.NONE, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.NONE, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }


  @Test
  public void multistageCommodityCodeForSpecificStage() {

    Integer participantPin = 25881582;
    Integer programYear = 2022;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
          MESSAGE_FAIL_BENEFIT_RISK_FAILED_AT_HIGH_END,
          MESSAGE_FAIL_PAYMENT_TOO_LARGE
      }), failMessages);
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      assertNotNull(itemResult.getEstimatedBenefit());
      assertTrue(itemResult.getEstimatedBenefit() > 0);
      assertEquals(Boolean.TRUE, itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      assertTrue(triageScenario.getBenefit().getTotalBenefit() > 0);
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }


  @Test
  public void missingApplesReceivable() {

    Integer participantPin = 26025916;
    Integer programYear = 2022;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
          MESSAGE_FAIL_STRUCTURE_CHANGE_NOT_ENABLED
      }), failMessages);
      
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      assertEquals(Double.valueOf(0.0), itemResult.getEstimatedBenefit());
      assertEquals(Boolean.FALSE, itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.NONE, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.NONE, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertEquals(Double.valueOf(0.0), triageScenario.getBenefit().getTotalBenefit());
      
      {
        assertNotNull(triageScenario.getFarmingYear());
        assertNotNull(triageScenario.getFarmingYear().getFarmingOperations());
        assertEquals(1, triageScenario.getFarmingYear().getFarmingOperations().size());
        
        FarmingOperation farmingOperation = triageScenario.getFarmingYear().getFarmingOperations().get(0);
        assertNotNull(farmingOperation);
        assertNotNull(farmingOperation.getReceivableItems());
        assertEquals(1, farmingOperation.getReceivableItems().size());
        
        ReceivableItem receivableItem = farmingOperation.getReceivableItems().get(0);
        assertEquals("60", receivableItem.getInventoryItemCode());
        assertEquals(Double.valueOf(35000.0), receivableItem.getTotalStartOfYearAmount());
        assertNull(receivableItem.getTotalEndOfYearAmount());
      }
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }


  @Test
  public void bpuWithZeroMargin() {

    Integer participantPin = 23639834;
    Integer programYear = 2022;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
          MESSAGE_FAIL_PAYMENT_TOO_LARGE
      }), failMessages);
      
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      assertNotNull(itemResult.getEstimatedBenefit());
      assertEquals(Boolean.TRUE, itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      assertTrue(triageScenario.getBenefit().getTotalBenefit() > 0);
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }


  @Test
  public void missingBpuUsePreviousYear() {

    Integer participantPin = 3684453;
    Integer programYear = 2015;

    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
  
      // Delete TRIAGE scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), errorMessages);
      
      assertEquals(Arrays.asList(new String[] {
      }), failMessages);
      
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
      assertNotNull(itemResult.getEstimatedBenefit());
      assertEquals(Boolean.FALSE, itemResult.getIsPaymentFile());
      assertTrue(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
  
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }


  @Test
  public void notEnrolled() {

    Integer participantPin = 23351265;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
    assertFalse(programYearMetadataList.isEmpty());

    ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
    
    List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
    {
      BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
      item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
      item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
      triageItems.add(item);
    }
    
    List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
    try {
      benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(triageItemResults);
    assertEquals(0, triageItemResults.size());
    
    programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);

    List<ScenarioMetaData> triageScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
    assertNotNull(triageScenarios);
    assertEquals(0, triageScenarios.size());
  }


  @Test
  public final void testBenefitTriageStatusByYear() {

    try {
      List<BenefitTriageStatus> triageStatusList = benefitTriageService.getBenefitTriageStatusByYear(2023);

      for (BenefitTriageStatus f : triageStatusList) {
        logger.debug(f.toString());
      }
      assertTrue(triageStatusList.size() > 0);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  
  /**
   * The benefit calculation will fail if any year is missing both income and expenses.
   * This results in a message in BenefitTriageItemResult.errorMessages for that PIN.
   * Benefit Triage has an additional rule that each year must have both income and expenses.
   * Cases where only one is missing will result in separate messages in BenefitTriageItemResult.failMessages
   */
  @Test
  public void benefitCalcErrorMissingIncomeAndExpenses() {
    
    Integer participantPin = 236791897;
    Integer programYear = 2024;
    
    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
      
      // Delete TRIAGE and Final scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      
      List<BenefitTriageCalculationItem> triageItems = new ArrayList<>();
      {
        BenefitTriageCalculationItem item = new BenefitTriageCalculationItem();
        item.setParticipantPin(participantPin);
        item.setProgramYear(programYear);
        item.setCraProgramYearVersionId(latestBaseDataScenarioMetadata.getProgramYearVersionId());
        item.setCraScenarioId(latestBaseDataScenarioMetadata.getScenarioId());
        item.setCraScenarioNumber(latestBaseDataScenarioMetadata.getScenarioNumber());
        triageItems.add(item);
      }
      
      List<BenefitTriageItemResult> triageItemResults = new ArrayList<>();
      try {
        benefitTriageService.calculateTriageBenefits(conn, triageItems, triageItemResults, null, null, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult itemResult = triageItemResults.get(0);
      assertNotNull(itemResult);
      List<String> errorMessages = itemResult.getErrorMessages();
      List<String> failMessages = itemResult.getFailMessages();
      
      assertNotNull(errorMessages);
      assertNotNull(failMessages);
      logErrorMessages(errorMessages);
      logFailMessages(failMessages);
      assertEquals(1, errorMessages.size());
      assertEquals(0, failMessages.size());
      
      String errorMessage1 = errorMessages.get(0);
      assertEquals("One or more of the reference years has both no income and no expenses.", errorMessage1);
      
      assertEquals(participantPin, itemResult.getParticipantPin());
      assertEquals(programYear, itemResult.getProgramYear());
      assertEquals("Failed", itemResult.getScenarioStateCodeDesc());
      assertNull(itemResult.getEstimatedBenefit());
      assertNull(itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertFalse(itemResult.isPaymentPass());
      
      programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.FAILED, triageScenario.getScenarioStateCode());
      assertNull(triageScenario.getBenefit());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }
}
