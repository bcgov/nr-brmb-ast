/**
 * Copyright (c) 2013,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.list;


/**
 * @author awilkinson
 * @created November 8, 2013
 */
public class SectorDetailCodeListView extends BaseListView {
  
  private String sectorCode;

  private String sectorCodeDescription;
  
  private String sectorDetailCode;
  
  private String sectorDetailCodeDescription;
  
  
  /** */
  public SectorDetailCodeListView() {
    super();
  }
  
  /**
   * @return inventoryItemCodeDescription
   */
  @Override
  public String getLabel() {
    return getSectorDetailCodeDescription();
  }
  
  /**
   * @return inventoryItemCode
   */
  @Override
  public String getValue() {
    return getSectorDetailCode();
  }

  /**
   * @return the sectorCode
   */
  public String getSectorCode() {
    return sectorCode;
  }

  /**
   * @param sectorCode the sectorCode to set
   */
  public void setSectorCode(String sectorCode) {
    this.sectorCode = sectorCode;
  }

  /**
   * @return the sectorCodeDescription
   */
  public String getSectorCodeDescription() {
    return sectorCodeDescription;
  }

  /**
   * @param sectorCodeDescription the sectorCodeDescription to set
   */
  public void setSectorCodeDescription(String sectorCodeDescription) {
    this.sectorCodeDescription = sectorCodeDescription;
  }

  /**
   * @return the sectorDetailCode
   */
  public String getSectorDetailCode() {
    return sectorDetailCode;
  }

  /**
   * @param sectorDetailCode the sectorDetailCode to set
   */
  public void setSectorDetailCode(String sectorDetailCode) {
    this.sectorDetailCode = sectorDetailCode;
  }

  /**
   * @return the sectorDetailCodeDescription
   */
  public String getSectorDetailCodeDescription() {
    return sectorDetailCodeDescription;
  }

  /**
   * @param sectorDetailCodeDescription the sectorDetailCodeDescription to set
   */
  public void setSectorDetailCodeDescription(String sectorDetailCodeDescription) {
    this.sectorDetailCodeDescription = sectorDetailCodeDescription;
  }

}
