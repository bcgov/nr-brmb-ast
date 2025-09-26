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
package ca.bc.gov.srm.farm.ui.struts.newparticipant;

import java.util.List;

import ca.bc.gov.srm.farm.list.ListView;

/**
 * @author awilkinson
 */
public class NewParticipantFormMetaData {

  private List<ListView> municipalities;
  
  private List<ListView> accountingCodes;

  public List<ListView> getMunicipalities() {
    return municipalities;
  }

  public void setMunicipalities(List<ListView> municipalities) {
    this.municipalities = municipalities;
  }

  public List<ListView> getAccountingCodes() {
    return accountingCodes;
  }

  public void setAccountingCodes(List<ListView> accountingCodes) {
    this.accountingCodes = accountingCodes;
  }
  
}
