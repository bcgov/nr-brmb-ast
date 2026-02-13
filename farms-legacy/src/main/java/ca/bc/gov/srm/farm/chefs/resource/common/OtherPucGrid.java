/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.common;

import java.util.Objects;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class OtherPucGrid extends ChefsResource {

  private LabelValue selectOtherLivestock;
  private Integer otherLivestockNumber;

  public OtherPucGrid() {
    super();
  }

  public OtherPucGrid(String label, String value, Integer otherLivestockNumber) {
    super();
    this.selectOtherLivestock = new LabelValue(label, value);
    this.otherLivestockNumber = otherLivestockNumber;
  }

  public LabelValue getSelectOtherLivestock() {
    return selectOtherLivestock;
  }

  public void setSelectOtherLivestock(LabelValue selectOtherLivestock) {
    this.selectOtherLivestock = selectOtherLivestock;
  }

  public Integer getOtherLivestockNumber() {
    return otherLivestockNumber;
  }

  public void setOtherLivestockNumber(Integer otherLivestockNumber) {
    this.otherLivestockNumber = otherLivestockNumber;
  }

  @Override
  public String toString() {
    return "SupplementalOtherGrid [selectOtherLivestock=" + selectOtherLivestock + ", otherLivestockNumber=" + otherLivestockNumber + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(otherLivestockNumber, selectOtherLivestock);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    OtherPucGrid other = (OtherPucGrid) obj;
    return Objects.equals(otherLivestockNumber, other.otherLivestockNumber) && Objects.equals(selectOtherLivestock, other.selectOtherLivestock);
  }

}