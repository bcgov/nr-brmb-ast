/**
 * Copyright (c) 2020,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.reasonability.production;

import static ca.bc.gov.srm.farm.reasonability.production.ProductionTest.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.reasonability.production.FruitVegProductionResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.CropUnitUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class FruitVegProductionSubTest {

  public static final String ERROR_MISSING_REPORTED_FRUIT_VEG_INVENTORY = 
      "Crop inventory not found for: {0}.";
  public static final String ERROR_MISSING_FRUIT_VEG_PRODUCTIVE_UNIT = 
      "Productive Units not found for: {0}.";
  public static final String ERROR_MISSING_FRUIT_VEG_TYPE_CODE = 
      "Inventory code has Commodity Type Fruit & Vegetables but is missing Fruit & Veg Type: {0} - {1}.";
  
  public static final String TARGET_UNIT = CropUnitCodes.POUNDS;
  public static final String TARGET_UNIT_DESCRIPTION = CropUnitCodes.POUNDS_DESCRIPTION;

  private double fruitVegProducedVarianceLimit;
  
  public FruitVegProductionSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    fruitVegProducedVarianceLimit = config.getProductionFruitVegVariance();
  }
  
  public void test(Scenario scenario) {
    
    ProductionTestResult result = scenario.getReasonabilityTestResults().getProductionTest();
    result.setFruitVegProducedVarianceLimit(fruitVegProducedVarianceLimit);
    result.setPassFruitVegTest(true);
    Map<String, List<ReasonabilityTestResultMessage>> errorsByType = new HashMap<>();
    
    Map<String, ProductiveUnitCapacity> productiveUnitCapacitiesMap = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario, CommodityTypeCodes.FRUIT_VEG, false, false);
    
    Map<String, List<CropItem>> cropInventoryMap = ScenarioUtils.getCropInventoryMap(scenario, CommodityTypeCodes.FRUIT_VEG, false);
    
    Set<String> inventoryCodes = new TreeSet<>();
    inventoryCodes.addAll(cropInventoryMap.keySet());
    inventoryCodes.addAll(productiveUnitCapacitiesMap.keySet());

    Map<String, List<ProductionInventoryItemTestResult>> inventoryResultsByFruitVegType = new HashMap<>();

    for(String inventoryItemCode : inventoryCodes) {
      List<CropItem> itemsForThisCode = cropInventoryMap.get(inventoryItemCode);
      ProductiveUnitCapacity productiveUnitCapacity = productiveUnitCapacitiesMap.get(inventoryItemCode);
      
      ProductionInventoryItemTestResult inventoryResult = getInventoryResultFromCropItems(itemsForThisCode, errorsByType);
      
      inventoryResult = getInventoryResultFromProductiveUnit(inventoryResult, productiveUnitCapacity, errorsByType);

      result.getFruitVegInventoryItems().add(inventoryResult);

      String fruitVegTypeCode = inventoryResult.getFruitVegTypeCode();
      if(fruitVegTypeCode == null) {
        List<ReasonabilityTestResultMessage> errors = getErrorsForFruitVegType(errorsByType, fruitVegTypeCode);
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_FRUIT_VEG_TYPE_CODE, inventoryItemCode, productiveUnitCapacity.getDescription());
      } else {
        
        if(inventoryResultsByFruitVegType.get(fruitVegTypeCode) == null) {
          inventoryResultsByFruitVegType.put(fruitVegTypeCode, new ArrayList<ProductionInventoryItemTestResult>());
        }
        inventoryResultsByFruitVegType.get(fruitVegTypeCode).add(inventoryResult);
      }
      
    }

    Set<String> fruitVegTypeCodes = new TreeSet<>();
    fruitVegTypeCodes.addAll(inventoryResultsByFruitVegType.keySet());
    
    List<FruitVegProductionResult> fruitVegResults = result.getFruitVegTestResults();
    
    
    // Process each FruitVegType
    for (String fruitVegTypeCode : fruitVegTypeCodes) {
      
      List<ReasonabilityTestResultMessage> errors = getErrorsForFruitVegType(errorsByType, fruitVegTypeCode);
      List<ProductionInventoryItemTestResult> inventoryResultsForThisCode = inventoryResultsByFruitVegType.get(fruitVegTypeCode);
      ProductionInventoryItemTestResult firstInventoryResult = inventoryResultsForThisCode.get(0);
      
      double totalReportedProduction = 0;
      double totalProductiveCapacity = 0;
      double totalExpectedProduction = 0;
      String fruitVegTypeCodeDescription = firstInventoryResult.getFruitVegTypeCodeDescription();
      
      FruitVegProductionResult fruitVegTypeResult = new FruitVegProductionResult();
      fruitVegTypeResult.setFruitVegTypeCode(fruitVegTypeCode);        
      fruitVegTypeResult.setFruitVegTypeCodeDescription(fruitVegTypeCodeDescription);        
      fruitVegTypeResult.setPass(false);
      fruitVegTypeResult.setCropUnitCode(TARGET_UNIT);
      
      boolean missingExpectedProduction = false;
      for (ProductionInventoryItemTestResult inventoryResult : inventoryResultsForThisCode) {
        if(inventoryResult.getReportedProduction() != null) {
          totalReportedProduction += inventoryResult.getReportedProduction();
        }
        if(inventoryResult.getProductiveCapacityAmount() != null) {
          totalProductiveCapacity += inventoryResult.getProductiveCapacityAmount();
        }
        if(inventoryResult.getExpectedQuantityProduced() != null) {
          totalExpectedProduction += inventoryResult.getExpectedQuantityProduced();
        }
        if(inventoryResult.getMissingExpectedProduction()) {
          missingExpectedProduction = true;
        }
      }
      
      
      fruitVegTypeResult.setReportedProduction(totalReportedProduction);
      fruitVegTypeResult.setProductiveCapacityAmount(totalProductiveCapacity);
      fruitVegTypeResult.setExpectedQuantityProduced(totalExpectedProduction);
      fruitVegResults.add(fruitVegTypeResult);
      
      if (totalReportedProduction > 0 && totalExpectedProduction == 0 && !missingExpectedProduction) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_FRUIT_VEG_PRODUCTIVE_UNIT, fruitVegTypeCodeDescription);

        setFailed(result, fruitVegTypeResult);
        continue;
      } else if (totalReportedProduction == 0 && totalExpectedProduction > 0) {
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_REPORTED_FRUIT_VEG_INVENTORY, fruitVegTypeCodeDescription);
        
        setFailed(result, fruitVegTypeResult);
        continue;
      }
      
      fruitVegTypeResult.setVarianceLimit(fruitVegProducedVarianceLimit);
      fruitVegTypeResult.setExpectedQuantityProduced(totalExpectedProduction);
      fruitVegTypeResult.setReportedProduction(totalReportedProduction);
      
      // calculate variance here
      boolean hasErrors = !errors.isEmpty();
      boolean divideByZeroError = totalReportedProduction != 0 && totalExpectedProduction == 0;
      if (!hasErrors && !divideByZeroError) {
        double variance;
        if(totalExpectedProduction == 0) { // and totalReportedProduction == 0
          variance = 0;
        } else {
          variance = (totalReportedProduction - totalExpectedProduction) / Math.abs(totalExpectedProduction);
          variance = ReasonabilityTestUtils.roundPercent(variance);
        }
        
        if (Math.abs(variance) <= fruitVegProducedVarianceLimit) {
          fruitVegTypeResult.setPass(true);
        } else {
          setFailed(result, fruitVegTypeResult);
        }
        fruitVegTypeResult.setVariance(variance);          
      } else {
        setFailed(result, fruitVegTypeResult);
      }
      
    }
    
    List<ReasonabilityTestResultMessage> errors = result.getErrorMessages();
    for (String fruitVegTypeCode : fruitVegTypeCodes) {
      List<ReasonabilityTestResultMessage> errorsForThisType = errorsByType.get(fruitVegTypeCode);
      if(errorsForThisType != null) {
        errors.addAll(errorsForThisType);
      }
    }
    
    List<ReasonabilityTestResultMessage> errorsForNullType = errorsByType.get(null);
    if(errorsForNullType != null) {
      errors.addAll(errorsForNullType);
    }
    
    if(!errors.isEmpty()) {
      result.setPassFruitVegTest(false);
    }
    
    setProductionTestResult(result, result.getPassFruitVegTest());
  }

  private ProductionInventoryItemTestResult getInventoryResultFromCropItems(List<CropItem> itemsForThisCode,
      Map<String, List<ReasonabilityTestResultMessage>> errorsByType) {
    ProductionInventoryItemTestResult inventoryResult = null;
    
    if(itemsForThisCode != null) {
      CropItem firstItem = itemsForThisCode.get(0);
      String fruitVegTypeCode = firstItem.getFruitVegTypeCode();
      inventoryResult = createInventoryResult(
          firstItem.getInventoryItemCode(),
          firstItem.getInventoryItemCodeDescription(),
          firstItem.getCommodityTypeCode(),
          fruitVegTypeCode,
          firstItem.getFruitVegTypeCodeDescription());
      
      double reportedProduction = 0;
      
      for(CropItem item : itemsForThisCode) {
        Double reportedQuantityProduced = 0d;
        if(item.getTotalQuantityProduced() != null) {
          reportedQuantityProduced = item.getTotalQuantityProduced();
        }
        
        if(!item.getCropUnitCode().equals(TARGET_UNIT)) {
          reportedQuantityProduced = CropUnitUtils.convert(item.getCropUnitConversion(), item.getTotalQuantityProduced(), item.getCropUnitCode(), TARGET_UNIT);
          if (reportedQuantityProduced == null) {
            List<ReasonabilityTestResultMessage> errors = getErrorsForFruitVegType(errorsByType, fruitVegTypeCode);
            ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_CONVERSION_FACTORS,
                item.getInventoryItemCode(), item.getInventoryItemCodeDescription(), item.getCropUnitCodeDescription(), TARGET_UNIT_DESCRIPTION);
          }
        }
 
        if(reportedQuantityProduced != null) {
          reportedProduction += reportedQuantityProduced;
          reportedProduction = MathUtils.round(reportedProduction, 3);
          inventoryResult.setReportedProduction(reportedProduction);
        }
      }
    }
    return inventoryResult;
  }

  private ProductionInventoryItemTestResult getInventoryResultFromProductiveUnit(ProductionInventoryItemTestResult inventoryResultParam,
      ProductiveUnitCapacity productiveUnitCapacity, Map<String, List<ReasonabilityTestResultMessage>> errorsByType) {
    
    ProductionInventoryItemTestResult inventoryResult = inventoryResultParam;
    if(inventoryResult == null) {
      inventoryResult = createInventoryResult(
          productiveUnitCapacity.getInventoryItemCode(),
          productiveUnitCapacity.getInventoryItemCodeDescription(),
          productiveUnitCapacity.getCommodityTypeCode(),
          productiveUnitCapacity.getFruitVegTypeCode(),
          productiveUnitCapacity.getFruitVegTypeCodeDescription());
    }
    
    inventoryResult.setMissingExpectedProduction(false);
    
    if(productiveUnitCapacity != null) {
      Double capacity = productiveUnitCapacity.getTotalProductiveCapacityAmount();
      inventoryResult.setProductiveCapacityAmount(capacity);
      
      BigDecimal expectedPerUnit = productiveUnitCapacity.getExpectedProductionPerProductiveUnit();
      inventoryResult.setMissingExpectedProduction(expectedPerUnit == null);
      if (expectedPerUnit == null) {
        List<ReasonabilityTestResultMessage> errors = getErrorsForFruitVegType(errorsByType, productiveUnitCapacity.getFruitVegTypeCode());
        ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_EXPECTED_PRODUCTION, inventoryResult.getInventoryItemCode(), inventoryResult.getInventoryItemCodeDescription());
      } else {
        
        double expectedQuantityProduced = capacity * expectedPerUnit.doubleValue();
        expectedQuantityProduced = MathUtils.round(expectedQuantityProduced, 3);
        inventoryResult.setExpectedProductionPerUnit(expectedPerUnit.doubleValue());
        inventoryResult.setExpectedQuantityProduced(expectedQuantityProduced);
      }

    }
    return inventoryResult;
  }

  private void setFailed(ProductionTestResult result, FruitVegProductionResult fruitVegTypeResult) {
    fruitVegTypeResult.setPass(false);
    result.setPassFruitVegTest(false);
  }

  private ProductionInventoryItemTestResult createInventoryResult(String inventoryItemCode, String inventoryItemCodeDescription,
      String commodityTypeCode, String fruitVegTypeCode, String fruitVegTypeCodeDescription) {
    ProductionInventoryItemTestResult inventoryResult = new ProductionInventoryItemTestResult();
    
    inventoryResult.setInventoryItemCode(inventoryItemCode);
    inventoryResult.setInventoryItemCodeDescription(inventoryItemCodeDescription);
    inventoryResult.setCropUnitCode(TARGET_UNIT);
    inventoryResult.setCommodityTypeCode(commodityTypeCode);
    inventoryResult.setFruitVegTypeCode(fruitVegTypeCode);
    inventoryResult.setFruitVegTypeCodeDescription(fruitVegTypeCodeDescription);
    inventoryResult.setProductiveCapacityAmount(0.0);
    inventoryResult.setExpectedProductionPerUnit(0.0);
    inventoryResult.setReportedProduction(0.0);
    inventoryResult.setExpectedQuantityProduced(0.0);
    
    return inventoryResult;
  }

  private List<ReasonabilityTestResultMessage> getErrorsForFruitVegType(
      Map<String, List<ReasonabilityTestResultMessage>> errorsByType,
      String fruitVegTypeCode) {
    
    List<ReasonabilityTestResultMessage> errors = errorsByType.get(fruitVegTypeCode);
    if(errors == null) {
      errors = new ArrayList<>();
      errorsByType.put(fruitVegTypeCode, errors);
    }
    return errors;
  }

}
