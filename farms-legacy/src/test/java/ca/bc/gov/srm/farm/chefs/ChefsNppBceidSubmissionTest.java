/**
 * Copyright (c) 2025,
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.NppSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppCommodityGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppCropGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppNurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionRequestDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.PartnershipInformation;
import ca.bc.gov.srm.farm.chefs.resource.npp.TreeFruitsFarmed;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsNppBceidSubmissionTest extends AbstractChefsNppSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsNppBceidSubmissionTest.class);


  @Test
  public void individualExistingParticipantPinNoEnwDueToEnrolmentStatus() {
    
    Integer participantPin = 844328088;
    Integer programYear = ProgramYearUtils.getCurrentCalendarYear();
    Integer enwYear = programYear - 2;
    String firstName = "Johnny";
    String lastName = "Appleseed";
    String sinNumber = "123456789";
    String submissionGuid = null;
    
    try {

      // NPP IDIR formId and formVersionId
      String formId = "c6d88314-b8d6-44f7-9fc1-1accc27e78b9";
      String formVersionId = "d9f37fd2-1892-4fa5-b0ad-788f56ec7d3f";
  
      NppSubmissionDataResource data = new NppSubmissionDataResource();
  
      data.setExistingAccount(true);
      data.setLateParticipant(false);
      data.setFirstName(firstName);
      data.setLastName(lastName);
  
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
  
      data.setEmail("johnny@farmer.ca");
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setTelephone("(648) 452-4357");
      data.setPostalCode("T5Y 4R4");
      data.setSinNumber(sinNumber);
      data.setAddress("1234 Home Road");
      data.setTownCity("Penticton");
      data.setProvince("BC");
      data.setFirstYearReporting("2022");
      data.setAccountingCode("cash");
      data.setLateEntry(false);
  
      data.setMunicipalityCode("37");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setThirdPartyFirstName("Paul");
      data.setThirdPartyLastName("Bunyan");
      data.setThirdPartyBusinessName("Lumber Inc");
      data.setThirdPartyEmail("Paul@lumber.inc");
      data.setThirdPartyAddress("345 Business Street");
      data.setThirdPartyTownCity("Kelowna");
      data.setThirdPartyProvince("BC");
      data.setThirdPartyPostalCode("V4N 0C0");
      data.setThirdPartyTelephone("(604) 555-5555");
      data.setThirdPartyFax("(604) 125-9338");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      data.setAccountingCode("cash");
      data.setWhatIsYourMainFarmingActivity("treefruit");
      data.setBlueberry36YearProductionAcres_5062(12.5);
      data.setCranberry4thYearProductionAcres_4994(60.6);
      data.setBroilersTurkeys_144(12.5);
      data.setFeederCattleFedOver900Lbs_106(90.0);
      data.setGala5YearProductionAcres_4826(22.0);
      data.setGala24YearProductionAcres_4824(99.5);
      data.setDidYouCompleteAProductionCycle("yes");
      data.setDidYouStartFarmingWithinTheLastSixMonths("yes");
      data.setWhatIsYourMainFarmingActivity("treefruit");
      data.setDoYouHaveMultipleOperations("no");
      data.setAgreeToTheTermsAndConditions(true);
  
      data.setFiscalYearEnd(Date.from(LocalDate.of(programYear, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setFiscalYearStart(Date.from(LocalDate.of(programYear, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  
      PartnershipInformation p1 = new PartnershipInformation("345345", null, "partner", "one", 10.0);
      PartnershipInformation p2 = new PartnershipInformation("122222", null, "partner", "two", 20.0);
      PartnershipInformation p3 = new PartnershipInformation("431444", "Tri-Partner Inc", null, null, 30.0);
      data.setPartnershipInformation(Arrays.asList(p1, p2, p3));
      data.setCommoditiesFarmed(Arrays.asList("treefruitGrapes", "nurseriesGreenhouses"));
      data.setBredCow_104(2.0);
      data.setChristmasTreesEstablishmentAcres(0.0);
      data.setChristmasTreesEstablishmentAcres1(1.2);
      data.setChristmasTreesEstablishmentAcres2(2.0);
      data.setChristmasTreesEstablishmentAcres3(3.0);
  
      TreeFruitsFarmed tff = new TreeFruitsFarmed();
      tff.setApples(true);
      tff.setCherries(false);
      tff.setGrapes(false);
      tff.setTreefruit(false);
      data.setTreefruitsFarmed(tff);
  
      NppNurseryGrid ng = new NppNurseryGrid();
      ng.setCommodity(new LabelValue("6930 - Maple Syrup", "6930"));
      ng.setUnitType("squareMeters");
      ng.setSquareMeters(7.0);
      data.setNurseryGrid(Arrays.asList(ng));
  
      SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
  
      SubmissionResource<NppSubmissionDataResource> submission = new SubmissionResource<>();
  
      submission.setData(data);
      submissionMetaData.setSubmission(submission);
      submissionMetaData.setSubmissionGuid(null);
  
      NppSubmissionRequestDataResource<NppSubmissionDataResource> request = new NppSubmissionRequestDataResource<>();
      request.setDraft(false);
      request.setCreatedBy(user);
      request.setCreatedAt(new Date().toString());
      request.setUpdatedBy(user);
      request.setUpdatedAt(new Date().toString());
      request.setSubmission(submission);
  
      String postSubmissionUrl = chefsConfig.postSubmissionUrl(formId, formVersionId);
      assertNotNull(postSubmissionUrl);
      try {
        submissionMetaData = chefsApiDao.postNppSubmission(postSubmissionUrl, request);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      NppSubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
  
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
      // Process the submission data
      NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, getFormUserType());
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
      processor.setBasicBCeIDFormsEnabled(true);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals("BCeID NPP submission", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals("This form was submitted through BCeID. Please review and verify the information.", task.getDescription());
  
      try {
        task = completeAndGetTask(crmConfig.getValidationErrorUrl(), task.getActivityId());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals("BCeID NPP submission", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), task.getStatusCode());
      assertEquals("This form was submitted through BCeID. Please review and verify the information.", task.getDescription());
  
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals(programYear + " NPP " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          "Primary Farming Activity: treefruit",
          task.getDescription());
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      List<ScenarioMetaData>programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData nppScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP,
          ScenarioTypeCodes.CHEF);
      Integer nppScenarioNumber = nppScenarioMetadata.getScenarioNumber();
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario chefScenario = null;
      try {
        chefScenario = calculatorService.loadScenario(participantPin, programYear, nppScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(chefScenario);
      assertEquals(participantPin, chefScenario.getClient().getParticipantPin());
      assertEquals(programYear, chefScenario.getYear());
      Integer nppDbSubmissionId = nppScenarioMetadata.getChefsFormSubmissionId();
      assertNotNull(nppDbSubmissionId);
      assertNotNull(nppScenarioNumber);
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      FarmingOperation fo = chefScenario.getFarmingYear().getFarmingOperationByNumber(1);
  
      for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
        logger.debug("getCraProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
      for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
        logger.debug("getLocalProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
  
      {
        List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();
        assertEquals(fo.getLocalProductiveUnitCapacities().size(), pucs.size());
    
        assertEquals(fo.getCraProductiveUnitCapacities().size(), 0);
      }
  
      List<FarmingOperationPartner> fops = fo.getFarmingOperationPartners();
      assertEquals(3, fops.size());
      {
        FarmingOperationPartner fop = fops.get(0);
        assertEquals(122222, fop.getParticipantPin());
        assertNull(fop.getCorpName());
        assertEquals("partner", fop.getFirstName());
        assertEquals("two", fop.getLastName());
        assertEquals(0.20, fop.getPartnerPercent().doubleValue());
      }
      {
        FarmingOperationPartner fop = fops.get(1);
        assertEquals(345345, fop.getParticipantPin());
        assertNull(fop.getCorpName());
        assertEquals("partner", fop.getFirstName());
        assertEquals("one", fop.getLastName());
        assertEquals(0.10, fop.getPartnerPercent().doubleValue());
      }
      {
        FarmingOperationPartner fop = fops.get(2);
        assertEquals(431444, fop.getParticipantPin());
        assertEquals("Tri-Partner Inc", fop.getCorpName());
        assertNull(fop.getFirstName());
        assertNull(fop.getLastName());
        assertEquals(0.30, fop.getPartnerPercent().doubleValue());
      }

      
      CrmAccountResource crmAccount = null;
      try {
        crmAccount = crmDao.getAccountByPin(participantPin);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmAccount);
      
      assertEquals(participantPin.toString(), crmAccount.getVsi_pin());
      assertEquals(sinNumber, crmAccount.getVsi_socialinsurancenumber());
      assertNull(crmAccount.getVsi_businessnumber());
      assertEquals(firstName, crmAccount.getVsi_firstname());
      assertEquals(lastName, crmAccount.getVsi_lastname());
      
      CrmProgramYearResource crmProgramYear = null;
      try {
        crmProgramYear = crmDao.getProgramYear(programYear);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmProgramYear);
      
      
      String accountId = crmAccount.getAccountid();
      String vsi_programyearid = crmProgramYear.getVsi_programyearid();
    
      CrmEnrolmentResource crmEnrolment = null;
      try {
        crmEnrolment = crmDao.getEnrolment(vsi_programyearid, accountId);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmEnrolment);
      assertEnrolmentStatusIsOneOf(crmEnrolment.getEnrolmentStatusCode(), CrmConstants.ENROLMENT_STATUS_CODE_UPDATED_ENROLMENT_FEES_CALCULATED);


      // ------------ ENW Scenario -------------------------------------------------------------------
      
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      
      ScenarioMetaData enwScenarioMetadata = ScenarioUtils.findLatestEnrolmentNoticeWorkflowScenario(programYearMetadata, enwYear);
      assertNull(enwScenarioMetadata);
      
    } finally {

      deleteUserScenarios(participantPin, enwYear);
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
      deleteSubmissionsFromChefs(submissionGuid);
    }

  }


  @Test
  public void individualNoPinThenAddPinHappyPath() {

    Integer participantPin = getUnusedParticipantPin();
    Integer programYear = ProgramYearUtils.getCurrentCalendarYear();
    Integer enwYear = programYear - 2;
    String firstName = "Johnny";
    String lastName = "Appleseed";
    String sinNumber = "123456789";
    String submissionGuid = null;

    try {

      // NPP IDIR formId and formVersionId
      String formId = "c6d88314-b8d6-44f7-9fc1-1accc27e78b9";
      String formVersionId = "d9f37fd2-1892-4fa5-b0ad-788f56ec7d3f";
  
      NppSubmissionDataResource data = new NppSubmissionDataResource();
  
      data.setExistingAccount(false);
      data.setLateParticipant(false);
      data.setFirstName(firstName);
      data.setLastName(lastName);
  
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
  
      data.setEmail("johnny@farmer.ca");
      data.setAgriStabilityAgriInvestPin(null);
      data.setTelephone("(648) 452-4357");
      data.setPostalCode("T5Y 4R4");
      data.setSinNumber(sinNumber);
      data.setAddress("1234 Home Road");
      data.setTownCity("Penticton");
      data.setProvince("BC");
      data.setFirstYearReporting("2022");
      data.setAccountingCode("cash");
      data.setLateEntry(false);
  
      data.setMunicipalityCode("37");
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
  
      data.setThirdPartyFirstName("Paul");
      data.setThirdPartyLastName("Bunyan");
      data.setThirdPartyBusinessName("Lumber Inc");
      data.setThirdPartyEmail("Paul@lumber.inc");
      data.setThirdPartyAddress("345 Business Street");
      data.setThirdPartyTownCity("Kelowna");
      data.setThirdPartyProvince("BC");
      data.setThirdPartyPostalCode("V4N 0C0");
      data.setThirdPartyTelephone("(604) 555-5555");
      data.setThirdPartyFax("(604) 125-9338");
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      data.setAccountingCode("cash");
      data.setWhatIsYourMainFarmingActivity("treefruit");
      data.setDidYouCompleteAProductionCycle("yes");
      data.setDidYouStartFarmingWithinTheLastSixMonths("yes");
      data.setWhatIsYourMainFarmingActivity("treefruit");
      data.setDoYouHaveMultipleOperations("no");
      data.setAgreeToTheTermsAndConditions(true);
  
      data.setFiscalYearEnd(Date.from(LocalDate.of(programYear, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setFiscalYearStart(Date.from(LocalDate.of(programYear, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  
      PartnershipInformation p1 = new PartnershipInformation("345345", null, "partner", "one", 10.0);
      PartnershipInformation p2 = new PartnershipInformation("122222", null, "partner", "two", 20.0);
      PartnershipInformation p3 = new PartnershipInformation("431444", "Tri-Partner Inc", null, null, 30.0);
      data.setPartnershipInformation(Arrays.asList(p1, p2, p3));
      data.setCommoditiesFarmed(Arrays.asList("nurseriesGreenhouses"));
  
      TreeFruitsFarmed tff = new TreeFruitsFarmed();
      tff.setApples(true);
      tff.setCherries(false);
      tff.setGrapes(false);
      tff.setTreefruit(false);
      data.setTreefruitsFarmed(tff);
  
      NppNurseryGrid ng = new NppNurseryGrid();
      ng.setCommodity(new LabelValue("6930 - Maple Syrup", "6930"));
      ng.setUnitType("squareMeters");
      ng.setSquareMeters(7.0);
      data.setNurseryGrid(Arrays.asList(ng));
  
      SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
  
      SubmissionResource<NppSubmissionDataResource> submission = new SubmissionResource<>();
  
      submission.setData(data);
      submissionMetaData.setSubmission(submission);
      submissionMetaData.setSubmissionGuid(null);
  
      NppSubmissionRequestDataResource<NppSubmissionDataResource> request = new NppSubmissionRequestDataResource<>();
      request.setDraft(false);
      request.setCreatedBy(user);
      request.setCreatedAt(new Date().toString());
      request.setUpdatedBy(user);
      request.setUpdatedAt(new Date().toString());
      request.setSubmission(submission);
  
      String postSubmissionUrl = chefsConfig.postSubmissionUrl(formId, formVersionId);
      assertNotNull(postSubmissionUrl);
      try {
        submissionMetaData = chefsApiDao.postNppSubmission(postSubmissionUrl, request);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      NppSubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
  
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
      // Process the submission data
      NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, getFormUserType());
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
      processor.setBasicBCeIDFormsEnabled(true);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(task);
      assertNull(task.getAccountId());
      assertEquals("BCeID NPP submission", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals("This form was submitted through BCeID. Please review and verify the information.", task.getDescription());
  
      try {
        task = completeAndGetTask(crmConfig.getValidationErrorUrl(), task.getActivityId());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(task);
      assertNull(task.getAccountId());
      assertEquals("BCeID NPP submission", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), task.getStatusCode());
      assertEquals("This form was submitted through BCeID. Please review and verify the information.", task.getDescription());
  
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(task);
      assertNull(task.getAccountId());
      assertEquals("NPP no PIN", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals("NPP client without a PIN", task.getDescription());
  
      try {
        task = completeAndGetTask(crmConfig.getValidationErrorUrl(), task.getActivityId());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(task);
      assertNull(task.getAccountId());
      assertEquals("NPP no PIN", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), task.getStatusCode());
      assertEquals("NPP client without a PIN", task.getDescription());
  
      // ------------ Update the form to add Participant PIN -----------------------------------------
      submissionMetaData.getSubmission().getData().setAgriStabilityAgriInvestPin(participantPin);
  
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals(programYear + " NPP " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          "Primary Farming Activity: treefruit",
          task.getDescription());
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      List<ScenarioMetaData>programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData nppScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP,
          ScenarioTypeCodes.CHEF);
      Integer nppScenarioNumber = nppScenarioMetadata.getScenarioNumber();
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario chefScenario = null;
      try {
        chefScenario = calculatorService.loadScenario(participantPin, programYear, nppScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(chefScenario);
      assertEquals(participantPin, chefScenario.getClient().getParticipantPin());
      assertEquals(programYear, chefScenario.getYear());
      Integer nppDbSubmissionId = nppScenarioMetadata.getChefsFormSubmissionId();
      assertNotNull(nppDbSubmissionId);
      assertNotNull(nppScenarioNumber);
  
      FarmingOperation fo = chefScenario.getFarmingYear().getFarmingOperationByNumber(1);
  
      for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
        logger.debug("getCraProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
      for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
        logger.debug("getLocalProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
  
      {
        List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();
        assertEquals(fo.getLocalProductiveUnitCapacities().size(), pucs.size());
    
        assertEquals(fo.getCraProductiveUnitCapacities().size(), 0);
      }
  
      List<FarmingOperationPartner> fops = fo.getFarmingOperationPartners();
      assertEquals(3, fops.size());
      {
        FarmingOperationPartner fop = fops.get(0);
        assertEquals(122222, fop.getParticipantPin());
        assertNull(fop.getCorpName());
        assertEquals("partner", fop.getFirstName());
        assertEquals("two", fop.getLastName());
        assertEquals(0.20, fop.getPartnerPercent().doubleValue());
      }
      {
        FarmingOperationPartner fop = fops.get(1);
        assertEquals(345345, fop.getParticipantPin());
        assertNull(fop.getCorpName());
        assertEquals("partner", fop.getFirstName());
        assertEquals("one", fop.getLastName());
        assertEquals(0.10, fop.getPartnerPercent().doubleValue());
      }
      {
        FarmingOperationPartner fop = fops.get(2);
        assertEquals(431444, fop.getParticipantPin());
        assertEquals("Tri-Partner Inc", fop.getCorpName());
        assertNull(fop.getFirstName());
        assertNull(fop.getLastName());
        assertEquals(0.30, fop.getPartnerPercent().doubleValue());
      }

      
      // ------------ ENW Scenario -------------------------------------------------------------------
      
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      
      ScenarioMetaData enwScenarioMetadata = ScenarioUtils.findLatestEnrolmentNoticeWorkflowScenario(programYearMetadata, enwYear);
      assertNotNull(enwScenarioMetadata);
      Integer enwScenarioNumber = enwScenarioMetadata.getScenarioNumber();
      Integer enwDbSubmissionId = enwScenarioMetadata.getChefsFormSubmissionId();
      assertNotNull(enwScenarioNumber);
      assertNotNull(enwDbSubmissionId);
      assertEquals(nppDbSubmissionId, enwDbSubmissionId);
      
      Scenario enwScenario = null;
      try {
        enwScenario = calculatorService.loadScenario(participantPin, enwYear, enwScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(enwScenario);
      Client client = enwScenario.getClient();
      assertNotNull(client);
      assertEquals(participantPin, client.getParticipantPin());
      assertEquals(sinNumber, client.getSin());
      assertNull(client.getBusinessNumber());
      assertEquals(enwYear, enwScenario.getYear());
      assertEquals(enwScenarioNumber, enwScenario.getScenarioNumber());
      assertEquals(ScenarioCategoryCodes.ENROLMENT_NOTICE_WORKFLOW, enwScenario.getScenarioCategoryCode());
      assertEquals(ScenarioTypeCodes.USER, enwScenario.getScenarioTypeCode());
      assertEquals(ScenarioStateCodes.ENROLMENT_NOTICE_COMPLETE, enwScenario.getScenarioStateCode());
      assertEquals(enwDbSubmissionId, enwScenario.getChefsSubmissionId());
      
      {
        FarmingOperation enwScenarioOperation = enwScenario.getFarmingYear().getFarmingOperationByNumber(1);
        assertEquals(0.40, enwScenarioOperation.getPartnershipPercent());
        
        List<ProductiveUnitCapacity> pucs = enwScenarioOperation.getProductiveUnitCapacities();
    
        Map<String, Double> productiveUnitsMap = buildProductiveUnitsMap(pucs);
        
        assertEquals(1, productiveUnitsMap.size());
        assertEquals(Double.valueOf(7.0), productiveUnitsMap.get("6930"));
      }
      
      
      EnwEnrolment enw = enwScenario.getEnwEnrolment();
      assertEquals(programYear, enw.getEnrolmentYear());
      assertEquals(EnwEnrolment.CALCULATION_TYPE_PROXY_MARGINS, enw.getEnrolmentCalculationTypeCode());
      assertEquals(Boolean.TRUE, enw.getHasBpus());
      assertEquals(Boolean.TRUE, enw.getHasProductiveUnits());
      assertEquals(Boolean.TRUE, enw.getCanCalculateProxyMargins());
      assertEquals(Double.valueOf(45.0), enw.getEnrolmentFee());
  
      
      CrmAccountResource crmAccount = null;
      try {
        crmAccount = crmDao.getAccountByPin(participantPin);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmAccount);
      
      assertEquals(participantPin.toString(), crmAccount.getVsi_pin());
      assertEquals(firstName, crmAccount.getVsi_firstname());
      assertEquals(lastName, crmAccount.getVsi_lastname());
      assertEquals(sinNumber, crmAccount.getVsi_socialinsurancenumber());
      
      CrmProgramYearResource crmProgramYear = null;
      try {
        crmProgramYear = crmDao.getProgramYear(programYear);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmProgramYear);


      String accountId = crmAccount.getAccountid();
      String vsi_programyearid = crmProgramYear.getVsi_programyearid();
    
      CrmEnrolmentResource crmEnrolment = null;
      try {
        crmEnrolment = crmDao.getEnrolment(vsi_programyearid, accountId);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmEnrolment);
      assertEnrolmentStatusIsOneOf(crmEnrolment.getEnrolmentStatusCode(),
          CrmConstants.ENROLMENT_STATUS_CODE_TO_BE_REVIEWED, CrmConstants.ENROLMENT_STATUS_CODE_INITIALIZED);

    } finally {
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
      deleteSubmissionsFromChefs(submissionGuid);
      deletePin(participantPin);
    }
    
  }


  @Test
  public void fillAllProductiveUnits() {

    Integer participantPin = 112969711;
    Integer programYear = ProgramYearUtils.getCurrentCalendarYear();
    Integer enwYear = programYear - 2;
    String corporationName = "CORP 112969711 TEST";
    String businessNumber = "999928888";
    String submissionGuid = null;

    deletePin(participantPin);

    try {
      NppSubmissionDataResource data = new NppSubmissionDataResource();
  
      data.setLateParticipant(false);
      data.setExistingAccount(true);
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_CORPORATION);
      farmType.setLabel("Corporation");
      data.setFarmType(farmType);
      data.setCorporationName(corporationName);
      data.setFirstNameCorporateContact(null);
      data.setLastNameCorporateContact(null);
      data.setFirstName(null);
      data.setLastName(null);
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setNoPin(false);
      data.setBusinessTaxNumberBn(businessNumber);
      data.setTrustBusinessNumber(null);
      data.setTrustNumber(null);
      data.setSinNumber(null);
      data.setBandNumber(null);
      data.setAddress("1234 Home Road");
      data.setTownCity("Penticton");
      data.setProvince("BC");
      data.setPostalCode("T5Y 4R4");
      data.setTelephone("(648) 452-4357");
      data.setEmail("johnny@farmer.ca");
  
      data.setDidYouStartFarmingWithinTheLastSixMonths("yes");
      data.setDidYouCompleteAProductionCycle("yes");
      data.setUnableToCompleteBecauseOfDisaster("no");
      data.setDoYouHaveMultipleOperations("no");
  
      data.setFirstYearReporting("2022");
      data.setMunicipalityCode("37");
      data.setWhatIsYourMainFarmingActivity("treefruit");
      data.setSpecifyOther(null);
      data.setFiscalYearStart(Date.from(LocalDate.of(programYear, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setFiscalYearEnd(Date.from(LocalDate.of(programYear, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setAccountingCode("cash");
      data.setProductionInsuranceGrowerNumber(Collections.emptyList());
  
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
        new NppCommodityGrid("5000 - Blackberries", "5000", 1.0, null, null),
        new NppCommodityGrid("5002 - Blueberries; Highbush", "5002", 2.0, null, null),
        new NppCommodityGrid("5006 - Cranberries", "5006", 3.0, null, null),
        new NppCommodityGrid("5007 - Currants; Black", "5007", 4.0, null, null),
        new NppCommodityGrid("5009 - Currants; Red", "5009", 5.0, null, null),
        new NppCommodityGrid("5010 - Elderberries", "5010", 6.0, null, null),
        new NppCommodityGrid("5012 - Gooseberries", "5012", 7.0, null, null),
        new NppCommodityGrid("5016 - Loganberries", "5016", 8.0, null, null),
        new NppCommodityGrid("5018 - Raspberries", "5018", 9.0, null, null),
        new NppCommodityGrid("5020 - Saskatoon Berries", "5020", 10.0, null, null),
        new NppCommodityGrid("5021 - Haskap", "5021", 11.0, null, null),
        new NppCommodityGrid("5022 - Seabuckthorn; Berries", "5022", 12.0, null, null),
        new NppCommodityGrid("5024 - Strawberries", "5024", 13.0, null, null)
      ));
  
      data.setTreeFruitGrid(Arrays.asList(
        new NppCommodityGrid("5014 - Grapes", "5014", 14.0, null, null),
        new NppCommodityGrid("5030 - Apples", "5030", 15.0, null, null),
        new NppCommodityGrid("5032 - Apricots", "5032", 16.0, null, null),
        new NppCommodityGrid("5033 - Fruit, Field Crops", "5033", 17.0, null, null),
        new NppCommodityGrid("5040 - Cherries; Sweet", "5040", 18.0, null, null),
        new NppCommodityGrid("5042 - Grapefruit", "5042", 19.0, null, null),
        new NppCommodityGrid("5044 - Kiwi Fruit", "5044", 20.0, null, null),
        new NppCommodityGrid("5046 - Lemons", "5046", 21.0, null, null),
        new NppCommodityGrid("5048 - Nectarines", "5048", 22.0, null, null),
        new NppCommodityGrid("5050 - Oranges", "5050", 23.0, null, null),
        new NppCommodityGrid("5052 - Peaches", "5052", 24.0, null, null),
        new NppCommodityGrid("5054 - Pears", "5054", 25.0, null, null),
        new NppCommodityGrid("5056 - Plums", "5056", 26.0, null, null),
        new NppCommodityGrid("7054 - Melons", "7054", 27.0, null, null),
        new NppCommodityGrid("5058 - Prunes", "5058", 28.0, null, null)
      ));
  
      data.setVegetableGrid(Arrays.asList(
        new NppCommodityGrid("6 - Borage", "6", 29.0, null, null),
        new NppCommodityGrid("5034 - Artichokes", "5034", 30.0, null, null),
        new NppCommodityGrid("6850 - Anise", "6850", 31.0, null, null),
        new NppCommodityGrid("6851 - Arugula", "6851", 32.0, null, null),
        new NppCommodityGrid("6852 - Basil", "6852", 33.0, null, null),
        new NppCommodityGrid("6854 - Borage", "6854", 34.0, null, null),
        new NppCommodityGrid("6855 - Chervil", "6855", 35.0, null, null),
        new NppCommodityGrid("6856 - Chives", "6856", 36.0, null, null),
        new NppCommodityGrid("6858 - Cilantro", "6858", 37.0, null, null),
        new NppCommodityGrid("6860 - Comfrey", "6860", 38.0, null, null),
        new NppCommodityGrid("6862 - Coriander", "6862", 39.0, null, null),
        new NppCommodityGrid("6864 - Cumin", "6864", 40.0, null, null),
        new NppCommodityGrid("6866 - Dill", "6866", 41.0, null, null),
        new NppCommodityGrid("6867 - Echinacea; Root Harvested", "6867", 42.0, null, null),
        new NppCommodityGrid("6868 - Echinacea", "6868", 43.0, null, null),
        new NppCommodityGrid("6869 - Echinacea; Establishment", "6869", 44.0, null, null),
        new NppCommodityGrid("6870 - Evening Primrose", "6870", 45.0, null, null),
        new NppCommodityGrid("6872 - Fennel", "6872", 46.0, null, null),
        new NppCommodityGrid("6874 - Fenugreek", "6874", 47.0, null, null),
        new NppCommodityGrid("6876 - Fireweed", "6876", 48.0, null, null),
        new NppCommodityGrid("6877 - Ginseng; Root Harvested", "6877", 49.0, null, null),
        new NppCommodityGrid("6878 - Ginseng", "6878", 50.0, null, null),
        new NppCommodityGrid("6879 - Ginseng; Establishment Stage", "6879", 51.0, null, null),
        new NppCommodityGrid("6880 - Marjoram", "6880", 52.0, null, null),
        new NppCommodityGrid("6881 - Lemon Balm", "6881", 53.0, null, null),
        new NppCommodityGrid("6882 - Mint", "6882", 54.0, null, null),
        new NppCommodityGrid("6883 - Lavender", "6883", 55.0, null, null),
        new NppCommodityGrid("6884 - Monarada", "6884", 56.0, null, null),
        new NppCommodityGrid("6886 - Oregano", "6886", 57.0, null, null),
        new NppCommodityGrid("6888 - Parsley", "6888", 58.0, null, null),
        new NppCommodityGrid("6890 - Rocket", "6890", 59.0, null, null),
        new NppCommodityGrid("6892 - Rosemary", "6892", 60.0, null, null),
        new NppCommodityGrid("6893 - Gingko Biloba", "6893", 61.0, null, null),
        new NppCommodityGrid("6894 - Sage", "6894", 62.0, null, null),
        new NppCommodityGrid("6896 - St. Johns Wort", "6896", 63.0, null, null),
        new NppCommodityGrid("6898 - Stevia", "6898", 64.0, null, null),
        new NppCommodityGrid("6900 - Summer Savory", "6900", 65.0, null, null),
        new NppCommodityGrid("6902 - Tarragon", "6902", 66.0, null, null),
        new NppCommodityGrid("6903 - Thyme", "6903", 67.0, null, null),
        new NppCommodityGrid("6904 - Water Cress", "6904", 68.0, null, null),
        new NppCommodityGrid("6920 - Kenaf", "6920", 69.0, null, null),
        new NppCommodityGrid("6922 - Okra", "6922", 70.0, null, null),
        new NppCommodityGrid("6932 - Mushrooms", "6932", 71.0, null, null),
        new NppCommodityGrid("6934 - Mustard Leaves", "6934", 72.0, null, null),
        new NppCommodityGrid("6946 - Sugar Beets", "6946", 73.0, null, null),
        new NppCommodityGrid("6970 - Beans; Adzuki", "6970", 74.0, null, null),
        new NppCommodityGrid("6972 - Beans; Broad", "6972", 75.0, null, null),
        new NppCommodityGrid("6974 - Beans; Green", "6974", 76.0, null, null),
        new NppCommodityGrid("6975 - Beans; Green; Organic", "6975", 77.0, null, null),
        new NppCommodityGrid("6976 - Beans; Jacob", "6976", 78.0, null, null),
        new NppCommodityGrid("6978 - Beans; Lima", "6978", 79.0, null, null),
        new NppCommodityGrid("6980 - Beans; Mung", "6980", 80.0, null, null),
        new NppCommodityGrid("6982 - Beans; Snap", "6982", 81.0, null, null),
        new NppCommodityGrid("6983 - Beans; Snap; Fresh", "6983", 82.0, null, null),
        new NppCommodityGrid("6984 - Beans; Soldier", "6984", 83.0, null, null),
        new NppCommodityGrid("6986 - Beans; Wax", "6986", 84.0, null, null),
        new NppCommodityGrid("6988 - Seabuckthorn; Leaves", "6988", 85.0, null, null),
        new NppCommodityGrid("6998 - Asparagus", "6998", 86.0, null, null),
        new NppCommodityGrid("7000 - Beets", "7000", 87.0, null, null),
        new NppCommodityGrid("7002 - Bok Choi", "7002", 88.0, null, null),
        new NppCommodityGrid("7004 - Broccoflower", "7004", 89.0, null, null),
        new NppCommodityGrid("7006 - Broccoli", "7006", 90.0, null, null),
        new NppCommodityGrid("7008 - Brussel Sprouts", "7008", 91.0, null, null),
        new NppCommodityGrid("7010 - Cabbage", "7010", 92.0, null, null),
        new NppCommodityGrid("7012 - Cabbage; Chinese", "7012", 93.0, null, null),
        new NppCommodityGrid("7014 - Carrots", "7014", 94.0, null, null),
        new NppCommodityGrid("7015 - Carrots; Organic", "7015", 95.0, null, null),
        new NppCommodityGrid("7016 - Cauliflower", "7016", 96.0, null, null),
        new NppCommodityGrid("7018 - Celery", "7018", 97.0, null, null),
        new NppCommodityGrid("7020 - Collards", "7020", 98.0, null, null),
        new NppCommodityGrid("7022 - Corn; Sweet", "7022", 99.0, null, null),
        new NppCommodityGrid("7024 - Cucumbers", "7024", 100.0, null, null),
        new NppCommodityGrid("7026 - Cucumbers; English", "7026", 101.0, null, null),
        new NppCommodityGrid("7030 - Egg Plant", "7030", 102.0, null, null),
        new NppCommodityGrid("7032 - Endive", "7032", 103.0, null, null),
        new NppCommodityGrid("7034 - Fiddle Heads", "7034", 104.0, null, null),
        new NppCommodityGrid("7035 - Edible Flowers", "7035", 105.0, null, null),
        new NppCommodityGrid("7036 - Garlic", "7036", 106.0, null, null),
        new NppCommodityGrid("7037 - Garlic; Organic", "7037", 107.0, null, null),
        new NppCommodityGrid("7038 - Gherkins", "7038", 108.0, null, null),
        new NppCommodityGrid("7039 - Hazelnuts", "7039", 109.0, null, null),
        new NppCommodityGrid("7040 - Horseradish; Condiment", "7040", 110.0, null, null),
        new NppCommodityGrid("7042 - Horseradish; Enzyme", "7042", 111.0, null, null),
        new NppCommodityGrid("7044 - Kohlrabi", "7044", 112.0, null, null),
        new NppCommodityGrid("7046 - Leeks", "7046", 113.0, null, null),
        new NppCommodityGrid("7047 - Leeks; Organic", "7047", 114.0, null, null),
        new NppCommodityGrid("7048 - Lettuce", "7048", 115.0, null, null),
        new NppCommodityGrid("7049 - Lettuce; Organic", "7049", 116.0, null, null),
        new NppCommodityGrid("7052 - Lettuce; Romaine", "7052", 117.0, null, null),
        new NppCommodityGrid("7056 - Onions", "7056", 118.0, null, null),
        new NppCommodityGrid("7057 - Onions; Organic", "7057", 119.0, null, null),
        new NppCommodityGrid("7058 - Parsnips", "7058", 120.0, null, null),
        new NppCommodityGrid("7060 - Peas; Green; Fresh", "7060", 121.0, null, null),
        new NppCommodityGrid("7062 - Peas; Sweet", "7062", 122.0, null, null),
        new NppCommodityGrid("7064 - Peppers; Green", "7064", 123.0, null, null),
        new NppCommodityGrid("7068 - Pumpkin", "7068", 125.0, null, null),
        new NppCommodityGrid("7069 - Gourds", "7069", 126.0, null, null),
        new NppCommodityGrid("7070 - Radish", "7070", 127.0, null, null),
        new NppCommodityGrid("7072 - Rhubarb", "7072", 128.0, null, null),
        new NppCommodityGrid("7074 - Rutabagas", "7074", 129.0, null, null),
        new NppCommodityGrid("7078 - Scorzonera", "7078", 130.0, null, null),
        new NppCommodityGrid("7080 - Shallots", "7080", 131.0, null, null),
        new NppCommodityGrid("7082 - Spinach", "7082", 132.0, null, null),
        new NppCommodityGrid("7083 - Spinach; Organic", "7083", 133.0, null, null),
        new NppCommodityGrid("7084 - Squash", "7084", 134.0, null, null),
        new NppCommodityGrid("7086 - Swiss Chard", "7086", 135.0, null, null),
        new NppCommodityGrid("7087 - Swiss Chard; Organic", "7087", 136.0, null, null),
        new NppCommodityGrid("7088 - Tomatoes", "7088", 137.0, null, null),
        new NppCommodityGrid("7094 - Turnips", "7094", 138.0, null, null),
        new NppCommodityGrid("7095 - Walnuts", "7095", 139.0, null, null),
        new NppCommodityGrid("7098 - Zucchini", "7098", 140.0, null, null),
        new NppCommodityGrid("7099 - Kale; Organic", "7099", 141.0, null, null),
        new NppCommodityGrid("7100 - Greens; Rapeseed", "7100", 142.0, null, null),
        new NppCommodityGrid("7200 - Potatoes", "7200", 143.0, null, null)
      ));
  
      data.setGrainGrid(Arrays.asList(
        new NppCommodityGrid("4784 - Hops", "4784", 144.0, null, null),
        new NppCommodityGrid("5100 - Barley", "5100", 145.0, null, null),
        new NppCommodityGrid("5370 - Beans, black, no. 1", "5370", 147.0, null, null),
        new NppCommodityGrid("5372 - Beans, black, no. 2", "5372", 148.0, null, null),
        new NppCommodityGrid("5374 - Beans, black, no. 3", "5374", 149.0, null, null),
        new NppCommodityGrid("5369 - Beans, black, organic", "5369", 150.0, null, null),
        new NppCommodityGrid("5375 - Beans, black, pedigreed seed", "5375", 151.0, null, null),
        new NppCommodityGrid("5376 - Beans, brown, no. 1", "5376", 152.0, null, null),
        new NppCommodityGrid("5378 - Beans, brown, no. 2", "5378", 153.0, null, null),
        new NppCommodityGrid("5380 - Beans, brown, no. 3", "5380", 154.0, null, null),
        new NppCommodityGrid("5382 - Beans, brown, organic", "5382", 155.0, null, null),
        new NppCommodityGrid("5384 - Beans, brown, pedigreed seed", "5384", 156.0, null, null),
        new NppCommodityGrid("5386 - Beans, cranberry, no. 1", "5386", 157.0, null, null),
        new NppCommodityGrid("5388 - Beans, cranberry, no. 2", "5388", 158.0, null, null),
        new NppCommodityGrid("5390 - Beans, cranberry, no. 3", "5390", 159.0, null, null),
        new NppCommodityGrid("5392 - Beans, cranberry, organic", "5392", 160.0, null, null),
        new NppCommodityGrid("5394 - Beans, cranberry, pedigreed seed", "5394", 161.0, null, null),
        new NppCommodityGrid("5468 - Beans, dry, yellow eye", "5468", 162.0, null, null),
        new NppCommodityGrid("5446 - Beans, feed", "5446", 163.0, null, null),
        new NppCommodityGrid("5396 - Beans, great northern, no. 1", "5396", 164.0, null, null),
        new NppCommodityGrid("5398 - Beans, great northern, no. 2", "5398", 165.0, null, null),
        new NppCommodityGrid("5400 - Beans, great northern, no. 3", "5400", 166.0, null, null),
        new NppCommodityGrid("5402 - Beans, great northern, organic", "5402", 167.0, null, null),
        new NppCommodityGrid("5404 - Beans, great northern, pedigreed seed", "5404", 168.0, null, null),
        new NppCommodityGrid("5406 - Beans, kidney, dark red, no. 1", "5406", 169.0, null, null),
        new NppCommodityGrid("5408 - Beans, kidney, dark red, no. 2", "5408", 170.0, null, null),
        new NppCommodityGrid("5410 - Beans, kidney, dark red, no. 3", "5410", 171.0, null, null),
        new NppCommodityGrid("5412 - Beans, kidney, dark red, organic", "5412", 172.0, null, null),
        new NppCommodityGrid("5414 - Beans, kidney, dark red, pedigreed seed", "5414", 173.0, null, null),
        new NppCommodityGrid("5416 - Beans, kidney, light red, no. 1", "5416", 174.0, null, null),
        new NppCommodityGrid("5418 - Beans, kidney, light red, no. 2", "5418", 175.0, null, null),
        new NppCommodityGrid("5420 - Beans, kidney, light red, no. 3", "5420", 176.0, null, null),
        new NppCommodityGrid("5422 - Beans, kidney, light red, organic", "5422", 177.0, null, null),
        new NppCommodityGrid("5424 - Beans, kidney, light red, pedigreed seed", "5424", 178.0, null, null),
        new NppCommodityGrid("5426 - Beans, pink, no. 1", "5426", 179.0, null, null),
        new NppCommodityGrid("5428 - Beans, pink, no. 2", "5428", 180.0, null, null),
        new NppCommodityGrid("5430 - Beans, pink, no. 3", "5430", 181.0, null, null),
        new NppCommodityGrid("5432 - Beans, pink, organic", "5432", 182.0, null, null),
        new NppCommodityGrid("5434 - Beans, pink, pedigreed seed", "5434", 183.0, null, null),
        new NppCommodityGrid("5436 - Beans, pinto, no. 1", "5436", 184.0, null, null),
        new NppCommodityGrid("5438 - Beans, pinto, no. 2", "5438", 185.0, null, null),
        new NppCommodityGrid("5440 - Beans, pinto, no. 3", "5440", 186.0, null, null),
        new NppCommodityGrid("5442 - Beans, pinto, organic", "5442", 187.0, null, null),
        new NppCommodityGrid("5444 - Beans, pinto, pedigreed seed", "5444", 188.0, null, null),
        new NppCommodityGrid("5448 - Beans, small red, no. 1", "5448", 189.0, null, null),
        new NppCommodityGrid("5450 - Beans, small red, no. 2", "5450", 190.0, null, null),
        new NppCommodityGrid("5452 - Beans, small red, no. 3", "5452", 191.0, null, null),
        new NppCommodityGrid("5454 - Beans, small red, organic", "5454", 192.0, null, null),
        new NppCommodityGrid("5456 - Beans, small red, pedigreed seed", "5456", 193.0, null, null),
        new NppCommodityGrid("5458 - Beans, white pea (navy), no. 1", "5458", 194.0, null, null),
        new NppCommodityGrid("5460 - Beans, white pea (navy), no. 2", "5460", 195.0, null, null),
        new NppCommodityGrid("5462 - Beans, white pea (navy), no. 3", "5462", 196.0, null, null),
        new NppCommodityGrid("5464 - Beans, white peas (navy), organic", "5464", 197.0, null, null),
        new NppCommodityGrid("5466 - Beans, white peas (navy), pedigreed seed", "5466", 198.0, null, null),
        new NppCommodityGrid("5240 - Buckwheat, no. 1", "5240", 199.0, null, null),
        new NppCommodityGrid("5242 - Buckwheat, no. 2", "5242", 200.0, null, null),
        new NppCommodityGrid("5244 - Buckwheat, no. 3", "5244", 201.0, null, null),
        new NppCommodityGrid("5246 - Buckwheat, organic", "5246", 202.0, null, null),
        new NppCommodityGrid("5248 - Buckwheat, pedigreed seed", "5248", 203.0, null, null),
        new NppCommodityGrid("5540 - Camelina", "5540", 204.0, null, null),
        new NppCommodityGrid("5542 - Camelina, organic", "5542", 205.0, null, null),
        new NppCommodityGrid("5544 - Camelina, pedigreed seed", "5544", 206.0, null, null),
        new NppCommodityGrid("5250 - Canary seed", "5250", 207.0, null, null),
        new NppCommodityGrid("5252 - Canary seed, organic", "5252", 208.0, null, null),
        new NppCommodityGrid("5254 - Canary seed, pedigreed seed", "5254", 209.0, null, null),
        new NppCommodityGrid("5261 - Canola & Other Oilseed", "5261", 210.0, null, null),
        new NppCommodityGrid("5290 - Caraway seed", "5290", 211.0, null, null),
        new NppCommodityGrid("5292 - Caraway seed, organic", "5292", 212.0, null, null),
        new NppCommodityGrid("5294 - Caraway seed, pedigreed seed", "5294", 213.0, null, null),
        new NppCommodityGrid("5300 - Chickpeas, desi, no. 1", "5300", 214.0, null, null),
        new NppCommodityGrid("5302 - Chickpeas, desi, no. 2", "5302", 215.0, null, null),
        new NppCommodityGrid("5303 - Chickpeas, desi, no. 3", "5303", 216.0, null, null),
        new NppCommodityGrid("5304 - Chickpeas, desi, organic", "5304", 217.0, null, null),
        new NppCommodityGrid("5306 - Chickpeas, desi, pedigreed seed", "5306", 218.0, null, null),
        new NppCommodityGrid("5330 - Chickpeas, feed", "5330", 219.0, null, null),
        new NppCommodityGrid("5310 - Chickpeas, large kabuli (average), no. 1", "5310", 220.0, null, null),
        new NppCommodityGrid("5312 - Chickpeas, large kabuli (average), no. 2", "5312", 221.0, null, null),
        new NppCommodityGrid("5314 - Chickpeas, large kabuli (average), no. 3", "5314", 222.0, null, null),
        new NppCommodityGrid("5316 - Chickpeas, large kabuli, organic", "5316", 223.0, null, null),
        new NppCommodityGrid("5318 - Chickpeas, large kabuli, pedigreed seed", "5318", 224.0, null, null),
        new NppCommodityGrid("5322 - Chickpeas, small kabuli, no. 1", "5322", 225.0, null, null),
        new NppCommodityGrid("5324 - Chickpeas, small kabuli, no. 2", "5324", 226.0, null, null),
        new NppCommodityGrid("5325 - Chickpeas, small kabuli, no. 3", "5325", 227.0, null, null),
        new NppCommodityGrid("5326 - Chickpeas, small kabuli, organic", "5326", 228.0, null, null),
        new NppCommodityGrid("5328 - Chickpeas, small kabuli, pedigreed seed", "5328", 229.0, null, null),
        new NppCommodityGrid("5340 - Corn, grain", "5340", 230.0, null, null),
        new NppCommodityGrid("5360 - Fababeans, feed", "5360", 231.0, null, null),
        new NppCommodityGrid("5350 - Fababeans, no. 1", "5350", 232.0, null, null),
        new NppCommodityGrid("5352 - Fababeans, no. 2", "5352", 233.0, null, null),
        new NppCommodityGrid("5354 - Fababeans, no. 3", "5354", 234.0, null, null),
        new NppCommodityGrid("5356 - Fababeans, organic", "5356", 235.0, null, null),
        new NppCommodityGrid("5358 - Fababeans, pedigreed seed", "5358", 236.0, null, null),
        new NppCommodityGrid("5550 - Flax", "5550", 237.0, null, null),
        new NppCommodityGrid("6826 - Harvest discount allowance", "6826", 238.0, null, null),
        new NppCommodityGrid("5750 - Hemp, fiber", "5750", 239.0, null, null),
        new NppCommodityGrid("5752 - Hemp, grain", "5752", 240.0, null, null),
        new NppCommodityGrid("5754 - Hemp, pedigreed seed", "5754", 241.0, null, null),
        new NppCommodityGrid("5070 - Kamut", "5070", 242.0, null, null),
        new NppCommodityGrid("5072 - Kamut, organic", "5072", 243.0, null, null),
        new NppCommodityGrid("5074 - Kamut, pedigreed seed", "5074", 244.0, null, null),
        new NppCommodityGrid("5822 - Lentils, black, organic", "5822", 245.0, null, null),
        new NppCommodityGrid("5760 - Lentils, dark green speckled, extra no. 3", "5760", 246.0, null, null),
        new NppCommodityGrid("5762 - Lentils, dark green speckled, no. 1", "5762", 247.0, null, null),
        new NppCommodityGrid("5764 - Lentils, dark green speckled, no. 2", "5764", 248.0, null, null),
        new NppCommodityGrid("5766 - Lentils, dark green speckled, no. 3", "5766", 249.0, null, null),
        new NppCommodityGrid("5768 - Lentils, dark green speckled, organic", "5768", 250.0, null, null),
        new NppCommodityGrid("5770 - Lentils, dark green speckled, pedigreed", "5770", 251.0, null, null),
        new NppCommodityGrid("5820 - Lentils, feed", "5820", 252.0, null, null),
        new NppCommodityGrid("5772 - Lentils, large green, extra no. 3", "5772", 253.0, null, null),
        new NppCommodityGrid("5774 - Lentils, large green, no. 1", "5774", 254.0, null, null),
        new NppCommodityGrid("5776 - Lentils, large green, no. 2", "5776", 255.0, null, null),
        new NppCommodityGrid("5778 - Lentils, large green, no. 3", "5778", 256.0, null, null),
        new NppCommodityGrid("5780 - Lentils, large green, organic", "5780", 257.0, null, null),
        new NppCommodityGrid("5782 - Lentils, large green, pedigreed seed", "5782", 258.0, null, null),
        new NppCommodityGrid("5784 - Lentils, medium green, extra no. 3", "5784", 259.0, null, null),
        new NppCommodityGrid("5786 - Lentils, medium green, no. 1", "5786", 260.0, null, null),
        new NppCommodityGrid("5788 - Lentils, medium green, no. 2", "5788", 261.0, null, null),
        new NppCommodityGrid("5790 - Lentils, medium green, no. 3", "5790", 262.0, null, null),
        new NppCommodityGrid("5792 - Lentils, medium green, organic", "5792", 263.0, null, null),
        new NppCommodityGrid("5794 - Lentils, medium green, pedigreed seed", "5794", 264.0, null, null),
        new NppCommodityGrid("5821 - Lentils, organic, pedigreed seed", "5821", 265.0, null, null),
        new NppCommodityGrid("5796 - Lentils, red, extra no. 3", "5796", 266.0, null, null),
        new NppCommodityGrid("5798 - Lentils, red, no. 1", "5798", 267.0, null, null),
        new NppCommodityGrid("5800 - Lentils, red, no. 2", "5800", 268.0, null, null),
        new NppCommodityGrid("5802 - Lentils, red, no. 3", "5802", 269.0, null, null),
        new NppCommodityGrid("5804 - Lentils, red, organic", "5804", 270.0, null, null),
        new NppCommodityGrid("5806 - Lentils, red, pedigreed seed", "5806", 271.0, null, null),
        new NppCommodityGrid("5808 - Lentils, small green, extra no. 3", "5808", 272.0, null, null),
        new NppCommodityGrid("5810 - Lentils, small green, no. 1", "5810", 273.0, null, null),
        new NppCommodityGrid("5812 - Lentils, small green, no. 2", "5812", 274.0, null, null),
        new NppCommodityGrid("5814 - Lentils, small green, no. 3", "5814", 275.0, null, null),
        new NppCommodityGrid("5816 - Lentils, small green, organic", "5816", 276.0, null, null),
        new NppCommodityGrid("5818 - Lentils, small green, pedigreed seed", "5818", 277.0, null, null),
        new NppCommodityGrid("5830 - Linola", "5830", 278.0, null, null),
        new NppCommodityGrid("5832 - Linola, organic", "5832", 279.0, null, null),
        new NppCommodityGrid("5834 - Linola, pedigreed seed", "5834", 280.0, null, null),
        new NppCommodityGrid("5836 - Linola, sample", "5836", 281.0, null, null),
        new NppCommodityGrid("5840 - Mixed grain", "5840", 282.0, null, null),
        new NppCommodityGrid("5841 - Mixed grain, organic", "5841", 283.0, null, null),
        new NppCommodityGrid("5850 - Mustard, brown, no. 1", "5850", 284.0, null, null),
        new NppCommodityGrid("5852 - Mustard, brown, no. 2", "5852", 285.0, null, null),
        new NppCommodityGrid("5854 - Mustard, brown, no. 3", "5854", 286.0, null, null),
        new NppCommodityGrid("5856 - Mustard, brown, no. 4", "5856", 287.0, null, null),
        new NppCommodityGrid("5858 - Mustard, brown, organic", "5858", 288.0, null, null),
        new NppCommodityGrid("5860 - Mustard, brown, pedigreed seed", "5860", 289.0, null, null),
        new NppCommodityGrid("5862 - Mustard, oriental, no. 1", "5862", 290.0, null, null),
        new NppCommodityGrid("5864 - Mustard, oriental, no. 2", "5864", 291.0, null, null),
        new NppCommodityGrid("5866 - Mustard, oriental, no. 3", "5866", 292.0, null, null),
        new NppCommodityGrid("5868 - Mustard, oriental, no. 4", "5868", 293.0, null, null),
        new NppCommodityGrid("5870 - Mustard, oriental, organic", "5870", 294.0, null, null),
        new NppCommodityGrid("5872 - Mustard, oriental, pedigreed seed", "5872", 295.0, null, null),
        new NppCommodityGrid("5874 - Mustard, sample", "5874", 296.0, null, null),
        new NppCommodityGrid("5876 - Mustard, yellow, no. 1", "5876", 297.0, null, null),
        new NppCommodityGrid("5878 - Mustard, yellow, no. 2", "5878", 298.0, null, null),
        new NppCommodityGrid("5880 - Mustard, yellow, no. 3", "5880", 299.0, null, null),
        new NppCommodityGrid("5882 - Mustard, yellow, no. 4", "5882", 300.0, null, null),
        new NppCommodityGrid("5884 - Mustard, yellow, organic", "5884", 301.0, null, null),
        new NppCommodityGrid("5886 - Mustard, yellow, pedigreed seed", "5886", 302.0, null, null),
        new NppCommodityGrid("5968 - Niger seed", "5968", 303.0, null, null),
        new NppCommodityGrid("5900 - Oats", "5900", 304.0, null, null),
        new NppCommodityGrid("5500 - Peas, dry, feed", "5500", 305.0, null, null),
        new NppCommodityGrid("5502 - Peas, dry, feed, organic", "5502", 306.0, null, null),
        new NppCommodityGrid("5504 - Peas, dry, food, green, no. 1", "5504", 307.0, null, null),
        new NppCommodityGrid("5506 - Peas, dry, food, green, no. 2", "5506", 308.0, null, null),
        new NppCommodityGrid("5508 - Peas, dry, food, green, organic", "5508", 309.0, null, null),
        new NppCommodityGrid("5510 - Peas, dry, food, yellow, no. 1", "5510", 310.0, null, null),
        new NppCommodityGrid("5512 - Peas, dry, food, yellow, no. 2", "5512", 311.0, null, null),
        new NppCommodityGrid("5514 - Peas, dry, food, yellow, organic", "5514", 312.0, null, null),
        new NppCommodityGrid("5516 - Peas, dry, maple", "5516", 313.0, null, null),
        new NppCommodityGrid("5518 - Peas, dry, marrowfat", "5518", 314.0, null, null),
        new NppCommodityGrid("5520 - Peas, dry, pedigreed seed", "5520", 315.0, null, null),
        new NppCommodityGrid("5076 - Quinoa", "5076", 316.0, null, null),
        new NppCommodityGrid("5078 - Quinoa, organic", "5078", 317.0, null, null),
        new NppCommodityGrid("5080 - Quinoa, pedigreed seed", "5080", 318.0, null, null),
        new NppCommodityGrid("5259 - Rapeseed, high erucic acid", "5259", 319.0, null, null),
        new NppCommodityGrid("5910 - Rye, fall", "5910", 320.0, null, null),
        new NppCommodityGrid("5912 - Rye, fall, organic", "5912", 321.0, null, null),
        new NppCommodityGrid("5914 - Rye, fall, pedigreed seed", "5914", 322.0, null, null),
        new NppCommodityGrid("5916 - Rye, spring", "5916", 323.0, null, null),
        new NppCommodityGrid("5918 - Rye, spring, organic", "5918", 324.0, null, null),
        new NppCommodityGrid("5920 - Rye, spring, pedigreed seed", "5920", 325.0, null, null),
        new NppCommodityGrid("5930 - Safflower, no. 1", "5930", 326.0, null, null),
        new NppCommodityGrid("5932 - Safflower, organic", "5932", 327.0, null, null),
        new NppCommodityGrid("5934 - Safflower, pedigreed seed", "5934", 328.0, null, null),
        new NppCommodityGrid("5936 - Safflower, sample", "5936", 329.0, null, null),
        new NppCommodityGrid("5907 - Screenings, all crops", "5907", 330.0, null, null),
        new NppCommodityGrid("5908 - Screenings, all crops, organic", "5908", 331.0, null, null),
        new NppCommodityGrid("5940 - Soybeans", "5940", 332.0, null, null),
        new NppCommodityGrid("5942 - Soybeans, organic", "5942", 333.0, null, null),
        new NppCommodityGrid("5944 - Soybeans, pedigreed seed", "5944", 334.0, null, null),
        new NppCommodityGrid("5946 - Soybeans, sample", "5946", 335.0, null, null),
        new NppCommodityGrid("5082 - Spelt", "5082", 336.0, null, null),
        new NppCommodityGrid("5084 - Spelt, organic", "5084", 337.0, null, null),
        new NppCommodityGrid("5086 - Spelt, pedigreed seed", "5086", 338.0, null, null),
        new NppCommodityGrid("5950 - Sunflower, confectionary, birdseed", "5950", 339.0, null, null),
        new NppCommodityGrid("5952 - Sunflower, confectionary, no. 1", "5952", 340.0, null, null),
        new NppCommodityGrid("5954 - Sunflower, confectionary, no. 2", "5954", 341.0, null, null),
        new NppCommodityGrid("5956 - Sunflower, feed", "5956", 342.0, null, null),
        new NppCommodityGrid("5964 - Sunflower, organic", "5964", 343.0, null, null),
        new NppCommodityGrid("5958 - Sunflower, pedigreed seed", "5958", 344.0, null, null),
        new NppCommodityGrid("5960 - Sunflowers, oilseed, no. 1", "5960", 345.0, null, null),
        new NppCommodityGrid("5962 - Sunflowers, oilseed, no. 2", "5962", 346.0, null, null),
        new NppCommodityGrid("5970 - Sunola", "5970", 347.0, null, null),
        new NppCommodityGrid("5972 - Sunola, organic", "5972", 348.0, null, null),
        new NppCommodityGrid("5974 - Sunola, pedigreed seed", "5974", 349.0, null, null),
        new NppCommodityGrid("5980 - Triticale", "5980", 350.0, null, null),
        new NppCommodityGrid("5982 - Triticale, organic", "5982", 351.0, null, null),
        new NppCommodityGrid("5984 - Triticale, pedigreed seed", "5984", 352.0, null, null),
        new NppCommodityGrid("6000 - Wheat", "6000", 353.0, null, null)
      ));
  
      data.setForageBasketGrid(Arrays.asList(
        new NppCropGrid("5560AlfalfaDehy", 1000.0, null),
        new NppCropGrid("5562Greenfeed", 1001.0, null),
        new NppCropGrid("5564HayAlfalfa", 1002.0, null),
        new NppCropGrid("5566HayAlfalfaOrganic", 1003.0, null),
        new NppCropGrid("5568HayAlfalfaBrome", 1004.0, null),
        new NppCropGrid("5570HayAlfalfaGrass", 1005.0, null),
        new NppCropGrid("5572HayClover", 1006.0, null),
        new NppCropGrid("5574HayGrass", 1007.0, null),
        new NppCropGrid("5576HayOther", 1008.0, null),
        new NppCropGrid("5578HaySlough", 1009.0, null),
        new NppCropGrid("5579HayTimothy", 1010.0, null),
        new NppCropGrid("5580Haylage", 1011.0, null),
        new NppCropGrid("5582Silage", 1012.0, null),
        new NppCropGrid("5583SilageCorn", 1013.0, null),
        new NppCropGrid("5586Straw", 1014.0, null),
        new NppCropGrid("5588SwathGrazing", 1015.0, null)
      ));
  
      data.setForageSeedGrid(Arrays.asList(
        new NppCropGrid("5600AlfalfaCommonSeed", 1016.0, null),
        new NppCropGrid("5603AlfalfaOrganicSeed", 1017.0, null),
        new NppCropGrid("5602AlfalfaPedigreedSeed", 1018.0, null),
        new NppCropGrid("5604BentgrassCommonSeed", 1019.0, null),
        new NppCropGrid("5606BentgrassPedigreedSeed", 1020.0, null),
        new NppCropGrid("5608BirdsfootTrefoilCommonSeed", 1021.0, null),
        new NppCropGrid("5610BirdsfoodTrefoilPredigreedSeed", 1022.0, null),
        new NppCropGrid("5736BlackMedic", 1023.0, null),
        new NppCropGrid("5612BlueGramaCommonSeed", 1024.0, null),
        new NppCropGrid("5614BlueGramaPedigreedSeed", 1025.0, null),
        new NppCropGrid("5724BromesMeadowCommonSeed", 1026.0, null),
        new NppCropGrid("5726BromesMeadowPedigreedSeed", 1027.0, null),
        new NppCropGrid("5723BromesSmoothCommonSeed", 1028.0, null),
        new NppCropGrid("5725BromesSmoothPedigreedSeed", 1029.0, null),
        new NppCropGrid("5729ChicklingVetchSeed", 1030.0, null),
        new NppCropGrid("5620CloverAlsikeCommonSeed", 1031.0, null),
        new NppCropGrid("5622CloverAlsikePedigreedSeed", 1032.0, null),
        new NppCropGrid("5624CloverKuraCommonSeed", 1033.0, null),
        new NppCropGrid("5619CloverOrganicSeed", 1034.0, null),
        new NppCropGrid("5628CloverOtherCommonSeed", 1035.0, null),
        new NppCropGrid("5732CloverRedCommonSeedDoubleCut", 1036.0, null),
        new NppCropGrid("5731CloverRedCommonSeedSingleCut", 1037.0, null),
        new NppCropGrid("5734CloverRedPedigreedSeedDoubleCut", 1038.0, null),
        new NppCropGrid("5733CloverRedPedigreedSeedSingleCut", 1039.0, null),
        new NppCropGrid("5636CloverSweetCommonSeed", 1040.0, null),
        new NppCropGrid("5638CloverSweetPedigreedSeed", 1041.0, null),
        new NppCropGrid("5640FescueMeadowCommonSeed", 1042.0, null),
        new NppCropGrid("5642FescueMeadowPedigreedSeed", 1043.0, null),
        new NppCropGrid("5727FescueRedCreepingCommonSeed", 1044.0, null),
        new NppCropGrid("5728FescueRedCreepingPedigreedSeed", 1045.0, null),
        new NppCropGrid("5644FescueTallForageCommonSeed", 1046.0, null),
        new NppCropGrid("5646FescueTallForagePedigreedSeed", 1047.0, null),
        new NppCropGrid("5648FescueTallTurfCommonSeed", 1048.0, null),
        new NppCropGrid("5650FescueTallTurfPedigreedSeed", 1049.0, null),
        new NppCropGrid("5652FescuesOtherCommonSeed", 1050.0, null),
        new NppCropGrid("5671GrassFowlBlue", 1051.0, null),
        new NppCropGrid("5656GrassGreenNeedleCommonSeed", 1052.0, null),
        new NppCropGrid("5658GrassGreenNeedlePedigreedSeed", 1053.0, null),
        new NppCropGrid("5660GrassIndianCommonSeed", 1054.0, null),
        new NppCropGrid("5662GrassIndianPedigreedSeed", 1055.0, null),
        new NppCropGrid("5664GrassJuneCommonSeed", 1056.0, null),
        new NppCropGrid("5666GrassJunePedigreedSeed", 1057.0, null),
        new NppCropGrid("5668GrassKentuckyBlueCommonSeed", 1058.0, null),
        new NppCropGrid("5670GrassKentuckyBluePedigreedSeed", 1059.0, null),
        new NppCropGrid("5672GrassOrchardCommonSeed", 1060.0, null),
        new NppCropGrid("5674GrassOrchardPedigreedSeed", 1061.0, null),
        new NppCropGrid("5676GrassOtherCommonSeed", 1062.0, null),
        new NppCropGrid("5680GrassReedCanaryCommonSeed", 1063.0, null),
        new NppCropGrid("5682GrassReedCanaryPedigreedSeed", 1064.0, null),
        new NppCropGrid("5684GrassSwitchCommonSeed", 1065.0, null),
        new NppCropGrid("5686GrassSwitchPedigreedSeed", 1066.0, null),
        new NppCropGrid("5688GrassTuftedHairCommonSeed", 1067.0, null),
        new NppCropGrid("5690GrassTuftedHairPedigreedSeed", 1068.0, null),
        new NppCropGrid("5592GrassWheatCrestedCommonSeed", 1069.0, null),
        new NppCropGrid("5596GrassWheatCrestedPedigreedSeed", 1070.0, null),
        new NppCropGrid("5593GrassWheatIntermediateCommonSeed", 1071.0, null),
        new NppCropGrid("5597GrassWheatIntermediatePedigreedSeed", 1072.0, null),
        new NppCropGrid("5595GrassWheatPubescentCommonSeed", 1073.0, null),
        new NppCropGrid("5599GrassWheatPubescentPedigreedSeed", 1074.0, null),
        new NppCropGrid("5594GrassWheatSlenderCommonSeed", 1075.0, null),
        new NppCropGrid("5598GrassWheatSlenderPedigreedSeed", 1076.0, null),
        new NppCropGrid("5699MilkvetchAmerican", 1077.0, null),
        new NppCropGrid("5696MilkvetchCanada", 1078.0, null),
        new NppCropGrid("5698MilkvetchPedigreedSeed", 1079.0, null),
        new NppCropGrid("5700MilletCommonSeed", 1080.0, null),
        new NppCropGrid("5702MilletPedigreedSeed", 1081.0, null),
        new NppCropGrid("5693NativeWheatgrassNorthern", 1082.0, null),
        new NppCropGrid("5695NativeWheatgrassStreambank", 1083.0, null),
        new NppCropGrid("5691NativeWheatgrassWestern", 1084.0, null),
        new NppCropGrid("5683NativeBluestemBig", 1085.0, null),
        new NppCropGrid("5685NativeBluestemLittle", 1086.0, null),
        new NppCropGrid("5659NativeNeedleAndThread", 1087.0, null),
        new NppCropGrid("5689NativePrairieChordgrass", 1088.0, null),
        new NppCropGrid("5687NativePrairieSandreed", 1089.0, null),
        new NppCropGrid("5615NativeSideoatsGrama", 1090.0, null),
        new NppCropGrid("5742NigerThistle", 1091.0, null),
        new NppCropGrid("5704RyegrassAnnualCommonSeed", 1092.0, null),
        new NppCropGrid("5706RyegrassAnnualPedigreedSeed", 1093.0, null),
        new NppCropGrid("5709RyegrassNativeCanadianWild", 1094.0, null),
        new NppCropGrid("5708RyegrassPerennialCommonSeed", 1095.0, null),
        new NppCropGrid("5714RyegrassPerennialPedigreedSeed", 1096.0, null),
        new NppCropGrid("5716SainfoinCommonSeed", 1097.0, null),
        new NppCropGrid("5718SainfoinPedigreedSeed", 1098.0, null),
        new NppCropGrid("5730SloughgrassAmerican", 1099.0, null),
        new NppCropGrid("5720TimothyCommonSeed", 1100.0, null),
        new NppCropGrid("5722TimothyPedigreedSeed", 1101.0, null)
      ));
  
      data.setNurseryGrid(Arrays.asList(
        new NppNurseryGrid("6930 - Maple Syrup", "6930", null, null, 354.0),
        new NppNurseryGrid("6931 - Maple Syrup; Vacuum", "6931", null, null, 355.0),
        new NppNurseryGrid("6937 - Sod", "6937", null, null, 356.0),
        new NppNurseryGrid("6938 - Radish Seed; Fodder", "6938", null, null, 357.0),
        new NppNurseryGrid("6940 - Radish Seed; Organic", "6940", null, null, 358.0),
        new NppNurseryGrid("6941 - Sod; Acres Seeded", "6941", null, null, 359.0),
        new NppNurseryGrid("6942 - Sod; Mineral Based", "6942", null, null, 360.0),
        new NppNurseryGrid("6943 - Sod; Acres Growing", "6943", null, null, 361.0),
        new NppNurseryGrid("6944 - Sod; Peat Moss-Based", "6944", null, null, 362.0),
        new NppNurseryGrid("6945 - Sod; Acres Harvested", "6945", null, null, 363.0),
        new NppNurseryGrid("6949 - Flowers; Fresh Cut; Greenhouse", "6949", null, null, 364.0),
        new NppNurseryGrid("6950 - Bedding Plants; Flowers", "6950", null, null, 365.0),
        new NppNurseryGrid("6951 - Flowers; Fresh Cut", "6951", null, null, 366.0),
        new NppNurseryGrid("6952 - Bedding Plants; Vegetables", "6952", null, null, 367.0),
        new NppNurseryGrid("6953 - Shrubs; Nursery", "6953", null, null, 368.0),
        new NppNurseryGrid("6954 - Trees; Cultivated Christmas", "6954", null, null, 369.0),
        new NppNurseryGrid("6955 - Trees; Nursery", "6955", null, null, 370.0),
        new NppNurseryGrid("6956 - Strawberry Plants", "6956", null, null, 371.0),
        new NppNurseryGrid("6957 - Raspberry Plants (Canes)", "6957", null, null, 372.0),
        new NppNurseryGrid("6958 - Trees; Cedar; Hedging", "6958", null, null, 373.0),
        new NppNurseryGrid("6959 - Bedding Plants", "6959", null, null, 374.0),
        new NppNurseryGrid("6965 - Christmas Trees", "6965", null, null, 375.0),
        new NppNurseryGrid("7028 - Cucumbers; Greenhouse", "7028", null, null, 376.0),
        new NppNurseryGrid("7066 - Peppers; Greenhouse", "7066", null, null, 377.0),
        new NppNurseryGrid("7073 - Rhubarb Plants", "7073", null, null, 378.0),
        new NppNurseryGrid("7076 - Salsify", "7076", null, null, 379.0),
        new NppNurseryGrid("7090 - Tomatoes; Cherry; Greenhouse", "7090", null, null, 380.0),
        new NppNurseryGrid("7092 - Tomatoes; Greenhouse", "7092", null, null, 381.0),
        new NppNurseryGrid("7101 - Plants; Potted", "7101", null, null, 382.0),
        new NppNurseryGrid("7102 - Perennials; Plugs/Liners", "7102", null, null, 383.0),
        new NppNurseryGrid("7103 - Perennials  2.5 inch", "7103", null, null, 384.0),
        new NppNurseryGrid("7104 - Perennials; 4 inch", "7104", null, null, 385.0),
        new NppNurseryGrid("7106 - Perennials; 1 gallon; Indoor", "7106", null, null, 386.0),
        new NppNurseryGrid("7108 - Perennials; 2 gallon; Indoor", "7108", null, null, 387.0),
        new NppNurseryGrid("7110 - Perennials; 1 gallon; Field/Container", "7110", null, null, 388.0),
        new NppNurseryGrid("7112 - Perennials; 2 gallon; Field/Container", "7112", null, null, 389.0),
        new NppNurseryGrid("7114 - Trees and Shrubs; Plugs/Liners", "7114", null, null, 390.0),
        new NppNurseryGrid("7115 - Trees and shrubs; high value ball and burlap field stock", "7115", null, null, 391.0),
        new NppNurseryGrid("7116 - Trees and Shrubs; 4 inch", "7116", null, null, 392.0),
        new NppNurseryGrid("7117 - Trees and Shrubs; Ball and Burlap; Field Stock", "7117", null, null, 393.0),
        new NppNurseryGrid("7118 - Trees and Shrubs; 1 gallon; Indoor", "7118", null, null, 394.0),
        new NppNurseryGrid("7120 - Trees and Shrubs; 2 gallon; Indoor", "7120", null, null, 395.0),
        new NppNurseryGrid("7122 - Trees and Shrubs; 5 gallon; Indoor;", "7122", null, null, 396.0),
        new NppNurseryGrid("7124 - Trees and Shrubs; 1 gallon; Field/Container", "7124", null, null, 397.0),
        new NppNurseryGrid("7126 - Trees and Shrubs; 2 gallon; Field/Container", "7126", null, null, 398.0),
        new NppNurseryGrid("7128 - Trees and Shrubs; 5 gallon; Field/Container", "7128", null, null, 399.0),
        new NppNurseryGrid("7129 - Trees and Shrubs; Caliper; Field Stock", "7129", null, null, 400.0),
        new NppNurseryGrid("7130 - Perennials; Potted; Indoor", "7130", null, null, 401.0),
        new NppNurseryGrid("7132 - Perennials; Potted; Outdoor; Nursery", "7132", null, null, 402.0),
        new NppNurseryGrid("7134 - Perennials; Rootstock; Field Grown", "7134", null, null, 403.0),
        new NppNurseryGrid("7140 - Perennials, 7 gallon", "7140", null, null, 404.0),
        new NppNurseryGrid("7142 - Perennials, 10 gallon", "7142", null, null, 405.0),
        new NppNurseryGrid("7144 - Perennials, 15 gallon", "7144", null, null, 406.0),
        new NppNurseryGrid("7146 - Perennials  25 gallon", "7146", null, null, 407.0),
        new NppNurseryGrid("7148 - Perennials  30 gallon", "7148", null, null, 408.0)
      ));
  
      data.setLivestockFarmed(Arrays.asList("cattle", "customFeed", "poultry", "swine", "otherLivestock"));
  
      data.setNeCattleGrid(Arrays.asList(
        new NppCommodityGrid("104 - Number of cows that calved", "104", null, null, 409.0),
        new NppCommodityGrid("105 - Number of feeders under 900lbs (at year start)", "105", null, null, 410.0),
        new NppCommodityGrid("106 - Number of feeders over 900lbs (at year start)", "106", null, null, 411.0)
      ));
  
      data.setLayersEggsForHatching_108(412.0);
      data.setLayersEggsForConsumption_109(413.0);
      data.setBroilersChickens_143(414.0);
      data.setBroilersTurkeys_144(415.0);
  
      data.setProductiveCapacityLC123(416.0);
      data.setFeederHogsFedOver50Lbs_124(417.0);
      data.setFeederHogsFedUpTo50Lbs_125(418.0);
  
      data.setOpdGrid(Arrays.asList(
        new NppCommodityGrid("100 - Alpaca", "100", null, null, 419.0),
        new NppCommodityGrid("101 - Bison", "101", null, null, 420.0),
        new NppCommodityGrid("102 - Feeder Bison (Under 700 lbs)", "102", null, null, 421.0),
        new NppCommodityGrid("103 - Finished Bison (Over 700 lbs)", "103", null, null, 422.0),
        new NppCommodityGrid("111 - Finished Dairy Cattle (Over 900 lbs)", "111", null, null, 423.0),
        new NppCommodityGrid("112 - Feeder Dairy Cattle (Under 900 lbs)", "112", null, null, 424.0),
        new NppCommodityGrid("113 - Dairy Quota, Butterfat", "113", null, null, 425.0),
        new NppCommodityGrid("114 - Dairy Quota, Milk", "114", null, null, 426.0),
        new NppCommodityGrid("115 - Deer", "115", null, null, 427.0),
        new NppCommodityGrid("117 - Elk", "117", null, null, 428.0),
        new NppCommodityGrid("118 - Elk, Bulls Producing Velvet", "118", null, null, 429.0),
        new NppCommodityGrid("122 - Goats", "122", null, null, 430.0),
        new NppCommodityGrid("126 - Honey Bees; Producing (Hives)", "126", null, null, 431.0),
        new NppCommodityGrid("127 - Horses", "127", null, null, 432.0),
        new NppCommodityGrid("128 - Pregnant Mare Urine Produced (PMU)", "128", null, null, 433.0),
        new NppCommodityGrid("129 - Leaf Cutter Bees, Producing (Gallons)", "129", null, null, 434.0),
        new NppCommodityGrid("130 - Llama", "130", null, null, 435.0),
        new NppCommodityGrid("132 - Ostrich", "132", null, null, 436.0),
        new NppCommodityGrid("136 - Reindeer", "136", null, null, 437.0),
        new NppCommodityGrid("138 - Sheep", "138", null, null, 438.0),
        new NppCommodityGrid("149 - Semen, Hog", "149", null, null, 439.0),
        new NppCommodityGrid("150 - Semen, Elk", "150", null, null, 440.0),
        new NppCommodityGrid("151 - Semen, Cattle", "151", null, null, 441.0),
        new NppCommodityGrid("152 - Semen, Deer", "152", null, null, 442.0),
        new NppCommodityGrid("166 - Quail, Breeder Hatching Eggs", "166", null, null, 443.0),
        new NppCommodityGrid("167 - Quail, Broilers", "167", null, null, 444.0),
        new NppCommodityGrid("178 - Donkeys", "178", null, null, 445.0),
        new NppCommodityGrid("191 - Goats, Dairy", "191", null, null, 446.0),
        new NppCommodityGrid("192 - Horse, Semen", "192", null, null, 447.0),
        new NppCommodityGrid("193 - Chinchillas", "193", null, null, 448.0),
        new NppCommodityGrid("194 - Fox", "194", null, null, 449.0),
        new NppCommodityGrid("195 - Mink", "195", null, null, 450.0),
        new NppCommodityGrid("196 - Rabbits", "196", null, null, 451.0)
      ));
  
      data.setOnBehalfOfParticipant("no");
      data.setSignature(null);
      data.setSignFirstName("Johnny");
      data.setSignLastName("Appleseed");
      data.setSignDate(Date.from(LocalDate.of(programYear, 6, 30).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setHowDoYouKnowTheParticipant("online");
  
      data.setSignature2(null);
      data.setSignatureDate2(Date.from(LocalDate.of(programYear, 6, 30).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  
      data.setOrigin("external");
      data.setExternalMethod("chefsForm");
      data.setEnvironment("DEV");
      data.setInternalMethod(null);
  
      SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
  
      SubmissionResource<NppSubmissionDataResource> submission = new SubmissionResource<>();
  
      NppSubmissionRequestDataResource<NppSubmissionDataResource> request = new NppSubmissionRequestDataResource<>();
      request.setDraft(false);
      request.setCreatedBy(user);
      request.setCreatedAt(new Date().toString());
      request.setUpdatedBy(user);
      request.setUpdatedAt(new Date().toString());
      request.setSubmission(submission);
  
      submission.setData(data);
      submissionMetaData.setSubmission(submission);
      submissionMetaData.setSubmissionGuid(null);
  
      // NPP IDIR formId and formVersionId
      String formId = "c6d88314-b8d6-44f7-9fc1-1accc27e78b9";
      String formVersionId = "60f441f4-b7ff-486b-acc6-c581bc05de81";
  
      String postSubmissionUrl = chefsConfig.postSubmissionUrl(formId, formVersionId);
      assertNotNull(postSubmissionUrl);
      try {
        submissionMetaData = chefsApiDao.postNppSubmission(postSubmissionUrl, request);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      NppSubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
  
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
  
      Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
      // Process the submission data
      NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, getFormUserType());
      processor.setUser(user);
      processor.setItemResourceMap(itemResourceMap);
      processor.setBasicBCeIDFormsEnabled(true);
  
      CrmTaskResource task = null;
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals("BCeID NPP submission", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals("This form was submitted through BCeID. Please review and verify the information.", task.getDescription());
  
      try {
        task = completeAndGetTask(crmConfig.getValidationErrorUrl(), task.getActivityId());
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals("BCeID NPP submission", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), task.getStatusCode());
      assertEquals("This form was submitted through BCeID. Please review and verify the information.", task.getDescription());
  
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals(programYear + " NPP " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          getFormUserType() + " NEW PARTICIPANT PLAN form was submitted but has validation errors:\n"
          + "\n"
          + "- Enrolment Fee calculation failed.\n"
          + "- Missing BPUs for program year Productive Units.\n"
          + "\n"
          + "Environment: DEV\n"
          + "\n"
          + "First Name: \n"
          + "Last Name: \n"
          + "Corporate Name: " + corporationName + "\n"
          + "Telephone: (648) 452-4357\n"
          + "Email: johnny@farmer.ca\n"
          + "Participant Type: corporation\n"
          + "Business Number: 999928888RC0001\n"
          + "",
          task.getDescription());
  
      // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
      // to track the status of the submission.
      ChefsSubmission submissionRec = null;
      try {
        submissionRec = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
      } catch (DataAccessException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData nppScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP,
          ScenarioTypeCodes.CHEF);
      Integer nppScenarioNumber = nppScenarioMetadata.getScenarioNumber();
      Integer nppDbSubmissionId = nppScenarioMetadata.getChefsFormSubmissionId();
      assertNotNull(nppScenarioNumber);
      assertNotNull(nppDbSubmissionId);
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario chefScenario = null;
      try {
        chefScenario = calculatorService.loadScenario(participantPin, programYear, nppScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
  
      assertNotNull(chefScenario);
      assertEquals(participantPin, chefScenario.getClient().getParticipantPin());
      assertEquals(programYear, chefScenario.getYear());
      assertEquals(nppScenarioNumber, chefScenario.getScenarioNumber());
      
      checkProductiveUnitValuesForEveryCode(data, chefScenario);

      
      CrmAccountResource crmAccount = null;
      try {
        crmAccount = crmDao.getAccountByPin(participantPin);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmAccount);
      
      assertEquals(participantPin.toString(), crmAccount.getVsi_pin());
      assertEquals("123456789", crmAccount.getVsi_socialinsurancenumber());
      assertEquals(businessNumber, crmAccount.getVsi_businessnumber());
      assertEquals(corporationName, crmAccount.getName());
      
      CrmProgramYearResource crmProgramYear = null;
      try {
        crmProgramYear = crmDao.getProgramYear(programYear);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(crmProgramYear);
      
  
      // ------------ ENW Scenario -------------------------------------------------------------------
      
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      
      ScenarioMetaData enwScenarioMetadata = ScenarioUtils.findLatestEnrolmentNoticeWorkflowScenario(programYearMetadata, enwYear);
      assertNotNull(enwScenarioMetadata);
      Integer enwScenarioNumber = enwScenarioMetadata.getScenarioNumber();
      Integer enwDbSubmissionId = enwScenarioMetadata.getChefsFormSubmissionId();
      assertNotNull(enwScenarioNumber);
      assertNotNull(enwDbSubmissionId);
      assertEquals(nppDbSubmissionId, enwDbSubmissionId);
      
      Scenario enwScenario = null;
      try {
        enwScenario = calculatorService.loadScenario(participantPin, enwYear, enwScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail(formatExceptionFailMessage(e));
      }
      assertNotNull(enwScenario);
      Client client = enwScenario.getClient();
      assertNotNull(client);
      assertEquals(participantPin, client.getParticipantPin());
      assertNull(client.getSin());
      assertEquals(businessNumber + BUSINESS_NUMBER_SUFFIX, client.getBusinessNumber());
      assertEquals(enwYear, enwScenario.getYear());
      assertEquals(enwScenarioNumber, enwScenario.getScenarioNumber());
      assertEquals(ScenarioCategoryCodes.ENROLMENT_NOTICE_WORKFLOW, enwScenario.getScenarioCategoryCode());
      assertEquals(ScenarioTypeCodes.USER, enwScenario.getScenarioTypeCode());
      assertEquals(ScenarioStateCodes.IN_PROGRESS, enwScenario.getScenarioStateCode());
      assertEquals(enwDbSubmissionId, enwScenario.getChefsSubmissionId());
      
      {
        FarmingOperation enwScenarioOperation = enwScenario.getFarmingYear().getFarmingOperationByNumber(1);
        assertEquals(1.0, enwScenarioOperation.getPartnershipPercent());
      }
      
      checkProductiveUnitValuesForEveryCode(data, enwScenario);
      
      EnwEnrolment enw = enwScenario.getEnwEnrolment();
      assertEquals(programYear, enw.getEnrolmentYear());
      assertEquals(EnwEnrolment.CALCULATION_TYPE_PROXY_MARGINS, enw.getEnrolmentCalculationTypeCode());
      assertEquals(Boolean.FALSE, enw.getHasBpus());
      assertEquals(Boolean.TRUE, enw.getHasProductiveUnits());
      assertEquals(Boolean.FALSE, enw.getCanCalculateProxyMargins());
      assertNull(enw.getEnrolmentFee());
      
    } finally {
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuids(submissionGuid);
      deleteSubmissionsFromChefs(submissionGuid);
      deletePin(participantPin);
    }
    
  }

  @Override
  protected String getFormUserType() {
    return ChefsConstants.USER_TYPE_BASIC_BCEID;
  }

}
