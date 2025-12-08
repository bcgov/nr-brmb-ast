/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.enrolment;

/**
 * @author awilkinson
 */
public class EnwProductiveValue {

  private Double bpuMargin;
  
  private Double productiveValue;

  public Double getBpuMargin() {
    return bpuMargin;
  }

  public void setBpuMargin(Double bpuMargin) {
    this.bpuMargin = bpuMargin;
  }

  public Double getProductiveValue() {
    return productiveValue;
  }

  public void setProductiveValue(Double productiveValue) {
    this.productiveValue = productiveValue;
  }

  @Override
  public String toString() {
    return "EnwProductiveValue [bpuMargin=" + bpuMargin + ", productiveValue=" + productiveValue + "]";
  }
  
}
