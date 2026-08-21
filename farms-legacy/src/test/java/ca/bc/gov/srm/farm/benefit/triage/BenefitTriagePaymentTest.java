/**
 * Copyright (c) 2026,
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.crm.resource.CrmCoreConfigurationResource;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageCalculationItem;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageItemResult;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * Test case where the Benefit/Payment Amount is zero.
 * 
 * @author awilkinson
 */
public class BenefitTriagePaymentTest extends AbstractBenefitTriageTest {
  
  private static Logger logger = LoggerFactory.getLogger(BenefitTriagePaymentTest.class);


  @Test
  public void paymentThreshold() {

    CrmCoreConfigurationResource coreConfiguration = null;
    try {
      coreConfiguration = getCoreConfiguration();
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(coreConfiguration);

    BigDecimal triagePaymentThreshold = coreConfiguration.getVsi_triagepaymentthreshold();
    assertNotNull(triagePaymentThreshold);
    logger.debug("triagePaymentThreshold: " + triagePaymentThreshold);
    
  }
  
  
  @Test
  public void paymentPass() {
    
    Integer participantPin = 3784295;
    Integer programYear = 2022;
    
    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
      
      // Delete TRIAGE and Final scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      TestUtils.deleteScenarios(participantPin, programYear, ScenarioCategoryCodes.COMPARISON_SCENARIO, ScenarioTypeCodes.USER, conn);
      
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
      assertEquals(Boolean.TRUE, itemResult.getIsPaymentFile());
      assertFalse(itemResult.isZeroPass());
      assertTrue(itemResult.isPaymentPass());
      
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
      assertTrue(triageScenario.getBenefit().getTotalBenefit() <= 5000);
      assertEquals(5, triageScenario.getReferenceScenarios().size());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.TRUE, triageStructuralChangeTestPassed);
      Boolean triageSCAdditiveDivisionTestPassed = triageTestResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
      assertEquals(Boolean.TRUE, triageSCAdditiveDivisionTestPassed);
      
      Boolean benefitRiskTestPassed = triageTestResults.getBenefitRisk().getResult();
      assertEquals(Boolean.FALSE, benefitRiskTestPassed);
      Double benefitRiskVariance = triageTestResults.getBenefitRisk().getVariance();
      assertNotNull(benefitRiskVariance);
      assertEquals(Boolean.TRUE, benefitRiskVariance < 0);
      
      
      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(1, verifiedFinalScenarios.size());
      
      ScenarioMetaData verifiedFinalScenarioMetaData = verifiedFinalScenarios.get(0);
      Integer verifiedFinalScenarioNumber = verifiedFinalScenarioMetaData.getScenarioNumber();
      assertNotNull(verifiedFinalScenarioNumber);
      Scenario verifiedFinalScenario = null;
      try {
        verifiedFinalScenario = calculatorService.loadScenario(participantPin, programYear, verifiedFinalScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(verifiedFinalScenario);
      assertNotNull(verifiedFinalScenario.getClient());
      assertEquals(participantPin, verifiedFinalScenario.getClient().getParticipantPin());
      assertEquals(programYear, verifiedFinalScenario.getYear());
      assertEquals(ScenarioTypeCodes.USER, verifiedFinalScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.FINAL, verifiedFinalScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.VERIFIED, verifiedFinalScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, verifiedFinalScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, verifiedFinalScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(verifiedFinalScenario.getBenefit());
      assertNotNull(verifiedFinalScenario.getBenefit().getTotalBenefit());
      assertEquals(4166.0, verifiedFinalScenario.getBenefit().getTotalBenefit());
      
      ReasonabilityTestResults verifiedTestResults = verifiedFinalScenario.getReasonabilityTestResults();
      assertNotNull(verifiedTestResults);
      assertNotNull(verifiedTestResults.getMarginTest());
      assertEquals(triageReferenceMarginTestPassed, verifiedTestResults.getMarginTest().getWithinLimitOfReferenceMargin());
      
      assertNotNull(verifiedTestResults.getStructuralChangeTest());
      assertEquals(triageStructuralChangeTestPassed, verifiedTestResults.getStructuralChangeTest().getResult());
      
      Double triageReferenceMargin = triageScenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
      Double pyTriageProductionMargAccrAdjs = triageScenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
      Double pyMinus1TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 1).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus2TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 2).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus3TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 3).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus4TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 4).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus5TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 5).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      
      Double verifiedReferenceMargin = verifiedFinalScenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
      Double pyVerifiedProductionMargAccrAdjs = verifiedFinalScenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
      Double pyMinus1VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 1).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus2VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 2).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus3VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 3).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus4VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 4).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus5VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 5).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      
      assertEquals(triageReferenceMargin, verifiedReferenceMargin);
      assertEquals(pyTriageProductionMargAccrAdjs, pyVerifiedProductionMargAccrAdjs);
      assertEquals(pyMinus1TriageProductionMargAftStrChangs, pyMinus1VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus2TriageProductionMargAftStrChangs, pyMinus2VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus3TriageProductionMargAftStrChangs, pyMinus3VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus4TriageProductionMargAftStrChangs, pyMinus4VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus5TriageProductionMargAftStrChangs, pyMinus5VerifiedProductionMargAftStrChangs);
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }
  
  
  @Test
  public void paymentFailMissingIncomeAndExpenses() {
    
    Integer participantPin = 699688128;
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
          MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED,
          MESSAGE_FAIL_MISSING_INCOME,
          MESSAGE_FAIL_MISSING_EXPENSES,
          MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED,
          MESSAGE_FAIL_ACCOUNTING_METHOD_CHANGED
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
      assertEquals(1.0, triageScenario.getBenefit().getTotalBenefit());
      assertEquals(5, triageScenario.getReferenceScenarios().size());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.FALSE, triageStructuralChangeTestPassed);
      Boolean triageSCAdditiveDivisionTestPassed = triageTestResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
      assertEquals(Boolean.FALSE, triageSCAdditiveDivisionTestPassed);
      
      Boolean benefitRiskTestPassed = triageTestResults.getBenefitRisk().getResult();
      assertEquals(Boolean.TRUE, benefitRiskTestPassed);
      Double benefitRiskVariance = triageTestResults.getBenefitRisk().getVariance();
      assertNull(benefitRiskVariance);
      
      
      // -----------------------------------------------------------------------------------------------
      
      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(0, verifiedFinalScenarios.size());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }
  
  
  @Test
  public void paymentFailCombinedFarm() {
    
    Integer participantPin = 98765757;
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
          MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED,
          MESSAGE_FAIL_COMBINED_FARM,
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
      assertTrue(triageScenario.getBenefit().getTotalBenefit() > 5000);
      assertEquals(5, triageScenario.getReferenceScenarios().size());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.TRUE, triageStructuralChangeTestPassed);
      Boolean triageSCAdditiveDivisionTestPassed = triageTestResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
      assertEquals(Boolean.TRUE, triageSCAdditiveDivisionTestPassed);
      
      Boolean benefitRiskTestPassed = triageTestResults.getBenefitRisk().getResult();
      assertEquals(Boolean.TRUE, benefitRiskTestPassed);
      Double benefitRiskVariance = triageTestResults.getBenefitRisk().getVariance();
      assertNull(benefitRiskVariance);
      
      
      // -----------------------------------------------------------------------------------------------
      
      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(0, verifiedFinalScenarios.size());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }
  
  
  @Test
  public void paymentFailAccountingMethodChanged() {
    
    Integer participantPin = 23303530;
    Integer programYear = 2022;
    
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
          MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED,
          MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED,
          MESSAGE_FAIL_ACCOUNTING_METHOD_CHANGED,
          MESSAGE_FAIL_BENEFIT_RISK_FAILED_AT_HIGH_END,
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
      assertTrue(triageScenario.getBenefit().getTotalBenefit() > 5000);
      assertEquals(5, triageScenario.getReferenceScenarios().size());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.FALSE, triageStructuralChangeTestPassed);
      Boolean triageSCAdditiveDivisionTestPassed = triageTestResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
      assertEquals(Boolean.FALSE, triageSCAdditiveDivisionTestPassed);
      
      Boolean benefitRiskTestPassed = triageTestResults.getBenefitRisk().getResult();
      assertEquals(Boolean.FALSE, benefitRiskTestPassed);
      Double benefitRiskVariance = triageTestResults.getBenefitRisk().getVariance();
      assertNotNull(benefitRiskVariance);
      assertEquals(Boolean.TRUE, benefitRiskVariance > 0);
      
      
      // -----------------------------------------------------------------------------------------------
      
      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(0, verifiedFinalScenarios.size());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }
  
  
  @Test
  public void paymentFailMunicipalityChanged() {
    
    Integer participantPin = 3227063;
    Integer programYear = 2022;
    
    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
      
      // Delete TRIAGE and Final scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      TestUtils.deleteScenarios(participantPin, programYear, ScenarioCategoryCodes.COMPARISON_SCENARIO, ScenarioTypeCodes.USER, conn);
      
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
          MESSAGE_FAIL_MUNICIPALITY_CHANGED
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
      assertTrue(triageScenario.getBenefit().getTotalBenefit() <= 5000);
      assertEquals(5, triageScenario.getReferenceScenarios().size());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.TRUE, triageStructuralChangeTestPassed);
      Boolean triageSCAdditiveDivisionTestPassed = triageTestResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
      assertEquals(Boolean.TRUE, triageSCAdditiveDivisionTestPassed);

      Boolean benefitRiskTestPassed = triageTestResults.getBenefitRisk().getResult();
      assertEquals(Boolean.FALSE, benefitRiskTestPassed);
      Double benefitRiskVariance = triageTestResults.getBenefitRisk().getVariance();
      assertNotNull(benefitRiskVariance);
      assertEquals(Boolean.TRUE, benefitRiskVariance < 0);

      
      // -----------------------------------------------------------------------------------------------
      
      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(0, verifiedFinalScenarios.size());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }
  
  
  @Test
  public void paymentFailBenefitRisk() {
    
    Integer participantPin = 23475924;
    Integer programYear = 2022;
    
    try {
      List<ScenarioMetaData> programYearMetadataList = TestUtils.getProgramYearMetadata(participantPin, programYear, conn);
      
      ScenarioMetaData latestBaseDataScenarioMetadata = ScenarioUtils.findLatestBaseDataScenario(programYearMetadataList, programYear);
      
      // Delete TRIAGE and Final scenarios if left over from a previous test run
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      TestUtils.deleteScenarios(participantPin, programYear, ScenarioCategoryCodes.COMPARISON_SCENARIO, ScenarioTypeCodes.USER, conn);
      
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
          MESSAGE_FAIL_BENEFIT_RISK_FAILED_AT_HIGH_END
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
      assertTrue(triageScenario.getBenefit().getTotalBenefit() <= 5000);
      assertEquals(5, triageScenario.getReferenceScenarios().size());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.TRUE, triageStructuralChangeTestPassed);
      Boolean triageSCAdditiveDivisionTestPassed = triageTestResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
      assertEquals(Boolean.TRUE, triageSCAdditiveDivisionTestPassed);

      Boolean benefitRiskTestPassed = triageTestResults.getBenefitRisk().getResult();
      assertEquals(Boolean.FALSE, benefitRiskTestPassed);
      Double benefitRiskVariance = triageTestResults.getBenefitRisk().getVariance();
      assertNotNull(benefitRiskVariance);
      assertEquals(Boolean.TRUE, benefitRiskVariance > 0);

      
      // -----------------------------------------------------------------------------------------------
      
      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(0, verifiedFinalScenarios.size());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 2);
    }
  }

}
