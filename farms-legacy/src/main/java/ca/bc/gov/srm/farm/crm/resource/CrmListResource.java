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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author awilkinson
 */
public class CrmListResource<T extends CrmResource> extends CrmResource {

  @JsonProperty("value")
  private List<T> list;

  @JsonProperty("@odata.context")
  private String odataContext;

  public List<T> getList() {
    return list;
  }

  public void setList(List<T> list) {
    this.list = list;
  }

  public String getOdataContext() {
    return odataContext;
  }

  public void setOdataContext(String odataContext) {
    this.odataContext = odataContext;
  }
  
}
