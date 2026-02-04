/**
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;


import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;


/**
 * Used by the webapp to popuplate objects used by the screens.
 */
public interface ClientAccountService {

  /**
   * Get the AgristabilityClients (and their owner person) that this user is
   * authorized to represent.
   *
   * @return  the clients
   *
   * @throws  ServiceException  on exception
   */
  Client[] getClientsForCurrentUser() throws ServiceException;
  
  /**
   * Used by screen 400 to get the benefits for reference years.
   * 
   * @param programYearScenario programYearScenario
   * @throws ServiceException on exception
   */
  void getReferenceYearBenefits(Scenario programYearScenario) throws ServiceException;
}
