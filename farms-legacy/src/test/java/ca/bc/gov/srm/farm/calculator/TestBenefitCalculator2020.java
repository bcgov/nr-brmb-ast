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
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public class TestBenefitCalculator2020 {
  
  private Logger logger = LoggerFactory.getLogger(TestBenefitCalculator2020.class);
  
  @BeforeAll
  protected static void setup() throws ServiceException {
    TestUtils.standardTestSetUp();
  }
  
  @Test
  public final void testBenefitCalculation2019() throws Exception {
    Scenario scenario = loadScenarioFromJsonFile("data/benefit/calc2020/Scenario1_2019.json");
    int programYear = scenario.getYear();
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, true);

    logger.info("Calculation errors:");
    int messageCount = 0;
    for(@SuppressWarnings("unchecked")
        Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
      ActionMessage msg = mi.next();
      logger.debug("Message: [" + msg.getKey() + "], values: [" + msg.getValues() + "]");
      messageCount++;
    }
    
    if(messageCount > 0) {
      fail("Unexpected errors calculating the benefit.");
    }
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
      assertEquals("115923.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("49194.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("66729.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("66729.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("2774.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("160099.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("139690.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("69210.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("70480.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("-2347.15", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("-4000.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("-6347.15", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("64132.85", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("23240.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("189671.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("73210.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
      assertEquals("1.21", currencyAsString(marginTotal.getFarmSizeRatio()));
      assertEquals("1.19", currencyAsString(marginTotal.getExpenseFarmSizeRatio()));
      assertEquals("13443.55", currencyAsString(marginTotal.getStructuralChangeAdjs()));
      assertEquals("14041.37", currencyAsString(marginTotal.getExpenseStructuralChangeAdjs()));
      assertEquals("77576.4", currencyAsString(marginTotal.getProductionMargAftStrChangs()));
      assertEquals("87251.37", currencyAsString(marginTotal.getExpensesAfterStructuralChange()));
      assertEquals(true, referenceScenario.getUsedInCalc());
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("113770.72", currencyAsString(benefit.getRatioAdjustedReferenceMargin()));
    assertEquals("113146.1", currencyAsString(benefit.getAdditiveAdjustedReferenceMargin()));
    assertEquals("113770.72", currencyAsString(benefit.getAdjustedReferenceMargin()));
    assertEquals("46220.97", currencyAsString(benefit.getReferenceMarginLimit()));
    assertEquals("79639.5", currencyAsString(benefit.getReferenceMarginLimitCap()));
    assertEquals("79639.5", currencyAsString(benefit.getReferenceMarginLimitForBenefitCalc()));
    assertEquals("66729.0", currencyAsString(benefit.getProgramYearMargin()));
    assertEquals("12910.5", currencyAsString(benefit.getMarginDecline()));
    
    assertEquals("0.0", currencyAsString(MathUtils.roundCurrency(benefit.getTier3MarginDecline())));
    assertEquals("0.0", currencyAsString(MathUtils.roundCurrency(benefit.getTier3Benefit())));
    assertEquals("0.0", currencyAsString(benefit.getNegativeMarginDecline()));
    assertEquals("0.0", currencyAsString(benefit.getNegativeMarginBenefit()));
    assertEquals("0.0", currencyAsString(benefit.getBenefitBeforeDeductions()));
    assertEquals("null", currencyAsString(benefit.getProdInsurDeemedBenefit()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterProdInsDeduction()));
    assertEquals("0.5", currencyAsString(benefit.getInterimBenefitPercent()));
    assertEquals("0.0", currencyAsString(benefit.getBenefitAfterInterimDeduction()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
    assertEquals("0.0", currencyAsString(benefit.getStandardBenefit()));
    
    assertEquals("113770.72", currencyAsString(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
    assertEquals("47041.72", currencyAsString(benefit.getEnhancedMarginDecline()));
    
    assertEquals("12910.5", currencyAsString(benefit.getEnhancedPositiveMarginDecline()));
    assertEquals("10328.4", currencyAsString(benefit.getEnhancedPositiveMarginBenefit()));
    assertEquals("0.0", currencyAsString(benefit.getEnhancedNegativeMarginDecline()));
    assertEquals("0.0", currencyAsString(benefit.getEnhancedNegativeMarginBenefit()));
    assertEquals("10328.4", currencyAsString(benefit.getEnhancedBenefitBeforeDeductions()));
    assertEquals("5164.2", currencyAsString(benefit.getEnhancedBenefitAfterInterimDeduction()));
    assertEquals("5164.0", currencyAsString(benefit.getEnhancedAdditionalBenefit()));
    assertEquals("5164.0", currencyAsString(benefit.getEnhancedTotalBenefit()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
    
    assertEquals("5164.0", currencyAsString(benefit.getTotalBenefit()));
  }

  
  @Test
  public final void testBenefitCalculation2020() throws Exception {
    Scenario scenario = loadScenarioFromJsonFile("data/benefit/calc2020/Scenario2_2020.json");
    int programYear = scenario.getYear();
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, true);

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
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
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
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("115923.0", currencyAsString(marginTotal.getTotalAllowableIncome()));
      assertEquals("49194.0", currencyAsString(marginTotal.getTotalAllowableExpenses()));
      assertEquals("66729.0", currencyAsString(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", currencyAsString(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", currencyAsString(marginTotal.getAccrualAdjsPayables()));
      assertEquals("66729.0", currencyAsString(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("2774.0", currencyAsString(marginTotal.getTotalUnallowableIncome()));
      assertEquals("160099.0", currencyAsString(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpenseAccrualAdjs()));
      
      assertEquals("1.0", currencyAsString(marginTotal.getFarmSizeRatio()));
      assertEquals("1.0", currencyAsString(marginTotal.getExpenseFarmSizeRatio()));
      assertEquals("0.0", currencyAsString(marginTotal.getStructuralChangeAdjs()));
      assertEquals("0.0", currencyAsString(marginTotal.getExpenseStructuralChangeAdjs()));
      assertEquals("66729.0", currencyAsString(marginTotal.getProductionMargAftStrChangs()));
      assertEquals("49194.0", currencyAsString(marginTotal.getExpensesAfterStructuralChange()));
      assertEquals(true, referenceScenario.getUsedInCalc());
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("87752.38", currencyAsString(benefit.getRatioAdjustedReferenceMargin()));
    assertEquals("88557.5", currencyAsString(benefit.getAdditiveAdjustedReferenceMargin()));
    assertEquals("87752.38", currencyAsString(benefit.getAdjustedReferenceMargin()));
    assertEquals("53770.09", currencyAsString(benefit.getReferenceMarginLimit()));
    assertEquals("61426.67", currencyAsString(benefit.getReferenceMarginLimitCap()));
    assertEquals("61426.67", currencyAsString(benefit.getReferenceMarginLimitForBenefitCalc()));
    assertEquals("1729.0", currencyAsString(benefit.getProgramYearMargin()));
    assertEquals("86023.38", currencyAsString(benefit.getMarginDecline()));
    
    assertEquals("59697.67", currencyAsString(benefit.getTier3MarginDecline()));
    assertEquals("41788.37", currencyAsString(benefit.getTier3Benefit()));
    assertEquals("0.0", currencyAsString(benefit.getNegativeMarginDecline()));
    assertEquals("0.0", currencyAsString(benefit.getNegativeMarginBenefit()));
    assertEquals("41788.37", currencyAsString(benefit.getBenefitBeforeDeductions()));
    assertEquals("null", currencyAsString(benefit.getProdInsurDeemedBenefit()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterProdInsDeduction()));
    assertEquals("0.5", currencyAsString(benefit.getInterimBenefitPercent()));
    assertEquals("20894.18", currencyAsString(benefit.getBenefitAfterInterimDeduction()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getBenefitAfterLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
    assertEquals("20894.0", currencyAsString(benefit.getStandardBenefit()));
    
    assertEquals("87752.38", currencyAsString(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
    assertEquals("86023.38", currencyAsString(benefit.getEnhancedMarginDecline()));
    
    assertEquals("59697.67", currencyAsString(benefit.getEnhancedPositiveMarginDecline()));
    assertEquals("47758.13", currencyAsString(benefit.getEnhancedPositiveMarginBenefit()));
    assertEquals("0.0", currencyAsString(benefit.getEnhancedNegativeMarginDecline()));
    assertEquals("0.0", currencyAsString(benefit.getEnhancedNegativeMarginBenefit()));
    assertEquals("47758.13", currencyAsString(benefit.getEnhancedBenefitBeforeDeductions()));
    assertEquals("23879.07", currencyAsString(benefit.getEnhancedBenefitAfterInterimDeduction()));
    assertEquals("2985.0", currencyAsString(benefit.getEnhancedAdditionalBenefit()));
    assertEquals("23879.0", currencyAsString(benefit.getEnhancedTotalBenefit()));
    assertEquals("null", currencyAsString(benefit.getEnhancedLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
    assertEquals("null", currencyAsString(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
    
    assertEquals("23879.0", currencyAsString(benefit.getTotalBenefit()));
  }

}
