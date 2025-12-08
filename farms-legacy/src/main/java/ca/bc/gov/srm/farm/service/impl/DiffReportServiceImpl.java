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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.diff.AccrualDiff;
import ca.bc.gov.srm.farm.domain.diff.CropDiff;
import ca.bc.gov.srm.farm.domain.diff.FarmingOperationDiff;
import ca.bc.gov.srm.farm.domain.diff.FieldDiff;
import ca.bc.gov.srm.farm.domain.diff.IncomeExpenseDiff;
import ca.bc.gov.srm.farm.domain.diff.InventoryDiff;
import ca.bc.gov.srm.farm.domain.diff.LivestockDiff;
import ca.bc.gov.srm.farm.domain.diff.ProductiveUnitDiff;
import ca.bc.gov.srm.farm.domain.diff.ProgramYearVersionDiff;
import ca.bc.gov.srm.farm.message.MessageUtility;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.DiffReportService;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * For a given year, DiffReportService compares the CRA
 * data (PYV) for a scenario or reference scenario to the
 * latest Program Year Version and produces an object
 * representing the differences.
 * 
 * @author awilkinson
 * @created Mar 22, 2011
 */
public class DiffReportServiceImpl extends BaseService implements DiffReportService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param oldFarmingYear FarmingYear
   * @param newFarmingYear the new FarmingYear to compare to
   * @return ProgramYearVersionDiff representing the differences
   */
  @Override
  public ProgramYearVersionDiff generateDiffReport(
      FarmingYear oldFarmingYear,
      FarmingYear newFarmingYear) throws InstantiationException, IllegalAccessException {
    
    logger.debug("Generating Diff Report");
    
    ProgramYearVersionDiff pyvDiff = new ProgramYearVersionDiff();
    pyvDiff.setIsLocallyUpdated(oldFarmingYear.getIsLocallyUpdated());
    List<FieldDiff> pyvFieldDiffs = getFieldDiffs(oldFarmingYear, newFarmingYear);
    pyvDiff.setFieldDiffs(pyvFieldDiffs);
    
    // List<FarmingOperationDiff>
    List<FarmingOperationDiff> foDiffs = new ArrayList<>();
    int opNum = 1;
    int maxOpNum = getMaxOpNum(oldFarmingYear, newFarmingYear);
    boolean hasLocallyGeneratedOps = false;
    boolean isNewCRAMissingOperations = false;
    
    while(opNum <= maxOpNum) {
      FarmingOperation opOld = oldFarmingYear.getFarmingOperationByNumber(new Integer(opNum));
      FarmingOperation opNew = newFarmingYear.getFarmingOperationByNumber(new Integer(opNum));
      
      if(opOld != null && opOld.getIsLocallyGenerated().booleanValue()) {
        opOld = null;
        hasLocallyGeneratedOps = true;
      }
      if(opNew != null && opNew.getIsLocallyGenerated().booleanValue()) {
        opNew = null;
      }
      
      if(opOld != null && !opOld.getIsLocallyGenerated().booleanValue() && opNew == null) {
        isNewCRAMissingOperations = true;
      }
      
      FarmingOperationDiff foDiff = null;
      
      if(opOld != null || opNew != null) {
        foDiff = generateFarmingOperationDiff(opOld, opNew);
        foDiffs.add(foDiff);
      }
      
      opNum++;
    }
    pyvDiff.setHasLocallyGeneratedOperations(hasLocallyGeneratedOps);
    pyvDiff.setIsNewCRAMissingOperations(isNewCRAMissingOperations);
    
    pyvDiff.setFarmingOperationDiffs(foDiffs);
    
    boolean hasPyvDetailDifferences = isHasPyvDetailDifferences(pyvDiff);
    pyvDiff.setIsHasPyvDetailDifferences(hasPyvDetailDifferences);
    
    boolean hasDifferences = isHasDifferences(pyvDiff);
    pyvDiff.setIsHasDifferences(hasDifferences);
    
    return pyvDiff;
  }


  private int getMaxOpNum(FarmingYear oldFarmingYear, FarmingYear newFarmingYear) {
    int maxOpNum = 0;
    for(FarmingOperation fo : oldFarmingYear.getFarmingOperations()) {
      int curOpNum = fo.getOperationNumber().intValue();
      if(curOpNum > maxOpNum) {
        maxOpNum = curOpNum;
      }
    }
    for(FarmingOperation fo : newFarmingYear.getFarmingOperations()) {
      int curOpNum = fo.getOperationNumber().intValue();
      if(curOpNum > maxOpNum) {
        maxOpNum = curOpNum;
      }
    }
    return maxOpNum;
  }
  
  
  private FarmingOperationDiff generateFarmingOperationDiff(FarmingOperation a, FarmingOperation b)
      throws InstantiationException, IllegalAccessException {
    
    FarmingOperationDiff foDiff = new FarmingOperationDiff();
    if(a != null) {
      foDiff.setOperationNumber(a.getOperationNumber());
      foDiff.setSchedule(a.getSchedule());
      foDiff.setIsLocallyUpdated(a.getIsLocallyUpdated());
    } else if(b != null) {
      foDiff.setOperationNumber(b.getOperationNumber());
      foDiff.setSchedule(b.getSchedule());
      foDiff.setIsLocallyUpdated(Boolean.FALSE);
    }
    foDiff.setOldOpExists(a != null);
    foDiff.setNewOpExists(b != null);
    if(a != null && b != null) {
      List<FieldDiff> opFieldDiffs = getFieldDiffs(a, b);
      foDiff.setFieldDiffs(opFieldDiffs);
      
      List<IncomeExpenseDiff> incomeExpenseDiffs = generateIncomeExpenseDiffs(a.getIncomeExpenses(), b.getIncomeExpenses());
      foDiff.setIncomeExpenseDiffs(incomeExpenseDiffs);
      
      List<CropDiff> cropDiffs = generateInventoryDiffs(a.getCropItems(), b.getCropItems(), CropDiff.class);
      foDiff.setCropDiffs(cropDiffs);
      
      List<LivestockDiff> livestockDiffs = generateInventoryDiffs(a.getLivestockItems(), b.getLivestockItems(), LivestockDiff.class);
      foDiff.setLivestockDiffs(livestockDiffs);
      
      List<AccrualDiff> inputDiffs = generateInventoryDiffs(a.getInputItems(), b.getInputItems(), AccrualDiff.class);
      foDiff.setInputDiffs(inputDiffs);
      
      List<AccrualDiff> receivableDiffs = generateInventoryDiffs(a.getReceivableItems(), b.getReceivableItems(), AccrualDiff.class);
      foDiff.setReceivableDiffs(receivableDiffs);
      
      List<AccrualDiff> payableDiffs = generateInventoryDiffs(a.getPayableItems(), b.getPayableItems(), AccrualDiff.class);
      foDiff.setPayableDiffs(payableDiffs);

      List<ProductiveUnitDiff> pucDiffs = generatePucDiffs(a.getProductiveUnitCapacities(), b.getProductiveUnitCapacities());
      foDiff.setProductiveUnitDiffs(pucDiffs);
    } else {
      foDiff.setFieldDiffs(new ArrayList<FieldDiff>());
    }
    
    boolean operationHasFieldDifferences = foDiff.getFieldDiffs() != null && ! foDiff.getFieldDiffs().isEmpty();
    foDiff.setIsHasFieldDifferences(operationHasFieldDifferences);
    
    boolean operationHasDifferences = operationHasDifferences(foDiff);
    foDiff.setIsHasDifferences(operationHasDifferences);
    
    return foDiff;
  }

  public boolean operationHasDifferences(FarmingOperationDiff foDiff) {
    if(foDiff.getOldOpExists() != null && ! foDiff.getOldOpExists().equals(foDiff.getNewOpExists())) {
      return true;
    }
    if(foDiff.getIsHasFieldDifferences()) {
      return true;
    }
    if(foDiff.getIncomeExpenseDiffs() != null && ! foDiff.getIncomeExpenseDiffs().isEmpty()) {
      return true;
    }
    if(foDiff.getCropDiffs() != null && ! foDiff.getCropDiffs().isEmpty()) {
      return true;
    }
    if(foDiff.getLivestockDiffs() != null && ! foDiff.getLivestockDiffs().isEmpty()) {
      return true;
    }
    if(foDiff.getInputDiffs() != null && ! foDiff.getInputDiffs().isEmpty()) {
      return true;
    }
    if(foDiff.getReceivableDiffs() != null && ! foDiff.getReceivableDiffs().isEmpty()) {
      return true;
    }
    if(foDiff.getPayableDiffs() != null && ! foDiff.getPayableDiffs().isEmpty()) {
      return true;
    }
    if(foDiff.getProductiveUnitDiffs() != null && ! foDiff.getProductiveUnitDiffs().isEmpty()) {
      return true;
    }
    return false;
  }
  
  
  private List<ProductiveUnitDiff> generatePucDiffs(List<ProductiveUnitCapacity> oldPucs, List<ProductiveUnitCapacity> newPucs) {
    List<ProductiveUnitDiff> diffs = new ArrayList<>();
    
    // create lists with the adjustment-only records removed
    List<ProductiveUnitCapacity> oldReportedPucs;
    if(oldPucs == null || oldPucs.isEmpty()) {
      oldReportedPucs = new ArrayList<>(0);
    } else {
      oldReportedPucs = new ArrayList<>();
      for(ProductiveUnitCapacity puc : oldPucs) {
        if(puc.getReportedProductiveUnitCapacityId() != null) {
          oldReportedPucs.add(puc);
        }
      }
    }
    List<ProductiveUnitCapacity> newReportedPucs;
    if(newPucs == null || newPucs.isEmpty()) {
      newReportedPucs = new ArrayList<>(0);
    } else {
      newReportedPucs = new ArrayList<>();
      for(ProductiveUnitCapacity puc : newPucs) {
        if(puc.getReportedProductiveUnitCapacityId() != null) {
          newReportedPucs.add(puc);
        }
      }
    }
    
    for(ProductiveUnitCapacity a : oldReportedPucs) {
      boolean matched = false;
      
      for(ProductiveUnitCapacity b : newReportedPucs) {
        if(a.matches(b)) {
          matched = true;
          if( ! a.equals(b) ) {
            ProductiveUnitDiff diff = new ProductiveUnitDiff();
            diff.setCode(a.getCode());
            diff.setDescription(a.getDescription());
            diff.setOldValue(a.getReportedAmount());
            diff.setNewValue(b.getReportedAmount());
            diffs.add(diff);
          }
          break;
        }
      }
      
      if( ! matched ) {
        ProductiveUnitDiff diff = new ProductiveUnitDiff();
        diff.setCode(a.getCode());
        diff.setDescription(a.getDescription());
        diff.setOldValue(a.getReportedAmount());
        diff.setNewValue(null);
        diffs.add(diff);
      }
    }
    
    for(Iterator<ProductiveUnitCapacity> oi = newReportedPucs.iterator(); oi.hasNext(); ) {
      ProductiveUnitCapacity b = oi.next();
      boolean matched = false;
      
      for(Iterator<ProductiveUnitCapacity> ni = oldReportedPucs.iterator(); ni.hasNext(); ) {
        ProductiveUnitCapacity a = ni.next();
        if(b.matches(a)) {
          matched = true;
          break;
        }
      }
      
      if( ! matched ) {
        ProductiveUnitDiff diff = new ProductiveUnitDiff();
        diff.setCode(b.getCode());
        diff.setDescription(b.getDescription());
        diff.setOldValue(null);
        diff.setNewValue(b.getReportedAmount());
        diffs.add(diff);
      }
    }
    
    return diffs;
  }
  
  
  private List<IncomeExpenseDiff> generateIncomeExpenseDiffs(List<IncomeExpense> oldIEs, List<IncomeExpense> newIEs) {
    List<IncomeExpenseDiff> diffs = new ArrayList<>();
    
    // create lists with the adjustment-only records removed
    List<IncomeExpense> oldReportedIEs;
    if(oldIEs == null || oldIEs.isEmpty()) {
      oldReportedIEs = new ArrayList<>(0);
    } else {
      oldReportedIEs = new ArrayList<>();
      for(IncomeExpense ie : oldIEs) {
        if(ie.getReportedIncomeExpenseId() != null) {
          oldReportedIEs.add(ie);
        }
      }
    }
    List<IncomeExpense> newReportedIEs;
    if(newIEs == null || newIEs.isEmpty()) {
      newReportedIEs = new ArrayList<>(0);
    } else {
      newReportedIEs = new ArrayList<>();
      for(IncomeExpense ie : newIEs) {
        if(ie.getReportedIncomeExpenseId() != null) {
          newReportedIEs.add(ie);
        }
      }
    }
    
    for(Iterator<IncomeExpense> oi = oldReportedIEs.iterator(); oi.hasNext(); ) {
      IncomeExpense a = oi.next();
      String code = a.getLineItem().getLineItem().toString();
      String description = a.getLineItem().getDescription();
      boolean matched = false;
      
      for(IncomeExpense b : newReportedIEs) {
        if(a.matches(b)) {
          matched = true;
          if( ! a.equals(b) ) {
            IncomeExpenseDiff diff = new IncomeExpenseDiff();
            diff.setCode(code);
            diff.setDescription(description);
            diff.setIsExpense(a.getIsExpense());
            diff.setOldValue(a.getReportedAmount());
            diff.setNewValue(b.getReportedAmount());
            diffs.add(diff);
          }
          break;
        }
      }
      
      if( ! matched ) {
        IncomeExpenseDiff diff = new IncomeExpenseDiff();
        diff.setCode(code);
        diff.setDescription(description);
        diff.setIsExpense(a.getIsExpense());
        diff.setOldValue(a.getReportedAmount());
        diff.setNewValue(null);
        diffs.add(diff);
      }
    }
    
    for(Iterator<IncomeExpense> oi = newReportedIEs.iterator(); oi.hasNext(); ) {
      IncomeExpense b = oi.next();
      String code = b.getLineItem().getLineItem().toString();
      String description = b.getLineItem().getDescription();
      boolean matched = false;
      
      for(Iterator<IncomeExpense> ni = oldReportedIEs.iterator(); ni.hasNext(); ) {
        IncomeExpense a = ni.next();
        if(b.matches(a)) {
          matched = true;
          break;
        }
      }
      
      if( ! matched ) {
        IncomeExpenseDiff diff = new IncomeExpenseDiff();
        diff.setCode(code);
        diff.setDescription(description);
        diff.setIsExpense(b.getIsExpense());
        diff.setOldValue(null);
        diff.setNewValue(b.getReportedAmount());
        diffs.add(diff);
      }
    }
    
    return diffs;
  }
  
  
  private <DiffType extends InventoryDiff> List<DiffType> generateInventoryDiffs(List<? extends InventoryItem> oldInventory, List<? extends InventoryItem> newInventory, Class<DiffType> diffClass)
      throws InstantiationException, IllegalAccessException {
    
    List<DiffType> diffs = new ArrayList<>();
    
    // create lists with the adjustment-only records removed
    List<InventoryItem> oldReportedInventory;
    if(oldInventory == null || oldInventory.isEmpty()) {
      oldReportedInventory = new ArrayList<>(0);
    } else {
      oldReportedInventory = new ArrayList<>();
      for(InventoryItem ri : oldInventory) {
        if(ri.getReportedInventoryId() != null) {
          oldReportedInventory.add(ri);
        }
      }
    }
    List<InventoryItem> newReportedInventory;
    if(newInventory == null || newInventory.isEmpty()) {
      newReportedInventory = new ArrayList<>(0);
    } else {
      newReportedInventory = new ArrayList<>();
      for(InventoryItem ri : newInventory) {
        if(ri.getReportedInventoryId() != null) {
          newReportedInventory.add(ri);
        }
      }
    }
    
    for(Iterator<InventoryItem> oi = oldReportedInventory.iterator(); oi.hasNext(); ) {
      InventoryItem a = oi.next();
      
      int index = newReportedInventory.indexOf(a);
      if(index >= 0) {
        oi.remove();
        newReportedInventory.remove(index);
      }
    }
    
    for(Iterator<InventoryItem> oi = oldReportedInventory.iterator(); oi.hasNext(); ) {
      InventoryItem a = oi.next();
      DiffType diff = createInventoryDiff(a, MessageConstants.ACTION_REMOVED, diffClass);
      diffs.add(diff);
    }
    
    for(Iterator<InventoryItem> ni = newReportedInventory.iterator(); ni.hasNext(); ) {
      InventoryItem b = ni.next();
      DiffType diff = createInventoryDiff(b, MessageConstants.ACTION_ADDED, diffClass);
      diffs.add(diff);
    }
    
    return diffs;
  }


  private <T extends InventoryDiff> T createInventoryDiff(InventoryItem a, String action, Class<T> diffClass)
      throws InstantiationException, IllegalAccessException {
    
    T diff = diffClass.newInstance();

    boolean isCrop = a.getInventoryClassCode().equals(InventoryClassCodes.CROP);
    boolean isLivestock = a.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK);
    boolean isAccrual =
      a.getInventoryClassCode().equals(InventoryClassCodes.INPUT)
      || a.getInventoryClassCode().equals(InventoryClassCodes.RECEIVABLE)
      || a.getInventoryClassCode().equals(InventoryClassCodes.PAYABLE);

    if(isCrop) {
      CropDiff cropDiff = (CropDiff) diff;
      CropItem cropItem = (CropItem) a;
      cropDiff.setCropUnitCode(cropItem.getCropUnitCode());
      cropDiff.setCropUnitCodeDescription(cropItem.getCropUnitCodeDescription());
      cropDiff.setQuantityProduced(cropItem.getReportedQuantityProduced());
      cropDiff.setOnFarmAcres(cropItem.getOnFarmAcres());
      cropDiff.setUnseedableAcres(cropItem.getUnseedableAcres());
    } else if(isLivestock) {
      // nothing to do
    } else if(isAccrual) {
      AccrualDiff accrDiff = (AccrualDiff) diff;
      accrDiff.setStartValue(a.getReportedStartOfYearAmount());
      accrDiff.setEndValue(a.getReportedEndOfYearAmount());
    }
    
    if(diff != null) {
      
      if(isCrop || isLivestock) {
        diff.setQuantityStart(a.getReportedQuantityStart());
        diff.setPriceStart(a.getReportedPriceStart());
        diff.setQuantityEnd(a.getReportedQuantityEnd());
        diff.setPriceEnd(a.getReportedPriceEnd());
      }
      
      diff.setAction(action);
      diff.setCode(a.getInventoryItemCode());
      diff.setDescription(a.getInventoryItemCodeDescription());
    }
    
    return diff;
  }


  private List<FieldDiff> getFieldDiffs(FarmingOperation a, FarmingOperation b) {
    List<FieldDiff> fds = new ArrayList<>();
    
    final int twoDecimalPlaces = 2;
    final int fourDecimalPlaces = 4;
    
    addFieldDiff(fds, MessageConstants.FIELD_SCHEDULE, a.getSchedule(), b.getSchedule());
    addFieldDiff(fds, MessageConstants.FIELD_ACCOUNTING_CODE, a.getAccountingCode(), b.getAccountingCode());
    addFieldDiff(fds, MessageConstants.FIELD_FISCAL_START, a.getFiscalYearStart(), b.getFiscalYearStart());
    addFieldDiff(fds, MessageConstants.FIELD_FISCAL_END, a.getFiscalYearEnd(), b.getFiscalYearEnd());
    addFieldDiff(fds, MessageConstants.FIELD_CROP_SHARE, a.getIsCropShare(), b.getIsCropShare());
    addFieldDiff(fds, MessageConstants.FIELD_FEEDER_MEMBER, a.getIsFeederMember(), b.getIsFeederMember());
    addFieldDiff(fds, MessageConstants.FIELD_PARTNERSHIP_PIN, a.getPartnershipPin(), b.getPartnershipPin());
    addFieldDiff(fds, MessageConstants.FIELD_PARTNERSHIP_NAME, a.getPartnershipName(), b.getPartnershipName());
    addFieldDiff(fds, MessageConstants.FIELD_PARTNERSHIP_PERCENT, a.getPartnershipPercent(), b.getPartnershipPercent(), fourDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_CROP_DISASTER, a.getIsCropDisaster(), b.getIsCropDisaster());
    addFieldDiff(fds, MessageConstants.FIELD_LIVESTOCK_DISASTER, a.getIsLivestockDisaster(), b.getIsLivestockDisaster());
    addFieldDiff(fds, MessageConstants.FIELD_LANDLORD, a.getIsLandlord(), b.getIsLandlord());
    addFieldDiff(fds, MessageConstants.FIELD_EXPENSES, a.getFarmingExpenses(), b.getFarmingExpenses(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_BUSINESS_USE_HOME_EXPENSE, a.getBusinessUseHomeExpense(), b.getBusinessUseHomeExpense(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_OTHER_DEDUCTIONS, a.getOtherDeductions(), b.getOtherDeductions(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_INVENTORY_ADJUSTMENTS, a.getInventoryAdjustments(), b.getInventoryAdjustments(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_GROSS_INCOME, a.getGrossIncome(), b.getGrossIncome(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_NET_FARM_INCOME, a.getNetFarmIncome(), b.getNetFarmIncome(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_NET_INCOME_BEFORE_ADJ, a.getNetIncomeBeforeAdj(), b.getNetIncomeBeforeAdj(), twoDecimalPlaces);
    addFieldDiff(fds, MessageConstants.FIELD_NET_INCOME_AFTER_ADJ, a.getNetIncomeAfterAdj(), b.getNetIncomeAfterAdj(), twoDecimalPlaces);
    
    // List<ProductionInsurance>
    List<ProductionInsurance> oldPolicyNumbersList = a.getProductionInsurances();
    List<ProductionInsurance> newPolicyNumbersList = b.getProductionInsurances();
    
    // List<String>
    List<String> oldPiNums = new ArrayList<>();
    List<String> newPiNums = new ArrayList<>();
    
    if(oldPolicyNumbersList != null) {
      for(ProductionInsurance pi : oldPolicyNumbersList) {
        String piNum = pi.getProductionInsuranceNumber();
        oldPiNums.add(piNum);
      }
    }
    if(newPolicyNumbersList != null) {
      for(ProductionInsurance pi : newPolicyNumbersList) {
        String piNum = pi.getProductionInsuranceNumber();
        newPiNums.add(piNum);
      }
    }
    
    Collections.sort(oldPiNums);
    Collections.sort(newPiNums);
    
    StringBuffer sbNewPi = new StringBuffer();
    StringBuffer sbOldPi = new StringBuffer();
    
    for(Iterator<String> ii = oldPiNums.iterator(); ii.hasNext(); ) {
      String piNum = ii.next();
      sbOldPi.append(piNum);
      if(ii.hasNext()) {
        sbOldPi.append(", ");
      }
    }
    for(Iterator<String> ii = newPiNums.iterator(); ii.hasNext(); ) {
      String piNum = ii.next();
      sbNewPi.append(piNum);
      if(ii.hasNext()) {
        sbNewPi.append(", ");
      }
    }
    
    addFieldDiff(fds, MessageConstants.FIELD_PRODUCTION_INSURANCE_NUMBERS, sbOldPi.toString(), sbNewPi.toString());

    return fds;
  }
    
    
  private List<FieldDiff> getFieldDiffs(FarmingYear a, FarmingYear b) {
    List<FieldDiff> fds = new ArrayList<>();
      
    addFieldDiff(fds, MessageConstants.FIELD_FARM_YEARS, a.getFarmYears(), b.getFarmYears());
    addFieldDiff(fds, MessageConstants.FIELD_FEDERAL_STATUS, a.getAgristabFedStsCode(), b.getAgristabFedStsCode());
    addFieldDiff(fds, MessageConstants.FIELD_PROVINCE_OF_RESIDENCE, a.getProvinceOfResidence(), b.getProvinceOfResidence());
    addFieldDiff(fds, MessageConstants.FIELD_PROVINCE_OF_MAIN_FARMSTEAD, a.getProvinceOfMainFarmstead(), b.getProvinceOfMainFarmstead());
    addFieldDiff(fds, MessageConstants.FIELD_OTHER_TEXT, a.getOtherText(), b.getOtherText());
    addFieldDiff(fds, MessageConstants.FIELD_MUNICIPALITY, a.getMunicipalityCode(), b.getMunicipalityCode());
    addFieldDiff(fds, MessageConstants.FIELD_POST_MARK_DATE, a.getPostMarkDate(), b.getPostMarkDate());
    addFieldDiff(fds, MessageConstants.FIELD_RECEIVED_DATE, a.getCraStatementAReceivedDate(), b.getCraStatementAReceivedDate());
    addFieldDiff(fds, MessageConstants.FIELD_COMMON_SHARE_TOTAL, a.getCommonShareTotal(), b.getCommonShareTotal());
    addFieldDiff(fds, MessageConstants.FIELD_PARTICIPANT_PROFILE, a.getParticipantProfileCode(), b.getParticipantProfileCode());
    addFieldDiff(fds, MessageConstants.FIELD_SOLE_PROPRIETOR, a.getIsSoleProprietor(), b.getIsSoleProprietor());
    addFieldDiff(fds, MessageConstants.FIELD_COMPLETED_PROD_CYCLE, a.getIsCompletedProdCycle(), b.getIsCompletedProdCycle());
    addFieldDiff(fds, MessageConstants.FIELD_CORPORATE_SHAREHOLDER, a.getIsCorporateShareholder(), b.getIsCorporateShareholder());
    addFieldDiff(fds, MessageConstants.FIELD_DISASTER, a.getIsDisaster(), b.getIsDisaster());
    addFieldDiff(fds, MessageConstants.FIELD_PARTNERSHIP_MEMBER, a.getIsPartnershipMember(), b.getIsPartnershipMember());
    addFieldDiff(fds, MessageConstants.FIELD_LAST_YEAR_FARMING, a.getIsLastYearFarming(), b.getIsLastYearFarming());
    addFieldDiff(fds, MessageConstants.FIELD_CO_OP_MEMBER, a.getIsCoopMember(), b.getIsCoopMember());
    addFieldDiff(fds, MessageConstants.FIELD_ACCRUAL_CASH_CONVERSION, a.getIsAccrualCashConversion(), b.getIsAccrualCashConversion());
    addFieldDiff(fds, MessageConstants.FIELD_COMBINED_THIS_YEAR, a.getIsCombinedFarm(), b.getIsCombinedFarm());
    addFieldDiff(fds, MessageConstants.FIELD_CAN_SEND_COB_TO_REP, a.getIsCanSendCobToRep(), b.getIsCanSendCobToRep());
    addFieldDiff(fds, MessageConstants.FIELD_CWB_WORKSHEET, a.getIsCwbWorksheet(), b.getIsCwbWorksheet());
    addFieldDiff(fds, MessageConstants.FIELD_RECEIPT, a.getIsReceipts(), b.getIsReceipts());
    addFieldDiff(fds, MessageConstants.FIELD_ACCRUAL_WORKSHEET, a.getIsAccrualWorksheet(), b.getIsAccrualWorksheet());
    addFieldDiff(fds, MessageConstants.FIELD_PERISHABLE_COMMODITIES, a.getIsPerishableCommodities(), b.getIsPerishableCommodities());
    
    return fds;
  }
  
  
  private void addFieldDiff(List<FieldDiff> fieldDiffs, String fieldName, String a, String b) {
    if(valuesDiffer(a, b)) {
      MessageUtility msgs = MessageUtility.getInstance();
      FieldDiff fd = new FieldDiff();
      fd.setOldValue(getDisplayValue(a));
      fd.setNewValue(getDisplayValue(b));
      fd.setFieldName(msgs.getPattern(fieldName));
      fieldDiffs.add(fd);
    }
  }
  
  
  private void addFieldDiff(List<FieldDiff> fieldDiffs, String fieldName, Double a, Double b, int scale) {
    if(valuesDiffer(a, b, scale)) {
      MessageUtility msgs = MessageUtility.getInstance();
      FieldDiff fd = new FieldDiff();
      fd.setOldValue(getDisplayValue(a));
      fd.setNewValue(getDisplayValue(b));
      fd.setFieldName(msgs.getPattern(fieldName));
      fieldDiffs.add(fd);
    }
  }


  private void addFieldDiff(List<FieldDiff> fieldDiffs, String fieldName, Integer a, Integer b) {
    if(valuesDiffer(a, b)) {
      MessageUtility msgs = MessageUtility.getInstance();
      FieldDiff fd = new FieldDiff();
      fd.setOldValue(getDisplayValue(a));
      fd.setNewValue(getDisplayValue(b));
      fd.setFieldName(msgs.getPattern(fieldName));
      fieldDiffs.add(fd);
    }
  }
  
  
  private void addFieldDiff(List<FieldDiff> fieldDiffs, String fieldName, Date a, Date b) {
    if(valuesDiffer(a, b)) {
      MessageUtility msgs = MessageUtility.getInstance();
      FieldDiff fd = new FieldDiff();
      fd.setOldValue(DateUtils.formatDate(a));
      fd.setNewValue(DateUtils.formatDate(b));
      fd.setFieldName(msgs.getPattern(fieldName));
      fieldDiffs.add(fd);
    }
  }
  
  
  private void addFieldDiff(List<FieldDiff> fieldDiffs, String fieldName, Boolean a, Boolean b) {
    if(valuesDiffer(a, b)) {
      MessageUtility msgs = MessageUtility.getInstance();
      FieldDiff fd = new FieldDiff();
      fd.setOldValue(getIndicator(a));
      fd.setNewValue(getIndicator(b));
      fd.setFieldName(msgs.getPattern(fieldName));
      fieldDiffs.add(fd);
    }
  }
  
  private String getDisplayValue(Object o) {
    if(o == null) {
      return "";
    } else {
      return o.toString();
    }
  }
  
  
  private String getIndicator(Boolean b) {
    String result;
    if(b == null) {
      result = "";
    } else if(b.booleanValue()) {
      result = "Y";
    } else {
      result = "N";
    }
    return result;
  }


  private boolean valuesDiffer(String a, String b) {
    return ! StringUtils.equal(a, b);
  }
  
  private boolean valuesDiffer(Double a, Double b, int scale) {
    return ! MathUtils.valuesEqual(a, b, scale);
  }
  
  private boolean valuesDiffer(Integer a, Integer b) {
    return ! MathUtils.valuesEqual(a, b);
  }
  
  private boolean valuesDiffer(Date a, Date b) {
    return ! DateUtils.equal(a, b);
  }
  
  private boolean valuesDiffer(Boolean a, Boolean b) {
    boolean equal;
    if(a == null && b == null) {
      equal = true;
    } else {
      if(a != null && a.equals(b)) {
        equal = true;
      } else {
        equal = false;
      }
    }
    return !equal;
  }

  private boolean isHasPyvDetailDifferences(ProgramYearVersionDiff pyvDiff) {
    List<FieldDiff> fieldDiffs = pyvDiff.getFieldDiffs();
    if(fieldDiffs != null && ! fieldDiffs.isEmpty()) {
      return true;
    }
    Boolean hasLocallyGeneratedOperations = pyvDiff.getHasLocallyGeneratedOperations();
    if(hasLocallyGeneratedOperations != null && hasLocallyGeneratedOperations.booleanValue()) {
      return true;
    }
    return false;
  }
  
  private boolean isHasDifferences(ProgramYearVersionDiff pyvDiff) {
    List<FarmingOperationDiff> farmingOperationDiffs = pyvDiff.getFarmingOperationDiffs();
    if(farmingOperationDiffs != null && ! farmingOperationDiffs.isEmpty()) {
      for(Iterator<FarmingOperationDiff> foi = farmingOperationDiffs.iterator(); foi.hasNext(); ) {
        FarmingOperationDiff fod = foi.next();
        if(fod.getIsHasDifferences()) {
          return true;
        }
      }
    }
    return pyvDiff.getIsHasPyvDetailDifferences();
  }

}
