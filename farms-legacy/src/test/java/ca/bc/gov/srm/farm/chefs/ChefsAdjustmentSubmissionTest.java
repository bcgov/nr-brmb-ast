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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.AdjustmentSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentGrid;
import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
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
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsAdjustmentSubmissionTest extends ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsAdjustmentSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.ADJ;

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

    SubmissionWrapperResource<AdjustmentSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, AdjustmentSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "9184b7dc-ccf2-4b8f-bb32-801f4b733642";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<AdjustmentSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, AdjustmentSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    logger.debug(submissionMetaData.toString());
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    AdjustmentSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JONNY APPLESEED", data.getParticipantName());
    assertEquals("individual", data.getBusinessStructure());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityPin());
    assertEquals("999999999", data.getSinNumber());
    assertEquals("other important details here", data.getOtherDetails());
    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("Hong-Yi", data.getSignatureFirstName());
    assertEquals("Wang", data.getSignatureLastName());
    assertEquals("2025/01/17", data.getSignatureDate());
    assertEquals("N/A", data.getHowDoYouKnowTheParticipant());
    assertFalse(data.getYearsToAdjust().get("2018"));
    assertFalse(data.getYearsToAdjust().get("2019"));
    assertTrue(data.getYearsToAdjust().get("2020"));
    assertTrue(data.getYearsToAdjust().get("2021"));
    assertTrue(data.getYearsToAdjust().get("2022"));
    assertTrue(data.getYearsToAdjust().get("2023"));
    assertTrue(data.getYearsToAdjust().get("2024"));
  }

  @Test
  public void getSubmissionCorporation() {

    String submissionGuid = "88ac8c68-dd5b-4464-9fb8-a873c06e58ec";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<AdjustmentSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, AdjustmentSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    logger.debug(submissionMetaData.toString());
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    AdjustmentSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("LARK FARMS INC", data.getParticipantName());
    assertEquals(Integer.valueOf("22720015"), data.getAgriStabilityPin());
    assertEquals("corporation", data.getBusinessStructure());
    assertEquals("999012346", data.getBusinessTaxNumberBn());
    assertEquals("chefsForm", data.getExternalMethod());
    assertEquals("Additional information submitted at the same time as your program forms", data.getSimpleselectadvanced().get(0).getLabel());
    assertEquals("other details", data.getOtherDetails());
    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("Hong-Yi", data.getSignatureFirstName());
    assertEquals("Wang", data.getSignatureLastName());
    assertEquals("2025/01/17", data.getSignatureDate());
    assertEquals("N/A", data.getHowDoYouKnowTheParticipant());
    assertFalse(data.getYearsToAdjust().get("2018"));
    assertFalse(data.getYearsToAdjust().get("2019"));
    assertFalse(data.getYearsToAdjust().get("2020"));
    assertFalse(data.getYearsToAdjust().get("2021"));
    assertTrue(data.getYearsToAdjust().get("2022"));
    assertFalse(data.getYearsToAdjust().get("2023"));
    assertTrue(data.getYearsToAdjust().get("2024"));
    assertNull(data.getSinNumber());

    AdjustmentGrid ag = new AdjustmentGrid();
    ag.setLineCode(4332);
    ag.setProgramYear(2024);
    ag.setRevisedAmount(3.0);
    ag.setPreviousAmount(2.0);
    ag.setDescription("apples");
    ag.setSectionOnTheForm("CROP");
    assertEquals(ag, data.getAdjustmentGrid().get(0));

  }

  @Test
  public void pinNotFoundInFARM() {

    String submissionGuid = "00000000-0000-0001-4001-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    // Delete the submission if it exists, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmission(conn, submissionGuid);
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

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setBusinessStructure("individual");
    data.setSinNumber("123456789");
    data.setAgriStabilityPin(12316589);
    data.setBusinessTaxNumberBn("");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setSubmissionGuid(submissionGuid);
    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2022", true);
    data.setYearsToAdjust(yearsToAdjust);
    AdjustmentGrid ag = new AdjustmentGrid();
    ag.setSectionOnTheForm("section text");
    ag.setDescription("text text");
    ag.setLineCode(104);
    ag.setRevisedAmount(123.00);
    ag.setPreviousAmount(120.00);
    data.setAdjustmentGrid(Collections.singletonList(ag));

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Adjustment 12316589", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
        + "- PIN not found in BCFARMS.\n" + "\n" + "Participant Name: Jon Snow\n",
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
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission if it exists, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmission(conn, submissionGuid);
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
  public void pinNotFoundInCRM() {

    String submissionGuid = "00000000-0000-0001-0002-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    // Delete the submission if it exists, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmission(conn, submissionGuid);
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

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("corporation");

    data.setSinNumber(null);
    data.setAgriStabilityPin(364783140);
    data.setBusinessTaxNumberBn("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2022", true);
    data.setYearsToAdjust(yearsToAdjust);
    AdjustmentGrid ag = new AdjustmentGrid();
    ag.setSectionOnTheForm("section text");
    ag.setDescription("text text");
    ag.setLineCode(104);
    ag.setRevisedAmount(123.00);
    ag.setPreviousAmount(120.00);
    data.setAdjustmentGrid(Collections.singletonList(ag));

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Adjustment 364783140", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n"
        + "- PIN not found in CRM.\n"
        + "- PIN not found in BCFARMS.\n"
        +"\n"
        + "Participant Name: Targaryen Kingdom\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Delete the submission if it exists, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmission(conn, submissionGuid);
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
  public void sinMismatch() {

    String submissionGuid = "00000000-0000-0001-0003-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    // Delete the submission if it exists, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmission(conn, submissionGuid);
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

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setBusinessStructure("individual");

    data.setAgriStabilityPin(3693470);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2020", true);
    yearsToAdjust.put("2021", true);
    yearsToAdjust.put("2022", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Adjustment 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n\n"
        + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n\n"
        + "Participant Name: Jon Snow\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void businessNumberMismatch() {

    String submissionGuid = "00000000-0000-0001-0004-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);
    deleteSubmission(submissionGuid);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("corporation");

    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2022", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Adjustment 5070370", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n"
        + "- Field \"Business Number\" with value \"123456789RC0001\" does not match BCFARMS: \"999999999RC0001\"."
        + " Note that only the first nine digits are compared.\n\n"
        + "Participant Name: Targaryen Kingdom\n", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void invalidBusinessNumberInFARM() {

    String submissionGuid = "00000000-0000-0001-0005-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);
    deleteSubmission(submissionGuid);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("corporation");

    data.setAgriStabilityPin(22503767);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2019", true);
    yearsToAdjust.put("2021", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2021 Adjustment 22503767", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n"
        + "- The following years were selected for reprocessing but do not have a Verified Final scenario: 2019\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n", task.getDescription());
  }

  @Test
  public void missingProgramYear() {

    String submissionGuid = "00000000-0ADJ-0001-0005-000000000001";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);
    deleteSubmission(submissionGuid);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("corporation");

    data.setAgriStabilityPin(22503767);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    Integer programYear = DateUtils.getYearFromDate(new Date());
    assertEquals(programYear + " Adjustment 22503767", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n"
        + "- Required field is blank: Program Year\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n", task.getDescription());
  }

  @Test
  public void allYearsNotFinal() {

    String submissionGuid = "00000000-0000-0001-0035-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);
    deleteSubmission(submissionGuid);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("individual");

    data.setAgriStabilityPin(98765689);
    data.setSinNumber("987654321");
    data.setBusinessTaxNumberBn("104087283");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2017", true);
    yearsToAdjust.put("2018", true);
    yearsToAdjust.put("2019", true);
    yearsToAdjust.put("2020", true);
    yearsToAdjust.put("2021", true);
    yearsToAdjust.put("2022", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Adjustment 98765689", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n"
        + "- The following years were selected for reprocessing but do not have a Verified Final scenario: 2017,2018,2019,2020,2021,2022\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n", task.getDescription());
  }

  @Test
  public void individualHappyPath() {

    Integer participantPin = 31415926;
    Integer programYear = 2024;
    String submissionGuid = "3fe185da-d539-498e-9db8-00769c443fe0";

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the adjustment USER Scenario if it exists, from a previous test
    // run.
    ScenarioMetaData adjustmentScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, ScenarioTypeCodes.USER);
    List<ScenarioMetaData> adjustmentScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, ScenarioTypeCodes.USER);
    for (ScenarioMetaData smd : adjustmentScenarioMetadataList) {
      logger.debug(smd.toString());
    }

    if (adjustmentScenarioMetadata != null) {
      Integer existingAdjustmentScenarioId = adjustmentScenarioMetadata.getScenarioId();
      assertNotNull(existingAdjustmentScenarioId);

      deleteUserScenario(existingAdjustmentScenarioId);
    }

    deleteSubmission(submissionGuid);

    // Set up the adjustment CHEFS Form submission data (not getting it from
    // CHEFS).
    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("individual");

    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("641256987");
    data.setBusinessTaxNumberBn(null);

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2018", false);
    yearsToAdjust.put("2019", false);
    yearsToAdjust.put("2020", false);
    yearsToAdjust.put("2021", false);
    yearsToAdjust.put("2022", false);
    yearsToAdjust.put("2023", false);
    yearsToAdjust.put("2024", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);

    adjustmentScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.PRODUCER_ADJUSTMENT,
        ScenarioTypeCodes.USER);
    Integer adjustmentScenarioNumber = adjustmentScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, adjustmentScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(adjustmentScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());

    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages calculationMessages = null;
    try {
      calculationMessages = benefitService.calculateBenefit(scenario, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(calculationMessages);
    assertEquals(0, calculationMessages.size());

    String newStateCode = ScenarioStateCodes.VERIFIED;
    String stateChangeReason = null;
    String newCategoryCode = ScenarioCategoryCodes.PRODUCER_ADJUSTMENT;
    String fifoResultType = null;
    try {
      calculatorService.updateScenario(scenario, newStateCode, stateChangeReason, newCategoryCode, USER_EMAIL, null, formUserType, ChefsFormTypeCodes.ADJ, fifoResultType, user);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, adjustmentScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertNotNull(scenario.getScenarioId());
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(adjustmentScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(ScenarioStateCodes.VERIFIED, scenario.getScenarioStateCode());
    assertEquals(ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, scenario.getScenarioCategoryCode());

    deleteUserScenario(scenario.getScenarioId());
    deleteSubmission(submissionGuid);

    logger.debug("End of happy test!");
  }

  @Test
  public void individualHappyPathMultipleYears() {

    Integer participantPin = 31415926;
    Integer[] programYears = new Integer[] { 2022, 2024 };
    String submissionGuid = "3fe185da-d539-498e-9db8-00769c443fe0";

    for (Integer programYear : programYears) {
      // Get the list of scenarios for the program year
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());

      // Delete the adjustment USER Scenario if it exists, from a previous test
      // run.
      ScenarioMetaData adjustmentScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, ScenarioTypeCodes.USER);
      List<ScenarioMetaData> adjustmentScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, ScenarioTypeCodes.USER);
      for (ScenarioMetaData smd : adjustmentScenarioMetadataList) {
        logger.debug(smd.toString());
      }

      if (adjustmentScenarioMetadata != null) {
        Integer existingadjustmentScenarioId = adjustmentScenarioMetadata.getScenarioId();
        assertNotNull(existingadjustmentScenarioId);

        deleteUserScenario(existingadjustmentScenarioId);
      }
    }

    deleteSubmission(submissionGuid);

    // Set up the adjustment CHEFS Form submission data (not getting it from
    // CHEFS).
    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setBusinessStructure("individual");

    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("641256987");
    data.setBusinessTaxNumberBn(null);

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2018", false);
    yearsToAdjust.put("2019", false);
    yearsToAdjust.put("2020", false);
    yearsToAdjust.put("2021", false);
    yearsToAdjust.put("2022", true);
    yearsToAdjust.put("2023", false);
    yearsToAdjust.put("2024", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

    for (Integer programYear : programYears) {
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);

      ScenarioMetaData adjustmentScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, ScenarioTypeCodes.USER);
      Integer adjustmentScenarioNumber = adjustmentScenarioMetadata.getScenarioNumber();

      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, adjustmentScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(scenario);
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(adjustmentScenarioNumber, scenario.getScenarioNumber());
      assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
      assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());

      BenefitService benefitService = ServiceFactory.getBenefitService();
      ActionMessages calculationMessages = null;
      try {
        calculationMessages = benefitService.calculateBenefit(scenario, user);
      } catch (Exception e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(calculationMessages);
      assertEquals(0, calculationMessages.size());

      String newStateCode = ScenarioStateCodes.VERIFIED;
      String stateChangeReason = null;
      String newCategoryCode = ScenarioCategoryCodes.PRODUCER_ADJUSTMENT;
      String fifoResultType = null;
      try {
        calculatorService.updateScenario(scenario, newStateCode, stateChangeReason, newCategoryCode, USER_EMAIL, null, formUserType,
            ChefsFormTypeCodes.ADJ, fifoResultType, user);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, adjustmentScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }

      assertNotNull(scenario);
      assertNotNull(scenario.getScenarioId());
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(adjustmentScenarioNumber, scenario.getScenarioNumber());
      assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
      assertEquals(ScenarioStateCodes.VERIFIED, scenario.getScenarioStateCode());
      assertEquals(ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, scenario.getScenarioCategoryCode());

      deleteUserScenario(scenario.getScenarioId());
    }

    deleteSubmission(submissionGuid);

    logger.debug("End of happy test!");
  }

  @Test
  public void fixValidationError() {

    Integer participantPin = 31415926;
    Integer programYear = 2024;
    String submissionGuid = "62a6e738-d7b8-4d95-afd0-a35d24eb96a6";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the adjustment Scenario if it exists, from a previous test run.
    ScenarioMetaData adjustmentScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, ScenarioTypeCodes.USER);
    if (adjustmentScenarioMetadata != null) {
      Integer existingadjustmentScenarioId = adjustmentScenarioMetadata.getScenarioId();
      assertNotNull(existingadjustmentScenarioId);

      deleteUserScenario(existingadjustmentScenarioId);
    }

    deleteSubmission(submissionGuid);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    AdjustmentSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setBusinessStructure("individual");

    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    HashMap<String, Boolean> yearsToAdjust = new HashMap<>();
    yearsToAdjust.put("2018", false);
    yearsToAdjust.put("2019", false);
    yearsToAdjust.put("2020", false);
    yearsToAdjust.put("2021", false);
    yearsToAdjust.put("2022", false);
    yearsToAdjust.put("2023", false);
    yearsToAdjust.put("2024", true);
    data.setYearsToAdjust(yearsToAdjust);

    AdjustmentSubmissionProcessor processor = new AdjustmentSubmissionProcessor(conn, formUserType);
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
    assertEquals("2024 Adjustment " + participantPin, validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
    assertEquals(formUserType + " Adjustment form was submitted but has validation errors:\n" + "\n"
        + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"641256987\".\n" + "\n" + "Participant Name: Jon Snow\n", validationTask.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    // Correct the SIN Number
    data.setSinNumber("641256987");

    try {
      validationTask = completeAndGetTask(crmConfig.getValidationErrorUrl(), validationTask.getActivityId());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(validationTask);
    assertNotNull(validationTask.getAccountId());
    assertEquals("2024 Adjustment " + participantPin, validationTask.getSubject());
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

    // Delete the adjustment Scenario if it exists, from a previous test run.
    adjustmentScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.PRODUCER_ADJUSTMENT,
        ScenarioTypeCodes.USER);
    if (adjustmentScenarioMetadata != null) {
      Integer existingadjustmentScenarioId = adjustmentScenarioMetadata.getScenarioId();
      assertNotNull(existingadjustmentScenarioId);

      deleteUserScenario(existingadjustmentScenarioId);
    }

    submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void crud() {

    String[] submissionGuidArray = { "00000000-0000-0001-0000-000000000000", "00000000-0000-0001-0000-000000000001",
        "00000000-0000-0001-0000-000000000002" };
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
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.ADJ);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.ADJ);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.ADJ);
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
      assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.ADJ, submissionRec.getFormTypeCode());
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
  public void readadjustmentSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.ADJ);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.ADJ, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
  }

  private SubmissionParentResource<AdjustmentSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<AdjustmentSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    AdjustmentSubmissionDataResource data = new AdjustmentSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}
