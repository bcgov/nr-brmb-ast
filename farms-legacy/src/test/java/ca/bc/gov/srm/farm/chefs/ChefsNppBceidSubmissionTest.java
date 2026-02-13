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
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsNppBceidSubmissionTest extends ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsNppBceidSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.NPP;


  @Test
  public void individualExistingParticipantPinHappyPath() {
    
    Integer participantPin = 844328088;
    Integer programYear = 2026;
    String submissionGuid = null;
    
    try {

      // NPP IDIR formId and formVersionId
      String formId = "c6d88314-b8d6-44f7-9fc1-1accc27e78b9";
      String formVersionId = "d9f37fd2-1892-4fa5-b0ad-788f56ec7d3f";
  
      NppSubmissionDataResource data = new NppSubmissionDataResource();
  
      data.setExistingAccount(true);
      data.setLateParticipant(false);
      data.setFirstName("Johnny");
      data.setLastName("Appleseed");
  
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
  
      data.setEmail("johnny@farmer.ca");
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setTelephone("(648) 452-4357");
      data.setPostalCode("T5Y 4R4");
      data.setSinNumber("123456789");
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
        fail("Unexpected Exception");
      }
  
      NppSubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
  
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData nppScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP,
          ScenarioTypeCodes.CHEF);
      Integer nppScenarioNumber = nppScenarioMetadata.getScenarioNumber();
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, nppScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(scenario);
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(nppScenarioNumber, scenario.getScenarioNumber());
  
      FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationByNumber(1);
  
      for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
        logger.debug("getCraProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
      for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
        logger.debug("getLocalProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
  
      List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();
      assertEquals(fo.getLocalProductiveUnitCapacities().size(), pucs.size());
  
      assertEquals(fo.getCraProductiveUnitCapacities().size(), 0);
  
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
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
      deleteSubmissionsFromChefs(submissionGuid);
    }

  }


  @Test
  public void individualNoPinThenAddPinHappyPath() {

    Integer participantPin = null;
    Integer programYear = 2023;
    String submissionGuid = null;

    try {

      // NPP IDIR formId and formVersionId
      String formId = "c6d88314-b8d6-44f7-9fc1-1accc27e78b9";
      String formVersionId = "d9f37fd2-1892-4fa5-b0ad-788f56ec7d3f";
  
      NppSubmissionDataResource data = new NppSubmissionDataResource();
  
      data.setExistingAccount(true);
      data.setLateParticipant(false);
      data.setFirstName("Johnny");
      data.setLastName("Appleseed");
  
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
  
      data.setEmail("johnny@farmer.ca");
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setTelephone("(648) 452-4357");
      data.setPostalCode("T5Y 4R4");
      data.setSinNumber("123456789");
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
  
      data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
      data.setFiscalYearStart(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
  
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
        fail("Unexpected Exception");
      }
  
      NppSubmissionDataResource resultData = submissionMetaData.getSubmission().getData();
      submissionGuid = submissionMetaData.getSubmissionGuid();
      resultData.setSubmissionGuid(submissionGuid);
      logger.debug("submissionGuid: " + submissionGuid);
  
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
      }
  
      assertNotNull(task);
      assertNull(task.getAccountId());
      assertEquals("NPP no PIN", task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_COMPLETED), task.getStatusCode());
      assertEquals("NPP client without a PIN", task.getDescription());
  
      // Put participant PIN back
      participantPin = 197623465;
      submissionMetaData.getSubmission().getData().setAgriStabilityAgriInvestPin(participantPin);
  
      try {
        processor.loadSubmissionsFromDatabase();
        task = processor.processSubmission(submissionMetaData);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
      }
      assertNotNull(submissionRec);
  
      assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData nppScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP,
          ScenarioTypeCodes.CHEF);
      Integer nppScenarioNumber = nppScenarioMetadata.getScenarioNumber();
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, nppScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(scenario);
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(nppScenarioNumber, scenario.getScenarioNumber());
  
      FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationByNumber(1);
  
      for (ProductiveUnitCapacity puc : fo.getCraProductiveUnitCapacities()) {
        logger.debug("getCraProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
      for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
        logger.debug("getLocalProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      }
  
      List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();
      assertEquals(fo.getLocalProductiveUnitCapacities().size(), pucs.size());
  
      assertEquals(fo.getCraProductiveUnitCapacities().size(), 0);
  
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
      
    } finally {
      
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuid(submissionGuid);
      deleteSubmissionsFromChefs(submissionGuid);
    }
    
  }


  @Test
  public void fillAllProductiveUnits() {

    Integer participantPin = 112969711;
    Integer programYear = 2023;
    String submissionGuid = null;

    deletePin(participantPin);

    try {
      NppSubmissionDataResource data = new NppSubmissionDataResource();
  
      data.setLateParticipant(false);
      data.setExistingAccount(true);
      LabelValue farmType = new LabelValue();
      farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
      farmType.setLabel("Individual");
      data.setFarmType(farmType);
      data.setCorporationName(null);
      data.setFirstNameCorporateContact(null);
      data.setLastNameCorporateContact(null);
      data.setFirstName("Johnny");
      data.setLastName("Appleseed");
      data.setAgriStabilityAgriInvestPin(participantPin);
      data.setNoPin(false);
      data.setBusinessTaxNumberBn(null);
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
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
        fail("Unexpected Exception");
      }
      assertNotNull(task);
      assertNotNull(task.getAccountId());
      assertEquals(programYear + " NPP " + participantPin, task.getSubject());
      assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
      assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
      assertEquals(
          "Enrolment not calculated: Ineligible\n\n" +
          "Primary Farming Activity: treefruit",
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
      assertNotNull(submissionRec.getValidationTaskGuid());
      assertNotNull(submissionRec.getSubmissionId());
      assertNotNull(submissionRec.getRevisionCount());
  
      programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
  
      ScenarioMetaData nppScenarioMetadata = ScenarioUtils.findScenarioByCategory(programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP,
          ScenarioTypeCodes.CHEF);
      Integer nppScenarioNumber = nppScenarioMetadata.getScenarioNumber();
      logger.debug("nppScenarioNumber:" + nppScenarioNumber);
  
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = null;
      try {
        scenario = calculatorService.loadScenario(participantPin, programYear, nppScenarioNumber);
      } catch (ServiceException e) {
        e.printStackTrace();
        fail("Unexpected Exception");
      }
  
      assertNotNull(scenario);
      assertEquals(participantPin, scenario.getClient().getParticipantPin());
      assertEquals(programYear, scenario.getYear());
      assertEquals(nppScenarioNumber, scenario.getScenarioNumber());
  
      FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationByNumber(1);
  
      for (ProductiveUnitCapacity puc : fo.getLocalProductiveUnitCapacities()) {
        switch (puc.getCode()) {
          case "5000": assertEquals(puc.getReportedAmount(), 1.0); break;
          case "5002": assertEquals(puc.getReportedAmount(), 2.0); break;
          case "5006": assertEquals(puc.getReportedAmount(), 3.0); break;
          case "5007": assertEquals(puc.getReportedAmount(), 4.0); break;
          case "5009": assertEquals(puc.getReportedAmount(), 5.0); break;
          case "5010": assertEquals(puc.getReportedAmount(), 6.0); break;
          case "5012": assertEquals(puc.getReportedAmount(), 7.0); break;
          case "5016": assertEquals(puc.getReportedAmount(), 8.0); break;
          case "5018": assertEquals(puc.getReportedAmount(), 9.0); break;
          case "5020": assertEquals(puc.getReportedAmount(), 10.0); break;
          case "5021": assertEquals(puc.getReportedAmount(), 11.0); break;
          case "5022": assertEquals(puc.getReportedAmount(), 12.0); break;
          case "5024": assertEquals(puc.getReportedAmount(), 13.0); break;
  
          case "5014": assertEquals(puc.getReportedAmount(), 14.0); break;
          case "5030": assertEquals(puc.getReportedAmount(), 15.0); break;
          case "5032": assertEquals(puc.getReportedAmount(), 16.0); break;
          case "5033": assertEquals(puc.getReportedAmount(), 17.0); break;
          case "5040": assertEquals(puc.getReportedAmount(), 18.0); break;
          case "5042": assertEquals(puc.getReportedAmount(), 19.0); break;
          case "5044": assertEquals(puc.getReportedAmount(), 20.0); break;
          case "5046": assertEquals(puc.getReportedAmount(), 21.0); break;
          case "5048": assertEquals(puc.getReportedAmount(), 22.0); break;
          case "5050": assertEquals(puc.getReportedAmount(), 23.0); break;
          case "5052": assertEquals(puc.getReportedAmount(), 24.0); break;
          case "5054": assertEquals(puc.getReportedAmount(), 25.0); break;
          case "5056": assertEquals(puc.getReportedAmount(), 26.0); break;
          case "7054": assertEquals(puc.getReportedAmount(), 27.0); break;
          case "5058": assertEquals(puc.getReportedAmount(), 28.0); break;
  
          case "6": assertEquals(puc.getReportedAmount(), 29.0); break;
          case "5034": assertEquals(puc.getReportedAmount(), 30.0); break;
          case "6850": assertEquals(puc.getReportedAmount(), 31.0); break;
          case "6851": assertEquals(puc.getReportedAmount(), 32.0); break;
          case "6852": assertEquals(puc.getReportedAmount(), 33.0); break;
          case "6854": assertEquals(puc.getReportedAmount(), 34.0); break;
          case "6855": assertEquals(puc.getReportedAmount(), 35.0); break;
          case "6856": assertEquals(puc.getReportedAmount(), 36.0); break;
          case "6858": assertEquals(puc.getReportedAmount(), 37.0); break;
          case "6860": assertEquals(puc.getReportedAmount(), 38.0); break;
          case "6862": assertEquals(puc.getReportedAmount(), 39.0); break;
          case "6864": assertEquals(puc.getReportedAmount(), 40.0); break;
          case "6866": assertEquals(puc.getReportedAmount(), 41.0); break;
          case "6867": assertEquals(puc.getReportedAmount(), 42.0); break;
          case "6868": assertEquals(puc.getReportedAmount(), 43.0); break;
          case "6869": assertEquals(puc.getReportedAmount(), 44.0); break;
          case "6870": assertEquals(puc.getReportedAmount(), 45.0); break;
          case "6872": assertEquals(puc.getReportedAmount(), 46.0); break;
          case "6874": assertEquals(puc.getReportedAmount(), 47.0); break;
          case "6876": assertEquals(puc.getReportedAmount(), 48.0); break;
          case "6877": assertEquals(puc.getReportedAmount(), 49.0); break;
          case "6878": assertEquals(puc.getReportedAmount(), 50.0); break;
          case "6879": assertEquals(puc.getReportedAmount(), 51.0); break;
          case "6880": assertEquals(puc.getReportedAmount(), 52.0); break;
          case "6881": assertEquals(puc.getReportedAmount(), 53.0); break;
          case "6882": assertEquals(puc.getReportedAmount(), 54.0); break;
          case "6883": assertEquals(puc.getReportedAmount(), 55.0); break;
          case "6884": assertEquals(puc.getReportedAmount(), 56.0); break;
          case "6886": assertEquals(puc.getReportedAmount(), 57.0); break;
          case "6888": assertEquals(puc.getReportedAmount(), 58.0); break;
          case "6890": assertEquals(puc.getReportedAmount(), 59.0); break;
          case "6892": assertEquals(puc.getReportedAmount(), 60.0); break;
          case "6893": assertEquals(puc.getReportedAmount(), 61.0); break;
          case "6894": assertEquals(puc.getReportedAmount(), 62.0); break;
          case "6896": assertEquals(puc.getReportedAmount(), 63.0); break;
          case "6898": assertEquals(puc.getReportedAmount(), 64.0); break;
          case "6900": assertEquals(puc.getReportedAmount(), 65.0); break;
          case "6902": assertEquals(puc.getReportedAmount(), 66.0); break;
          case "6903": assertEquals(puc.getReportedAmount(), 67.0); break;
          case "6904": assertEquals(puc.getReportedAmount(), 68.0); break;
          case "6920": assertEquals(puc.getReportedAmount(), 69.0); break;
          case "6922": assertEquals(puc.getReportedAmount(), 70.0); break;
          case "6932": assertEquals(puc.getReportedAmount(), 71.0); break;
          case "6934": assertEquals(puc.getReportedAmount(), 72.0); break;
          case "6946": assertEquals(puc.getReportedAmount(), 73.0); break;
          case "6972": assertEquals(puc.getReportedAmount(), 75.0); break;
          case "6974": assertEquals(puc.getReportedAmount(), 76.0); break;
          case "6975": assertEquals(puc.getReportedAmount(), 77.0); break;
          case "6976": assertEquals(puc.getReportedAmount(), 78.0); break;
          case "6978": assertEquals(puc.getReportedAmount(), 79.0); break;
          case "6980": assertEquals(puc.getReportedAmount(), 80.0); break;
          case "6982": assertEquals(puc.getReportedAmount(), 81.0); break;
          case "6983": assertEquals(puc.getReportedAmount(), 82.0); break;
          case "6984": assertEquals(puc.getReportedAmount(), 83.0); break;
          case "6986": assertEquals(puc.getReportedAmount(), 84.0); break;
          case "6988": assertEquals(puc.getReportedAmount(), 85.0); break;
          case "6998": assertEquals(puc.getReportedAmount(), 86.0); break;
          case "7000": assertEquals(puc.getReportedAmount(), 87.0); break;
          case "7002": assertEquals(puc.getReportedAmount(), 88.0); break;
          case "7004": assertEquals(puc.getReportedAmount(), 89.0); break;
          case "7006": assertEquals(puc.getReportedAmount(), 90.0); break;
          case "7008": assertEquals(puc.getReportedAmount(), 91.0); break;
          case "7010": assertEquals(puc.getReportedAmount(), 92.0); break;
          case "7012": assertEquals(puc.getReportedAmount(), 93.0); break;
          case "7014": assertEquals(puc.getReportedAmount(), 94.0); break;
          case "7015": assertEquals(puc.getReportedAmount(), 95.0); break;
          case "7016": assertEquals(puc.getReportedAmount(), 96.0); break;
          case "7018": assertEquals(puc.getReportedAmount(), 97.0); break;
          case "7020": assertEquals(puc.getReportedAmount(), 98.0); break;
          case "7022": assertEquals(puc.getReportedAmount(), 99.0); break;
          case "7024": assertEquals(puc.getReportedAmount(), 100.0); break;
          case "7026": assertEquals(puc.getReportedAmount(), 101.0); break;
          case "7030": assertEquals(puc.getReportedAmount(), 102.0); break;
          case "7032": assertEquals(puc.getReportedAmount(), 103.0); break;
          case "7034": assertEquals(puc.getReportedAmount(), 104.0); break;
          case "7035": assertEquals(puc.getReportedAmount(), 105.0); break;
          case "7036": assertEquals(puc.getReportedAmount(), 106.0); break;
          case "7037": assertEquals(puc.getReportedAmount(), 107.0); break;
          case "7038": assertEquals(puc.getReportedAmount(), 108.0); break;
          case "7039": assertEquals(puc.getReportedAmount(), 109.0); break;
          case "7040": assertEquals(puc.getReportedAmount(), 110.0); break;
          case "7042": assertEquals(puc.getReportedAmount(), 111.0); break;
          case "7044": assertEquals(puc.getReportedAmount(), 112.0); break;
          case "7046": assertEquals(puc.getReportedAmount(), 113.0); break;
          case "7047": assertEquals(puc.getReportedAmount(), 114.0); break;
          case "7048": assertEquals(puc.getReportedAmount(), 115.0); break;
          case "7049": assertEquals(puc.getReportedAmount(), 116.0); break;
          case "7052": assertEquals(puc.getReportedAmount(), 117.0); break;
          case "7056": assertEquals(puc.getReportedAmount(), 118.0); break;
          case "7057": assertEquals(puc.getReportedAmount(), 119.0); break;
          case "7058": assertEquals(puc.getReportedAmount(), 120.0); break;
          case "7060": assertEquals(puc.getReportedAmount(), 121.0); break;
          case "7062": assertEquals(puc.getReportedAmount(), 122.0); break;
          case "7064": assertEquals(puc.getReportedAmount(), 123.0); break;
          case "7068": assertEquals(puc.getReportedAmount(), 125.0); break;
          case "7069": assertEquals(puc.getReportedAmount(), 126.0); break;
          case "7070": assertEquals(puc.getReportedAmount(), 127.0); break;
          case "7072": assertEquals(puc.getReportedAmount(), 128.0); break;
          case "7074": assertEquals(puc.getReportedAmount(), 129.0); break;
          case "7078": assertEquals(puc.getReportedAmount(), 130.0); break;
          case "7080": assertEquals(puc.getReportedAmount(), 131.0); break;
          case "7082": assertEquals(puc.getReportedAmount(), 132.0); break;
          case "7083": assertEquals(puc.getReportedAmount(), 133.0); break;
          case "7084": assertEquals(puc.getReportedAmount(), 134.0); break;
          case "7086": assertEquals(puc.getReportedAmount(), 135.0); break;
          case "7087": assertEquals(puc.getReportedAmount(), 136.0); break;
          case "7088": assertEquals(puc.getReportedAmount(), 137.0); break;
          case "7094": assertEquals(puc.getReportedAmount(), 138.0); break;
          case "7095": assertEquals(puc.getReportedAmount(), 139.0); break;
          case "7098": assertEquals(puc.getReportedAmount(), 140.0); break;
          case "7099": assertEquals(puc.getReportedAmount(), 141.0); break;
          case "7100": assertEquals(puc.getReportedAmount(), 142.0); break;
          case "7200": assertEquals(puc.getReportedAmount(), 143.0); break;
  
          case "4784": assertEquals(puc.getReportedAmount(), 144.0); break;
          case "5100": assertEquals(puc.getReportedAmount(), 145.0); break;
          case "5370": assertEquals(puc.getReportedAmount(), 147.0); break;
          case "5372": assertEquals(puc.getReportedAmount(), 148.0); break;
          case "5374": assertEquals(puc.getReportedAmount(), 149.0); break;
          case "5369": assertEquals(puc.getReportedAmount(), 150.0); break;
          case "5375": assertEquals(puc.getReportedAmount(), 151.0); break;
          case "5376": assertEquals(puc.getReportedAmount(), 152.0); break;
          case "5378": assertEquals(puc.getReportedAmount(), 153.0); break;
          case "5380": assertEquals(puc.getReportedAmount(), 154.0); break;
          case "5382": assertEquals(puc.getReportedAmount(), 155.0); break;
          case "5384": assertEquals(puc.getReportedAmount(), 156.0); break;
          case "5386": assertEquals(puc.getReportedAmount(), 157.0); break;
          case "5388": assertEquals(puc.getReportedAmount(), 158.0); break;
          case "5390": assertEquals(puc.getReportedAmount(), 159.0); break;
          case "5392": assertEquals(puc.getReportedAmount(), 160.0); break;
          case "5394": assertEquals(puc.getReportedAmount(), 161.0); break;
          case "5468": assertEquals(puc.getReportedAmount(), 162.0); break;
          case "5446": assertEquals(puc.getReportedAmount(), 163.0); break;
          case "5396": assertEquals(puc.getReportedAmount(), 164.0); break;
          case "5398": assertEquals(puc.getReportedAmount(), 165.0); break;
          case "5400": assertEquals(puc.getReportedAmount(), 166.0); break;
          case "5402": assertEquals(puc.getReportedAmount(), 167.0); break;
          case "5404": assertEquals(puc.getReportedAmount(), 168.0); break;
          case "5406": assertEquals(puc.getReportedAmount(), 169.0); break;
          case "5408": assertEquals(puc.getReportedAmount(), 170.0); break;
          case "5410": assertEquals(puc.getReportedAmount(), 171.0); break;
          case "5412": assertEquals(puc.getReportedAmount(), 172.0); break;
          case "5414": assertEquals(puc.getReportedAmount(), 173.0); break;
          case "5416": assertEquals(puc.getReportedAmount(), 174.0); break;
          case "5418": assertEquals(puc.getReportedAmount(), 175.0); break;
          case "5420": assertEquals(puc.getReportedAmount(), 176.0); break;
          case "5422": assertEquals(puc.getReportedAmount(), 177.0); break;
          case "5424": assertEquals(puc.getReportedAmount(), 178.0); break;
          case "5426": assertEquals(puc.getReportedAmount(), 179.0); break;
          case "5428": assertEquals(puc.getReportedAmount(), 180.0); break;
          case "5430": assertEquals(puc.getReportedAmount(), 181.0); break;
          case "5432": assertEquals(puc.getReportedAmount(), 182.0); break;
          case "5434": assertEquals(puc.getReportedAmount(), 183.0); break;
          case "5436": assertEquals(puc.getReportedAmount(), 184.0); break;
          case "5438": assertEquals(puc.getReportedAmount(), 185.0); break;
          case "5440": assertEquals(puc.getReportedAmount(), 186.0); break;
          case "5442": assertEquals(puc.getReportedAmount(), 187.0); break;
          case "5444": assertEquals(puc.getReportedAmount(), 188.0); break;
          case "5448": assertEquals(puc.getReportedAmount(), 189.0); break;
          case "5450": assertEquals(puc.getReportedAmount(), 190.0); break;
          case "5452": assertEquals(puc.getReportedAmount(), 191.0); break;
          case "5454": assertEquals(puc.getReportedAmount(), 192.0); break;
          case "5456": assertEquals(puc.getReportedAmount(), 193.0); break;
          case "5458": assertEquals(puc.getReportedAmount(), 194.0); break;
          case "5460": assertEquals(puc.getReportedAmount(), 195.0); break;
          case "5462": assertEquals(puc.getReportedAmount(), 196.0); break;
          case "5464": assertEquals(puc.getReportedAmount(), 197.0); break;
          case "5466": assertEquals(puc.getReportedAmount(), 198.0); break;
          case "5240": assertEquals(puc.getReportedAmount(), 199.0); break;
          case "5242": assertEquals(puc.getReportedAmount(), 200.0); break;
          case "5244": assertEquals(puc.getReportedAmount(), 201.0); break;
          case "5246": assertEquals(puc.getReportedAmount(), 202.0); break;
          case "5248": assertEquals(puc.getReportedAmount(), 203.0); break;
          case "5540": assertEquals(puc.getReportedAmount(), 204.0); break;
          case "5542": assertEquals(puc.getReportedAmount(), 205.0); break;
          case "5544": assertEquals(puc.getReportedAmount(), 206.0); break;
          case "5250": assertEquals(puc.getReportedAmount(), 207.0); break;
          case "5252": assertEquals(puc.getReportedAmount(), 208.0); break;
          case "5254": assertEquals(puc.getReportedAmount(), 209.0); break;
          case "5261": assertEquals(puc.getReportedAmount(), 210.0); break;
          case "5290": assertEquals(puc.getReportedAmount(), 211.0); break;
          case "5292": assertEquals(puc.getReportedAmount(), 212.0); break;
          case "5294": assertEquals(puc.getReportedAmount(), 213.0); break;
          case "5300": assertEquals(puc.getReportedAmount(), 214.0); break;
          case "5302": assertEquals(puc.getReportedAmount(), 215.0); break;
          case "5303": assertEquals(puc.getReportedAmount(), 216.0); break;
          case "5304": assertEquals(puc.getReportedAmount(), 217.0); break;
          case "5306": assertEquals(puc.getReportedAmount(), 218.0); break;
          case "5330": assertEquals(puc.getReportedAmount(), 219.0); break;
          case "5310": assertEquals(puc.getReportedAmount(), 220.0); break;
          case "5312": assertEquals(puc.getReportedAmount(), 221.0); break;
          case "5314": assertEquals(puc.getReportedAmount(), 222.0); break;
          case "5316": assertEquals(puc.getReportedAmount(), 223.0); break;
          case "5318": assertEquals(puc.getReportedAmount(), 224.0); break;
          case "5322": assertEquals(puc.getReportedAmount(), 225.0); break;
          case "5324": assertEquals(puc.getReportedAmount(), 226.0); break;
          case "5325": assertEquals(puc.getReportedAmount(), 227.0); break;
          case "5326": assertEquals(puc.getReportedAmount(), 228.0); break;
          case "5328": assertEquals(puc.getReportedAmount(), 229.0); break;
          case "5340": assertEquals(puc.getReportedAmount(), 230.0); break;
          case "5360": assertEquals(puc.getReportedAmount(), 231.0); break;
          case "5350": assertEquals(puc.getReportedAmount(), 232.0); break;
          case "5352": assertEquals(puc.getReportedAmount(), 233.0); break;
          case "5354": assertEquals(puc.getReportedAmount(), 234.0); break;
          case "5356": assertEquals(puc.getReportedAmount(), 235.0); break;
          case "5358": assertEquals(puc.getReportedAmount(), 236.0); break;
          case "5550": assertEquals(puc.getReportedAmount(), 237.0); break;
          case "6826": assertEquals(puc.getReportedAmount(), 238.0); break;
          case "5750": assertEquals(puc.getReportedAmount(), 239.0); break;
          case "5752": assertEquals(puc.getReportedAmount(), 240.0); break;
          case "5754": assertEquals(puc.getReportedAmount(), 241.0); break;
          case "5070": assertEquals(puc.getReportedAmount(), 242.0); break;
          case "5072": assertEquals(puc.getReportedAmount(), 243.0); break;
          case "5074": assertEquals(puc.getReportedAmount(), 244.0); break;
          case "5822": assertEquals(puc.getReportedAmount(), 245.0); break;
          case "5760": assertEquals(puc.getReportedAmount(), 246.0); break;
          case "5762": assertEquals(puc.getReportedAmount(), 247.0); break;
          case "5764": assertEquals(puc.getReportedAmount(), 248.0); break;
          case "5766": assertEquals(puc.getReportedAmount(), 249.0); break;
          case "5768": assertEquals(puc.getReportedAmount(), 250.0); break;
          case "5770": assertEquals(puc.getReportedAmount(), 251.0); break;
          case "5820": assertEquals(puc.getReportedAmount(), 252.0); break;
          case "5772": assertEquals(puc.getReportedAmount(), 253.0); break;
          case "5774": assertEquals(puc.getReportedAmount(), 254.0); break;
          case "5776": assertEquals(puc.getReportedAmount(), 255.0); break;
          case "5778": assertEquals(puc.getReportedAmount(), 256.0); break;
          case "5780": assertEquals(puc.getReportedAmount(), 257.0); break;
          case "5782": assertEquals(puc.getReportedAmount(), 258.0); break;
          case "5784": assertEquals(puc.getReportedAmount(), 259.0); break;
          case "5786": assertEquals(puc.getReportedAmount(), 260.0); break;
          case "5788": assertEquals(puc.getReportedAmount(), 261.0); break;
          case "5790": assertEquals(puc.getReportedAmount(), 262.0); break;
          case "5792": assertEquals(puc.getReportedAmount(), 263.0); break;
          case "5794": assertEquals(puc.getReportedAmount(), 264.0); break;
          case "5821": assertEquals(puc.getReportedAmount(), 265.0); break;
          case "5796": assertEquals(puc.getReportedAmount(), 266.0); break;
          case "5798": assertEquals(puc.getReportedAmount(), 267.0); break;
          case "5800": assertEquals(puc.getReportedAmount(), 268.0); break;
          case "5802": assertEquals(puc.getReportedAmount(), 269.0); break;
          case "5804": assertEquals(puc.getReportedAmount(), 270.0); break;
          case "5806": assertEquals(puc.getReportedAmount(), 271.0); break;
          case "5808": assertEquals(puc.getReportedAmount(), 272.0); break;
          case "5810": assertEquals(puc.getReportedAmount(), 273.0); break;
          case "5812": assertEquals(puc.getReportedAmount(), 274.0); break;
          case "5814": assertEquals(puc.getReportedAmount(), 275.0); break;
          case "5816": assertEquals(puc.getReportedAmount(), 276.0); break;
          case "5818": assertEquals(puc.getReportedAmount(), 277.0); break;
          case "5830": assertEquals(puc.getReportedAmount(), 278.0); break;
          case "5832": assertEquals(puc.getReportedAmount(), 279.0); break;
          case "5834": assertEquals(puc.getReportedAmount(), 280.0); break;
          case "5836": assertEquals(puc.getReportedAmount(), 281.0); break;
          case "5840": assertEquals(puc.getReportedAmount(), 282.0); break;
          case "5841": assertEquals(puc.getReportedAmount(), 283.0); break;
          case "5850": assertEquals(puc.getReportedAmount(), 284.0); break;
          case "5852": assertEquals(puc.getReportedAmount(), 285.0); break;
          case "5854": assertEquals(puc.getReportedAmount(), 286.0); break;
          case "5856": assertEquals(puc.getReportedAmount(), 287.0); break;
          case "5858": assertEquals(puc.getReportedAmount(), 288.0); break;
          case "5860": assertEquals(puc.getReportedAmount(), 289.0); break;
          case "5862": assertEquals(puc.getReportedAmount(), 290.0); break;
          case "5864": assertEquals(puc.getReportedAmount(), 291.0); break;
          case "5866": assertEquals(puc.getReportedAmount(), 292.0); break;
          case "5868": assertEquals(puc.getReportedAmount(), 293.0); break;
          case "5870": assertEquals(puc.getReportedAmount(), 294.0); break;
          case "5872": assertEquals(puc.getReportedAmount(), 295.0); break;
          case "5874": assertEquals(puc.getReportedAmount(), 296.0); break;
          case "5876": assertEquals(puc.getReportedAmount(), 297.0); break;
          case "5878": assertEquals(puc.getReportedAmount(), 298.0); break;
          case "5880": assertEquals(puc.getReportedAmount(), 299.0); break;
          case "5882": assertEquals(puc.getReportedAmount(), 300.0); break;
          case "5884": assertEquals(puc.getReportedAmount(), 301.0); break;
          case "5886": assertEquals(puc.getReportedAmount(), 302.0); break;
          case "5968": assertEquals(puc.getReportedAmount(), 303.0); break;
          case "5900": assertEquals(puc.getReportedAmount(), 304.0); break;
          case "5500": assertEquals(puc.getReportedAmount(), 305.0); break;
          case "5502": assertEquals(puc.getReportedAmount(), 306.0); break;
          case "5504": assertEquals(puc.getReportedAmount(), 307.0); break;
          case "5506": assertEquals(puc.getReportedAmount(), 308.0); break;
          case "5508": assertEquals(puc.getReportedAmount(), 309.0); break;
          case "5510": assertEquals(puc.getReportedAmount(), 310.0); break;
          case "5512": assertEquals(puc.getReportedAmount(), 311.0); break;
          case "5514": assertEquals(puc.getReportedAmount(), 312.0); break;
          case "5516": assertEquals(puc.getReportedAmount(), 313.0); break;
          case "5518": assertEquals(puc.getReportedAmount(), 314.0); break;
          case "5520": assertEquals(puc.getReportedAmount(), 315.0); break;
          case "5076": assertEquals(puc.getReportedAmount(), 316.0); break;
          case "5078": assertEquals(puc.getReportedAmount(), 317.0); break;
          case "5080": assertEquals(puc.getReportedAmount(), 318.0); break;
          case "5259": assertEquals(puc.getReportedAmount(), 319.0); break;
          case "5910": assertEquals(puc.getReportedAmount(), 320.0); break;
          case "5912": assertEquals(puc.getReportedAmount(), 321.0); break;
          case "5914": assertEquals(puc.getReportedAmount(), 322.0); break;
          case "5916": assertEquals(puc.getReportedAmount(), 323.0); break;
          case "5918": assertEquals(puc.getReportedAmount(), 324.0); break;
          case "5920": assertEquals(puc.getReportedAmount(), 325.0); break;
          case "5930": assertEquals(puc.getReportedAmount(), 326.0); break;
          case "5932": assertEquals(puc.getReportedAmount(), 327.0); break;
          case "5934": assertEquals(puc.getReportedAmount(), 328.0); break;
          case "5936": assertEquals(puc.getReportedAmount(), 329.0); break;
          case "5907": assertEquals(puc.getReportedAmount(), 330.0); break;
          case "5908": assertEquals(puc.getReportedAmount(), 331.0); break;
          case "5940": assertEquals(puc.getReportedAmount(), 332.0); break;
          case "5942": assertEquals(puc.getReportedAmount(), 333.0); break;
          case "5944": assertEquals(puc.getReportedAmount(), 334.0); break;
          case "5946": assertEquals(puc.getReportedAmount(), 335.0); break;
          case "5082": assertEquals(puc.getReportedAmount(), 336.0); break;
          case "5084": assertEquals(puc.getReportedAmount(), 337.0); break;
          case "5086": assertEquals(puc.getReportedAmount(), 338.0); break;
          case "5950": assertEquals(puc.getReportedAmount(), 339.0); break;
          case "5952": assertEquals(puc.getReportedAmount(), 340.0); break;
          case "5954": assertEquals(puc.getReportedAmount(), 341.0); break;
          case "5956": assertEquals(puc.getReportedAmount(), 342.0); break;
          case "5964": assertEquals(puc.getReportedAmount(), 343.0); break;
          case "5958": assertEquals(puc.getReportedAmount(), 344.0); break;
          case "5960": assertEquals(puc.getReportedAmount(), 345.0); break;
          case "5962": assertEquals(puc.getReportedAmount(), 346.0); break;
          case "5970": assertEquals(puc.getReportedAmount(), 347.0); break;
          case "5972": assertEquals(puc.getReportedAmount(), 348.0); break;
          case "5974": assertEquals(puc.getReportedAmount(), 349.0); break;
          case "5980": assertEquals(puc.getReportedAmount(), 350.0); break;
          case "5982": assertEquals(puc.getReportedAmount(), 351.0); break;
          case "5984": assertEquals(puc.getReportedAmount(), 352.0); break;
          case "6000": assertEquals(puc.getReportedAmount(), 353.0); break;
  
          case "5560": assertEquals(puc.getReportedAmount(), 1000.0); break;
          case "5562": assertEquals(puc.getReportedAmount(), 1001.0); break;
          case "5564": assertEquals(puc.getReportedAmount(), 1002.0); break;
          case "5566": assertEquals(puc.getReportedAmount(), 1003.0); break;
          case "5568": assertEquals(puc.getReportedAmount(), 1004.0); break;
          case "5570": assertEquals(puc.getReportedAmount(), 1005.0); break;
          case "5572": assertEquals(puc.getReportedAmount(), 1006.0); break;
          case "5574": assertEquals(puc.getReportedAmount(), 1007.0); break;
          case "5576": assertEquals(puc.getReportedAmount(), 1008.0); break;
          case "5578": assertEquals(puc.getReportedAmount(), 1009.0); break;
          case "5579": assertEquals(puc.getReportedAmount(), 1010.0); break;
          case "5580": assertEquals(puc.getReportedAmount(), 1011.0); break;
          case "5582": assertEquals(puc.getReportedAmount(), 1012.0); break;
          case "5583": assertEquals(puc.getReportedAmount(), 1013.0); break;
          case "5586": assertEquals(puc.getReportedAmount(), 1014.0); break;
          case "5588": assertEquals(puc.getReportedAmount(), 1015.0); break;
  
          case "5600": assertEquals(puc.getReportedAmount(), 1016.0); break;
          case "5603": assertEquals(puc.getReportedAmount(), 1017.0); break;
          case "5602": assertEquals(puc.getReportedAmount(), 1018.0); break;
          case "5604": assertEquals(puc.getReportedAmount(), 1019.0); break;
          case "5606": assertEquals(puc.getReportedAmount(), 1020.0); break;
          case "5608": assertEquals(puc.getReportedAmount(), 1021.0); break;
          case "5610": assertEquals(puc.getReportedAmount(), 1022.0); break;
          case "5736": assertEquals(puc.getReportedAmount(), 1023.0); break;
          case "5612": assertEquals(puc.getReportedAmount(), 1024.0); break;
          case "5614": assertEquals(puc.getReportedAmount(), 1025.0); break;
          case "5724": assertEquals(puc.getReportedAmount(), 1026.0); break;
          case "5726": assertEquals(puc.getReportedAmount(), 1027.0); break;
          case "5723": assertEquals(puc.getReportedAmount(), 1028.0); break;
          case "5725": assertEquals(puc.getReportedAmount(), 1029.0); break;
          case "5729": assertEquals(puc.getReportedAmount(), 1030.0); break;
          case "5620": assertEquals(puc.getReportedAmount(), 1031.0); break;
          case "5622": assertEquals(puc.getReportedAmount(), 1032.0); break;
          case "5624": assertEquals(puc.getReportedAmount(), 1033.0); break;
          case "5619": assertEquals(puc.getReportedAmount(), 1034.0); break;
          case "5628": assertEquals(puc.getReportedAmount(), 1035.0); break;
          case "5732": assertEquals(puc.getReportedAmount(), 1036.0); break;
          case "5731": assertEquals(puc.getReportedAmount(), 1037.0); break;
          case "5734": assertEquals(puc.getReportedAmount(), 1038.0); break;
          case "5733": assertEquals(puc.getReportedAmount(), 1039.0); break;
          case "5636": assertEquals(puc.getReportedAmount(), 1040.0); break;
          case "5638": assertEquals(puc.getReportedAmount(), 1041.0); break;
          case "5640": assertEquals(puc.getReportedAmount(), 1042.0); break;
          case "5642": assertEquals(puc.getReportedAmount(), 1043.0); break;
          case "5727": assertEquals(puc.getReportedAmount(), 1044.0); break;
          case "5728": assertEquals(puc.getReportedAmount(), 1045.0); break;
          case "5644": assertEquals(puc.getReportedAmount(), 1046.0); break;
          case "5646": assertEquals(puc.getReportedAmount(), 1047.0); break;
          case "5648": assertEquals(puc.getReportedAmount(), 1048.0); break;
          case "5650": assertEquals(puc.getReportedAmount(), 1049.0); break;
          case "5652": assertEquals(puc.getReportedAmount(), 1050.0); break;
          case "5671": assertEquals(puc.getReportedAmount(), 1051.0); break;
          case "5656": assertEquals(puc.getReportedAmount(), 1052.0); break;
          case "5658": assertEquals(puc.getReportedAmount(), 1053.0); break;
          case "5660": assertEquals(puc.getReportedAmount(), 1054.0); break;
          case "5662": assertEquals(puc.getReportedAmount(), 1055.0); break;
          case "5664": assertEquals(puc.getReportedAmount(), 1056.0); break;
          case "5666": assertEquals(puc.getReportedAmount(), 1057.0); break;
          case "5668": assertEquals(puc.getReportedAmount(), 1058.0); break;
          case "5670": assertEquals(puc.getReportedAmount(), 1059.0); break;
          case "5672": assertEquals(puc.getReportedAmount(), 1060.0); break;
          case "5674": assertEquals(puc.getReportedAmount(), 1061.0); break;
          case "5676": assertEquals(puc.getReportedAmount(), 1062.0); break;
          case "5680": assertEquals(puc.getReportedAmount(), 1063.0); break;
          case "5682": assertEquals(puc.getReportedAmount(), 1064.0); break;
          case "5684": assertEquals(puc.getReportedAmount(), 1065.0); break;
          case "5686": assertEquals(puc.getReportedAmount(), 1066.0); break;
          case "5688": assertEquals(puc.getReportedAmount(), 1067.0); break;
          case "5690": assertEquals(puc.getReportedAmount(), 1068.0); break;
          case "5592": assertEquals(puc.getReportedAmount(), 1069.0); break;
          case "5596": assertEquals(puc.getReportedAmount(), 1070.0); break;
          case "5593": assertEquals(puc.getReportedAmount(), 1071.0); break;
          case "5597": assertEquals(puc.getReportedAmount(), 1072.0); break;
          case "5595": assertEquals(puc.getReportedAmount(), 1073.0); break;
          case "5599": assertEquals(puc.getReportedAmount(), 1074.0); break;
          case "5594": assertEquals(puc.getReportedAmount(), 1075.0); break;
          case "5598": assertEquals(puc.getReportedAmount(), 1076.0); break;
          case "5699": assertEquals(puc.getReportedAmount(), 1077.0); break;
          case "5696": assertEquals(puc.getReportedAmount(), 1078.0); break;
          case "5698": assertEquals(puc.getReportedAmount(), 1079.0); break;
          case "5700": assertEquals(puc.getReportedAmount(), 1080.0); break;
          case "5702": assertEquals(puc.getReportedAmount(), 1081.0); break;
          case "5693": assertEquals(puc.getReportedAmount(), 1082.0); break;
          case "5695": assertEquals(puc.getReportedAmount(), 1083.0); break;
          case "5691": assertEquals(puc.getReportedAmount(), 1084.0); break;
          case "5683": assertEquals(puc.getReportedAmount(), 1085.0); break;
          case "5685": assertEquals(puc.getReportedAmount(), 1086.0); break;
          case "5659": assertEquals(puc.getReportedAmount(), 1087.0); break;
          case "5689": assertEquals(puc.getReportedAmount(), 1088.0); break;
          case "5687": assertEquals(puc.getReportedAmount(), 1089.0); break;
          case "5615": assertEquals(puc.getReportedAmount(), 1090.0); break;
          case "5742": assertEquals(puc.getReportedAmount(), 1091.0); break;
          case "5704": assertEquals(puc.getReportedAmount(), 1092.0); break;
          case "5706": assertEquals(puc.getReportedAmount(), 1093.0); break;
          case "5709": assertEquals(puc.getReportedAmount(), 1094.0); break;
          case "5708": assertEquals(puc.getReportedAmount(), 1095.0); break;
          case "5714": assertEquals(puc.getReportedAmount(), 1096.0); break;
          case "5716": assertEquals(puc.getReportedAmount(), 1097.0); break;
          case "5718": assertEquals(puc.getReportedAmount(), 1098.0); break;
          case "5730": assertEquals(puc.getReportedAmount(), 1099.0); break;
          case "5720": assertEquals(puc.getReportedAmount(), 1100.0); break;
          case "5722": assertEquals(puc.getReportedAmount(), 1101.0); break;
  
          case "6930": assertEquals(puc.getReportedAmount(), 354.0); break;
          case "6931": assertEquals(puc.getReportedAmount(), 355.0); break;
          case "6937": assertEquals(puc.getReportedAmount(), 356.0); break;
          case "6938": assertEquals(puc.getReportedAmount(), 357.0); break;
          case "6940": assertEquals(puc.getReportedAmount(), 358.0); break;
          case "6941": assertEquals(puc.getReportedAmount(), 359.0); break;
          case "6942": assertEquals(puc.getReportedAmount(), 360.0); break;
          case "6943": assertEquals(puc.getReportedAmount(), 361.0); break;
          case "6944": assertEquals(puc.getReportedAmount(), 362.0); break;
          case "6945": assertEquals(puc.getReportedAmount(), 363.0); break;
          case "6949": assertEquals(puc.getReportedAmount(), 364.0); break;
          case "6950": assertEquals(puc.getReportedAmount(), 365.0); break;
          case "6951": assertEquals(puc.getReportedAmount(), 366.0); break;
          case "6952": assertEquals(puc.getReportedAmount(), 367.0); break;
          case "6953": assertEquals(puc.getReportedAmount(), 368.0); break;
          case "6954": assertEquals(puc.getReportedAmount(), 369.0); break;
          case "6955": assertEquals(puc.getReportedAmount(), 370.0); break;
          case "6956": assertEquals(puc.getReportedAmount(), 371.0); break;
          case "6957": assertEquals(puc.getReportedAmount(), 372.0); break;
          case "6958": assertEquals(puc.getReportedAmount(), 373.0); break;
          case "6959": assertEquals(puc.getReportedAmount(), 374.0); break;
          case "6965": assertEquals(puc.getReportedAmount(), 375.0); break;
          case "7028": assertEquals(puc.getReportedAmount(), 376.0); break;
          case "7073": assertEquals(puc.getReportedAmount(), 378.0); break;
          case "7076": assertEquals(puc.getReportedAmount(), 379.0); break;
          case "7090": assertEquals(puc.getReportedAmount(), 380.0); break;
          case "7092": assertEquals(puc.getReportedAmount(), 381.0); break;
          case "7101": assertEquals(puc.getReportedAmount(), 382.0); break;
          case "7102": assertEquals(puc.getReportedAmount(), 383.0); break;
          case "7103": assertEquals(puc.getReportedAmount(), 384.0); break;
          case "7104": assertEquals(puc.getReportedAmount(), 385.0); break;
          case "7106": assertEquals(puc.getReportedAmount(), 386.0); break;
          case "7108": assertEquals(puc.getReportedAmount(), 387.0); break;
          case "7110": assertEquals(puc.getReportedAmount(), 388.0); break;
          case "7112": assertEquals(puc.getReportedAmount(), 389.0); break;
          case "7114": assertEquals(puc.getReportedAmount(), 390.0); break;
          case "7115": assertEquals(puc.getReportedAmount(), 391.0); break;
          case "7116": assertEquals(puc.getReportedAmount(), 392.0); break;
          case "7117": assertEquals(puc.getReportedAmount(), 393.0); break;
          case "7118": assertEquals(puc.getReportedAmount(), 394.0); break;
          case "7120": assertEquals(puc.getReportedAmount(), 395.0); break;
          case "7122": assertEquals(puc.getReportedAmount(), 396.0); break;
          case "7124": assertEquals(puc.getReportedAmount(), 397.0); break;
          case "7126": assertEquals(puc.getReportedAmount(), 398.0); break;
          case "7128": assertEquals(puc.getReportedAmount(), 399.0); break;
          case "7129": assertEquals(puc.getReportedAmount(), 400.0); break;
          case "7130": assertEquals(puc.getReportedAmount(), 401.0); break;
          case "7132": assertEquals(puc.getReportedAmount(), 402.0); break;
          case "7134": assertEquals(puc.getReportedAmount(), 403.0); break;
          case "7140": assertEquals(puc.getReportedAmount(), 404.0); break;
          case "7142": assertEquals(puc.getReportedAmount(), 405.0); break;
          case "7144": assertEquals(puc.getReportedAmount(), 406.0); break;
          case "7146": assertEquals(puc.getReportedAmount(), 407.0); break;
          case "7148": assertEquals(puc.getReportedAmount(), 408.0); break;
  
          case "104": assertEquals(puc.getReportedAmount(), 409.0); break;
          case "105": assertEquals(puc.getReportedAmount(), 410.0); break;
          case "106": assertEquals(puc.getReportedAmount(), 411.0); break;
  
          case "100": assertEquals(puc.getReportedAmount(), 419.0); break;
          case "101": assertEquals(puc.getReportedAmount(), 420.0); break;
          case "102": assertEquals(puc.getReportedAmount(), 421.0); break;
          case "103": assertEquals(puc.getReportedAmount(), 422.0); break;
          case "111": assertEquals(puc.getReportedAmount(), 423.0); break;
          case "112": assertEquals(puc.getReportedAmount(), 424.0); break;
          case "113": assertEquals(puc.getReportedAmount(), 425.0); break;
          case "114": assertEquals(puc.getReportedAmount(), 426.0); break;
          case "115": assertEquals(puc.getReportedAmount(), 427.0); break;
          case "117": assertEquals(puc.getReportedAmount(), 428.0); break;
          case "118": assertEquals(puc.getReportedAmount(), 429.0); break;
          case "122": assertEquals(puc.getReportedAmount(), 430.0); break;
          case "126": assertEquals(puc.getReportedAmount(), 431.0); break;
          case "127": assertEquals(puc.getReportedAmount(), 432.0); break;
          case "128": assertEquals(puc.getReportedAmount(), 433.0); break;
          case "129": assertEquals(puc.getReportedAmount(), 434.0); break;
          case "130": assertEquals(puc.getReportedAmount(), 435.0); break;
          case "132": assertEquals(puc.getReportedAmount(), 436.0); break;
          case "136": assertEquals(puc.getReportedAmount(), 437.0); break;
          case "138": assertEquals(puc.getReportedAmount(), 438.0); break;
          case "149": assertEquals(puc.getReportedAmount(), 439.0); break;
          case "150": assertEquals(puc.getReportedAmount(), 440.0); break;
          case "151": assertEquals(puc.getReportedAmount(), 441.0); break;
          case "152": assertEquals(puc.getReportedAmount(), 442.0); break;
          case "166": assertEquals(puc.getReportedAmount(), 443.0); break;
          case "167": assertEquals(puc.getReportedAmount(), 444.0); break;
          case "178": assertEquals(puc.getReportedAmount(), 445.0); break;
          case "191": assertEquals(puc.getReportedAmount(), 446.0); break;
          case "192": assertEquals(puc.getReportedAmount(), 447.0); break;
          case "193": assertEquals(puc.getReportedAmount(), 448.0); break;
          case "194": assertEquals(puc.getReportedAmount(), 449.0); break;
          case "195": assertEquals(puc.getReportedAmount(), 450.0); break;
          case "196": assertEquals(puc.getReportedAmount(), 451.0); break;
        }
  
        assertEquals(data.getLayersEggsForHatching_108(), 412.0);
        assertEquals(data.getLayersEggsForConsumption_109(), 413.0);
        assertEquals(data.getBroilersChickens_143(), 414.0);
        assertEquals(data.getBroilersTurkeys_144(), 415.0);
  
        assertEquals(data.getProductiveCapacityLC123(), 416.0);
        assertEquals(data.getFeederHogsFedOver50Lbs_124(), 417.0);
        assertEquals(data.getFeederHogsFedUpTo50Lbs_125(), 418.0);
      }
      
    } finally {
      deleteSubmissionsFromFarm(submissionGuid);
      deleteValidationErrorTasksBySubmissionGuids(submissionGuid);
      deleteSubmissionsFromChefs(submissionGuid);
      deletePin(participantPin);
    }
    
  }

  private SubmissionParentResource<NppSubmissionDataResource> buildSubmissionMetaData() {

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = new SubmissionParentResource<>();
    submissionMetaData.setDeleted(false);
    submissionMetaData.setDraft(false);

    SubmissionResource<NppSubmissionDataResource> submission = new SubmissionResource<>();
    submissionMetaData.setSubmission(submission);

    NppSubmissionDataResource data = new NppSubmissionDataResource();
    submission.setData(data);

    return submissionMetaData;
  }

  @Override
  protected String getChefsFormType() {
    return CHEFS_FORM_TYPE;
  }

  @Override
  protected String getFormUserType() {
    return ChefsConstants.USER_TYPE_BASIC_BCEID;
  }

}
