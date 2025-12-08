package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;

public class RevenueRiskTestResult extends ReasonabilityTestResult {
  
  private static final long serialVersionUID = 1L;

  public static final String NAME = "REVENUE RISK";
  
  private Boolean forageGrainPass;
  private Double forageGrainVarianceLimit;
  private List<RevenueRiskInventoryItem> forageGrainInventory;
  private List<RevenueRiskIncomeTestResult> forageGrainIncomes;
  
  private Boolean fruitVegTestPass;
  private List<RevenueRiskInventoryItem> fruitVegInventory;
  private List<RevenueRiskFruitVegItemTestResult> fruitVegResults;
  
  private CattleRevenueRiskSubTestResult cattle;
  private NurseryRevenueRiskSubTestResult nursery;
  private HogsRevenueRiskSubTestResult hogs;
  private PoultryBroilersRevenueRiskSubTestResult poultryBroilers;
  private PoultryEggsRevenueRiskSubTestResult poultryEggs;

  /** back-reference to the object containing this */
  @JsonBackReference
  private ReasonabilityTestResults reasonabilityTestResults;
  
  public Double getForageGrainVarianceLimit() {
    return forageGrainVarianceLimit;
  }

  public void setForageGrainVarianceLimit(Double forageGrainVarianceLimit) {
    this.forageGrainVarianceLimit = forageGrainVarianceLimit;
  }

  public List<RevenueRiskInventoryItem> getForageGrainInventory() {
    if(forageGrainInventory == null) {
      forageGrainInventory = new ArrayList<>();
    }
    return forageGrainInventory;
  }

  public void setForageGrainInventory(List<RevenueRiskInventoryItem> forageGrainInventory) {
    this.forageGrainInventory = forageGrainInventory;
  }

  public List<RevenueRiskIncomeTestResult> getForageGrainIncomes() {
    if(forageGrainIncomes == null) {
      forageGrainIncomes = new ArrayList<>();
    }
    return forageGrainIncomes;
  }

  public void setForageGrainIncomes(List<RevenueRiskIncomeTestResult> forageGrainIncome) {
    this.forageGrainIncomes = forageGrainIncome;
  }

  public Boolean getForageGrainPass() {
    return forageGrainPass;
  }

  public void setForageGrainPass(Boolean forageGrainPass) {
    this.forageGrainPass = forageGrainPass;
  }

  public List<RevenueRiskFruitVegItemTestResult> getFruitVegResults() {
    if(fruitVegResults == null) {
      fruitVegResults = new ArrayList<>();
    }
    return fruitVegResults;
  }

  public void setFruitVegResults(List<RevenueRiskFruitVegItemTestResult> fruitVegResults) {
    this.fruitVegResults = fruitVegResults;
  }

  public Boolean getFruitVegTestPass() {
    return fruitVegTestPass;
  }

  public void setFruitVegTestPass(Boolean fruitVegTestPass) {
    this.fruitVegTestPass = fruitVegTestPass;
  }

  public CattleRevenueRiskSubTestResult getCattle() {
    return cattle;
  }

  public void setCattle(CattleRevenueRiskSubTestResult cattle) {
    this.cattle = cattle;
  }

  public NurseryRevenueRiskSubTestResult getNursery() {
    return nursery;
  }

  public void setNursery(NurseryRevenueRiskSubTestResult nursery) {
    this.nursery = nursery;
  }

  public HogsRevenueRiskSubTestResult getHogs() {
    return hogs;
  }

  public void setHogs(HogsRevenueRiskSubTestResult hogs) {
    this.hogs = hogs;
  }

  public PoultryBroilersRevenueRiskSubTestResult getPoultryBroilers() {
    return poultryBroilers;
  }

  public void setPoultryBroilers(PoultryBroilersRevenueRiskSubTestResult poultryBroilers) {
    this.poultryBroilers = poultryBroilers;
  }

  public PoultryEggsRevenueRiskSubTestResult getPoultryEggs() {
    return poultryEggs;
  }

  public void setPoultryEggs(PoultryEggsRevenueRiskSubTestResult poultryEggs) {
    this.poultryEggs = poultryEggs;
  }

  public List<RevenueRiskInventoryItem> getFruitVegInventory() {
    if(fruitVegInventory == null) {
      fruitVegInventory = new ArrayList<>();
    }
    return fruitVegInventory;
  }

  public void setFruitVegInventory(List<RevenueRiskInventoryItem> fruitVegInventory) {
    this.fruitVegInventory = fruitVegInventory;
  }

  public ReasonabilityTestResults getReasonabilityTestResults() {
    return reasonabilityTestResults;
  }

  public void setReasonabilityTestResults(ReasonabilityTestResults reasonabilityTestResults) {
    this.reasonabilityTestResults = reasonabilityTestResults;
  }

  public boolean isHasGrainForage() {
    return !getForageGrainIncomes().isEmpty()
        || !getForageGrainInventory().isEmpty();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasGrainForage(boolean hasGrainForage) {
    // do nothing
  }
  
  public boolean isForageInventoryConsumed() {
    boolean result = false;
    
    if(!result) {
      for(RevenueRiskInventoryItem item : getForageGrainInventory()) {
        if(CommodityTypeCodes.FORAGE.equals(item.getCommodityTypeCode())
            && item.getQuantityConsumed() != null
            && item.getQuantityConsumed() > 0) {
          result = true;
          break;
        }
      }
    }
    
    return result;
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setForageInventoryConsumed(boolean hasForage) {
    // do nothing
  }
  
  public boolean isHasFruitVeg() {
    return !getFruitVegResults().isEmpty()
        || !getFruitVegInventory().isEmpty();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasFruitVeg(boolean hasFruitVeg) {
    // do nothing
  }
  
  public boolean isHasCattle() {
    return getCattle() != null
        && getCattle().getHasCattle() != null
        && getCattle().getHasCattle();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasCattle(boolean hasCattle) {
    // do nothing
  }
  
  public boolean isHasNursery() {
    return getNursery() != null
        && getNursery().getHasNursery() != null
        && getNursery().getHasNursery();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasNursery(boolean hasCattle) {
    // do nothing
  }
  
  public boolean isHasHogs() {
    return getHogs() != null
        && getHogs().getHasHogs() != null
        && getHogs().getHasHogs();
  }
  
  public boolean isHasPoultryBroilers() {
    return getPoultryBroilers() != null
        && getPoultryBroilers().getHasPoultryBroilers() != null
        && getPoultryBroilers().getHasPoultryBroilers();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasPoultryBroilers(boolean hasPoultryBroilers) {
    // do nothing
  }
  
  public boolean isHasPoultryEggs() {
    return getPoultryEggs() != null
        && getPoultryEggs().getHasPoultryEggs() != null
        && getPoultryEggs().getHasPoultryEggs();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasPoultryEggs(boolean hasPoultryEggs) {
    // do nothing
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasHogs(boolean hasHogs) {
    // do nothing
  }

  @Override
  public String getName() {
    return RevenueRiskTestResult.NAME;
  }
  
  public void copy(RevenueRiskTestResult o) {
    super.copy(o);
    
    forageGrainPass = o.forageGrainPass;
    forageGrainVarianceLimit = o.forageGrainVarianceLimit;
    getForageGrainInventory().clear();
    getForageGrainInventory().addAll(o.getForageGrainInventory());
    getForageGrainIncomes().clear();
    getForageGrainIncomes().addAll(o.getForageGrainIncomes());
    
    fruitVegTestPass = o.fruitVegTestPass;
    getFruitVegInventory().clear();
    getFruitVegInventory().addAll(o.getFruitVegInventory());
    getFruitVegResults().clear();
    getFruitVegResults().addAll(o.getFruitVegResults());
    
    if(cattle == null) {
      cattle = new CattleRevenueRiskSubTestResult();
    }
    cattle.copy(o.cattle);
    
    if(nursery == null) {
      nursery = new NurseryRevenueRiskSubTestResult();
    }
    nursery.copy(o.nursery);
    
    if(hogs == null) {
      hogs = new HogsRevenueRiskSubTestResult();
    }
    hogs.copy(o.hogs);
    
    if(poultryBroilers == null) {
      poultryBroilers = new PoultryBroilersRevenueRiskSubTestResult();
    }
    poultryBroilers.copy(o.poultryBroilers);
    
    if(poultryEggs == null) {
      poultryEggs = new PoultryEggsRevenueRiskSubTestResult();
    }
    poultryEggs.copy(o.poultryEggs);
  }

}
