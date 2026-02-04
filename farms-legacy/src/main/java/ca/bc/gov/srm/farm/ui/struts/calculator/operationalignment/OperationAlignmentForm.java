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
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Feb 16, 2011
 */
public class OperationAlignmentForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645582621365403105L;
  
  private List<String> schedules;

  /** List<OperationDetailFormData> */
  private List<OperationDetailFormData> operationDetails;
  
  /** Map<String year, OpYearFormData> */
  private Map<String, OpYearFormData> yearScheduleOpNumMap = new HashMap<>();
  

  /**
   * @return the schedules
   */
  public List<String> getSchedules() {
    return schedules;
  }

  /**
   * @param schedules the schedules to set the value to
   */
  public void setSchedules(List<String> schedules) {
    this.schedules = schedules;
  }

  /**
   * @return the operationDetails
   */
  public List<OperationDetailFormData> getOperationDetails() {
    return operationDetails;
  }

  /**
   * @param operationDetails the operationDetails to set the value to
   */
  public void setOperationDetails(List<OperationDetailFormData> operationDetails) {
    this.operationDetails = operationDetails;
  }

  /**
   * @return the yearScheduleOpNumMap
   */
  public Map<String, OpYearFormData> getYearScheduleOpNumMap() {
    return yearScheduleOpNumMap;
  }

  /**
   * @param year String
   * @return the yearScheduleOpNumMap
   */
  public OpYearFormData getOpYearFormData(String year) {
    OpYearFormData result = yearScheduleOpNumMap.get(year);
    if(result == null) {
      result = new OpYearFormData();
      yearScheduleOpNumMap.put(year, result);
    }
    return result;
  }
  
  /**
   * @return size of requiredYears
   */
  public int getNumberOfRequiredYears() {
    int result;
    if(getRequiredYears() == null) {
      result = 0;
    } else {
      result = getRequiredYears().size();
    }
    return result;
  }

}
