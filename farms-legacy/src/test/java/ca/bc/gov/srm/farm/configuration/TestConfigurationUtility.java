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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.TestUtils;
import ca.bc.gov.webade.WebADEException;

final class TestConfigurationUtility extends ConfigurationUtility {

  private Logger logger = LoggerFactory.getLogger(getClass());

  public TestConfigurationUtility() {
    initialize(null);
  }

  /**
   * initialize.
   *
   * @param  resource  resource
   */
  @Override
  public void initialize(final Object resource) {

    setInitialized(false);
    
    try {
      TestUtils.loadWebADEApplication();
    } catch (WebADEException e) {
      logger.error("Failed to load WebADEApplication", e);
      throw new RuntimeException("Failed to load WebADEApplication");
    }
    
    Map<String, ConfigurationProvider> providers = getProviders();

    // clear out any existing providers...
    providers.clear();

    ConfigurationProvider sys = new SystemProvider();
    providers.put("sys", sys);

    ConfigurationProvider messages = new PropertiesProvider("config/messages.properties");
    providers.put("messages", messages);
    
    
    ConfigurationProvider ar = new PropertiesProvider("config/applicationResources.properties");
    providers.put("ar", ar);
    
    ConfigurationProvider test = new PropertiesProvider("test.properties");
    providers.put("test", test);

    ConfigurationProvider webade = new WebADEConfigurationProvider();
    getProviders().put("webade", webade);
    
    ConfigurationProvider farmDatabase = new FarmDatabaseConfigurationProvider();
    getProviders().put("farmDatabase", farmDatabase);

    setInitialized(true);
  }

  @Override
  public String getEnvironment() {
    return "DEV";
  }

}
