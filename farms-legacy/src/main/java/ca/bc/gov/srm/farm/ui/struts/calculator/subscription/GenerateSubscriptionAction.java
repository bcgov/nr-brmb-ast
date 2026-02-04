/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.subscription;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.GenerateSubscriptionResponse;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.SubscriptionService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * GenerateSubscriptionAction - used by screen 340.
 */
public class GenerateSubscriptionAction extends SubscriptionAction {

  /**
   * doExecute.
   *
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   *
   * @return  The return value
   *
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) 
  throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    SubscriptionService service = ServiceFactory.getSubscriptionService();
    SubscriptionForm form = (SubscriptionForm) actionForm;
    
    Scenario scenario = getScenario(form);
    Integer clientId = scenario.getClient().getClientId();
    Integer pin = scenario.getClient().getParticipantPin();
    
    GenerateSubscriptionResponse genResponse = 
    	service.generateSubscription(clientId, pin);

    if (genResponse.getErrorMessages().size() > 0) {
      //
      // The BCeID web service generated an error. Show it to the user.
      //
      List errorMessages = genResponse.getErrorMessages();
      String errorKey = MessageConstants.ERRORS_BCEID_WEB_SERVICE;
      ActionMessages errors = new ActionMessages();

      for (int ii = 0; ii < errorMessages.size(); ii++) {
        String msg = (String) errorMessages.get(ii);
        errors.add("", new ActionMessage(errorKey, msg));
      }

      saveErrors(request, errors);
    } else {
      //
      // A new farm_client_subscriptions entry has been created.
      //
      Integer clientSubscriptionId = genResponse.getClientSubscriptionId();

      // generate a PDF "letter" in a new browser.
      ReportService reportService = ServiceFactory.getReportService();
      String url = null; // TODO Remove subscriptions?
      form.setReportUrl(url);
    }
    
    // refresh the list of subscriptions
    getSubscriptionsAndUsers(request, form);

    return forward;
  }

}
