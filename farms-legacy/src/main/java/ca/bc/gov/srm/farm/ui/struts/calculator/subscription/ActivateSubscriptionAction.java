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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.SubscriptionService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;


/**
 * ActivateSubscriptionAction - special welcome action called 
 * from a link on the BCEID site.
 */
public class ActivateSubscriptionAction extends SecureAction {

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
  public ActionForward doExecute(final ActionMapping mapping,
    final ActionForm actionForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    SubscriptionService service = ServiceFactory.getSubscriptionService();
    ArrayList errorMessages = new ArrayList();

    int numActivated = service.activateSubscriptions(errorMessages);

    if (errorMessages.size() > 0) {

      //
      // The BCeID web service generated an error. Show it to the user.
      //
      String errorKey = MessageConstants.ERRORS_BCEID_WEB_SERVICE;
      ActionMessages errors = new ActionMessages();

      for (int ii = 0; ii < errorMessages.size(); ii++) {
        String msg = (String) errorMessages.get(ii);
        errors.add("", new ActionMessage(errorKey, msg));
      }

      saveErrors(request, errors);
    } else if (numActivated == 0) {

      //
      // If no subscriptions were updated, then they were probably invalidated,
      // so show the user an error message.
      //
      String errorKey = MessageConstants.ERRORS_NO_SUBCRIPTIONS_MATCH;
      ActionMessages errors = new ActionMessages();
      errors.add("", new ActionMessage(errorKey));
      saveErrors(request, errors);
    }

    return forward;
  }

}
