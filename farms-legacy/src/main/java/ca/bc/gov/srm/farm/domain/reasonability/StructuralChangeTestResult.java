/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.reasonability;



/**
 * @author awilkinson
 */
public class StructuralChangeTestResult extends ReasonabilityTestResult {

  private static final long serialVersionUID = 1L;

  private Double productionMargAccrAdjs;
  
  private Boolean withinRatioAdditiveDiffLimit;
  private Double ratioAdditiveDiffVarianceLimit;
  private Double ratioReferenceMargin;
  private Double additiveReferenceMargin;
  private Double ratioAdditiveDiffVariance;
  
  private Boolean withinAdditiveDivisionLimit;
  private Double additiveDivisionRatioLimit;
  private Double additiveDivisionRatio;
  
  private String methodToUse;

  private Boolean withinFarmSizeRatioLimit;
  private Double farmSizeRatioLimit;

  public Double getProductionMargAccrAdjs() {
    return productionMargAccrAdjs;
  }

  public void setProductionMargAccrAdjs(Double productionMargAccrAdjs) {
    this.productionMargAccrAdjs = productionMargAccrAdjs;
  }

  public Boolean getWithinRatioAdditiveDiffLimit() {
    return withinRatioAdditiveDiffLimit;
  }

  public void setWithinRatioAdditiveDiffLimit(Boolean withinRatioAdditiveDiffLimit) {
    this.withinRatioAdditiveDiffLimit = withinRatioAdditiveDiffLimit;
  }

  public Double getRatioAdditiveDiffVarianceLimit() {
    return ratioAdditiveDiffVarianceLimit;
  }

  public void setRatioAdditiveDiffVarianceLimit(Double ratioAdditiveDiffVarianceLimit) {
    this.ratioAdditiveDiffVarianceLimit = ratioAdditiveDiffVarianceLimit;
  }

  public Boolean getWithinAdditiveDivisionLimit() {
    return withinAdditiveDivisionLimit;
  }

  public void setWithinAdditiveDivisionLimit(Boolean withinAdditiveDivisionLimit) {
    this.withinAdditiveDivisionLimit = withinAdditiveDivisionLimit;
  }

  public String getMethodToUse() {
    return methodToUse;
  }

  public void setMethodToUse(String methodToUse) {
    this.methodToUse = methodToUse;
  }

  public Double getFarmSizeRatioLimit() {
    return farmSizeRatioLimit;
  }

  public void setFarmSizeRatioLimit(Double farmSizeRatioLimit) {
    this.farmSizeRatioLimit = farmSizeRatioLimit;
  }

  public Boolean getWithinFarmSizeRatioLimit() {
    return withinFarmSizeRatioLimit;
  }

  public void setWithinFarmSizeRatioLimit(Boolean withinFarmSizeRatioLimit) {
    this.withinFarmSizeRatioLimit = withinFarmSizeRatioLimit;
  }

  public Double getRatioReferenceMargin() {
    return ratioReferenceMargin;
  }

  public void setRatioReferenceMargin(Double ratioReferenceMargin) {
    this.ratioReferenceMargin = ratioReferenceMargin;
  }

  public Double getAdditiveReferenceMargin() {
    return additiveReferenceMargin;
  }

  public void setAdditiveReferenceMargin(Double additiveReferenceMargin) {
    this.additiveReferenceMargin = additiveReferenceMargin;
  }

  public Double getRatioAdditiveDiffVariance() {
    return ratioAdditiveDiffVariance;
  }

  public void setRatioAdditiveDiffVariance(Double ratioAdditiveDiffVariance) {
    this.ratioAdditiveDiffVariance = ratioAdditiveDiffVariance;
  }

  public Double getAdditiveDivisionRatioLimit() {
    return additiveDivisionRatioLimit;
  }

  public void setAdditiveDivisionRatioLimit(Double additiveDivisionRatioLimit) {
    this.additiveDivisionRatioLimit = additiveDivisionRatioLimit;
  }

  public Double getAdditiveDivisionRatio() {
    return additiveDivisionRatio;
  }

  public void setAdditiveDivisionRatio(Double additiveDivisionRatio) {
    this.additiveDivisionRatio = additiveDivisionRatio;
  }

  @Override
  public String toString() {
    return "StructuralChangeTestResult [\n"
        + "\t productionMargAccrAdjs=" + productionMargAccrAdjs + "\n"
        + "\t withinRatioAdditiveDiffLimit=" + withinRatioAdditiveDiffLimit + "\n"
        + "\t ratioAdditiveDiffVarianceLimit=" + ratioAdditiveDiffVarianceLimit + "\n"
        + "\t ratioReferenceMargin=" + ratioReferenceMargin + "\n"
        + "\t additiveReferenceMargin=" + additiveReferenceMargin + "\n"
        + "\t ratioAdditiveDiffVariance=" + ratioAdditiveDiffVariance + "\n"
        + "\t withinAdditiveDivisionLimit=" + withinAdditiveDivisionLimit + "\n"
        + "\t additiveDivisionRatioLimit=" + additiveDivisionRatioLimit + "\n"
        + "\t additiveDivisionRatio=" + additiveDivisionRatio + "\n"
        + "\t methodToUse=" + methodToUse + "\n"
        + "\t withinFarmSizeRatioLimit=" + withinFarmSizeRatioLimit + "\n"
        + "\t farmSizeRatioLimit=" + farmSizeRatioLimit + "]";
  }
  
  public void copy(StructuralChangeTestResult o) {
    super.copy(o);
    productionMargAccrAdjs = o.productionMargAccrAdjs;
    withinRatioAdditiveDiffLimit = o.withinRatioAdditiveDiffLimit;
    ratioAdditiveDiffVarianceLimit = o.ratioAdditiveDiffVarianceLimit;
    ratioReferenceMargin = o.ratioReferenceMargin;
    additiveReferenceMargin = o.additiveReferenceMargin;
    ratioAdditiveDiffVariance = o.ratioAdditiveDiffVariance;
    withinAdditiveDivisionLimit = o.withinAdditiveDivisionLimit;
    additiveDivisionRatioLimit = o.additiveDivisionRatioLimit;
    additiveDivisionRatio = o.additiveDivisionRatio;
    methodToUse = o.methodToUse;
    withinFarmSizeRatioLimit = o.withinFarmSizeRatioLimit;
    farmSizeRatioLimit = o.farmSizeRatioLimit;
  }

  @Override
  public String getName() {
    return null;
  }
}
