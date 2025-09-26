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

import java.util.Collections;
import java.util.Comparator;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.CattleRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskProductiveUnit;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;

/**
 * @author awilkinson
 */
public class CattleRevenueRiskSubTest {

  private double cattleVarianceLimit;
  private double cattleDeathRate;
  
  public CattleRevenueRiskSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    cattleVarianceLimit = config.getRevenueRiskCattleVariance();
    cattleDeathRate = config.getRevenueRiskCattleDeathRate();
  }

  public void test(Scenario scenario) {
    
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    CattleRevenueRiskSubTestResult result = new CattleRevenueRiskSubTestResult();
    revenueRiskResult.setCattle(result);
    result.setSubTestPass(true);
    
    result.setVarianceLimit(cattleVarianceLimit);
    result.setDeathRate(cattleDeathRate);

    {
      RevenueRiskProductiveUnit productiveUnitResult = new RevenueRiskProductiveUnit();
      productiveUnitResult.setStructureGroupCode("104");
      productiveUnitResult.setStructureGroupCodeDescription("Cattle");
      productiveUnitResult.setProductiveCapacityAmount(100d);
      // TODO figure this out once we have the updated requirements
//      productiveUnitResult.setDeaths(5d);
//      productiveUnitResult.setProduced(95d);
//      productiveUnitResult.setHeifers(47.5);
//      productiveUnitResult.setSteers(47.5);
      
      result.getProductiveUnits().add(productiveUnitResult);
    }
    
    {
      RevenueRiskProductiveUnit productiveUnitResult = new RevenueRiskProductiveUnit();
      productiveUnitResult.setStructureGroupCode("141");
      productiveUnitResult.setStructureGroupCodeDescription("Custom Fed Cattle");
      productiveUnitResult.setProductiveCapacityAmount(50d);
      
      result.getProductiveUnits().add(productiveUnitResult);
    }

    
    // INVENTORY
    {
      RevenueRiskInventoryItem inventoryResult = new RevenueRiskInventoryItem();
      inventoryResult.setInventoryItemCode("8000");
      inventoryResult.setInventoryItemCodeDescription("Beef, Breeding, Bulls");
      inventoryResult.setQuantityProduced(null);
      inventoryResult.setQuantityStart(15.0);
      inventoryResult.setQuantityEnd(10.0);
      inventoryResult.setReportedPrice(3100.0);
      inventoryResult.setQuantitySold(5.0);
      inventoryResult.setExpectedRevenue(62143.13);
      
      result.getInventory().add(inventoryResult);
    }
    
    {
      RevenueRiskInventoryItem inventoryResult = new RevenueRiskInventoryItem();
      inventoryResult.setInventoryItemCode("8040");
      inventoryResult.setInventoryItemCodeDescription("Beef; Feeder 501 - 600 lbs; Heifers");
      inventoryResult.setQuantityProduced(47.5);
      inventoryResult.setQuantityStart(20.0);
      inventoryResult.setQuantityEnd(5.0);
      inventoryResult.setReportedPrice(994.29);
      inventoryResult.setQuantitySold(62.5);
      inventoryResult.setExpectedRevenue(62143.13);
      
      result.getInventory().add(inventoryResult);
    }
    
    {
      RevenueRiskInventoryItem inventoryResult = new RevenueRiskInventoryItem();
      inventoryResult.setInventoryItemCode("8042");
      inventoryResult.setInventoryItemCodeDescription("Beef, Steer, Feeder, 501-600 lb");
      inventoryResult.setQuantityProduced(47.5);
      inventoryResult.setQuantityStart(30.0);
      inventoryResult.setQuantityEnd(7.0);
      inventoryResult.setReportedPrice(1123.43);
      inventoryResult.setQuantitySold(70.5);
      inventoryResult.setExpectedRevenue(792011.82);
      
      result.getInventory().add(inventoryResult);
    }


    {
      RevenueRiskIncomeTestResult incomeResult = new RevenueRiskIncomeTestResult();
      incomeResult.setLineItemCode(706);
      incomeResult.setDescription("Cattle  Cows and bulls");
      incomeResult.setReportedRevenue(71171d);
      
      result.getIncomes().add(incomeResult);
    }
    
    result.setReportedRevenue(34060.51);
    result.setExpectedRevenue(40000.00);
    result.setVariance(-0.148);
    
    sortTestResults(result);
  }
  
  private void sortTestResults(CattleRevenueRiskSubTestResult result) {
    Collections.sort(result.getProductiveUnits(), new Comparator<RevenueRiskProductiveUnit>() {
      @Override
      public int compare(RevenueRiskProductiveUnit obj1, RevenueRiskProductiveUnit obj2) {
        Integer invItemCode1 = Integer.parseInt(obj1.getCode());
        Integer invItemCode2 = Integer.parseInt(obj2.getCode());
        return Integer.compare(invItemCode1, invItemCode2);
      }
    });
    
    Collections.sort(result.getInventory(), new Comparator<RevenueRiskInventoryItem>() {
      @Override
      public int compare(RevenueRiskInventoryItem obj1, RevenueRiskInventoryItem obj2) {
        Integer invItemCode1 = Integer.parseInt(obj1.getInventoryItemCode());
        Integer invItemCode2 = Integer.parseInt(obj2.getInventoryItemCode());
        return Integer.compare(invItemCode1, invItemCode2);
      }
    });
    
    Collections.sort(result.getIncomes(), new Comparator<RevenueRiskIncomeTestResult>() {
      @Override
      public int compare(RevenueRiskIncomeTestResult obj1, RevenueRiskIncomeTestResult obj2) {
        return Integer.compare(obj1.getLineItemCode(), obj2.getLineItemCode());
      }
    });
  }
}
