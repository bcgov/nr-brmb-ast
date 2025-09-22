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
package ca.bc.gov.srm.farm.configuration;

import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Application;


/**
 * WebADEConfigurationUtility.
 *
 * @author   $author$
 * @version  $Revision: 5662 $, $Date: 2024-11-22 16:59:09 -0800 (Fri, 22 Nov 2024) $
 */
class WebADEConfigurationUtility extends ConfigurationUtility {


  /** Creates a new WebADEConfigurationUtility object. */
  WebADEConfigurationUtility() {
    initialize(null);
  }


  /**
   * getApplicationCode.
   *
   * @return  The return value
   */
  public String getApplicationCode() {
    checkInitialized();

    Application app = WebADERequest.getInstance().getApplication();

    return app.getApplicationCode();
  }


  /**
   * getApplicationVersion.
   *
   * @return  The return value
   */
  public String getApplicationVersion() {
    checkInitialized();

    Application app = WebADERequest.getInstance().getApplication();

    return app.getApplicationEnvironment();
  }


  @Override
  public String getEnvironment() {
    checkInitialized();

    Application app = WebADERequest.getInstance().getApplication();

    return app.getApplicationEnvironment();
  }


  /**
   * getServerName.
   *
   * @return  The return value
   */
  public String getServerName() {
    return null;
  }


  /**
   * initialize.
   *
   * @param  resource  resource
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);

    // clear out any existing providers...
    getProviders().clear();

    ConfigurationProvider sys = new SystemProvider();
    ConfigurationProvider webade = new WebADEConfigurationProvider();

    getProviders().put("sys", sys);
    getProviders().put("webade", webade);

    setInitialized(true);
  }

}
