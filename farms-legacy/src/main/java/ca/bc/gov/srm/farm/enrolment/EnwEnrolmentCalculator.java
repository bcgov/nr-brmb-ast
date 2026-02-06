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
package ca.bc.gov.srm.farm.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnwProductiveUnit;
import ca.bc.gov.srm.farm.domain.enrolment.EnwProductiveValue;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class EnwEnrolmentCalculator {

  protected EnwEnrolmentCalculator() {
  }
  
  public void initNonEditableFields(Scenario scenario) {
    
    EnwEnrolment enw = scenario.getEnwEnrolment();
    if(enw == null) {
      enw = new EnwEnrolment();
      scenario.setEnwEnrolment(enw);
    }
    
    int enrolmentYear = getEnrolmentYear(scenario);
    
    enw.setEnrolmentFeeMinimum(CalculatorConfig.MIN_ENROLMENT_FEE);
    enw.setEnrolmentFeeCalculationFactor(CalculatorConfig.getEnrolmentFeeFactor(enrolmentYear));
    enw.setInCombinedFarm(scenario.isInCombinedFarm());
    
    calculateBenefitMarginYears(scenario, enw);
    calculateProxyMarginYears(scenario, enw);
    calculateHasBpus(scenario, enw);
    calculateEnwProductiveUnits(scenario, enw);
    calculateTotalProductiveValues(enw);
  }
  
  public void calculateEnwEnrolment(Scenario scenario, boolean benefitCalculated) {
    EnwEnrolment enw = scenario.getEnwEnrolment();
    clearCalculatedFields(enw);
    enw.setBenefitCalculated(benefitCalculated);
    
    if(benefitCalculated && scenario.getFarmingYear() != null
      && scenario.getFarmingYear().getBenefit() != null) {
      enw.setCombinedFarmPercent(scenario.getFarmingYear().getBenefit().getCombinedFarmPercent());
    }
    
    calculateEnrolmentFromBenefit(scenario, benefitCalculated, enw);
    
    if(enw.getEnrolmentCalculationTypeCode() == null) {
      if(benefitCalculated) {
        enw.setEnrolmentCalculationTypeCode(EnwEnrolment.CALCULATION_TYPE_BENEFIT_MARGINS);
      } else if(enw.getCanCalculateProxyMargins()) {
        enw.setEnrolmentCalculationTypeCode(EnwEnrolment.CALCULATION_TYPE_PROXY_MARGINS);
      } else {
        enw.setEnrolmentCalculationTypeCode(EnwEnrolment.CALCULATION_TYPE_MANUAL_MARGINS);
      }
    }
    
    calculateProxyMarginsEnrolment(enw);
    calculateManualEnrolment(enw);
    calculateSelectedEnrolment(enw);
    
    setMarginsUsed(enw);
  }

  private void calculateEnrolmentFromBenefit(Scenario scenario, boolean benefitCalculated, EnwEnrolment enw) {
    int enrolmentYear = getEnrolmentYear(scenario);
    
    if(benefitCalculated) {
      StandardEnrolmentCalculator standardEnrolmentCalculator = EnrolmentCalculatorFactory.getStandardEnrolmentCalculator();
      EnrolmentStaging stagingEnrolment = standardEnrolmentCalculator.calculateStagingEnrolment(enrolmentYear, scenario, false);
      copyStagingEnrolmentToEnw(stagingEnrolment, enw);
    } else {
      enw.setEnrolmentYear(enrolmentYear);
    }
  }


  private void calculateBenefitMarginYears(Scenario scenario, EnwEnrolment enw) {
    final int firstYear = scenario.getYear() - 4;
    final int lastYear = scenario.getYear();
    List<Integer> years = new ArrayList<>(5);
    for(int year = firstYear; year <= lastYear; year++) {
      years.add(year);
    }
    enw.setBenefitMarginYears(years);
  }
  
  
  private void calculateProxyMarginYears(Scenario scenario, EnwEnrolment enw) {
    List<Integer> proxyMarginYears = new ArrayList<>();
    enw.setProxyMarginYears(proxyMarginYears);
    
    Integer programYear = scenario.getYear();
    final int minYear = getProxyMarginMinYear(programYear);
    for(int curYear = minYear; curYear <= programYear; curYear++) {
      proxyMarginYears.add(curYear);
    }
  }

  private int getProxyMarginMinYear(Integer programYear) {
    final int minYear = programYear - 2;
    return minYear;
  }

  
  private void calculateHasBpus(Scenario scenario, EnwEnrolment enw) {
    Map<String, ProductiveUnitCapacity> productiveUnits = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario, null, true);
    boolean valid = true;
    
    pucLoop: for (ProductiveUnitCapacity puc : productiveUnits.values()) {
      for(int curYear : enw.getProxyMarginYears()) {
        Double bpuMargin = findBpuMargin(scenario, puc, curYear);
        if(bpuMargin == 0) {
          valid = false;
          break pucLoop;
        }
      }
    }
    
    enw.setHasBpus(valid);
  }
  
  
  private void calculateEnwProductiveUnits(Scenario scenario, EnwEnrolment enw) {
    
    List<EnwProductiveUnit> enwProductiveUnits = new ArrayList<>();
    enw.setEnwProductiveUnits(enwProductiveUnits);
    
    Map<String, ProductiveUnitCapacity> productiveUnits = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario, null, true);
    boolean hasProductiveUnits = !productiveUnits.isEmpty();
    enw.setHasProductiveUnits(hasProductiveUnits);

    boolean canCalculateProxyMargins = enw.getCanCalculateProxyMargins();

    if(canCalculateProxyMargins) {
      for (String code : productiveUnits.keySet()) {
        ProductiveUnitCapacity puc = productiveUnits.get(code);
        EnwProductiveUnit enwPuc = new EnwProductiveUnit();
        enwProductiveUnits.add(enwPuc);
        
        Double productiveCapacityAmount = puc.getTotalProductiveCapacityAmount();
        
        enwPuc.setCode(code);
        enwPuc.setDescription(puc.getDescription());
        enwPuc.setProductiveCapacity(productiveCapacityAmount);
        
        List<EnwProductiveValue> productiveValues = new ArrayList<>();
        enwPuc.setProductiveValues(productiveValues);
        
        for(int curYear : enw.getProxyMarginYears()) {
          
          Double bpuMargin = findBpuMargin(scenario, puc, curYear);
          
          EnwProductiveValue enwProductiveValue = new EnwProductiveValue();
          productiveValues.add(enwProductiveValue);
          
          Double productiveValue = productiveCapacityAmount * bpuMargin;
          
          enwProductiveValue.setBpuMargin(bpuMargin);
          enwProductiveValue.setProductiveValue(productiveValue);
          
        }
      }
      
      Collections.sort(enwProductiveUnits);
    }
  }

  private Double findBpuMargin(Scenario scenario, ProductiveUnitCapacity puc, int curYear) {
    Double bpuMargin = 0.0;
    
    BasePricePerUnit bpu = puc.getBasePricePerUnit();
    
    if(bpu != null) {
      int programYear = scenario.getYear();
      
      int referenceYear = curYear;
      if(curYear == programYear) {
        referenceYear--; // there is no BPU for the program year so use the previous year.
      }
      
      ReferenceScenario refScenario = scenario.getReferenceScenarioByYear(referenceYear);
      int bpuYearToUse = referenceYear;
      
      if(refScenario != null && refScenario.getFarmingYear() != null
          && refScenario.getFarmingYear().getMarginTotal() != null
          && refScenario.getFarmingYear().getMarginTotal().getBpuLeadInd() != null) {
        Boolean bpuLeadInd = refScenario.getFarmingYear().getMarginTotal().getBpuLeadInd();
        if(!bpuLeadInd) {
          bpuYearToUse--; // if it's Lead, use the same year. If not, use previous year.
        }
      }
      
      for (BasePricePerUnitYear bpuYear : bpu.getBasePricePerUnitYears()) {
        if(bpuYear.getYear() == bpuYearToUse) {
          bpuMargin = bpuYear.getMargin();
          break;
        }
      }
    }
    
    return bpuMargin;
  }
  
  
  private void calculateTotalProductiveValues(EnwEnrolment enw) {
    
    if(enw.getCanCalculateProxyMargins()) {
      
      double productiveValueYearMinus2 = 0;
      double productiveValueYearMinus3 = 0;
      double productiveValueYearMinus4 = 0;
      
      for (EnwProductiveUnit enwProductiveUnit : enw.getEnwProductiveUnits()) {
        int pucYearMinus = 4;
        for(EnwProductiveValue prodValue : enwProductiveUnit.getProductiveValues()) {
          if(pucYearMinus == 4) {
            productiveValueYearMinus4 += prodValue.getProductiveValue();
          } else if(pucYearMinus == 3) {
            productiveValueYearMinus3 += prodValue.getProductiveValue();
          } else if(pucYearMinus == 2) {
            productiveValueYearMinus2 += prodValue.getProductiveValue();
          }
          pucYearMinus--;
        }
      }
      
      enw.setProductiveValueYearMinus2(productiveValueYearMinus2);
      enw.setProductiveValueYearMinus3(productiveValueYearMinus3);
      enw.setProductiveValueYearMinus4(productiveValueYearMinus4);
      
    }
    
  }

  
  private void calculateProxyMarginsEnrolment(EnwEnrolment enw) {

    List<EnwProductiveUnit> enwProductiveUnits = enw.getEnwProductiveUnits();
    if(!enwProductiveUnits.isEmpty()) {
    
      boolean hasCombinedFarmPercentOrDontNeedIt = !enw.getInCombinedFarm() || enw.getCombinedFarmPercent() != null;
      if(hasCombinedFarmPercentOrDontNeedIt) {
        
        double proxyMarginYearMinus2 = enw.getProductiveValueYearMinus2();
        double proxyMarginYearMinus3 = enw.getProductiveValueYearMinus3();
        double proxyMarginYearMinus4 = enw.getProductiveValueYearMinus4();
        
        if(enw.getInCombinedFarm()) {
          proxyMarginYearMinus2 *= enw.getCombinedFarmPercent();
          proxyMarginYearMinus3 *= enw.getCombinedFarmPercent();
          proxyMarginYearMinus4 *= enw.getCombinedFarmPercent();
        }
        
        enw.setProxyMarginYearMinus2(proxyMarginYearMinus2);
        enw.setProxyMarginYearMinus3(proxyMarginYearMinus3);
        enw.setProxyMarginYearMinus4(proxyMarginYearMinus4);
        
        double contributionMargin =
            (proxyMarginYearMinus2 + proxyMarginYearMinus3 + proxyMarginYearMinus4) / 3;
        contributionMargin = MathUtils.roundCurrency(contributionMargin);
        enw.setProxyContributionMargin(contributionMargin);
        
        double enrolmentFee = MathUtils.roundCurrency(contributionMargin * getEnrolmentFeeFactor(enw));
        
        if(enrolmentFee < CalculatorConfig.MIN_ENROLMENT_FEE) {
          enrolmentFee = CalculatorConfig.MIN_ENROLMENT_FEE;
        }
        
        enw.setProxyEnrolmentFee(enrolmentFee);
      }
      
      enw.setProxyMarginsCalculated(true);
    } else {
      enw.setProxyMarginsCalculated(false);
    }
    
  }

  private void calculateManualEnrolment(EnwEnrolment enw) {
    
    Double contributionMargin = null;
    
    boolean manualMarginsCalculated =
        enw.getManualMarginYearMinus2() != null
        && enw.getManualMarginYearMinus3() != null
        && enw.getManualMarginYearMinus4() != null;
    enw.setManualMarginsCalculated(manualMarginsCalculated);
    
    if(manualMarginsCalculated) {
      
      contributionMargin = (enw.getManualMarginYearMinus2() + enw.getManualMarginYearMinus3() + enw.getManualMarginYearMinus4()) / 3;
      contributionMargin = MathUtils.roundCurrency(contributionMargin);
      
      double enrolmentFee = MathUtils.roundCurrency(contributionMargin * getEnrolmentFeeFactor(enw));
      
      if(enrolmentFee < CalculatorConfig.MIN_ENROLMENT_FEE) {
        enrolmentFee = CalculatorConfig.MIN_ENROLMENT_FEE;
      }
      
      enw.setManualEnrolmentFee(enrolmentFee);
    }
    enw.setManualContributionMargin(contributionMargin);
  }

  private double getEnrolmentFeeFactor(EnwEnrolment enw) {
    return CalculatorConfig.getEnrolmentFeeFactor(enw.getEnrolmentYear());
  }
  
  
  private void calculateSelectedEnrolment(EnwEnrolment enw) {
    
    if(enw.getEnrolmentCalculationTypeCode().equals(EnwEnrolment.CALCULATION_TYPE_BENEFIT_MARGINS)) {
      enw.setContributionMargin(enw.getBenefitContributionMargin());
      enw.setEnrolmentFee(enw.getBenefitEnrolmentFee());
      
    } else if (enw.getEnrolmentCalculationTypeCode().equals(EnwEnrolment.CALCULATION_TYPE_PROXY_MARGINS)) {
      enw.setContributionMargin(enw.getProxyContributionMargin());
      enw.setEnrolmentFee(enw.getProxyEnrolmentFee());
      
    } else if (enw.getEnrolmentCalculationTypeCode().equals(EnwEnrolment.CALCULATION_TYPE_MANUAL_MARGINS)) {
      enw.setContributionMargin(enw.getManualContributionMargin());
      enw.setEnrolmentFee(enw.getManualEnrolmentFee());
      
    }
  }

  
  private void setMarginsUsed(EnwEnrolment enw) {
    
    if(enw.getEnrolmentCalculationTypeCode().equals(EnwEnrolment.CALCULATION_TYPE_BENEFIT_MARGINS)) {
      
      enw.setMarginYearMinus2(enw.getBenefitMarginYearMinus2());
      enw.setMarginYearMinus3(enw.getBenefitMarginYearMinus3());
      enw.setMarginYearMinus4(enw.getBenefitMarginYearMinus4());
      enw.setMarginYearMinus5(enw.getBenefitMarginYearMinus5());
      enw.setMarginYearMinus6(enw.getBenefitMarginYearMinus6());
      
      enw.setMarginYearMinus2Used(enw.getBenefitMarginYearMinus2Used());
      enw.setMarginYearMinus3Used(enw.getBenefitMarginYearMinus3Used());
      enw.setMarginYearMinus4Used(enw.getBenefitMarginYearMinus4Used());
      enw.setMarginYearMinus5Used(enw.getBenefitMarginYearMinus5Used());
      enw.setMarginYearMinus6Used(enw.getBenefitMarginYearMinus6Used());
      
    } else if(enw.getEnrolmentCalculationTypeCode().equals(EnwEnrolment.CALCULATION_TYPE_PROXY_MARGINS)) {
      
      enw.setMarginYearMinus2(enw.getProxyMarginYearMinus2());
      enw.setMarginYearMinus3(enw.getProxyMarginYearMinus3());
      enw.setMarginYearMinus4(enw.getProxyMarginYearMinus4());
      enw.setMarginYearMinus5(null);
      enw.setMarginYearMinus6(null);
      
      enw.setMarginYearMinus2Used(true);
      enw.setMarginYearMinus3Used(true);
      enw.setMarginYearMinus4Used(true);
      enw.setMarginYearMinus5Used(false);
      enw.setMarginYearMinus6Used(false);
      
    } else if(enw.getEnrolmentCalculationTypeCode().equals(EnwEnrolment.CALCULATION_TYPE_MANUAL_MARGINS)) {
      
      enw.setMarginYearMinus2(enw.getManualMarginYearMinus2());
      enw.setMarginYearMinus3(enw.getManualMarginYearMinus3());
      enw.setMarginYearMinus4(enw.getManualMarginYearMinus4());
      enw.setMarginYearMinus5(null);
      enw.setMarginYearMinus6(null);
      
      enw.setMarginYearMinus2Used(true);
      enw.setMarginYearMinus3Used(true);
      enw.setMarginYearMinus4Used(true);
      enw.setMarginYearMinus5Used(false);
      enw.setMarginYearMinus6Used(false);
    }
  }

  
  private int getEnrolmentYear(Scenario scenario) {
    int enrolmentYear = scenario.getYear();
    
    String chefsFormTypeCode = scenario.getChefsFormTypeCode();
    if( ! ChefsFormTypeCodes.NPP.equals(chefsFormTypeCode) ) {
      final int numYearsToAddForStandardEnw = 2;
      enrolmentYear += numYearsToAddForStandardEnw;
    }
    
    return enrolmentYear;
  }


  private void copyStagingEnrolmentToEnw(EnrolmentStaging stagingEnrolment, EnwEnrolment enw) {
    
    enw.setEnrolmentYear(stagingEnrolment.getEnrolmentYear());
    enw.setCombinedFarmPercent(stagingEnrolment.getCombinedFarmPercent());
    
    enw.setBenefitMarginYearMinus2Used(stagingEnrolment.getIsMarginYearMinus2Used());
    enw.setBenefitMarginYearMinus3Used(stagingEnrolment.getIsMarginYearMinus3Used());
    enw.setBenefitMarginYearMinus4Used(stagingEnrolment.getIsMarginYearMinus4Used());
    enw.setBenefitMarginYearMinus5Used(stagingEnrolment.getIsMarginYearMinus5Used());
    enw.setBenefitMarginYearMinus6Used(stagingEnrolment.getIsMarginYearMinus6Used());
    
    enw.setBenefitMarginYearMinus2(stagingEnrolment.getMarginYearMinus2());
    enw.setBenefitMarginYearMinus3(stagingEnrolment.getMarginYearMinus3());
    enw.setBenefitMarginYearMinus4(stagingEnrolment.getMarginYearMinus4());
    enw.setBenefitMarginYearMinus5(stagingEnrolment.getMarginYearMinus5());
    enw.setBenefitMarginYearMinus6(stagingEnrolment.getMarginYearMinus6());
    
    enw.setBenefitContributionMargin(stagingEnrolment.getContributionMarginAverage());
    enw.setBenefitEnrolmentFee(stagingEnrolment.getEnrolmentFee());
  }

  private void clearCalculatedFields(EnwEnrolment enw) {
    enw.setBenefitContributionMargin(null);
    enw.setBenefitEnrolmentFee(null);
    enw.setBenefitMarginYearMinus2(null);
    enw.setBenefitMarginYearMinus2Used(false);
    enw.setBenefitMarginYearMinus3(null);
    enw.setBenefitMarginYearMinus3Used(false);
    enw.setBenefitMarginYearMinus4(null);
    enw.setBenefitMarginYearMinus4Used(false);
    enw.setBenefitMarginYearMinus5(null);
    enw.setBenefitMarginYearMinus5Used(false);
    enw.setBenefitMarginYearMinus6(null);
    enw.setBenefitMarginYearMinus6Used(false);
    enw.setCombinedFarmPercent(null);
    enw.setContributionMargin(null);
    enw.setEnrolmentFee(null);
    enw.setEnrolmentYear(null);
    enw.setManualContributionMargin(null);
    enw.setManualEnrolmentFee(null);
    enw.setManualMarginsCalculated(null);
    enw.setMarginYearMinus2(null);
    enw.setMarginYearMinus2Used(false);
    enw.setMarginYearMinus3(null);
    enw.setMarginYearMinus3Used(false);
    enw.setMarginYearMinus4(null);
    enw.setMarginYearMinus4Used(false);
    enw.setMarginYearMinus5(null);
    enw.setMarginYearMinus5Used(false);
    enw.setMarginYearMinus6(null);
    enw.setMarginYearMinus6Used(false);
    enw.setProxyContributionMargin(null);
    enw.setProxyEnrolmentFee(null);
    enw.setProxyMarginsCalculated(null);
    enw.setProxyMarginYearMinus2(null);
    enw.setProxyMarginYearMinus3(null);
    enw.setProxyMarginYearMinus4(null);
  }

  public Enrolment convertEnwToEnrolment(Scenario scenario, EnwEnrolment enw) {
    Enrolment e = new Enrolment();
    
    e.setEnrolmentYear(enw.getEnrolmentYear());
    e.setCombinedFarmPercent(enw.getCombinedFarmPercent());
    
    e.setIsMarginYearMinus2Used(enw.getMarginYearMinus2Used());
    e.setIsMarginYearMinus3Used(enw.getMarginYearMinus3Used());
    e.setIsMarginYearMinus4Used(enw.getMarginYearMinus4Used());
    e.setIsMarginYearMinus5Used(enw.getMarginYearMinus5Used());
    e.setIsMarginYearMinus6Used(enw.getMarginYearMinus6Used());
    
    e.setMarginYearMinus2(enw.getMarginYearMinus2());
    e.setMarginYearMinus3(enw.getMarginYearMinus3());
    e.setMarginYearMinus4(enw.getMarginYearMinus4());
    e.setMarginYearMinus5(enw.getMarginYearMinus5());
    e.setMarginYearMinus6(enw.getMarginYearMinus6());
    
    e.setContributionMarginAverage(enw.getContributionMargin());
    e.setEnrolmentFee(enw.getEnrolmentFee());
    
    e.setClientId(scenario.getClient().getClientId());
    e.setGeneratedDate(new Date());
    e.setFailedToGenerate(false);
    e.setIsCreateTaskInBarn(false);
    e.setIsGeneratedFromCra(false);
    e.setIsGeneratedFromEnw(true);
    e.setIsLateParticipant(false);
    e.setIsInCombinedFarm(scenario.isInCombinedFarm());
    e.setMarginScenarioId(scenario.getScenarioId());
    e.setPin(scenario.getClient().getParticipantPin());
    e.setScenario(scenario);
    
    return e;
  }

  public void copyEditableFields(EnwEnrolment from, EnwEnrolment to) {
    if(from != null && to !=null) {
      to.setEnrolmentCalculationTypeCode(from.getEnrolmentCalculationTypeCode());
      to.setManualMarginYearMinus2(from.getManualMarginYearMinus2());
      to.setManualMarginYearMinus3(from.getManualMarginYearMinus3());
      to.setManualMarginYearMinus4(from.getManualMarginYearMinus4());
    }
  }
}
