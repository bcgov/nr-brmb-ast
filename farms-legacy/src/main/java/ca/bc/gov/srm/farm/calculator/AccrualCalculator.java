/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * 
 */
public abstract class AccrualCalculator {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected Scenario scenario;

  protected AccrualCalculator(Scenario scenario) {
    this.scenario = scenario;
  }
  
  private static final List<String> DEFERRED_PROGRAM_PAYMENT_CODES = Arrays.asList(new String[] {
      "406", "412", "426", "457", "468", "469", "473", "479", "482", "483",
      "484", "485", "486", "496", "497", "569", "579", "582", "588", "498"
    });

  private InventoryCalculator inventoryCalculator = CalculatorFactory.getInventoryCalculator();
  
  public abstract void calculateTotals();
  

  /**
   * @param yearMargin yearMargin
   * @param refScenarios refScenarios
   */
  protected void calculateTotals(
      MarginTotal yearMargin,
      List<ReferenceScenario> refScenarios) {

    final int numAccrualTypes = 5;
    // additional total for Deferred Program Payments
    final int numAdditionalTotals = 1;
    double[] totals = new double[numAccrualTypes + numAdditionalTotals];
    int ii;

    for(ReferenceScenario rs : refScenarios) {

      for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        double value = 0;
        
        calculateTotals(fo);
        
        ii = 0;
        value = fo.getMargin().getAccrualAdjsCropInventory().doubleValue();
        totals[ii++] += (value * partnershipPercent);
        
        value = fo.getMargin().getAccrualAdjsLvstckInventory().doubleValue();
        totals[ii++] += (value * partnershipPercent);
        
        value = fo.getMargin().getAccrualAdjsPayables().doubleValue();
        totals[ii++] += (value * partnershipPercent);
        
        value = fo.getMargin().getAccrualAdjsPurchasedInputs().doubleValue();
        totals[ii++] += (value * partnershipPercent);
        
        value = fo.getMargin().getAccrualAdjsReceivables().doubleValue();
        totals[ii++] += (value * partnershipPercent);
        
        value = fo.getMargin().getDeferredProgramPayments().doubleValue();
        totals[ii++] += (value * partnershipPercent);
      }
    }
    
    ii = 0;
    yearMargin.setAccrualAdjsCropInventory(new Double(totals[ii++]));
    yearMargin.setAccrualAdjsLvstckInventory(new Double(totals[ii++]));
    yearMargin.setAccrualAdjsPayables(new Double(totals[ii++]));
    yearMargin.setAccrualAdjsPurchasedInputs(new Double(totals[ii++]));
    yearMargin.setAccrualAdjsReceivables(new Double(totals[ii++]));
    yearMargin.setDeferredProgramPayments(new Double(totals[ii++]));
  }
  

  /**
   * 
   * @param fo fo
   */
  private void calculateTotals(FarmingOperation fo) {
    logger.debug("Calculating operation accrual totals...");
    
    ReferenceScenario refScenario = fo.getFarmingYear().getReferenceScenario();
    boolean isProgramYear = refScenario.isProgramYear();
    boolean isCashMargins = refScenario.getParentScenario().getFarmingYear().getIsCashMargins();
    
    Double cropsTotal;
    Double livestockTotal;
    Double payablesTotal;
    Double inputsTotal;
    Double deferredProgramPayments;
    Double receivablesTotal;
    
    // If the participant opted to Cash Margins for the program year,
    // do not apply accrual adjusments to the reference years.
    if( ! isProgramYear && isCashMargins ) {
      cropsTotal = 0.0;
      livestockTotal = 0.0;
      payablesTotal = 0.0;
      inputsTotal = 0.0;
      deferredProgramPayments = 0.0;
      receivablesTotal = 0.0;
    } else {
      cropsTotal = calculateTotal(fo.getCropItems());
      livestockTotal = calculateTotal(fo.getLivestockItems());
      payablesTotal = calculateTotal(fo.getPayableItems());
      inputsTotal = calculateTotal(fo.getInputItems());
      deferredProgramPayments = calculateDeferredProgramPaymentsTotal(fo);
      receivablesTotal = calculateTotal(fo.getReceivableItems());
    }

    fo.getMargin().setAccrualAdjsCropInventory(cropsTotal);
    
    fo.getMargin().setAccrualAdjsLvstckInventory(livestockTotal);
    
    fo.getMargin().setAccrualAdjsPayables(payablesTotal);
    
    fo.getMargin().setAccrualAdjsPurchasedInputs(inputsTotal);
    
    fo.getMargin().setDeferredProgramPayments(deferredProgramPayments);
    
    receivablesTotal = new Double(receivablesTotal.doubleValue() - deferredProgramPayments.doubleValue());
    fo.getMargin().setAccrualAdjsReceivables(receivablesTotal);
  }

  
  /**
   * 
   * @param items list of InventoryItem
   * @return total change in value
   */
  public Double calculateTotal(List<? extends InventoryItem> items) {
    double total = 0;
    
    if(items != null) {
      
      for(InventoryItem ii : items) {
        
        if(ii.getIsEligible()) {
          total += inventoryCalculator.calculateChangeInValue(ii);
        }
      }
    }
    
    return new Double(total);
  }
  
  
  /**
   * @param fo FarmingOperation
   * @return total change in value
   */
  public Double calculateDeferredProgramPaymentsTotal(FarmingOperation fo) {
    List<ReceivableItem> items = fo.getReceivableItems();
    double total = 0;
    
    if(items != null) {
      
      for(ReceivableItem ri : items) {
        
        if(DEFERRED_PROGRAM_PAYMENT_CODES.contains(ri.getInventoryItemCode())) {
          total += inventoryCalculator.calculateChangeInValue(ri);
        }
      }
    }
    
    return new Double(total);
  }

}
