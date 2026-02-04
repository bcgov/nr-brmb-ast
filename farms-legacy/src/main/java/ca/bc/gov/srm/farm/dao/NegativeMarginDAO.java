/**
 * Copyright (c) 2025,
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

import ca.bc.gov.srm.farm.domain.NegativeMargin;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author hwang
 */
public class NegativeMarginDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_NEGATIVE_MARGIN_PKG";


  private static final String GET_NEGATIVE_MARGINS_PROC = "GET_NEGATIVE_MARGINS";

  private static final String UPDATE_NEGATIVE_MARGIN_PROC = "UPDATE_NEGATIVE_MARGIN";

  private static final String CALCULATE_NEGATIVE_MARGINS_PROC = "CALCULATE_NEGATIVE_MARGINS";


  @SuppressWarnings("resource")
  public final List<NegativeMargin> getNegativeMargins(
      final Transaction transaction,
      final Integer farmingOperationId,
      final Integer scenarioId)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    String procName = PACKAGE_NAME + "." + GET_NEGATIVE_MARGINS_PROC;
    final int paramCount = 2;
    List<NegativeMargin> negativeMargins = new ArrayList<>();

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, true);) {

      int param = 1;
      proc.setInt(param++, farmingOperationId);
      proc.setInt(param++, scenarioId);
      proc.execute();
      try (ResultSet resultSet = proc.getResultSet();) {

        while (resultSet.next()) {
          NegativeMargin nm = populateNegativeMargin(resultSet);
          negativeMargins.add(nm);
        }
      }
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

    return negativeMargins;
  }


  private NegativeMargin populateNegativeMargin(ResultSet resultSet) throws SQLException {
    NegativeMargin nm = new NegativeMargin();
    nm.setNegativeMarginId(resultSet.getInt("NEGATIVE_MARGIN_ID"));
    nm.setFarmingOperationId(resultSet.getInt("FARMING_OPERATION_ID"));
    nm.setInventoryItemCode(resultSet.getString("INVENTORY_ITEM_CODE"));
    nm.setScenarioId(resultSet.getInt("AGRISTABILITY_SCENARIO_ID"));
    nm.setRevisionCount(resultSet.getInt("REVISION_COUNT"));
    nm.setInventory(resultSet.getString("INVENTORY_DESC"));
    nm.setDeductiblePercentage(resultSet.getBigDecimal("DEDUCTIBLE_PERCENTAGE"));
    nm.setInsurableValue(resultSet.getBigDecimal("REQUIRED_INSURABLE_VALUE"));
    nm.setInsurableValuePurchased(resultSet.getBigDecimal("INSURABLE_VALUE_PURCHASED"));
    nm.setReported(resultSet.getBigDecimal("REPORTED"));
    nm.setGuaranteedProdValue(resultSet.getBigDecimal("GUARANTEED_PROD_VALUE"));
    nm.setPremiumsPaid(resultSet.getBigDecimal("PREMIUMS_PAID"));
    nm.setClaimsReceived(resultSet.getBigDecimal("CLAIMS_RECEIVED"));
    nm.setDeemedReceived(resultSet.getBigDecimal("DEEMED_RECEIVED"));
    nm.setDeemedPiValue(resultSet.getBigDecimal("DEEMED_PI_VALUE"));
    nm.setPremiumRate(resultSet.getBigDecimal("PREMIUM_RATE"));
    nm.setClaimsCalculation(resultSet.getBigDecimal("CLAIMS_CALCULATION"));
    nm.setPremium(resultSet.getBigDecimal("REQUIRED_PREMIUM"));
    nm.setMrp(resultSet.getBigDecimal("MARKET_RATE_PREMIUM"));
    nm.setDeemedPremium(resultSet.getBigDecimal("DEEMED_PREMIUM"));
    return nm;
  }


  /**
   * @param transaction transaction
   * @param negativeMargin negativeMargin
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void updateNegativeMargins(final Transaction transaction,
      final List<NegativeMargin> negativeMargins,
      final String user)
  throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    final int paramCount = 7;
    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_NEGATIVE_MARGIN_PROC, paramCount, false); ) {

      for (NegativeMargin negativeMargin : negativeMargins) {

        int param = 1;
        proc.setInt(param++, negativeMargin.getNegativeMarginId());
        proc.setBigDecimal(param++, negativeMargin.getDeductiblePercentage());
        proc.setBigDecimal(param++, negativeMargin.getInsurableValuePurchased());
        proc.setBigDecimal(param++, negativeMargin.getGuaranteedProdValue());
        proc.setBigDecimal(param++, negativeMargin.getPremiumsPaid());
        proc.setBigDecimal(param++, negativeMargin.getClaimsReceived());
        proc.setString(param++, user);

        proc.addBatch();
      }

      proc.executeBatch();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }


  @SuppressWarnings("resource")
  public final void calculateNegativeMargins(
      final Transaction transaction,
      final Integer farmingOperationId,
      final Integer scenarioId,
      final String user)
  throws DataAccessException {

    Connection connection = getConnection(transaction);
    String procName = PACKAGE_NAME + "." + CALCULATE_NEGATIVE_MARGINS_PROC;
    final int paramCount = 3;

    try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, procName, paramCount, false);) {

      int param = 1;
      proc.setInt(param++, farmingOperationId);
      proc.setInt(param++, scenarioId);
      proc.setString(param++, user);
      proc.execute();
      
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }

  }
}
