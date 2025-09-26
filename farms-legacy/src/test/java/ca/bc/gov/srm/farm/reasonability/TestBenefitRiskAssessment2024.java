/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.reasonability;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCodes;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskAssessmentTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskProductiveUnit;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.revenue.RevenueRiskTest;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.TestUtils;
import ca.bc.gov.srm.farm.util.UnitTestModelBuilder;

/**
 * @author awilkinson
 * @created July 8, 2025
 */
public class TestBenefitRiskAssessment2024 {

  private static Logger logger = LoggerFactory.getLogger(TestBenefitRiskAssessment2024.class);
  
  private static final String SILAGE_CORN_INVENTORY_ITEM_CODE = "5583";
  private static final String SILAGE_CORN_INVENTORY_ITEM_DESCRIPTION = "Silage; Corn";

  private static final String SILAGE_INVENTORY_ITEM_CODE = "5584";
  private static final String SILAGE_INVENTORY_ITEM_DESCRIPTION = "Silage";

  private static final String GREENFEED_INVENTORY_ITEM_CODE = "5562";
  private static final String GREENFEED_INVENTORY_ITEM_DESCRIPTION = "Greenfeed";

  private static final String HAY_INVENTORY_ITEM_CODE = "5574";
  private static final String HAY_INVENTORY_ITEM_DESCRIPTION = "Hay; Grass";

  private static final Integer FORAGE_LINE_ITEM = 264;
  private static final String FORAGE_LINE_ITEM_DESCRIPTION = "Forage (including pellets; silage)";

  private static final int PROGRAM_YEAR = 2024;
  
  private static final int PROGRAM_YEAR_MINUS_1 = PROGRAM_YEAR - 1;
  private static final int PROGRAM_YEAR_MINUS_2 = PROGRAM_YEAR - 2;
  private static final int PROGRAM_YEAR_MINUS_3 = PROGRAM_YEAR - 3;
  private static final int PROGRAM_YEAR_MINUS_4 = PROGRAM_YEAR - 4;
  private static final int PROGRAM_YEAR_MINUS_5 = PROGRAM_YEAR - 5;
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  @Test
  public void pass5YearsZeroBenefit() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(1243.74);
    scenario.getBenefit().setStandardBenefit(0.0);
    scenario.getBenefit().setTotalBenefit(0.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(0.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(1.52);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
        assertEquals("6885.74", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    ReasonabilityUnitTestUtils.logMessages(logger, errorMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(1, infoMessages.size());
    
    {
      Iterator<ReasonabilityTestResultMessage> infoMessageIterator = infoMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(BenefitRiskAssessment.INFO_PASS_UNDER_MINIMUM_BENEFIT),
          infoMessageIterator.next().getMessage());
    }
    
    assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
    assertEquals("0.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("6885.74", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("4820.02", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("3576.28", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("2503.4", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals(null, result.getVariance());
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  
  @Test
  public void pass5Years249Benefit() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(1243.74);
    scenario.getBenefit().setStandardBenefit(249.99);
    scenario.getBenefit().setTotalBenefit(280.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(0.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(1.52);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
        assertEquals("6885.74", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    ReasonabilityUnitTestUtils.logMessages(logger, errorMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(1, infoMessages.size());
    
    {
      Iterator<ReasonabilityTestResultMessage> infoMessageIterator = infoMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(BenefitRiskAssessment.INFO_PASS_UNDER_MINIMUM_BENEFIT),
          infoMessageIterator.next().getMessage());
    }

    assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
    assertEquals("249.99", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("6885.74", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("4820.02", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("3576.28", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("2503.4", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals(null, result.getVariance());
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  @Test
  public void pass5YearsBenefitOver250() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(1243.74);
    scenario.getBenefit().setStandardBenefit(2823.0);
    scenario.getBenefit().setTotalBenefit(3000.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(0.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(1.52);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
        assertEquals("6885.74", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
    assertEquals("2823.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("6885.74", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("4820.02", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("3576.28", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("2503.4", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.128", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  @Test
  public void fail5YearsWithNoLag() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(100000.0);
    scenario.getBenefit().setStandardBenefit(114685.27);
    scenario.getBenefit().setEnhancedAdditionalBenefit(4685.27);
    scenario.getBenefit().setEnhancedTotalBenefit(120000.00);
    scenario.getBenefit().setTotalBenefit(120000.00);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("62.9", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("62.9", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
        assertEquals("284942.91", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("100000.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("114685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("284942.91", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("199460.04", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("99460.04", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("69622.03", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.647", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(false, result.getResult());
  }
  
  @Test
  public void pass5YearsWithLag() throws Exception {
    
    Scenario scenario = buildScenario();
    
    scenario.getBenefit().setProgramYearMargin(130363.24);
    scenario.getBenefit().setStandardBenefit(80685.27);
    scenario.getBenefit().setTotalBenefit(83000.0);
    
    UnitTestModelBuilder.setBpuLeadInd(scenario, PROGRAM_YEAR_MINUS_1, false);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("62.9", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("62.9", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5228.86", String.valueOf(puc.getBpuCalculated()));
        assertEquals("328895.42", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("130363.24", String.valueOf(result.getProgramYearMargin()));
    assertEquals("80685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("328895.42", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("230226.79", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("99863.55", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("69904.49", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.154", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  @Test
  public void fail4YearsWithNoLag() throws Exception {
    
    Scenario scenario = buildScenario();
    
    scenario.getBenefit().setProgramYearMargin(100000.0);
    scenario.getBenefit().setStandardBenefit(114685.27);
    scenario.getBenefit().setTotalBenefit(120000.00);
    
    UnitTestModelBuilder.removeReferenceYear(scenario, PROGRAM_YEAR_MINUS_5);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("62.9", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("62.9", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4079.0", String.valueOf(puc.getBpuCalculated()));
        assertEquals("256568.79", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("100000.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("114685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("256568.79", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("179598.15", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("79598.15", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("55718.7", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("1.058", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(false, result.getResult());
  }
  
  @Test
  public void pass4YearsWithLag() throws Exception {
    
    Scenario scenario = buildScenario();
    
    scenario.getBenefit().setProgramYearMargin(80000.0);
    scenario.getBenefit().setStandardBenefit(114685.27);
    scenario.getBenefit().setTotalBenefit(130000.0);
    
    UnitTestModelBuilder.removeReferenceYear(scenario, PROGRAM_YEAR_MINUS_5);
    UnitTestModelBuilder.setBpuLeadInd(scenario, PROGRAM_YEAR_MINUS_1, false);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(10.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(55.768);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("65.768", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("65.768", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4952.46", String.valueOf(puc.getBpuCalculated()));
        assertEquals("325713.06", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("80000.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("114685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("325713.06", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("227999.14", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("147999.14", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("103599.4", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.107", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  @Test
  public void pass3YearsWithLag() throws Exception {
    
    Scenario scenario = buildScenario();
    
    scenario.getBenefit().setProgramYearMargin(80000.0);
    scenario.getBenefit().setStandardBenefit(114685.27);
    scenario.getBenefit().setTotalBenefit(130000.0);

    UnitTestModelBuilder.removeReferenceYear(scenario, PROGRAM_YEAR_MINUS_5);
    UnitTestModelBuilder.removeReferenceYear(scenario, PROGRAM_YEAR_MINUS_4);
    UnitTestModelBuilder.setBpuLeadInd(scenario, PROGRAM_YEAR_MINUS_2, false);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(100.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(19.731);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("119.731", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("119.731", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("3358.07", String.valueOf(puc.getBpuCalculated()));
        assertEquals("402065.08", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("80000.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("114685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("402065.08", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("281445.56", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("201445.56", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("141011.89", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("-0.187", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  
  @Test
  public void failZeroBenefitBenchmark() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(0.0);
    scenario.getBenefit().setStandardBenefit(1000.0);
    scenario.getBenefit().setTotalBenefit(1200.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(0.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(0.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(0, productiveUnits.size());
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(1, infoMessages.size());
    
    {
      Iterator<ReasonabilityTestResultMessage> infoMessageIterator = infoMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(BenefitRiskAssessment.INFO_PASS_BENCHMARK_ZERO_OR_NEGATIVE),
          infoMessageIterator.next().getMessage());
    }
    
    assertEquals("0.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("1000.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("0.0", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("0.0", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("0.0", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("0.0", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals(null, result.getVariance());
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  
  @Test
  public void failNegativeBenefitBenchmark() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(1243.74);
    scenario.getBenefit().setStandardBenefit(1000.0);
    scenario.getBenefit().setTotalBenefit(1200.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(-1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setRollupInventoryItemCode("5002");
      puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
      puc.setReportedAmount(-2.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    UnitTestModelBuilder.setBpus(scenario);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("-3.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("-3.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
        assertEquals("-13590.28", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(1, infoMessages.size());
    
    {
      Iterator<ReasonabilityTestResultMessage> infoMessageIterator = infoMessages.iterator();
      assertEquals(
          ReasonabilityTestResultMessage.createMessageWithParameters(BenefitRiskAssessment.INFO_PASS_BENCHMARK_ZERO_OR_NEGATIVE),
          infoMessageIterator.next().getMessage());
    }
    
    assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
    assertEquals("1000.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("-13590.28", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("-9513.2", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("-10756.94", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("-7529.86", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals(null, result.getVariance());
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  
  @Test
  public void fail5YearsWithNoLagNegativeVariance() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(100000.0);
    scenario.getBenefit().setStandardBenefit(685.27);
    scenario.getBenefit().setTotalBenefit(750.0);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(1, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5002", puc.getCode());
        assertEquals("Blueberries; Highbush", puc.getDescription());
        assertEquals("62.9", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("62.9", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
        assertEquals("284942.91", String.valueOf(puc.getEstimatedIncome()));
      }
    }
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("100000.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("284942.91", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("199460.04", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("99460.04", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("69622.03", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("-0.99", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(false, result.getResult());
  }
  
  @Test
  public void failCombinedFarm() throws Exception {
    
    CombinedFarm combinedFarm = new CombinedFarm();
    Benefit combinedFarmBenefit = new Benefit();
    combinedFarm.setBenefit(combinedFarmBenefit);
    List<Scenario> combinedFarmScenarios = new ArrayList<>();
    combinedFarm.setScenarios(combinedFarmScenarios);
    
    double programYearMargin = 1243.74;
    double combinedFarmStandardBenefit = 3123.0;
    double combinedFarmTotalBenefit = 3500.0;
    
    combinedFarmBenefit.setProgramYearMargin(programYearMargin);
    combinedFarmBenefit.setStandardBenefit(combinedFarmStandardBenefit);
    combinedFarmBenefit.setTotalBenefit(combinedFarmTotalBenefit);
    
    // Start build scenario 1
    {
      Scenario scenario = buildScenario();
      scenario.setIsInCombinedFarmInd(true);
      
      double combinedFarmPercent = 0.785;
      
      combinedFarmScenarios.add(scenario);
      scenario.setCombinedFarm(combinedFarm);
      scenario.getClient().setParticipantPin(80000000);
      
      scenario.getFarmingYear().getBenefit().setProgramYearMargin(combinedFarmBenefit.getProgramYearMargin());
      scenario.getFarmingYear().getBenefit().setAppliedBenefitPercent(combinedFarmPercent);
      scenario.getFarmingYear().getBenefit().setStandardBenefit(MathUtils.roundCurrency(combinedFarmStandardBenefit * combinedFarmPercent));
      scenario.getFarmingYear().getBenefit().setTotalBenefit(MathUtils.roundCurrency(combinedFarmTotalBenefit * combinedFarmPercent));
      
      combinedFarm.getBenefit().setProgramYearMargin(scenario.getFarmingYear().getBenefit().getProgramYearMargin());
      
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        productiveUnitCapacities.add(puc);
        puc.setInventoryItemCode("5062");
        puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
        puc.setRollupInventoryItemCode("5002");
        puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
        puc.setReportedAmount(2.0);
        puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      }
      scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
      UnitTestModelBuilder.setBpus(scenario);
    }
    // End build scenario 1
    
    
    // Start build scenario 2
    {
      Scenario scenario = buildScenario();
      scenario.setIsInCombinedFarmInd(true);
      
      double combinedFarmPercent = 0.215;
      
      combinedFarmScenarios.add(scenario);
      scenario.setCombinedFarm(combinedFarm);
      scenario.getClient().setParticipantPin(90000000);
      
      scenario.getFarmingYear().getBenefit().setProgramYearMargin(programYearMargin);
      scenario.getFarmingYear().getBenefit().setAppliedBenefitPercent(combinedFarmPercent);
      scenario.getFarmingYear().getBenefit().setStandardBenefit(MathUtils.roundCurrency(combinedFarmStandardBenefit * combinedFarmPercent));
      scenario.getFarmingYear().getBenefit().setTotalBenefit(MathUtils.roundCurrency(combinedFarmTotalBenefit * combinedFarmPercent));
      
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        productiveUnitCapacities.add(puc);
        puc.setInventoryItemCode("5064");
        puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
        puc.setRollupInventoryItemCode("5002");
        puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
        puc.setReportedAmount(1.52);
        puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      }
      scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
      UnitTestModelBuilder.setBpus(scenario);
    }
    // End build scenario 2
    

    // Run the test using scenario 1
    runBenefitTest(combinedFarm.getScenarios().get(0));
    
    // Start scenario 1 assertions
    {
      Scenario scenario = combinedFarm.getScenarios().get(0);
  
      BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
      assertNotNull(result);
      
      {
        List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
        assertNotNull(productiveUnits);
        assertEquals(1, productiveUnits.size());
        Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
        {
          BenefitRiskProductiveUnit puc = iterator.next();
          assertEquals("5002", puc.getCode());
          assertEquals("Blueberries; Highbush", puc.getDescription());
          assertEquals("3.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
          assertNull(puc.getConsumedProductiveCapacityAmount());
          assertEquals("3.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
          assertEquals("4530.09", String.valueOf(puc.getBpuCalculated()));
          assertEquals("15945.93", String.valueOf(puc.getEstimatedIncome()));
        }
      }
      
      List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
      assertNotNull(errorMessages);
      assertNotNull(warningMessages);
      assertNotNull(infoMessages);
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
      
      assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
      assertEquals("2451.56", String.valueOf(result.getTotalBenefit()));
      
      assertEquals("15945.93", String.valueOf(result.getBenchmarkMargin()));
      assertEquals("11162.15", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
      assertEquals("9918.41", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
      assertEquals("6942.89", String.valueOf(result.getBenefitBenchmarkBeforeCombinedFarmPercent()));
      assertEquals("0.785", String.valueOf(result.getCombinedFarmPercent()));
      assertEquals("5450.17", String.valueOf(result.getBenefitBenchmark()));
      
      assertEquals("-0.55", String.valueOf(result.getVariance()));
      assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
      assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
      assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
      assertEquals(false, result.getResult());
    }
    // End scenario 1 assertions
    
    
    // Start scenario 2 assertions
    {
      ReasonabilityTestResults scenario1ReasonabilityResults = combinedFarm.getScenarios().get(0).getReasonabilityTestResults();
      Scenario scenario = combinedFarm.getScenarios().get(1);
      scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
      scenario.getReasonabilityTestResults().copy(scenario1ReasonabilityResults);
      
  
      BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
      assertNotNull(result);
      
      // Productive Units results already checked above
      
      List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
      List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
      List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
      assertNotNull(errorMessages);
      assertNotNull(warningMessages);
      assertNotNull(infoMessages);
      assertEquals(0, errorMessages.size());
      assertEquals(0, warningMessages.size());
      assertEquals(0, infoMessages.size());
      
      assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
      assertEquals("671.45", String.valueOf(result.getTotalBenefit()));
      
      assertEquals("15945.93", String.valueOf(result.getBenchmarkMargin()));
      assertEquals("11162.15", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
      assertEquals("9918.41", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
      assertEquals("6942.89", String.valueOf(result.getBenefitBenchmarkBeforeCombinedFarmPercent()));
      assertEquals("0.215", String.valueOf(result.getCombinedFarmPercent()));
      assertEquals("1492.72", String.valueOf(result.getBenefitBenchmark()));
      
      assertEquals("-0.55", String.valueOf(result.getVariance()));
      assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
      assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
      assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
      assertEquals(false, result.getResult());
    }
    // End scenario 2 assertions
  }

  private Scenario buildScenario() {
    Scenario scenario = new Scenario();
    scenario.setIsInCombinedFarmInd(false);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    Client client = new Client();
    scenario.setClient(client);
    
    {
      scenario.setYear(PROGRAM_YEAR);
      FarmingYear farmingYear = new FarmingYear();
      scenario.setFarmingYear(farmingYear);
      {
        Benefit benefit = new Benefit();
        farmingYear.setBenefit(benefit);
      }
      List<FarmingOperation> farmingOperations = new ArrayList<>();
      {
        FarmingOperation farmingOperation = new FarmingOperation();
        farmingOperations.add(farmingOperation);
        List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
        {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          productiveUnitCapacities.add(puc);
          puc.setInventoryItemCode("5062");
          puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
          puc.setRollupInventoryItemCode("5002");
          puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
          puc.setReportedAmount(26.2);
          puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
        }
        {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          productiveUnitCapacities.add(puc);
          puc.setInventoryItemCode("5064");
          puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
          puc.setRollupInventoryItemCode("5002");
          puc.setRollupInventoryItemCodeDescription("Blueberries; Highbush");
          puc.setReportedAmount(36.7);
          puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
        }
        farmingYear.setFarmingOperations(farmingOperations);
        farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
      }
    }
    {
      List<ReferenceScenario> referenceScenarios = new ArrayList<>();
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_1, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_2, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_3, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_4, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_5, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      scenario.setReferenceScenarios(referenceScenarios);
      UnitTestModelBuilder.setBpus(scenario);
    }
    return scenario;
  }


  private ReferenceScenario buildReferenceScenario(int year, boolean bpuLeadInd) {
    ReferenceScenario referenceScenario = new ReferenceScenario();
    referenceScenario.setYear(year);
    referenceScenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    FarmingYear farmingYear = new FarmingYear();
    referenceScenario.setFarmingYear(farmingYear);
    {
      MarginTotal marginTotal = new MarginTotal();
      farmingYear.setMarginTotal(marginTotal);
      marginTotal.setBpuLeadInd(bpuLeadInd);
    }
    return referenceScenario;
  }
  
  @Test
  public void forageFedOutToCattleInCorrectOrder() {
    
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(PROGRAM_YEAR));
    scenario.setIsInCombinedFarmInd(false);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    
    FarmingOperation farmingOperation = new FarmingOperation();

    List<FarmingOperation> farmingOperations = new ArrayList<>();
    farmingOperations.add(farmingOperation);
    farmingYear.setFarmingOperations(farmingOperations);
    
    Benefit benefit = new Benefit();
    farmingYear.setBenefit(benefit);
    benefit.setProgramYearMargin(100.);
    benefit.setStandardBenefit(2600.0);
    benefit.setTotalBenefit(2900.0);

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
    
    
    //----------------- Productive Units ----------------------
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.CATTLE_DESCRIPTION);
      puc.setRollupStructureGroupCode("300");
      puc.setRollupStructureGroupCodeDescription("Bovine");
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get300_2024_BPU());
      puc.setReportedAmount(2.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.FEEDER_CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.FEEDER_CATTLE_DESCRIPTION);
      puc.setRollupStructureGroupCode("300");
      puc.setRollupStructureGroupCodeDescription("Bovine");
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get300_2024_BPU());
      puc.setReportedAmount(5.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.FINISHED_CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.FINISHED_CATTLE_DESCRIPTION);
      puc.setRollupStructureGroupCode("300");
      puc.setRollupStructureGroupCodeDescription("Bovine");
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get300_2024_BPU());
      puc.setReportedAmount(1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.CATTLE_BRED_HEIFERS);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.CATTLE_BRED_HEIFERS_DESCRIPTION);
      puc.setRollupStructureGroupCode("300");
      puc.setRollupStructureGroupCodeDescription("Bovine");
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get300_2024_BPU());
      puc.setReportedAmount(1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(HAY_INVENTORY_ITEM_CODE);
      puc.setStructureGroupCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      puc.setRollupInventoryItemCode("5564");
      puc.setRollupInventoryItemCodeDescription("Hay, Alfalfa");
      puc.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5574_2024_BPU());
      puc.setReportedAmount(1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(SILAGE_INVENTORY_ITEM_CODE);
      puc.setStructureGroupCodeDescription(SILAGE_INVENTORY_ITEM_DESCRIPTION);
      puc.setRollupInventoryItemCode("5584");
      puc.setRollupInventoryItemCodeDescription("Silage");
      puc.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5584_2024_BPU());
      puc.setReportedAmount(7.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
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
    
    farmingOperation.setCropItems(cropItems);
    farmingOperation.setIncomeExpenses(incomeExpenses);
    farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);

    {
      List<ReferenceScenario> referenceScenarios = new ArrayList<>();
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_1, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_2, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_3, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_4, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(PROGRAM_YEAR_MINUS_5, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      scenario.setReferenceScenarios(referenceScenarios);
      UnitTestModelBuilder.setBpus(scenario);
    }


    try {
      runBenefitTest(scenario);
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    assertNotNull(reasonabilityTestResults);
    
    List<ForageConsumer> forageConsumers = reasonabilityTestResults.getForageConsumers();
    assertNotNull(forageConsumers);
    assertEquals(4, forageConsumers.size());
    Iterator<ForageConsumer> forageConsumersIterator = forageConsumers.iterator();
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.CATTLE, forageConsumer.getStructureGroupCode());
      assertEquals(StructureGroupCodes.CATTLE_DESCRIPTION, forageConsumer.getStructureGroupCodeDescription());
      assertEquals("2.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("1.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("3.0", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.FEEDER_CATTLE, forageConsumer.getStructureGroupCode());
      assertEquals(StructureGroupCodes.FEEDER_CATTLE_DESCRIPTION, forageConsumer.getStructureGroupCodeDescription());
      assertEquals("5.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("0.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("2.5", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.FINISHED_CATTLE, forageConsumer.getStructureGroupCode());
      assertEquals(StructureGroupCodes.FINISHED_CATTLE_DESCRIPTION, forageConsumer.getStructureGroupCodeDescription());
      assertEquals("1.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("0.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("0.5", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    {
      ForageConsumer forageConsumer = forageConsumersIterator.next();
      assertEquals(StructureGroupCodes.CATTLE_BRED_HEIFERS, forageConsumer.getStructureGroupCode());
      assertEquals(StructureGroupCodes.CATTLE_BRED_HEIFERS_DESCRIPTION, forageConsumer.getStructureGroupCodeDescription());
      assertEquals("1.0", String.valueOf(forageConsumer.getProductiveUnitCapacity()));
      assertEquals("1.5", String.valueOf(forageConsumer.getQuantityConsumedPerUnit()));
      assertEquals("1.5", String.valueOf(forageConsumer.getQuantityConsumed()));
    }
    
    // the sum of quantity consumed from the above forage consumers
    assertEquals("7.5", String.valueOf(reasonabilityTestResults.getForageConsumerCapacity()));
    
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(3, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      
      // cattle productive units
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("300", puc.getCode());
        assertEquals("Bovine", puc.getDescription());
        assertEquals("9.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("9.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("521.19", String.valueOf(puc.getBpuCalculated()));
        assertEquals("4690.75", String.valueOf(puc.getEstimatedIncome()));
      }
      
      // forage productive units
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5564", puc.getCode());
        assertEquals("Hay, Alfalfa", puc.getDescription());
        assertEquals("1.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertEquals("0.5", String.valueOf(puc.getConsumedProductiveCapacityAmount()));
        assertEquals("0.5", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("83.24", String.valueOf(puc.getBpuCalculated()));
        assertEquals("41.62", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5584", puc.getCode());
        assertEquals("Silage", puc.getDescription());
        assertEquals("7.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertEquals("7.0", String.valueOf(puc.getConsumedProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("83.24", String.valueOf(puc.getBpuCalculated()));
        assertEquals("0.0", String.valueOf(puc.getEstimatedIncome()));
      }
    }

    
    assertEquals("100.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("2600.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("4732.37", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("3312.66", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("3212.66", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("2248.86", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.156", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }

  private void runBenefitTest(Scenario scenario) throws ReasonabilityTestException {
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    revenueRiskTest.test(scenario);
    BenefitRiskAssessment benefitRiskAssessment = new BenefitRiskAssessment();
    benefitRiskAssessment.test(scenario);
  }

}
