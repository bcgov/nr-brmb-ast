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

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.SubscriptionService;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;

/**
 * SubscriptionAction - used by screen 340.
 */
public abstract class SubscriptionAction extends CalculatorAction {
	
  /**
   * 
   * @param request request
   * @param form form
   * @throws Exception Exception
   */
  protected void getSubscriptionsAndUsers(
    final HttpServletRequest request, 
  	final SubscriptionForm form
  ) throws Exception {
  	SubscriptionService service = ServiceFactory.getSubscriptionService();
  	Integer pin = new Integer(form.getPin());
    
    List subscriptions = service.getSubscriptions(pin);
    request.setAttribute("subscriptions", subscriptions);
    
    List users = service.getAuthorizedUsers(pin);
    request.setAttribute("authorizedUsers", users);
  }

}
