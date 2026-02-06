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

import org.apache.struts.action.ActionMessages;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.NolSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.nol.NolSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.nol.NolSubmissionDataResource.IncomeBelowThresholdCheckboxes;
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
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class ChefsNolSubmissionTest extends ChefsSubmissionTest {

  @SuppressWarnings("unused")
  private static Logger logger = LoggerFactory.getLogger(ChefsNolSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.NOL;

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

    SubmissionWrapperResource<NolSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NolSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "847e9d7e-0a73-495c-a444-baecf6b061fd";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<NolSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NolSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NolSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JOHNNY APPLESEED", data.getGrowerCorporationName());
    assertEquals("(604) 617-3472", data.getPhoneNumber());
    assertEquals("JOHNNY@FARM.CA", data.getEmail());

    assertNull(data.getSubmitTo());
    assertEquals("individual", data.getParticipantType());

    assertEquals(Integer.valueOf(2024), data.getProgramYear());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityPin());
    assertEquals("999999999", data.getSinNumber());
    assertEquals("999988888", data.getBusinessNumber());
    assertEquals("treefruit", data.getPrimaryFarmingActivity());
    assertNull(data.getPrimaryFarmingActivityOther());
    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("Hong-Yi", data.getSignatureFirstName());
    assertEquals("Wang", data.getSignatureLastName());
    assertEquals("2025/01/21", data.getSignatureDate());
    assertEquals("N/A", data.getHowDoYouKnowTheParticipant());

    assertNotNull(data.getIncomeBelowThresholdCheckboxes());
    assertEquals(Boolean.TRUE, data.getIncomeBelowThresholdCheckboxes().getYes());
    assertEquals(Boolean.FALSE, data.getIncomeBelowThresholdCheckboxes().getNo());

    assertEquals("increase", data.getIncomeDecreaseDetails());
    assertEquals("decrease", data.getExpenseIncreaseDetails());

    assertNull(data.getOrigin());
    assertNull(data.getExternalMethod());
    assertNull(data.getEnvironment());

    assertEquals(Boolean.TRUE, data.getSubmit());
    assertEquals(Boolean.FALSE, data.getLateEntry());
  }

  @Test
  public void getSubmissionCorporation() {

    String submissionGuid = "e0689522-f2ed-4853-acdc-e2c2bb093c8f";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<NolSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NolSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NolSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("APPLES R US", data.getGrowerCorporationName());
    assertEquals("(250) 555-5555", data.getPhoneNumber());
    assertEquals("APPLES@FARM.CA", data.getEmail());

    assertNull(data.getSubmitTo());

    assertEquals("corporation", data.getParticipantType());

    assertEquals(Integer.valueOf(2024), data.getProgramYear());
    assertEquals(Integer.valueOf(283235576), data.getAgriStabilityPin());
    assertNull(data.getSinNumber());
    assertEquals("789996666", data.getBusinessNumber());
    assertEquals("treefruit", data.getPrimaryFarmingActivity());
    assertNull(data.getPrimaryFarmingActivityOther());
    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("Hong-Yi", data.getSignatureFirstName());
    assertEquals("Wang", data.getSignatureLastName());
    assertEquals("2025/01/21", data.getSignatureDate());
    assertEquals("N/A", data.getHowDoYouKnowTheParticipant());

    assertNotNull(data.getIncomeBelowThresholdCheckboxes());
    assertEquals(Boolean.TRUE, data.getIncomeBelowThresholdCheckboxes().getYes());
    assertEquals(Boolean.FALSE, data.getIncomeBelowThresholdCheckboxes().getNo());

    assertEquals("fire damage", data.getIncomeDecreaseDetails());
    assertEquals("N/A", data.getExpenseIncreaseDetails());

    assertNull(data.getOrigin());
    assertNull(data.getExternalMethod());
    assertNull(data.getEnvironment());

    assertEquals(Boolean.TRUE, data.getSubmit());
    assertEquals(Boolean.FALSE, data.getLateEntry());
  }

  @Test
  public void pinNotFoundInFARM() {

    String submissionGuid = "00000000-0000-0000-0001-000000000000";
    
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Jon Snow");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setSubmitTo("Agristability");
    data.setParticipantType("individual");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(12316589);
    data.setSinNumber("123456789");
    data.setBusinessNumber(null);
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NOL 12316589", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Notice of Loss form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
            + "- PIN not found in BCFARMS.\n" + "\n"
            + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n"
            + "Participant Type: individual\n" + "SIN Number: 123456789\n" + "Primary Farming Activity: Other: Giants",
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
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

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

    String submissionGuid = "00000000-0000-0000-0002-000000000000";

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("corporation");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(4302394);
    data.setSinNumber(null);
    data.setBusinessNumber("1234 56789");
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NOL 4302394", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Notice of Loss form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
        + "\n" + "Participant Name: Targaryen Kingdom\n"
        + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n" + "Participant Type: corporation\n"
        + "Business Number: 123456789RC0001\n" + "Primary Farming Activity: Other: Giants", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

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

    String submissionGuid = "00000000-0000-0000-0003-000000000000";

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Jon Snow");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("individual");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(3693470);
    data.setSinNumber("123456789");
    data.setBusinessNumber(null);
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NOL 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Notice of Loss form was submitted but has validation errors:\n" + "\n"
        + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n"
        + "Participant Name: Jon Snow\n"
        + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n" + "Participant Type: individual\n"
        + "SIN Number: 123456789\n" + "Primary Farming Activity: Other: Giants", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

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
  public void businessNumberMismatch() {

    String submissionGuid = "00000000-0000-0000-0004-000000000000";
    
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("corporation");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessNumber("1234 56789");
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NOL 5070370", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Notice of Loss form was submitted but has validation errors:\n" + "\n"
        + "- Field \"Business Number\" with value \"123456789RC0001\" does not match BCFARMS: \"999999999RC0001\"."
        + " Note that only the first nine digits are compared.\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
        + "Email: targaryen@game.of.thrones\n" + "Participant Type: corporation\n"
        + "Business Number: 123456789RC0001\n" + "Primary Farming Activity: Other: Giants", task.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

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
  public void invalidBusinessNumberInFARM() {

    String submissionGuid = "00000000-0000-0000-0005-000000000000";
    
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("corporation");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(22503767);
    data.setSinNumber(null);
    data.setBusinessNumber("1234 56789");
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NOL 22503767", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Notice of Loss form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n"
        + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n" + "Participant Type: corporation\n"
        + "Business Number: 123456789RC0001\n" + "Primary Farming Activity: Other: Giants", task.getDescription());
  }

  @Test
  public void individualHappyPath() {

    Integer participantPin = 3778842;
    Integer programYear = 2024;
    String submissionGuid = "492947c8-8fd8-4ee9-b432-12a36616d1aa";

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

    // Delete the submission if it exists, from a previous test run.
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

    // Set up the NOL CHEFS Form submission data (not getting it from CHEFS).
    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("individual");
    data.setProgramYear(programYear);
    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("999999999");
    data.setBusinessNumber(null);
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " NOL " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        "Primary Farming Activity: Other: Giants\n"
            + "Income Decrease Details: 80%. Dragons ate my livestock.\n"
            + "Expense Increase Details: 200%. Now paying protection money so dragons don't eat more livestock.",
        task.getDescription());

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
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertEquals(task.getActivityId(), submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);

    ScenarioMetaData nolScenarioMetadata = ScenarioUtils.findNolScenario(programYearMetadata, programYear);
    Integer nolScenarioNumber = nolScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, nolScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(nolScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(task.getActivityId(), scenario.getCrmTaskGuid());
    assertEquals(ScenarioCategoryCodes.NOL, scenario.getScenarioCategoryCode());
    assertEquals(ScenarioTypeCodes.USER, scenario.getScenarioTypeCode());

    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages calculationMessages = null;
    try {
      calculationMessages = benefitService.calculateBenefit(scenario, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(calculationMessages);

    String newStateCode = ScenarioStateCodes.COMPLETED;
    String stateChangeReason = null;
    String newCategoryCode = ScenarioCategoryCodes.NOL;
    String benefitTriageResultType = null;
    try {
      calculatorService.updateScenario(scenario, newStateCode, stateChangeReason, newCategoryCode, USER_EMAIL, null, formUserType,
          ChefsFormTypeCodes.NOL, benefitTriageResultType, user);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      task = crmDao.getNolTask(task.getActivityId());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(task);
    assertNotNull(task.getAccountId());
    assertEquals(programYear + " NOL " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), task.getStateCode());
    // TODO Expected code 5, but getting 2
//    assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_COMPLETED), task.getStatusCode());

    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, nolScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertNotNull(scenario.getScenarioId());
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(nolScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(task.getActivityId(), scenario.getCrmTaskGuid());
    assertEquals(ScenarioStateCodes.COMPLETED, scenario.getScenarioStateCode());
    assertEquals(ScenarioCategoryCodes.NOL, scenario.getScenarioCategoryCode());
    
    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    
    // TODO To properly test the creation of the CHEF_NOL scenario, we need a test that creates a new PIN, like the NPP test.
    ScenarioMetaData chefsScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.CHEF_NOL, ScenarioTypeCodes.CHEF);
    Integer chefsScenarioNumber = chefsScenarioMetadata.getScenarioNumber();

    Scenario chefsScenario = null;
    try {
      chefsScenario = calculatorService.loadScenario(participantPin, programYear, chefsScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsScenario);
    
    assertEquals(ScenarioCategoryCodes.CHEF_NOL, chefsScenario.getScenarioCategoryCode());
    assertEquals(ScenarioTypeCodes.CHEF, chefsScenario.getScenarioTypeCode());
    

    // Delete the USER scenarios linked to this submission to cleanup the database.
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

    // Delete the submission if it exists, to cleanup the database
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
  public void fixValidationError() {

    Integer participantPin = 3693470;
    Integer programYear = 2023;
    String submissionGuid = "2e457fae-ebd3-43c7-b69e-256544c01253";
    
    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Jon Snow");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("individual");
    data.setProgramYear(programYear);
    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessNumber(null);
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NOL 3693470", validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
//  assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_NOT_STARTED), task.getStatusCode());
    assertEquals(formUserType + " Notice of Loss form was submitted but has validation errors:\n" + "\n"
        + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n"
        + "Participant Name: Jon Snow\n"
        + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n" + "Participant Type: individual\n"
        + "SIN Number: 123456789\n" + "Primary Farming Activity: Other: Giants", validationTask.getDescription());

    ChefsSubmission submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
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
    assertEquals("2023 NOL 3693470", validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_NOT_STARTED), validationTask.getStatusCode());
//    assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_COMPLETED), validationTask.getStatusCode());

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
    assertEquals("2023 NOL 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        "Primary Farming Activity: Other: Giants\n"
            + "Income Decrease Details: 80%. Dragons ate my livestock.\n"
            + "Expense Increase Details: 200%. Now paying protection money so dragons don't eat more livestock.",
        task.getDescription());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the USER scenarios linked to this submission to cleanup the database.
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

    submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertEquals(task.getActivityId(), submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    // Delete the submission to cleanup the database
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
  public void notForThisEnvironment() {

    String submissionGuid = "00000000-0000-0000-0008-000000000000";

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("corporation");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessNumber("1234 56789");
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("TEST");

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.OTHER_ENV, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    // Delete the submission to cleanup the database
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
  public void adminUseNotCompleted() {

    String submissionGuid = "00000000-0000-0000-0009-000000000000";

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

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setGrowerCorporationName("Targaryen Kingdom");
    data.setPhoneNumber("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setSubmitTo("Agristability");

    data.setParticipantType("corporation");
    data.setProgramYear(2023);
    data.setAgriStabilityPin(5070370);
    data.setSinNumber(null);
    data.setBusinessNumber("1234 56789");
    data.setPrimaryFarmingActivity("other");
    data.setPrimaryFarmingActivityOther("Giants");

    IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes = data.new IncomeBelowThresholdCheckboxes();
    incomeBelowThresholdCheckboxes.setYes(Boolean.TRUE);
    incomeBelowThresholdCheckboxes.setNo(Boolean.FALSE);

    data.setIncomeDecreaseDetails("80%. Dragons ate my livestock.");
    data.setExpenseIncreaseDetails("200%. Now paying protection money so dragons don't eat more livestock.");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment(null);

    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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

  // Commented out so this is not run accidentally.
  // This test was just in order to ignore the submissions created with old field
  // names.
  @Disabled 
  @Test
  public void cancelAllSubmissionsInFARM() {

    List<SubmissionListItemResource> itemResourceList = null;
    try {
      itemResourceList = chefsApiDao.getResourceList(submissionsUrl, SubmissionListItemResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(itemResourceList);
    assertTrue(itemResourceList.size() > 0);

    Map<String, SubmissionListItemResource> itemResourceMap = new HashMap<>();
    for (SubmissionListItemResource itemResource : itemResourceList) {
      assertNotNull(itemResource);
      String submissionGuid = itemResource.getSubmissionGuid();
      assertNotNull(submissionGuid);
      itemResourceMap.put(submissionGuid, itemResource);
    }

    Map<String, ChefsSubmission> submissionRecordMap = null;
    try {
      submissionRecordMap = chefsDatabaseDao.readSubmissionsByGuid(conn, itemResourceMap.keySet());
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRecordMap);

    for (String submissionGuid : submissionRecordMap.keySet()) {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getSubmissionGuid());
      assertNotNull(submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getRevisionCount());
    }

    List<ChefsSubmission> submissionUpdates = new ArrayList<>();
    List<ChefsSubmission> newSubmissions = new ArrayList<>();

    for (String submissionGuid : itemResourceMap.keySet()) {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);

      boolean isProcessed = submissionRec != null
          && (ChefsSubmissionStatusCodes.CANCELLED.equals(submissionRec.getSubmissionStatusCode())
              || ChefsSubmissionStatusCodes.PROCESSED.equals(submissionRec.getSubmissionStatusCode())
              || ChefsSubmissionStatusCodes.OTHER_ENV.equals(submissionRec.getSubmissionStatusCode()));

      if (!isProcessed) {
        ChefsSubmission submission = new ChefsSubmission();
        submission.setFormTypeCode(ChefsFormTypeCodes.NOL);
        submission.setSubmissionGuid(submissionGuid);
        submission.setSubmissionStatusCode(ChefsSubmissionStatusCodes.CANCELLED);

        if (submissionRec != null) {
          submission.setSubmissionId(submissionRec.getSubmissionId());
          submissionUpdates.add(submission);
        } else {
          newSubmissions.add(submission);
        }
      }
    }

    if (!newSubmissions.isEmpty()) {
      try {
        chefsDatabaseDao.createSubmissions(conn, newSubmissions, user);
      } catch (DataAccessException e) {
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

    if (!submissionUpdates.isEmpty()) {
      try {
        chefsDatabaseDao.updateSubmissions(conn, submissionUpdates, user);
      } catch (DataAccessException e) {
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

    Map<String, ChefsSubmission> updatedSubmissionRecordMap = null;
    try {
      updatedSubmissionRecordMap = chefsDatabaseDao.readSubmissionsByGuid(conn, itemResourceMap.keySet());
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(updatedSubmissionRecordMap);
    assertTrue(updatedSubmissionRecordMap.size() >= submissionRecordMap.size());

    for (String submissionGuid : updatedSubmissionRecordMap.keySet()) {
      ChefsSubmission submissionRec = updatedSubmissionRecordMap.get(submissionGuid);
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
      assertTrue(ChefsSubmissionStatusCodes.CANCELLED.equals(submissionRec.getSubmissionStatusCode())
          || ChefsSubmissionStatusCodes.PROCESSED.equals(submissionRec.getSubmissionStatusCode())
          || ChefsSubmissionStatusCodes.OTHER_ENV.equals(submissionRec.getSubmissionStatusCode()));
    }

  }

  @Test
  public void crud() {

    String[] submissionGuidArray = { "00000000-0000-0000-0000-000000000000", "00000000-0000-0000-0000-000000000001",
        "00000000-0000-0000-0000-000000000002" };
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
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.NOL);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.NOL);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.NOL);
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
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555001", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666001", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666000", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
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
  public void readNolSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.NOL);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.NOL, submission.getFormTypeCode());
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
    
    String existingSubmissionGuid = "e9bf8f4e-626b-40db-9cf1-8d509d03a8e5";
    String duplicateSubmissionGuid = "0a23f0e7-93e3-4742-be8f-051db476c044";

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(duplicateSubmissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Confirm that an NOL USER Scenario exists and is linked to a CHEFS NOL submission
    ScenarioMetaData nolScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.NOL, ScenarioTypeCodes.USER);
    assertNotNull(nolScenarioMetadata);

    // Set up the CHEFS Form submission data (not getting it from CHEFS).
    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NolSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(duplicateSubmissionGuid);

    data.setGrowerCorporationName("PETER PARKER");
    data.setProgramYear(programYear);
    data.setPhoneNumber("(250) 111-1111");
    data.setEmail("johnny@farm.ca");
    data.setParticipantType("individual");
    
    data.setAgriStabilityPin(participantPin);
    data.setSinNumber("987654321");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    
    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(duplicateSubmissionGuid);
    // Process the submission data
    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.NOL, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
  }


  @Disabled 
  @Test
  public void processNolSubmissionsFromRealChefsForms() {

    try {

      NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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

    SubmissionWrapperResource<NolSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NolSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NolSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    NolSubmissionProcessor processor = new NolSubmissionProcessor(conn, formUserType);
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

  private SubmissionParentResource<NolSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<NolSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    NolSubmissionDataResource data = new NolSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}
