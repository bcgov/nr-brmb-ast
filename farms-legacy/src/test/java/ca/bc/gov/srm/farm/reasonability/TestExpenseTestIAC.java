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
package ca.bc.gov.srm.farm.reasonability;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ExpenseTestIACResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 * @created May 14, 2019
 */
public class TestExpenseTestIAC{
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  @Test
  public void resultFail() throws Exception {
    Scenario scenario = TestUtils.loadScenarioFromJsonFile("data/reasonability/expense/iac/Scenario2Fail.json");
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    ExpenseTestIAC expenseTestIndustryAverageComparison = new ExpenseTestIAC();
    expenseTestIndustryAverageComparison.test(scenario);
    
    ExpenseTestIACResult result = scenario.getReasonabilityTestResults().getExpenseTestIAC();
    assertNotNull(result);
    
    assertEquals("374355.0", String.valueOf(result.getExpenseAccrualAdjs()));
    assertEquals("486176.19", String.valueOf(result.getIndustryAverage()));
    assertEquals("-0.23", String.valueOf(result.getIndustryVariance()));
    assertEquals("0.2", String.valueOf(result.getIndustryVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getResult());
  }
  
  @Test
  public void resultPass() throws Exception {
    Scenario scenario = TestUtils.loadScenarioFromJsonFile("data/reasonability/expense/iac/Scenario1Pass.json");
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    ExpenseTestIAC expenseTestIndustryAverageComparison = new ExpenseTestIAC();
    expenseTestIndustryAverageComparison.test(scenario);
    
    ExpenseTestIACResult result = scenario.getReasonabilityTestResults().getExpenseTestIAC();
    assertNotNull(result);
    
    assertEquals("404355.0", String.valueOf(result.getExpenseAccrualAdjs()));
    assertEquals("486176.19", String.valueOf(result.getIndustryAverage()));
    assertEquals("-0.168", String.valueOf(result.getIndustryVariance()));
    assertEquals("0.2", String.valueOf(result.getIndustryVarianceLimit()));
    assertEquals(Boolean.TRUE, result.getResult());
  }
  
  @Test
  public void appleProducer() throws Exception {
    Scenario scenario = TestUtils.loadScenarioFromJsonFile("data/reasonability/expense/iac/Scenario3AppleProducer.json");
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    ExpenseTestIAC expenseTestIndustryAverageComparison = new ExpenseTestIAC();
    expenseTestIndustryAverageComparison.test(scenario);
    
    ExpenseTestIACResult result = scenario.getReasonabilityTestResults().getExpenseTestIAC();
    assertNotNull(result);
    
    assertEquals("23346.5", String.valueOf(result.getExpenseAccrualAdjs()));
    assertEquals("12825.52", String.valueOf(result.getIndustryAverage()));
    assertEquals("0.82", String.valueOf(result.getIndustryVariance()));
    assertEquals("0.2", String.valueOf(result.getIndustryVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getResult());
  }

}
