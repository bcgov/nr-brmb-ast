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
package ca.bc.gov.srm.farm.ui.struts.calculator.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.service.SearchService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.AccountSearchResult;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class CalculatorSearchAction extends CalculatorAction {

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
  protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorSearchForm form = (CalculatorSearchForm) actionForm;
    ActionMessages errors = validateForm(mapping, form, request);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String searchString = form.getSearchString();
      String searchType = form.getSearchType();
      logger.debug("searchType = [" + searchType + "]");
      logger.debug("searchString = [" + searchString + "]");
  
      String pin = null;
      String name = null;
      String searchStringTrim = searchString.trim();

      if(searchType.equals("pin")) {
        pin = searchStringTrim;
      } else if(searchType.equals("name")) {
        name = searchStringTrim;
      }

      SearchService service = ServiceFactory.getSearchService();
      List<AccountSearchResult> results = service.searchAccounts(
          pin,
          name,
          null, // not searching on city
          null, // not searching on postal code
          null); // not searching on user ID;
      form.setSearchResults(results);
    }

    return forward;
  }


  /**
   * Perform additional validation on top of the struts validator.
   *
   * @param   mapping  WebADEActionMapping
   * @param   form     form to validate
   * @param   request  HttpServletRequest
   *
   * @return  ActionMessages messages
   */
  private ActionMessages validateForm(final ActionMapping mapping,
    final CalculatorSearchForm form, final HttpServletRequest request) {
    ActionMessages errors = form.validate(mapping, request);

    if (errors.isEmpty()) {

      String searchType = form.getSearchType();
      if(searchType.equals("pin") || searchType.equals("sin")) {
        try {
          Integer.parseInt(form.getSearchString().trim());
        } catch (Exception e) {
          String message = null;
          if(searchType.equals("pin")) {
            message = MessageConstants.ERRORS_PIN_NUMBERIC;
          } else if(searchType.equals("sin")) {
            message = MessageConstants.ERRORS_SIN_NUMBERIC;
          }
          errors.add("", new ActionMessage(message));
        }
      }
    }

    return errors;
  }

}
