/**
 *
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 * 
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.enrolment;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * This class represents a client's enrolment status in the
 * Agristability Program for a particular year.
 * 
 * @author awilkinson
 * @created July 19, 2021
 */
public class EnrolmentPartner implements Serializable {
  
  private static final long serialVersionUID = 5925929752184006372L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Enrolment enrolment;
  
  /**
   * partnershipName is the name of the partnership for the FarmingOperation.
   */
  private String partnershipName;

  /**
   * partnershipPercent is the Percentage of the Partnership (100% will be
   * stored as 1.0).
   */
  private BigDecimal partnershipPercent;

  /**
   * partnershipPin uniquely identifies the partnership. If both the partners in
   * an operation file applications, the same partnershipPin will show up under
   * both pins. Partnership pins represent the same operation if/when they are
   * used in different stab years.
   */
  private Integer partnershipPin;

  public Enrolment getEnrolment() {
    return enrolment;
  }

  public void setEnrolment(Enrolment enrolment) {
    this.enrolment = enrolment;
  }

  public String getPartnershipName() {
    return partnershipName;
  }

  public void setPartnershipName(String partnershipName) {
    this.partnershipName = partnershipName;
  }

  public BigDecimal getPartnershipPercent() {
    return partnershipPercent;
  }

  public void setPartnershipPercent(BigDecimal partnershipPercent) {
    this.partnershipPercent = partnershipPercent;
  }

  public Integer getPartnershipPin() {
    return partnershipPin;
  }

  public void setPartnershipPin(Integer partnershipPin) {
    this.partnershipPin = partnershipPin;
  }

  /**
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer enrolmentId = null;
    if(enrolment != null) {
      enrolmentId = enrolment.getEnrolmentId();
    }

    return "EnrolmentPartner"+"\n"+
    "\t enrolmentId : "+enrolmentId+"\n"+
    partnershipName+partnershipName+"\n"+
    "\t partnershipPercent : "+partnershipPercent+"\n"+
    "\t partnershipPin : "+partnershipPin+"\n";
  }

}
