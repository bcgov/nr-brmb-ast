package ca.bc.gov.srm.farm.reasonability.production;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;

public class TestProductionForageSeedTest {
  
  private static Logger logger = LoggerFactory.getLogger(TestProductionForageSeedTest.class);
  
  private static final String ALFALFA_COMMON_SEED_ITEM_CODE = "5600";
  private static final String ALFALFA_COMMON_SEED_ITEM_DESCRIPTION = "Alfalfa; Common Seed";
  
  private static final String BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE = "5606";
  private static final String BENTGRASS_PEDIGREED_ITEM_DESCRIPTION = "Bentgrass; Pedigreed Seed";
  
  @BeforeAll
  protected static void setUp() {
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
    farmOp.setPartnershipPercent(0.50);
    farmOp.setSchedule("A");
    farmOp.setCropItems(cropItems);
    farmOps.add(farmOp);
    
    farmYear.setFarmingOperations(farmOps);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    
    return scenario;
  }
  
  private Scenario buildScenarioWithCombinedFarm(List<CropItem> cropItems, List<CropItem> cropItems2) {
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2018));
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
    
    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    
    List<FarmingOperation> farmOps = new ArrayList<>();
    FarmingOperation farmOp = new FarmingOperation();
    farmOp.setPartnershipPercent(0.50);
    farmOp.setSchedule("A");
    farmOp.setCropItems(cropItems);
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOp2.setPartnershipPercent(0.10);
    farmOp2.setSchedule("A");
    farmOp2.setCropItems(cropItems2);
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    farmOp2.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    return scenario;
  }
  
  @Test
  public void passWithNoVariance() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    cropItem1.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setInventoryClassCode(InventoryClassCodes.CROP);
    cropItem1.setReportedQuantityProduced(2.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
        
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());  
    assertEquals(true, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(0, Math.round(result.getVariance()));
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals(new Double(2), result.getReportedProduction());
    assertEquals(new Double(2), result.getExpectedQuantityProduced());
    assertEquals(new Double(1), result.getExpectedProductionPerUnit());
    assertEquals(new Double(2), result.getProductiveCapacityAmount());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
  }
  
  @Test
  public void passWithVarianceLessThanLimit() {
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(10.0);
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(6.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());  
    assertEquals(true, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals("-0.167", String.valueOf(result.getVariance()));
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals("10.0", String.valueOf(result.getReportedProduction()));
    assertEquals("12.0", String.valueOf(result.getExpectedQuantityProduced()));
    assertEquals("2.0", String.valueOf(result.getExpectedProductionPerUnit()));
    assertEquals("6.0", String.valueOf(result.getProductiveCapacityAmount()));
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
  }
  
  @Test
  public void failForReportedProdInNonPounds() {
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(new Double(1));
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(new Double(1));
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(false, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ForageProductionSubTest.FORAGE_SEED_ERROR_NOT_REPORTED_IN_POUNDS, ALFALFA_COMMON_SEED_ITEM_CODE, ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, CropUnitCodes.TONNES_DESCRIPTION), 
        scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());
    assertEquals(1, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    
    assertNull(scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0).getReportedProduction());
    assertNull(scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0).getVariance());  
  }
  
  @Test
  public void failForVarianceGreaterThanLimit() {
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(30.0);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(10.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);    
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    assertEquals(false, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest().booleanValue());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals(new Double(30), result.getReportedProduction());
    assertEquals(new Double(20), result.getExpectedQuantityProduced());
    assertEquals(new Double(2), result.getExpectedProductionPerUnit());
    assertEquals(new Double(10), result.getProductiveCapacityAmount());
    assertEquals(false, result.getPass());
    assertEquals(true, result.getVariance() > varianceLimit);
    assertEquals(new Double(.5), result.getVariance());
  }
  
  @Test
  public void failForNoProdCapUnitForCropItem() {
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(2.9);
      
      cropItems.add(cropItem);
    }
        
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(1, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ProductionTest.ERROR_MISSING_PRODUCTIVE_UNIT, ALFALFA_COMMON_SEED_ITEM_CODE, ALFALFA_COMMON_SEED_ITEM_DESCRIPTION), 
        scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());
    assertEquals(false, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertNull(result.getVariance());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals(new Double(2.9), result.getReportedProduction());
    assertNull(result.getExpectedQuantityProduced());
    assertNull(result.getExpectedProductionPerUnit());
    assertNull(result.getProductiveCapacityAmount());
    assertEquals(false, result.getPass());
  }
  
  @Test
  public void failForNoCropItemWithProdCapUnitProvided() {
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(3));
    prodUnitCapacity.setReportedAmount(new Double(5));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(false, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ProductionTest.ERROR_MISSING_REPORTED_INVENTORY, ALFALFA_COMMON_SEED_ITEM_CODE, ALFALFA_COMMON_SEED_ITEM_DESCRIPTION),
        scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());
    assertEquals(1, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertNull(result.getVariance());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertNull(result.getReportedProduction());
    assertNull(result.getExpectedQuantityProduced());
    assertNull(result.getExpectedProductionPerUnit());
    assertNull(result.getExpectedProductionPerUnit());
    assertEquals(false, result.getPass());
  }
  
  @Test
  public void passForCropItemAndProdCapUnitWithZeroVals() {
    
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(new Double(0));
          
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(new Double(0));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    assertEquals(true, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().size());
    
  }
  
  @Test
  public void passForNoCropItemAndProdCapUnitWithZero() {
    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(new Double(0));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    assertEquals(true, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().size());
  }
  
  @Test
  public void passWithTwoItems() {
    
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(2.0);
      cropItems.add(cropItem);
    }

    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(BENTGRASS_PEDIGREED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(4.0);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity prodUnitCapacity2 = new ProductiveUnitCapacity();
    prodUnitCapacity2.setInventoryItemCode(BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE);
    prodUnitCapacity2.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity2.setReportedAmount(3.4);
    prodUnitCapacity2.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    prodUnitCapacities.add(prodUnitCapacity2);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
        
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    assertEquals(2, scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().size());
    assertEquals(true, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
    
    Collections.sort(scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(new Double(0), result.getVariance());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals(new Double(2), result.getReportedProduction());
    assertEquals(new Double(2), result.getExpectedQuantityProduced());
    assertEquals(new Double(1), result.getExpectedProductionPerUnit());
    assertEquals(new Double(2), result.getProductiveCapacityAmount());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
    
    
    ProductionInventoryItemTestResult result2 = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(1);
    assertNotNull(result2);
    
    assertEquals(new Double(0.176), result2.getVariance());
    assertEquals(BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE, result2.getInventoryItemCode());
    assertEquals(BENTGRASS_PEDIGREED_ITEM_DESCRIPTION, result2.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result2.getCropUnitCode());
    assertEquals(new Double(4), result2.getReportedProduction());
    assertEquals(new Double(3.4), result2.getExpectedQuantityProduced());
    assertEquals(new Double(1), result2.getExpectedProductionPerUnit());
    assertEquals(new Double(3.4), result2.getProductiveCapacityAmount());
    assertEquals(true, result2.getPass());
    assertEquals(true, result2.getVariance() < varianceLimit);
  }
 
  @Test
  public void passWithTwoItemsWithCombinedFarm() {
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(1.0);
      cropItems.add(cropItem);
    }
    
    List<CropItem> cropItems2 = new ArrayList<>();
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(BENTGRASS_PEDIGREED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(5.0);
      cropItems2.add(cropItem);
    }
    
    
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, cropItems2);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(1.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity prodUnitCapacity2 = new ProductiveUnitCapacity();
    prodUnitCapacity2.setInventoryItemCode(BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE);
    prodUnitCapacity2.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity2.setReportedAmount(2.5);
    prodUnitCapacity2.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    prodUnitCapacity2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    
    List<ProductiveUnitCapacity> prodUnitCapacities2 = new ArrayList<>();
    prodUnitCapacities2.add(prodUnitCapacity2);
    int i = 0;
    for (Scenario scen : scenario.getCombinedFarm().getScenarios()) {
      if (i == 0) {
        scen.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
      } else {
        scen.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities2);
      }
      i++;
    }
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
        
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(0, productionTestResult.getErrorMessages().size());
    assertEquals(2, scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().size());
    
    Collections.sort(scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals("1.0", String.valueOf(result.getReportedProduction()));
    assertEquals("1.0", String.valueOf(result.getProductiveCapacityAmount()));
    assertEquals("1.0", String.valueOf(result.getExpectedProductionPerUnit()));
    assertEquals("1.0", String.valueOf(result.getExpectedQuantityProduced()));
    assertEquals("0.0", String.valueOf(result.getVariance()));
    assertEquals(true, result.getVariance() < varianceLimit);
    assertEquals(true, result.getPass());
    
    
    ProductionInventoryItemTestResult result2 = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(1);
    assertNotNull(result2);
    
    assertEquals(BENTGRASS_PEDIGREED_INVENTORY_ITEM_CODE, result2.getInventoryItemCode());
    assertEquals(BENTGRASS_PEDIGREED_ITEM_DESCRIPTION, result2.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result2.getCropUnitCode());
    assertEquals("5.0", String.valueOf(result2.getReportedProduction()));
    assertEquals("2.5", String.valueOf(result2.getProductiveCapacityAmount()));
    assertEquals("2.0", String.valueOf(result2.getExpectedProductionPerUnit()));
    assertEquals("5.0", String.valueOf(result2.getExpectedQuantityProduced()));
    assertEquals("0.0", String.valueOf(result2.getVariance()));
    assertEquals(true, result2.getVariance() < varianceLimit);
    assertEquals(true, result2.getPass());
    
    assertEquals(true, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest());
  }
  
  @Test
  public void failForVarianceGreaterThanLimitOnCombinedFarm() {
    List<CropItem> cropItems = new ArrayList<>();
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(30.0);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(10.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertEquals(0, scenario.getReasonabilityTestResults().getProductionTest().getMessages().get(MessageTypeCodes.ERROR).size());
    assertEquals(false, scenario.getReasonabilityTestResults().getProductionTest().getPassForageAndForageSeedTest().booleanValue());
    
    ProductionInventoryItemTestResult result = scenario.getReasonabilityTestResults().getProductionTest().getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals(new Double(30), result.getReportedProduction());
    assertEquals(new Double(20), result.getExpectedQuantityProduced());
    assertEquals(new Double(2), result.getExpectedProductionPerUnit());
    assertEquals(new Double(10), result.getProductiveCapacityAmount());
    assertEquals(false, result.getPass());
    assertEquals(true, result.getVariance() > varianceLimit);
    assertEquals(new Double(.5), result.getVariance());
  }
  
  @Test
  public void passWithMultipleCropItemsSameInventoryCodeAndUnit() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(1.0);
      
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
      cropItem.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setInventoryClassCode(InventoryClassCodes.CROP);
      cropItem.setReportedQuantityProduced(2.0);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(ALFALFA_COMMON_SEED_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE_SEED);
    prodUnitCapacity.setReportedAmount(1.5);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    double varianceLimit = config.getProductionForageVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(0, productionTestResult.getErrorMessages().size());  
    assertEquals(1, productionTestResult.getForageSeedTestResults().size());  
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageSeedTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(ALFALFA_COMMON_SEED_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(ALFALFA_COMMON_SEED_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals("3.0", String.valueOf(result.getReportedProduction()));
    assertEquals("1.5", String.valueOf(result.getProductiveCapacityAmount()));
    assertEquals("3.0", String.valueOf(result.getExpectedQuantityProduced()));
    assertEquals("2.0", String.valueOf(result.getExpectedProductionPerUnit()));
    assertEquals("0.0", String.valueOf(result.getVariance()));
    
    assertEquals(true, result.getVariance() < varianceLimit);
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(true, result.getPass());
  }
}
