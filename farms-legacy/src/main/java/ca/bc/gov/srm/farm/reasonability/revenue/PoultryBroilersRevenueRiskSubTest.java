/**
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.reasonability.revenue;

import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.PoultryBroilersRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class PoultryBroilersRevenueRiskSubTest {

  public static final String ERROR_MISSING_CHICKEN_BR_PRODUCTIVE_UNIT_CODE = 
      "Productive Units not found for Chicken, Broilers: {0}";
  public static final String ERROR_MISSING_TURKEY_BR_PRODUCTIVE_UNIT_CODE = 
      "Productive Units not found for Turkey, Broilers: {0}";
  public static final String ERROR_MISSING_CHICKEN_BR_INCOME = 
      "Income not found for Chicken, Broilers.";
  public static final String ERROR_MISSING_TURKEY_BR_INCOME = 
      "Income not found for Turkey, Broilers.";

  private double chickenAverageWeightKg;
  private double turkeyAverageWeightKg;
  private double chickenVarianceLimit;
  private double turkeyVarianceLimit;

  public PoultryBroilersRevenueRiskSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    chickenAverageWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    turkeyAverageWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    chickenVarianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    turkeyVarianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
  }

  public void test(Scenario scenario) {

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    PoultryBroilersRevenueRiskSubTestResult result = new PoultryBroilersRevenueRiskSubTestResult();
    revenueRiskResult.setPoultryBroilers(result);
    
    Map<Integer, IncomeExpense> incomesWithReceivables = ScenarioUtils.getIncomesWithReceivables(scenario);
    
    result.setSubTestPass(true);
    
    boolean hasChickens = ScenarioUtils.hasCommdityType(scenario, CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    result.setHasChickens(hasChickens);
    boolean hasTurkeys = ScenarioUtils.hasCommdityType(scenario, CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    result.setHasTurkeys(hasTurkeys);
    result.setHasPoultryBroilers(hasChickens || hasTurkeys);
    
    result.setChickenAverageWeightKg(chickenAverageWeightKg);
    result.setTurkeyAverageWeightKg(turkeyAverageWeightKg);

    result.setChickenVarianceLimit(chickenVarianceLimit);
    result.setChickenPass(true);
    
    result.setTurkeyVarianceLimit(turkeyVarianceLimit);
    result.setTurkeyPass(true);
    
    computeReportedIncome(incomesWithReceivables, result);
    computeExpectedRevenues(scenario, result, revenueRiskResult);
    checkMissingValues(result, revenueRiskResult); // This must be run after computeReportedIncome and computeExpectedRevenues
    computeVariance(result);
    
    if (!result.getChickenPass() || !result.getTurkeyPass()) {
      result.setSubTestPass(false);
    }
  }
  
  private void computeReportedIncome(Map<Integer, IncomeExpense> incomesWithReceivables, PoultryBroilersRevenueRiskSubTestResult result) {
    for (Integer lineItemId : incomesWithReceivables.keySet()) {
      IncomeExpense incomeExpense = incomesWithReceivables.get(lineItemId);
      if (incomeExpense.getTotalAmount() != 0) {
        String commodityTypeCode = incomeExpense.getLineItem().getCommodityTypeCode();
        if (CommodityTypeCodes.POULTRY_BROILER_CHICKEN.equals(commodityTypeCode)) {
          Double chickenReportedRevenue = result.getChickenReportedRevenue() == null ? 0d : result.getChickenReportedRevenue();
          result.setChickenReportedRevenue(chickenReportedRevenue + incomeExpense.getTotalAmount());
        } else if (CommodityTypeCodes.POULTRY_BROILER_TURKEY.equals(commodityTypeCode)) {
          Double turkeyReportedRevenue = result.getTurkeyReportedRevenue() == null ? 0d : result.getTurkeyReportedRevenue();
          result.setTurkeyReportedRevenue(turkeyReportedRevenue + incomeExpense.getTotalAmount());
        }
      }
    }
  }
  
  private void computeExpectedRevenues(Scenario scenario, PoultryBroilersRevenueRiskSubTestResult result, RevenueRiskTestResult revenueRiskResult) {
    Double chickenExpectedRevenue = null;
    Double chickenExpectedSoldCount = null;
    Double chickenKgProduced = null;
    Double turkeyKgProduced = null;
    Double turkeyExpectedRevenue = null;
    Double turkeyExpectedSoldCount = null;
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for (FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        for(ProductiveUnitCapacity pu : fo.getProductiveUnitCapacities()) {
          String structureGroupCode = pu.getStructureGroupCode();
          boolean isBroiler = StructureGroupCodes.CHICKEN_BROILERS.equals(structureGroupCode) || StructureGroupCodes.TURKEY_BROILERS.equals(structureGroupCode);
          Double productiveUnits = pu.getTotalProductiveCapacityAmount() * partnershipPercent;
          if (isBroiler && productiveUnits > 0) {
            if (structureGroupCode.equals(StructureGroupCodes.CHICKEN_BROILERS)) {
              Double expectedSold = MathUtils.roundToWholeNumber(productiveUnits / result.getChickenAverageWeightKg());
              chickenExpectedSoldCount = chickenExpectedSoldCount == null ? 0d : chickenExpectedSoldCount;
              chickenExpectedSoldCount += expectedSold;
              chickenKgProduced = chickenKgProduced == null ? 0d : chickenKgProduced;
              chickenKgProduced += productiveUnits;
              if (fo.getChickenBroilerFMVPrice() != null) {
                chickenExpectedRevenue = chickenExpectedRevenue == null ? 0d : chickenExpectedRevenue;
                chickenExpectedRevenue += expectedSold * fo.getChickenBroilerFMVPrice();
              } else {
                result.setChickenPass(false);
                revenueRiskResult.addErrorMessage(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV, pu.getStructureGroupCode());
              }
            } else if (structureGroupCode.equals(StructureGroupCodes.TURKEY_BROILERS)) {
              Double expectedSold = MathUtils.roundToWholeNumber(productiveUnits / result.getTurkeyAverageWeightKg());
              turkeyExpectedSoldCount = turkeyExpectedSoldCount == null ? 0d : turkeyExpectedSoldCount;
              turkeyExpectedSoldCount += expectedSold;
              turkeyKgProduced = turkeyKgProduced == null ? 0d : turkeyKgProduced;
              turkeyKgProduced += productiveUnits;
              if (fo.getTurkeyBroilerFMVPrice() != null) {
                turkeyExpectedRevenue = turkeyExpectedRevenue == null ? 0d : turkeyExpectedRevenue;
                turkeyExpectedRevenue += expectedSold * fo.getTurkeyBroilerFMVPrice();
              } else {
                result.setTurkeyPass(false);
                revenueRiskResult.addErrorMessage(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV, pu.getStructureGroupCode());
              }
            }
          }
        }
      }
    }
    
    result.setChickenExpectedRevenue(chickenExpectedRevenue);
    result.setChickenExpectedSoldCount(chickenExpectedSoldCount);
    result.setChickenKgProduced(chickenKgProduced);
    result.setTurkeyExpectedRevenue(turkeyExpectedRevenue);
    result.setTurkeyExpectedSoldCount(turkeyExpectedSoldCount);
    result.setTurkeyKgProduced(turkeyKgProduced);
    
    if (chickenExpectedSoldCount != null && chickenExpectedSoldCount != 0 && chickenExpectedRevenue != null) {
      Double chickenPricePerBird = chickenExpectedRevenue / chickenExpectedSoldCount;
      result.setChickenPricePerBird(chickenPricePerBird);
    }
    if (turkeyExpectedSoldCount != null && turkeyExpectedSoldCount != 0 && turkeyExpectedRevenue != null) {
      Double turkeyPricePerBird = turkeyExpectedRevenue / turkeyExpectedSoldCount;
      result.setTurkeyPricePerBird(turkeyPricePerBird);
    }
  }
  
  private void checkMissingValues(PoultryBroilersRevenueRiskSubTestResult result, RevenueRiskTestResult revenueRiskResult) {
    if (result.getChickenKgProduced() == null && result.getChickenReportedRevenue() != null) {
      result.setChickenPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_CHICKEN_BR_PRODUCTIVE_UNIT_CODE, StructureGroupCodes.CHICKEN_BROILERS);
    } else if (result.getChickenKgProduced() != null && result.getChickenReportedRevenue() == null) {
      result.setChickenPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_CHICKEN_BR_INCOME);
    }
    
    if (result.getTurkeyKgProduced() == null && result.getTurkeyReportedRevenue() != null) {
      result.setTurkeyPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_TURKEY_BR_PRODUCTIVE_UNIT_CODE, StructureGroupCodes.TURKEY_BROILERS);
    } else if (result.getTurkeyKgProduced() != null && result.getTurkeyReportedRevenue() == null) {
      result.setTurkeyPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_TURKEY_BR_INCOME);
    }
  }
  
  private void computeVariance(PoultryBroilersRevenueRiskSubTestResult result) {
    Double chickenExpectedRevenue = result.getChickenExpectedRevenue();
    Double turkeyExpectedRevenue = result.getTurkeyExpectedRevenue();
    Double chickenReportedRevenue = result.getChickenReportedRevenue();
    Double turkeyReportedRevenue = result.getTurkeyReportedRevenue();
    
    if (result.getChickenPass() && chickenReportedRevenue != null && chickenExpectedRevenue != null && chickenExpectedRevenue != 0) {
      Double variance = (chickenReportedRevenue - chickenExpectedRevenue) / Math.abs(chickenExpectedRevenue);
      variance = ReasonabilityTestUtils.roundPercent(variance);
      result.setChickenVariance(variance);
      if (Math.abs(variance) > chickenVarianceLimit) {
        result.setChickenPass(false);
      }
    }
    
    if (result.getTurkeyPass() && turkeyReportedRevenue != null && turkeyExpectedRevenue != null && turkeyExpectedRevenue != 0) {
      Double variance = (turkeyReportedRevenue - turkeyExpectedRevenue) / Math.abs(turkeyExpectedRevenue);
      variance = ReasonabilityTestUtils.roundPercent(variance);
      result.setTurkeyVariance(variance);
      if (Math.abs(variance) > turkeyVarianceLimit) {
        result.setTurkeyPass(false);
      }
    }
  }
}
