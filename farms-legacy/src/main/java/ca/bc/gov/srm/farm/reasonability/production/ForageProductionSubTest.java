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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTest;
import ca.bc.gov.srm.farm.util.CropItemUtils;
import ca.bc.gov.srm.farm.util.CropUnitUtils;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class ForageProductionSubTest {
  
  public static final String FORAGE_SEED_ERROR_NOT_REPORTED_IN_POUNDS = 
      "Forage Seed must be reported in pounds. {0} - {1} is reported in {2}.";

  private double forageProducedVarianceLimit;
  
  public ForageProductionSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    forageProducedVarianceLimit = config.getProductionForageVariance();
  }
  
  public void performForageTest(
      Map<String, List<CropItem>> forageItemsMap, 
      ProductionTestResult result, 
      Map<String, ProductiveUnitCapacity> productiveUnitCapacities) {
    
    result.setForageProducedVarianceLimit(forageProducedVarianceLimit);
    
    boolean forageTestPass = true;
    final String targetUnit = CropUnitCodes.TONNES;
    final String targetUnitDescription = CropUnitCodes.TONNES_DESCRIPTION;
    
    List<String> inventoryItemCodes = new ArrayList<>();
    inventoryItemCodes.addAll(forageItemsMap.keySet());
    Collections.sort(inventoryItemCodes);
    
    inventoryCodesLoop: for (String inventoryItemCode : inventoryItemCodes) {
      List<CropItem> itemsForThisCode = forageItemsMap.get(inventoryItemCode);
      CropItem firstItem = itemsForThisCode.get(0);
      String inventoryItemCodeDescription = firstItem.getInventoryItemCodeDescription();
      
      ProductionInventoryItemTestResult forageItemResult = new ProductionInventoryItemTestResult();
      forageItemResult.setInventoryItemCode(firstItem.getInventoryItemCode());
      forageItemResult.setInventoryItemCodeDescription(firstItem.getInventoryItemCodeDescription());
      forageItemResult.setCropUnitCode(targetUnit);
      result.getForageTestResults().add(forageItemResult);
      
      double reportedQuantityProduced = 0d;
      for (CropItem item : itemsForThisCode) {
        Double curQuantityProduced = item.getTotalQuantityProduced();
        curQuantityProduced = CropUnitUtils.convert(firstItem.getCropUnitConversion(), curQuantityProduced, item.getCropUnitCode(), targetUnit);

        // If curQuantityProduced is null, it means conversion failed.
        if(curQuantityProduced == null) {
          result.setPassForageAndForageSeedTest(false);
          forageTestPass = false;
          forageItemResult.setPass(false);          
          result.addErrorMessage(ProductionTest.ERROR_MISSING_CONVERSION_FACTORS, inventoryItemCode, forageItemResult.getInventoryItemCodeDescription(), item.getCropUnitCodeDescription(), targetUnitDescription);
          continue inventoryCodesLoop;
        }
        
        reportedQuantityProduced += curQuantityProduced;
      }
      
      forageItemResult.setReportedProduction(reportedQuantityProduced);
      
      ProductiveUnitCapacity productiveUnitCapacity = productiveUnitCapacities.get(inventoryItemCode);
      
      if (hasOnlyZeroValues(firstItem, productiveUnitCapacity)) {
        continue;
      }
      
      double productiveCapacity = 0;
      if (productiveUnitCapacity != null && productiveUnitCapacity.getTotalProductiveCapacityAmount() != null) {
        productiveCapacity = productiveUnitCapacity.getTotalProductiveCapacityAmount();
        forageItemResult.setProductiveCapacityAmount(productiveCapacity);
      } else {
        // If there is no Productive Unit provided for this crop item, variance calculation cannot be performed for it
        result.addErrorMessage(ProductionTest.ERROR_MISSING_PRODUCTIVE_UNIT, inventoryItemCode, inventoryItemCodeDescription);
        
        forageTestPass = false;
        forageItemResult.setPass(false);
        continue;
      }
            
      if (productiveUnitCapacity.getExpectedProductionPerProductiveUnit() == null) {
        result.addErrorMessage(ProductionTest.ERROR_MISSING_EXPECTED_PRODUCTION,  inventoryItemCode, inventoryItemCodeDescription);
        
        forageTestPass = false;
        forageItemResult.setPass(false);
        continue;
      }
      
      double expectedProductionPerUnit = productiveUnitCapacity.getExpectedProductionPerProductiveUnit().doubleValue();
      double expectedQuantityProduced = productiveCapacity * expectedProductionPerUnit; 
      forageItemResult.setExpectedProductionPerUnit(expectedProductionPerUnit);
      
      forageItemResult.setExpectedQuantityProduced(expectedQuantityProduced);
      
      if (expectedQuantityProduced != 0) {
        double variance = (reportedQuantityProduced / expectedQuantityProduced) - 1;
        variance = MathUtils.round(variance, ReasonabilityTest.PERCENT_DECIMAL_PLACES);
        if (Math.abs(variance) > forageProducedVarianceLimit) {
          forageTestPass = false;
          forageItemResult.setPass(false);
        } else {
          forageItemResult.setPass(true);
        }
        forageItemResult.setVariance(variance);          
      } else if (expectedQuantityProduced == 0 && reportedQuantityProduced > 0) {
        forageTestPass = false;
        forageItemResult.setPass(false);          
      }
    }
    
    ProductionTest.setProductionTestResult(result, forageTestPass);
    setResult(result, forageTestPass);
  }
  
  public void performForageSeedTest(
      Map<String, List<CropItem>> forageSeedItemsMap,
      ProductionTestResult result, 
      Map<String, ProductiveUnitCapacity> productiveUnitCapacities) {
    
    boolean forageSeedTestPass = true;
    
    List<String> inventoryItemCodes = new ArrayList<>();
    inventoryItemCodes.addAll(forageSeedItemsMap.keySet());
    Collections.sort(inventoryItemCodes);
    
    inventoryCodesLoop: for (String inventoryItemCode : inventoryItemCodes) {
      List<CropItem> itemsForThisCode = forageSeedItemsMap.get(inventoryItemCode);
      CropItem firstItem = itemsForThisCode.get(0);
      String inventoryItemCodeDescription = firstItem.getInventoryItemCodeDescription();
      
      ProductionInventoryItemTestResult forageSeedItemResult = new ProductionInventoryItemTestResult();
      forageSeedItemResult.setInventoryItemCode(inventoryItemCode);
      forageSeedItemResult.setInventoryItemCodeDescription(inventoryItemCodeDescription);
      forageSeedItemResult.setCropUnitCode(CropUnitCodes.POUNDS);
      result.getForageSeedTestResults().add(forageSeedItemResult);
      
      double reportedQuantityProduced = 0d;
      for (CropItem item : itemsForThisCode) {
        Double curQuantityProduced = item.getTotalQuantityProduced();

        // Forage Seeds MUST be reported in pounds
        if(curQuantityProduced > 0 && !firstItem.getCropUnitCode().equals(CropUnitCodes.POUNDS)) {
          result.setPassForageAndForageSeedTest(false);
          forageSeedTestPass = false;
          forageSeedItemResult.setPass(false);          
          result.addErrorMessage(FORAGE_SEED_ERROR_NOT_REPORTED_IN_POUNDS, inventoryItemCode, inventoryItemCodeDescription, item.getCropUnitCodeDescription());
          continue inventoryCodesLoop;
        }
        
        reportedQuantityProduced += curQuantityProduced;
      }
      
      forageSeedItemResult.setReportedProduction(reportedQuantityProduced);
      
      ProductiveUnitCapacity productiveUnitCapacity = productiveUnitCapacities.get(firstItem.getInventoryItemCode());
      
      double productiveCapacity = 0;
      if (productiveUnitCapacity != null && productiveUnitCapacity.getTotalProductiveCapacityAmount() != null) {
        productiveCapacity = productiveUnitCapacity.getTotalProductiveCapacityAmount();
        forageSeedItemResult.setProductiveCapacityAmount(productiveCapacity);
      } else {
        // If there is no Productive Unit provided for this crop item, variance calculation cannot be performed for it
        forageSeedItemResult.setReportedProduction(reportedQuantityProduced);
        
        result.addErrorMessage(ProductionTest.ERROR_MISSING_PRODUCTIVE_UNIT, inventoryItemCode, inventoryItemCodeDescription);
        
        forageSeedTestPass = false;
        forageSeedItemResult.setPass(false);
        continue;
      }
      
      if (productiveUnitCapacity.getExpectedProductionPerProductiveUnit() == null) {
        result.addErrorMessage(ProductionTest.ERROR_MISSING_EXPECTED_PRODUCTION, inventoryItemCode, inventoryItemCodeDescription);
        
        forageSeedTestPass = false;
        forageSeedItemResult.setPass(false);
        continue;
      }
      
      double expectedProductionPerProductiveUnit = productiveUnitCapacity.getExpectedProductionPerProductiveUnit().doubleValue();
      double expectedQuantityProduced = productiveCapacity * expectedProductionPerProductiveUnit; 
      forageSeedItemResult.setExpectedProductionPerUnit(expectedProductionPerProductiveUnit);
      
      forageSeedItemResult.setExpectedQuantityProduced(expectedQuantityProduced);
      forageSeedItemResult.setReportedProduction(reportedQuantityProduced);
      
      if (expectedQuantityProduced != 0) {
        double variance = (reportedQuantityProduced - expectedQuantityProduced) / expectedQuantityProduced;
        variance = MathUtils.round(variance, ReasonabilityTest.PERCENT_DECIMAL_PLACES);
        if (Math.abs(variance) > forageProducedVarianceLimit) {
          forageSeedTestPass = false;
          forageSeedItemResult.setPass(false);
        } else {
          forageSeedItemResult.setPass(true);
        }
        forageSeedItemResult.setVariance(variance);          
      } else if (expectedQuantityProduced == 0 && reportedQuantityProduced > 0) {
        forageSeedTestPass = false;
        forageSeedItemResult.setPass(false);          
      }
    }
    
    ProductionTest.setProductionTestResult(result, forageSeedTestPass);
    setResult(result, forageSeedTestPass);
  }

  private void setResult(ProductionTestResult result, boolean curTestResult) {
    if (result.getPassForageAndForageSeedTest() != null) {
      result.setPassForageAndForageSeedTest(result.getPassForageAndForageSeedTest() && curTestResult);
    } else {
      result.setPassForageAndForageSeedTest(curTestResult);
    }
  }
  
  private boolean hasOnlyZeroValues(CropItem cropItem, ProductiveUnitCapacity productiveUnitCapacity) {
    if (CropItemUtils.hasOnlyZeroQuantities(cropItem)
        &&  (productiveUnitCapacity == null || productiveUnitCapacity.getTotalProductiveCapacityAmount().equals(0d))) {
      return true;
    }
    
    return false;
  }

}
