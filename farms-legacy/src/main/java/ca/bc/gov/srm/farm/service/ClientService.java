/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;


/**
 * @author   cboreen
 * @created  16-Sep-2009 4:52:32 PM
 */
public interface ClientService {

  String COMP_FIRST_MODE = "COMP";
  String DEF_FIRST_MODE = "DEF";
  String ENROLMENT_MODE = "ENROL";

  /**
   * @param   pin             int
   * @param   programYear     int
   * @param   scenarioNumber  Integer
   * @param   mode            String 
   *
   * @return  AgristabilityClient
   * @throws ServiceException ServiceException
   */
  Scenario getClientInfoWithHistory(final int pin,
    final int programYear, final Integer scenarioNumber, final String mode) throws ServiceException; 

  /**
   * @param pin int
   * @param programYear int
   * @param scenarioNumber Integer
   * @param mode String 
   * @return Scenario
   * @throws ServiceException ServiceException
   * @see ca.bc.gov.srm.farm.service.ClientService#getClientInfoWithHistory(int, int, Integer, String)
   */
  Scenario getClientInfoWithoutHistory(final int pin,
      final int programYear, final Integer scenarioNumber, final String mode) throws ServiceException;

}
