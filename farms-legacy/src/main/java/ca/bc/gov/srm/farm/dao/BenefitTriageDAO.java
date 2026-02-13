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

import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageCalculationItem;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageStatus;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.webade.dbpool.WrapperConnection;

public class BenefitTriageDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_FIFO_PKG";
  private static final String READ_FIFO_STATUS_BY_YEAR_PROC = "READ_FIFO_STATUS_BY_YEAR";
  private static final String READ_FIFO_CALCULATION_ITEMS_PROC = "READ_FIFO_CALCULATION_ITEMS";

  @SuppressWarnings("resource")
  public List<BenefitTriageStatus> readTriageStatusByYear(final Transaction transaction, final int year) throws DataAccessException {

    final int paramCount = 1;
    List<BenefitTriageStatus> triageStatusList = new ArrayList<>();
    Connection connection = getOracleConnection(transaction);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(
        connection, PACKAGE_NAME + "." + READ_FIFO_STATUS_BY_YEAR_PROC, paramCount, true);) {

      int param = paramCount;
      proc.setInt(param++, year);
      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          BenefitTriageStatus triageStatus = new BenefitTriageStatus();
          triageStatus.setParticipantPin(getInteger(rs, "participant_pin"));
          triageStatus.setClientName(getString(rs, "client_name"));
          triageStatus.setScenarioStateCodeDesc(getString(rs, "scenario_state_code_desc"));
          triageStatus.setEstimatedBenefit(getDouble(rs, "estimated_benefit"));
          triageStatus.setIsPaymentFile(getString(rs, "is_payment_file"));
          triageStatus.setScenarioNumber(getInteger(rs, "scenario_number"));

          triageStatusList.add(triageStatus);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return triageStatusList;
  }


  @SuppressWarnings("resource")
  public List<BenefitTriageCalculationItem> readTriageCalculationItems(final Connection conn) throws DataAccessException {

    final int paramCount = 0;
    List<BenefitTriageCalculationItem> triageCalculationItemList = new ArrayList<>();
    
    Connection connection = getWrappedConnection(conn);

    try (DAOStoredProcedure proc = new DAOStoredProcedure(
        connection, PACKAGE_NAME + "." + READ_FIFO_CALCULATION_ITEMS_PROC, paramCount, true);) {

      proc.execute();

      try (ResultSet rs = proc.getResultSet();) {

        while (rs.next()) {
          BenefitTriageCalculationItem triageStatus = new BenefitTriageCalculationItem();
          triageStatus.setParticipantPin(getInteger(rs, "Participant_Pin"));
          triageStatus.setProgramYear(getInteger(rs, "Program_Year"));
          triageStatus.setCraProgramYearVersionId(getInteger(rs, "Cra_Pyv_Id"));
          triageStatus.setCraScenarioId(getInteger(rs, "Cra_Scenario_Id"));
          triageStatus.setCraScenarioNumber(getInteger(rs, "Cra_Scenario_Number"));

          triageCalculationItemList.add(triageStatus);
        }
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return triageCalculationItemList;
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
