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
        proc.setInt(param++, reasonabilityTestResultId);        
        proc.setString(param++, user);
        
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }    
  }  
  
  
  private void deleteReasonabilityResults(
      final Transaction transaction,
      final Integer reasonabilityTestResultId)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + DELETE_REASONABILITY_RESULTS_PROC, DELETE_REASONABILITY_RESULTS_PARAM, false);) {      
      
      int param = 1;
            
        proc.setInt(param++, reasonabilityTestResultId);
        proc.execute();            
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + UPDATE_RSN_PRDCTN_FRUT_INVNTRIES_PROC, UPDATE_RSN_PRDCTN_FRUT_INVNTRIES_PARAM, false);) {      
      
      int param = 1;
      
      List<ProductionInventoryItemTestResult> testResults = productionTest.getFruitVegInventoryItems();
      
      for (ProductionInventoryItemTestResult inventoryResult : testResults) {
        param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setDouble(param++, inventoryResult.getProductiveCapacityAmount());
        proc.setDouble(param++, inventoryResult.getReportedProduction());
        proc.setDouble(param++, inventoryResult.getExpectedProductionPerUnit());
        proc.setDouble(param++, inventoryResult.getExpectedQuantityProduced());
        proc.setString(param++, inventoryResult.getInventoryItemCode());
        proc.setString(param++, inventoryResult.getCropUnitCode());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }    
    
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS_PROC, UPDATE_RSNBLTY_PRDCTN_FRUIT_VEG_RSLTS_PARAM, false);) {      
      
      int param = 1;
      
      List<FruitVegProductionResult> testResults = productionTest.getFruitVegTestResults();
            
      for (FruitVegProductionResult testResult : testResults) {
        param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setDouble(param++, testResult.getProductiveCapacityAmount());
        proc.setDouble(param++, testResult.getReportedProduction());
        proc.setDouble(param++, testResult.getExpectedQuantityProduced());
        proc.setDouble(param++, testResult.getVariance());
        proc.setIndicator(param++, testResult.getPass());
        proc.setString(param++, testResult.getFruitVegTypeCode());
        proc.setString(param++, testResult.getCropUnitCode());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_PRDCTN_GRAINS_RSLTS_PROC, UPDATE_FARM_RSNBLTY_PRDCTN_GRAINS_RSLTS_PARAM, false);) {      
      
      int param = 1;
      
      List<ProductionInventoryItemTestResult> testResults = productionTest.getGrainItemTestResults();
            
      for (ProductionInventoryItemTestResult testResult : testResults) {
        param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setDouble(param++, testResult.getProductiveCapacityAmount());
        proc.setDouble(param++, testResult.getReportedProduction());
        proc.setDouble(param++, testResult.getExpectedProductionPerUnit());
        proc.setDouble(param++, testResult.getExpectedQuantityProduced());
        proc.setDouble(param++, testResult.getVariance());
        proc.setIndicator(param++, testResult.getPass());
        proc.setString(param++, testResult.getInventoryItemCode());
        proc.setString(param++, testResult.getCropUnitCode());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS_PROC, UPDATE_FARM_RSNBLTY_PRDCTN_FORAGE_RSLTS_PARAM, false);) {      
      
      int param = 1;
      
      List<ProductionInventoryItemTestResult> forageAndForageSeedResults = new ArrayList<>();
      forageAndForageSeedResults.addAll(productionTest.getForageTestResults());
      forageAndForageSeedResults.addAll(productionTest.getForageSeedTestResults());
            
      for (ProductionInventoryItemTestResult testResult : forageAndForageSeedResults) {
        param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setDouble(param++, testResult.getProductiveCapacityAmount());
        proc.setDouble(param++, testResult.getReportedProduction());
        proc.setDouble(param++, testResult.getExpectedProductionPerUnit());
        proc.setDouble(param++, testResult.getExpectedQuantityProduced());
        proc.setDouble(param++, testResult.getVariance());
        proc.setIndicator(param++, testResult.getPass());
        proc.setString(param++, testResult.getInventoryItemCode());
        proc.setString(param++, testResult.getCropUnitCode());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try {
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PROC, UPDATE_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PARAM, false);) {      
      
        int param = 1;
        List<RevenueRiskInventoryItem> forageGrainInventory = revenueRiskTest.getForageGrainInventory();
              
        for (RevenueRiskInventoryItem testResult : forageGrainInventory) {
          param = 1;
          proc.setInt(param++, reasonabilityTestResultId);
          proc.setDouble(param++, testResult.getQuantityProduced());
          proc.setDouble(param++, testResult.getQuantityStart());
          proc.setDouble(param++, testResult.getQuantityEnd());
          proc.setDouble(param++, testResult.getQuantityConsumed());
          proc.setDouble(param++, testResult.getQuantitySold());
          proc.setDouble(param++, testResult.getExpectedRevenue());
          proc.setDouble(param++, testResult.getReportedPrice());
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
          proc.setInt(param++, reasonabilityTestResultId);
          proc.setDouble(param++, forageGrainIncomeItem.getReportedRevenue());
          proc.setDouble(param++, forageGrainIncomeItem.getExpectedRevenue());
          proc.setDouble(param++, forageGrainIncomeItem.getVariance());
          proc.setIndicator(param++, forageGrainIncomeItem.getPass());
          proc.setString(param++, forageGrainIncomeItem.getLineItemCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }
      
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PROCS, UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PARAM, false);) {      
      
      int param = 1;
      List<RevenueRiskInventoryItem> results = revenueRiskTest.getFruitVegInventory();
            
      for (RevenueRiskInventoryItem inventoryResult : results) {
        param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setDouble(param++, inventoryResult.getQuantityProduced());
        proc.setDouble(param++, inventoryResult.getFmvPrice());
        proc.setDouble(param++, inventoryResult.getExpectedRevenue());
        proc.setString(param++, inventoryResult.getInventoryItemCode());
        proc.setString(param++, inventoryResult.getCropUnitCode());
        proc.setString(param++, user);
        proc.addBatch();
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }    
    
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS_PROCS, UPDATE_FARM_RSNBLTY_REV_FRUIT_VEG_RESULTS_PARAM, false);) {      
      
      int param = 1;
      List<RevenueRiskFruitVegItemTestResult> results = revenueRiskTest.getFruitVegResults();
      
      for (RevenueRiskFruitVegItemTestResult testResult : results) {
        param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setDouble(param++, testResult.getReportedRevenue());
        proc.setDouble(param++, testResult.getQuantityProduced());
        proc.setDouble(param++, testResult.getExpectedPrice());
        proc.setDouble(param++, testResult.getExpectedRevenue());
        proc.setDouble(param++, testResult.getVariance());
        proc.setIndicator(param++, testResult.getPass());
        proc.setDouble(param++, testResult.getVarianceLimit());
        proc.setString(param++, testResult.getFruitVegTypeCode());
        proc.setString(param++, testResult.getCropUnitCode());
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
  private void updateNurseryRevenueTest(
      final Transaction transaction,
      final Integer reasonabilityTestResultId,
      final RevenueRiskTestResult revenueRiskTest,
      final String user)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    
    try {
      
      // Nursery main results
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS_PROC, UPDATE_FARM_RSNBLTY_REV_NRSRY_RESULTS_PARAM, false);) {      
        
        int param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setIndicator(param++, revenueRiskTest.getNursery().getSubTestPass());
        proc.setDouble(param++, revenueRiskTest.getNursery().getReportedRevenue());
        proc.setDouble(param++, revenueRiskTest.getNursery().getExpectedRevenue());
        proc.setDouble(param++, revenueRiskTest.getNursery().getVariance());
        proc.setDouble(param++, revenueRiskTest.getNursery().getVarianceLimit());
        proc.setString(param++, user);  
        
        proc.execute();
      }
      
      
      // Nursery incomes
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES_PROC, UPDATE_FARM_RSNBLTY_REV_NRSRY_INCOMES_PARAM, false);) {      
             
        List<RevenueRiskIncomeTestResult> nurseryIncomes = revenueRiskTest.getNursery().getIncomes();
        
        for (RevenueRiskIncomeTestResult nurseryIncomeItem : nurseryIncomes) {
          int param = 1;
          proc.setInt(param++, reasonabilityTestResultId);
          proc.setInt(param++, nurseryIncomeItem.getLineItemCode());
          proc.setDouble(param++, nurseryIncomeItem.getReportedRevenue());
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
          proc.setInt(param++, reasonabilityTestResultId);
          proc.setDouble(param++, nurseryInvItem.getQuantityProduced());
          proc.setDouble(param++, nurseryInvItem.getQuantityStart());
          proc.setDouble(param++, nurseryInvItem.getQuantityEnd());
          proc.setDouble(param++, nurseryInvItem.getQuantitySold());
          proc.setDouble(param++, nurseryInvItem.getExpectedRevenue());
          proc.setDouble(param++, nurseryInvItem.getFmvPrice());
          proc.setString(param++, nurseryInvItem.getInventoryItemCode());
          proc.setString(param++, nurseryInvItem.getCropUnitCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PROC, INSERT_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PARAM, false);) {
          
      int param = 1;
      proc.setInt(param++, reasonabilityTestResultId);
      proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getHasPoultryBroilers());
      proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getSubTestPass());
      proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getHasChickens());
      proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getChickenPass());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenAverageWeightKg());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenExpectedSoldCount());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenPricePerBird());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenExpectedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenReportedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenKgProduced());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenVariance());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getChickenVarianceLimit());
      proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getHasTurkeys());
      proc.setIndicator(param++, revenueRiskTest.getPoultryBroilers().getTurkeyPass());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyAverageWeightKg());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyExpectedSoldCount());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyPricePerBird());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyExpectedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyReportedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyKgProduced());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyVariance());
      proc.setDouble(param++, revenueRiskTest.getPoultryBroilers().getTurkeyVarianceLimit());
      proc.setString(param++, user);  
      
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
        + INSERT_FARM_RSNBLTY_REV_EGG_RSLTS_PROC, INSERT_FARM_RSNBLTY_REV_EGG_RSLTS_PARAM, false);) {
      
      int param = 1;
      proc.setInt(param++, reasonabilityTestResultId);
      proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getHasPoultryEggs());
      proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getSubTestPass());
      proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getConsumptionPass());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionLayers());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionAverageEggsPerLayer());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionEggsTotal());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionEggsDozen());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionEggsDozenPrice());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionExpectedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionReportedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionVariance());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getConsumptionVarianceLimit());
      proc.setIndicator(param++, revenueRiskTest.getPoultryEggs().getHatchingPass());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingLayers());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingAverageEggsPerLayer());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingEggsTotal());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingEggsDozen());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingEggsDozenPrice());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingExpectedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingReportedRevenue());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingVariance());
      proc.setDouble(param++, revenueRiskTest.getPoultryEggs().getHatchingVarianceLimit());
      proc.setString(param++, user);  
      
      proc.execute();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
    
    try {
      
      // Hogs main results
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + INSERT_FARM_RSN_REV_HOG_RSLTS_PROC, INSERT_FARM_RSN_REV_HOG_RSLTS_PARAM, false);) {      
        
        int param = 1;
        proc.setInt(param++, reasonabilityTestResultId);
        proc.setIndicator(param++, revenueRiskTest.getHogs().getHasHogs());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getHogsPass());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getFarrowToFinishOperation());
        proc.setIndicator(param++, revenueRiskTest.getHogs().getFeederOperation());
        proc.setDouble(param++, revenueRiskTest.getHogs().getReportedExpenses());
        proc.setDouble(param++, revenueRiskTest.getHogs().getTotalQuantityStart());
        proc.setDouble(param++, revenueRiskTest.getHogs().getTotalQuantityEnd());
        proc.setDouble(param++, revenueRiskTest.getHogs().getSowsBreeding());
        proc.setDouble(param++, revenueRiskTest.getHogs().getBirthsPerCycle());
        proc.setDouble(param++, revenueRiskTest.getHogs().getBirthCyclesPerYear());
        proc.setDouble(param++, revenueRiskTest.getHogs().getTotalBirthsPerCycle());
        proc.setDouble(param++, revenueRiskTest.getHogs().getTotalBirthsAllCycles());
        proc.setDouble(param++, revenueRiskTest.getHogs().getDeathRate());
        proc.setDouble(param++, revenueRiskTest.getHogs().getDeaths());
        proc.setDouble(param++, revenueRiskTest.getHogs().getBoarPurchaseCount());
        proc.setDouble(param++, revenueRiskTest.getHogs().getBoarPurchasePrice());
        proc.setDouble(param++, revenueRiskTest.getHogs().getBoarPurchaseExpense());
        proc.setDouble(param++, revenueRiskTest.getHogs().getSowPurchaseExpense());
        proc.setDouble(param++, revenueRiskTest.getHogs().getSowPurchaseCount());
        proc.setDouble(param++, revenueRiskTest.getHogs().getSowPurchasePrice());
        proc.setDouble(param++, revenueRiskTest.getHogs().getFeederProductiveUnits());
        proc.setDouble(param++, revenueRiskTest.getHogs().getWeanlingPurchaseExpense());
        proc.setDouble(param++, revenueRiskTest.getHogs().getWeanlingPurchasePrice());
        proc.setDouble(param++, revenueRiskTest.getHogs().getWeanlingPurchaseCount());
        proc.setDouble(param++, revenueRiskTest.getHogs().getTotalPurchaseCount());
        proc.setDouble(param++, revenueRiskTest.getHogs().getExpectedSold());
        proc.setDouble(param++, revenueRiskTest.getHogs().getHeaviestHogFmvPrice());
        proc.setDouble(param++, revenueRiskTest.getHogs().getReportedRevenue());
        proc.setDouble(param++, revenueRiskTest.getHogs().getExpectedRevenue());
        proc.setDouble(param++, revenueRiskTest.getHogs().getRevenueVariance());
        proc.setDouble(param++, revenueRiskTest.getHogs().getVarianceLimit());
        proc.setString(param++, user);  
        
        proc.execute();
      }
      
      
      // Insert Hogs Inventory
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + INSERT_FARM_RSN_REV_HOG_INVTRY_PROC, INSERT_FARM_RSN_REV_HOG_INVTRY_PARAM, false);) {      
             
        List<RevenueRiskInventoryItem> hogsInvItems = revenueRiskTest.getHogs().getInventory();
        
        for (RevenueRiskInventoryItem hogsInvItem : hogsInvItems) {
          int param = 1;
          proc.setInt(param++, reasonabilityTestResultId);
          proc.setDouble(param++, hogsInvItem.getQuantityStart());
          proc.setDouble(param++, hogsInvItem.getQuantityEnd());
          proc.setDouble(param++, hogsInvItem.getFmvPrice());
          proc.setString(param++, hogsInvItem.getInventoryItemCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }    
  }
  
  
  private void updateForageConsumers(
      final Transaction transaction,
      final ReasonabilityTestResults results,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    try {
      
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_RSN_FORAGE_CONSUMERS_PROC, UPDATE_RSN_FORAGE_CONSUMERS_PARAM, false);) {      
        
        int param = 1;
        
        List<ForageConsumer> forageConsumers = results.getForageConsumers();
        
        for (ForageConsumer forageGrainIncomeItem : forageConsumers) {
          param = 1;
          proc.setInt(param++, results.getReasonabilityTestResultId());
          proc.setDouble(param++, forageGrainIncomeItem.getProductiveUnitCapacity());
          proc.setDouble(param++, forageGrainIncomeItem.getQuantityConsumedPerUnit());
          proc.setDouble(param++, forageGrainIncomeItem.getQuantityConsumed());
          proc.setString(param++, forageGrainIncomeItem.getStructureGroupCode());
          proc.setString(param++, user);
          proc.addBatch();
        }
        
        proc.executeBatch();
      }
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }
  
  
  public void updateReasonabilityTests(
      final Transaction transaction,
      final ReasonabilityTestResults results,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);

    try {
      
      if (results.getReasonabilityTestResultId() != null) {
        deleteReasonabilityResults(transaction, results.getReasonabilityTestResultId());
      }
      
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + UPDATE_REASONABILITY_TESTS_PROC, UPDATE_REASONABILITY_TESTS_PARAM, false);) {
      
        int param = 1;
        proc.registerOutParameter(param++, Types.INTEGER);
  
        param = 1;
        proc.setInt(param++, results.getReasonabilityTestResultId());
        proc.setIndicator(param++, results.getIsFresh());
        proc.setDouble(param++, results.getForageConsumerCapacity());
        proc.setIndicator(param++, results.getBenefitRisk().getResult());
        proc.setDouble(param++, results.getBenefitRisk().getProgramYearMargin());
        proc.setDouble(param++, results.getBenefitRisk().getTotalBenefit());
        proc.setDouble(param++, results.getBenefitRisk().getBenchmarkMargin());
        proc.setDouble(param++, results.getBenefitRisk().getBenefitRiskDeductible());
        proc.setDouble(param++, results.getBenefitRisk().getBenefitBenchmarkLessDeductible());
        proc.setDouble(param++, results.getBenefitRisk().getBenefitBenchmarkLessProgramYearMargin());
        proc.setDouble(param++, results.getBenefitRisk().getBenefitRiskPayoutLevel());
        proc.setDouble(param++, results.getBenefitRisk().getBenefitBenchmarkBeforeCombinedFarmPercent());
        proc.setDouble(param++, results.getBenefitRisk().getCombinedFarmPercent());
        proc.setDouble(param++, results.getBenefitRisk().getBenefitBenchmark());
        proc.setDouble(param++, results.getBenefitRisk().getVariance());
        proc.setDouble(param++, results.getBenefitRisk().getVarianceLimit());
        proc.setIndicator(param++, results.getMarginTest().getResult());
        proc.setDouble(param++, results.getMarginTest().getAdjustedReferenceMargin());
        proc.setDouble(param++, results.getMarginTest().getAdjustedReferenceMarginVariance());
        proc.setDouble(param++, results.getMarginTest().getAdjustedReferenceMarginVarianceLimit());
        proc.setIndicator(param++, results.getMarginTest().getWithinLimitOfReferenceMargin());
        proc.setDouble(param++, results.getMarginTest().getIndustryAverage());
        proc.setDouble(param++, results.getMarginTest().getIndustryVariance());
        proc.setDouble(param++, results.getMarginTest().getIndustryVarianceLimit());
        proc.setIndicator(param++, results.getMarginTest().getWithinLimitOfIndustryAverage());
        proc.setIndicator(param++, results.getStructuralChangeTest().getResult());
        proc.setDouble(param++, results.getStructuralChangeTest().getProductionMargAccrAdjs());
        proc.setDouble(param++, results.getStructuralChangeTest().getRatioReferenceMargin());
        proc.setDouble(param++, results.getStructuralChangeTest().getAdditiveReferenceMargin());
        proc.setDouble(param++, results.getStructuralChangeTest().getRatioAdditiveDiffVariance());
        proc.setDouble(param++, results.getStructuralChangeTest().getRatioAdditiveDiffVarianceLimit());
        proc.setIndicator(param++, results.getStructuralChangeTest().getWithinRatioAdditiveDiffLimit());
        proc.setDouble(param++, results.getStructuralChangeTest().getAdditiveDivisionRatio());
        proc.setDouble(param++, results.getStructuralChangeTest().getAdditiveDivisionRatioLimit());
        proc.setIndicator(param++, results.getStructuralChangeTest().getWithinAdditiveDivisionLimit());
        proc.setString(param++, results.getStructuralChangeTest().getMethodToUse());
        proc.setDouble(param++, results.getStructuralChangeTest().getFarmSizeRatioLimit());
        proc.setIndicator(param++, results.getStructuralChangeTest().getWithinFarmSizeRatioLimit());
        proc.setIndicator(param++, results.getExpenseTestIAC().getResult());
        proc.setDouble(param++, results.getExpenseTestIAC().getExpenseAccrualAdjs());
        proc.setDouble(param++, results.getExpenseTestIAC().getIndustryAverage());
        proc.setDouble(param++, results.getExpenseTestIAC().getIndustryVariance());
        proc.setDouble(param++, results.getExpenseTestIAC().getIndustryVarianceLimit());
        proc.setInt(param++, results.getScenario().getScenarioId());
        proc.setIndicator(param++, results.getProductionTest().getResult());
        proc.setIndicator(param++, results.getProductionTest().isPassForageAndForageSeedTest());
        proc.setIndicator(param++, results.getProductionTest().getPassFruitVegTest());
        proc.setDouble(param++, results.getProductionTest().getForageProducedVarianceLimit());
        proc.setDouble(param++, results.getProductionTest().getFruitVegProducedVarianceLimit());
        proc.setDouble(param++, results.getProductionTest().getGrainProducedVarianceLimit());
        proc.setIndicator(param++, results.getProductionTest().getPassGrainTest());
        proc.setIndicator(param++, results.getExpenseTestRefYearCompGC().getResult());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getProgramYearAcrAdjExpense());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus1());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus2());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus3());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus4());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getExpenseStructuralChangeYearMinus5());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getExpenseStructrualChangeAverage());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getVariance());
        proc.setDouble(param++, results.getExpenseTestRefYearCompGC().getVarianceLimit());
        proc.setIndicator(param++, results.getRevenueRiskTest().getForageGrainPass());
        proc.setIndicator(param++, results.getRevenueRiskTest().getResult());
        proc.setDouble(param++, results.getRevenueRiskTest().getForageGrainVarianceLimit());
        proc.setIndicator(param++, results.getRevenueRiskTest().getFruitVegTestPass());
        proc.setString(param++, user);
        proc.execute();
      
        param = 1;
        results.setReasonabilityTestResultId(new Integer(proc.getInt(param++)));
      }
      
      updateForageConsumers(transaction, results, user);
      
      // Create Productive Units for the test
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + CREATE_FARM_RSNBLTY_BNFT_RSK_PU_PROC, CREATE_FARM_RSNBLTY_BNFT_RSK_PU_PARAM, false);) {
      
        for(BenefitRiskProductiveUnit unit : results.getBenefitRisk().getBenefitRiskProductiveUnits()) {
          
          int param = 1;
          proc.setInt(param++, results.getReasonabilityTestResultId());
          proc.setDouble(param++, unit.getReportedProductiveCapacityAmount());
          proc.setDouble(param++, unit.getConsumedProductiveCapacityAmount());
          proc.setDouble(param++, unit.getNetProductiveCapacityAmount());
          proc.setDouble(param++, unit.getBpuCalculated());
          proc.setDouble(param++, unit.getEstimatedIncome());
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
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
    
  }
  
  
  public void flagReasonabilityTestsStale(
      final Transaction transaction,
      final ReasonabilityTestResults results,
      final String user)
          throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    
    if(results != null && results.getReasonabilityTestResultId() != null) {
      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + FLAG_REASONABILITY_TESTS_STALE_PROC, FLAG_REASONABILITY_TESTS_STALE_PARAM, false);) {
        
        int param = 1;
        proc.setInt(param++, results.getReasonabilityTestResultId());
        proc.setString(param++, user);
        proc.execute();
        
      } catch (SQLException e) {
        logSqlException(e);
        handleException(e);
      }
    }
    
  }

}
