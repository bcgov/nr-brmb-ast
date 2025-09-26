/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.NegativeMargin;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.NegativeMarginService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * 
 */
public abstract class NegativeMarginCalculator {
  
  protected Scenario scenario;
  protected BigDecimal negativeMarginPaymentPercentage;
  
  private Set<String> negativeMarginFailedInventoryCodes = new HashSet<>();
  
  public List<String> getNegativeMarginFailedInventoryCodeList() {
    return toList(negativeMarginFailedInventoryCodes);
  }

  public Set<String> getNegativeMarginFailedInventoryCodes() {
    return negativeMarginFailedInventoryCodes;
  }

  protected NegativeMarginCalculator(Scenario scenario) {
    this.scenario = scenario;
    this.negativeMarginPaymentPercentage = CalculatorConfig.getNegativeMarginPaymentPercentage(scenario.getYear());
  }

  /**
   * Calculates the Deemed Production Insurance for the program year
   * and puts the results into the Margin and MarginTotal objects.
   * Note that this is only performed when there is a negative margin.
   */
  public abstract void calculateDeemedPiTotals(String user) throws ServiceException;

  
  /**
   * Calculates the Deemed Production Insurance for all farming operations and put results
   * into the Margin objects and the MarginTotal passed in.
   */
  protected void calculateDeemedPiTotals(
      MarginTotal yearMargin,
      List<Scenario> scenarios,
      String user)
          throws ServiceException {

    final int numTotals = 2;
    double[] totals = new double[numTotals];
    int ii = 0;

    for(Scenario sc : scenarios) {
      
      for(FarmingOperation fo : sc.getFarmingYear().getFarmingOperations()) {
        Margin m = fo.getMargin();
        calculateDeemedPiTotals(fo, user);
        
        ii = 0;
        totals[ii++] += m.getProdInsurDeemedSubtotal().doubleValue();
        totals[ii++] += m.getProdInsurDeemedTotal().doubleValue();
      }
    }
    
    ii = 0;
    yearMargin.setProdInsurDeemedSubtotal(new Double(totals[ii++]));
    yearMargin.setProdInsurDeemedTotal(new Double(totals[ii++]));
  }


  /**
   * Calculates the Deemed Production Insurance for a farming operation
   * and put results into the Margin object.
   */
  private void calculateDeemedPiTotals(FarmingOperation fo, String user) throws ServiceException {

    Integer farmingOperationId = fo.getFarmingOperationId();
    Integer scenarioId = fo.getFarmingYear().getReferenceScenario().getScenarioId();
    
    NegativeMarginService nmService = ServiceFactory.getNegativeMarginService();
    nmService.calculateNegativeMargins(farmingOperationId, scenarioId , user);
    List<NegativeMargin> negativeMargins = nmService.getNegativeMargins(farmingOperationId, scenarioId);
		
    BigDecimal deemedPiSubtotal = BigDecimal.ZERO;
    
		for(NegativeMargin nm : negativeMargins) {
		  boolean valid = validate(nm);
		  
		  if(valid) {
  			BigDecimal deemedPi = nm.getDeemedPiValue();
			  deemedPiSubtotal = deemedPiSubtotal.add(deemedPi);
		  } else {
		    negativeMarginFailedInventoryCodes.add(nm.getInventoryItemCode());
		  }
		}

		BigDecimal partnershipPercent = BigDecimal.valueOf(ScenarioUtils.getPartnershipPercent(fo));
    BigDecimal amountAfterPartnership = deemedPiSubtotal.multiply(partnershipPercent);
    BigDecimal deemedPiTotal = amountAfterPartnership.multiply(negativeMarginPaymentPercentage)
        .setScale(2, BigDecimal.ROUND_HALF_UP);
    
		fo.getMargin().setProdInsurDeemedSubtotal(deemedPiSubtotal.doubleValue());
		fo.getMargin().setProdInsurDeemedTotal(deemedPiTotal.doubleValue());
	}

  private boolean validate(NegativeMargin nm) {
    return validate(nm.getDeductiblePercentage())
        && validate(nm.getInsurableValuePurchased())
        && validate(nm.getGuaranteedProdValue())
        && validate(nm.getPremiumsPaid())
        && validate(nm.getClaimsReceived())
        && validate(nm.getDeemedPiValue());
  }

  private boolean validate(BigDecimal value) {
    return value != null;
  }

  private List<String> toList(Set<String> set) {
    ArrayList<String> list = new ArrayList<>();
    
    list.addAll(set);
    Collections.sort(list);
    
    return list;
  }

}
