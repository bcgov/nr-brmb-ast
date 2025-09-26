/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.combined;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.CombinedFarmClient;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;

/**
 * Action for screen 920
 */
public class CombinedFarmViewAction extends CalculatorAction {

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

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CombinedFarmForm form = (CombinedFarmForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, request, scenario);
    
    return forward;
  }

  /**
   * Fill the form fields
   * @param form The form object to fill.
   * @param request request
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  protected void populateForm(CombinedFarmForm form,
      HttpServletRequest request, Scenario scenario)
  throws Exception {
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    
    CalculatorService calcService = ServiceFactory.getCalculatorService();
    
    Map<Integer, List<Integer>> scNumMap = calcService.getCombinedFarmInProgressScenarioNumbers(
        scenario.getClient().getParticipantPin(),
        scenario.getCombinedFarmNumber());
    List<CombinedFarmClient> clients = scenario.getCombinedFarmClients();
    
    if(clients != null) {
      for(CombinedFarmClient client : clients) {
        Integer curPin = client.getParticipantPin();
        Integer curScNum = client.getScenarioNumber();
        
        form.setCombinedScenarioNumber(curPin.toString(), curScNum.toString());
        form.setCombinedScenarioId(curPin.toString(), client.getScenarioId().toString());
        
        List<Integer> scNumbers = scNumMap.get(curPin);
        if(scNumbers == null) {
          scNumbers = new ArrayList<>();
        }
        if(!scNumbers.contains(curScNum)) {
          scNumbers.add(curScNum);
        }
        
        CodeListView[] codeList = new CodeListView[scNumbers.size()];
        int index = 0;
        for(Integer scNum : scNumbers) {
          String scNumStr = scNum.toString();
          codeList[index++] = new CodeListView(scNumStr, scNumStr);
        }
        
        request.setAttribute("request.list.scenario.numbers." + curPin, codeList);
      }
    }
  }

}
