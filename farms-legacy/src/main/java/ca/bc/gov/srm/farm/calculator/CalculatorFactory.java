/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import ca.bc.gov.srm.farm.calculator.basic.BasicAccrualCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicBenchmarkMarginCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicBenefitCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicBenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.basic.BasicBenefitValidator;
import ca.bc.gov.srm.farm.calculator.basic.BasicFarmSizeRatioCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicIncomeExpenseCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicNegativeMarginCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicProductionMarginCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicReferenceYearCalculator;
import ca.bc.gov.srm.farm.calculator.basic.BasicStructuralChangeCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedAccrualCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedAppliedBenefitPercentCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedBenchmarkMarginCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedBenefitCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedBenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.combined.CombinedBenefitValidator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedFarmSizeRatioCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedIncomeExpenseCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedNegativeMarginCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedProductionMarginCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedReferenceYearCalculator;
import ca.bc.gov.srm.farm.calculator.combined.CombinedStructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public final class CalculatorFactory {

  private CalculatorFactory() {
  }

  public static AccrualCalculator getAccrualCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedAccrualCalculator(scenario);
    }
    return new BasicAccrualCalculator(scenario);
  }
  
  public static BenefitCalculator getBenefitCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedBenefitCalculator(scenario);
    }
    return new BasicBenefitCalculator(scenario);
  }
  
  public static CombinedBenefitCalculator getCombinedBenefitCalculator(Scenario scenario) {
    return new CombinedBenefitCalculator(scenario);
  }
  
  public static BenefitNullFixer getBenefitNullFixer(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedBenefitNullFixer();
    }
    return new BasicBenefitNullFixer();
  }
  
  public static BenefitValidator getBenefitValidator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedBenefitValidator();
    }
    return new BasicBenefitValidator();
  }
  
  public static FarmSizeRatioCalculator getFarmSizeRatioCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedFarmSizeRatioCalculator(scenario);
    }
    return new BasicFarmSizeRatioCalculator(scenario);
  }
  
  public static IncomeExpenseCalculator getIncomeExpenseCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedIncomeExpenseCalculator(scenario);
    }
    return new BasicIncomeExpenseCalculator(scenario);
  }
  
  public static ProductionMarginCalculator getProductionMarginCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedProductionMarginCalculator(scenario);
    }
    return new BasicProductionMarginCalculator(scenario);
  }
  
  public static ReferenceYearCalculator getReferenceYearCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedReferenceYearCalculator();
    }
    return new BasicReferenceYearCalculator();
  }
  
  public static StructuralChangeCalculator getStructuralChangeCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedStructuralChangeCalculator(scenario);
    }
    return new BasicStructuralChangeCalculator(scenario);
  }
  
  public static BenchmarkMarginCalculator getBenchmarkMarginCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedBenchmarkMarginCalculator();
    }
    return new BasicBenchmarkMarginCalculator();
  }
  
  public static NegativeMarginCalculator getNegativeMarginCalculator(Scenario scenario) {
    if(scenario.isInCombinedFarm()) {
      return new CombinedNegativeMarginCalculator(scenario);
    }
    return new BasicNegativeMarginCalculator(scenario);
  }
  
  public static CombinedBenchmarkMarginCalculator getCombinedBenchmarkMarginCalculator() {
    return new CombinedBenchmarkMarginCalculator();
  }

  public static CombinedAppliedBenefitPercentCalculator getCombinedAppliedBenefitPercentCalculator(Scenario scenario) {
    return new CombinedAppliedBenefitPercentCalculator(scenario);
  }
  
  public static SupplyManagedCommoditiesRatioCalculator getSupplyManagedCommoditiesRatioCalculator(Scenario scenario) {
    return new SupplyManagedCommoditiesRatioCalculator(scenario);
  }
  
  public static ProductiveValueCalculator getProductiveValueCalculator(Scenario scenario) {
    return new ProductiveValueCalculator(scenario);
  }
  
  public static InventoryCalculator getInventoryCalculator() {
    return new InventoryCalculator();
  }
  
  public static FmvCalculator getFmvCalculator() {
    return new FmvCalculator();
  }
}
