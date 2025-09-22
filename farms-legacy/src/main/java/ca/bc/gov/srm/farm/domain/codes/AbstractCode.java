/**
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import java.util.Date;

import ca.bc.gov.srm.farm.util.DateUtils;

/**
 * @author awilkinson
 */
public class AbstractCode {

  private String description;
  private Date establishedDate;
  private Date expiryDate;
  private Integer revisionCount;

  /**
   * Gets description
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description
   *
   * @param pDescription the description to set
   */
  public void setDescription(String pDescription) {
    description = pDescription;
  }

  /**
   * Gets establishedDate
   *
   * @return the establishedDate
   */
  public Date getEstablishedDate() {
    return establishedDate;
  }

  /**
   * Sets establishedDate
   *
   * @param pEstablishedDate the establishedDate to set
   */
  public void setEstablishedDate(Date pEstablishedDate) {
    establishedDate = pEstablishedDate;
  }

  /**
   * Gets expiryDate
   *
   * @return the expiryDate
   */
  public Date getExpiryDate() {
    return expiryDate;
  }

  /**
   * Sets expiryDate
   *
   * @param pExpiryDate the expiryDate to set
   */
  public void setExpiryDate(Date pExpiryDate) {
    expiryDate = pExpiryDate;
  }

  /**
   * Gets revisionCount
   *
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * Sets revisionCount
   *
   * @param pRevisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer pRevisionCount) {
    revisionCount = pRevisionCount;
  }

  /**
   * @return boolean
   */
  public boolean isExpired() {
    return DateUtils.isExpired(expiryDate);
  }
  
}
