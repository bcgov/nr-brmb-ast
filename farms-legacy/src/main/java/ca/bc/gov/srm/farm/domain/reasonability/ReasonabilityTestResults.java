/**
 *
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;

/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReasonabilityTestResults implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private Integer reasonabilityTestResultId;
  private Boolean isFresh;
  private Date generatedDate;
  
  private List<ForageConsumer> forageConsumers;
  private Double forageConsumerCapacity;

  private BenefitRiskAssessmentTestResult benefitRisk;
  private MarginTestResult marginTest;
  private StructuralChangeTestResult structuralChangeTest;
  private ExpenseTestIACResult expenseTestIAC;
  private ProductionTestResult productionTest;
  private ExpenseTestRefYearCompGCResult expenseTestRefYearCompGC;
  private RevenueRiskTestResult revenueRiskTest;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;

  public List<ReasonabilityTestResult> getTestResults() {
    List<ReasonabilityTestResult> results = new ArrayList<>();
    
    results.add(benefitRisk);
    results.add(marginTest);
    results.add(structuralChangeTest);
    results.add(expenseTestIAC);
    results.add(productionTest);
    results.add(expenseTestRefYearCompGC);
    results.add(revenueRiskTest);
    
    return results;
  }

  public Integer getReasonabilityTestResultId() {
    return reasonabilityTestResultId;
  }

  public void setReasonabilityTestResultId(Integer reasonabilityTestResultId) {
    this.reasonabilityTestResultId = reasonabilityTestResultId;
  }

  public Boolean getIsFresh() {
    return isFresh;
  }

  public void setIsFresh(Boolean isFresh) {
    this.isFresh = isFresh;
  }

  public Date getGeneratedDate() {
    return generatedDate;
  }

  public void setGeneratedDate(Date generatedDate) {
    this.generatedDate = generatedDate;
  }

  public BenefitRiskAssessmentTestResult getBenefitRisk() {
    return benefitRisk;
  }

  public void setBenefitRisk(BenefitRiskAssessmentTestResult benefitRisk) {
    this.benefitRisk = benefitRisk;
    this.benefitRisk.setReasonabilityTestResults(this);
  }

  public MarginTestResult getMarginTest() {
    return marginTest;
  }

  public void setMarginTest(MarginTestResult marginTest) {
    this.marginTest = marginTest;
  }

  public StructuralChangeTestResult getStructuralChangeTest() {
    return structuralChangeTest;
  }

  public void setStructuralChangeTest(StructuralChangeTestResult structuralChangeTest) {
    this.structuralChangeTest = structuralChangeTest;
  }

  public ExpenseTestIACResult getExpenseTestIAC() {
    return expenseTestIAC;
  }

  public void setExpenseTestIAC(ExpenseTestIACResult expenseTestIAC) {
    this.expenseTestIAC = expenseTestIAC;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }
  
  public ProductionTestResult getProductionTest() {
    return productionTest;
  }

  public void setProductionTest(ProductionTestResult productionTest) {
    this.productionTest = productionTest;
  }  

  public ExpenseTestRefYearCompGCResult getExpenseTestRefYearCompGC() {
    return expenseTestRefYearCompGC;
  }

  public void setExpenseTestRefYearCompGC(ExpenseTestRefYearCompGCResult expenseTestRefYearCompGC) {
    this.expenseTestRefYearCompGC = expenseTestRefYearCompGC;
  }

  public RevenueRiskTestResult getRevenueRiskTest() {
    return revenueRiskTest;
  }

  public void setRevenueRiskTest(RevenueRiskTestResult revenueRiskTest) {
    this.revenueRiskTest = revenueRiskTest;
    revenueRiskTest.setReasonabilityTestResults(this);
  }

  public List<ForageConsumer> getForageConsumers() {
    if(forageConsumers == null) {
      forageConsumers = new ArrayList<>();
    }
    return forageConsumers;
  }

  public void setForageConsumers(List<ForageConsumer> forageConsumers) {
    this.forageConsumers = forageConsumers;
  }
  
  public Double getForageConsumerCapacity() {
    return forageConsumerCapacity;
  }

  public void setForageConsumerCapacity(Double forageConsumerCapacity) {
    this.forageConsumerCapacity = forageConsumerCapacity;
  }

  public boolean isInCombinedFarm() {
    return scenario.isInCombinedFarm();
  }

  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setInCombinedFarm(boolean inCombinedFarm) {
    // do nothing
  }
  
  public boolean isForageProductiveUnitsConsumed() {
    return benefitRisk.isForageProductiveUnitsConsumed();
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setForageProductiveUnitsConsumed(boolean forageProductiveUnitsConsumed) {
    // do nothing
  }

  @Override
  public String toString() {
    return "ReasonabilityTestResults ["
        + "\t reasonabilityTestResultId=" + reasonabilityTestResultId + "\n"
        + "\t isFresh=" + isFresh + "\n"
        + "\t generatedDate=" + generatedDate + "\n"
        + "\t forageConsumerCapacity=" + forageConsumerCapacity + "\n"
        + "\t benefitRisk=" + benefitRisk + "\n"
        + "\t marginTest=" + marginTest + "\n"
        + "\t structuralChangeTest=" + structuralChangeTest + "\n"
        + "\t expenseTestIACResult=" + expenseTestIAC + "\n"
        + "\t expenseTestRefYearCompGC=" + expenseTestRefYearCompGC + "\n"
        + "\t productionTest=" + productionTest + "\n"
        + "\t revenueRiskTest=" + revenueRiskTest + "]";
  }

  public void copy(ReasonabilityTestResults o) {
    isFresh = o.isFresh;
    generatedDate = o.generatedDate;
    
    forageConsumerCapacity = o.forageConsumerCapacity;
    getForageConsumers().clear();
    getForageConsumers().addAll(o.getForageConsumers());
    
    if(benefitRisk == null) {
      setBenefitRisk(new BenefitRiskAssessmentTestResult());
    }
    benefitRisk.copy(o.benefitRisk);
    
    if(marginTest == null) {
      setMarginTest(new MarginTestResult());
    }
    if(o.marginTest != null) {
      marginTest.copy(o.marginTest);
    }
    
    if(structuralChangeTest == null) {
      setStructuralChangeTest(new StructuralChangeTestResult());
    }
    if(o.structuralChangeTest != null) {
      structuralChangeTest.copy(o.structuralChangeTest);
    }
    
    if(expenseTestIAC == null) {
      setExpenseTestIAC(new ExpenseTestIACResult());
    }
    if(o.expenseTestIAC != null) {
      expenseTestIAC.copy(o.expenseTestIAC);
    }
    
    if (productionTest == null) {
      setProductionTest(new ProductionTestResult());
    }
    if(o.productionTest != null) {
      productionTest.copy(o.productionTest);
    }
    
    if(expenseTestRefYearCompGC == null) {
      setExpenseTestRefYearCompGC(new ExpenseTestRefYearCompGCResult());
    }
    if(o.expenseTestRefYearCompGC != null) {
      expenseTestRefYearCompGC.copy(o.expenseTestRefYearCompGC);
    }
    
    if (revenueRiskTest == null) {
      setRevenueRiskTest(new RevenueRiskTestResult());
    }
    if(o.structuralChangeTest != null) {
      revenueRiskTest.copy(o.getRevenueRiskTest());
    }
  }
}
