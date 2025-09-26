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
package ca.bc.gov.srm.farm.ui.struts.codes.sectors;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class SectorCodesFilterContext implements Serializable {

  private static final long serialVersionUID = 1;
  
  private String sectorCodeDescriptionFilter;

  private String sectorDetailCodeFilter;
  
  private String sectorDetailCodeDescriptionFilter;

  public String getSectorCodeDescriptionFilter() {
    return sectorCodeDescriptionFilter;
  }

  public void setSectorCodeDescriptionFilter(String sectorCodeDescriptionFilter) {
    this.sectorCodeDescriptionFilter = sectorCodeDescriptionFilter;
  }

  public String getSectorDetailCodeFilter() {
    return sectorDetailCodeFilter;
  }

  public void setSectorDetailCodeFilter(String sectorDetailCodeFilter) {
    this.sectorDetailCodeFilter = sectorDetailCodeFilter;
  }

  public String getSectorDetailCodeDescriptionFilter() {
    return sectorDetailCodeDescriptionFilter;
  }

  public void setSectorDetailCodeDescriptionFilter(String sectorDetailCodeDescriptionFilter) {
    this.sectorDetailCodeDescriptionFilter = sectorDetailCodeDescriptionFilter;
  }


}
