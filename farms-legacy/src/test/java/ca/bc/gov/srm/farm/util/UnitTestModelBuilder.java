package ca.bc.gov.srm.farm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;

/**
 * common test code
 */
public final class UnitTestModelBuilder {
	
	/** empty private constructor. */
	private UnitTestModelBuilder() {
	}
	
	private static Map<String, Map<Integer, BasePricePerUnit>> bpuMap = new HashMap<>();
	
	static {
	  // Populate bpuMap
	  
    get18_BPU(2019);
    get18_BPU(2020);
    get18_BPU(2021);
    get18_BPU(2022);
    get18_BPU(2023);
    get18_BPU(2024);
    
	  get4826_2018_BPU();
	  get5062_2017_BPU();
	  get5064_2017_BPU();
	  get5062_2018_BPU();
	  get5064_2018_BPU();
	  get5002_2024_BPU();
	  get104_2018_BPU();
	  get105_2018_BPU();
	  get106_2018_BPU();
	  get171_2018_BPU();
	  get300_2024_BPU();
	  get5574_2018_BPU();
	  get5574_2024_BPU();
	  get5584_2018_BPU();
	  get5584_2024_BPU();
	  get5564_2024_BPU();
	}
	
  public static BasePricePerUnit buildBpu(String inventoryItemCode, String structureGroupCode, int programYear,
      double[] margins, double[] expenses) {
    BasePricePerUnit basePricePerUnit = new BasePricePerUnit();
    final int numberOfBpuYears = 6;
    int year = programYear - numberOfBpuYears;
    basePricePerUnit.setInventoryCode(inventoryItemCode);
    basePricePerUnit.setStructureGroupCode(structureGroupCode);
    basePricePerUnit.setRevisionCount(1);
    List<BasePricePerUnitYear> basePricePerUnitYears = new ArrayList<>();
    for(int i = 0; i < 6; i++) {
      basePricePerUnitYears.add(buildBpuYear(year++, margins[i], expenses[i]));
    }
    basePricePerUnit.setBasePricePerUnitYears(basePricePerUnitYears);
    
    String code;
    Map<Integer, BasePricePerUnit> programYearBpuMap;
    if(inventoryItemCode != null) {
      code = inventoryItemCode;
    } else {
      code = structureGroupCode;
    }
    programYearBpuMap = bpuMap.get(code);
    if(programYearBpuMap == null) {
      programYearBpuMap = new HashMap<>();
      bpuMap.put(code, programYearBpuMap);
    }
    
    programYearBpuMap.put(programYear, basePricePerUnit);
    
    return basePricePerUnit;
  }

  public static BasePricePerUnitYear buildBpuYear(int year, double margin, double expense) {
    BasePricePerUnitYear bpuYear = new BasePricePerUnitYear();
    bpuYear.setYear(year);
    bpuYear.setMargin(margin);
    bpuYear.setExpense(expense);
    bpuYear.setRevisionCount(1);
    return bpuYear;
  }

  //-------------------- Inventory Item Code BPUs ---------------------------------------
  
  public static BasePricePerUnit get4826_2018_BPU() {
    final String inventoryItemCode = "4826";
    int programYear = 2018;
    double[] margins = {3844.19, 2513.74, 869.12, 1595.71, 2691.9, 0.01};
    double[] expenses = {4709.92, 4967.52, 5156.48, 4537.09, 4936.05, 5990.48};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5062_2017_BPU() {
    final String inventoryItemCode = "5062";
    int programYear = 2018;
    double[] margins = {2541.94, -415.45, 1559.51, 4272.63, 83.62, -1170.83};
    double[] expenses = {5559.34, 5637.05, 5991.83, 6125.62, 5975.6, 6168.87};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }

  public static BasePricePerUnit get5064_2017_BPU() {
    final String inventoryItemCode = "5064";
    int programYear = 2018;
    double[] margins = {4958.06, 90.16, 3422.42, 7872.97, 974.52, 0.01};
    double[] expenses = {8351.19, 8488.2, 8983.36, 9209.86, 8979.91, 9230.44};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get18_BPU(int programYear) {
    final String inventoryItemCode = "18";
    double[] margins = {0, 0, 0, 0, 0, 0};
    double[] expenses = {0, 0, 0, 0, 0, 0};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5062_2018_BPU() {
    final String inventoryItemCode = "5062";
    int programYear = 2018;
    double[] margins = {3169.78, 811.32, 2796.05, 5562.83, 2115.65, 931.01};
    double[] expenses = {4841.2, 4779.94, 5196.91, 5358.95, 4921.94, 4607.19};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5064_2018_BPU() {
    final String inventoryItemCode = "5064";
    int programYear = 2018;
    double[] margins = {6658.4, 2819.93, 6082.83, 10625.93, 4901.56, 3007.16};
    double[] expenses = {6502.49, 6365.7, 7048.47, 7316.99, 6660.21, 6091.32};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5002_2024_BPU() {
    final String inventoryItemCode = "5002";
    int programYear = 2024;
    double[] margins = {4591.43, 6334.49, 8189.93, 5172.07, 3223.91, -269.93};
    double[] expenses = {5892.58, 5741.19, 5438.62, 5270.21, 6373.82, 6329.85};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5574_2018_BPU() {
    final String inventoryItemCode = "5574";
    int programYear = 2018;
    double[] margins = {0.01, 0.01, 23.5, 170.65, 156.79, 65.27};
    double[] expenses = {143.46, 139.87, 144.45, 133.45, 121.6, 119.62};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5574_2024_BPU() {
    final String inventoryItemCode = "5574";
    int programYear = 2024;
    double[] margins = {0.01, 0.01, 23.5, 170.65, 156.79, 65.27};
    double[] expenses = {143.46, 139.87, 144.45, 133.45, 121.6, 119.62};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5584_2018_BPU() {
    final String inventoryItemCode = "5584";
    int programYear = 2018;
    double[] margins = {0.01, 0.01, 23.5, 170.65, 156.79, 65.27};
    double[] expenses = {143.46, 139.87, 144.45, 133.45, 121.6, 119.62};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5584_2024_BPU() {
    final String inventoryItemCode = "5584";
    int programYear = 2024;
    double[] margins = {0.01, 0.01, 23.5, 170.65, 156.79, 65.27};
    double[] expenses = {143.46, 139.87, 144.45, 133.45, 121.6, 119.62};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get5564_2024_BPU() {
    final String inventoryItemCode = "5564";
    int programYear = 2024;
    double[] margins = {0.01, 0.01, 23.5, 170.65, 156.79, 65.27};
    double[] expenses = {143.46, 139.87, 144.45, 133.45, 121.6, 119.62};
    
    return buildBpu(inventoryItemCode, null, programYear, margins, expenses);
  }
  
  
  //-------------------- Structure Group Code BPUs ---------------------------------------
  
  public static BasePricePerUnit get104_2018_BPU() {
    final String structureGroupCode = "104";
    int programYear = 2018;
    double[] margins = {369.46, 250.75, 661.53, 1000.72, 587.89, 583.41};
    double[] expenses = {423.79, 489.5, 499.88, 459.0, 441.09, 454.64};
    
    return buildBpu(null, structureGroupCode, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get105_2018_BPU() {
    final String structureGroupCode = "105";
    int programYear = 2018;
    double[] margins = {0.01, 0.01, 473.31, 84.14, 0.01, 283.13};
    double[] expenses = {1212.89, 1186.29, 1224.35, 1986.1, 1957.99, 1279.39};
    
    return buildBpu(null, structureGroupCode, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get106_2018_BPU() {
    final String structureGroupCode = "106";
    int programYear = 2018;
    double[] margins = {0.01, 36.93, 26.48, 18.45, 156.06, 146.0};
    double[] expenses = {1548.09, 1538.82, 2023.51, 2446.89, 1871.75, 1897.59};
    
    return buildBpu(null, structureGroupCode, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get171_2018_BPU() {
    final String structureGroupCode = "171";
    int programYear = 2018;
    double[] margins = {231.71, 127.03, 0.01, 585.12, 442.9, 386.35};
    double[] expenses = {1262.66, 1232.14, 1671.79, 2144.74, 1570.87, 1538.65};
    
    return buildBpu(null, structureGroupCode, programYear, margins, expenses);
  }
  
  public static BasePricePerUnit get300_2024_BPU() {
    final String structureGroupCode = "300";
    int programYear = 2024;
    double[] margins = {537.75, 524.11, 493.46, 432.95, 368.92, 786.53};
    double[] expenses = {480.54, 489.1, 500.55, 582.38, 757.06, 817.44};
    
    return buildBpu(null, structureGroupCode, programYear, margins, expenses);
  }

  public static void removeReferenceYear(Scenario scenario, int referenceYear) {
    {
      // Remove the 2013 reference scenario so that there are only 4 reference years.
      
      for(Iterator<ReferenceScenario> iterator = scenario.getReferenceScenarios().iterator(); iterator.hasNext(); ) {
        ReferenceScenario refScenario = iterator.next();
        if(refScenario.getYear() == referenceYear) {
          iterator.remove();
          break;
        }
      }
    }
  }

  public static void setBpuLeadInd(Scenario scenario, int year, boolean bpuLeadInd) {
    scenario.getReferenceScenarioByYear(year).getFarmingYear().getMarginTotal().setBpuLeadInd(bpuLeadInd);
  }

  public static IncomeExpense buildIncome(int lineItemCode, double amount, String description, boolean eligible, String commodityTypeCode, int year) {
    return buildIncomeExpense(false, lineItemCode, amount, description, eligible, commodityTypeCode, year);
  }
  
  public static IncomeExpense buildExpense(int lineItemCode, double amount, String description, boolean eligible, String commodityTypeCode, int year) {
    return buildIncomeExpense(true, lineItemCode, amount, description, eligible, commodityTypeCode, year);
  }
  
  public static IncomeExpense buildIncomeNoValue(int lineItemCode, String description, boolean eligible, String commodityTypeCode) {
    return buildIncomeExpense(false, lineItemCode, null, description, eligible, commodityTypeCode, null);
  }
  
  public static IncomeExpense buildExpenseNoValue(int lineItemCode, String description, boolean eligible, String commodityTypeCode) {
    return buildIncomeExpense(true, lineItemCode, null, description, eligible, commodityTypeCode, null);
  }

  public static IncomeExpense buildIncomeExpense(Boolean expense, int lineItemCode, Double amount, String description, boolean eligible, String commodityTypeCode, Integer year) {
    LineItem lineItem = new LineItem();
    lineItem.setCommodityTypeCode(commodityTypeCode);
    lineItem.setLineItem(lineItemCode);
    lineItem.setDescription(description);
    lineItem.setProgramYear(year);
    lineItem.setIsEligible(eligible);
    lineItem.setIsEligibleRefYears(false);
    lineItem.setIsYardage(isYardage(lineItemCode));
    lineItem.setIsContractWork(isContractWork(lineItemCode));
    lineItem.setIsManualExpense(false); // there aren't any
    lineItem.setIsProgramPayment(isProgramPayment(lineItemCode));
    lineItem.setIsSupplyManagedCommodity(isSupplyManagedCommodity(lineItemCode));
    
    IncomeExpense incomeExpense = new IncomeExpense();
    incomeExpense.setIsExpense(expense);
    incomeExpense.setLineItem(lineItem);
    incomeExpense.setReportedAmount(amount);
    return incomeExpense;
  }

  private static Boolean isYardage(int lineItemCode) {
    Integer[] codes = {243, 246, 572, 573, 576, 577};
    return Arrays.asList(codes).contains(lineItemCode);
  }
  
  private static Boolean isProgramPayment(int lineItemCode) {
    Integer[] codes = {
        412,426,457,468,469,473,477,479,480,481,482,483,484,485,486,487,488,489,491,
        492,493,494,496,497,498,551,552,564,569,578,579,580,582,583,588,593,594,595,
        597,598,599,600,608,609,610,611,612,613,614,615,616,617,618,621,622,623,624,
        625,626,627,682,683,699};
    return Arrays.asList(codes).contains(lineItemCode);
  }
  
  private static Boolean isSupplyManagedCommodity(int lineItemCode) {
    Integer[] codes = {319,334,342,343,344,360,361,362,365,682,682,683,683};
    return Arrays.asList(codes).contains(lineItemCode);
  }
  
  private static Boolean isContractWork(int lineItemCode) {
    return lineItemCode == 9601;
  }

  public static ProductiveUnitCapacity buildProductiveUnit(String inventoryItemCode, String inventoryItemCodeDescription,
      String structureGroupCode, String structureGroupCodeDescription, Double amount, BasePricePerUnit bpu) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setInventoryItemCode(inventoryItemCode);
    puc.setInventoryItemCodeDescription(inventoryItemCodeDescription);
    puc.setStructureGroupCode(structureGroupCode);
    puc.setStructureGroupCodeDescription(structureGroupCodeDescription);
    puc.setReportedAmount(amount);
    puc.setBasePricePerUnit(bpu);
    puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return puc;
  }

  public static FarmingOperation buildFarmingOperation(String schedule, int operationNumber, double partnershipPercent) {
    FarmingOperation fo = new FarmingOperation();
    fo.setSchedule(schedule);
    fo.setOperationNumber(operationNumber);
    fo.setPartnershipPercent(partnershipPercent);
    return fo;
  }

  public static void addIncomeExpenseForAllYears(Scenario scenario, String schedule, IncomeExpense baseIE, Double[] values) {
    final int minYear = scenario.getYear() - 5;
    int valueIndex = 0;
    for(int curYear = minYear; curYear <= scenario.getYear(); curYear++) {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(curYear);
      Double curValue = values[valueIndex];
      
      if(curValue != null) { // only create the incomeExpense if there is a value
        if(referenceScenario == null || referenceScenario.getFarmingYear() == null) {
          throw new IllegalStateException("Cannot set incomeExpense value for a reference year that does not exist. Year: " + curYear);
        }
        FarmingYear farmingYear = referenceScenario.getFarmingYear();
        FarmingOperation farmingOperation = farmingYear.getFarmingOperationBySchedule(schedule);
        if(farmingOperation == null) {
          throw new IllegalStateException("Cannot set incomeExpense value for a farming operation that does not exist. Year: " + curYear + ", Operation: " + schedule);
        }
        
        List<IncomeExpense> incomeExpenses = farmingOperation.getIncomeExpenses();
        if(incomeExpenses == null) {
          incomeExpenses = new ArrayList<>();
        }
        
        IncomeExpense incomeExpense = buildIncomeExpense(
            baseIE.getIsExpense(),
            baseIE.getLineItem().getLineItem(),
            curValue, baseIE.getLineItem().getDescription(),
            baseIE.getLineItem().getIsEligible(),
            baseIE.getLineItem().getCommodityTypeCode(), curYear);
        incomeExpenses.add(incomeExpense);
        
        farmingOperation.setIncomeExpenses(incomeExpenses);
      }
      
      valueIndex++;
    }
  }
  
  public static void addProductiveUnitForAllYears(Scenario scenario, String schedule, ProductiveUnitCapacity basePuc, Double[] values) {
    final int minYear = scenario.getYear() - 5;
    int valueIndex = 0;
    for(int curYear = minYear; curYear <= scenario.getYear(); curYear++) {
      ReferenceScenario referenceScenario = scenario.getReferenceScenarioByYear(curYear);
      Double curValue = values[valueIndex];
      
      if(curValue != null) { // only create the incomeExpense if there is a value
        if(referenceScenario == null || referenceScenario.getFarmingYear() == null) {
          throw new IllegalStateException("Cannot set incomeExpense value for a reference year that does not exist. Year: " + curYear);
        }
        FarmingYear farmingYear = referenceScenario.getFarmingYear();
        FarmingOperation farmingOperation = farmingYear.getFarmingOperationBySchedule(schedule);
        if(farmingOperation == null) {
          throw new IllegalStateException("Cannot set incomeExpense value for a farming operation that does not exist. Year: " + curYear + ", Operation: " + schedule);
        }
        
        List<ProductiveUnitCapacity> productiveUnits = farmingOperation.getProductiveUnitCapacities();
        
        ProductiveUnitCapacity productiveUnit =
            buildProductiveUnit(basePuc.getInventoryItemCode(), basePuc.getInventoryItemCodeDescription(),
            basePuc.getStructureGroupCode(), basePuc.getStructureGroupCodeDescription(),
            curValue, basePuc.getBasePricePerUnit());
        productiveUnits.add(productiveUnit);
        
        farmingOperation.setProductiveUnitCapacities(productiveUnits);
      }
      
      valueIndex++;
    }
  }

  public static void setBpus(Scenario scenario) {
    
    Integer programYear = scenario.getYear();
    Collection<Scenario> scenarios;
    
    if (scenario.isInCombinedFarm()) {
      CombinedFarm combinedFarm = scenario.getCombinedFarm();
      scenarios = combinedFarm.getScenarios();
    } else {
        scenarios = Collections.singletonList(scenario);
    }
  
    scenarios.stream()
        .flatMap(scn -> scn.getAllScenarios().stream())
        .filter(refScenario -> refScenario.getFarmingYear() != null)
        .filter(refScenario -> refScenario.getFarmingYear().getFarmingOperations() != null)
        .flatMap(refScenario -> refScenario.getFarmingYear().getFarmingOperations().stream())
        .flatMap(
            farmingOp -> Stream.concat(
                farmingOp.getAllRolledUpProductiveUnitCapacities().stream(),
                farmingOp.getAllProductiveUnitCapacities().stream()
                )
            )
        .filter(puc -> puc.getBasePricePerUnit() == null)
        .filter(puc -> bpuMap.get(puc.getCode()) != null)
        .filter(puc -> bpuMap.get(puc.getCode()).get(programYear) != null)
        .forEach(puc -> {
            puc.setBasePricePerUnit(bpuMap.get(puc.getCode()).get(programYear));
        });
  }

}
