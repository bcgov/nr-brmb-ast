/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.DeductionLineItem;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.CalculatorInboxItem;

/**
 * @author awilkinson
 * @created Mar 28, 2011
 */
public class CalculatorViewDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  private static final String READ_INBOX_PROC = "READ_INBOX";
  
  private static final int READ_INBOX_PARAM = 4;
  
  private static final String GET_SCENARIO_REVISION_COUNT_PROC = "GET_SC_REV_COUNT";
  
  private static final int GET_SCENARIO_REVISION_COUNT_PARAM = 1;
  
  private static final String PYV_HAS_VERIFIED_SC_PROC = "PYV_HAS_VERIFIED_SC";
  
  private static final int PYV_HAS_VERIFIED_SC_PARAM = 1;
  
  private static final String GET_DEDUCTION_LINE_ITEMS_PROC = "GET_DEDUCTION_LINE_ITEMS";
  
  private static final int GET_DEDUCTION_LINE_ITEMS_PARAM = 2;
  
  private static final String GET_COMBINED_FARM_IP_SC_PROC = "GET_COMBINED_FARM_IP_SC";
  
  private static final int GET_COMBINED_FARM_IP_SC_PARAM = 2;
  
  private static final String GET_IP_SC_COMBINED_FARM_NUMBER_PROC = "GET_IP_SC_COMBINED_FARM_NUMBER";
  
  private static final int GET_IP_SC_COMBINED_FARM_NUMBER_PARAM = 2;
  
  private static final String GET_VERIFED_SC_CF_NUMBER_PROC = "GET_VERIFED_SC_CF_NUMBER";
  
  private static final int GET_VERIFED_SC_CF_NUMBER_PARAM = 2;
  
  
  /**
   * @param transaction Transaction
   * @param participantPin Integer
   * @param combinedFarmNumber Integer
   * @throws DataAccessException On Exception
   * 
   * @return Map<pin, List<scenarioNumber>>
   */
  @SuppressWarnings("resource")
  public Map<Integer, List<Integer>> getCombinedFarmInProgressScenarioNumbers(final Transaction transaction,
      final Integer participantPin,
      final Integer combinedFarmNumber)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    Map<Integer, List<Integer>> scNumMap = new HashMap<>();

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_COMBINED_FARM_IP_SC_PROC, GET_COMBINED_FARM_IP_SC_PARAM, true);

      int param = 1;
      proc.setInt(param++, participantPin);
      proc.setInt(param++, combinedFarmNumber);
      proc.execute();

      rs = proc.getResultSet();

      while (rs.next()) {
        Integer curPin = getInteger(rs, "participant_pin");
        Integer curScNum = getInteger(rs, "scenario_number");
        
        List<Integer> pinScNumbers = scNumMap.get(curPin);
        if(pinScNumbers == null) {
          pinScNumbers = new ArrayList<>();
          scNumMap.put(curPin, pinScNumbers);
        }
        pinScNumbers.add(curScNum);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return scNumMap;
  }
  
  
  /**
   * @param transaction Transaction
   * @param searchType String
   * @param year Integer
   * @param userGuid String
   * @param scenarioStateCodes List
   * @throws DataAccessException On Exception
   * 
   * @return List<CalculatorInboxItem>
   */
  @SuppressWarnings("resource")
  public List<CalculatorInboxItem> readInboxItems(final Transaction transaction,
      final String searchType,
      final Integer year,
      final String userGuid,
      final List<String> scenarioStateCodes)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<CalculatorInboxItem> inboxItems = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + READ_INBOX_PROC, READ_INBOX_PARAM, true);
      
      Array oracleArray = createStringOracleArray(connection, scenarioStateCodes);

      int param = 1;
      proc.setString(param++, searchType);
      proc.setInt(param++, year);
      proc.setString(param++, userGuid);
      proc.setArray(param++, oracleArray);
      proc.execute();

      rs = proc.getResultSet();

      inboxItems = new ArrayList<>();

      while (rs.next()) {
        CalculatorInboxItem item = new CalculatorInboxItem();
        item.setScenarioId(getInteger(rs, "agristability_scenario_id"));
        item.setName(getString(rs, "client_name"));
        item.setPin(getInteger(rs, "participant_pin"));
        item.setScenarioStateCode(getString(rs, "scenario_state_code"));
        item.setScenarioStateCodeDescription(getString(rs, "scenario_state_code_desc"));
        item.setLastChangedDate(getDate(rs, "last_changed"));
        item.setTotalBenefit(getDouble(rs, "total_benefit"));
        item.setAssignedToUserId(getString(rs, "assigned_to_userid"));
        item.setAssignedToUserGuid(getString(rs, "assigned_to_user_guid"));
        item.setReceivedDate(getDate(rs, "recieved_date"));
        
        inboxItems.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return inboxItems;
  }
  
  
  /**
   * @param transaction transaction
   * @param scenarioId Integer
   * @throws DataAccessException On Exception
   * @return revision count
   */
  @SuppressWarnings("resource")
  public Integer getScenarioRevisionCount(final Transaction transaction,
      final Integer scenarioId)
      throws DataAccessException {

    Connection connection = getOracleConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    Integer revisionCount = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_SCENARIO_REVISION_COUNT_PROC, GET_SCENARIO_REVISION_COUNT_PARAM, true);
      
      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.execute();

      rs = proc.getResultSet();

      if(rs.next()) {
        int c = 1;
        revisionCount = getInteger(rs, c++);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return revisionCount;
  }
  
  
  /**
   * 
   * @param transaction transaction
   * @param programYearVersionId programYearVersionId
   * @return boolean
   * @throws DataAccessException On Exception 
   */
  @SuppressWarnings("resource")
  public boolean programYearVersionHasVerifiedScenario(
      final Transaction transaction,
      final Integer programYearVersionId)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    boolean result = false;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + PYV_HAS_VERIFIED_SC_PROC, PYV_HAS_VERIFIED_SC_PARAM, Types.INTEGER);

      int param = 1;
      proc.setInt(param++, programYearVersionId);
      proc.execute();
      
      int resultInt = proc.getInt(1);
      result = resultInt == 1;
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }

    return result;
  }
  
  
  /**
   * @param transaction Transaction
   * @param programYear Integer
   * @param deductionType String
   * @throws DataAccessException On Exception
   * 
   * @return List<DeductionLineItem>
   */
  @SuppressWarnings("resource")
  public List<DeductionLineItem> getDeductionLineItems(final Transaction transaction,
      final Integer programYear,
      final String deductionType)
      throws DataAccessException {

    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<DeductionLineItem> items = null;

    try {

      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_DEDUCTION_LINE_ITEMS_PROC, GET_DEDUCTION_LINE_ITEMS_PARAM, true);

      int param = 1;
      proc.setInt(param++, programYear);
      proc.setString(param++, deductionType);
      proc.execute();

      rs = proc.getResultSet();

      items = new ArrayList<>();

      while (rs.next()) {
        DeductionLineItem item = new DeductionLineItem();
        item.setLineItem(getInteger(rs, "line_item"));
        item.setDescription(getString(rs, "description"));
        item.setIsDeductionProgramYearMinus5(Boolean.valueOf(getIndicator(rs, "py_minus_5_ind")));
        item.setIsDeductionProgramYearMinus4(Boolean.valueOf(getIndicator(rs, "py_minus_4_ind")));
        item.setIsDeductionProgramYearMinus3(Boolean.valueOf(getIndicator(rs, "py_minus_3_ind")));
        item.setIsDeductionProgramYearMinus2(Boolean.valueOf(getIndicator(rs, "py_minus_2_ind")));
        item.setIsDeductionProgramYearMinus1(Boolean.valueOf(getIndicator(rs, "py_minus_1_ind")));
        item.setIsDeductionProgramYear(Boolean.valueOf(getIndicator(rs, "py_ind")));
        
        items.add(item);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
    }
    
    return items;
  }
  
  
  /**
   * @param transaction transaction
   * @param pin Integer
   * @param programYear Integer
   * @return Integer
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public Integer getInProgressCombinedFarmNumber(
      final Transaction transaction,
      final Integer pin,
      final Integer programYear) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer combinedFarmNumber = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_IP_SC_COMBINED_FARM_NUMBER_PROC, GET_IP_SC_COMBINED_FARM_NUMBER_PARAM, Types.INTEGER);
      
      int c = 1;
      
      proc.setInt(c++, pin);
      proc.setInt(c++, programYear);
      
      proc.execute();
      combinedFarmNumber = new Integer(proc.getInt(1));
      if(combinedFarmNumber.intValue() == 0) {
        combinedFarmNumber = null;
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return combinedFarmNumber;
  }
  
  
  /**
   * @param transaction transaction
   * @param pin Integer
   * @param programYear Integer
   * @return Integer
   * @throws DataAccessException DataAccessException
   */
  @SuppressWarnings("resource")
  public Integer getVerifiedCombinedFarmNumber(
      final Transaction transaction,
      final Integer pin,
      final Integer programYear) throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;
    Integer combinedFarmNumber = null;
    
    try {
      proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + GET_VERIFED_SC_CF_NUMBER_PROC, GET_VERIFED_SC_CF_NUMBER_PARAM, Types.INTEGER);
      
      int c = 1;
      
      proc.setInt(c++, pin);
      proc.setInt(c++, programYear);
      
      proc.execute();
      combinedFarmNumber = new Integer(proc.getInt(1));
      if(combinedFarmNumber.intValue() == 0) {
        combinedFarmNumber = null;
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
    
    return combinedFarmNumber;
  }

}
