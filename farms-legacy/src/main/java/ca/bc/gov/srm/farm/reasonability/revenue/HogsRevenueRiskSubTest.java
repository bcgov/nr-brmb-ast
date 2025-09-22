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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.HogsRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.LivestockItemUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class HogsRevenueRiskSubTest {

  private static final String[] HOGS_FEEDER_LIGHTEST_TO_HEAVIEST_ARRAY = {
      "8763", "8764", "8765", "8766", "8767", "8768", "8769", "8770" 
  };
  private static final List<String> HOGS_FEEDER_LIGHTEST_TO_HEAVIEST;
  static {
    HOGS_FEEDER_LIGHTEST_TO_HEAVIEST = Arrays.asList(HOGS_FEEDER_LIGHTEST_TO_HEAVIEST_ARRAY);
  }

  public static final String ERROR_MISSING_PRODUCTIVE_UNITS = 
      "Productive Units not found for Hogs. Expected 123 - Hogs, Farrow to Finish or 124 - Hogs/Feeders.";
  public static final String ERROR_MISSING_INCOME = 
      "Income not found for Hogs. Expected 341 - Swine/Hogs";
  public static final String ERROR_MISSING_INVENTORY = 
      "Inventory not found for Hogs.";
  public static final String ERROR_MISSING_HEAVIEST_HOG_FEEDER_PRICE = 
      "FMV Price not found for heaviest hog inventory.";
  public static final String ERROR_MISSING_HOG_BIRTH_PRICE = 
      "Price not found for 8763 - Hogs; Feeder; Birth - 18 lbs. Cannot calculate assumed purchase.";
  public static final String ERROR_MISSING_REQUIRED_INVENTORY = 
      "Inventory not found for {0} - {1}.";
  public static final String ERROR_MISSING_HOG_EXPENSE = 
      "Expense not found for Hogs. Expected 341 - Swine/Hogs. Cannot calculate assumed purchase.";
  public static final String ERROR_MISSING_PRICE = 
      "Price not found for {0} - {1}. Cannot calculate assumed purchase.";

  private double farrowToFinishDeathRate;
  private double farrowToFinishVarianceLimit;
  private double feederVarianceLimit;
  private double farrowToFinishBirthsPerCycle;
  private double farrowToFinishBirthCyclesPerYear;

  public HogsRevenueRiskSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    farrowToFinishDeathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    farrowToFinishBirthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    farrowToFinishBirthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    feederVarianceLimit = config.getRevenueRiskHogsFeederVariance();
  }

  public void test(Scenario scenario) {

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    HogsRevenueRiskSubTestResult result = new HogsRevenueRiskSubTestResult();
    revenueRiskResult.setHogs(result);

    result.setDeathRate(farrowToFinishDeathRate);
    result.setBirthsPerCycle(farrowToFinishBirthsPerCycle);
    result.setBirthCyclesPerYear(farrowToFinishBirthCyclesPerYear);
    
    boolean hasHogs = ScenarioUtils.hasCommdityType(scenario, CommodityTypeCodes.HOG);
    result.setHasHogs(hasHogs);
    
    List<ReasonabilityTestResultMessage> errors = new ArrayList<>();
    
    boolean hogsPass = true;
    boolean farrowToFinishOperation = false;
    boolean feederOperation = false;
    
    if(hasHogs) {
      
      Map<String, ProductiveUnitCapacity> productiveUnitsMap = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario);
      Map<Integer, IncomeExpense> incomesWithReceivables = ScenarioUtils.getIncomesWithReceivables(scenario, CommodityTypeCodes.HOG);
      Map<Integer, IncomeExpense> expensesMap = ScenarioUtils.getConsolidatedIncomeExpense(scenario, true, CommodityTypeCodes.HOG);
      Map<String, List<LivestockItem>> livestockInventoryMap = ScenarioUtils.getLivestockInventoryMap(scenario, CommodityTypeCodes.HOG, true);
      
      double sowsBreeding = getProductiveUnitAmount(productiveUnitsMap, StructureGroupCodes.HOGS_FARROW_TO_FINISH);
      double feederProductiveUnits = getProductiveUnitAmount(productiveUnitsMap, StructureGroupCodes.HOGS_FEEDER);
      result.setFeederProductiveUnits(feederProductiveUnits);
      
      farrowToFinishOperation = sowsBreeding > 0;
      feederOperation = !farrowToFinishOperation && feederProductiveUnits > 0;
      
      boolean hasProductiveUnits = farrowToFinishOperation || feederOperation;
      if(!hasProductiveUnits) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_PRODUCTIVE_UNITS);
      }
      
      double hogExpense = getTotalIncomeExpense(expensesMap);
      result.setReportedExpenses(hogExpense);
      
      if(farrowToFinishOperation) {
        handleFarrowToFinishOperation(revenueRiskResult, errors, livestockInventoryMap, sowsBreeding, hogExpense);
      } else if(feederOperation) {
        handleFeederOperation(revenueRiskResult, errors, livestockInventoryMap, hogExpense);
      }
      
      setZeroesWhereNull(result);
      
      handleInventory(result, errors, livestockInventoryMap);

      double totalPurchaseCount = result.getTotalPurchaseCount();
      double totalBirthsAllCycles = result.getTotalBirthsAllCycles();
      double deaths = result.getDeaths();
      double totalQuantityStart = result.getTotalQuantityStart();
      double totalQuantityEnd = result.getTotalQuantityEnd();
      Double heaviestHogFmvPrice = result.getHeaviestHogFmvPrice();
      
      double expectedSold = totalQuantityStart + totalBirthsAllCycles + totalPurchaseCount - deaths - totalQuantityEnd;
      if(expectedSold < 0) {
        expectedSold = 0;
      }
      result.setExpectedSold(expectedSold);
      
      Double expectedRevenue = null;
      if(heaviestHogFmvPrice == null) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_HEAVIEST_HOG_FEEDER_PRICE);        
      } else {
        expectedRevenue = MathUtils.roundCurrency(expectedSold * heaviestHogFmvPrice);
        result.setExpectedRevenue(expectedRevenue);
      }
      
      double hogIncome = getTotalIncomeExpense(incomesWithReceivables);
      result.setReportedRevenue(hogIncome);
      boolean hasIncome = hogIncome > 0;
      if(!hasIncome) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_INCOME);
      }
      
      if(hasIncome && expectedRevenue != null && expectedRevenue != 0) {
        double revenueVariance = MathUtils.round(hogIncome / expectedRevenue - 1, 3);
        result.setRevenueVariance(revenueVariance);
        Double varianceLimit = result.getVarianceLimit();
        hogsPass = errors.isEmpty()
            && Math.abs(revenueVariance) <= varianceLimit;
      }
      
      sortTestResults(result);
    }
    
    setZeroesWhereNull(result);
    
    if(!errors.isEmpty()) {
      hogsPass = false;
      revenueRiskResult.getErrorMessages().addAll(errors);
    }
    
    result.setHogsPass(hogsPass);
    result.setFarrowToFinishOperation(farrowToFinishOperation);
    result.setFeederOperation(feederOperation);
  }


  private void handleFarrowToFinishOperation(RevenueRiskTestResult revenueRiskResult,
      List<ReasonabilityTestResultMessage> errors, Map<String, List<LivestockItem>> livestockInventoryMap, double sowsBreeding, double hogExpense) {
    
    HogsRevenueRiskSubTestResult result = revenueRiskResult.getHogs();
    result.setVarianceLimit(farrowToFinishVarianceLimit);
    
    HogInventoryDetails boarDetails = getHogInventoryDetails(livestockInventoryMap, InventoryItemCodes.HOGS_BOARS_BREEDING, true);
    double boarPurchaseCount = 0;
    double sowPurchaseCount = 0;
    if(!boarDetails.found) {
      ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_REQUIRED_INVENTORY, InventoryItemCodes.HOGS_BOARS_BREEDING, InventoryItemCodes.HOGS_BOARS_BREEDING_DESCRIPTION);
    } else if(boarDetails.quantityEnd > boarDetails.quantityStart) {
      boarPurchaseCount = boarDetails.quantityEnd - boarDetails.quantityStart;
      if(boarPurchaseCount < 0) {
        boarPurchaseCount = 0;
      }
    }
    result.setBoarPurchaseCount(boarPurchaseCount);
    
    if(boarPurchaseCount > 0 && hogExpense <= 0) {
      ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_HOG_EXPENSE);
    }
    
    if(hogExpense > 0) {
      
      Double boarPurchaseExpense = null;
      double remainingExpense = hogExpense;
      if(boarPurchaseCount == 0) {
        boarPurchaseExpense = 0.0;
      } else { // assume boars purchased
        if(boarDetails.fmvPrice == null) {
          ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_PRICE, InventoryItemCodes.HOGS_BOARS_BREEDING, InventoryItemCodes.HOGS_BOARS_BREEDING_DESCRIPTION);
        } else {
          boarPurchaseExpense = boarPurchaseCount * boarDetails.fmvPrice;
          if(boarPurchaseExpense > hogExpense) {
            boarPurchaseExpense = hogExpense;
          }
          remainingExpense = hogExpense - boarPurchaseExpense;
        }
      }
      
      HogInventoryDetails sowDetails = getHogInventoryDetails(livestockInventoryMap, InventoryItemCodes.HOGS_SOWS_BREEDING, true);
      
      Double sowPurchaseExpense = null;
      // if sow inventory was found and boar purchase expense successfully calculated, calculate sow purchase
      if(!sowDetails.found) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_REQUIRED_INVENTORY, InventoryItemCodes.HOGS_SOWS_BREEDING, InventoryItemCodes.HOGS_SOWS_BREEDING_DESCRIPTION);
      } else if(boarPurchaseExpense != null) {
        if(remainingExpense > 0) {
          sowPurchaseExpense = remainingExpense;
          if(sowDetails.fmvPrice == null) {
            ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_PRICE, InventoryItemCodes.HOGS_SOWS_BREEDING, InventoryItemCodes.HOGS_SOWS_BREEDING_DESCRIPTION);
          } else {
            sowPurchaseCount = sowPurchaseExpense / sowDetails.fmvPrice;
            sowPurchaseCount = MathUtils.roundToWholeNumber(sowPurchaseCount);
          }
        }
      }
      
      result.setBoarPurchasePrice(boarDetails.fmvPrice);
      result.setBoarPurchaseExpense(boarPurchaseExpense);
      result.setSowPurchasePrice(sowDetails.fmvPrice); 
      result.setSowPurchaseExpense(sowPurchaseExpense);
    }
    
    double totalPurchaseCount = boarPurchaseCount + sowPurchaseCount;
    result.setSowPurchaseCount(sowPurchaseCount);
    result.setTotalPurchaseCount(totalPurchaseCount);
    
    double totalBirthsPerCycle = sowsBreeding * farrowToFinishBirthsPerCycle;
    double birthsAllCycles = totalBirthsPerCycle * farrowToFinishBirthCyclesPerYear;
    double deaths = MathUtils.roundToWholeNumber(birthsAllCycles * farrowToFinishDeathRate);
    
    result.setSowsBreeding(sowsBreeding);
    result.setTotalBirthsPerCycle(totalBirthsPerCycle);
    result.setTotalBirthsAllCycles(birthsAllCycles);
    result.setDeaths(deaths);
  }


  private void handleFeederOperation(RevenueRiskTestResult revenueRiskResult,
      List<ReasonabilityTestResultMessage> errors, Map<String, List<LivestockItem>> livestockInventoryMap, double hogExpense) {
    
    HogsRevenueRiskSubTestResult result = revenueRiskResult.getHogs();
    result.setVarianceLimit(feederVarianceLimit);
    
    // weanling is another name for a young pig, from birth to 18 pounds
    HogInventoryDetails weanlingDetails = getHogInventoryDetails(livestockInventoryMap, InventoryItemCodes.HOGS_FEEDER_BIRTH, false);
    result.setWeanlingPurchasePrice(weanlingDetails.fmvPrice);
    
    if(hogExpense > 0) {
      double weanlingPurchaseCount = 0;
      
      Double weanlingPurchaseExpense = hogExpense;
      if(weanlingDetails.fmvPrice == null) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_HOG_BIRTH_PRICE);
      } else {
        weanlingPurchaseCount = MathUtils.roundToWholeNumber(weanlingPurchaseExpense / weanlingDetails.fmvPrice);
      }
      
      double totalPurchaseCount = weanlingPurchaseCount;
      
      result.setWeanlingPurchaseCount(weanlingPurchaseCount);
      result.setWeanlingPurchaseExpense(weanlingPurchaseExpense);
      result.setTotalPurchaseCount(totalPurchaseCount);
    }
    
  }


  private void handleInventory(HogsRevenueRiskSubTestResult result,
      List<ReasonabilityTestResultMessage> errors, Map<String, List<LivestockItem>> livestockInventoryMap) {
    if(livestockInventoryMap.isEmpty()) {
      ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_INVENTORY);
    }
    
    List<String> inventoryItemCodeList = new ArrayList<>();
    for(String inventoryItemCode : livestockInventoryMap.keySet()) {
      List<LivestockItem> hogLivestockItemList = livestockInventoryMap.get(inventoryItemCode);
      for(LivestockItem item : hogLivestockItemList) {
        if(isPriceNeeded(inventoryItemCode) || LivestockItemUtils.hasNonZeroQuantities(item)) {
          if(!inventoryItemCodeList.contains(inventoryItemCode)) {
            inventoryItemCodeList.add(inventoryItemCode);
          }
        }
      }
    }
    Collections.sort(inventoryItemCodeList);
    
    double totalQuantityStart = 0;
    double totalQuantityEnd = 0;
    
    String heaviestHogInventoryItemCode = null;
    Double heaviestHogFmvPrice = null;
    for(String curInventoryItemCode : inventoryItemCodeList) {
      boolean preferPriceEnd = !curInventoryItemCode.equals(InventoryItemCodes.HOGS_FEEDER_BIRTH);
      HogInventoryDetails hogDetails = getHogInventoryDetails(livestockInventoryMap, curInventoryItemCode, preferPriceEnd);
      totalQuantityStart += hogDetails.quantityStart;
      totalQuantityEnd += hogDetails.quantityEnd;
      
      if(HOGS_FEEDER_LIGHTEST_TO_HEAVIEST.contains(curInventoryItemCode)) {
        if(heaviestHogInventoryItemCode == null
            || weightRank(curInventoryItemCode) > weightRank(heaviestHogInventoryItemCode)) {
          heaviestHogInventoryItemCode = curInventoryItemCode;
          heaviestHogFmvPrice = hogDetails.fmvPrice;
        }
      }
      
      RevenueRiskInventoryItem inventoryResult = new RevenueRiskInventoryItem();
      inventoryResult.setInventoryItemCode(curInventoryItemCode);
      inventoryResult.setInventoryItemCodeDescription(hogDetails.inventoryItemCodeDescription);
      inventoryResult.setQuantityStart(hogDetails.quantityStart);
      inventoryResult.setQuantityEnd(hogDetails.quantityEnd);
      inventoryResult.setFmvPrice(hogDetails.fmvPrice);
      
      result.getInventory().add(inventoryResult);
    }
    
    result.setTotalQuantityStart(totalQuantityStart);
    result.setTotalQuantityEnd(totalQuantityEnd);
    result.setHeaviestHogFmvPrice(heaviestHogFmvPrice);
  }


  private HogInventoryDetails getHogInventoryDetails(Map<String, List<LivestockItem>> livestockInventoryMap, String hogInventoryItemCode, boolean usePriceEnd) {
    List<LivestockItem> hogLivestockItemList = livestockInventoryMap.get(hogInventoryItemCode);
    HogInventoryDetails hogDetails = new HogInventoryDetails();
    
    if(hogLivestockItemList ==  null) {
      hogDetails.quantityStart = 0;
      hogDetails.quantityEnd = 0;
      hogDetails.found = false;
    } else {
      hogDetails.found = true;
      
      for(LivestockItem item : hogLivestockItemList) {
        if(hogDetails.inventoryItemCodeDescription == null) {
          hogDetails.inventoryItemCodeDescription = item.getInventoryItemCodeDescription();
        }
        
        hogDetails.quantityStart += ScenarioUtils.applyPartnershipPercent(item, item.getTotalQuantityStart());
        hogDetails.quantityEnd += ScenarioUtils.applyPartnershipPercent(item, item.getTotalQuantityEnd());
        
        if(usePriceEnd) {
          if(hogDetails.fmvPrice == null && MathUtils.getPrimitiveValue(item.getFmvEnd()) > 0) {
            hogDetails.fmvPrice = item.getFmvEnd();
          }
        } else {
          if(hogDetails.fmvPrice == null && MathUtils.getPrimitiveValue(item.getFmvStart()) > 0) {
            hogDetails.fmvPrice = item.getFmvStart();
          }
        }
      }
      
    }
    return hogDetails;
  }

  private double getProductiveUnitAmount(Map<String, ProductiveUnitCapacity> productiveUnitsMap, String productiveUnitCode) {
    ProductiveUnitCapacity productiveUnitCapacity = productiveUnitsMap.get(productiveUnitCode);
    double productiveUnitsAmount = 0.0;
    if(productiveUnitCapacity != null && productiveUnitCapacity.getTotalProductiveCapacityAmount() > 0) {
      productiveUnitsAmount = productiveUnitCapacity.getTotalProductiveCapacityAmount();
    }
    return productiveUnitsAmount;
  }

  private double getTotalIncomeExpense(Map<Integer, IncomeExpense> incomeExpenseMap) {
    double hogIncome = 0;
    for(Integer lineItem : incomeExpenseMap.keySet()) {
      IncomeExpense incomeExpense = incomeExpenseMap.get(lineItem);
      if(CommodityTypeCodes.HOG.equals(incomeExpense.getLineItem().getCommodityTypeCode())) {
        hogIncome += incomeExpense.getTotalAmount();
      }
    }
    return hogIncome;
  }

  private void setZeroesWhereNull(HogsRevenueRiskSubTestResult result) {
    result.setReportedExpenses(MathUtils.getPrimitiveValue(result.getReportedExpenses()));
    
    result.setBoarPurchaseCount(MathUtils.getPrimitiveValue(result.getBoarPurchaseCount()));
    result.setBoarPurchaseExpense(MathUtils.getPrimitiveValue(result.getBoarPurchaseExpense()));
    result.setSowPurchaseCount(MathUtils.getPrimitiveValue(result.getSowPurchaseCount()));
    result.setSowPurchaseExpense(MathUtils.getPrimitiveValue(result.getSowPurchaseExpense()));
    
    result.setSowsBreeding(MathUtils.getPrimitiveValue(result.getSowsBreeding()));
    result.setTotalBirthsPerCycle(MathUtils.getPrimitiveValue(result.getTotalBirthsPerCycle()));
    result.setTotalBirthsAllCycles(MathUtils.getPrimitiveValue(result.getTotalBirthsAllCycles()));
    result.setDeaths(MathUtils.getPrimitiveValue(result.getDeaths()));
    
    result.setFeederProductiveUnits(MathUtils.getPrimitiveValue(result.getFeederProductiveUnits()));
    result.setWeanlingPurchaseCount(MathUtils.getPrimitiveValue(result.getWeanlingPurchaseCount()));
    result.setWeanlingPurchaseExpense(MathUtils.getPrimitiveValue(result.getWeanlingPurchaseExpense()));
    
    result.setTotalPurchaseCount(MathUtils.getPrimitiveValue(result.getTotalPurchaseCount()));
    
    result.setTotalQuantityStart(MathUtils.getPrimitiveValue(result.getTotalQuantityStart()));
    result.setTotalQuantityEnd(MathUtils.getPrimitiveValue(result.getTotalQuantityEnd()));
    result.setExpectedSold(MathUtils.getPrimitiveValue(result.getExpectedSold()));
    result.setReportedRevenue(MathUtils.getPrimitiveValue(result.getReportedRevenue()));
  }

  private int weightRank(String curInventoryItemCode) {
    return HOGS_FEEDER_LIGHTEST_TO_HEAVIEST.indexOf(curInventoryItemCode);
  }

  private boolean isPriceNeeded(String inventoryItemCode) {
    return inventoryItemCode.equals(InventoryItemCodes.HOGS_BOARS_BREEDING)
        || inventoryItemCode.equals(InventoryItemCodes.HOGS_SOWS_BREEDING)
        || inventoryItemCode.equals(InventoryItemCodes.HOGS_FEEDER_BIRTH);
  }
  
  private void sortTestResults(HogsRevenueRiskSubTestResult result) {
    Collections.sort(result.getInventory(), new Comparator<RevenueRiskInventoryItem>() {
      @Override
      public int compare(RevenueRiskInventoryItem obj1, RevenueRiskInventoryItem obj2) {
        Integer invItemCode1 = Integer.parseInt(obj1.getInventoryItemCode());
        Integer invItemCode2 = Integer.parseInt(obj2.getInventoryItemCode());
        return Integer.compare(invItemCode1, invItemCode2);
      }
    });
  }
  
  private class HogInventoryDetails {
    
    public HogInventoryDetails() {
    }
    
    boolean found;
    String inventoryItemCodeDescription;
    Double fmvPrice;
    double quantityStart = 0;
    double quantityEnd = 0;
  }
}
