/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.resource;

import java.math.BigDecimal;

/**
 * @author awilkinson
 */
public class CrmCoreConfigurationResource extends CrmResource {
  
  private BigDecimal vsi_triagepaymentthreshold;

  public BigDecimal getVsi_triagepaymentthreshold() {
    return vsi_triagepaymentthreshold;
  }

  public void setVsi_triagepaymentthreshold(BigDecimal vsi_triagepaymentthreshold) {
    this.vsi_triagepaymentthreshold = vsi_triagepaymentthreshold;
  }

  @Override
  public String toString() {
    return "CrmCoreConfigurationResource [vsi_triagepaymentthreshold=" + vsi_triagepaymentthreshold + "]";
  }
  
}
