/**
 * Copyright (c) 2011,
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
 * @created May 14, 2019
 */
public class TestBenefitRiskAssessment {

  private static Logger logger = LoggerFactory.getLogger(TestBenefitRiskAssessment.class);
  
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

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  @Test
  public void pass5YearsZeroBenefit() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(1243.74);
    scenario.getBenefit().setTotalBenefit(0.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setReportedAmount(0.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setReportedAmount(1.52);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    
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
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
        assertEquals("8340.97", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("8340.97", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("5838.68", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("4594.94", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("3216.46", String.valueOf(result.getBenefitBenchmark()));
    
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
    scenario.getBenefit().setTotalBenefit(249.99);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setReportedAmount(0.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setReportedAmount(1.52);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    
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
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
        assertEquals("8340.97", String.valueOf(puc.getEstimatedIncome()));
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
          ReasonabilityTestResultMessage.createMessageWithParameters(BenefitRiskAssessment.INFO_PASS_UNDER_MINIMUM_BENEFIT),
          infoMessageIterator.next().getMessage());
    }

    assertEquals("1243.74", String.valueOf(result.getProgramYearMargin()));
    assertEquals("249.99", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("8340.97", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("5838.68", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("4594.94", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("3216.46", String.valueOf(result.getBenefitBenchmark()));
    
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
    scenario.getBenefit().setTotalBenefit(3123.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setReportedAmount(0.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setReportedAmount(1.52);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    
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
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
        assertEquals("8340.97", String.valueOf(puc.getEstimatedIncome()));
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
    assertEquals("3123.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("8340.97", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("5838.68", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("4594.94", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("3216.46", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("-0.029", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(true, result.getResult());
  }
  
  @Test
  public void fail5YearsWithNoLag() throws Exception {
    
    Scenario scenario = buildScenario();
    scenario.getBenefit().setProgramYearMargin(100000.0);
    scenario.getBenefit().setTotalBenefit(114685.27);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("26.2", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("26.2", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("2443.37", String.valueOf(puc.getBpuCalculated()));
        assertEquals("64016.35", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("36.7", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("36.7", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
        assertEquals("201390.59", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("265406.94", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("185784.86", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("85784.86", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("60049.4", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.91", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(false, result.getResult());
  }
  
  @Test
  public void pass5YearsWithLag() throws Exception {
    
    Scenario scenario = buildScenario();
    
    scenario.getBenefit().setProgramYearMargin(100000.0);
    scenario.getBenefit().setTotalBenefit(80685.27);
    
    // Set BPU Lead to false for 2017 reference year
    UnitTestModelBuilder.setBpuLeadInd(scenario, 2017, false);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("26.2", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("26.2", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("2680.3", String.valueOf(puc.getBpuCalculated()));
        assertEquals("70223.86", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("36.7", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("36.7", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5866.36", String.valueOf(puc.getBpuCalculated()));
        assertEquals("215295.49", String.valueOf(puc.getEstimatedIncome()));
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
    assertEquals("80685.27", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("285519.35", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("199863.55", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
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
    scenario.getBenefit().setTotalBenefit(114685.27);
    
    UnitTestModelBuilder.removeReferenceYear(scenario, 2013);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("26.2", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("26.2", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("2851.39", String.valueOf(puc.getBpuCalculated()));
        assertEquals("74706.29", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("36.7", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("36.7", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("6154.37", String.valueOf(puc.getBpuCalculated()));
        assertEquals("225865.38", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("300571.67", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("210400.17", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("110400.17", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("77280.12", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("0.484", String.valueOf(result.getVariance()));
    assertEquals("0.2", String.valueOf(result.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(result.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(result.getBenefitRiskPayoutLevel()));
    assertEquals(false, result.getResult());
  }
  
  @Test
  public void pass4YearsWithLag() throws Exception {
    
    Scenario scenario = buildScenario();
    
    scenario.getBenefit().setProgramYearMargin(80000.0);
    scenario.getBenefit().setTotalBenefit(114685.27);
    
    UnitTestModelBuilder.removeReferenceYear(scenario, 2013);
    UnitTestModelBuilder.setBpuLeadInd(scenario, 2017, false);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("26.2", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("26.2", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("3147.55", String.valueOf(puc.getBpuCalculated()));
        assertEquals("82465.68", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("36.7", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("36.7", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("6627.97", String.valueOf(puc.getBpuCalculated()));
        assertEquals("243246.5", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("325712.18", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("227998.53", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("147998.53", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("103598.97", String.valueOf(result.getBenefitBenchmark()));
    
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
    scenario.getBenefit().setTotalBenefit(114685.27);

    UnitTestModelBuilder.removeReferenceYear(scenario, 2013);
    UnitTestModelBuilder.removeReferenceYear(scenario, 2014);
    UnitTestModelBuilder.setBpuLeadInd(scenario, 2016, false);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("26.2", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("26.2", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("4018.89", String.valueOf(puc.getBpuCalculated()));
        assertEquals("105294.92", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("36.7", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("36.7", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("8086.34", String.valueOf(puc.getBpuCalculated()));
        assertEquals("296768.68", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("402063.6", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("281444.52", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("201444.52", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("141011.16", String.valueOf(result.getBenefitBenchmark()));
    
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
    scenario.getBenefit().setTotalBenefit(1000.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setReportedAmount(0.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setReportedAmount(0.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    
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
    scenario.getBenefit().setTotalBenefit(1000.0);
    
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5062");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
      puc.setReportedAmount(-1.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      productiveUnitCapacities.add(puc);
      puc.setInventoryItemCode("5064");
      puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
      puc.setReportedAmount(-2.0);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    }
    scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("-1.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("-1.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("2443.37", String.valueOf(puc.getBpuCalculated()));
        assertEquals("-2443.37", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("-2.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("-2.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
        assertEquals("-10974.96", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("-13418.33", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("-9392.83", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("-10636.57", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("-7445.6", String.valueOf(result.getBenefitBenchmark()));
    
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
    scenario.getBenefit().setTotalBenefit(685.27);
    
    runBenefitTest(scenario);
    
    BenefitRiskAssessmentTestResult result = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(result);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
      assertNotNull(productiveUnits);
      assertEquals(2, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5062", puc.getCode());
        assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
        assertEquals("26.2", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("26.2", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("2443.37", String.valueOf(puc.getBpuCalculated()));
        assertEquals("64016.35", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals("5064", puc.getCode());
        assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
        assertEquals("36.7", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("36.7", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
        assertEquals("201390.59", String.valueOf(puc.getEstimatedIncome()));
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
    
    assertEquals("265406.94", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("185784.86", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("85784.86", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("60049.4", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("-0.989", String.valueOf(result.getVariance()));
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
    double combinedFarmTotalBenefit = 3123.0;
    
    combinedFarmBenefit.setProgramYearMargin(programYearMargin);
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
      scenario.getFarmingYear().getBenefit().setTotalBenefit(MathUtils.roundCurrency(combinedFarmTotalBenefit * combinedFarmPercent));
      
      combinedFarm.getBenefit().setProgramYearMargin(scenario.getFarmingYear().getBenefit().getProgramYearMargin());
      
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        productiveUnitCapacities.add(puc);
        puc.setInventoryItemCode("5062");
        puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
        puc.setReportedAmount(2.0);
        puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
        puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      }
      scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
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
      
      scenario.getFarmingYear().getBenefit().setAppliedBenefitPercent(combinedFarmPercent);
      
      scenario.getFarmingYear().getBenefit().setProgramYearMargin(programYearMargin);
      scenario.getFarmingYear().getBenefit().setTotalBenefit(MathUtils.roundCurrency(combinedFarmTotalBenefit * combinedFarmPercent));
      
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
        productiveUnitCapacities.add(puc);
        puc.setInventoryItemCode("5064");
        puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
        puc.setReportedAmount(1.52);
        puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
        puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      }
      scenario.getFarmingYear().getFarmingOperations().get(0).setProductiveUnitCapacities(productiveUnitCapacities);
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
        assertEquals(2, productiveUnits.size());
        Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
        {
          BenefitRiskProductiveUnit puc = iterator.next();
          assertEquals("5062", puc.getCode());
          assertEquals("Blueberries; High bush (Years 3 to 6 of Production)", puc.getDescription());
          assertEquals("2.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
          assertNull(puc.getConsumedProductiveCapacityAmount());
          assertEquals("2.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
          assertEquals("2443.37", String.valueOf(puc.getBpuCalculated()));
          assertEquals("4886.74", String.valueOf(puc.getEstimatedIncome()));
        }
        {
          BenefitRiskProductiveUnit puc = iterator.next();
          assertEquals("5064", puc.getCode());
          assertEquals("Blueberries; High bush (Years 10+ of Production)", puc.getDescription());
          assertEquals("1.52", String.valueOf(puc.getReportedProductiveCapacityAmount()));
          assertNull(puc.getConsumedProductiveCapacityAmount());
          assertEquals("1.52", String.valueOf(puc.getNetProductiveCapacityAmount()));
          assertEquals("5487.48", String.valueOf(puc.getBpuCalculated()));
          assertEquals("8340.97", String.valueOf(puc.getEstimatedIncome()));
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
      
      assertEquals("13227.71", String.valueOf(result.getBenchmarkMargin()));
      assertEquals("9259.4", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
      assertEquals("8015.66", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
      assertEquals("5610.96", String.valueOf(result.getBenefitBenchmarkBeforeCombinedFarmPercent()));
      assertEquals("0.785", String.valueOf(result.getCombinedFarmPercent()));
      assertEquals("4404.6", String.valueOf(result.getBenefitBenchmark()));
      
      assertEquals("-0.443", String.valueOf(result.getVariance()));
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
      
      assertEquals("13227.71", String.valueOf(result.getBenchmarkMargin()));
      assertEquals("9259.4", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
      assertEquals("8015.66", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
      assertEquals("5610.96", String.valueOf(result.getBenefitBenchmarkBeforeCombinedFarmPercent()));
      assertEquals("0.215", String.valueOf(result.getCombinedFarmPercent()));
      assertEquals("1206.36", String.valueOf(result.getBenefitBenchmark()));
      
      assertEquals("-0.443", String.valueOf(result.getVariance()));
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
      scenario.setYear(2018);
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
        farmingYear.setFarmingOperations(farmingOperations);
        List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
        {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          productiveUnitCapacities.add(puc);
          puc.setInventoryItemCode("5062");
          puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
          puc.setReportedAmount(26.2);
          puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2018_BPU());
          puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
        }
        {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          productiveUnitCapacities.add(puc);
          puc.setInventoryItemCode("5064");
          puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
          puc.setReportedAmount(36.7);
          puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2018_BPU());
          puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
        }
        farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
      }
    }
    {
      List<ReferenceScenario> referenceScenarios = new ArrayList<>();
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2017, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2016, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2015, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2014, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2013, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      scenario.setReferenceScenarios(referenceScenarios);
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
    scenario.setYear(new Integer(2018));
    scenario.setIsInCombinedFarmInd(false);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    FarmingYear farmingYear = new FarmingYear();
    
    FarmingOperation farmingOperation = new FarmingOperation();

    List<FarmingOperation> farmingOperations = new ArrayList<>();
    farmingOperations.add(farmingOperation);
    farmingYear.setFarmingOperations(farmingOperations);
    scenario.setFarmingYear(farmingYear);
    
    Benefit benefit = new Benefit();
    farmingYear.setBenefit(benefit);
    benefit.setProgramYearMargin(100.);
    benefit.setTotalBenefit(1000.0);

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
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.CATTLE_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get104_2018_BPU());
      puc.setReportedAmount(2.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.FEEDER_CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.FEEDER_CATTLE_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get105_2018_BPU());
      puc.setReportedAmount(5.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.FINISHED_CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.FINISHED_CATTLE_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get106_2018_BPU());
      puc.setReportedAmount(1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.CATTLE_BRED_HEIFERS);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.CATTLE_BRED_HEIFERS_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get171_2018_BPU());
      puc.setReportedAmount(1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(HAY_INVENTORY_ITEM_CODE);
      puc.setStructureGroupCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5574_2018_BPU());
      puc.setReportedAmount(1.0);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(SILAGE_INVENTORY_ITEM_CODE);
      puc.setStructureGroupCodeDescription(SILAGE_INVENTORY_ITEM_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5584_2018_BPU());
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
        ReferenceScenario referenceScenario = buildReferenceScenario(2017, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2016, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2015, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2014, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      {
        ReferenceScenario referenceScenario = buildReferenceScenario(2013, true); // year, bpuLeadInd
        referenceScenarios.add(referenceScenario);
      }
      scenario.setReferenceScenarios(referenceScenarios);
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
      assertEquals(6, productiveUnits.size());
      Iterator<BenefitRiskProductiveUnit> iterator = productiveUnits.iterator();
      
      // cattle productive units
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals(StructureGroupCodes.CATTLE, puc.getCode());
        assertEquals(StructureGroupCodes.CATTLE_DESCRIPTION, puc.getDescription());
        assertEquals("2.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("2.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("616.86", String.valueOf(puc.getBpuCalculated()));
        assertEquals("1233.72", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals(StructureGroupCodes.FEEDER_CATTLE, puc.getCode());
        assertEquals(StructureGroupCodes.FEEDER_CATTLE_DESCRIPTION, puc.getDescription());
        assertEquals("5.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("5.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("168.12", String.valueOf(puc.getBpuCalculated()));
        assertEquals("840.6", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals(StructureGroupCodes.FINISHED_CATTLE, puc.getCode());
        assertEquals(StructureGroupCodes.FINISHED_CATTLE_DESCRIPTION, puc.getDescription());
        assertEquals("1.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("76.78", String.valueOf(puc.getBpuCalculated()));
        assertEquals("76.78", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals(StructureGroupCodes.CATTLE_BRED_HEIFERS, puc.getCode());
        assertEquals(StructureGroupCodes.CATTLE_BRED_HEIFERS_DESCRIPTION, puc.getDescription());
        assertEquals("1.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertNull(puc.getConsumedProductiveCapacityAmount());
        assertEquals("1.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("308.28", String.valueOf(puc.getBpuCalculated()));
        assertEquals("308.28", String.valueOf(puc.getEstimatedIncome()));
      }
      
      // forage productive units
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals(HAY_INVENTORY_ITEM_CODE, puc.getCode());
        assertEquals(HAY_INVENTORY_ITEM_DESCRIPTION, puc.getDescription());
        assertEquals("1.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertEquals("0.5", String.valueOf(puc.getConsumedProductiveCapacityAmount()));
        assertEquals("0.5", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("83.24", String.valueOf(puc.getBpuCalculated()));
        assertEquals("41.62", String.valueOf(puc.getEstimatedIncome()));
      }
      {
        BenefitRiskProductiveUnit puc = iterator.next();
        assertEquals(SILAGE_INVENTORY_ITEM_CODE, puc.getCode());
        assertEquals(SILAGE_INVENTORY_ITEM_DESCRIPTION, puc.getDescription());
        assertEquals("7.0", String.valueOf(puc.getReportedProductiveCapacityAmount()));
        assertEquals("7.0", String.valueOf(puc.getConsumedProductiveCapacityAmount()));
        assertEquals("0.0", String.valueOf(puc.getNetProductiveCapacityAmount()));
        assertEquals("83.24", String.valueOf(puc.getBpuCalculated()));
        assertEquals("0.0", String.valueOf(puc.getEstimatedIncome()));
      }
    }

    
    assertEquals("100.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("1000.0", String.valueOf(result.getTotalBenefit()));
    
    assertEquals("2501.0", String.valueOf(result.getBenchmarkMargin()));
    assertEquals("1750.7", String.valueOf(result.getBenefitBenchmarkLessDeductible()));
    assertEquals("1650.7", String.valueOf(result.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("1155.49", String.valueOf(result.getBenefitBenchmark()));
    
    assertEquals("-0.135", String.valueOf(result.getVariance()));
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
