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
import java.math.BigDecimal;
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

  private static final String PACKAGE_NAME = "FARMS_WRITE_PKG";

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
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;

    final int paramCount = 3;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
      proc.setString(index++, version);
      proc.setString(index++, userId);
      
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;

    final int paramCount = 68;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index++, Types.BIGINT);
      proc.registerOutParameter(index++, Types.INTEGER);

      index = 1;
      proc.setLong(index++, benefit.getClaimId() == null ? null : benefit.getClaimId().longValue());
      proc.setInt(index++, benefit.getRevisionCount());
      
      proc.setBigDecimal(index++, benefit.getAdministrativeCostShare() == null ? null : BigDecimal.valueOf(benefit.getAdministrativeCostShare()));
      proc.setBigDecimal(index++, benefit.getLateApplicationPenalty() == null ? null : BigDecimal.valueOf(benefit.getLateApplicationPenalty()));
      proc.setBigDecimal(index++, benefit.getMaximumContribution() == null ? null : BigDecimal.valueOf(benefit.getMaximumContribution()));
      proc.setBigDecimal(index++, benefit.getOutstandingFees() == null ? null : BigDecimal.valueOf(benefit.getOutstandingFees()));
      proc.setBigDecimal(index++, benefit.getProgramYearMargin() == null ? null : BigDecimal.valueOf(benefit.getProgramYearMargin()));
      proc.setBigDecimal(index++, benefit.getProdInsurDeemedBenefit() == null ? null : BigDecimal.valueOf(benefit.getProdInsurDeemedBenefit()));
      proc.setIndicator(index++, benefit.getProdInsurDeemedBenefitManuallyCalculated());
      proc.setBigDecimal(index++, benefit.getProgramYearPaymentsReceived() == null ? null : BigDecimal.valueOf(benefit.getProgramYearPaymentsReceived()));
      proc.setBigDecimal(index++, benefit.getAllocatedReferenceMargin() == null ? null : BigDecimal.valueOf(benefit.getAllocatedReferenceMargin()));
      proc.setBigDecimal(index++, benefit.getRepaymentOfCashAdvances() == null ? null : BigDecimal.valueOf(benefit.getRepaymentOfCashAdvances()));
      proc.setBigDecimal(index++, benefit.getTotalPayment() == null ? null : BigDecimal.valueOf(benefit.getTotalPayment()));
      proc.setBigDecimal(index++, benefit.getSupplyManagedCommoditiesAdj() == null ? null : BigDecimal.valueOf(benefit.getSupplyManagedCommoditiesAdj()));
      proc.setBigDecimal(index++, benefit.getProducerShare() == null ? null : BigDecimal.valueOf(benefit.getProducerShare()));
      proc.setBigDecimal(index++, benefit.getFederalContributions() == null ? null : BigDecimal.valueOf(benefit.getFederalContributions()));
      proc.setBigDecimal(index++, benefit.getProvincialContributions() == null ? null : BigDecimal.valueOf(benefit.getProvincialContributions()));
      proc.setBigDecimal(index++, benefit.getInterimContributions() == null ? null : BigDecimal.valueOf(benefit.getInterimContributions()));
      proc.setBigDecimal(index++, benefit.getWholeFarmAllocation() == null ? null : BigDecimal.valueOf(benefit.getWholeFarmAllocation()));
      proc.setBigDecimal(index++, benefit.getTier2Trigger() == null ? null : BigDecimal.valueOf(benefit.getTier2Trigger()));
      proc.setBigDecimal(index++, benefit.getTier2MarginDecline() == null ? null : BigDecimal.valueOf(benefit.getTier2MarginDecline()));
      proc.setBigDecimal(index++, benefit.getTier2Benefit() == null ? null : BigDecimal.valueOf(benefit.getTier2Benefit()));
      proc.setBigDecimal(index++, benefit.getTier3Trigger() == null ? null : BigDecimal.valueOf(benefit.getTier3Trigger()));
      proc.setBigDecimal(index++, benefit.getTier3MarginDecline() == null ? null : BigDecimal.valueOf(benefit.getTier3MarginDecline()));
      proc.setBigDecimal(index++, benefit.getTier3Benefit() == null ? null : BigDecimal.valueOf(benefit.getTier3Benefit()));
      proc.setBigDecimal(index++, benefit.getMarginDecline() == null ? null : BigDecimal.valueOf(benefit.getMarginDecline()));
      proc.setBigDecimal(index++, benefit.getNegativeMarginDecline() == null ? null : BigDecimal.valueOf(benefit.getNegativeMarginDecline()));
      proc.setBigDecimal(index++, benefit.getNegativeMarginBenefit() == null ? null : BigDecimal.valueOf(benefit.getNegativeMarginBenefit()));
      proc.setBigDecimal(index++, benefit.getTotalBenefit() == null ? null : BigDecimal.valueOf(benefit.getTotalBenefit()));
      proc.setBigDecimal(index++, benefit.getAdjustedReferenceMargin() == null ? null : BigDecimal.valueOf(benefit.getAdjustedReferenceMargin()));
      proc.setBigDecimal(index++, benefit.getUnadjustedReferenceMargin() == null ? null : BigDecimal.valueOf(benefit.getUnadjustedReferenceMargin()));
      proc.setBigDecimal(index++, benefit.getReferenceMarginLimit() == null ? null : BigDecimal.valueOf(benefit.getReferenceMarginLimit()));
      proc.setBigDecimal(index++, benefit.getReferenceMarginLimitCap() == null ? null : BigDecimal.valueOf(benefit.getReferenceMarginLimitCap()));
      proc.setBigDecimal(index++, benefit.getReferenceMarginLimitForBenefitCalc() == null ? null : BigDecimal.valueOf(benefit.getReferenceMarginLimitForBenefitCalc()));
      proc.setBigDecimal(index++, benefit.getBenefitBeforeDeductions() == null ? null : BigDecimal.valueOf(benefit.getBenefitBeforeDeductions()));
      proc.setBigDecimal(index++, benefit.getBenefitAfterProdInsDeduction() == null ? null : BigDecimal.valueOf(benefit.getBenefitAfterProdInsDeduction()));
      proc.setBigDecimal(index++, benefit.getAppliedBenefitPercent() == null ? null : BigDecimal.valueOf(benefit.getAppliedBenefitPercent()));
      proc.setBigDecimal(index++, benefit.getBenefitAfterAppliedBenefitPercent() == null ? null : BigDecimal.valueOf(benefit.getBenefitAfterAppliedBenefitPercent()));
      proc.setBigDecimal(index++, benefit.getInterimBenefitPercent() == null ? null : BigDecimal.valueOf(benefit.getInterimBenefitPercent()));
      proc.setBigDecimal(index++, benefit.getBenefitAfterInterimDeduction() == null ? null : BigDecimal.valueOf(benefit.getBenefitAfterInterimDeduction()));
      proc.setBigDecimal(index++, benefit.getPaymentCap() == null ? null : BigDecimal.valueOf(benefit.getPaymentCap()));
      proc.setBigDecimal(index++, benefit.getBenefitAfterPaymentCap() == null ? null : BigDecimal.valueOf(benefit.getBenefitAfterPaymentCap()));
      proc.setBigDecimal(index++, benefit.getLateEnrolmentPenalty() == null ? null : BigDecimal.valueOf(benefit.getLateEnrolmentPenalty()));
      proc.setBigDecimal(index++, benefit.getBenefitAfterLateEnrolmentPenalty() == null ? null : BigDecimal.valueOf(benefit.getBenefitAfterLateEnrolmentPenalty()));
      proc.setBigDecimal(index++, benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent() == null ? null : BigDecimal.valueOf(benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      
      proc.setBigDecimal(index++, benefit.getStandardBenefit() == null ? null : BigDecimal.valueOf(benefit.getStandardBenefit()));
      proc.setBigDecimal(index++, benefit.getEnhancedReferenceMarginForBenefitCalculation() == null ? null : BigDecimal.valueOf(benefit.getEnhancedReferenceMarginForBenefitCalculation()));
      proc.setBigDecimal(index++, benefit.getEnhancedMarginDecline() == null ? null : BigDecimal.valueOf(benefit.getEnhancedMarginDecline()));
      proc.setBigDecimal(index++, benefit.getEnhancedPositiveMarginDecline() == null ? null : BigDecimal.valueOf(benefit.getEnhancedPositiveMarginDecline()));
      proc.setBigDecimal(index++, benefit.getEnhancedPositiveMarginBenefit() == null ? null : BigDecimal.valueOf(benefit.getEnhancedPositiveMarginBenefit()));
      proc.setBigDecimal(index++, benefit.getEnhancedNegativeMarginDecline() == null ? null : BigDecimal.valueOf(benefit.getEnhancedNegativeMarginDecline()));
      proc.setBigDecimal(index++, benefit.getEnhancedNegativeMarginBenefit() == null ? null : BigDecimal.valueOf(benefit.getEnhancedNegativeMarginBenefit()));
      proc.setBigDecimal(index++, benefit.getEnhancedBenefitBeforeDeductions() == null ? null : BigDecimal.valueOf(benefit.getEnhancedBenefitBeforeDeductions()));
      proc.setBigDecimal(index++, benefit.getEnhancedBenefitAfterProdInsDeduction() == null ? null : BigDecimal.valueOf(benefit.getEnhancedBenefitAfterProdInsDeduction()));
      proc.setBigDecimal(index++, benefit.getEnhancedBenefitAfterInterimDeduction() == null ? null : BigDecimal.valueOf(benefit.getEnhancedBenefitAfterInterimDeduction()));
      proc.setBigDecimal(index++, benefit.getEnhancedBenefitAfterPaymentCap() == null ? null : BigDecimal.valueOf(benefit.getEnhancedBenefitAfterPaymentCap()));
      proc.setBigDecimal(index++, benefit.getEnhancedTotalBenefit() == null ? null : BigDecimal.valueOf(benefit.getEnhancedTotalBenefit()));
      proc.setBigDecimal(index++, benefit.getEnhancedAdditionalBenefit() == null ? null : BigDecimal.valueOf(benefit.getEnhancedAdditionalBenefit()));
      proc.setBigDecimal(index++, benefit.getEnhancedLateEnrolmentPenalty() == null ? null : BigDecimal.valueOf(benefit.getEnhancedLateEnrolmentPenalty()));
      proc.setBigDecimal(index++, benefit.getEnhancedBenefitAfterLateEnrolmentPenalty() == null ? null : BigDecimal.valueOf(benefit.getEnhancedBenefitAfterLateEnrolmentPenalty()));
      proc.setBigDecimal(index++, benefit.getEnhancedBenefitAfterAppliedBenefitPercent() == null ? null : BigDecimal.valueOf(benefit.getEnhancedBenefitAfterAppliedBenefitPercent()));
      proc.setBigDecimal(index++, benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent() == null ? null : BigDecimal.valueOf(benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent()));
      proc.setBigDecimal(index++, benefit.getRatioAdjustedReferenceMargin() == null ? null : BigDecimal.valueOf(benefit.getRatioAdjustedReferenceMargin()));
      proc.setBigDecimal(index++, benefit.getAdditiveAdjustedReferenceMargin() == null ? null : BigDecimal.valueOf(benefit.getAdditiveAdjustedReferenceMargin()));
      
      proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
      proc.setString(index++, benefit.getStructuralChangeMethodCode());
      proc.setString(index++, benefit.getExpenseStructuralChangeMethodCode());
      proc.setString(index++, userId);
      
      proc.execute();

      index = 1;
      benefit.setClaimId(new Integer((int)proc.getLong(index++)));
      benefit.setRevisionCount(new Integer(proc.getInt(index++)));

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;

    final int paramCount = 28;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index++, Types.BIGINT);
      proc.registerOutParameter(index++, Types.INTEGER);

      index = 1;
      proc.setLong(index++, margin.getMarginId() == null ? null : margin.getMarginId().longValue());
      proc.setInt(index++, margin.getRevisionCount());
      
      proc.setBigDecimal(index++, margin.getTotalAllowableExpenses() == null ? null : BigDecimal.valueOf(margin.getTotalAllowableExpenses()));
      proc.setBigDecimal(index++, margin.getTotalAllowableIncome() == null ? null : BigDecimal.valueOf(margin.getTotalAllowableIncome()));
      proc.setBigDecimal(index++, margin.getUnadjustedProductionMargin() == null ? null : BigDecimal.valueOf(margin.getUnadjustedProductionMargin()));
      proc.setBigDecimal(index++, margin.getProductionMargAccrAdjs() == null ? null : BigDecimal.valueOf(margin.getProductionMargAccrAdjs()));
      proc.setBigDecimal(index++, margin.getAccrualAdjsCropInventory() == null ? null : BigDecimal.valueOf(margin.getAccrualAdjsCropInventory()));
      proc.setBigDecimal(index++, margin.getAccrualAdjsLvstckInventory() == null ? null : BigDecimal.valueOf(margin.getAccrualAdjsLvstckInventory()));
      proc.setBigDecimal(index++, margin.getAccrualAdjsPayables() == null ? null : BigDecimal.valueOf(margin.getAccrualAdjsPayables()));
      proc.setBigDecimal(index++, margin.getAccrualAdjsPurchasedInputs() == null ? null : BigDecimal.valueOf(margin.getAccrualAdjsPurchasedInputs()));
      proc.setBigDecimal(index++, margin.getAccrualAdjsReceivables() == null ? null : BigDecimal.valueOf(margin.getAccrualAdjsReceivables()));
      proc.setBigDecimal(index++, margin.getUnadjustedAllowableIncome() == null ? null : BigDecimal.valueOf(margin.getUnadjustedAllowableIncome()));
      proc.setBigDecimal(index++, margin.getYardageIncome() == null ? null : BigDecimal.valueOf(margin.getYardageIncome()));
      proc.setBigDecimal(index++, margin.getProgramPaymentIncome() == null ? null : BigDecimal.valueOf(margin.getProgramPaymentIncome()));
      proc.setBigDecimal(index++, margin.getSupplyManagedCommodityIncome() == null ? null : BigDecimal.valueOf(margin.getSupplyManagedCommodityIncome()));
      proc.setBigDecimal(index++, margin.getTotalUnallowableIncome() == null ? null : BigDecimal.valueOf(margin.getTotalUnallowableIncome()));
      proc.setBigDecimal(index++, margin.getUnadjustedAllowableExpenses() == null ? null : BigDecimal.valueOf(margin.getUnadjustedAllowableExpenses()));
      proc.setBigDecimal(index++, margin.getYardageExpenses() == null ? null : BigDecimal.valueOf(margin.getYardageExpenses()));
      proc.setBigDecimal(index++, margin.getContractWorkExpenses() == null ? null : BigDecimal.valueOf(margin.getContractWorkExpenses()));
      proc.setBigDecimal(index++, margin.getManualExpenses() == null ? null : BigDecimal.valueOf(margin.getManualExpenses()));
      proc.setBigDecimal(index++, margin.getTotalUnallowableExpenses() == null ? null : BigDecimal.valueOf(margin.getTotalUnallowableExpenses()));
      proc.setBigDecimal(index++, margin.getDeferredProgramPayments() == null ? null : BigDecimal.valueOf(margin.getDeferredProgramPayments()));
      proc.setBigDecimal(index++, margin.getExpenseAccrualAdjs() == null ? null : BigDecimal.valueOf(margin.getExpenseAccrualAdjs()));
      proc.setBigDecimal(index++, margin.getProdInsurDeemedSubtotal() == null ? null : BigDecimal.valueOf(margin.getProdInsurDeemedSubtotal()));
      proc.setBigDecimal(index++, margin.getProdInsurDeemedTotal() == null ? null : BigDecimal.valueOf(margin.getProdInsurDeemedTotal()));
      proc.setLong(index++, operationId == null ? null : operationId.longValue());
      proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
      proc.setString(index++, userId);
      
      proc.execute();

      index = 1;
      margin.setMarginId(new Integer((int)proc.getLong(index++)));
      margin.setRevisionCount(new Integer(proc.getInt(index++)));

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;

    final int paramCount = 40;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.registerOutParameter(index++, Types.BIGINT);
      proc.registerOutParameter(index++, Types.INTEGER);

      index = 1;
      proc.setLong(index++, mt.getMarginTotalId() == null ? null : mt.getMarginTotalId().longValue());
      proc.setInt(index++, mt.getRevisionCount());
      
      proc.setBigDecimal(index++, mt.getTotalAllowableExpenses() == null ? null : BigDecimal.valueOf(mt.getTotalAllowableExpenses()));
      proc.setBigDecimal(index++, mt.getTotalAllowableIncome() == null ? null : BigDecimal.valueOf(mt.getTotalAllowableIncome()));
      proc.setBigDecimal(index++, mt.getUnadjustedProductionMargin() == null ? null : BigDecimal.valueOf(mt.getUnadjustedProductionMargin()));
      proc.setBigDecimal(index++, mt.getProductionMargAccrAdjs() == null ? null : BigDecimal.valueOf(mt.getProductionMargAccrAdjs()));
      proc.setBigDecimal(index++, mt.getProductionMargAftStrChangs() == null ? null : BigDecimal.valueOf(mt.getProductionMargAftStrChangs()));
      proc.setBigDecimal(index++, mt.getAccrualAdjsCropInventory() == null ? null : BigDecimal.valueOf(mt.getAccrualAdjsCropInventory()));
      proc.setBigDecimal(index++, mt.getAccrualAdjsLvstckInventory() == null ? null : BigDecimal.valueOf(mt.getAccrualAdjsLvstckInventory()));
      proc.setBigDecimal(index++, mt.getAccrualAdjsPayables() == null ? null : BigDecimal.valueOf(mt.getAccrualAdjsPayables()));
      proc.setBigDecimal(index++, mt.getAccrualAdjsPurchasedInputs() == null ? null : BigDecimal.valueOf(mt.getAccrualAdjsPurchasedInputs()));
      proc.setBigDecimal(index++, mt.getAccrualAdjsReceivables() == null ? null : BigDecimal.valueOf(mt.getAccrualAdjsReceivables()));
      proc.setBigDecimal(index++, mt.getUnadjustedAllowableIncome() == null ? null : BigDecimal.valueOf(mt.getUnadjustedAllowableIncome()));
      proc.setBigDecimal(index++, mt.getYardageIncome() == null ? null : BigDecimal.valueOf(mt.getYardageIncome()));
      proc.setBigDecimal(index++, mt.getProgramPaymentIncome() == null ? null : BigDecimal.valueOf(mt.getProgramPaymentIncome()));
      proc.setBigDecimal(index++, mt.getSupplyManagedCommodityIncome() == null ? null : BigDecimal.valueOf(mt.getSupplyManagedCommodityIncome()));
      proc.setBigDecimal(index++, mt.getTotalUnallowableIncome() == null ? null : BigDecimal.valueOf(mt.getTotalUnallowableIncome()));
      proc.setBigDecimal(index++, mt.getUnadjustedAllowableExpenses() == null ? null : BigDecimal.valueOf(mt.getUnadjustedAllowableExpenses()));
      proc.setBigDecimal(index++, mt.getYardageExpenses() == null ? null : BigDecimal.valueOf(mt.getYardageExpenses()));
      proc.setBigDecimal(index++, mt.getContractWorkExpenses() == null ? null : BigDecimal.valueOf(mt.getContractWorkExpenses()));
      proc.setBigDecimal(index++, mt.getManualExpenses() == null ? null : BigDecimal.valueOf(mt.getManualExpenses()));
      proc.setBigDecimal(index++, mt.getTotalUnallowableExpenses() == null ? null : BigDecimal.valueOf(mt.getTotalUnallowableExpenses()));
      proc.setBigDecimal(index++, mt.getDeferredProgramPayments() == null ? null : BigDecimal.valueOf(mt.getDeferredProgramPayments()));
      proc.setBigDecimal(index++, mt.getExpenseAccrualAdjs() == null ? null : BigDecimal.valueOf(mt.getExpenseAccrualAdjs()));
      proc.setBigDecimal(index++, mt.getExpenseStructuralChangeAdjs() == null ? null : BigDecimal.valueOf(mt.getExpenseStructuralChangeAdjs()));
      proc.setBigDecimal(index++, mt.getExpensesAfterStructuralChange() == null ? null : BigDecimal.valueOf(mt.getExpensesAfterStructuralChange()));
      proc.setBigDecimal(index++, mt.getStructuralChangeAdjs() == null ? null : BigDecimal.valueOf(mt.getStructuralChangeAdjs()));
      proc.setBigDecimal(index++, mt.getFiscalYearProRateAdj() == null ? null : BigDecimal.valueOf(mt.getFiscalYearProRateAdj()));
      proc.setBigDecimal(index++, mt.getFarmSizeRatio() == null ? null : BigDecimal.valueOf(mt.getFarmSizeRatio()));
      proc.setBigDecimal(index++, mt.getExpenseFarmSizeRatio() == null ? null : BigDecimal.valueOf(mt.getExpenseFarmSizeRatio()));
      proc.setIndicator(index++, mt.getIsStructuralChangeNotable());
      proc.setIndicator(index++, mt.getBpuLeadInd());
      proc.setBigDecimal(index++, mt.getRatioStructuralChangeAdjs() == null ? null : BigDecimal.valueOf(mt.getRatioStructuralChangeAdjs()));
      proc.setBigDecimal(index++, mt.getAdditiveStructuralChangeAdjs() == null ? null : BigDecimal.valueOf(mt.getAdditiveStructuralChangeAdjs()));
      proc.setBigDecimal(index++, mt.getRatioProductionMargAftStrChangs() == null ? null : BigDecimal.valueOf(mt.getRatioProductionMargAftStrChangs()));
      proc.setBigDecimal(index++, mt.getAdditiveProductionMargAftStrChangs() == null ? null : BigDecimal.valueOf(mt.getAdditiveProductionMargAftStrChangs()));
      proc.setIndicator(index++, mt.getIsRatioStructuralChangeNotable());
      proc.setIndicator(index++, mt.getIsAdditiveStructuralChangeNotable());
      
      proc.setLong(index++, scenarioId == null ? null : scenarioId.longValue());
      proc.setString(index++, userId);
      
      proc.execute();

      index = 1;
      mt.setMarginTotalId(new Integer((int)proc.getLong(index++)));
      mt.setRevisionCount(new Integer(proc.getInt(index++)));

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;

    final int paramCount = 5;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setInt(index++, programYearScenarioId);
      proc.setInt(index++, rs.getScenarioId());
      proc.setIndicator(index++, rs.getUsedInCalc());
      proc.setIndicator(index++, rs.getIsDeemedFarmingYear());
      proc.setString(index++, userId);
      
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    final int paramCount = 3;
    String procName = PACKAGE_NAME + "." + WRITE_FINAL_VERIFICATION_NOTES;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setInt(index++, programYearId);
      proc.setString(index++, comments);
      proc.setString(index++, userId);
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(resultSet, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    Clob clob = null;
    final int paramCount = 2;
    String procName = PACKAGE_NAME + "." + WRITE_INTERIM_VERIFICATION_NOTES;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

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

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(resultSet, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
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
    boolean originalAutoCommit = true;
    Clob clob = null;
    final int paramCount = 2;
    String procName = PACKAGE_NAME + "." + WRITE_ADJUSTMENT_VERIFICATION_NOTES;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

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

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(resultSet, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
}