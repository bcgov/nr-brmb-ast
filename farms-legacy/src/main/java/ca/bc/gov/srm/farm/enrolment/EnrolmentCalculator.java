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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;
import ca.bc.gov.srm.farm.ui.domain.dataimport.LogConverter;

/**
 * @author awilkinson
 */
public abstract class EnrolmentCalculator {
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  protected EnrolmentCalculator() {
  }
  
  public EnrolmentStaging enrolmentStagingFailed(Integer pin, Integer enrolmentYear, boolean error, String reason) {
    final int reasonMaxSize = 1000;
    String failedReason = LogConverter.formatErrorMessage(reason);
    if(failedReason.length() > reasonMaxSize) {
      failedReason = failedReason.substring(0, reasonMaxSize);
    }

    EnrolmentStaging enrolment = new EnrolmentStaging();
    enrolment.setPin(pin);
    enrolment.setEnrolmentYear(enrolmentYear);
    enrolment.setFailedToGenerate(true);
    enrolment.setFailedToCalculateFromBenefitMargins(true);
    enrolment.setIsError(error);
    enrolment.setFailedReason(failedReason);
    enrolment.setIsMarginYearMinus2Used(false);
    enrolment.setIsMarginYearMinus3Used(false);
    enrolment.setIsMarginYearMinus4Used(false);
    enrolment.setIsMarginYearMinus5Used(false);
    enrolment.setIsMarginYearMinus6Used(false);
    return enrolment;
  }

  protected boolean yearMissingProductiveUnits(ReferenceScenario scenario) {
    boolean missingProductiveUnits = true;
    
    for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
      if(fo.getProductiveUnitCapacitiesForStructureChange() != null) {
        
        for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacitiesForStructureChange()) {
          Double prodCap = puc.getTotalProductiveCapacityAmount();
          
          if(prodCap != null && prodCap != 0) {
            missingProductiveUnits = false;
            break;
          }
        }
      }
    }
    return missingProductiveUnits;
  }
  
  
  protected boolean hasOversizeMargins(List<Double> margins) {
    final double maxMargin = 99999999.99;
    boolean result = false;
    
    for(Double margin : margins) {
      if(margin > maxMargin) {
        result = true;
        break;
      }
    }
    
    return result;
  }

  protected void logPin(String logMessage, Integer pin) {
    StringBuilder sb = new StringBuilder();
    sb.append(logMessage);
    sb.append(" PIN: ");
    sb.append(pin);
    logger.debug(sb.toString());
  }
  
  protected void logPin(String logMessage, Integer pin, String param1Name, Object param1) {
    StringBuilder sb = new StringBuilder();
    sb.append(logMessage);
    sb.append(pin);
    sb.append(" ");
    sb.append(param1Name);
    sb.append(": ");
    sb.append(param1);
    logger.debug(sb.toString());
  }
}
