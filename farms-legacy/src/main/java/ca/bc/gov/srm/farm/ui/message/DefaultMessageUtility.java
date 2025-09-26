/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.message;

import ca.bc.gov.srm.farm.message.MessageProvider;
import ca.bc.gov.srm.farm.message.MessageUtility;
import ca.bc.gov.srm.farm.message.PropertiesProvider;


/**
 * DefaultMessageUtility.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
class DefaultMessageUtility extends MessageUtility {

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  @Override
  public void initialize(final Object resource) {

    // the default constructor for MessageUtility should call initialize...
    setInitialized(false);
    getProviders().clear();

    MessageProvider ui = new PropertiesProvider(
        "config/applicationResources.properties");
    getProviders().put("ui", ui);

    setInitialized(true);
  }

}
