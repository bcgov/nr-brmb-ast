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
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.HogsRevenueRiskSubTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskInventoryItem;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class TestHogsRevenueRiskTest {

  private static Logger logger = LoggerFactory.getLogger(TestHogsRevenueRiskTest.class);

  private static final String BOARS_INVENTORY_ITEM_CODE = InventoryItemCodes.HOGS_BOARS_BREEDING;
  private static final String BOARS_INVENTORY_ITEM_DESCRIPTION = InventoryItemCodes.HOGS_BOARS_BREEDING_DESCRIPTION;

  private static final String SOWS_INVENTORY_ITEM_CODE = InventoryItemCodes.HOGS_SOWS_BREEDING;
  private static final String SOWS_INVENTORY_ITEM_DESCRIPTION = InventoryItemCodes.HOGS_SOWS_BREEDING_DESCRIPTION;

  private static final String FEEDER_BIRTH_18_INVENTORY_ITEM_CODE = InventoryItemCodes.HOGS_FEEDER_BIRTH;
  private static final String FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION = InventoryItemCodes.HOGS_FEEDER_BIRTH_DESCRIPTION;

  private static final String FEEDER_19_TO_36_INVENTORY_ITEM_CODE = "8764";
  private static final String FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION = "Hogs; Feeder; 19 lbs. - 36 lbs.";

  private static final String FEEDER_66_TO_100_INVENTORY_ITEM_CODE = "8766";
  private static final String FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION = "Hogs; Feeder; 66 lbs. - 100 lbs.";

  private static final String FEEDER_101_TO_140_INVENTORY_ITEM_CODE = "8767";
  private static final String FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION = "Hogs; Feeder; 101 lbs. - 140 lbs.";

  private static final String FEEDER_181_TO_220_INVENTORY_ITEM_CODE = "8769";
  private static final String FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION = "Hogs; Feeder; 181 lbs. - 220 lbs.";

  private static final String FEEDER_221_TO_240_INVENTORY_ITEM_CODE = "8770";
  private static final String FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION = "Hogs; Feeder; 221 lbs. - 240+ lbs.";


  private static final Integer HOGS_LINE_ITEM = 341;
  private static final String HOGS_LINE_ITEM_DESCRIPTION = "Swine/hogs";

  private static final String HOGS_COMMODITY_CODE_DESCRIPTION = "Hogs";

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }

  private Scenario buildScenarioWithNoCombinedFarm() {
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
    
    farmOp.setSchedule("A");
    farmOp.setOperationNumber(1);
    farmOp.setPartnershipPercent(0.5);

    return scenario;
  }

  private Scenario buildScenarioWithCombinedFarm() {

    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setIsInCombinedFarmInd(true);
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
    scenario2.setIsInCombinedFarmInd(true);

    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    FarmingOperation farmOp = new FarmingOperation();
    farmOp.setSchedule("A");
    farmOp.setOperationNumber(1);
    farmOp.setPartnershipPercent(0.5);

    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);

    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOp2.setSchedule("A");
    farmOp2.setOperationNumber(1);
    farmOp2.setPartnershipPercent(0.1);

    List<FarmingOperation> farmOps2 = new ArrayList<>();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);

    return scenario;
  }

  @Test
  public void passFarrowToFinish() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
        pu.setReportedAmount(650.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(2400000.0);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(7, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertEquals("666.0", String.valueOf(hogsResult.getBoarPurchasePrice()));
    assertEquals("1332.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("2.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertEquals("270.0", String.valueOf(hogsResult.getSowPurchasePrice()));
    assertEquals("20462.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("76.0", String.valueOf(hogsResult.getSowPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3251.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("2889.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("78.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("8159.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("122.72", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("1001272.48", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("1200000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.198", String.valueOf(hogsResult.getRevenueVariance()));
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(true, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(true, revenueRiskResult.getResult());
  }

  @Test
  public void failFarrowToFinishOutsideVariance() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
        pu.setReportedAmount(650.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(1600000.0);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(7, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertEquals("666.0", String.valueOf(hogsResult.getBoarPurchasePrice()));
    assertEquals("1332.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("2.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertEquals("270.0", String.valueOf(hogsResult.getSowPurchasePrice()));
    assertEquals("20462.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("76.0", String.valueOf(hogsResult.getSowPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3251.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("2889.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("78.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("8159.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("122.72", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("1001272.48", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("800000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("-0.201", String.valueOf(hogsResult.getRevenueVariance()));
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void failFarrowToFinishMissingExpense() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
        pu.setReportedAmount(650.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(2413808.0);
        incomeExpenses.add(incExp);
      }

      {
        // missing expense
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(7, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("0.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("2.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3251.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("2.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2889.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("8083.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("122.72", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("991945.76", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("1206904.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.217", String.valueOf(hogsResult.getRevenueVariance()));

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(1, errorMessages.size());
    assertEquals(0, warningMessages.size());

    Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_HOG_EXPENSE),
        errorIterator.next().getMessage());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void failFarrowToFinishMissingBoarPrice() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(null); // missing boar price
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(null); // missing sow price
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
        pu.setReportedAmount(650.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(2413808.0);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(7, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertNull(item.getFmvPrice());
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertNull(item.getFmvPrice());
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("2.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3251.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("2.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2889.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("8083.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("122.72", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("991945.76", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("1206904.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.217", String.valueOf(hogsResult.getRevenueVariance()));

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(1, errorMessages.size());
    assertEquals(0, warningMessages.size());

    Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_PRICE, InventoryItemCodes.HOGS_BOARS_BREEDING, InventoryItemCodes.HOGS_BOARS_BREEDING_DESCRIPTION),
        errorIterator.next().getMessage());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void failFarrowToFinishMissingSowPrice() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(null); // missing sow price
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
        pu.setReportedAmount(650.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(2413808.0);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(7, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertNull(item.getFmvPrice());
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertEquals("666.0", String.valueOf(hogsResult.getBoarPurchasePrice()));
    assertEquals("1332.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("2.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("20462.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3251.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("2.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2889.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("8083.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("122.72", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("991945.76", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("1206904.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.217", String.valueOf(hogsResult.getRevenueVariance()));

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(1, errorMessages.size());
    assertEquals(0, warningMessages.size());

    Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_PRICE, InventoryItemCodes.HOGS_SOWS_BREEDING, InventoryItemCodes.HOGS_SOWS_BREEDING_DESCRIPTION),
        errorIterator.next().getMessage());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void failFarrowToFinishMissingInventoryMissingIncome() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      // Missing inventory, but has cattle inventory. Cattle inventory should be ignored.
      {
        LivestockItem item = new LivestockItem();
        item.setInventoryItemCode("1234");
        item.setInventoryItemCodeDescription("Not a hog");
        item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
        item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(666.0);
        item.setFmvEnd(600.0);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
        pu.setReportedAmount(650.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        // missing income
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(0, inventoryItems.size()); // missing inventory

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("7719.0", String.valueOf(hogsResult.getExpectedSold()));

    assertNull(hogsResult.getHeaviestHogFmvPrice());
    assertNull(hogsResult.getExpectedRevenue());
    assertEquals("0.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertNull(hogsResult.getRevenueVariance());

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(5, errorMessages.size());
    assertEquals(0, warningMessages.size());

    Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_REQUIRED_INVENTORY, InventoryItemCodes.HOGS_BOARS_BREEDING, InventoryItemCodes.HOGS_BOARS_BREEDING_DESCRIPTION),
        errorIterator.next().getMessage());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_REQUIRED_INVENTORY, InventoryItemCodes.HOGS_SOWS_BREEDING, InventoryItemCodes.HOGS_SOWS_BREEDING_DESCRIPTION),
        errorIterator.next().getMessage());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_INVENTORY),
        errorIterator.next().getMessage());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_HEAVIEST_HOG_FEEDER_PRICE),
        errorIterator.next().getMessage());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_INCOME),
        errorIterator.next().getMessage());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void passFarrowToFinishWithCombinedFarm() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double farrowToFinishVarianceLimit = config.getRevenueRiskHogsFarrowToFinishVariance();
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithCombinedFarm();

    // Combined Farm first client
    {
      FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

      {
        List<LivestockItem> livestockItems = new ArrayList<>();
        {
          LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(6.0);
          item.setReportedQuantityEnd(10.0);
          item.setReportedPriceStart(500.0);
          item.setReportedPriceEnd(600.0);
          item.setFmvEnd(666.0);
          livestockItems.add(item);
        }

        {
          LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(648.0);
          item.setReportedQuantityEnd(650.0);
          item.setReportedPriceStart(200.0);
          item.setReportedPriceEnd(360.0);
          item.setFmvEnd(270.0);
          livestockItems.add(item);
        }

        {
          LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(858.0);
          item.setReportedQuantityEnd(816.0);
          item.setReportedPriceStart(37.1);
          item.setReportedPriceEnd(40.0);
          item.setFmvStart(33.5);
          item.setFmvEnd(30.0);
          livestockItems.add(item);
        }

        {
          LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(1718.0);
          item.setReportedQuantityEnd(1714.0);
          item.setReportedPriceStart(50.0);
          item.setReportedPriceEnd(54.96);
          item.setFmvEnd(52.8);
          livestockItems.add(item);
        }

        {
          LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(924.0);
          item.setReportedQuantityEnd(850.0);
          item.setReportedPriceStart(80.0);
          item.setReportedPriceEnd(83.18);
          item.setFmvEnd(79.91);
          livestockItems.add(item);
        }

        {
          LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(94.0);
          item.setReportedQuantityEnd(76.0);
          item.setReportedPriceEnd(99.52);
          item.setFmvEnd(95.61);
          livestockItems.add(item);
        }

        farmingOperation.setLivestockItems(livestockItems);
      }

      {
        List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
        {
          ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
          pu.setReportedAmount(550.0);
          productiveUnitCapacities.add(pu);
        }
        farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
      }

      {
        List<IncomeExpense> incomeExpenses = new ArrayList<>();
        {
          IncomeExpense incExp = getHogsIncome();
          incExp.setReportedAmount(400000.0);
          incomeExpenses.add(incExp);
        }

        {
          IncomeExpense incExp = getHogsExpense();
          incExp.setReportedAmount(23588.0);
          incomeExpenses.add(incExp);
        }
        farmingOperation.setIncomeExpenses(incomeExpenses);
      }
    } // Combined Farm first client


    // Combined Farm second client
    {
      FarmingOperation farmingOperation = scenario.getCombinedFarm().getScenarios().get(1).getFarmingYear().getFarmingOperationBySchedule("A");

      {
        List<LivestockItem> livestockItems = new ArrayList<>();

        {
          LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(6500.0);
          item.setReportedQuantityEnd(4000.0);
          item.setReportedPriceEnd(90.0);
          item.setFmvEnd(96.0);
          livestockItems.add(item);
        }
        
        {
          LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
          item.setReportedQuantityStart(4770.0);
          item.setReportedQuantityEnd(4310.0);
          item.setReportedPriceStart(100.0);
          item.setReportedPriceEnd(127.74);
          item.setFmvEnd(122.72);
          livestockItems.add(item);
        }

        farmingOperation.setLivestockItems(livestockItems);
      }

      {
        List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
        {
          ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FARROW_TO_FINISH, StructureGroupCodes.HOGS_FARROW_TO_FINISH_DESCRIPTION);
          pu.setReportedAmount(500.0);
          productiveUnitCapacities.add(pu);
        }
        farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
      }

      {
        List<IncomeExpense> incomeExpenses = new ArrayList<>();
        {
          IncomeExpense incExp = getHogsIncome();
          incExp.setReportedAmount(10000000.0);
          incomeExpenses.add(incExp);
        }

        {
          IncomeExpense incExp = getHogsExpense();
          incExp.setReportedAmount(100000.0);
          incomeExpenses.add(incExp);
        }
        farmingOperation.setIncomeExpenses(incomeExpenses);
      }
    } // Combined Farm second client


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(7, inventoryItems.size());
    
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(farrowToFinishVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(true, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("325.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("3250.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("8125.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("406.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertEquals("666.0", String.valueOf(hogsResult.getBoarPurchasePrice()));
    assertEquals("1332.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("2.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertEquals("270.0", String.valueOf(hogsResult.getSowPurchasePrice()));
    assertEquals("20462.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("76.0", String.valueOf(hogsResult.getSowPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));
    
    assertEquals("3251.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("78.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2889.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("8159.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("122.72", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("1001272.48", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("1200000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.198", String.valueOf(hogsResult.getRevenueVariance()));
    
    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(true, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(true, revenueRiskResult.getResult());
  }

  @Test
  public void failMissingProductiveUnitsMissingIncome() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      // missing productive units
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        // missing income
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(1, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }
    
    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertNull(hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(false, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("0.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("0.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("5.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("0.0", String.valueOf(hogsResult.getExpectedSold()));

    assertNull(hogsResult.getHeaviestHogFmvPrice());
    assertNull(hogsResult.getExpectedRevenue());
    assertEquals("0.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertNull(hogsResult.getRevenueVariance());

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(3, errorMessages.size());
    assertEquals(0, warningMessages.size());

    Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_PRODUCTIVE_UNITS),
        errorIterator.next().getMessage());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_HEAVIEST_HOG_FEEDER_PRICE),
        errorIterator.next().getMessage());
    
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_INCOME),
        errorIterator.next().getMessage());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void passFeederOperation() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    Double feederVarianceLimit = config.getRevenueRiskHogsFeederVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FEEDER, StructureGroupCodes.HOGS_FEEDER_DESCRIPTION);
        pu.setReportedAmount(24507.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(4.0);
        item.setReportedQuantityEnd(2.0);
        item.setReportedPriceStart(130.0);
        item.setReportedPriceEnd(140.0);
        item.setFmvEnd(132.71);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(280000.00);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }

    {
      List<ReceivableItem> receivableItems = new ArrayList<>();
      {
        ReceivableItem item = getHogsReceivable();
        item.setReportedStartOfYearAmount(4000.00);
        item.setReportedEndOfYearAmount(8000.00);
        receivableItems.add(item);
      }
      farmingOperation.setReceivableItems(receivableItems);
    }

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(8, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("2.0", String.valueOf(item.getQuantityStart()));
      assertEquals("1.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("132.71", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(feederVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(false, hogsResult.getFarrowToFinishOperation());
    assertEquals(true, hogsResult.getFeederOperation());
    assertEquals("0.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("0.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));

    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("12253.5", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertEquals("33.5", String.valueOf(hogsResult.getWeanlingPurchasePrice()));
    assertEquals("21794.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("651.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3253.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("651.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2890.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("1014.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("132.71", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("134567.94", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("142000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.055", String.valueOf(hogsResult.getRevenueVariance()));

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(true, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(true, revenueRiskResult.getResult());
  }

  // Test that the price for weanlings/birth inventory is used even if there are no start/end quantities
  @Test
  public void passFeederOperationBirthPriceOnly() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    Double feederVarianceLimit = config.getRevenueRiskHogsFeederVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FEEDER, StructureGroupCodes.HOGS_FEEDER_DESCRIPTION);
        pu.setReportedAmount(24507.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(null);
        item.setReportedQuantityEnd(0.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(4.0);
        item.setReportedQuantityEnd(2.0);
        item.setReportedPriceStart(130.0);
        item.setReportedPriceEnd(140.0);
        item.setFmvEnd(132.71);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(280000.00);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }

    {
      List<ReceivableItem> receivableItems = new ArrayList<>();
      {
        ReceivableItem item = getHogsReceivable();
        item.setReportedStartOfYearAmount(4000.00);
        item.setReportedEndOfYearAmount(8000.00);
        receivableItems.add(item);
      }
      farmingOperation.setReceivableItems(receivableItems);
    }

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(8, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("0.0", String.valueOf(item.getQuantityStart()));
      assertEquals("0.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("2.0", String.valueOf(item.getQuantityStart()));
      assertEquals("1.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("132.71", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(feederVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(false, hogsResult.getFarrowToFinishOperation());
    assertEquals(true, hogsResult.getFeederOperation());
    assertEquals("0.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("0.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));

    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));
    assertEquals("12253.5", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertEquals("33.5", String.valueOf(hogsResult.getWeanlingPurchasePrice()));
    assertEquals("21794.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("651.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("2824.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("651.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2482.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("993.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("132.71", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("131781.03", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("142000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.078", String.valueOf(hogsResult.getRevenueVariance()));

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(true, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(true, revenueRiskResult.getResult());
  }

  @Test
  public void failFeederOperationMissingHogBirthPriceMissingHeaviestHogPrice() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    Double feederVarianceLimit = config.getRevenueRiskHogsFeederVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FEEDER, StructureGroupCodes.HOGS_FEEDER_DESCRIPTION);
        pu.setReportedAmount(24507.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(null); // missing hog birth price
        item.setFmvEnd(33.5);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(4.0);
        item.setReportedQuantityEnd(2.0);
        item.setReportedPriceStart(110.0);
        item.setReportedPriceEnd(130.0);
        item.setFmvEnd(null);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(280000.00);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }

    {
      List<ReceivableItem> receivableItems = new ArrayList<>();
      {
        ReceivableItem item = getHogsReceivable();
        item.setReportedStartOfYearAmount(4000.00);
        item.setReportedEndOfYearAmount(8000.00);
        receivableItems.add(item);
      }
      farmingOperation.setReceivableItems(receivableItems);
    }

    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(8, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertNull(item.getFmvPrice());
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("2.0", String.valueOf(item.getQuantityStart()));
      assertEquals("1.0", String.valueOf(item.getQuantityEnd()));
      assertNull(item.getFmvPrice());
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(feederVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(false, hogsResult.getFarrowToFinishOperation());
    assertEquals(true, hogsResult.getFeederOperation());
    assertEquals("0.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("0.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));

    assertEquals("12253.5", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("21794.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3253.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2890.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("363.0", String.valueOf(hogsResult.getExpectedSold()));

    assertNull(hogsResult.getHeaviestHogFmvPrice());
    assertNull(hogsResult.getExpectedRevenue());
    assertEquals("142000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertNull(hogsResult.getRevenueVariance());

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(2, errorMessages.size());
    assertEquals(0, warningMessages.size());

    Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_HOG_BIRTH_PRICE),
        errorIterator.next().getMessage());

    assertEquals(
        ReasonabilityTestResultMessage.createMessageWithParameters(HogsRevenueRiskSubTest.ERROR_MISSING_HEAVIEST_HOG_FEEDER_PRICE),
        errorIterator.next().getMessage());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void failFeederOperationOutsideVariance() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    Double feederVarianceLimit = config.getRevenueRiskHogsFeederVariance();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = getHogProductiveUnitCapacity(StructureGroupCodes.HOGS_FEEDER, StructureGroupCodes.HOGS_FEEDER_DESCRIPTION);
        pu.setReportedAmount(24507.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = getHogLivestockItem(BOARS_INVENTORY_ITEM_CODE, BOARS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(600.0);
        item.setFmvEnd(666.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(SOWS_INVENTORY_ITEM_CODE, SOWS_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(648.0);
        item.setReportedQuantityEnd(650.0);
        item.setReportedPriceStart(200.0);
        item.setReportedPriceEnd(360.0);
        item.setFmvEnd(270.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(858.0);
        item.setReportedQuantityEnd(816.0);
        item.setReportedPriceStart(37.1);
        item.setReportedPriceEnd(40.0);
        item.setFmvStart(33.5);
        item.setFmvEnd(30.0);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1718.0);
        item.setReportedQuantityEnd(1714.0);
        item.setReportedPriceStart(50.0);
        item.setReportedPriceEnd(54.96);
        item.setFmvEnd(52.8);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(924.0);
        item.setReportedQuantityEnd(850.0);
        item.setReportedPriceStart(80.0);
        item.setReportedPriceEnd(83.18);
        item.setFmvEnd(79.91);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(1394.0);
        item.setReportedQuantityEnd(876.0);
        item.setReportedPriceEnd(99.52);
        item.setFmvEnd(95.61);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(954.0);
        item.setReportedQuantityEnd(862.0);
        item.setReportedPriceStart(100.0);
        item.setReportedPriceEnd(127.74);
        item.setFmvEnd(122.72);
        livestockItems.add(item);
      }

      {
        LivestockItem item = getHogLivestockItem(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION);
        item.setReportedQuantityStart(4.0);
        item.setReportedQuantityEnd(2.0);
        item.setReportedPriceStart(130.0);
        item.setReportedPriceEnd(140.0);
        item.setFmvEnd(132.71);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        IncomeExpense incExp = getHogsIncome();
        incExp.setReportedAmount(360000.00);
        incomeExpenses.add(incExp);
      }

      {
        IncomeExpense incExp = getHogsExpense();
        incExp.setReportedAmount(43588.0);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }

    {
      List<ReceivableItem> receivableItems = new ArrayList<>();
      {
        ReceivableItem item = getHogsReceivable();
        item.setReportedStartOfYearAmount(4000.00);
        item.setReportedEndOfYearAmount(8000.00);
        receivableItems.add(item);
      }
      farmingOperation.setReceivableItems(receivableItems);
    }
    
    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(8, inventoryItems.size());
    Iterator<RevenueRiskInventoryItem> inventoryIterator = inventoryItems.iterator();

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(BOARS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(BOARS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("3.0", String.valueOf(item.getQuantityStart()));
      assertEquals("5.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("666.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(SOWS_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(SOWS_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("324.0", String.valueOf(item.getQuantityStart()));
      assertEquals("325.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("270.0", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_BIRTH_18_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("429.0", String.valueOf(item.getQuantityStart()));
      assertEquals("408.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("33.5", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_19_TO_36_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("859.0", String.valueOf(item.getQuantityStart()));
      assertEquals("857.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("52.8", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_66_TO_100_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("462.0", String.valueOf(item.getQuantityStart()));
      assertEquals("425.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("79.91", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_101_TO_140_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("697.0", String.valueOf(item.getQuantityStart()));
      assertEquals("438.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("95.61", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_181_TO_220_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("477.0", String.valueOf(item.getQuantityStart()));
      assertEquals("431.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("122.72", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    {
      RevenueRiskInventoryItem item = inventoryIterator.next();
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
      assertEquals(FEEDER_221_TO_240_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
      assertEquals("2.0", String.valueOf(item.getQuantityStart()));
      assertEquals("1.0", String.valueOf(item.getQuantityEnd()));
      assertEquals("132.71", String.valueOf(item.getFmvPrice()));
      assertNull(item.getReportedPrice());
    }

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertEquals(feederVarianceLimit, hogsResult.getVarianceLimit());

    assertEquals(true, hogsResult.getHasHogs());
    assertEquals(false, hogsResult.getFarrowToFinishOperation());
    assertEquals(true, hogsResult.getFeederOperation());
    assertEquals("0.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("0.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("21794.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));

    assertEquals("12253.5", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertEquals("33.5", String.valueOf(hogsResult.getWeanlingPurchasePrice()));
    assertEquals("21794.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("651.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("3253.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("651.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("2890.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("1014.0", String.valueOf(hogsResult.getExpectedSold()));

    assertEquals("132.71", String.valueOf(hogsResult.getHeaviestHogFmvPrice()));
    assertEquals("134567.94", String.valueOf(hogsResult.getExpectedRevenue()));
    assertEquals("182000.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertEquals("0.352", String.valueOf(hogsResult.getRevenueVariance()));

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(false, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(false, revenueRiskResult.getResult());
  }

  @Test
  public void passNoHogs() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    Double birthsPerCycle = config.getRevenueRiskHogsFarrowToFinishBirthsPerCycle();
    Double birthCyclesPerYear = config.getRevenueRiskHogsFarrowToFinishBirthCyclesPerYear();
    Double deathRate = config.getRevenueRiskHogsFarrowToFinishDeathRate();
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();

    Scenario scenario = buildScenarioWithNoCombinedFarm();
    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule("A");

    {
      List<LivestockItem> livestockItems = new ArrayList<>();
      {
        LivestockItem item = new LivestockItem();
        item.setInventoryItemCode("1234");
        item.setInventoryItemCodeDescription("Not a hog");
        item.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
        item.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE);
        item.setReportedQuantityStart(6.0);
        item.setReportedQuantityEnd(10.0);
        item.setReportedPriceStart(500.0);
        item.setReportedPriceEnd(666.0);
        item.setFmvEnd(600.0);
        livestockItems.add(item);
      }

      farmingOperation.setLivestockItems(livestockItems);
    }

    {
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity pu = new ProductiveUnitCapacity();
        pu.setStructureGroupCode("999");
        pu.setStructureGroupCodeDescription("Not hogs");
        pu.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
        pu.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE);
        pu.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
        pu.setReportedAmount(24507.0);
        productiveUnitCapacities.add(pu);
      }
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
    }

    {
      List<IncomeExpense> incomeExpenses = new ArrayList<>();
      {
        LineItem lineItem = new LineItem();
        lineItem.setLineItem(4321);
        lineItem.setDescription("Not hog money");
        lineItem.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
        lineItem.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE);

        IncomeExpense incExp = new IncomeExpense();
        incExp.setIsExpense(Boolean.FALSE);
        incExp.setLineItem(lineItem);
        incExp.setReportedAmount(360000.00);
        incomeExpenses.add(incExp);
      }

      {
        LineItem lineItem = new LineItem();
        lineItem.setLineItem(4321);
        lineItem.setDescription("Not hog money");
        lineItem.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
        lineItem.setCommodityTypeCodeDescription(CommodityTypeCodes.CATTLE);

        IncomeExpense incExp = new IncomeExpense();
        incExp.setIsExpense(Boolean.TRUE);
        incExp.setLineItem(lineItem);
        incExp.setReportedAmount(43588.00);
        incomeExpenses.add(incExp);
      }
      farmingOperation.setIncomeExpenses(incomeExpenses);
    }


    try {
      revenueRiskTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    RevenueRiskTestResult revenueRiskResult = scenario.getReasonabilityTestResults().getRevenueRiskTest();
    assertNotNull(revenueRiskResult);
    HogsRevenueRiskSubTestResult hogsResult = revenueRiskResult.getHogs();
    assertNotNull(hogsResult);

    List<RevenueRiskInventoryItem> inventoryItems = hogsResult.getInventory();
    assertNotNull(inventoryItems);
    assertEquals(0, inventoryItems.size());

    assertEquals(birthsPerCycle, hogsResult.getBirthsPerCycle());
    assertEquals(birthCyclesPerYear, hogsResult.getBirthCyclesPerYear());
    assertEquals(deathRate, hogsResult.getDeathRate());
    assertNull(hogsResult.getVarianceLimit());

    assertEquals(false, hogsResult.getHasHogs());
    assertEquals(false, hogsResult.getFarrowToFinishOperation());
    assertEquals(false, hogsResult.getFeederOperation());
    assertEquals("0.0", String.valueOf(hogsResult.getSowsBreeding()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsPerCycle()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalBirthsAllCycles()));
    assertEquals("0.0", String.valueOf(hogsResult.getDeaths()));

    assertEquals("0.0", String.valueOf(hogsResult.getReportedExpenses()));
    
    assertNull(hogsResult.getBoarPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getBoarPurchaseCount()));
    assertNull(hogsResult.getSowPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getSowPurchaseExpense()));

    assertEquals("0.0", String.valueOf(hogsResult.getFeederProductiveUnits()));
    assertNull(hogsResult.getWeanlingPurchasePrice());
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseExpense()));
    assertEquals("0.0", String.valueOf(hogsResult.getWeanlingPurchaseCount()));

    assertEquals("0.0", String.valueOf(hogsResult.getTotalQuantityStart()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalQuantityEnd()));
    assertEquals("0.0", String.valueOf(hogsResult.getTotalPurchaseCount()));
    assertEquals("0.0", String.valueOf(hogsResult.getExpectedSold()));

    assertNull(hogsResult.getHeaviestHogFmvPrice());
    assertNull(hogsResult.getExpectedRevenue());
    assertEquals("0.0", String.valueOf(hogsResult.getReportedRevenue()));
    assertNull(hogsResult.getRevenueVariance());

    assertNotNull(revenueRiskResult.getMessages());
    assertNotNull(revenueRiskResult.getErrorMessages());
    assertNotNull(revenueRiskResult.getWarningMessages());
    assertNotNull(revenueRiskResult.getInfoMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, revenueRiskResult.getWarningMessages());
    List<ReasonabilityTestResultMessage> errorMessages = revenueRiskResult.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = revenueRiskResult.getWarningMessages();
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());

    assertEquals(true, revenueRiskResult.getHogs().getHogsPass());
    assertEquals(true, revenueRiskResult.getResult());
  }
  
  private ProductiveUnitCapacity getHogProductiveUnitCapacity(String inventoryItemCode, String inventoryItemDescription) {
    ProductiveUnitCapacity pu = new ProductiveUnitCapacity();
    pu.setStructureGroupCode(inventoryItemCode);
    pu.setStructureGroupCodeDescription(inventoryItemDescription);
    pu.setCommodityTypeCode(CommodityTypeCodes.HOG);
    pu.setCommodityTypeCodeDescription(HOGS_COMMODITY_CODE_DESCRIPTION);
    pu.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    return pu;
  }

  private LivestockItem getHogLivestockItem(String inventoryItemCode, String inventoryItemDescription) {
    LivestockItem item = new LivestockItem();
    item.setInventoryItemCode(inventoryItemCode);
    item.setInventoryItemCodeDescription(inventoryItemDescription);
    item.setCommodityTypeCode(CommodityTypeCodes.HOG);
    item.setCommodityTypeCodeDescription(HOGS_COMMODITY_CODE_DESCRIPTION);
    return item;
  }

  private IncomeExpense getHogsIncome() {
    IncomeExpense incExp = getHogsIncomeExpense();
    incExp.setIsExpense(Boolean.FALSE);
    return incExp;
  }

  private IncomeExpense getHogsExpense() {
    IncomeExpense incExp = getHogsIncomeExpense();
    incExp.setIsExpense(Boolean.TRUE);
    return incExp;
  }

  private IncomeExpense getHogsIncomeExpense() {
    LineItem lineItem = new LineItem();
    lineItem.setLineItem(HOGS_LINE_ITEM);
    lineItem.setDescription(HOGS_LINE_ITEM_DESCRIPTION);
    lineItem.setCommodityTypeCode(CommodityTypeCodes.HOG);
    lineItem.setCommodityTypeCodeDescription(HOGS_COMMODITY_CODE_DESCRIPTION);

    IncomeExpense incExp = new IncomeExpense();
    incExp.setLineItem(lineItem);
    return incExp;
  }

  private ReceivableItem getHogsReceivable() {
    ReceivableItem item = new ReceivableItem();
    item.setInventoryItemCode(String.valueOf(HOGS_LINE_ITEM));
    item.setInventoryItemCodeDescription(HOGS_LINE_ITEM_DESCRIPTION);
    item.setCommodityTypeCode(CommodityTypeCodes.HOG);
    item.setCommodityTypeCodeDescription(HOGS_COMMODITY_CODE_DESCRIPTION);
    item.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
    return item;
  }
}
