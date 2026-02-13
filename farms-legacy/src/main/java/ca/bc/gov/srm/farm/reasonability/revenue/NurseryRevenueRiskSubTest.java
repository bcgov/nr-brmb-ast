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
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.NurseryRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTest;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class NurseryRevenueRiskSubTest {
  
  public static final String ERROR_MISSING_INCOME =
      "Nursery income not found.";
  public static final String ERROR_MISSING_INVENTORY =
      "Nursery inventory not found.";

  private double nurseryVarianceLimit;
  
  public NurseryRevenueRiskSubTest() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    nurseryVarianceLimit = config.getRevenueRiskNurseryVariance();
  }

  public void test(Scenario scenario) {
    
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    NurseryRevenueRiskSubTestResult result = new NurseryRevenueRiskSubTestResult();
    revenueRiskResult.setNursery(result);
    result.setSubTestPass(true);
    
    result.setVarianceLimit(nurseryVarianceLimit);
    
    boolean hasNursery = ScenarioUtils.hasCommdityType(scenario, CommodityTypeCodes.NURSERY);

    if(hasNursery) {
      
      List<ReasonabilityTestResultMessage> errors = new ArrayList<>();
      
      Map<String, List<CropItem>> cropInventoryMap = ScenarioUtils.getCropInventoryMap(scenario, CommodityTypeCodes.NURSERY, false);
      Map<Integer, IncomeExpense> incomesWithReceivables = ScenarioUtils.getIncomesWithReceivables(scenario, CommodityTypeCodes.NURSERY);
      
      handleInventory(cropInventoryMap, result, errors);
      handleIncomes(incomesWithReceivables, result);
      
      if(result.getInventory().isEmpty()) {
        addErrorMessage(errors, ERROR_MISSING_INVENTORY);
      }
      
      if(result.getIncomes().isEmpty() || result.getReportedRevenue() == 0) {
        addErrorMessage(errors, ERROR_MISSING_INCOME);
      }
      
      computeVariance(result, errors);
      revenueRiskResult.getErrorMessages().addAll(errors);
      sortTestResults(result);
    }
  }
  
  private void handleInventory(
      Map<String, List<CropItem>> cropInventoryMap,
      NurseryRevenueRiskSubTestResult result,
      List<ReasonabilityTestResultMessage> errors) {
    
    result.setExpectedRevenue(0d);
    
    for (String inventoryItemCode : cropInventoryMap.keySet()) {
      List<CropItem> itemsForThisCode = cropInventoryMap.get(inventoryItemCode);
      
      for(CropItem nurseryItem : itemsForThisCode) {
        
        handleNurseryInventoryItem(result, errors, nurseryItem);
      }
    }
  }

  private void handleNurseryInventoryItem(
      NurseryRevenueRiskSubTestResult result,
      List<ReasonabilityTestResultMessage> errors,
      CropItem nurseryItem) {
    
    double quantityProduced = MathUtils.getPrimitiveValue(nurseryItem.getTotalQuantityProduced());
    double quantityStart = MathUtils.getPrimitiveValue(nurseryItem.getTotalQuantityStart());
    double quantityEnd = MathUtils.getPrimitiveValue(nurseryItem.getTotalQuantityEnd());

    if(quantityProduced != 0 || quantityStart != 0 || quantityEnd != 0) {
      RevenueRiskInventoryItem nurseryInventoryItem = new RevenueRiskInventoryItem();
      result.getInventory().add(nurseryInventoryItem);
      nurseryInventoryItem.setInventoryItemCode(nurseryItem.getInventoryItemCode());
      nurseryInventoryItem.setInventoryItemCodeDescription(nurseryItem.getInventoryItemCodeDescription());
      nurseryInventoryItem.setCropUnitCode(nurseryItem.getCropUnitCode());
      nurseryInventoryItem.setCropUnitCodeDescription(nurseryItem.getCropUnitCodeDescription());
      
      FarmingOperation farmingOperation = nurseryItem.getFarmingOperation();
      Double partnershipPercent = ScenarioUtils.getPartnershipPercent(farmingOperation);
      
      quantityProduced *= partnershipPercent;
      quantityStart *= partnershipPercent;
      quantityEnd *= partnershipPercent;
      
      nurseryInventoryItem.setQuantityProduced(quantityProduced);
      nurseryInventoryItem.setQuantityStart(quantityStart);
      nurseryInventoryItem.setQuantityEnd(quantityEnd);
      
      double quantitySold = quantityStart + quantityProduced - quantityEnd;
      
      if(quantitySold < 0) {
        quantitySold = 0d;
      }
      nurseryInventoryItem.setQuantitySold(quantitySold);
      
      Double fmvEnd = nurseryItem.getFmvEnd();
      nurseryInventoryItem.setFmvPrice(fmvEnd);
      
      Double expectedRevenue = null;
      if (fmvEnd == null) {
        // If the fiscal year dates are missing then the FMV cannot be found,
        // so only show the missing FMV message if the operation has the dates.
        if(farmingOperation.getFiscalYearStart() == null || farmingOperation.getFiscalYearEnd() == null) {
          ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_FISCAL_YEAR_DATES, farmingOperation.getSchedule());
        } else {
          addErrorMessage(errors, ERROR_INVENTORY_MISSING_FMV, nurseryItem.getInventoryItemCode(), nurseryItem.getInventoryItemCodeDescription());
        }
      } else {
        expectedRevenue = quantitySold * fmvEnd;
      }
      nurseryInventoryItem.setExpectedRevenue(expectedRevenue);
      
      if (expectedRevenue != null) {
        result.setExpectedRevenue(result.getExpectedRevenue() + expectedRevenue);
      }
    }
  }
  
  private void handleIncomes(
      Map<Integer, IncomeExpense> incomesWithReceivables,
      NurseryRevenueRiskSubTestResult result) {
    
    double reportedRevenue = 0d;
    
    for (Integer lineItemCode : incomesWithReceivables.keySet()) {
      IncomeExpense incExp = incomesWithReceivables.get(lineItemCode);
      LineItem lineItem = incExp.getLineItem();
      
      reportedRevenue += incExp.getTotalAmount();
      
      RevenueRiskIncomeTestResult revenueRiskIncomeTestResult = new RevenueRiskIncomeTestResult();
      revenueRiskIncomeTestResult.setLineItemCode(lineItem.getLineItem());
      revenueRiskIncomeTestResult.setDescription(lineItem.getDescription());
      revenueRiskIncomeTestResult.setReportedRevenue(incExp.getTotalAmount());
      result.getIncomes().add(revenueRiskIncomeTestResult);
    }
    
    result.setReportedRevenue(reportedRevenue);
    
  }
  
  private void computeVariance(NurseryRevenueRiskSubTestResult result, List<ReasonabilityTestResultMessage> errors) {
    Double expectedRevenue = result.getExpectedRevenue();
    Double reportedRevenue = result.getReportedRevenue();
    if (errors.isEmpty() && expectedRevenue != null && reportedRevenue != null && expectedRevenue != 0) {
      Double variance = (reportedRevenue / expectedRevenue) - 1;
      variance = MathUtils.round(variance, ReasonabilityTest.PERCENT_DECIMAL_PLACES);
      variance = Math.abs(variance);
      result.setVariance(variance);
      
      if (variance >= nurseryVarianceLimit) {
        result.setSubTestPass(false);
      }
    } else {
      result.setSubTestPass(false);
    }
  }
  
  public ReasonabilityTestResultMessage addErrorMessage(List<ReasonabilityTestResultMessage> errors, String message, Object... parameters) {
    ReasonabilityTestResultMessage msg = new ReasonabilityTestResultMessage(message, MessageTypeCodes.ERROR, parameters);
    errors.add(msg);
    return msg;
  }
  
  private void sortTestResults(NurseryRevenueRiskSubTestResult result) {
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
