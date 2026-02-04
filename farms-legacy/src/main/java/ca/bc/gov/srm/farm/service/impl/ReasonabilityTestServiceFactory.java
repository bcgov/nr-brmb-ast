/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.reasonability.BenefitRiskAssessment;
import ca.bc.gov.srm.farm.reasonability.ExpenseTestRefYearCompGC;
import ca.bc.gov.srm.farm.reasonability.ExpenseTestIAC;
import ca.bc.gov.srm.farm.reasonability.MarginTest;
import ca.bc.gov.srm.farm.reasonability.StructuralChangeTest;
import ca.bc.gov.srm.farm.reasonability.production.ProductionTest;
import ca.bc.gov.srm.farm.reasonability.revenue.RevenueRiskTest;
import ca.bc.gov.srm.farm.service.ReasonabilityTestService;

/**
 * @author awilkinson
 */
public final class ReasonabilityTestServiceFactory implements
    ReasonabilityTestService {

  private static Logger logger = LoggerFactory.getLogger(ReasonabilityTestServiceFactory.class);

  private static ReasonabilityTestServiceFactory instance = null;
  
  public static ReasonabilityTestService getInstance() {
    if (instance == null){
      instance = new ReasonabilityTestServiceFactory();
    }

    return instance;
  }

  @Override
  public ReasonabilityTestResults test(Scenario scenario) throws ReasonabilityTestException {
    LoggingUtils.logMethodStart(logger);
    
    ReasonabilityTestResults results = scenario.getReasonabilityTestResults();
    if(results == null) {
      results = new ReasonabilityTestResults();
      scenario.setReasonabilityTestResults(results);
    }
    results.setIsFresh(Boolean.TRUE);
    
    BenefitRiskAssessment benefitRiskAssessment = new BenefitRiskAssessment();
    StructuralChangeTest structuralChangeTest = new StructuralChangeTest();
    ExpenseTestIAC expenseTestIAC = new ExpenseTestIAC();
    ProductionTest productionTest = new ProductionTest();
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();
    RevenueRiskTest revenueRisk = new RevenueRiskTest();
    MarginTest marginTest = new MarginTest();
    
    structuralChangeTest.test(scenario);
    expenseTestIAC.test(scenario);
    productionTest.test(scenario);
    expenseTestRefYearCompGC.test(scenario);
    revenueRisk.test(scenario);
    benefitRiskAssessment.test(scenario); // benefit test needs to run after revenue test to get forage consumers
    marginTest.test(scenario);            // margin test needs to run after revenue test to get forage consumers
    
    LoggingUtils.logMethodEnd(logger);
    return results;
  }

  @Override
  public boolean missingRequiredData(Scenario scenario) {
    return BenefitRiskAssessment.missingRequiredData(scenario)
        || MarginTest.missingRequiredData(scenario)
        || StructuralChangeTest.missingRequiredData(scenario)
        || ExpenseTestIAC.missingRequiredData(scenario)
        || ProductionTest.missingRequiredData(scenario)
        || ExpenseTestRefYearCompGC.missingRequiredData(scenario)
        || RevenueRiskTest.missingRequiredData(scenario);
  }

}
