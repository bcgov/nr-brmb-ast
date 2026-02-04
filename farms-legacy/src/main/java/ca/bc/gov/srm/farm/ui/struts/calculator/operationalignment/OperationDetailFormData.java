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

/**
 * @author awilkinson
 * @created Feb 17, 2011
 */
public class OperationDetailFormData {

  private String year;
  
  private String operationNumber;
  
  private String partnershipName;
  
  private String partnershipPercent;
  
  private String partnershipPin;
  
  private String[] topIncome;


  /**
   * @return the operationNumber
   */
  public String getOperationNumber() {
    return operationNumber;
  }

  /**
   * @param operationNumber the operationNumber to set the value to
   */
  public void setOperationNumber(String operationNumber) {
    this.operationNumber = operationNumber;
  }

  /**
   * @return the partnershipName
   */
  public String getPartnershipName() {
    return partnershipName;
  }

  /**
   * @param partnershipName the partnershipName to set the value to
   */
  public void setPartnershipName(String partnershipName) {
    this.partnershipName = partnershipName;
  }

  /**
   * @return the partnershipPercent
   */
  public String getPartnershipPercent() {
    return partnershipPercent;
  }

  /**
   * @param partnershipPercent the partnershipPercent to set the value to
   */
  public void setPartnershipPercent(String partnershipPercent) {
    this.partnershipPercent = partnershipPercent;
  }

  /**
   * @return the partnershipPin
   */
  public String getPartnershipPin() {
    return partnershipPin;
  }

  /**
   * @param partnershipPin the partnershipPin to set the value to
   */
  public void setPartnershipPin(String partnershipPin) {
    this.partnershipPin = partnershipPin;
  }

  /**
   * @return the topIncome
   */
  public String[] getTopIncome() {
    return topIncome;
  }

  /**
   * @param topIncome the topIncome to set the value to
   */
  public void setTopIncome(String[] topIncome) {
    this.topIncome = topIncome;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @param year the year to set the value to
   */
  public void setYear(String year) {
    this.year = year;
  }
  
  
}
