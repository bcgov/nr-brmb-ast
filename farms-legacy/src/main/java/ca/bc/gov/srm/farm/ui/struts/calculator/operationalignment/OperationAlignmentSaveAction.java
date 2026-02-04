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
package ca.bc.gov.srm.farm.ui.struts.calculator.operationalignment;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;

/**
 * @author awilkinson
 * @created Feb 16, 2011
 */
public class OperationAlignmentSaveAction extends OperationAlignmentViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Scenario...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    OperationAlignmentForm form = (OperationAlignmentForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    final String errorNumeric = MessageConstants.ERRORS_OPERATION_NUMBER_NUMERIC;
    final String errorDuplicate = MessageConstants.ERRORS_OPERATION_NUMBER_DUPLICATE;
    final String errorNonexistent = MessageConstants.ERRORS_OPERATION_NUMBER_NONEXISTENT;
    final String errorMissing = MessageConstants.ERRORS_OPERATION_NUMBER_MISSING;

    Scenario scenario = getScenario(form);

    populateForm(form, scenario, false, request);
    
    checkReadOnly(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      List<FarmingOperation> updatedOperations = new ArrayList<>();
      
      List<Integer> requiredYears = getRequiredYears(scenario);
      Map<Integer, ReferenceScenario> yearScenarioMap = getYearScenarioMap(scenario);

      boolean canEditAllYears =
        canPerformAction(request, SecurityActionConstants.OPERATION_ALIGNMENT_EDIT_ALL_YEARS);

      for(Integer year : requiredYears) {
        
        if(scenario.getYear().equals(year) || canEditAllYears) {
          
          Map<Integer, FarmingOperation> opMap = new HashMap<>();
          ReferenceScenario rs = yearScenarioMap.get(year);
          if(rs != null) {
            for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
              Integer operationNumber = fo.getOperationNumber();
              opMap.put(operationNumber, fo);
            }
          }
          
          OpYearFormData opYearFormData = form.getOpYearFormData(year.toString());
          Map<String, String> scheduleOpNumMap = opYearFormData.getScheduleOpNumMap();
          Set<Integer> formOpNumSet = new HashSet<>();
          
          for(String schedule : scheduleOpNumMap.keySet()) {
            String opNumStr = scheduleOpNumMap.get(schedule);
            Integer opNum = null;
            boolean badValue = false;
      
            try {
              opNum = DataParseUtils.parseIntegerObject(opNumStr);
            } catch(ParseException pe) {
              errors.add("", new ActionMessage(errorNumeric, year.toString(), schedule));
              opYearFormData.setError(schedule, Boolean.TRUE);
              badValue = true;
            }
            
            if(opNum != null) {
              
              if(formOpNumSet.contains(opNum)) {
                errors.add("", new ActionMessage(errorDuplicate, year.toString(), opNum.toString()));
                for(String schedule2 : scheduleOpNumMap.keySet()) {
                  String opNumStr2 = scheduleOpNumMap.get(schedule2);
                  if(opNumStr.equals(opNumStr2)) {
                    opYearFormData.setError(schedule2, Boolean.TRUE);
                    badValue = true;
                  }
                }
              } else {
                formOpNumSet.add(opNum);
              }
              
      
              if(opMap.keySet().contains(opNum)) {
                FarmingOperation fo = opMap.get(opNum);
                if( ! badValue &&  ! fo.getSchedule().equals(schedule) ) {
                  FarmingOperation updatedFo = new FarmingOperation();
                  updatedFo.setFarmingOperationId(fo.getFarmingOperationId());
                  updatedFo.setOperationNumber(fo.getOperationNumber());
                  updatedFo.setSchedule(schedule);
                  updatedFo.setRevisionCount(fo.getRevisionCount());
                  updatedOperations.add(updatedFo);
                }
              } else {
                errors.add("",
                    new ActionMessage(errorNonexistent, year.toString(), schedule, opNum.toString()));
                opYearFormData.setError(schedule, Boolean.TRUE);
                badValue = true;
              }
            }
          }
          
          for(Integer opNum : opMap.keySet()) {
            if( ! formOpNumSet.contains(opNum) ) {
              errors.add("", new ActionMessage(errorMissing, opNum, year.toString()));
            }
          }
        }
      }

      if ( ! errors.isEmpty() ) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
  
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        
        try {
          calculatorService.updateOperationAlignment(scenario, updatedOperations, getUserId());
  
          scenario = refreshScenario(form);
          populateForm(form, scenario, true, request);
          
          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());

        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

}
