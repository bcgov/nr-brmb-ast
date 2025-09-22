package ca.bc.gov.srm.farm.reasonability.revenue;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.NurseryRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestNurseryRevenueRiskTest {
  
  private static Logger logger = LoggerFactory.getLogger(TestNurseryRevenueRiskTest.class);
  
  private static final String NURSERY_ITEM_1_INVENTORY_ITEM_CODE = "0000";
  private static final String NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION = "Description for item 1.";
  
  private static final Integer NURSERY_ITEM_1_LINE_ITEM = 1;
  private static final String NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION = "Description for item 1 line item.";
  
  private static final String NURSERY_ITEM_2_INVENTORY_ITEM_CODE = "0001";
  private static final String NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION = "Description for item 2.";
  
  private static final Integer NURSERY_ITEM_2_LINE_ITEM = 2;
  private static final String NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION = "Description for item 2 line item.";
  
  private static final String FORAGE_INVENTORY_ITEM_CODE = "0003";
  private static final String FORAGE_INVENTORY_ITEM_DESCRIPTION = "Description for forage item.";
  
  private static final Integer FORAGE_ITEM_LINE_ITEM = 3;
  private static final String FORAGE_LINE_ITEM_DESCRIPTION = "Description for forage line item.";
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  private Scenario buildScenarioWithNoCombinedFarm(List<CropItem> cropItems) {
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setIsInCombinedFarmInd(false);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());

    FarmingYear farmYear = new FarmingYear();
    FarmingOperation farmOp = new FarmingOperation();
    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    scenario.setFarmingYear(farmYear);
    
    farmOp.setCropItems(cropItems);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());

    return scenario;
  }
  
  private Scenario buildScenarioWithCombinedFarm(List<CropItem> cropItems, List<CropItem> cropItems2) {
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

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    List<FarmingOperation> farmOps = new ArrayList<>();
    
    FarmingOperation farmOp = new FarmingOperation();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    
    farmOp.setCropItems(cropItems);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());

    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    
    farmOp2.setCropItems(cropItems2);
    farmOp2.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());


    return scenario;
  }

  @Test
  public void passWithVarianceLessThanLimit() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    NurseryRevenueRiskSubTestResult nurseryResult = revenueRiskResult.getNursery();
    assertNotNull(nurseryResult);
    
    List<RevenueRiskInventoryItem> inventoryItems = nurseryResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(1, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();
    
    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
      assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
      assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
      
      assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    }

    List<RevenueRiskIncomeTestResult> incomes = nurseryResult.getIncomes();
    assertNotNull(incomes);
    assertEquals(1, incomes.size());
    Iterator<RevenueRiskIncomeTestResult> incomeIterator = incomes.iterator();
    {
      RevenueRiskIncomeTestResult incomeResult = incomeIterator.next();
      assertNotNull(incomeResult);
      assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
      assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
      assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    }

    assertEquals("102.0", String.valueOf(nurseryResult.getExpectedRevenue()));
    assertEquals("100.0", String.valueOf(nurseryResult.getReportedRevenue()));
    assertEquals("0.02", String.valueOf(nurseryResult.getVariance()));
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(true, nurseryResult.getVariance() < varianceLimit);
    assertEquals(true, nurseryResult.getSubTestPass());
    assertEquals(true, revenueRiskResult.getResult());
  }
  
  @Test
  public void failWithVarianceLessThanLimit() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(2.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals(false, revenueRiskResult.getNursery().getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(1, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("2.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("204.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(1, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));

    assertEquals(false, revenueRiskResult.getResult());
    assertEquals(false, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("204.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("100.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals("0.51", String.valueOf(revenueRiskResult.getNursery().getVariance()));
  }
  
  @Test
  public void passWithMultipleItems() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem2.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(3.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setReportedQuantityStart(300.0);
    cropItem2.setFmvEnd(1.00);
    cropItem2.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
    lineItem2.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(false);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    
    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(false);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(300.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals(true, revenueRiskResult.getNursery().getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(2, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    
    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getNursery().getInventory().get(1);
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult2.getFmvPrice()));
    assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("203.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals("203.0", String.valueOf(invItemResult2.getExpectedRevenue()));
    
    assertEquals("300.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(2, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    
    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getNursery().getIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult2.getDescription()));
    assertEquals("300.0", String.valueOf(incomeResult2.getReportedRevenue()));

    assertEquals(true, revenueRiskResult.getResult());
    assertEquals(true, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("305.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("400.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals("0.311", String.valueOf(revenueRiskResult.getNursery().getVariance()).substring(0,5));
  }
  
  @Test
  public void failWithMultipleItems() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem2.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(3.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setReportedQuantityStart(500.0);
    cropItem2.setFmvEnd(1.00);
    cropItem2.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
    lineItem2.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(false);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(600.0);
    
    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(false);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(300.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals(false, revenueRiskResult.getNursery().getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(2, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    
    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getNursery().getInventory().get(1);
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult2.getFmvPrice()));
    assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("403.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals("403.0", String.valueOf(invItemResult2.getExpectedRevenue()));
    
    assertEquals("500.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(2, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("600.0", String.valueOf(incomeResult.getReportedRevenue()));
    
    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getNursery().getIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult2.getDescription()));
    assertEquals("300.0", String.valueOf(incomeResult2.getReportedRevenue()));

    assertEquals(false, revenueRiskResult.getResult());
    assertEquals(false, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("505.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("900.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals("0.782", String.valueOf(revenueRiskResult.getNursery().getVariance()).substring(0,5));
  }
  
  @Test
  public void passMixOfNurseryAndNonNurseryItems() {
    
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
      cropItem.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(2.00);
      cropItem.setReportedQuantityEnd(100.0);
      cropItem.setReportedQuantityStart(200.0);
      cropItem.setFmvEnd(1.00);
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
      cropItem.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(3.00);
      cropItem.setReportedQuantityEnd(100.0);
      cropItem.setReportedQuantityStart(300.0);
      cropItem.setFmvEnd(1.00);
      cropItem.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
      cropItem.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(FORAGE_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(FORAGE_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(3.00);
      cropItem.setReportedQuantityEnd(100.0);
      cropItem.setReportedQuantityStart(300.0);
      cropItem.setFmvEnd(1.00);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItem.setLineItem(FORAGE_ITEM_LINE_ITEM);
      cropItems.add(cropItem);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
      lineItem.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
      lineItem.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);

      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(false);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(100.0);
      incomeExpenses.add(incExp);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
      lineItem.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
      lineItem.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(false);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(300.0);
      incomeExpenses.add(incExp);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      lineItem.setLineItem(FORAGE_ITEM_LINE_ITEM);
      lineItem.setDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(false);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(300.0);
      incomeExpenses.add(incExp);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    NurseryRevenueRiskSubTestResult nurseryResult = revenueRiskResult.getNursery();
    assertNotNull(nurseryResult);

    List<RevenueRiskInventoryItem> inventoryItems = nurseryResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(2, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
      assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
      assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    }

    {
      RevenueRiskInventoryItem invItemResult2 = inventoryIterator.next();
      assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
      assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult2.getFmvPrice()));
      assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
      assertEquals("203.0", String.valueOf(invItemResult2.getQuantitySold()));
      assertEquals("203.0", String.valueOf(invItemResult2.getExpectedRevenue()));
      assertEquals("300.0", String.valueOf(invItemResult2.getQuantityStart()));
      assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));
    }

    List<RevenueRiskIncomeTestResult> incomes = nurseryResult.getIncomes();
    assertNotNull(incomes);
    assertEquals(2, incomes.size());
    Iterator<RevenueRiskIncomeTestResult> incomeIterator = incomes.iterator();
    
    {
      RevenueRiskIncomeTestResult incomeResult = incomeIterator.next();
      assertNotNull(incomeResult);
      assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
      assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
      assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    }
    
    {
      RevenueRiskIncomeTestResult incomeResult = incomeIterator.next();
      assertNotNull(incomeResult);
      assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult.getLineItemCode());
      assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult.getDescription()));
      assertEquals("300.0", String.valueOf(incomeResult.getReportedRevenue()));
    }
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());

    assertEquals(true, nurseryResult.getVariance() < varianceLimit);

    assertEquals("305.0", String.valueOf(nurseryResult.getExpectedRevenue()));
    assertEquals("400.0", String.valueOf(nurseryResult.getReportedRevenue()));
    assertEquals("0.311", String.valueOf(nurseryResult.getVariance()).substring(0,5));
    assertEquals(true, nurseryResult.getSubTestPass());
    assertEquals(false, revenueRiskResult.getResult());
  }
  
  @Test
  public void passWithMultipleItemsOnCombinedFarm() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem2.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(3.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setReportedQuantityStart(300.0);
    cropItem2.setFmvEnd(1.00);
    cropItem2.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
    lineItem2.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(false);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    
    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(false);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(300.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    List<CropItem> cropItems2 = new ArrayList<>();
    cropItems2.add(cropItem2);
    
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, cropItems2);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals(true, revenueRiskResult.getNursery().getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(2, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    
    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getNursery().getInventory().get(1);
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult2.getFmvPrice()));
    assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("203.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals("203.0", String.valueOf(invItemResult2.getExpectedRevenue()));
    
    assertEquals("300.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(2, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    
    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getNursery().getIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult2.getDescription()));
    assertEquals("300.0", String.valueOf(incomeResult2.getReportedRevenue()));

    assertEquals(true, revenueRiskResult.getResult());
    assertEquals(true, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("305.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("400.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals("0.311", String.valueOf(revenueRiskResult.getNursery().getVariance()).substring(0,5));
  }
  
  @Test
  public void failWithMissingIncome() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(2.00);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    
    NurseryRevenueRiskSubTestResult nurseryResult = revenueRiskResult.getNursery();
    assertNotNull(nurseryResult);
    
    List<RevenueRiskInventoryItem> inventoryItems = nurseryResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(1, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(1, revenueRiskResult.getNursery().getInventory().size());

    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
      assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
      assertEquals("2.0", String.valueOf(invItemResult.getFmvPrice()));
      assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("204.0", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    }

    List<RevenueRiskIncomeTestResult> incomes = nurseryResult.getIncomes();
    assertNotNull(incomes);
    assertEquals(0, incomes.size());
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(NurseryRevenueRiskSubTest.ERROR_MISSING_INCOME),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertEquals(false, revenueRiskResult.getResult());
    assertEquals(false, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("204.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("0.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals(null, revenueRiskResult.getNursery().getVariance());
  }
  
  @Test
  public void failWithQuantityStartMissing() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem2.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(3.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setFmvEnd(1.00);
    cropItem2.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
    lineItem2.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(false);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    
    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(false);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(300.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals("2.922", String.valueOf(revenueRiskResult.getNursery().getVariance()));

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(2, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    
    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getNursery().getInventory().get(1);
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult2.getFmvPrice()));
    assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("0.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));
    assertEquals("0.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals("0.0", String.valueOf(invItemResult2.getExpectedRevenue()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(2, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    
    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getNursery().getIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult2.getDescription()));
    assertEquals("300.0", String.valueOf(incomeResult2.getReportedRevenue()));

    assertEquals(false, revenueRiskResult.getResult());
    assertEquals(false, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("102.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("400.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals("2.922", String.valueOf(revenueRiskResult.getNursery().getVariance()));
  }
  
  @Test
  public void passWithQuantityEndMissing() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem2.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(3.00);
    cropItem2.setReportedQuantityStart(300.0);
    cropItem2.setFmvEnd(1.00);
    cropItem2.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
    lineItem2.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(false);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    
    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(false);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(300.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals("0.012", String.valueOf(revenueRiskResult.getNursery().getVariance()));

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(2, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    
    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getNursery().getInventory().get(1);
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult2.getFmvPrice()));
    assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("303.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals("303.0", String.valueOf(invItemResult2.getExpectedRevenue()));
    
    assertEquals("300.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("0.0", String.valueOf(invItemResult2.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(2, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    
    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getNursery().getIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult2.getDescription()));
    assertEquals("300.0", String.valueOf(incomeResult2.getReportedRevenue()));

    assertEquals(true, revenueRiskResult.getResult());
    assertEquals(true, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("405.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("400.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
  }
  
  @Test
  public void passWithMissingQuantProd() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskNurseryVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
    assertEquals(true, revenueRiskResult.getNursery().getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(1, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("100.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(1, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));

    assertEquals(true, revenueRiskResult.getResult());
    assertEquals(true, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("100.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("100.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals("0.0", String.valueOf(revenueRiskResult.getNursery().getVariance()).substring(0,3));
  }
  
  @Test
  public void failWithMissingFMV() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem1.setInventoryItemCode(NURSERY_ITEM_1_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setFmvEnd(1.00);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    cropItem2.setInventoryItemCode(NURSERY_ITEM_2_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(3.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setReportedQuantityStart(500.0);
    cropItem2.setLineItemDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem2.setLineItem(NURSERY_ITEM_2_LINE_ITEM);
    lineItem2.setDescription(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(false);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(600.0);
    
    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(false);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(300.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_INVENTORY_MISSING_FMV,
            cropItem2.getInventoryItemCode(), cropItem2.getInventoryItemCodeDescription()),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(null, revenueRiskResult.getNursery().getVariance());

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(2, revenueRiskResult.getNursery().getInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getNursery().getInventory().get(0);
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_1_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getFmvPrice()));
    assertEquals("2.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("102.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("102.0", String.valueOf(invItemResult.getExpectedRevenue()));
    
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));
    
    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getNursery().getInventory().get(1);
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(NURSERY_ITEM_2_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals(null, invItemResult2.getFmvPrice());
    assertEquals("3.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("403.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals(null, invItemResult2.getExpectedRevenue());
    
    assertEquals("500.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(2, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("600.0", String.valueOf(incomeResult.getReportedRevenue()));
    
    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getNursery().getIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(NURSERY_ITEM_2_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals(NURSERY_ITEM_2_LINE_ITEM_DESCRIPTION, String.valueOf(incomeResult2.getDescription()));
    assertEquals("300.0", String.valueOf(incomeResult2.getReportedRevenue()));

    assertEquals(false, revenueRiskResult.getResult());
    assertEquals(false, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("102.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("900.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
  }
  
  @Test
  public void failWithMissingInvItem() {

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.NURSERY);
    lineItem1.setLineItem(NURSERY_ITEM_1_LINE_ITEM);
    lineItem1.setDescription(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

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
        ReasonabilityTestResultMessage.createMessageWithParameters(NurseryRevenueRiskSubTest.ERROR_MISSING_INVENTORY),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertEquals(false, revenueRiskResult.getResult().booleanValue());

    assertNotNull(revenueRiskResult.getNursery().getInventory());
    assertEquals(0, revenueRiskResult.getNursery().getInventory().size());

    assertNotNull(revenueRiskResult.getNursery().getIncomes());
    assertEquals(1, revenueRiskResult.getNursery().getIncomes().size());
    
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getNursery().getIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(NURSERY_ITEM_1_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(NURSERY_ITEM_1_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));

    assertEquals(false, revenueRiskResult.getResult());
    assertEquals(false, revenueRiskResult.getNursery().getSubTestPass());
    assertEquals("0.0", String.valueOf(revenueRiskResult.getNursery().getExpectedRevenue()));
    assertEquals("100.0", String.valueOf(revenueRiskResult.getNursery().getReportedRevenue()));
    assertEquals(null, revenueRiskResult.getNursery().getVariance());
  }
}
