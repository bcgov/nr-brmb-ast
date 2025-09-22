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
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskFruitVegItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestFruitVegRevenueRiskTest {
  private static Logger logger = LoggerFactory.getLogger(TestFruitVegRevenueRiskTest.class);

  private static final String APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION = "Apple";

  private static final String BEET_FRUIT_VEG_TYPE_CODE = "BEET";
  private static final String BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION = "Beet";

  private static final String CHERRY_FRUIT_VEG_TYPE_CODE = "CHERRY";
  private static final String CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION = "Cherries";

  private static final String APPLES_INVENTORY_ITEM_CODE = "5030";
  private static final String APPLES_INVENTORY_ITEM_DESCRIPTION = "Apples";

  private static final String APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_CODE = "4812";
  private static final String APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_DESCRIPTION = "Apples; Ambrosia; 1st year of fruit production; high density";

  private static final String APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_CODE = "4824";
  private static final String APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_DESCRIPTION = "Apples; Gala; 2nd to 4th year of fruit production; medium density";

  private static final String BEETS_INVENTORY_ITEM_CODE = "7000";
  private static final String BEETS_INVENTORY_ITEM_DESCRIPTION = "Beets";

  private static final String CHERRIES_SOUR_INVENTORY_ITEM_CODE = "5038";
  private static final String CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION = "Cherries; Sour";
  
  private static final String CHERRIES_SWEET_INVENTORY_ITEM_CODE = "5040";
  private static final String CHERRIES_SWEET_INVENTORY_ITEM_DESCRIPTION = "Cherries; Sweet";

  private static final Integer BEETS_LINE_ITEM = 162;
  private static final String BEETS_LINE_ITEM_DESCRIPTION = "Beets - Field fresh";

  private static final Integer CHERRIES_LINE_ITEM = 92;
  private static final String CHERRIES_LINE_ITEM_DESCRIPTION = "Cherries (sweet; sour)";

  private static final Integer APPLES_LINE_ITEM = 60;
  private static final String APPLES_LINE_ITEM_DESCRIPTION = "Apples";
  
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
    FarmingOperation farmOp = new FarmingOperation();
    farmOp.setCropItems(cropItems);

    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    farmYear.setMarginTotal(new MarginTotal());
    scenario.setFarmingYear(farmYear);

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
    scenario.setIsInCombinedFarmInd(true);
    scenario2.setCombinedFarm(combinedFarm);
    scenario2.setIsInCombinedFarmInd(true);

    FarmingYear farmYear = new FarmingYear();
    FarmingOperation farmOp = new FarmingOperation();
    farmOp.setCropItems(cropItems);

    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    scenario.setFarmingYear(farmYear);

    FarmingYear farmYear2 = new FarmingYear();
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOp2.setCropItems(cropItems2);

    List<FarmingOperation> farmOps2 = new ArrayList<>();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    scenario2.setFarmingYear(farmYear2);

    return scenario;
  }

  @Test
  public void passHappyPath() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(5.00);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();

      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("500.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.05", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
      }
    }
    
    assertEquals(true, revenueRiskResult.getFruitVegResults().get(0).getVariance() < varianceLimit);
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithVarianceLessThanLimitForTwoFruitVegTypes() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(5.00);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem2.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    cropItem2.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem2.setInventoryItemCode(CHERRIES_SOUR_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem2.setReportedQuantityProduced(99.00);
    cropItem2.setFmvEnd(4.00);
    cropItem2.setLineItem(CHERRIES_LINE_ITEM);
    cropItem2.setLineItemDescription(CHERRIES_LINE_ITEM_DESCRIPTION);
    cropItem2.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem2.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    lineItem2.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem2.setLineItem(CHERRIES_LINE_ITEM);
    lineItem2.setDescription(CHERRIES_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(450.0);

    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);
    incomeExpenses.add(incExp2);

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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(2, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("99.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("4.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("396.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(CHERRIES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(CHERRIES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(2, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
    
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("500.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.05", String.valueOf(result.getVariance()));
        assertEquals(true, result.getVariance() < varianceLimit);
        assertEquals(Boolean.TRUE, result.getPass());
      }

      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("99.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("450.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("396.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("4.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.136", String.valueOf(result.getVariance()));
        assertEquals(true, result.getVariance() < varianceLimit);
        assertEquals(Boolean.TRUE, result.getPass());
      }
    }
  }

  @Test
  public void passWithNoItemsAndNoLineItems() {

    List<CropItem> cropItems = new ArrayList<>();

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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    assertNotNull(revenueRiskResult.getFruitVegInventory());
    assertEquals(0, revenueRiskResult.getFruitVegInventory().size());
    
    assertNotNull(revenueRiskResult.getFruitVegResults());
    assertEquals(0, revenueRiskResult.getFruitVegResults().size());

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithVarianceLessThanLimitWithDuplicateItems() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(5.00);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem2.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem2.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem2.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem2.setReportedQuantityProduced(280.00);
    cropItem2.setFmvEnd(5.00);
    cropItem2.setLineItem(BEETS_LINE_ITEM);
    cropItem2.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem2.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(2000.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);
    scenario.getFarmingYear().getMarginTotal().setBpuLeadInd(Boolean.TRUE);

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("380.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("1900.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("380.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("2000.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("1900.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.053", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithVarianceLessThanLimitWithDuplicateItems() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(5.00);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem2.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem2.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem2.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem2.setReportedQuantityProduced(200.00);
    cropItem2.setFmvEnd(5.00);
    cropItem2.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem2.setLineItem(BEETS_LINE_ITEM);
    cropItem2.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(2000.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    scenario.getFarmingYear().getFarmingOperations().get(0).setIncomeExpenses(incomeExpenses);
    scenario.getFarmingYear().getMarginTotal().setBpuLeadInd(Boolean.TRUE);

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("300.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("1500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
  
      assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
    
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("300.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("2000.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("1500.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.333", String.valueOf(result.getVariance()));
        assertEquals(Boolean.FALSE, result.getPass());
        assertEquals(true, result.getVariance() > varianceLimit);
      }
    }
  }

  @Test
  public void failWithVarianceGreaterThanLimit() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(3.00);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("3.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("300.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();

      {
    
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
    
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("300.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("3.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.75", String.valueOf(result.getVariance()));
        assertEquals(Boolean.FALSE, result.getPass());
        assertEquals(true, result.getVariance() > varianceLimit);
      }
    }
    
    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithLineItemProvidedButNoInvItems() {

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

    List<CropItem> cropItems = new ArrayList<>();

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
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INVENTORY_FOR_INCOME, lineItem1.getLineItem(), lineItem1.getDescription()),
        scenario.getReasonabilityTestResults().getRevenueRiskTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());

    // Inventory Results
    List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
    assertEquals(0, inventoryResults.size());

    // Fruit & Veg Type Results
    assertNotNull(revenueRiskResult.getFruitVegResults());
    assertEquals(0, revenueRiskResult.getFruitVegResults().size());

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithNoLineItemProvided() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(3.00);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

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
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(FruitVegRevenueRiskSubTest.ERROR_FRUIT_VEG_TYPE_MISSING_LINE_ITEM, cropItem1.getFruitVegTypeCodeDescription()),
        scenario.getReasonabilityTestResults().getRevenueRiskTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("3.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("300.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      RevenueRiskFruitVegItemTestResult result = iterator.next();
      assertNotNull(result);

      {
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("300.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("3.0", String.valueOf(result.getExpectedPrice()));
        assertNull(result.getReportedRevenue());
        assertNull(result.getVariance());
        assertEquals(Boolean.FALSE, result.getPass());
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithItemMissingVarianceLimit() {

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(3.00);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
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
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(FruitVegRevenueRiskSubTest.ERROR_FRUIT_VEG_MISSING_VARIANCE_LIMIT, cropItem1.getFruitVegTypeCode(), cropItem1.getFruitVegTypeCodeDescription()),
        scenario.getReasonabilityTestResults().getRevenueRiskTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("3.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("300.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertNull(inventoryResult.getVarianceLimit());
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertNull(result.getVarianceLimit());
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("300.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("3.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.75", String.valueOf(result.getVariance()));
        assertEquals(Boolean.FALSE, result.getPass());
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failForNoFMV() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
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
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    assertEquals(false, revenueRiskResult.getResult().booleanValue());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_INVENTORY_MISSING_FMV, cropItem1.getInventoryItemCode(), cropItem1.getInventoryItemCodeDescription()),
        scenario.getReasonabilityTestResults().getRevenueRiskTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertNull(inventoryResult.getFmvPrice());
        assertNull(inventoryResult.getExpectedRevenue());
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("0.0", String.valueOf(result.getExpectedRevenue()));
        assertNull(result.getExpectedPrice());
        assertNull(result.getVariance());
        assertEquals(Boolean.FALSE, result.getPass());
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithApples() {
    Double varianceLimit = .2;

    List<CropItem> cropItems = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();

    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      cropItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem.setInventoryItemCode(APPLES_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(APPLES_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setReportedQuantityProduced(200.00);
      cropItem.setFmvEnd(5.00);
      cropItem.setLineItem(APPLES_LINE_ITEM);
      cropItem.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      cropItem.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      lineItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      lineItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      lineItem.setLineItem(APPLES_LINE_ITEM);
      lineItem.setDescription(APPLES_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(Boolean.FALSE);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(13.0);
      incomeExpenses.add(incExp);
    }
    
    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      receivableItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      receivableItem.setInventoryItemCode(APPLES_INVENTORY_ITEM_CODE);
      receivableItem.setInventoryItemCodeDescription(APPLES_INVENTORY_ITEM_DESCRIPTION);
      receivableItem.setLineItem(APPLES_LINE_ITEM);
      receivableItem.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setReportedEndOfYearAmount(28.0);
      receivableItem.setAdjEndOfYearAmount(1.0);
      receivableItems.add(receivableItem);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);
    farmingOperation.setGalaAppleFmvPrice(Double.valueOf(0.13));

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(APPLES_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(APPLES_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(FruitVegTypeCodes.APPLE, inventoryResult.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("200.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("0.13", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("26.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(APPLES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(APPLES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(FruitVegTypeCodes.APPLE, result.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("200.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("29.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("26.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("0.13", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.115", String.valueOf(result.getVariance()));
        assertEquals(true, result.getVariance() < varianceLimit);
        assertEquals(Boolean.TRUE, result.getPass());
      }
    }

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithApplesMissingFmv() {
    Double varianceLimit = .2;

    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem1.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      cropItem1.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem1.setReportedQuantityProduced(100.00);
      cropItem1.setFmvEnd(5.00);
      cropItem1.setLineItem(APPLES_LINE_ITEM);
      cropItem1.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      cropItem1.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem1);
    }

    {
      CropItem cropItem2 = new CropItem();
      cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem2.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      cropItem2.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem2.setInventoryItemCode(APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_CODE);
      cropItem2.setInventoryItemCodeDescription(APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_DESCRIPTION);
      cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem2.setReportedQuantityProduced(100.00);
      cropItem2.setFmvEnd(4.00);
      cropItem2.setLineItem(APPLES_LINE_ITEM);
      cropItem2.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      cropItem2.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem2);
    }
    
    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      receivableItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      receivableItem.setInventoryItemCode(APPLES_INVENTORY_ITEM_CODE);
      receivableItem.setInventoryItemCodeDescription(APPLES_INVENTORY_ITEM_DESCRIPTION);
      receivableItem.setLineItem(APPLES_LINE_ITEM);
      receivableItem.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setReportedEndOfYearAmount(29.0);
      receivableItems.add(receivableItem);
    }

    {
      LineItem lineItem1 = new LineItem();
      lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      lineItem1.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      lineItem1.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      lineItem1.setLineItem(APPLES_LINE_ITEM);
      lineItem1.setDescription(APPLES_LINE_ITEM_DESCRIPTION);
  
      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem1);
      incExp1.setReportedAmount(29.0);
      incomeExpenses.add(incExp1);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);

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
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_INVENTORY_MISSING_FMV, APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_CODE, APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_DESCRIPTION),
        scenario.getReasonabilityTestResults().getRevenueRiskTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_INVENTORY_MISSING_FMV, APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_CODE, APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_DESCRIPTION),
        scenario.getReasonabilityTestResults().getRevenueRiskTest().getMessages().get(MessageTypeCodes.ERROR).get(1).getMessage());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(2, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(FruitVegTypeCodes.APPLE, inventoryResult.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertNull(inventoryResult.getFmvPrice());
        assertNull(inventoryResult.getExpectedRevenue());
        assertEquals(String.valueOf(APPLES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(APPLES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_2ND_TO_4TH_YEAR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(FruitVegTypeCodes.APPLE, inventoryResult.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertNull(inventoryResult.getFmvPrice());
        assertNull(inventoryResult.getExpectedRevenue());
        assertEquals(String.valueOf(APPLES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(APPLES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(FruitVegTypeCodes.APPLE, result.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("200.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("29.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("0.0", String.valueOf(result.getExpectedRevenue()));
        assertNull(result.getExpectedPrice());
        assertNull(result.getVariance());
        assertEquals(Boolean.FALSE, result.getPass());
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void failWithApplesZeroIncome() {
    Double varianceLimit = .2;

    List<CropItem> cropItems = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();

    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      cropItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem.setInventoryItemCode(APPLES_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(APPLES_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setReportedQuantityProduced(200.00);
      cropItem.setFmvEnd(5.00);
      cropItem.setLineItem(APPLES_LINE_ITEM);
      cropItem.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      cropItem.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem);
    }

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      lineItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      lineItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      lineItem.setLineItem(APPLES_LINE_ITEM);
      lineItem.setDescription(APPLES_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(Boolean.FALSE);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(13.0);
      incomeExpenses.add(incExp);
    }
    
    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      receivableItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      receivableItem.setInventoryItemCode(APPLES_INVENTORY_ITEM_CODE);
      receivableItem.setInventoryItemCodeDescription(APPLES_INVENTORY_ITEM_DESCRIPTION);
      receivableItem.setLineItem(APPLES_LINE_ITEM);
      receivableItem.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setReportedStartOfYearAmount(1.0);
      receivableItem.setAdjStartOfYearAmount(2.0);
      receivableItem.setReportedEndOfYearAmount(null);
      receivableItem.setAdjEndOfYearAmount(null);
      receivableItems.add(receivableItem);
    }

    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);
    farmingOperation.setGalaAppleFmvPrice(Double.valueOf(0.13));

    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(APPLES_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(APPLES_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(FruitVegTypeCodes.APPLE, inventoryResult.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("200.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("0.13", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("26.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(APPLES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(APPLES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(FruitVegTypeCodes.APPLE, result.getFruitVegTypeCode());
        assertEquals(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("200.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertNull(result.getReportedRevenue());
        assertEquals("26.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("0.13", String.valueOf(result.getExpectedPrice()));
        assertNull(result.getVariance());
        assertEquals(false, result.getPass());
      }
    }

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    {
      Iterator<ReasonabilityTestResultMessage> iterator = scenario.getReasonabilityTestResults().getRevenueRiskTest().getErrorMessages().iterator();
      {
        ReasonabilityTestResultMessage message = iterator.next();
        assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(FruitVegRevenueRiskSubTest.ERROR_FRUIT_VEG_TYPE_MISSING_LINE_ITEM, APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION),
            message.getMessage());
      }
      
      assertEquals(false, revenueRiskResult.getResult());
    }
  }
  
  @Test
  public void failWithApplesMissingInventory() {
    
    List<CropItem> cropItems = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    
    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      lineItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      lineItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      lineItem.setLineItem(APPLES_LINE_ITEM);
      lineItem.setDescription(APPLES_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(Boolean.FALSE);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(13.0);
      incomeExpenses.add(incExp);
    }
    
    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(FruitVegTypeCodes.APPLE);
      receivableItem.setFruitVegTypeCodeDescription(APPLE_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      receivableItem.setInventoryItemCode(APPLES_INVENTORY_ITEM_CODE);
      receivableItem.setInventoryItemCodeDescription(APPLES_INVENTORY_ITEM_DESCRIPTION);
      receivableItem.setLineItem(APPLES_LINE_ITEM);
      receivableItem.setLineItemDescription(APPLES_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setReportedStartOfYearAmount(29.0);
      receivableItem.setReportedEndOfYearAmount(29.0);
      receivableItems.add(receivableItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperations().get(0);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setReceivableItems(receivableItems);
    farmingOperation.setGalaAppleFmvPrice(Double.valueOf(0.13));
    
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    
    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    
    // Inventory Results
    List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
    assertEquals(0, inventoryResults.size());

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(0, revenueRiskResult.getFruitVegResults().size());
    }

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());
    
    {
      Iterator<ReasonabilityTestResultMessage> iterator = scenario.getReasonabilityTestResults().getRevenueRiskTest().getErrorMessages().iterator();
      {
        ReasonabilityTestResultMessage message = iterator.next();
        assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(FruitVegRevenueRiskSubTest.ERROR_MISSING_INVENTORY_FOR_APPLE_RECEIVABLE),
            message.getMessage());
      }
      
      assertEquals(false, revenueRiskResult.getResult());
    }
  }

  @Test
  public void failWithDifferentFmvWithinSameFruitVegCategory() {
    Double varianceLimit = .2;

    List<CropItem> cropItems = new ArrayList<>();
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
      cropItem.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem.setInventoryItemCode(CHERRIES_SWEET_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(CHERRIES_SWEET_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setReportedQuantityProduced(100.00);
      cropItem.setFmvEnd(5.00);
      cropItem.setLineItem(CHERRIES_LINE_ITEM);
      cropItem.setLineItemDescription(CHERRIES_LINE_ITEM_DESCRIPTION);
      cropItem.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem);
    }

    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
      cropItem.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem.setInventoryItemCode(CHERRIES_SOUR_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setReportedQuantityProduced(100.00);
      cropItem.setFmvEnd(4.00);
      cropItem.setLineItem(CHERRIES_LINE_ITEM);
      cropItem.setLineItemDescription(CHERRIES_LINE_ITEM_DESCRIPTION);
      cropItem.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem);
    }

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(CHERRIES_LINE_ITEM);
    lineItem1.setDescription(CHERRIES_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    incomeExpenses.add(incExp1);

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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    assertEquals(0, revenueRiskResult.getWarningMessages().size());

    // Inventory Results
    List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
    assertEquals(2, inventoryResults.size());
    {
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("4.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("400.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(CHERRIES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(CHERRIES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(CHERRIES_SWEET_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(CHERRIES_SWEET_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(CHERRIES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(CHERRIES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("200.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("900.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("4.5", String.valueOf(result.getExpectedPrice()));
        assertEquals("-0.417", String.valueOf(result.getVariance()));
        assertEquals(Boolean.FALSE, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithDifferentFmvWithinSameFruitVegCategory() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(CHERRIES_SOUR_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(5.00);
    cropItem1.setLineItem(CHERRIES_LINE_ITEM);
    cropItem1.setLineItemDescription(CHERRIES_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem2.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    cropItem2.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem2.setInventoryItemCode(CHERRIES_SWEET_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(CHERRIES_SWEET_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem2.setReportedQuantityProduced(100.00);
    cropItem2.setFmvEnd(4.00);
    cropItem2.setLineItem(CHERRIES_LINE_ITEM);
    cropItem2.setLineItemDescription(CHERRIES_LINE_ITEM_DESCRIPTION);
     cropItem2.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(CHERRIES_LINE_ITEM);
    lineItem1.setDescription(CHERRIES_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(1025.0);
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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    assertEquals(0, revenueRiskResult.getWarningMessages().size());

    assertNotNull(revenueRiskResult.getFruitVegResults());
    assertEquals(1, revenueRiskResult.getFruitVegResults().size());

    
    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(2, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(CHERRIES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(CHERRIES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(CHERRIES_SWEET_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(CHERRIES_SWEET_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("4.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("400.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(CHERRIES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(CHERRIES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("200.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("1025.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("900.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("4.5", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.139", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }

  @Test
  public void passWithTwoItemsWithCombinedFarm() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setReportedQuantityProduced(100.00);
    cropItem1.setFmvEnd(5.00);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem2.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    cropItem2.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem2.setInventoryItemCode(CHERRIES_SOUR_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem2.setReportedQuantityProduced(99.00);
    cropItem2.setFmvEnd(4.00);
    cropItem2.setLineItem(CHERRIES_LINE_ITEM);
    cropItem2.setLineItemDescription(CHERRIES_LINE_ITEM_DESCRIPTION);
    cropItem2.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    LineItem lineItem2 = new LineItem();
    lineItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem2.setFruitVegTypeCode(CHERRY_FRUIT_VEG_TYPE_CODE);
    lineItem2.setFruitVegTypeCodeDescription(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem2.setLineItem(CHERRIES_LINE_ITEM);
    lineItem2.setDescription(CHERRIES_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);

    IncomeExpense incExp2 = new IncomeExpense();
    incExp2.setIsExpense(Boolean.FALSE);
    incExp2.setLineItem(lineItem2);
    incExp2.setReportedAmount(450.0);

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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());


    
    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(2, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CHERRIES_SOUR_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("99.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("4.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("396.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(CHERRIES_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(CHERRIES_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(2, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("500.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.05", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(CHERRY_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("99.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("450.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("396.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("4.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.136", String.valueOf(result.getVariance()));
        assertEquals(true, result.getVariance() < varianceLimit);
        assertEquals(Boolean.TRUE, result.getPass());
      }
    }
    
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }


  @Test
  public void passWithReceivable() {
    final Double varianceLimit = .2;
    
    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem1.setReportedQuantityProduced(100.00);
      cropItem1.setFmvEnd(5.00);
      cropItem1.setLineItem(BEETS_LINE_ITEM);
      cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
      cropItem1.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem1);
    }

    {
      LineItem lineItem1 = new LineItem();
      lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      lineItem1.setLineItem(BEETS_LINE_ITEM);
      lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

      IncomeExpense incExp1 = new IncomeExpense();
      incExp1.setIsExpense(Boolean.FALSE);
      incExp1.setLineItem(lineItem1);
      incExp1.setReportedAmount(525.0);
      incomeExpenses.add(incExp1);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BEETS_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
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
    assertEquals(0, revenueRiskResult.getErrorMessages().size());

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      RevenueRiskFruitVegItemTestResult result = iterator.next();
      assertNotNull(result);

      {
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("515.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("500.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.03", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }

    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }


  @Test
  public void failWithCropWithReceivableMissingIncome() {
    final Double varianceLimit = .2;
    
    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
      cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem1.setReportedQuantityProduced(100.00);
      cropItem1.setFmvEnd(5.00);
      cropItem1.setLineItem(BEETS_LINE_ITEM);
      cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
      cropItem1.setRevenueVarianceLimit(varianceLimit);
      cropItems.add(cropItem1);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BEETS_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setLineItem(BEETS_LINE_ITEM);
      receivableItem.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      receivableItem.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
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
        ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INCOME_FOR_RECEIVABLE,
            BEETS_LINE_ITEM, BEETS_LINE_ITEM_DESCRIPTION),
        revenueRiskResult.getErrorMessages().get(0).getMessage());

    assertNotNull(revenueRiskResult.getFruitVegResults());
    assertEquals(1, revenueRiskResult.getFruitVegResults().size());


    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("100.0", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("500.0", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      RevenueRiskFruitVegItemTestResult result = iterator.next();
      assertNotNull(result);

      {
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("100.0", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("-10.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("500.0", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("-1.02", String.valueOf(result.getVariance()));
        assertEquals(Boolean.FALSE, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }


  @Test
  public void failWithReceivableMissingCropMissingIncome() {
    
    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BEETS_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setLineItem(BEETS_LINE_ITEM);
      receivableItem.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      receivableItem.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
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
    

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(0, inventoryResults.size());
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(0, revenueRiskResult.getFruitVegResults().size());
    }

    {
      assertNotNull(revenueRiskResult.getMessages());
      assertNotNull(revenueRiskResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
      assertEquals(0, revenueRiskResult.getInfoMessages().size());
      assertEquals(0, revenueRiskResult.getWarningMessages().size());
      assertEquals(2, revenueRiskResult.getErrorMessages().size());
  
      {
        Iterator<ReasonabilityTestResultMessage> iterator = revenueRiskResult.getErrorMessages().iterator();
        
        assertEquals(
            ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INCOME_FOR_RECEIVABLE,
                BEETS_LINE_ITEM, BEETS_LINE_ITEM_DESCRIPTION),
            iterator.next().getMessage());
        assertEquals(
            ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INVENTORY_FOR_RECEIVABLE,
                BEETS_LINE_ITEM, BEETS_LINE_ITEM_DESCRIPTION),
            iterator.next().getMessage());
      }
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }


  @Test
  public void failWithIncomeWithReceivableMissingCrop() {
    
    List<CropItem> cropItems = new ArrayList<>();
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    List<ReceivableItem> receivableItems = new ArrayList<>();

    {
      LineItem lineItem = new LineItem();
      lineItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      lineItem.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      lineItem.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
      lineItem.setLineItem(BEETS_LINE_ITEM);
      lineItem.setDescription(BEETS_LINE_ITEM_DESCRIPTION);
      
      IncomeExpense incExp = new IncomeExpense();
      incExp.setIsExpense(Boolean.FALSE);
      incExp.setLineItem(lineItem);
      incExp.setReportedAmount(29.0);
      incomeExpenses.add(incExp);
    }

    {
      ReceivableItem receivableItem = new ReceivableItem();
      receivableItem.setInventoryItemCode(BEETS_LINE_ITEM.toString());
      receivableItem.setInventoryItemCodeDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      receivableItem.setLineItem(BEETS_LINE_ITEM);
      receivableItem.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
      receivableItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      receivableItem.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
      receivableItem.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
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

    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(0, inventoryResults.size());
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(0, revenueRiskResult.getFruitVegResults().size());
    }
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(1, revenueRiskResult.getErrorMessages().size());

    {
      Iterator<ReasonabilityTestResultMessage> iterator = revenueRiskResult.getErrorMessages().iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(RevenueRiskTest.ERROR_MISSING_INVENTORY_FOR_INCOME,
              BEETS_LINE_ITEM, BEETS_LINE_ITEM_DESCRIPTION),
          iterator.next().getMessage());
    }

    assertEquals(Boolean.FALSE, revenueRiskResult.getResult());
  }

  @Test
  public void passReportedInTonnes() {
    Double varianceLimit = .2;

    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setReportedQuantityProduced(0.045);
    cropItem1.setFmvEnd(11023.11);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);

    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);

    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("99.208", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("496.04", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }

    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();

      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("99.208", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("496.04", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.058", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
      }
    }
    
    assertEquals(true, revenueRiskResult.getFruitVegResults().get(0).getVariance() < varianceLimit);
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }
  
  
  @Test
  public void passReportedInKg() {
    Double varianceLimit = .2;
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    cropItem1.setInventoryItemCode(BEETS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(BEETS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.KILOGRAM);
    cropItem1.setReportedQuantityProduced(45.0);
    cropItem1.setFmvEnd(11.02);
    cropItem1.setLineItem(BEETS_LINE_ITEM);
    cropItem1.setLineItemDescription(BEETS_LINE_ITEM_DESCRIPTION);
    cropItem1.setRevenueVarianceLimit(varianceLimit);
    
    LineItem lineItem1 = new LineItem();
    lineItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    lineItem1.setFruitVegTypeCode(BEET_FRUIT_VEG_TYPE_CODE);
    lineItem1.setFruitVegTypeCodeDescription(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION);
    lineItem1.setLineItem(BEETS_LINE_ITEM);
    lineItem1.setDescription(BEETS_LINE_ITEM_DESCRIPTION);
    
    IncomeExpense incExp1 = new IncomeExpense();
    incExp1.setIsExpense(Boolean.FALSE);
    incExp1.setLineItem(lineItem1);
    incExp1.setReportedAmount(525.0);
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
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    assertEquals(0, revenueRiskResult.getErrorMessages().size());
    
    // Inventory Results
    {
      List<RevenueRiskInventoryItem> inventoryResults = revenueRiskResult.getFruitVegInventory();
      assertEquals(1, inventoryResults.size());
      Iterator<RevenueRiskInventoryItem> iterator = inventoryResults.iterator();
      {
        RevenueRiskInventoryItem inventoryResult = iterator.next();
        assertEquals(BEETS_INVENTORY_ITEM_CODE, inventoryResult.getInventoryItemCode());
        assertEquals(BEETS_INVENTORY_ITEM_DESCRIPTION, inventoryResult.getInventoryItemCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, inventoryResult.getCropUnitCode());
        assertEquals(CropUnitCodes.POUNDS_DESCRIPTION, inventoryResult.getCropUnitCodeDescription());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, inventoryResult.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, inventoryResult.getFruitVegTypeCodeDescription());
        assertEquals("99.208", String.valueOf(inventoryResult.getQuantityProduced()));
        assertEquals("5.0", String.valueOf(inventoryResult.getFmvPrice()));
        assertEquals("496.04", String.valueOf(inventoryResult.getExpectedRevenue()));
        assertEquals(String.valueOf(BEETS_LINE_ITEM), String.valueOf(inventoryResult.getLineItemCode()));
        assertEquals(BEETS_LINE_ITEM_DESCRIPTION, String.valueOf(inventoryResult.getLineItemDescription()));
        assertEquals("0.2", String.valueOf(inventoryResult.getVarianceLimit()));
      }
    }
    
    // Fruit & Veg Type Results
    {
      assertNotNull(revenueRiskResult.getFruitVegResults());
      assertEquals(1, revenueRiskResult.getFruitVegResults().size());
      Iterator<RevenueRiskFruitVegItemTestResult> iterator = revenueRiskResult.getFruitVegResults().iterator();
      
      {
        RevenueRiskFruitVegItemTestResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEET_FRUIT_VEG_TYPE_CODE_DESCRIPTION, result.getFruitVegTypeDesc());
        assertEquals("99.208", String.valueOf(result.getQuantityProduced()));
        assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
        assertEquals("525.0", String.valueOf(result.getReportedRevenue()));
        assertEquals("496.04", String.valueOf(result.getExpectedRevenue()));
        assertEquals("5.0", String.valueOf(result.getExpectedPrice()));
        assertEquals("0.058", String.valueOf(result.getVariance()));
        assertEquals(Boolean.TRUE, result.getPass());
      }
    }
    
    assertEquals(true, revenueRiskResult.getFruitVegResults().get(0).getVariance() < varianceLimit);
    assertEquals(Boolean.TRUE, revenueRiskResult.getResult());
  }
}
