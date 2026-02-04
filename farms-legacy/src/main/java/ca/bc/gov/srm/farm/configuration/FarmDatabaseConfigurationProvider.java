/**
 *
 * Copyright (c) 2019,
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

import ca.bc.gov.srm.farm.exception.ConfigurationNotFoundException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ConfigurationService;
import ca.bc.gov.srm.farm.service.ServiceFactory;


/**
 * @author   awilkinson
 */
final class FarmDatabaseConfigurationProvider extends ConfigurationProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private ConfigurationService configurationService;
  
  FarmDatabaseConfigurationProvider() {
    initialize(null);
    setInitialized(true);
  }


  @Override
  public void initialize(final Object resource) {
    configurationService = ServiceFactory.getConfigurationService();
  }


  @Override
  protected String getConfigurationValue(final String key)
    throws ConfigurationNotFoundException {

    Map<String, String> configurationParameters = null;
    try {
      configurationParameters = configurationService.getConfigurationParameters();
    } catch (ServiceException e) {
      logger.error("Failed to load Farm configuration parameters", e);
      throw new ConfigurationNotFoundException();
    }
    if (!configurationParameters.containsKey(key)) {
      throw new ConfigurationNotFoundException();
    }

    return configurationParameters.get(key);
  }

}
