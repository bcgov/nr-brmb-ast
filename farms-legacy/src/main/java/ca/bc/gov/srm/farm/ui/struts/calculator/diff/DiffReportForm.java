/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.diff;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.diff.ProgramYearVersionDiff;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * @author awilkinson
 * @created Mar 22, 2011
 */
public class DiffReportForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645502968765405790L;

  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  private String formDataJson;
  private String pyvDiffJson;
  
  private Integer diffYear;
  
  public DiffReportFormData getFormData() throws Exception {
    DiffReportFormData result = null;
    if(formDataJson != null) {
      result = jsonObjectMapper.readValue(formDataJson, DiffReportFormData.class);
    }
    return result;
  }
  
  public ProgramYearVersionDiff getPyvDiff() throws Exception {
    ProgramYearVersionDiff result = null;
    if(formDataJson != null) {
      result = jsonObjectMapper.readValue(pyvDiffJson, ProgramYearVersionDiff.class);
    }
    return result;
  }

  public String getFormDataJson() {
    return formDataJson;
  }

  public void setFormDataJson(String formDataJson) {
    this.formDataJson = formDataJson;
  }

  public String getPyvDiffJson() {
    return pyvDiffJson;
  }

  public void setPyvDiffJson(String pyvDiffJson) {
    this.pyvDiffJson = pyvDiffJson;
  }

  public Integer getDiffYear() {
    return diffYear;
  }

  public void setDiffYear(Integer diffYear) {
    this.diffYear = diffYear;
  }

}
