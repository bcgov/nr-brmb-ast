/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.subscription;


import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * SubscriptionForm - for screen 340.
 */
public class SubscriptionForm extends CalculatorForm {

  /** DOCUMENT ME! */
  private static final long serialVersionUID = 8830013273402595168L;

  /** DOCUMENT ME! */
  private Integer clientSubscriptionId;

  /** DOCUMENT ME! */
  private Integer revisionCount;

  /** DOCUMENT ME! */
  private String reportUrl;

  /**
   * @return  the clientSubscriptionId
   */
  public Integer getClientSubscriptionId() {
    return clientSubscriptionId;
  }

  /**
   * @param  clientSubscriptionId  the clientSubscriptionId to set
   */
  public void setClientSubscriptionId(Integer clientSubscriptionId) {
    this.clientSubscriptionId = clientSubscriptionId;
  }

  /**
   * @return  the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param  revisionCount  the revisionCount to set
   */
  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  /**
   * @return  the reportUrl
   */
  public String getReportUrl() {
    return reportUrl;
  }

  /**
   * @param  reportUrl  the reportUrl to set
   */
  public void setReportUrl(String reportUrl) {
    this.reportUrl = reportUrl;
  }
}
