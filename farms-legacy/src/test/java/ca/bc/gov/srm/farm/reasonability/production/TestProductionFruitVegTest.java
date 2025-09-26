package ca.bc.gov.srm.farm.reasonability.production;

import static ca.bc.gov.srm.farm.reasonability.production.FruitVegProductionSubTest.*;
import static ca.bc.gov.srm.farm.reasonability.production.ProductionTest.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
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
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.production.FruitVegProductionResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;

public class TestProductionFruitVegTest {
  
  private static Logger logger = LoggerFactory.getLogger(TestProductionFruitVegTest.class);
  
  private static final String APPLES_AMBROSIA_1ST_YEAR_CODE = "4812";
  private static final String APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION = "Apples; Ambrosia; 1st year of fruit production; high density";
  
  private static final String APPLES_AMBROSIA_5_PLUS_YEARS_CODE = "4816";
  private static final String APPLES_AMBROSIA_5_PLUS_YEARS_DESCRIPTION = "Apples; Ambrosia; 5+ years of fruit production";
  
  private static final String APPLES_CODE = "5030";
  private static final String APPLES_DESCRIPTION = "Apples";
  
  private static final String FIELD_VEGETABLES_CODE = "7001";
  private static final String FIELD_VEGETABLES_DESCRIPTION = "Field Vegetables";
  
  private static final String BEANS_SNAP_CODE = "6982";
  private static final String BEANS_SNAP_DESCRIPTION = "Beans; Snap";
  
  private static final String BEANS_SNAP_FRESH_CODE = "6983";
  private static final String BEANS_SNAP_FRESH_DESCRIPTION = "Beans; Snap; Fresh";
  
  private static final String PEACHES_INVENTORY_ITEM_CODE = "5052";
  private static final String PEACHES_INVENTORY_ITEM_DESCRIPTION = "Peaches";
  
  private static final String APPLES_FRUIT_VEG_TYPE_CODE = FruitVegTypeCodes.APPLE;
  private static final String APPLES_FRUIT_VEG_TYPE_DESCRIPTION = "Apples";
  
  private static final String BEANS_FRUIT_VEG_TYPE_CODE = FruitVegTypeCodes.BEAN;
  private static final String BEANS_FRUIT_VEG_TYPE_DESCRIPTION = "Beans";
  
  private static final String PEACHES_FRUIT_VEG_TYPE_CODE = FruitVegTypeCodes.PEACH;
  private static final String PEACHES_FRUIT_VEG_TYPE_DESCRIPTION = "Peaches";
  
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
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    
    farmOp.setPartnershipPercent(0.50);
    farmOp.setSchedule("A");
    farmOp.setCropItems(cropItems);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    farmOp.setProductiveUnitCapacities(prodUnitCapacities);     
    
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
    
    FarmingYear farmYear = new FarmingYear();
    scenario.setFarmingYear(farmYear);
    
    List<FarmingOperation> farmOps = new ArrayList<>();
    FarmingOperation farmOp = new FarmingOperation();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    
    farmOp.setPartnershipPercent(0.50);
    farmOp.setSchedule("A");
    farmOp.setCropItems(cropItems);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    
    farmOp2.setPartnershipPercent(0.10);
    farmOp2.setSchedule("A");
    farmOp2.setCropItems(cropItems2);
    farmOp2.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    scenario2.setIsInCombinedFarmInd(true);
    
    return scenario;
  }
  
  @Test
  public void passWithVarianceLessThanLimit() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(18));
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(new Double(4));
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(4));
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("18.0", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("4.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      Iterator<FruitVegProductionResult> iterator = productionTestResult.getFruitVegTestResults().iterator();
      
      {
        FruitVegProductionResult result = iterator.next();
        assertNotNull(result);
      
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(new Double(18), result.getReportedProduction());
        assertEquals(new Double(16), result.getExpectedQuantityProduced());
        assertEquals(new Double(4), result.getProductiveCapacityAmount());
        assertEquals(new Double(0.125), result.getVariance());
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  @Test
  public void passWithVarianceLessThanLimitOnMultipleItems() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
      cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
      cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
      cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(18.0);
      cropItems.add(cropItem1);
    }
    
    {
      CropItem cropItem2 = new CropItem();
      cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem2.setFruitVegTypeCode(PEACHES_FRUIT_VEG_TYPE_CODE);
      cropItem2.setFruitVegTypeCodeDescription(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION);
      cropItem2.setInventoryItemCode(PEACHES_INVENTORY_ITEM_CODE);
      cropItem2.setInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
      cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem2.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem2.setReportedQuantityProduced(14.0);
      cropItems.add(cropItem2);
    }
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(4.0);
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(4));
      prodUnitCapacities.add(prodUnitCapacity);
    }
    
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(PEACHES_INVENTORY_ITEM_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(PEACHES_INVENTORY_ITEM_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(PEACHES_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(4.0);
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(4));
      prodUnitCapacities.add(prodUnitCapacity);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(2, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("18.0", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("4.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(PEACHES_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
        assertEquals(PEACHES_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("14.0", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("4.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(2, productionTestResult.getFruitVegTestResults().size());
      Iterator<FruitVegProductionResult> iterator = productionTestResult.getFruitVegTestResults().iterator();
      
      {
        FruitVegProductionResult result = iterator.next();
        assertNotNull(result);
      
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(new Double(18), result.getReportedProduction());
        assertEquals(new Double(16), result.getExpectedQuantityProduced());
        assertEquals(new Double(4), result.getProductiveCapacityAmount());
        assertEquals(new Double(0.125), result.getVariance());
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
      
      {
        FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(1);
        assertNotNull(result);
        
        assertEquals(new Double(-0.125), result.getVariance());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(new Double(14), result.getReportedProduction());
        assertEquals(new Double(16), result.getExpectedQuantityProduced());
        assertEquals(new Double(4), result.getProductiveCapacityAmount());
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  @Test
  public void passWithConversionToPounds() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(4.0);
    
    CropUnitConversion convItem = new CropUnitConversion();
    convItem.setDefaultCropUnitCode(CropUnitCodes.TONNES);
    CropUnitConversionItem convItem1 = new CropUnitConversionItem();
    convItem1.setTargetCropUnitCode(CropUnitCodes.POUNDS);
    convItem1.setConversionFactor(BigDecimal.valueOf(2204.622600d));
    
    List<CropUnitConversionItem> cropConvItems = new ArrayList<>();
    cropConvItems.add(convItem1);
    convItem.setConversionItems(cropConvItems);
    cropItem1.setCropUnitConversion(convItem);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(4.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(BigDecimal.valueOf(2000.0));
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("8818.49", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("2000.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("8000.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      Iterator<FruitVegProductionResult> iterator = productionTestResult.getFruitVegTestResults().iterator();
      
      {
        FruitVegProductionResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("8818.49", String.valueOf(result.getReportedProduction()));
        assertEquals("4.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("8000.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals(new Double(0.102), result.getVariance());
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  @Test
  public void failForNoConversionToPoundsWithNoProductiveUnits() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(8));
    
    CropUnitConversion convItem = new CropUnitConversion();
    convItem.setDefaultCropUnitCode(CropUnitCodes.POUNDS);
    CropUnitConversionItem convItem1 = new CropUnitConversionItem();
    convItem1.setTargetCropUnitCode(CropUnitCodes.LARGE_BALES);
    convItem1.setTargetCropUnitCodeDescription(CropUnitCodes.LARGE_BALES_DESCRIPTION);
    convItem1.setConversionFactor(new BigDecimal(2));
    
    List<CropUnitConversionItem> cropConvItems = new ArrayList<>();
    cropConvItems.add(convItem1);
    convItem.setConversionItems(cropConvItems);
    cropItem1.setCropUnitConversion(convItem);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("0.0", String.valueOf(item.getReportedProduction()));
        assertEquals("0.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("0.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      Iterator<FruitVegProductionResult> iterator = productionTestResult.getFruitVegTestResults().iterator();
      
      {
        FruitVegProductionResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("0.0", String.valueOf(result.getReportedProduction()));
        assertEquals("0.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals(false, result.getPass());
        assertNull(result.getVariance());
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(1, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());

      Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(ERROR_MISSING_CONVERSION_FACTORS, APPLES_AMBROSIA_1ST_YEAR_CODE, APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, CropUnitCodes.TONNES_DESCRIPTION, CropUnitCodes.POUNDS_DESCRIPTION),
          errorIterator.next().getMessage());
    }
    
    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
  
  @Test
  public void failForMissingProductiveUnits() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(16));
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("16.0", String.valueOf(item.getReportedProduction()));
        assertEquals("0.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("0.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      Iterator<FruitVegProductionResult> iterator = productionTestResult.getFruitVegTestResults().iterator();
      
      {
        FruitVegProductionResult result = iterator.next();
        assertNotNull(result);
        
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("16.0", String.valueOf(result.getReportedProduction()));
        assertEquals("0.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals(false, result.getPass());
        assertNull(result.getVariance());
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(1, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());

      Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(ERROR_MISSING_FRUIT_VEG_PRODUCTIVE_UNIT, APPLES_FRUIT_VEG_TYPE_DESCRIPTION),
          errorIterator.next().getMessage());
    }
    
    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
  
  @Test
  public void failForMissingExpectedProduction() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(16.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setReportedAmount(8.0);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(null); // missing
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
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ERROR_MISSING_EXPECTED_PRODUCTION, APPLES_AMBROSIA_1ST_YEAR_CODE, APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION), 
        productionTestResult.getMessages().get(MessageTypeCodes.ERROR).get(0).getMessage());
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("16.0", String.valueOf(item.getReportedProduction()));
        assertEquals("8.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("0.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(true, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      Iterator<FruitVegProductionResult> iterator = productionTestResult.getFruitVegTestResults().iterator();
      
      {
        FruitVegProductionResult result = iterator.next();
        assertNotNull(result);
        
        assertNull(result.getVariance());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("16.0", String.valueOf(result.getReportedProduction()));
        assertEquals("8.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertNull(result.getVariance());
        assertEquals(false, result.getPass());
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(1, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());

      Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(ERROR_MISSING_EXPECTED_PRODUCTION, APPLES_AMBROSIA_1ST_YEAR_CODE, APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION),
          errorIterator.next().getMessage());
    }

    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
  
  @Test
  public void failForMissingInventoryWithProvidedProductiveUnits() {
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());
    scenario.getFarmingYear().getFarmingOperations().get(0).setCropItems(new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(8.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
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
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertEquals(false, productionTestResult.getPassFruitVegTest().booleanValue());
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("0.0", String.valueOf(item.getReportedProduction()));
        assertEquals("8.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("2.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      
      FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
      assertNotNull(result);
      
      assertNull(result.getVariance());
      assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
      assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
      assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
      assertEquals(Double.valueOf(0), result.getReportedProduction());
      assertEquals(Double.valueOf(16), result.getExpectedQuantityProduced());
      assertEquals(new Double(8), result.getProductiveCapacityAmount());
      assertEquals(false, result.getPass());
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(1, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());

      Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(ERROR_MISSING_REPORTED_FRUIT_VEG_INVENTORY, APPLES_FRUIT_VEG_TYPE_DESCRIPTION),
          errorIterator.next().getMessage());
    }

    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
  
  @Test
  public void failWithVarianceGreaterThanLimit() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(6.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(8.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("6.0", String.valueOf(item.getReportedProduction()));
        assertEquals("8.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("2.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
      FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
      assertNotNull(result);
      
      assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
      assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
      assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
      assertEquals(new Double(6), result.getReportedProduction());
      assertEquals(new Double(16), result.getExpectedQuantityProduced());
      assertEquals(new Double(8), result.getProductiveCapacityAmount());
      assertEquals(new Double(-0.625), result.getVariance());
      assertEquals(false, result.getPass());
      assertEquals(true, Math.abs(result.getVariance()) > varianceLimit);
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
  
  @Test
  public void passWithTwoItemsWithCombinedFarm() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(18.0);
    
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setInventoryItemCode(PEACHES_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem2.setFruitVegTypeCode(PEACHES_FRUIT_VEG_TYPE_CODE);
    cropItem2.setFruitVegTypeCodeDescription(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem2.setInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(20.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    List<CropItem> cropItems2 = new ArrayList<>();
    cropItems2.add(cropItem2);
    
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, cropItems2) ;
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(4.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(4));
    
    ProductiveUnitCapacity prodUnitCapacity2 = new ProductiveUnitCapacity();
    prodUnitCapacity2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity2.setInventoryItemCode(PEACHES_INVENTORY_ITEM_CODE);
    prodUnitCapacity2.setInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity2.setRollupInventoryItemCode(PEACHES_INVENTORY_ITEM_CODE);
    prodUnitCapacity2.setRollupInventoryItemCodeDescription(PEACHES_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity2.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity2.setFruitVegTypeCode(PEACHES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity2.setFruitVegTypeCodeDescription(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity2.setReportedAmount(17.0);
    prodUnitCapacity2.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    
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
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(2, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("18.0", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("4.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(PEACHES_INVENTORY_ITEM_CODE, item.getInventoryItemCode());
        assertEquals(PEACHES_INVENTORY_ITEM_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("20.0", String.valueOf(item.getReportedProduction()));
        assertEquals("17.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("1.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("17.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(2, productionTestResult.getFruitVegTestResults().size());
    
      {
        FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
        assertNotNull(result);
        
        assertEquals(new Double(0.125), result.getVariance());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("18.0", String.valueOf(result.getReportedProduction()));
        assertEquals("4.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("16.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
      
      {
        FruitVegProductionResult result2 = productionTestResult.getFruitVegTestResults().get(1);
        assertNotNull(result2);
        
        assertEquals(new Double(0.176), result2.getVariance());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_CODE, result2.getFruitVegTypeCode());
        assertEquals(PEACHES_FRUIT_VEG_TYPE_DESCRIPTION, result2.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result2.getCropUnitCode());
        assertEquals("20.0", String.valueOf(result2.getReportedProduction()));
        assertEquals("17.0", String.valueOf(result2.getProductiveCapacityAmount()));
        assertEquals("17.0", String.valueOf(result2.getExpectedQuantityProduced()));
        
        assertEquals(true, result2.getPass());
        assertEquals(true, result2.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  @Test
  public void failWithVarianceGreaterThanLimitOnCombinedFarm() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(6.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(8.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(2));
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(new Double(-0.625), result.getVariance());
    assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
    assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
    assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
    assertEquals(new Double(6), result.getReportedProduction());
    assertEquals(new Double(16), result.getExpectedQuantityProduced());
    assertEquals(new Double(8), result.getProductiveCapacityAmount());
    assertEquals(false, result.getPass());
    assertEquals(true, Math.abs(result.getVariance()) > varianceLimit);
    
    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
  
  @Test
  public void passWithZeroExpectedProduction() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    cropItem1.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    cropItem1.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(0));
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
    prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
    prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
    prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
    prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    prodUnitCapacity.setReportedAmount(new Double(2));
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(0));
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("0.0", String.valueOf(item.getReportedProduction()));
        assertEquals("2.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("0.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());
    
      {
        FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
        assertNotNull(result);
        
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("0.0", String.valueOf(result.getReportedProduction()));
        assertEquals("2.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals(true, result.getPass());
        assertEquals("0.0", String.valueOf(result.getVariance()));
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  /**
   * The reported inventory and productive units can have different inventory codes but matching fruit & veg codes.
   * For beans there is one inventory code the same and one that only has productive units.
   * For apples the crops and productive units have different inventory codes.
   */
  @Test
  public void passWithMismatchedIventoryAndProductiveUnitCodes() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(BEANS_FRUIT_VEG_TYPE_CODE);
      cropItem.setFruitVegTypeCodeDescription(BEANS_FRUIT_VEG_TYPE_DESCRIPTION);
      cropItem.setInventoryItemCode(BEANS_SNAP_CODE);
      cropItem.setInventoryItemCodeDescription(BEANS_SNAP_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setReportedQuantityProduced(new Double(400));
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
      cropItem.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
      cropItem.setInventoryItemCode(APPLES_CODE);
      cropItem.setInventoryItemCodeDescription(APPLES_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setReportedQuantityProduced(new Double(100));
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(BEANS_SNAP_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(BEANS_SNAP_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(FIELD_VEGETABLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(FIELD_VEGETABLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(BEANS_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(BEANS_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(new Double(8));
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(8));
      prodUnitCapacities.add(prodUnitCapacity);
    }
    
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(BEANS_SNAP_FRESH_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(BEANS_SNAP_FRESH_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(FIELD_VEGETABLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(FIELD_VEGETABLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(BEANS_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(BEANS_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(new Double(20));
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(20));
      prodUnitCapacities.add(prodUnitCapacity);
    }
    
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(new Double(4));
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(4));
      prodUnitCapacities.add(prodUnitCapacity);
    }
    
    {
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_5_PLUS_YEARS_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_5_PLUS_YEARS_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(APPLES_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(APPLES_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(new Double(10));
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(10));
      prodUnitCapacities.add(prodUnitCapacity);
    }
    
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertEquals(0, productionTestResult.getMessages().get(MessageTypeCodes.ERROR).size());
    
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(5, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("0.0", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("4.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_5_PLUS_YEARS_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_5_PLUS_YEARS_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("0.0", String.valueOf(item.getReportedProduction()));
        assertEquals("10.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("10.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("100.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("100.0", String.valueOf(item.getReportedProduction()));
        assertEquals("0.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("0.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(BEANS_SNAP_CODE, item.getInventoryItemCode());
        assertEquals(BEANS_SNAP_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(BEANS_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(BEANS_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("400.0", String.valueOf(item.getReportedProduction()));
        assertEquals("8.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("8.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("64.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
      
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(BEANS_SNAP_FRESH_CODE, item.getInventoryItemCode());
        assertEquals(BEANS_SNAP_FRESH_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertEquals(BEANS_FRUIT_VEG_TYPE_CODE, item.getFruitVegTypeCode());
        assertEquals(BEANS_FRUIT_VEG_TYPE_DESCRIPTION, item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("0.0", String.valueOf(item.getReportedProduction()));
        assertEquals("20.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("20.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("400.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(2, productionTestResult.getFruitVegTestResults().size());  
      
      {
        FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
        assertNotNull(result);
        
        assertEquals(APPLES_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(APPLES_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals(new Double(100), result.getReportedProduction());
        assertEquals(new Double(116), result.getExpectedQuantityProduced());
        assertEquals(new Double(14), result.getProductiveCapacityAmount());
        assertEquals(new Double(-0.138), result.getVariance());
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
      
      {
        FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(1);
        assertNotNull(result);
        
        assertEquals(BEANS_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEANS_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("400.0", String.valueOf(result.getReportedProduction()));
        assertEquals("464.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals("28.0", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("-0.138", String.valueOf(result.getVariance()));
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  @Test
  public void passWithMultipleCropUnits() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(BEANS_FRUIT_VEG_TYPE_CODE);
      cropItem.setFruitVegTypeCodeDescription(BEANS_FRUIT_VEG_TYPE_DESCRIPTION);
      cropItem.setInventoryItemCode(BEANS_SNAP_CODE);
      cropItem.setInventoryItemCodeDescription(BEANS_SNAP_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.CWT_HUNDREDWEIGHT);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.CWT_HUNDREDWEIGHT_DESCRIPTION);
      cropItem.setReportedQuantityProduced(0.02);
      
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem.setFruitVegTypeCode(BEANS_FRUIT_VEG_TYPE_CODE);
      cropItem.setFruitVegTypeCodeDescription(BEANS_FRUIT_VEG_TYPE_DESCRIPTION);
      cropItem.setInventoryItemCode(BEANS_SNAP_CODE);
      cropItem.setInventoryItemCodeDescription(BEANS_SNAP_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setReportedQuantityProduced(1.0);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    {
      // Set the prod unit capacities within the farming operation
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(BEANS_SNAP_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(BEANS_SNAP_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(FIELD_VEGETABLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(FIELD_VEGETABLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(BEANS_FRUIT_VEG_TYPE_CODE);
      prodUnitCapacity.setFruitVegTypeCodeDescription(BEANS_FRUIT_VEG_TYPE_DESCRIPTION);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(0.3);
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(10));
      
      List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
      prodUnitCapacities.add(prodUnitCapacity);
      scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    }
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(1, productionTestResult.getFruitVegTestResults().size());  

      {
        FruitVegProductionResult result = productionTestResult.getFruitVegTestResults().get(0);
        assertNotNull(result);
        
        assertEquals(BEANS_FRUIT_VEG_TYPE_CODE, result.getFruitVegTypeCode());
        assertEquals(BEANS_FRUIT_VEG_TYPE_DESCRIPTION, result.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, result.getCropUnitCode());
        assertEquals("3.0", String.valueOf(result.getReportedProduction()));
        assertEquals("0.3", String.valueOf(result.getProductiveCapacityAmount()));
        assertEquals("3.0", String.valueOf(result.getExpectedQuantityProduced()));
        assertEquals("0.0", String.valueOf(result.getVariance()));
        
        assertEquals(true, result.getPass());
        assertEquals(true, result.getVariance() < varianceLimit);
      }
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
    }
    
    assertEquals(true, productionTestResult.getPassFruitVegTest());
    assertEquals(true, productionTestResult.getResult());
  }
  
  @Test
  public void failWithMissingFruitVegTypeCode() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem1 = new CropItem();
      cropItem1.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      cropItem1.setFruitVegTypeCode(null);
      cropItem1.setFruitVegTypeCodeDescription(null);
      cropItem1.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
      cropItem1.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
      cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem1.setReportedQuantityProduced(18.0);
      cropItem1.setReportedQuantityStart(50.0);
      cropItem1.setReportedQuantityEnd(25.0);
      
      cropItems.add(cropItem1);
    }
       
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    {
      // Set the prod unit capacities within the farming operation
      ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setInventoryItemCode(APPLES_AMBROSIA_1ST_YEAR_CODE);
      prodUnitCapacity.setInventoryItemCodeDescription(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION);
      prodUnitCapacity.setRollupInventoryItemCode(APPLES_CODE);
      prodUnitCapacity.setRollupInventoryItemCodeDescription(APPLES_DESCRIPTION);
      prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FRUIT_VEG);
      prodUnitCapacity.setFruitVegTypeCode(null);
      prodUnitCapacity.setFruitVegTypeCodeDescription(null);
      prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      prodUnitCapacity.setReportedAmount(new Double(4));
      prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(4));
      List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
      prodUnitCapacities.add(prodUnitCapacity);
      scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(prodUnitCapacities);
    }
    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance(false);
    @SuppressWarnings("unused")
    double varianceLimit = config.getProductionFruitVegVariance();
    ProductionTest productionTest = new ProductionTest();
    
    try {
      productionTest.test(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    
    // Inventory Item Code Results
    {
      List<ProductionInventoryItemTestResult> inventoryResults = productionTestResult.getFruitVegInventoryItems();
      assertNotNull(inventoryResults);
      assertEquals(1, inventoryResults.size());
      Iterator<ProductionInventoryItemTestResult> inventoryIterator = inventoryResults.iterator();
  
      {
        ProductionInventoryItemTestResult item = inventoryIterator.next();
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_CODE, item.getInventoryItemCode());
        assertEquals(APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION, item.getInventoryItemCodeDescription());
        assertEquals(CommodityTypeCodes.FRUIT_VEG, item.getCommodityTypeCode());
        assertNull(item.getFruitVegTypeCode());
        assertNull(item.getFruitVegTypeCodeDescription());
        assertEquals(CropUnitCodes.POUNDS, item.getCropUnitCode());
        assertEquals("18.0", String.valueOf(item.getReportedProduction()));
        assertEquals("4.0", String.valueOf(item.getProductiveCapacityAmount()));
        assertEquals("4.0", String.valueOf(item.getExpectedProductionPerUnit()));
        assertEquals("16.0", String.valueOf(item.getExpectedQuantityProduced()));
        assertEquals(false, item.getMissingExpectedProduction());
        assertNull(item.getPass());
        assertNull(item.getVariance());
      }
    }
    
    
    // Fruit & Veg Code Results
    {
      assertEquals(0, productionTestResult.getFruitVegTestResults().size());
    }
    
    
    // Error/warning/info messages
    {
      assertNotNull(productionTestResult.getMessages());
      assertNotNull(productionTestResult.getErrorMessages());
      assertNotNull(productionTestResult.getWarningMessages());
      assertNotNull(productionTestResult.getInfoMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getWarningMessages());
      ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getInfoMessages());
      List<ReasonabilityTestResultMessage> errorMessages = productionTestResult.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = productionTestResult.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = productionTestResult.getInfoMessages();
      assertEquals(1, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());

      Iterator<ReasonabilityTestResultMessage> errorIterator = errorMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(
              ERROR_MISSING_FRUIT_VEG_TYPE_CODE, APPLES_AMBROSIA_1ST_YEAR_CODE, APPLES_AMBROSIA_1ST_YEAR_DESCRIPTION),
          errorIterator.next().getMessage());
    }
    
    assertEquals(false, productionTestResult.getPassFruitVegTest());
    assertEquals(false, productionTestResult.getResult());
  }
}
