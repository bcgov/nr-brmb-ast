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
public class EnrolmentCombinedFarmOwner implements Serializable {
  
  private static final long serialVersionUID = 5925929752184006372L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Enrolment enrolment;

  private BigDecimal combinedFarmPercent;

  private Integer participantPin;

  public Enrolment getEnrolment() {
    return enrolment;
  }

  public void setEnrolment(Enrolment enrolment) {
    this.enrolment = enrolment;
  }

  public Integer getParticipantPin() {
    return participantPin;
  }

  public void setParticipantPin(Integer participantPin) {
    this.participantPin = participantPin;
  }

  public BigDecimal getCombinedFarmPercent() {
    return combinedFarmPercent;
  }

  public void setCombinedFarmPercent(BigDecimal combinedFarmPercent) {
    this.combinedFarmPercent = combinedFarmPercent;
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

    return "EnrolmentCombinedFarmOwner"+"\n"+
    "\t enrolmentId : "+enrolmentId+"\n"+
    "\t participantPin : "+participantPin+"\n"+
    "\t combinedFarmPercent : "+combinedFarmPercent+"\n";
  }

}
