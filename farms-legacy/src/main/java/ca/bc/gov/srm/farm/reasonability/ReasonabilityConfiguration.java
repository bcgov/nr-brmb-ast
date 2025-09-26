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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;

/**
 * @author awilkinson
 */
public class ReasonabilityConfiguration {

  private static Logger logger = LoggerFactory.getLogger(ReasonabilityConfiguration.class);
  
  private static ReasonabilityConfiguration instance;
  
  private boolean useConfigurationParameters = false;
  
  private ReasonabilityConfiguration(boolean useDefaultsOnly) {
    this.useConfigurationParameters = useDefaultsOnly;
  }
  
  public static ReasonabilityConfiguration getInstance() {
    return getInstance(true);
  }
  
  public static ReasonabilityConfiguration getInstance(boolean useConfigurationParameters) {
    if(instance == null) {
      instance = new ReasonabilityConfiguration(useConfigurationParameters);
    }
    return instance;
  }
  
  public double getBenefitRiskVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_BENEFIT_RISK_VARIANCE_PREF, 20) / 100;
  }
  
  public double getBenefitRiskDeductiblePercent() {
    return getDouble(ConfigurationKeys.REASONABILITY_BENEFIT_RISK_DEDUCTIBLE_PREF, 30) / 100;
  }
  
  public double getBenefitRiskPayoutLevelPercent() {
    return getDouble(ConfigurationKeys.REASONABILITY_BENEFIT_RISK_PAYOUT_LEVEL_PREF, 70) / 100;
  }
  
  public double getReferenceMarginVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_MARGIN_TEST_REFERENCE_VARIANCE_PREF, 20) / 100;
  }
  
  public double getMarginIndustryVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_MARGIN_TEST_INDUSTRY_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRatioAdditiveDiffVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_STRUCTURAL_CHANGE_TEST_RATIO_ADDITIVE_DIFF_VARIANCE_PREF, 20000.00);
  }
  
  public double getAdditiveDivisionVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_STRUCTURAL_CHANGE_TEST_ADDITIVE_DIVISION_VARIANCE_PREF, 1.00);
  }
  
  public double getFarmSizeRatio() {
    return getDouble(ConfigurationKeys.REASONABILITY_STRUCTURAL_CHANGE_TEST_FARM_SIZE_RATIO_PREF, 250) / 100;
  }
  
  public double getExpenseIACIndustryVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_EXPENSE_TEST_IAC_INDUSTRY_VARIANCE_PREF, 20) / 100;
  }
  
  public double getProductionForageVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_PRODUCTION_FORAGE_TEST_VARIANCE_PREF, 20) / 100;
  }
  
  public double getProductionFruitVegVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_PRODUCTION_FRUIT_VEG_TEST_VARIANCE_PREF, 20) / 100;
  }
  
  public double getProductionGrainVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_PRODUCTION_GRAINS_TEST_VARIANCE_PREF, 20) / 100;
  }
  
  public double getExpenseGCVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_EXPENSE_TEST_GC_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskForageVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_FORAGE_GRAIN_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskCattleFedPerProductiveUnit() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_CATTLE, 1.5);
  }
  
  public double getRevenueRiskCattleBredHeifersFedPerProductiveUnit() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_CATTLE_BRED_HEIFERS, 1.5);
  }
  
  public double getRevenueRiskFeederCattleFedPerProductiveUnit() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_FEEDER_CATTLE, 0.5);
  }
  
  public double getRevenueRiskFinishedCattleFedPerProductiveUnit() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_FINISHED_CATTLE, 0.5);
  }
  
  public double getRevenueRiskCattleVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_CATTLE_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskCattleDeathRate() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_CATTLE_DEATH_RATE_PREF, 5) / 100;
  }
  
  public double getRevenueRiskNurseryVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_NURSERY_VARIANCE_PREF, 50) / 100;
  }
  
  public double getRevenueRiskHogsFarrowToFinishVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskHogsFarrowToFinishDeathRate() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_DEATH_RATE_PREF, 5) / 100;
  }
  
  public double getRevenueRiskHogsFarrowToFinishBirthsPerCycle() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_BIRTHS_PER_CYCLE_PREF, 10);
  }
  
  public double getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_BIRTH_CYCLES_PER_YEAR_PREF, 2.5);
  }
  
  public double getRevenueRiskHogsFeederVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_HOGS_FEEDER_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskPoultryBroilersChickensVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_CHICKENS_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskPoultryBroilersChickensAverageWeightKg() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_CHICKENS_AVERAGE_WEIGHT_PREF, 2.0);
  }
  
  public double getRevenueRiskPoultryBroilersTurkeysVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_TURKEYS_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskPoultryBroilersTurkeysAverageWeightKg() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_TURKEYS_AVERAGE_WEIGHT_PREF, 6.5);
  }
  
  public double getRevenueRiskPoultryEggsConsumptionVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_POULTRY_EGGS_CONSUMPTION_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_EGGS_CONSUMPTION_AVERAGE_EGGS_PER_LAYER_PREF, 290);
  }
  
  public double getRevenueRiskPoultryEggsHatchingVariance() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_POULTRY_EGGS_HATCHING_VARIANCE_PREF, 20) / 100;
  }
  
  public double getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer() {
    return getDouble(ConfigurationKeys.REASONABILITY_REVENUE_RISK_TEST_EGGS_HATCHING_AVERAGE_EGGS_PER_LAYER_PREF, 140);
  }
  
  private double getDouble(String configurationKey, double defaultValue) {
    double result = defaultValue;
    if(useConfigurationParameters) {
      String t = ConfigurationUtility.getInstance().getValue(configurationKey);
      if (t != null) {
        try {
          result = Double.parseDouble(t);
        } catch (NumberFormatException e) {
          logger.warn("Unexpected error: ", e);
        }
      }
    }
    return result;
  }
  
  public Map<String, String> getParameters() {
    Map<String, String> result = new HashMap<>();
    for (String parameterName : ConfigurationKeys.REASONABILITY_TEST_PARAMETERS) {
      ConfigurationUtility configUtil = ConfigurationUtility.getInstance();
      String parameterValue = configUtil.getValue(parameterName);
      result.put(parameterName, parameterValue);
    }
    
    return result;
  }
}
