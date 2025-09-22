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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Feb 16, 2011
 */
public class UpdateToLatestPyVersionAction extends DiffReportViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());


  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Updating scenario to latest program year version...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    DiffReportForm form = (DiffReportForm) actionForm;
    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    DiffReportFormData formData = form.getFormData();
    
    if(scenario.getRevisionCount().equals(formData.getScenarioRevisionCount())) {
      ActionMessages errors = checkErrors(form, mapping, request);
      if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
        generateDiffReport(form, scenario);
      } else {
        List<Integer> opNumsKeepOldData = new ArrayList<>();
        Map<Integer, Boolean> operationAcceptNewData = formData.getOperationAcceptNewData();
        for(Integer opNum : operationAcceptNewData.keySet()) {
          Boolean opAccept = operationAcceptNewData.get(opNum);
          if(opAccept != null && ! opAccept) {
            opNumsKeepOldData.add(opNum);
          }
        }
        
        Integer diffYear = form.getDiffYear();
        Integer newPyvNumber = formData.getNewPyvNumber();
        Boolean pyvKeepOldData;
        if(formData.getPyvHasDifferences() && formData.getPyvLocallyUpdated()) {
          pyvKeepOldData = !formData.getPyvAcceptNewData();
        } else {
          pyvKeepOldData = false;
        }
        
        Integer newScenarioNumber = calculatorService.updateScenarioPyVersion(
            scenario, diffYear, newPyvNumber, pyvKeepOldData, opNumsKeepOldData, getUserId());
        if(scenario.getYear().equals(diffYear)) {
          form.setScenarioNumber(newScenarioNumber);
        }
        scenario = refreshScenario(form);
        
        form.setFormDataJson(null);
        form.setPyvDiffJson(null);
        
        BenefitService benefitService = ServiceFactory.getBenefitService();
        // ignore error messages returned
        benefitService.calculateBenefit(scenario, getUserId());
      }
    } else {
      ActionMessages errorMessages = new ActionMessages();
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_SCENARIO_INVALID_REVISION_COUNT_REFRESHED));
      saveErrors(request, errorMessages);
      forward = mapping.findForward(ActionConstants.FAILURE);

      scenario = refreshScenario(form);
      generateDiffReport(form, scenario);
    }

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }


  private ActionMessages checkErrors(
      DiffReportForm form,
      ActionMapping mapping,
      HttpServletRequest request) throws Exception {
    ActionMessages errors = form.validate(mapping, request);
    
    DiffReportFormData formData = form.getFormData();
    
    boolean pyvHasDifferences = formData.getPyvHasDifferences();
    boolean pyvLocallyUpdated = formData.getPyvLocallyUpdated();
    
    if(pyvHasDifferences && pyvLocallyUpdated && formData.getPyvAcceptNewData() == null) {
      String errorKey = MessageConstants.ERRORS_805_PROGRAM_YEAR_DETAIL_ACCEPT_NEW_DATA;
      errors.add("", new ActionMessage(errorKey));
    }
    
    Map<Integer, Boolean> operationHasDifferences = formData.getOperationHasFieldDifferences();
    Map<Integer, Boolean> operationLocallyUpdated = formData.getOperationLocallyUpdated();
    Map<Integer, Boolean> operationAcceptNewData = formData.getOperationAcceptNewData();
    
    for(Integer opNum : operationLocallyUpdated.keySet()) {
      Boolean opHasFieldDifferences = operationHasDifferences.get(opNum);
      Boolean opLocallyUpdated = operationLocallyUpdated.get(opNum);
      if(opHasFieldDifferences && opLocallyUpdated) {
        if(operationAcceptNewData.get(opNum) == null) {
          String errorKey = MessageConstants.ERRORS_805_OPERATION_ACCEPT_NEW_DATA;
          errors.add("", new ActionMessage(errorKey, opNum.toString()));
        }
      }
    }
    
    return errors;
  }

}
