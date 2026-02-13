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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.forms.CoverageFormConstants;
import ca.bc.gov.srm.farm.chefs.processor.CoverageSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageCommodityGrid;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.OperationsAffected;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsCoverageSubmissionTest extends ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsCoverageSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.CN;


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

    SubmissionWrapperResource<CoverageSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CoverageSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "71581b60-4706-48f5-907d-8e640c57d3b2";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<CoverageSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CoverageSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    CoverageSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JONNY APPLESEED", data.getParticipantName());
    assertEquals("individual", data.getBusinessStructure());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityAgriInvestPin());
    assertEquals("(555) 555-5555", data.getTelephone());
    assertEquals("999999999", data.getSinNumber());
    assertEquals("9999 99999", data.getBusinessTaxNumber());
    assertNull(data.getOrigin());
    assertNull(data.getExternalMethod());

  }

  @Test
  public void getSubmissionCorporation() {

    Integer participantPin = 31415933;
    String submissionGuid = "c73ced7a-c766-4c94-b949-f2f4bf81b9de";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<CoverageSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CoverageSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    logger.debug(submissionMetaData.toString());
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    CoverageSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("LARK FARMS INC", data.getParticipantName());
    assertEquals("(604) 617-3472", data.getTelephone());
    assertEquals("LARK.FARMS@LARKFARM.COM", data.getEmail());
    assertEquals(participantPin, data.getAgriStabilityAgriInvestPin());
    assertEquals("corporation", data.getBusinessStructure());
    assertEquals("9990 12346", data.getBusinessTaxNumber());
    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("Hong-Yi", data.getSignatureFirstName());
    assertEquals("Wang", data.getSignatureLastName());
    assertEquals("2025/01/20", data.getSignatureDate());
    assertEquals("N/A", data.getHowDoYouKnowTheParticipant());
    assertNull(data.getSinNumber());
    assertNull(data.getOrigin());
    assertNull(data.getExternalMethod());

  }
  
  @Test
  public void pinNotFoundInFARM() {

    Integer programYear = 2023;
    Integer participantPin = 12316589;
    String submissionGuid = "00000000-0000-0001-0001-000000000COV";
    
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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");
    data.setBusinessStructure("individual");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setSinNumber("123456789");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setBusinessTaxNumber("");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Coverage Notice form was submitted but has validation errors:\n" + "\n"
        + "- No Verified Final found for 2022\n"
        + "- PIN not found in CRM.\n"
        + "- PIN not found in BCFARMS.\n" + "\n" + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n"
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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

    Integer programYear = 2023;
    Integer participantPin = 3713203;
    String submissionGuid = "00000000-0000-0001-0002-000000000COV";
    
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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setBusinessStructure("corporation");
    data.setSinNumber(null);
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setBusinessTaxNumber("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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

    assertNull(task.getAccountId());  // This PIN does not exist in CRM
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Coverage Notice form was submitted but has validation errors:\n\n"
        + "- No Verified Final found for 2022\n"
        + "- PIN not found in CRM.\n"
        + "\n"
        + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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

    String submissionGuid = "00000000-0000-0001-0003-000000000COV";
    Integer programYear = 2023;
    Integer participantPin = 3693470;

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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");
    data.setBusinessStructure("individual");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumber(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Coverage Notice form was submitted but has validation errors:\n\n"
            + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n"
            + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n",
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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
  public void sinMissingInFarm() {

    Integer programYear = 2023;
    Integer participantPin = 31415976;
    String submissionGuid = "00000000-0000-0001-0003-000000000COV";

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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");
    data.setBusinessStructure("individual");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setSinNumber("123456789");
    data.setBusinessTaxNumber(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Coverage Notice form was submitted but has validation errors:\n\n"
            + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"null\".\n" + "\n"
            + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n",
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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
  public void businessNumberMissingInFarm() {

    Integer programYear = 2023;
    Integer participantPin = 31415975;
    String submissionGuid = "00000000-0000-0001-0004-000000000COV";
    
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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");
    data.setBusinessStructure("corporation");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setSinNumber(null);
    data.setBusinessTaxNumber("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Coverage Notice form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n\n"
        + "Participant Name: Targaryen Kingdom\n"
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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

    Integer programYear = 2023;
    Integer participantPin = 5070370;
    String submissionGuid = "00000000-0000-0001-0006-000000000COV";

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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setSinNumber(null);
    data.setBusinessTaxNumber("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Coverage Notice form was submitted but has validation errors:\n" + "\n"
        + "- Field \"Business Number\" with value \"123456789RC0001\" does not match BCFARMS: \"999999999RC0001\"."
        + " Note that only the first nine digits are compared.\n" + "\n" + "Participant Name: Targaryen Kingdom\n"
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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

    Integer programYear = 2023;
    Integer participantPin = 22503767;
    String submissionGuid = "00000000-0000-0001-0005-000000000COV";

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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");
    data.setBusinessStructure("corporation");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber(null);
    data.setBusinessTaxNumber("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Coverage Notice form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
        + "Email: targaryen@game.of.thrones\n", task.getDescription());
  }

  @Disabled 
  @Test
  public void submissionAlreadyProcessed() {
    String submissionGuid = "d67b7e26-521b-42d9-9ef2-3d3295d0ca89";

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
    processor.setUser(user);
    try {
      processor.loadSubmissionsFromChefs();

      List<SubmissionListItemResource> submissionItems = processor.getSubmissionItems();

      List<SubmissionListItemResource> newSubmissionItems = submissionItems.stream()
          .filter(s -> s.getSubmissionGuid().equals(submissionGuid)).collect(Collectors.toList());
      processor.setSubmissionItems(newSubmissionItems);

      processor.loadSubmissionsFromDatabase();

      Map<String, ChefsSubmission> submissionRecordMap = processor.getSubmissionRecordMap();

      assertTrue(submissionRecordMap.size() > 0);
      assertEquals(1, processor.getSubmissionItems().size());

      ChefsSubmission processedSubmission = submissionRecordMap.get(submissionGuid);
      assertEquals(submissionGuid, processedSubmission.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CN, processedSubmission.getFormTypeCode());
      assertEquals("PROCESSED", processedSubmission.getSubmissionStatusCode());

      processor.processSubmissions();

    } catch (ServiceException e) {
      e.printStackTrace();
      fail("ServiceException");
    }

  }

  @Test
  public void individualHappyPath() {

    // TODO Switch to a different PIN. This PIN and program year are used by the Interim individualHappyPath test.
    Integer programYear = 2023;
    Integer participantPin = 24107278;
    String submissionGuid = "70335d05-aa2a-4ac6-b47e-3dad26ec9315";

    String userEmail = "ASTWORK@gov.bc.ca";

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the Coverage Notice USER Scenario if it exists, from a previous test run.
    ScenarioMetaData coverageScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
    List<ScenarioMetaData> coverageScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata,
        programYear, ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
    for (ScenarioMetaData smd : coverageScenarioMetadataList) {
      logger.debug(smd.toString());
    }

    if (coverageScenarioMetadata != null) {
      Integer existingCoverageScenarioId = coverageScenarioMetadata.getScenarioId();
      assertNotNull(existingCoverageScenarioId);

      try {
        calculatorDao.deleteUserScenario(conn, existingCoverageScenarioId);
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

    // Set up the Coverage CHEFS Form submission data (not getting it from CHEFS).
    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("TARBOX, TSUKASA");
    data.setTelephone("(250) 111-1111");
    data.setEmail("johnny@farm.ca");
    data.setBusinessStructure("individual");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));

    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("999999999");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    
    OperationsAffected operationsAffected = new OperationsAffected();
    operationsAffected.setDownsizing(true);
    operationsAffected.setExpansion(true);
    operationsAffected.setLargeScaleBreedingStockReplacement(true);
    operationsAffected.setChangeInCommoditiesOrVarieties(true);
    operationsAffected.setChangeInOwnership(true);
    operationsAffected.setDisasterOrDisease(true);
    operationsAffected.setOtherPleaseSpecify(true);
    data.setOperationsAffected(operationsAffected);
    data.setSpecifyOperation("other operations");
    
    data.setCompleted6MonthsOfFarmingActivities("yes");
    data.setHaveProductionInsurance("yes");
    data.setUninsuredCommodities("yes");
    data.setOwnershipInterestInAnotherFarm("yes");
    data.setProductionInsuranceNumbers(Arrays.asList("123456", "345543", "34555"));
    data.setCommoditiesFarmed(Arrays.asList(
        "berriesChristmasTrees",
        "grainLivestock",
        "hops",
        "nurseriesGreenhouses",
        "treefruitGrapes",
        "vegetables"));
    
    {
      CoverageCommodityGrid coverageCommodityGrid = new CoverageCommodityGrid();
      coverageCommodityGrid.setCommodity(new LabelValue("5032 - Apricots", "5032"));
      coverageCommodityGrid.setAcres(2.6);
      data.setTreeFruitGrid(Arrays.asList(coverageCommodityGrid));
    }
    {
      CoverageCommodityGrid coverageCommodityGrid = new CoverageCommodityGrid();
      coverageCommodityGrid.setCommodity(new LabelValue("104 - Cattle", "104"));
      coverageCommodityGrid.setAcres(5.99);
      data.setLivestockGrid(Arrays.asList(coverageCommodityGrid));
    }

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
    // Process the submission data
    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        "Commodities Farmed: berriesChristmasTrees, grainLivestock, hops, nurseriesGreenhouses, treefruitGrapes, vegetables",
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);

    coverageScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
    Integer coverageScenarioNumber = coverageScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, coverageScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(coverageScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());

    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperations().get(0);

    HashMap<String, Double> productiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
      productiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    logger.debug("productiveUnitsMap: " + productiveUnitsMap);
    assertEquals(Double.valueOf(5.99), productiveUnitsMap.get("104"));
    assertEquals(Double.valueOf(2.6), productiveUnitsMap.get("5032"));

    HashMap<String, Double> localProductiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
      localProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    logger.debug("localProductiveUnitsMap: " + localProductiveUnitsMap);
    assertEquals(Double.valueOf(5.99), localProductiveUnitsMap.get("104"));
    assertEquals(Double.valueOf(2.6), localProductiveUnitsMap.get("5032"));
    
    HashMap<String, Double> craProductiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
      craProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    logger.debug("craProductiveUnitsMap: " + craProductiveUnitsMap);
    assertEquals(0, fo.getCraProductiveUnitCapacities().size());

    // Update the scenario state to Verified.
    // This will trigger a benefit update in CRM.
    String newStateCode = ScenarioStateCodes.VERIFIED;
    String stateChangeReason = null;
    String newCategoryCode = ScenarioCategoryCodes.COVERAGE_NOTICE;
    String benefitTriageResultType = null;
    try {
      calculatorService.updateScenario(scenario, newStateCode, stateChangeReason, newCategoryCode, userEmail, null, formUserType,
          ChefsFormTypeCodes.CN, benefitTriageResultType, user);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, coverageScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertNotNull(scenario.getScenarioId());
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(coverageScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(ScenarioStateCodes.VERIFIED, scenario.getScenarioStateCode());
    assertEquals(ScenarioCategoryCodes.COVERAGE_NOTICE, scenario.getScenarioCategoryCode());
    assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());

    // Delete the Coverage USER Scenario to cleanup the database
//    try {
//      calculatorDao.deleteUserScenario(conn, scenario.getScenarioId());
//      conn.commit();
//    } catch (DataAccessException | SQLException e) {
//      e.printStackTrace();
//      try {
//        conn.rollback();
//      } catch (SQLException e1) {
//        e1.printStackTrace();
//        fail("Unexpected Exception");
//      }
//      fail("Unexpected Exception");
//    }
//
//    // Delete the submission if it exists, to cleanup the database
//    try {
//      chefsDatabaseDao.deleteSubmission(conn, submissionGuid);
//      conn.commit();
//    } catch (DataAccessException | SQLException e) {
//      e.printStackTrace();
//      try {
//        conn.rollback();
//      } catch (SQLException e1) {
//        e1.printStackTrace();
//        fail("Unexpected Exception");
//      }
//      fail("Unexpected Exception");
//    }
  }

  @Test
  public void fixValidationError() {

    // TODO Switch to a different PIN. This PIN and program year are used by the Interim individualHappyPath test.
    Integer participantPin = 24107278;
    Integer programYear = 2023;
    String submissionGuid = "70335d05-aa2a-4ac6-b47e-3dad26ec9315";

    deleteValidationErrorTasksBySubmissionGuid(submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the Coverage Scenario if it exists, from a previous test run.
    ScenarioMetaData coverageScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
    if (coverageScenarioMetadata != null) {
      Integer existingCoverageScenarioId = coverageScenarioMetadata.getScenarioId();
      assertNotNull(existingCoverageScenarioId);

      try {
        calculatorDao.deleteUserScenario(conn, existingCoverageScenarioId);
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

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");
    data.setBusinessStructure("individual");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumber(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setCommoditiesFarmed(Arrays.asList("berriesChristmasTrees"));

    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
    assertEquals(
        formUserType + " Coverage Notice form was submitted but has validation errors:\n" + "\n"
            + "- Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".\n" + "\n"
            + "Participant Name: Jon Snow\n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n",
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, validationTask.getSubject());
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

    assertNotNull(task);
    assertEquals(programYear + " " + CoverageFormConstants.FORM_LONG_NAME + " " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), validationTask.getStatusCode());
    assertEquals("Commodities Farmed: berriesChristmasTrees", task.getDescription());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the Coverage Scenario if it exists, from a previous test run.
    coverageScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
    if (coverageScenarioMetadata != null) {
      Integer existingCoverageScenarioId = coverageScenarioMetadata.getScenarioId();
      assertNotNull(existingCoverageScenarioId);

      try {
        calculatorDao.deleteUserScenario(conn, existingCoverageScenarioId);
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

    submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
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
  public void crud() {

    String[] submissionGuidArray = { "00000000-0000-0001-0000-000000000000", "00000000-0000-0001-0000-000000000001",
        "00000000-0000-0001-0000-000000000002" };
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
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.CN);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.CN);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.CN);
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
      assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555001", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666001", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555000", submissionRec.getValidationTaskGuid());
      assertEquals("66666666-6666-6666-6666-666666666000", submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[1]);
      assertEquals(submissionGuidArray[1], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNull(submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
    }
    {
      ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuidArray[2]);
      assertEquals(submissionGuidArray[2], submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
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
  public void duplicateSubmission() {

    Integer programYear = 2024;
    Integer participantPin = 95085414;
    
    String existingSubmissionGuid = "023a05b3-fd1e-4e2d-9fd4-528d5cf82e98";
    String duplicateSubmissionGuid = "fdb4676e-e0d7-4003-8af5-e181e945ce40";

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(duplicateSubmissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Confirm that a Coverage Notice USER Scenario exists and is linked to a CHEFS Coverage Notice submission
    ScenarioMetaData coverageScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
    assertNotNull(coverageScenarioMetadata);

    // Set up the Coverage CHEFS Form submission data (not getting it from CHEFS).
    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    CoverageSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(duplicateSubmissionGuid);

    data.setParticipantName("TARBOX, TSUKASA");
    data.setTelephone("(250) 111-1111");
    data.setEmail("johnny@farm.ca");
    data.setBusinessStructure("individual");
    data.setProgramYear(new LabelValue(programYear.toString(), programYear.toString()));

    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("999999999");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    
    OperationsAffected operationsAffected = new OperationsAffected();
    operationsAffected.setDownsizing(true);
    operationsAffected.setExpansion(true);
    operationsAffected.setLargeScaleBreedingStockReplacement(true);
    operationsAffected.setChangeInCommoditiesOrVarieties(true);
    operationsAffected.setChangeInOwnership(true);
    operationsAffected.setDisasterOrDisease(true);
    operationsAffected.setOtherPleaseSpecify(true);
    data.setOperationsAffected(operationsAffected);
    data.setSpecifyOperation("other operations");
    
    data.setCompleted6MonthsOfFarmingActivities("yes");
    data.setHaveProductionInsurance("yes");
    data.setUninsuredCommodities("yes");
    data.setOwnershipInterestInAnotherFarm("yes");
    data.setProductionInsuranceNumbers(Arrays.asList("123456", "345543", "34555"));
    data.setCommoditiesFarmed(Arrays.asList(
        "berriesChristmasTrees",
        "grainLivestock",
        "hops",
        "nurseriesGreenhouses",
        "treefruitGrapes",
        "vegetables"));
    
    {
      CoverageCommodityGrid coverageCommodityGrid = new CoverageCommodityGrid();
      coverageCommodityGrid.setCommodity(new LabelValue("5032 - Apricots", "5032"));
      coverageCommodityGrid.setAcres(2.6);
      data.setTreeFruitGrid(Arrays.asList(coverageCommodityGrid));
    }
    {
      CoverageCommodityGrid coverageCommodityGrid = new CoverageCommodityGrid();
      coverageCommodityGrid.setCommodity(new LabelValue("104 - Cattle", "104"));
      coverageCommodityGrid.setAcres(5.99);
      data.setLivestockGrid(Arrays.asList(coverageCommodityGrid));
    }

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(duplicateSubmissionGuid);
    // Process the submission data
    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.CN, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
  }

  @Test
  public void readCoverageSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.CN);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.CN, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
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

    Integer participantPin = 95085414;
    Integer programYear = 2024;
    String submissionGuid = "023a05b3-fd1e-4e2d-9fd4-528d5cf82e98";
    boolean reprocess = true; // Set to true if this submission has been processed before
                              // and you want the Coverage USER scenario and submission record
                              // to be deleted before processing the submission again.


    if(reprocess) {
      // Get the list of scenarios for the program year
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      // Delete the Coverage USER Scenario if it exists, from a previous test run.
      ScenarioMetaData coverageScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
          ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
      List<ScenarioMetaData> coverageScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata,
          programYear, ScenarioCategoryCodes.COVERAGE_NOTICE, ScenarioTypeCodes.USER);
      for (ScenarioMetaData smd : coverageScenarioMetadataList) {
        logger.debug(smd.toString());
      }
  
      if (coverageScenarioMetadata != null) {
        Integer existingCoverageScenarioId = coverageScenarioMetadata.getScenarioId();
        assertNotNull(existingCoverageScenarioId);
  
        try {
          calculatorDao.deleteUserScenario(conn, existingCoverageScenarioId);
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
    }


    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<CoverageSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CoverageSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    CoverageSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    CoverageSubmissionProcessor processor = new CoverageSubmissionProcessor(conn, formUserType);
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

  private SubmissionParentResource<CoverageSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<CoverageSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    CoverageSubmissionDataResource data = new CoverageSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }


  @Test
  public void bpu10YearAverage() {
  
    Integer participantPin = 4375666;
    Integer programYear = 2023;
    Integer scenarioNumber = 3;
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(scenario);
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      List<FarmingOperation> farmingOperations = refScenario.getFarmingYear().getFarmingOperations();
      for (FarmingOperation fo : farmingOperations) {
        List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacitiesForStructureChange();
        for (ProductiveUnitCapacity puc : pucs) {
          BasePricePerUnit bpu = puc.getBasePricePerUnit();
          List<BasePricePerUnitYear> bpuYears = bpu.getBasePricePerUnitYears();
          Set<Integer> yearSet = new HashSet<>();
          for (BasePricePerUnitYear basePricePerUnitYear : bpuYears) {
            Integer year = basePricePerUnitYear.getYear();
            assertTrue(year >= 2013, "Expected BPU year to be at least 2013");
            assertTrue(year <= 2022, "Expected BPU year to be no more than 2022");
            
            boolean added = yearSet.add(year);
            if(!added) {
              fail("Found duplicate BPU year");
            }
            
          }
          assertEquals(10, bpuYears.size());
        }
      }
    }
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }
  
}
