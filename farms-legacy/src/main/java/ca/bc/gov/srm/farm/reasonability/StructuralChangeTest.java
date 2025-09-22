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
package ca.bc.gov.srm.farm.reasonability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.StructuralChangeTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class StructuralChangeTest extends ReasonabilityTest {
  
  private static Logger logger = LoggerFactory.getLogger(StructuralChangeTest.class);

  private double ratioAdditiveDiffVarianceLimit;
  private double additiveDivisionVarianceLimit;
  private double farmSizeRatioLimit;

  public StructuralChangeTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    ratioAdditiveDiffVarianceLimit = config.getRatioAdditiveDiffVariance();
    additiveDivisionVarianceLimit = config.getAdditiveDivisionVariance();
    farmSizeRatioLimit = config.getFarmSizeRatio();
  }
  
  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    LoggingUtils.logMethodStart(logger);
    StructuralChangeTestResult result = new StructuralChangeTestResult();
    scenario.getReasonabilityTestResults().setStructuralChangeTest(result);
    
    if (missingRequiredData(scenario)) {
      throw new ReasonabilityTestException(MessageConstants.ERRORS_REASONABILITY_BENEFIT_NOT_CALCULATED);
    }
      
    double productionMargAccrAdjs = scenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
    result.setProductionMargAccrAdjs(productionMargAccrAdjs);
    
    // BEGIN - Ratio, Additive Difference
    boolean withinRatioAdditiveDiffLimit = true;
    double ratioReferenceMargin = scenario.getFarmingYear().getBenefit().getRatioAdjustedReferenceMargin();
    double additiveReferenceMargin = scenario.getFarmingYear().getBenefit().getAdditiveAdjustedReferenceMargin();
    
    double ratioAdditiveDiffVariance = MathUtils.roundCurrency( ratioReferenceMargin - additiveReferenceMargin );
    withinRatioAdditiveDiffLimit = Math.abs(ratioAdditiveDiffVariance) < ratioAdditiveDiffVarianceLimit;
    
    result.setWithinRatioAdditiveDiffLimit(withinRatioAdditiveDiffLimit);
    result.setRatioAdditiveDiffVarianceLimit(ratioAdditiveDiffVarianceLimit);
    result.setRatioReferenceMargin(ratioReferenceMargin);
    result.setAdditiveReferenceMargin(additiveReferenceMargin);
    result.setRatioAdditiveDiffVariance(ratioAdditiveDiffVariance);
    // END - Ratio, Additive Difference
    
    
    // START - Additive Division
    boolean withinAdditiveDivisionLimit;
    if(additiveReferenceMargin == 0d) {
      result.setAdditiveDivisionRatio(null);
      withinAdditiveDivisionLimit = true;
    } else {
      double additiveDivisionVariance = MathUtils.roundCurrency( ratioAdditiveDiffVariance / additiveReferenceMargin );
      result.setAdditiveDivisionRatio(additiveDivisionVariance);
      withinAdditiveDivisionLimit = Math.abs(additiveDivisionVariance) < additiveDivisionVarianceLimit;
    }
    
    result.setWithinAdditiveDivisionLimit(withinAdditiveDivisionLimit);
    result.setAdditiveDivisionRatioLimit(additiveDivisionVarianceLimit);
    // End - Additive Division
    
    
    // START - Farm Size Ratio Test
    boolean withinFarmSizeRatioLimit = true;
    
    for(ReferenceScenario refScenario : scenario.getReferenceScenarios()) {
      
      double farmSizeRatio = refScenario.getFarmingYear().getMarginTotal().getFarmSizeRatio();
      
      if(Math.abs(farmSizeRatio) >= farmSizeRatioLimit) {
        withinFarmSizeRatioLimit = false;
        break;
      }
    }
    result.setWithinFarmSizeRatioLimit(withinFarmSizeRatioLimit);
    result.setFarmSizeRatioLimit(farmSizeRatioLimit);
    // END - Farm Size Ratio Test
    
    
    // START - Method to Use
    String methodToUse;
    
    if(withinFarmSizeRatioLimit && withinRatioAdditiveDiffLimit && withinAdditiveDivisionLimit) {
      methodToUse = "Ratio";
    } else if(withinFarmSizeRatioLimit && !withinRatioAdditiveDiffLimit && !withinAdditiveDivisionLimit) {
      methodToUse = "Additive";
    } else {
      methodToUse = "Manual";
    }
    
    result.setMethodToUse(methodToUse);
    // END - Method to Use
    
    
    boolean resultBoolean = withinRatioAdditiveDiffLimit && withinAdditiveDivisionLimit;
    if(scenario.getYear() >= CalculatorConfig.GROWING_FORWARD_2024) {
      resultBoolean &= withinFarmSizeRatioLimit;
    }
    result.setResult(resultBoolean);
    
    LoggingUtils.logMethodStart(logger);
  }

  public static boolean missingRequiredData(Scenario scenario) {
    return scenario.getFarmingYear() == null
        || scenario.getFarmingYear().getBenefit() == null
        || scenario.getFarmingYear().getBenefit().getRatioAdjustedReferenceMargin() == null
        || scenario.getFarmingYear().getBenefit().getAdditiveAdjustedReferenceMargin() == null;
  }

}
