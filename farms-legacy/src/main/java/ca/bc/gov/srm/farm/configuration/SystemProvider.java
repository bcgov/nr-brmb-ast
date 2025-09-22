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

import ca.bc.gov.srm.farm.exception.ConfigurationNotFoundException;


/**
 * SystemProvider.
 *
 * @author   dzwiers
 */
final class SystemProvider extends ConfigurationProvider {


  SystemProvider() {
    setInitialized(true);
  }


  @Override
  public void initialize(final Object resource) {
    // do nothing
  }


  @Override
  protected String getConfigurationValue(final String key)
    throws ConfigurationNotFoundException {

    if (!System.getProperties().containsKey(key)) {
      throw new ConfigurationNotFoundException();
    }

    String result = System.getProperty(key);

    return result;
  }

}
