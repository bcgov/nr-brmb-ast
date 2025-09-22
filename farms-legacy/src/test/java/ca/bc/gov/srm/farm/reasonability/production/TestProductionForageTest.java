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
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityConfiguration;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityUnitTestUtils;

public class TestProductionForageTest {
  private static Logger logger = LoggerFactory.getLogger(TestProductionForageTest.class);
  
  private static final String HAY_INVENTORY_ITEM_CODE = "5574";
  private static final String HAY_INVENTORY_ITEM_DESCRIPTION = "Hay; Grass";
  
  private static final String APRICOTS_INVENTORY_ITEM_CODE = "5575";
  private static final String APRICOTS_INVENTORY_ITEM_DESCRIPTION = "Apricots";
  
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
    scenario.setIsInCombinedFarmInd(true);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    Scenario scenario2 = new Scenario();
    scenario2.setYear(new Integer(2018));
    scenario2.setIsInCombinedFarmInd(true);
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
    
    FarmingOperation farmOp = new FarmingOperation();
    farmOp.setPartnershipPercent(0.50);
    farmOp.setSchedule("A");
    farmOp.setCropItems(cropItems);
    
    List<FarmingOperation> farmOps = new ArrayList<>();
    farmOps.add(farmOp);
    farmYear.setFarmingOperations(farmOps);
    farmOp.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    FarmingYear farmYear2 = new FarmingYear();
    scenario2.setFarmingYear(farmYear2);
    
    List<FarmingOperation> farmOps2 = new ArrayList<>();
    FarmingOperation farmOp2 = new FarmingOperation();
    farmOp2.setPartnershipPercent(0.50);
    farmOp2.setSchedule("A");
    farmOp2.setCropItems(cropItems2);
    farmOps2.add(farmOp2);
    farmYear2.setFarmingOperations(farmOps2);
    farmOp2.setProductiveUnitCapacities(new ArrayList<ProductiveUnitCapacity>());
    
    return scenario;
  }
  
  @Test
  public void passWithZeroPercentVariance() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
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
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(new Double(0), result.getVariance());
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals(new Double(2), result.getReportedProduction());
    assertEquals(new Double(2), result.getExpectedQuantityProduced());
    assertEquals(new Double(1), result.getExpectedProductionPerUnit());
    assertEquals(new Double(2), result.getProductiveCapacityAmount());

    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
  }
  
  @Test
  public void failForNoProdCapUnitForCropItem() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(1.45));
    
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
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(1, productionTestResult.getErrorMessages().size());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ProductionTest.ERROR_MISSING_PRODUCTIVE_UNIT, HAY_INVENTORY_ITEM_CODE, HAY_INVENTORY_ITEM_DESCRIPTION), 
        productionTestResult.getErrorMessages().get(0).getMessage());
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertNull(result.getVariance());
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertNotNull(result.getReportedProduction());
    assertEquals("1.45", result.getReportedProduction().toString());
    assertNull(result.getExpectedQuantityProduced());
    assertNull(result.getExpectedProductionPerUnit());
    assertNull(result.getProductiveCapacityAmount());
    
    assertEquals(false, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(false, result.getPass());
  }
  
  @Test
  public void passWithConversionToTonnes() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.POUNDS);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(20.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    CropUnitConversion convItem = new CropUnitConversion();
    convItem.setDefaultCropUnitCode(CropUnitCodes.TONNES);
    CropUnitConversionItem convItem1 = new CropUnitConversionItem();
    convItem1.setTargetCropUnitCode(CropUnitCodes.POUNDS);
    convItem1.setConversionFactor(new BigDecimal(2));
    
    List<CropUnitConversionItem> cropConvItems = new ArrayList<>();
    cropConvItems.add(convItem1);
    convItem.setConversionItems(cropConvItems);
    cropItem1.setCropUnitConversion(convItem);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(6.0);
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
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals("-0.167", String.valueOf(result.getVariance()));
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals("10.0", String.valueOf(result.getReportedProduction()));
    assertEquals("12.0", String.valueOf(result.getExpectedQuantityProduced()));
    assertEquals("2.0", String.valueOf(result.getExpectedProductionPerUnit()));
    assertEquals("6.0", String.valueOf(result.getProductiveCapacityAmount()));
    
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
  }
  
  @Test
  public void failForNoConversionToTonnes() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(APRICOTS_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(APRICOTS_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.SMALL_BALES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.SMALL_BALES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(36));
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    CropUnitConversion convItem = new CropUnitConversion();
    convItem.setDefaultCropUnitCode(CropUnitCodes.TONNES);
    
    List<CropUnitConversionItem> cropConvItems = new ArrayList<>();
    convItem.setConversionItems(cropConvItems);
    cropItem1.setCropUnitConversion(convItem);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(APRICOTS_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(APRICOTS_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
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
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(1, productionTestResult.getErrorMessages().size());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ProductionTest.ERROR_MISSING_CONVERSION_FACTORS, APRICOTS_INVENTORY_ITEM_CODE, APRICOTS_INVENTORY_ITEM_DESCRIPTION, CropUnitCodes.SMALL_BALES_DESCRIPTION, CropUnitCodes.TONNES_DESCRIPTION),
        productionTestResult.getErrorMessages().get(0).getMessage());
  
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(APRICOTS_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(APRICOTS_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertNull(result.getReportedProduction());
    assertNull(result.getExpectedQuantityProduced());
    
    assertEquals(false, productionTestResult.getPassForageAndForageSeedTest());
    assertNull(result.getVariance());
    assertEquals(false, result.getPass());
  }
  
  @Test
  public void failForNotReportedInTonnesWithNoProvidedConvFactors() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.SMALL_BALES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.SMALL_BALES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(36));
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);    
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);

    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
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
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(1, productionTestResult.getErrorMessages().size());
    assertNull(productionTestResult.getForageTestResults().get(0).getReportedProduction());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ProductionTest.ERROR_MISSING_CONVERSION_FACTORS, HAY_INVENTORY_ITEM_CODE, HAY_INVENTORY_ITEM_DESCRIPTION, CropUnitCodes.SMALL_BALES_DESCRIPTION, CropUnitCodes.TONNES_DESCRIPTION), 
        productionTestResult.getErrorMessages().get(0).getMessage());
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertNull(result.getReportedProduction());
    assertNull(result.getExpectedQuantityProduced());
    assertNull(result.getExpectedProductionPerUnit());
    
    assertNull(result.getVariance());
    assertEquals(false, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(false, result.getPass());
  }  
  
  @Test
  public void failForNoCropItemWithProdCapUnitProvided() {
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
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
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(1, productionTestResult.getErrorMessages().size());
    assertEquals(false, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ProductionTest.ERROR_MISSING_REPORTED_INVENTORY, HAY_INVENTORY_ITEM_CODE, HAY_INVENTORY_ITEM_DESCRIPTION), 
        productionTestResult.getErrorMessages().get(0).getMessage());
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertNull(result.getVariance());
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertNull(result.getReportedProduction());
    assertNull(result.getExpectedQuantityProduced());
    assertNull(result.getExpectedProductionPerUnit());
    assertNull(result.getExpectedProductionPerUnit());
    assertEquals(false, result.getPass());
  }
  
  @Test
  public void failForVarianceGreaterThanLimit() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(30.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(10.0);
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
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals(new Double(30), result.getReportedProduction());
    assertEquals(new Double(20), result.getExpectedQuantityProduced());
    assertEquals(new Double(2), result.getExpectedProductionPerUnit());
    assertEquals(new Double(10), result.getProductiveCapacityAmount());
    
    assertEquals(false, productionTestResult.getPassForageAndForageSeedTest().booleanValue());
    assertEquals(false, result.getPass());
    assertEquals(true, result.getVariance() > varianceLimit);
    assertEquals(new Double(.5), result.getVariance());
  }
  
  @Test
  public void passForCropItemAndProdCapUnitWithZeroVals() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(0));
        
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
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
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(0, productionTestResult.getErrorMessages().size());
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(0, productionTestResult.getForageTestResults().size());
    
  }
  
  @Test
  public void passForNoCropItemAndProdCapUnitWithZero() {
    Scenario scenario = buildScenarioWithNoCombinedFarm(new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
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
    
    ProductionTestResult productionTestResult = scenario.getReasonabilityTestResults().getProductionTest();
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(0, productionTestResult.getErrorMessages().size());
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(0, productionTestResult.getForageTestResults().size());
  }

  @Test
  public void passForCropItemWithZeroAndNoProdCapUnit() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(new Double(0));
        
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
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(0, productionTestResult.getErrorMessages().size());
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(0, productionTestResult.getForageTestResults().size());  
  }
  
  @Test
  public void passForCropItemWithNullQuantityProducedAndNoProdCapUnit() {
    
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(null);
    cropItem1.setReportedQuantityStart(new Double(0));
    
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
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());
    assertEquals(0, productionTestResult.getErrorMessages().size());
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(0, productionTestResult.getForageTestResults().size());
  }
  
  @Test
  public void passWithTwoItems() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.0);
    
   
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem2.setInventoryItemCode(APRICOTS_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(APRICOTS_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(4.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    cropItems.add(cropItem2);
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity prodUnitCapacity2 = new ProductiveUnitCapacity();
    prodUnitCapacity2.setInventoryItemCode(APRICOTS_INVENTORY_ITEM_CODE);
    prodUnitCapacity2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity2.setReportedAmount(3.4);
    prodUnitCapacity2.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    List<ProductiveUnitCapacity> prodUnitCapacities = new ArrayList<>();
    prodUnitCapacities.add(prodUnitCapacity);
    prodUnitCapacities.add(prodUnitCapacity2);
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
    assertEquals(2, productionTestResult.getForageTestResults().size());
    
    Collections.sort(productionTestResult.getForageTestResults());
    
    {
      ProductionInventoryItemTestResult itemResult = productionTestResult.getForageTestResults().get(0);
      assertNotNull(itemResult);
      
      assertEquals(new Double(0), itemResult.getVariance());
      assertEquals(HAY_INVENTORY_ITEM_CODE, itemResult.getInventoryItemCode());
      assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, itemResult.getInventoryItemCodeDescription());
      assertEquals(CropUnitCodes.TONNES, itemResult.getCropUnitCode());
      assertEquals(new Double(2), itemResult.getReportedProduction());
      assertEquals(new Double(2), itemResult.getExpectedQuantityProduced());
      assertEquals(new Double(1), itemResult.getExpectedProductionPerUnit());
      assertEquals(new Double(2), itemResult.getProductiveCapacityAmount());
      assertEquals(true, itemResult.getPass());
      assertEquals(true, itemResult.getVariance() < varianceLimit);
    }
    
    {
      ProductionInventoryItemTestResult itemResult = productionTestResult.getForageTestResults().get(1);
      assertNotNull(itemResult);
      
      assertEquals(new Double(0.176), itemResult.getVariance());
      assertEquals(APRICOTS_INVENTORY_ITEM_CODE, itemResult.getInventoryItemCode());
      assertEquals(APRICOTS_INVENTORY_ITEM_DESCRIPTION, itemResult.getInventoryItemCodeDescription());
      assertEquals(CropUnitCodes.TONNES, itemResult.getCropUnitCode());
      assertEquals(new Double(4), itemResult.getReportedProduction());
      assertEquals(new Double(3.4), itemResult.getExpectedQuantityProduced());
      assertEquals(new Double(1), itemResult.getExpectedProductionPerUnit());
      assertEquals(new Double(3.4), itemResult.getProductiveCapacityAmount());
      assertEquals(true, itemResult.getPass());
      assertEquals(true, itemResult.getVariance() < varianceLimit);
    }
    
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
  }
  
  @Test
  public void passWithTwoItemsWithCombinedFarm() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(2.0);
    
    CropItem cropItem2 = new CropItem();
    cropItem2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem2.setInventoryItemCode(APRICOTS_INVENTORY_ITEM_CODE);
    cropItem2.setInventoryItemCodeDescription(APRICOTS_INVENTORY_ITEM_DESCRIPTION);
    cropItem2.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem2.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem2.setReportedQuantityProduced(4.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    List<CropItem> cropItems2 = new ArrayList<>();
    cropItems2.add(cropItem2);
    
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, cropItems2);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
    prodUnitCapacity.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    
    ProductiveUnitCapacity prodUnitCapacity2 = new ProductiveUnitCapacity();
    prodUnitCapacity2.setInventoryItemCode(APRICOTS_INVENTORY_ITEM_CODE);
    prodUnitCapacity2.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity2.setReportedAmount(3.4);
    prodUnitCapacity2.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
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
    assertEquals(2, productionTestResult.getForageTestResults().size());
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    
    Collections.sort(productionTestResult.getForageTestResults());
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(new Double(0), result.getVariance());
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals(new Double(2), result.getReportedProduction());
    assertEquals(new Double(2), result.getExpectedQuantityProduced());
    assertEquals(new Double(1), result.getExpectedProductionPerUnit());
    assertEquals(new Double(2), result.getProductiveCapacityAmount());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
    
    
    ProductionInventoryItemTestResult result2 = productionTestResult.getForageTestResults().get(1);
    assertNotNull(result2);
    
    assertEquals(new Double(0.176), result2.getVariance());
    assertEquals(APRICOTS_INVENTORY_ITEM_CODE, result2.getInventoryItemCode());
    assertEquals(APRICOTS_INVENTORY_ITEM_DESCRIPTION, result2.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result2.getCropUnitCode());
    assertEquals(new Double(4), result2.getReportedProduction());
    assertEquals(new Double(3.4), result2.getExpectedQuantityProduced());
    assertEquals(new Double(1), result2.getExpectedProductionPerUnit());
    assertEquals(new Double(3.4), result2.getProductiveCapacityAmount());
    assertEquals(true, result2.getPass());
    assertEquals(true, result2.getVariance() < varianceLimit);
  }
  
  @Test
  public void failForVarianceGreaterThanLimitOnCombinedFarm() {
    CropItem cropItem1 = new CropItem();
    cropItem1.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    cropItem1.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    cropItem1.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    cropItem1.setCropUnitCode(CropUnitCodes.TONNES);
    cropItem1.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
    cropItem1.setReportedQuantityProduced(30.0);
    
    List<CropItem> cropItems = new ArrayList<>(); 
    cropItems.add(cropItem1);
    
    Scenario scenario = buildScenarioWithCombinedFarm(cropItems, new ArrayList<CropItem>());
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(10.0);
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
    assertEquals(0, productionTestResult.getErrorMessages().size());
    assertNotNull(productionTestResult);
    assertNotNull(productionTestResult.getMessages());
    assertNotNull(productionTestResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, productionTestResult.getErrorMessages());

    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals(new Double(30), result.getReportedProduction());
    assertEquals(new Double(20), result.getExpectedQuantityProduced());
    assertEquals(new Double(2), result.getExpectedProductionPerUnit());
    assertEquals(new Double(10), result.getProductiveCapacityAmount());
    
    assertEquals(false, productionTestResult.getPassForageAndForageSeedTest().booleanValue());
    assertEquals(false, result.getPass());
    assertEquals(true, result.getVariance() > varianceLimit);
    assertEquals(new Double(.5), result.getVariance());
  }
  
  @Test
  public void passWithMultipleCropUnits() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(1.0);
      
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.POUNDS);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.POUNDS_DESCRIPTION);
      cropItem.setReportedQuantityProduced(2204.6226);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
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
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(new Double(0), result.getVariance());
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals("2.0", result.getReportedProduction().toString());
    assertEquals("2.0", result.getExpectedQuantityProduced().toString());
    assertEquals("1.0", result.getExpectedProductionPerUnit().toString());
    assertEquals("2.0", result.getProductiveCapacityAmount().toString());
    
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
  }
  
  @Test
  public void passWithMultipleCropItemsSameInventoryCodeAndUnit() {
    
    List<CropItem> cropItems = new ArrayList<>(); 
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(1.0);
      
      cropItems.add(cropItem);
    }
    
    {
      CropItem cropItem = new CropItem();
      cropItem.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      cropItem.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
      cropItem.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      cropItem.setCropUnitCode(CropUnitCodes.TONNES);
      cropItem.setCropUnitCodeDescription(CropUnitCodes.TONNES_DESCRIPTION);
      cropItem.setReportedQuantityProduced(1.0);
      
      cropItems.add(cropItem);
    }
    
    Scenario scenario = buildScenarioWithNoCombinedFarm(cropItems);
    
    // Set the prod unit capacities within the farming operation
    ProductiveUnitCapacity prodUnitCapacity = new ProductiveUnitCapacity();
    prodUnitCapacity.setInventoryItemCode(HAY_INVENTORY_ITEM_CODE);
    prodUnitCapacity.setInventoryItemCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
    prodUnitCapacity.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
    prodUnitCapacity.setReportedAmount(2.0);
    prodUnitCapacity.setExpectedProductionPerProductiveUnit(new BigDecimal(1));
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
    
    ProductionInventoryItemTestResult result = productionTestResult.getForageTestResults().get(0);
    assertNotNull(result);
    
    assertEquals(new Double(0), result.getVariance());
    assertEquals(HAY_INVENTORY_ITEM_CODE, result.getInventoryItemCode());
    assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, result.getInventoryItemCodeDescription());
    assertEquals(CropUnitCodes.TONNES, result.getCropUnitCode());
    assertEquals("2.0", result.getReportedProduction().toString());
    assertEquals("2.0", result.getExpectedQuantityProduced().toString());
    assertEquals("1.0", result.getExpectedProductionPerUnit().toString());
    assertEquals("2.0", result.getProductiveCapacityAmount().toString());
    
    assertEquals(true, productionTestResult.getPassForageAndForageSeedTest());
    assertEquals(true, result.getPass());
    assertEquals(true, result.getVariance() < varianceLimit);
  }
}
