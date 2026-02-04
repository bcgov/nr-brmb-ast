/**
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 * @created April 18, 2019
 */
public class ConfigurationDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";
  
  private static final String GET_CONFIGURATION_PARAMETERS_PROC = "GET_CONFIGURATION_PARAMETERS";
  
  private static final int GET_CONFIGURATION_PARAMETERS_PARAM = 0;
  
  private static final String GET_YEAR_CONFIGURATION_PARAMS_PROC = "GET_YEAR_CONFIGURATION_PARAMS";
  
  private static final int GET_YEAR_CONFIGURATION_PARAMS_PARAM = 0;
  
  @SuppressWarnings("resource")
  public Map<String, String> getConfigurationParameters(final Transaction transaction)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    Map<String, String> parameters = new HashMap<>();

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_CONFIGURATION_PARAMETERS_PROC, GET_CONFIGURATION_PARAMETERS_PARAM, true); ) {

      proc.execute();

      try(ResultSet rs = proc.getResultSet(); ) {

        while (rs.next()) {
          String name = getString(rs, "parameter_name");
          String value = getString(rs, "parameter_value");
          parameters.put(name, value);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return parameters;
  }
  
  @SuppressWarnings("resource")
  public Map<Integer, Map<String, String>> getYearConfigurationParameters(final Transaction transaction)
      throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    Map<Integer, Map<String, String>> parametersByYear = new HashMap<>();
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + GET_YEAR_CONFIGURATION_PARAMS_PROC, GET_YEAR_CONFIGURATION_PARAMS_PARAM, true); ) {
      
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet(); ) {
        
        while (rs.next()) {
          Integer programYear = getInteger(rs, "program_year");
          String name = getString(rs, "parameter_name");
          String value = getString(rs, "parameter_value");
          
          Map<String, String> yearParameters = parametersByYear.get(programYear);
          if(yearParameters == null) {
            yearParameters = new HashMap<>();
            parametersByYear.put(programYear, yearParameters);
          }
          
          yearParameters.put(name, value);
        }
      }
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
    
    return parametersByYear;
  }

}
