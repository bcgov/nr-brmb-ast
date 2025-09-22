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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author awilkinson
 * @created Feb 24, 2012
 */
public class NewParticipant implements Serializable {
  
  private static final long serialVersionUID = 1;
  
  private Client client;
  
  private Integer programYear;
  
  private String municipalityCode;
  
  private List<FarmingOperation> farmingOperations;

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public List<FarmingOperation> getFarmingOperations() {
    return farmingOperations;
  }

  public void setFarmingOperations(List<FarmingOperation> farmingOperations) {
    this.farmingOperations = farmingOperations;
  }

  public String getMunicipalityCode() {
    return municipalityCode;
  }

  public void setMunicipalityCode(String municipalityCode) {
    this.municipalityCode = municipalityCode;
  }
}
