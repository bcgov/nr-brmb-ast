/**
 *
 * Copyright (c) 2018, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.reasonability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.MarginTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class MarginTest extends ReasonabilityTest {
  
  private static Logger logger = LoggerFactory.getLogger(MarginTest.class);

  private double referenceMarginVarianceLimit;
  private double industryVarianceLimit;

  public MarginTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    
    referenceMarginVarianceLimit = config.getReferenceMarginVariance();
    industryVarianceLimit = config.getMarginIndustryVariance();
  }
  
  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    LoggingUtils.logMethodStart(logger);
    
    MarginTestResult result = new MarginTestResult();
    scenario.getReasonabilityTestResults().setMarginTest(result);
    
    if (missingRequiredData(scenario)) {
      throw new ReasonabilityTestException(MessageConstants.ERRORS_REASONABILITY_BENEFIT_NOT_CALCULATED);
    }
    
    double programYearMargin = scenario.getFarmingYear().getBenefit().getProgramYearMargin();
    result.setProgramYearMargin(programYearMargin);
    
    // Begin Part A
    boolean withinLimitOfReferenceMargin;
    double adjustedReferenceMargin = scenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
    double variance;
    if(adjustedReferenceMargin == 0.0d) {
      result.setAdjustedReferenceMarginVariance(null);
      withinLimitOfReferenceMargin = false;
    } else {
      variance = (programYearMargin - adjustedReferenceMargin) / Math.abs(adjustedReferenceMargin);
      variance = MathUtils.round(variance, PERCENT_DECIMAL_PLACES);
      result.setAdjustedReferenceMarginVariance(variance);
      withinLimitOfReferenceMargin = Math.abs(variance) < referenceMarginVarianceLimit;
    }
    
    result.setAdjustedReferenceMargin(adjustedReferenceMargin);
    result.setAdjustedReferenceMarginVarianceLimit(referenceMarginVarianceLimit);
    result.setWithinLimitOfReferenceMargin(withinLimitOfReferenceMargin);
    // End Part A
    
    
    // Start Part B
    double industryAverage = scenario.getReasonabilityTestResults().getBenefitRisk().getBenchmarkMargin();
    
    boolean withinLimitOfIndustryAverage;
    
    double industryVariance;
    if(industryAverage == 0.0d) {
      result.setIndustryVariance(null);
      withinLimitOfIndustryAverage = false;
    } else {
      industryVariance = MathUtils.round( ( (programYearMargin - industryAverage) / Math.abs(industryAverage) ), PERCENT_DECIMAL_PLACES);
      result.setIndustryVariance(industryVariance);
      
      withinLimitOfIndustryAverage = Math.abs(industryVariance) < industryVarianceLimit;
    }
    
    result.setWithinLimitOfIndustryAverage(withinLimitOfIndustryAverage);
    result.setIndustryAverage(industryAverage);
    result.setIndustryVarianceLimit(industryVarianceLimit);
    // End Part B
    
    boolean resultStatus = withinLimitOfReferenceMargin && withinLimitOfIndustryAverage;
    result.setResult(resultStatus);
    
    LoggingUtils.logMethodStart(logger);
  }

  public static boolean missingRequiredData(Scenario scenario) {
    return scenario.getFarmingYear() == null
        || scenario.getFarmingYear().getBenefit() == null
        || scenario.getFarmingYear().getBenefit().getProgramYearMargin() == null;
  }

}
