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
package ca.bc.gov.srm.farm.chefs.resource.submission;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

/**
 * @author awilkinson
 */
public class SubmissionResource<T> extends ChefsResource {

  private String state;
  
  private T data;

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
