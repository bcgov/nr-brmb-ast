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
package ca.bc.gov.srm.farm.ui.domain;

import java.io.Serializable;

/**
 * @author awilkinson
 * @created Apr 1, 2011
 */
public class CalculatorInboxContext implements Serializable {

  private static final long serialVersionUID = -2645502921365403591L;

  private int inboxYear;
  
  private String inboxSearchType;
  
  /** checkboxes */
  private boolean inProgressCB;
  private boolean verifiedCB;
  private boolean closedCB;

  /**
   * @return the verifiedCB
   */
  public boolean isVerifiedCB() {
    return verifiedCB;
  }

  /**
   * @param verifiedCB the verifiedCB to set the value to
   */
  public void setVerifiedCB(boolean verifiedCB) {
    this.verifiedCB = verifiedCB;
  }

  /**
   * @return the closedCB
   */
  public boolean isClosedCB() {
    return closedCB;
  }

  /**
   * @param closedCB the closedCB to set the value to
   */
  public void setClosedCB(boolean closedCB) {
    this.closedCB = closedCB;
  }

  /**
   * @return the inboxSearchType
   */
  public String getInboxSearchType() {
    return inboxSearchType;
  }

  /**
   * @param inboxSearchType the inboxSearchType to set the value to
   */
  public void setInboxSearchType(String inboxSearchType) {
    this.inboxSearchType = inboxSearchType;
  }

  /**
   * @return the inProgressCB
   */
  public boolean isInProgressCB() {
    return inProgressCB;
  }

  /**
   * @param inProgressCB the inProgressCB to set the value to
   */
  public void setInProgressCB(boolean inProgressCB) {
    this.inProgressCB = inProgressCB;
  }

  /**
   * @return the inboxYear
   */
  public int getInboxYear() {
    return inboxYear;
  }

  /**
   * @param inboxYear the inboxYear to set the value to
   */
  public void setInboxYear(int inboxYear) {
    this.inboxYear = inboxYear;
  }

}
