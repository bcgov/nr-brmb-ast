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
package ca.bc.gov.srm.farm.domain.codes;

/**
 * @author awilkinson
 */
public class FMVPeriod {

  private Integer fairMarketValueId;
  private Integer period;
  private Double price;
  private Double percentVariance;
  private Integer revisionCount;

  /**
   * Gets fairMarketValueId
   *
   * @return the fairMarketValueId
   */
  public Integer getFairMarketValueId() {
    return fairMarketValueId;
  }

  /**
   * Sets fairMarketValueId
   *
   * @param pFairMarketValueId the fairMarketValueId to set
   */
  public void setFairMarketValueId(Integer pFairMarketValueId) {
    fairMarketValueId = pFairMarketValueId;
  }

  /**
   * Gets period
   *
   * @return the period
   */
  public Integer getPeriod() {
    return period;
  }

  /**
   * Sets period
   *
   * @param pPeriod the period to set
   */
  public void setPeriod(Integer pPeriod) {
    period = pPeriod;
  }

  /**
   * Gets price
   *
   * @return the price
   */
  public Double getPrice() {
    return price;
  }

  /**
   * Sets price
   *
   * @param pPrice the price to set
   */
  public void setPrice(Double pPrice) {
    price = pPrice;
  }

  /**
   * Gets percentVariance
   *
   * @return the percentVariance
   */
  public Double getPercentVariance() {
    return percentVariance;
  }

  /**
   * Sets percentVariance
   *
   * @param pPercentVariance the percentVariance to set
   */
  public void setPercentVariance(Double pPercentVariance) {
    percentVariance = pPercentVariance;
  }

  /**
   * Gets revisionCount
   *
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * Sets revisionCount
   *
   * @param pRevisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer pRevisionCount) {
    revisionCount = pRevisionCount;
  }

  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    
    return "FMVPeriod"+"\n"+
    "\t fairMarketValueId : "+fairMarketValueId+"\n"+
    "\t period : "+period+"\n"+
    "\t price : "+price+"\n"+
    "\t percentVariance : "+percentVariance+"\n"+
    "\t revisionCount : "+revisionCount+"\n";
  }

}
