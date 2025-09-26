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
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.PoultryEggsRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class PoultryEggsRevenueRiskSubTest {
  
  public static final String ERROR_MISSING_HATCH_EGGS_PRODUCTIVE_UNIT_CODE = 
      "Productive Units not found for Hatch Chicken Eggs: {0}";
  public static final String ERROR_MISSING_CONSUMP_EGGS_PRODUCTIVE_UNIT_CODE = 
      "Productive Units not found for Consumption Chicken Eggs: {0}";
  public static final String ERROR_MISSING_HATCH_EGGS_INCOME = 
      "Income not found for Hatch Chicken Eggs.";
  public static final String ERROR_MISSING_CONSUMP_EGGS_INCOME = 
      "Income not found for Consumption Chicken Eggs.";

  private double consumptionAverageEggsPerLayer;
  private double consumptionVarianceLimit;
  private double hatchingAverageEggsPerLayer;
  private double hatchingVarianceLimit;

  public PoultryEggsRevenueRiskSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    consumptionAverageEggsPerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    consumptionVarianceLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    hatchingAverageEggsPerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    hatchingVarianceLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
  }

  public void test(Scenario scenario) {

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    PoultryEggsRevenueRiskSubTestResult result = new PoultryEggsRevenueRiskSubTestResult();
    revenueRiskResult.setPoultryEggs(result);
    
    Map<Integer, IncomeExpense> incomesWithReceivables = ScenarioUtils.getIncomesWithReceivables(scenario);
    
    result.setSubTestPass(true);
    result.setHasPoultryEggs(false);

    result.setConsumptionVarianceLimit(consumptionVarianceLimit);
    result.setConsumptionAverageEggsPerLayer(consumptionAverageEggsPerLayer);
    result.setConsumptionPass(true);
    
    result.setHatchingVarianceLimit(hatchingVarianceLimit);
    result.setHatchingAverageEggsPerLayer(hatchingAverageEggsPerLayer);
    result.setHatchingPass(true);
    
    computeReportedIncome(incomesWithReceivables, result);
    computeExpectedRevenues(scenario, result, revenueRiskResult);
    checkMissingValues(result, revenueRiskResult); // This must be run after computeReportedIncome and computeExpectedRevenues
    computeVariance(result);
    
    if (!result.getHatchingPass() || !result.getConsumptionPass()) {
      result.setSubTestPass(false);
    }
  }
  
  private void computeReportedIncome(Map<Integer, IncomeExpense> incomesWithReceivables, PoultryEggsRevenueRiskSubTestResult result) {
    for (Integer lineItemId : incomesWithReceivables.keySet()) {
      IncomeExpense incomeExpense = incomesWithReceivables.get(lineItemId);
      LineItem lineItem = incomeExpense.getLineItem();
      if (lineItem == null || lineItem.getCommodityTypeCode() == null || incomeExpense.getTotalAmount() == null) {
        continue;
      }
      if (lineItem.getCommodityTypeCode().equals(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION)) {
        Double consumptionReportedRevenue = result.getConsumptionReportedRevenue() == null ? 0d : result.getConsumptionReportedRevenue();
        result.setConsumptionReportedRevenue(consumptionReportedRevenue + incomeExpense.getTotalAmount());
      } else if (lineItem.getCommodityTypeCode().equals(CommodityTypeCodes.POULTRY_EGGS_HATCHING)) {
        Double hatchingReportedRevenue = result.getHatchingReportedRevenue() == null ? 0d : result.getHatchingReportedRevenue();
        result.setHatchingReportedRevenue(hatchingReportedRevenue + incomeExpense.getTotalAmount());
      }
    }
  }
  
  private void computeExpectedRevenues(Scenario scenario, PoultryEggsRevenueRiskSubTestResult result, RevenueRiskTestResult revenueRiskResult) {
    Double consumptionLayers = null;
    Double consumptionEggsTotal = null;
    Double consumptionEggsDozen = null;
    Double consumptionExpectedRevenue = null;
    
    Double hatchingLayers = null;
    Double hatchingEggsTotal = null;
    Double hatchingEggsDozen = null;
    Double hatchingExpectedRevenue = null;
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for (FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        for(ProductiveUnitCapacity pu : fo.getProductiveUnitCapacities()) {
          String strucGroupCode = pu.getStructureGroupCode();
          if (strucGroupCode != null && 
              (strucGroupCode.equals(StructureGroupCodes.CHICKEN_EGGS_HATCH) || strucGroupCode.equals(StructureGroupCodes.CHICKEN_EGGS_CONSUMPTION))) {
            result.setHasPoultryEggs(true);
            Double productiveUnits = pu.getTotalProductiveCapacityAmount() == null ? 0d : pu.getTotalProductiveCapacityAmount() * partnershipPercent;
            if (strucGroupCode.equals(StructureGroupCodes.CHICKEN_EGGS_CONSUMPTION)) {
              consumptionLayers = consumptionLayers == null ? productiveUnits : consumptionLayers + productiveUnits;
              Double totalEggs = productiveUnits * result.getConsumptionAverageEggsPerLayer();
              consumptionEggsTotal = consumptionEggsTotal == null ? totalEggs : consumptionEggsTotal + totalEggs;
              Double estimatedSoldInDozen = totalEggs / 12;
              consumptionEggsDozen = consumptionEggsDozen == null ? estimatedSoldInDozen : estimatedSoldInDozen + consumptionEggsDozen;
              
              if (fo.getChickenEggsConsumpFMVPrice() != null) {
                consumptionExpectedRevenue = consumptionExpectedRevenue == null ? 0d : consumptionExpectedRevenue;
                consumptionExpectedRevenue += estimatedSoldInDozen * fo.getChickenEggsConsumpFMVPrice();
              } else {
                result.setConsumptionPass(false);
                revenueRiskResult.addErrorMessage(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV, pu.getStructureGroupCode());
              }
            } else if (strucGroupCode.equals(StructureGroupCodes.CHICKEN_EGGS_HATCH)) {
              hatchingLayers = hatchingLayers == null ? productiveUnits : hatchingLayers + productiveUnits;
              Double totalEggs = productiveUnits * result.getHatchingAverageEggsPerLayer();
              hatchingEggsTotal = hatchingEggsTotal == null ? totalEggs : hatchingEggsTotal + totalEggs;
              Double estimatedSoldInDozen = totalEggs / 12;
              hatchingEggsDozen = hatchingEggsDozen == null ? estimatedSoldInDozen : estimatedSoldInDozen + hatchingEggsDozen;
              
              if (fo.getChickenEggsHatchFMVPrice() != null) {
                hatchingExpectedRevenue = hatchingExpectedRevenue == null ? 0d : hatchingExpectedRevenue;
                hatchingExpectedRevenue += estimatedSoldInDozen * fo.getChickenEggsHatchFMVPrice();
              } else {
                result.setHatchingPass(false);
                revenueRiskResult.addErrorMessage(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV, pu.getStructureGroupCode());
              }
            }
          }
        }
      }
    }
    
    result.setConsumptionLayers(consumptionLayers);
    result.setConsumptionEggsTotal(consumptionEggsTotal);
    result.setConsumptionEggsDozen(consumptionEggsDozen);
    result.setConsumptionExpectedRevenue(consumptionExpectedRevenue);
    
    result.setHatchingLayers(hatchingLayers);
    result.setHatchingEggsTotal(hatchingEggsTotal);
    result.setHatchingEggsDozen(hatchingEggsDozen);
    result.setHatchingExpectedRevenue(hatchingExpectedRevenue);
    
    if (consumptionEggsDozen != null && consumptionEggsDozen != 0 && consumptionExpectedRevenue != null) {
      Double consumptionEggsDozenPrice = consumptionExpectedRevenue / consumptionEggsDozen;
      result.setConsumptionEggsDozenPrice(consumptionEggsDozenPrice);
    }
    if (hatchingEggsDozen != null && hatchingEggsDozen != 0 && hatchingExpectedRevenue != null) {
      Double hatchingEggsDozenPrice = hatchingExpectedRevenue / hatchingEggsDozen;
      result.setHatchingEggsDozenPrice(hatchingEggsDozenPrice);
    }
  }
  
  private void checkMissingValues(PoultryEggsRevenueRiskSubTestResult result, RevenueRiskTestResult revenueRiskResult) {
    if (result.getConsumptionLayers() == null && result.getConsumptionReportedRevenue() != null) {
      result.setConsumptionPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_CONSUMP_EGGS_PRODUCTIVE_UNIT_CODE, StructureGroupCodes.CHICKEN_EGGS_CONSUMPTION);
    } else if (result.getConsumptionLayers() != null && result.getConsumptionReportedRevenue() == null) {
      result.setConsumptionPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_CONSUMP_EGGS_INCOME);
    }
    
    if (result.getHatchingLayers() == null && result.getHatchingReportedRevenue() != null) {
      result.setHatchingPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_HATCH_EGGS_PRODUCTIVE_UNIT_CODE, StructureGroupCodes.CHICKEN_EGGS_HATCH);
    } else if (result.getHatchingLayers() != null && result.getHatchingReportedRevenue() == null) {
      result.setHatchingPass(false);
      revenueRiskResult.addErrorMessage(ERROR_MISSING_HATCH_EGGS_INCOME);
    }
  }
  
  private void computeVariance(PoultryEggsRevenueRiskSubTestResult result) {
    Double consumptionExpectedRevenue = result.getConsumptionExpectedRevenue();
    Double consumptionReportedRevenue = result.getConsumptionReportedRevenue();
    Double hatchingExpectedRevenue = result.getHatchingExpectedRevenue();
    Double hatchingReportedRevenue = result.getHatchingReportedRevenue();
    
    if (result.getHatchingPass() && hatchingReportedRevenue != null && hatchingExpectedRevenue != null && hatchingExpectedRevenue != 0) {
      Double variance = (hatchingReportedRevenue / hatchingExpectedRevenue) - 1;
      variance = ReasonabilityTestUtils.roundPercent(variance);
      result.setHatchingVariance(variance);
      if (Math.abs(variance) > hatchingVarianceLimit) {
        result.setHatchingPass(false);
      }
    }
    
    if (result.getConsumptionPass() && consumptionReportedRevenue != null && consumptionExpectedRevenue != null && consumptionExpectedRevenue != 0) {
      Double variance = (consumptionReportedRevenue - consumptionExpectedRevenue) / Math.abs(consumptionExpectedRevenue);
      variance = ReasonabilityTestUtils.roundPercent(variance);
      result.setConsumptionVariance(variance);
      if (Math.abs(variance) > consumptionVarianceLimit) {
        result.setConsumptionPass(false);
      }
    }
  }
}
