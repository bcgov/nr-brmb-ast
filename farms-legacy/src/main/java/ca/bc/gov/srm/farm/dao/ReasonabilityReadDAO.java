/**
 * Copyright (c) 2020,
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskAssessmentTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskProductiveUnit;
import ca.bc.gov.srm.farm.domain.reasonability.ExpenseTestIACResult;
import ca.bc.gov.srm.farm.domain.reasonability.ExpenseTestRefYearCompGCResult;
import ca.bc.gov.srm.farm.domain.reasonability.MarginTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.StructuralChangeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.FruitVegProductionResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.CattleRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.HogsRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.PoultryBroilersRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.PoultryEggsRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskFruitVegItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.NurseryRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.webade.dbpool.WrapperConnection;

/**
 * @author awilkinson
 */
public class ReasonabilityReadDAO {

  private static final String PACKAGE_NAME = "FARM_REASONABILITY_READ_PKG";
  
  private static final String READ_REASONABILITY_TESTS_PROC = "READ_REASONABILITY_TESTS";
  private static final int READ_REASONABILITY_TESTS_PARAM = 1;
  
  private static final String READ_REASONABILITY_BENEFIT_RISK_PU_PROC = "READ_REASONABILITY_BENEFIT_RISK_PU";
  private static final int READ_REASONABILITY_BENEFIT_RISK_PU_PARAM = 2;
  
  private static final String READ_REASONABILITY_FORAGE_PRODUCTION_TEST_PROC = "READ_REASONABILITY_FORAGE_PRODUCTION_TEST";
  private static final int READ_REASONABILITY_FORAGE_PRODUCTION_TEST_PARAM = 2;
  
  private static final String READ_REASONABILITY_TEST_MESSAGES_PROC = "READ_REASONABILITY_TEST_MESSAGES";
  private static final int READ_REASONABILITY_TEST_MESSAGES_PARAM = 2;
  
  private static final String READ_PRODUCTION_FRUIT_VEG_INVENTORY_PROC = "READ_PRODUCTION_FRUIT_VEG_INVENTORY";
  private static final int READ_PRODUCTION_FRUIT_VEG_INVENTORY_PARAM = 2;
  
  private static final String READ_REASONABILITY_FRUIT_VEG_PRODUCTION_TEST_PROC = "READ_REASONABILITY_FRUIT_VEG_PRODUCTION_TEST";
  private static final int READ_REASONABILITY_FRUIT_VEG_PRODUCTION_TEST_PARAM = 1;
  
  private static final String READ_REASONABILITY_GRAINS_PRODUCTION_TEST_PROC = "READ_REASONABILITY_GRAIN_PRODUCTION_TEST";
  private static final int READ_REASONABILITY_GRAINS_PRODUCTION_TEST_PARAM = 2;
  
  private static final String READ_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PARAM = 2;
  
  private static final String READ_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PARAM = 3;
  
  private static final String READ_FARM_RSN_FORAGE_CONSUMERS_PROC = "READ_FARM_RSN_FORAGE_CONSUMERS";
  private static final int READ_FARM_RSN_FORAGE_CONSUMERS_PARAM = 1;
  
  private static final String READ_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PROC = "READ_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY";
  private static final int READ_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PARAM = 2;
  
  private static final String READ_FARM_RSNBLTY_REV_FRUIT_VEG_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_FRUIT_VEG_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_FRUIT_VEG_RSLTS_PARAM = 1;
  
  private static final String READ_FARM_RSNBLTY_REV_NRSRY_RESULTS_PROC = "READ_FARM_RSNBLTY_REV_NRSRY_RESULTS";
  private static final int READ_FARM_RSNBLTY_REV_NRSRY_RESULTS_PARAM = 1;
  
  private static final String READ_FARM_RSNBLTY_REV_NRSRY_INVN_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_NRSRY_INVN_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_NRSRY_INVN_RSLTS_PARAM = 2;
  
  private static final String READ_FARM_RSNBLTY_REV_NRSRY_INCM_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_NRSRY_INCM_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_NRSRY_INCM_RSLTS_PARAM = 3;
  
  private static final String READ_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PARAM = 1;
  
  private static final String READ_FARM_RSNBLTY_REV_PLTRY_EGG_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_PLTRY_EGG_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_PLTRY_EGG_RSLTS_PARAM = 1;
  
  private static final String READ_FARM_RSNBLTY_REV_HOGS_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_HOGS_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_HOGS_RSLTS_PARAM = 1;
  
  private static final String READ_FARM_RSNBLTY_REV_HOGS_INVN_RSLTS_PROC = "READ_FARM_RSNBLTY_REV_HOGS_INVN_RSLTS";
  private static final int READ_FARM_RSNBLTY_REV_HOGS_INVN_RSLTS_PARAM = 1;
  

  private Connection conn = null;
  
  @SuppressWarnings("unused")
  private Connection neverUse = null;

  /**
   * @param c Input parameter to initialize class.
   */
  public ReasonabilityReadDAO(final Connection c) {
    neverUse = c;
    if (c instanceof WrapperConnection) {
      WrapperConnection wc = (WrapperConnection) c;
      this.conn = wc.getWrappedConnection();
    } else {
      this.conn = c;
    }
  }

  private String getString(final ResultSet rs, final int i) throws SQLException {
    return rs.getString(i);
  }

  private Boolean getIndicator(final ResultSet rs, final int i)
      throws SQLException {
    String value = rs.getString(i);
    Boolean result = Boolean.FALSE;

    try {
      result = Boolean.valueOf(DataParseUtils.parseBoolean(value));
    } catch (ParseException e) {
      throw new SQLException(e.toString());
    }

    return result;
  }

  private Date getDate(final ResultSet rs, final int i) throws SQLException {
    Date result = null;
    Timestamp timestamp = rs.getTimestamp(i);
    
    if(timestamp != null) {
      result = new Date(timestamp.getTime());
    }
    
    return result;
  }

  private Double getDouble(final ResultSet rs, final int i) throws SQLException {
    double v = rs.getDouble(i);

    if (rs.wasNull()) {
      return null;
    }

    return Double.valueOf(v);
  }
  
  private Integer getInteger(final ResultSet rs, final int i)
      throws SQLException {
    int v = rs.getInt(i);

    if (rs.wasNull()) {
      return null;
    }

    return Integer.valueOf(v);
  }
   
  private final ProductionTestResult readProductionTestResults(Integer reasonabilityTestResultId, Integer programYear) throws SQLException {
    ProductionTestResult productionTest = new ProductionTestResult();
    productionTest.setForageTestResults(new ArrayList<ProductionInventoryItemTestResult>());
    productionTest.setForageSeedTestResults(new ArrayList<ProductionInventoryItemTestResult>());
    productionTest.setGrainItemTestResults(new ArrayList<ProductionInventoryItemTestResult>());
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_REASONABILITY_FORAGE_PRODUCTION_TEST_PROC, READ_REASONABILITY_FORAGE_PRODUCTION_TEST_PARAM, true);) {
    
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          ProductionInventoryItemTestResult testRecord = new ProductionInventoryItemTestResult();
          
          testRecord.setProductiveCapacityAmount(getDouble(rs, c++));
          testRecord.setExpectedProductionPerUnit(getDouble(rs,c++));
          testRecord.setReportedProduction(getDouble(rs, c++));
          testRecord.setExpectedQuantityProduced(getDouble(rs,c++));
          testRecord.setVariance(getDouble(rs, c++));
          testRecord.setPass(getIndicator(rs, c++));
          testRecord.setInventoryItemCode(getString(rs, c++));
          testRecord.setInventoryItemCodeDescription(getString(rs, c++));
          testRecord.setCommodityTypeCode(getString(rs, c++));
          
          if (testRecord.getCommodityTypeCode().equals(CommodityTypeCodes.FORAGE)) {
            productionTest.getForageTestResults().add(testRecord);
          } else if (testRecord.getCommodityTypeCode().equals(CommodityTypeCodes.FORAGE_SEED)) {
            productionTest.getForageSeedTestResults().add(testRecord);
          }
        }
      }
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_PRODUCTION_FRUIT_VEG_INVENTORY_PROC, READ_PRODUCTION_FRUIT_VEG_INVENTORY_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        List<ProductionInventoryItemTestResult> fruitVegInventoryItems = new ArrayList<>();
        productionTest.setFruitVegInventoryItems(fruitVegInventoryItems);
        while (rs.next()) {
          c = 1;
          ProductionInventoryItemTestResult testRecord = new ProductionInventoryItemTestResult();
          fruitVegInventoryItems.add(testRecord);
          
          testRecord.setProductiveCapacityAmount(getDouble(rs, c++));
          testRecord.setExpectedProductionPerUnit(getDouble(rs, c++));
          testRecord.setReportedProduction(getDouble(rs, c++));
          testRecord.setExpectedQuantityProduced(getDouble(rs,c++));
          testRecord.setInventoryItemCode(getString(rs, c++));
          testRecord.setInventoryItemCodeDescription(getString(rs, c++));
          testRecord.setCropUnitCode(getString(rs, c++));
          testRecord.setFruitVegTypeCode(getString(rs, c++));
          testRecord.setFruitVegTypeCodeDescription(getString(rs, c++));
        }
      }
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_REASONABILITY_FRUIT_VEG_PRODUCTION_TEST_PROC, READ_REASONABILITY_FRUIT_VEG_PRODUCTION_TEST_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        List<FruitVegProductionResult> fruitVegItemTestResults = new ArrayList<>();
        productionTest.setFruitVegTestResults(fruitVegItemTestResults);
        while (rs.next()) {
          c = 1;
          FruitVegProductionResult testRecord = new FruitVegProductionResult();
          fruitVegItemTestResults.add(testRecord);
          
          testRecord.setProductiveCapacityAmount(getDouble(rs, c++));
          testRecord.setReportedProduction(getDouble(rs, c++));
          testRecord.setExpectedQuantityProduced(getDouble(rs,c++));
          testRecord.setVariance(getDouble(rs, c++));
          testRecord.setPass(getIndicator(rs, c++));
          testRecord.setFruitVegTypeCode(getString(rs, c++));
          testRecord.setFruitVegTypeCodeDescription(getString(rs, c++));
        }
      }
    }
      
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_REASONABILITY_GRAINS_PRODUCTION_TEST_PROC, READ_REASONABILITY_GRAINS_PRODUCTION_TEST_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          ProductionInventoryItemTestResult testRecord = new ProductionInventoryItemTestResult();
          productionTest.getGrainItemTestResults().add(testRecord);
          
          testRecord.setProductiveCapacityAmount(getDouble(rs, c++));
          testRecord.setExpectedProductionPerUnit(getDouble(rs,c++));
          testRecord.setReportedProduction(getDouble(rs, c++));
          testRecord.setExpectedQuantityProduced(getDouble(rs,c++));
          testRecord.setVariance(getDouble(rs, c++));
          testRecord.setPass(getIndicator(rs, c++));
          testRecord.setInventoryItemCode(getString(rs, c++));
          testRecord.setInventoryItemCodeDescription(getString(rs, c++));
          testRecord.setCommodityTypeCode(getString(rs, c++));
        }
      }
    }
    
    Collections.sort(productionTest.getForageTestResults());
    Collections.sort(productionTest.getForageSeedTestResults());
    Collections.sort(productionTest.getGrainItemTestResults());
    Collections.sort(productionTest.getFruitVegInventoryItems());
    Collections.sort(productionTest.getFruitVegTestResults());
    
    return productionTest;
  }
  
  private final RevenueRiskTestResult readRevenueRiskTestResults(Integer reasonabilityTestResultId, Integer programYear, Date verifiedDate) throws SQLException {
    RevenueRiskTestResult revenueRiskTestResult = new RevenueRiskTestResult();
    
    readRevenueRiskGrainForageResults(reasonabilityTestResultId, programYear, verifiedDate, revenueRiskTestResult);
    readRevenueRiskFruitVegResults(reasonabilityTestResultId, programYear, revenueRiskTestResult);
    readRevenueRiskCattleResults(reasonabilityTestResultId, revenueRiskTestResult);
    readRevenueRiskHogsResults(reasonabilityTestResultId, revenueRiskTestResult);
    readRevenueRiskNurseryResults(reasonabilityTestResultId, programYear, verifiedDate, revenueRiskTestResult);
    readRevenueRiskPoultryBroilersResults(reasonabilityTestResultId, revenueRiskTestResult);
    readRevenueRiskPoultryEggResults(reasonabilityTestResultId, revenueRiskTestResult);
    
    Collections.sort(revenueRiskTestResult.getForageGrainInventory());
    Collections.sort(revenueRiskTestResult.getForageGrainIncomes());
    Collections.sort(revenueRiskTestResult.getFruitVegResults());
    
    return revenueRiskTestResult;
  }

  private void readRevenueRiskGrainForageResults(Integer reasonabilityTestResultId, Integer programYear, Date verifiedDate,
      RevenueRiskTestResult revenueRiskTestResult) throws SQLException {
    
    revenueRiskTestResult.setForageGrainIncomes(new ArrayList<RevenueRiskIncomeTestResult>());
    revenueRiskTestResult.setForageGrainInventory(new ArrayList<RevenueRiskInventoryItem>());
    
    try (DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PROC, READ_FARM_RSNBLTY_REV_G_F_FS_INVN_RSLTS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
      
        while (rs.next()) {
          c = 1;
          RevenueRiskInventoryItem testRecord = new RevenueRiskInventoryItem();
          revenueRiskTestResult.getForageGrainInventory().add(testRecord);
          
          testRecord.setQuantityProduced(getDouble(rs, c++));
          testRecord.setQuantityStart(getDouble(rs, c++));
          testRecord.setQuantityEnd(getDouble(rs, c++));
          testRecord.setQuantityConsumed(getDouble(rs, c++));
          testRecord.setQuantitySold(getDouble(rs, c++));
          testRecord.setExpectedRevenue(getDouble(rs, c++));
          testRecord.setReportedPrice(getDouble(rs, c++));
          testRecord.setInventoryItemCode(getString(rs, c++));
          testRecord.setInventoryItemCodeDescription(getString(rs, c++));
          testRecord.setCropUnitCode(getString(rs, c++));
          testRecord.setCropUnitCodeDescription(getString(rs, c++));
          testRecord.setCommodityTypeCode(getString(rs, c++));
          testRecord.setCommodityTypeCodeDescription(getString(rs, c++));
        }
      }
    }
    
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PROC, READ_FARM_RSNBLTY_REV_G_F_FS_INCM_RSLTS_PARAM, true);) {
    
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.setDate(c++, verifiedDate);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          RevenueRiskIncomeTestResult testRecord = new RevenueRiskIncomeTestResult();
          revenueRiskTestResult.getForageGrainIncomes().add(testRecord);
          
          testRecord.setLineItemCode(getInteger(rs, c++));
          testRecord.setReportedRevenue(getDouble(rs, c++));
          testRecord.setExpectedRevenue(getDouble(rs, c++));
          testRecord.setVariance(getDouble(rs, c++));
          testRecord.setPass(getIndicator(rs, c++));
          testRecord.setDescription(getString(rs, c++));
          testRecord.setCommodityTypeCode(getString(rs, c++));
          testRecord.setCommodityTypeCodeDescription(getString(rs, c++));
        }
      }
    }
    
  }
  
  private void readForageConsumers(ReasonabilityTestResults results) throws SQLException {
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSN_FORAGE_CONSUMERS_PROC, READ_FARM_RSN_FORAGE_CONSUMERS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, results.getReasonabilityTestResultId());
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          ForageConsumer forageConsumer = new ForageConsumer();
          results.getForageConsumers().add(forageConsumer);
          
          forageConsumer.setProductiveUnitCapacity(getDouble(rs, c++));
          forageConsumer.setQuantityConsumedPerUnit(getDouble(rs, c++));
          forageConsumer.setQuantityConsumed(getDouble(rs, c++));
          forageConsumer.setStructureGroupCode(getString(rs, c++));
          forageConsumer.setStructureGroupCodeDescription(getString(rs, c++));
        }
      }
    }
    
  }

  private void readRevenueRiskFruitVegResults(Integer reasonabilityTestResultId,
      Integer programYear,
      RevenueRiskTestResult revenueRiskTestResult) throws SQLException {
    
    List<RevenueRiskInventoryItem> fruitVegInventory = new ArrayList<>();
    revenueRiskTestResult.setFruitVegInventory(fruitVegInventory);
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PROC, READ_FARM_RSNBLTY_REV_FRUIT_VEG_INVENTORY_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
      
        while (rs.next()) {
          c = 1;
          RevenueRiskInventoryItem testRecord = new RevenueRiskInventoryItem();
          revenueRiskTestResult.getFruitVegInventory().add(testRecord);
          
          testRecord.setInventoryItemCode(getString(rs, c++));
          testRecord.setInventoryItemCodeDescription(getString(rs, c++));
          testRecord.setQuantityProduced(getDouble(rs, c++));
          testRecord.setFmvPrice(getDouble(rs, c++));
          testRecord.setExpectedRevenue(getDouble(rs, c++));
          testRecord.setFruitVegTypeCode(getString(rs, c++));
          testRecord.setFruitVegTypeCodeDescription(getString(rs, c++));
          testRecord.setCropUnitCode(getString(rs, c++));
          testRecord.setCropUnitCodeDescription(getString(rs, c++));
        }
      }
    }
    
    
    List<RevenueRiskFruitVegItemTestResult> fruitVegResults = new ArrayList<>();
    revenueRiskTestResult.setFruitVegResults(fruitVegResults);
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_FRUIT_VEG_RSLTS_PROC, READ_FARM_RSNBLTY_REV_FRUIT_VEG_RSLTS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        
        while (rs.next()) {
          c = 1;
          RevenueRiskFruitVegItemTestResult testRecord = new RevenueRiskFruitVegItemTestResult();
          revenueRiskTestResult.getFruitVegResults().add(testRecord);
          
          testRecord.setQuantityProduced(getDouble(rs, c++));
          testRecord.setVariance(getDouble(rs, c++));
          testRecord.setVarianceLimit(getDouble(rs, c++));
          testRecord.setReportedRevenue(getDouble(rs, c++));
          testRecord.setExpectedPrice(getDouble(rs, c++));
          testRecord.setExpectedRevenue(getDouble(rs, c++));
          testRecord.setPass(getIndicator(rs, c++));
          testRecord.setFruitVegTypeCode(getString(rs, c++));
          testRecord.setCropUnitCode(getString(rs, c++));
          testRecord.setFruitVegTypeDesc(getString(rs, c++));
        }
      }
    }
  }

  private void readRevenueRiskNurseryResults(Integer reasonabilityTestResultId, Integer programYear, Date verifiedDate,
      RevenueRiskTestResult revenueRiskTestResult) throws SQLException {
    
    NurseryRevenueRiskSubTestResult nurseryTestResult = new NurseryRevenueRiskSubTestResult();
    nurseryTestResult.setInventory(new ArrayList<RevenueRiskInventoryItem>());
    nurseryTestResult.setIncomes(new ArrayList<RevenueRiskIncomeTestResult>());
    revenueRiskTestResult.setNursery(nurseryTestResult);
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_NRSRY_RESULTS_PROC, READ_FARM_RSNBLTY_REV_NRSRY_RESULTS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          nurseryTestResult.setSubTestPass(getIndicator(rs, c++));
          nurseryTestResult.setVariance(getDouble(rs, c++));
          nurseryTestResult.setVarianceLimit(getDouble(rs, c++));
          nurseryTestResult.setExpectedRevenue(getDouble(rs, c++));
          nurseryTestResult.setReportedRevenue(getDouble(rs, c++));
        }
      }
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_NRSRY_INVN_RSLTS_PROC, READ_FARM_RSNBLTY_REV_NRSRY_INVN_RSLTS_PARAM, true);) {      
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          RevenueRiskInventoryItem invItem = new RevenueRiskInventoryItem();
          nurseryTestResult.getInventory().add(invItem);
          
          invItem.setQuantityProduced(getDouble(rs, c++));
          invItem.setQuantityStart(getDouble(rs, c++));
          invItem.setQuantityEnd(getDouble(rs, c++));
          invItem.setQuantitySold(getDouble(rs, c++));
          invItem.setExpectedRevenue(getDouble(rs, c++));
          invItem.setFmvPrice(getDouble(rs, c++));
          invItem.setInventoryItemCode(getString(rs, c++));
          invItem.setInventoryItemCodeDescription(getString(rs, c++));
        }
      }
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_NRSRY_INCM_RSLTS_PROC, READ_FARM_RSNBLTY_REV_NRSRY_INCM_RSLTS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setInt(c++, programYear);
      proc.setDate(c++, verifiedDate);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          c = 1;
          RevenueRiskIncomeTestResult incomeResult = new RevenueRiskIncomeTestResult();
          nurseryTestResult.getIncomes().add(incomeResult);
          
          incomeResult.setLineItemCode(getInteger(rs, c++));
          incomeResult.setReportedRevenue(getDouble(rs, c++));
          incomeResult.setDescription(getString(rs, c++));
        }
      }
    }
  }

  private void readRevenueRiskPoultryBroilersResults(Integer reasonabilityTestResultId, RevenueRiskTestResult revenueRiskTestResult)
      throws SQLException {
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PROC, READ_FARM_RSNBLTY_REV_PLTRY_BRL_RSLTS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
      
        while (rs.next()) {
          c = 1;
          PoultryBroilersRevenueRiskSubTestResult poultryBroilersRevenueRiskSubTestResult = new PoultryBroilersRevenueRiskSubTestResult();
          revenueRiskTestResult.setPoultryBroilers(poultryBroilersRevenueRiskSubTestResult);
          
          poultryBroilersRevenueRiskSubTestResult.setHasPoultryBroilers(getIndicator(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setSubTestPass(getIndicator(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setHasChickens(getIndicator(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenPass(getIndicator(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenAverageWeightKg(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenExpectedSoldCount(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenPricePerBird(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenExpectedRevenue(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenReportedRevenue(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenKgProduced(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenVariance(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setChickenVarianceLimit(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setHasTurkeys(getIndicator(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyPass(getIndicator(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyAverageWeightKg(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyExpectedSoldCount(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyPricePerBird(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyExpectedRevenue(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyReportedRevenue(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyKgProduced(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyVariance(getDouble(rs, c++));
          poultryBroilersRevenueRiskSubTestResult.setTurkeyVarianceLimit(getDouble(rs, c++));
        }
      }
    }
  }

  private void readRevenueRiskPoultryEggResults(Integer reasonabilityTestResultId, RevenueRiskTestResult revenueRiskTestResult) throws SQLException {
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_PLTRY_EGG_RSLTS_PROC, READ_FARM_RSNBLTY_REV_PLTRY_EGG_RSLTS_PARAM, true);) {

        int c = 1;
        proc.setInt(c++, reasonabilityTestResultId);
        proc.execute();
        
        try(ResultSet rs = proc.getResultSet();) {
        
          while (rs.next()) {
            c = 1;
            PoultryEggsRevenueRiskSubTestResult poultryEggsRevenueRiskSubTestResult = new PoultryEggsRevenueRiskSubTestResult();
            revenueRiskTestResult.setPoultryEggs(poultryEggsRevenueRiskSubTestResult);
            
            poultryEggsRevenueRiskSubTestResult.setSubTestPass(getIndicator(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionPass(getIndicator(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHasPoultryEggs(getIndicator(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionLayers(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionAverageEggsPerLayer(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionEggsTotal(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionEggsDozen(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionEggsDozenPrice(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionExpectedRevenue(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionReportedRevenue(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionVariance(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setConsumptionVarianceLimit(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingPass(getIndicator(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingLayers(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingAverageEggsPerLayer(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingEggsTotal(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingEggsDozen(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingEggsDozenPrice(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingExpectedRevenue(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingReportedRevenue(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingVariance(getDouble(rs, c++));
            poultryEggsRevenueRiskSubTestResult.setHatchingVarianceLimit(getDouble(rs, c++));
          }
        }
    }
  }
  
  
  private void readRevenueRiskCattleResults(Integer reasonabilityTestResultId, RevenueRiskTestResult revenueRiskTestResult) throws SQLException {
    
    CattleRevenueRiskSubTestResult cattleTestResult = new CattleRevenueRiskSubTestResult();
    revenueRiskTestResult.setCattle(cattleTestResult);
    
    // TODO implement cattle test
    cattleTestResult.setHasCattle(false);
  }


  private void readRevenueRiskHogsResults(Integer reasonabilityTestResultId, RevenueRiskTestResult revenueRiskTestResult) throws SQLException {
    
    HogsRevenueRiskSubTestResult hogsTestResult = null;
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_FARM_RSNBLTY_REV_HOGS_RSLTS_PROC, READ_FARM_RSNBLTY_REV_HOGS_RSLTS_PARAM, true);) {
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        while (rs.next()) {
          hogsTestResult = new HogsRevenueRiskSubTestResult();
          revenueRiskTestResult.setHogs(hogsTestResult);
          c = 1;
          hogsTestResult.setHasHogs(getIndicator(rs, c++));
          hogsTestResult.setHogsPass(getIndicator(rs, c++));
          hogsTestResult.setFarrowToFinishOperation(getIndicator(rs, c++));
          hogsTestResult.setFeederOperation(getIndicator(rs, c++));
          hogsTestResult.setReportedExpenses(getDouble(rs, c++));
          hogsTestResult.setTotalQuantityStart(getDouble(rs, c++));
          hogsTestResult.setTotalQuantityEnd(getDouble(rs, c++));
          hogsTestResult.setSowsBreeding(getDouble(rs, c++));
          hogsTestResult.setBirthsPerCycle(getDouble(rs, c++));
          hogsTestResult.setBirthCyclesPerYear(getDouble(rs, c++));
          hogsTestResult.setTotalBirthsPerCycle(getDouble(rs, c++));
          hogsTestResult.setTotalBirthsAllCycles(getDouble(rs, c++));
          hogsTestResult.setDeathRate(getDouble(rs, c++));
          hogsTestResult.setDeaths(getDouble(rs, c++));
          hogsTestResult.setBoarPurchaseCount(getDouble(rs, c++));
          hogsTestResult.setBoarPurchasePrice(getDouble(rs, c++));
          hogsTestResult.setBoarPurchaseExpense(getDouble(rs, c++));
          hogsTestResult.setSowPurchaseExpense(getDouble(rs, c++));
          hogsTestResult.setSowPurchaseCount(getDouble(rs, c++));
          hogsTestResult.setSowPurchasePrice(getDouble(rs, c++));
          hogsTestResult.setFeederProductiveUnits(getDouble(rs, c++));
          hogsTestResult.setWeanlingPurchaseExpense(getDouble(rs, c++));
          hogsTestResult.setWeanlingPurchasePrice(getDouble(rs, c++));
          hogsTestResult.setWeanlingPurchaseCount(getDouble(rs, c++));
          hogsTestResult.setTotalPurchaseCount(getDouble(rs, c++));
          hogsTestResult.setExpectedSold(getDouble(rs, c++));
          hogsTestResult.setHeaviestHogFmvPrice(getDouble(rs, c++));
          hogsTestResult.setReportedRevenue(getDouble(rs, c++));
          hogsTestResult.setExpectedRevenue(getDouble(rs, c++));
          hogsTestResult.setRevenueVariance(getDouble(rs, c++));
          hogsTestResult.setVarianceLimit(getDouble(rs, c++));
        }
      }
    }
    
    if(hogsTestResult != null) {
      try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_FARM_RSNBLTY_REV_HOGS_INVN_RSLTS_PROC, READ_FARM_RSNBLTY_REV_HOGS_INVN_RSLTS_PARAM, true);) {      
        
        int c = 1;
        proc.setInt(c++, reasonabilityTestResultId);
        proc.execute();
        
        try(ResultSet rs = proc.getResultSet();) {
          while (rs.next()) {
            c = 1;
            RevenueRiskInventoryItem invItem = new RevenueRiskInventoryItem();
            hogsTestResult.getInventory().add(invItem);
            
            invItem.setQuantityStart(getDouble(rs, c++));
            invItem.setQuantityEnd(getDouble(rs, c++));
            invItem.setFmvPrice(getDouble(rs, c++));
            invItem.setInventoryItemCode(getString(rs, c++));
            invItem.setInventoryItemCodeDescription(getString(rs, c++));
          }
        }
      }
    }
    
  }


  private final Map<String, List<ReasonabilityTestResultMessage>> readReasonabilityTestMessages(
      Integer reasonabilityTestResultId, 
      String testName) 
  throws SQLException {
    
    Map<String, List<ReasonabilityTestResultMessage>> messages = new HashMap<>();
    messages.put(MessageTypeCodes.ERROR, new ArrayList<ReasonabilityTestResultMessage>());
    messages.put(MessageTypeCodes.WARNING, new ArrayList<ReasonabilityTestResultMessage>());
    messages.put(MessageTypeCodes.INFO, new ArrayList<ReasonabilityTestResultMessage>());
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_REASONABILITY_TEST_MESSAGES_PROC, READ_REASONABILITY_TEST_MESSAGES_PARAM, true);) {      
      
      int c = 1;
      proc.setInt(c++, reasonabilityTestResultId);
      proc.setString(c++, testName);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {     
        while (rs.next()) {
          c = 1;
          
          ReasonabilityTestResultMessage reasonabilityMessage = new ReasonabilityTestResultMessage(getString(rs, c++), getString(rs, c++));
          if (reasonabilityMessage.getMessageTypeCode().equals(MessageTypeCodes.ERROR)) {
            messages.get(MessageTypeCodes.ERROR).add(reasonabilityMessage);
          } else if (reasonabilityMessage.getMessageTypeCode().equals(MessageTypeCodes.WARNING)) {
            messages.get(MessageTypeCodes.WARNING).add(reasonabilityMessage);
          } else if (reasonabilityMessage.getMessageTypeCode().equals(MessageTypeCodes.INFO)) {
            messages.get(MessageTypeCodes.INFO).add(reasonabilityMessage);
          }
          
        }
      }

    }
    return messages;
  }

  public final ReasonabilityTestResults readReasonabilityTestResults(Scenario scenario, Date verifiedDate) throws SQLException {
    ReasonabilityTestResults results = null;

    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
          + READ_REASONABILITY_TESTS_PROC, READ_REASONABILITY_TESTS_PARAM, true);) {

      int c = 1;
      proc.setInt(c++, scenario.getScenarioId());
      proc.execute();

      try(ResultSet rs = proc.getResultSet();) {

        if (rs.next()) {
  
          results = new ReasonabilityTestResults();
          BenefitRiskAssessmentTestResult benefitRisk = new BenefitRiskAssessmentTestResult();
          MarginTestResult margin = new MarginTestResult();
          StructuralChangeTestResult structureChange = new StructuralChangeTestResult();
          ExpenseTestIACResult expenseIAC = new ExpenseTestIACResult();
          ExpenseTestRefYearCompGCResult expenseGC = new ExpenseTestRefYearCompGCResult();
          
          results.setBenefitRisk(benefitRisk);
          results.setMarginTest(margin);
          results.setStructuralChangeTest(structureChange);
          results.setExpenseTestIAC(expenseIAC);
          results.setExpenseTestRefYearCompGC(expenseGC);
          
          c = 1;
          
          results.setReasonabilityTestResultId(getInteger(rs, c++));
          results.setIsFresh(getIndicator(rs, c++));
          results.setGeneratedDate(getDate(rs, c++));
          results.setForageConsumerCapacity(getDouble(rs, c++));
          
          benefitRisk.setResult(getIndicator(rs, c++));
          Double programYearMargin = getDouble(rs, c++);
          benefitRisk.setProgramYearMargin(programYearMargin);
          benefitRisk.setTotalBenefit(getDouble(rs, c++));
          benefitRisk.setBenchmarkMargin(getDouble(rs, c++));
          benefitRisk.setBenefitRiskDeductible(getDouble(rs, c++));
          benefitRisk.setBenefitBenchmarkLessDeductible(getDouble(rs, c++));
          benefitRisk.setBenefitBenchmarkLessProgramYearMargin(getDouble(rs, c++));
          benefitRisk.setBenefitRiskPayoutLevel(getDouble(rs, c++));
          benefitRisk.setBenefitBenchmarkBeforeCombinedFarmPercent(getDouble(rs, c++));
          benefitRisk.setCombinedFarmPercent(getDouble(rs, c++));
          benefitRisk.setBenefitBenchmark(getDouble(rs, c++));
          benefitRisk.setVariance(getDouble(rs, c++));
          benefitRisk.setVarianceLimit(getDouble(rs, c++));
          
          margin.setResult(getIndicator(rs, c++));
          margin.setProgramYearMargin(programYearMargin);
          margin.setAdjustedReferenceMargin(getDouble(rs, c++));
          margin.setAdjustedReferenceMarginVariance(getDouble(rs, c++));
          margin.setAdjustedReferenceMarginVarianceLimit(getDouble(rs, c++));
          margin.setWithinLimitOfReferenceMargin(getIndicator(rs, c++));
          margin.setIndustryAverage(getDouble(rs, c++));
          margin.setIndustryVariance(getDouble(rs, c++));
          margin.setIndustryVarianceLimit(getDouble(rs, c++));
          margin.setWithinLimitOfIndustryAverage(getIndicator(rs, c++));
          
          structureChange.setResult(getIndicator(rs, c++));
          structureChange.setProductionMargAccrAdjs(getDouble(rs, c++));
          structureChange.setRatioReferenceMargin(getDouble(rs, c++));
          structureChange.setAdditiveReferenceMargin(getDouble(rs, c++));
          structureChange.setRatioAdditiveDiffVariance(getDouble(rs, c++));
          structureChange.setRatioAdditiveDiffVarianceLimit(getDouble(rs, c++));
          structureChange.setWithinRatioAdditiveDiffLimit(getIndicator(rs, c++));
          structureChange.setAdditiveDivisionRatio(getDouble(rs, c++));
          structureChange.setAdditiveDivisionRatioLimit(getDouble(rs, c++));
          structureChange.setWithinAdditiveDivisionLimit(getIndicator(rs, c++));
          structureChange.setMethodToUse(getString(rs, c++));
          structureChange.setFarmSizeRatioLimit(getDouble(rs, c++));
          structureChange.setWithinFarmSizeRatioLimit(getIndicator(rs, c++));
          
          expenseIAC.setResult(getIndicator(rs, c++));
          expenseIAC.setExpenseAccrualAdjs(getDouble(rs, c++));
          expenseIAC.setIndustryAverage(getDouble(rs, c++));
          expenseIAC.setIndustryVariance(getDouble(rs, c++));
          expenseIAC.setIndustryVarianceLimit(getDouble(rs, c++));
          
          ProductionTestResult productionTest = readProductionTestResults(results.getReasonabilityTestResultId(), scenario.getYear());
          results.setProductionTest(productionTest);
          productionTest.setResult(getIndicator(rs, c++));
          productionTest.setPassForageAndForageSeedTest(getIndicator(rs, c++));
          productionTest.setPassFruitVegTest(getIndicator(rs, c++));
          productionTest.setForageProducedVarianceLimit(getDouble(rs, c++));
          productionTest.setFruitVegProducedVarianceLimit(getDouble(rs, c++));
          productionTest.setGrainProducedVarianceLimit(getDouble(rs, c++));
          
          expenseGC.setResult(getIndicator(rs, c++));
          expenseGC.setProgramYearAcrAdjExpense(getDouble(rs, c++));
          expenseGC.setExpenseStructuralChangeYearMinus1(getDouble(rs, c++));
          expenseGC.setExpenseStructuralChangeYearMinus2(getDouble(rs, c++));
          expenseGC.setExpenseStructuralChangeYearMinus3(getDouble(rs, c++));
          expenseGC.setExpenseStructuralChangeYearMinus4(getDouble(rs, c++));
          expenseGC.setExpenseStructuralChangeYearMinus5(getDouble(rs, c++));
          expenseGC.setExpenseStructrualChangeAverage(getDouble(rs, c++));
          expenseGC.setVariance(getDouble(rs, c++));
          expenseGC.setVarianceLimit(getDouble(rs, c++));
          
          readForageConsumers(results);
          
          RevenueRiskTestResult revenueRiskTest = readRevenueRiskTestResults(results.getReasonabilityTestResultId(), scenario.getYear(), verifiedDate);
          results.setRevenueRiskTest(revenueRiskTest);
          
          revenueRiskTest.setResult(getIndicator(rs, c++));
          revenueRiskTest.setForageGrainPass(getIndicator(rs, c++));
          revenueRiskTest.setForageGrainVarianceLimit(getDouble(rs, c++));
          revenueRiskTest.setFruitVegTestPass(getIndicator(rs, c++));
          
          
          readBenefitRiskProductiveUnits(scenario, results);
          
          
          for(ReasonabilityTestResult testResult : results.getTestResults()) {
            testResult.setMessages(readReasonabilityTestMessages(results.getReasonabilityTestResultId(), testResult.getName()));
          }

        }
      }
      
    }
    
    return results;
  }

  private void readBenefitRiskProductiveUnits(Scenario scenario, ReasonabilityTestResults results)
      throws SQLException {
    
    BenefitRiskAssessmentTestResult benefitRisk = results.getBenefitRisk();
    
    int c;
    try(DAOStoredProcedure puProc = new DAOStoredProcedure(conn, PACKAGE_NAME + "."
        + READ_REASONABILITY_BENEFIT_RISK_PU_PROC, READ_REASONABILITY_BENEFIT_RISK_PU_PARAM, true);) {
      
      // productive units
      c = 1;
      puProc.setInt(c++, results.getReasonabilityTestResultId());
      puProc.setInt(c++, scenario.getYear());
      puProc.execute();
      
      try(ResultSet puRs = puProc.getResultSet();) {
        
        while(puRs.next()) {
          BenefitRiskProductiveUnit rpu = new BenefitRiskProductiveUnit();
          benefitRisk.getBenefitRiskProductiveUnits().add(rpu);
          
          c = 1;
          
          rpu.setReportedProductiveCapacityAmount(getDouble(puRs, c++));
          rpu.setConsumedProductiveCapacityAmount(getDouble(puRs, c++));
          rpu.setNetProductiveCapacityAmount(getDouble(puRs, c++));
          rpu.setBpuCalculated(getDouble(puRs, c++));
          rpu.setEstimatedMargin(getDouble(puRs, c++));
          rpu.setInventoryItemCode(getString(puRs, c++));
          rpu.setInventoryItemCodeDescription(getString(puRs, c++));
          rpu.setStructureGroupCode(getString(puRs, c++));
          rpu.setStructureGroupCodeDescription(getString(puRs, c++));
          rpu.setCommodityTypeCode(getString(puRs, c++));
          rpu.setCommodityTypeCodeDescription(getString(puRs, c++));
        }
      }
    }
  }
  
}
