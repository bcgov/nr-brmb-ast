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

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.util.JsonUtils;


/**
 * @author awilkinson
 */
public class PartnersForm extends CalculatorForm {

  private static final long serialVersionUID = 1;
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  private String formDataJson;
  private String metaDataJson;
  
  public PartnersFormData getFormData() throws Exception {
    return jsonObjectMapper.readValue(formDataJson, PartnersFormData.class);
  }

  public String getFormDataJson() {
    return formDataJson;
  }

  public void setFormDataJson(String formDataJson) {
    this.formDataJson = formDataJson;
  }

  public String getMetaDataJson() {
    return metaDataJson;
  }

  public void setMetaDataJson(String metaDataJson) {
    this.metaDataJson = metaDataJson;
  }

}
