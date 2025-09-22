package ca.bc.gov.srm.farm.reasonability.revenue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestPoultryEggsRevenueRiskTest {
  private static Logger logger = LoggerFactory.getLogger(TestPoultryEggsRevenueRiskTest.class);
  
  private static final String PRODUCTIVE_UNIT_CODE_EGGS_HATCH = "108";
  private static final String PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR = "Description for Eggs hatch PU.";

  private static final String PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP = "109";
  private static final String PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR = "Description for Eggs consumption PU.";
  
  private static final Integer EGGS_HATCH_LINE_ITEM_1 = 1;
  private static final String EGGS_HATCH_LINE_ITEM_1_DESCR = "Description for Eggs hatch 1 line item.";
  
  private static final Integer EGGS_CONSUMP_LINE_ITEM_1 = 2;
  private static final String  EGGS_CONSUMP_LINE_ITEM_1_DESCR = "Description for Eggs consumption line item 1 description.";

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  private Scenario buildScenarioWithNoCombinedFarm(List<ProductiveUnitCapacity> productiveUnitCapacities) {
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setIsInCombinedFarmInd(false);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    List<FarmingOperation> farmOps = new ArrayList<>();
    FarmingOperation farmOp = new FarmingOperation();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    
    farmOp.setProductiveUnitCapacities(productiveUnitCapacities);

    return scenario;
  }
  
  private Scenario buildScenarioWithCombinedFarm(
      List<ProductiveUnitCapacity> productiveUnitCapacities, 
      List<ProductiveUnitCapacity> productiveUnitCapacities2,
      Double eggHatchFmv, 
      Double eggConsumpFmv) {
    
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setIsInCombinedFarmInd(true);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());

    Scenario scenario2 = new Scenario();
    scenario2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario2.setIsInCombinedFarmInd(true);
    scenario2.setYear(new Integer(2018));

    List<Scenario> scenarios = new ArrayList<>();
    scenarios.add(scenario);
    scenarios.add(scenario2);

    CombinedFarm combinedFarm = new CombinedFarm();
    combinedFarm.setScenarios(scenarios);
    scenario.setCombinedFarm(combinedFarm);
    scenario.setIsInCombinedFarmInd(true);
    scenario2.setCombinedFarm(combinedFarm);
    scenario2.setIsInCombinedFarmInd(true);

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    
    List<FarmingOperation> farmOps = new ArrayList<>();
    FarmingOperation farmOp = new FarmingOperation();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    
    farmOp.setChickenEggsHatchFMVPrice(eggHatchFmv);
    farmOp.setChickenEggsConsumpFMVPrice(eggConsumpFmv);
    farmOp.setProductiveUnitCapacities(productiveUnitCapacities);

    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    
    farmOp2.setChickenEggsHatchFMVPrice(eggHatchFmv);
    farmOp2.setChickenEggsConsumpFMVPrice(eggConsumpFmv);
    farmOp2.setProductiveUnitCapacities(productiveUnitCapacities2);

    return scenario;
  }
  
  @Test
  public void passWithVarLessThanLimit() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(14.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.077", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.154", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
  
  @Test
  public void failWithVarLessThanLimit() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(24.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(25.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(false, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(24), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.846", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(25), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.923", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
  
  @Test
  public void passWithCombinedFarm() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 15.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    List<ProductiveUnitCapacity> productiveUnitCapacities2 = new ArrayList<>();
    productiveUnitCapacities2.add(pu2);
    
    Scenario scenario = buildScenarioWithCombinedFarm(productiveUnitCapacities, productiveUnitCapacities2, eggsHatchFmv, eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(16.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(16), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.067", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.154", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
  
  @Test
  public void failForMissingEggsHatchFMV() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(14.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV,
            pu1.getStructureGroupCode()),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingVariance());
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.154", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
  
  @Test
  public void failForMissingEggsConsumpFMV() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 22.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(25.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV,
            pu2.getStructureGroupCode()),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(25), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(22), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.136", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionVariance());
  }
  
  @Test
  public void failForMissingEggsHatchPU() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(14.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryEggsRevenueRiskSubTest.ERROR_MISSING_HATCH_EGGS_PRODUCTIVE_UNIT_CODE,
            StructureGroupCodes.CHICKEN_EGGS_HATCH),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingVariance());
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.154", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
 
  @Test
  public void failForEggsConsumpMissingPU() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(14.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryEggsRevenueRiskSubTest.ERROR_MISSING_CONSUMP_EGGS_PRODUCTIVE_UNIT_CODE,
            StructureGroupCodes.CHICKEN_EGGS_CONSUMPTION),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.077", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionVariance());
  }
  
  @Test
  public void failForHatchEggsMissingIncome() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(15.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryEggsRevenueRiskSubTest.ERROR_MISSING_HATCH_EGGS_INCOME),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getHatchingVariance());
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.154", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
  
  @Test
  public void failForMissingConsumpEggsIncome() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(14.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryEggsRevenueRiskSubTest.ERROR_MISSING_CONSUMP_EGGS_INCOME),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(false, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(pu1.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.077", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(pu2.getTotalProductiveCapacityAmount(), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals(null, revenueRiskResult.getPoultryEggs().getConsumptionVariance());
  }
  
  @Test
  public void passForMultiplePUItems() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double hatchVarLimit = config.getRevenueRiskPoultryEggsHatchingVariance();
    Double consumpVarLimit = config.getRevenueRiskPoultryEggsConsumptionVariance();
    Double eggsHatchingAveragePerLayer = config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer();
    Double eggsConsumpAveragePerLayer = config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    Double eggsHatchFmv = 13.0;
    Double eggsConsumpFmv = 13.0;
    Double puTotal = 12.0;
    
    ProductiveUnitCapacity pu1 = new ProductiveUnitCapacity();
    pu1.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu1.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu1.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu3 = new ProductiveUnitCapacity();
    pu3.setReportedAmount(puTotal / eggsHatchingAveragePerLayer);
    pu3.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_HATCH);
    pu3.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_HATCH_DESCR);
    pu3.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu2 = new ProductiveUnitCapacity();
    pu2.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu2.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu2.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity pu4 = new ProductiveUnitCapacity();
    pu4.setReportedAmount(puTotal / eggsConsumpAveragePerLayer);
    pu4.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP);
    pu4.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_EGGS_CONSUMP_DESCR);
    pu4.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.add(pu1);
    productiveUnitCapacities.add(pu2);
    productiveUnitCapacities.add(pu3);
    productiveUnitCapacities.add(pu4);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsHatchFMVPrice(eggsHatchFmv);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenEggsConsumpFMVPrice(eggsConsumpFmv);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_HATCHING);
    lineItem1.setLineItem(EGGS_HATCH_LINE_ITEM_1);
    lineItem1.setDescription(EGGS_HATCH_LINE_ITEM_1_DESCR);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(29.0);
    
    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.POULTRY_EGGS_CONSUMPTION);
    lineItem2.setLineItem(EGGS_CONSUMP_LINE_ITEM_1);
    lineItem2.setDescription(EGGS_CONSUMP_LINE_ITEM_1_DESCR);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(30.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
  
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionPass());
    assertEquals(true, revenueRiskResult.getPoultryEggs().getHatchingVariance() < hatchVarLimit);
    assertEquals(true, revenueRiskResult.getPoultryEggs().getConsumptionVariance() < consumpVarLimit);
    assertEquals(hatchVarLimit, revenueRiskResult.getPoultryEggs().getHatchingVarianceLimit());
    assertEquals(consumpVarLimit, revenueRiskResult.getPoultryEggs().getConsumptionVarianceLimit());
    
    assertEquals(new Double(puTotal/eggsHatchingAveragePerLayer * 2), revenueRiskResult.getPoultryEggs().getHatchingLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsHatchingAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getHatchingAverageEggsPerLayer());
    assertEquals(new Double(puTotal * 2), revenueRiskResult.getPoultryEggs().getHatchingEggsTotal());
    assertEquals(new Double(puTotal * 2 / 12), revenueRiskResult.getPoultryEggs().getHatchingEggsDozen());
    assertEquals(new Double(eggsHatchFmv * puTotal * 2 / 12), revenueRiskResult.getPoultryEggs().getHatchingExpectedRevenue());
    assertEquals(new Double(29), revenueRiskResult.getPoultryEggs().getHatchingReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getHatchingEggsDozenPrice());
    assertEquals("0.115", String.valueOf(revenueRiskResult.getPoultryEggs().getHatchingVariance()).substring(0,5));
    
    assertEquals(new Double(puTotal/eggsConsumpAveragePerLayer * 2), revenueRiskResult.getPoultryEggs().getConsumptionLayers());
    assertEquals(new Double(config.getRevenueRiskPoultryEggsConsumptionAverageEggsPerLayer()), revenueRiskResult.getPoultryEggs().getConsumptionAverageEggsPerLayer());
    assertEquals(new Double(puTotal * 2), revenueRiskResult.getPoultryEggs().getConsumptionEggsTotal());
    assertEquals(new Double(puTotal * 2 / 12), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozen());
    assertEquals(new Double(eggsConsumpFmv * puTotal * 2 / 12), revenueRiskResult.getPoultryEggs().getConsumptionExpectedRevenue());
    assertEquals(new Double(30), revenueRiskResult.getPoultryEggs().getConsumptionReportedRevenue());
    assertEquals(new Double(13), revenueRiskResult.getPoultryEggs().getConsumptionEggsDozenPrice());
    assertEquals("0.154", String.valueOf(revenueRiskResult.getPoultryEggs().getConsumptionVariance()).substring(0,5));
  }
}
