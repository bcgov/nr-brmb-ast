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

import java.util.List;

/**
 * @author awilkinson
 */
public class EnwProductiveUnit implements Comparable<EnwProductiveUnit> {

  private String code;
  
  private String description;
  
  private Double productiveCapacity;
  
  private List<EnwProductiveValue> productiveValues;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Double getProductiveCapacity() {
    return productiveCapacity;
  }

  public void setProductiveCapacity(Double productiveCapacity) {
    this.productiveCapacity = productiveCapacity;
  }

  public List<EnwProductiveValue> getProductiveValues() {
    return productiveValues;
  }

  public void setProductiveValues(List<EnwProductiveValue> productiveValues) {
    this.productiveValues = productiveValues;
  }
  
  @Override
  public int compareTo(EnwProductiveUnit o) {
    return this.getCode().compareTo(o.getCode());
  }
}
