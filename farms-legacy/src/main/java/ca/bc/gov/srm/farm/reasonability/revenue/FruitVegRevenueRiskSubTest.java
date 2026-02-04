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

import static ca.bc.gov.srm.farm.reasonability.revenue.RevenueRiskTest.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskFruitVegItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.CropUnitUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class FruitVegRevenueRiskSubTest {

  public static final String ERROR_FRUIT_VEG_MISSING_VARIANCE_LIMIT =
      "Variance Limit not found for: {0} - {1}.";
  public static final String ERROR_FRUIT_VEG_TYPE_MISSING_LINE_ITEM =
      "Income not found for: {0}.";
  public static final String ERROR_MISSING_INVENTORY_FOR_APPLE_RECEIVABLE =
      "Crop not found for Apple Receivable.";
  public static final String ERROR_MISSING_FRUIT_VEG_TYPE_CODE = 
      "Inventory code has Commodity Type Fruit & Vegetables but is missing Fruit & Veg Type: {0} - {1}.";

  public static final String TARGET_UNIT = CropUnitCodes.POUNDS;
  public static final String TARGET_UNIT_DESCRIPTION = CropUnitCodes.POUNDS_DESCRIPTION;

  public void test(Scenario scenario) {
    
    RevenueRiskTestResult result = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    result.setFruitVegTestPass(true);
    List<ReasonabilityTestResultMessage> errors = new ArrayList<>();
    
    Map<Integer, IncomeExpense> incomesWithReceivables = ScenarioUtils.getIncomesWithReceivables(scenario);
    Map<Integer, IncomeExpense> reportedIncomes = ScenarioUtils.getConsolidatedIncomeExpense(scenario, false);
    Map<String, InventoryItem> reportedReceivables = ScenarioUtils.getConsolidatedAccruals(scenario, InventoryClassCodes.RECEIVABLE, null);
    Map<String, InventoryItem> receivablesIncludingNoChangeInValue = ScenarioUtils.getConsolidatedAccruals(scenario, InventoryClassCodes.RECEIVABLE, CommodityTypeCodes.FRUIT_VEG, false);
    
    Map<String, List<CropItem>> cropInventoryMap = ScenarioUtils.getCropInventoryMap(scenario, CommodityTypeCodes.FRUIT_VEG, false);
    
    Set<String> inventoryCodes = new TreeSet<>();
    inventoryCodes.addAll(cropInventoryMap.keySet());

    Map<String, List<RevenueRiskInventoryItem>> inventoryResultsByFruitVegType = new HashMap<>();
    List<RevenueRiskInventoryItem> fruitVegInventory = result.getFruitVegInventory();

    for(String inventoryItemCode : inventoryCodes) {
      List<CropItem> itemsForThisCode = cropInventoryMap.get(inventoryItemCode);
      CropItem firstItem = itemsForThisCode.get(0);
      String fruitVegTypeCode = firstItem.getFruitVegTypeCode();
      
      RevenueRiskInventoryItem inventoryResult = getInventoryResultFromCropItems(itemsForThisCode, errors);
      fruitVegInventory.add(inventoryResult);
      
      if(fruitVegTypeCode != null) {
        List<RevenueRiskInventoryItem> inventoryForFruitVegType = inventoryResultsByFruitVegType.get(fruitVegTypeCode);
        if(inventoryForFruitVegType == null) {
          inventoryForFruitVegType = new ArrayList<>();
          inventoryResultsByFruitVegType.put(fruitVegTypeCode, inventoryForFruitVegType);
        }
        inventoryForFruitVegType.add(inventoryResult);
      }
    }
    
    
    Set<String> fruitVegTypeCodes = new TreeSet<>();
    fruitVegTypeCodes.addAll(inventoryResultsByFruitVegType.keySet());
     
    for (String fruitVegTypeCode : fruitVegTypeCodes) {
      List<RevenueRiskInventoryItem> itemsForThisCode = inventoryResultsByFruitVegType.get(fruitVegTypeCode);
      RevenueRiskInventoryItem firstItem = itemsForThisCode.get(0);
      boolean isApple = fruitVegTypeCode.equals(FruitVegTypeCodes.APPLE);
      
      RevenueRiskFruitVegItemTestResult fruitVegTypeResult = new RevenueRiskFruitVegItemTestResult();
      fruitVegTypeResult.setPass(true);
      fruitVegTypeResult.setCropUnitCode(TARGET_UNIT);
      fruitVegTypeResult.setFruitVegTypeCode(fruitVegTypeCode);
      fruitVegTypeResult.setFruitVegTypeDesc(firstItem.getFruitVegTypeCodeDescription());
      fruitVegTypeResult.setIsApple(isApple);
      result.getFruitVegResults().add(fruitVegTypeResult);
      
      double expectedRevenue = 0;
      double quantityProduced = 0;
      
      for(RevenueRiskInventoryItem inventoryResult : itemsForThisCode) {
        if (inventoryResult.getVarianceLimit() == null) {
          ReasonabilityTestUtils.addErrorMessage(errors, ERROR_FRUIT_VEG_MISSING_VARIANCE_LIMIT,
              inventoryResult.getFruitVegTypeCode(), inventoryResult.getFruitVegTypeCodeDescription());
            
          result.setFruitVegTestPass(false);
          fruitVegTypeResult.setPass(false);
        } else {
          fruitVegTypeResult.setVarianceLimit(inventoryResult.getVarianceLimit());
        }
        quantityProduced += inventoryResult.getQuantityProduced();
        
        if(inventoryResult.getExpectedRevenue() != null) {
          expectedRevenue += inventoryResult.getExpectedRevenue();
        }
      }
      
      fruitVegTypeResult.setQuantityProduced(quantityProduced);
      fruitVegTypeResult.setExpectedRevenue(expectedRevenue);
      
      // calculate average expected price for Fruit & Veg Type
      boolean missingPrice = false;
      double priceForType = 0.0;
      for(RevenueRiskInventoryItem inventoryResult : itemsForThisCode) {
        if(inventoryResult.getFmvPrice() == null) {
          missingPrice = true;
        } else {
          priceForType += inventoryResult.getQuantityProduced() / quantityProduced * inventoryResult.getFmvPrice();
        }
      }
      
      // if one price is missing we can't calculate the average expected price
      if(!missingPrice) {
        fruitVegTypeResult.setExpectedPrice(priceForType);
      }
      
      Double reportedRevenue = getRevenueForType(fruitVegTypeCode, incomesWithReceivables, receivablesIncludingNoChangeInValue);
      
      if(reportedRevenue == 0.0) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_FRUIT_VEG_TYPE_MISSING_LINE_ITEM,
            fruitVegTypeResult.getFruitVegTypeDesc());
      } else {
        fruitVegTypeResult.setReportedRevenue(reportedRevenue);
      }
      
      boolean pass = false;
      if(reportedRevenue != 0.0 && expectedRevenue != 0) {
        double variance = (reportedRevenue - expectedRevenue) / Math.abs(expectedRevenue);
        variance = ReasonabilityTestUtils.roundPercent(variance);
        fruitVegTypeResult.setVariance(variance);
        
        pass = fruitVegTypeResult.getVarianceLimit() != null
            && Math.abs(variance) <= fruitVegTypeResult.getVarianceLimit();
      }
      
      fruitVegTypeResult.setPass(pass);
      if(!pass) {
        result.setFruitVegTestPass(false);
      }
      
    }
    
    checkMissingInventoryItems(result, incomesWithReceivables, reportedIncomes, reportedReceivables, errors);
    checkMissingAppleInventoryItems(result, receivablesIncludingNoChangeInValue);
    
    if(!errors.isEmpty()) {
      result.setFruitVegTestPass(false);
      result.getErrorMessages().addAll(errors);
    }
    
    sortTestResults(result);
  }

  private RevenueRiskInventoryItem getInventoryResultFromCropItems(List<CropItem> itemsForThisInventoryCode,
      List<ReasonabilityTestResultMessage> errors) {
    
    CropItem firstItem = itemsForThisInventoryCode.get(0);
    
    String inventoryItemCode = firstItem.getInventoryItemCode();
    String inventoryItemCodeDescription = firstItem.getInventoryItemCodeDescription();
    String fruitVegTypeCode = firstItem.getFruitVegTypeCode();
    
    boolean isApple = FruitVegTypeCodes.APPLE.equals(fruitVegTypeCode);
    
    if(fruitVegTypeCode == null) {
      ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_FRUIT_VEG_TYPE_CODE,
          inventoryItemCode, inventoryItemCodeDescription);
    }
    
    RevenueRiskInventoryItem inventoryResult = new RevenueRiskInventoryItem();
    inventoryResult.setInventoryItemCode(inventoryItemCode);
    inventoryResult.setInventoryItemCodeDescription(inventoryItemCodeDescription);
    inventoryResult.setCropUnitCode(TARGET_UNIT);
    inventoryResult.setCropUnitCodeDescription(TARGET_UNIT_DESCRIPTION);
    inventoryResult.setFruitVegTypeCode(fruitVegTypeCode);
    inventoryResult.setFruitVegTypeCodeDescription(firstItem.getFruitVegTypeCodeDescription());
    inventoryResult.setLineItemCode(firstItem.getLineItem());
    inventoryResult.setLineItemDescription(firstItem.getLineItemDescription());
    inventoryResult.setVarianceLimit(firstItem.getRevenueVarianceLimit());
    
    double totalQuantityProduced = 0;
    
    for(CropItem item : itemsForThisInventoryCode) {
      Double curQuantityProduced = MathUtils.getPrimitiveValue(item.getTotalQuantityProduced());
      
      if(!item.getCropUnitCode().equals(TARGET_UNIT)) {
        curQuantityProduced = CropUnitUtils.convert(item.getCropUnitConversion(), item.getTotalQuantityProduced(), item.getCropUnitCode(), TARGET_UNIT);
        if (curQuantityProduced == null) {
          ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_CONVERSIONS,
              inventoryItemCode, inventoryItemCodeDescription, item.getCropUnitCodeDescription(), TARGET_UNIT_DESCRIPTION);
        }
      }
 
      if(curQuantityProduced != null) {
        totalQuantityProduced += MathUtils.round(curQuantityProduced * ScenarioUtils.getPartnershipPercent(item.getFarmingOperation()), 3);
      }
      
      Double fmv;
      if(isApple) {
        fmv = item.getFarmingOperation().getGalaAppleFmvPrice();
      } else {
        fmv = item.getFmvEnd();
      }
      
      if(!item.getCropUnitCode().equals(TARGET_UNIT) && fmv != null && fmv != 0) {
        fmv = CropUnitUtils.convertPricePerUnit(item.getCropUnitConversion(), fmv, item.getCropUnitCode(), TARGET_UNIT);
      }
      
      if (fmv == null) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_INVENTORY_MISSING_FMV, item.getInventoryItemCode(), item.getInventoryItemCodeDescription());
      } else if (inventoryResult.getFmvPrice() == null || item.getCropUnitCode().equals(TARGET_UNIT)) { // If inventoryResult has a converted price it will be overwritten by the price per pound.
        fmv = MathUtils.roundCurrency(fmv);
        inventoryResult.setFmvPrice(fmv);
      }
    }
    
    // If partnership percent reduces the quantity to zero then set it to the minimum value,
    // otherwise it will cause a divide by zero error (NaN). This is for a workaround so that
    // this inventory code will show up on the Negative Margins screen.
    if(totalQuantityProduced == 0) {
      totalQuantityProduced = InventoryItem.MINIMUM_QUANTITY;
    }
    
    inventoryResult.setQuantityProduced(totalQuantityProduced);
    
    if (inventoryResult.getFmvPrice() != null) {
      inventoryResult.setExpectedRevenue(MathUtils.roundCurrency(totalQuantityProduced * inventoryResult.getFmvPrice()));
    }
    
    return inventoryResult;
  }

  private Double getRevenueForType(String fruitVegTypeCode, Map<Integer, IncomeExpense> incomesWithReceivables,
      Map<String, InventoryItem> receivablesIncludingNoChangeInValue) {
    
    boolean isApple = fruitVegTypeCode.equals(FruitVegTypeCodes.APPLE);
    Double reportedRevenue = 0.0;
    if(isApple) { // Inventory is not required for Apples. Instead, receivable end value is required.
      for(String inventoryItemCode : receivablesIncludingNoChangeInValue.keySet()) {
        InventoryItem receivableItem = receivablesIncludingNoChangeInValue.get(inventoryItemCode);
        if(fruitVegTypeCode.equals(receivableItem.getFruitVegTypeCode())) {
          if(receivableItem.getTotalEndOfYearAmount() != null) {
            reportedRevenue += receivableItem.getTotalEndOfYearAmount();
          }
        }
      }
    } else { // Revenue for non-apple
      for (Integer lineItem : incomesWithReceivables.keySet()) {
        IncomeExpense incExp = incomesWithReceivables.get(lineItem);
        if (incExp.getLineItem() != null && fruitVegTypeCode.equals(incExp.getLineItem().getFruitVegTypeCode())) {
          reportedRevenue += incExp.getTotalAmount();
        }
      }
    }
    return reportedRevenue;
  }
  
  private void checkMissingInventoryItems(RevenueRiskTestResult result,
      Map<Integer, IncomeExpense> incomesWithReceivables,
      Map<Integer, IncomeExpense> reportedIncomes,
      Map<String, InventoryItem> reportedReceivables,
      List<ReasonabilityTestResultMessage> errors) {
    List<RevenueRiskFruitVegItemTestResult> recordedFruitVegItems = result.getFruitVegResults();
    
    for (Integer lineItem : incomesWithReceivables.keySet()) {
      
      IncomeExpense incomeExpense = incomesWithReceivables.get(lineItem);
      LineItem lineItemObj = incomeExpense.getLineItem();
      boolean isFruitVeg = lineItemObj.getFruitVegTypeCode() != null;
      boolean isApple = FruitVegTypeCodes.APPLE.equals(lineItemObj.getFruitVegTypeCode());
      
      if(isFruitVeg && !isApple) { // Inventory is not required for Apples. Instead, receivable end value is required.
        boolean foundInventory = false;
        boolean hasReportedIncome = false;
        boolean hasReceivable = false;
        if(incomeExpense.getTotalAmount() == 0) {
          continue;
        }
  
        hasReportedIncome = reportedIncomes.containsKey(lineItem) && reportedIncomes.get(lineItem).getTotalAmount() != 0;
        hasReceivable = reportedReceivables.containsKey(lineItem.toString());
        
        for (RevenueRiskFruitVegItemTestResult fruitVegItem : recordedFruitVegItems) {
          if(fruitVegItem.getFruitVegTypeCode().equals(incomeExpense.getLineItem().getFruitVegTypeCode())) {
            foundInventory = true;
            break;
          }
        }
          
        boolean fail = false;
        
        if(!foundInventory) {
          fail = true;
          
          if(hasReportedIncome) {
            ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_INVENTORY_FOR_INCOME,
                lineItem, incomeExpense.getLineItem().getDescription());
          } else if(hasReceivable) { // no income
            ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_INVENTORY_FOR_RECEIVABLE,
                lineItem, incomeExpense.getLineItem().getDescription());
          }
        }
        
        if(!hasReportedIncome && hasReceivable) {
          fail = true;
          result.addErrorMessage(ERROR_MISSING_INCOME_FOR_RECEIVABLE, lineItem, incomeExpense.getLineItem().getDescription());
        }
        
        if(fail) {
          result.setResult(Boolean.FALSE);
          result.setFruitVegTestPass(Boolean.FALSE);
        }
      }
    }
  }
  
  private void checkMissingAppleInventoryItems(RevenueRiskTestResult result,
      Map<String, InventoryItem> receivablesIncludingNoChangeInValue) {
    List<RevenueRiskFruitVegItemTestResult> recordedFruitVegItems = result.getFruitVegResults();
    
    // Process the codes in order for ease of debugging
    Set<String> receivablesCodes = new TreeSet<>();
    receivablesCodes.addAll(receivablesIncludingNoChangeInValue.keySet());
    
    double appleAmount = 0.0;
    for(String inventoryCode : receivablesCodes) {
      InventoryItem receivable = receivablesIncludingNoChangeInValue.get(inventoryCode);
      if(receivable != null && FruitVegTypeCodes.APPLE.equals(receivable.getFruitVegTypeCode())) {
        Double curEndOfYearAmount = receivable.getTotalEndOfYearAmount();
        if(curEndOfYearAmount != null) {
          appleAmount += curEndOfYearAmount;
        }
      }
    }
    
    if(appleAmount > 0) {
      boolean foundInventory = false;
      for (RevenueRiskFruitVegItemTestResult fruitVegItem : recordedFruitVegItems) {
        if(FruitVegTypeCodes.APPLE.equals(fruitVegItem.getFruitVegTypeCode())) {
          foundInventory = true;
          break;
        }
      }
      
      if(!foundInventory) {
        result.addErrorMessage(FruitVegRevenueRiskSubTest.ERROR_MISSING_INVENTORY_FOR_APPLE_RECEIVABLE);
        result.setResult(Boolean.FALSE);
        result.setFruitVegTestPass(Boolean.FALSE);
      }
    }
  }
  
  private void sortTestResults(RevenueRiskTestResult result) {
    Collections.sort(result.getFruitVegInventory(), new Comparator<RevenueRiskInventoryItem>() {
      @Override
      public int compare(RevenueRiskInventoryItem obj1, RevenueRiskInventoryItem obj2) {
        int compare = obj1.getFruitVegTypeCode().compareTo(obj2.getFruitVegTypeCode());
        if(compare == 0) {
          compare = obj1.getInventoryItemCode().compareTo(obj2.getInventoryItemCode());
        }
        return compare;
      }
    });
    Collections.sort(result.getFruitVegResults(), new Comparator<RevenueRiskFruitVegItemTestResult>() {
      @Override
      public int compare(RevenueRiskFruitVegItemTestResult obj1, RevenueRiskFruitVegItemTestResult obj2) {
        return obj1.getFruitVegTypeCode().compareTo(obj2.getFruitVegTypeCode());
      }
    });
  }
}
