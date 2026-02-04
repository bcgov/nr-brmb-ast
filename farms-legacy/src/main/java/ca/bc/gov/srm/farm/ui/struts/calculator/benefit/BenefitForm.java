/**
 * 
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.benefit;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;


/**
 * Form for screen 850.
 */
public class BenefitForm extends CalculatorSearchForm {
	
	public static final String YES = "YES";
	public static final String NO = "NO";

  private static final long serialVersionUID = -2649172988005403512L;

  private final int maxNumYears = 10;
  private boolean[] isDeemedFarmYear = new boolean[maxNumYears];
  private String scMaterialOverride;
  private String insuranceBenefit;

  private String appliedBenefitPercent; // for basic scenarios, not for growing forward 2013
  private String interimBenefitPercent;
  // Map<year, percent>
  private Map<String, String> appliedBenefitPercentMap = new HashMap<>(); // for combined farms, not for growing forward 2013
  
  private Benefit benefit;
  private boolean[] usedInCalc;
  
  private boolean paymentCapEnabled;
  private Double paymentCapPercentageOfMarginDecline;
  
  private Double productionInsuranceFactor;
  private Double paymentTriggerFactor;
  private Double standardPositiveMarginCompensationRate;
  private Double standardNegativeMarginCompensationRate;
  private Double enhancedPositiveMarginCompensationRate;
  private Double enhancedNegativeMarginCompensationRate;
  
  private int lineNumber;
  
  //
  // Rely on the fact the the Margin and MarginTotal classes have the same
  // method names.
  //
  private Object[] totals;
  
  /**
   */
  public BenefitForm() {
  }

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    
    for(int ii = 0; ii< isDeemedFarmYear.length; ii++) {
    	isDeemedFarmYear[ii] = false;
    }
  }
  
  /**
   * @return isDeemedFarmYear array
   */
  public boolean[] getIsDeemedFarmYear() {
  	return isDeemedFarmYear;
  }
  
  /**
   * only gets called when deemedFarmingYear is true;
   * 
   * @param index index
   * @param deemedFarmingYear deemedFarmingYear
   */
  public void setIsDeemedFarmYear(int index, boolean deemedFarmingYear) {
  	isDeemedFarmYear[index] = deemedFarmingYear;
  }

  /**
   * @return scMaterialOverride array
   */
  public String getScMaterialOverride() {
  	return scMaterialOverride;
  }
  
  /**
   * set scMaterialOverride 
   * 
   * @param value value
   */
  public void setScMaterialOverride(String value) {
  	scMaterialOverride = value;
  }
  
	/**
	 * @return the insuranceBenefit
	 */
	public String getInsuranceBenefit() {
		return insuranceBenefit;
	}


	/**
	 * @param insuranceBenefit the insuranceBenefit to set
	 */
	public void setInsuranceBenefit(String insuranceBenefit) {
		this.insuranceBenefit = insuranceBenefit;
	}


	/**
   * @return the appliedBenefitPercent
   */
  public String getAppliedBenefitPercent() {
    return appliedBenefitPercent;
  }


  /**
   * @param appliedBenefitPercent the appliedBenefitPercent to set
   */
  public void setAppliedBenefitPercent(String appliedBenefitPercent) {
    this.appliedBenefitPercent = appliedBenefitPercent;
  }


  /**
   * @param pin pin
   * @return the appliedBenefitPercent
   */
  public String getAppliedBenefitPercentMap(String pin) {
    String result = appliedBenefitPercentMap.get(pin);
    return result;
  }


  /**
   * @param pin pin
   * @param newValue the appliedBenefitPercent to set
   */
  public void setAppliedBenefitPercentMap(String pin, String newValue) {
    appliedBenefitPercentMap.put(pin, newValue);
  }


  public String getInterimBenefitPercent() {
    return interimBenefitPercent;
  }


  public void setInterimBenefitPercent(String interimBenefitPercent) {
    this.interimBenefitPercent = interimBenefitPercent;
  }


  /**
	 * @return the totals
	 */
	public Object[] getTotals() {
		return totals;
	}


	/**
	 * @param totals the totals to set
	 */
	public void setTotals(Object[] totals) {
		this.totals = totals;
	}


  /**
   * @return the benefit
   */
  public Benefit getBenefit() {
    return benefit;
  }


  /**
   * @param benefit the benefit to set
   */
  public void setBenefit(Benefit benefit) {
    this.benefit = benefit;
  }


  /**
   * @return the usedInCalc
   */
  public boolean[] getUsedInCalc() {
    return usedInCalc;
  }


  /**
   * @param usedInCalc the usedInCalc to set
   */
  public void setUsedInCalc(boolean[] usedInCalc) {
    this.usedInCalc = usedInCalc;
  }
  
  public boolean isPaymentCapEnabled() {
    return paymentCapEnabled;
  }

  public void setPaymentCapEnabled(boolean paymentCapEnabled) {
    this.paymentCapEnabled = paymentCapEnabled;
  }

  public Double getPaymentCapPercentageOfMarginDecline() {
    return paymentCapPercentageOfMarginDecline;
  }

  public void setPaymentCapPercentageOfMarginDecline(Double paymentCapPercentageOfMarginDecline) {
    this.paymentCapPercentageOfMarginDecline = paymentCapPercentageOfMarginDecline;
  }

  public Double getProductionInsuranceFactor() {
    return productionInsuranceFactor;
  }

  public void setProductionInsuranceFactor(Double productionInsuranceFactor) {
    this.productionInsuranceFactor = productionInsuranceFactor;
  }

  public Double getPaymentTriggerFactor() {
    return paymentTriggerFactor;
  }

  public void setPaymentTriggerFactor(Double paymentTriggerFactor) {
    this.paymentTriggerFactor = paymentTriggerFactor;
  }

  public Double getStandardPositiveMarginCompensationRate() {
    return standardPositiveMarginCompensationRate;
  }

  public void setStandardPositiveMarginCompensationRate(Double standardPositiveMarginCompensationRate) {
    this.standardPositiveMarginCompensationRate = standardPositiveMarginCompensationRate;
  }

  public Double getStandardNegativeMarginCompensationRate() {
    return standardNegativeMarginCompensationRate;
  }

  public void setStandardNegativeMarginCompensationRate(Double standardNegativeMarginCompensationRate) {
    this.standardNegativeMarginCompensationRate = standardNegativeMarginCompensationRate;
  }

  public Double getEnhancedPositiveMarginCompensationRate() {
    return enhancedPositiveMarginCompensationRate;
  }

  public void setEnhancedPositiveMarginCompensationRate(Double enhancedPositiveMarginCompensationRate) {
    this.enhancedPositiveMarginCompensationRate = enhancedPositiveMarginCompensationRate;
  }

  public Double getEnhancedNegativeMarginCompensationRate() {
    return enhancedNegativeMarginCompensationRate;
  }

  public void setEnhancedNegativeMarginCompensationRate(Double enhancedNegativeMarginCompensationRate) {
    this.enhancedNegativeMarginCompensationRate = enhancedNegativeMarginCompensationRate;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
  }

  public int getNextLineNumber() {
    return ++lineNumber;
  }

  public boolean isShow2020ViewWithoutRML() {
    return getYear() >= CalculatorConfig.GROWING_FORWARD_2020
        && benefit.getReferenceMarginLimitForBenefitCalc() == null;
  }

}
