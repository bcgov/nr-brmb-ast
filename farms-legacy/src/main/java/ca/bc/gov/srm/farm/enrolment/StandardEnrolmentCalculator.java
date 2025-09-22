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
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class StandardEnrolmentCalculator extends EnrolmentCalculator {
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  protected StandardEnrolmentCalculator() {
  }
  
  public Enrolment calculateEnrolment(int enrolmentYear, Scenario scenario, boolean createTaskInBarn) {
    logger.debug("<calculateStagingEnrolment");
    
    EnrolmentStaging stagingEnrolment = calculateStagingEnrolment(enrolmentYear, scenario, createTaskInBarn);
    Enrolment enrolment = convertToEnrolment(stagingEnrolment);

    // EnrolmentStaging does not include clientId
    enrolment.setClientId(scenario.getClient().getClientId());
    // Staging generated date gets set by a database proc when part of the batch process used by the Enrolments screen.
    enrolment.setGeneratedDate(new Date());
    
    logger.debug(">calculateStagingEnrolment");
    return enrolment;
  }

  public EnrolmentStaging calculateStagingEnrolment(int enrolmentYear, Scenario scenario, boolean createTaskInBarn) {
    logger.debug("<calculateStagingEnrolment");
    
    Integer pin = scenario.getClient().getParticipantPin();
    
    EnrolmentStaging e;
    int enrYear = enrolmentYear;
    Benefit benefit = scenario.getFarmingYear().getBenefit();
    
    double percentOfMargin;
    if(scenario.isInCombinedFarm()) {
      percentOfMargin = benefit.getCombinedFarmPercent();
    } else {
      percentOfMargin = 1.0; // if it's not a combined farm they get 100% of the benefit
    }

    Double m2 = null;
    Double m3 = null;
    Double m4 = null;
    Double m5 = null;
    Double m6 = null;
    
    boolean m2Used = false;
    boolean m3Used = false;
    boolean m4Used = false;
    boolean m5Used = false;
    boolean m6Used = false;
    
    List<Double> margins = new ArrayList<>();
    Double min = null;
    Double max = null;
    
    final int n2 = 2;
    final int n3 = 3;
    final int n4 = 4;
    final int n5 = 5;
    final int n6 = 6;
    
    boolean programYearMissingProductiveUnits = yearMissingProductiveUnits(scenario);
    boolean missingProductiveUnits = programYearMissingProductiveUnits;
    
    for(ReferenceScenario rs : scenario.getAllScenarios()) {
      if(rs != null && rs.getFarmingYear() != null && rs.getFarmingYear().getMarginTotal() != null) {
        int rsYear = rs.getYear();
        MarginTotal marginTotal = rs.getFarmingYear().getMarginTotal();
        boolean refYearMissingProductiveUnits = yearMissingProductiveUnits(rs);
        if(refYearMissingProductiveUnits) {
          missingProductiveUnits = true;
        }

        Double curMargin;
        if(rsYear == scenario.getYear()
            || programYearMissingProductiveUnits       
            || refYearMissingProductiveUnits) {        

          // 1. Structural change is not calcuated for the program year,
          //    so for it we must use the accrual adjusted margin.
          //
          // 2. If there are no productive units for the program year or reference year
          //    then a ratio cannot be calculated so we must use the accrual adjusted margin.
          
          curMargin = marginTotal.getProductionMargAccrAdjs();
        } else {
          curMargin = marginTotal.getProductionMargAftStrChangs();
        }
        curMargin = MathUtils.roundCurrency(curMargin);

        double income = marginTotal.getTotalAllowableIncome();
        double expenses = marginTotal.getTotalUnallowableExpenses();
        boolean hasIncomeOrExpenses = income != 0 || expenses != 0;

        if(rsYear >= enrYear - n6 && rsYear <= enrYear - n2
            && curMargin != null
            && hasIncomeOrExpenses) {
          
          curMargin *= percentOfMargin;  // apply combined farm percent if applicable.

          if (Math.abs(curMargin) != 0.0) {
            margins.add(curMargin);
          }

          if(min == null || curMargin < min) {
            min = curMargin;
          }
          
          if(max == null || curMargin > max) {
            max = curMargin;
          }
          
          if(rsYear == enrYear - n2) {
            m2 = curMargin;
            m2Used = true;
          } else if(rsYear == enrYear - n3) {
            m3 = curMargin;
            m3Used = true;
          } else if(rsYear == enrYear - n4) {
            m4 = curMargin;
            m4Used = true;
          } else if(rsYear == enrYear - n5 && Math.abs(curMargin) != 0.0) { // cannot use zeroes to calculate, per FARM-1029
            m5 = curMargin;
            m5Used = true;
          } else if(rsYear == enrYear - n6 && Math.abs(curMargin) != 0.0) {
            m6 = curMargin;
            m6Used = true;
          }
        }
      }
    }
    
    // must have margins for the three most recent years (year - 2 to year - 4)
    boolean hasThreeMostRecentYears = m2 != null && m3 != null && m4 != null;
    @SuppressWarnings({ "null" })
    boolean hasZeroMarginsInThreeMostRecentYears = hasThreeMostRecentYears && (m2 == 0.0 || m3 == 0.0 || m4 == 0.0);
    
    boolean oversizeMargins = hasOversizeMargins(margins);

    if(hasZeroMarginsInThreeMostRecentYears) {
      // error. Do not calculate olympic average.

    // if there are 5 years then calculate olympic average (remove the min and max margins)
    } else if(min != null && margins.size() == n5) {
      
      // remove the minimum and mark as not used
      margins.remove(min);
      if(m2 != null && m2.equals(min)) {
        m2Used = false;
      } else if(m3 != null && m3.equals(min)) {
        m3Used = false;
      } else if(m4 != null && m4.equals(min)) {
        m4Used = false;
      } else if(m5 != null && m5.equals(min)) {
        m5Used = false;
      } else if(m6 != null && m6.equals(min)) {
        m6Used = false;
      }
      
      // remove the maximum and mark as not used
      margins.remove(max);
      if(m2 != null && m2.equals(max)) {
        m2Used = false;
      } else if(m3 != null && m3.equals(max)) {
        m3Used = false;
      } else if(m4 != null && m4.equals(max)) {
        m4Used = false;
      } else if(m5 != null && m5.equals(max)) {
        m5Used = false;
      } else if(m6 != null && m6.equals(max)) {
        m6Used = false;
      }

    // if there are 4 years then use the 3 most recent
    } else if(margins.size() == n4 && hasThreeMostRecentYears) {

      if(m5 != null) {
        margins.remove(m5);
        m5Used = false;
      } else if(m6 != null) {
        margins.remove(m6);
        m6Used = false;
      }
    }
    
    if(!hasThreeMostRecentYears || margins.size() < n3) {
      logPin("Failed to calculate. " + Enrolment.REASON_INSUFF_REF_MARGIN_DATA, pin);
      e = enrolmentStagingFailed(pin, enrolmentYear, false, Enrolment.REASON_INSUFF_REF_MARGIN_DATA);
    } else if(hasZeroMarginsInThreeMostRecentYears) {
      logPin("Failed to calculate. " + Enrolment.REASON_ZERO_MARGINS, pin);
      e = enrolmentStagingFailed(pin, enrolmentYear, true, Enrolment.REASON_ZERO_MARGINS);
    } else if(margins.size() > n3) { // Unexpected condition. This would be a bug.
      logPin("Failed to calculate. " + Enrolment.REASON_FOUND_TOO_MANY_MARGINS, pin);
      e = enrolmentStagingFailed(pin, enrolmentYear, true, Enrolment.REASON_FOUND_TOO_MANY_MARGINS);
    } else if(oversizeMargins) {
      logPin("Failed to calculate. " + Enrolment.REASON_OVERSIZE_MARGIN, pin);
      e = enrolmentStagingFailed(pin, enrolmentYear, false, Enrolment.REASON_OVERSIZE_MARGIN);
    } else {
      
      double marginSum = 0;
      for(Iterator<Double> mi = margins.iterator(); mi.hasNext(); ) {
        Double m = mi.next();
        marginSum += m;
      }
      double contributionMargin = MathUtils.roundCurrency(marginSum / margins.size());

      double fee = contributionMargin * CalculatorConfig.getEnrolmentFeeFactor(enrolmentYear);
      
      fee = MathUtils.roundCurrency(fee);
      if(fee < CalculatorConfig.MIN_ENROLMENT_FEE) {
        fee = CalculatorConfig.MIN_ENROLMENT_FEE;
      }

      e = new EnrolmentStaging();
      e.setPin(pin);
      e.setEnrolmentYear(enrolmentYear);
      e.setEnrolmentFee(fee);
      e.setFailedToGenerate(false);
      e.setFailedToCalculateFromBenefitMargins(false);
      e.setIsError(false);
      e.setContributionMarginAverage(contributionMargin);
      e.setMarginYearMinus2(m2);
      e.setMarginYearMinus3(m3);
      e.setMarginYearMinus4(m4);
      e.setMarginYearMinus5(m5);
      e.setMarginYearMinus6(m6);
      e.setIsMarginYearMinus2Used(m2Used);
      e.setIsMarginYearMinus3Used(m3Used);
      e.setIsMarginYearMinus4Used(m4Used);
      e.setIsMarginYearMinus5Used(m5Used);
      e.setIsMarginYearMinus6Used(m6Used);
      e.setIsGeneratedFromCra(true);
      e.setIsGeneratedFromEnw(scenario.isEnrolmentNoticeWorkflow());
      e.setIsCreateTaskInBarn(createTaskInBarn);
      e.setMarginScenarioId(scenario.getScenarioId());
      e.setCombinedFarmPercent(benefit.getCombinedFarmPercent());
      
      if(missingProductiveUnits) {
        e.setFailedReason(Enrolment.WARNING_MISSING_PRODUCTIVE_CAPACITY);
      }

      logPin("Calculated Contribution Margin for PIN: ", pin, "Contribution Margin", contributionMargin);
    }

    logger.debug(">calculateStagingEnrolment");
    return e;
  }

  private Enrolment convertToEnrolment(EnrolmentStaging stagingEnrolment) {
    Enrolment enrolment = new Enrolment();
    
    enrolment.setContributionMarginAverage(stagingEnrolment.getContributionMarginAverage());
    enrolment.setCombinedFarmPercent(stagingEnrolment.getCombinedFarmPercent());
    enrolment.setEnrolmentFee(stagingEnrolment.getEnrolmentFee());
    enrolment.setEnrolmentYear(stagingEnrolment.getEnrolmentYear());
    enrolment.setFailedReason(stagingEnrolment.getFailedReason());
    enrolment.setFailedToGenerate(stagingEnrolment.getFailedToGenerate());
    enrolment.setGeneratedDate(stagingEnrolment.getGeneratedDate());
    enrolment.setIsCreateTaskInBarn(stagingEnrolment.getIsCreateTaskInBarn());
    enrolment.setIsGeneratedFromCra(stagingEnrolment.getIsGeneratedFromCra());
    enrolment.setIsGeneratedFromEnw(stagingEnrolment.getIsGeneratedFromEnw());
    enrolment.setIsMarginYearMinus2Used(stagingEnrolment.getIsMarginYearMinus2Used());
    enrolment.setIsMarginYearMinus3Used(stagingEnrolment.getIsMarginYearMinus3Used());
    enrolment.setIsMarginYearMinus4Used(stagingEnrolment.getIsMarginYearMinus4Used());
    enrolment.setIsMarginYearMinus5Used(stagingEnrolment.getIsMarginYearMinus5Used());
    enrolment.setIsMarginYearMinus6Used(stagingEnrolment.getIsMarginYearMinus6Used());
    enrolment.setMarginScenarioId(stagingEnrolment.getMarginScenarioId());
    enrolment.setMarginYearMinus2(stagingEnrolment.getMarginYearMinus2());
    enrolment.setMarginYearMinus3(stagingEnrolment.getMarginYearMinus3());
    enrolment.setMarginYearMinus4(stagingEnrolment.getMarginYearMinus4());
    enrolment.setMarginYearMinus5(stagingEnrolment.getMarginYearMinus5());
    enrolment.setMarginYearMinus6(stagingEnrolment.getMarginYearMinus6());
    enrolment.setPin(stagingEnrolment.getPin());
    
    return enrolment;
  }

}
