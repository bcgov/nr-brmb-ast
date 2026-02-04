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
package ca.bc.gov.srm.farm.service;

import java.math.BigDecimal;
import java.util.Map;

import ca.bc.gov.srm.farm.exception.ServiceException;


public interface ConfigurationService {

  
  Map<String, String> getConfigurationParameters()
      throws ServiceException;

  void loadConfigurationParameters()
      throws ServiceException;
  
  Map<Integer, Map<String, String>> getYearConfigurationParameters()
      throws ServiceException;
  
  void loadYearConfigurationParameters()
      throws ServiceException;
  
  String getValue(Integer programYear, String parameterName);
  
  String getValue(Integer programYear, String parameterName, boolean errorOnNotFound);

  double getDouble(Integer programYear, String parameterName);
  
  double getPercent(Integer programYear, String parameterName);
  
  BigDecimal getBigDecimal(Integer programYear, String parameterName);
  
  BigDecimal getBigDecimalPercent(Integer programYear, String parameterName);
}
