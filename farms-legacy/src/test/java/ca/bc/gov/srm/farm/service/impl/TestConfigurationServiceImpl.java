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
package ca.bc.gov.srm.farm.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.ConfigurationDAO;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ConfigurationService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.transaction.TransactionProvider;
import ca.bc.gov.srm.farm.util.DataParseUtils;


final class TestConfigurationServiceImpl extends BaseService implements ConfigurationService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, String> configurationParameters;
  
  private Map<Integer, Map<String, String>> yearConfigurationParameters;
  
  @Override
  public Map<String, String> getConfigurationParameters()
      throws ServiceException {
    
    if(configurationParameters == null) {
      loadConfigurationParameters();
    }
    
    return configurationParameters;
  }
  
  @Override
  public void loadConfigurationParameters()
          throws ServiceException {
    
    Transaction transaction = null;
    TransactionProvider transactionProvider = null;
    ConfigurationDAO dao = new ConfigurationDAO();
    
    try {
      transactionProvider = TransactionProvider.getInstance();
      transaction = transactionProvider.getTransaction(BusinessAction.system());
      
      configurationParameters = dao.getConfigurationParameters(transaction);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
  }
  
  @Override
  public Map<Integer, Map<String, String>> getYearConfigurationParameters()
      throws ServiceException {
    
    if(yearConfigurationParameters == null) {
      loadYearConfigurationParameters();
    }
    
    return yearConfigurationParameters;
  }
  
  @Override
  public void loadYearConfigurationParameters()
      throws ServiceException {
    
    Transaction transaction = null;
    TransactionProvider transactionProvider = null;
    ConfigurationDAO dao = new ConfigurationDAO();
    
    try {
      transactionProvider = TransactionProvider.getInstance();
      transaction = transactionProvider.getTransaction(BusinessAction.system());
      
      yearConfigurationParameters = dao.getYearConfigurationParameters(transaction);
      
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    setParam(2009, "BC Enhanced Benefit - Enabled", "N");
    setParam(2010, "BC Enhanced Benefit - Enabled", "N");
    setParam(2011, "BC Enhanced Benefit - Enabled", "N");
    setParam(2012, "BC Enhanced Benefit - Enabled", "N");
    setParam(2013, "BC Enhanced Benefit - Enabled", "N");
    setParam(2014, "BC Enhanced Benefit - Enabled", "N");
    setParam(2015, "BC Enhanced Benefit - Enabled", "N");
    setParam(2016, "BC Enhanced Benefit - Enabled", "N");
    setParam(2017, "BC Enhanced Benefit - Enabled", "Y");
    setParam(2018, "BC Enhanced Benefit - Enabled", "N");
    setParam(2019, "BC Enhanced Benefit - Enabled", "Y");
    setParam(2020, "BC Enhanced Benefit - Enabled", "Y");
    setParam(2021, "BC Enhanced Benefit - Enabled", "Y");
    setParam(2022, "BC Enhanced Benefit - Enabled", "N");
    
    setParam(2017, "Late Participant - Penalty Percentage", "0");
    setParam(2018, "Late Participant - Penalty Percentage", "20");
    setParam(2019, "Late Participant - Penalty Percentage", "20");
    setParam(2020, "Late Participant - Penalty Percentage", "20");
    setParam(2021, "Late Participant - Penalty Percentage", "20");
    setParam(2022, "Late Participant - Penalty Percentage", "20");
    
    setParam(2017, "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2018, "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2019, "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2020, "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2021, "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2022, "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage", "80");
    
    setParam(2017, "BC Enhanced Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2019, "BC Enhanced Benefit - Negative Margin Compensation Rate Percentage", "80");
    setParam(2020, "BC Enhanced Benefit - Negative Margin Compensation Rate Percentage", "80");
    setParam(2021, "BC Enhanced Benefit - Negative Margin Compensation Rate Percentage", "80");
    setParam(2022, "BC Enhanced Benefit - Negative Margin Compensation Rate Percentage", "80");
    
    setParam(2009, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2010, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2011, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2012, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "80");
    setParam(2013, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2014, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2015, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2016, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2017, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2018, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2019, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2020, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2021, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    setParam(2022, "AgriStability Benefit - Positive Margin Compensation Rate Percentage", "70");
    
    setParam(2009, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "60");
    setParam(2010, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "60");
    setParam(2011, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "60");
    setParam(2012, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "60");
    setParam(2013, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2014, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2015, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2016, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2017, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2018, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2019, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2020, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2021, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
    setParam(2022, "AgriStability Benefit - Negative Margin Compensation Rate Percentage", "70");
  }
  
  private void setParam(Integer year, String paramName, String paramValue) {
    
    Map<String, String> yearParams = yearConfigurationParameters.get(year);
    if(yearParams == null) {
      yearParams = new HashMap<>();
      yearConfigurationParameters.put(year, yearParams);
    }
    yearParams.put(paramName, paramValue);
  }

  @Override
  public String getValue(Integer programYear, String parameterName) {
    return getValue(programYear, parameterName, true);
  }
    
    @Override
    public String getValue(Integer programYear, String parameterName, boolean errorOnNotFound) {
    
    Map<String, String> yearParameters = yearConfigurationParameters.get(programYear);
    
    if(yearParameters == null) {
      throw new IllegalArgumentException("Configuration Parameters do not exist for program year: " + programYear);
    }
    
    String value = yearParameters.get(parameterName);
    
    if(value == null && errorOnNotFound) {
      String message = "Configuration Parameter '%s' not found for program year: %d";
      throw new IllegalArgumentException(String.format(message, parameterName, programYear, value));
    }
    
    return value;
  }

  @Override
  public double getDouble(Integer programYear, String parameterName) {

    String stringValue = getValue(programYear, parameterName);
    try {
      Double result = DataParseUtils.parseDoubleObject(stringValue);
      return result;
    } catch (ParseException e) {
      String message = "Configuration Parameter %s for program year %d is not a decimal number: %s";
      throw new IllegalArgumentException(String.format(message, parameterName, programYear, stringValue));
    }

  }
  
  @Override
  public double getPercent(Integer programYear, String parameterName) {
    
    return getDouble(programYear, parameterName) / 100;
  }

  @Override
  public BigDecimal getBigDecimal(Integer programYear, String parameterName) {

    String stringValue = getValue(programYear, parameterName);
    try {
      BigDecimal result = DataParseUtils.parseBigDecimal(stringValue);
      return result;
    } catch (ParseException e) {
      String message = "Configuration Parameter %s for program year %d is not a decimal number: %s";
      throw new IllegalArgumentException(String.format(message, parameterName, programYear, stringValue));
    }

  }
  
  @Override
  public BigDecimal getBigDecimalPercent(Integer programYear, String parameterName) {
    
    return getBigDecimal(programYear, parameterName).multiply(new BigDecimal("100"));
  }
}
