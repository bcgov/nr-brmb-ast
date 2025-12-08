/**
 * Copyright (c) 2020,
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

import ca.bc.gov.srm.farm.domain.Benefit;
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
import ca.bc.gov.srm.farm.domain.reasonability.MarginTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.domain.reasonability.revenue.ForageConsumer;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.reasonability.revenue.RevenueRiskTest;
import ca.bc.gov.srm.farm.util.TestUtils;
import ca.bc.gov.srm.farm.util.UnitTestModelBuilder;

/**
 * @author awilkinson
 * @created May 14, 2019
 */
public class TestMarginTest {
  
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
  public void failWith5Years() throws Exception {
    Scenario scenario = buildScenario();
    
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    runMarginTest(scenario);
    
    MarginTestResult result = scenario.getReasonabilityTestResults().getMarginTest();
    assertNotNull(result);
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("595356.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("429631.99", String.valueOf(result.getAdjustedReferenceMargin()));
    assertEquals("0.386", String.valueOf(result.getAdjustedReferenceMarginVariance()));
    assertEquals("0.2", String.valueOf(result.getAdjustedReferenceMarginVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getWithinLimitOfReferenceMargin());
    assertEquals("113409.47", String.valueOf(result.getIndustryAverage()));
    assertEquals("4.25", String.valueOf(result.getIndustryVariance()));
    assertEquals("0.2", String.valueOf(result.getIndustryVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getWithinLimitOfIndustryAverage());
    assertEquals(Boolean.FALSE, result.getResult());
  }
  
  @Test
  public void failWith4YearsWithLag() throws Exception {
    Scenario scenario = buildScenario();
    
    UnitTestModelBuilder.removeReferenceYear(scenario, 2013);
    
    // Set BPU Lead to false for 2017 reference year
    UnitTestModelBuilder.setBpuLeadInd(scenario, 2017, false);
    
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    runMarginTest(scenario);
    
    MarginTestResult result = scenario.getReasonabilityTestResults().getMarginTest();
    assertNotNull(result);
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("595356.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("429631.99", String.valueOf(result.getAdjustedReferenceMargin()));
    assertEquals("0.386", String.valueOf(result.getAdjustedReferenceMarginVariance()));
    assertEquals("0.2", String.valueOf(result.getAdjustedReferenceMarginVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getWithinLimitOfReferenceMargin());
    assertEquals("160813.59", String.valueOf(result.getIndustryAverage()));
    assertEquals("2.702", String.valueOf(result.getIndustryVariance()));
    assertEquals("0.2", String.valueOf(result.getIndustryVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getWithinLimitOfIndustryAverage());
    assertEquals(Boolean.FALSE, result.getResult());
  }

  private Scenario buildScenario() {
    Scenario scenario = new Scenario();
    scenario.setIsInCombinedFarmInd(false);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    {
      scenario.setYear(2018);
      FarmingYear farmingYear = new FarmingYear();
      scenario.setFarmingYear(farmingYear);
      {
        Benefit benefit = new Benefit();
        farmingYear.setBenefit(benefit);
        benefit.setProgramYearMargin(595356.0);
        benefit.setAdjustedReferenceMargin(429631.99);
        benefit.setTotalBenefit(0.0);
      }
      List<FarmingOperation> farmingOperations = new ArrayList<>();
      FarmingOperation farmingOperation = new FarmingOperation();
      farmingOperations.add(farmingOperation);
      List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
      {
        {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          productiveUnitCapacities.add(puc);
          puc.setInventoryItemCode("5062");
          puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 3 to 6 of Production)");
          puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
          puc.setAdjAmount(26.2);
          puc.setBasePricePerUnit(UnitTestModelBuilder.get5062_2017_BPU());
        }
        {
          ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
          productiveUnitCapacities.add(puc);
          puc.setInventoryItemCode("5064");
          puc.setInventoryItemCodeDescription("Blueberries; High bush (Years 10+ of Production)");
          puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
          puc.setAdjAmount(36.7);
          puc.setBasePricePerUnit(UnitTestModelBuilder.get5064_2017_BPU());
        }
      }
      farmingYear.setFarmingOperations(farmingOperations);
      farmingOperation.setProductiveUnitCapacities(productiveUnitCapacities);
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
    benefit.setAdjustedReferenceMargin(90.0);

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
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get104_2018_BPU());
      puc.setReportedAmount(2.0);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.FEEDER_CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.FEEDER_CATTLE_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get105_2018_BPU());
      puc.setReportedAmount(5.0);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.FINISHED_CATTLE);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.FINISHED_CATTLE_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get106_2018_BPU());
      puc.setReportedAmount(1.0);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(StructureGroupCodes.CATTLE_BRED_HEIFERS);
      puc.setStructureGroupCodeDescription(StructureGroupCodes.CATTLE_BRED_HEIFERS_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.CATTLE);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get171_2018_BPU());
      puc.setReportedAmount(1.0);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(HAY_INVENTORY_ITEM_CODE);
      puc.setStructureGroupCodeDescription(HAY_INVENTORY_ITEM_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5574_2018_BPU());
      puc.setReportedAmount(1.0);
      productiveUnitCapacities.add(puc);
    }
    {
      ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
      puc.setStructureGroupCode(SILAGE_INVENTORY_ITEM_CODE);
      puc.setStructureGroupCodeDescription(SILAGE_INVENTORY_ITEM_DESCRIPTION);
      puc.setCommodityTypeCode(CommodityTypeCodes.FORAGE);
      puc.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
      puc.setBasePricePerUnit(UnitTestModelBuilder.get5584_2018_BPU());
      puc.setReportedAmount(7.0);
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
      runMarginTest(scenario);
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
    
    
    BenefitRiskAssessmentTestResult benefitRiskResult = scenario.getReasonabilityTestResults().getBenefitRisk();
    assertNotNull(benefitRiskResult);
    
    {
      List<BenefitRiskProductiveUnit> productiveUnits = benefitRiskResult.getBenefitRiskProductiveUnits();
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

    
    assertEquals("100.0", String.valueOf(benefitRiskResult.getProgramYearMargin()));
    assertEquals("1000.0", String.valueOf(benefitRiskResult.getTotalBenefit()));
    
    assertEquals("2501.0", String.valueOf(benefitRiskResult.getBenchmarkMargin()));
    assertEquals("1750.7", String.valueOf(benefitRiskResult.getBenefitBenchmarkLessDeductible()));
    assertEquals("1650.7", String.valueOf(benefitRiskResult.getBenefitBenchmarkLessProgramYearMargin()));
    assertEquals("1155.49", String.valueOf(benefitRiskResult.getBenefitBenchmark()));
    
    assertEquals("-0.135", String.valueOf(benefitRiskResult.getVariance()));
    assertEquals("0.2", String.valueOf(benefitRiskResult.getVarianceLimit()));
    assertEquals("0.3", String.valueOf(benefitRiskResult.getBenefitRiskDeductible()));
    assertEquals("0.7", String.valueOf(benefitRiskResult.getBenefitRiskPayoutLevel()));
    assertEquals(true, benefitRiskResult.getResult());
    
    
    // Margin Test results
    MarginTestResult result = scenario.getReasonabilityTestResults().getMarginTest();
    assertNotNull(result);
    
    List<ReasonabilityTestResultMessage> errorMessages = result.getErrorMessages();
    List<ReasonabilityTestResultMessage> warningMessages = result.getWarningMessages();
    List<ReasonabilityTestResultMessage> infoMessages = result.getInfoMessages();
    assertNotNull(errorMessages);
    assertNotNull(warningMessages);
    assertNotNull(infoMessages);
    assertEquals(0, errorMessages.size());
    assertEquals(0, warningMessages.size());
    assertEquals(0, infoMessages.size());
    
    assertEquals("100.0", String.valueOf(result.getProgramYearMargin()));
    assertEquals("90.0", String.valueOf(result.getAdjustedReferenceMargin()));
    assertEquals("0.111", String.valueOf(result.getAdjustedReferenceMarginVariance()));
    assertEquals("0.2", String.valueOf(result.getAdjustedReferenceMarginVarianceLimit()));
    assertEquals(Boolean.TRUE, result.getWithinLimitOfReferenceMargin());
    assertEquals("2501.0", String.valueOf(result.getIndustryAverage()));
    assertEquals("-0.96", String.valueOf(result.getIndustryVariance()));
    assertEquals("0.2", String.valueOf(result.getIndustryVarianceLimit()));
    assertEquals(Boolean.FALSE, result.getWithinLimitOfIndustryAverage());
    assertEquals(Boolean.FALSE, result.getResult());
  }


  private void runMarginTest(Scenario scenario) throws ReasonabilityTestException {
    RevenueRiskTest revenueRiskTest = new RevenueRiskTest();
    revenueRiskTest.test(scenario);

    BenefitRiskAssessment benefitRiskAssessment = new BenefitRiskAssessment();
    benefitRiskAssessment.test(scenario);

    MarginTest marginTest = new MarginTest();
    marginTest.test(scenario);
  }
}
