/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.newparticipant;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class OperationFormData {

  private String operationNumber;
  
  private String partnershipPin;
  private String partnershipName;
  private String partnershipPercent;

  private String accountingCode;
  
  private String fiscalStartDate;
  private String fiscalEndDate;
  
  private List<PartnerFormData> partners;

  public String getOperationNumber() {
    return operationNumber;
  }

  public void setOperationNumber(String operationNumber) {
    this.operationNumber = operationNumber;
  }

  public String getPartnershipPin() {
    return partnershipPin;
  }

  public void setPartnershipPin(String partnershipPin) {
    this.partnershipPin = partnershipPin;
  }

  public String getPartnershipName() {
    return partnershipName;
  }

  public void setPartnershipName(String partnershipName) {
    this.partnershipName = partnershipName;
  }

  public String getPartnershipPercent() {
    return partnershipPercent;
  }

  public void setPartnershipPercent(String partnershipPercent) {
    this.partnershipPercent = partnershipPercent;
  }

  public String getAccountingCode() {
    return accountingCode;
  }

  public void setAccountingCode(String accountingCode) {
    this.accountingCode = accountingCode;
  }

  public String getFiscalStartDate() {
    return fiscalStartDate;
  }

  public void setFiscalStartDate(String fiscalStartDate) {
    this.fiscalStartDate = fiscalStartDate;
  }

  public String getFiscalEndDate() {
    return fiscalEndDate;
  }

  public void setFiscalEndDate(String fiscalEndDate) {
    this.fiscalEndDate = fiscalEndDate;
  }

  public List<PartnerFormData> getPartners() {
    return partners;
  }

  public void setPartners(List<PartnerFormData> partners) {
    this.partners = partners;
  }

}
