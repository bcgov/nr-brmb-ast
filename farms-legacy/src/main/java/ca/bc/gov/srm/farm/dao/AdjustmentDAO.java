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

import static ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.AccrualItemUtils;
import ca.bc.gov.srm.farm.util.AdjustmentUtils;
import ca.bc.gov.srm.farm.util.CropItemUtils;
import ca.bc.gov.srm.farm.util.LivestockItemUtils;
import ca.bc.gov.srm.farm.util.ObjectUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Jan 10, 2011
 */
public class AdjustmentDAO extends OracleDAO {
  
  /** PACKAGE_NAME. */
  private static final String PACKAGE_NAME = "FARMS_ADJUSTMENT_PKG";
  
  private static final String ADJUST_PUC_PROC = "ADJUST_PUC";
  private static final int ADJUST_PUC_PARAM = 12;
  
  private static final String ADJUST_INCOME_EXPENSE_PROC = "ADJUST_INCOME_EXPENSE";
  private static final int ADJUST_INCOME_EXPENSE_PARAM = 12;
  
  private static final String ADJUST_INVENTORY_PROC = "ADJUST_INV";
  private static final int ADJUST_INVENTORY_PARAM = 22;



  /**
   * @param transaction transaction
   * @param adjustmentMap Map<String action, List<ProductiveUnitCapacity>>
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void adjustProductiveUnitCapacities(
      final Transaction transaction,
      final Map<String, List<ProductiveUnitCapacity>> adjustmentMap,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + ADJUST_PUC_PROC, ADJUST_PUC_PARAM, false); ) {

        for(String action : adjustmentMap.keySet()) {
          List<ProductiveUnitCapacity> actionAdjustments = adjustmentMap.get(action);
          
          if(actionAdjustments != null) {
            for(ProductiveUnitCapacity puc : actionAdjustments) {
              int param = 1;
              FarmingOperation operation = puc.getFarmingOperation();
        
              Integer refScenarioId = null;
              Integer parentScenarioId = null;
              if (operation.getFarmingYear() != null && operation.getFarmingYear().getReferenceScenario() != null) {            	
                ReferenceScenario refScenario = operation.getFarmingYear().getReferenceScenario();
                Scenario parentScenario = refScenario.getParentScenario();
                refScenarioId = refScenario.getScenarioId();
                parentScenarioId = parentScenario.getScenarioId();
              }

              proc.setString(param++, action);
              proc.setLong(param++, puc.getAdjProductiveUnitCapacityId() == null ? null : puc.getAdjProductiveUnitCapacityId().longValue());
              proc.setLong(param++, puc.getReportedProductiveUnitCapacityId() == null ? null : puc.getReportedProductiveUnitCapacityId().longValue());
              proc.setBigDecimal(param++, puc.getAdjAmount() == null ? null : BigDecimal.valueOf(puc.getAdjAmount()));
              proc.setLong(param++, operation.getFarmingOperationId() == null ? null : operation.getFarmingOperationId().longValue());
              proc.setLong(param++, refScenarioId == null ? null : refScenarioId.longValue());
              proc.setLong(param++, parentScenarioId == null ? null : parentScenarioId.longValue());
              proc.setString(param++, puc.getStructureGroupCode());
              proc.setString(param++, puc.getInventoryItemCode());
              proc.setString(param++, puc.getParticipantDataSrcCode());
              proc.setInt(param++, puc.getRevisionCount());
              proc.setString(param++, user);
              proc.addBatch();
            }
          }
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
  

  public void copyProductiveUnitCapacities(
      final Transaction transaction,
      final FarmingOperation targetOperation,
      final List<ProductiveUnitCapacity> sourcePucs,
      final String participantDataSrcCode,
      final String user)
      throws DataAccessException {
    
    if (sourcePucs == null) {
      return;
    }

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + ADJUST_PUC_PROC, ADJUST_PUC_PARAM, false); ) {
        
        ReferenceScenario targetRefScenario = targetOperation.getFarmingYear().getReferenceScenario();
        Integer parentScenarioId = targetRefScenario.getParentScenario().getScenarioId();
        List<ProductiveUnitCapacity> targetOpPucs = targetOperation.getProductiveUnitCapacities(participantDataSrcCode);

        for (ProductiveUnitCapacity sourcePuc : sourcePucs) {
          
          ProductiveUnitCapacity targetPuc = targetOpPucs.stream()
              .filter(p -> StringUtils.equal(p.getStructureGroupCode(), sourcePuc.getStructureGroupCode())
                  && StringUtils.equal(p.getInventoryItemCode(), sourcePuc.getInventoryItemCode()))
              .findFirst()
              .orElse(null);
          
          String action = AdjustmentService.ACTION_ADD;
          Integer adjProductiveUnitCapacityId = null;
          Integer reportedProductiveUnitCapacityId = null;
          Integer revisionCount;
          revisionCount = null;
          
          if(targetPuc != null) {
            action = AdjustmentService.ACTION_UPDATE;
            adjProductiveUnitCapacityId = targetPuc.getAdjProductiveUnitCapacityId();
            reportedProductiveUnitCapacityId = targetPuc.getReportedProductiveUnitCapacityId();
            revisionCount = targetPuc.getRevisionCount();
            
            if( ! targetPuc.getTotalProductiveCapacityAmount().equals(0.0d) ) {
              throw new IllegalStateException(
                  String.format("Found existing Productive Unit with non-zero value. inventory code: %s, structure group code: %s",
                      sourcePuc.getInventoryItemCode(), sourcePuc.getStructureGroupCode()));
            }
          }
          
          int param = 1;
          
          proc.setString(param++, action);
          proc.setLong(param++, adjProductiveUnitCapacityId == null ? null : adjProductiveUnitCapacityId.longValue());
          proc.setLong(param++, reportedProductiveUnitCapacityId == null ? null : reportedProductiveUnitCapacityId.longValue());
          proc.setBigDecimal(param++, sourcePuc.getTotalProductiveCapacityAmount() == null ? null : BigDecimal.valueOf(sourcePuc.getTotalProductiveCapacityAmount()));
          proc.setLong(param++, targetOperation.getFarmingOperationId() == null ? null : targetOperation.getFarmingOperationId().longValue());
          proc.setLong(param++, targetRefScenario.getScenarioId() == null ? null : targetRefScenario.getScenarioId().longValue());
          proc.setLong(param++, parentScenarioId == null ? null : parentScenarioId.longValue());
          proc.setString(param++, sourcePuc.getStructureGroupCode());
          proc.setString(param++, sourcePuc.getInventoryItemCode());
          proc.setString(param++, participantDataSrcCode);
          proc.setInt(param++, revisionCount);
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
  
  /**
   * @param transaction transaction
   * @param adjustmentMap Map<String action, List<IncomeExpense>>
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void adjustIncomeExpenses(
      final Transaction transaction,
      final Map<String, List<IncomeExpense>> adjustmentMap,
      final String user)
  throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + ADJUST_INCOME_EXPENSE_PROC, ADJUST_INCOME_EXPENSE_PARAM, false); ) {
        
        for(String action : adjustmentMap.keySet()) {
          List<IncomeExpense> actionAdjustments = adjustmentMap.get(action);
          
          if(actionAdjustments != null) {
            for(IncomeExpense ie : actionAdjustments) {
              int param = 1;
              FarmingOperation operation = ie.getFarmingOperation();
              
              Integer refScenarioId = null;
              Integer parentScenarioId = null;
              if (operation.getFarmingYear() != null && operation.getFarmingYear().getReferenceScenario() != null) {            	
                ReferenceScenario refScenario = operation.getFarmingYear().getReferenceScenario();
                Scenario parentScenario = refScenario.getParentScenario();
                refScenarioId = refScenario.getScenarioId();
                parentScenarioId = parentScenario.getScenarioId();
              }

              proc.setString(param++, action);
              proc.setLong(param++, ie.getAdjIncomeExpenseId() == null ? null : ie.getAdjIncomeExpenseId().longValue());
              proc.setLong(param++, ie.getReportedIncomeExpenseId() == null ? null : ie.getReportedIncomeExpenseId().longValue());
              proc.setBigDecimal(param++, ie.getAdjAmount() == null ? null : BigDecimal.valueOf(ie.getAdjAmount()));
              proc.setString(param++, getIndicatorYN(ie.getIsExpense()));
              proc.setString(param++, getIndicatorYN(ie.getLineItem().getIsEligible()));
              proc.setLong(param++, operation.getFarmingOperationId() == null ? null : operation.getFarmingOperationId().longValue());
              proc.setLong(param++, refScenarioId == null ? null : refScenarioId.longValue());
              proc.setLong(param++, parentScenarioId == null ? null : parentScenarioId.longValue());
              proc.setShort(param++, ie.getLineItem().getLineItem() == null ? null : ie.getLineItem().getLineItem().shortValue());
              proc.setInt(param++, ie.getRevisionCount());
              proc.setString(param++, user);
              proc.addBatch();
            }
          }
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


  /**
   * @param transaction transaction
   * @param adjustmentMap Map<String action, List<InventoryItem>>
   * @param user user
   * @throws DataAccessException On Exception
   */
  public void adjustInventories(
      final Transaction transaction,
      final Map<String, List<InventoryItem>> adjustmentMap,
      final String user)
      throws DataAccessException {

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
            + ADJUST_INVENTORY_PROC, ADJUST_INVENTORY_PARAM, false); ) {

        for(String action : adjustmentMap.keySet()) {
          List<InventoryItem> actionAdjustments = adjustmentMap.get(action);
          
          if(actionAdjustments != null) {
            for(InventoryItem item : actionAdjustments) {
              FarmingOperation operation = item.getFarmingOperation();
              
              
              Integer refScenarioId = null;
              Integer parentScenarioId = null;
              if (operation.getFarmingYear() != null && operation.getFarmingYear().getReferenceScenario() != null) {            	
                ReferenceScenario refScenario = operation.getFarmingYear().getReferenceScenario();
                Scenario parentScenario = refScenario.getParentScenario();
                refScenarioId = refScenario.getScenarioId();
                parentScenarioId = parentScenario.getScenarioId();
              }

              // this is mostly here to avoid casts and allow for easy checks
              CropItem cropItem = null;
              if(CROP.equals(item.getInventoryClassCode())) {
                cropItem = (CropItem) item;
              }
              int param = 1;
              proc.setString(param++, action);
              proc.setLong(param++, item.getAdjInventoryId() == null ? null : item.getAdjInventoryId().longValue());
              proc.setLong(param++, item.getReportedInventoryId() == null ? null : item.getReportedInventoryId().longValue());
              proc.setLong(param++, operation.getFarmingOperationId() == null ? null : operation.getFarmingOperationId().longValue());
              proc.setLong(param++, refScenarioId == null ? null : refScenarioId.longValue());
              proc.setLong(param++, parentScenarioId == null ? null : parentScenarioId.longValue());
              proc.setBigDecimal(param++, item.getAdjPriceStart() == null ? null : BigDecimal.valueOf(item.getAdjPriceStart()));
              proc.setBigDecimal(param++, item.getAdjPriceEnd() == null ? null : BigDecimal.valueOf(item.getAdjPriceEnd()));
              proc.setBigDecimal(param++, item.getAdjEndYearProducerPrice() == null ? null : BigDecimal.valueOf(item.getAdjEndYearProducerPrice()));
              proc.setBigDecimal(param++, item.getAdjQuantityStart() == null ? null : BigDecimal.valueOf(item.getAdjQuantityStart()));
              proc.setBigDecimal(param++, item.getAdjQuantityEnd() == null ? null : BigDecimal.valueOf(item.getAdjQuantityEnd()));
              proc.setBigDecimal(param++, item.getAdjStartOfYearAmount() == null ? null : BigDecimal.valueOf(item.getAdjStartOfYearAmount()));
              proc.setBigDecimal(param++, item.getAdjEndOfYearAmount() == null ? null : BigDecimal.valueOf(item.getAdjEndOfYearAmount()));
              if(cropItem != null) {
                proc.setBigDecimal(param++, cropItem.getAdjQuantityProduced() == null ? null : BigDecimal.valueOf(cropItem.getAdjQuantityProduced()));
                proc.setString(param++, cropItem.getCropUnitCode());
                proc.setBigDecimal(param++, cropItem.getOnFarmAcres() == null ? null : BigDecimal.valueOf(cropItem.getOnFarmAcres()));
                proc.setBigDecimal(param++, cropItem.getUnseedableAcres() == null ? null : BigDecimal.valueOf(cropItem.getUnseedableAcres()));
              } else if(item.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
                proc.setNull(param++, Types.NUMERIC);
                proc.setString(param++, CropUnitCodes.getLivestockUnitCode(item.getInventoryItemCode()));
                proc.setNull(param++, Types.VARCHAR);
                proc.setNull(param++, Types.VARCHAR);
              } else {
                proc.setNull(param++, Types.NUMERIC);
                proc.setNull(param++, Types.VARCHAR);
                proc.setNull(param++, Types.VARCHAR);
                proc.setNull(param++, Types.VARCHAR);
              }
              proc.setLong(param++, item.getCommodityXrefId() == null ? null : item.getCommodityXrefId().longValue());
              proc.setInt(param++, item.getRevisionCount());
              proc.setString(param++, item.getInventoryItemCode());
              proc.setString(param++, item.getInventoryClassCode());
              proc.setString(param++, user);
              proc.addBatch();
            }
          }
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
  

  public void copyInventories(
      final Transaction transaction,
      final FarmingOperation targetOperation,
      final List<InventoryItem> sourceItems,
      final String user)
      throws DataAccessException {
    
    if (sourceItems == null) {
      return;
    }

    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection, PACKAGE_NAME + "."
          + ADJUST_INVENTORY_PROC, ADJUST_INVENTORY_PARAM, false); ) {
        
        ReferenceScenario targetRefScenario = targetOperation.getFarmingYear().getReferenceScenario();
        Integer parentScenarioId = targetRefScenario.getParentScenario().getScenarioId();
        List<InventoryItem> targetOpItems = targetOperation.getInventoryItems();

        for (InventoryItem sourceItem : sourceItems) {
          
          InventoryItem targetItem = targetOpItems.stream()
              .filter(i -> StringUtils.equal(i.getInventoryClassCode(), sourceItem.getInventoryClassCode())
                  && StringUtils.equal(i.getInventoryItemCode(), sourceItem.getInventoryItemCode()))
              .findFirst()
              .orElse(null);
          
          String inventoryClassCode = sourceItem.getInventoryClassCode();
          boolean isCrop = CROP.equals(inventoryClassCode);
          boolean isLivestock = LIVESTOCK.equals(inventoryClassCode);
          boolean isAccrual = StringUtils.isOneOf(inventoryClassCode, INPUT, RECEIVABLE, PAYABLE);
          
          CropItem targetCropItem = null;
          CropItem sourceCropItem = null;
          Double sourceOnFarmAcres = null;
          Double sourceUnseedableAcres = null;
          Double sourceEndYearProducerPrice = null;
          Double targetOnFarmAcres = null;
          Double targetUnseedableAcres = null;
          Double targetEndYearProducerPrice = null;
          
          if(isCrop) {
            sourceCropItem = (CropItem) sourceItem;
            targetCropItem = (CropItem) targetItem;
          }
          
          if(sourceCropItem != null) {
            sourceOnFarmAcres = sourceCropItem.getOnFarmAcres();
            sourceUnseedableAcres = sourceCropItem.getUnseedableAcres();
            sourceEndYearProducerPrice = sourceCropItem.getUnseedableAcres();
          }
          if(targetCropItem != null) {
            targetOnFarmAcres = targetCropItem.getOnFarmAcres();
            targetUnseedableAcres = targetCropItem.getUnseedableAcres();
            targetEndYearProducerPrice = targetCropItem.getUnseedableAcres();
          }
          
          boolean sourceHasOnFarmAcres = sourceOnFarmAcres != null && sourceOnFarmAcres != 0;
          boolean sourceHasUnseedableAcres = sourceUnseedableAcres != null && sourceUnseedableAcres != 0;
          boolean sourceHasEndYearProducerPrice = sourceEndYearProducerPrice != null && sourceEndYearProducerPrice != 0;
          boolean targetHasOnFarmAcres = targetOnFarmAcres != null && targetOnFarmAcres != 0;
          boolean targetHasUnseedableAcres = targetUnseedableAcres != null && targetUnseedableAcres != 0;
          boolean targetHasEndYearProducerPrice = targetEndYearProducerPrice != null && targetEndYearProducerPrice != 0;
          
          String action = AdjustmentService.ACTION_ADD;
          Integer adjInventoryId = null;
          Integer reportedInventoryId = null;
          Integer revisionCount = null;

          Double reportedPriceStart = null;
          Double reportedPriceEnd = null;
          Double reportedQuantityProduced = null;
          Double reportedQuantityStart = null;
          Double reportedQuantityEnd = null;
          Double reportedStartOfYearAmount = null;
          Double reportedEndOfYearAmount = null;
          
          if(targetItem != null) {
            reportedInventoryId = targetItem.getReportedInventoryId();
            
            reportedPriceStart = targetItem.getReportedPriceStart();
            reportedPriceEnd = targetItem.getReportedPriceEnd();
            
            reportedQuantityStart = targetItem.getReportedQuantityStart();
            reportedQuantityEnd = targetItem.getReportedQuantityEnd();
            
            reportedStartOfYearAmount = targetItem.getReportedStartOfYearAmount();
            reportedEndOfYearAmount = targetItem.getReportedEndOfYearAmount();
            
            if(targetCropItem != null) {
              reportedQuantityProduced = targetCropItem.getReportedQuantityProduced();
            }
            
            if(targetItem.getAdjInventoryId() != null) {
              action = AdjustmentService.ACTION_UPDATE;
              adjInventoryId = targetItem.getAdjInventoryId();
              revisionCount = targetItem.getRevisionCount();
              
              if( (isCrop && CropItemUtils.hasNonZeroQuantities((CropItem) targetItem))
                  || (isLivestock && LivestockItemUtils.hasNonZeroQuantities((LivestockItem) targetItem))
                  || (isAccrual && AccrualItemUtils.hasNonZeroAmounts(targetItem))) {
                throw new IllegalStateException(
                    String.format("Found existing Inventory Item with non-zero value. inventory code: %s",
                        sourceItem.getInventoryItemCode()));
              }
            }
          }
          
          Double adjPriceStart = AdjustmentUtils.calculatePriceAdjustment(sourceItem.getTotalPriceStart(), reportedPriceStart);
          Double adjPriceEnd = AdjustmentUtils.calculatePriceAdjustment(sourceItem.getTotalPriceEnd(), reportedPriceEnd);
          
          Double adjQuantityStart = AdjustmentUtils.calculateQuantityAdjustment(sourceItem.getTotalQuantityStart(), reportedQuantityStart);
          Double adjQuantityEnd = AdjustmentUtils.calculateQuantityAdjustment(sourceItem.getTotalQuantityEnd(), reportedQuantityEnd);
          
          Double adjStartOfYearAmount = AdjustmentUtils.calculateAccrualAdjustment(sourceItem.getTotalStartOfYearAmount(), reportedStartOfYearAmount);
          Double adjEndOfYearAmount = AdjustmentUtils.calculateAccrualAdjustment(sourceItem.getTotalEndOfYearAmount(), reportedEndOfYearAmount);
          
          Double adjQuantityProduced = null;
          if(sourceCropItem != null) {
            adjQuantityProduced = AdjustmentUtils.calculateQuantityAdjustment(sourceCropItem.getTotalQuantityProduced(), reportedQuantityProduced);
          }
          
          int param = 1;
          proc.setString(param++, action);
          proc.setLong(param++, adjInventoryId == null ? null : adjInventoryId.longValue());
          proc.setLong(param++, reportedInventoryId == null ? null : reportedInventoryId.longValue());
          proc.setLong(param++, targetOperation.getFarmingOperationId() == null ? null : targetOperation.getFarmingOperationId().longValue());
          proc.setLong(param++, targetRefScenario.getScenarioId() == null ? null : targetRefScenario.getScenarioId().longValue());
          proc.setLong(param++, parentScenarioId == null ? null : parentScenarioId.longValue());
          proc.setBigDecimal(param++, adjPriceStart == null ? null : BigDecimal.valueOf(adjPriceStart));
          proc.setBigDecimal(param++, adjPriceEnd == null ? null : BigDecimal.valueOf(adjPriceEnd));
          
          // TODO Same issue as onFarmAcres and unseedableAcres, I think.
          if(!targetHasEndYearProducerPrice && sourceHasEndYearProducerPrice) {
            proc.setBigDecimal(param++, BigDecimal.valueOf(ObjectUtils.ifNull(sourceEndYearProducerPrice, Double.valueOf(0))));
          } else {
            proc.setBigDecimal(param++, targetEndYearProducerPrice == null ? null : BigDecimal.valueOf(targetEndYearProducerPrice));
          }
          
          proc.setBigDecimal(param++, adjQuantityStart == null ? null : BigDecimal.valueOf(adjQuantityStart));
          proc.setBigDecimal(param++, adjQuantityEnd == null ? null : BigDecimal.valueOf(adjQuantityEnd));
          proc.setBigDecimal(param++, adjStartOfYearAmount == null ? null : BigDecimal.valueOf(adjStartOfYearAmount));
          proc.setBigDecimal(param++, adjEndOfYearAmount == null ? null : BigDecimal.valueOf(adjEndOfYearAmount));
          if(sourceCropItem != null) {
            proc.setBigDecimal(param++, adjQuantityProduced == null ? null : BigDecimal.valueOf(adjQuantityProduced));
            proc.setString(param++, sourceCropItem.getCropUnitCode());
            
            // TODO onFarmAcres, unseedableAcres are being populated in the adjustment read and displayed only from the reported record. Fix?
            if(!targetHasOnFarmAcres && !targetHasUnseedableAcres && (sourceHasOnFarmAcres || sourceHasUnseedableAcres)) {
              proc.setBigDecimal(param++, BigDecimal.valueOf(ObjectUtils.ifNull(sourceOnFarmAcres, Double.valueOf(0))));
              proc.setBigDecimal(param++, BigDecimal.valueOf(ObjectUtils.ifNull(sourceUnseedableAcres, Double.valueOf(0))));
            } else {
              proc.setBigDecimal(param++, targetOnFarmAcres == null ? null : BigDecimal.valueOf(targetOnFarmAcres));
              proc.setBigDecimal(param++, targetUnseedableAcres == null ? null : BigDecimal.valueOf(targetUnseedableAcres));
            }
          } else if(sourceItem.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
            proc.setNull(param++, Types.NUMERIC);
            proc.setString(param++, CropUnitCodes.getLivestockUnitCode(sourceItem.getInventoryItemCode()));
            proc.setNull(param++, Types.VARCHAR);
            proc.setNull(param++, Types.VARCHAR);
          } else {
            proc.setNull(param++, Types.NUMERIC);
            proc.setNull(param++, Types.VARCHAR);
            proc.setNull(param++, Types.VARCHAR);
            proc.setNull(param++, Types.VARCHAR);
          }
          proc.setLong(param++, sourceItem.getCommodityXrefId() == null ? null : sourceItem.getCommodityXrefId().longValue());
          proc.setInt(param++, revisionCount);
          proc.setString(param++, sourceItem.getInventoryItemCode());
          proc.setString(param++, sourceItem.getInventoryClassCode());
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

}
