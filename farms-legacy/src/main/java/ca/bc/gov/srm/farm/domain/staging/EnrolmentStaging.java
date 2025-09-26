/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.staging;

import java.util.Date;


/**
 * @author awilkinson
 */
public class EnrolmentStaging {
  
  private Integer pin;

  private Integer enrolmentYear;
  
  private Double enrolmentFee;
  
  private Date generatedDate;
  
  private String failedReason;
  
  private Boolean failedToGenerate;
  
  private Boolean failedToCalculateFromBenefitMargins;
  
  private Boolean isError;
  
  private Double contributionMarginAverage;
  private Double marginYearMinus2;
  private Double marginYearMinus3;
  private Double marginYearMinus4;
  private Double marginYearMinus5;
  private Double marginYearMinus6;

  private Boolean isMarginYearMinus2Used;
  private Boolean isMarginYearMinus3Used;
  private Boolean isMarginYearMinus4Used;
  private Boolean isMarginYearMinus5Used;
  private Boolean isMarginYearMinus6Used;
  
  /** The scenario ID from which the program year margins
   *  were retrieved and the fee was calculated */
  private Integer marginScenarioId;

  private Boolean isGeneratedFromCra;
  private Boolean isGeneratedFromEnw;
  private Boolean isCreateTaskInBarn;

  private Double combinedFarmPercent;
  
  /**
   * Gets pin
   *
   * @return the pin
   */
  public Integer getPin() {
    return pin;
  }

  /**
   * Sets pin
   *
   * @param pin the pin to set
   */
  public void setPin(Integer pin) {
    this.pin = pin;
  }

  /**
   * Gets enrolmentYear
   *
   * @return the enrolmentYear
   */
  public Integer getEnrolmentYear() {
    return enrolmentYear;
  }

  /**
   * Sets enrolmentYear
   *
   * @param enrolmentYear the enrolmentYear to set
   */
  public void setEnrolmentYear(Integer enrolmentYear) {
    this.enrolmentYear = enrolmentYear;
  }

  /**
   * Gets enrolmentFee
   *
   * @return the enrolmentFee
   */
  public Double getEnrolmentFee() {
    return enrolmentFee;
  }

  /**
   * Sets enrolmentFee
   *
   * @param enrolmentFee the enrolmentFee to set
   */
  public void setEnrolmentFee(Double enrolmentFee) {
    this.enrolmentFee = enrolmentFee;
  }

  /**
   * Gets generatedDate
   *
   * @return the generatedDate
   */
  public Date getGeneratedDate() {
    return generatedDate;
  }

  /**
   * Sets generatedDate
   *
   * @param generatedDate the generatedDate to set
   */
  public void setGeneratedDate(Date generatedDate) {
    this.generatedDate = generatedDate;
  }

  /**
   * Gets failedReason
   *
   * @return the failedReason
   */
  public String getFailedReason() {
    return failedReason;
  }

  /**
   * Sets failedReason
   *
   * @param failedReason the failedReason to set
   */
  public void setFailedReason(String failedReason) {
    this.failedReason = failedReason;
  }

  /**
   * Gets failedToGenerate
   *
   * @return the failedToGenerate
   */
  public Boolean getFailedToGenerate() {
    return failedToGenerate;
  }

  /**
   * Sets failedToGenerate
   *
   * @param failedToGenerate the failedToGenerate to set
   */
  public void setFailedToGenerate(Boolean failedToGenerate) {
    this.failedToGenerate = failedToGenerate;
  }

  /**
   * Gets isError
   *
   * @return the isError
   */
  public Boolean getIsError() {
    return isError;
  }

  /**
   * Sets isError
   *
   * @param isError the isError to set
   */
  public void setIsError(Boolean isError) {
    this.isError = isError;
  }

  /**
   * Gets contributionMarginAverage
   *
   * @return the contributionMarginAverage
   */
  public Double getContributionMarginAverage() {
    return contributionMarginAverage;
  }

  /**
   * Sets contributionMarginAverage
   *
   * @param contributionMarginAverage the contributionMarginAverage to set
   */
  public void setContributionMarginAverage(Double contributionMarginAverage) {
    this.contributionMarginAverage = contributionMarginAverage;
  }

  /**
   * Gets marginYearMinus2
   *
   * @return the marginYearMinus2
   */
  public Double getMarginYearMinus2() {
    return marginYearMinus2;
  }

  /**
   * Sets marginYearMinus2
   *
   * @param marginYearMinus2 the marginYearMinus2 to set
   */
  public void setMarginYearMinus2(Double marginYearMinus2) {
    this.marginYearMinus2 = marginYearMinus2;
  }

  /**
   * Gets marginYearMinus3
   *
   * @return the marginYearMinus3
   */
  public Double getMarginYearMinus3() {
    return marginYearMinus3;
  }

  /**
   * Sets marginYearMinus3
   *
   * @param marginYearMinus3 the marginYearMinus3 to set
   */
  public void setMarginYearMinus3(Double marginYearMinus3) {
    this.marginYearMinus3 = marginYearMinus3;
  }

  /**
   * Gets marginYearMinus4
   *
   * @return the marginYearMinus4
   */
  public Double getMarginYearMinus4() {
    return marginYearMinus4;
  }

  /**
   * Sets marginYearMinus4
   *
   * @param marginYearMinus4 the marginYearMinus4 to set
   */
  public void setMarginYearMinus4(Double marginYearMinus4) {
    this.marginYearMinus4 = marginYearMinus4;
  }

  /**
   * Gets marginYearMinus5
   *
   * @return the marginYearMinus5
   */
  public Double getMarginYearMinus5() {
    return marginYearMinus5;
  }

  /**
   * Sets marginYearMinus5
   *
   * @param marginYearMinus5 the marginYearMinus5 to set
   */
  public void setMarginYearMinus5(Double marginYearMinus5) {
    this.marginYearMinus5 = marginYearMinus5;
  }

  /**
   * Gets marginYearMinus6
   *
   * @return the marginYearMinus6
   */
  public Double getMarginYearMinus6() {
    return marginYearMinus6;
  }

  /**
   * Sets marginYearMinus6
   *
   * @param marginYearMinus6 the marginYearMinus6 to set
   */
  public void setMarginYearMinus6(Double marginYearMinus6) {
    this.marginYearMinus6 = marginYearMinus6;
  }

  /**
   * Gets isMarginYearMinus2Used
   *
   * @return the isMarginYearMinus2Used
   */
  public Boolean getIsMarginYearMinus2Used() {
    return isMarginYearMinus2Used;
  }

  /**
   * Sets isMarginYearMinus2Used
   *
   * @param isMarginYearMinus2Used the isMarginYearMinus2Used to set
   */
  public void setIsMarginYearMinus2Used(Boolean isMarginYearMinus2Used) {
    this.isMarginYearMinus2Used = isMarginYearMinus2Used;
  }

  /**
   * Gets isMarginYearMinus3Used
   *
   * @return the isMarginYearMinus3Used
   */
  public Boolean getIsMarginYearMinus3Used() {
    return isMarginYearMinus3Used;
  }

  /**
   * Sets isMarginYearMinus3Used
   *
   * @param isMarginYearMinus3Used the isMarginYearMinus3Used to set
   */
  public void setIsMarginYearMinus3Used(Boolean isMarginYearMinus3Used) {
    this.isMarginYearMinus3Used = isMarginYearMinus3Used;
  }

  /**
   * Gets isMarginYearMinus4Used
   *
   * @return the isMarginYearMinus4Used
   */
  public Boolean getIsMarginYearMinus4Used() {
    return isMarginYearMinus4Used;
  }

  /**
   * Sets isMarginYearMinus4Used
   *
   * @param isMarginYearMinus4Used the isMarginYearMinus4Used to set
   */
  public void setIsMarginYearMinus4Used(Boolean isMarginYearMinus4Used) {
    this.isMarginYearMinus4Used = isMarginYearMinus4Used;
  }

  /**
   * Gets isMarginYearMinus5Used
   *
   * @return the isMarginYearMinus5Used
   */
  public Boolean getIsMarginYearMinus5Used() {
    return isMarginYearMinus5Used;
  }

  /**
   * Sets isMarginYearMinus5Used
   *
   * @param isMarginYearMinus5Used the isMarginYearMinus5Used to set
   */
  public void setIsMarginYearMinus5Used(Boolean isMarginYearMinus5Used) {
    this.isMarginYearMinus5Used = isMarginYearMinus5Used;
  }

  /**
   * Gets isMarginYearMinus6Used
   *
   * @return the isMarginYearMinus6Used
   */
  public Boolean getIsMarginYearMinus6Used() {
    return isMarginYearMinus6Used;
  }

  /**
   * Sets isMarginYearMinus6Used
   *
   * @param isMarginYearMinus6Used the isMarginYearMinus6Used to set
   */
  public void setIsMarginYearMinus6Used(Boolean isMarginYearMinus6Used) {
    this.isMarginYearMinus6Used = isMarginYearMinus6Used;
  }

  /**
   * Gets marginScenarioId
   *
   * @return the marginScenarioId
   */
  public Integer getMarginScenarioId() {
    return marginScenarioId;
  }

  /**
   * Sets marginScenarioId
   *
   * @param marginScenarioId the marginScenarioId to set
   */
  public void setMarginScenarioId(Integer marginScenarioId) {
    this.marginScenarioId = marginScenarioId;
  }

  /**
   * Gets isGeneratedFromCra
   *
   * @return the isGeneratedFromCra
   */
  public Boolean getIsGeneratedFromCra() {
    return isGeneratedFromCra;
  }

  /**
   * Sets isGeneratedFromCra
   *
   * @param isGeneratedFromCra the isGeneratedFromCra to set
   */
  public void setIsGeneratedFromCra(Boolean isGeneratedFromCra) {
    this.isGeneratedFromCra = isGeneratedFromCra;
  }

  public Boolean getIsGeneratedFromEnw() {
    return isGeneratedFromEnw;
  }

  public void setIsGeneratedFromEnw(Boolean isGeneratedFromEnw) {
    this.isGeneratedFromEnw = isGeneratedFromEnw;
  }

  /**
   * @return the isCreateTaskInBarn
   */
  public Boolean getIsCreateTaskInBarn() {
    return isCreateTaskInBarn;
  }

  /**
   * @param isCreateTaskInBarn the isCreateTaskInBarn to set
   */
  public void setIsCreateTaskInBarn(Boolean isCreateTaskInBarn) {
    this.isCreateTaskInBarn = isCreateTaskInBarn;
  }

  public Double getCombinedFarmPercent() {
    return combinedFarmPercent;
  }

  public void setCombinedFarmPercent(Double combinedFarmPercent) {
    this.combinedFarmPercent = combinedFarmPercent;
  }

  public Boolean getFailedToCalculateFromBenefitMargins() {
    return failedToCalculateFromBenefitMargins;
  }

  public void setFailedToCalculateFromBenefitMargins(Boolean failedToCalculateFromBenefitMargins) {
    this.failedToCalculateFromBenefitMargins = failedToCalculateFromBenefitMargins;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){

    return "EnrolmentStaging"+"\n"+
    "\t pin : "+pin+"\n"+
    "\t enrolmentYear : "+enrolmentYear+"\n"+
    "\t enrolmentFee : "+enrolmentFee+"\n"+
    "\t generatedDate : "+generatedDate+"\n"+
    "\t failedToGenerate : "+failedToGenerate+"\n"+
    "\t failedToCalculateFromBenefitMargins : "+failedToCalculateFromBenefitMargins+"\n"+
    "\t isError : "+isError+"\n"+
    "\t failedReason : "+failedReason+"\n"+
    "\t contributionMarginAverage : "+contributionMarginAverage+"\n"+
    "\t marginYearMinus2 : "+marginYearMinus2+"\n"+
    "\t marginYearMinus3 : "+marginYearMinus3+"\n"+
    "\t marginYearMinus4 : "+marginYearMinus4+"\n"+
    "\t marginYearMinus5 : "+marginYearMinus5+"\n"+
    "\t marginYearMinus6 : "+marginYearMinus6+"\n"+
    "\t isMarginYearMinus2Used : "+isMarginYearMinus2Used+"\n"+
    "\t isMarginYearMinus3Used : "+isMarginYearMinus3Used+"\n"+
    "\t isMarginYearMinus4Used : "+isMarginYearMinus4Used+"\n"+
    "\t isMarginYearMinus5Used : "+isMarginYearMinus5Used+"\n"+
    "\t isMarginYearMinus6Used : "+isMarginYearMinus6Used+"\n"+
    "\t marginScenarioId : "+marginScenarioId+"\n"+
    "\t isGeneratedFromCra : "+isGeneratedFromCra+"\n"+
    "\t isGeneratedFromEnw : "+isGeneratedFromEnw+"\n"+
    "\t isCreateTaskInBarn : "+isCreateTaskInBarn+"\n"+
    "\t combinedFarmPercent : "+combinedFarmPercent+"\n";
  }

}
