package ca.bc.gov.srm.farm.chefs.resource.supplemental;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.chefs.resource.common.CustomFeedGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.CattleGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.GrainGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.InputGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.LivestockGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.NurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.OtherPucGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.PayableGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.ReceivablesGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.CropGrid;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;

public class SupplementalBaseDataResource extends ChefsSubmissionDataResource {

  private List<ReceivablesGrid> receivablesGrid;
  private List<PayableGrid> expensesGrid;
  private List<InputGrid> inputGrid;

  private List<String> cropsFarmed;
  private List<String> livestockFarmed;
  private List<String> productionInsurancePolicy;

  private List<CropGrid> berryGrid = new ArrayList<>();
  private List<CropGrid> vegetableGrid = new ArrayList<>();
  private List<CropGrid> treeFruitGrid = new ArrayList<>();
  private List<GrainGrid> grainGrid = new ArrayList<>();
  private List<NurseryGrid> nurseryGrid = new ArrayList<>();
  private List<CropGrid> neHorticultureGrid = new ArrayList<>();

  private List<CattleGrid> cattleGrid = new ArrayList<>();
  private List<LivestockGrid> poultryGrid = new ArrayList<>();
  private List<LivestockGrid> swineGrid = new ArrayList<>();
  private List<LivestockGrid> otherGrid = new ArrayList<>();
  private List<OtherPucGrid> opdGrid = new ArrayList<>();
  private List<CustomFeedGrid> customFedGrid = new ArrayList<>();

  private Double numberOfCowsThatCalved;
  private Double numberFeedersOver9;
  private Double numberFeedersUnder9;
  private Double turkeyBroilersLC144;
  private Double chickenBroilersLC143;
  private Double eggsForHatchingLC108;
  private Double eggsForConsumptionLC109;
  private Double numberOfHogsFedUpTo50LbsLC124;
  private Double numberOfSowsThatFarrowedLC123;
  private Double numberOfHogsFedOver50LbsFeedersLC125;

  public List<String> getProductionInsurancePolicy() {
    return productionInsurancePolicy;
  }

  public void setProductionInsurancePolicy(List<String> productionInsurancePolicy) {
    this.productionInsurancePolicy = productionInsurancePolicy;
  }

  public List<ReceivablesGrid> getReceivablesGrid() {
    return receivablesGrid;
  }

  public void setReceivablesGrid(List<ReceivablesGrid> receivablesGrid) {
    this.receivablesGrid = receivablesGrid;
  }

  public List<PayableGrid> getExpensesGrid() {
    return expensesGrid;
  }

  public void setExpensesGrid(List<PayableGrid> expensesGrid) {
    this.expensesGrid = expensesGrid;
  }

  public List<InputGrid> getInputGrid() {
    return inputGrid;
  }

  public void setInputGrid(List<InputGrid> inputGrid) {
    this.inputGrid = inputGrid;
  }

  public List<String> getCropsFarmed() {
    return cropsFarmed;
  }

  public void setCropsFarmed(List<String> cropsFarmed) {
    this.cropsFarmed = cropsFarmed;
  }

  public List<String> getLivestockFarmed() {
    return livestockFarmed;
  }

  public void setLivestockFarmed(List<String> livestockFarmed) {
    this.livestockFarmed = livestockFarmed;
  }

  public List<CropGrid> getBerryGrid() {
    return berryGrid;
  }

  public void setBerryGrid(List<CropGrid> berryGrid) {
    this.berryGrid = berryGrid;
  }

  public List<CropGrid> getVegetableGrid() {
    return vegetableGrid;
  }

  public void setVegetableGrid(List<CropGrid> vegetableGrid) {
    this.vegetableGrid = vegetableGrid;
  }

  public List<CropGrid> getTreeFruitGrid() {
    return treeFruitGrid;
  }

  public void setTreeFruitGrid(List<CropGrid> treeFruitGrid) {
    this.treeFruitGrid = treeFruitGrid;
  }

  public List<GrainGrid> getGrainGrid() {
    return grainGrid;
  }

  public void setGrainGrid(List<GrainGrid> grainGrid) {
    this.grainGrid = grainGrid;
  }

  public List<NurseryGrid> getNurseryGrid() {
    return nurseryGrid;
  }

  public void setNurseryGrid(List<NurseryGrid> nurseryGrid) {
    this.nurseryGrid = nurseryGrid;
  }

  public List<CropGrid> getNeHorticultureGrid() {
    return neHorticultureGrid;
  }

  public void setNeHorticultureGrid(List<CropGrid> neHorticultureGrid) {
    this.neHorticultureGrid = neHorticultureGrid;
  }

  public List<CattleGrid> getCattleGrid() {
    return cattleGrid;
  }

  public void setCattleGrid(List<CattleGrid> cattleGrid) {
    this.cattleGrid = cattleGrid;
  }

  public List<LivestockGrid> getPoultryGrid() {
    return poultryGrid;
  }

  public void setPoultryGrid(List<LivestockGrid> poultryGrid) {
    this.poultryGrid = poultryGrid;
  }

  public List<LivestockGrid> getSwineGrid() {
    return swineGrid;
  }

  public void setSwineGrid(List<LivestockGrid> swineGrid) {
    this.swineGrid = swineGrid;
  }

  public List<LivestockGrid> getOtherGrid() {
    return otherGrid;
  }

  public void setOtherGrid(List<LivestockGrid> otherGrid) {
    this.otherGrid = otherGrid;
  }

  public Double getTurkeyBroilersLC144() {
    return turkeyBroilersLC144;
  }

  public void setTurkeyBroilersLC144(Double turkeyBroilersLC144) {
    this.turkeyBroilersLC144 = turkeyBroilersLC144;
  }

  public Double getChickenBroilersLC143() {
    return chickenBroilersLC143;
  }

  public void setChickenBroilersLC143(Double chickenBroilersLC143) {
    this.chickenBroilersLC143 = chickenBroilersLC143;
  }

  public Double getEggsForHatchingLC108() {
    return eggsForHatchingLC108;
  }

  public void setEggsForHatchingLC108(Double eggsForHatchingLC108) {
    this.eggsForHatchingLC108 = eggsForHatchingLC108;
  }

  public Double getEggsForConsumptionLC109() {
    return eggsForConsumptionLC109;
  }

  public void setEggsForConsumptionLC109(Double eggsForConsumptionLC109) {
    this.eggsForConsumptionLC109 = eggsForConsumptionLC109;
  }

  public Double getNumberOfHogsFedUpTo50LbsLC124() {
    return numberOfHogsFedUpTo50LbsLC124;
  }

  public void setNumberOfHogsFedUpTo50LbsLC124(Double numberOfHogsFedUpTo50LbsLC124) {
    this.numberOfHogsFedUpTo50LbsLC124 = numberOfHogsFedUpTo50LbsLC124;
  }

  public Double getNumberOfSowsThatFarrowedLC123() {
    return numberOfSowsThatFarrowedLC123;
  }

  public void setNumberOfSowsThatFarrowedLC123(Double numberOfSowsThatFarrowedLC123) {
    this.numberOfSowsThatFarrowedLC123 = numberOfSowsThatFarrowedLC123;
  }

  public Double getNumberOfHogsFedOver50LbsFeedersLC125() {
    return numberOfHogsFedOver50LbsFeedersLC125;
  }

  public void setNumberOfHogsFedOver50LbsFeedersLC125(Double numberOfHogsFedOver50LbsFeedersLC125) {
    this.numberOfHogsFedOver50LbsFeedersLC125 = numberOfHogsFedOver50LbsFeedersLC125;
  }

  public Double getNumberOfCowsThatCalved() {
    return numberOfCowsThatCalved;
  }

  public void setNumberOfCowsThatCalved(Double numberOfCowsThatCalved) {
    this.numberOfCowsThatCalved = numberOfCowsThatCalved;
  }

  public Double getNumberFeedersOver9() {
    return numberFeedersOver9;
  }

  public void setNumberFeedersOver9(Double numberFeedersOver9) {
    this.numberFeedersOver9 = numberFeedersOver9;
  }

  public Double getNumberFeedersUnder9() {
    return numberFeedersUnder9;
  }

  public void setNumberFeedersUnder9(Double numberFeedersUnder9) {
    this.numberFeedersUnder9 = numberFeedersUnder9;
  }

  public List<OtherPucGrid> getOpdGrid() {
    return opdGrid;
  }

  public void setOpdGrid(List<OtherPucGrid> opdGrid) {
    this.opdGrid = opdGrid;
  }

  public List<CustomFeedGrid> getCustomFedGrid() {
    return customFedGrid;
  }

  public void setCustomFedGrid(List<CustomFeedGrid> customFedGrid) {
    this.customFedGrid = customFedGrid;
  }

}
