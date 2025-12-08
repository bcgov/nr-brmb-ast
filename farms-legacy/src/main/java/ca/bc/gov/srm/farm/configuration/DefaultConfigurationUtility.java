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


final class DefaultConfigurationUtility extends WebADEConfigurationUtility {


  /**
   * initialize.
   *
   * @param  resource  resource
   */
  @Override
  public void initialize(final Object resource) {
    super.initialize(resource);
    setInitialized(super.isInitialized());

    ConfigurationProvider messages = new PropertiesProvider("config/messages.properties");
    getProviders().put("messages", messages);
    
    
    ConfigurationProvider ar = new PropertiesProvider("config/applicationResources.properties");
    getProviders().put("ar", ar);
    
    ConfigurationProvider farmDatabase = new FarmDatabaseConfigurationProvider();
    getProviders().put("farmDatabase", farmDatabase);

    setInitialized(true);
  }

}
