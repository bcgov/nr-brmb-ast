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
  private static final String PACKAGE_NAME = "FARM_ADJUSTMENT_PKG";
  
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
            proc.setInt(param++, puc.getAdjProductiveUnitCapacityId());
            proc.setInt(param++, puc.getReportedProductiveUnitCapacityId());
            proc.setDouble(param++, puc.getAdjAmount());
            proc.setInt(param++, operation.getFarmingOperationId());
            proc.setInt(param++, refScenarioId);
            proc.setInt(param++, parentScenarioId);
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

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
        proc.setInt(param++, adjProductiveUnitCapacityId);
        proc.setInt(param++, reportedProductiveUnitCapacityId);
        proc.setDouble(param++, sourcePuc.getTotalProductiveCapacityAmount());
        proc.setInt(param++, targetOperation.getFarmingOperationId());
        proc.setInt(param++, targetRefScenario.getScenarioId());
        proc.setInt(param++, parentScenarioId);
        proc.setString(param++, sourcePuc.getStructureGroupCode());
        proc.setString(param++, sourcePuc.getInventoryItemCode());
        proc.setString(param++, participantDataSrcCode);
        proc.setInt(param++, revisionCount);
        proc.setString(param++, user);
        proc.addBatch();
      }

      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
            proc.setInt(param++, ie.getAdjIncomeExpenseId());
            proc.setInt(param++, ie.getReportedIncomeExpenseId());
            proc.setDouble(param++, ie.getAdjAmount());
            proc.setString(param++, getIndicatorYN(ie.getIsExpense()));
            proc.setString(param++, getIndicatorYN(ie.getLineItem().getIsEligible()));
            proc.setInt(param++, operation.getFarmingOperationId());
            proc.setInt(param++, refScenarioId);
            proc.setInt(param++, parentScenarioId);
            proc.setInt(param++, ie.getLineItem().getLineItem());
            proc.setInt(param++, ie.getRevisionCount());
            proc.setString(param++, user);
            proc.addBatch();
          }
        }
      }
      
      proc.executeBatch();
      
    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
            proc.setInt(param++, item.getAdjInventoryId());
            proc.setInt(param++, item.getReportedInventoryId());
            proc.setInt(param++, operation.getFarmingOperationId());
            proc.setInt(param++, refScenarioId);
            proc.setInt(param++, parentScenarioId);
            proc.setDouble(param++, item.getAdjPriceStart());
            proc.setDouble(param++, item.getAdjPriceEnd());
            proc.setDouble(param++, item.getAdjEndYearProducerPrice());
            proc.setDouble(param++, item.getAdjQuantityStart());
            proc.setDouble(param++, item.getAdjQuantityEnd());
            proc.setDouble(param++, item.getAdjStartOfYearAmount());
            proc.setDouble(param++, item.getAdjEndOfYearAmount());
            if(cropItem != null) {
            	proc.setDouble(param++, cropItem.getAdjQuantityProduced());
              proc.setString(param++, cropItem.getCropUnitCode());
              proc.setDouble(param++, cropItem.getOnFarmAcres());
              proc.setDouble(param++, cropItem.getUnseedableAcres());
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
            proc.setInt(param++, item.getCommodityXrefId());
            proc.setInt(param++, item.getRevisionCount());
            proc.setString(param++, item.getInventoryItemCode());
            proc.setString(param++, item.getInventoryClassCode());
            proc.setString(param++, user);
            proc.addBatch();
          }
        }
      }

      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
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
        proc.setInt(param++, adjInventoryId);
        proc.setInt(param++, reportedInventoryId);
        proc.setInt(param++, targetOperation.getFarmingOperationId());
        proc.setInt(param++, targetRefScenario.getScenarioId());
        proc.setInt(param++, parentScenarioId);
        proc.setDouble(param++, adjPriceStart);
        proc.setDouble(param++, adjPriceEnd);
        
        // TODO Same issue as onFarmAcres and unseedableAcres, I think.
        if(!targetHasEndYearProducerPrice && sourceHasEndYearProducerPrice) {
          proc.setDouble(param++, ObjectUtils.ifNull(sourceEndYearProducerPrice, Double.valueOf(0)));
        } else {
          proc.setDouble(param++, targetEndYearProducerPrice);
        }
        
        proc.setDouble(param++, adjQuantityStart);
        proc.setDouble(param++, adjQuantityEnd);
        proc.setDouble(param++, adjStartOfYearAmount);
        proc.setDouble(param++, adjEndOfYearAmount);
        if(sourceCropItem != null) {
          proc.setDouble(param++, adjQuantityProduced);
          proc.setString(param++, sourceCropItem.getCropUnitCode());
          
          // TODO onFarmAcres, unseedableAcres are being populated in the adjustment read and displayed only from the reported record. Fix?
          if(!targetHasOnFarmAcres && !targetHasUnseedableAcres && (sourceHasOnFarmAcres || sourceHasUnseedableAcres)) {
            proc.setDouble(param++, ObjectUtils.ifNull(sourceOnFarmAcres, Double.valueOf(0)));
            proc.setDouble(param++, ObjectUtils.ifNull(sourceUnseedableAcres, Double.valueOf(0)));
          } else {
            proc.setDouble(param++, targetOnFarmAcres);
            proc.setDouble(param++, targetUnseedableAcres);
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
        proc.setInt(param++, sourceItem.getCommodityXrefId());
        proc.setInt(param++, revisionCount);
        proc.setString(param++, sourceItem.getInventoryItemCode());
        proc.setString(param++, sourceItem.getInventoryClassCode());
        proc.setString(param++, user);
        proc.addBatch();
      }

      proc.executeBatch();

    } catch (SQLException e) {
      logSqlException(e);
      handleException(e);
    }
  }

}
