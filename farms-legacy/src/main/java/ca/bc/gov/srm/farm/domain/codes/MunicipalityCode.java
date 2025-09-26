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

import java.util.List;


/**
 * @author awilkinson
 */
public class MunicipalityCode extends AbstractCode {

  private String code;
  
  private List<String> regionalOfficeCodes;


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
   * Gets regionalOfficeCodes
   *
   * @return the regionalOfficeCodes
   */
  public List<String> getRegionalOfficeCodes() {
    return regionalOfficeCodes;
  }

  /**
   * Sets regionalOfficeCodes
   *
   * @param pRegionalOfficeCodes the regionalOfficeCodes to set
   */
  public void setRegionalOfficeCodes(List<String> pRegionalOfficeCodes) {
    regionalOfficeCodes = pRegionalOfficeCodes;
  }
  
}
