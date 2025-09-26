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
package ca.bc.gov.srm.farm.util;

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.BenefitValidator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.FederalAccountingCodes;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;

/**
 * @author awilkinson
 * @created Feb 14, 2011
 */
public final class ScenarioUtils {

  /** */
  private ScenarioUtils() {
  }
  
  /**
   * @param scenario scenario
   * @return boolean
   */
  public static boolean hasUserScenario(Scenario scenario) {
    boolean result = false;
    
    for(ScenarioMetaData scMeta : scenario.getScenarioMetaDataList()) {
      Integer curYear = scenario.getYear();
      if(scMeta.getProgramYear().equals(curYear)
          && scMeta.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)) {
        result = true;
        break;
      }
    }
    
    return result;
  }
  
  
  /**
   * Check if the program year has any supplemental data at all.
   * @param scenario scenario
   * @return boolean
   */
  public static boolean hasProgramYearSupplemental(Scenario scenario) {
    return hasProgramYearSupplemental(scenario, false);
  }


  /**
   * Check if the program year has supplemental data from the CRA.
   * @param scenario scenario
   * @return boolean
   */
  public static boolean hasCraProgramYearSupplemental(Scenario scenario) {
    return hasProgramYearSupplemental(scenario, true);
  }
  
  
  /**
   * Check if the program year has any supplemental data at all.
   * @param scenario scenario
   * @param checkCRAOnly If true, adjustments don't count. Only CRA data counts.
   * @return boolean
   */
  private static boolean hasProgramYearSupplemental(Scenario scenario, boolean checkCRAOnly) {
    boolean hasSupplementalRecord = hasProgramYearSupplementalInventory(scenario, checkCRAOnly);

    if( ! hasSupplementalRecord) {
      hasSupplementalRecord = hasProgramYearSupplementalAccruals(scenario, checkCRAOnly);
    }
    if( ! hasSupplementalRecord) {
      hasSupplementalRecord = hasProgramYearSupplementalProductiveUnits(scenario, checkCRAOnly);
    }
    
    return hasSupplementalRecord;
  }
  
  
  /**
   * Check if the program year has any supplemental data at all.
   * @param scenario scenario
   * @param checkCRAOnly If true, adjustments don't count. Only CRA data counts.
   * @return boolean
   */
  private static boolean hasProgramYearSupplementalInventory(Scenario scenario, boolean checkCRAOnly) {
    boolean hasSupplementalRecord = false;
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      operationLoop: for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        
        // check whether inventory record exists
        if(fo.getInventoryItems() != null) {
          for(InventoryItem inventoryItem : fo.getInventoryItems()) {
            boolean hasValues = hasSupplemental(inventoryItem, checkCRAOnly);
            
            if(hasValues) {
              hasSupplementalRecord = true;
              break operationLoop;
            }
          }
        }
        
      }
    }
    return hasSupplementalRecord;
  }
  
  
  /**
   * Check if the program year has any supplemental data at all.
   * @param scenario scenario
   * @param checkCRAOnly If true, adjustments don't count. Only CRA data counts.
   * @return boolean
   */
  private static boolean hasProgramYearSupplementalAccruals(Scenario scenario, boolean checkCRAOnly) {
    boolean hasSupplementalRecord = false;
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      operationLoop: for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        
        // check whether accrual record exists
        if(fo.getAccrualItems() != null) {
          for(InventoryItem inventoryItem : fo.getAccrualItems()) {
            
            if(checkCRAOnly) {
              
              hasSupplementalRecord = inventoryItem.getReportedEndOfYearAmount() != null;
              if(hasSupplementalRecord) {
                break operationLoop;
              }
              
            } else {
              
              hasSupplementalRecord = inventoryItem.getTotalEndOfYearAmount() != null;
              if(hasSupplementalRecord) {
                break operationLoop;
              }
              
            }
            
          }
        }
        
      }
    }
    return hasSupplementalRecord;
  }
  
  
  /**
   * Check if the program year has any supplemental data at all.
   * @param scenario scenario
   * @param checkCRAOnly If true, adjustments don't count. Only CRA data counts.
   * @return boolean
   */
  private static boolean hasProgramYearSupplementalProductiveUnits(Scenario scenario, boolean checkCRAOnly) {
    boolean hasSupplementalRecord = false;
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      operationLoop: for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        
        if(checkCRAOnly) {
          
          for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
            
            hasSupplementalRecord = puc.getReportedAmount() != null;
            
            if(hasSupplementalRecord) {
              break operationLoop;
            }
          }
          
        } else {
          hasSupplementalRecord = !fo.getProductiveUnitCapacities().isEmpty();
        }
        
      }
    }
    return hasSupplementalRecord;
  }
  
  public static boolean hasProgramYearIncomeExpenses(Scenario scenario) {
    boolean hasSupplementalIncomeExpenses = false;
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        
        hasSupplementalIncomeExpenses = fo.getIncomeExpenses() != null && !fo.getIncomeExpenses().isEmpty();
        if(hasSupplementalIncomeExpenses) {
          break;
        }
      }
    }
    return hasSupplementalIncomeExpenses;
  }

  /**
   * @param inventoryItem inventoryItem
   * @param checkCRAOnly If true, adjustments don't count. Only CRA data counts.
   * @return true if the InventoryItem has values that count as Supplemental Data
   */
  public static boolean hasSupplemental(InventoryItem inventoryItem, boolean checkCRAOnly) {
    boolean hasValues = false;
    if(inventoryItem instanceof CropItem) {
      CropItem cropItem = (CropItem) inventoryItem;
      
      if(checkCRAOnly) {
        hasValues = cropItem.getReportedQuantityProduced() != null ||
            cropItem.getReportedQuantityEnd() != null ||
            cropItem.getReportedPriceEnd() != null ||
            cropItem.getUnseedableAcres() != null ||
            cropItem.getOnFarmAcres() != null;
      } else {
        hasValues = cropItem.getTotalQuantityProduced() != null ||
            cropItem.getTotalQuantityEnd() != null ||
            cropItem.getTotalPriceEnd() != null ||
            cropItem.getUnseedableAcres() != null ||
            cropItem.getOnFarmAcres() != null;
      }

    } else if(inventoryItem instanceof LivestockItem) {
      LivestockItem livestockItem = (LivestockItem) inventoryItem;
      
      if(checkCRAOnly) {
        hasValues = livestockItem.getReportedQuantityEnd() != null ||
            livestockItem.getReportedPriceEnd() != null;
      } else {
        hasValues = livestockItem.getTotalQuantityEnd() != null ||
            livestockItem.getTotalPriceEnd() != null;
      }
      
    } else if(inventoryItem.isAccrual()) {
      
      if(checkCRAOnly) {
        hasValues = inventoryItem.getReportedEndOfYearAmount() != null;
      } else {
        hasValues = inventoryItem.getTotalEndOfYearAmount() != null;
      }
      
    }
    return hasValues;
  }
  
  
  /**
   * @param scenario scenario
   * @return boolean
   */
  public static boolean hasAccrualAccountingOperations(Scenario scenario) {
    boolean hasAccrualAccountingOperations = false;
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        if(FederalAccountingCodes.ACCRUAL.equals(fo.getAccountingCode())) {
          hasAccrualAccountingOperations = true;
          break;
        }
      }
    }
    return hasAccrualAccountingOperations;
  }
  
  
  /**
   * @param scenario scenario
   * @return boolean
   */
  public static boolean isInPartnership(Scenario scenario) {
    boolean inPartnership = false;
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        
        if(fo.getPartnershipPin() != null && fo.getPartnershipPin() != 0) {
          inPartnership = true;
          break;
        }
        
        if(StringUtils.isNotBlank(fo.getPartnershipName())) {
          inPartnership = true;
          break;
        }
        
        if(fo.getPartnershipPercent() != null && fo.getPartnershipPercent() != 1.0) {
          inPartnership = true;
          break;
        }
        
        List<FarmingOperationPartner> partners = fo.getFarmingOperationPartners();
        if(partners != null && partners.size() >= 1) {
          inPartnership = true;
          break;
        }
      }
    }
    return inPartnership;
  }
  
  
  public static ScenarioMetaData getLatestBaseScenario(Scenario scenario, Integer year) {
    ScenarioMetaData result = null;
    int maxPyv = 0;
    
    for(ScenarioMetaData scMeta : scenario.getScenarioMetaDataList()) {
      if(scMeta.getProgramYear().equals(year)
          && (scMeta.getScenarioTypeCode().equals(ScenarioTypeCodes.CRA)
              || scMeta.getScenarioTypeCode().equals(ScenarioTypeCodes.GEN)
              || scMeta.getScenarioTypeCode().equals(ScenarioTypeCodes.LOCAL)
              || scMeta.getScenarioTypeCode().equals(ScenarioTypeCodes.CHEF))) {

        int curPyv = scMeta.getProgramYearVersion().intValue();
        
        if(result == null) {
          result = scMeta;
          maxPyv = scMeta.getProgramYearVersion().intValue();
        } else if(curPyv > maxPyv) {
          result = scMeta;
          maxPyv = scMeta.getProgramYearVersion().intValue();
        }
      }
    }
    
    return result;
  }

  
  /**
   * @param scenario scenario
   * @return boolean
   */
  public static boolean isBpuSetComplete(Scenario scenario) {
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    boolean bpuSetComplete;
    if(scenario.getFarmingYear() == null
        || scenario.getFarmingYear().getFarmingOperations() == null) {
      bpuSetComplete = false;
    } else {
      BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
      nullFixer.fixNulls(scenario);
      bpuSetComplete = validator.validateBpus(scenario);
    }
    return bpuSetComplete;
  }


  /**
   * @param scenario Scenario
   * @return boolean
   */
  public static boolean isFmvSetComplete(Scenario scenario) {
    Map<Integer, Set<String>> missingFmvCodes = getAllInventoryWithMissingFMVs(scenario);
    return missingFmvCodes.isEmpty();
  }
  
  
  /**
   * @param scenario Scenario
   * @return Set<inventoryItemCode>
   */
  public static Map<Integer, Set<String>> getAllInventoryWithMissingFMVs(Scenario scenario) {
    Map<Integer, Set<String>> result = new TreeMap<>(Collections.reverseOrder());
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      Set<String> rsInvCodes = getScenarioInventoryWithMissingFMVs(refScenario);
      if(!rsInvCodes.isEmpty()) {
        result.put(refScenario.getYear(), rsInvCodes);
      }
    }
    return result;
  }

  
  /**
   * @param refScenario ReferenceScenario
   * @return Set<inventoryItemCode>
   */
  public static Set<String> getScenarioInventoryWithMissingFMVs(ReferenceScenario refScenario) {
    Set<String> invCodes = new TreeSet<>();
    if(refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        Set<String> foInvCodes = getOperationInventoryWithMissingFMVs(fo);
        invCodes.addAll(foInvCodes);
      }
    }
    return invCodes;
  }
  

  /**
   * @param fo FarmingOperation
   * @return Set<inventoryItemCode>
   */
  public static Set<String> getOperationInventoryWithMissingFMVs(FarmingOperation fo) {
    Set<String> invCodes = new TreeSet<>();
    if(fo.getProducedItems() != null) {
      for(ProducedItem pi : fo.getProducedItems()) {

        CropItem cropItem = null;
        if(pi.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
          cropItem = (CropItem) pi;
        }
        
        boolean hasNonZeroValues =
          (cropItem != null && cropItem.getTotalQuantityProduced() != null
              && cropItem.getTotalQuantityProduced().doubleValue() != 0)
          || (pi.getTotalQuantityStart() != null && pi.getTotalQuantityStart().doubleValue() != 0)
          || (pi.getTotalPriceStart() != null && pi.getTotalPriceStart().doubleValue() != 0)
          || (pi.getTotalQuantityEnd() != null && pi.getTotalQuantityEnd().doubleValue() != 0)
          || (pi.getTotalPriceEnd() != null && pi.getTotalPriceEnd().doubleValue() != 0);
        
        if(hasNonZeroValues) {
          if(pi.getFmvStart() == null
              || pi.getFmvEnd() == null
              || pi.getFmvVariance() == null
              || pi.getFmvAverage() == null) {
            invCodes.add(pi.getInventoryItemCode());
          }
        }
      }
    }

    return invCodes;
  }
  
  
  /**
   * @param refScenario ReferenceScenario
   * @return Set<inventoryItemCode>
   */
  public static Set<String> getNonMarketCommoditiesWithMismatchedPrices(ReferenceScenario refScenario) {
    Set<String> invCodes = new TreeSet<>();
    if(refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        Set<String> foInvCodes = getNonMarketCommoditiesWithMismatchedPrices(fo);
        invCodes.addAll(foInvCodes);
      }
    }
    return invCodes;
  }
  
  
  /**
   * @param fo FarmingOperation
   * @return Set<inventoryItemCode>
   */
  public static Set<String> getNonMarketCommoditiesWithMismatchedPrices(FarmingOperation fo) {
    Set<String> invCodes = new TreeSet<>();
    if(fo.getLivestockItems() != null) {
      for(LivestockItem li : fo.getLivestockItems()) {
        
        if(!li.getIsMarketCommodity().booleanValue()) {
        
          boolean pricesDiffer =
              ! MathUtils.equalToTwoDecimalPlaces(li.getTotalPriceStart(), li.getTotalPriceEnd());
          
          if(pricesDiffer) {
            invCodes.add(li.getInventoryItemCode());
          }
        }
        
      }
    }
    
    return invCodes;
  }
  

  /**
   * @param refScenario ReferenceScenario
   * @return Boolean
   */
  public static Boolean hasNewBaseDataArrived(ReferenceScenario refScenario) {
    Boolean newBaseDataArrived = Boolean.FALSE;
    if(refScenario.getFarmingYear() != null
        && refScenario.getFarmingYear().getProgramYearVersionId() != null) {
      Integer year = refScenario.getYear();
      int pyv = refScenario.getFarmingYear().getProgramYearVersionNumber().intValue();
      for(ScenarioMetaData meta : refScenario.getParentScenario().getScenarioMetaDataList()) {
        if(year.equals(meta.getProgramYear())
            && meta.getProgramYearVersion().intValue() > pyv) {
          newBaseDataArrived = Boolean.TRUE;
          break;
        }
      }
    }
    return newBaseDataArrived;
  }
  
  public static double applyPartnershipPercent(InventoryItem item, Double value) {
    double result = 0;
    if(value != null) {
      result = value;
      double partnershipPercent = getPartnershipPercent(item.getFarmingOperation());
      result *= partnershipPercent;
    }
    return result;
  }

  public static double getPartnershipPercent(FarmingOperation fo) {
    Scenario scenario = fo.getFarmingYear().getReferenceScenario().getParentScenario();

    // Use partnership percent from program year operation.
    // If no program year operation exists for this schedule, then use 100%.
    double partnershipPercent = 1;
    if(scenario.getFarmingYear() != null) {
      FarmingOperation programYearFo =
          scenario.getFarmingYear().getFarmingOperationBySchedule(fo.getSchedule());
      if(programYearFo != null) {
        partnershipPercent = programYearFo.getPartnershipPercent().doubleValue();
      }
    }
    
    return partnershipPercent;
  }


  /**
   * @param scenario scenario
   * @return String
   */
  public static String getScenarioInfoForLog(Scenario scenario) {
    StringBuffer sb = new StringBuffer();
    
    sb.append("Error occurred for User: ");
    if(CurrentUser.getUser() != null) {
      sb.append(CurrentUser.getUser().getAccountName());
    } else {
      sb.append("null");
    }

    if(scenario == null) {
      sb.append(", Scenario is null");
    } else {
      
      if(scenario.getClient() == null) {
        sb.append(", Client is null");
      } else {
        sb.append(", PIN: ");
        sb.append(scenario.getClient().getParticipantPin());
      }
      sb.append(", Program Year: ");
      sb.append(scenario.getYear());
      sb.append(", Scenario Number: ");
      sb.append(scenario.getScenarioNumber());
    }
    
    return sb.toString();
  }
  

  /**
   * @param programYear Integer
   * @return List<Integer>
   */
  public static List<Integer> getAllYears(Integer programYear) {
    final int numYears = 6;
    List<Integer> years = new ArrayList<>(numYears);
    int year = programYear.intValue() - numYears + 1;
    for(int ii = 0; ii < numYears; ii++) {
      years.add(new Integer(year++));
    }
    return years;
  }
  
  /**
   * @param programYear Integer
   * @return List<Integer>
   */
  public static List<Integer> getReferenceYears(Integer programYear) {
    final int numYears = 5;
    List<Integer> years = new ArrayList<>(numYears);
    int year = programYear.intValue() - numYears;
    for(int ii = 0; ii < numYears; ii++) {
      years.add(new Integer(year++));
    }
    return years;
  }
  
  /**
   * Only some scenario categories represent a real benefit calculation
   * that will result in a benefit being paid.
   * @param scenarioCategoryCode
   * @return true if the category represents a real benefit to be paid.
   */
  public static boolean categoryIsRealBenefit(String scenarioCategoryCode) {
    return PRE_VERIFICATION.equals(scenarioCategoryCode)
        || INTERIM.equals(scenarioCategoryCode)
        || FINAL.equals(scenarioCategoryCode)
        || ADMINISTRATIVE_ADJUSTMENT.equals(scenarioCategoryCode)
        || PRODUCER_ADJUSTMENT.equals(scenarioCategoryCode);
  }
  
  public static List<ProductiveUnitCapacity> getFarmTypeDetailedProductiveCapacities(Map<String, ProductiveUnitCapacity> pucMap) {
    return pucMap.values().stream()
        .filter(puc -> puc.getTotalProductiveCapacityAmount() > 0)
        .sorted((puc1, puc2) -> puc2.getTotalProductiveCapacityAmount().compareTo(puc1.getTotalProductiveCapacityAmount()))
        .limit(5)
        .collect(Collectors.toList());
  }

  public static List<ProductiveUnitCapacity> getFarmTypeDetailedProductiveCapacities(Scenario scenario) {
    Map<String, ProductiveUnitCapacity> pucMap = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario);
    return getFarmTypeDetailedProductiveCapacities(pucMap).stream()
        .collect(Collectors.toList());
  }

  public static List<String> getFarmTypeDetailedCodes(Scenario scenario) {
    return getFarmTypeDetailedProductiveCapacities(scenario).stream()
        .map(ProductiveUnitCapacity::getCode)
        .collect(Collectors.toList());
  }

  public static List<String> getFarmTypeDetailedDescriptions(Scenario scenario) {
    return getFarmTypeDetailedProductiveCapacities(scenario).stream()
        .map(ProductiveUnitCapacity::getDescription)
        .collect(Collectors.toList());
  }

  public static Map<String, ProductiveUnitCapacity> getConsolidatedProductiveUnitsMap(Scenario scenario) {
    return getConsolidatedProductiveUnitsMap(scenario, null, false);
  }

  public static Map<String, ProductiveUnitCapacity> getConsolidatedProductiveUnitsMap(Scenario scenario, boolean isForStructureChange) {
    return getConsolidatedProductiveUnitsMap(scenario, null, isForStructureChange);
  }
  
  public static Map<String, ProductiveUnitCapacity> getConsolidatedProductiveUnitsMap(Scenario scenario, String commodityTypeCode, boolean isForStructureChange) {
    return getConsolidatedProductiveUnitsMap(scenario, commodityTypeCode, isForStructureChange, true);
  }
  
  public static Map<String, ProductiveUnitCapacity> getConsolidatedProductiveUnitsMap(Scenario scenario, String commodityTypeCode, boolean isForStructureChange, boolean applyPartnershipPercent) {
    Map<String, ProductiveUnitCapacity> pucMap = new HashMap<>();
    for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
      
      if(programYearScenario.getFarmingYear() != null
          && programYearScenario.getFarmingYear().getFarmingOperations() != null) {
        
        for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
          double partnershipPercent = getPartnershipPercent(fo);
          
          List<ProductiveUnitCapacity> pucList = isForStructureChange ? fo.getProductiveUnitCapacitiesForStructureChange() : fo.getProductiveUnitCapacities();
          for(ProductiveUnitCapacity curPuc : pucList) {
            
            String code = curPuc.getCode();
            
            boolean commodityTypeMatch =
                commodityTypeCode == null || commodityTypeCode.equals(curPuc.getCommodityTypeCode());
            
            if (!commodityTypeMatch) {
              continue;
            }
            
            ProductiveUnitCapacity consolidatedPuc = pucMap.get(code);
            if(consolidatedPuc == null) {
              consolidatedPuc = new ProductiveUnitCapacity();
              pucMap.put(code, consolidatedPuc);
              consolidatedPuc.setInventoryItemCode(curPuc.getInventoryItemCode());
              consolidatedPuc.setInventoryItemCodeDescription(curPuc.getInventoryItemCodeDescription());
              consolidatedPuc.setStructureGroupCode(curPuc.getStructureGroupCode());
              consolidatedPuc.setStructureGroupCodeDescription(curPuc.getStructureGroupCodeDescription());
              consolidatedPuc.setAdjAmount(0d);
              consolidatedPuc.setReportedAmount(0d);
              consolidatedPuc.setBasePricePerUnit(curPuc.getBasePricePerUnit());
              consolidatedPuc.setCommodityTypeCode(curPuc.getCommodityTypeCode());
              consolidatedPuc.setCommodityTypeCodeDescription(curPuc.getCommodityTypeCodeDescription());
              consolidatedPuc.setFruitVegTypeCode(curPuc.getFruitVegTypeCode());
              consolidatedPuc.setFruitVegTypeCodeDescription(curPuc.getFruitVegTypeCodeDescription());
              consolidatedPuc.setExpectedProductionPerProductiveUnit(curPuc.getExpectedProductionPerProductiveUnit());
              consolidatedPuc.setExpectedProductionCropUnitCode(curPuc.getExpectedProductionCropUnitCode());
              consolidatedPuc.setOnFarmAcres(0d);
              consolidatedPuc.setUnseedableAcres(0d);
            }
            
            double adjUnits = MathUtils.getPrimitiveValue(curPuc.getAdjAmount());
            double reportedUnits = MathUtils.getPrimitiveValue(curPuc.getReportedAmount());
            double onFarmAcres = MathUtils.getPrimitiveValue(curPuc.getOnFarmAcres());
            double unseedableAcres = MathUtils.getPrimitiveValue(curPuc.getUnseedableAcres());
            
            if(applyPartnershipPercent) {
              adjUnits *= partnershipPercent;
              reportedUnits *= partnershipPercent;
              onFarmAcres *= partnershipPercent;
              unseedableAcres *= partnershipPercent;
            }
            
            adjUnits = MathUtils.roundProduction(adjUnits);
            reportedUnits = MathUtils.roundProduction(reportedUnits);
            onFarmAcres = MathUtils.roundProduction(onFarmAcres);
            onFarmAcres = MathUtils.roundProduction(onFarmAcres);
            
            consolidatedPuc.setAdjAmount(consolidatedPuc.getAdjAmount() + adjUnits);
            consolidatedPuc.setReportedAmount(consolidatedPuc.getReportedAmount() + reportedUnits);
            consolidatedPuc.setOnFarmAcres(consolidatedPuc.getOnFarmAcres() + onFarmAcres);
            consolidatedPuc.setUnseedableAcres(consolidatedPuc.getUnseedableAcres() + unseedableAcres);
          }
        }
      }
    }
    
    if( ! pucMap.isEmpty() ) {
      for(Iterator<Map.Entry<String, ProductiveUnitCapacity>> iterator = pucMap.entrySet().iterator(); iterator.hasNext(); ) {
        Map.Entry<String, ProductiveUnitCapacity> item = iterator.next();
        if(item.getValue().getTotalProductiveCapacityAmount() == 0) {
          iterator.remove();
        }
      }
    }
    
    return pucMap;
  }
  
  public static Map<String, InventoryItem> getConsolidatedAccruals(Scenario scenario, String inventoryClassCode, String commodityTypeCode) {
    return getConsolidatedAccruals(scenario, inventoryClassCode, commodityTypeCode, true);
  }
  
  public static Map<String, InventoryItem> getConsolidatedAccruals(Scenario scenario, String inventoryClassCode, String commodityTypeCode, boolean excludeZeroChangeInValue) {
    Map<String, InventoryItem> accrualsMap = new HashMap<>();
    
    for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        if (fo.getReceivableItems() == null) {
          continue;
        }
        double partnershipPercent = getPartnershipPercent(fo);
        List<InventoryItem> accrualItems = fo.getAccrualItems();
        
        for (InventoryItem accrualItem : accrualItems) {
          
          boolean inventoryClassCodeMatch = accrualItem.getInventoryClassCode().equals(inventoryClassCode);
          boolean commodityTypeMatch =
              commodityTypeCode == null || commodityTypeCode.equals(accrualItem.getCommodityTypeCode());
          
          if (!inventoryClassCodeMatch || !commodityTypeMatch) {
            continue;
          }

          double repStartAmnt = MathUtils.getPrimitiveValue(accrualItem.getReportedStartOfYearAmount());
          double adjStartAmnt = MathUtils.getPrimitiveValue(accrualItem.getAdjStartOfYearAmount());
          double repEndAmnt = MathUtils.getPrimitiveValue(accrualItem.getReportedEndOfYearAmount());
          double adjEndAmnt = MathUtils.getPrimitiveValue(accrualItem.getAdjEndOfYearAmount());
          
          repStartAmnt *= partnershipPercent;
          adjStartAmnt *= partnershipPercent;
          repEndAmnt *= partnershipPercent;
          adjEndAmnt *= partnershipPercent;
          
          repStartAmnt = MathUtils.roundCurrency(repStartAmnt);
          adjStartAmnt = MathUtils.roundCurrency(adjStartAmnt);
          repEndAmnt = MathUtils.roundCurrency(repEndAmnt);
          adjEndAmnt = MathUtils.roundCurrency(adjEndAmnt);
          
          InventoryItem invItem = accrualsMap.get(accrualItem.getInventoryItemCode());
          if (invItem == null) {
            if (accrualItem instanceof InputItem) {
              invItem = new InputItem();
            } else if (accrualItem instanceof PayableItem) {
              invItem = new PayableItem();
            } else if (accrualItem instanceof ReceivableItem) {
              invItem = new ReceivableItem();
            } else {
              throw new UnsupportedOperationException("Unknown accrual class: " + accrualItem.getClass().getCanonicalName());
            }
            accrualsMap.put(accrualItem.getInventoryItemCode(), invItem);
            
            invItem.setInventoryClassCode(inventoryClassCode);
            invItem.setInventoryItemCode(accrualItem.getInventoryItemCode());
            invItem.setInventoryItemCodeDescription(accrualItem.getInventoryItemCodeDescription());
            invItem.setReportedStartOfYearAmount(0.0);
            invItem.setAdjStartOfYearAmount(0.0);
            invItem.setReportedEndOfYearAmount(0.0);
            invItem.setAdjEndOfYearAmount(0.0);
            invItem.setFruitVegTypeCode(accrualItem.getFruitVegTypeCode());
            invItem.setFruitVegTypeCodeDescription(accrualItem.getFruitVegTypeCodeDescription());
            invItem.setCommodityTypeCode(accrualItem.getCommodityTypeCode());
            invItem.setCommodityTypeCodeDescription(accrualItem.getCommodityTypeCodeDescription());
          }
          
          repStartAmnt += invItem.getReportedStartOfYearAmount();
          adjStartAmnt += invItem.getAdjStartOfYearAmount();
          repEndAmnt += invItem.getReportedEndOfYearAmount();
          adjEndAmnt += invItem.getAdjEndOfYearAmount();
          
          invItem.setReportedStartOfYearAmount(repStartAmnt);
          invItem.setAdjStartOfYearAmount(adjStartAmnt);
          invItem.setReportedEndOfYearAmount(repEndAmnt);
          invItem.setAdjEndOfYearAmount(adjEndAmnt);
        }
      }
    }
    
    if(excludeZeroChangeInValue) {
      InventoryCalculator inventoryCalc = CalculatorFactory.getInventoryCalculator();
      
      List<String> inventoryItemCodes = new ArrayList<>();
      inventoryItemCodes.addAll(accrualsMap.keySet());
      
      for(String inventoryItemCode : inventoryItemCodes) {
        InventoryItem accrual = accrualsMap.get(inventoryItemCode);
        double accrualAmount = inventoryCalc.calculateChangeInValue(accrual);
        accrualAmount = MathUtils.roundCurrency(accrualAmount);
        if(accrualAmount == 0) {
          accrualsMap.remove(inventoryItemCode);
        }
      }
    }
    
    return accrualsMap;
  }
  
  public static Map<Integer, IncomeExpense> getConsolidatedIncomeExpense(Scenario scenario, boolean getExpenses) {
    return getConsolidatedIncomeExpense(scenario, getExpenses, null);
  }
  
  /**
   * @param getExpenses If true, get expenses, else get incomes.
   * @param commodityTypeCode Optional. If specified only return those with matching type.
   */
  public static Map<Integer, IncomeExpense> getConsolidatedIncomeExpense(Scenario scenario, boolean getExpenses, String commodityTypeCode) {
    Map<Integer, IncomeExpense> incMap = new HashMap<>();
    for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        if (fo.getIncomeExpenses() == null) {
          continue;
        }
        double partnershipPercent = getPartnershipPercent(fo);
        
        for (IncomeExpense incomeExpense : fo.getIncomeExpenses()) {
          LineItem lineItem = incomeExpense.getLineItem();
          
          boolean expenseFilterMatch =
              getExpenses == incomeExpense.getIsExpense();
          boolean commodityTypeMatch =
              commodityTypeCode == null || commodityTypeCode.equals(lineItem.getCommodityTypeCode());
          
          if (!expenseFilterMatch || !commodityTypeMatch) {
            continue;
          }
          
          double reportedAmount = MathUtils.getPrimitiveValue(incomeExpense.getReportedAmount());
          double adjustedAmount = MathUtils.getPrimitiveValue(incomeExpense.getAdjAmount());
          
          reportedAmount *= partnershipPercent;
          adjustedAmount *= partnershipPercent;
          
          IncomeExpense incExpense = incMap.get(lineItem.getLineItem());
          if (incExpense == null) {
            incExpense = new IncomeExpense();
            incExpense.setLineItem(lineItem);
            incExpense.setReportedAmount(0.0);
            incExpense.setAdjAmount(0.0);
            incExpense.setIsExpense(getExpenses);
            incMap.put(incExpense.getLineItem().getLineItem(), incExpense);
          }
          
          reportedAmount += incExpense.getReportedAmount();
          adjustedAmount += incExpense.getAdjAmount();
          
          incExpense.setReportedAmount(reportedAmount);
          incExpense.setAdjAmount(adjustedAmount);
        }
      }
    }
    
    List<Integer> lineItems = new ArrayList<>();
    lineItems.addAll(incMap.keySet());
    for(Integer lineItem : lineItems) {
      IncomeExpense incExpense = incMap.get(lineItem);
      if(incExpense.getTotalAmount() == 0) {
        incMap.remove(lineItem);
      }
    }
    
    return incMap;
  }
  
  
  public static Map<String, List<CropItem>> getCropInventoryMap(Scenario scenario, String commodityTypeCode, boolean includeItemsWithZeroQuantities) {
    Map<String, List<CropItem>> itemListMap = new HashMap<>();
    for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        
        if(fo.getCropItems() != null) {
          for(CropItem item : fo.getCropItems()) {
            
            // Either commodityTypeCode is not specified or it matches
            boolean commodityTypeMatch =
                commodityTypeCode == null || commodityTypeCode.equals(item.getCommodityTypeCode());
            boolean passQuantityCheck = true;
            if(!includeItemsWithZeroQuantities) {
              if(CommodityTypeCodes.FRUIT_VEG.equals(item.getCommodityTypeCode())) {
                passQuantityCheck = CropItemUtils.hasNonZeroQuantityProduced(item);
              } else {
                passQuantityCheck = CropItemUtils.hasNonZeroQuantities(item);
              }
            }
            
            if(commodityTypeMatch && passQuantityCheck) {
              String inventoryItemCode = item.getInventoryItemCode();
              List<CropItem> itemsForThisCode = itemListMap.get(inventoryItemCode);
              if(itemsForThisCode == null) {
                itemsForThisCode = new ArrayList<>();
                itemListMap.put(inventoryItemCode, itemsForThisCode);
              }
              itemsForThisCode.add(item);
            }
          }
        }
      }
    }
    
    return itemListMap;
  }
  
  
  public static Map<String, List<LivestockItem>> getLivestockInventoryMap(Scenario scenario, String commodityTypeCode, boolean includeItemsWithZeroQuantities) {
    Map<String, List<LivestockItem>> itemListMap = new HashMap<>();
    for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        
        if(fo.getLivestockItems() != null) {
          for(LivestockItem item : fo.getLivestockItems()) {
            
            // Either commodityTypeCode is not specified or it matches
            boolean commodityTypeMatch =
                commodityTypeCode == null || commodityTypeCode.equals(item.getCommodityTypeCode());
            boolean passQuantityCheck =
                includeItemsWithZeroQuantities || LivestockItemUtils.hasNonZeroQuantities(item);
            
            if(commodityTypeMatch && passQuantityCheck) {
              String inventoryItemCode = item.getInventoryItemCode();
              List<LivestockItem> itemsForThisCode = itemListMap.get(inventoryItemCode);
              if(itemsForThisCode == null) {
                itemsForThisCode = new ArrayList<>();
                itemListMap.put(inventoryItemCode, itemsForThisCode);
              }
              itemsForThisCode.add(item);
            }
          }
        }
      }
    }
    
    return itemListMap;
  }


  public static Map<Integer, IncomeExpense> getIncomesWithReceivables(Scenario scenario) {
    return getIncomesWithReceivables(scenario, null);
  }
  
  public static Map<Integer, IncomeExpense> getIncomesWithReceivables(Scenario scenario, String commodityTypeCode) {
    Map<Integer, IncomeExpense> reportedIncomes = getConsolidatedIncomeExpense(scenario, false, commodityTypeCode);
    InventoryCalculator inventoryCalc = CalculatorFactory.getInventoryCalculator();
    Map<String, InventoryItem> receivableAccruals = getConsolidatedAccruals(scenario, InventoryClassCodes.RECEIVABLE, commodityTypeCode);
    
    for(Integer lineItem : reportedIncomes.keySet()) {
      
      InventoryItem receivable = receivableAccruals.get(lineItem.toString());
      if(receivable != null) {
        double receivableChangeInValue = inventoryCalc.calculateChangeInValue(receivable);
        IncomeExpense income = reportedIncomes.get(lineItem);
        Double adjAmount = income.getAdjAmount();
        if(adjAmount == null) {
          adjAmount = 0d;
        }
        income.setAdjAmount(adjAmount + receivableChangeInValue);
      }
    }
    
    for(String inventoryItemCode : receivableAccruals.keySet()) {
      InventoryItem receivable = receivableAccruals.get(inventoryItemCode);
      
      Integer lineItem = Integer.valueOf(inventoryItemCode);
      if(!reportedIncomes.containsKey(lineItem)) {
        IncomeExpense income = new IncomeExpense();
        reportedIncomes.put(lineItem, income);
        LineItem lineItemObj = new LineItem();
        income.setLineItem(lineItemObj);
        lineItemObj.setLineItem(lineItem);
        lineItemObj.setDescription(receivable.getInventoryItemCodeDescription());
        lineItemObj.setCommodityTypeCode(receivable.getCommodityTypeCode());
        lineItemObj.setCommodityTypeCodeDescription(receivable.getCommodityTypeCodeDescription());
        lineItemObj.setFruitVegTypeCode(receivable.getFruitVegTypeCode());
        lineItemObj.setFruitVegTypeCodeDescription(receivable.getFruitVegTypeCodeDescription());
        
        double receivableChangeInValue = inventoryCalc.calculateChangeInValue(receivable);
        income.setAdjAmount(receivableChangeInValue);
      }
    }
    
    return reportedIncomes;
  }
  
  
  /**
   * Check if the program year has any productive units with the fruit/veg type APPLE.
   */
  public static boolean hasAppleInventory(Scenario scenario) {
    boolean hasApples = false;
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      operationLoop: for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        
        for(CropItem cropItem : fo.getCropItems()) {
          
          if(FruitVegTypeCodes.APPLE.equals(cropItem.getFruitVegTypeCode())) {
            hasApples = (cropItem.getTotalQuantityProduced() != null && cropItem.getTotalQuantityProduced() > 0)
                || (cropItem.getTotalQuantityStart() != null && cropItem.getTotalQuantityStart() > 0)
                || (cropItem.getTotalQuantityEnd() != null && cropItem.getTotalQuantityEnd() > 0);
            
            if(hasApples) {
              break operationLoop;
            }
          }
        }
      }
    }
    return hasApples;
  }
  
  /**
   * Check if the program year has any productive units with the passed in structure group code.
   */
  public static boolean hasProductiveUnitsWithStructureGroupCode(Scenario scenario, String strucGroupCode) {
    boolean hasCommodTypeInvent = false;
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      operationLoop: for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        for (ProductiveUnitCapacity pu : fo.getProductiveUnitCapacities()) {
          if (pu.getStructureGroupCode() != null && pu.getStructureGroupCode().equals(strucGroupCode)) {
            hasCommodTypeInvent = true;
            break operationLoop;
          }
        }
      }
    }
    return hasCommodTypeInvent;
  }
  
  
  /**
   * Check if the program year has any inventory, income, expense, or productive units of commodity type.
   */
  public static boolean hasCommdityType(Scenario scenario, String commodityTypeCode) {
    boolean result = false;
    
    scenarioLoop: for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      if(programYearScenario.getFarmingYear() != null && programYearScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
          
          result = hasCropOfCommodityType(fo, commodityTypeCode);
          if(result) {
            break scenarioLoop;
          }
          
          result = hasLivestockOfCommodityType(fo, commodityTypeCode);
          if(result) {
            break scenarioLoop;
          }
          
          result = hasIncomeExpenseOfCommodityType(fo, commodityTypeCode);
          if(result) {
            break scenarioLoop;
          }
          
          result = hasAccrualOfCommodityType(fo, commodityTypeCode, InventoryClassCodes.RECEIVABLE);
          if(result) {
            break scenarioLoop;
          }
          
          result = hasProductiveUnitOfCommodityType(fo, commodityTypeCode);
          if(result) {
            break scenarioLoop;
          }
        }
      }
    }
    return result;
  }
  
  private static boolean hasCropOfCommodityType(FarmingOperation fo, String commodityTypeCode) {
    boolean result = false;
    for(CropItem cropItem : fo.getCropItems()) {
      
      if(commodityTypeCode.equals(cropItem.getCommodityTypeCode())) {
        result = CropItemUtils.hasNonZeroQuantities(cropItem);
        if(result) {
          break;
        }
      }
    }
    return result;
  }

  private static boolean hasLivestockOfCommodityType(FarmingOperation fo, String commodityTypeCode) {
    boolean result = false;
    for(LivestockItem livestockItem : fo.getLivestockItems()) {
      
      if(commodityTypeCode.equals(livestockItem.getCommodityTypeCode())) {
        result = LivestockItemUtils.hasNonZeroQuantities(livestockItem);
        if(result) {
          break;
        }
      }
    }
    return result;
  }

  private static boolean hasIncomeExpenseOfCommodityType(FarmingOperation fo, String commodityTypeCode) {
    boolean result = false;
    if(fo.getIncomeExpenses() != null) {
      for(IncomeExpense incomeExpense : fo.getIncomeExpenses()) {
        
        if(commodityTypeCode.equals(incomeExpense.getLineItem().getCommodityTypeCode())) {
          result = incomeExpense.getTotalAmount() > 0;
          if(result) {
            break;
          }
        }
      }
    }
    return result;
  }

  private static boolean hasAccrualOfCommodityType(FarmingOperation fo, String commodityTypeCode, String inventoryClassCode) {
    boolean result = false;
    InventoryCalculator inventoryCalc = CalculatorFactory.getInventoryCalculator();
    for(InventoryItem item : fo.getAccrualItems()) {
      
      if(inventoryClassCode.equals(item.getInventoryClassCode()) && commodityTypeCode.equals(item.getCommodityTypeCode())) {
        double accrualAmount = inventoryCalc.calculateChangeInValue(item);
        result = accrualAmount > 0;
        if(result) {
          break;
        }
      }
    }
    return result;
  }

  private static boolean hasProductiveUnitOfCommodityType(FarmingOperation fo, String commodityTypeCode) {
    boolean result = false;
    for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
      
      if(commodityTypeCode.equals(puc.getCommodityTypeCode())) {
        result = puc.getTotalProductiveCapacityAmount() > 0;
        if(result) {
          break;
        }
      }
    }
    return result;
  }
  
  
  /**
   * Check if any year has any crop inventory, or productive units of commodity type.
   */
  public static boolean hasMultistageCommodity(Scenario scenario) {
    boolean result = false;
    
    scenarioLoop: for (ReferenceScenario refScenario : scenario.getAllScenarios()) {
      if(refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
          
          result = hasMultistageCommodityCropInventory(fo);
          if(result) {
            break scenarioLoop;
          }
          
          result = hasMultistageCommodityProductiveUnit(fo);
          if(result) {
            break scenarioLoop;
          }
        }
      }
    }
    return result;
  }
  
  
  private static boolean hasMultistageCommodityCropInventory(FarmingOperation fo) {
    boolean result = false;
    for(CropItem cropItem : fo.getCropItems()) {
      
      boolean isGenericMultistageCommodityCode = InventoryItemCodes.isGenericMultistageCommodityCode(cropItem.getInventoryItemCode());
      if(isGenericMultistageCommodityCode || cropItem.getMultiStageCommodityCode() != null) {
        result = CropItemUtils.hasNonZeroQuantities(cropItem);
        if(result) {
          break;
        }
      }
    }
    return result;
  }

  
  private static boolean hasMultistageCommodityProductiveUnit(FarmingOperation fo) {
    boolean result = false;
    for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
      
      boolean isGenericMultistageCommodityCode = InventoryItemCodes.isGenericMultistageCommodityCode(puc.getInventoryItemCode());
      if(isGenericMultistageCommodityCode || puc.getMultiStageCommodityCode() != null) {
        result = puc.getTotalProductiveCapacityAmount() > 0;
        if(result) {
          break;
        }
      }
    }
    return result;
  }


  public static boolean programYearHasVerifiedFinal(Scenario scenario) {
    List<ScenarioMetaData> scenarioMetaDataList = scenario.getScenarioMetaDataList();
    Integer year = scenario.getYear();
    return programYearHasVerifiedFinal(scenarioMetaDataList, year);
  }

  public static boolean programYearHasVerifiedFinal(List<ScenarioMetaData> scenarioMetaDataList, Integer year) {
    boolean verifiedFinalExists = false;
    for(ScenarioMetaData curScenarioMetaData : scenarioMetaDataList) {
      
      boolean isProgramYear = curScenarioMetaData.getProgramYear().equals(year);
      boolean isUserScenario = curScenarioMetaData.getScenarioTypeCode().equals(ScenarioTypeCodes.USER);
      boolean isFinal = curScenarioMetaData.getScenarioCategoryCode().equals(FINAL);
      boolean isVerified = curScenarioMetaData.stateIsOneOf(VERIFIED, AMENDED);
      
      if(isProgramYear && isUserScenario && isVerified && isFinal) {
        verifiedFinalExists = true;
        break;
      }
    }
    return verifiedFinalExists;
  }
  
  
  public static boolean programYearHasVerifiedInterim(Scenario scenario) {
    boolean preVerifiedExists = false;
    List<ScenarioMetaData> scenarios = scenario.getScenarioMetaDataList();
    for(ScenarioMetaData curScenario : scenarios) {
      
      boolean isProgramYear = curScenario.getProgramYear().equals(scenario.getYear());
      boolean isUserScenario = curScenario.getScenarioTypeCode().equals(ScenarioTypeCodes.USER);
      boolean isInterim = curScenario.getScenarioCategoryCode().equals(INTERIM);
      boolean isVerified = curScenario.stateIsOneOf(VERIFIED, AMENDED);
      
      if(isProgramYear && isUserScenario && isVerified && isInterim) {
        preVerifiedExists = true;
        break;
      }
    }
    return preVerifiedExists;
  }


  public static boolean programYearHasInProgressRealBenefit(Scenario scenario) {
    boolean inProgressRealBenefitExists = false;
    List<ScenarioMetaData> scenarios = scenario.getScenarioMetaDataList();
    for(ScenarioMetaData curScenario : scenarios) {
      
      boolean isProgramYear = curScenario.getProgramYear().equals(scenario.getYear());
      boolean isUserScenario = curScenario.getScenarioTypeCode().equals(ScenarioTypeCodes.USER);
      boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(curScenario.getScenarioCategoryCode());
      boolean isInProgress = curScenario.getScenarioStateCode().equals(IN_PROGRESS);
      
      if(isProgramYear && isUserScenario && isInProgress && isRealBenefit) {
        inProgressRealBenefitExists = true;
        break;
      }
    }
    return inProgressRealBenefitExists;
  }
  
  
  public static boolean programYearVersionHasVerifiedBenefit(Scenario scenario) {
    boolean result = false;
    Integer programYearVersionNumber = scenario.getFarmingYear().getProgramYearVersionNumber();
    List<ScenarioMetaData> scenarios = scenario.getScenarioMetaDataList();
    for(ScenarioMetaData curScenario : scenarios) {
      
      boolean isProgramYear = curScenario.getProgramYear().equals(scenario.getYear());
      boolean isSameProgramYearVersionNumber = curScenario.getProgramYearVersion() == programYearVersionNumber;
      boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(curScenario.getScenarioCategoryCode());
      boolean isVerified = curScenario.stateIsOneOf(VERIFIED, AMENDED);
      
      if(isProgramYear && isSameProgramYearVersionNumber && isRealBenefit && isVerified) {
        result = true;
        break;
      }
    }
    return result;
  }

  public static boolean isMissingSupplementalDates(Scenario scenario) {
    return scenario.getCraSupplementalReceivedDate() == null
    && scenario.getLocalSupplementalReceivedDate() == null
    && scenario.isRealBenefit()
    && !scenario.getScenarioCategoryCode().equals(INTERIM);
  }
  
  public static boolean isMissingStatementAReceivedDates(Scenario scenario) {
    return scenario.getCraStatementAReceivedDate() == null
    && scenario.getLocalStatementAReceivedDate() == null
    && scenario.isRealBenefit()
    && !scenario.getScenarioCategoryCode().equals(INTERIM);
  }

  public static boolean hasEnhancedBenefits(Scenario scenario) {
    boolean result = false;
    
    if(scenario != null && scenario.getBenefit() != null) {
      Double enhancedTotalBenefit = scenario.getBenefit().getEnhancedTotalBenefit();
      
      boolean enhancedBenefitCalculated = enhancedTotalBenefit != null;
      
      result = enhancedBenefitCalculated
          || CalculatorConfig.hasEnhancedBenefits(scenario.getYear());
    }
    
    return result;
  }


  public static ScenarioMetaData findScenarioByYearAndNumber(List<ScenarioMetaData> programYearMetadata,
      Integer programYear, Integer scenarioNumber) {
    
    ScenarioMetaData scenarioMetaData = programYearMetadata
        .stream()
        .filter(y -> y.getProgramYear().equals(programYear)
            && y.getScenarioNumber().equals(scenarioNumber))
        .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
        .orElse(null);
    return scenarioMetaData;
  }


  public static ScenarioMetaData findNolScenario(List<ScenarioMetaData> programYearMetadata, Integer programYear) {
    ScenarioMetaData scenarioMetaData = programYearMetadata
        .stream()
        .filter(y -> y.getProgramYear().equals(programYear)
            && y.categoryIsOneOf(ScenarioCategoryCodes.NOL)
            && y.stateIsOneOf(IN_PROGRESS, COMPLETED)
            && y.typeIsOneOf(ScenarioTypeCodes.USER))
        .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
        .orElse(null);
    return scenarioMetaData;
  }
  

	public static ScenarioMetaData findScenarioByCategory(List<ScenarioMetaData> programYearMetadata, Integer programYear,
	    String categoryCode, String typeCode) {
		ScenarioMetaData scenarioMetaData = programYearMetadata.stream()
		    .filter(y -> y.getProgramYear().equals(programYear)
		        && y.categoryIsOneOf(categoryCode)
		        && y.getScenarioTypeCode().equals(typeCode))
		    .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
		    .orElse(null);
		return scenarioMetaData;
	}
	
	public static List<ScenarioMetaData> findScenariosByCategory(List<ScenarioMetaData> programYearMetadata, Integer programYear,
	    String categoryCode, String typeCode) {
		List<ScenarioMetaData> scenarioMetaDataList = programYearMetadata.stream()
		    .filter(y -> y.getProgramYear().equals(programYear)
		        && y.categoryIsOneOf(categoryCode)
            && y.getScenarioTypeCode().equals(typeCode))
		    .collect(Collectors.toList());
		return scenarioMetaDataList;
	}


  public static ScenarioMetaData findLatestScenarioByType(List<ScenarioMetaData> programYearMetadata, Integer programYear, String typeCode) {
    ScenarioMetaData scenarioMetaData = programYearMetadata
        .stream()
        .filter(y -> y.getProgramYear().equals(programYear)
            && y.getScenarioTypeCode().equals(typeCode))
        .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
        .orElse(null);
    return scenarioMetaData;
  }

  public static ScenarioMetaData findVerifiedFinalOrLatestAdjustment(List<ScenarioMetaData> programYearMetadata, Integer programYear) {
    ScenarioMetaData scenarioMetaData = programYearMetadata.stream()
        .filter(y -> y.getProgramYear().equals(programYear)
            && y.categoryIsOneOf(FINAL, PRODUCER_ADJUSTMENT, ADMINISTRATIVE_ADJUSTMENT)
            && y.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)
            && y.stateIsOneOf(VERIFIED, AMENDED))
        .max(Comparator.comparing(ScenarioMetaData::getScenarioNumber))
        .orElse(null);
    return scenarioMetaData;
  }

  public static List<ScenarioMetaData> findScenariosByChefSubmissionGuid(List<ScenarioMetaData> programYearMetadata, String chefSubmissionGuid) {
    List<ScenarioMetaData> scenarioMetaDataList = programYearMetadata.stream()
        .filter(y -> chefSubmissionGuid.equals(y.getChefsFormSubmissionGuid()))
        .collect(Collectors.toList());
    return scenarioMetaDataList;
  }
  
  public static List<ScenarioMetaData> findCompletedFifoScenarios(List<ScenarioMetaData> programYearMetadata, Integer programYear) {
    List<ScenarioMetaData> scenarioMetaDataList = programYearMetadata.stream()
        .filter(y -> y.getProgramYear().equals(programYear)
            && y.categoryIsOneOf(FIFO)
            && y.typeIsOneOf(FIFO)
            && y.stateIsOneOf(COMPLETED))
        .collect(Collectors.toList());
    return scenarioMetaDataList;
  }
}
