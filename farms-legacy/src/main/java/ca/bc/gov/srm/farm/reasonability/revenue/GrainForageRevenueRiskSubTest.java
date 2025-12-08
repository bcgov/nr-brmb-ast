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

import static ca.bc.gov.srm.farm.reasonability.ReasonabilityTest.*;
import static ca.bc.gov.srm.farm.reasonability.revenue.RevenueRiskTest.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.CropItemUtils;
import ca.bc.gov.srm.farm.util.CropUnitUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class GrainForageRevenueRiskSubTest {

  // Forage Seed specific messages
  public static final String ERROR_FORAGE_SEED_NOT_REPORTED_IN_POUNDS =
      "Forage Seed must be reported in pounds. {0} - {1} is reported in {2}.";

  private double forageGrainVarianceLimit;
  private double cattleTonnesFedPerPU;
  private double cattleTonnesBredHeiferFedPerPU;
  private double finishedCattleTonnesFedPerPU;
  private double feederCattleTonnesFedPerPU;

  public GrainForageRevenueRiskSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    forageGrainVarianceLimit = config.getRevenueRiskForageVariance();
    cattleTonnesFedPerPU = config.getRevenueRiskCattleFedPerProductiveUnit();
    cattleTonnesBredHeiferFedPerPU = config.getRevenueRiskCattleBredHeifersFedPerProductiveUnit();
    finishedCattleTonnesFedPerPU = config.getRevenueRiskFinishedCattleFedPerProductiveUnit();
    feederCattleTonnesFedPerPU = config.getRevenueRiskFeederCattleFedPerProductiveUnit();
  }
  

  private void initializeTestResult(RevenueRiskTestResult result) {
    
    result.setForageGrainVarianceLimit(forageGrainVarianceLimit);
    result.setForageGrainPass(true);
    
    result.setForageGrainInventory(new ArrayList<RevenueRiskInventoryItem>());
    result.setForageGrainIncomes(new ArrayList<RevenueRiskIncomeTestResult>());
  }

  public void test(Scenario scenario) {
    
    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    reasonabilityTestResults.getForageConsumers().clear();
    RevenueRiskTestResult result = reasonabilityTestResults.getRevenueRiskTest();
    initializeTestResult(result);
    
    Map<Integer, IncomeExpense> incomesWithReceivables = ScenarioUtils.getIncomesWithReceivables(scenario);
    Map<Integer, IncomeExpense> reportedIncomes = ScenarioUtils.getConsolidatedIncomeExpense(scenario, false);
    Map<String, InventoryItem> reportedReceivables = ScenarioUtils.getConsolidatedAccruals(scenario, InventoryClassCodes.RECEIVABLE, null);
    
    List<CropItem> forageAndGrainItems = findForageAndGrainCrops(scenario);
    
    double amountFedToCattle = calculateAmountsFedToCattle(scenario);
    
    Map<String, RevenueRiskInventoryItem> invItemsMap = createInvItemsMapAndUpdateResultState(forageAndGrainItems, result, amountFedToCattle);
    Collection<RevenueRiskInventoryItem> invItems = invItemsMap.values();
    
    // This step constructs a map of LineItem -> RevenueRiskIncomeTestResult
    Map<Integer, RevenueRiskIncomeTestResult> incomesMap = new HashMap<>();
    Map<Integer, Boolean> missingLineItems = new HashMap<>();
    for (RevenueRiskInventoryItem item : invItems) {
      if (incomesMap.containsKey(item.getLineItemCode())) {
        RevenueRiskIncomeTestResult incTestItem = incomesMap.get(item.getLineItemCode());
        double currentEstimatedIncome = incTestItem.getExpectedRevenue();
        incTestItem.setExpectedRevenue(currentEstimatedIncome + item.getExpectedRevenue());
      } else {
        if (incomesWithReceivables.containsKey(item.getLineItemCode())) {
          RevenueRiskIncomeTestResult incTestItem = new RevenueRiskIncomeTestResult();
          incTestItem.setReportedRevenue(incomesWithReceivables.get(item.getLineItemCode()).getTotalAmount());
          incTestItem.setExpectedRevenue(item.getExpectedRevenue());
          incTestItem.setLineItemCode(item.getLineItemCode());
          incTestItem.setDescription(item.getLineItemDescription());
          incomesMap.put(item.getLineItemCode(), incTestItem);
        } else if (!missingLineItems.containsKey(item.getLineItemCode())) {
          missingLineItems.put(item.getLineItemCode(), true);
          result.setForageGrainPass(false);
          result.addErrorMessage(ERROR_MISSING_INCOME_FOR_LINE_ITEM, item.getLineItemCode(), item.getLineItemDescription());
        }
      }
    }
    
    List<RevenueRiskIncomeTestResult> incomesList = new ArrayList<>();
    for (Integer lineItem : incomesMap.keySet()) {
      RevenueRiskIncomeTestResult incomeTestResult = incomesMap.get(lineItem);
      incomesList.add(incomeTestResult);
      
      Double reportedIncome = incomeTestResult.getReportedRevenue();
      Double estimatedIncome = incomeTestResult.getExpectedRevenue();
      
      if (reportedIncome != null && estimatedIncome != null && estimatedIncome != 0) {
        double variance = (reportedIncome / estimatedIncome) - 1;
        variance = MathUtils.round(variance, PERCENT_DECIMAL_PLACES);
        if (Math.abs(variance) > forageGrainVarianceLimit) {
          incomeTestResult.setPass(false);
          result.setForageGrainPass(false);
        } else {
          incomeTestResult.setPass(true);
        }
        incomeTestResult.setVariance(variance);
      } else if (reportedIncome == null || estimatedIncome == null || (estimatedIncome == 0 && reportedIncome > 0)) {
        incomeTestResult.setPass(false);
        result.setForageGrainPass(false);
      }
    }

    result.getForageGrainInventory().addAll(invItems);
    result.getForageGrainIncomes().addAll(incomesList);
    
    checkForMissingInventoryItems(result, incomesWithReceivables, reportedIncomes, reportedReceivables);
    
    sortTestResults(reasonabilityTestResults);
  }


  private List<CropItem> findForageAndGrainCrops(Scenario scenario) {
    List<CropItem> forageAndGrainItems = new ArrayList<>();
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for (FarmingOperation farmingOperation : programYearScenario.getFarmingYear().getFarmingOperations()) {
        if (farmingOperation.getCropItems() != null) {
          for (CropItem cropItem : farmingOperation.getCropItems()) {
            if (cropItem.isForage() || cropItem.isForageSeed() || cropItem.isGrain()) {
              forageAndGrainItems.add(cropItem);
            }
          }
        }
      }
    }
    return forageAndGrainItems;
  }
  
  /**
   * @param scenario
   * @return
   */
  private double calculateAmountsFedToCattle(Scenario scenario) {
    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    
    double totalAmountFedToCattle = 0.0;
    Map<String, ProductiveUnitCapacity> productiveUnitCapacities = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario);
    
    for (ProductiveUnitCapacity puc : productiveUnitCapacities.values()) {
      String structureGroupCode = puc.getStructureGroupCode();
      boolean isForageConsumer = StructureGroupCodes.CATTLE.equals(structureGroupCode)
          || StructureGroupCodes.CATTLE_BRED_HEIFERS.equals(structureGroupCode)
          || StructureGroupCodes.FEEDER_CATTLE.equals(structureGroupCode)
          || StructureGroupCodes.FINISHED_CATTLE.equals(structureGroupCode);
      
      if (isForageConsumer && puc.getTotalProductiveCapacityAmount() != null) {
        
        double tonnesFedPerProductiveUnit = 0.0;
        if (structureGroupCode.equals(StructureGroupCodes.CATTLE)) {
          tonnesFedPerProductiveUnit = cattleTonnesFedPerPU;
        } else if (structureGroupCode.equals(StructureGroupCodes.CATTLE_BRED_HEIFERS)) {
          tonnesFedPerProductiveUnit = cattleTonnesBredHeiferFedPerPU;
        } else if (structureGroupCode.equals(StructureGroupCodes.FEEDER_CATTLE)) {
          tonnesFedPerProductiveUnit = feederCattleTonnesFedPerPU;
        } else if (structureGroupCode.equals(StructureGroupCodes.FINISHED_CATTLE)) {
          tonnesFedPerProductiveUnit = finishedCattleTonnesFedPerPU;
        }
        
        double amountFedToCattle = puc.getTotalProductiveCapacityAmount() * tonnesFedPerProductiveUnit;
        totalAmountFedToCattle += amountFedToCattle;
        
        ForageConsumer forageConsumer = new ForageConsumer();
        forageConsumer.setStructureGroupCode(structureGroupCode);
        forageConsumer.setStructureGroupCodeDescription(puc.getStructureGroupCodeDescription());
        forageConsumer.setProductiveUnitCapacity(puc.getTotalProductiveCapacityAmount());
        forageConsumer.setQuantityConsumedPerUnit(tonnesFedPerProductiveUnit);
        forageConsumer.setQuantityConsumed(amountFedToCattle);
        reasonabilityTestResults.getForageConsumers().add(forageConsumer);
      }
    }
    
    reasonabilityTestResults.setForageConsumerCapacity(totalAmountFedToCattle);
    return totalAmountFedToCattle;
  }

  private Map<String, RevenueRiskInventoryItem> createInvItemsMapAndUpdateResultState(List<CropItem> forageAndGrainItems,
      RevenueRiskTestResult result, double amountFedToCattle) {
    
    Map<String, List<CropItem>> cropItemListsMap = new HashMap<>();
    for (CropItem item : forageAndGrainItems) {
      if (CropItemUtils.hasNonZeroQuantities(item)) {
        List<CropItem> itemsForThisCode = cropItemListsMap.get(item.getInventoryItemCode());
        if (itemsForThisCode == null) {
          itemsForThisCode = new ArrayList<>();
          cropItemListsMap.put(item.getInventoryItemCode(), itemsForThisCode);
        }
        itemsForThisCode.add(item);
      }
    }
    
    Map<String, RevenueRiskInventoryItem> invItemsMap = new HashMap<>();
    inventoryCodesLoop: for (String inventoryItemCode : cropItemListsMap.keySet()) {
      List<CropItem> itemsForThisCode = cropItemListsMap.get(inventoryItemCode);
      CropItem firstItem = itemsForThisCode.get(0);
      
      double partnershipPercent = ScenarioUtils.getPartnershipPercent(firstItem.getFarmingOperation());
      RevenueRiskInventoryItem inventoryResult = new RevenueRiskInventoryItem();
      invItemsMap.put(inventoryItemCode, inventoryResult);
      inventoryResult.setLineItemCode(firstItem.getLineItem());
      inventoryResult.setLineItemDescription(firstItem.getLineItemDescription());
      inventoryResult.setInventoryItemCode(inventoryItemCode);
      inventoryResult.setInventoryItemCodeDescription(firstItem.getInventoryItemCodeDescription());
      inventoryResult.setExpectedRevenue(0d);
      
      Double price = null;
      String priceCropUnitCode = null;
      
      final String targetUnit;
      final String targetUnitDescription;
      if(firstItem.isForageSeed()) {
        targetUnit = CropUnitCodes.POUNDS;
        targetUnitDescription = CropUnitCodes.POUNDS_DESCRIPTION;
      } else { // Grains and Forage
        targetUnit = CropUnitCodes.TONNES;
        targetUnitDescription = CropUnitCodes.TONNES_DESCRIPTION;
      }
      
      // Forage Seeds MUST be reported in pounds
      if(firstItem.isForageSeed()) {
        for (CropItem item : itemsForThisCode) {
          if(!item.getCropUnitCode().equals(CropUnitCodes.POUNDS)) {

            result.setForageGrainPass(false);
            result.addErrorMessage(ERROR_FORAGE_SEED_NOT_REPORTED_IN_POUNDS, item.getInventoryItemCode(), item.getInventoryItemCodeDescription(), item.getCropUnitCodeDescription());
            continue inventoryCodesLoop;
          }
        }
        
      }

      // Try to get the price end where there is a quantity end and unit is the targetUnit
      for (CropItem item : itemsForThisCode) {
        if(item.getTotalQuantityEnd() != null && item.getTotalQuantityEnd() > 0
            && item.getTotalPriceEnd() != null && item.getTotalPriceEnd() > 0
            && item.getCropUnitCode().equals(targetUnit)) {
          price = item.getTotalPriceEnd();
          priceCropUnitCode = item.getCropUnitCode();
          break;
        }
      }
      
      // Try to get the price end where there is a quantity end
      if(price == null) {
        for (CropItem item : itemsForThisCode) {
          if(item.getTotalQuantityEnd() != null && item.getTotalQuantityEnd() > 0
              && item.getTotalPriceEnd() != null && item.getTotalPriceEnd() > 0) {
            price = item.getTotalPriceEnd();
            priceCropUnitCode = item.getCropUnitCode();
            break;
          }
        }
      }
      
      // Try to get any price end
      if(price == null) {
        for (CropItem item : itemsForThisCode) {
          if(item.getTotalPriceEnd() != null && item.getTotalPriceEnd() > 0) {
            price = item.getTotalPriceEnd();
            priceCropUnitCode = item.getCropUnitCode();
            break;
          }
        }
      }
      
      // Try to get price start
      if(price == null) {
        for (CropItem item : itemsForThisCode) {
          if(item.getTotalPriceStart() != null && item.getTotalPriceStart() > 0) {
            price = item.getTotalPriceStart();
            priceCropUnitCode = item.getCropUnitCode();
            break;
          }
        }
      }
      
      inventoryResult.setCropUnitCode(targetUnit);
      inventoryResult.setCropUnitCodeDescription(targetUnitDescription);

      if(price == null) {
        result.setForageGrainPass(false);
        result.addErrorMessage(ERROR_MISSING_PRICE, inventoryItemCode, inventoryResult.getInventoryItemCodeDescription());
        continue;
      }
      
      Double pricePerUnit = CropUnitUtils.convertPricePerUnit(firstItem.getCropUnitConversion(), price, priceCropUnitCode, targetUnit);
      inventoryResult.setReportedPrice(pricePerUnit);
      
      double quantityStart = 0d;
      double quantityEnd = 0d;
      double quantityProduced = 0d;
      for (CropItem curItem : itemsForThisCode) {
        Double curQuantityStart = CropUnitUtils.convert(firstItem.getCropUnitConversion(), curItem.getTotalQuantityStart(), curItem.getCropUnitCode(), targetUnit);
        Double curQuantityEnd = CropUnitUtils.convert(firstItem.getCropUnitConversion(), curItem.getTotalQuantityEnd(), curItem.getCropUnitCode(), targetUnit);
        Double curQuantityProduced = CropUnitUtils.convert(firstItem.getCropUnitConversion(), curItem.getTotalQuantityProduced(), curItem.getCropUnitCode(), targetUnit);

        // We have checked that at least one of the quantities is not null. If
        // these are all null, it means conversion failed.
        if(curQuantityStart == null && curQuantityEnd == null && curQuantityProduced == null) {
          result.setForageGrainPass(false);
          result.addErrorMessage(ERROR_MISSING_CONVERSIONS, inventoryItemCode, inventoryResult.getInventoryItemCodeDescription(), curItem.getCropUnitCodeDescription(), targetUnitDescription);
          continue inventoryCodesLoop;
        }
        
        quantityStart += curQuantityStart == null ? 0d : curQuantityStart;
        quantityEnd += curQuantityEnd == null ? 0d : curQuantityEnd;
        quantityProduced += curQuantityProduced == null ? 0d : curQuantityProduced;
      }
      
      quantityStart *= partnershipPercent;
      quantityEnd *= partnershipPercent;
      quantityProduced *= partnershipPercent;
      
      inventoryResult.setQuantityStart(MathUtils.roundInventoryQuantity(quantityStart));
      inventoryResult.setQuantityEnd(MathUtils.roundInventoryQuantity(quantityEnd));
      inventoryResult.setQuantityProduced(MathUtils.roundInventoryQuantity(quantityProduced));
      
      double quantitySold = quantityStart + quantityProduced - quantityEnd;
      inventoryResult.setQuantitySold(MathUtils.roundInventoryQuantity(quantitySold));
    }
    
    subtractQuantityUsedForCattleFeed(invItemsMap, amountFedToCattle);
    calculateExpectedRevenues(invItemsMap);
    
    return invItemsMap;
  }

  private void calculateExpectedRevenues(Map<String, RevenueRiskInventoryItem> invItemsMap) {
    for(String inventoryItemCode : invItemsMap.keySet()) {
      RevenueRiskInventoryItem inventoryResult = invItemsMap.get(inventoryItemCode);
      
      Double quantitySold = inventoryResult.getQuantitySold();
      Double priceEnd = inventoryResult.getReportedPrice();
      if(quantitySold != null && priceEnd != null) {
        double expectedRevenue = quantitySold * priceEnd;
        expectedRevenue = MathUtils.roundCurrency(expectedRevenue);
        inventoryResult.setExpectedRevenue(expectedRevenue);
      }
    }
  }
  
  private double subtractQuantityUsedForCattleFeed(Map<String, RevenueRiskInventoryItem> invItemsMap, double amountFedToCattle) {

    double amountFedRemaining = amountFedToCattle;
    for (String inventoryItemCode : ReasonabilityTestUtils.FED_OUT_INVENTORY_CODE_ORDER) {
      RevenueRiskInventoryItem inventoryResult = invItemsMap.get(inventoryItemCode);
      if(inventoryResult == null) {
        continue;
      }
      if(amountFedRemaining == 0.0) {
        inventoryResult.setQuantityConsumed(0.0);
        continue;
      }
      
      if(inventoryResult.getQuantitySold() != null) {
        Double quantityConsumed = null;
        if(inventoryResult.getQuantitySold() < amountFedRemaining) {
          quantityConsumed = inventoryResult.getQuantitySold();
          amountFedRemaining -= quantityConsumed;
          inventoryResult.setQuantitySold(0.0);
        } else {
          quantityConsumed = amountFedRemaining;
          double quantitySold = inventoryResult.getQuantitySold() - quantityConsumed;
          inventoryResult.setQuantitySold(quantitySold);
          amountFedRemaining = 0.0;
        }
        inventoryResult.setQuantityConsumed(quantityConsumed);
      }
    }
    
    return amountFedRemaining;
  }
  
  
  private void checkForMissingInventoryItems(RevenueRiskTestResult result,
      Map<Integer, IncomeExpense> incomesWithReceivables,
      Map<Integer, IncomeExpense> reportedIncomes,
      Map<String, InventoryItem> reportedReceivables) {
    List<RevenueRiskInventoryItem> forageGraintInvItems = result.getForageGrainInventory();
    
    for (Integer lineItem : incomesWithReceivables.keySet()) {
      
      IncomeExpense incomeExpense = incomesWithReceivables.get(lineItem);
      LineItem lineItemObj = incomeExpense.getLineItem();
      boolean isGrainForage = lineItemObj.isForage() || lineItemObj.isForageSeed() || lineItemObj.isGrain();
      
      if(isGrainForage) {
        if(incomeExpense.getTotalAmount() == 0) {
          continue;
        }
        
        boolean foundInventory = false;
        
        for (RevenueRiskInventoryItem invItem : forageGraintInvItems) {
          if(lineItem.equals(invItem.getLineItemCode())) {
            foundInventory = true;
            break;
          }
        }
  
        boolean hasReportedIncome = reportedIncomes.containsKey(lineItem) && reportedIncomes.get(lineItem).getTotalAmount() != 0;
        boolean hasReceivable = reportedReceivables.containsKey(lineItem.toString());
        
        boolean fail = false;
        
        if(!foundInventory) {
          fail = true;
          
          if(hasReportedIncome) {
            result.addErrorMessage(ERROR_MISSING_INVENTORY_FOR_INCOME, lineItem, incomeExpense.getLineItem().getDescription());
          } else if(hasReceivable) { // no income
            result.addErrorMessage(ERROR_MISSING_INVENTORY_FOR_RECEIVABLE, lineItem, incomeExpense.getLineItem().getDescription());
          }
        }
        
        if(!hasReportedIncome && hasReceivable) {
          fail = true;
          result.addErrorMessage(ERROR_MISSING_INCOME_FOR_RECEIVABLE, lineItem, incomeExpense.getLineItem().getDescription());
        }
        
        if(fail) {
          result.setResult(false);
          result.setForageGrainPass(false);
        }
      }
    }
    
  }

  
  private void sortTestResults(ReasonabilityTestResults reasonabilityTestResults) {
    RevenueRiskTestResult revenueRiskResult = reasonabilityTestResults.getRevenueRiskTest();
    Collections.sort(revenueRiskResult.getForageGrainInventory(), new Comparator<RevenueRiskInventoryItem>() {
      @Override
      public int compare(RevenueRiskInventoryItem obj1, RevenueRiskInventoryItem obj2) {
        Integer invItemCode1 = Integer.parseInt(obj1.getInventoryItemCode());
        Integer invItemCode2 = Integer.parseInt(obj2.getInventoryItemCode());
        return Integer.compare(invItemCode1, invItemCode2);
      }
    });
    
    Collections.sort(revenueRiskResult.getForageGrainIncomes(), new Comparator<RevenueRiskIncomeTestResult>() {
      @Override
      public int compare(RevenueRiskIncomeTestResult obj1, RevenueRiskIncomeTestResult obj2) {
        return Integer.compare(obj1.getLineItemCode(), obj2.getLineItemCode());
      }
    });
    
    Collections.sort(reasonabilityTestResults.getForageConsumers(), new Comparator<ForageConsumer>() {
      @Override
      public int compare(ForageConsumer obj1, ForageConsumer obj2) {
        Integer structureGroupCode1 = Integer.parseInt(obj1.getStructureGroupCode());
        Integer structureGroupCode2 = Integer.parseInt(obj2.getStructureGroupCode());
        return Integer.compare(structureGroupCode1, structureGroupCode2);
      }
    });
  }
}
