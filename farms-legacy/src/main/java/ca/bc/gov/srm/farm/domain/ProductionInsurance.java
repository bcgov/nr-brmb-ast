/**
 *
 * Copyright (c) 2010,
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

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * ProductionInsurance provides the production Insurance contract numbers
 * provided by the participant on the supplemental page of the AgriStability
 * application.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:56 PM
 */
public final class ProductionInsurance implements Serializable {
  
  private static final long serialVersionUID = 3599418803804433028L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingOperation farmingOperation;

  /**
   * productionInsuranceId is a surrogate unique identifier for
   * ProductionInsurance.
   */
  private Integer productionInsuranceId;

  /**
   * productionInsuranceNumber is the contract number provided by the
   * participant on the supplemental page of the AgriStability application.
   */
  private String productionInsuranceNumber;

  /** isLocallyUpdated identifies if the record was updated by the client. */
  private Boolean isLocallyUpdated;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /** Constructor. */
  public ProductionInsurance() {

  }

  /**
   * productionInsuranceId is a surrogate unique identifier for
   * ProductionInsurance.
   *
   * @return  Integer
   */
  public Integer getProductionInsuranceId() {
    return productionInsuranceId;
  }

  /**
   * productionInsuranceId is a surrogate unique identifier for
   * ProductionInsurance.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductionInsuranceId(final Integer newVal) {
    productionInsuranceId = newVal;
  }

  /**
   * ProductionInsuranceNumber is the contract number provided by the
   * participant on the supplemental page of the AgriStability application.
   *
   * @return  String
   */
  public String getProductionInsuranceNumber() {
    return productionInsuranceNumber;
  }

  /**
   * ProductionInsuranceNumber is the contract number provided by the
   * participant on the supplemental page of the AgriStability application.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductionInsuranceNumber(final String newVal) {
    productionInsuranceNumber = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }

  /**
   * @return  Boolean
   */
  public Boolean getIsLocallyUpdated() {
    return isLocallyUpdated;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setIsLocallyUpdated(final Boolean newVal) {
    isLocallyUpdated = newVal;
  }

  /**
   * @return the farmingOperation
   */
  public FarmingOperation getFarmingOperation() {
    return farmingOperation;
  }

  /**
   * @param farmingOperation the farmingOperation to set the value to
   */
  public void setFarmingOperation(FarmingOperation farmingOperation) {
    this.farmingOperation = farmingOperation;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingOperationId = null;
    if(farmingOperation != null) {
      farmingOperationId = farmingOperation.getFarmingOperationId();
    }

    return "ProductionInsurance"+"\n"+
    "\t farmingOperation : "+farmingOperationId+"\n"+
    "\t isLocallyUpdated : "+isLocallyUpdated+"\n"+
    "\t productionInsuranceId : "+productionInsuranceId+"\n"+
    "\t productionInsuranceNumber : "+productionInsuranceNumber+"\n"+
    "\t revisionCount : "+revisionCount;
  }
}
