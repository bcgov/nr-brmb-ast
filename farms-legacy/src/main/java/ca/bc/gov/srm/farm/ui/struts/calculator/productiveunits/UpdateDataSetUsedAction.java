/**
 *
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.JsonUtils;

public class UpdateDataSetUsedAction extends ProductiveUnitsViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());
  CalculatorService calculatorService = ServiceFactory.getCalculatorService();

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Update Data Set Used...");

    ProductiveUnitsForm form = (ProductiveUnitsForm) actionForm;
    Scenario scenario = getScenario(form);

    String dataSetUsed = form.getDataSetUsedRadio();
    if (dataSetUsed == null || dataSetUsed.isEmpty()) {
      throw new ServiceException("DataSetUsed not found");
    }

    calculatorService.updateScenarioParticipantDataSrcCode(scenario, dataSetUsed, getUserId());
    scenario = refreshScenario(form);

    populateForm(form, scenario, true);

    setReadOnlyFlag(request, form, scenario);
    
    ResponseObject responseObj = new ResponseObject();
    responseObj.setParticipantDataSrcCode(scenario.getParticipantDataSrcCode());
    responseObj.setRevisionCount(scenario.getRevisionCount());
    
    String responseJson = JsonUtils.getJsonObjectMapper().writeValueAsString(responseObj);
    IOUtils.writeJsonToResponse(response, responseJson);

    return null;
  }

}

class ResponseObject {
  String participantDataSrcCode;
  Integer revisionCount;

  public String getParticipantDataSrcCode() {
    return participantDataSrcCode;
  }

  public void setParticipantDataSrcCode(String participantDataSrcCode) {
    this.participantDataSrcCode = participantDataSrcCode;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }
}
