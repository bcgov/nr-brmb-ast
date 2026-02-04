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
package ca.bc.gov.srm.farm.ui.struts.codes.generic;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.Code;

/**
 *
 * @author awilkinson
 */
public class CodesForm extends ValidatorForm {

  private static final long serialVersionUID = -161432187543579054L;

  private String codeTable;
  
  private List<Code> codes;
  private int numCodes;

  private String code;
  private String description;
  private Integer revisionCount;

  private boolean isNew = false;
  
  private boolean canCreate;
  private boolean canDelete;

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
   * Gets codeTable
   *
   * @return the codeTable
   */
  public String getCodeTable() {
    return codeTable;
  }

  /**
   * Sets codeTable
   *
   * @param pCodeTable the codeTable to set
   */
  public void setCodeTable(String pCodeTable) {
    codeTable = pCodeTable;
  }

  /**
   * Gets codes
   *
   * @return the codes
   */
  public List<Code> getCodes() {
    return codes;
  }

  /**
   * Sets codes
   *
   * @param pCodes the codes to set
   */
  public void setCodes(List<Code> pCodes) {
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

  public boolean isCanCreate() {
    return canCreate;
  }

  public void setCanCreate(boolean canCreate) {
    this.canCreate = canCreate;
  }

  public boolean isCanDelete() {
    return canDelete;
  }

  public void setCanDelete(boolean canDelete) {
    this.canDelete = canDelete;
  }

}
