/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.operationalignment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author awilkinson
 * @created Feb 18, 2011
 */
public class OpYearFormData {

  /** Map<String schedule, String operationNumber> */
  private Map<String, String> scheduleOpNumMap = new HashMap<>();
  
  /** Map<String schedule, Boolean> */
  private Map<String, Boolean> errors = new HashMap<>();

  /**
   * @return the scheduleOpNumMap
   */
  public Map<String, String> getScheduleOpNumMap() {
    return scheduleOpNumMap;
  }
  
  /**
   * @param schedule String
   * @return String
   */
  public String getScheduleOpNum(String schedule) {
    return scheduleOpNumMap.get(schedule);
  }
  
  /**
   * @param schedule String
   * @param opNumStr String
   */
  public void setScheduleOpNum(String schedule, String opNumStr) {
    scheduleOpNumMap.put(schedule, opNumStr);
  }
  
  
  /**
   * @return the scheduleErrorMap
   */
  public Map<String, Boolean> getErrors() {
    return errors;
  }

  /**
   * @param schedule String
   * @param errorStatus Boolean
   */
  public void setError(String schedule, Boolean errorStatus) {
    errors.put(schedule, errorStatus);
  }
}
