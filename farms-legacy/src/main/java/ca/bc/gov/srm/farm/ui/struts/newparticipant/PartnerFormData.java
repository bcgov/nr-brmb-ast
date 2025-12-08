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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PartnerFormData {

  private Integer operationNumber;
  
  private String sin;
  
  private String partnerPercent;

  private String corpName;
  
  private String firstName;
  
  private String lastName;

  protected Integer getOperationNumber() {
    return operationNumber;
  }

  protected void setOperationNumber(Integer operationNumber) {
    this.operationNumber = operationNumber;
  }

  protected String getSin() {
    return sin;
  }

  protected void setSin(String sin) {
    this.sin = sin;
  }

  protected String getPartnerPercent() {
    return partnerPercent;
  }

  protected void setPartnerPercent(String partnerPercent) {
    this.partnerPercent = partnerPercent;
  }

  protected String getCorpName() {
    return corpName;
  }

  protected void setCorpName(String corpName) {
    this.corpName = corpName;
  }

  protected String getFirstName() {
    return firstName;
  }

  protected void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  protected String getLastName() {
    return lastName;
  }

  protected void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
