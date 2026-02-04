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
package ca.bc.gov.srm.farm.ui.struts.codes.sector.details;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.SectorDetailCode;
import ca.bc.gov.srm.farm.list.CodeListView;

public class SectorDetailCodesForm extends ValidatorForm {
  private static final long serialVersionUID = 1L;
  
  private List<SectorDetailCode> codes;
  private int numCodes;
  private List<CodeListView> sectorCodeListItems;
  
  private String sectorCode;
  private String sectorCodeDescription;
  private String sectorDetailCode;
  private String sectorDetailCodeDescription;
  private Integer revisionCount;
  
  private String sectorCodeDescriptionFilter;
  private String sectorDetailCodeFilter;
  private String sectorDetailCodeDescriptionFilter;
  private boolean isSetFilterContext;
  
  private boolean isNew = false;
  
  private boolean isMixed = false;

  public List<SectorDetailCode> getCodes() {
    return codes;
  }

  public void setCodes(List<SectorDetailCode> codes) {
    this.codes = codes;
  }

  public int getNumCodes() {
    return numCodes;
  }

  public void setNumCodes(int numCodes) {
    this.numCodes = numCodes;
  }

  public List<CodeListView> getSectorCodeListItems() {
    return sectorCodeListItems;
  }

  public void setSectorCodeListItems(List<CodeListView> sectorCodeListItems) {
    this.sectorCodeListItems = sectorCodeListItems;
  }

  public String getSectorCode() {
    return sectorCode;
  }

  public void setSectorCode(String sectorCode) {
    this.sectorCode = sectorCode;
  }

  public String getSectorCodeDescription() {
    return sectorCodeDescription;
  }

  public void setSectorCodeDescription(String sectorCodeDescription) {
    this.sectorCodeDescription = sectorCodeDescription;
  }

  public String getSectorDetailCode() {
    return sectorDetailCode;
  }

  public void setSectorDetailCode(String sectorDetailCode) {
    this.sectorDetailCode = sectorDetailCode;
  }

  public String getSectorDetailCodeDescription() {
    return sectorDetailCodeDescription;
  }

  public void setSectorDetailCodeDescription(String sectorDetailCodeDescription) {
    this.sectorDetailCodeDescription = sectorDetailCodeDescription;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

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

  public boolean isSetFilterContext() {
    return isSetFilterContext;
  }

  public void setSetFilterContext(boolean isSetFilterContext) {
    this.isSetFilterContext = isSetFilterContext;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public boolean isMixed() {
    return isMixed;
  }

  public void setMixed(boolean isMixed) {
    this.isMixed = isMixed;
  }

}