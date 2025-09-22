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
import ca.bc.gov.srm.farm.util.CropUnitUtils;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class GrainProductionSubTest {

  private double grainProducedVarianceLimit;
  
  public GrainProductionSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    grainProducedVarianceLimit = config.getProductionGrainVariance();
  }
  
  public void test(
      Map<String, List<CropItem>> grainItemsMap, 
      ProductionTestResult result, 
      Map<String, ProductiveUnitCapacity> productiveUnitCapacities) {
    
    result.setGrainProducedVarianceLimit(grainProducedVarianceLimit);
    
    boolean grainTestPass = true;
    final String targetUnit = CropUnitCodes.TONNES;
    final String targetUnitDescription = CropUnitCodes.TONNES_DESCRIPTION;
    
    List<String> inventoryItemCodes = new ArrayList<>();
    inventoryItemCodes.addAll(grainItemsMap.keySet());
    Collections.sort(inventoryItemCodes);
    
    inventoryCodesLoop: for (String inventoryItemCode : inventoryItemCodes) {
      List<CropItem> itemsForThisCode = grainItemsMap.get(inventoryItemCode);
      CropItem firstItem = itemsForThisCode.get(0);
      String inventoryItemCodeDescription = firstItem.getInventoryItemCodeDescription();
    
      ProductionInventoryItemTestResult grainItemResult = new ProductionInventoryItemTestResult();
      grainItemResult.setInventoryItemCode(inventoryItemCode);
      grainItemResult.setCropUnitCode(targetUnit);
      grainItemResult.setInventoryItemCodeDescription(inventoryItemCodeDescription);
      result.getGrainItemTestResults().add(grainItemResult);
      
      double reportedQuantityProduced = 0d;
      for (CropItem item : itemsForThisCode) {
        Double curQuantityProduced = item.getTotalQuantityProduced();
        curQuantityProduced = CropUnitUtils.convert(firstItem.getCropUnitConversion(), curQuantityProduced, item.getCropUnitCode(), targetUnit);

        // If curQuantityProduced is null, it means conversion failed.
        if(curQuantityProduced == null) {
          result.setPassForageAndForageSeedTest(false);
          grainTestPass = false;
          grainItemResult.setPass(false);          
          result.addErrorMessage(ProductionTest.ERROR_MISSING_CONVERSION_FACTORS, inventoryItemCode, inventoryItemCodeDescription, item.getCropUnitCodeDescription(), targetUnitDescription);
          continue inventoryCodesLoop;
        }
        
        reportedQuantityProduced += curQuantityProduced;
      }
      
      grainItemResult.setReportedProduction(reportedQuantityProduced);
      
      ProductiveUnitCapacity productiveUnitCapacity = productiveUnitCapacities.get(inventoryItemCode);
      
      // Note that partnership percent has already been applied to productive capacity.
      double productiveCapacity = 0;
      if (productiveUnitCapacity != null && productiveUnitCapacity.getTotalProductiveCapacityAmount() != null) {
        productiveCapacity = productiveUnitCapacity.getTotalProductiveCapacityAmount();
        grainItemResult.setProductiveCapacityAmount(productiveCapacity);
      } else {
        // If there is no Productive Unit for this crop item, the variance cannot be calculated.
        result.addErrorMessage(ProductionTest.ERROR_MISSING_PRODUCTIVE_UNIT, inventoryItemCode, inventoryItemCodeDescription);
        
        grainTestPass = false;
        grainItemResult.setPass(false);
        continue;
      }
            
      if (productiveUnitCapacity.getExpectedProductionPerProductiveUnit() == null) {
        result.addErrorMessage(ProductionTest.ERROR_MISSING_EXPECTED_PRODUCTION, inventoryItemCode, inventoryItemCodeDescription);
        
        grainTestPass = false;
        grainItemResult.setPass(false);
        continue;
      }
      
      double expectedProductionPerProductiveUnit = productiveUnitCapacity.getExpectedProductionPerProductiveUnit().doubleValue();
      double expectedQuantityProduced = productiveCapacity * expectedProductionPerProductiveUnit; 
      grainItemResult.setExpectedProductionPerUnit(expectedProductionPerProductiveUnit);
      grainItemResult.setExpectedQuantityProduced(expectedQuantityProduced);
      
      if (expectedQuantityProduced != 0) {
        double variance = (reportedQuantityProduced / expectedQuantityProduced) - 1;          
        variance = MathUtils.round(variance, ReasonabilityTest.PERCENT_DECIMAL_PLACES);
        if (Math.abs(variance) > grainProducedVarianceLimit) {
          grainTestPass = false;
          grainItemResult.setPass(false);
        } else {
          grainItemResult.setPass(true);
        }
        grainItemResult.setVariance(variance);
      } else if (expectedQuantityProduced == 0 && reportedQuantityProduced > 0) {
        grainTestPass = false;
        grainItemResult.setPass(false);
      }
    }
    
    ProductionTest.setProductionTestResult(result, grainTestPass);
    result.setPassGrainTest(grainTestPass);
  }

}
