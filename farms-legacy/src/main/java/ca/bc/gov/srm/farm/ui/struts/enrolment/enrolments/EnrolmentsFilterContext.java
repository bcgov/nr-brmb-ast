/**
 * Copyright (c) 2014,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class EnrolmentsFilterContext implements Serializable {

  private static final long serialVersionUID = -6382308024959666188L;

  private int year;
  private String regionalOfficeCode;
  private String enrolmentStatusFilter;
  private String scenarioStateFilter;
  private String startDateFilter;
  private String endDateFilter;
  private String pinFilter;
  private String failedReasonFilter;

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getRegionalOfficeCode() {
    return regionalOfficeCode;
  }

  public void setRegionalOfficeCode(String regionalOfficeCode) {
    this.regionalOfficeCode = regionalOfficeCode;
  }

  public String getEnrolmentStatusFilter() {
    return enrolmentStatusFilter;
  }

  public void setEnrolmentStatusFilter(String enrolmentStatusFilter) {
    this.enrolmentStatusFilter = enrolmentStatusFilter;
  }

  public String getScenarioStateFilter() {
    return scenarioStateFilter;
  }

  public void setScenarioStateFilter(String scenarioStateFilter) {
    this.scenarioStateFilter = scenarioStateFilter;
  }

  public String getStartDateFilter() {
    return startDateFilter;
  }

  public void setStartDateFilter(String startDateFilter) {
    this.startDateFilter = startDateFilter;
  }

  public String getEndDateFilter() {
    return endDateFilter;
  }

  public void setEndDateFilter(String endDateFilter) {
    this.endDateFilter = endDateFilter;
  }

  public String getPinFilter() {
    return pinFilter;
  }

  public void setPinFilter(String pinFilter) {
    this.pinFilter = pinFilter;
  }

  public String getFailedReasonFilter() {
    return failedReasonFilter;
  }

  public void setFailedReasonFilter(String failedReasonFilter) {
    this.failedReasonFilter = failedReasonFilter;
  }
}
