/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.security;

import java.util.Properties;

/**
 * WebADESecurityUtility.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
class WebADESecurityUtility extends SecurityUtility {

  /** Creates a new WebADESecurityUtility object. */
  WebADESecurityUtility() {
    initialize(null);
  }

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);

    //clear out any existing providers...
    getProviders().clear();

    SecurityRuleProvider rp = new PropertiesRuleProvider(
        new Properties());
    SecurityProvider sp = new WebADESecurityProvider(rp);

    getProviders().put("webade", sp);

    setInitialized(true);
  }

}
