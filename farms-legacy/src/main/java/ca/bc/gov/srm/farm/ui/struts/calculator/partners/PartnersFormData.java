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
package ca.bc.gov.srm.farm.ui.struts.calculator.partners;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PartnersFormData {

  private List<PartnerFormData> partners;

  public List<PartnerFormData> getPartners() {
    return partners;
  }

  public void setPartners(List<PartnerFormData> partners) {
    this.partners = partners;
  }

}
