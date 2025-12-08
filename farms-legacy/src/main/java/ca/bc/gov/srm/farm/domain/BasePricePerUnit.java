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
import java.util.Iterator;
import java.util.List;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * BasePricePerUnit (BPU) is the term used by the clients; however it is really
 * the expected profit margin on a commodity.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:48 PM
 */
public final class BasePricePerUnit implements Serializable {

  private static final long serialVersionUID = -3433128708134533837L;

  /**
   * basePricePerUnitId is a surrogate unique identifier for BasePricePerUnits.
   */
  private Integer basePricePerUnitId;

  private String comment;

  private String inventoryCode;
  
  private String structureGroupCode;
  
  private String municipalityCode;

  private List<BasePricePerUnitYear> basePricePerUnitYears;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /** Constructor. */
  public BasePricePerUnit() {

  }

  /**
   * @return  Integer
   */
  public Integer getBasePricePerUnitId() {
    return basePricePerUnitId;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setBasePricePerUnitId(final Integer newVal) {
    basePricePerUnitId = newVal;
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
   * @return  String
   *
   * @see     java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String basePricePerUnitYearsOutput = null;
    if(basePricePerUnitYears != null) {
      basePricePerUnitYearsOutput = basePricePerUnitYears.size() +
      "\n" + StringUtils.concat(basePricePerUnitYears.toArray());
    }
    
    return "BasePricePerUnit"+"\n"+
    "\t basePricePerUnitId : "+basePricePerUnitId + "\n"+
    "\t comment : "+comment+"\n" +
    "\t inventoryCode : " + inventoryCode + "\n"+
    "\t revisionCount : " + revisionCount + "\n"+
    "\t municipalityCode : " + municipalityCode + "\n"+
    "\t basePricePerUnitYears : "+ basePricePerUnitYearsOutput + "\n";
  }

  /**
   * @return  the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * @param  pComment  the comment to set
   */
  public void setComment(String pComment) {
    comment = pComment;
  }

  /**
   * @return  the basePricePerUnitYears
   */
  public List<BasePricePerUnitYear> getBasePricePerUnitYears() {
    return basePricePerUnitYears;
  }

  /**
   * @param  basePricePerUnitYears  the basePricePerUnitYears to set
   */
  public void setBasePricePerUnitYears(List<BasePricePerUnitYear> basePricePerUnitYears) {
    if(basePricePerUnitYears != null) {
      for(Iterator<BasePricePerUnitYear> pi = basePricePerUnitYears.iterator(); pi.hasNext(); ) {
        BasePricePerUnitYear cur = pi.next();
        cur.setBasePricePerUnit(this);
      }
    }
    this.basePricePerUnitYears = basePricePerUnitYears;
  }

  /**
   * @return the municipalityCode
   */
  public String getMunicipalityCode() {
    return municipalityCode;
  }

  /**
   * @param pMunicipalityCode the municipalityCode to set
   */
  public void setMunicipalityCode(String pMunicipalityCode) {
    municipalityCode = pMunicipalityCode;
  }

  /**
   * @return the inventoryCode
   */
  public String getInventoryCode() {
    return inventoryCode;
  }

  /**
   * @param pInventoryCode the inventoryCode to set
   */
  public void setInventoryCode(String pInventoryCode) {
    inventoryCode = pInventoryCode;
  }

  /**
   * @return the structureGroupCode
   */
  public String getStructureGroupCode() {
    return structureGroupCode;
  }

  /**
   * @param value the structureGroupCode to set
   */
  public void setStructureGroupCode(String value) {
    this.structureGroupCode = value;
  }
  
  
}
