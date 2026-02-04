package ca.bc.gov.srm.farm.reasonability.production;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionInventoryItemTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.production.ProductionTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTest;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;
import ca.bc.gov.srm.farm.util.CropItemUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ProductionTest extends ReasonabilityTest {
  
  // General messages
  public static final String ERROR_MISSING_PRODUCTIVE_UNIT = 
      "Productive Units not found for: {0} - {1}.";
  public static final String ERROR_MISSING_EXPECTED_PRODUCTION =
      "Expected Production not found for: {0} - {1}.";
  public static final String ERROR_MISSING_CONVERSION_FACTORS =
      "Conversion Factors not found for: {0} - {1}. Failed to convert {2} to {3}.";
  public static final String ERROR_MISSING_REPORTED_INVENTORY = 
      "Crop inventory not found for: {0} - {1}.";
  
  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    
    ProductionTestResult result = initializeTestResult();
    scenario.getReasonabilityTestResults().setProductionTest(result);
    
    List<CropItem> fruitVegItems = new ArrayList<>();
    Map<String, List<CropItem>> forageItemsMap = new HashMap<>();
    Map<String, List<CropItem>> forageSeedItemsMap = new HashMap<>();
    Map<String, List<CropItem>> grainItemsMap = new HashMap<>();
    HashMap<String, Boolean> testedInv = new HashMap<>();
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for (FarmingOperation farmingOperation : programYearScenario.getFarmingYear().getFarmingOperations()) {
        if (farmingOperation.getCropItems() == null) {
          continue;
        }
        
        for (CropItem cropItem : farmingOperation.getCropItems()) {
          if(CropItemUtils.hasNonZeroQuantities(cropItem)) {
            if (cropItem.getFruitVegTypeCode() != null) {
              testedInv.put(cropItem.getInventoryItemCode(), true);
              fruitVegItems.add(cropItem);
            } else if (cropItem.isForage()) {
              List<CropItem> itemsForThisCode = forageItemsMap.get(cropItem.getInventoryItemCode());
              if (itemsForThisCode == null) {
                itemsForThisCode = new ArrayList<>();
                forageItemsMap.put(cropItem.getInventoryItemCode(), itemsForThisCode);
              }
              itemsForThisCode.add(cropItem);
              testedInv.put(cropItem.getInventoryItemCode(), true);
            } else if (cropItem.isForageSeed()) {
              List<CropItem> itemsForThisCode = forageSeedItemsMap.get(cropItem.getInventoryItemCode());
              if (itemsForThisCode == null) {
                itemsForThisCode = new ArrayList<>();
                forageSeedItemsMap.put(cropItem.getInventoryItemCode(), itemsForThisCode);
              }
              itemsForThisCode.add(cropItem);
              testedInv.put(cropItem.getInventoryItemCode(), true);
            } else if (cropItem.isGrain()) {
              List<CropItem> itemsForThisCode = grainItemsMap.get(cropItem.getInventoryItemCode());
              if (itemsForThisCode == null) {
                itemsForThisCode = new ArrayList<>();
                grainItemsMap.put(cropItem.getInventoryItemCode(), itemsForThisCode);
              }
              itemsForThisCode.add(cropItem);
              testedInv.put(cropItem.getInventoryItemCode(), true);
            }
          }
        }
      }      
    }
    
    Map<String, ProductiveUnitCapacity> productiveUnitCapacities = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario, null, false, false);
    
    ForageProductionSubTest forageTest = new ForageProductionSubTest();
    forageTest.performForageTest(forageItemsMap, result, productiveUnitCapacities);
    forageTest.performForageSeedTest(forageSeedItemsMap, result, productiveUnitCapacities);
    
    GrainProductionSubTest grainTest = new GrainProductionSubTest();
    grainTest.test(grainItemsMap, result, productiveUnitCapacities);
    
    FruitVegProductionSubTest fruitVegTest = new FruitVegProductionSubTest();
    fruitVegTest.test(scenario);
    
    checkMissingInvItems(productiveUnitCapacities, testedInv, result);
  }
  
  private ProductionTestResult initializeTestResult() {
    ProductionTestResult result = new ProductionTestResult();

    result.setForageTestResults(new ArrayList<ProductionInventoryItemTestResult>());
    result.setForageSeedTestResults(new ArrayList<ProductionInventoryItemTestResult>());
    result.setGrainItemTestResults(new ArrayList<ProductionInventoryItemTestResult>());
    
    result.setMessages(new HashMap<String, List<ReasonabilityTestResultMessage>>());
    result.getMessages().put(MessageTypeCodes.ERROR, new ArrayList<ReasonabilityTestResultMessage>());
    result.getMessages().put(MessageTypeCodes.WARNING, new ArrayList<ReasonabilityTestResultMessage>());
    result.getMessages().put(MessageTypeCodes.INFO, new ArrayList<ReasonabilityTestResultMessage>());
    
    return result;
  }
  
  public static void setProductionTestResult(ProductionTestResult result, boolean curTestResult) {
    if (result.getResult() != null) {
      result.setResult(result.getResult() && curTestResult);
    } else {
      result.setResult(curTestResult);
    }
  }
  
  
  /* Checks if Productive Capacity Units were provided, but no Inventory Reported */
  private void checkMissingInvItems(Map<String, ProductiveUnitCapacity> productiveUnitCapacities, HashMap<String, Boolean> testedInv, ProductionTestResult result) {
    for (ProductiveUnitCapacity pu : productiveUnitCapacities.values()) {
      if (!testedInv.containsKey(pu.getInventoryItemCode()) && 
          (pu.isGrain() || pu.isForage() || pu.isForageSeed()) && 
          !pu.getTotalProductiveCapacityAmount().equals(0d)) {
        
        result.addErrorMessage(ERROR_MISSING_REPORTED_INVENTORY, pu.getInventoryItemCode(), pu.getInventoryItemCodeDescription());
        result.setResult(false);
        if (pu.isGrain() || pu.isForage() || pu.isForageSeed()) {
          ProductionInventoryItemTestResult testResult = null;
          
          if (pu.isForage()) {
            testResult =  new ProductionInventoryItemTestResult();
            testResult.setCropUnitCode(CropUnitCodes.TONNES);
            result.getForageTestResults().add(testResult);
            result.setPassForageAndForageSeedTest(Boolean.FALSE);
          } else if (pu.isForageSeed()) {
            testResult =  new ProductionInventoryItemTestResult();
            testResult.setCropUnitCode(CropUnitCodes.POUNDS);
            result.getForageSeedTestResults().add(testResult);
            result.setPassForageAndForageSeedTest(Boolean.FALSE);
          } else if (pu.isGrain()) {
            testResult =  new ProductionInventoryItemTestResult();
            testResult.setCropUnitCode(CropUnitCodes.TONNES);
            result.getGrainItemTestResults().add(testResult);
            result.setPassGrainTest(Boolean.FALSE);
          }
          if (testResult != null) {
            testResult.setProductiveCapacityAmount(pu.getTotalProductiveCapacityAmount());
            testResult.setInventoryItemCode(pu.getInventoryItemCode());
            testResult.setInventoryItemCodeDescription(pu.getInventoryItemCodeDescription());
            testResult.setPass(Boolean.FALSE);
          }
        }
      }
    }
  }

  public static boolean missingRequiredData(Scenario scenario) {
    if (scenario.getProgramYearScenarios() == null) {
      return true;
    }
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      if (programYearScenario.getFarmingYear() == null || programYearScenario.getFarmingYear().getFarmingOperations() == null) {
        return true;
      }
    }
    return false;
  }
}
