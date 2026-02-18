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
package ca.bc.gov.srm.farm.chefs;

import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.forms.ChefsFarmTypeCodes;
import ca.bc.gov.srm.farm.chefs.processor.StatementASubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.common.CropGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.GrainGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.IncomeExpenseGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.InputGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.LivestockGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.NurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.OtherPucGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.PayableGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.ReceivablesGrid;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementACombined;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementANonParticipantPartner;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAPartner;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionRequestDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.dao.ImportDAO;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageCalculationItem;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageItemResult;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageResults;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class ChefsStatementASubmissionTest extends ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsStatementASubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.STA;

  @Test
  public void getSubmissions() {

    List<SubmissionListItemResource> submissionsList = null;
    try {
      submissionsList = chefsApiDao.getResourceList(submissionsUrl, SubmissionListItemResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionsList);
    assertTrue(submissionsList.size() > 0);

    SubmissionListItemResource firstSubmission = submissionsList.get(0);
    assertNotNull(firstSubmission);
    String submissionGuid = firstSubmission.getSubmissionGuid();
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "6a8659bf-4290-477e-a16b-1dd457c4e4f3";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    StatementASubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals(Integer.valueOf(93778842), data.getAgriStabilityAgriInvestPin());
    assertEquals("987654321", data.getSinNumber());
    assertEquals("DEV", data.getEnvironment());

    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertNull(data.getInternalMethod());
    assertEquals(Arrays.asList("cattle", "customFeed", "poultry", "swine", "otherLivestock"), data.getLivestockFarmed());
    assertEquals(Arrays.asList("berries", "grainsOilseeds", "treefruitsGrapes", "vegetables", "nurseriesGreenhouse", "nonEdibleHorticulture"),
        data.getCropsFarmed());

    GrainGrid grainGrid = new GrainGrid();
    grainGrid.setAcres(0.89);
    grainGrid.setGrade("AAA");
    grainGrid.setUnits(new LabelValue("Bushels", "4"));
    grainGrid.setCommodity(new LabelValue("5100 - Barley", "5100"));
    grainGrid.setIsIrrigated("yes");
    grainGrid.setEndingInventory(0.0);
    grainGrid.setEndingPricePerUnit(12.2);
    grainGrid.setQuantitySold(5.0);
    grainGrid.setQuantityProduced(4.0);
    grainGrid.setQuantityPurchased(5.0);
    grainGrid.setQuantityUsedForFeed(2.0);
    grainGrid.setQuantityUsedForSeed(1.0);

    GrainGrid grainGrid2 = new GrainGrid();
    grainGrid2.setAcres(20.0);
    grainGrid2.setGrade("B");
    grainGrid2.setUnits(new LabelValue("Tonnes", "2"));
    grainGrid2.setCommodity(new LabelValue("5274 - Canola, Polish", "5274"));
    grainGrid2.setIsIrrigated("yes");
    grainGrid2.setEndingInventory(12.0);
    grainGrid2.setEndingPricePerUnit(2.0);
    grainGrid2.setQuantitySold(1.0);
    grainGrid2.setQuantityProduced(1.0);
    grainGrid2.setQuantityPurchased(1.0);
    grainGrid2.setQuantityUsedForFeed(2.0);
    grainGrid2.setQuantityUsedForSeed(2.0);

    List<GrainGrid> expectedGrainGridList = new ArrayList<>();
    expectedGrainGridList.add(grainGrid);
    expectedGrainGridList.add(grainGrid2);

    assertEquals(2, data.getGrainGrid().size());
    assertArrayEquals(expectedGrainGridList.toArray(), data.getGrainGrid().toArray());

    CropGrid berryGrid = new CropGrid();
    berryGrid.setAcres(1.5);
    berryGrid.setUnits(new LabelValue("Pounds", "1"));
    berryGrid.setCommodity(new LabelValue("5000 - Blackberries", "5000"));
    berryGrid.setQuantitySold(1.5);
    berryGrid.setQuantityProduced(1.5);
    List<CropGrid> expectedBerryGridList = new ArrayList<>();
    expectedBerryGridList.add(berryGrid);

    berryGrid = new CropGrid();
    berryGrid.setAcres(0.5);
    berryGrid.setUnits(new LabelValue("Kilograms", "5"));
    berryGrid.setCommodity(new LabelValue("5012 - Gooseberries", "5012"));
    berryGrid.setQuantitySold(0.7);
    berryGrid.setQuantityProduced(0.9);
    expectedBerryGridList.add(berryGrid);

    assertEquals(2, data.getBerryGrid().size());
    assertArrayEquals(expectedBerryGridList.toArray(), data.getBerryGrid().toArray());

    List<InputGrid> expectedInputGridList = new ArrayList<>();
    InputGrid inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("264 - Forage", "264"));
    inputGrid.setAmountRemainingAtYearEnd(4.0);
    expectedInputGridList.add(inputGrid);

    inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("9764 - Fuel", "9764"));
    inputGrid.setAmountRemainingAtYearEnd(44.0);
    expectedInputGridList.add(inputGrid);

    assertEquals(2, data.getInputGrid().size());
    assertArrayEquals(expectedInputGridList.toArray(), data.getInputGrid().toArray());

    List<LivestockGrid> expectedOtherGridList = new ArrayList<>();
    LivestockGrid otherGrid = new LivestockGrid();
    otherGrid.setCommodity(new LabelValue("7560 - Llama; Crias Born", "7560"));
    otherGrid.setEndingFmv(7.0);
    otherGrid.setEndingInventory(7.0);
    otherGrid.setQuantitySold(7.0);
    expectedOtherGridList.add(otherGrid);

    assertEquals(1, data.getOtherGrid().size());
    assertArrayEquals(expectedOtherGridList.toArray(), data.getOtherGrid().toArray());

    List<LivestockGrid> expectedSwineGridList = new ArrayList<>();
    LivestockGrid swineGrid = new LivestockGrid();
    swineGrid.setCommodity(new LabelValue("8700 - Pot Bellied Pigs", "8700"));
    swineGrid.setEndingFmv(5.0);
    swineGrid.setEndingInventory(5.0);
    swineGrid.setQuantitySold(5.0);
    expectedSwineGridList.add(swineGrid);

    swineGrid = new LivestockGrid();
    swineGrid.setCommodity(new LabelValue("8710 - Pot Bellied Pigs; Piglets Born", "8710"));
    swineGrid.setEndingFmv(2.0);
    swineGrid.setEndingInventory(2.0);
    swineGrid.setQuantitySold(2.0);
    expectedSwineGridList.add(swineGrid);

    assertEquals(2, data.getSwineGrid().size());
    assertArrayEquals(expectedSwineGridList.toArray(), data.getSwineGrid().toArray());

    List<NurseryGrid> expectedNurseryGridList = new ArrayList<>();
    NurseryGrid nurseryGrid = new NurseryGrid();
    nurseryGrid.setCommodity(new LabelValue("6930 - Maple Syrup", "6930"));
    nurseryGrid.setUnits(new LabelValue("Kilograms", "5"));
    nurseryGrid.setQuantitySold(5.0);
    nurseryGrid.setQuantityProduced(5.0);
    nurseryGrid.setEndingInventory(5.0);
    nurseryGrid.setSquareMeters(5);
    expectedNurseryGridList.add(nurseryGrid);

    nurseryGrid = new NurseryGrid();
    nurseryGrid.setCommodity(new LabelValue("6951 - Flowers; Fresh Cut", "6951"));
    nurseryGrid.setUnits(new LabelValue("Bushels", "4"));
    nurseryGrid.setQuantitySold(4.0);
    nurseryGrid.setQuantityProduced(4.0);
    nurseryGrid.setEndingInventory(4.0);
    nurseryGrid.setSquareMeters(4);
    expectedNurseryGridList.add(nurseryGrid);

    assertEquals(2, data.getNurseryGrid().size());
    assertArrayEquals(expectedNurseryGridList.toArray(), data.getNurseryGrid().toArray());

    List<LivestockGrid> expectedPoultryGridList = new ArrayList<>();
    LivestockGrid poultryGrid = new LivestockGrid();
    poultryGrid.setCommodity(new LabelValue("7658 - Chickens; Pullets", "7658"));
    poultryGrid.setEndingInventory(20.0);
    poultryGrid.setQuantitySold(111.0);
    poultryGrid.setEndingFmv(25.0);
    expectedPoultryGridList.add(poultryGrid);

    assertEquals(1, data.getPoultryGrid().size());
    assertArrayEquals(expectedPoultryGridList.toArray(), data.getPoultryGrid().toArray());

    List<PayableGrid> expectedExpensesGridList = new ArrayList<>();
    PayableGrid expensesGrid = new PayableGrid();
    expensesGrid.setSourceOfExpense(new LabelValue("135 - Seed", "135"));
    expensesGrid.setProgramExpensesNotPaidByYearEnd(444.0);
    expectedExpensesGridList.add(expensesGrid);

    expensesGrid = new PayableGrid();
    expensesGrid.setSourceOfExpense(new LabelValue("9815 - Arm's Length Salaries", "9815"));
    expensesGrid.setProgramExpensesNotPaidByYearEnd(12.0);
    expectedExpensesGridList.add(expensesGrid);

    assertEquals(2, data.getExpensesGrid().size());
    assertArrayEquals(expectedExpensesGridList.toArray(), data.getExpensesGrid().toArray());

    List<CropGrid> expectedTreeFruitGridList = new ArrayList<>();
    CropGrid treeFruitGrid = new CropGrid();
    treeFruitGrid.setCommodity(new LabelValue("5030 - Apples", "5030"));
    treeFruitGrid.setUnits(new LabelValue("Pounds", "1"));
    treeFruitGrid.setAcres(5.0);
    treeFruitGrid.setQuantitySold(0.0);
    treeFruitGrid.setQuantityProduced(0.0);
    expectedTreeFruitGridList.add(treeFruitGrid);

    assertEquals(1, data.getTreeFruitGrid().size());
    assertArrayEquals(expectedTreeFruitGridList.toArray(), data.getTreeFruitGrid().toArray());

    List<CropGrid> expectedVegetablesGridList = new ArrayList<>();
    CropGrid vegetableGrid = new CropGrid();
    vegetableGrid.setCommodity(new LabelValue("5034 - Artichokes", "5034"));
    vegetableGrid.setUnits(new LabelValue("Tonnes", "2"));
    vegetableGrid.setAcres(1.0);
    vegetableGrid.setQuantitySold(1.0);
    vegetableGrid.setQuantityProduced(1.0);
    expectedVegetablesGridList.add(vegetableGrid);

    vegetableGrid = new CropGrid();
    vegetableGrid.setCommodity(new LabelValue("6903 - Thyme", "6903"));
    vegetableGrid.setUnits(new LabelValue("Pounds", "1"));
    vegetableGrid.setAcres(2.0);
    vegetableGrid.setQuantitySold(2.0);
    vegetableGrid.setQuantityProduced(2.0);
    expectedVegetablesGridList.add(vegetableGrid);

    assertEquals(2, data.getVegetableGrid().size());
    assertArrayEquals(expectedVegetablesGridList.toArray(), data.getVegetableGrid().toArray());

    assertEquals("245.99", data.getTotalAllowableExpenses().toString());
  }

  @Test
  public void getSubmissionIndividualPartnership() {

    String submissionGuid = "16607d6b-02ec-47b5-bd28-8b18d901a7ff";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    StatementASubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals(Integer.valueOf(31415954), data.getAgriStabilityAgriInvestPin());
    assertEquals("987654320", data.getSinNumber());
    assertEquals("DEV", data.getEnvironment());

    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertNull(data.getInternalMethod());

    assertEquals("Status Indian farming on a reserve", data.getFarmType().getLabel());
    assertEquals("statusIndianFarmingOnAReserve", data.getFarmType().getValue());
    assertEquals("partnership", data.getBusinessStructure());

    List<StatementAPartner> partnerships = new ArrayList<>();

    StatementAPartner partner = new StatementAPartner();
    partner.setPin(31415955);
    partner.setPercent(20.0);
    partnerships.add(partner);

    partner = new StatementAPartner();
    partner.setPin(31415956);
    partner.setPercent(5.5);
    partnerships.add(partner);

    assertArrayEquals(partnerships.toArray(), data.getPartnershipGrid().toArray());

    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("no", data.getCompletedProductionCycle());
    assertEquals("yes", data.getUnableToCompleteBecauseOfDisaster());
    assertEquals("346.0", data.getTotalAllowableIncome().toString());
    assertEquals("122.08", data.getTotalAllowableExpenses().toString());
    assertEquals("34.0", data.getTotalNonAllowableIncome().toString());
    assertEquals("346.0", data.getTotalAllowableIncomeSummary().toString());
    assertEquals("275.82", data.getNetIncomeAfterAdjustments().toString());
    assertEquals("257.92", data.getNetIncomeBeforeAdjustments().toString());
    assertEquals("BC", data.getProvinceTerritoryOfMainFarmstead());
    
    assertEquals(Arrays.asList("poultry", "otherLivestock"), data.getLivestockFarmed());
    assertEquals(Arrays.asList("treefruitsGrapes", "grainsOilseeds", "nurseriesGreenhouse"), data.getCropsFarmed());

    GrainGrid grainGrid = new GrainGrid();
    grainGrid.setAcres(454.0);
    grainGrid.setGrade("");
    grainGrid.setCommodity(new LabelValue("6740 - Wheat, CPS white, organic", "6740"));
    grainGrid.setIsIrrigated("no");
    grainGrid.setUnits(new LabelValue(null, null));

    GrainGrid grainGrid2 = new GrainGrid();
    grainGrid2.setAcres(23.0);
    grainGrid2.setGrade("aa");
    grainGrid2.setUnits(new LabelValue("Bushels", "4"));
    grainGrid2.setCommodity(new LabelValue("5238 - Barley, CW select two-row", "5238"));
    grainGrid2.setIsIrrigated("yes");
    grainGrid2.setEndingInventory(5.0);
    grainGrid2.setEndingPricePerUnit(5.0);
    grainGrid2.setQuantitySold(5.0);
    grainGrid2.setQuantityProduced(5.0);
    grainGrid2.setQuantityPurchased(5.0);
    grainGrid2.setQuantityUsedForFeed(5.0);
    grainGrid2.setQuantityUsedForSeed(5.0);

    List<GrainGrid> expectedGrainGridList = new ArrayList<>();
    expectedGrainGridList.add(grainGrid2);
    expectedGrainGridList.add(grainGrid);

    List<InputGrid> expectedInputGridList = new ArrayList<>();
    InputGrid inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("264 - Forage", "264"));
    inputGrid.setAmountRemainingAtYearEnd(12.0);
    expectedInputGridList.add(inputGrid);

    inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("9764 - Fuel", "9764"));
    inputGrid.setAmountRemainingAtYearEnd(3.0);
    expectedInputGridList.add(inputGrid);

    inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("9661 - Containers and twine", "9661"));
    inputGrid.setAmountRemainingAtYearEnd(77.0);
    expectedInputGridList.add(inputGrid);

    assertEquals(3, data.getInputGrid().size());
    assertArrayEquals(expectedInputGridList.toArray(), data.getInputGrid().toArray());

    List<LivestockGrid> expectedOtherGridList = new ArrayList<>();
    LivestockGrid otherGrid = new LivestockGrid();
    otherGrid.setCommodity(new LabelValue("7512 - Alpaca; Cull; Hembras", "7512"));
    otherGrid.setEndingFmv(3.0);
    otherGrid.setEndingInventory(3.0);
    otherGrid.setQuantitySold(3.0);
    expectedOtherGridList.add(otherGrid);

    assertEquals(1, data.getOtherGrid().size());
    assertArrayEquals(expectedOtherGridList.toArray(), data.getOtherGrid().toArray());

  }

  @Test
  public void getSubmissionCorporation() {
    String submissionGuid = "2ca3c0ae-aba8-45e9-ae7a-6d39b3324b6f";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    StatementASubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("APPLES@FARM.CA", data.getEmail());
    assertEquals(Integer.valueOf("31415949"), data.getAgriStabilityAgriInvestPin());
    assertEquals("9990 12346", data.getBusinessTaxNumber());
    assertNull(data.getSinNumber());
    assertNull(data.getTrustNumber());
    assertNull(data.getTrustBusinessNumber());

    assertEquals("corporation", data.getFarmType().getValue());

    assertEquals(2, data.getAllowableIncomeGrid().size());
    assertEquals(2, data.getAllowableExpensesGrid().size());
    assertEquals(2, data.getNonAllowablesGrid().size());
    assertEquals(2, data.getNonAllowableExpensesGrid().size());
    assertEquals(2, data.getReceivablesGrid().size());
    assertEquals(3, data.getExpensesGrid().size());
    assertEquals(4, data.getInputGrid().size());
    assertEquals(2, data.getBerryGrid().size());
    assertEquals(1, data.getVegetableGrid().size());
    assertEquals(3, data.getTreeFruitGrid().size());
    assertEquals(3, data.getGrainGrid().size());
    assertEquals(2, data.getNurseryGrid().size());
    assertEquals(1, data.getNeHorticultureGrid().size());
    assertEquals(2, data.getCattleGrid().size());
    assertEquals(3, data.getPoultryGrid().size());
    assertEquals(1, data.getSwineGrid().size());

    assertEquals("yes", data.getAuthorizeThirdParty());
    assertEquals("PAUL", data.getThirdPartyFirstName());
    assertEquals("BUNYAN", data.getThirdPartyLastName());
    assertEquals("345 BUSINESS STREET", data.getThirdPartyAddress());
    assertEquals("PAUL@LUMBER.INC", data.getThirdPartyEmail());
    assertEquals("KELOWNA", data.getThirdPartyTownCity());
    assertEquals("BC", data.getThirdPartyProvince());
    assertEquals("F3E 3E4", data.getThirdPartyPostalCode());
    assertEquals("(604) 123-5699", data.getThirdPartyTelephone());
    assertEquals("yes", data.getCopyOfCOB());

    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertEquals("DEV", data.getEnvironment());

    Date startDate = null;
    Date endDate = null;
    try {
      startDate = formatter.parse("2024-01-01");
      endDate = formatter.parse("2024-12-31");
    } catch (ParseException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertEquals(formatter.format(startDate), data.getFiscalYearStart());
    assertEquals(formatter.format(endDate), data.getFiscalYearEnd());

  }

  @Test
  public void pinNotFoundInFARM() {

    String submissionGuid = "STA00000-0000-STA0-0001-000000000000";
    Integer participantPin = 12316589; 

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);
    
    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("jsnow@game.of.thrones");
      data.setTelephone("(250) 555-5555");
      data.setCorporationName("APPLES R US");
      data.setSinNumber("123456789");
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE, ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE));
      data.setTelephone("(250) 555-5555");
      data.setCorporationName("APPLES R US");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNull(task.getAccountId());
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
          + "- PIN not found in BCFARMS.\n" + "\n"
          + "Corporation Name: APPLES R US\n" + "Telephone: (250) 555-5555\n"
          + "Email: jsnow@game.of.thrones\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }

  }

  @Test
  public void pinNotFoundInCRM() {

    String submissionGuid = "STA00000-0000-0001-0002-000000000000";
    Integer participantPin = 3707197; 

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("targaryen@game.of.thrones");
      data.setTelephone("(250) 555-5555");
      data.setCorporationName("APPLES R US");
      data.setSinNumber(null);
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CORPORATION, ChefsFarmTypeCodes.CORPORATION));
      data.setBusinessTaxNumber("123456789");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNull(task.getAccountId()); // This PIN does not exist in CRM
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n" + "\n"
          + "Corporation Name: APPLES R US\n" + "Telephone: (250) 555-5555\n"
          + "Email: targaryen@game.of.thrones\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
  }

  @Test
  public void sinMismatch() {

    String submissionGuid = "STA00000-0000-STA0-0003-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      data.setEmail("JOHNNY@FARM.CA");
      data.setTelephone("(250) 555-5555");
      data.setCorporationName("APPLES R US");
  
      data.setAgriStabilityAgriInvestPin(3693470);
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE, ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE));
      data.setSinNumber("123456789");
      data.setBusinessTaxNumber(null);
      
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A 3693470", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n\n"
          + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n" + "Corporation Name: APPLES R US\n"
          + "Telephone: (250) 555-5555\n" + "Email: JOHNNY@FARM.CA\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
  }

  @Test
  public void sinMissingInFarm() {

    String submissionGuid = "STA00000-0000-STA0-0004-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("JOHNNY@FARM.CA");
      data.setTelephone("(250) 555-5555");
      data.setCorporationName("APPLES R US");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE, ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE));
      data.setAgriStabilityAgriInvestPin(31415976);
      data.setSinNumber("123456789");
      data.setBusinessTaxNumber(null);
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A 31415976", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n\n"
          + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"null\".\n" + "\n" 
          + "Corporation Name: APPLES R US\n"
          + "Telephone: (250) 555-5555\n" + "Email: JOHNNY@FARM.CA\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }

  }

  @Test
  public void businessNumberMissingInFarm() {

    String submissionGuid = "STA00000-0000-0001-0004-000000000000";
    Integer participantPin = 31415975;

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("APPLES@FARM.CA");
      data.setCorporationName("APPLES INC");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CORPORATION, ChefsFarmTypeCodes.CORPORATION));
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setBusinessTaxNumber("1234 56789");
      data.setSinNumber(null);
      data.setTelephone("(250) 555-5555");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A 31415975", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n\n" 
          + "Corporation Name: APPLES INC\n"
          + "Telephone: (250) 555-5555\n" 
          + "Email: APPLES@FARM.CA\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }

  @Test
  public void businessNumberMismatch() {

    String submissionGuid = "STA00000-0000-0001-0004-000000000000";
    Integer participantPin = 5070370;

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setEmail("APPLES@FARM.CA");
      data.setCorporationName("APPLES INC");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CORPORATION, ChefsFarmTypeCodes.CORPORATION));
      data.setTelephone("(250) 555-5555");
  
      data.setSinNumber(null);
      data.setBusinessTaxNumber("1234 56789");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Field \"Business Number\" with value \"123456789RC0001\" does not match BCFARMS: \"999999999RC0001\"."
          + " Note that only the first nine digits are compared.\n" + "\n" 
          + "Corporation Name: APPLES INC\n" + "Telephone: (250) 555-5555\n"
          + "Email: APPLES@FARM.CA\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
  }

  @Test
  public void trustNumberMissingInFarm() {

    String submissionGuid = "STA00000-0000-0001-0004-T10000000000";
    Integer participantPin  = 31415975; 

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("targaryen@game.of.thrones");
  
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setCorporationName("APPLES INC");
      data.setTelephone("(250) 555-5555");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION, ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION));
      data.setSinNumber(null);
      data.setTrustNumber(null);
      data.setBusinessTaxNumber("1234 56789");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
              + "- Required field is blank: Trust Number\n\n"
              + "Corporation Name: APPLES INC\n" + "Telephone: (250) 555-5555\n"
              + "Email: targaryen@game.of.thrones\n",
          task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }

  }

  @Test
  public void invalidTrustNumberFormat() {

    String submissionGuid = "STA00000-0000-0001-0004-T10000000000";
    Integer participantPin  = 31415975; 

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("targaryen@game.of.thrones");
  
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setCorporationName("APPLES INC");
      data.setTelephone("(250) 555-5555");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION, ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION));
      data.setSinNumber(null);
      data.setTrustNumber("ffdsa");
      data.setBusinessTaxNumber("1234 56789");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
              + "- Trust Number in BCFARMS does not start with a 8 digit number. Unable to validate.\n\n"
              + "Corporation Name: APPLES INC\n" + "Telephone: (250) 555-5555\n"
              + "Email: targaryen@game.of.thrones\n",
          task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }

  }

  @Test
  public void trustNumberMismatch() {

    String submissionGuid = "STA00000-0000-0001-0004-T00000000000";
    Integer participantPin  = 23468788; 

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setEmail("targaryen@game.of.thrones");
  
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setCorporationName("APPLES INC");
      data.setTelephone("(250) 555-5555");
      data.setAgriStabilityAgriInvestPin(23468788);
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION, ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION));
      data.setTrustNumber("12345678");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Field \"Trust Number\" with value \"T12345678\" does not match BCFARMS: \"T99999999\"."
          + " Note that only the first eight digits are compared.\n" + "\n"
          + "Corporation Name: APPLES INC\n" + "Telephone: (250) 555-5555\n"
          + "Email: targaryen@game.of.thrones\n", task.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());

    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }

  @Test
  public void invalidBusinessNumberInFARM() {

    String submissionGuid = "STA00000-0000-STA0-0005-000000000000";
    Integer participantPin = 22503767;

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      Date startDate = null;
      Date endDate = null;
      try {
        startDate = formatter.parse("2024-01-01");
        endDate = formatter.parse("2024-12-31");
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
  
      data.setEmail("targaryen@game.of.thrones");
      data.setCorporationName("APPLES INC");
      data.setTelephone("(250) 555-5555");
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CORPORATION, ChefsFarmTypeCodes.CORPORATION));
      data.setSinNumber(null);
      data.setBusinessTaxNumber("123456789");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      assertNotNull(task.getAccountId());
      assertEquals("2024 Statement A " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n" + "\n"
          + "Corporation Name: APPLES INC\n" + "Telephone: (250) 555-5555\n"
          + "Email: targaryen@game.of.thrones\n", task.getDescription());
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }

  @Test
  public void corporateHappyPath() {

    String submissionGuid = "2ca3c0ae-aba8-45e9-ae7a-6d39b3324b6f";
    Integer participantPin = 31415949;
    
    Date startDate = null;
    Date endDate = null;
    try {
      startDate = formatter.parse("2024-01-01");
      endDate = formatter.parse("2024-12-31");
    } catch (ParseException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    Integer programYear = DateUtils.getYearFromDate(endDate);

    
    // Get the list of scenarios for the program year
    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the USER scenarios linked to this submission if any exist, from a
    // previous test run.
    // Update scenarioSubmissionId to null for non-USER scenarios.
    TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
    TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
    deleteUserScenarios(submissionGuid, programYearMetadata);
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {
      
      // Set up the Statement A CHEFS Form submission data (not getting it from
      // CHEFS).
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      data.setEmail("APPLES@FARM.CA");
      data.setCorporationName("JOHNNY APPLESEED");
      data.setFirstNameCorporateContact("JOHNNY");
      data.setLastNameCorporateContact("APPLESEED");
      data.setAddress("1111 GRAPE LANE");
      data.setPostalCode("V5R6T6");
      data.setTownCity("PENTICTON2");
      data.setProvince("BC");
      data.setTelephone("(604) 555-5555");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CORPORATION, ChefsFarmTypeCodes.CORPORATION));
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setBusinessTaxNumber("9990 12346");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setAuthorizeThirdParty("yes");
      data.setThirdPartyFirstName("PAUL");
      data.setThirdPartyLastName("BUNYAN");
      data.setThirdPartyBusinessName("LUMBER INC");
      data.setThirdPartyAddress("345 BUSINESS STREET");
      data.setThirdPartyTownCity("KELOWNA");
      data.setThirdPartyProvince("BC");
      data.setThirdPartyPostalCode("F3E 3E4");
      data.setThirdPartyTelephone("(604) 123-5699");
      data.setThirdPartyFax("(604) 777-7777");
      data.setThirdPartyEmail("PAUL@LUMBER.INC");
  
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setCompletedProductionCycle("no");
      data.setCopyOfCOB("yes");
      data.setBusinessStructure("partnership");
      data.setWas2024FinalYearOfFarming("yes");
      Integer numberOfYearsFarmed = 6;
      data.setNumberOfYearsFarmed(numberOfYearsFarmed);
      data.setProvinceTerritoryOfMainFarmstead("BC");
      data.setCompletedProductionCycle("no");
      data.setUnableToCompleteBecauseOfDisaster("yes");
      data.setAccountingMethod("cash");
      data.setDoYouHaveMultipleOperations("no");
      
      data.setBusinessStructure("partnership");
      data.setFarmingOperationPercentage(50.0);
      List<StatementAPartner> statementAPartners = new ArrayList<>();
      StatementAPartner p = new StatementAPartner();
      p.setPin(4375663);
      p.setPercent(10.0);
      statementAPartners.add(p);
      
      p = new StatementAPartner();
      p.setPin(23366735);
      p.setPercent(30.0);
      statementAPartners.add(p);
      data.setPartnershipGrid(statementAPartners);
      
      List<StatementANonParticipantPartner> nonParticipantPartners = new ArrayList<>();
      StatementANonParticipantPartner np = new StatementANonParticipantPartner();
      np.setName("non participant");
      np.setPercent(10.0);
      nonParticipantPartners.add(np);
      data.setNonParticipantPartnershipGrid(nonParticipantPartners);
      
      List<StatementACombined> combinedGrid = new ArrayList<>();
      StatementACombined c = new StatementACombined();
      c.setPin(93887754);
      c.setAddRemove("add");
      combinedGrid.add(c);
      
      c = new StatementACombined();
      c.setPin(31415926);
      c.setAddRemove("remove");
      combinedGrid.add(c);
      
      data.setCombinedGrid(combinedGrid);
      
      data.setTotalAdjustments(1.0);
      data.setTotalIncome(100.00);
      data.setTotalExpenses(40.99);
      data.setNetIncomeBeforeAdjustments(230.1);
      data.setNetIncomeAfterAdjustments(200.00);
      
      List<IncomeExpenseGrid> allowableIncomes = new ArrayList<>();
      IncomeExpenseGrid income = new IncomeExpenseGrid();
      income.setCategory(new LabelValue("Cherries (sweet; sour) - 92", "92"));
      income.setAmount(10.0);
      allowableIncomes.add(income);
      data.setAllowableIncomeGrid(allowableIncomes);
      
      List<IncomeExpenseGrid> nonAllowableIncomes = new ArrayList<>();
      {
        IncomeExpenseGrid nonAllowableIncome = new IncomeExpenseGrid();
        nonAllowableIncome.setCategory(new LabelValue("Resales, rebates, GST/HST for non-allowable expenses, recapture of capital cost allowance (CCA) - 9575", "9575"));
        nonAllowableIncome.setAmount(100.0);
        nonAllowableIncomes.add(nonAllowableIncome);
        data.setNonAllowablesGrid(nonAllowableIncomes);
      }
      {
        IncomeExpenseGrid nonAllowableIncome = new IncomeExpenseGrid();
        nonAllowableIncome.setCategory(new LabelValue("Other Income (specify) - 9600", "9600"));
        nonAllowableIncome.setAmount(200.0);
        nonAllowableIncome.setSpecify("Canada Emergency Business Account - CEBA Loan");
        nonAllowableIncomes.add(nonAllowableIncome);
        data.setNonAllowablesGrid(nonAllowableIncomes);
      }
      {
        IncomeExpenseGrid nonAllowableIncome = new IncomeExpenseGrid();
        nonAllowableIncome.setCategory(new LabelValue("Other Income (specify) - 9600", "9600"));
        nonAllowableIncome.setAmount(300.0);
        nonAllowableIncome.setSpecify("AgriStability Interim Payment");
        nonAllowableIncomes.add(nonAllowableIncome);
        data.setNonAllowablesGrid(nonAllowableIncomes);
      }
      
      List<IncomeExpenseGrid> allowableExpenses = new ArrayList<>();
      IncomeExpenseGrid allowableExpense = new IncomeExpenseGrid();
      allowableExpense.setCategory(new LabelValue("Fertilizers and soil supplements - 9662", "9662"));
      allowableExpense.setAmount(7.2);
      allowableExpenses.add(allowableExpense);
      data.setAllowableExpensesGrid(allowableExpenses);
      
      List<IncomeExpenseGrid> nonAllowableExpenses = new ArrayList<>();
      {
        IncomeExpenseGrid nonAllowableExpense = new IncomeExpenseGrid();
        nonAllowableExpense.setCategory(new LabelValue("Machinery (repairs, licences, insurance) - 9760", "9760"));
        nonAllowableExpense.setAmount(4.1);
        nonAllowableExpenses.add(nonAllowableExpense);
        data.setNonAllowableExpensesGrid(nonAllowableExpenses);
      }
      {
        IncomeExpenseGrid nonAllowableExpense = new IncomeExpenseGrid();
        nonAllowableExpense.setCategory(new LabelValue("Other expenses - 9896", "9896"));
        nonAllowableExpense.setAmount(50.0);
        nonAllowableExpenses.add(nonAllowableExpense);
        data.setNonAllowableExpensesGrid(nonAllowableExpenses);
      }
      {
        IncomeExpenseGrid nonAllowableExpense = new IncomeExpenseGrid();
        nonAllowableExpense.setCategory(new LabelValue("Other expenses - 9896", "9896"));
        nonAllowableExpense.setAmount(20.0);
        nonAllowableExpenses.add(nonAllowableExpense);
        data.setNonAllowableExpensesGrid(nonAllowableExpenses);
      }
  
      data.setNumberOfCowsThatCalved(12.8);
      data.setEggsForConsumptionLC109(2.0);
      data.setEggsForHatchingLC108(5.0);
      data.setChickenBroilersLC143(4.0);
      data.setTurkeyBroilersLC144(6.0);
      data.setNumberFeedersUnder9(6.0);
      data.setNumberFeedersOver9(7.3);
      data.setNumberOfHogsFedUpTo50LbsLC124(9.9);
      data.setNumberOfHogsFedOver50LbsFeedersLC125(9.9);
      data.setNumberOfSowsThatFarrowedLC123(5.0);
  
      data.setLivestockFarmed(Arrays.asList("cattle", "customFeed", "poultry", "swine", "otherLivestock"));
      data.setCropsFarmed(Arrays.asList("berries", "grainsOilseeds", "treefruitsGrapes", "vegetables", "nurseriesGreenhouse", "nonEdibleHorticulture"));
  
      GrainGrid grainGrid = new GrainGrid();
      grainGrid.setAcres(3423.0);
      grainGrid.setGrade("AAA");
      grainGrid.setUnits(new LabelValue("Kilograms", "5"));
      grainGrid.setCommodity(new LabelValue("5392 - Beans, cranberry, organic", "5392"));
      grainGrid.setIsIrrigated("yes");
      grainGrid.setEndingInventory(6.0);
      grainGrid.setEndingPricePerUnit(7.0);
      grainGrid.setQuantitySold(4.0);
      grainGrid.setQuantityProduced(2.0);
      grainGrid.setQuantityPurchased(3.0);
      grainGrid.setQuantityUsedForFeed(8.0);
      grainGrid.setQuantityUsedForSeed(8.0);
  
      GrainGrid grainGrid2 = new GrainGrid();
      grainGrid2.setAcres(34.0);
      grainGrid2.setGrade("");
      grainGrid2.setCommodity(new LabelValue("5962 - Sunflowers, oilseed, no. 2", "5962"));
      grainGrid2.setQuantityProduced(5.6);
      grainGrid2.setIsIrrigated("no");
  
      List<GrainGrid> grainGridList = new ArrayList<>();
      grainGridList.add(grainGrid);
      grainGridList.add(grainGrid2);
      data.setGrainGrid(grainGridList);
  
      CropGrid berryGrid = new CropGrid();
      berryGrid.setAcres(34.0);
      berryGrid.setUnits(new LabelValue("Kilograms", "5"));
      berryGrid.setCommodity(new LabelValue("5012 - Gooseberries", "5012"));
      berryGrid.setQuantitySold(2.0);
      berryGrid.setQuantityProduced(5.0);
      List<CropGrid> berryGridList = new ArrayList<>();
      berryGridList.add(berryGrid);
  
      berryGrid = new CropGrid();
      berryGrid.setAcres(2.0);
      berryGrid.setUnits(new LabelValue("Pounds", "1"));
      berryGrid.setCommodity(new LabelValue("5010 - Elderberries", "5010"));
      berryGrid.setQuantitySold(4.0);
      berryGrid.setQuantityProduced(3.0);
      berryGridList.add(berryGrid);
  
      data.setBerryGrid(berryGridList);
      
      List<CropGrid> neHorticultureGridList = new ArrayList<>();
      CropGrid tree = new CropGrid();
      tree.setAcres(4.0);
      tree.setCommodity(new LabelValue("6962 - Christmas Trees; (3rd to 5th years)", "6962"));
      tree.setQuantitySold(2.0);
      tree.setQuantityProduced(3.0);
      neHorticultureGridList.add(tree);
      data.setNeHorticultureGrid(neHorticultureGridList);
      
      List<NurseryGrid> nurseryGridList = new ArrayList<>();
      NurseryGrid nurseryGrid = new NurseryGrid();
      nurseryGrid.setCommodity(new LabelValue("6930 - Maple Syrup", "6930"));
      nurseryGrid.setUnits(new LabelValue("Kilograms", "5"));
      nurseryGrid.setQuantitySold(5.0);
      nurseryGrid.setQuantityProduced(5.0);
      nurseryGrid.setEndingInventory(5.0);
      nurseryGrid.setSquareMeters(5);
      nurseryGridList.add(nurseryGrid);
      data.setNurseryGrid(nurseryGridList);
  
      List<ReceivablesGrid> receivableGridList = new ArrayList<>();
      ReceivablesGrid receivableGrid = new ReceivablesGrid();
      receivableGrid.setIncomeSource(new LabelValue("402 - PI insurance", "402"));
      receivableGrid.setIncomeReceivedAfterYearEnd(1.05);
      receivableGridList.add(receivableGrid);
  
      receivableGrid = new ReceivablesGrid();
      receivableGrid.setIncomeSource(new LabelValue("9600", "9600"));
      receivableGrid.setSpecify("other input");
      receivableGrid.setIncomeReceivedAfterYearEnd(65.5);
      receivableGridList.add(receivableGrid);
  
      receivableGrid = new ReceivablesGrid();
      receivableGrid.setIncomeSource(new LabelValue("401 - PI insurance", "401"));
      receivableGrid.setIncomeReceivedAfterYearEnd(1.05);
      receivableGridList.add(receivableGrid);
  
      data.setReceivablesGrid(receivableGridList);
  
      List<PayableGrid> payableGridList = new ArrayList<>();
      PayableGrid payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9896 - Other Input (Specify)", "9896"));
      payableGrid.setSpecify("other input");
      payableGrid.setProgramExpensesNotPaidByYearEnd(42.02);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "135"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "264"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "9662"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "9663"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "9714"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "9764"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "9815"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9713 - Medicine", "9836"));
      payableGrid.setProgramExpensesNotPaidByYearEnd(51.0);
      payableGridList.add(payableGrid);
  
      data.setExpensesGrid(payableGridList);
  
      List<InputGrid> inputGridList = new ArrayList<>();
      InputGrid inputGrid = new InputGrid();
      inputGrid.setInput(new LabelValue("9896 - Other Input (Specify)", "9896"));
      inputGrid.setSpecify("other input");
      inputGrid.setAmountRemainingAtYearEnd(44.02);
      inputGridList.add(inputGrid);
  
      inputGrid = new InputGrid();
      inputGrid.setInput(new LabelValue("9713 - Medicine", "9713"));
      inputGrid.setAmountRemainingAtYearEnd(1.0);
      inputGridList.add(inputGrid);
  
      inputGrid = new InputGrid();
      inputGrid.setInput(new LabelValue("9661 - Containers and twine", "9661"));
      inputGrid.setAmountRemainingAtYearEnd(1.0);
      inputGridList.add(inputGrid);
  
      data.setInputGrid(inputGridList);
  
      List<LivestockGrid> otherGridList = new ArrayList<>();
      LivestockGrid otherGrid = new LivestockGrid();
      otherGrid.setCommodity(new LabelValue("7616 - Leaf Cutter Bees", "7616"));
      otherGrid.setEndingFmv(7.0);
      otherGrid.setEndingInventory(7.0);
      otherGrid.setQuantitySold(7.0);
      otherGridList.add(otherGrid);
  
      otherGrid = new LivestockGrid();
      otherGrid.setCommodity(new LabelValue("7560 - Llama; Crias Born", "7560"));
      otherGrid.setEndingFmv(5.0);
      otherGrid.setEndingInventory(4.0);
      otherGrid.setQuantitySold(3.0);
      otherGridList.add(otherGrid);
  
      otherGrid = new LivestockGrid();
      otherGrid.setCommodity(new LabelValue("7560 - Llama; Crias Born", "7512"));
      otherGrid.setEndingFmv(5.0);
      otherGrid.setEndingInventory(4.0);
      otherGrid.setQuantitySold(3.0);
      otherGridList.add(otherGrid);
  
      otherGrid = new LivestockGrid();
      otherGrid.setCommodity(new LabelValue("7560 - Llama; Crias Born", "7928"));
      otherGrid.setEndingFmv(5.0);
      otherGrid.setEndingInventory(4.0);
      otherGrid.setQuantitySold(3.0);
      otherGridList.add(otherGrid);
  
      data.setOtherGrid(otherGridList);
  
      List<LivestockGrid> swineGridList = new ArrayList<>();
      LivestockGrid swineGrid = new LivestockGrid();
      swineGrid.setCommodity(new LabelValue("4001 - Livestock Inventory Default", "4001"));
      swineGrid.setEndingFmv(9.0);
      swineGrid.setEndingInventory(43.0);
      swineGrid.setQuantitySold(3.0);
      swineGridList.add(swineGrid);
  
      swineGrid = new LivestockGrid();
      swineGrid.setCommodity(new LabelValue("8710 - Pot Bellied Pigs; Piglets Born", "8710"));
      swineGrid.setEndingFmv(3.0);
      swineGrid.setEndingInventory(12.0);
      swineGrid.setQuantitySold(0.0);
      swineGridList.add(swineGrid);
  
      swineGrid = new LivestockGrid();
      swineGrid.setCommodity(new LabelValue("8710 - Pot Bellied Pigs; Piglets Born", "8708"));
      swineGrid.setEndingFmv(3.0);
      swineGrid.setEndingInventory(12.0);
      swineGrid.setQuantitySold(0.0);
      swineGridList.add(swineGrid);
  
      data.setSwineGrid(swineGridList);
  
      List<OtherPucGrid> otherPucGridList = new ArrayList<>();
      OtherPucGrid otherPucGrid = new OtherPucGrid();
      otherPucGrid.setSelectOtherLivestock(new LabelValue("101 - Bison", "101"));
      otherPucGrid.setOtherLivestockNumber(7);
      otherPucGridList.add(otherPucGrid);
      data.setOpdGrid(otherPucGridList);
      
      data.setTaxesProgramYear("no");

      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
      // Process the submission data
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNull(task);
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData statementAScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.CHEF_STA, ScenarioTypeCodes.CHEF);
      Integer statementAScenarioNumber = statementAScenarioMetadata.getScenarioNumber();
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, statementAScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
  
      assertNotNull(scenario);
      assertNotNull(scenario.getScenarioId());
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(statementAScenarioNumber, scenario.getScenarioNumber());
      assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
      assertEquals(ScenarioStateCodes.RECEIVED, scenario.getScenarioStateCode());
      assertEquals(ScenarioCategoryCodes.CHEF_STA, scenario.getScenarioCategoryCode());
      assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());
      Date currentDate  = new Date();
      assertEquals(formatter.format(currentDate), formatter.format(scenario.getLocalStatementAReceivedDate()));
      assertEquals(formatter.format(currentDate), formatter.format(scenario.getLocalSupplementalReceivedDate()));
  
      FarmingOperation fo = scenario.getFarmingYear().getFarmingOperations().get(0);
  
      HashMap<String, Double> productiveUnitsMap = new HashMap<>();
      for (ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
        productiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
      }
      logger.debug("productiveUnitsMap: " + productiveUnitsMap);
      assertEquals(Double.valueOf(12.8), productiveUnitsMap.get("104"));
      assertEquals(Double.valueOf(5.0), productiveUnitsMap.get("108"));
      assertEquals(Double.valueOf(2.0), productiveUnitsMap.get("109"));
      assertEquals(Double.valueOf(3423.0), productiveUnitsMap.get("5392"));
      assertEquals(Double.valueOf(34.0), productiveUnitsMap.get("5962"));
      assertEquals(Double.valueOf(2.0), productiveUnitsMap.get("5010"));
      assertEquals(Double.valueOf(34.0), productiveUnitsMap.get("5012"));
      assertEquals(Double.valueOf(7.0), productiveUnitsMap.get("101"));
      assertEquals(Double.valueOf(5.0), productiveUnitsMap.get("6930"));
  
      HashMap<String, Double> localProductiveUnitsMap = new HashMap<>();
      for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
        localProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
      }
      logger.debug("localProductiveUnitsMap: " + localProductiveUnitsMap);
      assertEquals(Double.valueOf(12.8), localProductiveUnitsMap.get("104"));
      assertEquals(Double.valueOf(5.0), localProductiveUnitsMap.get("108"));
      assertEquals(Double.valueOf(2.0), localProductiveUnitsMap.get("109"));
      assertEquals(Double.valueOf(3423.0), localProductiveUnitsMap.get("5392"));
      assertEquals(Double.valueOf(34.0), localProductiveUnitsMap.get("5962"));
      assertEquals(Double.valueOf(2.0), localProductiveUnitsMap.get("5010"));
      assertEquals(Double.valueOf(34.0), localProductiveUnitsMap.get("5012"));
      assertEquals(Double.valueOf(7.0), productiveUnitsMap.get("101"));
  
      HashMap<String, Double> craProductiveUnitsMap = new HashMap<>();
      for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
        craProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
      }
      logger.debug("craProductiveUnitsMap: " + craProductiveUnitsMap);
      assertEquals(0, fo.getCraProductiveUnitCapacities().size());
      
      assertEquals(6, fo.getCropItems().size());
      for (InventoryItem in : fo.getCropItems()) {
        logger.debug(in.toString());
      }
  
      
      FarmingYear fy = scenario.getFarmingYear();
      assertEquals(numberOfYearsFarmed, fy.getFarmYears());
      assertFalse(fy.getIsSoleProprietor());
      assertFalse(fy.getIsCoopMember());
      assertFalse(fy.getIsCompletedProdCycle());
      assertTrue(fy.getIsDisaster());
      assertTrue(fy.getIsCorporateShareholder());
      assertTrue(fy.getIsLastYearFarming());
      assertEquals("BC", fy.getProvinceOfMainFarmstead());
      assertEquals(formatter.format(currentDate), formatter.format(fy.getPostMarkDate()));
      assertEquals(formatter.format(currentDate), formatter.format(fy.getCraStatementAReceivedDate()));
      
      fo = fy.getFarmingOperations().get(0);
      
      assertEquals(Double.valueOf(100.00), fo.getGrossIncome());
      assertEquals(Double.valueOf(40.99), fo.getFarmingExpenses());
      assertEquals(Double.valueOf(230.1), fo.getNetIncomeBeforeAdj());
      assertEquals(Double.valueOf(200.00), fo.getNetIncomeAfterAdj());
      assertEquals(Double.valueOf(1.00), fo.getInventoryAdjustments());
      assertEquals(Double.valueOf(0.5), fo.getPartnershipPercent());
      assertEquals("2", fo.getAccountingCode());
      assertEquals(startDate, fo.getFiscalYearStart());
      assertEquals(endDate, fo.getFiscalYearEnd());
      
      List<FarmingOperationPartner> farmingOperationPartners = fo.getFarmingOperationPartners();
      assertEquals(2, farmingOperationPartners.size());
      for (FarmingOperationPartner fop : farmingOperationPartners) {
        logger.debug("FarmingOperationPartner = " + fop.toString());
      }
      
      
      // ------------ Run Benefit Triage ---------------------------------------------------------
      
      Integer triageImportVersionId = processor.getTriageImportVersionId();
      assertNotNull(triageImportVersionId);
      
      BenefitTriageCalculationItem triageItem = new BenefitTriageCalculationItem();
      triageItem.setCraProgramYearVersionId(statementAScenarioMetadata.getProgramYearVersionId());
      triageItem.setCraScenarioId(statementAScenarioMetadata.getScenarioId());
      triageItem.setCraScenarioNumber(statementAScenarioNumber);
      triageItem.setParticipantPin(participantPin);
      triageItem.setProgramYear(programYear);
      
      List<BenefitTriageCalculationItem> triageItems = Collections.singletonList(triageItem);
      
      BenefitTriageService triageService = ServiceFactory.getBenefitTriageService();
      BenefitTriageResults benefitTriageResults = null;
      try {
        benefitTriageResults = triageService.processBenefitTriageItems(conn, triageImportVersionId, triageItems, user);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(benefitTriageResults);
      assertNull(benefitTriageResults.getUnexpectedError());
      
      
      // ------------ Benefit Triage Results ---------------------------------------------------------
      
      List<BenefitTriageItemResult> triageItemResults = benefitTriageResults.getTriageItemResults();
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult triageItemResult = triageItemResults.get(0);
      List<String> triageErrorMessages = triageItemResult.getErrorMessages();
      List<String> triageFailMessages = triageItemResult.getFailMessages();
      
      assertNotNull(triageErrorMessages);
      assertNotNull(triageFailMessages);
      logErrorMessages(triageErrorMessages);
      logFailMessages(triageFailMessages);
      assertEquals(0, triageErrorMessages.size());
      assertEquals(0, triageFailMessages.size());
      
      assertEquals("JOHNNY APPLESEED", triageItemResult.getClientName());
      assertEquals(Double.valueOf(0), triageItemResult.getEstimatedBenefit());
      assertEquals(Boolean.FALSE, triageItemResult.getIsPaymentFile());
      assertEquals(participantPin, triageItemResult.getParticipantPin());
      assertEquals(2024, triageItemResult.getProgramYear());
      assertEquals("Completed", triageItemResult.getScenarioStateCodeDesc());
      assertTrue(triageItemResult.isZeroPass());
      
      ImportDAO importDao = new ImportDAO();
      ImportVersion importVersion = null;
      try {
        importVersion = importDao.getImportVersion(conn, triageImportVersionId);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(importVersion);
      assertEquals(ImportClassCodes.TRIAGE, importVersion.getImportClassCode());
      assertEquals(ImportStateCodes.IMPORT_COMPLETE, importVersion.getImportStateCode());
      
      
      // ------------ Triage Scenario ----------------------------------------------------------------
      
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);

      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      assertEquals(0.0, triageScenario.getBenefit().getTotalBenefit());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      Double triageReferenceMarginVariance = triageTestResults.getMarginTest().getAdjustedReferenceMarginVariance();
      Double triageReferenceMarginVarianceLimit = triageTestResults.getMarginTest().getAdjustedReferenceMarginVarianceLimit();
      assertTrue(triageReferenceMarginVariance >= triageReferenceMarginVarianceLimit);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.TRUE, triageStructuralChangeTestPassed);


      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(1, verifiedFinalScenarios.size());
      
      ScenarioMetaData verifiedFinalScenarioMetaData = verifiedFinalScenarios.get(0);
      Integer verifiedFinalScenarioNumber = verifiedFinalScenarioMetaData.getScenarioNumber();
      assertNotNull(verifiedFinalScenarioNumber);
      Scenario verifiedFinalScenario = null;
      try {
        verifiedFinalScenario = calculatorService.loadScenario(participantPin, programYear, verifiedFinalScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(verifiedFinalScenario);
      assertNotNull(verifiedFinalScenario.getClient());
      assertEquals(participantPin, verifiedFinalScenario.getClient().getParticipantPin());
      assertEquals(programYear, verifiedFinalScenario.getYear());
      assertEquals(ScenarioTypeCodes.USER, verifiedFinalScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.FINAL, verifiedFinalScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.VERIFIED, verifiedFinalScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, verifiedFinalScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, verifiedFinalScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(verifiedFinalScenario.getBenefit());
      assertNotNull(verifiedFinalScenario.getBenefit().getTotalBenefit());
      assertEquals(0.0, verifiedFinalScenario.getBenefit().getTotalBenefit());
      
      ReasonabilityTestResults verifiedTestResults = verifiedFinalScenario.getReasonabilityTestResults();
      assertNotNull(verifiedTestResults);
      assertNotNull(verifiedTestResults.getMarginTest());
      assertEquals(triageReferenceMarginTestPassed, verifiedTestResults.getMarginTest().getWithinLimitOfReferenceMargin());
      
      assertNotNull(verifiedTestResults.getStructuralChangeTest());
      assertEquals(triageStructuralChangeTestPassed, verifiedTestResults.getStructuralChangeTest().getResult());
      
      Double triageReferenceMargin = triageScenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
      Double pyTriageProductionMargAccrAdjs = triageScenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
      Double pyMinus1TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 1).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus2TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 2).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus3TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 3).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus4TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 4).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus5TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 5).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      
      Double verifiedReferenceMargin = verifiedFinalScenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
      Double pyVerifiedProductionMargAccrAdjs = verifiedFinalScenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
      Double pyMinus1VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 1).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus2VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 2).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus3VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 3).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus4VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 4).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus5VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 5).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      
      assertEquals(triageReferenceMargin, verifiedReferenceMargin);
      assertEquals(pyTriageProductionMargAccrAdjs, pyVerifiedProductionMargAccrAdjs);
      assertEquals(pyMinus1TriageProductionMargAftStrChangs, pyMinus1VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus2TriageProductionMargAftStrChangs, pyMinus2VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus3TriageProductionMargAftStrChangs, pyMinus3VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus4TriageProductionMargAftStrChangs, pyMinus4VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus5TriageProductionMargAftStrChangs, pyMinus5VerifiedProductionMargAftStrChangs);
      
      ImportService importService = ServiceFactory.getImportService();
      List<String> importClassCodes = Collections.singletonList(ImportClassCodes.XSTATE);
      List<ImportSearchResult> stateTransfers = null;
      try {
        stateTransfers = importService.searchImports(importClassCodes, DateUtils.todayAtStartOfDay());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(stateTransfers);
      assertTrue(stateTransfers.size() > 0);
      
      // State Transfers are ordered latest first.
      // Two should have been created so index 1 should be the first of those.
      {
        ImportSearchResult stateTransfer = stateTransfers.get(2);
        assertNotNull(stateTransfer);
        assertTrue(StringUtils.isOneOf(stateTransfer.getStateCode(),
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            ImportStateCodes.IMPORT_COMPLETE));
        assertEquals("State Change Transfer for " + programYear
            + " PIN: " + participantPin
            + ", State: Received"
            + ", Category: CHEF Statement A",
            stateTransfer.getDescription());
      }
      {
        ImportSearchResult stateTransfer = stateTransfers.get(1);
        assertNotNull(stateTransfer);
        assertTrue(StringUtils.isOneOf(stateTransfer.getStateCode(),
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            ImportStateCodes.IMPORT_COMPLETE));
        assertEquals("State Change Transfer for " + programYear
            + " PIN: " + participantPin
            + ", State: In Progress"
            + ", Category: Final",
            stateTransfer.getDescription());
      }
      {
        ImportSearchResult stateTransfer = stateTransfers.get(0);
        assertNotNull(stateTransfer);
        assertTrue(StringUtils.isOneOf(stateTransfer.getStateCode(),
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            ImportStateCodes.IMPORT_COMPLETE));
        assertEquals("State Change Transfer for " + programYear
            + " PIN: " + participantPin
            + ", State: Verified"
            + ", Category: Final",
            stateTransfer.getDescription());
      }
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 3);
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      deleteUserScenarios(submissionGuid, programYearMetadata);
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }
  
  @Test
  public void farmDetailsHappyPath() {

    String submissionGuid = "a70334f9-f811-42de-a8a7-7ad6b8152626";
    Integer participantPin = 31415950;
    
    Date startDate = null;
    Date endDate = null;
    try {
      startDate = formatter.parse("2024-01-01");
      endDate = formatter.parse("2024-12-31");
    } catch (ParseException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    Integer programYear = DateUtils.getYearFromDate(endDate);


    // Get the list of scenarios for the program year
    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the USER scenarios linked to this submission if any exist, from a
    // previous test run.
    // Update scenarioSubmissionId to null for non-USER scenarios.
    TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
    TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
    deleteUserScenarios(submissionGuid, programYearMetadata);
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {
  
      // Set up the Statement A CHEFS Form submission data (not getting it from
      // CHEFS).
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      data.setEmail("APPLES@FARM.CA");
      data.setCorporationName("JOHNNY APPLESEED");
      data.setFirstNameCorporateContact("JOHNNY");
      data.setLastNameCorporateContact("APPLESEED");
      data.setAddress("1111 GRAPE LANE");
      data.setPostalCode("V5R6T6");
      data.setTownCity("PENTICTON2");
      data.setProvince("BC");
      data.setTelephone("(604) 555-5555");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CO_OPERATIVE, ChefsFarmTypeCodes.CO_OPERATIVE));
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setBusinessTaxNumber("9990 12346");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setAuthorizeThirdParty("yes");
      data.setAuthorizeThirdParty("yes");
      data.setThirdPartyFirstName("PAUL");
      data.setThirdPartyLastName("BUNYAN");
      data.setThirdPartyBusinessName("LUMBER INC");
      data.setThirdPartyAddress("345 BUSINESS STREET");
      data.setThirdPartyTownCity("KELOWNA");
      data.setThirdPartyProvince("BC");
      data.setThirdPartyPostalCode("F3E 3E4");
      data.setThirdPartyTelephone("(604) 123-5699");
      data.setThirdPartyFax("(604) 777-7777");
      data.setThirdPartyEmail("PAUL@LUMBER.INC");
  
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      List<StatementAPartner> statementAPartners = new ArrayList<>();
      StatementAPartner p = new StatementAPartner();
      p.setPin(31415926);
      p.setPercent(10.2);
      statementAPartners.add(p);
      
      p = new StatementAPartner();
      p.setPin(31415927);
      p.setPercent(5.0);
      statementAPartners.add(p);
      data.setPartnershipGrid(statementAPartners);
      data.setDoYouHaveMultipleOperations("no");
      
      data.setCopyOfCOB("no");
      data.setBusinessStructure("sole proprietorship");
      data.setWas2024FinalYearOfFarming("no");
      data.setNumberOfYearsFarmed(12);
      data.setProvinceTerritoryOfMainFarmstead("BC");
      data.setCompletedProductionCycle("yes");
      data.setAccountingMethod("cash");
      data.setTotalAdjustments(1.0);
      data.setTotalIncome(190.00);
      data.setTotalExpenses(40.99);
      data.setNetIncomeBeforeAdjustments(230.1);
      data.setNetIncomeAfterAdjustments(200.00);
  
      data.setLivestockFarmed(Arrays.asList("cattle", "customFeed", "swine", "otherLivestock"));
      data.setCropsFarmed(Arrays.asList("berries", "grainsOilseeds", "treefruitsGrapes", "vegetables"));
      
      List<IncomeExpenseGrid> allowableIncomes = new ArrayList<>();
      IncomeExpenseGrid income = new IncomeExpenseGrid();
      income.setCategory(new LabelValue("Cherries (sweet; sour) - 92", "92"));
      income.setAmount(10.0);
      allowableIncomes.add(income);
      data.setAllowableIncomeGrid(allowableIncomes);
      
      List<IncomeExpenseGrid> allowableExpenses = new ArrayList<>();
      IncomeExpenseGrid allowableExpense = new IncomeExpenseGrid();
      allowableExpense.setCategory(new LabelValue("Fertilizers and soil supplements - 9662", "9662"));
      allowableExpense.setAmount(7.2);
      allowableExpenses.add(allowableExpense);
      data.setAllowableExpensesGrid(allowableExpenses);

  
      List<ReceivablesGrid> receivableGridList = new ArrayList<>();
      ReceivablesGrid receivableGrid = new ReceivablesGrid();
      receivableGrid.setIncomeSource(new LabelValue("402 - PI insurance", "402"));
      receivableGrid.setIncomeReceivedAfterYearEnd(1.05);
      receivableGridList.add(receivableGrid);
  
      data.setReceivablesGrid(receivableGridList);
  
      List<PayableGrid> payableGridList = new ArrayList<>();
      PayableGrid payableGrid = new PayableGrid();
      payableGrid.setSourceOfExpense(new LabelValue("9896 - Other Input (Specify)", "9896"));
      payableGrid.setSpecify("other input");
      payableGrid.setProgramExpensesNotPaidByYearEnd(42.02);
      payableGridList.add(payableGrid);
  
      data.setExpensesGrid(payableGridList);
  
      List<InputGrid> inputGridList = new ArrayList<>();
      InputGrid inputGrid = new InputGrid();
      inputGrid.setInput(new LabelValue("9896 - Other Input (Specify)", "9896"));
      inputGrid.setSpecify("other input");
      inputGrid.setAmountRemainingAtYearEnd(44.02);
      inputGridList.add(inputGrid);
  
      data.setInputGrid(inputGridList);
  
      List<LivestockGrid> otherGridList = new ArrayList<>();
      LivestockGrid otherGrid = new LivestockGrid();
      otherGrid.setCommodity(new LabelValue("7616 - Leaf Cutter Bees", "7616"));
      otherGrid.setEndingFmv(7.0);
      otherGrid.setEndingInventory(7.0);
      otherGrid.setQuantitySold(7.0);
      otherGridList.add(otherGrid);
  
      data.setOtherGrid(otherGridList);
  
      List<LivestockGrid> swineGridList = new ArrayList<>();
      LivestockGrid swineGrid = new LivestockGrid();
      swineGrid.setCommodity(new LabelValue("4001 - Livestock Inventory Default", "4001"));
      swineGrid.setEndingFmv(9.0);
      swineGrid.setEndingInventory(43.0);
      swineGrid.setQuantitySold(3.0);
      swineGridList.add(swineGrid);
  
      data.setSwineGrid(swineGridList);
  
      List<OtherPucGrid> otherPucGridList = new ArrayList<>();
      OtherPucGrid otherPucGrid = new OtherPucGrid();
      otherPucGrid.setSelectOtherLivestock(new LabelValue("101 - Bison", "101"));
      otherPucGrid.setOtherLivestockNumber(7);
      otherPucGridList.add(otherPucGrid);
      data.setOpdGrid(otherPucGridList);
      
      data.setTaxesProgramYear("no");
  
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
      // Process the submission data
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNull(task);
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData statementAScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.CHEF_STA, ScenarioTypeCodes.CHEF);
      Integer statementAScenarioNumber = statementAScenarioMetadata.getScenarioNumber();
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, statementAScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
  
      assertNotNull(scenario);
      assertNotNull(scenario.getScenarioId());
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(statementAScenarioNumber, scenario.getScenarioNumber());
      assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
      assertEquals(ScenarioStateCodes.RECEIVED, scenario.getScenarioStateCode());
      assertEquals(ScenarioCategoryCodes.CHEF_STA, scenario.getScenarioCategoryCode());
      assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());
      Date currentDate  = new Date();
      assertEquals(formatter.format(currentDate), formatter.format(scenario.getLocalStatementAReceivedDate()));
      assertEquals(formatter.format(currentDate), formatter.format(scenario.getLocalSupplementalReceivedDate()));
      
      FarmingYear fy = scenario.getFarmingYear();
      assertEquals(Integer.valueOf(12), fy.getFarmYears());
      assertTrue(fy.getIsSoleProprietor());
      assertTrue(fy.getIsCoopMember());
      assertFalse(fy.getIsCorporateShareholder());
      assertFalse(fy.getIsLastYearFarming());
      assertEquals("BC", fy.getProvinceOfMainFarmstead());
      assertEquals(formatter.format(currentDate), formatter.format(fy.getPostMarkDate()));
      assertEquals(formatter.format(currentDate), formatter.format(fy.getCraStatementAReceivedDate()));
  
      FarmingOperation fo = scenario.getFarmingYear().getFarmingOperations().get(0);
      
      assertEquals(Double.valueOf(190.00), fo.getGrossIncome());
      assertEquals(Double.valueOf(40.99), fo.getFarmingExpenses());
      assertEquals(Double.valueOf(230.1), fo.getNetIncomeBeforeAdj());
      assertEquals(Double.valueOf(200.00), fo.getNetIncomeAfterAdj());
      assertEquals(Double.valueOf(1.00), fo.getInventoryAdjustments());
      assertEquals("2", fo.getAccountingCode());
      assertEquals(startDate, fo.getFiscalYearStart());
      assertEquals(endDate, fo.getFiscalYearEnd());
  
      HashMap<String, Double> craProductiveUnitsMap = new HashMap<>();
      for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
        craProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
      }
      logger.debug("craProductiveUnitsMap: " + craProductiveUnitsMap);
      assertEquals(0, fo.getCraProductiveUnitCapacities().size());
      
      
      // ------------ Run Benefit Triage ---------------------------------------------------------
      
      Integer triageImportVersionId = processor.getTriageImportVersionId();
      assertNotNull(triageImportVersionId);
      
      BenefitTriageCalculationItem triageItem = new BenefitTriageCalculationItem();
      triageItem.setCraProgramYearVersionId(statementAScenarioMetadata.getProgramYearVersionId());
      triageItem.setCraScenarioId(statementAScenarioMetadata.getScenarioId());
      triageItem.setCraScenarioNumber(statementAScenarioNumber);
      triageItem.setParticipantPin(participantPin);
      triageItem.setProgramYear(programYear);
      
      List<BenefitTriageCalculationItem> triageItems = Collections.singletonList(triageItem);
      
      BenefitTriageService triageService = ServiceFactory.getBenefitTriageService();
      BenefitTriageResults benefitTriageResults = null;
      try {
        benefitTriageResults = triageService.processBenefitTriageItems(conn, triageImportVersionId, triageItems, user);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(benefitTriageResults);
      assertNull(benefitTriageResults.getUnexpectedError());
      
      
      // ------------ Benefit Triage Results ---------------------------------------------------------
      
      List<BenefitTriageItemResult> triageItemResults = benefitTriageResults.getTriageItemResults();
      assertNotNull(triageItemResults);
      assertEquals(1, triageItemResults.size());
      
      BenefitTriageItemResult triageItemResult = triageItemResults.get(0);
      List<String> triageErrorMessages = triageItemResult.getErrorMessages();
      List<String> triageFailMessages = triageItemResult.getFailMessages();
      
      assertNotNull(triageErrorMessages);
      assertNotNull(triageFailMessages);
      logErrorMessages(triageErrorMessages);
      logFailMessages(triageFailMessages);
      assertEquals(0, triageErrorMessages.size());
      assertEquals(0, triageFailMessages.size());
      
      assertEquals("JOHNNY APPLESEED", triageItemResult.getClientName());
      assertEquals(Double.valueOf(0), triageItemResult.getEstimatedBenefit());
      assertEquals(Boolean.FALSE, triageItemResult.getIsPaymentFile());
      assertEquals(participantPin, triageItemResult.getParticipantPin());
      assertEquals(2024, triageItemResult.getProgramYear());
      assertEquals("Completed", triageItemResult.getScenarioStateCodeDesc());
      assertTrue(triageItemResult.isZeroPass());
      
      ImportDAO importDao = new ImportDAO();
      ImportVersion importVersion = null;
      try {
        importVersion = importDao.getImportVersion(conn, triageImportVersionId);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(importVersion);
      assertEquals(ImportClassCodes.TRIAGE, importVersion.getImportClassCode());
      assertEquals(ImportStateCodes.IMPORT_COMPLETE, importVersion.getImportStateCode());
      
      
      // ------------ Triage Scenario ----------------------------------------------------------------
      
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);

      List<ScenarioMetaData> triageScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.TRIAGE, ScenarioTypeCodes.TRIAGE);
      assertNotNull(triageScenarios);
      assertEquals(1, triageScenarios.size());
      
      ScenarioMetaData triageScenarioMetaData = triageScenarios.get(0);
      Integer triageScenarioNumber = triageScenarioMetaData.getScenarioNumber();
      assertNotNull(triageScenarioNumber);
      Scenario triageScenario = null;
      try {
        triageScenario = calculatorService.loadScenario(participantPin, programYear, triageScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(triageScenario);
      assertNotNull(triageScenario.getClient());
      assertEquals(participantPin, triageScenario.getClient().getParticipantPin());
      assertEquals(programYear, triageScenario.getYear());
      assertEquals(ScenarioTypeCodes.TRIAGE, triageScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.TRIAGE, triageScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.COMPLETED, triageScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, triageScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(triageScenario.getBenefit());
      assertNotNull(triageScenario.getBenefit().getTotalBenefit());
      assertEquals(0.0, triageScenario.getBenefit().getTotalBenefit());
      
      ReasonabilityTestResults triageTestResults = triageScenario.getReasonabilityTestResults();
      assertNotNull(triageTestResults);
      assertNotNull(triageTestResults.getMarginTest());
      Boolean triageReferenceMarginTestPassed = triageTestResults.getMarginTest().getWithinLimitOfReferenceMargin();
      assertEquals(Boolean.FALSE, triageReferenceMarginTestPassed);
      Double triageReferenceMarginVariance = triageTestResults.getMarginTest().getAdjustedReferenceMarginVariance();
      Double triageReferenceMarginVarianceLimit = triageTestResults.getMarginTest().getAdjustedReferenceMarginVarianceLimit();
      assertTrue(triageReferenceMarginVariance >= triageReferenceMarginVarianceLimit);
      
      assertNotNull(triageTestResults.getStructuralChangeTest());
      Boolean triageStructuralChangeTestPassed = triageTestResults.getStructuralChangeTest().getResult();
      assertEquals(Boolean.TRUE, triageStructuralChangeTestPassed);


      // ------------ Verified Final ----------------------------------------------------------------
      List<ScenarioMetaData> verifiedFinalScenarios =
          ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.FINAL, ScenarioTypeCodes.USER);
      assertNotNull(verifiedFinalScenarios);
      assertEquals(1, verifiedFinalScenarios.size());
      
      ScenarioMetaData verifiedFinalScenarioMetaData = verifiedFinalScenarios.get(0);
      Integer verifiedFinalScenarioNumber = verifiedFinalScenarioMetaData.getScenarioNumber();
      assertNotNull(verifiedFinalScenarioNumber);
      Scenario verifiedFinalScenario = null;
      try {
        verifiedFinalScenario = calculatorService.loadScenario(participantPin, programYear, verifiedFinalScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(verifiedFinalScenario);
      assertNotNull(verifiedFinalScenario.getClient());
      assertEquals(participantPin, verifiedFinalScenario.getClient().getParticipantPin());
      assertEquals(programYear, verifiedFinalScenario.getYear());
      assertEquals(ScenarioTypeCodes.USER, verifiedFinalScenario.getScenarioTypeCode());
      assertEquals(ScenarioCategoryCodes.FINAL, verifiedFinalScenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.VERIFIED, verifiedFinalScenario.getScenarioStateCode());
      assertEquals(StructuralChangeCodes.RATIO, verifiedFinalScenario.getBenefit().getStructuralChangeMethodCode());
      assertEquals(StructuralChangeCodes.RATIO, verifiedFinalScenario.getBenefit().getExpenseStructuralChangeMethodCode());
      assertNotNull(verifiedFinalScenario.getBenefit());
      assertNotNull(verifiedFinalScenario.getBenefit().getTotalBenefit());
      assertEquals(0.0, verifiedFinalScenario.getBenefit().getTotalBenefit());
      
      ReasonabilityTestResults verifiedTestResults = verifiedFinalScenario.getReasonabilityTestResults();
      assertNotNull(verifiedTestResults);
      assertNotNull(verifiedTestResults.getMarginTest());
      assertEquals(triageReferenceMarginTestPassed, verifiedTestResults.getMarginTest().getWithinLimitOfReferenceMargin());
      
      assertNotNull(verifiedTestResults.getStructuralChangeTest());
      assertEquals(triageStructuralChangeTestPassed, verifiedTestResults.getStructuralChangeTest().getResult());
      
      Double triageReferenceMargin = triageScenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
      Double pyTriageProductionMargAccrAdjs = triageScenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
      Double pyMinus1TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 1).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus2TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 2).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus3TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 3).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus4TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 4).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus5TriageProductionMargAftStrChangs = triageScenario.getReferenceScenarioByYear(programYear - 5).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      
      Double verifiedReferenceMargin = verifiedFinalScenario.getFarmingYear().getBenefit().getAdjustedReferenceMargin();
      Double pyVerifiedProductionMargAccrAdjs = verifiedFinalScenario.getFarmingYear().getMarginTotal().getProductionMargAccrAdjs();
      Double pyMinus1VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 1).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus2VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 2).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus3VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 3).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus4VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 4).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      Double pyMinus5VerifiedProductionMargAftStrChangs = verifiedFinalScenario.getReferenceScenarioByYear(programYear - 5).getFarmingYear().getMarginTotal().getProductionMargAftStrChangs();
      
      assertEquals(triageReferenceMargin, verifiedReferenceMargin);
      assertEquals(pyTriageProductionMargAccrAdjs, pyVerifiedProductionMargAccrAdjs);
      assertEquals(pyMinus1TriageProductionMargAftStrChangs, pyMinus1VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus2TriageProductionMargAftStrChangs, pyMinus2VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus3TriageProductionMargAftStrChangs, pyMinus3VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus4TriageProductionMargAftStrChangs, pyMinus4VerifiedProductionMargAftStrChangs);
      assertEquals(pyMinus5TriageProductionMargAftStrChangs, pyMinus5VerifiedProductionMargAftStrChangs);
      
      ImportService importService = ServiceFactory.getImportService();
      List<String> importClassCodes = Collections.singletonList(ImportClassCodes.XSTATE);
      List<ImportSearchResult> stateTransfers = null;
      try {
        stateTransfers = importService.searchImports(importClassCodes, DateUtils.todayAtStartOfDay());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(stateTransfers);
      assertTrue(stateTransfers.size() > 0);
      
      // State Transfers are ordered latest first.
      // Two should have been created so index 1 should be the first of those.
      {
        ImportSearchResult stateTransfer = stateTransfers.get(2);
        assertNotNull(stateTransfer);
        assertTrue(StringUtils.isOneOf(stateTransfer.getStateCode(),
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            ImportStateCodes.IMPORT_COMPLETE));
        assertEquals("State Change Transfer for " + programYear
            + " PIN: " + participantPin
            + ", State: Received"
            + ", Category: CHEF Statement A",
            stateTransfer.getDescription());
      }
      {
        ImportSearchResult stateTransfer = stateTransfers.get(1);
        assertNotNull(stateTransfer);
        assertTrue(StringUtils.isOneOf(stateTransfer.getStateCode(),
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            ImportStateCodes.IMPORT_COMPLETE));
        assertEquals("State Change Transfer for " + programYear
            + " PIN: " + participantPin
            + ", State: In Progress"
            + ", Category: Final",
            stateTransfer.getDescription());
      }
      {
        ImportSearchResult stateTransfer = stateTransfers.get(0);
        assertNotNull(stateTransfer);
        assertTrue(StringUtils.isOneOf(stateTransfer.getStateCode(),
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            ImportStateCodes.IMPORT_COMPLETE));
        assertEquals("State Change Transfer for " + programYear
            + " PIN: " + participantPin
            + ", State: Verified"
            + ", Category: Final",
            stateTransfer.getDescription());
      }
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 3);
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      deleteUserScenarios(submissionGuid, programYearMetadata);
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }

  @Test
  public void fixValidationError() {

    String submissionGuid = "cee6a21e-e877-429e-b806-4a73795e0420";
    Integer participantPin = 31415935;
    
    Date startDate = null;
    Date endDate = null;
    try {
      startDate = formatter.parse("2024-01-01");
      endDate = formatter.parse("2024-12-31");
    } catch (ParseException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    Integer programYear = DateUtils.getYearFromDate(endDate);

    
    // Get the list of scenarios for the program year
    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the USER scenarios linked to this submission if any exist, from a
    // previous test run.
    // Update scenarioSubmissionId to null for non-USER scenarios.
    TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
    TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
    deleteUserScenarios(submissionGuid, programYearMetadata);
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    deleteSubmissionsFromFarm(submissionGuid);

    try {

      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(submissionGuid);
  
      data.setCorporationName("APPLES R US");
      data.setEmail("APPLES@FARM.CA");
      data.setFirstNameCorporateContact("JOHNNY");
      data.setLastNameCorporateContact("APPLESEED");
      data.setAddress("1111 GRAPE LANE");
      data.setPostalCode("V5R6T6");
      data.setTownCity("PENTICTON2");
      data.setProvince("BC");
      data.setTelephone("(250) 555-5555");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE, ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE));
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setSinNumber("123456789");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setAuthorizeThirdParty("no");
  
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
      
      data.setCompletedProductionCycle("no");
      data.setCopyOfCOB("no");
      data.setBusinessStructure("partnership");
      data.setWas2024FinalYearOfFarming("no");
      data.setNumberOfYearsFarmed(0);
      data.setProvinceTerritoryOfMainFarmstead("BC");
      data.setAccountingMethod("cash");
  
      data.setCropsFarmed(Arrays.asList("berries"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null)
      ));
  
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource validationTask = null;
      try {
        processor.loadSubmissionsFromDatabase();
        validationTask = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(validationTask);
      assertNotNull(validationTask.getAccountId());
      assertEquals("2024 Statement A " + participantPin, validationTask.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"987654321\".\n" + "\n"
          + "Corporation Name: APPLES R US\n" + "Telephone: (250) 555-5555\n"
          + "Email: APPLES@FARM.CA\n", validationTask.getDescription());
  
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      // Correct the SIN Number
      data.setSinNumber("987654321");
  
      try {
        validationTask = completeAndGetTask(crmConfig.getValidationErrorUrl(), validationTask.getActivityId());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(validationTask);
      assertNotNull(validationTask.getAccountId());
      assertEquals("2024 Statement A " + participantPin, validationTask.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), validationTask.getStatusCode());
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNull(task);
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 1);
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      deleteUserScenarios(submissionGuid, programYearMetadata);
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }

  @Test
  public void crud() {

    String[] submissionGuidArray = { "00000000-STA0-0001-0000-000000000000", "00000000-STA0-0001-0000-000000000001",
        "00000000-STA0-0001-0000-000000000002" };
    List<String> submissionGuidList = Arrays.asList(submissionGuidArray);

    deleteSubmissionsFromFarm(submissionGuidArray);

    // Confirm that the submissions now do not exist.
    {
      Map<String, ChefsSubmission> submissionRecordMap = null;
      try {
        submissionRecordMap = chefsDatabaseDao.readSubmissionsByGuid(conn, submissionGuidList);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRecordMap);
      assertTrue(submissionRecordMap.isEmpty());
    }

    // Create the submissions

    List<ChefsSubmission> submissionRecords = new ArrayList<>();
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[0]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.STA);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.STA);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.STA);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.DUPLICATE);
      submissionRec.setValidationTaskGuid(null);
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }

    try {
      chefsDatabaseDao.createSubmissions(conn, submissionRecords, user);
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

    // Read the created submissions

    Map<String, ChefsSubmission> submissionRecordMap = null;
    try {
      submissionRecordMap = chefsDatabaseDao.readSubmissionsByGuid(conn, submissionGuidList);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRecordMap);

    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[0]);
      assertEquals(submissionGuidArray[0], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555001", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666001", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }

    // Update the submissions
    try {
      {
        ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[0]);
        submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
        submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666000");
        chefsDatabaseDao.updateSubmission(conn, submissionRec, user);
        conn.commit();
      }
      {
        ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
        submissionRec.setValidationTaskGuid(null);
        submissionRec.setMainTaskGuid(null);
        chefsDatabaseDao.updateSubmission(conn, submissionRec, user);
        conn.commit();
      }
      {
        ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
        submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.CANCELLED);
        submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555002");
        chefsDatabaseDao.updateSubmission(conn, submissionRec, user);
        conn.commit();
      }
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

    // Read the updated submissions

    try {
      submissionRecordMap = chefsDatabaseDao.readSubmissionsByGuid(conn, submissionGuidList);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRecordMap);

    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[0]);
      assertEquals(submissionGuidArray[0], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666000", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.CANCELLED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555002", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }

    deleteSubmissionsFromFarm(submissionGuidArray);
  }

  @Test
  public void readStatementASubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.STA);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.STA, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
  }


  @Test
  public void duplicateSubmission() {

    Integer programYear = 2024;
    Integer participantPin = 4375663;
    
    String existingSubmissionGuid = "e9a5122d-e236-4c64-902a-f9dd2ae7b7f2";
    String duplicateSubmissionGuid = "c0d1bf63-d705-4c3d-8b51-867ae7f307c9";

    
    // Get the list of scenarios for the program year
    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the USER scenarios linked to this submission if any exist, from a
    // previous test run.
    // Update scenarioSubmissionId to null for non-USER scenarios.
    TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
    TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
    deleteUserScenarios(duplicateSubmissionGuid, programYearMetadata);
    deleteValidationErrorTasksBySubmissionGuid(duplicateSubmissionGuid);
    deleteSubmissionsFromFarm(duplicateSubmissionGuid);

    try {

      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      // Confirm that an Statement A CHEF Scenario exists and is linked to a CHEFS Statement A submission
      ScenarioMetaData statementAScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.CHEF_STA, ScenarioTypeCodes.CHEF);
      assertNotNull(statementAScenarioMetadata);
  
      // Set up the CHEFS Form submission data (not getting it from CHEFS).
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
      StatementASubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(duplicateSubmissionGuid);
  
      data.setCorporationName("WINDSET NORTH TRUST");
      data.setEmail("johnny@farm.ca");
      data.setFarmType(new LabelValue(ChefsFarmTypeCodes.CORPORATION, ChefsFarmTypeCodes.CORPORATION));
      
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setTrustNumber("T99999999");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      try {
        Date date = format.parse(programYear + "-12-31");
        data.setFiscalYearEnd(date);
      } catch (ParseException e) {
        e.printStackTrace();
        fail();
      }
      
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(duplicateSubmissionGuid);
      // Process the submission data
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(task);
  
      // Verify the task that was created in CRM by the submission processor
      assertNotNull(task.getAccountId());
      assertEquals("Duplicate form: " + programYear + " " + processor.getFormShortName() + " " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          processor.getFormUserType() + " " + processor.getFormLongName()
          + " form was submitted but has previous submissions for this PIN and program year:\n"
          + "\n"
          + "Form submissions of this type have been previously submitted for this PIN and program year: " + existingSubmissionGuid
          + "\n\n"
          + "Environment: DEV\n",
          task.getDescription());  
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, duplicateSubmissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(duplicateSubmissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      TestUtils.runQueuedImports(conn, 3);
      TestUtils.deleteBenefitTriageScenarios(participantPin, programYear, conn);
      TestUtils.deleteFinalScenarios(participantPin, programYear, conn);
      deleteUserScenarios(duplicateSubmissionGuid, programYearMetadata);
      deleteSubmissionsFromFarm(duplicateSubmissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(duplicateSubmissionGuid);
    }
    
  }


  @Disabled
  @Test
  public void deleteSubmissionsForPinAndProgramYear() {

    Integer participantPin = 31415950;
    Integer programYear = 2024;

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    String[] submissionGuids = programYearMetadata.stream()
    .filter(s -> s.getChefsFormSubmissionGuid() != null)
    .map(ScenarioMetaData::getChefsFormSubmissionGuid)
    .collect(Collectors.toList())
    .toArray(new String[0]);
    
    deleteSubmissionsFromFarm(submissionGuids);
  }

  @Disabled
  @Test
  public void processSubmissions() {

    ChefsService chefsService = ServiceFactory.getChefsService();
    try {
      chefsService.processSubmissions(conn);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }

  /**
   * This method is not really a unit test.
   * It is a convenient way to run the process for a
   * single submission after manually filling a CHEFS form.
   * It does not check the results.
   * 
   * So, the Test annotation should be commented out except
   * when you want to run it, since it's not actually a test.
   */
  @Disabled
  @Test
  public void processSpecificSubmission() {

    String submissionGuid = "ff77ce16-6535-4610-9270-89a1b94e4c75";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    StatementASubmissionDataResource data = submission.getData();
    assertNotNull(data);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
    processor.setUser(user);
    processor.setItemResourceMap(itemResourceMap);

    CrmTaskResource task = null;
    try {
      processor.loadSubmissionsFromDatabase();
      task = processor.processSubmission(submissionMetaData);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(task);

  }

  @Test
  @Disabled
  public void deleteScenarios() {

    Integer participantPin = 31415949;
    Integer programYear = 2024;

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    List<ScenarioMetaData> interimScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata,
        programYear, ScenarioCategoryCodes.CHEF_STA, ScenarioTypeCodes.CHEF);
    
    for (ScenarioMetaData scenarioMetaData : interimScenarioMetadataList) {
      if(scenarioMetaData.getScenarioNumber() != 1) {
        deleteUserScenario(scenarioMetaData.getScenarioId());
      }
    }

  }

  @Test
  public void duplicateProductiveUnits() {

    Integer participantPin = 638816783;
    Integer programYear = 2023;
    String submissionGuid = null;

    try {
      
      StatementASubmissionDataResource data = new StatementASubmissionDataResource();
  
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
      data.setCorporationName(null);
      data.setFirstNameCorporateContact(null);
      data.setLastNameCorporateContact(null);
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setTrustBusinessNumber(null);
      data.setTrustNumber(null);
      data.setSinNumber("123456789");
      data.setBandNumber(null);
      data.setAddress("1234 Home Road");
      data.setTownCity("Penticton");
      data.setProvince("BC");
      data.setPostalCode("T5Y 4R4");
      data.setTelephone("(648) 452-4357");
      data.setEmail("johnny@farmer.ca");
  
      data.setUnableToCompleteBecauseOfDisaster("no");
      data.setDoYouHaveMultipleOperations("no");
  
      data.setFiscalYearStart(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  
      data.setAuthorizeThirdParty("yes");
      data.setThirdPartyFirstName("Paul");
      data.setThirdPartyLastName("Bunyan");
      data.setThirdPartyBusinessName("Lumber Inc");
      data.setThirdPartyAddress("345 Business Street");
      data.setThirdPartyTownCity("Kelowna");
      data.setThirdPartyProvince("BC");
      data.setThirdPartyPostalCode("V4N 0C0");
      data.setThirdPartyTelephone("(604) 555-5555");
      data.setThirdPartyFax("(604) 125-9338");
      data.setThirdPartyEmail("Paul@lumber.inc");
  
      data.setCropsFarmed(Arrays.asList("berries", "grainsOilseeds", "treefruitsGrapes", "vegetables", "nurseriesGreenhouse", "nonEdibleHorticulture"));
  
      data.setBerryGrid(Arrays.asList(
        new CropGrid("5000 - Blackberries", "5000", 1.0, null, null),
        new CropGrid("5000 - Blackberries", "5000", 1.1, null, null)
      ));
  
      data.setTreeFruitGrid(Arrays.asList(
        new CropGrid("5014 - Grapes", "5014", 14.0, null, null),
        new CropGrid("5014 - Grapes", "5014", 14.4, null, null)
      ));
  
      data.setVegetableGrid(Arrays.asList(
        new CropGrid("6 - Borage", "6", 29.0, null, null),
        new CropGrid("6 - Borage", "6", 29.9, null, null)
      ));
  
      data.setGrainGrid(Arrays.asList(
        new GrainGrid("4784 - Hops", "4784", 144.0),
        new GrainGrid("4784 - Hops", "4784", 144.4)
      ));
  
      data.setNurseryGrid(Arrays.asList(
        new NurseryGrid("6930 - Maple Syrup", "6930", 354, null, null),
        new NurseryGrid("6930 - Maple Syrup", "6930", 355, null, null)
      ));
  
      data.setLivestockFarmed(Arrays.asList("cattle", "customFeed", "poultry", "swine", "otherLivestock"));
  
      data.setOpdGrid(Arrays.asList(
          new OtherPucGrid("100 - Alpaca", "100", 419),
          new OtherPucGrid("100 - Alpaca", "100", 420)
      ));
  
      data.setOnBehalfOfParticipant("no");
      data.setSignature(null);
      data.setHowDoYouKnowTheParticipant("online");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      data.setInternalMethod(null);
  
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
  
      SubmissionResource<StatementASubmissionDataResource> submission = new SubmissionResource<>();
  
      StatementASubmissionRequestDataResource<StatementASubmissionDataResource> request = new StatementASubmissionRequestDataResource<>();
      request.setDraft(false);
      request.setCreatedBy(user);
      request.setCreatedAt(new Date().toString());
      request.setUpdatedBy(user);
      request.setUpdatedAt(new Date().toString());
      request.setSubmission(submission);
  
      submission.setData(data);
      submissionMetaData.setSubmission(submission);
      submissionMetaData.setSubmissionGuid(null);
  
      // Statement A IDIR formId and formVersionId
      String formId = "48516d70-689e-42c2-8f7f-4111af257e12";
      String formVersionId = "ab63ba9c-8131-4729-8dc6-421e8b8b1664";
  
      String postSubmissionUrl = chefsConfig.postSubmissionUrl(formId, formVersionId);
      assertNotNull(postSubmissionUrl);
      try {
        submissionMetaData = chefsApiDao.postStatementASubmission(postSubmissionUrl, request);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      StatementASubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
  
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
      // Process the submission data
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
  
      CrmTaskResource validationTask = null;
      try {
        processor.loadSubmissionsFromDatabase();
        validationTask = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(validationTask);
      assertNotNull(validationTask.getAccountId());
      assertEquals(programYear + " Statement A " + participantPin, validationTask.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n"
          + "- The following productive unit codes have duplicates: 100, 5000, 6, 5014, 4784, 6930\n"
          + "\n" + "Corporation Name: \n"
          + "Telephone: (648) 452-4357\n" + "Email: johnny@farmer.ca\n", validationTask.getDescription());
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      validationTask = completeAndGetValidationErrorTask(validationTask);
  
      assertNotNull(validationTask);
      assertNotNull(validationTask.getAccountId());
      assertEquals(programYear + " Statement A " + participantPin, validationTask.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_NOT_STARTED), validationTask.getStatusCode());
      
    } finally {
      
      deleteSubmissions(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
  }
  
  @Test
  public void missingProductiveUnits() {
    
    Integer participantPin = 638816783;
    Integer programYear = 2023;
    String submissionGuid = null;
    
    try {

      StatementASubmissionDataResource data = new StatementASubmissionDataResource();

      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
      data.setCorporationName(null);
      data.setFirstNameCorporateContact(null);
      data.setLastNameCorporateContact(null);
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setTrustBusinessNumber(null);
      data.setTrustNumber(null);
      data.setSinNumber("123456789");
      data.setBandNumber(null);
      data.setAddress("1234 Home Road");
      data.setTownCity("Penticton");
      data.setProvince("BC");
      data.setPostalCode("T5Y 4R4");
      data.setTelephone("(648) 452-4357");
      data.setEmail("johnny@farmer.ca");
  
      data.setUnableToCompleteBecauseOfDisaster("no");
      data.setDoYouHaveMultipleOperations("no");
  
      data.setFiscalYearStart(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  
      data.setAuthorizeThirdParty("yes");
      data.setThirdPartyFirstName("Paul");
      data.setThirdPartyLastName("Bunyan");
      data.setThirdPartyBusinessName("Lumber Inc");
      data.setThirdPartyAddress("345 Business Street");
      data.setThirdPartyTownCity("Kelowna");
      data.setThirdPartyProvince("BC");
      data.setThirdPartyPostalCode("V4N 0C0");
      data.setThirdPartyTelephone("(604) 555-5555");
      data.setThirdPartyFax("(604) 125-9338");
      data.setThirdPartyEmail("Paul@lumber.inc");
  
      data.setOnBehalfOfParticipant("no");
      data.setSignature(null);
      data.setHowDoYouKnowTheParticipant("online");
      
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      data.setInternalMethod(null);
      
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      
      SubmissionResource<StatementASubmissionDataResource> submission = new SubmissionResource<>();
      
      StatementASubmissionRequestDataResource<StatementASubmissionDataResource> request = new StatementASubmissionRequestDataResource<>();
      request.setDraft(false);
      request.setCreatedBy(user);
      request.setCreatedAt(new Date().toString());
      request.setUpdatedBy(user);
      request.setUpdatedAt(new Date().toString());
      request.setSubmission(submission);
      
      submission.setData(data);
      submissionMetaData.setSubmission(submission);
      submissionMetaData.setSubmissionGuid(null);
      
      // Statement A IDIR formId and formVersionId
      String formId = "48516d70-689e-42c2-8f7f-4111af257e12";
      String formVersionId = "ab63ba9c-8131-4729-8dc6-421e8b8b1664";
  
      String postSubmissionUrl = chefsConfig.postSubmissionUrl(formId, formVersionId);
      assertNotNull(postSubmissionUrl);
      try {
        submissionMetaData = chefsApiDao.postStatementASubmission(postSubmissionUrl, request);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      
      StatementASubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
      
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
      
      // Process the submission data
      StatementASubmissionProcessor processor = new StatementASubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
      
      CrmTaskResource validationTask = null;
      try {
        processor.loadSubmissionsFromDatabase();
        validationTask = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(validationTask);
      assertNotNull(validationTask.getAccountId());
      assertEquals(programYear + " Statement A " + participantPin, validationTask.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
      assertEquals(formUserType + " Statement A form was submitted but has validation errors:\n" + "\n"
          + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n"
          + "- No productive units entered\n"
          + "\n" + "Corporation Name: \n"
          + "Telephone: (648) 452-4357\n" + "Email: johnny@farmer.ca\n", validationTask.getDescription());
      
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
      
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.STA, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
      validationTask = completeAndGetValidationErrorTask(validationTask);
      
      assertNotNull(validationTask);
      assertNotNull(validationTask.getAccountId());
      assertEquals(programYear + " Statement A " + participantPin, validationTask.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_NOT_STARTED), validationTask.getStatusCode());
      
    } finally {
      
      deleteSubmissions(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
    }
    
  }


  private SubmissionParentResource<StatementASubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<StatementASubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    StatementASubmissionDataResource data = new StatementASubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}
