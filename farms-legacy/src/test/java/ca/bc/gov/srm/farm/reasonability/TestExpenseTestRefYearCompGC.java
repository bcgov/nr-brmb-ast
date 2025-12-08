package ca.bc.gov.srm.farm.reasonability;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ExpenseTestRefYearCompGCResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author psidhu
 * @created May 14, 2019
 */
public class TestExpenseTestRefYearCompGC {
  
  private static Logger logger = LoggerFactory.getLogger(TestExpenseTestRefYearCompGC.class);
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    ReasonabilityConfiguration.getInstance(false);
  }
  
  private Scenario buildScenario() {
    Scenario scenario = new Scenario();
    scenario.setYear(new Integer(2019));
    scenario.setIsInCombinedFarmInd(true);
    scenario.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    scenario.setReasonabilityTestResults(new ReasonabilityTestResults());
    
    return scenario;
  }
  
  @Test
  public void passWithExpensesForAllYears() {
    Scenario scenario = buildScenario();
   
    MarginTotal marginTotal = new MarginTotal();
    marginTotal.setExpenseAccrualAdjs(250000.0);
    FarmingYear farmYear = new FarmingYear();
    farmYear.setMarginTotal(marginTotal);
    scenario.setFarmingYear(farmYear);
    
    ReferenceScenario refScen1 = new ReferenceScenario();
    refScen1.setYear(new Integer(2018));
    refScen1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal1 = new MarginTotal();
    marginTotal1.setExpensesAfterStructuralChange(100000.0);
    FarmingYear farmYear1 = new FarmingYear();
    farmYear1.setMarginTotal(marginTotal1);
    refScen1.setFarmingYear(farmYear1);
    
    ReferenceScenario refScen2 = new ReferenceScenario();
    refScen2.setYear(new Integer(2017));
    refScen2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal2 = new MarginTotal();
    marginTotal2.setExpensesAfterStructuralChange(200000.0);
    FarmingYear farmYear2 = new FarmingYear();
    farmYear2.setMarginTotal(marginTotal2);
    refScen2.setFarmingYear(farmYear2);
    
    ReferenceScenario refScen3 = new ReferenceScenario();
    refScen3.setYear(new Integer(2016));
    refScen3.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal3 = new MarginTotal();
    marginTotal3.setExpensesAfterStructuralChange(300000.0);
    FarmingYear farmYear3 = new FarmingYear();
    farmYear3.setMarginTotal(marginTotal3);
    refScen3.setFarmingYear(farmYear3);
    
    ReferenceScenario refScen4 = new ReferenceScenario();
    refScen4.setYear(new Integer(2015));
    refScen4.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal4 = new MarginTotal();
    marginTotal4.setExpensesAfterStructuralChange(400000.0);
    FarmingYear farmYear4 = new FarmingYear();
    farmYear4.setMarginTotal(marginTotal4);
    refScen4.setFarmingYear(farmYear4);
    
    ReferenceScenario refScen5 = new ReferenceScenario();
    refScen5.setYear(new Integer(2014));
    refScen5.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal5 = new MarginTotal();
    marginTotal5.setExpensesAfterStructuralChange(500000.0);
    FarmingYear farmYear5 = new FarmingYear();
    farmYear5.setMarginTotal(marginTotal5);
    refScen5.setFarmingYear(farmYear5);
    
    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    scenario.setReferenceScenarios(referenceScenarios);
    referenceScenarios.add(refScen1);
    referenceScenarios.add(refScen2);
    referenceScenarios.add(refScen3);
    referenceScenarios.add(refScen4);
    referenceScenarios.add(refScen5);
    
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();

    try {
      expenseTestRefYearCompGC.test(scenario);  
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    assertNotNull(scenario.getReasonabilityTestResults());
    ExpenseTestRefYearCompGCResult testResult = scenario.getReasonabilityTestResults().getExpenseTestRefYearCompGC();
    assertNotNull(testResult);
    assertEquals("250000.0", String.valueOf(testResult.getProgramYearAcrAdjExpense()));
    assertEquals("100000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus1()));
    assertEquals("200000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus2()));
    assertEquals("300000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus3()));
    assertEquals("400000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus4()));
    assertEquals("500000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus5()));
    assertEquals("300000.0", String.valueOf(testResult.getExpenseStructrualChangeAverage()));
    assertEquals("-0.167", String.valueOf(testResult.getVariance()));
    assertEquals("0.2", String.valueOf(testResult.getVarianceLimit()));
    assertEquals(true, testResult.getResult());
  }
  
  @Test
  public void passWithZeroesForYearMinus4AndMinus5() {
    Scenario scenario = buildScenario();
   
    MarginTotal marginTotal = new MarginTotal();
    marginTotal.setExpenseAccrualAdjs(200000.0);
    FarmingYear farmYear = new FarmingYear();
    farmYear.setMarginTotal(marginTotal);
    scenario.setFarmingYear(farmYear);
    
    ReferenceScenario refScen1 = new ReferenceScenario();
    refScen1.setYear(new Integer(2018));
    refScen1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal1 = new MarginTotal();
    marginTotal1.setExpensesAfterStructuralChange(100000.0);
    FarmingYear farmYear1 = new FarmingYear();
    farmYear1.setMarginTotal(marginTotal1);
    refScen1.setFarmingYear(farmYear1);
    
    ReferenceScenario refScen2 = new ReferenceScenario();
    refScen2.setYear(new Integer(2017));
    refScen2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal2 = new MarginTotal();
    marginTotal2.setExpensesAfterStructuralChange(200000.0);
    FarmingYear farmYear2 = new FarmingYear();
    farmYear2.setMarginTotal(marginTotal2);
    refScen2.setFarmingYear(farmYear2);
    
    ReferenceScenario refScen3 = new ReferenceScenario();
    refScen3.setYear(new Integer(2016));
    refScen3.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal3 = new MarginTotal();
    marginTotal3.setExpensesAfterStructuralChange(300000.0);
    FarmingYear farmYear3 = new FarmingYear();
    farmYear3.setMarginTotal(marginTotal3);
    refScen3.setFarmingYear(farmYear3);
    
    ReferenceScenario refScen4 = new ReferenceScenario();
    refScen4.setYear(new Integer(2015));
    refScen4.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal4 = new MarginTotal();
    marginTotal4.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear4 = new FarmingYear();
    farmYear4.setMarginTotal(marginTotal4);
    refScen4.setFarmingYear(farmYear4);
    
    ReferenceScenario refScen5 = new ReferenceScenario();
    refScen5.setYear(new Integer(2014));
    refScen5.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal5 = new MarginTotal();
    marginTotal5.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear5 = new FarmingYear();
    farmYear5.setMarginTotal(marginTotal5);
    refScen5.setFarmingYear(farmYear5);
    
    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    scenario.setReferenceScenarios(referenceScenarios);
    referenceScenarios.add(refScen1);
    referenceScenarios.add(refScen2);
    referenceScenarios.add(refScen3);
    referenceScenarios.add(refScen4);
    referenceScenarios.add(refScen5);
    
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();

    try {
      expenseTestRefYearCompGC.test(scenario);  
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    assertNotNull(scenario.getReasonabilityTestResults());
    ExpenseTestRefYearCompGCResult testResult = scenario.getReasonabilityTestResults().getExpenseTestRefYearCompGC();
    assertNotNull(testResult);
    assertEquals("200000.0", String.valueOf(testResult.getProgramYearAcrAdjExpense()));
    assertEquals("100000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus1()));
    assertEquals("200000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus2()));
    assertEquals("300000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus3()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus4()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus5()));
    assertEquals("200000.0", String.valueOf(testResult.getExpenseStructrualChangeAverage()));
    assertEquals("0.0", String.valueOf(testResult.getVariance()));
    assertEquals("0.2", String.valueOf(testResult.getVarianceLimit()));
    assertEquals(true, testResult.getResult());
  }
  
  @Test
  public void failVariance() {
    Scenario scenario = buildScenario();
    
    MarginTotal marginTotal = new MarginTotal();
    marginTotal.setExpenseAccrualAdjs(100000.0);
    FarmingYear farmYear = new FarmingYear();
    farmYear.setMarginTotal(marginTotal);
    scenario.setFarmingYear(farmYear);
    
    ReferenceScenario refScen1 = new ReferenceScenario();
    refScen1.setYear(new Integer(2018));
    refScen1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal1 = new MarginTotal();
    marginTotal1.setExpensesAfterStructuralChange(100000.0);
    FarmingYear farmYear1 = new FarmingYear();
    farmYear1.setMarginTotal(marginTotal1);
    refScen1.setFarmingYear(farmYear1);
    
    ReferenceScenario refScen2 = new ReferenceScenario();
    refScen2.setYear(new Integer(2017));
    refScen2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal2 = new MarginTotal();
    marginTotal2.setExpensesAfterStructuralChange(200000.0);
    FarmingYear farmYear2 = new FarmingYear();
    farmYear2.setMarginTotal(marginTotal2);
    refScen2.setFarmingYear(farmYear2);
    
    ReferenceScenario refScen3 = new ReferenceScenario();
    refScen3.setYear(new Integer(2016));
    refScen3.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal3 = new MarginTotal();
    marginTotal3.setExpensesAfterStructuralChange(300000.0);
    FarmingYear farmYear3 = new FarmingYear();
    farmYear3.setMarginTotal(marginTotal3);
    refScen3.setFarmingYear(farmYear3);
    
    ReferenceScenario refScen4 = new ReferenceScenario();
    refScen4.setYear(new Integer(2015));
    refScen4.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal4 = new MarginTotal();
    marginTotal4.setExpensesAfterStructuralChange(400000.0);
    FarmingYear farmYear4 = new FarmingYear();
    farmYear4.setMarginTotal(marginTotal4);
    refScen4.setFarmingYear(farmYear4);
    
    ReferenceScenario refScen5 = new ReferenceScenario();
    refScen5.setYear(new Integer(2014));
    refScen5.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal5 = new MarginTotal();
    marginTotal5.setExpensesAfterStructuralChange(500000.0);
    FarmingYear farmYear5 = new FarmingYear();
    farmYear5.setMarginTotal(marginTotal5);
    refScen5.setFarmingYear(farmYear5);
    
    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    scenario.setReferenceScenarios(referenceScenarios);
    referenceScenarios.add(refScen1);
    referenceScenarios.add(refScen2);
    referenceScenarios.add(refScen3);
    referenceScenarios.add(refScen4);
    referenceScenarios.add(refScen5);
    
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();

    try {
      expenseTestRefYearCompGC.test(scenario);  
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }

    assertNotNull(scenario.getReasonabilityTestResults());
    ExpenseTestRefYearCompGCResult testResult = scenario.getReasonabilityTestResults().getExpenseTestRefYearCompGC();
    assertNotNull(testResult);
    assertEquals("100000.0", String.valueOf(testResult.getProgramYearAcrAdjExpense()));
    assertEquals("100000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus1()));
    assertEquals("200000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus2()));
    assertEquals("300000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus3()));
    assertEquals("400000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus4()));
    assertEquals("500000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus5()));
    assertEquals("300000.0", String.valueOf(testResult.getExpenseStructrualChangeAverage()));
    assertEquals("-0.667", String.valueOf(testResult.getVariance()));
    assertEquals("0.2", String.valueOf(testResult.getVarianceLimit()));
    assertEquals(false, testResult.getResult());    
  }
  
  @Test
  public void noRefScenarios() {
    Scenario scenario = buildScenario();
    
    MarginTotal marginTotal = new MarginTotal();
    marginTotal.setExpenseAccrualAdjs(100000.0);
    FarmingYear farmYear = new FarmingYear();
    farmYear.setMarginTotal(marginTotal);
    scenario.setFarmingYear(farmYear);    
    scenario.setReferenceScenarios(new ArrayList<ReferenceScenario>());
    
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();

    try {
      expenseTestRefYearCompGC.test(scenario);  
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertNotNull(scenario.getReasonabilityTestResults());
    ExpenseTestRefYearCompGCResult testResult = scenario.getReasonabilityTestResults().getExpenseTestRefYearCompGC();
    assertNotNull(testResult);
    assertEquals("100000.0", String.valueOf(testResult.getProgramYearAcrAdjExpense()));
    assertNull(testResult.getExpenseStructuralChangeYearMinus1());
    assertNull(testResult.getExpenseStructuralChangeYearMinus2());
    assertNull(testResult.getExpenseStructuralChangeYearMinus3());
    assertNull(testResult.getExpenseStructuralChangeYearMinus4());
    assertNull(testResult.getExpenseStructuralChangeYearMinus5());
    assertNull(testResult.getExpenseStructrualChangeAverage());
    assertNull(testResult.getVariance());
    assertEquals("0.2", String.valueOf(testResult.getVarianceLimit()));
    assertEquals(false, testResult.getResult());      
  }
  
  @Test
  public void failWithZeroesForAllReferenceYears() {
    Scenario scenario = buildScenario();
    
    MarginTotal marginTotal = new MarginTotal();
    marginTotal.setExpenseAccrualAdjs(100000.0);
    FarmingYear farmYear = new FarmingYear();
    farmYear.setMarginTotal(marginTotal);
    scenario.setFarmingYear(farmYear);
    
    ReferenceScenario refScen1 = new ReferenceScenario();
    refScen1.setYear(new Integer(2018));
    refScen1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal1 = new MarginTotal();
    marginTotal1.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear1 = new FarmingYear();
    farmYear1.setMarginTotal(marginTotal1);
    refScen1.setFarmingYear(farmYear1);
    
    ReferenceScenario refScen2 = new ReferenceScenario();
    refScen2.setYear(new Integer(2017));
    refScen2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal2 = new MarginTotal();
    marginTotal2.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear2 = new FarmingYear();
    farmYear2.setMarginTotal(marginTotal2);
    refScen2.setFarmingYear(farmYear2);
    
    ReferenceScenario refScen3 = new ReferenceScenario();
    refScen3.setYear(new Integer(2016));
    refScen3.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal3 = new MarginTotal();
    marginTotal3.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear3 = new FarmingYear();
    farmYear3.setMarginTotal(marginTotal3);
    refScen3.setFarmingYear(farmYear3);
    
    ReferenceScenario refScen4 = new ReferenceScenario();
    refScen4.setYear(new Integer(2015));
    refScen4.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal4 = new MarginTotal();
    marginTotal4.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear4 = new FarmingYear();
    farmYear4.setMarginTotal(marginTotal4);
    refScen4.setFarmingYear(farmYear4);
    
    ReferenceScenario refScen5 = new ReferenceScenario();
    refScen5.setYear(new Integer(2014));
    refScen5.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal5 = new MarginTotal();
    marginTotal5.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear5 = new FarmingYear();
    farmYear5.setMarginTotal(marginTotal5);
    refScen5.setFarmingYear(farmYear5);
    
    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    scenario.setReferenceScenarios(referenceScenarios);
    referenceScenarios.add(refScen1);
    referenceScenarios.add(refScen2);
    referenceScenarios.add(refScen3);
    referenceScenarios.add(refScen4);
    referenceScenarios.add(refScen5);
    
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();

    try {
      expenseTestRefYearCompGC.test(scenario);  
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertNotNull(scenario.getReasonabilityTestResults());
    ExpenseTestRefYearCompGCResult testResult = scenario.getReasonabilityTestResults().getExpenseTestRefYearCompGC();
    assertNotNull(testResult);
    assertEquals("100000.0", String.valueOf(testResult.getProgramYearAcrAdjExpense()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus1()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus2()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus3()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus4()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus5()));
    assertNull(testResult.getExpenseStructrualChangeAverage());
    assertNull(testResult.getVariance());
    assertEquals("0.2", String.valueOf(testResult.getVarianceLimit()));
    
    assertNotNull(testResult.getMessages());
    assertNotNull(testResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, testResult.getErrorMessages());
    assertEquals(1, testResult.getErrorMessages().size());
    
    {
      Iterator<ReasonabilityTestResultMessage> iterator = testResult.getErrorMessages().iterator();
      {
        ReasonabilityTestResultMessage message = iterator.next();
        assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ExpenseTestRefYearCompGC.ERROR_MISSING_REFERENCE_YEAR_EXPENSES),
            message.getMessage());
      }
      
      assertEquals(false, testResult.getResult());
    }
  }
  
  @Test
  public void failWithZeroForOneOfThreeMostRecentRefYears() {
    Scenario scenario = buildScenario();
    
    MarginTotal marginTotal = new MarginTotal();
    marginTotal.setExpenseAccrualAdjs(100000.0);
    FarmingYear farmYear = new FarmingYear();
    farmYear.setMarginTotal(marginTotal);
    scenario.setFarmingYear(farmYear);
    
    ReferenceScenario refScen1 = new ReferenceScenario();
    refScen1.setYear(new Integer(2018));
    refScen1.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal1 = new MarginTotal();
    marginTotal1.setExpensesAfterStructuralChange(100000.0);
    FarmingYear farmYear1 = new FarmingYear();
    farmYear1.setMarginTotal(marginTotal1);
    refScen1.setFarmingYear(farmYear1);
    
    ReferenceScenario refScen2 = new ReferenceScenario();
    refScen2.setYear(new Integer(2017));
    refScen2.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal2 = new MarginTotal();
    marginTotal2.setExpensesAfterStructuralChange(200000.0);
    FarmingYear farmYear2 = new FarmingYear();
    farmYear2.setMarginTotal(marginTotal2);
    refScen2.setFarmingYear(farmYear2);
    
    ReferenceScenario refScen3 = new ReferenceScenario();
    refScen3.setYear(new Integer(2016));
    refScen3.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal3 = new MarginTotal();
    marginTotal3.setExpensesAfterStructuralChange(0.0);
    FarmingYear farmYear3 = new FarmingYear();
    farmYear3.setMarginTotal(marginTotal3);
    refScen3.setFarmingYear(farmYear3);
    
    ReferenceScenario refScen4 = new ReferenceScenario();
    refScen4.setYear(new Integer(2015));
    refScen4.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal4 = new MarginTotal();
    marginTotal4.setExpensesAfterStructuralChange(400000.0);
    FarmingYear farmYear4 = new FarmingYear();
    farmYear4.setMarginTotal(marginTotal4);
    refScen4.setFarmingYear(farmYear4);
    
    ReferenceScenario refScen5 = new ReferenceScenario();
    refScen5.setYear(new Integer(2014));
    refScen5.setParticipantDataSrcCode(ParticipantDataSrcCodes.CRA);
    MarginTotal marginTotal5 = new MarginTotal();
    marginTotal5.setExpensesAfterStructuralChange(500000.0);
    FarmingYear farmYear5 = new FarmingYear();
    farmYear5.setMarginTotal(marginTotal5);
    refScen5.setFarmingYear(farmYear5);
    
    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    scenario.setReferenceScenarios(referenceScenarios);
    referenceScenarios.add(refScen1);
    referenceScenarios.add(refScen2);
    referenceScenarios.add(refScen3);
    referenceScenarios.add(refScen4);
    referenceScenarios.add(refScen5);
    
    ExpenseTestRefYearCompGC expenseTestRefYearCompGC = new ExpenseTestRefYearCompGC();

    try {
      expenseTestRefYearCompGC.test(scenario);  
    } catch (ReasonabilityTestException e) {
      e.printStackTrace();
      fail();
    }
    
    assertNotNull(scenario.getReasonabilityTestResults());
    ExpenseTestRefYearCompGCResult testResult = scenario.getReasonabilityTestResults().getExpenseTestRefYearCompGC();
    assertNotNull(testResult);
    assertEquals("100000.0", String.valueOf(testResult.getProgramYearAcrAdjExpense()));
    assertEquals("100000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus1()));
    assertEquals("200000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus2()));
    assertEquals("0.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus3()));
    assertEquals("400000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus4()));
    assertEquals("500000.0", String.valueOf(testResult.getExpenseStructuralChangeYearMinus5()));
    assertNull(testResult.getExpenseStructrualChangeAverage());
    assertNull(testResult.getVariance());
    assertEquals("0.2", String.valueOf(testResult.getVarianceLimit()));
    
    assertNotNull(testResult.getMessages());
    assertNotNull(testResult.getErrorMessages());
    ReasonabilityUnitTestUtils.logMessages(logger, testResult.getErrorMessages());
    assertEquals(1, testResult.getErrorMessages().size());
    
    {
      Iterator<ReasonabilityTestResultMessage> iterator = testResult.getErrorMessages().iterator();
      {
        ReasonabilityTestResultMessage message = iterator.next();
        assertEquals(ReasonabilityTestResultMessage.createMessageWithParameters(ExpenseTestRefYearCompGC.ERROR_MISSING_REFERENCE_YEAR_EXPENSES),
            message.getMessage());
      }
      
      assertEquals(false, testResult.getResult());
    }
  }
}
