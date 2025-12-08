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
package ca.bc.gov.srm.farm.security;

import ca.bc.gov.srm.farm.Provider;
import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;


/**
 * SecurityProvider.
 *
 * @author   $Author: dzwiers $
 * @version  $Revision: 256 $
 */
abstract class SecurityProvider extends Provider {

  /**
   * canPerformAction.
   *
   * @param   businessAction  The parameter value.
   * @param   user            The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract boolean canPerformAction(BusinessAction businessAction,
    User user) throws ProviderException;

  /**
   * canPerformAction.
   *
   * @param   businessActionName  The parameter value.
   * @param   user                The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract boolean canPerformAction(String businessActionName, User user)
    throws ProviderException;

  /**
   * getBusinessRoles.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract BusinessRole[] getBusinessRoles() throws ProviderException;

  /**
   * getBusinessRolesForUser.
   *
   * @param   user  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract BusinessRole[] getBusinessRolesForUser(User user)
    throws ProviderException;

  /**
   * getSecurityRule.
   *
   * @param   key  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract SecurityRule getSecurityRule(String key)
    throws ProviderException;

  /**
   * getSecurityRule.
   *
   * @param   businessAction  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract SecurityRule getSecurityRule(BusinessAction businessAction)
    throws ProviderException;

  /**
   * isInRole.
   *
   * @param   businessRole  Input parameter.
   * @param   user          Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract boolean isInRole(BusinessRole businessRole, User user)
    throws ProviderException;

  /**
   * isInRole.
   *
   * @param   businessRoleName  Input parameter.
   * @param   user              Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public abstract boolean isInRole(String businessRoleName, User user)
    throws ProviderException;

}
