/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.fifo;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.domain.fifo.FifoCalculationItem;
import ca.bc.gov.srm.farm.domain.fifo.FifoItemResult;
import ca.bc.gov.srm.farm.domain.fifo.FifoStatus;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ConfigurationService;
import ca.bc.gov.srm.farm.service.FifoService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 */
public class FifoTest {

  private static Logger logger = LoggerFactory.getLogger(FifoTest.class);

  private static CalculatorService calculatorService;
  private static FifoService fifoService;
  private static ConfigurationService configService;
  private static CalculatorDAO calculatorDao = new CalculatorDAO();
  
  private static Connection conn;
  private static ReadDAO readDAO;
  
  private String user = this.getClass().getSimpleName();

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    
    calculatorService = ServiceFactory.getCalculatorService();
    fifoService = ServiceFactory.getFifoService();
    configService = ServiceFactory.getConfigurationService();

    configService.loadConfigurationParameters();
    configService.loadYearConfigurationParameters();
    
    conn = TestUtils.openConnection();
    readDAO = new ReadDAO(conn);
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }

  @Disabled 
  @Test
  public void runFifoCalculations() {
    
    try {
      // The importVersionId does not have a specific list of PINs attached to it.
      // It is just a place to record the results of the process.
      // The query in FARM_FIFO_PKG.Read_Fifo_Calculation_Items() gets all the PINs that haven't been processed yet.
      int importVersionId = 155739;
      fifoService.processFifoCalculations(conn, null, importVersionId, user);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
  }

  @Disabled 
  @Test
  public void runForSpecificPin() {

    Integer participantPin = 98765744;
    Integer programYear = 2024;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO and Final scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    deleteFinalScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertNotNull(itemResult.getEstimatedBenefit());
    assertNotNull(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertNotNull(fifoScenario.getBenefit().getTotalBenefit());
  }

  @Test
  public void notEnoughReferenceYears() {

    Integer participantPin = 22871610;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertEquals("Failed", itemResult.getScenarioStateCodeDesc());
    assertNull(itemResult.getEstimatedBenefit());
    assertNull(itemResult.getIsPaymentFile());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(1, errorMessages.size());
    
    String errorMessage = errorMessages.get(0);
    assertEquals("At least three reference years are required.", errorMessage);
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.FAILED, fifoScenario.getScenarioStateCode());
    assertNull(fifoScenario.getBenefit());
  }


  @Test
  public void inventoryEndValuesMissing() {

    Integer participantPin = 2570547;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    assertEquals(Double.valueOf(0), itemResult.getEstimatedBenefit());
    assertFalse(itemResult.getIsPaymentFile());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertNotNull(fifoScenario.getBenefit());
    assertEquals(Double.valueOf(0), fifoScenario.getBenefit().getTotalBenefit());
    
    {
      assertNotNull(fifoScenario.getFarmingYear());
      assertNotNull(fifoScenario.getFarmingYear().getFarmingOperations());
      assertEquals(1, fifoScenario.getFarmingYear().getFarmingOperations().size());
      
      FarmingOperation farmingOperation = fifoScenario.getFarmingYear().getFarmingOperations().get(0);
      assertNotNull(farmingOperation);
      assertNotNull(farmingOperation.getCropItems());
      assertEquals(3, farmingOperation.getCropItems().size());
    }
  }


  @Test
  public void missingBpuTurnOffStructureChange() {

    Integer participantPin = 25762691;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertNotNull(itemResult.getEstimatedBenefit());
    assertFalse(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.NONE, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.NONE, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertNotNull(fifoScenario.getBenefit().getTotalBenefit());
  }


  @Test
  public void multistageCommodityCodeForSpecificStage() {

    Integer participantPin = 25881582;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertNotNull(itemResult.getEstimatedBenefit());
    assertTrue(itemResult.getEstimatedBenefit() > 0);
    assertTrue(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertNotNull(fifoScenario.getBenefit().getTotalBenefit());
    assertTrue(fifoScenario.getBenefit().getTotalBenefit() > 0);
  }


  @Test
  public void missingApplesReceivable() {

    Integer participantPin = 26025916;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertEquals(Double.valueOf(0.0), itemResult.getEstimatedBenefit());
    assertFalse(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.NONE, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.NONE, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertEquals(Double.valueOf(0.0), fifoScenario.getBenefit().getTotalBenefit());
    
    {
      assertNotNull(fifoScenario.getFarmingYear());
      assertNotNull(fifoScenario.getFarmingYear().getFarmingOperations());
      assertEquals(1, fifoScenario.getFarmingYear().getFarmingOperations().size());
      
      FarmingOperation farmingOperation = fifoScenario.getFarmingYear().getFarmingOperations().get(0);
      assertNotNull(farmingOperation);
      assertNotNull(farmingOperation.getReceivableItems());
      assertEquals(1, farmingOperation.getReceivableItems().size());
      
      ReceivableItem receivableItem = farmingOperation.getReceivableItems().get(0);
      assertEquals("60", receivableItem.getInventoryItemCode());
      assertEquals(Double.valueOf(35000.0), receivableItem.getTotalStartOfYearAmount());
      assertEquals(Double.valueOf(35000.0), receivableItem.getTotalEndOfYearAmount());
    }
  }


  @Test
  public void bpuWithZeroMargin() {

    Integer participantPin = 23639834;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertNotNull(itemResult.getEstimatedBenefit());
    assertTrue(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertNotNull(fifoScenario.getBenefit().getTotalBenefit());
  }


  @Test
  public void missingBpuUsePreviousYear() {

    Integer participantPin = 3684453;
    Integer programYear = 2015;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertNotNull(itemResult.getEstimatedBenefit());
    assertNotNull(itemResult.getIsPaymentFile());
    assertFalse(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertNotNull(fifoScenario.getBenefit().getTotalBenefit());
  }


  @Test
  public void zeroPass() {

    Integer participantPin = 23370778;
    Integer programYear = 2022;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);
    
    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);

    // Delete FIFO and Final scenarios if left over from a previous test run
    deleteFifoScenarios(programYear, programYearMetadataList);
    deleteFinalScenarios(programYear, programYearMetadataList);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(1, fifoItemResults.size());
    
    FifoItemResult itemResult = fifoItemResults.get(0);
    assertNotNull(itemResult);
    assertEquals(participantPin, itemResult.getParticipantPin());
    assertEquals(programYear, itemResult.getProgramYear());
    assertNotNull(itemResult.getEstimatedBenefit());
    assertFalse(itemResult.getIsPaymentFile());
    assertEquals("Completed", itemResult.getScenarioStateCodeDesc());
    
    List<String> errorMessages = itemResult.getErrorMessages();
    assertNotNull(errorMessages);
    assertEquals(0, errorMessages.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(1, fifoScenarios.size());
    
    ScenarioMetaData fifoScenarioMetaData = fifoScenarios.get(0);
    Integer fifoScenarioNumber = fifoScenarioMetaData.getScenarioNumber();
    assertNotNull(fifoScenarioNumber);
    Scenario fifoScenario = null;
    try {
      fifoScenario = calculatorService.loadScenario(participantPin, programYear, fifoScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(fifoScenario);
    assertNotNull(fifoScenario.getClient());
    assertEquals(participantPin, fifoScenario.getClient().getParticipantPin());
    assertEquals(programYear, fifoScenario.getYear());
    assertEquals(ScenarioTypeCodes.FIFO, fifoScenario.getScenarioTypeCode());
    assertEquals(ScenarioCategoryCodes.FIFO, fifoScenario.getScenarioCategoryCode());
    assertEquals(ScenarioStateCodes.COMPLETED, fifoScenario.getScenarioStateCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getStructuralChangeMethodCode());
    assertEquals(StructuralChangeCodes.RATIO, fifoScenario.getBenefit().getExpenseStructuralChangeMethodCode());
    assertNotNull(fifoScenario.getBenefit());
    assertNotNull(fifoScenario.getBenefit().getTotalBenefit());
  }


  @Test
  public void notEnrolled() {

    Integer participantPin = 98765747;
    Integer programYear = 2024;

    List<ScenarioMetaData> programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    ScenarioMetaData latestCraScenario = ScenarioUtils.findLatestScenarioByType(programYearMetadataList, programYear, ScenarioTypeCodes.CRA);
    
    List<FifoCalculationItem> fifoItems = new ArrayList<>();
    {
      FifoCalculationItem item = new FifoCalculationItem();
      item.setParticipantPin(participantPin);
      item.setProgramYear(programYear);
      item.setCraProgramYearVersionId(latestCraScenario.getProgramYearVersionId());
      item.setCraScenarioId(latestCraScenario.getScenarioId());
      item.setCraScenarioNumber(latestCraScenario.getScenarioNumber());
      item.setFifoScenarioId(null);
      item.setFifoScenarioNumber(null);
      item.setFifoProgramYearVersionId(null);
      fifoItems.add(item);
    }
    
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    try {
      fifoService.calculateFifoBenefits(conn, fifoItems, fifoItemResults, null, null, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(fifoItemResults);
    assertEquals(0, fifoItemResults.size());
    
    programYearMetadataList = getProgramYearMetadata(participantPin, programYear);

    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadataList, programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO);
    assertNotNull(fifoScenarios);
    assertEquals(0, fifoScenarios.size());
  }

  @Test
  public final void testFifoStatusByYear() {

    try {
      List<FifoStatus> fifoStatusList = fifoService.getFifoStatusByYear(2023);

      for (FifoStatus f : fifoStatusList) {
        logger.debug(f.toString());
      }
      assertTrue(fifoStatusList.size() > 0);
    } catch (Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }


  private void deleteFinalScenarios(Integer programYear, List<ScenarioMetaData> programYearMetadata) {
    deleteScenarios(programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER, programYearMetadata);
  }


  private void deleteFifoScenarios(Integer programYear, List<ScenarioMetaData> programYearMetadata) {
    deleteScenarios(programYear, ScenarioCategoryCodes.FIFO, ScenarioTypeCodes.FIFO, programYearMetadata);
  }


  private void deleteScenarios(
      Integer programYear,
      String scenarioCategoryCode,
      String scenarioTypeCode,
      List<ScenarioMetaData> programYearMetadata) {
    // Delete the FIFO scenarios
    List<ScenarioMetaData> fifoScenarios =
        ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear, scenarioCategoryCode, scenarioTypeCode);
    for(ScenarioMetaData scenarioMetadata : fifoScenarios) {
      Integer fifoScenario = scenarioMetadata.getScenarioId();
      assertNotNull(fifoScenario);

      try {
        calculatorDao.deleteUserScenario(conn, fifoScenario);
        conn.commit();
      } catch (DataAccessException | SQLException e) {
        e.printStackTrace();
        try {
          conn.rollback();
        } catch (SQLException e1) {
          e1.printStackTrace();
          fail("Unexpected Exception");
        }
        fail("Unexpected Exception");
      }
    }
  }

  private List<ScenarioMetaData> getProgramYearMetadata(Integer participantPin, Integer programYear) {
    // Get the list of scenarios for the program year
    List<ScenarioMetaData> programYearMetadata = null;
    try {
      programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
    } catch (SQLException e) {
      e.printStackTrace();
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
        fail("Unexpected Exception");
      }
      fail("Unexpected Exception");
    }
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());
    return programYearMetadata;
  }

}
