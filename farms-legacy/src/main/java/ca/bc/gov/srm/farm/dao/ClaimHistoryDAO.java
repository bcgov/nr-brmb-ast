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
package ca.bc.gov.srm.farm.dao;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.domain.Benefit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DAO used by screen 400
 */
public class ClaimHistoryDAO extends OracleDAO {

  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARM_WEBAPP_PKG";

  /** CLAIM_PROC. */
  private static final String CLAIM_PROC = "get_ref_year_claim";
  

  /**
   * @param   transaction  transaction
   * @param   scenarioId   scenarioId
   *
   * @throws  DataAccessException  on exception
   * @return  Benefit
   */
  @SuppressWarnings("resource")
  public Benefit getReferenceYearBenefit(
  		final Transaction transaction, 
  		final Integer scenarioId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + CLAIM_PROC;
    Benefit benefit = null;
    Connection connection = getConnection(transaction);
    ResultSet resultSet = null;
    DAOStoredProcedure proc = null;
    final int paramCount = 1;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);

      int param = 1;
      proc.setInt(param++, scenarioId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
      	benefit = createBenefit(resultSet);
      }

    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }

    return benefit;
  }
  
  
  /**
   * @param rs rs
   * @return Benefit
   * @throws SQLException On Exception
   */
  private Benefit createBenefit(ResultSet rs) throws SQLException {
  	Benefit benefit = new Benefit();

    int c = 1;
    benefit.setClaimId(getInteger(rs, c++));
    benefit.setAdministrativeCostShare(getDouble(rs, c++));
    benefit.setLateApplicationPenalty(getDouble(rs, c++));
    benefit.setMaximumContribution(getDouble(rs, c++));
    benefit.setOutstandingFees(getDouble(rs, c++));
    benefit.setProgramYearMargin(getDouble(rs, c++));
    benefit.setProdInsurDeemedBenefit(getDouble(rs, c++));
    benefit.setProgramYearPaymentsReceived(getDouble(rs, c++));
    benefit.setAllocatedReferenceMargin(getDouble(rs, c++));
    benefit.setRepaymentOfCashAdvances(getDouble(rs, c++));
    benefit.setTotalPayment(getDouble(rs, c++));
    benefit.setSupplyManagedCommoditiesAdj(getDouble(rs, c++));
    benefit.setProducerShare(getDouble(rs, c++));
    benefit.setFederalContributions(getDouble(rs, c++));
    benefit.setProvincialContributions(getDouble(rs, c++));
    benefit.setInterimContributions(getDouble(rs, c++));
    benefit.setWholeFarmAllocation(getDouble(rs, c++));
    benefit.setMarginDecline(getDouble(rs, c++));
    benefit.setNegativeMarginDecline(getDouble(rs, c++));
    benefit.setNegativeMarginBenefit(getDouble(rs, c++));
    benefit.setTotalBenefit(getDouble(rs, c++));
    benefit.setAdjustedReferenceMargin(getDouble(rs, c++));
    benefit.setUnadjustedReferenceMargin(getDouble(rs, c++));
    benefit.setReferenceMarginLimit(getDouble(rs, c++));
    benefit.setTier2Trigger(getDouble(rs, c++));
    benefit.setTier2MarginDecline(getDouble(rs, c++));
    benefit.setTier2Benefit(getDouble(rs, c++));
    benefit.setTier3Trigger(getDouble(rs, c++));
    benefit.setTier3MarginDecline(getDouble(rs, c++));
    benefit.setTier3Benefit(getDouble(rs, c++));
    benefit.setAppliedBenefitPercent(getDouble(rs, c++));
    benefit.setInterimBenefitPercent(getDouble(rs, c++));
    benefit.setBenefitBeforeDeductions(getDouble(rs, c++));
    benefit.setBenefitAfterProdInsDeduction(getDouble(rs, c++));
    benefit.setBenefitAfterAppliedBenefitPercent(getDouble(rs, c++));
    benefit.setBenefitAfterInterimDeduction(getDouble(rs, c++));
    
    benefit.setStructuralChangeMethodCode(rs.getString(c++));
    benefit.setExpenseStructuralChangeMethodCode(rs.getString(c++));
    // structual change code descriptions not needed
    
    return benefit;
  }
}
