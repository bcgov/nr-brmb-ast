/**
 *
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import static ca.bc.gov.srm.farm.util.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.StringUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public class TestBenefitCalculator2021 {
  
  private Logger logger = LoggerFactory.getLogger(TestBenefitCalculator2021.class);
  
  @BeforeAll
  protected static void setup() throws ServiceException {
    TestUtils.standardTestSetUp();
  }
  
  @Test
  public final void positiveBenefit2021() throws Exception {
    Scenario scenario = loadScenarioFromJsonFile("data/benefit/calc2021/Scenario1_2021.json");
    int programYear = scenario.getYear();
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, false);
    
    logger.info("Calculation errors:");
    int messageCount = 0;
    for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
      ActionMessage msg = mi.next();
      logger.debug("Message: [" + msg.getKey() + "], values: [" + msg.getValues() + "]");
      messageCount++;
    }
    
    if(messageCount > 0) {
      fail("Unexpected errors calculating the benefit.");
    }
    
    assertEquals(false, scenario.isLateParticipant());
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
      assertEquals("50923.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("54194.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("-3271.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("-3271.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("54194.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("50923.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("49194.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("1729.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("1729.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
      
      assertEquals("1.0", currencyAsString(marginTotal.getFarmSizeRatio()));
      assertEquals("1.0", currencyAsString(marginTotal.getExpenseFarmSizeRatio()));
      assertEquals("0.0", currencyAsString(marginTotal.getStructuralChangeAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getExpenseStructuralChangeAdjs()));
      assertEquals("1729.0", currencyAsString(marginTotal.getProductionMargAftStrChangs()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpensesAfterStructuralChange()));
      assertEquals(false, referenceScenario.getUsedInCalc());
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("59170.86", currencyAsString(benefit.getRatioAdjustedReferenceMargin()));
    assertEquals("67432.37", currencyAsString(benefit.getAdditiveAdjustedReferenceMargin()));
    assertEquals("59170.86", currencyAsString(benefit.getAdjustedReferenceMargin()));
    assertEquals("69359.42", currencyAsString(benefit.getReferenceMarginLimit()));
    assertEquals("41419.6", currencyAsString(benefit.getReferenceMarginLimitCap()));
    assertEquals("69359.42", currencyAsString(benefit.getReferenceMarginLimitForBenefitCalc()));
    assertEquals("-3271.0", currencyAsString(benefit.getProgramYearMargin()));
    assertEquals("62441.86", currencyAsString(benefit.getMarginDecline()));
    
    assertEquals("41419.6", currencyAsString(benefit.getTier3MarginDecline()));
    assertEquals("28993.72", currencyAsString(benefit.getTier3Benefit()));
    assertEquals("3271.0", currencyAsString(benefit.getNegativeMarginDecline()));
    assertEquals("2289.7", currencyAsString(benefit.getNegativeMarginBenefit()));
    assertEquals("31283.42", currencyAsString(benefit.getBenefitBeforeDeductions()));
    assertEquals("1428.57", currencyAsString(benefit.getProdInsurDeemedBenefit()));
    assertEquals("30283.42", currencyAsString(benefit.getBenefitAfterProdInsDeduction()));
    assertEquals("0.5", currencyAsString(benefit.getInterimBenefitPercent()));
    assertEquals("15141.71", currencyAsString(benefit.getBenefitAfterInterimDeduction()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterLateEnrolmentPenalty()));
    assertEquals("15142.0", currencyAsString(benefit.getStandardBenefit()));
    
    assertEquals("59170.86", currencyAsString(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
    assertEquals("62441.86", currencyAsString(benefit.getEnhancedMarginDecline()));
    
    assertEquals("41419.6", currencyAsString(benefit.getEnhancedPositiveMarginDecline()));
    assertEquals("33135.68", currencyAsString(benefit.getEnhancedPositiveMarginBenefit()));
    assertEquals("3271.0", currencyAsString(benefit.getEnhancedNegativeMarginDecline()));
    assertEquals("2616.8", currencyAsString(benefit.getEnhancedNegativeMarginBenefit()));
    assertEquals("35752.48", currencyAsString(benefit.getEnhancedBenefitBeforeDeductions()));
    assertEquals("17376.24", currencyAsString(benefit.getEnhancedBenefitAfterInterimDeduction()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
    assertEquals("2234.0", currencyAsString(benefit.getEnhancedAdditionalBenefit()));
    assertEquals("17376.0", currencyAsString(benefit.getEnhancedTotalBenefit()));
    assertEquals("17376.0", currencyAsString(benefit.getTotalBenefit()));
    
    // Should not be set since this is not a Combined Farm (applied benefit percent is actually combined farm percent).
    assertEquals("null", currencyAsString(benefit.getBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
  }

   
  @Test
  public final void lateParticipant2021() throws Exception {
    Scenario scenario = loadScenarioFromJsonFile("data/benefit/calc2021/Scenario2_2021_late_participant.json");
    int programYear = scenario.getYear();
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, false);
    
    logger.info("Calculation errors:");
    int messageCount = 0;
    for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
      ActionMessage msg = mi.next();
      logger.debug("Message: [" + msg.getKey() + "], values: [" + msg.getValues() + "]");
      messageCount++;
    }
    
    if(messageCount > 0) {
      fail("Unexpected errors calculating the benefit.");
    }
    
    assertEquals(true, scenario.isLateParticipant());
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
      assertEquals("50923.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("54194.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("-3271.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("-3271.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("54194.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("50923.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("49194.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("1729.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("1729.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
      
      assertEquals("1.0", currencyAsString(marginTotal.getFarmSizeRatio()));
      assertEquals("1.0", currencyAsString(marginTotal.getExpenseFarmSizeRatio()));
      assertEquals("0.0", currencyAsString(marginTotal.getStructuralChangeAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getExpenseStructuralChangeAdjs()));
      assertEquals("1729.0", currencyAsString(marginTotal.getProductionMargAftStrChangs()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpensesAfterStructuralChange()));
      assertEquals(false, referenceScenario.getUsedInCalc());
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("59170.86", currencyAsString(benefit.getRatioAdjustedReferenceMargin()));
    assertEquals("67432.37", currencyAsString(benefit.getAdditiveAdjustedReferenceMargin()));
    assertEquals("59170.86", currencyAsString(benefit.getAdjustedReferenceMargin()));
    assertEquals("69359.42", currencyAsString(benefit.getReferenceMarginLimit()));
    assertEquals("41419.6", currencyAsString(benefit.getReferenceMarginLimitCap()));
    assertEquals("69359.42", currencyAsString(benefit.getReferenceMarginLimitForBenefitCalc()));
    assertEquals("-3271.0", currencyAsString(benefit.getProgramYearMargin()));
    assertEquals("62441.86", currencyAsString(benefit.getMarginDecline()));
    
    assertEquals("41419.6", currencyAsString(benefit.getTier3MarginDecline()));
    assertEquals("28993.72", currencyAsString(benefit.getTier3Benefit()));
    assertEquals("3271.0", currencyAsString(benefit.getNegativeMarginDecline()));
    assertEquals("2289.7", currencyAsString(benefit.getNegativeMarginBenefit()));
    assertEquals("31283.42", currencyAsString(benefit.getBenefitBeforeDeductions()));
    assertEquals("1428.57", currencyAsString(benefit.getProdInsurDeemedBenefit()));
    assertEquals("30283.42", currencyAsString(benefit.getBenefitAfterProdInsDeduction()));
    assertEquals("0.5", currencyAsString(benefit.getInterimBenefitPercent()));
    assertEquals("15141.71", currencyAsString(benefit.getBenefitAfterInterimDeduction()));
    assertEquals("3028.0", currencyAsString(benefit.getLateEnrolmentPenalty()));
    assertEquals("12113.71", currencyAsString(benefit.getBenefitAfterLateEnrolmentPenalty()));
    assertEquals("12114.0", currencyAsString(benefit.getStandardBenefit()));
    
    assertEquals("59170.86", currencyAsString(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
    assertEquals("62441.86", currencyAsString(benefit.getEnhancedMarginDecline()));
    
    assertEquals("41419.6", currencyAsString(benefit.getEnhancedPositiveMarginDecline()));
    assertEquals("33135.68", currencyAsString(benefit.getEnhancedPositiveMarginBenefit()));
    assertEquals("3271.0", currencyAsString(benefit.getEnhancedNegativeMarginDecline()));
    assertEquals("2616.8", currencyAsString(benefit.getEnhancedNegativeMarginBenefit()));
    assertEquals("35752.48", currencyAsString(benefit.getEnhancedBenefitBeforeDeductions()));
    assertEquals("17376.24", currencyAsString(benefit.getEnhancedBenefitAfterInterimDeduction()));
    assertEquals("3475.0", currencyAsString(benefit.getEnhancedLateEnrolmentPenalty()));
    assertEquals("13901.24", currencyAsString(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
    assertEquals("1787.0", currencyAsString(benefit.getEnhancedAdditionalBenefit()));
    assertEquals("13901.0", currencyAsString(benefit.getEnhancedTotalBenefit()));
    assertEquals("13901.0", currencyAsString(benefit.getTotalBenefit()));
    
    // Should not be set since this is not a Combined Farm (applied benefit percent is actually combined farm percent).
    assertEquals("null", currencyAsString(benefit.getBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
  }
  
  
  @Test
  public final void combinedFarm2021() throws Exception {
    Scenario scenario = loadScenarioFromJsonFile("data/benefit/calc2021/Scenario3_2021_combined_farm.json");
    int programYear = scenario.getYear();
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, false);
    
    logger.info("Calculation errors:");
    int messageCount = 0;
    for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
      ActionMessage msg = mi.next();
      logger.debug("Message: [" + msg.getKey() + "], values: [" + msg.getValues() + "]");
      messageCount++;
    }
    
    if(messageCount > 0) {
      fail("Unexpected errors calculating the benefit.");
    }
    
    assertEquals(false, scenario.isLateParticipant());
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
      assertEquals("280002.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("-4200450.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("-4200450.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("4499984.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("19532.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("19532.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
      
      assertEquals("0.95", currencyAsString(marginTotal.getFarmSizeRatio()));
      assertEquals("0.95", currencyAsString(marginTotal.getExpenseFarmSizeRatio()));
      assertEquals("-993.78", currencyAsString(marginTotal.getStructuralChangeAdjs()));
      assertEquals("-217481.75", currencyAsString(marginTotal.getExpenseStructuralChangeAdjs()));
      assertEquals("18538.22", currencyAsString(marginTotal.getProductionMargAftStrChangs()));
      assertEquals("4262970.25", currencyAsString(marginTotal.getExpensesAfterStructuralChange()));
      assertEquals(false, referenceScenario.getUsedInCalc());
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("5270585.0", currencyAsString(benefit.getRatioAdjustedReferenceMargin()));
    assertEquals("4413053.6", currencyAsString(benefit.getAdditiveAdjustedReferenceMargin()));
    assertEquals("5270585.0", currencyAsString(benefit.getAdjustedReferenceMargin()));
    assertEquals("4135130.38", currencyAsString(benefit.getReferenceMarginLimit()));
    assertEquals("3689409.5", currencyAsString(benefit.getReferenceMarginLimitCap()));
    assertEquals("4135130.38", currencyAsString(benefit.getReferenceMarginLimitForBenefitCalc()));
    assertEquals("-4200450.0", currencyAsString(benefit.getProgramYearMargin()));
    assertEquals("9471035.0", currencyAsString(benefit.getMarginDecline()));
    
    assertEquals("3689409.5", currencyAsString(benefit.getTier3MarginDecline()));
    assertEquals("2582586.65", currencyAsString(benefit.getTier3Benefit()));
    assertEquals("4200450.0", currencyAsString(benefit.getNegativeMarginDecline()));
    assertEquals("2940315.0", currencyAsString(benefit.getNegativeMarginBenefit()));
    assertEquals("5522901.65", currencyAsString(benefit.getBenefitBeforeDeductions()));
    assertEquals("null", currencyAsString(benefit.getProdInsurDeemedBenefit()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterProdInsDeduction()));
    assertEquals("0.5", currencyAsString(benefit.getInterimBenefitPercent()));
    assertEquals("2761450.83", currencyAsString(benefit.getBenefitAfterInterimDeduction()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterLateEnrolmentPenalty()));
    assertEquals("2761451.0", currencyAsString(benefit.getStandardBenefit()));
    
    assertEquals("5270585.0", currencyAsString(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
    assertEquals("9471035.0", currencyAsString(benefit.getEnhancedMarginDecline()));
    
    assertEquals("3689409.5", currencyAsString(benefit.getEnhancedPositiveMarginDecline()));
    assertEquals("2951527.6", currencyAsString(benefit.getEnhancedPositiveMarginBenefit()));
    assertEquals("4200450.0", currencyAsString(benefit.getEnhancedNegativeMarginDecline()));
    assertEquals("3360360.0", currencyAsString(benefit.getEnhancedNegativeMarginBenefit()));
    assertEquals("6311887.6", currencyAsString(benefit.getEnhancedBenefitBeforeDeductions()));
    assertEquals("3155943.8", currencyAsString(benefit.getEnhancedBenefitAfterInterimDeduction()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
    assertEquals("238549.0", currencyAsString(benefit.getEnhancedAdditionalBenefit()));
    assertEquals("3000000.0", currencyAsString(benefit.getEnhancedTotalBenefit()));
    assertEquals("3000000.0", currencyAsString(benefit.getTotalBenefit()));
    
    // Should not be set for the Combined Farm Benefit, (only for the FarmingYear Benefit) so expect null here.
    assertEquals("null", currencyAsString(benefit.getBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));

    {
      // Need to get the Benefit for the specific PIN. The one above is the combined farm benefit.
      Benefit pinBenefit = scenario.getFarmingYear().getBenefit();
      assertEquals("0.937", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("2587480.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("2811000.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("2811000.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
    {
      int pin = 23260011;
      Scenario pinScenario = getScenarioFromCombinedFarm(scenario, pin);
      Benefit pinBenefit = pinScenario.getBenefit();
      
      assertEquals("0.937", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("2587480.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("2811000.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("223520.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("2811000.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
    {
      int pin = 25527219;
      Scenario pinScenario = getScenarioFromCombinedFarm(scenario, pin);
      Benefit pinBenefit = pinScenario.getBenefit();
      
      assertEquals("0.062", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("171210.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("186000.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("14790.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("186000.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
    {
      int pin = 25527227;
      Scenario pinScenario = getScenarioFromCombinedFarm(scenario, pin);
      Benefit pinBenefit = pinScenario.getBenefit();
      
      assertEquals("0.001", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("2761.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("3000.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("null", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("239.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("3000.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
  }
  
  
  @Test
  public final void combinedFarmLateParticipant2021() throws Exception {
    Scenario scenario = loadScenarioFromJsonFile("data/benefit/calc2021/Scenario4_2021_combined_farm_late_participant.json");
    int programYear = scenario.getYear();
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, false);
    
    logger.info("Calculation errors:");
    int messageCount = 0;
    for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
      ActionMessage msg = mi.next();
      logger.debug("Message: [" + msg.getKey() + "], values: [" + msg.getValues() + "]");
      messageCount++;
    }
    
    if(messageCount > 0) {
      fail("Unexpected errors calculating the benefit.");
    }
    
    assertEquals(true, scenario.isLateParticipant());
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
      assertEquals("280002.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("-4200450.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("-4200450.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("4499984.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("19532.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("19532.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("4480452.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
      
      assertEquals("0.95", currencyAsString(marginTotal.getFarmSizeRatio()));
      assertEquals("0.95", currencyAsString(marginTotal.getExpenseFarmSizeRatio()));
      assertEquals("-993.78", currencyAsString(marginTotal.getStructuralChangeAdjs()));
      assertEquals("-217481.75", currencyAsString(marginTotal.getExpenseStructuralChangeAdjs()));
      assertEquals("18538.22", currencyAsString(marginTotal.getProductionMargAftStrChangs()));
      assertEquals("4262970.25", currencyAsString(marginTotal.getExpensesAfterStructuralChange()));
      assertEquals(false, referenceScenario.getUsedInCalc());
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("5270585.0", currencyAsString(benefit.getRatioAdjustedReferenceMargin()));
    assertEquals("4413053.6", currencyAsString(benefit.getAdditiveAdjustedReferenceMargin()));
    assertEquals("5270585.0", currencyAsString(benefit.getAdjustedReferenceMargin()));
    assertEquals("4135130.38", currencyAsString(benefit.getReferenceMarginLimit()));
    assertEquals("3689409.5", currencyAsString(benefit.getReferenceMarginLimitCap()));
    assertEquals("4135130.38", currencyAsString(benefit.getReferenceMarginLimitForBenefitCalc()));
    assertEquals("-4200450.0", currencyAsString(benefit.getProgramYearMargin()));
    assertEquals("9471035.0", currencyAsString(benefit.getMarginDecline()));
    
    assertEquals("3689409.5", currencyAsString(benefit.getTier3MarginDecline()));
    assertEquals("2582586.65", currencyAsString(benefit.getTier3Benefit()));
    assertEquals("4200450.0", currencyAsString(benefit.getNegativeMarginDecline()));
    assertEquals("2940315.0", currencyAsString(benefit.getNegativeMarginBenefit()));
    assertEquals("5522901.65", currencyAsString(benefit.getBenefitBeforeDeductions()));
    assertEquals("null", currencyAsString(benefit.getProdInsurDeemedBenefit()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterProdInsDeduction()));
    assertEquals("0.5", currencyAsString(benefit.getInterimBenefitPercent()));
    assertEquals("2761450.83", currencyAsString(benefit.getBenefitAfterInterimDeduction()));
    assertEquals("552290.0", currencyAsString(benefit.getLateEnrolmentPenalty()));
    assertEquals("2209160.83", currencyAsString(benefit.getBenefitAfterLateEnrolmentPenalty()));
    assertEquals("2209161.0", currencyAsString(benefit.getStandardBenefit()));
    
    assertEquals("5270585.0", currencyAsString(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
    assertEquals("9471035.0", currencyAsString(benefit.getEnhancedMarginDecline()));
    
    assertEquals("3689409.5", currencyAsString(benefit.getEnhancedPositiveMarginDecline()));
    assertEquals("2951527.6", currencyAsString(benefit.getEnhancedPositiveMarginBenefit()));
    assertEquals("4200450.0", currencyAsString(benefit.getEnhancedNegativeMarginDecline()));
    assertEquals("3360360.0", currencyAsString(benefit.getEnhancedNegativeMarginBenefit()));
    assertEquals("6311887.6", currencyAsString(benefit.getEnhancedBenefitBeforeDeductions()));
    assertEquals("3155943.8", currencyAsString(benefit.getEnhancedBenefitAfterInterimDeduction()));
    assertEquals("631189.0", currencyAsString(benefit.getEnhancedLateEnrolmentPenalty()));
    assertEquals("2524754.8", currencyAsString(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
    assertEquals("315594.0", currencyAsString(benefit.getEnhancedAdditionalBenefit()));
    assertEquals("2524755.0", currencyAsString(benefit.getEnhancedTotalBenefit()));
    assertEquals("2524755.0", currencyAsString(benefit.getTotalBenefit()));
    
    // Should not be set for the Combined Farm Benefit, (only for the FarmingYear Benefit) so expect null here.
    assertEquals("null", currencyAsString(benefit.getBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));

    {
      // Need to get the Benefit for the specific PIN. The one above is the combined farm benefit.
      Benefit pinBenefit = scenario.getFarmingYear().getBenefit();
      assertEquals("0.937", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("2069984.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("517496.0", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("2365695.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("591424.0", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("295711.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("2365695.0", currencyAsString(pinBenefit.getEnhancedTotalBenefit()));
      assertEquals("2365695.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
    {
      int pin = 23260011;
      Scenario pinScenario = getScenarioFromCombinedFarm(scenario, pin);
      Benefit pinBenefit = pinScenario.getBenefit();
      
      assertEquals("0.937", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("2069984.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("517496.0", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("2365695.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("591424.0", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("295711.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("2365695.0", currencyAsString(pinBenefit.getEnhancedTotalBenefit()));
      assertEquals("2365695.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
    {
      int pin = 25527219;
      Scenario pinScenario = getScenarioFromCombinedFarm(scenario, pin);
      Benefit pinBenefit = pinScenario.getBenefit();
      
      assertEquals("0.062", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("136968.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("34242.0", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("156535.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("39134.0", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("19567.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("156535.0", currencyAsString(pinBenefit.getEnhancedTotalBenefit()));
      assertEquals("156535.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
    {
      int pin = 25527227;
      Scenario pinScenario = getScenarioFromCombinedFarm(scenario, pin);
      Benefit pinBenefit = pinScenario.getBenefit();
      
      assertEquals("0.001", StringUtils.formatThreeDecimalPlaces(pinBenefit.getCombinedFarmPercent()));
      assertEquals("2209.0", currencyAsString(pinBenefit.getBenefitAfterAppliedBenefitPercent()));
      assertEquals("552.0", currencyAsString(pinBenefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("2525.0", currencyAsString(pinBenefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      assertEquals("631.0", currencyAsString(pinBenefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      assertEquals("316.0", currencyAsString(pinBenefit.getEnhancedAdditionalBenefit()));
      assertEquals("2525.0", currencyAsString(pinBenefit.getEnhancedTotalBenefit()));
      assertEquals("2525.0", currencyAsString(pinBenefit.getTotalBenefit()));
    }
    
  }
  
  
  private Scenario getScenarioFromCombinedFarm(Scenario scenario, int pin) {
    Scenario pinScenario = null;
    for(Scenario curScenario : scenario.getCombinedFarm().getScenarios()) {
      if(curScenario.getClient().getParticipantPin() == pin) {
        pinScenario = curScenario;
      }
    }
    return pinScenario;
  }
  
}
