/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.inbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ListServiceConstants;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.domain.CalculatorInboxItem;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.UiUtils;

/**
 * @author awilkinson
 * @created Dec 15, 2010
 */
public class CalculatorInboxViewAction extends CalculatorAction {
  
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

    logger.debug("Viewing Calculator Inbox...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorInboxForm form = (CalculatorInboxForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(CalculatorInboxForm form) throws Exception {
    
    syncInboxContextWithForm(form);
    
    int inboxYear = form.getInboxYear();
    String searchType = form.getInboxSearchType();

    List<String> scenarioStateCodes = new ArrayList<>();
    if(searchType.equals(ListServiceConstants.CALCULATOR_INBOX_SEARCH_TYPE_READY)) {
      scenarioStateCodes.add(ScenarioStateCodes.RECEIVED);
    } else {
      scenarioStateCodes.add(ScenarioStateCodes.IN_PROGRESS);
      scenarioStateCodes.add(ScenarioStateCodes.VERIFIED);
      scenarioStateCodes.add(ScenarioStateCodes.CLOSED);
    }
    
    String user = CurrentUser.getUser().getUserId();
    String userGuid = CurrentUser.getUser().getGuid();

    CalculatorService service = ServiceFactory.getCalculatorService();
    List<CalculatorInboxItem> inboxItems = service.readInboxItems(
        searchType,
        new Integer(inboxYear),
        userGuid,
        scenarioStateCodes);
    
    form.setInboxItems(inboxItems);
    form.setUser(user);
    form.setUserGuid(userGuid);
    
    ListView[] searchTypes = (ListView[]) CacheFactory
      .getApplicationCache().getItem(ListService.SERVER_LIST + ListService.CALCULATOR_INBOX_SEARCH_TYPE);
    List<ListView> searchTypeList = Arrays.asList(searchTypes);
    form.setAssignedToSelectOptions(searchTypeList);
    
    setProgramYearSelectOptions(form);
  }

  protected void setProgramYearSelectOptions(CalculatorInboxForm form) {
    List<ListView> options = UiUtils.getAdminYearsSelectOptions();
    form.setProgramYearSelectOptions(options);
  }

}
