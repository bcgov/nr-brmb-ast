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
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.FmvCalculator;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.dao.AdjustmentDAO;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ReasonabilityWriteDAO;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.LineItemNotFoundException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.AdjustmentUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ObjectUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * This service groups together actions related to creating
 * and updating adjustments to FARM data.
 * 
 * @author awilkinson
 * @created Jan 10, 2011
 */
public class AdjustmentServiceImpl extends BaseService implements AdjustmentService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param scenario Scenario
   * @param adjustmentMap Map<String action, List<ProductiveUnitCapacity>>
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void adjustProductiveUnitCapacities(
      final Scenario scenario,
      final Map<String, List<ProductiveUnitCapacity>> adjustmentMap,
      final String user)
  throws ServiceException {
    logMethodStart(logger);
    
    Transaction transaction = null;
    AdjustmentDAO adjDAO = new AdjustmentDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      adjDAO.adjustProductiveUnitCapacities(
          transaction,
          adjustmentMap,
          user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(), user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    logMethodEnd(logger);
  }
  

  /**
   * @param scenario Scenario
   * @param adjustmentMap Map<String action, List<IncomeExpense>>
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void adjustIncomeExpenses(
      final Scenario scenario,
      final Map<String, List<IncomeExpense>> adjustmentMap,
      final String user)
  throws ServiceException {
    logMethodStart(logger);
    
    Transaction transaction = null;
    AdjustmentDAO adjDAO = new AdjustmentDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      adjDAO.adjustIncomeExpenses(
          transaction,
          adjustmentMap,
          user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(), user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (LineItemNotFoundException e) {
      logger.warn("Line Item not found:", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    logMethodEnd(logger);
  }


  /**
   * @param scenario Scenario
   * @param adjustmentMap Map<String action, List<InventoryItem>>
   * @param user user
   * @throws ServiceException On Exception
   */
  @Override
  public void adjustInventoriesAndAccruals(
      final Scenario scenario,
      final Map<String, List<InventoryItem>> adjustmentMap,
      final String user)
  throws ServiceException {
    logMethodStart(logger);
    
    Transaction transaction = null;
    AdjustmentDAO adjDAO = new AdjustmentDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      adjDAO.adjustInventories(
          transaction,
          adjustmentMap,
          user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(), user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    logMethodEnd(logger);
  }


  /**
   * @param scenario Scenario
   * @throws ServiceException On Exception 
   */
  @Override
  public void makeInventoryValuationAdjustments(
      final Scenario scenario)
  throws ServiceException {

    makeInventoryValuationAdjustments(scenario, true, false);
  }


  /**
   * @param scenario Scenario
   * @param saveAdjustments saveAdjustments
   * @throws ServiceException On Exception 
   */
  @Override
  public void makeInventoryValuationAdjustments(
      final Scenario scenario, final boolean saveAdjustments,
      final boolean makeFifoAdjustments)
  throws ServiceException {
    
    logger.debug("<makeInventoryValuationAdjustments - Auto-adjusting Inventories based on inventory valuation rules");

    List<InventoryItem> adjAddList = new ArrayList<>();
    List<InventoryItem> adjUpdateList = new ArrayList<>();
    
    addAdjusmtentsForExistingItems(scenario, adjAddList, adjUpdateList);
    addAdjusmtentsForMissingItems(scenario, adjAddList);
    
    if(makeFifoAdjustments) {
      makeInventoryEndEqualStart(scenario, adjAddList, adjUpdateList);
      makeApplesReceivableEndEqualStart(scenario, adjAddList, adjUpdateList);
    }
    
    if(saveAdjustments) {
      processInventoryAdjustments(scenario, adjAddList, adjUpdateList);
    }
    
    logMethodEnd(logger);
  }


  /**
   * For inventory, if there is a start value with no end (quantity or price) set the end value to the start value. 
   */
  private void makeInventoryEndEqualStart(
      Scenario scenario, List<InventoryItem> adjAddList, List<InventoryItem> adjUpdateList) {
    logMethodStart(logger);

    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      if(refScenario != null && refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
          for(ProducedItem item : fo.getProducedItems()) {

            boolean adjust = false;
            
            Double qs = item.getTotalQuantityStart();
            Double qe = item.getTotalQuantityEnd();
            Double ps = item.getTotalPriceStart();
            Double pe = item.getTotalPriceEnd();
            
            Double qeAdj = item.getAdjQuantityEnd();
            Double peAdj = item.getAdjPriceEnd();
            
            if(qe == null && qs != null) {
              qeAdj = qs;
              item.setAdjQuantityEnd(qeAdj);
              adjust = true;
            }
            
            if(pe == null && ps != null) {
              peAdj = ps;
              item.setAdjPriceEnd(peAdj);
              adjust = true;
            }
             
            if(adjust) {
              if(item.getAdjInventoryId() == null) {
                if( ! containsItem(adjAddList, item) ) {
                  adjAddList.add(item);
                }
              } else if( ! containsItem(adjUpdateList, item) ) {
                adjUpdateList.add(item);
              }
            }
    
          }
        }
      }
    }
    
    logMethodEnd(logger);
  }  


  /**
   * For apples receivables, if there is a start value with no end, set the end value to the start value. 
   */
  private void makeApplesReceivableEndEqualStart(
      Scenario scenario, List<InventoryItem> adjAddList, List<InventoryItem> adjUpdateList) {
    logMethodStart(logger);

    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      if(refScenario != null && refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
          for(ReceivableItem item : fo.getReceivableItems()) {

            boolean adjust = false;
            
            Double startAmount = item.getTotalStartOfYearAmount();
            Double endAmount = item.getTotalEndOfYearAmount();
            
            Double endAdj = item.getAdjEndOfYearAmount();
            
            if(endAmount == null && startAmount != null) {
              endAdj = startAmount;
              item.setAdjEndOfYearAmount(endAdj);
              adjust = true;
            }
             
            if(adjust) {
              if(item.getAdjInventoryId() == null) {
                if( ! containsItem(adjAddList, item) ) {
                  adjAddList.add(item);
                }
              } else if( ! containsItem(adjUpdateList, item) ) {
                adjUpdateList.add(item);
              }
            }
    
          }
        }
      }
    }
    
    logMethodEnd(logger);
  }


  @Override
  public void copyInventoriesAndAccruals(
      final Scenario scenario,
      final FarmingOperation targetOperation,
      final List<InventoryItem> sourceItems, 
      final String user) throws ServiceException {
    logMethodStart(logger);
    
    Transaction transaction = null;
    AdjustmentDAO adjDAO = new AdjustmentDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      adjDAO.copyInventories(transaction, targetOperation, sourceItems, user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(), user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    logMethodEnd(logger);
  }


  @Override
  public void copyProductiveUnitCapacities(
      final Scenario scenario,
      final FarmingOperation targetOperation,
      final List<ProductiveUnitCapacity> sourcePucs, 
      final String participantDataSrcCode,
      final String user) throws ServiceException {
    logMethodStart(logger);
    
    Transaction transaction = null;
    AdjustmentDAO adjDAO = new AdjustmentDAO();
    CalculatorDAO calcDAO = new CalculatorDAO();
    ReasonabilityWriteDAO reasonabilityDAO = new ReasonabilityWriteDAO();
    
    try {
      transaction = openTransaction();
      transaction.begin();

      adjDAO.copyProductiveUnitCapacities(transaction, targetOperation, sourcePucs, participantDataSrcCode, user);
      
      reasonabilityDAO.flagReasonabilityTestsStale(transaction,
          scenario.getReasonabilityTestResults(),
          user);
      
      calcDAO.updateScenarioRevisionCount(transaction,
          scenario.getScenarioId(),
          scenario.getRevisionCount(), user);

      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      rollback(transaction);
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      rollback(transaction);
      if(e instanceof ServiceException) {
        throw (ServiceException) e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    
    logMethodEnd(logger);
  }


  /**
   * @param scenario scenario
   * @param adjUpdateList adjUpdateList
   * @param adjAddList adjAddList
   */
  private void addAdjusmtentsForExistingItems(Scenario scenario, List<InventoryItem> adjAddList, List<InventoryItem> adjUpdateList) {
    logMethodStart(logger);
    
    InventoryCalculator invCalc = CalculatorFactory.getInventoryCalculator();

    // perform inventory valuation rules for current program year only
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      List<FarmingOperation> curYearFOs = scenario.getFarmingYear().getFarmingOperations();
      for(FarmingOperation fo : curYearFOs) {
        for(ProducedItem pi : fo.getProducedItems()) {
          
          Double rqs = pi.getReportedQuantityStart();
          Double rqe = pi.getReportedQuantityEnd();
          Double rps = pi.getReportedPriceStart();
          Double rpe = pi.getReportedPriceEnd();
          
          Double qs = invCalc.getStartQuantity(pi);
          Double qe = invCalc.getEndQuantity(pi);
  
          Double pe = invCalc.getEndPrice(pi, qe);
          Double ps = invCalc.getStartPrice(pi, qs, pe);
          
          Double qsAdj = AdjustmentUtils.calculateQuantityAdjustment(qs, rqs);
          Double qeAdj = AdjustmentUtils.calculateQuantityAdjustment(qe, rqe);
          Double psAdj = AdjustmentUtils.calculatePriceAdjustment(ps, rps);
          Double peAdj = AdjustmentUtils.calculatePriceAdjustment(pe, rpe);
  
          if(qsAdj != null || qeAdj != null || psAdj != null || peAdj != null) {
            pi.setAdjQuantityStart(qsAdj);
            pi.setAdjQuantityEnd(qeAdj);
            pi.setAdjPriceStart(psAdj);
            pi.setAdjPriceEnd(peAdj);
            
            adjAddList.add(pi);
          }
        }
      }
    }


    // For inventory, perform "Pull start from end" rule for reference years 
    for(ReferenceScenario refScenario : scenario.getReferenceScenarios()) {
      if(refScenario != null && refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
          for(ProducedItem item : fo.getProducedItems()) {

            List<InventoryItem> sameYearMatchingInventoryItems = invCalc.getSameYearMatchingInventoryItems(item);
            boolean hasDuplicates = sameYearMatchingInventoryItems.size() > 1;
            if(!hasDuplicates) {
              
              boolean adjust = false;
              boolean isMarketCommodity = invCalc.isMarketCommodity(item);
              boolean missingAStartValue;
  
              if(isMarketCommodity) {
                missingAStartValue =
                    item.getTotalQuantityStart() == null || item.getTotalPriceStart() == null;
              } else {
                missingAStartValue =
                    item.getTotalQuantityStart() == null;
  
                if(!MathUtils.equalToTwoDecimalPlaces(item.getTotalPriceStart(), item.getTotalPriceEnd())) {
                  Double priceStartAdj = AdjustmentUtils.calculatePriceAdjustment(
                      item.getTotalPriceEnd(), item.getReportedPriceStart());
                  item.setAdjPriceStart(priceStartAdj);
                  adjust = true;
                }
              }
    
              if(missingAStartValue) {
                InventoryItem prevYearItem = invCalc.getPreviousYearInventoryItem(item);
                
                if(prevYearItem != null) {
                  Double quantityStartAdj = null;
                  Double priceStartAdj = null;
                  
                  if(item.getTotalQuantityStart() == null && prevYearItem.getTotalQuantityEnd() != null) {
                    quantityStartAdj = prevYearItem.getTotalQuantityEnd();
                    item.setAdjQuantityStart(quantityStartAdj);
                    adjust = true;
                  }
                  
                  if(!isMarketCommodity
                      && item.getTotalPriceStart() == null
                      && item.getTotalQuantityStart() != null
                      && prevYearItem.getTotalPriceEnd() != null) {
                    priceStartAdj = prevYearItem.getTotalPriceEnd();
                    item.setAdjPriceStart(priceStartAdj);
                    adjust = true;
                  }
                }
              }
              
              if(adjust) {
                if(item.getAdjInventoryId() == null) {
                  adjAddList.add(item);
                } else {
                  adjUpdateList.add(item);
                }
              }
            }
    
          }
        }
      }
    }


    // For accruals, perform "Pull start from end" rule for all years
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      if(refScenario != null && refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
          for(InventoryItem item : fo.getAccrualItems()) {

            List<InventoryItem> sameYearMatchingInventoryItems = invCalc.getSameYearMatchingInventoryItems(item);
            boolean hasDuplicates = sameYearMatchingInventoryItems.size() > 1;
            if(!hasDuplicates) {
              boolean missingStartValue = item.getTotalStartOfYearAmount() == null;
              if(missingStartValue) {
                Double startAdj = invCalc.getStartOfYearAmount(item);
                if(startAdj != null) {
                  item.setAdjStartOfYearAmount(startAdj);
                  if(item.getAdjInventoryId() == null) {
                    adjAddList.add(item);
                  } else {
                    adjUpdateList.add(item);
                  }
                }
              }
            }
            
          }
        }
      }
    }
    
    logMethodEnd(logger);
  }


  /**
   * @param scenario scenario
   * @param adjAddList adjAddList
   */
  private void addAdjusmtentsForMissingItems(Scenario scenario, List<InventoryItem> adjAddList) {
    logMethodStart(logger);
    
    for(ReferenceScenario curScenario : scenario.getAllScenarios()) {
      boolean isProgramYear = curScenario.getYear().equals(scenario.getYear());
      
      int yearToLookFor = curScenario.getYear().intValue() - 1;
      ReferenceScenario prevRs = null;
      
      for(ReferenceScenario currentRs : scenario.getReferenceScenarios()) {
        
        if(currentRs.getYear().intValue() == yearToLookFor) {
          prevRs = currentRs;
          break;
        }
      }
      
      InventoryCalculator invCalc = CalculatorFactory.getInventoryCalculator();
      FmvCalculator fmvCalc = CalculatorFactory.getFmvCalculator();
      
      if(prevRs != null && prevRs.getFarmingYear() != null
          && prevRs.getFarmingYear().getFarmingOperations() != null) {
        
        for(FarmingOperation prevYearFo : prevRs.getFarmingYear().getFarmingOperations()) {
          String schedule = prevYearFo.getSchedule();
          FarmingOperation curYearFo = curScenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
          
          if(curYearFo != null) {
  
            for(ProducedItem prevYearPi : prevYearFo.getProducedItems()) {
              
              Double prevYearQtyEnd = prevYearPi.getTotalQuantityEnd();
              if(prevYearQtyEnd != null && prevYearQtyEnd.doubleValue() > 0) {
                
                List<InventoryItem> nextYearInventoryItems = invCalc.getNextYearInventoryItems(prevYearPi);
                if(nextYearInventoryItems.isEmpty()) {
                  ProducedItem curYearPi = null;
                  
                  if(prevYearPi.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
                    CropItem prevYearCropItem = (CropItem) prevYearPi;
                    CropItem curYearCropItem = new CropItem();
                    curYearPi = curYearCropItem;
                    curYearCropItem.setCropUnitCode(prevYearCropItem.getCropUnitCode());
                    List<CropItem> cropItems = curYearFo.getCropItems();
                    if(cropItems == null) {
                      cropItems = new ArrayList<>();
                      curYearFo.setCropItems(cropItems);
                    }
                    cropItems.add(curYearCropItem);
                  } else /*if(prevYearPi.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK))*/ {
                    LivestockItem curYearLivestockItem = new LivestockItem();
                    curYearPi = curYearLivestockItem;
                    List<LivestockItem> livestockItems = curYearFo.getLivestockItems();
                    if(livestockItems == null) {
                      livestockItems = new ArrayList<>();
                      curYearFo.setLivestockItems(livestockItems);
                    }
                    livestockItems.add(curYearLivestockItem);
                  }
                  
                  curYearPi.setFarmingOperation(curYearFo);
                  curYearPi.setInventoryItemCode(prevYearPi.getInventoryItemCode());
                  curYearPi.setInventoryClassCode(prevYearPi.getInventoryClassCode());
                  curYearPi.setCommodityXrefId(prevYearPi.getCommodityXrefId());
                  curYearPi.setAdjQuantityStart(prevYearQtyEnd);
                  curYearPi.setIsEligible(prevYearPi.getIsEligible());
                  
                  // Use this logic to get the price in case there are more than one matching inventory item
                  // then the quantity and price will come from the same item
                  // unless the price from this item is out of FMV range.
                  // Only check FMV range for the current program year.
                  Double prevYearPriceEnd = prevYearPi.getTotalPriceEnd();
                  if((prevYearPriceEnd != null && !fmvCalc.isPriceEndOutOfVariance(prevYearPi))
                      || !isProgramYear) {
                    curYearPi.setAdjPriceStart(prevYearPriceEnd);
                  } else {
                    Double calcPriceStart = invCalc.getStartPrice(curYearPi, prevYearQtyEnd, null);
                    curYearPi.setAdjPriceStart(calcPriceStart);
                  }
    
                  adjAddList.add(curYearPi);
                }
              }
            }
            
            
            for(InventoryItem prevYearItem : prevYearFo.getAccrualItems()) {
              
              Double prevYearEnd = prevYearItem.getTotalEndOfYearAmount();
              if(prevYearEnd != null && prevYearEnd.doubleValue() > 0) {
                
                List<InventoryItem> nextYearInventoryItems = invCalc.getNextYearInventoryItems(prevYearItem);
                if(nextYearInventoryItems.isEmpty()) {
                  InventoryItem curYearItem = null;
                  
                  if(prevYearItem.getInventoryClassCode().equals(InventoryClassCodes.INPUT)) {
                    curYearItem = new InputItem();
                    List<InputItem> inputItems = curYearFo.getInputItems();
                    if(inputItems == null) {
                      inputItems = new ArrayList<>();
                      curYearFo.setInputItems(inputItems);
                    }
                    inputItems.add((InputItem) curYearItem);
                  } else if(prevYearItem.getInventoryClassCode().equals(InventoryClassCodes.RECEIVABLE)) {
                    curYearItem = new ReceivableItem();
                    List<ReceivableItem> receivableItems = curYearFo.getReceivableItems();
                    if(receivableItems == null) {
                      receivableItems = new ArrayList<>();
                      curYearFo.setReceivableItems(receivableItems);
                    }
                    receivableItems.add((ReceivableItem) curYearItem);
                  } else /*if(prevYearItem.getInventoryClassCode().equals(InventoryClassCodes.PAYABLE))*/ {
                    curYearItem = new PayableItem();
                    List<PayableItem> payableItems = curYearFo.getPayableItems();
                    if(payableItems == null) {
                      payableItems = new ArrayList<>();
                      curYearFo.setPayableItems(payableItems);
                    }
                    payableItems.add((PayableItem) curYearItem);
                  }
                  
                  curYearItem.setFarmingOperation(curYearFo);
                  curYearItem.setInventoryItemCode(prevYearItem.getInventoryItemCode());
                  curYearItem.setInventoryClassCode(prevYearItem.getInventoryClassCode());
                  curYearItem.setCommodityXrefId(prevYearItem.getCommodityXrefId());
                  curYearItem.setAdjStartOfYearAmount(prevYearEnd);
                  curYearItem.setIsEligible(prevYearItem.getIsEligible());
                  
                  adjAddList.add(curYearItem);
                }
              }
            }
  
          }
  
        }
      }
    }
    
    logMethodEnd(logger);
  }


  /**
   * @param scenario scenario
   * @param adjAddList List<InventoryItem>
   * @param adjUpdateList adjUpdateList
   * @throws ServiceException On Exception
   */
  private void processInventoryAdjustments(
      Scenario scenario, List<InventoryItem> adjAddList, List<InventoryItem> adjUpdateList)
  throws ServiceException {
    logMethodStart(logger);
    
    if( ! adjAddList.isEmpty() || ! adjUpdateList.isEmpty() ) {
      Map<String, List<InventoryItem>> adjustmentMap = new HashMap<>();
      adjustmentMap.put(AdjustmentService.ACTION_ADD, adjAddList);
      adjustmentMap.put(AdjustmentService.ACTION_UPDATE, adjUpdateList);

      
      Transaction transaction = null;
      AdjustmentDAO adjDAO = new AdjustmentDAO();
      
      try {
        transaction = openTransaction();
        transaction.begin();

        adjDAO.adjustInventories(
            transaction,
            adjustmentMap,
            AUTO_ADJUST_USER);

        transaction.commit();
      } catch (InvalidRevisionCountException e) {
        logger.warn("Optimistic locking exception: ", e);
        rollback(transaction);
        throw e;
      } catch (Exception e) {
        e.printStackTrace();
        logger.error("Unexpected error: ", e);
        logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
        rollback(transaction);
        if(e instanceof ServiceException) {
          throw (ServiceException) e;
        }
        throw new ServiceException(e);
      } finally {
        closeTransaction(transaction);
      }
    }
    
    logMethodEnd(logger);
  }
  
  public static boolean containsItem(List<InventoryItem> list, InventoryItem item) {
    return ObjectUtils.listContainsReference(list, item);
  }
}
