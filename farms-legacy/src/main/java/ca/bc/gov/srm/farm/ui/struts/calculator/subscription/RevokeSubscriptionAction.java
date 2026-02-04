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


import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.SubscriptionService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * AccountRevokeSubscriptionAction - used by screen 340.
 */
public class RevokeSubscriptionAction extends SubscriptionAction {

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
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    SubscriptionService service = ServiceFactory.getSubscriptionService();
    SubscriptionForm form = (SubscriptionForm) actionForm;

    service.revokeSubscription(
    	form.getClientSubscriptionId(),
      form.getRevisionCount());
    
    getSubscriptionsAndUsers(request, form);

    return forward;
  }

}
