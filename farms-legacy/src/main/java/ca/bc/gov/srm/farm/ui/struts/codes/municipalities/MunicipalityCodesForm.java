/**
 * Copyright (c) 2011, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.municipalities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 *
 * @author awilkinson
 */
public class MunicipalityCodesForm extends ValidatorForm {

  private static final long serialVersionUID = -2920496701577082143L;

  private List codes;
  private int numCodes;

  private String code;
  private String description;
  private Integer revisionCount;

  private boolean isNew = false;
  
  /* List<Code> */
  private List regionCodes;
  private Map regionCodeSelections = new HashMap();

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
  }

  /**
   * Gets codes
   *
   * @return the codes
   */
  public List getCodes() {
    return codes;
  }

  /**
   * Sets codes
   *
   * @param pCodes the codes to set
   */
  public void setCodes(List pCodes) {
    codes = pCodes;
  }

  /**
   * Gets numCodes
   *
   * @return the numCodes
   */
  public int getNumCodes() {
    return numCodes;
  }

  /**
   * Sets numCodes
   *
   * @param pNumCodes the numCodes to set
   */
  public void setNumCodes(int pNumCodes) {
    numCodes = pNumCodes;
  }

  /**
   * Gets code
   *
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets code
   *
   * @param pCode the code to set
   */
  public void setCode(String pCode) {
    code = pCode;
  }

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
   * Gets isNew
   *
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * Sets isNew
   *
   * @param pIsNew the isNew to set
   */
  public void setNew(boolean pIsNew) {
    isNew = pIsNew;
  }

  /**
   * Gets regionCodes
   *
   * @return the regionCodes
   */
  public List getRegionCodes() {
    return regionCodes;
  }

  /**
   * Sets regionCodes
   *
   * @param pRegionCodes the regionCodes to set
   */
  public void setRegionCodes(List pRegionCodes) {
    regionCodes = pRegionCodes;
  }

  /**
   * Gets regionCodeSelections
   *
   * @return the regionCodeSelections
   */
  public Map getRegionCodeSelections() {
    return regionCodeSelections;
  }

  /**
   * Sets regionCodeSelections
   *
   * @param pRegionCodeSelections the regionCodeSelections to set
   */
  public void setRegionCodeSelections(Map pRegionCodeSelections) {
    regionCodeSelections = pRegionCodeSelections;
  }

  /**
   * @param regionCode regionCode
   * @return selected
   */
  public Boolean getRegionCodeSelection(String regionCode) {
    return (Boolean) regionCodeSelections.get(regionCode);
  }
  
  /**
   * @param regionCode regionCode
   * @param selected selected
   */
  public void setRegionCodeSelection(String regionCode, Boolean selected) {
    regionCodeSelections.put(regionCode, selected);
  }
}
