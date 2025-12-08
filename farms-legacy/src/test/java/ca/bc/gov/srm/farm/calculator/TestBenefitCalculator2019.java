/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import static ca.bc.gov.srm.farm.util.UnitTestModelBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.LineItemCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public class TestBenefitCalculator2019 {
  
  private Logger logger = LoggerFactory.getLogger(TestBenefitCalculator2019.class);
  
  @BeforeAll
  protected static void setup() throws ServiceException {
    TestUtils.standardTestSetUp();
  }
  
  @Test
  public final void testBenefitCalculation() throws Exception {
    int programYear = 2019;
    Scenario scenario = buildScenario(programYear);
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages messages = benefitService.calculateBenefit(scenario, "UNIT_TEST", false, true, true);

    logger.info("Calculation errors:");
    int messageCount = 0;
    for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
      ActionMessage msg = mi.next();
      logger.debug("Message: [" + msg.getKey() + "], values: [" + msg.getValues() + "]");
      messageCount++;
    }
    
    if(messageCount > 0) {
      fail("Unexpected errors calculating the benefit.");
    }
    
    // PROGRAM YEAR
    {
      MarginTotal marginTotal = scenario.getFarmingYear().getMarginTotal();
      assertEquals("115923.0", String.valueOf(marginTotal.getTotalAllowableIncome()));
      assertEquals("49194.0", String.valueOf(marginTotal.getTotalAllowableExpenses()));
      assertEquals("66729.0", String.valueOf(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", String.valueOf(marginTotal.getTotalAccrualAdjs()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsLvstckInventory()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsPayables()));
      assertEquals("66729.0", String.valueOf(marginTotal.getProductionMargAccrAdjs()));
      assertEquals("2774.0", String.valueOf(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", String.valueOf(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("49194.0", String.valueOf(marginTotal.getExpenseAccrualAdjs()));
    }
    
    // PROGRAM YEAR MINUS 1
    {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
      MarginTotal marginTotal = referenceScenario.getFarmingYear().getMarginTotal();
      assertEquals("139690.0", String.valueOf(marginTotal.getTotalAllowableIncome()));
      assertEquals("69210.0", String.valueOf(marginTotal.getTotalAllowableExpenses()));
      assertEquals("70480.0", String.valueOf(marginTotal.getUnadjustedProductionMargin()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsCropInventory()));
      assertEquals("-2347.15", String.valueOf(MathUtils.roundCurrency(marginTotal.getAccrualAdjsLvstckInventory())));
      assertEquals("-4000.0", String.valueOf(marginTotal.getAccrualAdjsPurchasedInputs()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsReceivables()));
      assertEquals("0.0", String.valueOf(marginTotal.getAccrualAdjsPayables()));
      assertEquals("-6347.15", String.valueOf(MathUtils.roundCurrency(marginTotal.getTotalAccrualAdjs())));
      assertEquals("64132.85", String.valueOf(MathUtils.roundCurrency(marginTotal.getProductionMargAccrAdjs())));
      assertEquals("23240.0", String.valueOf(marginTotal.getTotalUnallowableIncome()));
      assertEquals("0.0", String.valueOf(marginTotal.getTotalUnallowableExpenses()));
      assertEquals("73210.0", String.valueOf(marginTotal.getExpenseAccrualAdjs()));
      assertEquals("1.24", String.valueOf(MathUtils.roundCurrency(marginTotal.getFarmSizeRatio())));
      assertEquals("1.19", String.valueOf(MathUtils.roundCurrency(marginTotal.getExpenseFarmSizeRatio())));
      
      assertEquals("84205.8", String.valueOf(marginTotal.getProductiveValue()));
    }
    
    Benefit benefit = scenario.getBenefit();
    assertEquals("4565.0", String.valueOf(benefit.getTotalBenefit()));
    assertEquals("4565.0", String.valueOf(benefit.getEnhancedTotalBenefit()));
  }

  private Scenario buildScenario(int programYear) throws Exception {
    Scenario scenario = new Scenario();
    scenario.setScenarioCategoryCode(ScenarioCategoryCodes.INTERIM);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setIsInCombinedFarmInd(false);
    
    Client client = new Client();
    scenario.setClient(client);
    client.setParticipantPin(23179765);
    
    {
      scenario.setYear(programYear);
      FarmingYear programYearFarmingYear = new FarmingYear();
      scenario.setFarmingYear(programYearFarmingYear);
      programYearFarmingYear.setMunicipalityCode("41");
      programYearFarmingYear.setMunicipalityCodeDescription("Cariboo");
      programYearFarmingYear.setIsLateParticipant(false);
      programYearFarmingYear.setIsCashMargins(false);
      
      {
        Benefit benefit = new Benefit();
        programYearFarmingYear.setBenefit(benefit);
        benefit.setStructuralChangeMethodCode(StructuralChangeCodes.RATIO);
        benefit.setExpenseStructuralChangeMethodCode(StructuralChangeCodes.RATIO);
      }
      List<FarmingOperation> farmingOperations = new ArrayList<>();
      
      {
        FarmingOperation farmingOperation = buildFarmingOperation("A", 1, 1.0);
        int fiscalStartYear = programYear - 1;
        int fiscalEndYear = programYear;

        farmingOperation.setFiscalYearStart(DateUtils.getTimestamp(fiscalStartYear, Calendar.SEPTEMBER, 1, 0, 0, 0, 0));
        farmingOperation.setFiscalYearEnd(DateUtils.getTimestamp(fiscalEndYear, Calendar.AUGUST, 31, 0, 0, 0, 0));
        farmingOperations.add(farmingOperation);
        
        List<LivestockItem> livestockItems = new ArrayList<>();
        farmingOperation.setLivestockItems(livestockItems);
      }
      programYearFarmingYear.setFarmingOperations(farmingOperations);
    }
    
    //=============================================================================================================
    
    {
      List<ReferenceScenario> referenceScenarios = new ArrayList<>();
      
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(programYear - 1, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
        referenceScenario.setIsDeemedFarmingYear(true);
        FarmingYear farmingYear = new FarmingYear();
        referenceScenario.setFarmingYear(farmingYear);
        
        List<FarmingOperation> farmingOperations = new ArrayList<>();
        {
          FarmingOperation farmingOperation = buildFarmingOperation("A", 1, 1.0);
          farmingOperations.add(farmingOperation);
          
          addProgramYearMinus1OperationALivestock(farmingOperation);
          addProgramYearMinus1OperationAAccruals(farmingOperation);
        }
        farmingYear.setFarmingOperations(farmingOperations);
      }
      
      //=============================================================================================================
      
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(programYear - 2, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
        referenceScenario.setIsDeemedFarmingYear(true);
        FarmingYear farmingYear = new FarmingYear();
        referenceScenario.setFarmingYear(farmingYear);
        
        List<FarmingOperation> farmingOperations = new ArrayList<>();
        {
          FarmingOperation farmingOperation = buildFarmingOperation("A", 1, 1.0);
          farmingOperations.add(farmingOperation);
          
          addProgramYearMinus2OperationALivestock(farmingOperation);
          addProgramYearMinus2OperationAAccruals(farmingOperation);
        }
        farmingYear.setFarmingOperations(farmingOperations);
      }
      
      //=============================================================================================================
      
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(programYear - 3, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
        referenceScenario.setIsDeemedFarmingYear(true);
        FarmingYear farmingYear = new FarmingYear();
        referenceScenario.setFarmingYear(farmingYear);
        
        List<FarmingOperation> farmingOperations = new ArrayList<>();
        {
          FarmingOperation farmingOperation = buildFarmingOperation("A", 1, 1.0);
          farmingOperations.add(farmingOperation);
          
          addProgramYearMinus3OperationALivestock(farmingOperation);
          addProgramYearMinus3OperationAAccruals(farmingOperation);
        }
        farmingYear.setFarmingOperations(farmingOperations);
      }
      
      //=============================================================================================================
      
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(programYear - 4, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
        referenceScenario.setIsDeemedFarmingYear(true);
        FarmingYear farmingYear = new FarmingYear();
        referenceScenario.setFarmingYear(farmingYear);
        
        List<FarmingOperation> farmingOperations = new ArrayList<>();
        {
          FarmingOperation farmingOperation = buildFarmingOperation("A", 1, 1.0);
          farmingOperations.add(farmingOperation);
          
          addProgramYearMinus4OperationALivestock(farmingOperation);
          addProgramYearMinus4OperationAAccruals(farmingOperation);
        }
        farmingYear.setFarmingOperations(farmingOperations);
      }
      
      //=============================================================================================================
      
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(programYear - 5, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
        referenceScenario.setIsDeemedFarmingYear(true);
        FarmingYear farmingYear = new FarmingYear();
        referenceScenario.setFarmingYear(farmingYear);
        
        List<FarmingOperation> farmingOperations = new ArrayList<>();
        {
          FarmingOperation farmingOperation = buildFarmingOperation("A", 1, 1.0);
          farmingOperations.add(farmingOperation);
          
          addProgramYearMinus5OperationALivestock(farmingOperation);
          addProgramYearMinus5OperationAAccruals(farmingOperation);
        }
        farmingYear.setFarmingOperations(farmingOperations);
      }
      scenario.setReferenceScenarios(referenceScenarios);
      
      buildIncomeAndExpenses(scenario);
      buildProductiveUnits(scenario);
    }
    
    return scenario;
  }
  

  private ReferenceScenario buildReferenceScenario(int year, boolean bpuLeadInd) {
    ReferenceScenario referenceScenario = new ReferenceScenario();
    referenceScenario.setYear(year);
    referenceScenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    FarmingYear farmingYear = new FarmingYear();
    farmingYear.setIsCashMargins(false);
    referenceScenario.setFarmingYear(farmingYear);
    {
      MarginTotal marginTotal = new MarginTotal();
      farmingYear.setMarginTotal(marginTotal);
      marginTotal.setBpuLeadInd(bpuLeadInd);
    }
    return referenceScenario;
  }


  private void buildIncomeAndExpenses(Scenario scenario) {
    String schedule = "A";

    //
    // INCOME
    // 
    // Eligible 
    {
      IncomeExpense incomeExpense = buildIncomeNoValue(706, null, true, CommodityTypeCodes.CATTLE);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {94478.00, 154756.00, 149722.00, 93832.00, 137772.00, 115923.00});
    }
    {
      IncomeExpense incomeExpense = buildIncomeNoValue(7574, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {1477.00, 2229.00, 936.00, 727.00, 1918.00, null});
    }
    // Ineligible 
    {
      IncomeExpense incomeExpense = buildIncomeNoValue(9540, null, false, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {3762.00, 2950.00, 1884.00, 1009.00, 23240.00, 2774.00});
    }

    //
    // EXPENSES
    //
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(LineItemCodes.GRAIN_LINE_ITEM, LineItemCodes.GRAIN_LINE_ITEM_DESCRIPTION, true, CommodityTypeCodes.GRAIN);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {2378.00, null, null, null, 12891.00, 3512.00});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(LineItemCodes.FORAGE_LINE_ITEM, LineItemCodes.FORAGE_LINE_ITEM_DESCRIPTION, true, CommodityTypeCodes.FORAGE);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {6116.0, 19441.0, 33245.0, 12690.0, 33610.0, 15000.0});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(722, null, true, CommodityTypeCodes.CATTLE);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {2800.00, 14929.00, 15596.00, 8077.00, 9414.00, 16933.00});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(9661, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {null, 792.00, null, null, null, null});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(9713, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {1038.00, 1165.00, 2566.00, 1452.00, 1114.00, 2741.00});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(9764, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {9078.00, 14037.00, 10207.00, 8232.00, 12181.00, 9442.00});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(9799, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {null, null, null, null, null, 1566.00});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(9801, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {813.00, 0.00, 2000.00, 1000.00, null, null});
    }
    {
      IncomeExpense incomeExpense = buildExpenseNoValue(9802, null, true, CommodityTypeCodes.NULL);
      addIncomeExpenseForAllYears(scenario, schedule, incomeExpense, new Double[] {1570.00, 65.00, null, null, null, null});
    }
    
    
    // Make sure incomeExpenses is not null for any farming operations
    for (ReferenceScenario referenceScenario : scenario.getReferenceScenarios()) {
      List<FarmingOperation> farmingOperations = referenceScenario.getFarmingYear().getFarmingOperations();
      for (FarmingOperation farmingOperation : farmingOperations) {
        List<IncomeExpense> incomeExpenses = farmingOperation.getIncomeExpenses();
        if(incomeExpenses == null) {
          incomeExpenses = new ArrayList<>();
        }
        farmingOperation.setIncomeExpenses(incomeExpenses);
      }
    }
  }

  
  private void buildProductiveUnits(Scenario scenario) {
    int programYear = scenario.getYear();
    String schedule = "A";
    
    {
      ProductiveUnitCapacity productiveUnit = buildProductiveUnit("18", "Pasture Acres", null, null, null, 
          get18_BPU(programYear));
      addProductiveUnitForAllYears(scenario, schedule, productiveUnit, new Double[] {null, 900.0, 900.0, 900.0, 900.0, null});
    }
    {
      ProductiveUnitCapacity productiveUnit = buildProductiveUnit(null, null, "104", "Cattle", null, 
          get104_BPU_Cariboo_Municipality(programYear));
      addProductiveUnitForAllYears(scenario, schedule, productiveUnit, new Double[] {142.0, 137.0, 140.0, 135.0, 130.0, 164.0});
    }
    {
      ProductiveUnitCapacity productiveUnit = buildProductiveUnit("5576", "Hay; Other", null, null, null, 
          get5576_BPU_Cariboo_Municipality(programYear));
      addProductiveUnitForAllYears(scenario, schedule, productiveUnit, new Double[] {150.0, 150.0, 150.0, 150.0, 150.0, 150.0});
    }
    
  }

  
  private BasePricePerUnit get104_BPU_Cariboo_Municipality(int programYear) {
    String structureGroupCode = "104";
    double[] margins = {250.75, 661.53, 1000.72, 587.89, 583.41, 537.75};
    double[] expenses = {489.5, 499.88, 459.0, 441.09, 454.64, 480.54};
    
    return buildBpu(null, structureGroupCode, programYear, margins, expenses);
  }
  
  private BasePricePerUnit get5576_BPU_Cariboo_Municipality(int programYear) {
    String inventoryItemCode = "5576";
    double[] margins = {0.01, 22.24, 180.87, 162.89, 55.75, 115.43};
    double[] expenses = {155.68, 160.27, 154.5, 142.84, 143.12, 151.44};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }


  private void addProgramYearMinus1OperationALivestock(FarmingOperation farmingOperation) {
    List<LivestockItem> livestockItems = new ArrayList<>();
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8000");
      item.setInventoryItemCodeDescription("Beef; Breeding; Bulls");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(8.0);
      item.setReportedQuantityEnd(7.0);
      item.setReportedPriceStart(3100.0);
      item.setReportedPriceEnd(3100.00);
      item.setFmvEnd(3100.00);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8002");
      item.setInventoryItemCodeDescription("Beef; Breeding; Cows");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(150.0);
      item.setReportedQuantityEnd(150.0);
      item.setReportedPriceStart(1675.0);
      item.setReportedPriceEnd(1675.0);
      item.setFmvEnd(1675.00);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8032");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Heifers");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(71.0);
      item.setReportedQuantityEnd(65.0);
      item.setReportedPriceStart(678.65);
      item.setReportedPriceEnd(768.18);
      item.setFmvEnd(768.18);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8034");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Steers");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(60.0);
      item.setReportedQuantityEnd(65.0);
      item.setReportedPriceStart(773.57);
      item.setReportedPriceEnd(848.30);
      item.setFmvEnd(848.30);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8062");
      item.setInventoryItemCodeDescription("Beef; Replacement Heifers");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(23.0);
      item.setReportedQuantityEnd(20.0);
      item.setReportedPriceStart(1850.0);
      item.setReportedPriceEnd(1641.5);
      item.setFmvEnd(1641.5);
      livestockItems.add(item);
    }
    farmingOperation.setLivestockItems(livestockItems);
  }


  private void addProgramYearMinus2OperationALivestock(FarmingOperation farmingOperation) {
    List<LivestockItem> livestockItems = new ArrayList<>();
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8000");
      item.setInventoryItemCodeDescription("Beef; Breeding; Bulls");
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(6.0);
      item.setReportedQuantityEnd(8.0);
      item.setReportedPriceStart(3150.0);
      item.setReportedPriceEnd(3150.00);
      item.setFmvEnd(3150.00);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8002");
      item.setInventoryItemCodeDescription("Beef; Breeding; Cows");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(140.0);
      item.setReportedQuantityEnd(150.0);
      item.setReportedPriceStart(1628.59);
      item.setReportedPriceEnd(1628.59);
      item.setFmvEnd(1628.59);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8032");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(70.0);
      item.setReportedQuantityEnd(71.0);
      item.setReportedPriceStart(681.35);
      item.setReportedPriceEnd(678.65);
      item.setFmvEnd(678.65);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8034");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Steers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(60.0);
      item.setReportedQuantityEnd(60.0);
      item.setReportedPriceStart(742.00);
      item.setReportedPriceEnd(773.57);
      item.setFmvEnd(773.57);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8062");
      item.setInventoryItemCodeDescription("Beef; Replacement Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(25.0);
      item.setReportedQuantityEnd(23.0);
      item.setReportedPriceStart(1640.78);
      item.setReportedPriceEnd(1850.0);
      item.setFmvEnd(1850.0);
      livestockItems.add(item);
    }
    farmingOperation.setLivestockItems(livestockItems);
  }


  private void addProgramYearMinus3OperationALivestock(FarmingOperation farmingOperation) {
    List<LivestockItem> livestockItems = new ArrayList<>();
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8000");
      item.setInventoryItemCodeDescription("Beef; Breeding; Bulls");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(7.0);
      item.setReportedQuantityEnd(6.0);
      item.setReportedPriceStart(3200.0);
      item.setReportedPriceEnd(3200.0);
      item.setFmvEnd(3200.0);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8002");
      item.setInventoryItemCodeDescription("Beef; Breeding; Cows");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(137.0);
      item.setReportedQuantityEnd(140.0);
      item.setReportedPriceStart(1580.06);
      item.setReportedPriceEnd(1580.06);
      item.setFmvEnd(1580.06);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8032");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(65.0);
      item.setReportedQuantityEnd(70.0);
      item.setReportedPriceStart(1052.63);
      item.setReportedPriceEnd(681.35);
      item.setFmvEnd(681.35);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8034");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Steers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(60.0);
      item.setReportedQuantityEnd(60.0);
      item.setReportedPriceStart(1186.92);
      item.setReportedPriceEnd(742.0);
      item.setFmvEnd(742.0);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8062");
      item.setInventoryItemCodeDescription("Beef; Replacement Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(21.0);
      item.setReportedQuantityEnd(25.0);
      item.setReportedPriceStart(2600.00);
      item.setReportedPriceEnd(1640.78);
      item.setFmvEnd(1640.78);
      livestockItems.add(item);
    }
    farmingOperation.setLivestockItems(livestockItems);
  }


  private void addProgramYearMinus4OperationALivestock(FarmingOperation farmingOperation) {
    List<LivestockItem> livestockItems = new ArrayList<>();
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8000");
      item.setInventoryItemCodeDescription("Beef; Breeding; Bulls");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(6.0);
      item.setReportedQuantityEnd(7.0);
      item.setReportedPriceStart(3350.0);
      item.setReportedPriceEnd(3350.0);
      item.setFmvEnd(3350.0);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8002");
      item.setInventoryItemCodeDescription("Beef; Breeding; Cows");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(142.0);
      item.setReportedQuantityEnd(137.0);
      item.setReportedPriceStart(2108.33);
      item.setReportedPriceEnd(2108.33);
      item.setFmvEnd(2108.33);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8032");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(75.0);
      item.setReportedQuantityEnd(65.0);
      item.setReportedPriceStart(928.34);
      item.setReportedPriceEnd(1052.63);
      item.setFmvEnd(1052.63);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8034");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Steers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(55.0);
      item.setReportedQuantityEnd(60.0);
      item.setReportedPriceStart(1034.53);
      item.setReportedPriceEnd(1186.92);
      item.setFmvEnd(1186.92);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8062");
      item.setInventoryItemCodeDescription("Beef; Replacement Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(25.0);
      item.setReportedQuantityEnd(21.0);
      item.setReportedPriceStart(1575.00);
      item.setReportedPriceEnd(2600.00);
      item.setFmvEnd(2600.00);
      livestockItems.add(item);
    }
    farmingOperation.setLivestockItems(livestockItems);
  }


  private void addProgramYearMinus5OperationALivestock(FarmingOperation farmingOperation) {
    List<LivestockItem> livestockItems = new ArrayList<>();
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8000");
      item.setInventoryItemCodeDescription("Beef; Breeding; Bulls");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(6.0);
      item.setReportedQuantityEnd(6.0);
      item.setReportedPriceStart(2950.0);
      item.setReportedPriceEnd(2950.0);
      item.setFmvEnd(2950.0);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8002");
      item.setInventoryItemCodeDescription("Beef; Breeding; Cows");
      item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      item.setIsMarketCommodity(false);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(145.0);
      item.setReportedQuantityEnd(142.0);
      item.setReportedPriceStart(1663.33);
      item.setReportedPriceEnd(1663.33);
      item.setFmvEnd(1663.33);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8032");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(65.0);
      item.setReportedQuantityEnd(75.0);
      item.setReportedPriceStart(512.75);
      item.setReportedPriceEnd(928.34);
      item.setFmvEnd(928.34);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8034");
      item.setInventoryItemCodeDescription("Beef; Feeder 301 - 400 lbs; Steers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(77.0);
      item.setReportedQuantityEnd(55.0);
      item.setReportedPriceStart(547.75);
      item.setReportedPriceEnd(1034.53);
      item.setFmvEnd(1034.53);
      livestockItems.add(item);
    }
    {
      LivestockItem item = new LivestockItem();
      item.setInventoryItemCode("8062");
      item.setInventoryItemCodeDescription("Beef; Replacement Heifers");
      item.setIsMarketCommodity(true);
      item.setIsEligible(true);
      item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE_DESCRIPTION);
      item.setReportedQuantityStart(23.0);
      item.setReportedQuantityEnd(25.0);
      item.setReportedPriceStart(1375.0);
      item.setReportedPriceEnd(1375.0);
      item.setFmvEnd(1375.0);
      livestockItems.add(item);
    }
    farmingOperation.setLivestockItems(livestockItems);
  }


  private void addProgramYearMinus1OperationAAccruals(FarmingOperation farmingOperation) {
    List<InputItem> inputItems = new ArrayList<>();
    {
      InputItem item = new InputItem();
      item.setInventoryItemCode("39");
      item.setInventoryItemCodeDescription("Grain (Pellets; Screenings; Silage)");
      item.setReportedStartOfYearAmount(null);
      item.setReportedEndOfYearAmount(6000.0);
      item.setIsEligible(true);
      item.setInventoryClassCode(InventoryClassCodes.INPUT);
      item.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      inputItems.add(item);
    }
    {
      InputItem item = new InputItem();
      item.setInventoryItemCode("264");
      item.setInventoryItemCodeDescription("Forage (Pellets; Silage)");
      item.setReportedStartOfYearAmount(10000.0);
      item.setReportedEndOfYearAmount(null);
      item.setIsEligible(true);
      item.setInventoryClassCode(InventoryClassCodes.INPUT);
      item.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      inputItems.add(item);
    }
    farmingOperation.setInputItems(inputItems);
  }


  private void addProgramYearMinus2OperationAAccruals(FarmingOperation farmingOperation) {
    List<InputItem> inputItems = new ArrayList<>();
    {
      InputItem item = new InputItem();
      item.setInventoryItemCode("264");
      item.setInventoryItemCodeDescription("Forage (Pellets; Silage)");
      item.setReportedStartOfYearAmount(null);
      item.setReportedEndOfYearAmount(10000.0);
      item.setIsEligible(true);
      item.setInventoryClassCode(InventoryClassCodes.INPUT);
      item.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      inputItems.add(item);
    }
    farmingOperation.setInputItems(inputItems);
  }


  private void addProgramYearMinus3OperationAAccruals(FarmingOperation farmingOperation) {
    List<InputItem> inputItems = new ArrayList<>();
    farmingOperation.setInputItems(inputItems);
  }


  private void addProgramYearMinus4OperationAAccruals(FarmingOperation farmingOperation) {
    List<InputItem> inputItems = new ArrayList<>();
    farmingOperation.setInputItems(inputItems);
  }


  private void addProgramYearMinus5OperationAAccruals(FarmingOperation farmingOperation) {
    List<InputItem> inputItems = new ArrayList<>();
    farmingOperation.setInputItems(inputItems);
  }

}
