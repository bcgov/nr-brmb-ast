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

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.CashMarginsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.cashMargin.CashMarginsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author thuynh
 */
public class ChefsCashMarginsSubmissionTest extends ChefsSubmissionTest {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(ChefsCashMarginsSubmissionTest.class);

  private static final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.CM;


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

    SubmissionWrapperResource<CashMarginsSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CashMarginsSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "f6aef312-4f85-4d26-8850-495f0d434a19";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<CashMarginsSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CashMarginsSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    CashMarginsSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("Johnny Appleseed", data.getParticipantName());
    assertEquals("(579) 344-1852", data.getPhoneNumber());
    assertEquals("johnny@farm.ca", data.getEmail());

    assertEquals("individual", data.getBusinessStructure());

    assertEquals(Integer.valueOf(23723331), data.getAgriStabilityPin());
    assertEquals("987654321", data.getSinNumber());
    assertEquals("9999 99999", data.getBusinessTaxNumberBn());

    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertEquals("DEV", data.getEnvironment());

    assertEquals(Boolean.TRUE, data.getSubmit());
    assertEquals(Boolean.FALSE, data.getLateEntry());
  }

  @Test
  public void getSubmissionCorporation() {

    String submissionGuid = "46503de7-aebf-4a8d-baa9-bb18428ac02c";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<CashMarginsSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CashMarginsSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    CashMarginsSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("Apples R Us", data.getParticipantName());
    assertEquals("(250) 555-5555", data.getPhoneNumber());
    assertEquals("apples@farm.ca", data.getEmail());

    assertEquals("corporation", data.getBusinessStructure());

    assertEquals(Integer.valueOf(98765731), data.getAgriStabilityPin());
    assertNull(data.getSinNumber());
    assertEquals("9999 99999", data.getBusinessTaxNumberBn());

    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertEquals("DEV", data.getEnvironment());

    assertEquals(Boolean.TRUE, data.getSubmit());
    assertEquals(Boolean.FALSE, data.getLateEntry());
  }

  @Test
  public void pinNotFoundInFARM() {

    String submissionGuid = "CASH0000-0000-0000-0001-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");
    data.setAgriStabilityPin(12316589);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    assertEquals(ProgramYearUtils.getCurrentCalendarYear() + " Cash Margins 12316589", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Cash Margins form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
        + "- PIN not found in BCFARMS.\n" + "\n" + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n"
        + "Participant Type: individual\n" + "SIN Number: 123456789\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

  }

  @Test
  public void pinNotFoundInCRM() {

    String submissionGuid = "CASH1000-0000-0000-0002-000000000000";

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityPin(4302394);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    
    // If the Validation Error task gets deleted or marked complete,
    // a new one will be created with the current calendar year in the subject.
    assertEquals("2024 Cash Margins 4302394", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Cash Margins form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n"
        + "Participant Type: corporation\n" + "Business Number: 1234 56789RC0001\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);
  }

  @Test
  public void sinMismatch() {

    String submissionGuid = "CASH1000-0000-0000-0003-000000000000";

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");
    data.setAgriStabilityPin(3693470);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    
    // If the Validation Error task gets deleted or marked complete,
    // a new one will be created with the current calendar year in the subject.
    assertEquals("2024 Cash Margins 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Cash Margins form was submitted but has validation errors:\n" + "\n"
            + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n" + "Participant Name: Jon Snow\n"
            + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n" + "Participant Type: individual\n" + "SIN Number: 123456789\n",
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
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);
  }

  @Test
  public void businessNumberMismatch() {

    String submissionGuid = "CASH0000-0000-0000-0004-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    assertEquals(ProgramYearUtils.getCurrentCalendarYear() + " Cash Margins 5070370", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Cash Margins form was submitted but has validation errors:\n" + "\n"
            + "- Field \"Business Number\" with value \"123456789RC0001\" does not match BCFARMS: \"999999999RC0001\"."
            + " Note that only the first nine digits are compared.\n" + "\n" + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
            + "Email: targaryen@game.of.thrones\n" + "Participant Type: corporation\n" + "Business Number: 1234 56789RC0001\n",
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
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);
  }

  @Test
  public void invalidBusinessNumberInFARM() {

    String submissionGuid = "CASH0000-0000-0000-0005-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityPin(22503767);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    assertEquals(ProgramYearUtils.getCurrentCalendarYear() + " Cash Margins 22503767", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Cash Margins form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n" + "\n" + "Participant Name: Targaryen Kingdom\n"
        + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n" + "Participant Type: corporation\n"
        + "Business Number: 1234 56789RC0001\n", task.getDescription());
  }

  @Test
  public void individualHappyPath() {

    Integer participantPin = 31415945;
    Integer programYear = ProgramYearUtils.getCurrentCalendarYear();
    String submissionGuid = "d56649d9-ba3e-441b-b454-9e29c2bdf305";

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid);

    // Set up the Cash Margins CHEFS Form submission data (not getting it from
    // CHEFS).
    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("individual");
    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("987654321");
    data.setBusinessTaxNumberBn("7899 96666");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    // If the task is non-null, the validation eror task will need to be manually completed.
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
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);

    ScenarioMetaData cashScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_CM,
        ScenarioTypeCodes.CHEF);
    Integer cashScenarioNumber = cashScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, cashScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(cashScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(ScenarioCategoryCodes.CHEF_CM, scenario.getScenarioCategoryCode());
    assertEquals(ScenarioTypeCodes.CHEF, scenario.getScenarioTypeCode());
    assertTrue(scenario.getFarmingYear().getIsCashMargins());
    assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("yyyy-MM-dd").format(scenario.getFarmingYear().getCashMarginsOptInDate()));

    deleteSubmission(submissionGuid);
  }

  @Test
  public void fixValidationError() {

    Integer participantPin = 3693470;
    Integer programYear = ProgramYearUtils.getCurrentCalendarYear();
    String submissionGuid = "2e457fae-ebd3-43c7-b69e-256544c01253";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");
    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    assertEquals(ProgramYearUtils.getCurrentCalendarYear() + " Cash Margins 3693470", validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
    assertEquals(
        formUserType + " Cash Margins form was submitted but has validation errors:\n" + "\n"
            + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n" + "Participant Name: Jon Snow\n"
            + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n" + "Participant Type: individual\n" + "SIN Number: 123456789\n",
        validationTask.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

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
    assertEquals(ProgramYearUtils.getCurrentCalendarYear() + " Cash Margins 3693470", validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_NOT_STARTED), validationTask.getStatusCode());

    CrmTaskResource task = null;
    try {
      processor.loadSubmissionsFromDatabase();
      task = processor.processSubmission(submissionMetaData);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    // If the task is non-null, the validation eror task will need to be manually completed.
    assertNull(task);

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);

    ScenarioMetaData cashScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_CM,
        ScenarioTypeCodes.CHEF);
    Integer cashScenarioNumber = cashScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, cashScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(cashScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(ScenarioCategoryCodes.CHEF_CM, scenario.getScenarioCategoryCode());
    assertEquals(ScenarioTypeCodes.CHEF, scenario.getScenarioTypeCode());
    assertTrue(scenario.getFarmingYear().getIsCashMargins());
    assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
        new SimpleDateFormat("yyyy-MM-dd").format(scenario.getFarmingYear().getCashMarginsOptInDate()));

    deleteSubmission(submissionGuid);
  }

  @Test
  public void notForThisEnvironment() {

    String submissionGuid = "CASH0000-0000-0000-0008-000000000000";

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("TEST");

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    // If the task is non-null, the validation eror task will need to be manually completed.
    assertNull(task);

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.OTHER_ENV, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission to cleanup the database
    deleteSubmission(submissionGuid);
  }

  @Test
  public void adminUseNotCompleted() {

    String submissionGuid = "CASH0000-0000-0000-0009-000000000000";

    // Delete the submission if it exists, from a previously failed test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CashMarginsSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment(null);

    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
    // If the task is non-null, the validation eror task will need to be manually completed.
    assertNull(task);

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNull(submissionRec);
  }

  @Test
  public void crud() {

    String[] submissionGuidArray = { "CASH0000-0000-0000-0000-000000000000", "CASH0000-0000-0000-0000-000000000001",
        "CASH0000-0000-0000-0000-000000000002" };
    List<String> submissionGuidList = Arrays.asList(submissionGuidArray);

    deleteSubmissions(submissionGuidList);

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
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.CM);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.CM);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.CM);
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
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(1, submissionRec.getRevisionCount().intValue());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555001", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666001", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(1, submissionRec.getRevisionCount().intValue());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(1, submissionRec.getRevisionCount().intValue());
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
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666000", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(2, submissionRec.getRevisionCount().intValue());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(2, submissionRec.getRevisionCount().intValue());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.CANCELLED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555002", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(2, submissionRec.getRevisionCount().intValue());
    }

    deleteSubmissions(submissionGuidList);
  }

  @Test
  public void readCashMarginSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.CM);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.CM, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
  }


  @Test
  public void duplicateSubmission() {

    Integer participantPin = 964222943;
    Integer programYear = ProgramYearUtils.getCurrentCalendarYear();
    String submissionGuid1 = "CASH0000-0000-0000-0010-000000000000";
    String submissionGuid2 = "CASH0000-0000-0000-0011-000000000000";
    
    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid1);
    deleteSubmission(submissionGuid2);
    
    // -----------------------------  First Submission  --------------------------------------------------
    {
      
      // Set up the Cash Margins CHEFS Form submission data (not getting it from
      // CHEFS).
      SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
      CashMarginsSubmissionDataResource data = submission.getData();
      
      submissionMetaData.setSubmissionGuid(submissionGuid1);
      
      data.setParticipantName("Targaryen Kingdom");
      data.setPhoneNumber("(250) 555-5555");
      data.setEmail("targaryen@game.of.thrones");
      
      data.setBusinessStructure("corporation");
      data.setAgriStabilityPin(participantPin);
      data.setSinNumber(null);
      data.setBusinessTaxNumberBn("999928888");
      
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid1);
  
      // Process the submission data
      CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
      // If the task is non-null, the validation eror task will need to be manually completed.
      assertNull(task);
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid1);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid1, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(1, submissionRec.getRevisionCount().intValue());
      
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertFalse(programYearMetadata.isEmpty());
  
      ScenarioMetaData cashScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_CM,
          ScenarioTypeCodes.CHEF);
      Integer cashScenarioNumber = cashScenarioMetadata.getScenarioNumber();
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, cashScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(scenario);
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(cashScenarioNumber, scenario.getScenarioNumber());
      assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
      assertEquals(ScenarioCategoryCodes.CHEF_CM, scenario.getScenarioCategoryCode());
      assertEquals(ScenarioTypeCodes.CHEF, scenario.getScenarioTypeCode());
      assertTrue(scenario.getFarmingYear().getIsCashMargins());
      assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("yyyy-MM-dd").format(scenario.getFarmingYear().getCashMarginsOptInDate()));
    }
    
    
    // -----------------------------  Second Submission  --------------------------------------------------
    {
      
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      long scenarioCountAfterSubmission1 = programYearMetadata.stream()
          .filter(s -> s.getProgramYear().equals(programYear))
          .count();
      
      // Set up the Cash Margins CHEFS Form submission data (not getting it from
      // CHEFS).
      SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
      SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
      CashMarginsSubmissionDataResource data = submission.getData();
      
      submissionMetaData.setSubmissionGuid(submissionGuid2);
      
      data.setParticipantName("Targaryen Kingdom");
      data.setPhoneNumber("(250) 555-5555");
      data.setEmail("targaryen@game.of.thrones");
      
      data.setBusinessStructure("individual");
      data.setAgriStabilityPin(participantPin);
      data.setSinNumber("987654321");
      data.setBusinessTaxNumberBn("7899 96666");
      
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid2);
      
      // Process the submission data
      CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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
      assertNotNull(task.getAccountId());
      assertEquals("Duplicate form: " + programYear + " Cash Margins " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          "IDIR Cash Margins form was submitted but has previous submissions for this PIN and program year:\n"
          + "\n"
          + "Form submissions of this type have been previously submitted for this PIN and program year: " + submissionGuid1
          + "\n\n"
          + "Environment: DEV\n",
          task.getDescription());
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid2);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid2, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(1, submissionRec.getRevisionCount().intValue());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertFalse(programYearMetadata.isEmpty());
      
      long scenarioCountAfterSubmission2 = programYearMetadata.stream()
          .filter(s -> s.getProgramYear().equals(programYear))
          .count();
      assertEquals(scenarioCountAfterSubmission1, scenarioCountAfterSubmission2);
    }
    
    deleteSubmission(submissionGuid1);
    deleteSubmission(submissionGuid2);
  }

  @Disabled
  @Test
  public void processCashMarginSubmissionsFromRealChefsForms() {

    try {

      CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
      processor.setUser(user);
      processor.loadSubmissionsFromChefs();
      processor.loadSubmissionsFromDatabase();
      processor.processSubmissions();

      conn.commit();
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
        fail("Unexpected Exception");
      }
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

    String submissionGuid = "6bfdcd01-d653-47c4-92a3-40e5b35a51fe";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<CashMarginsSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CashMarginsSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    CashMarginsSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    CashMarginsSubmissionProcessor processor = new CashMarginsSubmissionProcessor(conn, formUserType);
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

  private SubmissionParentResource<CashMarginsSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<CashMarginsSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    CashMarginsSubmissionDataResource data = new CashMarginsSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }


  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}
