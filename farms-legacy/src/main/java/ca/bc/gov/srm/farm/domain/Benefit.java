/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Benefit is the calculated program benefit for an
 * Client. Benefit data will originate from provincial
 * calculations (note that historical data will come from federal sources).
 * Benefit is associated with a unique Client for a
 * given Program Year. An Benefit includes the monies paid, broken
 * down by various funding sources, specifically:
 *
 * <p>1) Tier 1 2) Tier 2 3) Tier 3 4) Negative</p>
 *
 * @author awilkinson
 * @created Nov 18, 2010
 */
public final class Benefit implements Serializable {
  
  private static final long serialVersionUID = 7221416134640610551L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingYear farmingYear;

  /**
   * claimId is a surrogate unique identifier for a Benefit
   */
  private Integer claimId;

  /**
   * administrativeCostShare are costs incurred by the client for administrative
   * purposes.
   */
  private Double administrativeCostShare;

  /**
   * lateApplicationPenalty is the penalty applied due to a late application to
   * Agri Stability program.
   */
  private Double lateApplicationPenalty;

  /**
   * maximumContribution is the maximum amount the client is eligible for in
   * this Benefit.
   */
  private Double maximumContribution;

  /**
   * outstandingFees are fees which have not yet been paid by the
   * Client.
   */
  private Double outstandingFees;

  /**
   * prodInsurDeemedBenefit must not be blank if there is a negative margin
   * amount.
   */
  private Double prodInsurDeemedBenefit;
  
  private Boolean prodInsurDeemedBenefitManuallyCalculated = Boolean.FALSE;

  /** programYearMargin is determined by the Income less allowable expenses. */
  private Double programYearMargin;

  /**
   * programYearPaymentsReceived are payments received by the
   * Client to date.
   */
  private Double programYearPaymentsReceived;

  private Double referenceMarginLimit;
  
  /**
   * Used for 2018 forward. The Reference Margin Limit used to calculate the benefit
   * can be no less than 70% of the Reference Margin.
   */
  private Double referenceMarginLimitCap;
  
  /**
   * Used for 2018 forward. The Reference Margin Limit after the cap has been applied.
   * allocatedReferenceMargin will be the lesser of this or adjustedReferenceMargin.
   */
  private Double referenceMarginLimitForBenefitCalc;
  
  /**
   * allocatedReferenceMargin is the reference margin allocated to the
   * Margin.
   */
  private Double allocatedReferenceMargin;

  /**
   * repaymentOfCashAdvances is the amount received by the Client
   * as cash advances to date.
   */
  private Double repaymentOfCashAdvances;

  /**
   * totalPayment is the total amount of payment due to the Client.
   */
  private Double totalPayment;

  /** supplyManagedCommoditiesAdj is the supply managed adjusted commodities. */
  private Double supplyManagedCommoditiesAdj;

  /**
   * producerShare identifies the amount of AgriStability withdrawals that are
   * the producers share.
   */
  private Double producerShare;

  /**
   * federalContributions identifies the amount of contributions from the
   * federal government that the participant has received.
   */
  private Double federalContributions;

  /**
   * provincialContributions identifies the contributions from the provincial
   * government the participant has received.
   */
  private Double provincialContributions;

  /**
   * interimContributions is the amount of contributions have been allocated for
   * the participant if a final calculation has not yet been made. If a final
   * calculation has been made this amount will be zero.
   */
  private Double interimContributions;

  /**
   * wholeFarmAllocation is the percentage of the farm associated with the
   * current ProgramYear.
   */
  private Double wholeFarmAllocation;

  /** tier2MarginDecline is the margin decline for tier2. */
  private Double tier2MarginDecline;

  /** tier3MarginDecline is the margin decline for tier3. */
  private Double tier3MarginDecline;

  /** tier2Benefit is the benefit for tier2. */
  private Double tier2Benefit;

  /** tier3Benefit is the benefit for tier3. */
  private Double tier3Benefit;

  /** negativeMarginBenefit is the negative amout of margin benefit. */
  private Double negativeMarginBenefit;

  /** negativeMarginDecline is the negative amout of margin decline. */
  private Double negativeMarginDecline;

  /** totalBenefit is the total amount to be paid from both federal and provincial funding. */
  private Double totalBenefit;
  
  /** ratioAdjustedReferenceMargin is the reference margin calculated after structure change using the Ratio method. */
  private Double ratioAdjustedReferenceMargin;
  
  /** additiveAdjustedReferenceMargin is the reference margin calculated after structure change using the Additive method. */
  private Double additiveAdjustedReferenceMargin;

  /** adjustedReferenceMargin is the reference margin calculated after structure change. */
  private Double adjustedReferenceMargin;

  /** unadjustedReferenceMargin is the adjusted reference margin. */
  private Double unadjustedReferenceMargin;
  
  /** The structural change method used. */
  private String structuralChangeMethodCode;

  /** The structural change method description. */
  private String structuralChangeMethodCodeDescription;
  
  /** The expense structural change method used. */
  private String expenseStructuralChangeMethodCode;

  /** The expense structural change method description. */
  private String expenseStructuralChangeMethodCodeDescription;

  // new 2.0.0 fields
  private Double marginDecline;
  private Double tier2Trigger;
  private Double tier3Trigger;
  private Double benefitBeforeDeductions;
  private Double benefitAfterProdInsDeduction;
  private Double appliedBenefitPercent;
  private Double interimBenefitPercent;
  private Double benefitAfterAppliedBenefitPercent;
  private Double benefitAfterInterimDeduction;
  
  private Double paymentCap;
  private Double benefitAfterPaymentCap;
  
  private Double lateEnrolmentPenalty;
  private Double benefitAfterLateEnrolmentPenalty;
  private Double lateEnrolmentPenaltyAfterAppliedBenefitPercent;

  private Double enhancedReferenceMarginForBenefitCalculation;
  
  /** Margin decline without applying the reference margin limit */
  private Double enhancedPositiveMarginDecline;
  private Double enhancedPositiveMarginBenefit;
  private Double enhancedNegativeMarginDecline;
  private Double enhancedNegativeMarginBenefit;
  private Double enhancedBenefitBeforeDeductions;
  private Double enhancedBenefitAfterProdInsDeduction;
  private Double enhancedBenefitAfterInterimDeduction;
  private Double enhancedBenefitAfterAppliedBenefitPercent;
  private Double enhancedLateEnrolmentPenalty;
  private Double enhancedBenefitAfterLateEnrolmentPenalty;
  private Double enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent;
  private Double enhancedBenefitAfterPaymentCap;
  
  /** The total benefit to be paid from the Agristability program */
  private Double standardBenefit;
  
  /** The total benefit to be paid under the new rules */
  private Double enhancedTotalBenefit;
  
  /** The additional benefit to be paid under the new rules. enhancedTotalBenefit minus standardBenefit */
  private Double enhancedAdditionalBenefit;
  
  /** The total margin decline. */
  private Double enhancedMarginDecline;


  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  

  /** Constructor. */
  public Benefit() {

  }


  /**
   * claimId is a surrogate unique identifier for a Benefit
   *
   * @return  Integer
   */
  public Integer getClaimId() {
    return claimId;
  }

  /**
   * claimId is a surrogate unique identifier for a Benefit
   * 
   * @param  newVal  The new value for this property
   */
  public void setClaimId(final Integer newVal) {
    claimId = newVal;
  }

  /**
   * AdministrativeCostShare are costs incurred by the client for administrative
   * purposes.
   *
   * @return  Double
   */
  public Double getAdministrativeCostShare() {
    return administrativeCostShare;
  }

  /**
   * AdministrativeCostShare are costs incurred by the client for administrative
   * purposes.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdministrativeCostShare(final Double newVal) {
    administrativeCostShare = newVal;
  }

  /**
   * LateApplicationPenalty is the penalty applied due to a late application to
   * Agri Stability program.
   *
   * @return  Double
   */
  public Double getLateApplicationPenalty() {
    return lateApplicationPenalty;
  }

  /**
   * LateApplicationPenalty is the penalty applied due to a late application to
   * Agri Stability program.
   *
   * @param  newVal  The new value for this property
   */
  public void setLateApplicationPenalty(final Double newVal) {
    lateApplicationPenalty = newVal;
  }

  /**
   * MaximumContribution is the maximum amount the client is eligible for in
   * this Benefit.
   *
   * @return  Double
   */
  public Double getMaximumContribution() {
    return maximumContribution;
  }

  /**
   * MaximumContribution is the maximum amount the client is eligible for in
   * this Benefit.
   *
   * @param  newVal  The new value for this property
   */
  public void setMaximumContribution(final Double newVal) {
    maximumContribution = newVal;
  }

  /**
   * OutstandingFees are fees which have not yet been paid by the
   * Client.
   *
   * @return  Double
   */
  public Double getOutstandingFees() {
    return outstandingFees;
  }

  /**
   * OutstandingFees are fees which have not yet been paid by the
   * Client.
   *
   * @param  newVal  The new value for this property
   */
  public void setOutstandingFees(final Double newVal) {
    outstandingFees = newVal;
  }

  /**
   * ProdInsurDeemedBenefit must not be blank if there is a negative margin
   * amount.
   *
   * @return  Double
   */
  public Double getProdInsurDeemedBenefit() {
    return prodInsurDeemedBenefit;
  }

  /**
   * ProdInsurDeemedBenefit must not be blank if there is a negative margin
   * amount.
   *
   * @param  newVal  The new value for this property
   */
  public void setProdInsurDeemedBenefit(final Double newVal) {
    prodInsurDeemedBenefit = newVal;
  }

  /**
   * ProgramYearMargin is determined by the Income less allowable expenses.
   *
   * @return  Double
   */
  public Double getProgramYearMargin() {
    return programYearMargin;
  }

  /**
   * ProgramYearMargin is determined by the Income less allowable expenses.
   *
   * @param  newVal  The new value for this property
   */
  public void setProgramYearMargin(final Double newVal) {
    programYearMargin = newVal;
  }

  /**
   * ProgramYearPaymentsReceived are payments received by the
   * Client to date.
   *
   * @return  Double
   */
  public Double getProgramYearPaymentsReceived() {
    return programYearPaymentsReceived;
  }

  /**
   * ProgramYearPaymentsReceived are payments received by the
   * Client to date.
   *
   * @param  newVal  The new value for this property
   */
  public void setProgramYearPaymentsReceived(final Double newVal) {
    programYearPaymentsReceived = newVal;
  }

  /**
   * @return the referenceMarginLimit
   */
  public Double getReferenceMarginLimit() {
    return referenceMarginLimit;
  }


  /**
   * @param referenceMarginLimit the referenceMarginLimit to set
   */
  public void setReferenceMarginLimit(Double referenceMarginLimit) {
    this.referenceMarginLimit = referenceMarginLimit;
  }

  public Double getReferenceMarginLimitCap() {
    return referenceMarginLimitCap;
  }

  public void setReferenceMarginLimitCap(Double referenceMarginLimitCap) {
    this.referenceMarginLimitCap = referenceMarginLimitCap;
  }

  public Double getReferenceMarginLimitForBenefitCalc() {
    return referenceMarginLimitForBenefitCalc;
  }

  public void setReferenceMarginLimitForBenefitCalc(Double referenceMarginLimitForBenefitCalc) {
    this.referenceMarginLimitForBenefitCalc = referenceMarginLimitForBenefitCalc;
  }

  /**
   * AllocatedReferenceMargin is the reference margin allocated to the
   * Margin.
   *
   * @return  Double
   */
  public Double getAllocatedReferenceMargin() {
    return allocatedReferenceMargin;
  }

  /**
   * AllocatedReferenceMargin is the reference margin allocated to the
   * Margin.
   *
   * @param  newVal  The new value for this property
   */
  public void setAllocatedReferenceMargin(final Double newVal) {
    allocatedReferenceMargin = newVal;
  }

  /**
   * RepaymentOfCashAdvances is the amount received by the Client
   * as cash advances to date.
   *
   * @return  Double
   */
  public Double getRepaymentOfCashAdvances() {
    return repaymentOfCashAdvances;
  }

  /**
   * RepaymentOfCashAdvances is the amount received by the Client
   * as cash advances to date.
   *
   * @param  newVal  The new value for this property
   */
  public void setRepaymentOfCashAdvances(final Double newVal) {
    repaymentOfCashAdvances = newVal;
  }

  /**
   * TotalPayment is the total amount of payment due to the Client.
   *
   * @return  Double
   */
  public Double getTotalPayment() {
    return totalPayment;
  }

  /**
   * TotalPayment is the total amount of payment due to the Client.
   *
   * @param  newVal  The new value for this property
   */
  public void setTotalPayment(final Double newVal) {
    totalPayment = newVal;
  }

  /**
   * SupplyManagedCommoditiesAdj is the supply managed adjusted commodities.
   *
   * @return  Double
   */
  public Double getSupplyManagedCommoditiesAdj() {
    return supplyManagedCommoditiesAdj;
  }

  /**
   * SupplyManagedCommoditiesAdj is the supply managed adjusted commodities.
   *
   * @param  newVal  The new value for this property
   */
  public void setSupplyManagedCommoditiesAdj(final Double newVal) {
    supplyManagedCommoditiesAdj = newVal;
  }

  /**
   * ProducerShare identifies the amount of AgriStability withdrawals that are
   * the producers share.
   *
   * @return  Double
   */
  public Double getProducerShare() {
    return producerShare;
  }

  /**
   * ProducerShare identifies the amount of AgriStability withdrawals that are
   * the producers share.
   *
   * @param  newVal  The new value for this property
   */
  public void setProducerShare(final Double newVal) {
    producerShare = newVal;
  }

  /**
   * FederalContributions identifies the amount of contributions from the
   * federal government that the participant has received.
   *
   * @return  Double
   */
  public Double getFederalContributions() {
    return federalContributions;
  }

  /**
   * FederalContributions identifies the amount of contributions from the
   * federal government that the participant has received.
   *
   * @param  newVal  The new value for this property
   */
  public void setFederalContributions(final Double newVal) {
    federalContributions = newVal;
  }

  /**
   * ProvincialContributions identifies the contributions from the provincial
   * government the participant has received.
   *
   * @return  Double
   */
  public Double getProvincialContributions() {
    return provincialContributions;
  }

  /**
   * ProvincialContributions identifies the contributions from the provincial
   * government the participant has received.
   *
   * @param  newVal  The new value for this property
   */
  public void setProvincialContributions(final Double newVal) {
    provincialContributions = newVal;
  }

  /**
   * InterimContributions is the amount of contributions have been allocated for
   * the participant if a final calculation has not yet been made. If a final
   * calculation has been made this amount will be zero.
   *
   * @return  Double
   */
  public Double getInterimContributions() {
    return interimContributions;
  }

  /**
   * InterimContributions is the amount of contributions have been allocated for
   * the participant if a final calculation has not yet been made. If a final
   * calculation has been made this amount will be zero.
   *
   * @param  newVal  The new value for this property
   */
  public void setInterimContributions(final Double newVal) {
    interimContributions = newVal;
  }

  /**
   * WholeFarmAllocation is the percentage of the farm associated with the
   * current ProgramYear.
   *
   * @return  Double
   */
  public Double getWholeFarmAllocation() {
    return wholeFarmAllocation;
  }

  /**
   * WholeFarmAllocation is the percentage of the farm associated with the
   * current ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setWholeFarmAllocation(final Double newVal) {
    wholeFarmAllocation = newVal;
  }

  /**
   * Tier2MarginDecline is the margin decline for tier2.
   *
   * @return  Double
   */
  public Double getTier2MarginDecline() {
    return tier2MarginDecline;
  }

  /**
   * Tier2MarginDecline is the margin decline for tier2.
   *
   * @param  newVal  The new value for this property
   */
  public void setTier2MarginDecline(final Double newVal) {
    tier2MarginDecline = newVal;
  }

  /**
   * Tier3MarginDecline is the margin decline for tier3.
   *
   * @return  Double
   */
  public Double getTier3MarginDecline() {
    return tier3MarginDecline;
  }

  /**
   * Tier3MarginDecline is the margin decline for tier3.
   *
   * @param  newVal  The new value for this property
   */
  public void setTier3MarginDecline(final Double newVal) {
    tier3MarginDecline = newVal;
  }

  /**
   * Tier2Benefit is the benefit for tier2.
   *
   * @return  Double
   */
  public Double getTier2Benefit() {
    return tier2Benefit;
  }

  /**
   * Tier2Benefit is the benefit for tier2.
   *
   * @param  newVal  The new value for this property
   */
  public void setTier2Benefit(final Double newVal) {
    tier2Benefit = newVal;
  }

  /**
   * Tier3Benefit is the benefit for tier3.
   *
   * @return  Double
   */
  public Double getTier3Benefit() {
    return tier3Benefit;
  }

  /**
   * Tier3Benefit is the benefit for tier3.
   *
   * @param  newVal  The new value for this property
   */
  public void setTier3Benefit(final Double newVal) {
    tier3Benefit = newVal;
  }

  /**
   * NegativeMarginBenefit is the negative amout of margin benefit.
   *
   * @return  Double
   */
  public Double getNegativeMarginBenefit() {
    return negativeMarginBenefit;
  }

  /**
   * NegativeMarginBenefit is the negative amout of margin benefit.
   *
   * @param  newVal  The new value for this property
   */
  public void setNegativeMarginBenefit(final Double newVal) {
    negativeMarginBenefit = newVal;
  }

  /**
   * NegativeMarginDecline is the negative amout of margin decline.
   *
   * @return  Double
   */
  public Double getNegativeMarginDecline() {
    return negativeMarginDecline;
  }

  /**
   * NegativeMarginDecline is the negative amout of margin decline.
   *
   * @param  newVal  The new value for this property
   */
  public void setNegativeMarginDecline(final Double newVal) {
    negativeMarginDecline = newVal;
  }

  /**
   * TotalBenefit is the total calculated benefit prior to deductions.
   *
   * @return  Double
   */
  public Double getTotalBenefit() {
    return totalBenefit;
  }

  /**
   * TotalBenefit is the total calculated benefit prior to deductions.
   *
   * @param  newVal  The new value for this property
   */
  public void setTotalBenefit(final Double newVal) {
    totalBenefit = newVal;
  }

  public Double getRatioAdjustedReferenceMargin() {
    return ratioAdjustedReferenceMargin;
  }


  public void setRatioAdjustedReferenceMargin(Double ratioAdjustedReferenceMargin) {
    this.ratioAdjustedReferenceMargin = ratioAdjustedReferenceMargin;
  }


  public Double getAdditiveAdjustedReferenceMargin() {
    return additiveAdjustedReferenceMargin;
  }


  public void setAdditiveAdjustedReferenceMargin(Double additiveAdjustedReferenceMargin) {
    this.additiveAdjustedReferenceMargin = additiveAdjustedReferenceMargin;
  }


  /**
   * AdjustedReferenceMargin is the adjustedReferenceMargin.
   *
   * @return  Double
   */
  public Double getAdjustedReferenceMargin() {
    return adjustedReferenceMargin;
  }

  /**
   * AdjustedReferenceMargin is the adjustedReferenceMargin.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjustedReferenceMargin(final Double newVal) {
    adjustedReferenceMargin = newVal;
  }

  /**
   * UnadjustedReferenceMargin is the adjusted reference margin.
   *
   * @return  Double
   */
  public Double getUnadjustedReferenceMargin() {
    return unadjustedReferenceMargin;
  }

  /**
   * UnadjustedReferenceMargin is the adjusted reference margin.
   *
   * @param  newVal  The new value for this property
   */
  public void setUnadjustedReferenceMargin(final Double newVal) {
    unadjustedReferenceMargin = newVal;
  }

  /**
   * @return  Double
   */
  public Double getMarginDecline() {
    return marginDecline;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setMarginDecline(final Double newVal) {
    marginDecline = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }

  

  /**
   * @return the farmingYear
   */
  public FarmingYear getFarmingYear() {
    return farmingYear;
  }


  /**
   * @param farmingYear the farmingYear to set the value to
   */
  public void setFarmingYear(FarmingYear farmingYear) {
    this.farmingYear = farmingYear;
  }
  

  /**
   * @return the tier2Trigger
   */
  public Double getTier2Trigger() {
    return tier2Trigger;
  }


  /**
   * @param tier2Trigger the tier2Trigger to set
   */
  public void setTier2Trigger(Double tier2Trigger) {
    this.tier2Trigger = tier2Trigger;
  }


  /**
   * @return the tier3Trigger
   */
  public Double getTier3Trigger() {
    return tier3Trigger;
  }


  /**
   * @param tier3Trigger the tier3Trigger to set
   */
  public void setTier3Trigger(Double tier3Trigger) {
    this.tier3Trigger = tier3Trigger;
  }
  
  @JsonIgnore
  public Double getCombinedFarmPercent() {
    return getAppliedBenefitPercent();
  }

  public Double getAppliedBenefitPercent() {
    return appliedBenefitPercent;
  }


  /**
   * @param appliedBenefitPercent the appliedBenefitPercent to set
   */
  public void setAppliedBenefitPercent(Double appliedBenefitPercent) {
    this.appliedBenefitPercent = appliedBenefitPercent;
  }


  public Double getInterimBenefitPercent() {
    return interimBenefitPercent;
  }


  public void setInterimBenefitPercent(Double interimBenefitPercent) {
    this.interimBenefitPercent = interimBenefitPercent;
  }


  /**
   * @return the benefitAfterInterimDeduction
   */
  public Double getBenefitAfterInterimDeduction() {
    return benefitAfterInterimDeduction;
  }


  /**
   * @param benefitAfterInterimDeduction the benefitAfterInterimDeduction to set
   */
  public void setBenefitAfterInterimDeduction(Double benefitAfterInterimDeduction) {
    this.benefitAfterInterimDeduction = benefitAfterInterimDeduction;
  }

  public Double getLateEnrolmentPenalty() {
    return lateEnrolmentPenalty;
  }

  public void setLateEnrolmentPenalty(Double lateEnrolmentPenalty) {
    this.lateEnrolmentPenalty = lateEnrolmentPenalty;
  }

  public Double getBenefitAfterLateEnrolmentPenalty() {
    return benefitAfterLateEnrolmentPenalty;
  }

  public void setBenefitAfterLateEnrolmentPenalty(Double benefitAfterLateEnrolmentPenalty) {
    this.benefitAfterLateEnrolmentPenalty = benefitAfterLateEnrolmentPenalty;
  }

  public Double getLateEnrolmentPenaltyAfterAppliedBenefitPercent() {
    return lateEnrolmentPenaltyAfterAppliedBenefitPercent;
  }


  public void setLateEnrolmentPenaltyAfterAppliedBenefitPercent(Double lateEnrolmentPenaltyAfterAppliedBenefitPercent) {
    this.lateEnrolmentPenaltyAfterAppliedBenefitPercent = lateEnrolmentPenaltyAfterAppliedBenefitPercent;
  }


  /**
   * @return the benefitAfterProdInsDeduction
   */
  public Double getBenefitAfterProdInsDeduction() {
    return benefitAfterProdInsDeduction;
  }


  /**
   * @param benefitAfterProdInsDeduction the benefitAfterProdInsDeduction to set
   */
  public void setBenefitAfterProdInsDeduction(Double benefitAfterProdInsDeduction) {
    this.benefitAfterProdInsDeduction = benefitAfterProdInsDeduction;
  }


  /**
   * @return the benefitAfterAppliedBenefitPercent
   */
  public Double getBenefitAfterAppliedBenefitPercent() {
    return benefitAfterAppliedBenefitPercent;
  }


  /**
   * @param benefitAfterAppliedBenefitPercent the benefitAfterAppliedBenefitPercent to set
   */
  public void setBenefitAfterAppliedBenefitPercent(Double benefitAfterAppliedBenefitPercent) {
    this.benefitAfterAppliedBenefitPercent = benefitAfterAppliedBenefitPercent;
  }


  /**
   * @return the benefitBeforeDeductions
   */
  public Double getBenefitBeforeDeductions() {
    return benefitBeforeDeductions;
  }


  /**
   * @param benefitBeforeDeductions the benefitBeforeDeductions to set
   */
  public void setBenefitBeforeDeductions(Double benefitBeforeDeductions) {
    this.benefitBeforeDeductions = benefitBeforeDeductions;
  }

  /**
   * @return  the structuralChangeMethodCode
   */
  public String getStructuralChangeMethodCode() {
    return structuralChangeMethodCode;
  }

  /**
   * @param  newVal  the structuralChangeMethodCode to set
   */
  public void setStructuralChangeMethodCode(final String newVal) {
    this.structuralChangeMethodCode = newVal;
  }

  /**
   * @return  the structuralChangeMethodCodeDescription
   */
  public String getStructuralChangeMethodCodeDescription() {
    return structuralChangeMethodCodeDescription;
  }

  /**
   * @param  newVal  the structuralChangeMethodCodeDescription to set
   */
  public void setStructuralChangeMethodCodeDescription(final String newVal) {
    this.structuralChangeMethodCodeDescription = newVal;
  }
  
  /**
   * @return  the expenseStructuralChangeMethodCode
   */
  public String getExpenseStructuralChangeMethodCode() {
    return expenseStructuralChangeMethodCode;
  }

  /**
   * @param  newVal  the expenseStructuralChangeMethodCode to set
   */
  public void setExpenseStructuralChangeMethodCode(final String newVal) {
    this.expenseStructuralChangeMethodCode = newVal;
  }

  /**
   * @return  the expenseStructuralChangeMethodCodeDescription
   */
  public String getExpenseStructuralChangeMethodCodeDescription() {
    return expenseStructuralChangeMethodCodeDescription;
  }

  /**
   * @param  newVal  the expenseStructuralChangeMethodCodeDescription to set
   */
  public void setExpenseStructuralChangeMethodCodeDescription(final String newVal) {
    this.expenseStructuralChangeMethodCodeDescription = newVal;
  }

  public Double getEnhancedTotalBenefit() {
    return enhancedTotalBenefit;
  }

  public void setEnhancedTotalBenefit(Double enhancedTotalBenefit) {
    this.enhancedTotalBenefit = enhancedTotalBenefit;
  }

  public Double getEnhancedPositiveMarginDecline() {
    return enhancedPositiveMarginDecline;
  }

  public void setEnhancedPositiveMarginDecline(Double enhancedPositiveMarginDecline) {
    this.enhancedPositiveMarginDecline = enhancedPositiveMarginDecline;
  }

  public Double getStandardBenefit() {
    return standardBenefit;
  }

  public void setStandardBenefit(Double standardBenefit) {
    this.standardBenefit = standardBenefit;
  }

  public Double getEnhancedBenefitAfterInterimDeduction() {
    return enhancedBenefitAfterInterimDeduction;
  }

  public void setEnhancedBenefitAfterInterimDeduction(Double enhancedBenefitAfterInterimDeduction) {
    this.enhancedBenefitAfterInterimDeduction = enhancedBenefitAfterInterimDeduction;
  }

  public Double getEnhancedReferenceMarginForBenefitCalculation() {
    return enhancedReferenceMarginForBenefitCalculation;
  }

  public void setEnhancedReferenceMarginForBenefitCalculation(Double enhancedReferenceMarginForBenefitCalculation) {
    this.enhancedReferenceMarginForBenefitCalculation = enhancedReferenceMarginForBenefitCalculation;
  }

  public Double getEnhancedPositiveMarginBenefit() {
    return enhancedPositiveMarginBenefit;
  }

  public void setEnhancedPositiveMarginBenefit(Double enhancedPositiveMarginBenefit) {
    this.enhancedPositiveMarginBenefit = enhancedPositiveMarginBenefit;
  }

  public Double getEnhancedBenefitBeforeDeductions() {
    return enhancedBenefitBeforeDeductions;
  }

  public void setEnhancedBenefitBeforeDeductions(Double enhancedBenefitBeforeDeductions) {
    this.enhancedBenefitBeforeDeductions = enhancedBenefitBeforeDeductions;
  }

  public Double getEnhancedBenefitAfterProdInsDeduction() {
    return enhancedBenefitAfterProdInsDeduction;
  }

  public void setEnhancedBenefitAfterProdInsDeduction(Double enhancedBenefitAfterProdInsDeduction) {
    this.enhancedBenefitAfterProdInsDeduction = enhancedBenefitAfterProdInsDeduction;
  }

  public Double getEnhancedMarginDecline() {
    return enhancedMarginDecline;
  }

  public void setEnhancedMarginDecline(Double enhancedMarginDecline) {
    this.enhancedMarginDecline = enhancedMarginDecline;
  }

  public Double getEnhancedAdditionalBenefit() {
    return enhancedAdditionalBenefit;
  }

  public void setEnhancedAdditionalBenefit(Double enhancedAdditionalBenefit) {
    this.enhancedAdditionalBenefit = enhancedAdditionalBenefit;
  }


  public Double getEnhancedBenefitAfterAppliedBenefitPercent() {
    return enhancedBenefitAfterAppliedBenefitPercent;
  }


  public void setEnhancedBenefitAfterAppliedBenefitPercent(Double enhancedBenefitAfterAppliedBenefitPercent) {
    this.enhancedBenefitAfterAppliedBenefitPercent = enhancedBenefitAfterAppliedBenefitPercent;
  }
  
  public Double getEnhancedNegativeMarginDecline() {
    return enhancedNegativeMarginDecline;
  }

  public void setEnhancedNegativeMarginDecline(Double enhancedNegativeMarginDecline) {
    this.enhancedNegativeMarginDecline = enhancedNegativeMarginDecline;
  }

  public Double getEnhancedNegativeMarginBenefit() {
    return enhancedNegativeMarginBenefit;
  }

  public void setEnhancedNegativeMarginBenefit(Double enhancedNegativeMarginBenefit) {
    this.enhancedNegativeMarginBenefit = enhancedNegativeMarginBenefit;
  }

  public Double getEnhancedLateEnrolmentPenalty() {
    return enhancedLateEnrolmentPenalty;
  }

  public void setEnhancedLateEnrolmentPenalty(Double enhancedLateEnrolmentPenalty) {
    this.enhancedLateEnrolmentPenalty = enhancedLateEnrolmentPenalty;
  }

  public Double getEnhancedBenefitAfterLateEnrolmentPenalty() {
    return enhancedBenefitAfterLateEnrolmentPenalty;
  }

  public void setEnhancedBenefitAfterLateEnrolmentPenalty(Double enhancedBenefitAfterLateEnrolmentPenalty) {
    this.enhancedBenefitAfterLateEnrolmentPenalty = enhancedBenefitAfterLateEnrolmentPenalty;
  }

  public Double getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent() {
    return enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent;
  }

  public void setEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent(Double enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent) {
    this.enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent = enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent;
  }

  public Double getPaymentCap() {
    return paymentCap;
  }

  public void setPaymentCap(Double paymentCap) {
    this.paymentCap = paymentCap;
  }

  public Double getBenefitAfterPaymentCap() {
    return benefitAfterPaymentCap;
  }

  public void setBenefitAfterPaymentCap(Double benefitAfterPaymentCap) {
    this.benefitAfterPaymentCap = benefitAfterPaymentCap;
  }

  public Double getEnhancedBenefitAfterPaymentCap() {
    return enhancedBenefitAfterPaymentCap;
  }

  public void setEnhancedBenefitAfterPaymentCap(Double enhancedBenefitAfterPaymentCap) {
    this.enhancedBenefitAfterPaymentCap = enhancedBenefitAfterPaymentCap;
  }

  public Boolean getProdInsurDeemedBenefitManuallyCalculated() {
    return prodInsurDeemedBenefitManuallyCalculated;
  }

  public void setProdInsurDeemedBenefitManuallyCalculated(Boolean prodInsurDeemedBenefitManuallyCalculated) {
    this.prodInsurDeemedBenefitManuallyCalculated = prodInsurDeemedBenefitManuallyCalculated;
  }


  @JsonIgnore
  public Double getMarginDeclineFromPaymentTrigger() {
    Double result = null;
    if(tier3MarginDecline != null && negativeMarginDecline != null) {
      result = tier3MarginDecline + negativeMarginDecline;
    }
    return result;
  }

  @JsonIgnore
  public Double getProductionInsuranceDeduction() {
    Double result = null;
    if(benefitBeforeDeductions != null && benefitAfterProdInsDeduction != null) {
      result = benefitAfterProdInsDeduction - benefitBeforeDeductions;
    }
    return result;
  }

  @JsonIgnore
  public Double getEnhancedProductionInsuranceDeduction() {
    Double result = null;
    if(enhancedBenefitBeforeDeductions != null && enhancedBenefitAfterProdInsDeduction != null) {
      result = enhancedBenefitAfterProdInsDeduction - enhancedBenefitBeforeDeductions;
    }
    return result;
  }

  /**
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingYearId = null;
    if(farmingYear != null) {
      farmingYearId = farmingYear.getProgramYearVersionId();
    }

    return "Benefit"+"\n"+
    "\t farmingYear : "+farmingYearId+"\n"+
    "\t structuralChangeMethodCode : "+structuralChangeMethodCode+"\n"+
    "\t structuralChangeMethodCodeDescription : "+structuralChangeMethodCodeDescription+"\n"+
    "\t expenseStructuralChangeMethodCode : "+expenseStructuralChangeMethodCode+"\n"+
    "\t expenseStructuralChangeMethodCodeDescription : "+expenseStructuralChangeMethodCodeDescription+"\n"+
    "\t ratioAdjustedReferenceMargin : "+ratioAdjustedReferenceMargin+"\n"+
    "\t additiveAdjustedReferenceMargin : "+additiveAdjustedReferenceMargin+"\n"+
    "\t adjustedReferenceMargin : "+adjustedReferenceMargin+"\n"+
    "\t administrativeCostShare : "+administrativeCostShare+"\n"+
    "\t claimId : "+claimId+"\n"+
    "\t referenceMarginLimit : "+referenceMarginLimit+"\n"+
    "\t referenceMarginLimitCap : "+referenceMarginLimitCap+"\n"+
    "\t referenceMarginLimitForBenefitCalc : "+referenceMarginLimitForBenefitCalc+"\n"+
    "\t allocatedReferenceMargin : "+allocatedReferenceMargin+"\n"+
    "\t federalContributions : "+federalContributions+"\n"+
    "\t interimContributions : "+interimContributions+"\n"+
    "\t lateApplicationPenalty : "+lateApplicationPenalty+"\n"+
    "\t marginDecline : "+marginDecline+"\n"+
    "\t maximumContribution : "+maximumContribution+"\n"+
    "\t negativeMarginBenefit : "+negativeMarginBenefit+"\n"+
    "\t negativeMarginDecline : "+negativeMarginDecline+"\n"+
    "\t outstandingFees : "+outstandingFees+"\n"+
    "\t prodInsurDeemedBenefit : "+prodInsurDeemedBenefit+"\n"+
    "\t prodInsurDeemedBenefitManuallyCalculated : "+prodInsurDeemedBenefitManuallyCalculated+"\n"+
    "\t producerShare : "+producerShare+"\n"+
    "\t programYearMargin : "+programYearMargin+"\n"+
    "\t programYearPaymentsReceived : "+programYearPaymentsReceived+"\n"+
    "\t provincialContributions : "+provincialContributions+"\n"+
    "\t repaymentOfCashAdvances : "+repaymentOfCashAdvances+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t supplyManagedCommoditiesAdj : "+supplyManagedCommoditiesAdj+"\n"+
    "\t tier2Benefit : "+tier2Benefit+"\n"+
    "\t tier2MarginDecline : "+tier2MarginDecline+"\n"+
    "\t tier3Benefit : "+tier3Benefit+"\n"+
    "\t tier3MarginDecline : "+tier3MarginDecline+"\n"+
    "\t totalBenefit : "+totalBenefit+"\n"+
    "\t totalPayment : "+totalPayment+"\n"+
    "\t unadjustedReferenceMargin : "+unadjustedReferenceMargin+"\n"+
    "\t wholeFarmAllocation : "+wholeFarmAllocation+"\n"+
    "\t tier2Trigger : "+tier2Trigger+"\n"+
    "\t tier3Trigger : "+tier3Trigger+"\n"+
    "\t benefitBeforeDeductions : "+benefitBeforeDeductions+"\n"+
    "\t benefitAfterProdInsDeduction : "+benefitAfterProdInsDeduction+"\n"+
    "\t interimBenefitPercent : "+interimBenefitPercent+"\n"+
    "\t appliedBenefitPercent : "+appliedBenefitPercent+"\n"+
    "\t benefitAfterAppliedBenefitPercent : "+benefitAfterAppliedBenefitPercent+"\n"+
    "\t benefitAfterInterimDeduction : "+benefitAfterInterimDeduction+"\n"+
    "\t paymentCap : "+paymentCap+"\n"+
    "\t benefitAfterPaymentCap : "+benefitAfterPaymentCap+"\n"+
    "\t lateEnrolmentPenalty : "+lateEnrolmentPenalty+"\n"+
    "\t benefitAfterLateEnrolmentPenalty : "+benefitAfterLateEnrolmentPenalty+"\n"+
    "\t lateEnrolmentPenaltyAfterAppliedBenefitPercent : "+lateEnrolmentPenaltyAfterAppliedBenefitPercent+"\n"+
    "\t enhancedReferenceMarginForBenefitCalculation : "+enhancedReferenceMarginForBenefitCalculation+"\n"+
    "\t enhancedPositiveMarginBenefit : "+enhancedPositiveMarginBenefit+"\n"+
    "\t enhancedMarginDecline : "+enhancedMarginDecline+"\n"+
    "\t enhancedBenefitBeforeDeductions : "+enhancedBenefitBeforeDeductions+"\n"+
    "\t enhancedBenefitAfterProdInsDeduction : "+enhancedBenefitAfterProdInsDeduction+"\n"+
    "\t enhancedPositiveMarginDecline : "+enhancedPositiveMarginDecline+"\n"+
    "\t enhancedNegativeMarginDecline : "+enhancedNegativeMarginDecline+"\n"+
    "\t enhancedBenefitAfterInterimDeduction : "+enhancedBenefitAfterInterimDeduction+"\n"+
    "\t enhancedTotalBenefit : "+enhancedTotalBenefit+"\n"+
    "\t enhancedAdditionalBenefit : "+enhancedAdditionalBenefit+"\n"+
    "\t enhancedBenefitAfterAppliedBenefitPercent : "+enhancedBenefitAfterAppliedBenefitPercent+"\n"+
    "\t enhancedLateEnrolmentPenalty : "+enhancedLateEnrolmentPenalty+"\n"+
    "\t enhancedBenefitAfterLateEnrolmentPenalty : "+enhancedBenefitAfterLateEnrolmentPenalty+"\n"+
    "\t enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent : "+enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent+"\n"+
    "\t enhancedBenefitAfterPaymentCap : "+enhancedBenefitAfterPaymentCap+"\n"+
    "\t standardBenefit : "+standardBenefit;
  }

}
