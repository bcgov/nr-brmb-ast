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
import ca.bc.gov.srm.farm.exception.ProviderException;


/**
 * SecurityRuleProvider.
 *
 * @author   $Author: dzwiers $
 * @version  $Revision: 256 $
 */
abstract class SecurityRuleProvider extends Provider {

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

}
