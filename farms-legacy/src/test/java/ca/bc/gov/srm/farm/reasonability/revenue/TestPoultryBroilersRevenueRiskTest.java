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

public class TestPoultryBroilersRevenueRiskTest {
  
  private static Logger logger = LoggerFactory.getLogger(TestPoultryBroilersRevenueRiskTest.class);

  private static final String PRODUCTIVE_UNIT_CODE_CHICK_BR = "143";
  private static final String PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION = "Chicken, Broilers (# of Kgs produced)";

  private static final String PRODUCTIVE_UNIT_CODE_TURK_BR = "144";
  private static final String PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION = "Turkey, Broilers (# of Kgs produced)";
  
  private static final Integer CHICKEN_BROILER_LINE_ITEM = 366;
  private static final String CHICKEN_BROILER_LINE_ITEM_DESCRIPTION = "Chickens";
  
  private static final Integer TURKEY_BROILER_LINE_ITEM = 334;
  private static final String  TURKEY_BROILER_LINE_ITEM_DESCRIPTION = "Turkeys";

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  private Scenario buildScenarioWithNoCombinedFarm(List<ProductiveUnitCapacity> productiveUnitCapacities) {
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setIsInCombinedFarmInd(false);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    
    FarmingOperation farmOp = new FarmingOperation();
    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    farmOp.setProductiveUnitCapacities(productiveUnitCapacities);

    return scenario;
  }
  
  private Scenario buildScenarioWithCombinedFarm(
      List<ProductiveUnitCapacity> productiveUnitCapacities, 
      List<ProductiveUnitCapacity> productiveUnitCapacities2,
      Double fmv1, 
      Double fmv2) {
    
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setIsInCombinedFarmInd(true);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());

    Scenario scenario2 = new Scenario();
    scenario2.setYear(new Integer(2018));
    scenario2.setIsInCombinedFarmInd(true);
    scenario2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);

    List<Scenario> scenarios = new ArrayList<>();
    scenarios.add(scenario);
    scenarios.add(scenario2);

    CombinedFarm combinedFarm = new CombinedFarm();
    combinedFarm.setScenarios(scenarios);
    scenario.setCombinedFarm(combinedFarm);

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    
    FarmingOperation farmOp = new FarmingOperation();
    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);

    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    
    FarmingOperation farmOp2 = new FarmingOperation();
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    
    farmOp.setTurkeyBroilerFMVPrice(fmv1);
    farmOp.setChickenBroilerFMVPrice(fmv1);
    farmOp.setProductiveUnitCapacities(productiveUnitCapacities);
    
    farmOp2.setTurkeyBroilerFMVPrice(fmv2);
    farmOp2.setChickenBroilerFMVPrice(fmv2);
    farmOp2.setProductiveUnitCapacities(productiveUnitCapacities2);

    return scenario;
  }
  
  @Test
  public void passWithVarLessThanLimitForChickenBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    Double averageChickenWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageChickenWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    lineItem1.setLineItem(CHICKEN_BROILER_LINE_ITEM);
    lineItem1.setDescription(CHICKEN_BROILER_LINE_ITEM_DESCRIPTION);

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());

    assertEquals("0.167", String.valueOf(revenueRiskResult.getPoultryBroilers().getChickenVariance()));
  }
  
  @Test
  public void passWithMultipleChickenBRPUs() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    Double averageChickenWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageChickenWeightKg;
    double puc2Amount = 6.0 * averageChickenWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc2Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    lineItem1.setLineItem(CHICKEN_BROILER_LINE_ITEM);
    lineItem1.setDescription(CHICKEN_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(25.0);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(new Double(puc1Amount + puc2Amount), revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(new Double(24/12), revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(new Double(24), revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(new Double(25), revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());

    assertEquals("0.042", String.valueOf(revenueRiskResult.getPoultryBroilers().getChickenVariance()));
  }
  
  @Test
  public void passWithChickenBROnCombinedFarm() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    Double averageChickenWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageChickenWeightKg;
    double puc2Amount = 6.0 * averageChickenWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    List<ProductiveUnitCapacity> productiveUnitCapacities2 = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc2Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities2.add(puc);
    }
    
    
    Scenario scenario = buildScenarioWithCombinedFarm(productiveUnitCapacities, productiveUnitCapacities2, 2.0, 2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    lineItem1.setLineItem(CHICKEN_BROILER_LINE_ITEM);
    lineItem1.setDescription(CHICKEN_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(25.0);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(new Double(puc1Amount+puc2Amount), revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(new Double(24/12), revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(new Double(24), revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(new Double(25), revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());

    assertEquals("0.042", String.valueOf(revenueRiskResult.getPoultryBroilers().getChickenVariance()));
  }
  
  @Test
  public void passWithVarLessThanLimitForTurkeyBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setTurkeyBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());

    assertEquals("0.167", String.valueOf(revenueRiskResult.getPoultryBroilers().getTurkeyVariance()));
  }
  
  @Test
  public void passForTurkeyBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setTurkeyBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());

    assertEquals("0.167", String.valueOf(revenueRiskResult.getPoultryBroilers().getTurkeyVariance()));
  }
  
  @Test
  public void passForTurkeyBRWithMultiplePUs() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    double puc2Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc2Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setTurkeyBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(25.0);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(new Double(puc1Amount + puc2Amount), revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(new Double(24/12), revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(new Double(24), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(25), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());

    assertEquals("0.042", String.valueOf(revenueRiskResult.getPoultryBroilers().getTurkeyVariance()));
  }
  
  @Test
  public void passForTurkeyBRWithCombinedFarm() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    double puc2Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    List<ProductiveUnitCapacity> productiveUnitCapacities2 = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc2Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities2.add(puc);
    }
    
    
    Scenario scenario = buildScenarioWithCombinedFarm(productiveUnitCapacities, productiveUnitCapacities2, 2.0, 2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(25.0);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, revenueRiskResult.getResult().booleanValue());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(new Double(puc1Amount + puc2Amount), revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(new Double(24/12), revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(new Double(24), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(25), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());

    assertEquals("0.042", String.valueOf(revenueRiskResult.getPoultryBroilers().getTurkeyVariance()));
  }
  
  @Test
  public void failWithVarGreatThanLimitForChickenBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    Double averageChickenWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageChickenWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(6.0 * averageChickenWeightKg);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    lineItem1.setLineItem(CHICKEN_BROILER_LINE_ITEM);
    lineItem1.setDescription(CHICKEN_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(15.0);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getChickenVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());

    assertEquals("0.25", String.valueOf(revenueRiskResult.getPoultryBroilers().getChickenVariance()).substring(0,4));
  }
  
  @Test
  public void failWithVarGreatThanLimitForTurkeyBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setTurkeyBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(15.0);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getTurkeyVariance() < varianceLimit);
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());

    assertEquals("0.25", String.valueOf(revenueRiskResult.getPoultryBroilers().getTurkeyVariance()).substring(0,4));
  }
  
  @Test
  public void failForMissingChickenBRFMV() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    Double averageChickenWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageChickenWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    lineItem1.setLineItem(CHICKEN_BROILER_LINE_ITEM);
    lineItem1.setDescription(CHICKEN_BROILER_LINE_ITEM_DESCRIPTION);

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
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV,
            PRODUCTIVE_UNIT_CODE_CHICK_BR),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenVariance());
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(new Double(14), revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());
  }
  
  @Test
  public void failForMissingTurkeyBRFMV() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(15.0);
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
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_PRODUCTIVE_UNIT_MISSING_FMV,
            PRODUCTIVE_UNIT_CODE_TURK_BR),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyVariance());
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());
  }
  
  @Test
  public void failWithMissingPUChickenBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_CHICKEN);
    lineItem1.setLineItem(CHICKEN_BROILER_LINE_ITEM);
    lineItem1.setDescription(CHICKEN_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(15.0);
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
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryBroilersRevenueRiskSubTest.ERROR_MISSING_CHICKEN_BR_PRODUCTIVE_UNIT_CODE,
            StructureGroupCodes.CHICKEN_BROILERS),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenVariance());
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());
  }
  
  @Test
  public void failWithMissingPUTurkeyBR() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setTurkeyBroilerFMVPrice(2.0);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.POULTRY_BROILER_TURKEY);
    lineItem1.setLineItem(TURKEY_BROILER_LINE_ITEM);
    lineItem1.setDescription(TURKEY_BROILER_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(15.0);
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
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryBroilersRevenueRiskSubTest.ERROR_MISSING_TURKEY_BR_PRODUCTIVE_UNIT_CODE,
            StructureGroupCodes.TURKEY_BROILERS),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyVariance());
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(new Double(15), revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());
  }
  
  @Test
  public void failWithMissingChickenBRIncome() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersChickensVariance();
    Double averageChickenWeightKg = config.getRevenueRiskPoultryBroilersChickensAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageChickenWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_CHICK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_CHICK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setChickenBroilerFMVPrice(2.0);
    
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
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryBroilersRevenueRiskSubTest.ERROR_MISSING_CHICKEN_BR_INCOME),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenVariance());
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getChickenVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getChickenKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersChickensAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getChickenAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getChickenExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getChickenPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getChickenExpectedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getChickenReportedRevenue());
  }
  
  @Test
  public void failWithMissingTurkeyBRIncome() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double varianceLimit = config.getRevenueRiskPoultryBroilersTurkeysVariance();
    Double averageTurkeyWeightKg = config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    double puc1Amount = 6.0 * averageTurkeyWeightKg;
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setReportedAmount(puc1Amount);
      puc.setStructureGroupCode(PRODUCTIVE_UNIT_CODE_TURK_BR);
      puc.setStructureGroupCodeDescription(PRODUCTIVE_UNIT_CODE_TURK_BR_DESCRIPTION);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(productiveUnitCapacities);
    scenario.getFarmingYear().getFarmingOperations().get(0).setTurkeyBroilerFMVPrice(2.0);
    
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
        ReasonabilityTestResultMessage.createMessageWithParameters(PoultryBroilersRevenueRiskSubTest.ERROR_MISSING_TURKEY_BR_INCOME),
        revenueRiskResult.getErrorMessages().get(0).getMessage());
    
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getSubTestPass());
    assertEquals(true, revenueRiskResult.getPoultryBroilers().getChickenPass());
    assertEquals(false, revenueRiskResult.getPoultryBroilers().getTurkeyPass());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyVariance());
    assertEquals(varianceLimit, revenueRiskResult.getPoultryBroilers().getTurkeyVarianceLimit());
    
    assertEquals(puc1Amount, revenueRiskResult.getPoultryBroilers().getTurkeyKgProduced());
    assertEquals(new Double(config.getRevenueRiskPoultryBroilersTurkeysAverageWeightKg()), revenueRiskResult.getPoultryBroilers().getTurkeyAverageWeightKg());
    assertEquals(new Double(6), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedSoldCount());
    assertEquals(new Double(12/6), revenueRiskResult.getPoultryBroilers().getTurkeyPricePerBird());
    assertEquals(new Double(12), revenueRiskResult.getPoultryBroilers().getTurkeyExpectedRevenue());
    assertEquals(null, revenueRiskResult.getPoultryBroilers().getTurkeyReportedRevenue());
  }
}
