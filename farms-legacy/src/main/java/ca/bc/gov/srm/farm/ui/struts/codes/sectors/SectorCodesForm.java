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

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.SectorCode;

public class SectorCodesForm extends ValidatorForm {
  private static final long serialVersionUID = 1L;
  
  private List<SectorCode> codes;
  private int numCodes;
  
  private String code;
  private String description;
  private Integer revisionCount;
  
  private String sectorCodeFilter;
  private String sectorCodeDescriptionFilter;
  private boolean isSetFilterContext;
  
  private boolean isNew = false;

  public List<SectorCode> getCodes() {
    return codes;
  }

  public void setCodes(List<SectorCode> codes) {
    this.codes = codes;
  }

  public int getNumCodes() {
    return numCodes;
  }

  public void setNumCodes(int numSectorCodes) {
    this.numCodes = numSectorCodes;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public String getSectorCodeFilter() {
    return sectorCodeFilter;
  }

  public void setSectorCodeFilter(String sectorCodeFilter) {
    this.sectorCodeFilter = sectorCodeFilter;
  }

  public String getSectorCodeDescriptionFilter() {
    return sectorCodeDescriptionFilter;
  }

  public void setSectorCodeDescriptionFilter(String sectorCodeDescriptionFilter) {
    this.sectorCodeDescriptionFilter = sectorCodeDescriptionFilter;
  }

  public boolean isSetFilterContext() {
    return isSetFilterContext;
  }

  public void setSetFilterContext(boolean isSetFilterContext) {
    this.isSetFilterContext = isSetFilterContext;
  }

}