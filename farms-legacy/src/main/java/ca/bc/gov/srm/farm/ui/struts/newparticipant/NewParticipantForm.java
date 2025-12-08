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

import org.apache.struts.validator.ValidatorForm;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.util.JsonUtils;


/**
 * @author awilkinson
 */
public class NewParticipantForm extends ValidatorForm {

  private static final long serialVersionUID = 1;
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  private String newParticipantJson;
  private String metaDataJson;
  
  public NewParticipantFormData getParticipant() throws Exception {
    return jsonObjectMapper.readValue(newParticipantJson, NewParticipantFormData.class);
  }

  public String getNewParticipantJson() {
    return newParticipantJson;
  }

  public void setNewParticipantJson(String newParticipantJson) {
    this.newParticipantJson = newParticipantJson;
  }

  public String getMetaDataJson() {
    return metaDataJson;
  }

  public void setMetaDataJson(String metaDataJson) {
    this.metaDataJson = metaDataJson;
  }

}
