/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import ca.bc.gov.srm.farm.chefs.processor.SupplementalSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.common.CropGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.GrainGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.InputGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.LivestockGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.NurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.OtherPucGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.PayableGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.ReceivablesGrid;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalSubmissionDataResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsSupplementalSubmissionTest extends ChefsSubmissionTest{

  private static Logger logger = LoggerFactory.getLogger(ChefsSupplementalSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.SUPP;

  public ChefsSupplementalSubmissionTest() {

    // Override default username (the unit test class name) because 
    // it is too long for the WHO_CREATED and WHEN_CREATED columns
    user = "ChefsSupplementalTest";
  }

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

    SubmissionWrapperResource<SupplementalSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, SupplementalSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "948dd6fa-6c13-48bc-ab9c-5849b9ed0eb2";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<SupplementalSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, SupplementalSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    SupplementalSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JONNY APPLESEED", data.getParticipantName());
    assertEquals("individual", data.getBusinessStructure());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityAgriInvestPin());
    assertNull(data.getTelephone());
    assertEquals("999999999", data.getSinNumber());
    assertEquals("DEV", data.getEnvironment());

    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertNull(data.getInternalMethod());
    assertEquals(Arrays.asList("cattle", "customFeed", "poultry", "swine", "otherLivestock"), data.getLivestockFarmed());
    assertEquals(Arrays.asList("berries", "grainsOilseeds", "treefruitsGrapes", "vegetables", "nurseriesGreenhouse", "nonEdibleHorticulture"),
        data.getCropsFarmed());

    GrainGrid grainGrid = new GrainGrid();
    grainGrid.setAcres(23.0);
    grainGrid.setGrade("AAA");
    grainGrid.setUnits(new LabelValue("Bushels", "4"));
    grainGrid.setCommodity(new LabelValue("5292 - Caraway seed, organic", "5292"));
    grainGrid.setIsIrrigated("yes");
    grainGrid.setEndingInventory(68.0);
    grainGrid.setEndingPricePerUnit(55.0);
    grainGrid.setQuantitySold(989.0);
    grainGrid.setQuantityProduced(3456.0);
    grainGrid.setQuantityPurchased(845.0);
    grainGrid.setQuantityUsedForFeed(66.0);
    grainGrid.setQuantityUsedForSeed(0.0);

    GrainGrid grainGrid2 = new GrainGrid();
    grainGrid2.setAcres(56756.0);
    grainGrid2.setGrade("");
    grainGrid2.setUnits(new LabelValue());
    grainGrid2.setCommodity(new LabelValue("5390 - Beans, cranberry, no. 3", "5390"));
    grainGrid2.setIsIrrigated("no");

    List<GrainGrid> expectedGrainGridList = new ArrayList<>();
    expectedGrainGridList.add(grainGrid);
    expectedGrainGridList.add(grainGrid2);

    assertEquals(2, data.getGrainGrid().size());
    assertArrayEquals(expectedGrainGridList.toArray(), data.getGrainGrid().toArray());

    CropGrid berryGrid = new CropGrid();
    berryGrid.setAcres(7.0);
    berryGrid.setUnits(new LabelValue("Pounds", "1"));
    berryGrid.setCommodity(new LabelValue("5000 - Blackberries", "5000"));
    berryGrid.setQuantitySold(8.0);
    berryGrid.setQuantityProduced(7.0);
    List<CropGrid> expectedBerryGridList = new ArrayList<>();
    expectedBerryGridList.add(berryGrid);

    berryGrid = new CropGrid();
    berryGrid.setAcres(5.0);
    berryGrid.setUnits(new LabelValue("Kilograms", "5"));
    berryGrid.setCommodity(new LabelValue("5012 - Gooseberries", "5012"));
    berryGrid.setQuantitySold(5.0);
    berryGrid.setQuantityProduced(5.0);
    expectedBerryGridList.add(berryGrid);

    assertEquals(2, data.getBerryGrid().size());
    assertArrayEquals(expectedBerryGridList.toArray(), data.getBerryGrid().toArray());

    List<InputGrid> expectedInputGridList = new ArrayList<>();
    InputGrid inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("9896 - Other Input (Specify)", "9896"));
    inputGrid.setSpecify("other input");
    inputGrid.setAmountRemainingAtYearEnd(0.9);
    expectedInputGridList.add(inputGrid);

    inputGrid = new InputGrid();
    inputGrid.setInput(new LabelValue("9764 - Fuel", "9764"));
    inputGrid.setAmountRemainingAtYearEnd(456.09);
    expectedInputGridList.add(inputGrid);

    assertEquals(2, data.getInputGrid().size());
    assertArrayEquals(expectedInputGridList.toArray(), data.getInputGrid().toArray());

    List<LivestockGrid> expectedOtherGridList = new ArrayList<>();
    LivestockGrid otherGrid = new LivestockGrid();
    otherGrid.setCommodity(new LabelValue("7512 - Alpaca; Cull; Hembras", "7512"));
    otherGrid.setEndingFmv(4.0);
    otherGrid.setEndingInventory(3.0);
    otherGrid.setQuantitySold(3.0);
    expectedOtherGridList.add(otherGrid);

    otherGrid = new LivestockGrid();
    otherGrid.setCommodity(new LabelValue("7604 - Honey", "7604"));
    otherGrid.setEndingFmv(55.0);
    otherGrid.setEndingInventory(5543.0);
    otherGrid.setQuantitySold(335.0);
    expectedOtherGridList.add(otherGrid);

    assertEquals(2, data.getOtherGrid().size());
    assertArrayEquals(expectedOtherGridList.toArray(), data.getOtherGrid().toArray());

    List<LivestockGrid> expectedSwineGridList = new ArrayList<>();
    LivestockGrid swineGrid = new LivestockGrid();
    swineGrid.setCommodity(new LabelValue("4001 - Livestock Inventory Default", "4001"));
    swineGrid.setEndingFmv(2.78);
    swineGrid.setEndingInventory(6.0);
    swineGrid.setQuantitySold(6.0);
    expectedSwineGridList.add(swineGrid);

    assertEquals(1, data.getSwineGrid().size());
    assertArrayEquals(expectedSwineGridList.toArray(), data.getSwineGrid().toArray());

    List<NurseryGrid> expectedNurseryGridList = new ArrayList<>();
    NurseryGrid nurseryGrid = new NurseryGrid();
    nurseryGrid.setCommodity(new LabelValue("6930 - Maple Syrup", "6930"));
    nurseryGrid.setUnits(new LabelValue("Grams", "6"));
    nurseryGrid.setQuantitySold(5.0);
    nurseryGrid.setQuantityProduced(5.0);
    nurseryGrid.setEndingInventory(5.0);
    nurseryGrid.setSquareMeters(434);
    expectedNurseryGridList.add(nurseryGrid);

    assertEquals(1, data.getNurseryGrid().size());
    assertArrayEquals(expectedNurseryGridList.toArray(), data.getNurseryGrid().toArray());

    List<LivestockGrid> expectedPoultryGridList = new ArrayList<>();
    LivestockGrid poultryGrid = new LivestockGrid();
    poultryGrid.setCommodity(new LabelValue("7658 - Chickens; Pullets", "7658"));
    poultryGrid.setEndingInventory(8.0);
    poultryGrid.setQuantitySold(8.0);
    poultryGrid.setEndingFmv(8.0);
    expectedPoultryGridList.add(poultryGrid);

    poultryGrid = new LivestockGrid();
    poultryGrid.setCommodity(new LabelValue("7656 - Chickens; Layers; Eggs for Consumption", "7656"));
    poultryGrid.setEndingInventory(6.0);
    poultryGrid.setQuantitySold(6.0);
    poultryGrid.setEndingFmv(6.0);
    expectedPoultryGridList.add(poultryGrid);

    assertEquals(2, data.getPoultryGrid().size());
    assertArrayEquals(expectedPoultryGridList.toArray(), data.getPoultryGrid().toArray());

    List<PayableGrid> expectedExpensesGridList = new ArrayList<>();
    PayableGrid expensesGrid = new PayableGrid();
    expensesGrid.setSourceOfExpense(new LabelValue("9896 - Other Allowable Expense (Specify)", "9896"));
    expensesGrid.setProgramExpensesNotPaidByYearEnd(123.7);
    expensesGrid.setSpecify("other payable");
    expectedExpensesGridList.add(expensesGrid);

    expensesGrid = new PayableGrid();
    expensesGrid.setSourceOfExpense(new LabelValue("264 - Forage", "264"));
    expensesGrid.setProgramExpensesNotPaidByYearEnd(9.95);
    expectedExpensesGridList.add(expensesGrid);

    assertEquals(2, data.getExpensesGrid().size());
    assertArrayEquals(expectedExpensesGridList.toArray(), data.getExpensesGrid().toArray());

    List<CropGrid> expectedTreeFruitGridList = new ArrayList<>();
    CropGrid treeFruitGrid = new CropGrid();
    treeFruitGrid.setCommodity(new LabelValue("5014 - Grapes", "5014"));
    treeFruitGrid.setUnits(new LabelValue("Pounds", "1"));
    treeFruitGrid.setAcres(90.0);
    treeFruitGrid.setQuantitySold(0.0);
    treeFruitGrid.setQuantityProduced(90.0);
    expectedTreeFruitGridList.add(treeFruitGrid);

    assertEquals(1, data.getTreeFruitGrid().size());
    assertArrayEquals(expectedTreeFruitGridList.toArray(), data.getTreeFruitGrid().toArray());

    List<CropGrid> expectedVegetablesGridList = new ArrayList<>();
    CropGrid vegetableGrid = new CropGrid();
    vegetableGrid.setCommodity(new LabelValue("6877 - Ginseng; Root Harvested", "6877"));
    vegetableGrid.setUnits(new LabelValue("Tonnes", "2"));
    vegetableGrid.setAcres(1.0);
    vegetableGrid.setQuantitySold(3.0);
    vegetableGrid.setQuantityProduced(2.0);
    expectedVegetablesGridList.add(vegetableGrid);

    assertEquals(1, data.getVegetableGrid().size());
    assertArrayEquals(expectedVegetablesGridList.toArray(), data.getVegetableGrid().toArray());

  }

  @Test
  public void pinNotFoundInFARM() {

    String submissionGuid = "00000000-0000-SUPP-0001-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    data.setSinNumber("123456789");
    data.setAgriStabilityAgriInvestPin(12316589);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    data.setProgramYear(new LabelValue("2024", "2024"));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals("2024 Supplemental 12316589", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
        + "- PIN not found in BCFARMS.\n" + "\n" + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n",
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
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    deleteSubmission(submissionGuid);

  }

  @Test
  public void pinNotFoundInCRM() {

    String submissionGuid = "00000000-0000-0001-0002-000000000000";
    int participantPin = 3709672;
    int programYear = 2024;

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    data.setSinNumber(null);
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setBusinessTaxNumber("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue(String.valueOf(programYear), String.valueOf(programYear)));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " Supplemental " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void sinMismatch() {

    String submissionGuid = "00000000-0000-SUPP-0003-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    data.setAgriStabilityAgriInvestPin(3693470);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumber(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue("2024", "2024"));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals("2024 Supplemental 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n\n"
        + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n" + "Participant Name: Jon Snow\n"
        + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void businessNumberMissingInFarm() {

    String submissionGuid = "00000000-0000-0001-0004-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    data.setAgriStabilityAgriInvestPin(31415975);
    data.setSinNumber(null);
    data.setBusinessTaxNumber("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue("2024", "2024"));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals("2024 Supplemental 31415975", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n\n" + "Participant Name: Targaryen Kingdom\n"
        + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void businessNumberMismatch() {

    String submissionGuid = "00000000-0000-0001-0004-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    data.setAgriStabilityAgriInvestPin(5070370);
    data.setSinNumber(null);
    data.setBusinessTaxNumber("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue("2024", "2024"));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals("2024 Supplemental 5070370", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n" + "\n"
        + "- Field \"Business Number\" with value \"123456789RC0001\" does not match BCFARMS: \"999999999RC0001\"."
        + " Note that only the first nine digits are compared.\n" + "\n" + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
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
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void invalidBusinessNumberInFARM() {

    String submissionGuid = "00000000-0000-SUPP-0005-000000000000";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityAgriInvestPin(22503767);
    data.setSinNumber(null);
    data.setProgramYear(new LabelValue("2023", "2023"));
    data.setBusinessTaxNumber("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue("2024", "2024"));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals("2024 Supplemental 22503767", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n" + "\n" + "Participant Name: Targaryen Kingdom\n"
        + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n", task.getDescription());
  }

  @Test
  public void individualHappyPath() {

    Integer participantPin = 3778842;
    Integer programYear = 2023;
    String submissionGuid = "e87eec38-cd5d-48f8-a356-d86e60669c4d";

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());
    
    // Delete the USER scenarios linked to this submission if any exist, from a previous test run.
    // Update scenarioSubmissionId to null for non-USER scenarios.
    List<ScenarioMetaData> scenariosLinkedToSubmission = ScenarioUtils.findScenariosByChefSubmissionGuid(programYearMetadata, submissionGuid);
    for(ScenarioMetaData scenarioMetadata : scenariosLinkedToSubmission) {
      Integer linkedScenario = scenarioMetadata.getScenarioId();
      assertNotNull(linkedScenario);

      if(scenarioMetadata.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)) {
        try {
          calculatorDao.deleteUserScenario(conn, linkedScenario);
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
      } else {
        try {
          chefsDatabaseDao.updateScenarioSubmissionId(conn, linkedScenario, null, user);
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

    deleteSubmission(submissionGuid);

    // Set up the Supplemental CHEFS Form submission data (not getting it from
    // CHEFS).
    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Johnny Appleseed");
    data.setTelephone("(250) 555-5555");
    data.setEmail("johnny@farm.ca");
    data.setBusinessStructure("individual");

    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("999999999");
    data.setBusinessTaxNumber("999999999");
    LabelValue municipalityCode = new LabelValue();
    municipalityCode.setValue("41");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue("2023", "2023"));

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
    
    swineGrid = new LivestockGrid();
    swineGrid.setCommodity(new LabelValue("8710 - Pot Bellied Pigs; Piglets Born", "4001"));
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

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);

    ScenarioMetaData supplementalScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_SUPP,
        ScenarioTypeCodes.CHEF);
    Integer supplementalScenarioNumber = supplementalScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, supplementalScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(supplementalScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(ScenarioCategoryCodes.CHEF_SUPP, scenario.getScenarioCategoryCode());
    assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());

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


    // Delete the USER scenarios linked to this submission if any exist, from a previous test run.
    // Update scenarioSubmissionId to null for non-USER scenarios.
    scenariosLinkedToSubmission = ScenarioUtils.findScenariosByChefSubmissionGuid(programYearMetadata, submissionGuid);
    for(ScenarioMetaData scenarioMetadata : scenariosLinkedToSubmission) {
      Integer linkedScenario = scenarioMetadata.getScenarioId();
      assertNotNull(linkedScenario);

      if(scenarioMetadata.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)) {
        try {
          calculatorDao.deleteUserScenario(conn, linkedScenario);
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
      } else {
        try {
          chefsDatabaseDao.updateScenarioSubmissionId(conn, linkedScenario, null, user);
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

    deleteSubmission(submissionGuid);
  }

  @Test
  public void fixValidationError() {

    Integer participantPin = 24107278;
    Integer programYear = 2024;
    String submissionGuid = "2c695023-1611-41fa-91f6-95ca1fd5a120";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    deleteSubmission(submissionGuid);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    SupplementalSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumber(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));

    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
    // assertNotNull(validationTask.getAccountId());
    assertEquals("2024 Supplemental " + participantPin, validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
    assertEquals(formUserType + " Local Supplemental form was submitted but has validation errors:\n" + "\n"
        + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n" + "Participant Name: Jon Snow\n"
        + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n", validationTask.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    // Correct the SIN Number
    data.setSinNumber("999999999");

    try {
      validationTask = completeAndGetTask(crmConfig.getValidationErrorUrl(), validationTask.getActivityId());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(validationTask);
    assertNotNull(validationTask.getAccountId());
    assertEquals("2024 Supplemental " + participantPin, validationTask.getSubject());
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
    assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void crud() {

    String[] submissionGuidArray = { "00000000-SUPP-0001-0000-000000000000", "00000000-SUPP-0001-0000-000000000001",
        "00000000-SUPP-0001-0000-000000000002" };
    List<String> submissionGuidList = Arrays.asList(submissionGuidArray);

    // Delete the submissions if they exist, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmissions(conn, submissionGuidList);
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
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.INTERIM);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.INTERIM);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.INTERIM);
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
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555001", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666001", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666000", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.CANCELLED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555002", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }

    // Delete the submissions
    try {
      chefsDatabaseDao.deleteSubmissions(conn, submissionGuidList);
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

  @Test
  public void readSupplementalSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.SUPP);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.SUPP, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
  }


  @Test
  public void duplicateSubmission() {

    Integer programYear = 2024;
    Integer participantPin = 98765675;
    
    String existingSubmissionGuid = "b6526118-7dfc-4a5a-9475-3232e50e7ebc";
    String duplicateSubmissionGuid = "0a23f0e7-93e3-4742-be8f-051db476c044";
    
    try {

      // Delete the submission if it exists, from a previous test run.
      deleteSubmission(duplicateSubmissionGuid);
  
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      // Confirm that a Supplemental CHEF Scenario exists and is linked to a CHEFS Supplemental submission
      ScenarioMetaData supplementalScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.CHEF_SUPP, ScenarioTypeCodes.CHEF);
      assertNotNull(supplementalScenarioMetadata);
  
      // Set up the CHEFS Form submission data (not getting it from CHEFS).
      SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
      SupplementalSubmissionDataResource data = submission.getData();
  
      submissionMetaData.setSubmissionGuid(duplicateSubmissionGuid);
  
      data.setParticipantName("PETER PARKER");
      data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
      data.setEmail("johnny@farm.ca");
      data.setBusinessStructure("individual");
      
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setSinNumber("987654321");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(duplicateSubmissionGuid);
      // Process the submission data
      SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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
      assertEquals(ChefsFormTypeCodes.SUPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      
    } finally {
      
      deleteSubmission(duplicateSubmissionGuid);
      
    }
  }


  @Disabled
  @Test
  public void deleteSubmissionsForPinAndProgramYear() {

    Integer participantPin = 3778842;
    Integer programYear = 2023;

    List<ScenarioMetaData> programYearMetadata = null;
    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    String[] submissionGuids = programYearMetadata.stream()
    .filter(s -> s.getChefsFormSubmissionGuid() != null)
    .map(ScenarioMetaData::getChefsFormSubmissionGuid)
    .collect(Collectors.toList())
    .toArray(new String[0]);
    
    deleteSubmissions(submissionGuids);
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

    SubmissionWrapperResource<SupplementalSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, SupplementalSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    SupplementalSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    SupplementalSubmissionProcessor processor = new SupplementalSubmissionProcessor(conn, formUserType);
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

  private SubmissionParentResource<SupplementalSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<SupplementalSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    SupplementalSubmissionDataResource data = new SupplementalSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}
