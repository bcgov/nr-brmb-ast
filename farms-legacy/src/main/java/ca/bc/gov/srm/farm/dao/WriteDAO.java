/**
 *
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

import java.io.IOException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.transaction.Transaction;


/**
 *
 */
public final class WriteDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_WRITE_PKG";

  private static final String CLAIM_PROC = "WRITE_CLAIM";
  
  private static final String MARGIN_PROC = "WRITE_MARGIN";
  
  private static final String MARGIN_TOTAL_PROC = "WRITE_MARGIN_TOTAL";
  
  private static final String WRITE_FINAL_VERIFICATION_NOTES = "WRITE_FINAL_VERIFICATION_NOTES";
  
  private static final String WRITE_INTERIM_VERIFICATION_NOTES = "WRITE_INTERIM_VERIFICATION_NOTES";
  
  private static final String WRITE_ADJUSTMENT_VERIFICATION_NOTES = "WRITE_ADJUSTMENT_VERIFICATION_NOTES";
  
  private static final String WRITE_REFERENCE_SCENARIO = "WRITE_REFERENCE_SCENARIO";
  
  private static final String WRITE_CALC_VERSION = "WRITE_CALC_VERSION";
  
  
  
  /**
   * @param   transaction    transaction
   * @param   version  version
   * @param   scenarioId  scenarioId
   * @param   userId  userId
   *
   * @throws  DataAccessException  on exception
   */
  public void writeCalculatorVersion(
  	final Transaction transaction,
    final String version,
    final Integer scenarioId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + WRITE_CALC_VERSION;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    final int paramCount = 3;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setInt(index++, scenarioId);
      proc.setString(index++, version);
      proc.setString(index++, userId);
      
      proc.execute();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  /**
   * @param   transaction    transaction
   * @param   benefit  benefit
   * @param   scenarioId  scenarioId
   * @param   userId  userId
   *
   * @throws  DataAccessException  on exception
   */
  public void writeBenefit(
  	final Transaction transaction,
    final Benefit benefit,
    final Integer scenarioId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + CLAIM_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    final int paramCount = 68;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index++, Types.INTEGER);
      proc.registerOutParameter(index++, Types.INTEGER);

      index = 1;
      proc.setInt(index++, benefit.getClaimId());
      proc.setInt(index++, benefit.getRevisionCount());
      
      proc.setDouble(index++, benefit.getAdministrativeCostShare());
      proc.setDouble(index++, benefit.getLateApplicationPenalty());
      proc.setDouble(index++, benefit.getMaximumContribution());
      proc.setDouble(index++, benefit.getOutstandingFees());
      proc.setDouble(index++, benefit.getProgramYearMargin());
      proc.setDouble(index++, benefit.getProdInsurDeemedBenefit());
      proc.setIndicator(index++, benefit.getProdInsurDeemedBenefitManuallyCalculated());
      proc.setDouble(index++, benefit.getProgramYearPaymentsReceived());
      proc.setDouble(index++, benefit.getAllocatedReferenceMargin());
      proc.setDouble(index++, benefit.getRepaymentOfCashAdvances());
      proc.setDouble(index++, benefit.getTotalPayment());
      proc.setDouble(index++, benefit.getSupplyManagedCommoditiesAdj());
      proc.setDouble(index++, benefit.getProducerShare());
      proc.setDouble(index++, benefit.getFederalContributions());
      proc.setDouble(index++, benefit.getProvincialContributions());
      proc.setDouble(index++, benefit.getInterimContributions());
      proc.setDouble(index++, benefit.getWholeFarmAllocation());
      proc.setDouble(index++, benefit.getTier2Trigger());
      proc.setDouble(index++, benefit.getTier2MarginDecline());
      proc.setDouble(index++, benefit.getTier2Benefit());
      proc.setDouble(index++, benefit.getTier3Trigger());
      proc.setDouble(index++, benefit.getTier3MarginDecline());
      proc.setDouble(index++, benefit.getTier3Benefit());
      proc.setDouble(index++, benefit.getMarginDecline());
      proc.setDouble(index++, benefit.getNegativeMarginDecline());
      proc.setDouble(index++, benefit.getNegativeMarginBenefit());
      proc.setDouble(index++, benefit.getTotalBenefit());
      proc.setDouble(index++, benefit.getAdjustedReferenceMargin());
      proc.setDouble(index++, benefit.getUnadjustedReferenceMargin());
      proc.setDouble(index++, benefit.getReferenceMarginLimit());
      proc.setDouble(index++, benefit.getReferenceMarginLimitCap());
      proc.setDouble(index++, benefit.getReferenceMarginLimitForBenefitCalc());
      proc.setDouble(index++, benefit.getBenefitBeforeDeductions());
      proc.setDouble(index++, benefit.getBenefitAfterProdInsDeduction());
      proc.setDouble(index++, benefit.getAppliedBenefitPercent());
      proc.setDouble(index++, benefit.getBenefitAfterAppliedBenefitPercent());
      proc.setDouble(index++, benefit.getInterimBenefitPercent());
      proc.setDouble(index++, benefit.getBenefitAfterInterimDeduction());
      proc.setDouble(index++, benefit.getPaymentCap());
      proc.setDouble(index++, benefit.getBenefitAfterPaymentCap());
      proc.setDouble(index++, benefit.getLateEnrolmentPenalty());
      proc.setDouble(index++, benefit.getBenefitAfterLateEnrolmentPenalty());
      proc.setDouble(index++, benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent());
      
      proc.setDouble(index++, benefit.getStandardBenefit());
      proc.setDouble(index++, benefit.getEnhancedReferenceMarginForBenefitCalculation());
      proc.setDouble(index++, benefit.getEnhancedMarginDecline());
      proc.setDouble(index++, benefit.getEnhancedPositiveMarginDecline());
      proc.setDouble(index++, benefit.getEnhancedPositiveMarginBenefit());
      proc.setDouble(index++, benefit.getEnhancedNegativeMarginDecline());
      proc.setDouble(index++, benefit.getEnhancedNegativeMarginBenefit());
      proc.setDouble(index++, benefit.getEnhancedBenefitBeforeDeductions());
      proc.setDouble(index++, benefit.getEnhancedBenefitAfterProdInsDeduction());
      proc.setDouble(index++, benefit.getEnhancedBenefitAfterInterimDeduction());
      proc.setDouble(index++, benefit.getEnhancedBenefitAfterPaymentCap());
      proc.setDouble(index++, benefit.getEnhancedTotalBenefit());
      proc.setDouble(index++, benefit.getEnhancedAdditionalBenefit());
      proc.setDouble(index++, benefit.getEnhancedLateEnrolmentPenalty());
      proc.setDouble(index++, benefit.getEnhancedBenefitAfterLateEnrolmentPenalty());
      proc.setDouble(index++, benefit.getEnhancedBenefitAfterAppliedBenefitPercent());
      proc.setDouble(index++, benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent());
      proc.setDouble(index++, benefit.getRatioAdjustedReferenceMargin());
      proc.setDouble(index++, benefit.getAdditiveAdjustedReferenceMargin());
      
      proc.setInt(index++, scenarioId);
      proc.setString(index++, benefit.getStructuralChangeMethodCode());
      proc.setString(index++, benefit.getExpenseStructuralChangeMethodCode());
      proc.setString(index++, userId);
      
      proc.execute();

      index = 1;
      benefit.setClaimId(new Integer(proc.getInt(index++)));
      benefit.setRevisionCount(new Integer(proc.getInt(index++)));
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  /**
   * @param   transaction    transaction
   * @param   margin  margin
   * @param   scenarioId  scenarioId
   * @param   operationId  operationId
   * @param   userId  userId
   *
   * @throws  DataAccessException  on exception
   */
  public void writeMargin(
  	final Transaction transaction,
    final Margin margin,
    final Integer scenarioId,
    final Integer operationId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + MARGIN_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    final int paramCount = 28;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index++, Types.INTEGER);
      proc.registerOutParameter(index++, Types.INTEGER);

      index = 1;
      proc.setInt(index++, margin.getMarginId());
      proc.setInt(index++, margin.getRevisionCount());
      
      proc.setDouble(index++, margin.getTotalAllowableExpenses());
      proc.setDouble(index++, margin.getTotalAllowableIncome());
      proc.setDouble(index++, margin.getUnadjustedProductionMargin());
      proc.setDouble(index++, margin.getProductionMargAccrAdjs());
      proc.setDouble(index++, margin.getAccrualAdjsCropInventory());
      proc.setDouble(index++, margin.getAccrualAdjsLvstckInventory());
      proc.setDouble(index++, margin.getAccrualAdjsPayables());
      proc.setDouble(index++, margin.getAccrualAdjsPurchasedInputs());
      proc.setDouble(index++, margin.getAccrualAdjsReceivables());
      proc.setDouble(index++, margin.getUnadjustedAllowableIncome());
      proc.setDouble(index++, margin.getYardageIncome());
      proc.setDouble(index++, margin.getProgramPaymentIncome());
      proc.setDouble(index++, margin.getSupplyManagedCommodityIncome());
      proc.setDouble(index++, margin.getTotalUnallowableIncome());
      proc.setDouble(index++, margin.getUnadjustedAllowableExpenses());
      proc.setDouble(index++, margin.getYardageExpenses());
      proc.setDouble(index++, margin.getContractWorkExpenses());
      proc.setDouble(index++, margin.getManualExpenses());
      proc.setDouble(index++, margin.getTotalUnallowableExpenses());
      proc.setDouble(index++, margin.getDeferredProgramPayments());
      proc.setDouble(index++, margin.getExpenseAccrualAdjs());
      proc.setDouble(index++, margin.getProdInsurDeemedSubtotal());
      proc.setDouble(index++, margin.getProdInsurDeemedTotal());
      proc.setInt(index++, operationId);
      proc.setInt(index++, scenarioId);
      proc.setString(index++, userId);
      
      proc.execute();

      index = 1;
      margin.setMarginId(new Integer(proc.getInt(index++)));
      margin.setRevisionCount(new Integer(proc.getInt(index++)));
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  /**
   * @param   transaction    transaction
   * @param   mt  mt
   * @param   scenarioId  scenarioId
   * @param   userId  userId
   *
   * @throws  DataAccessException  on exception
   */
  public void writeMarginTotal(
  	final Transaction transaction,
    final MarginTotal mt,
    final Integer scenarioId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + MARGIN_TOTAL_PROC;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    final int paramCount = 40;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index++, Types.INTEGER);
      proc.registerOutParameter(index++, Types.INTEGER);

      index = 1;
      proc.setInt(index++, mt.getMarginTotalId());
      proc.setInt(index++, mt.getRevisionCount());
      
      proc.setDouble(index++, mt.getTotalAllowableExpenses());
      proc.setDouble(index++, mt.getTotalAllowableIncome());
      proc.setDouble(index++, mt.getUnadjustedProductionMargin());
      proc.setDouble(index++, mt.getProductionMargAccrAdjs());
      proc.setDouble(index++, mt.getProductionMargAftStrChangs());
      proc.setDouble(index++, mt.getAccrualAdjsCropInventory());
      proc.setDouble(index++, mt.getAccrualAdjsLvstckInventory());
      proc.setDouble(index++, mt.getAccrualAdjsPayables());
      proc.setDouble(index++, mt.getAccrualAdjsPurchasedInputs());
      proc.setDouble(index++, mt.getAccrualAdjsReceivables());
      proc.setDouble(index++, mt.getUnadjustedAllowableIncome());
      proc.setDouble(index++, mt.getYardageIncome());
      proc.setDouble(index++, mt.getProgramPaymentIncome());
      proc.setDouble(index++, mt.getSupplyManagedCommodityIncome());
      proc.setDouble(index++, mt.getTotalUnallowableIncome());
      proc.setDouble(index++, mt.getUnadjustedAllowableExpenses());
      proc.setDouble(index++, mt.getYardageExpenses());
      proc.setDouble(index++, mt.getContractWorkExpenses());
      proc.setDouble(index++, mt.getManualExpenses());
      proc.setDouble(index++, mt.getTotalUnallowableExpenses());
      proc.setDouble(index++, mt.getDeferredProgramPayments());
      proc.setDouble(index++, mt.getExpenseAccrualAdjs());
      proc.setDouble(index++, mt.getExpenseStructuralChangeAdjs());
      proc.setDouble(index++, mt.getExpensesAfterStructuralChange());
      proc.setDouble(index++, mt.getStructuralChangeAdjs());
      proc.setDouble(index++, mt.getFiscalYearProRateAdj());
      proc.setDouble(index++, mt.getFarmSizeRatio());
      proc.setDouble(index++, mt.getExpenseFarmSizeRatio());
      proc.setIndicator(index++, mt.getIsStructuralChangeNotable());
      proc.setIndicator(index++, mt.getBpuLeadInd());
      proc.setDouble(index++, mt.getRatioStructuralChangeAdjs());
      proc.setDouble(index++, mt.getAdditiveStructuralChangeAdjs());
      proc.setDouble(index++, mt.getRatioProductionMargAftStrChangs());
      proc.setDouble(index++, mt.getAdditiveProductionMargAftStrChangs());
      proc.setIndicator(index++, mt.getIsRatioStructuralChangeNotable());
      proc.setIndicator(index++, mt.getIsAdditiveStructuralChangeNotable());
      
      proc.setInt(index++, scenarioId);
      proc.setString(index++, userId);
      
      proc.execute();

      index = 1;
      mt.setMarginTotalId(new Integer(proc.getInt(index++)));
      mt.setRevisionCount(new Integer(proc.getInt(index++)));
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  /**
   * @param   transaction    transaction
   * @param   rs  rs
   * @param   programYearScenarioId  programYearScenarioId
   * @param   userId  userId
   *
   * @throws  DataAccessException  on exception
   */
  public void writeReferenceScenario(
  	final Transaction transaction,
    final ReferenceScenario rs,
    final Integer programYearScenarioId,
    final String userId) 
  throws DataAccessException {
    String procName = PACKAGE_NAME + "." + WRITE_REFERENCE_SCENARIO;

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    DAOStoredProcedure proc = null;

    final int paramCount = 5;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setInt(index++, programYearScenarioId);
      proc.setInt(index++, rs.getScenarioId());
      proc.setIndicator(index++, rs.getUsedInCalc());
      proc.setIndicator(index++, rs.getIsDeemedFarmingYear());
      proc.setString(index++, userId);
      
      proc.execute();
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
    }
  }
  
  
  
  @SuppressWarnings("resource")
  public void writeFinalVerificationNotes(
  	final Transaction transaction,
  	final String comments,
    final Integer programYearId,
    final String userId)
  throws DataAccessException, IOException {
    DAOStoredProcedure proc = null;
    ResultSet resultSet = null;
    Connection connection = getConnection(transaction);
    Clob clob = null;
    final int paramCount = 2;
    String procName = PACKAGE_NAME + "." + WRITE_FINAL_VERIFICATION_NOTES;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setInt(index++, programYearId);
      proc.setString(index++, userId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        clob = resultSet.getClob(1);
        try(Writer writer = clob.setCharacterStream(0);) {
          writer.write(comments);
          writer.flush();
        }
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }
  }
  
  
  @SuppressWarnings("resource")
  public void writeInterimVerificationNotes(
    final Transaction transaction,
    final String comments,
    final Integer programYearId,
    final String userId)
  throws DataAccessException, IOException {
    DAOStoredProcedure proc = null;
    ResultSet resultSet = null;
    Connection connection = getConnection(transaction);
    Clob clob = null;
    final int paramCount = 2;
    String procName = PACKAGE_NAME + "." + WRITE_INTERIM_VERIFICATION_NOTES;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setInt(index++, programYearId);
      proc.setString(index++, userId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        clob = resultSet.getClob(1);
        try(Writer writer = clob.setCharacterStream(0);) {
          writer.write(comments);
          writer.flush();
        }
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }
  }
  
  
  @SuppressWarnings("resource")
  public void writeAdjustmentVerificationNotes(
    final Transaction transaction,
    final String comments,
    final Integer programYearId,
    final String userId)
  throws DataAccessException, IOException {
    DAOStoredProcedure proc = null;
    ResultSet resultSet = null;
    Connection connection = getConnection(transaction);
    Clob clob = null;
    final int paramCount = 2;
    String procName = PACKAGE_NAME + "." + WRITE_ADJUSTMENT_VERIFICATION_NOTES;

    try {
      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setInt(index++, programYearId);
      proc.setString(index++, userId);
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        clob = resultSet.getClob(1);
        try(Writer writer = clob.setCharacterStream(0);) {
          writer.write(comments);
          writer.flush();
        }
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    } finally {
      close(resultSet, proc);
    }
  }
}