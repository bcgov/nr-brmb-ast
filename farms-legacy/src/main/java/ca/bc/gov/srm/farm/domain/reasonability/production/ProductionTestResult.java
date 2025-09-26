package ca.bc.gov.srm.farm.domain.reasonability.production;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResult;

public class ProductionTestResult extends ReasonabilityTestResult {

  private static final long serialVersionUID = 314125279128859574L;
  
  public static final String NAME = "PRODUCTION";
  
  private Double forageProducedVarianceLimit;
  private Double fruitVegProducedVarianceLimit;
  private Double grainProducedVarianceLimit;
  
  private List<ProductionInventoryItemTestResult> forageTestResults;
  private List<ProductionInventoryItemTestResult> forageSeedTestResults;
  private List<ProductionInventoryItemTestResult> grainItemTestResults;
  
  private List<ProductionInventoryItemTestResult> fruitVegInventoryItems;
  private List<FruitVegProductionResult> fruitVegTestResults;

  private Boolean passForageAndForageSeedTest;
  private Boolean passFruitVegTest;
  private Boolean passGrainTest;
  
  public Boolean isPassForageAndForageSeedTest() {
    return passForageAndForageSeedTest;
  }

  public void setPassForageAndForageSeedTest(Boolean passForageAndForageSeedTest) {
    this.passForageAndForageSeedTest = passForageAndForageSeedTest;
  }

  public List<ProductionInventoryItemTestResult> getForageTestResults() {
    if(forageTestResults == null) {
      forageTestResults = new ArrayList<>();
    }
    return forageTestResults;
  }

  public void setForageTestResults(List<ProductionInventoryItemTestResult> forageTestRecords) {
    this.forageTestResults = forageTestRecords;
  }

  public List<ProductionInventoryItemTestResult> getForageSeedTestResults() {
    if(forageSeedTestResults == null) {
      forageSeedTestResults = new ArrayList<>();
    }
    return forageSeedTestResults;
  }

  public void setForageSeedTestResults(List<ProductionInventoryItemTestResult> forageSeedTestRecords) {
    this.forageSeedTestResults = forageSeedTestRecords;
  }

  public Boolean getPassForageAndForageSeedTest() {
    return passForageAndForageSeedTest;
  }

  public List<FruitVegProductionResult> getFruitVegTestResults() {
    if(fruitVegTestResults == null) {
      fruitVegTestResults = new ArrayList<>();
    }
    return fruitVegTestResults;
  }

  public void setFruitVegTestResults(List<FruitVegProductionResult> fruitVegTestRecords) {
    this.fruitVegTestResults = fruitVegTestRecords;
  }  

  public Boolean getPassFruitVegTest() {
    return passFruitVegTest;
  }

  public void setPassFruitVegTest(Boolean passFruitVegTest) {
    this.passFruitVegTest = passFruitVegTest;
  }

  public Double getForageProducedVarianceLimit() {
    return forageProducedVarianceLimit;
  }

  public void setForageProducedVarianceLimit(Double forageProducedVarianceLimit) {
    this.forageProducedVarianceLimit = forageProducedVarianceLimit;
  }

  public Double getFruitVegProducedVarianceLimit() {
    return fruitVegProducedVarianceLimit;
  }

  public void setFruitVegProducedVarianceLimit(Double fruitVegProducedVarianceLimit) {
    this.fruitVegProducedVarianceLimit = fruitVegProducedVarianceLimit;
  }

  public Double getGrainProducedVarianceLimit() {
    return grainProducedVarianceLimit;
  }

  public void setGrainProducedVarianceLimit(Double grainProducedVarianceLimit) {
    this.grainProducedVarianceLimit = grainProducedVarianceLimit;
  }
  
  public List<ProductionInventoryItemTestResult> getGrainItemTestResults() {
    if(grainItemTestResults == null) {
      grainItemTestResults = new ArrayList<>();
    }
    return grainItemTestResults;
  }

  public void setGrainItemTestResults(List<ProductionInventoryItemTestResult> grainItemTestResults) {
    this.grainItemTestResults = grainItemTestResults;
  }

  public List<ProductionInventoryItemTestResult> getFruitVegInventoryItems() {
    if(fruitVegInventoryItems == null) {
      fruitVegInventoryItems = new ArrayList<>();
    }
    return fruitVegInventoryItems;
  }

  public void setFruitVegInventoryItems(List<ProductionInventoryItemTestResult> fruitVegInventoryItems) {
    this.fruitVegInventoryItems = fruitVegInventoryItems;
  }

  public Boolean getPassGrainTest() {
    return passGrainTest;
  }

  public void setPassGrainTest(Boolean passGrainTest) {
    this.passGrainTest = passGrainTest;
  }

  public boolean isHasForage() {
    return !getForageTestResults().isEmpty();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasForage(boolean hasForage) {
    // do nothing
  }
  
  public boolean isHasForageSeed() {
    return !getForageSeedTestResults().isEmpty();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasForageSeed(boolean hasForageSeed) {
    // do nothing
  }
  
  public boolean isHasGrain() {
    return !getGrainItemTestResults().isEmpty();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasGrain(boolean hasGrain) {
    // do nothing
  }
  
  public boolean isHasFruitVeg() {
    return !getFruitVegTestResults().isEmpty()
        || !getFruitVegInventoryItems().isEmpty();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasFruitVeg(boolean hasFruitVeg) {
    // do nothing
  }

  @Override
  public String getName() {
    return ProductionTestResult.NAME;
  }
  
  public void copy(ProductionTestResult o) {
    super.copy(o);
    
    getForageTestResults().clear();
    getForageTestResults().addAll(getForageTestResults());
    
    getForageSeedTestResults().clear();
    getForageSeedTestResults().addAll(getForageSeedTestResults());
    
    getFruitVegTestResults().clear();
    getFruitVegTestResults().addAll(getFruitVegTestResults());
    
    getGrainItemTestResults().clear();
    getGrainItemTestResults().addAll(getGrainItemTestResults());
    
    forageProducedVarianceLimit = o.getForageProducedVarianceLimit();
    fruitVegProducedVarianceLimit = o.getFruitVegProducedVarianceLimit();
    grainProducedVarianceLimit = o.getGrainProducedVarianceLimit();
    passForageAndForageSeedTest = o.getPassForageAndForageSeedTest();
    passFruitVegTest = o.getPassFruitVegTest();
    passGrainTest = o.getPassGrainTest();
  }
}
