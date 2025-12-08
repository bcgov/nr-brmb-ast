package ca.bc.gov.srm.farm.reasonability.revenue;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskIncomeTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestGrainForageRevenueRiskTest {

  private static Logger logger = LoggerFactory.getLogger(TestGrainForageRevenueRiskTest.class);

  private static final String SILAGE_CORN_INVENTORY_ITEM_CODE = "5583";
  private static final String SILAGE_CORN_INVENTORY_ITEM_DESCRIPTION = "Silage; Corn";

  private static final String SILAGE_INVENTORY_ITEM_CODE = "5584";
  private static final String SILAGE_INVENTORY_ITEM_DESCRIPTION = "Silage";

  private static final String GREENFEED_INVENTORY_ITEM_CODE = "5562";
  private static final String GREENFEED_INVENTORY_ITEM_DESCRIPTION = "Greenfeed";

  private static final String HAY_INVENTORY_ITEM_CODE = "5574";
  private static final String HAY_INVENTORY_ITEM_DESCRIPTION = "Hay; Grass";

  private static final String HAY_TIMOTHY_INVENTORY_ITEM_CODE = "5579";
  private static final String HAY_TIMOTHY_INVENTORY_ITEM_DESCRIPTION = "Hay; Timothy";
  
  private static final String STRAW_INVENTORY_ITEM_CODE = "5586";
  private static final String STRAW_INVENTORY_ITEM_DESCRIPTION = "Straw";

  private static final String BARLEY_FEED_INVENTORY_ITEM_CODE = "5195";
  private static final String BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION = "Barley; Feed (Off Board)";

  private static final Integer FORAGE_LINE_ITEM = 264;
  private static final String FORAGE_LINE_ITEM_DESCRIPTION = "Forage (including pellets; silage)";

  private static final Integer BARLEY_LINE_ITEM = 3;
  private static final String BARLEY_LINE_ITEM_DESCRIPTION = "Barley";

  private static final Integer STRAW_LINE_ITEM = 267;
  private static final String STRAW_LINE_ITEM_DESCRIPTION = "Straw";
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }

  private Scenario buildScenarioWithNoCombinedFarm(List<CropItem> cropItems) {
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setIsInCombinedFarmInd(false);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    
    List<FarmingOperation> farmOps = new ArrayList<>();
    FarmingOperation farmOp = new FarmingOperation();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    
    farmOp.setCropItems(cropItems);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());

    return scenario;
  }

  private Scenario buildScenarioWithCombinedFarm(List<CropItem> cropItems, List<CropItem> cropItems2) {
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
    
    List<FarmingOperation> farmOps = new ArrayList<>();
    FarmingOperation farmOp = new FarmingOperation();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);

    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    
    farmOp.setCropItems(cropItems);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    farmOp2.setCropItems(cropItems2);
    farmOp2.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());

    return scenario;
  }

  @Test
  public void passWithVarianceLessThanLimit() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedPriceEnd(1.00);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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
    assertEquals(true, revenueRiskResult.getForageGrainIncomes().get(0).getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.01", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithGrain() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      cropItem1.setInventoryItemCode(BARLEY_FEED_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(1.00);
      cropItem1.setReportedQuantityEnd(100.0);
      cropItem1.setReportedQuantityStart(200.0);
      cropItem1.setReportedPriceEnd(1.00);
      cropItem1.setLineItem(BARLEY_LINE_ITEM);
      cropItem1.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem1);
    }

    {
      LineItem lineItem1 = new LineItem();
      lineItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      lineItem1.setLineItem(BARLEY_LINE_ITEM);
      lineItem1.setDescription(BARLEY_LINE_ITEM_DESCRIPTION);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem1);
      incExp1.setReportedAmount(100.0);
      incomeExpenses.add(incExp1);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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
    assertEquals(true, revenueRiskResult.getForageGrainIncomes().get(0).getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(BARLEY_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(BARLEY_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.01", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithGrainWithReceivable() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      cropItem1.setInventoryItemCode(BARLEY_FEED_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(1.00);
      cropItem1.setReportedQuantityEnd(100.0);
      cropItem1.setReportedQuantityStart(200.0);
      cropItem1.setReportedPriceEnd(1.00);
      cropItem1.setLineItem(BARLEY_LINE_ITEM);
      cropItem1.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem1);
    }

    {
      LineItem lineItem1 = new LineItem();
      lineItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      lineItem1.setLineItem(BARLEY_LINE_ITEM);
      lineItem1.setDescription(BARLEY_LINE_ITEM_DESCRIPTION);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem1);
      incExp1.setReportedAmount(100.0);
      incomeExpenses.add(incExp1);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BARLEY_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setAdjStartOfYearAmount(1.0);
      receivableItem.setReportedEndOfYearAmount(18.0);
      receivableItem.setAdjEndOfYearAmount(2.0);
      receivableItems.add(receivableItem);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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
    assertEquals(true, revenueRiskResult.getForageGrainIncomes().get(0).getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(BARLEY_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(BARLEY_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("90.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.109", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithGrainWithDupReceivables() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      cropItem1.setInventoryItemCode(BARLEY_FEED_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(1.00);
      cropItem1.setReportedQuantityEnd(100.0);
      cropItem1.setReportedQuantityStart(200.0);
      cropItem1.setReportedPriceEnd(1.00);
      cropItem1.setLineItem(BARLEY_LINE_ITEM);
      cropItem1.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem1);
    }

    {
      LineItem lineItem1 = new LineItem();
      lineItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      lineItem1.setLineItem(BARLEY_LINE_ITEM);
      lineItem1.setDescription(BARLEY_LINE_ITEM_DESCRIPTION);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem1);
      incExp1.setReportedAmount(110.0);
      incomeExpenses.add(incExp1);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BARLEY_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setAdjStartOfYearAmount(1.0);
      receivableItem.setReportedEndOfYearAmount(18.0);
      receivableItem.setAdjEndOfYearAmount(2.0);
      receivableItems.add(receivableItem);

      ReceivableItem receivableItem2 = new ReceivableItem();
      receivableItem2.setInventoryItemCode(BARLEY_LINE_ITEM.toString());
      receivableItem2.setInventoryItemCodeDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem2.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem2.setReportedStartOfYearAmount(29.0);
      receivableItem2.setAdjStartOfYearAmount(1.0);
      receivableItem2.setReportedEndOfYearAmount(18.0);
      receivableItem2.setAdjEndOfYearAmount(2.0);
      receivableItems.add(receivableItem2);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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
    assertEquals(true, revenueRiskResult.getForageGrainIncomes().get(0).getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(BARLEY_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(BARLEY_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("90.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.109", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithNoInvItemsAndNoLineItems() {
    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());

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
    assertNotNull(revenueRiskResult.getResult());
    assertEquals(true, scenario.getReasonabilityTestResults().getRevenueRiskTest().getResult().booleanValue());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(0, revenueRiskResult.getForageGrainInventory().size());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithConversionAppliedToDupInvItem() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedPriceEnd(0.25);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem2.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(1.00);
    cropItem2.setReportedQuantityStart(30.0);
    cropItem2.setReportedQuantityEnd(25.0);
    cropItem2.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(FORAGE_LINE_ITEM);

    CropUnitConversion convItem = new CropUnitConversion();
    convItem.setDefaultCropUnitCode(CropUnitCodes.TONNES);
    CropUnitConversionItem convItem1 = new CropUnitConversionItem();
    convItem1.setTargetCropUnitCode(CropUnitCodes.POUNDS);
    convItem1.setConversionFactor(new BigDecimal(2));

    List<CropUnitConversionItem> cropConvItems = new ArrayList<>();
    cropConvItems.add(convItem1);
    convItem.setConversionItems(cropConvItems);
    cropItem1.setCropUnitConversion(convItem);
    cropItem2.setCropUnitConversion(convItem);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(31.24);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    {
      RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals("5574", invItemResult.getInventoryItemCode());
      assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
      assertEquals("0.5", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("1.5", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("130.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("75.0", String.valueOf(invItemResult.getQuantityEnd()));
  
      assertEquals("56.5", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("28.25", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    
    {
      RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
      assertNotNull(incomeResult);
      assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
      assertEquals("31.24", String.valueOf(incomeResult.getReportedRevenue()));
      assertEquals("28.25", String.valueOf(incomeResult.getExpectedRevenue()));
      assertEquals("0.106", String.valueOf(incomeResult.getVariance()));
      assertEquals(true, incomeResult.getVariance() < varianceLimit);
      assertEquals(Boolean.TRUE, incomeResult.getPass());
    }

    assertEquals(true, revenueRiskResult.getResult());
  }

  @Test
  public void passWithDupInvAndDifferentPriceEndItem() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(null);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedPriceStart(2.0);
    cropItem1.setReportedPriceEnd(null);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem2.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(1.00);
    cropItem2.setReportedQuantityEnd(25.0);
    cropItem2.setReportedQuantityStart(30.0);
    cropItem2.setReportedPriceEnd(4.00);
    cropItem2.setLineItem(FORAGE_LINE_ITEM);
    cropItem2.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);

    CropUnitConversion convItem = new CropUnitConversion();
    convItem.setDefaultCropUnitCode(CropUnitCodes.TONNES);
    CropUnitConversionItem convItem1 = new CropUnitConversionItem();
    convItem1.setTargetCropUnitCode(CropUnitCodes.POUNDS);
    convItem1.setConversionFactor(new BigDecimal(2));

    List<CropUnitConversionItem> cropConvItems = new ArrayList<>();
    cropConvItems.add(convItem1);
    convItem.setConversionItems(cropConvItems);
    cropItem1.setCropUnitConversion(convItem);
    cropItem2.setCropUnitConversion(convItem);

    LineItem lineItem1 = new LineItem();
    lineItem1.setLineItem(FORAGE_LINE_ITEM);
    lineItem1.setDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(400.00);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("4.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.5", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("130.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("25.0", String.valueOf(invItemResult.getQuantityEnd()));
    assertEquals("106.5", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("426.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("400.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("426.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.061", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
    assertEquals(true, incomeResult.getVariance() < varianceLimit);
  }

  @Test
  public void failWithVarianceGreaterThanLimit() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(50.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedPriceEnd(3.00);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("3.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("50.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("151.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("453.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("453.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.779", String.valueOf(incomeResult.getVariance()));
    assertEquals(true, Math.abs(revenueRiskResult.getForageGrainIncomes().get(0).getVariance()) > varianceLimit);
    assertEquals(Boolean.FALSE, incomeResult.getPass());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());

  }

  @Test
  public void failWithMissingLineItem() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedPriceEnd(1.00);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);

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

    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INCOME_FOR_LINE_ITEM,
            cropItem1.getLineItem(), cropItem1.getLineItemDescription()),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());

    assertEquals(0, revenueRiskResult.getForageGrainIncomes().size());
    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());

  }

  @Test
  public void failWithMissingInventoryButProvidedLineItem() {

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);
    lineItem1.setDescription(FORAGE_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

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

    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(
        RevenueRiskTest.ERROR_MISSING_INVENTORY_FOR_INCOME, FORAGE_LINE_ITEM.toString(),
        lineItem1.getDescription()), revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(0, revenueRiskResult.getForageGrainInventory().size());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(0, revenueRiskResult.getForageGrainIncomes().size());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithNoPriceEndProvidedForInvItem() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

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
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_PRICE,
            HAY_INVENTORY_ITEM_CODE, HAY_INVENTORY_ITEM_DESCRIPTION),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertNull(invItemResult.getReportedPrice());

    assertEquals("0.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("0.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertNull(incomeResult.getVariance());

    assertEquals(Boolean.FALSE, incomeResult.getPass());
    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithNoConversionFactors() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.SMALL_BALES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.SMALL_BALES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedPriceEnd(1.00);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem2.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(1.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setReportedQuantityStart(200.0);
    cropItem2.setReportedPriceEnd(1.00);
    cropItem2.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(FORAGE_LINE_ITEM);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

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

    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_CONVERSIONS,
        HAY_INVENTORY_ITEM_CODE, HAY_INVENTORY_ITEM_DESCRIPTION, cropItem1.getCropUnitCodeDescription(),
        CropUnitCodes.TONNES_DESCRIPTION), revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertNull(invItemResult.getQuantityProduced());
    assertNull(invItemResult.getQuantityStart());
    assertNull(invItemResult.getQuantityEnd());

    assertNull(invItemResult.getQuantitySold());
    assertEquals("0.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("0.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertNull(incomeResult.getVariance());
    assertEquals(Boolean.FALSE, incomeResult.getPass());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithTwoItemsWithCombinedFarm() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(1.00);
    cropItem1.setReportedQuantityEnd(100.0);
    cropItem1.setReportedQuantityStart(200.0);
    cropItem1.setReportedPriceEnd(1.00);
    cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(FORAGE_LINE_ITEM);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem2.setInventoryItemCode(SILAGE_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(SILAGE_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(1.00);
    cropItem2.setReportedQuantityEnd(100.0);
    cropItem2.setReportedQuantityStart(200.0);
    cropItem2.setReportedPriceEnd(1.00);
    cropItem2.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(BARLEY_LINE_ITEM);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem1.setLineItem(FORAGE_LINE_ITEM);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    lineItem2.setLineItem(BARLEY_LINE_ITEM);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(100.0);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(100.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<IncomeExpense> incomeExpenses2 = new ArrayList<>();
    incomeExpenses2.add(incExp2);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);

    List<CropItem> cropItems2 = new ArrayList<>();
    cropItems2.add(cropItem2);

    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, cropItems2);

    int i = 0;
    for (Scenario scen : scenario.getCombinedFarm().getScenarios()) {
      if (i == 0) {
        scen.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);
      } else {
        scen.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses2);
      }
      i++;
    }

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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
    assertEquals(true, revenueRiskResult.getForageGrainIncomes().get(0).getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(2, revenueRiskResult.getForageGrainInventory().size());

    Collections.sort(revenueRiskResult.getForageGrainInventory());
    Collections.sort(revenueRiskResult.getForageGrainIncomes());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(2, revenueRiskResult.getForageGrainIncomes().size());

    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(BARLEY_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.01", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    RevenueRiskInventoryItem invItemResult2 = revenueRiskResult.getForageGrainInventory().get(1);
    assertEquals(CropUnitCodes.TONNES, invItemResult2.getCropUnitCode());
    assertEquals(SILAGE_INVENTORY_ITEM_CODE, invItemResult2.getInventoryItemCode());
    assertEquals(SILAGE_INVENTORY_ITEM_DESCRIPTION, invItemResult2.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult2.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult2.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult2.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult2.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult2.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult2.getExpectedRevenue()));
    assertEquals(BARLEY_LINE_ITEM, invItemResult2.getLineItemCode());
    assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, invItemResult2.getLineItemDescription());

    RevenueRiskIncomeTestResult incomeResult2 = revenueRiskResult.getForageGrainIncomes().get(1);
    assertNotNull(incomeResult2);
    assertEquals(FORAGE_LINE_ITEM, incomeResult2.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult2.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult2.getExpectedRevenue()));
    assertEquals("-0.01", String.valueOf(incomeResult2.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult2.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passForProvidedIncomeAndExpWithSameLineItem() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(1.00);
      cropItem1.setReportedQuantityEnd(100.0);
      cropItem1.setReportedQuantityStart(200.0);
      cropItem1.setReportedPriceEnd(1.00);
      cropItem1.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItem1.setLineItem(FORAGE_LINE_ITEM);
      cropItems.add(cropItem1);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      lineItem.setLineItem(FORAGE_LINE_ITEM);

      IncomeExpense income = new IncomeExpense();
      income.setIsExpense(Boolean.FALSE);
      income.setLineItem(lineItem);
      income.setReportedAmount(100.0);
      incomeExpenses.add(income);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      lineItem.setLineItem(FORAGE_LINE_ITEM);

      IncomeExpense expense = new IncomeExpense();
      expense.setIsExpense(Boolean.TRUE);
      expense.setLineItem(lineItem);
      expense.setReportedAmount(100.0);
      incomeExpenses.add(expense);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);

    {
      FarmingOperation operation2 = new FarmingOperation();
      operation2.setFarmingYear(scenario.getFarmingYear());
      operation2.setCropItems(new ArrayList<CropItem>());
      operation2.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());

      List<IncomeExpense> operation2IncomeExpenses = new ArrayList<>();
      operation2.setIncomeExpenses(operation2IncomeExpenses);
      scenario.getFarmingYear().getFarmingOperations().add(operation2);
    }

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
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
    assertEquals(true, revenueRiskResult.getForageGrainIncomes().get(0).getVariance() < varianceLimit);

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals("5574", invItemResult.getInventoryItemCode());
    assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("100.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.01", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithForageFedOutToCattle() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();

    // Grain - not affected by fed out rules
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      cropItem.setInventoryItemCode(BARLEY_FEED_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(100.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(BARLEY_LINE_ITEM);
      cropItem.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem);
    }

    // Straw - is forage, but not affected by fed out rules
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(STRAW_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(STRAW_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(11.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(STRAW_LINE_ITEM);
      cropItem.setLineItemDescription(STRAW_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem);
    }
    
    // 5574 - Hay; Grass - forage affected by fed out rules
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(4.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem); 
    }
      
    // 5562 - Greenfeed - forage affected by fed out rules - before Hay
    { 
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(GREENFEED_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(GREENFEED_INVENTORY_ITEM_DESCRIPTION); 
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(3.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem);
    
    }

    // 5583 - Silage; Corn - forage affected by fed out rules - before Greenfeed
    { 
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(SILAGE_CORN_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(SILAGE_CORN_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(1.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem); 
    }
    
    // 5579 - Hay; Timothy - forage affected by fed out rules - last in the list of affected
    { 
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_TIMOTHY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_TIMOTHY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(0.1);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem); 
    }

    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.CATTLE);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(2));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }

    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.CATTLE_BRED_HEIFERS);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(1));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.FEEDER_CATTLE);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(5));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }

    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.FINISHED_CATTLE);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(1));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(BARLEY_LINE_ITEM);
      lineItem.setDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      lineItem.setCommodityTypeCode(CommodityTypeCodes.GRAIN);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem);
      incExp1.setReportedAmount(115.00);
      incomeExpenses.add(incExp1);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(STRAW_LINE_ITEM);
      lineItem.setDescription(STRAW_LINE_ITEM_DESCRIPTION);
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem);
      incExp1.setReportedAmount(12.00);
      incomeExpenses.add(incExp1);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(FORAGE_LINE_ITEM);
      lineItem.setDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem);
      incExp1.setReportedAmount(0.55);
      incomeExpenses.add(incExp1);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    assertNotNull(reasonabilityTestResults);
    RevenueRiskTestResult revenueRiskResult = reasonabilityTestResults.getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(6, revenueRiskResult.getForageGrainInventory().size());
    
    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(3, revenueRiskResult.getForageGrainIncomes().size());
    
    
    Iterator<RevenueRiskInventoryItem> inventoryIterator = revenueRiskResult.getForageGrainInventory().iterator();

    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
      assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
      assertEquals("5195", invItemResult.getInventoryItemCode());
      assertEquals("Barley; Feed (Off Board)", invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("100.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
      assertNull(invItemResult.getQuantityConsumed());
      assertEquals("100.0", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("100.0", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(BARLEY_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }
    
    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
      assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
      assertEquals("5562", invItemResult.getInventoryItemCode());
      assertEquals("Greenfeed", invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("3.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
      assertEquals("3.0", String.valueOf(invItemResult.getQuantityConsumed()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("0.0", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }
    
    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
      assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
      assertEquals("5574", invItemResult.getInventoryItemCode());
      assertEquals("Hay; Grass", invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("4.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
      assertEquals("3.5", String.valueOf(invItemResult.getQuantityConsumed()));
      assertEquals("0.5", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("0.5", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }
    
    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
      assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
      assertEquals(HAY_TIMOTHY_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
      assertEquals(HAY_TIMOTHY_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("0.1", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityConsumed()));
      assertEquals("0.1", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("0.1", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }
    
    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
      assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
      assertEquals("5583", invItemResult.getInventoryItemCode());
      assertEquals("Silage; Corn", invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
      assertEquals("1.0", String.valueOf(invItemResult.getQuantityConsumed()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantitySold()));
      assertEquals("0.0", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }
    
    {
      RevenueRiskInventoryItem invItemResult = inventoryIterator.next();
      assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
      assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
      assertEquals("Tonnes", invItemResult.getCropUnitCodeDescription());
      assertEquals("5586", invItemResult.getInventoryItemCode());
      assertEquals("Straw", invItemResult.getInventoryItemCodeDescription());
      assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
      assertEquals("11.0", String.valueOf(invItemResult.getQuantityProduced()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
      assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
      assertEquals("11.0", String.valueOf(invItemResult.getQuantitySold()));
      assertNull(invItemResult.getQuantityConsumed());
      assertEquals("11.0", String.valueOf(invItemResult.getExpectedRevenue()));
      assertEquals(STRAW_LINE_ITEM, invItemResult.getLineItemCode());
      assertEquals(STRAW_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    }
 
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(2);
    assertNotNull(incomeResult);
    assertEquals(STRAW_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("12.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("11.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("0.091", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());
    assertEquals(true, incomeResult.getVariance() < varianceLimit); 

    
    incomeResult = revenueRiskResult.getForageGrainIncomes().get(1);
    assertNotNull(incomeResult); 
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("0.55", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("0.6",String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-0.083",String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE,incomeResult.getPass()); 
    assertEquals(true, incomeResult.getVariance() < varianceLimit);
    
    incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(BARLEY_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("115.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("100.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("0.15", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE, incomeResult.getPass());
    assertEquals(true, incomeResult.getVariance() < varianceLimit);
    
    assertEquals("7.5", String.valueOf(reasonabilityTestResults.getForageConsumerCapacity()));
    assertEquals("0.2", String.valueOf(varianceLimit));
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
     
  }
  
  @Test
  public void forageFedOutToCattleInCorrectOrder() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    
    /*
     *  FEED OUT ORDER : 
     * {"5584","5583","5562","5580","5572","5574","5576","5578","5579"};
     * 
    */
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(5.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem); 
    }

    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(SILAGE_CORN_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(SILAGE_CORN_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(3.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem); 
    }
      
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(GREENFEED_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(GREENFEED_INVENTORY_ITEM_DESCRIPTION); 
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(3.00);
      cropItem.setReportedQuantityEnd(null);
      cropItem.setReportedQuantityStart(null);
      cropItem.setReportedPriceStart(null);
      cropItem.setReportedPriceEnd(1.0);
      cropItem.setLineItem(FORAGE_LINE_ITEM);
      cropItem.setLineItemDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem);
    }

    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.CATTLE);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      prodUnitCapacity.setReportedAmount(new Double(2));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }

    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.CATTLE_BRED_HEIFERS);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(1));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.FEEDER_CATTLE);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(5));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }

    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setStructureGroupCode(StructureGroupCodes.FINISHED_CATTLE);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      prodUnitCapacity.setReportedAmount(new Double(1));
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(prodUnitCapacity);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setLineItem(FORAGE_LINE_ITEM);
      lineItem.setDescription(FORAGE_LINE_ITEM_DESCRIPTION);
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem);
      incExp1.setReportedAmount(3.55);
      incomeExpenses.add(incExp1);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getRevenueRiskForageVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    assertNotNull(reasonabilityTestResults);
    RevenueRiskTestResult revenueRiskResult = reasonabilityTestResults.getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    assertNotNull(reasonabilityTestResults.getForageConsumers());
    assertEquals(4, reasonabilityTestResults.getForageConsumers().size());
    Iterator<ForageConsumer> forageConsumersIterator = reasonabilityTestResults.getForageConsumers().iterator();
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.CATTLE, forageConsumer.getStructureGroupCode());
      assertEquals("2.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("1.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("3.0", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.FEEDER_CATTLE, forageConsumer.getStructureGroupCode());
      assertEquals("5.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("0.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("2.5", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.FINISHED_CATTLE, forageConsumer.getStructureGroupCode());
      assertEquals("1.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("0.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("0.5", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.CATTLE_BRED_HEIFERS, forageConsumer.getStructureGroupCode());
      assertEquals("1.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("1.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("1.5", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    
    assertEquals("7.5", String.valueOf(reasonabilityTestResults.getForageConsumerCapacity()));
    
    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(3, revenueRiskResult.getForageGrainInventory().size());
    
    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(2);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(SILAGE_CORN_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(SILAGE_CORN_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("3.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
    assertEquals("3.0", String.valueOf(invItemResult.getQuantityConsumed()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("0.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    
    invItemResult = revenueRiskResult.getForageGrainInventory().get(1);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(HAY_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("5.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
    assertEquals("1.5", String.valueOf(invItemResult.getQuantityConsumed()));
    assertEquals("3.5", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("3.5", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());
    
    invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(GREENFEED_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(GREENFEED_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("3.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantityEnd()));
    assertEquals("3.0", String.valueOf(invItemResult.getQuantityConsumed()));
    assertEquals("0.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("0.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(FORAGE_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(FORAGE_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    RevenueRiskIncomeTestResult incomeResult =
    revenueRiskResult.getForageGrainIncomes().get(0); assertNotNull(incomeResult);
    assertEquals(FORAGE_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals("3.55", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("3.5",String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("0.014",String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.TRUE,incomeResult.getPass()); assertEquals(true,
    incomeResult.getVariance() < varianceLimit);
    
    assertEquals("0.2", String.valueOf(varianceLimit));
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithCropWithReceivableMissingIncome() {

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      cropItem1.setInventoryItemCode(BARLEY_FEED_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(1.00);
      cropItem1.setReportedQuantityEnd(100.0);
      cropItem1.setReportedQuantityStart(200.0);
      cropItem1.setReportedPriceEnd(1.00);
      cropItem1.setLineItem(BARLEY_LINE_ITEM);
      cropItem1.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      cropItems.add(cropItem1);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BARLEY_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setLineItem(BARLEY_LINE_ITEM);
      receivableItem.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      receivableItem.setFruitVegTypeCode(null);
      receivableItem.setFruitVegTypeCodeDescription(null);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setAdjStartOfYearAmount(1.0);
      receivableItem.setReportedEndOfYearAmount(18.0);
      receivableItem.setAdjEndOfYearAmount(2.0);
      receivableItems.add(receivableItem);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);

    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    @SuppressWarnings("unused")
    double varianceLimit = config.getRevenueRiskForageVariance();
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
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INCOME_FOR_RECEIVABLE,
            BARLEY_LINE_ITEM.toString(), BARLEY_LINE_ITEM_DESCRIPTION),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());

    assertNotNull(revenueRiskResult.getForageGrainInventory());
    assertEquals(1, revenueRiskResult.getForageGrainInventory().size());

    RevenueRiskInventoryItem invItemResult = revenueRiskResult.getForageGrainInventory().get(0);
    assertEquals(CropUnitCodes.TONNES, invItemResult.getCropUnitCode());
    assertEquals(CropUnitCodes.TONNES_DESCRIPTION, invItemResult.getCropUnitCodeDescription());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_CODE, invItemResult.getInventoryItemCode());
    assertEquals(BARLEY_FEED_INVENTORY_ITEM_DESCRIPTION, invItemResult.getInventoryItemCodeDescription());
    assertEquals("1.0", String.valueOf(invItemResult.getReportedPrice()));
    assertEquals("1.0", String.valueOf(invItemResult.getQuantityProduced()));
    assertEquals("200.0", String.valueOf(invItemResult.getQuantityStart()));
    assertEquals("100.0", String.valueOf(invItemResult.getQuantityEnd()));

    assertEquals("101.0", String.valueOf(invItemResult.getQuantitySold()));
    assertEquals("101.0", String.valueOf(invItemResult.getExpectedRevenue()));
    assertEquals(BARLEY_LINE_ITEM, invItemResult.getLineItemCode());
    assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, invItemResult.getLineItemDescription());

    assertNotNull(revenueRiskResult.getForageGrainIncomes());
    assertEquals(1, revenueRiskResult.getForageGrainIncomes().size());
    RevenueRiskIncomeTestResult incomeResult = revenueRiskResult.getForageGrainIncomes().get(0);
    assertNotNull(incomeResult);
    assertEquals(BARLEY_LINE_ITEM, incomeResult.getLineItemCode());
    assertEquals(BARLEY_LINE_ITEM_DESCRIPTION, incomeResult.getDescription());
    assertEquals("-10.0", String.valueOf(incomeResult.getReportedRevenue()));
    assertEquals("101.0", String.valueOf(incomeResult.getExpectedRevenue()));
    assertEquals("-1.099", String.valueOf(incomeResult.getVariance()));
    assertEquals(Boolean.FALSE, incomeResult.getPass());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }


  @Test
  public void failWithReceivableMissingCropMissingIncome() {
    
    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BARLEY_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setLineItem(BARLEY_LINE_ITEM);
      receivableItem.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      receivableItem.setFruitVegTypeCode(null);
      receivableItem.setFruitVegTypeCodeDescription(null);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setAdjStartOfYearAmount(1.0);
      receivableItem.setReportedEndOfYearAmount(18.0);
      receivableItem.setAdjEndOfYearAmount(2.0);
      receivableItems.add(receivableItem);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);
    scenario.getFarmingYear().getFarmingOperations().get(0).setReceivableItems(receivableItems);

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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(2, revenueRiskResult.getErrorMessages().size());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INVENTORY_FOR_RECEIVABLE,
            BARLEY_LINE_ITEM, BARLEY_LINE_ITEM_DESCRIPTION),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INCOME_FOR_RECEIVABLE,
            BARLEY_LINE_ITEM, BARLEY_LINE_ITEM_DESCRIPTION),
        revenueRiskResult.getErrorMessages().get(1).getMessage());

    assertNotNull(revenueRiskResult.getFruitVegResults());
    assertEquals(0, revenueRiskResult.getFruitVegResults().size());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }


  @Test
  public void failWithIncomeWithReceivableMissingCrop() {
    
    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      lineItem.setFruitVegTypeCode(null);
      lineItem.setFruitVegTypeCodeDescription(null);
      lineItem.setLineItem(BARLEY_LINE_ITEM);
      lineItem.setDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(Boolean.FALSE);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(29.0);
      incomeExpenses.add(incExp);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BARLEY_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setLineItem(BARLEY_LINE_ITEM);
      receivableItem.setLineItemDescription(BARLEY_LINE_ITEM_DESCRIPTION);
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.GRAIN);
      receivableItem.setFruitVegTypeCode(null);
      receivableItem.setFruitVegTypeCodeDescription(null);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setAdjStartOfYearAmount(1.0);
      receivableItem.setReportedEndOfYearAmount(18.0);
      receivableItem.setAdjEndOfYearAmount(2.0);
      receivableItems.add(receivableItem);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);
    scenario.getFarmingYear().getFarmingOperations().get(0).setReceivableItems(receivableItems);

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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INVENTORY_FOR_INCOME,
            BARLEY_LINE_ITEM, BARLEY_LINE_ITEM_DESCRIPTION),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertNotNull(revenueRiskResult.getFruitVegResults());
    assertEquals(0, revenueRiskResult.getFruitVegResults().size());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }
}
