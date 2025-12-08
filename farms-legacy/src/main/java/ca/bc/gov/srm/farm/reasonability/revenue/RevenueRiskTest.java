package ca.bc.gov.srm.farm.reasonability.revenue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.RevenueRiskTestResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTest;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;

public class RevenueRiskTest extends ReasonabilityTest {

  // General error messages
  public static final String ERROR_MISSING_INCOME_FOR_LINE_ITEM =
      "Income not found for Line Item: {0} - {1}.";
  public static final String ERROR_MISSING_INVENTORY_FOR_INCOME =
      "Crop not found for Reported Income: {0} - {1}.";
  public static final String ERROR_MISSING_INVENTORY_FOR_RECEIVABLE =
      "Crop not found for Receivable: {0} - {1}.";
  public static final String ERROR_MISSING_INCOME_FOR_RECEIVABLE =
      "Income not found for Receivable: {0} - {1}.";
  public static final String ERROR_MISSING_PRICE =
      "Price not found for Crop: {0} - {1}.";
  public static final String ERROR_MISSING_CONVERSIONS =
      "Conversion Factors not found for: {0} - {1}. Failed to convert {2} to {3}.";
  public static final String ERROR_INVENTORY_MISSING_FMV =
      "FMV not found for crop: {0} - {1}.";
  public static final String ERROR_PRODUCTIVE_UNIT_MISSING_FMV =
      "FMV not found for Productive Unit Code: {0}.";

  private void initializeTestResult(RevenueRiskTestResult result, Scenario scenario) {
    
    result.setMessages(new HashMap<String, List<ReasonabilityTestResultMessage>>());
    result.getMessages().put(MessageTypeCodes.ERROR, new ArrayList<ReasonabilityTestResultMessage>());
    result.getMessages().put(MessageTypeCodes.WARNING, new ArrayList<ReasonabilityTestResultMessage>());
    result.getMessages().put(MessageTypeCodes.INFO, new ArrayList<ReasonabilityTestResultMessage>());
    
    result.setReasonabilityTestResults(scenario.getReasonabilityTestResults());
  }

  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    
    RevenueRiskTestResult result = new RevenueRiskTestResult();
    initializeTestResult(result, scenario);
    result.setResult(Boolean.TRUE);
    scenario.getReasonabilityTestResults().setRevenueRiskTest(result);
    
    GrainForageRevenueRiskSubTest grainForageTest = new GrainForageRevenueRiskSubTest();
    grainForageTest.test(scenario);
    
    FruitVegRevenueRiskSubTest fruitVegTest = new FruitVegRevenueRiskSubTest();
    fruitVegTest.test(scenario);
    
    CattleRevenueRiskSubTest cattleTest = new CattleRevenueRiskSubTest();
    cattleTest.test(scenario);
    
    NurseryRevenueRiskSubTest nurseryTest = new NurseryRevenueRiskSubTest();
    nurseryTest.test(scenario);
    
    HogsRevenueRiskSubTest hogsTest = new HogsRevenueRiskSubTest();
    hogsTest.test(scenario);
    
    PoultryBroilersRevenueRiskSubTest poultryBroilersTest = new PoultryBroilersRevenueRiskSubTest();
    poultryBroilersTest.test(scenario);
    
    PoultryEggsRevenueRiskSubTest poultryEggsTest = new PoultryEggsRevenueRiskSubTest();
    poultryEggsTest.test(scenario);
    
    determineTestResult(result);
  }
  
  private void determineTestResult(RevenueRiskTestResult result) {
    if (!result.getForageGrainPass()
        || !result.getFruitVegTestPass()
        || !result.getNursery().getSubTestPass()
        || !result.getPoultryBroilers().getSubTestPass()
        || !result.getPoultryEggs().getSubTestPass()
        || !result.getHogs().getHogsPass()) {
      result.setResult(false);
    }
  }
  
  public static boolean missingRequiredData(Scenario scenario) {
    if(scenario.getProgramYearScenarios() == null) {
      return true;
    }
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      if(programYearScenario.getFarmingYear() == null || programYearScenario.getFarmingYear().getFarmingOperations() == null) {
        return true;
      }
    }
    return false;
  }
}