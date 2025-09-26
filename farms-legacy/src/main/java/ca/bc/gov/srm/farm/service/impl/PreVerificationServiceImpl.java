/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.domain.codes.TriageQueueCodes.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.PreVerificationChecklist;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.MarginTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.StructuralChangeTestResult;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.PreVerificationService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

/**
 * PreVerificationService evaluates a scenario to get answers to a list
 * of questions pertinent to how work on the scenario should be routed,
 * that is, which task queue it should be added to.
 * 
 * @author awilkinson
 * @created Sep 16, 2021
 */
public class PreVerificationServiceImpl extends BaseService implements PreVerificationService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public PreVerificationChecklist getPreVerificationChecklist(Scenario scenario) throws ServiceException {
    logger.debug("<getPreVerificationChecklist");
    
    PreVerificationChecklist checklist = new PreVerificationChecklist();
    
    double paymentAmountRequiringASpecialist = CalculatorConfig.getPreVerificationPaymentAmountRequiringASpecialist();
    double incomeAmountRequiringASpecialist = CalculatorConfig.getPreVerificationIncomeAmountRequiringASpecialist();
    
    List<String> farmTypeDetailedCodesRequiringSpecialist = CalculatorConfig.getPreVerificationFarmTypeDetailedCodesRequiringSpecialist();
    List<String> farmTypeDetailedCodesForAbbotsford = CalculatorConfig.getPreVerificationFarmTypeDetailedCodesForAbbotsford();
    List<String> farmTypeDetailedCodesForKelowna = CalculatorConfig.getPreVerificationFarmTypeDetailedCodesForKelowna();

    Boolean hasBenefitPayment = null;
    Boolean paymentOverSpecialistThreshold = null;
    Boolean referenceMarginTestFailed = null;
    Boolean structureChangeTestFailed = null;
    Boolean inCombinedFarm;
    Boolean allowableIncomeOverSpecialistThreshold = null;
    String farmTypeDetailCode = null;
    String farmTypeDetailCodeDescription = null;
    String triageQueueCode = null;
    
    if( ! scenario.getFarmTypeDetailedCodes().isEmpty() ) {
      farmTypeDetailCode = scenario.getFarmTypeDetailedCodes().get(0);
      farmTypeDetailCodeDescription = scenario.getFarmTypeDetailedDescriptions().get(0);
    }

    FarmingYear farmingYear = scenario.getFarmingYear();
    if(farmingYear != null) {
      Benefit benefit = scenario.getFarmingYear().getBenefit();
      if(benefit != null) {
        Double totalBenefit = benefit.getTotalBenefit();
        if(totalBenefit != null) {
          hasBenefitPayment = totalBenefit > 0;
          
          paymentOverSpecialistThreshold = totalBenefit > paymentAmountRequiringASpecialist;
        }
      }
    }
    
    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    if(reasonabilityTestResults != null) {
      MarginTestResult marginTest = reasonabilityTestResults.getMarginTest();
      if(marginTest != null && marginTest.getWithinLimitOfReferenceMargin() != null) {
        referenceMarginTestFailed = ! marginTest.getWithinLimitOfReferenceMargin();
      }
      
      StructuralChangeTestResult structuralChangeTest = reasonabilityTestResults.getStructuralChangeTest();
      if(structuralChangeTest != null && structuralChangeTest.getResult() != null) {
        structureChangeTestFailed = ! structuralChangeTest.getResult();
      }
    }
    
    inCombinedFarm = scenario.isInCombinedFarm();
    
    if(farmingYear != null) {
      
      MarginTotal marginTotal = farmingYear.getMarginTotal();
      if(marginTotal != null) {
        Double totalAllowableIncome = marginTotal.getTotalAllowableIncome();
        if(totalAllowableIncome != null) {
          allowableIncomeOverSpecialistThreshold = totalAllowableIncome > incomeAmountRequiringASpecialist;
        }
      }
    }
    
    checklist.setFarmTypeDetailCode(farmTypeDetailCode);
    checklist.setFarmTypeDetailCodeDescription(farmTypeDetailCodeDescription);
    
    
    if(hasBenefitPayment != null
        && paymentOverSpecialistThreshold != null
        && referenceMarginTestFailed != null
        && structureChangeTestFailed != null
        && allowableIncomeOverSpecialistThreshold != null
        && farmTypeDetailCode != null) {
      
      boolean specialistSector = farmTypeDetailedCodesRequiringSpecialist.contains(farmTypeDetailCode);
      boolean abbotsfordSector = farmTypeDetailedCodesForAbbotsford.contains(farmTypeDetailCode);
      boolean kelownaSector = farmTypeDetailedCodesForKelowna.contains(farmTypeDetailCode);
      
      boolean zeroPayment = !hasBenefitPayment;
      boolean testsPass = ! referenceMarginTestFailed
                && ! structureChangeTestFailed;
      boolean testsFailed = ! testsPass;
      boolean zeroPaymentPass = zeroPayment && testsPass;
      
      if(zeroPaymentPass) {
        triageQueueCode = ZERO_PAYMENT_PASS;
      } else if(paymentOverSpecialistThreshold
          || inCombinedFarm
          || allowableIncomeOverSpecialistThreshold
          || specialistSector) {
        triageQueueCode = VERIFICATION_SPECIALIST;
      } else if(abbotsfordSector) {
        if(zeroPayment && testsFailed) {
          triageQueueCode = ABBOTSFORD_ZERO_FAIL;
        } else {
          triageQueueCode = ABBOTSFORD_UNDER_100K;
        }
      } else if(kelownaSector) {
        if(zeroPayment && testsFailed) {
          triageQueueCode = KELOWNA_ZERO_FAIL;
        } else {
          triageQueueCode = KELOWNA_UNDER_100K;
        }
      }
    }
    
    String triageQueueDescrption = getTriageQueueDescrption(triageQueueCode);
    
    
    checklist.setPaymentAmountRequiringASpecialist(paymentAmountRequiringASpecialist);
    checklist.setIncomeAmountRequiringASpecialist(incomeAmountRequiringASpecialist);
    checklist.setHasBenefitPayment(hasBenefitPayment);
    checklist.setPaymentOverSpecialistThreshold(paymentOverSpecialistThreshold);
    checklist.setReferenceMarginTest(referenceMarginTestFailed);
    checklist.setStructureChangeTestFailed(structureChangeTestFailed);
    checklist.setInCombinedFarm(inCombinedFarm);
    checklist.setAllowableIncomeOverSpecialistThreshold(allowableIncomeOverSpecialistThreshold);
    checklist.setFarmTypeDetailCodeDescription(farmTypeDetailCodeDescription);
    checklist.setTriageQueue(triageQueueCode);
    checklist.setTriageQueueDescription(triageQueueDescrption);
    
    logger.debug(">getPreVerificationChecklist");
    return checklist;
  }

  private String getTriageQueueDescrption(String triageQueueCode) {
    String result = null;
    
    if(triageQueueCode != null) {
      ListService listService = ServiceFactory.getListService();
      
      List<ListView> triageQueueCodes = listService.getList(ListService.TRIAGE_QUEUE);
      
      ListView code = null;
      for (ListView curTriageCode : triageQueueCodes) {
        if(curTriageCode.getValue().equals(triageQueueCode)) {
          code = curTriageCode;
          break;
        }
      }
      
      if(code != null) {
        result = code.getLabel();
      }
    }
    
    return result;
  }

}
