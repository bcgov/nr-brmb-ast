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
package ca.bc.gov.srm.farm.chefs;

/**
 * @author awilkinson
 */
public class ChefsFormCredentials {

  private String formId; // username
  
  private String apiKey; // password

  public ChefsFormCredentials(String formId, String apiKey) {
    super();
    this.formId = formId;
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }
  
}
