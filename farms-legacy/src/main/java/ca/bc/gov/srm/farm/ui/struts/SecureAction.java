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
package ca.bc.gov.srm.farm.ui.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.util.FarmSecurityUtils;
import ca.bc.gov.webade.WebADEException;


/**
 * SecureAction.
 */
public abstract class SecureAction extends Action {

  /** log. */
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * execute.
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
  public ActionForward execute(final ActionMapping mapping,
    final ActionForm actionForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    SecureActionMapping secureMapping = (SecureActionMapping) mapping;
    String secureAction = secureMapping.getSecureAction();

    checkAuthorization(request, secureAction);

    BusinessAction ba = new BusinessAction(secureAction);
    CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION,
      ba);

    try {
      ActionForward forward = doExecute(mapping, actionForm, request, response);
      return forward;

    } catch(Exception e) {
      logger.error("Error occurred performing action requested by user: " +
                   CurrentUser.getUser().getAccountName());
      throw e;
    }
  }


  /**
   * @param   mapping   mapping
   * @param   actForm   actForm
   * @param   request   request
   * @param   response  response
   * @return  ActionForward
   * @throws  Exception  On Exception
   */
  protected abstract ActionForward doExecute(final ActionMapping mapping,
    final ActionForm actForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception;


  /**
   * checkAuthorization.
   *
   * @param   request  get request
   * @param   action   get action
   *
   * @throws  WebADEException  On Exception
   */
  protected void checkAuthorization(
  	final HttpServletRequest request,
    final String ...actions)
  throws WebADEException {
    
    if(actions.length == 0) {
      throw new IllegalArgumentException("actions must have at lease one element");
    }
    
    boolean authorized = false;
    for(String action : actions) {
      boolean authorizedAction = canPerformAction(request, action);
      if(authorizedAction) {
        authorized = true;
      }
    }

    if (!authorized) {
      String message;
      if(actions.length == 1) {
        message = "The user is not authorized to perform action: " + actions[0];
      } else {
        message = "The user is not authorized to perform actions: " + actions;
      }
      logger.error(message);
      throw new SecurityException(
        message);
    }
  }
  
  /**
   * 
   * @param request request
   * @param webadeAction webadeAction
   * @return true is allowed to do action
   * @throws WebADEException on error
   */
  protected boolean canPerformAction(
  		final HttpServletRequest request, 
  		final String webadeAction) 
  throws WebADEException {
    return FarmSecurityUtils.canPerformAction(request, webadeAction);
  }


  /**
   * @return The user account name for the currently logged in user.
   */
  protected String getUserId() {
    return CurrentUser.getUser().getUserId();
  }


  /**
   * @return The user account email for the currently logged in user.
   */
  protected String getUserEmail() {
    return CurrentUser.getUser().getEmailAddress();
  }


  /**
   * @return The user account name for the currently logged in user.
   */
  protected String getUserAccountName() {
    return CurrentUser.getUser().getAccountName();
  }

}
