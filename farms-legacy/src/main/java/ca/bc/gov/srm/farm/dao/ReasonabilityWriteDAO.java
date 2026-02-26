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
package ca.bc.gov.srm.farm.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskProductiveUnit;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.production.FruitVegProductionResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskFruitVegItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 */
public class ReasonabilityWriteDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_REASONABILITY_WRITE_PKG";
  
  
  private static final String UPDATE_REASONABILITY_TESTS_PROC = "UPDATE_REASONABILITY_TESTS";
  private static final int UPDATE_REASONABILITY_TESTS_PARAM = 66;

  private static final String DELETE_REASONABILITY_RESULTS_PROC = "DELETE_REASONABILITY_RESULTS";
  private static final int DELETE_REASONABILITY_RESULTS_PARAM = 1;
  
  private static final String CREATE_FARM_RSNBLTY_BNFT_RSK_PU_PROC = "CREATE_FARM_RSNBLTY_BNFT_RSK_PU";
  private static final int CREATE_FARM_RSNBLTY_BNFT_RSK_PU_PARAM = 9;
  
  private static final String FLAG_REASONABILITY_TESTS_STALE_PROC = "FLAG_REASONABILITY_TESTS_STALE";
  private static final int FLAG_REASONABILITY_TESTS_STALE_PARAM = 2;

  private static final String UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS_PROC = "UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS";
  private static final int UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS_PARAM = 10;
  
  private static final String CREATE_RSNBLTY_TEST_MESSAGE_PROC = "CREATE_RSNBLTY_TEST_MESSAGE";
  private static final int CREATE_RSNBLTY_TEST_MESSAGE_PARAM = 5;
  
  private static final String UPDATE_RSN_PRDCTN_FRUT_INVNTRIES_PROC = "UPDATE_RSN_PRDCTN_FRUT_INVNTRIES";
  private static final int UPDATE_RSN_PRDCTN_FRUT_INVNTRIES_PARAM = 8;
  
  private static final String UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS_PROC = "UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS";
  private static final int UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS_PARAM = 9;
  
  private static final String UPDATE_FARM_RSNBLTY_PRDCTN_GRAINS_RSLTS_PROC = "UPDATE_FARM_RSNBLTY_PRDCTN_GRAIN_RSLTS";
  private static final int UPDATE_FARM_RSNBLTY_PRDCTN_GRAINS_RSLTS_PARAM = 10;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PROC = "UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS";
  private static final int UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PARAM = 11;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PROC = "UPDATE_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS";
  private static final int UPDATE_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PARAM = 7;
  
  private static final String UPDATE_RSN_FORAGE_CONSUMERS_PROC = "UPDATE_RSN_FORAGE_CONSUMERS";
  private static final int UPDATE_RSN_FORAGE_CONSUMERS_PARAM = 6;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PROCS = "UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY";
  private static final int UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PARAM = 7;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS_PROCS = "UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS";
  private static final int UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS_PARAM = 11;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS_PROC = "UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS";
  private static final int UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS_PARAM = 7;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES_PROC = "UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES";
  private static final int UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES_PARAM = 4;
  
  private static final String UPDATE_FARM_RSNBLTY_REV_NRSRY_INVNTRIES_PROC = "UPDATE_FARM_RSNBLTY_REV_NRSRY_INVNTRIES";
  private static final int UPDATE_FARM_RSNBLTY_REV_NRSRY_INVNTRIES_PARAM = 10;
  
  private static final String INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PROC = "INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS";
  private static final int INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PARAM = 24;
  
  private static final String INSERT_FARM_RSNBLTY_REV_EGG_RSLTS_PROC = "INSERT_FARM_RSNBLTY_REV_EGG_RSLTS";
  private static final int INSERT_FARM_RSNBLTY_REV_EGG_RSLTS_PARAM = 24;
  
  private static final String INSERT_FARM_RSN_REV_HOG_RSLTS_PROC = "INSERT_FARM_RSN_REV_HOG_RSLTS";
  private static final int INSERT_FARM_RSN_REV_HOG_RSLTS_PARAM = 33;
  
  private static final String INSERT_FARM_RSN_REV_HOG_INVTRY_PROC = "INSERT_FARM_RSN_REV_HOG_INVTRY";
  private static final int INSERT_FARM_RSN_REV_HOG_INVTRY_PARAM = 6;
  
  
  private void createReasonabilityTestMessages(
      final Transaction transaction,
      final ReasonabilityTestResult result,
      final Integer reasonabilityTestResultId,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + CREATE_RSNBLTY_TEST_MESSAGE_PROC, CREATE_RSNBLTY_TEST_MESSAGE_PARAM, false);) {      
        
        int param = 1;
        
        List<ReasonabilityTestResultMessage> messages = new ArrayList<>();
        messages.addAll(result.getErrorMessages());
        messages.addAll(result.getWarningMessages());
        messages.addAll(result.getInfoMessages());
        
        for (ReasonabilityTestResultMessage message : messages) {
          param = 1;
          
          proc.setString(param++, result.getName());
          proc.setString(param++, message.getMessage());
          proc.setString(param++, message.getMessageTypeCode());
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());        
          proc.setString(param++, user);
          
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }  
  
  
  private void deleteReasonabilityResults(
      final Transaction transaction,
      final Integer reasonabilityTestResultId)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + DELETE_REASONABILITY_RESULTS_PROC, DELETE_REASONABILITY_RESULTS_PARAM, false);) {      
        
        int param = 1;
              
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.execute();            
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  private void updateFruitVegProductionTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final ProductionTestResult productionTest,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_RSN_PRDCTN_FRUT_INVNTRIES_PROC, UPDATE_RSN_PRDCTN_FRUT_INVNTRIES_PARAM, false);) {      
        
        int param = 1;
        
        List<ProductionInventoryItemTestResult> testResults = productionTest.getFruitVegInventoryItems();
        
        for (ProductionInventoryItemTestResult inventoryResult : testResults) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, inventoryResult.getProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(inventoryResult.getProductiveCapacityAmount()));
          proc.setBigDecimal(param++, inventoryResult.getReportedProduction() == null ? null : BigDecimal.valueOf(inventoryResult.getReportedProduction()));
          proc.setBigDecimal(param++, inventoryResult.getExpectedProductionPerUnit() == null ? null : BigDecimal.valueOf(inventoryResult.getExpectedProductionPerUnit()));
          proc.setBigDecimal(param++, inventoryResult.getExpectedQuantityProduced() == null ? null : BigDecimal.valueOf(inventoryResult.getExpectedQuantityProduced()));
          proc.setString(param++, inventoryResult.getInventoryItemCode());
          proc.setString(param++, inventoryResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS_PROC, UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS_PARAM, false);) {      
        
        int param = 1;
        
        List<FruitVegProductionResult> testResults = productionTest.getFruitVegTestResults();
              
        for (FruitVegProductionResult testResult : testResults) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, testResult.getProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(testResult.getProductiveCapacityAmount()));
          proc.setBigDecimal(param++, testResult.getReportedProduction() == null ? null : BigDecimal.valueOf(testResult.getReportedProduction()));
          proc.setBigDecimal(param++, testResult.getExpectedQuantityProduced() == null ? null : BigDecimal.valueOf(testResult.getExpectedQuantityProduced()));
          proc.setBigDecimal(param++, testResult.getVariance() == null ? null : BigDecimal.valueOf(testResult.getVariance()));
          proc.setIndicator(param++, testResult.getPass());
          proc.setString(param++, testResult.getFruitVegTypeCode());
          proc.setString(param++, testResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  private void updateGrainProductionTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final ProductionTestResult productionTest,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_FARM_RSNBLTY_PRDCTN_GRAINS_RSLTS_PROC, UPDATE_FARM_RSNBLTY_PRDCTN_GRAINS_RSLTS_PARAM, false);) {      
        
        int param = 1;
        
        List<ProductionInventoryItemTestResult> testResults = productionTest.getGrainItemTestResults();
              
        for (ProductionInventoryItemTestResult testResult : testResults) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, testResult.getProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(testResult.getProductiveCapacityAmount()));
          proc.setBigDecimal(param++, testResult.getReportedProduction() == null ? null : BigDecimal.valueOf(testResult.getReportedProduction()));
          proc.setBigDecimal(param++, testResult.getExpectedProductionPerUnit() == null ? null : BigDecimal.valueOf(testResult.getExpectedProductionPerUnit()));
          proc.setBigDecimal(param++, testResult.getExpectedQuantityProduced() == null ? null : BigDecimal.valueOf(testResult.getExpectedQuantityProduced()));
          proc.setBigDecimal(param++, testResult.getVariance() == null ? null : BigDecimal.valueOf(testResult.getVariance()));
          proc.setIndicator(param++, testResult.getPass());
          proc.setString(param++, testResult.getInventoryItemCode());
          proc.setString(param++, testResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  private void updateForageProductionTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final ProductionTestResult productionTest,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS_PROC, UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS_PARAM, false);) {      
        
        int param = 1;
        
        List<ProductionInventoryItemTestResult> forageAndForageSeedResults = new ArrayList<>();
        forageAndForageSeedResults.addAll(productionTest.getForageTestResults());
        forageAndForageSeedResults.addAll(productionTest.getForageSeedTestResults());
              
        for (ProductionInventoryItemTestResult testResult : forageAndForageSeedResults) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, testResult.getProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(testResult.getProductiveCapacityAmount()));
          proc.setBigDecimal(param++, testResult.getReportedProduction() == null ? null : BigDecimal.valueOf(testResult.getReportedProduction()));
          proc.setBigDecimal(param++, testResult.getExpectedProductionPerUnit() == null ? null : BigDecimal.valueOf(testResult.getExpectedProductionPerUnit()));
          proc.setBigDecimal(param++, testResult.getExpectedQuantityProduced() == null ? null : BigDecimal.valueOf(testResult.getExpectedQuantityProduced()));
          proc.setBigDecimal(param++, testResult.getVariance() == null ? null : BigDecimal.valueOf(testResult.getVariance()));
          proc.setIndicator(param++, testResult.getPass());
          proc.setString(param++, testResult.getInventoryItemCode());
          proc.setString(param++, testResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  private void updateForageAndGrainsRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PROC, UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PARAM, false);) {      
      
        int param = 1;
        List<RevenueRiskInventoryItem> forageGrainInventory = revenueRiskTest.getForageGrainInventory();
              
        for (RevenueRiskInventoryItem testResult : forageGrainInventory) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, testResult.getQuantityProduced() == null ? null : BigDecimal.valueOf(testResult.getQuantityProduced()));
          proc.setBigDecimal(param++, testResult.getQuantityStart() == null ? null : BigDecimal.valueOf(testResult.getQuantityStart()));
          proc.setBigDecimal(param++, testResult.getQuantityEnd() == null ? null : BigDecimal.valueOf(testResult.getQuantityEnd()));
          proc.setBigDecimal(param++, testResult.getQuantityConsumed() == null ? null : BigDecimal.valueOf(testResult.getQuantityConsumed()));
          proc.setBigDecimal(param++, testResult.getQuantitySold() == null ? null : BigDecimal.valueOf(testResult.getQuantitySold()));
          proc.setBigDecimal(param++, testResult.getExpectedRevenue() == null ? null : BigDecimal.valueOf(testResult.getExpectedRevenue()));
          proc.setBigDecimal(param++, testResult.getReportedPrice() == null ? null : BigDecimal.valueOf(testResult.getReportedPrice()));
          proc.setString(param++, testResult.getInventoryItemCode());
          proc.setString(param++, testResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }
      
      
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PROC, UPDATE_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PARAM, false);) {      
     
        int param = 1;
        
        List<RevenueRiskIncomeTestResult> forageGrainIncomes = revenueRiskTest.getForageGrainIncomes();
        
        for (RevenueRiskIncomeTestResult forageGrainIncomeItem : forageGrainIncomes) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, forageGrainIncomeItem.getReportedRevenue() == null ? null : BigDecimal.valueOf(forageGrainIncomeItem.getReportedRevenue()));
          proc.setBigDecimal(param++, forageGrainIncomeItem.getExpectedRevenue() == null ? null : BigDecimal.valueOf(forageGrainIncomeItem.getExpectedRevenue()));
          proc.setBigDecimal(param++, forageGrainIncomeItem.getVariance() == null ? null : BigDecimal.valueOf(forageGrainIncomeItem.getVariance()));
          proc.setIndicator(param++, forageGrainIncomeItem.getPass());
          proc.setShort(param++, forageGrainIncomeItem.getLineItemCode() == null ? null : forageGrainIncomeItem.getLineItemCode().shortValue());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  private void updateFruitVegRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PROCS, UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PARAM, false);) {      
        
        int param = 1;
        List<RevenueRiskInventoryItem> results = revenueRiskTest.getFruitVegInventory();
              
        for (RevenueRiskInventoryItem inventoryResult : results) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, inventoryResult.getQuantityProduced() == null ? null : BigDecimal.valueOf(inventoryResult.getQuantityProduced()));
          proc.setBigDecimal(param++, inventoryResult.getFmvPrice() == null ? null : BigDecimal.valueOf(inventoryResult.getFmvPrice()));
          proc.setBigDecimal(param++, inventoryResult.getExpectedRevenue() == null ? null : BigDecimal.valueOf(inventoryResult.getExpectedRevenue()));
          proc.setString(param++, inventoryResult.getInventoryItemCode());
          proc.setString(param++, inventoryResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS_PROCS, UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS_PARAM, false);) {      
        
        int param = 1;
        List<RevenueRiskFruitVegItemTestResult> results = revenueRiskTest.getFruitVegResults();
        
        for (RevenueRiskFruitVegItemTestResult testResult : results) {
          param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, testResult.getReportedRevenue() == null ? null : BigDecimal.valueOf(testResult.getReportedRevenue()));
          proc.setBigDecimal(param++, testResult.getQuantityProduced() == null ? null : BigDecimal.valueOf(testResult.getQuantityProduced()));
          proc.setBigDecimal(param++, testResult.getExpectedPrice() == null ? null : BigDecimal.valueOf(testResult.getExpectedPrice()));
          proc.setBigDecimal(param++, testResult.getExpectedRevenue() == null ? null : BigDecimal.valueOf(testResult.getExpectedRevenue()));
          proc.setBigDecimal(param++, testResult.getVariance() == null ? null : BigDecimal.valueOf(testResult.getVariance()));
          proc.setIndicator(param++, testResult.getPass());
          proc.setBigDecimal(param++, testResult.getVarianceLimit() == null ? null : BigDecimal.valueOf(testResult.getVarianceLimit()));
          proc.setString(param++, testResult.getFruitVegTypeCode());
          proc.setString(param++, testResult.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  @SuppressWarnings("resource")
  private void updateNurseryRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
      
      // Nursery main results
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS_PROC, UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS_PARAM, false);) {      
        
        int param = 1;
        proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
        proc.setIndicator(param++, revenueRiskTest.getNursery().getSubTestPass());
        proc.setBigDecimal(param++, revenueRiskTest.getNursery().getReportedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getNursery().getReportedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getNursery().getExpectedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getNursery().getExpectedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getNursery().getVariance() == null ? null : BigDecimal.valueOf(revenueRiskTest.getNursery().getVariance()));
        proc.setBigDecimal(param++, revenueRiskTest.getNursery().getVarianceLimit() == null ? null : BigDecimal.valueOf(revenueRiskTest.getNursery().getVarianceLimit()));
        proc.setString(param++, user);  
        
        proc.execute();
      }
      
      
      // Nursery incomes
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES_PROC, UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES_PARAM, false);) {      
             
        List<RevenueRiskIncomeTestResult> nurseryIncomes = revenueRiskTest.getNursery().getIncomes();
        
        for (RevenueRiskIncomeTestResult nurseryIncomeItem : nurseryIncomes) {
          int param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setShort(param++, nurseryIncomeItem.getLineItemCode() == null ? null : nurseryIncomeItem.getLineItemCode().shortValue());
          proc.setBigDecimal(param++, nurseryIncomeItem.getReportedRevenue() == null ? null : BigDecimal.valueOf(nurseryIncomeItem.getReportedRevenue()));
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }
      
      
      // Nursery inventory
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_NRSRY_INVNTRIES_PROC, UPDATE_FARM_RSNBLTY_REV_NRSRY_INVNTRIES_PARAM, false);) {      
             
        List<RevenueRiskInventoryItem> nurseryInvItems = revenueRiskTest.getNursery().getInventory();
        
        for (RevenueRiskInventoryItem nurseryInvItem : nurseryInvItems) {
          int param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, nurseryInvItem.getQuantityProduced() == null ? null : BigDecimal.valueOf(nurseryInvItem.getQuantityProduced()));
          proc.setBigDecimal(param++, nurseryInvItem.getQuantityStart() == null ? null : BigDecimal.valueOf(nurseryInvItem.getQuantityStart()));
          proc.setBigDecimal(param++, nurseryInvItem.getQuantityEnd() == null ? null : BigDecimal.valueOf(nurseryInvItem.getQuantityEnd()));
          proc.setBigDecimal(param++, nurseryInvItem.getQuantitySold() == null ? null : BigDecimal.valueOf(nurseryInvItem.getQuantitySold()));
          proc.setBigDecimal(param++, nurseryInvItem.getExpectedRevenue() == null ? null : BigDecimal.valueOf(nurseryInvItem.getExpectedRevenue()));
          proc.setBigDecimal(param++, nurseryInvItem.getFmvPrice() == null ? null : BigDecimal.valueOf(nurseryInvItem.getFmvPrice()));
          proc.setString(param++, nurseryInvItem.getInventoryItemCode());
          proc.setString(param++, nurseryInvItem.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  @SuppressWarnings("resource")
  private void updatePoultryBroilersRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PROC, INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PARAM, false);) {
            
        int param = 1;
        proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
        proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getHasPoultryBroilers());
        proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getSubTestPass());
        proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getHasChickens());
        proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getChickenPass());
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenAverageWeightKg() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenAverageWeightKg()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenExpectedSoldCount() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenExpectedSoldCount()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenPricePerBird() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenPricePerBird()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenExpectedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenExpectedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenReportedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenReportedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenKgProduced() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenKgProduced()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenVariance() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenVariance()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getChickenVarianceLimit() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getChickenVarianceLimit()));
        proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getHasTurkeys());
        proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getTurkeyPass());
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyAverageWeightKg() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyAverageWeightKg()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyExpectedSoldCount() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyExpectedSoldCount()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyPricePerBird() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyPricePerBird()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyExpectedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyExpectedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyReportedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyReportedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyKgProduced() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyKgProduced()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyVariance() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyVariance()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryBroilers().getTurkeyVarianceLimit() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryBroilers().getTurkeyVarianceLimit()));
        proc.setString(param++, user);  
        
        proc.execute();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
  }
  
  
  @SuppressWarnings("resource")
  private void updatePoultryEggsRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
          throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + INSERT_FARM_RSNBLTY_REV_EGG_RSLTS_PROC, INSERT_FARM_RSNBLTY_REV_EGG_RSLTS_PARAM, false);) {
        
        int param = 1;
        proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
        proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getHasPoultryEggs());
        proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getSubTestPass());
        proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getConsumptionPass());
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionLayers() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionLayers()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionAverageEggsPerLayer() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionAverageEggsPerLayer()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionEggsTotal() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionEggsTotal()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionEggsDozen() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionEggsDozen()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionEggsDozenPrice() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionEggsDozenPrice()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionExpectedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionExpectedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionReportedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionReportedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionVariance() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionVariance()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getConsumptionVarianceLimit() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getConsumptionVarianceLimit()));
        proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getHatchingPass());
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingLayers() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingLayers()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingAverageEggsPerLayer() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingAverageEggsPerLayer()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingEggsTotal() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingEggsTotal()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingEggsDozen() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingEggsDozen()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingEggsDozenPrice() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingEggsDozenPrice()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingExpectedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingExpectedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingReportedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingReportedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingVariance() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingVariance()));
        proc.setBigDecimal(param++, revenueRiskTest.getPoultryEggs().getHatchingVarianceLimit() == null ? null : BigDecimal.valueOf(revenueRiskTest.getPoultryEggs().getHatchingVarianceLimit()));
        proc.setString(param++, user);  
        
        proc.execute();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
  }
  
  
  @SuppressWarnings("resource")
  private void updateHogsRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);
      
      // Hogs main results
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + INSERT_FARM_RSN_REV_HOG_RSLTS_PROC, INSERT_FARM_RSN_REV_HOG_RSLTS_PARAM, false);) {      
        
        int param = 1;
        proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getHasHogs());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getHogsPass());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getFarrowToFinishOperation());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getFeederOperation());
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getReportedExpenses() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getReportedExpenses()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getTotalQuantityStart() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getTotalQuantityStart()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getTotalQuantityEnd() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getTotalQuantityEnd()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getSowsBreeding() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getSowsBreeding()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getBirthsPerCycle() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getBirthsPerCycle()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getBirthCyclesPerYear() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getBirthCyclesPerYear()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getTotalBirthsPerCycle() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getTotalBirthsPerCycle()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getTotalBirthsAllCycles() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getTotalBirthsAllCycles()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getDeathRate() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getDeathRate()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getDeaths() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getDeaths()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getBoarPurchaseCount() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getBoarPurchaseCount()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getBoarPurchasePrice() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getBoarPurchasePrice()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getBoarPurchaseExpense() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getBoarPurchaseExpense()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getSowPurchaseExpense() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getSowPurchaseExpense()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getSowPurchaseCount() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getSowPurchaseCount()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getSowPurchasePrice() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getSowPurchasePrice()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getFeederProductiveUnits() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getFeederProductiveUnits()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getWeanlingPurchaseExpense() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getWeanlingPurchaseExpense()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getWeanlingPurchasePrice() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getWeanlingPurchasePrice()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getWeanlingPurchaseCount() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getWeanlingPurchaseCount()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getTotalPurchaseCount() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getTotalPurchaseCount()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getExpectedSold() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getExpectedSold()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getHeaviestHogFmvPrice() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getHeaviestHogFmvPrice()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getReportedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getReportedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getExpectedRevenue() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getExpectedRevenue()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getRevenueVariance() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getRevenueVariance()));
        proc.setBigDecimal(param++, revenueRiskTest.getHogs().getVarianceLimit() == null ? null : BigDecimal.valueOf(revenueRiskTest.getHogs().getVarianceLimit()));
        proc.setString(param++, user);  
        
        proc.execute();
      }
      
      
      // Insert Hogs Inventory
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + INSERT_FARM_RSN_REV_HOG_INVTRY_PROC, INSERT_FARM_RSN_REV_HOG_INVTRY_PARAM, false);) {      
             
        List<RevenueRiskInventoryItem> hogsInvItems = revenueRiskTest.getHogs().getInventory();
        
        for (RevenueRiskInventoryItem hogsInvItem : hogsInvItems) {
          int param = 1;
          proc.setLong(param++, reasonabilityTestResultId == null ? null : reasonabilityTestResultId.longValue());
          proc.setBigDecimal(param++, hogsInvItem.getQuantityStart() == null ? null : BigDecimal.valueOf(hogsInvItem.getQuantityStart()));
          proc.setBigDecimal(param++, hogsInvItem.getQuantityEnd() == null ? null : BigDecimal.valueOf(hogsInvItem.getQuantityEnd()));
          proc.setBigDecimal(param++, hogsInvItem.getFmvPrice() == null ? null : BigDecimal.valueOf(hogsInvItem.getFmvPrice()));
          proc.setString(param++, hogsInvItem.getInventoryItemCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  private void updateForageConsumers(
      final Transaction transaction,
      final ReasonabilityTestResults results,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_RSN_FORAGE_CONSUMERS_PROC, UPDATE_RSN_FORAGE_CONSUMERS_PARAM, false);) {      
        
        int param = 1;
        
        List<ForageConsumer> forageConsumers = results.getForageConsumers();
        
        for (ForageConsumer forageGrainIncomeItem : forageConsumers) {
          param = 1;
          proc.setLong(param++, results.getReasonabilityTestResultId() == null ? null : results.getReasonabilityTestResultId().longValue());
          proc.setBigDecimal(param++, forageGrainIncomeItem.getProductiveUnitCapacity() == null ? null : BigDecimal.valueOf(forageGrainIncomeItem.getProductiveUnitCapacity()));
          proc.setBigDecimal(param++, forageGrainIncomeItem.getQuantityConsumedPerUnit() == null ? null : BigDecimal.valueOf(forageGrainIncomeItem.getQuantityConsumedPerUnit()));
          proc.setBigDecimal(param++, forageGrainIncomeItem.getQuantityConsumed() == null ? null : BigDecimal.valueOf(forageGrainIncomeItem.getQuantityConsumed()));
          proc.setString(param++, forageGrainIncomeItem.getStructureGroupCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  public void updateReasonabilityTests(
      final Transaction transaction,
      final ReasonabilityTestResults results,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      if (results.getReasonabilityTestResultId() != null) {
        deleteReasonabilityResults(transaction, results.getReasonabilityTestResultId());
      }
      
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_REASONABILITY_TESTS_PROC, UPDATE_REASONABILITY_TESTS_PARAM, false);) {
      
        int param = 1;
        proc.registerOutParameter(param++, Types.INTEGER);
  
        param = 1;
        proc.setLong(param++, results.getReasonabilityTestResultId() == null ? null : results.getReasonabilityTestResultId().longValue());
        proc.setIndicator(param++, results.getIsFresh());
        proc.setBigDecimal(param++, results.getForageConsumerCapacity() == null ? null : BigDecimal.valueOf(results.getForageConsumerCapacity()));
        proc.setIndicator(param++, results.getBenefitRisk().getResult());
        proc.setBigDecimal(param++, results.getBenefitRisk().getProgramYearMargin() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getProgramYearMargin()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getTotalBenefit() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getTotalBenefit()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenchmarkMargin() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenchmarkMargin()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenefitRiskDeductible() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenefitRiskDeductible()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenefitBenchmarkLessDeductible() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenefitBenchmarkLessDeductible()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenefitBenchmarkLessProgramYearMargin() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenefitBenchmarkLessProgramYearMargin()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenefitRiskPayoutLevel() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenefitRiskPayoutLevel()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenefitBenchmarkBeforeCombinedFarmPercent() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenefitBenchmarkBeforeCombinedFarmPercent()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getCombinedFarmPercent() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getCombinedFarmPercent()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getBenefitBenchmark() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getBenefitBenchmark()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getVariance() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getVariance()));
        proc.setBigDecimal(param++, results.getBenefitRisk().getVarianceLimit() == null ? null : BigDecimal.valueOf(results.getBenefitRisk().getVarianceLimit()));
        proc.setIndicator(param++, results.getMarginTest().getResult());
        proc.setBigDecimal(param++, results.getMarginTest().getAdjustedReferenceMargin() == null ? null : BigDecimal.valueOf(results.getMarginTest().getAdjustedReferenceMargin()));
        proc.setBigDecimal(param++, results.getMarginTest().getAdjustedReferenceMarginVariance() == null ? null : BigDecimal.valueOf(results.getMarginTest().getAdjustedReferenceMarginVariance()));
        proc.setBigDecimal(param++, results.getMarginTest().getAdjustedReferenceMarginVarianceLimit() == null ? null : BigDecimal.valueOf(results.getMarginTest().getAdjustedReferenceMarginVarianceLimit()));
        proc.setIndicator(param++, results.getMarginTest().getWithinLimitOfReferenceMargin());
        proc.setBigDecimal(param++, results.getMarginTest().getIndustryAverage() == null ? null : BigDecimal.valueOf(results.getMarginTest().getIndustryAverage()));
        proc.setBigDecimal(param++, results.getMarginTest().getIndustryVariance() == null ? null : BigDecimal.valueOf(results.getMarginTest().getIndustryVariance()));
        proc.setBigDecimal(param++, results.getMarginTest().getIndustryVarianceLimit() == null ? null : BigDecimal.valueOf(results.getMarginTest().getIndustryVarianceLimit()));
        proc.setIndicator(param++, results.getMarginTest().getWithinLimitOfIndustryAverage());
        proc.setIndicator(param++, results.getStructuralChangeTest().getResult());
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getProductionMargAccrAdjs() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getProductionMargAccrAdjs()));
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getRatioReferenceMargin() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getRatioReferenceMargin()));
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getAdditiveReferenceMargin() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getAdditiveReferenceMargin()));
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getRatioAdditiveDiffVariance() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getRatioAdditiveDiffVariance()));
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getRatioAdditiveDiffVarianceLimit() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getRatioAdditiveDiffVarianceLimit()));
        proc.setIndicator(param++, results.getStructuralChangeTest().getWithinRatioAdditiveDiffLimit());
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getAdditiveDivisionRatio() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getAdditiveDivisionRatio()));
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getAdditiveDivisionRatioLimit() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getAdditiveDivisionRatioLimit()));
        proc.setIndicator(param++, results.getStructuralChangeTest().getWithinAdditiveDivisionLimit());
        proc.setString(param++, results.getStructuralChangeTest().getMethodToUse());
        proc.setBigDecimal(param++, results.getStructuralChangeTest().getFarmSizeRatioLimit() == null ? null : BigDecimal.valueOf(results.getStructuralChangeTest().getFarmSizeRatioLimit()));
        proc.setIndicator(param++, results.getStructuralChangeTest().getWithinFarmSizeRatioLimit());
        proc.setIndicator(param++, results.getExpenseTestIAC().getResult());
        proc.setBigDecimal(param++, results.getExpenseTestIAC().getExpenseAccrualAdjs() == null ? null : BigDecimal.valueOf(results.getExpenseTestIAC().getExpenseAccrualAdjs()));
        proc.setBigDecimal(param++, results.getExpenseTestIAC().getIndustryAverage() == null ? null : BigDecimal.valueOf(results.getExpenseTestIAC().getIndustryAverage()));
        proc.setBigDecimal(param++, results.getExpenseTestIAC().getIndustryVariance() == null ? null : BigDecimal.valueOf(results.getExpenseTestIAC().getIndustryVariance()));
        proc.setBigDecimal(param++, results.getExpenseTestIAC().getIndustryVarianceLimit() == null ? null : BigDecimal.valueOf(results.getExpenseTestIAC().getIndustryVarianceLimit()));
        proc.setLong(param++, results.getScenario().getScenarioId() == null ? null : results.getScenario().getScenarioId().longValue());
        proc.setIndicator(param++, results.getProductionTest().getResult());
        proc.setIndicator(param++, results.getProductionTest().isPassForageAndForageSeedTest());
        proc.setIndicator(param++, results.getProductionTest().getPassFruitVegTest());
        proc.setBigDecimal(param++, results.getProductionTest().getForageProducedVarianceLimit() == null ? null : BigDecimal.valueOf(results.getProductionTest().getForageProducedVarianceLimit()));
        proc.setBigDecimal(param++, results.getProductionTest().getFruitVegProducedVarianceLimit() == null ? null : BigDecimal.valueOf(results.getProductionTest().getFruitVegProducedVarianceLimit()));
        proc.setBigDecimal(param++, results.getProductionTest().getGrainProducedVarianceLimit() == null ? null : BigDecimal.valueOf(results.getProductionTest().getGrainProducedVarianceLimit()));
        proc.setIndicator(param++, results.getProductionTest().getPassGrainTest());
        proc.setIndicator(param++, results.getExpenseTestRefYearCompGC().getResult());
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getProgramYearAcrAdjExpense() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getProgramYearAcrAdjExpense()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus1() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus1()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus2() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus2()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus3() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus3()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus4() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus4()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus5() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus5()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getExpenseStructrualChangeAverage() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getExpenseStructrualChangeAverage()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getVariance() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getVariance()));
        proc.setBigDecimal(param++, results.getExpenseTestRefYearCompGC().getVarianceLimit() == null ? null : BigDecimal.valueOf(results.getExpenseTestRefYearCompGC().getVarianceLimit()));
        proc.setIndicator(param++, results.getRevenueRiskTest().getForageGrainPass());
        proc.setIndicator(param++, results.getRevenueRiskTest().getResult());
        proc.setBigDecimal(param++, results.getRevenueRiskTest().getForageGrainVarianceLimit() == null ? null : BigDecimal.valueOf(results.getRevenueRiskTest().getForageGrainVarianceLimit()));
        proc.setIndicator(param++, results.getRevenueRiskTest().getFruitVegTestPass());
        proc.setString(param++, user);
        proc.execute();
      
        param = 1;
        results.setReasonabilityTestResultId(new Integer(proc.getInt(param++)));
      }
      
      updateForageConsumers(transaction, results, user);
      connection.setAutoCommit(false);
      
      // Create Productive Units for the test
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FARM_RSNBLTY_BNFT_RSK_PU_PROC, CREATE_FARM_RSNBLTY_BNFT_RSK_PU_PARAM, false);) {
      
        for(BenefitRiskProductiveUnit unit : results.getBenefitRisk().getBenefitRiskProductiveUnits()) {
          
          int param = 1;
          proc.setLong(param++, results.getReasonabilityTestResultId() == null ? null : results.getReasonabilityTestResultId().longValue());
          proc.setBigDecimal(param++, unit.getReportedProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(unit.getReportedProductiveCapacityAmount()));
          proc.setBigDecimal(param++, unit.getConsumedProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(unit.getConsumedProductiveCapacityAmount()));
          proc.setBigDecimal(param++, unit.getNetProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(unit.getNetProductiveCapacityAmount()));
          proc.setBigDecimal(param++, unit.getBpuCalculated() == null ? null : BigDecimal.valueOf(unit.getBpuCalculated()));
          proc.setBigDecimal(param++, unit.getEstimatedIncome() == null ? null : BigDecimal.valueOf(unit.getEstimatedIncome()));
          proc.setString(param++, unit.getInventoryItemCode());
          proc.setString(param++, unit.getStructureGroupCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }
      
      // Update (or create) production test results
      updateForageProductionTest(transaction, results.getReasonabilityTestResultId(), results.getProductionTest(), user);
      updateGrainProductionTest(transaction, results.getReasonabilityTestResultId(), results.getProductionTest(), user);
      updateFruitVegProductionTest(transaction, results.getReasonabilityTestResultId(), results.getProductionTest(), user);
      
      // Revenue Risk Tests
      updateForageAndGrainsRevenueTest(transaction, results.getReasonabilityTestResultId(), results.getRevenueRiskTest(), user);
      updateFruitVegRevenueTest(transaction, results.getReasonabilityTestResultId(), results.getRevenueRiskTest(), user);
      updateNurseryRevenueTest(transaction, results.getReasonabilityTestResultId(), results.getRevenueRiskTest(), user);
      updateHogsRevenueTest(transaction, results.getReasonabilityTestResultId(), results.getRevenueRiskTest(), user);
      updatePoultryBroilersRevenueTest(transaction, results.getReasonabilityTestResultId(), results.getRevenueRiskTest(), user);
      updatePoultryEggsRevenueTest(transaction, results.getReasonabilityTestResultId(), results.getRevenueRiskTest(), user);
      
      for(ReasonabilityTestResult testResult : results.getTestResults()) {
        createReasonabilityTestMessages(transaction, testResult, results.getReasonabilityTestResultId(), user);
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
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
  }
  
  
  public void flagReasonabilityTestsStale(
      final Transaction transaction,
      final ReasonabilityTestResults results,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    
    if(results != null && results.getReasonabilityTestResultId() != null) {
      try {
        originalAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
              + FLAG_REASONABILITY_TESTS_STALE_PROC, FLAG_REASONABILITY_TESTS_STALE_PARAM, false);) {
          
          int param = 1;
          proc.setLong(param++, results.getReasonabilityTestResultId() == null ? null : results.getReasonabilityTestResultId().longValue());
          proc.setString(param++, user);
          proc.execute();
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
        try {
          connection.setAutoCommit(originalAutoCommit);
        } catch (SQLException ex) {
          handleException(ex);
        }
      }
    }
    
  }

}
