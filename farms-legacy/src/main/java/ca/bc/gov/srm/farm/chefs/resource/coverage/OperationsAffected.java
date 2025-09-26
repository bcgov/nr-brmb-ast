package ca.bc.gov.srm.farm.chefs.resource.coverage;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class OperationsAffected extends ChefsResource {

  private Boolean none;
  private Boolean expansion;
  private Boolean downsizing;
  private Boolean changeInOwnership;
  private Boolean disasterOrDisease;
  private Boolean otherPleaseSpecify;
  private Boolean changeInCommoditiesOrVarieties;
  private Boolean largeScaleBreedingStockReplacement;

  public Boolean getNone() {
    return none;
  }

  public void setNone(Boolean none) {
    this.none = none;
  }

  public Boolean getExpansion() {
    return expansion;
  }

  public void setExpansion(Boolean expansion) {
    this.expansion = expansion;
  }

  public Boolean getDownsizing() {
    return downsizing;
  }

  public void setDownsizing(Boolean downsizing) {
    this.downsizing = downsizing;
  }

  public Boolean getChangeInOwnership() {
    return changeInOwnership;
  }

  public void setChangeInOwnership(Boolean changeInOwnership) {
    this.changeInOwnership = changeInOwnership;
  }

  public Boolean getDisasterOrDisease() {
    return disasterOrDisease;
  }

  public void setDisasterOrDisease(Boolean disasterOrDisease) {
    this.disasterOrDisease = disasterOrDisease;
  }

  public Boolean getOtherPleaseSpecify() {
    return otherPleaseSpecify;
  }

  public void setOtherPleaseSpecify(Boolean otherPleaseSpecify) {
    this.otherPleaseSpecify = otherPleaseSpecify;
  }

  public Boolean getChangeInCommoditiesOrVarieties() {
    return changeInCommoditiesOrVarieties;
  }

  public void setChangeInCommoditiesOrVarieties(Boolean changeInCommoditiesOrVarieties) {
    this.changeInCommoditiesOrVarieties = changeInCommoditiesOrVarieties;
  }

  public Boolean getLargeScaleBreedingStockReplacement() {
    return largeScaleBreedingStockReplacement;
  }

  public void setLargeScaleBreedingStockReplacement(Boolean largeScaleBreedingStockReplacement) {
    this.largeScaleBreedingStockReplacement = largeScaleBreedingStockReplacement;
  }

}
