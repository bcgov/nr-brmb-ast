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
package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class TipReportsFilterContext implements Serializable {

  private static final long serialVersionUID = -6382308024959666188L;

  private int year;
  private String reportStatusFilter;
  private String pinFilter;
  private String tipParticipantIndFilter;
  private String agriStabilityParticipantIndFilter;

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getReportStatusFilter() {
    return reportStatusFilter;
  }

  public void setReportStatusFilter(String reportStatusFilter) {
    this.reportStatusFilter = reportStatusFilter;
  }

  public String getPinFilter() {
    return pinFilter;
  }

  public void setPinFilter(String pinFilter) {
    this.pinFilter = pinFilter;
  }

  public String getTipParticipantIndFilter() {
    return tipParticipantIndFilter;
  }

  public void setTipParticipantIndFilter(String tipParticipantIndFilter) {
    this.tipParticipantIndFilter = tipParticipantIndFilter;
  }

  public String getAgriStabilityParticipantIndFilter() {
    return agriStabilityParticipantIndFilter;
  }

  public void setAgriStabilityParticipantIndFilter(String agriStabilityFilter) {
    this.agriStabilityParticipantIndFilter = agriStabilityFilter;
  }
}
