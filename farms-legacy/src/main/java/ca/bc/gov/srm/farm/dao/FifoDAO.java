/**
 * Copyright (c) 2024,
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
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.fifo.FifoCalculationItem;
import ca.bc.gov.srm.farm.domain.fifo.FifoStatus;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.webade.dbpool.WrapperConnection;

public class FifoDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_FIFO_PKG";
  private static final String READ_FIFO_STATUS_BY_YEAR_PROC = "READ_FIFO_STATUS_BY_YEAR";
  private static final String READ_FIFO_CALCULATION_ITEMS_PROC = "READ_FIFO_CALCULATION_ITEMS";

  @SuppressWarnings("resource")
  public List<FifoStatus> readFifoStatusByYear(final Transaction transaction, final int year) throws DataAccessException {

    final int paramCount = 1;
    List<FifoStatus> fifoStatusList = new ArrayList<>();
    Connection connection = getOracleConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(
        connection, PACKAGE_NAME + "." + READ_FIFO_STATUS_BY_YEAR_PROC, paramCount, true);) {

      int param = paramCount;
      proc.setInt(param++, year);
      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          FifoStatus fifoStatus = new FifoStatus();
          fifoStatus.setParticipantPin(getInteger(rs, "participant_pin"));
          fifoStatus.setClientName(getString(rs, "client_name"));
          fifoStatus.setScenarioStateCodeDesc(getString(rs, "scenario_state_code_desc"));
          fifoStatus.setEstimatedBenefit(getDouble(rs, "estimated_benefit"));
          fifoStatus.setIsPaymentFile(getString(rs, "is_payment_file"));
          fifoStatus.setScenarioNumber(getInteger(rs, "scenario_number"));

          fifoStatusList.add(fifoStatus);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return fifoStatusList;
  }


  @SuppressWarnings("resource")
  public List<FifoCalculationItem> readFifoCalculationItems(final Connection conn) throws DataAccessException {

    final int paramCount = 0;
    List<FifoCalculationItem> fifoCalculationItemList = new ArrayList<>();
    
    Connection connection = getWrappedConnection(conn);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(
        connection, PACKAGE_NAME + "." + READ_FIFO_CALCULATION_ITEMS_PROC, paramCount, true);) {

      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          FifoCalculationItem fifoStatus = new FifoCalculationItem();
          fifoStatus.setParticipantPin(getInteger(rs, "Participant_Pin"));
          fifoStatus.setProgramYear(getInteger(rs, "Program_Year"));
          fifoStatus.setCraProgramYearVersionId(getInteger(rs, "Cra_Pyv_Id"));
          fifoStatus.setCraScenarioId(getInteger(rs, "Cra_Scenario_Id"));
          fifoStatus.setCraScenarioNumber(getInteger(rs, "Cra_Scenario_Number"));
          fifoStatus.setFifoScenarioId(getInteger(rs, "Fifo_Scenario_Id"));
          fifoStatus.setFifoScenarioNumber(getInteger(rs, "Fifo_Scenario_Number"));
          fifoStatus.setFifoProgramYearVersionId(getInteger(rs, "Fifo_Pyv_Id"));
          fifoStatus.setFifoScenarioStateCode(getString(rs, "Fifo_Scenario_State_Code"));

          fifoCalculationItemList.add(fifoStatus);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return fifoCalculationItemList;
  }

  
  @SuppressWarnings("resource")
  private Connection getWrappedConnection(Connection connection) {
    Connection result;
    if(connection instanceof WrapperConnection){
      WrapperConnection wc = (WrapperConnection)connection;
      result = wc.getWrappedConnection();
    }else{
      result = connection;
    }
    return result;
    
  }
}
