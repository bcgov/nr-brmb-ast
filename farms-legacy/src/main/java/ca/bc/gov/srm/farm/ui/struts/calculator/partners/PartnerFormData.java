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
package ca.bc.gov.srm.farm.ui.struts.calculator.partners;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PartnerFormData {

  private String operationSchedule;
  private String partnerId;
  
  private String participantPin;
  
  private String partnerPercent;
  private String partnerSin;
  
  private String firstName;
  private String lastName;
  private String corpName;
  
  private String revisionCount;

  public String getOperationSchedule() {
    return operationSchedule;
  }

  public void setOperationSchedule(String operationSchedule) {
    this.operationSchedule = operationSchedule;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getParticipantPin() {
    return participantPin;
  }

  public void setParticipantPin(String participantPin) {
    this.participantPin = participantPin;
  }

  public String getPartnerPercent() {
    return partnerPercent;
  }

  public void setPartnerPercent(String partnershipPercent) {
    this.partnerPercent = partnershipPercent;
  }

  public String getPartnerSin() {
    return partnerSin;
  }

  public void setPartnerSin(String partnerSin) {
    this.partnerSin = partnerSin;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCorpName() {
    return corpName;
  }

  public void setCorpName(String corpName) {
    this.corpName = corpName;
  }

  public String getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(String revisionCount) {
    this.revisionCount = revisionCount;
  }

}
