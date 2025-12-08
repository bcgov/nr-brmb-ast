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
import ca.bc.gov.srm.farm.util.PropertyLoader;

import java.util.Properties;


/**
 * PropertiesProvider.
 *
 * @author   $author$
 * @version  $Revision: 2145 $, $Date: 2013-04-11 15:06:51 -0700 (Thu, 11 Apr 2013) $
 */
final class PropertiesProvider extends ConfigurationProvider {


  /** DOCUMENT ME! */
  private Properties properties = null;


  /**
   * Creates a new PropertiesProvider object.
   *
   * @param  value  Input Parameter
   */
  PropertiesProvider(final Properties value) {
    initialize(value);
  }


  /**
   * Creates a new PropertiesProvider object.
   *
   * @param  path  Input Parameter
   */
  PropertiesProvider(final String path) {
    initialize(path);
  }


  /**
   * initialize.
   *
   * @param  resource  resource
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);

    if (resource == null) {
      throw new IllegalArgumentException("resource cannot be null.");
    }

    if (!((resource instanceof Properties) || (resource instanceof String))) {
      throw new IllegalArgumentException(
        "resource is expected to be Properties or String, not "
        + resource.getClass().getName());
    }

    if (resource instanceof Properties) {
      properties = (Properties) resource;
    } else if (resource instanceof String) {
      properties = PropertyLoader.loadProperties((String) resource);
    }

    setInitialized(properties != null);
  }


  /**
   * getConfigurationValue.
   *
   * @param   key  get key
   *
   * @return  The return value
   *
   * @throws  ConfigurationNotFoundException  On Exception
   */
  @Override
  protected String getConfigurationValue(final String key)
    throws ConfigurationNotFoundException {

    if (!properties.containsKey(key)) {
      throw new ConfigurationNotFoundException();
    }

    String result = properties.getProperty(key);

    return result;
  }

}
