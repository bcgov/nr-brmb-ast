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
package ca.bc.gov.srm.farm.service;

import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * This service groups together actions related to creating
 * and updating adjustments to FARM data.
 * 
 * @author awilkinson
 * @created Jan 10, 2011
 */
public interface AdjustmentService {

  String ACTION_ADD = "ADD";
  String ACTION_UPDATE = "UPDATE";
  String ACTION_DELETE = "DELETE";
  
  String AUTO_ADJUST_USER = "SYSTEM";

  /**
   * @param scenario Scenario
   * @param adjustmentMap Map<String action, List<ProductiveUnitCapacity>>
   * @param user user
   * @throws ServiceException On Exception
   */
  void adjustProductiveUnitCapacities(
      final Scenario scenario,
      final Map<String, List<ProductiveUnitCapacity>> adjustmentMap,
      final String user)
  throws ServiceException;
  
  /**
   * @param scenario Scenario
   * @param adjustmentMap Map<String action, List<IncomeExpense>>
   * @param user user
   * @throws ServiceException On Exception
   */
  void adjustIncomeExpenses(
      final Scenario scenario,
      final Map<String, List<IncomeExpense>> adjustmentMap,
      final String user)
  throws ServiceException;


  /**
   * @param refScenario Scenario
   * @param adjustmentMap Map<String action, List<InventoryItem>>
   * @param user user
   * @throws ServiceException On Exception
   */
  void adjustInventoriesAndAccruals(
      final Scenario refScenario,
      final Map<String, List<InventoryItem>> adjustmentMap,
      final String user)
  throws ServiceException;
  
  /**
   * @param scenario Scenario
   * @throws ServiceException On Exception 
   */
  void makeInventoryValuationAdjustments(
      final Scenario scenario)
  throws ServiceException;


  /**
   * @param scenario Scenario
   * @param saveAdjustments saveAdjustments
   * @throws ServiceException On Exception 
   */
  void makeInventoryValuationAdjustments(
      final Scenario scenario, final boolean saveAdjustments,
      final boolean makeFifoAdjustments)
  throws ServiceException;


  void copyInventoriesAndAccruals(
      final Scenario scenario,
      final FarmingOperation targetOperation,
      final List<InventoryItem> sourceItems, 
      final String user) throws ServiceException;


  /**
   * @param scenario Scenario
   * @param targetOperation targetOperation
   * @param sourcePucs List<ProductiveUnitCapacity>
   * @param participantDataSrcCode participantDataSrcCode
   * @param user user
   * @throws ServiceException On Exception
   */
  void copyProductiveUnitCapacities(
      final Scenario scenario,
      final FarmingOperation targetOperation,
      final List<ProductiveUnitCapacity> sourcePucs, 
      final String participantDataSrcCode,
      final String user) 
  throws ServiceException;
  
}
