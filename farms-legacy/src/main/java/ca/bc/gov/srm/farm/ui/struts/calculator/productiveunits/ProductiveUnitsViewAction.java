/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 21, 2010
 */
public class ProductiveUnitsViewAction extends CalculatorAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Productive Units...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ProductiveUnitsForm form = (ProductiveUnitsForm) actionForm;
    
    Scenario scenario = getScenario(form);
    
    populateForm(form, scenario, true);
    
    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @param setAdjustedValues if false, do not clear the form, and set everything but adjusted values
   * @throws Exception On Exception
   */
  protected void populateForm(
      ProductiveUnitsForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    
    if(setAdjustedValues) {
      form.clear();
    }
    
    if(StringUtils.isBlank(form.getFinancialViewRadio())) {
      form.setUnitsViewRadio(ProductiveUnitsForm.UNITS_VIEW_ROLLUP);
      if(scenario.isUserScenario() || scenario.isFifoScenario()) {
        form.setFinancialViewRadio(ProductiveUnitsForm.FINANCIAL_VIEW_ADJUSTED);
      } else {
        form.setFinancialViewRadio(ProductiveUnitsForm.FINANCIAL_VIEW_CRA);
      }
    }
    
    if(setAdjustedValues) {
      form.setScenarioRevisionCount(scenario.getRevisionCount());
    }
    
    syncFarmViewCacheWithForm(form);
    
    if(form.getFarmView().equals(CalculatorForm.FARM_VIEW_WHOLE)) {
      populateFormForWholeFarm(form, scenario);
      populateFormForRolledUpWholeFarm(form, scenario);
    } else {
      String schedule = form.getFarmView();
      form.setYearOperationMap(getYearOpMap(schedule, scenario));
      populateFormForSingleOperation(form, scenario, setAdjustedValues);
      populateFormForRolledUpSingleOperation(form, scenario, setAdjustedValues);
    }
    
    String participantDataSrcCode = scenario.getParticipantDataSrcCode();
    if (participantDataSrcCode == null) {
      participantDataSrcCode = ParticipantDataSrcCodes.CRA;
    }
    
    if(StringUtils.isBlank(form.getViewDataSetRadio())) {
      form.setViewDataSetRadio(participantDataSrcCode);
    }
    
    if(StringUtils.isBlank(form.getDataSetUsedRadio())) {
      form.setDataSetUsedRadio(participantDataSrcCode);
    }
    
    populateRequiredYears(form, scenario);
    populateFarmViewOptions(form, scenario);
    populateOperationsForImport(form, scenario);
    populateParticipantDataSrcCodes(form, scenario);
  }

  private void populateFormForSingleOperation(
      ProductiveUnitsForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    populateFormForSingleOperation(form, scenario, setAdjustedValues, false);
  }

  private void populateFormForRolledUpSingleOperation(
      ProductiveUnitsForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    populateFormForSingleOperation(form, scenario, setAdjustedValues, true);
  }

  /**
   * @param form ProductiveUnitsForm
   * @param scenario Scenario
   * @param setAdjustedValues boolean
   * @throws Exception On Exception
   */
  private void populateFormForSingleOperation(
      ProductiveUnitsForm form,
      Scenario scenario,
      boolean setAdjustedValues,
      boolean isRolledUp)
  throws Exception {
    
    List<ProductiveUnitCapacity> pucs = isRolledUp ? getRolledUpPucs(form, scenario) : getPucs(form, scenario);
    Set<String> participantDataSrcCodeSet = new HashSet<> (); 
    
    for(ProductiveUnitCapacity puc : pucs) {
      String year = puc.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear().toString();
      participantDataSrcCodeSet.add(puc.getParticipantDataSrcCode());
      if (isRolledUp) {
        populateFormFromRolledUpPuc(form, year, puc, setAdjustedValues, scenario);
      } else {
        populateFormFromPuc(form, year, puc, setAdjustedValues, scenario);
      }
    }
    logger.debug("participantDataSrcCodeSet size: " + participantDataSrcCodeSet.size());

  }

  private void populateFormForWholeFarm(
      ProductiveUnitsForm form,
      Scenario scenario)
  throws Exception {
    populateFormForWholeFarm(form, scenario, false);
  }

  private void populateFormForRolledUpWholeFarm(
      ProductiveUnitsForm form,
      Scenario scenario)
  throws Exception {
    populateFormForWholeFarm(form, scenario, true);
  }

  /**
   * Fill the form fields from the reference scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  private void populateFormForWholeFarm(
      ProductiveUnitsForm form,
      Scenario scenario,
      boolean isRolledUp)
  throws Exception {
    
    List<ProductiveUnitCapacity> pucs = isRolledUp? getRolledUpPucs(form, scenario) : getPucs(form, scenario);
    
    // Map<year, Map<lineKey, Map<participantDataSrcCode, List<ProductiveUnitCapacity>>>>
    Map<String, Map<String, Map<String, List<ProductiveUnitCapacity>>>> yearLineKeyDataSrcPucListMap = new HashMap<>(scenario.getReferenceScenarios().size() + 1);
    
    for(ProductiveUnitCapacity puc : pucs) {
      
      String year = puc.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear().toString();
      
      String participantDataSrcCode;
      // See comment in populateParticipantDataSrcCodes method below.
      if(scenario.isInCombinedFarm() && form.isWholeFarmView()) {
        participantDataSrcCode = ParticipantDataSrcCodes.NONE;
      } else {
        participantDataSrcCode = puc.getParticipantDataSrcCode();
      }
      
      String lineKey = ProductiveUnitFormLine.getLineKey(
          puc.getStructureGroupCode(), puc.getInventoryItemCode());

      // Map<lineKey, Map<participantDataSrcCode, List<ProductiveUnitCapacity>>>
      Map<String, Map<String, List<ProductiveUnitCapacity>>> lineKeyDataSrcPucListMap = yearLineKeyDataSrcPucListMap.get(year);
      if(lineKeyDataSrcPucListMap == null) {
        lineKeyDataSrcPucListMap = new HashMap<>();
        yearLineKeyDataSrcPucListMap.put(year, lineKeyDataSrcPucListMap);
      }
      
      // Map<participantDataSrcCode, List<ProductiveUnitCapacity>>
      Map<String, List<ProductiveUnitCapacity>> dataSrcPucListMap = lineKeyDataSrcPucListMap.get(lineKey);
      if(dataSrcPucListMap == null) {
        dataSrcPucListMap = new HashMap<>();
        lineKeyDataSrcPucListMap.put(lineKey, dataSrcPucListMap);
      }
      
      List<ProductiveUnitCapacity> pucList = dataSrcPucListMap.get(participantDataSrcCode);
      if(pucList == null) {
        pucList = new ArrayList<>();
        dataSrcPucListMap.put(participantDataSrcCode, pucList);
      }
      
      pucList.add(puc);
    }
    

    for(String year : yearLineKeyDataSrcPucListMap.keySet()) {
      Map<String, Map<String, List<ProductiveUnitCapacity>>> lineKeyDataSrcPucListMap = yearLineKeyDataSrcPucListMap.get(year);
      
      for(String lineKey : lineKeyDataSrcPucListMap.keySet()) {
        Map<String, List<ProductiveUnitCapacity>> dataSrcPucListMap = lineKeyDataSrcPucListMap.get(lineKey);
        
        for(String participantDataSrcCode : dataSrcPucListMap.keySet()) {
          List<ProductiveUnitCapacity> pucList = dataSrcPucListMap.get(participantDataSrcCode);
          
          ProductiveUnitCapacity wholeFarmPuc = new ProductiveUnitCapacity();
          
          double reportedAmountSum = 0d;
          double adjAmountSum = 0d;
          double onFarmAcresSum = 0d;
          double unseedableAcresSum = 0d;
          
          boolean reportedExists = false;
          boolean adjExists = false;
          boolean onFarmAcresExists = false;
          boolean unseedableAcresExists = false;
          
          boolean firstPuc = true;
          for(ProductiveUnitCapacity puc : pucList) {
            FarmingOperation fo = puc.getFarmingOperation();
            double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
            
            if(firstPuc) {
              firstPuc = false;
              wholeFarmPuc.setStructureGroupCode(puc.getStructureGroupCode());
              wholeFarmPuc.setStructureGroupCodeDescription(puc.getStructureGroupCodeDescription());
              wholeFarmPuc.setRollupStructureGroupCode(puc.getRollupStructureGroupCode());
              wholeFarmPuc.setRollupStructureGroupCodeDescription(puc.getRollupStructureGroupCodeDescription());
              wholeFarmPuc.setInventoryItemCode(puc.getInventoryItemCode());
              wholeFarmPuc.setInventoryItemCodeDescription(puc.getInventoryItemCodeDescription());
              wholeFarmPuc.setRollupInventoryItemCode(puc.getRollupInventoryItemCode());
              wholeFarmPuc.setRollupInventoryItemCodeDescription(puc.getRollupInventoryItemCodeDescription());
              wholeFarmPuc.setMultiStageCommodityCode(puc.getMultiStageCommodityCode());
              wholeFarmPuc.setParticipantDataSrcCode(participantDataSrcCode);
            }
            
            if(puc.getReportedAmount() != null) {
              reportedExists = true;
              reportedAmountSum += puc.getReportedAmount().doubleValue() * partnershipPercent;
            }
            if(puc.getAdjAmount() != null) {
              adjExists = true;
              adjAmountSum += puc.getAdjAmount().doubleValue() * partnershipPercent;
            }
            Double onFarmAcres = puc.getOnFarmAcres();
            if(onFarmAcres != null) {
              onFarmAcresExists = true;
              onFarmAcresSum += onFarmAcres.doubleValue() * partnershipPercent;
            }
            Double unseedableAcres = puc.getUnseedableAcres();
            if(unseedableAcres != null) {
              unseedableAcresExists = true;
              unseedableAcresSum += unseedableAcres.doubleValue() * partnershipPercent;
            }
          }
          
          final int scale = 3; // decimal places
          
          if(reportedExists) {
            double roundedAmount = MathUtils.round(reportedAmountSum, scale);
            wholeFarmPuc.setReportedAmount(new Double(roundedAmount));
          }
          if(adjExists) {
            double roundedAmount = MathUtils.round(adjAmountSum, scale);
            wholeFarmPuc.setAdjAmount(new Double(roundedAmount));
          }
          if(onFarmAcresExists) {
            double roundedAmount = MathUtils.round(onFarmAcresSum, scale);
            wholeFarmPuc.setOnFarmAcres(new Double(roundedAmount));
          }
          if(unseedableAcresExists) {
            double roundedAmount = MathUtils.round(unseedableAcresSum, scale);
            wholeFarmPuc.setUnseedableAcres(new Double(roundedAmount));
          }

          if (isRolledUp) {
            populateFormFromRolledUpPuc(form, year, wholeFarmPuc, true, scenario);
          } else {
            populateFormFromPuc(form, year, wholeFarmPuc, true, scenario);
          }
        }
      }

    }
    
  }

  private List<ProductiveUnitCapacity> getPucs(
      ProductiveUnitsForm form,
      Scenario scenario)
  throws Exception {
    return getPucs(form, scenario, false);
  }

  private List<ProductiveUnitCapacity> getRolledUpPucs(
      ProductiveUnitsForm form,
      Scenario scenario)
  throws Exception {
    return getPucs(form, scenario, true);
  }

  /**
   * @param form ProductiveUnitsForm
   * @param scenario Scenario
   * @return List<ProductiveUnitCapacity>
   * @throws Exception On Exception
   */
  private List<ProductiveUnitCapacity> getPucs(
      ProductiveUnitsForm form,
      Scenario scenario,
      boolean isRolledUp)
  throws Exception {
    
    List<ProductiveUnitCapacity> pucs = new ArrayList<>();
    
    String schedule = null;
    if( ! form.getFarmView().equals(CalculatorForm.FARM_VIEW_WHOLE) ) {
      schedule = form.getFarmView();
    }
    
    List<Integer> allYears = ScenarioUtils.getAllYears(scenario.getYear());
    for(int year : allYears) {
      
      boolean isProgramYear = scenario.getYear().equals(year);
      
      List<ReferenceScenario> referenceScenarios;
      // If schedule is null then we are displaying Whole Farm View or Combined Farm View,
      // which will combine multiple operations.
      // If schedule is not null then just display for the one operation for this PIN.
      if(schedule == null) {
        referenceScenarios = scenario.getReferenceScenariosByYear(year);
      } else {
        referenceScenarios = new ArrayList<>();
        ReferenceScenario rs = scenario.getReferenceScenarioByYear(year);
        if(rs != null) {
          referenceScenarios.add(rs);
        }
      }
      
      for(ReferenceScenario refScenario : referenceScenarios) {
        if(refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
          List<FarmingOperation> farmingOperations = refScenario.getFarmingYear().getFarmingOperations();
          for(FarmingOperation fo : farmingOperations) {
            logger.debug(fo.toString());
            
            if(form.isWholeFarmView()
                || fo.getSchedule().equals(schedule)) {
              
              List<ProductiveUnitCapacity> opPucs;
              
              // For the program year, get both local and CRA PUCs.
              // For the reference year, just get the PUCs for the
              // participantDataSrcCode that was selected for that year.
              if(isProgramYear && !(form.isWholeFarmView() && scenario.isInCombinedFarm())) {
                opPucs = isRolledUp? fo.getAllRolledUpProductiveUnitCapacities() : fo.getAllProductiveUnitCapacities();
              } else {
                opPucs = isRolledUp? fo.getRolledUpProductiveUnitCapacities() : fo.getProductiveUnitCapacities();
              }
              
              for(ProductiveUnitCapacity puc : opPucs) {
                pucs.add(puc);
              }
              
            }
          }
        }
      }
    }
    
    return pucs;
  }

  private void populateFormFromPuc(
      ProductiveUnitsForm form,
      String year,
      ProductiveUnitCapacity puc,
      boolean setAdjustedValues,
      Scenario scenario)
  throws Exception {
    populateFormFromPuc(form, year, puc, setAdjustedValues, scenario, false);
  }

  private void populateFormFromRolledUpPuc(
      ProductiveUnitsForm form,
      String year,
      ProductiveUnitCapacity puc,
      boolean setAdjustedValues,
      Scenario scenario)
  throws Exception {
    populateFormFromPuc(form, year, puc, setAdjustedValues, scenario, true);
  }

  /**
   * Fill the form fields from the reference scenario
   * @param form The form object to fill.
   * @param year String
   * @param puc The ProductiveUnitCapacity to fill the form from.
   * @param setAdjustedValues if false, don not clear the form and set everything but adjusted values
   * @param scenario scenario
   * @throws Exception On Exception
   */
  private void populateFormFromPuc(
      ProductiveUnitsForm form,
      String year,
      ProductiveUnitCapacity puc,
      boolean setAdjustedValues,
      Scenario scenario,
      boolean isRolledUp)
  throws Exception {
    DecimalFormat df = new DecimalFormat("#0.000");
    
    String programYearString = String.valueOf(form.getYear());
    boolean isProgramYear = year.equals(programYearString);
    String participantDataSrcCode = puc.getParticipantDataSrcCode();

    String lineKey = null;
    ProductiveUnitFormLine item = null;
    if (isRolledUp) {
      lineKey = ProductiveUnitFormLine.getLineKey(
          puc.getRollupStructureGroupCode(), puc.getRollupInventoryItemCode());
      item = form.getRolledUpItem(lineKey);
    } else {
      lineKey = ProductiveUnitFormLine.getLineKey(
          puc.getStructureGroupCode(), puc.getInventoryItemCode());
      item = form.getItem(lineKey);
    }
    ProductiveUnitFormRecord record = item.getRecord(participantDataSrcCode);

    if(puc.getStructureGroupCode() != null) {
      item.setType(ProductiveUnitFormLine.TYPE_STRUCTURE_GROUP);
      item.setLineCode(isRolledUp ? puc.getRollupStructureGroupCode() : puc.getStructureGroupCode());
      item.setLineCodeDescription(isRolledUp ? puc.getRollupStructureGroupCodeDescription() : puc.getStructureGroupCodeDescription());
    } else {
      item.setType(ProductiveUnitFormLine.TYPE_INVENTORY_ITEM);
      item.setLineCode(isRolledUp ? puc.getRollupInventoryItemCode() : puc.getInventoryItemCode());
      item.setLineCodeDescription(isRolledUp ? puc.getRollupInventoryItemCodeDescription() : puc.getInventoryItemCodeDescription());
    }

    if(ParticipantDataSrcCodes.CRA.equals(puc.getParticipantDataSrcCode())) {
      item.setOnFarmAcres(year, StringUtils.formatDouble(puc.getOnFarmAcres(), df));
      item.setUnseedableAcres(year, StringUtils.formatDouble(puc.getUnseedableAcres(), df));
      item.setMultiStageCommodityCode(puc.getMultiStageCommodityCode());
    }
    
    if(setAdjustedValues) {
      record.setAdjusted(year, StringUtils.formatDouble(puc.getTotalProductiveCapacityAmount(), df));
    }

    record.setAdjustmentId(year, puc.getAdjProductiveUnitCapacityId());
    record.setAdjustment(year, StringUtils.formatDouble(puc.getAdjAmount(), df));
    record.setAdjustmentUser(year, StringUtils.formatUserIdForDisplay(puc.getAdjustedByUserId()));
    record.setRevisionCount(year, puc.getRevisionCount());

    record.setReportedId(year, puc.getReportedProductiveUnitCapacityId());
    record.setCra(year, StringUtils.formatDouble(puc.getReportedAmount(), df));
    record.setParticipantDataSrcCode(participantDataSrcCode);
    
    if(isProgramYear) {
      // Make sure we create an entry in the record map for all data sources
      List<String> participantDataSrcCodeList = ParticipantDataSrcCodes.getCodeList();
      for (String pdsc : participantDataSrcCodeList) {
        item.getRecord(pdsc);
      }
    }
  }
  
  private void populateOperationsForImport(ProductiveUnitsForm form, Scenario scenario) throws ServiceException {

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    List<FarmingOperationImportOption> operations = calculatorService.readOperationsForProductiveUnitsImport(
        scenario.getClient().getParticipantPin(),
        scenario.getYear(),
        scenario.getFarmingYear().getProgramYearVersionNumber());
    
    form.setOperationsForImport(operations);
  }

  private void populateParticipantDataSrcCodes(ProductiveUnitsForm form, Scenario scenario) {
    
    Map<String, List<String>> participantDataSrcCodesByYear = new HashMap<>();
    
    // For Combined Farms, in Combined Farm view,
    // set participantDataSrcCode to NONE for reference years
    // because the user can only switch between participantDataSrcCodes
    // for the program year. For a Combined Farm the selected
    // participantDataSrcCode may be different so this avoids a conflict.
    for (Integer year: scenario.getRequiredReferenceYears()) {
      ReferenceScenario refScenario = scenario.getReferenceScenarioByYear(year);
      
      String participantDataSrcCode;
      if(refScenario == null // No data for this reference year
          || (scenario.isInCombinedFarm() && form.isWholeFarmView())) {
        participantDataSrcCode = ParticipantDataSrcCodes.NONE;
      } else {
        participantDataSrcCode = refScenario.getParticipantDataSrcCode();
      }
      
      List<String> participantDataSrcCodes = Collections.singletonList(participantDataSrcCode);
      participantDataSrcCodesByYear.put(year.toString(), participantDataSrcCodes);
    }
    
    List<String> participantDataSrcCodes;
    if(scenario.isInCombinedFarm() && form.isWholeFarmView()) {
      participantDataSrcCodes = Collections.singletonList(ParticipantDataSrcCodes.NONE);
    } else {
      participantDataSrcCodes = ParticipantDataSrcCodes.getCodeList();
    }
    String programYearString = scenario.getYear().toString();
    participantDataSrcCodesByYear.put(programYearString, participantDataSrcCodes);
    
    form.setParticipantDataSrcCodesByYear(participantDataSrcCodesByYear);
  }


  /**
   * @param request request
   * @param form form
   * @param scenario Scenario
   * @return boolean
   * @throws Exception On Exception
   */
  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean readOnly = super.isReadOnly(request, form, scenario);

    boolean wholeFarmView = form.isWholeFarmView();

    if(!readOnly && !wholeFarmView) {
      readOnly = false;
    } else {
      readOnly = true;
    }
    
    return readOnly;
  }

}
