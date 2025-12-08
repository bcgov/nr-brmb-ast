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
package ca.bc.gov.srm.farm.message;

import ca.bc.gov.srm.farm.exception.MessageNotFoundException;
import ca.bc.gov.srm.farm.util.PropertyLoader;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;
import java.util.Properties;


/**
 * PropertiesProvider.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public final class PropertiesProvider extends MessageProvider {

  /** patterns. */
  private Properties patterns = null;

  /**
   * Creates a new PropertiesProvider object.
   *
   * @param  properties  Input parameter to initialize class.
   */
  public PropertiesProvider(final Properties properties) {
    initialize(properties);
  }

  /**
   * Creates a new PropertiesProvider object.
   *
   * @param  path  Input parameter to initialize class.
   */
  public PropertiesProvider(final String path) {
    initialize(path);
  }

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
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
      patterns = (Properties) resource;
    } else if (resource instanceof String) {
      patterns = PropertyLoader.loadProperties((String) resource);
    }

    setInitialized(patterns != null);
  }

  /**
   * getPattern.
   *
   * @param   locale  Input parameter.
   * @param   key     Input parameter.
   *
   * @return  The return value.
   *
   * @throws  MessageNotFoundException  On exception.
   */
  @Override
  protected String getPattern(final Locale locale, final String key)
    throws MessageNotFoundException {

    //try with locale first...
    String localeKey = locale + "." + key;
    String result = patterns.getProperty(localeKey);

    if (StringUtils.isBlank(result)) {
      result = patterns.getProperty(key);
    }

    if (StringUtils.isBlank(result)) {
      throw new MessageNotFoundException();
    }

    return result;
  }

}
