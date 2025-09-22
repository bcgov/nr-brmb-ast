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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.struts.action.ActionMessages;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.InterimSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.common.CattleGrid;
import ca.bc.gov.srm.farm.chefs.resource.interim.ExpenseGrid;
import ca.bc.gov.srm.farm.chefs.resource.interim.IncomeGrid;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InventoryGridLivestock;
import ca.bc.gov.srm.farm.chefs.resource.interim.ProductionGrid;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmUser;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
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
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.UserService;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsInterimSubmissionTest extends ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsInterimSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.INTERIM;


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

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, InterimSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  }
  
  @Test
  public void getAdminUseSubmissions() {

    List<SubmissionListItemResource> submissionsList = null;
    try {
      submissionsList = chefsApiDao.getResourceList(submissionsUrl, SubmissionListItemResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionsList);
    assertTrue(submissionsList.size() > 0);
    
    ConfigurationUtility configUtil = ConfigurationUtility.getInstance();
    String webADEEnvironment = configUtil.getEnvironment();
    
    for (SubmissionListItemResource s : submissionsList) {

      String submissionGuid = s.getSubmissionGuid();
      assertNotNull(submissionGuid);

      String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
      assertNotNull(submissionUrl);
      
      String submissionResponseStr = null;
      try {
        submissionResponseStr = chefsApiDao.get(submissionUrl);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      logger.debug(submissionResponseStr);
      
      
      ObjectMapper jsonObjectMapper = new ObjectMapper();
      JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(SubmissionWrapperResource.class,
          ChefsSubmissionDataResource.class);
      
      SubmissionWrapperResource<ChefsSubmissionDataResource> submissionWrapper = null;
      try {
        submissionWrapper = chefsApiDao.parseResource(submissionResponseStr, parametricType);
        assertNotNull(submissionWrapper);
        logger.debug("submissionWrapper = {}",  submissionWrapper.toString());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
      assertNotNull(submissionWrapper);
      
      String chefsFormEnvironment = submissionWrapper.getSubmissionMetaData().getSubmission().getData().getEnvironment();
      if (! webADEEnvironment.equals(chefsFormEnvironment)) {
        logger.debug("Not the same, webADEEnvironment: {}, CHEFS form environment: {}", webADEEnvironment,  chefsFormEnvironment);
      } else {
        logger.debug("Same, webADEEnvironment: {}, CHEFS form environment: {}", webADEEnvironment,  chefsFormEnvironment);
      }
    }

  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "6c1cee6e-7327-4d30-b8d1-e1dc89ba8e13";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, InterimSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    InterimSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JONNY APPLESEED", data.getParticipantName());
    assertEquals("individual", data.getFarmType().getValue());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityAgriInvestPin());
    assertNull(data.getTelephone());
    assertEquals("999999999", data.getSinNumber());
    assertEquals(1, data.getCustomFedGrid().size());
    assertEquals(2, data.getAllowableExpensesGrid().size());
    assertEquals(2, data.getAllowableIncomeGrid().size());

    assertEquals("17", data.getMunicipalityCode().getValue());
    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());

    assertEquals(Double.valueOf(45.0), data.getProductiveCapacityLC104());
    assertNull(data.getProductiveCapacityLC145());
    assertNull(data.getProductiveCapacityLC123());
    assertNull(data.getProductiveCapacityLC4001());
    
    assertNull(data.getSpecifyOtherLivestock());
    assertNull(data.getProductiveCapacityLC109());
    assertNull(data.getProductiveCapacityLC141());
    assertNull(data.getProductiveCapacityLC105());
    assertNull(data.getProductiveCapacityLC106());
    assertNull(data.getProductiveCapacityLC144());

    assertEquals("9799", data.getAllowableExpensesGrid().get(0).getCategory().getValue());

    assertEquals(Double.valueOf(4564565.0), data.getAllowableExpensesGrid().get(0).getAmount());

    assertEquals(Long.valueOf("1709193600000"), (Long) data.getFiscalYearStartDate().toInstant().toEpochMilli());
    assertEquals(Long.valueOf("1732867200000"), (Long) data.getFiscalYearEndDate().toInstant().toEpochMilli());

  }

  @Test
  public void getSubmissionCorporation() {

    String submissionGuid = "7d93ad6f-37c3-4502-a037-fc0db3f4786d";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, InterimSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    logger.debug(submissionMetaData.toString());
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    InterimSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("LARK FARMS INC", data.getParticipantName());
    assertNull(data.getTelephone());
    assertNull(data.getEmail());
    assertEquals(Integer.valueOf("31415948"), data.getAgriStabilityAgriInvestPin());
    assertEquals("corporation", data.getFarmType().getValue());
    assertEquals("999012346", data.getBusinessTaxNumberBn());
    assertNull(data.getSinNumber());
    
    assertEquals(1, data.getAllowableExpensesGrid().size());
  
    assertEquals("9799", data.getAllowableExpensesGrid().get(0).getCategory().getValue());
    assertEquals(655, data.getAllowableExpensesGrid().get(0).getAmount().intValue());
    
    assertNull(data.getProductiveCapacityLC4001());

    assertEquals(Long.valueOf("1672473600000"), (Long) data.getFiscalYearStartDate().toInstant().toEpochMilli());
    assertEquals(Long.valueOf("1703923200000"), (Long) data.getFiscalYearEndDate().toInstant().toEpochMilli());

  }
  
  @Test
  public void pinNotFoundInFARM() {

    String submissionGuid = "00000000-0000-0001-0001-000000000000";
    
    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    data.setSinNumber("123456789");
    data.setAgriStabilityAgriInvestPin(12316589);
    data.setBusinessTaxNumberBn("");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Interim 12316589", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Interim form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);

  }

  @Test
  public void pinNotFoundInCRM() {

    int participantPin = 3773199;
    int programYear = 2022;
    String submissionGuid = "00000000-0000-0001-0002-000000000000";
    
    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setSinNumber(null);
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setBusinessTaxNumberBn("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setMunicipalityCode(new LabelValue("code", "41"));

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals(programYear + " Interim " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Interim form was submitted but has validation errors:\n" + "\n" + "- PIN not found in CRM.\n" + "\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void sinMismatch() {

    String submissionGuid = "00000000-0000-0001-0003-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(3693470);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Interim 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Interim form was submitted but has validation errors:\n\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }
  
  @Test
  public void sinMissingInFarm() {

    String submissionGuid = "00000000-0000-0001-0003-000000000000";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(31415976);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Interim 31415976", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " Interim form was submitted but has validation errors:\n\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }

  
  @Test
  public void businessNumberMissingInFarm() {

    String submissionGuid = "00000000-0000-0001-0004-000000000000";
    
    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(31415975);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Interim 31415975", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Interim form was submitted but has validation errors:\n" + "\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
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

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(5070370);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("1234 56789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Interim 5070370", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Interim form was submitted but has validation errors:\n" + "\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
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

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");

    data.setBusinessStructure("corporation");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse("2022-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(22503767);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("123456789");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals("2022 Interim 22503767", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " Interim form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n" + "\n"
        + "Participant Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
        + "Email: targaryen@game.of.thrones\n", task.getDescription());
  }

  @Test
  public void submissionAlreadyProcessed() {
    String submissionGuid = "d67b7e26-521b-42d9-9ef2-3d3295d0ca89";

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
      assertEquals(ChefsFormTypeCodes.INTERIM, processedSubmission.getFormTypeCode());
      assertEquals("PROCESSED", processedSubmission.getSubmissionStatusCode());

      processor.processSubmissions();

    } catch (ServiceException e) {
      e.printStackTrace();
      fail("ServiceException");
    }

  }

  @Test
  public void individualHappyPath() {

    Integer participantPin = 24107278;
    Integer programYear = 2023;
    String submissionGuid = "c37de765-19b9-4bf1-8cfd-3f85f0c2672f";

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the Interim USER Scenario to cleanup the database
    deleteUserScenario(programYear, programYearMetadata);
    
    // Set CHEFS Scenario submissionId to null if it exists, from a previous test run.
    setScenarioSubmissionId(programYear, programYearMetadata, null);

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid);

    // Set up the Interim CHEFS Form submission data (not getting it from CHEFS).
    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Johnny Appleseed");
    data.setTelephone("(250) 555-5555");
    data.setEmail("johnny@farm.ca");
    data.setBusinessStructure("individual");
    data.setReasonForApplying("I am applying for an interim because dragons ate my cattle.");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date startDate = format.parse("2023-01-01");
      Date endDate = format.parse("2023-12-31");
      data.setFiscalYearStart(startDate);
      data.setFiscalYearEnd(endDate);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("999999999");
    data.setBusinessTaxNumberBn("999999999");
    LabelValue municipalityCode = new LabelValue();
    municipalityCode.setValue("41");
    data.setMunicipalityCode(municipalityCode);

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    data.setProductiveCapacityLC104(12.8);
    data.setProductiveCapacityLC108(11.9);

    List<ExpenseGrid> expenseGridList = new ArrayList<>();
    IncomeGrid ig = new IncomeGrid();
    ig.setIncomeCategories(new LabelValue("Cattle raised from birth", "702"));
    ig.setEstimatedTotalReceived(231.0);
    data.setIncomeGrid(Collections.singletonList(ig));
    ExpenseGrid eg = new ExpenseGrid();
    eg.setExpenseCategories(new LabelValue("label", "264"));
    eg.setEstimatedTotalExpenseAmount(40000.00);
    expenseGridList.add(eg);
    
    eg = new ExpenseGrid();
    eg.setExpenseCategories(new LabelValue("Other Allowable Expenses (Specify)", "9896"));
    eg.setSpecify("storage");
    eg.setEstimatedTotalExpenseAmount(12.00);
    expenseGridList.add(eg);

    eg = new ExpenseGrid();
    eg.setExpenseCategories(new LabelValue("Other Allowable Expenses (Specify)", "9896"));
    eg.setSpecify("pollination");
    eg.setEstimatedTotalExpenseAmount(600.00);
    expenseGridList.add(eg);
  
    data.setExpenseGrid(expenseGridList);
    
    LabelValue commodity = new LabelValue("8062 - Beef; Replacement Heifers", "8062");
    InventoryGridLivestock igl = new InventoryGridLivestock();
    igl.setCommodity(commodity);
    igl.setOpeningInventoryUnits("30");
    igl.setEstimatedEndingInventoryUnits("");
    data.setInventoryGridLivestock(Collections.singletonList(igl));
    
    List<ProductionGrid> productionGridList = new ArrayList<>();
    ProductionGrid pg  = new ProductionGrid();
    pg.setCrop(new LabelValue("6932 - Mushrooms","6932"));
    pg.setProductiveArea(10.0);
    pg.setEstimatedProductionIntendedForSale(78.0);
    pg.setCropUnit(new LabelValue("Pounds","1"));
    productionGridList.add(pg);
    data.setProductiongrid(productionGridList);
    
    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);

    ScenarioMetaData interimScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.INTERIM, ScenarioTypeCodes.USER);
    Integer interimScenarioNumber = interimScenarioMetadata.getScenarioNumber();

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, interimScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(interimScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());

    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperations().get(0);

    HashMap<String, Double> productiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : fo.getProductiveUnitCapacities()) {
      productiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    logger.debug("productiveUnitsMap: " + productiveUnitsMap);
    assertEquals(Double.valueOf(12.8), productiveUnitsMap.get("104"));
    assertEquals(Double.valueOf(11.9), productiveUnitsMap.get("108"));

    HashMap<String, Double> localProductiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
      localProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    logger.debug("localProductiveUnitsMap: " + localProductiveUnitsMap);
    assertEquals(Double.valueOf(12.8), localProductiveUnitsMap.get("104"));
    assertEquals(Double.valueOf(11.9), localProductiveUnitsMap.get("108"));
    
    HashMap<String, Double> craProductiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
      craProductiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    logger.debug("craProductiveUnitsMap: " + craProductiveUnitsMap);
    assertEquals(0, fo.getCraProductiveUnitCapacities().size());
    
    assertEquals(1, fo.getCropItems().size());
    CropItem in = fo.getCropItems().get(0);
    assertEquals(Double.valueOf(78.0), in.getReportedQuantityProduced());

    IncomeExpense income = null;
    IncomeExpense expense = null;
    IncomeExpense otherSpecify = null;
    for (IncomeExpense ie : fo.getIncomeExpenses()) {
      if (ie.getIsExpense()) {
        if (ie.getLineItem().getLineItem() == 9896)
          otherSpecify = ie;
        else {
          expense = ie;
        }
      } else {
        income = ie;
      }
    }
    assertNotNull(income);
    assertEquals(income.getReportedAmount().intValue(), 231);
    assertEquals("Cattle raised from birth - Slaughter cattle", income.getLineItem().getDescription());
    assertFalse(income.getIsExpense());

    assertNotNull(expense);
    assertEquals(expense.getReportedAmount().intValue(), 40000L);
    assertEquals(expense.getLineItem().getLineItem().intValue(), 264);
    assertTrue(expense.getIsExpense());
    
    assertNotNull(otherSpecify);
      assertEquals(otherSpecify.getReportedAmount().intValue(), 612L);
      assertTrue(expense.getIsExpense());

    BenefitService benefitService = ServiceFactory.getBenefitService();
    ActionMessages calculationMessages = null;
    try {
      calculationMessages = benefitService.calculateBenefit(scenario, user);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(calculationMessages);

    // Update the scenario state to Verified.
    // This will trigger a benefit update in CRM.
    String newStateCode = ScenarioStateCodes.VERIFIED;
    String stateChangeReason = null;
    String newCategoryCode = ScenarioCategoryCodes.INTERIM;
    
    String userGuid = "BPITT000000000000000000000000000";
    String accountName = "BPITT";
    UserService userService = ServiceFactory.getUserService();
    FarmUser farmUser = null;
    try {
      farmUser = userService.getUserByUserGuid(userGuid);
      assertEquals(accountName, farmUser.getAccountName());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(farmUser);
    
    scenario.setVerifierUserId(farmUser.getUserId());
    
    String fifoResultType = null;
    try {
      calculatorService.updateScenario(scenario, newStateCode, stateChangeReason, newCategoryCode, USER_EMAIL, null, formUserType,
          ChefsFormTypeCodes.INTERIM, fifoResultType, user);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, interimScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertNotNull(scenario);
    assertNotNull(scenario.getScenarioId());
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(interimScenarioNumber, scenario.getScenarioNumber());
    assertEquals(submissionRec.getSubmissionId(), scenario.getChefsSubmissionId());
    assertEquals(ScenarioStateCodes.VERIFIED, scenario.getScenarioStateCode());
    assertEquals(ScenarioCategoryCodes.INTERIM, scenario.getScenarioCategoryCode());
    assertEquals(submissionGuid, scenario.getChefsSubmissionGuid());
    assertEquals(accountName, scenario.getVerifierAccountName());
    
    ScenarioMetaData chefsScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.CHEF_INTRM, ScenarioTypeCodes.CHEF);
    Integer chefsScenarioNumber = chefsScenarioMetadata.getScenarioNumber();

    Scenario chefsScenario = null;
    try {
      chefsScenario = calculatorService.loadScenario(participantPin, programYear, chefsScenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsScenario);
    
    assertEquals(submissionRec.getSubmissionId(), chefsScenario.getChefsSubmissionId());
    assertEquals(ScenarioCategoryCodes.CHEF_INTRM, chefsScenario.getScenarioCategoryCode());
    assertEquals(ScenarioTypeCodes.CHEF, chefsScenario.getScenarioTypeCode());

    deleteUserScenario(programYear, programYearMetadata);
    
    setScenarioSubmissionId(programYear, programYearMetadata, null);

    deleteSubmission(submissionGuid);
  }
  

  @Test
  public void fixValidationError() {

    Integer participantPin = 24107278;
    Integer programYear = 2023;
    String submissionGuid = "b9c01414-e17a-45de-84fa-e0113456208d";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Delete the Interim USER Scenario to cleanup the database
    deleteUserScenario(programYear, programYearMetadata);
    
    // Set CHEFS Scenario submissionId to null if it exists, from a previous test run.
    setScenarioSubmissionId(programYear, programYearMetadata, null);

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setParticipantName("Jon Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");

    data.setBusinessStructure("individual");

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse(programYear + "-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("123456789");
    data.setBusinessTaxNumberBn(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    LabelValue mc = new LabelValue();
    mc.setValue("15");
    mc.setLabel("Cariboo");
    data.setMunicipalityCode(mc);

    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
//    assertNotNull(validationTask.getAccountId());
    assertEquals("2023 Interim " + participantPin, validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
    assertEquals(
        formUserType + " Interim form was submitted but has validation errors:\n" + "\n"
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
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
    assertEquals("2023 Interim " + participantPin, validationTask.getSubject());
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

    // Delete the Interim USER Scenario to cleanup the database
    deleteUserScenario(programYear, programYearMetadata);
    
    // Set CHEFS Scenario submissionId to null if it exists, from a previous test run.
    setScenarioSubmissionId(programYear, programYearMetadata, null);

    submissionRec = null;
    try {
      submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec);

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid);
  }
  
  @Test
  public void correctEnvironment() {

    String submissionGuid = "c37de765-19b9-4bf1-8cfd-3f85f0c2672f";

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    String submissionResponseStr = null;
    try {
      submissionResponseStr = chefsApiDao.get(submissionUrl);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
    // Process the submission data
    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
    processor.setUser(user);

    Boolean isCorrectEnvironment = null;
    try {
      isCorrectEnvironment = processor.isCorrectEnvironment(submissionResponseStr);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    assertEquals(Boolean.TRUE, isCorrectEnvironment);
  }

  @Test
  public void wrongEnvironment() {

    String submissionGuid = "55d4718f-8e66-48c6-b06c-a261f060100b";
    
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    String submissionResponseStr = null;
    try {
      submissionResponseStr = chefsApiDao.get(submissionUrl);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
    // Process the submission data
    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
    processor.setUser(user);

    Boolean isCorrectEnvironment = null;
    try {
      isCorrectEnvironment = processor.isCorrectEnvironment(submissionResponseStr);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
    assertEquals(Boolean.FALSE, isCorrectEnvironment);
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
      assertEquals(1, submissionRec.getRevisionCount().intValue());
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
      assertEquals(1, submissionRec.getRevisionCount().intValue());
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
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.CANCELLED, submissionRec.getSubmissionStatusCode());
      assertEquals("55555555-5555-5555-5555-555555555002", submissionRec.getValidationTaskGuid());
      assertNull(submissionRec.getMainTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
      assertEquals(2, submissionRec.getRevisionCount().intValue());
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
  public void readInterimSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.INTERIM);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.INTERIM, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
  }

  @Test
  public void duplicateSubmission() {

    Integer programYear = 2023;
    Integer participantPin = 22275051;
    
    String existingSubmissionGuid = "87404be8-14da-4c0d-9992-d863d418bda5";
    String duplicateSubmissionGuid = "bc975e66-294c-4fbb-89eb-a8e517701466";

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(duplicateSubmissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertFalse(programYearMetadata.isEmpty());

    // Confirm that an Interim USER Scenario exists and is linked to a CHEFS Interim submission
    ScenarioMetaData interimScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.INTERIM, ScenarioTypeCodes.USER);
    assertNotNull(interimScenarioMetadata);

    // Set up the CHEFS Form submission data (not getting it from CHEFS).
    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    InterimSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(duplicateSubmissionGuid);

    data.setParticipantName("TARBOX, TSUKASA");
    data.setTelephone("(250) 111-1111");
    data.setEmail("johnny@farm.ca");
    data.setBusinessStructure("individual");
    
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      Date date = format.parse(programYear + "-12-31");
      data.setFiscalYearEnd(date);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }

    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber("999999999");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    
    {
      CattleGrid cattleGrid = new CattleGrid();
      cattleGrid.setCattleType(new LabelValue("8000 - Beef; Breeding; Bulls", "8000"));
      cattleGrid.setEndingInventoryCattle(100.0);
      data.setCattleGrid(Arrays.asList(cattleGrid));
    }

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(duplicateSubmissionGuid);
    // Process the submission data
    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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
    assertEquals(ChefsFormTypeCodes.INTERIM, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
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

    Integer participantPin = 4375663;
    Integer programYear = 2022;
    String submissionGuid = "393bcf4e-d396-4bca-972b-ec25eb4841d5";
    
    // Set to true if this submission has been processed before
    // and you want the Interim USER scenario and submission record
    // to be deleted before processing the submission again.
    boolean reprocess = true;

    if(reprocess) {
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      deleteUserScenario(programYear, programYearMetadata);
  
      deleteSubmission(submissionGuid);
    }


    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, InterimSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    InterimSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    InterimSubmissionProcessor processor = new InterimSubmissionProcessor(conn, formUserType);
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

  }

  private SubmissionParentResource<InterimSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<InterimSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    InterimSubmissionDataResource data = new InterimSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }
  
  private void deleteUserScenario(Integer programYear, List<ScenarioMetaData> programYearMetadata) {

    ScenarioMetaData interimScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.INTERIM, ScenarioTypeCodes.USER);
    List<ScenarioMetaData> interimScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata,
        programYear, ScenarioCategoryCodes.INTERIM, ScenarioTypeCodes.USER);
    for (ScenarioMetaData smd : interimScenarioMetadataList) {
      logger.debug(smd.toString());
    }

    if (interimScenarioMetadata != null) {
      deleteUserScenario(interimScenarioMetadata.getScenarioId());
    }
  }
  
  private void setScenarioSubmissionId(Integer programYear, List<ScenarioMetaData> programYearMetadata, Integer submissionId) {
    
    ScenarioMetaData chefsScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear,
        ScenarioCategoryCodes.CHEF_INTRM, ScenarioTypeCodes.CHEF);
    List<ScenarioMetaData> chefsScenarioMetadataList = ScenarioUtils.findScenariosByCategory(programYearMetadata,
        programYear, ScenarioCategoryCodes.CHEF_INTRM, ScenarioTypeCodes.CHEF);
    for (ScenarioMetaData smd : chefsScenarioMetadataList) {
      logger.debug(smd.toString());
    }

    if (chefsScenarioMetadata != null) {
      try {
        chefsDatabaseDao.updateScenarioSubmissionId(conn, chefsScenarioMetadata.getScenarioId(), submissionId, user);
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

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

}
