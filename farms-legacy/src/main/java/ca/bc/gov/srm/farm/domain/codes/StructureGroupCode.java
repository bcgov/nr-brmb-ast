/**
 * Copyright (c) 2012, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;


/**
 * @author hwang
 */
public class StructureGroupCode extends AbstractCode {

  private String code;
  private String rollupStructureGroupCode;
  private String rollupStructureGroupCodeDescription;

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the rollupStructureGroupCode
   */
  public String getRollupStructureGroupCode() {
    return rollupStructureGroupCode;
  }

  /**
   * @param rollupStructureGroupCode the rollupStructureGroupCode to set
   */
  public void setRollupStructureGroupCode(String rollupStructureGroupCode) {
    this.rollupStructureGroupCode = rollupStructureGroupCode;
  }

  /**
   * @return the rollupStructureGroupCodeDescription
   */
  public String getRollupStructureGroupCodeDescription() {
    return rollupStructureGroupCodeDescription;
  }

  /**
   * @param rollupStructureGroupCodeDescription the rollupStructureGroupCodeDescription to set
   */
  public void setRollupStructureGroupCodeDescription(String rollupStructureGroupCodeDescription) {
    this.rollupStructureGroupCodeDescription = rollupStructureGroupCodeDescription;
  }
}
