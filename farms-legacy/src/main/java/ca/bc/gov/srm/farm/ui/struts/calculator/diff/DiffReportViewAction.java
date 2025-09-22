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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.diff.FarmingOperationDiff;
import ca.bc.gov.srm.farm.domain.diff.ProgramYearVersionDiff;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.DiffReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 * @created Mar 22, 2011
 */
public class DiffReportViewAction extends CalculatorAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Diff Report...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    DiffReportForm form = (DiffReportForm) actionForm;
    Scenario scenario = getScenario(form);

    setReadOnlyFlag(request, form, scenario);

    generateDiffReport(form, scenario);
    
    return forward;
  }

  protected void generateDiffReport(DiffReportForm form, Scenario scenario) throws Exception {
    Integer diffYear = form.getDiffYear();
    ScenarioMetaData latestMSc = ScenarioUtils.getLatestBaseScenario(scenario, diffYear);
    Integer latestScNum = latestMSc.getScenarioNumber();
    
    CalculatorService calcService = ServiceFactory.getCalculatorService();
    Scenario latestScenario =
      calcService.loadScenarioWithoutHistory(form.getPin(), diffYear.intValue(), latestScNum);
    FarmingYear latestFarmingYear = latestScenario.getFarmingYear();
    
    FarmingYear curFarmingYear = scenario.getReferenceScenarioByYear(diffYear).getFarmingYear();
    
    DiffReportService diffService = ServiceFactory.getDiffReportService();
    
    ProgramYearVersionDiff pyvDiff = diffService.generateDiffReport(curFarmingYear, latestFarmingYear);
    
    DiffReportFormData formData = new DiffReportFormData();
    formData.setReadOnly(form.isReadOnly());
    
    formData.setPyvLocallyUpdated(pyvDiff.getIsLocallyUpdated());
    formData.setPyvAcceptNewData(pyvDiff.getIsLocallyUpdated());
    formData.setHasLocallyGeneratedOperations(pyvDiff.getHasLocallyGeneratedOperations());
    formData.setNewCRAMissingOperations(pyvDiff.getIsNewCRAMissingOperations());
    
    if(pyvDiff.getFieldDiffs() != null && ! pyvDiff.getFieldDiffs().isEmpty()) {
      formData.setPyvHasDifferences(true);
    } else {
      formData.setPyvHasDifferences(false);
    }
    
    for(FarmingOperationDiff foDiff : pyvDiff.getFarmingOperationDiffs()) {
      if(foDiff.getIsLocallyUpdated()) {
        int opNum = foDiff.getOperationNumber().intValue();
        formData.setOperationLocallyUpdated(opNum, String.valueOf(true));
        formData.setOperationAcceptNewData(opNum, String.valueOf(true));
        boolean opHasFieldDifferences = foDiff.getIsHasFieldDifferences();
        formData.setOperationHasFieldDifferences(opNum, String.valueOf(opHasFieldDifferences));
      }
    }
    
    formData.setScenarioRevisionCount(scenario.getRevisionCount());
    formData.setNewPyvNumber(latestFarmingYear.getProgramYearVersionNumber());
    
    String formDataJson = jsonObjectMapper.writeValueAsString(formData);
    String pyvDiffJson = jsonObjectMapper.writeValueAsString(pyvDiff);
    
    form.setFormDataJson(formDataJson);
    form.setPyvDiffJson(pyvDiffJson);
  }

}
