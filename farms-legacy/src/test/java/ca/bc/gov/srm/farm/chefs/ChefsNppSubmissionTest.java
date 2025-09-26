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

import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
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
import ca.bc.gov.srm.farm.chefs.processor.NppSubmissionProcessor;
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
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.Client;
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
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class ChefsNppSubmissionTest extends ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsNppSubmissionTest.class);

  private final String CHEFS_FORM_TYPE = ChefsFormTypeCodes.NPP;


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

    SubmissionWrapperResource<NppSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NppSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    logger.info(submission.toString());
    assertNotNull(submission);
  }

  @Test
  public void getSubmissionIndividual() {

    String submissionGuid = "db4e9969-c2cf-44f1-b565-93343aba0e2c";
    assertNotNull(submissionGuid);

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<NppSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NppSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    logger.debug(submissionMetaData.toString());
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NppSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("Johnny", data.getFirstName());
    assertEquals("Appleseed", data.getLastName());
    assertEquals("individual", data.getBusinessStructure());
    assertEquals(Integer.valueOf(31415927), data.getAgriStabilityAgriInvestPin());
    assertEquals("(640) 555-5555", data.getTelephone());
    assertEquals("987654321", data.getSinNumber());
    assertEquals("123 Home Road", data.getAddress());
    assertEquals("37", data.getMunicipalityCode());
    assertEquals("cash", data.getAccountingCode());

    assertEquals("treefruit", data.getWhatIsYourMainFarmingActivity());
    assertArrayEquals(Arrays.asList(354343, 234234).toArray(), data.getProductionInsuranceGrowerNumber().toArray());
    assertArrayEquals(Arrays.asList("treefruitGrapes").toArray(), data.getCommoditiesFarmed().toArray());

    assertEquals("yes", data.getDidYouCompleteAProductionCycle());
    assertEquals("external", data.getOrigin());
    assertNull(data.getInternalMethod());
    assertEquals("chefsForm", data.getExternalMethod());
    assertEquals("DEV", data.getEnvironment());

  }

  @Test
  public void getSubmissionCorporation() {

    String submissionGuid = "7ea099a9-a737-486a-8dd5-c63414dbbeab";

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<NppSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NppSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    logger.debug(submissionMetaData.toString());
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NppSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertNull(data.getFirstName());
    assertNull(data.getLastName());
    assertEquals("Apples R Us", data.getCorporationName());
    assertEquals("corporation", data.getBusinessStructure());
    assertEquals("admin@farmer.ca", data.getEmail());
    assertEquals(Integer.valueOf(31415966), data.getAgriStabilityAgriInvestPin());
    assertEquals("(648) 452-4357", data.getTelephone());
    assertEquals("T5Y 4R4", data.getPostalCode());
    assertEquals("999988888", data.getBusinessTaxNumberBn());
    assertEquals("1234 Home Road", data.getAddress());
    assertEquals("Penticton", data.getTownCity());
    assertEquals("2023", data.getFirstYearReporting());
    assertEquals(3, data.getPartnershipInformation().size());
    assertEquals("cash", data.getAccountingCode());

    assertNull(data.getSinNumber());
    assertEquals("37", data.getMunicipalityCode());
    assertEquals("external", data.getOrigin());
    assertEquals("chefsForm", data.getExternalMethod());
    assertEquals("DEV", data.getEnvironment());

    assertEquals("Paul", data.getThirdPartyFirstName());
    assertEquals("Bunyan", data.getThirdPartyLastName());
    assertEquals("Lumber Inc", data.getThirdPartyBusinessName());
    assertEquals("345 Business Street", data.getThirdPartyAddress());
    assertEquals("Kelowna", data.getThirdPartyTownCity());
    assertEquals("BC", data.getThirdPartyProvince());
    assertEquals("V4N 0C0", data.getThirdPartyPostalCode());
    assertEquals("(604) 555-5555", data.getThirdPartyTelephone());
    assertEquals("(604) 125-9338", data.getThirdPartyFax());

    PartnershipInformation p1 = new PartnershipInformation("345345", "partner one", 10.0);
    PartnershipInformation p2 = new PartnershipInformation("122222", "partner two", 20.0);
    PartnershipInformation p3 = new PartnershipInformation("431444", "Tri-Partner Inc", 30.0);
    assertArrayEquals(Arrays.asList(p1, p2, p3).toArray(), data.getPartnershipInformation().toArray());
    assertEquals(3, data.getPartnershipInformation().size());
    assertNull(data.getTreefruitsFarmed());
    assertEquals("otherPleaseSpecify", data.getWhatIsYourMainFarmingActivity());
    assertEquals("salmon", data.getSpecifyOther());
    assertEquals("5572HayClover", data.getForageBasketGrid().get(0).getCrop());

  }

  @Test
  public void invalidBusinessNumber() {

    String submissionGuid = "00000000-0000-0001-0004-000000000000";
    Integer participantPin = getUnusedParticpantPin();

    deleteValidationErrorTasksBySubmissionId(submissionGuid);
    
    deleteSubmission(submissionGuid);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NppSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setCorporationName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_COOPERATIVE);
    farmType.setLabel("Co-operative");
    data.setFarmType(farmType);
    
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setSinNumber(null);
    data.setBusinessTaxNumberBn("12345678");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setProvince("BC");
    data.setPostalCode("w0w 3e3");
    data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setEnvironment("DEV");
    data.setWhatIsYourMainFarmingActivity("livestock");

    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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

    assertEquals("2023 NPP " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " NEW PARTICIPANT PLAN form was submitted but has validation errors:\n" + "\n"
        + "- Business Number in BCFARMS does not start with a 9 digit number. Unable to validate.\n\n" + "Environment: DEV\n" + "\n"
        + "First Name: \n" + "Last Name: \n" + "Corporate Name: Targaryen Kingdom\n" + "Telephone: (250) 555-5555\n"
        + "Email: targaryen@game.of.thrones\n" + "Participant Type: coOperative\n" + "Business Number: 12345678RC0001\n",
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
    assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());
    
    try {
      CrmTaskResource  validationErrorTask = getValidationErrorBySubmissionId(submissionGuid);
      logger.debug(validationErrorTask.toString());
      assertEquals("2023 NPP " + participantPin, validationErrorTask.getSubject());
    } catch (ServiceException e) {
      e.printStackTrace();
    }

    deleteSubmission(submissionGuid);
  }


  @Test
  public void corporationHappyPath() {

    Integer participantPin = getUnusedParticpantPin();
    Integer programYear = 2023;
    
    assertNotNull(participantPin);

    // NPP IDIR formId and formVersionId
    String formId = "cdffa52c-8995-4518-960c-0b14fa3077e8";
    String formVersionId = "2d891d0b-865b-4efc-9044-5dd37d0c49ca";

    NppSubmissionDataResource data = new NppSubmissionDataResource();

    data.setExistingAccount(false);
    data.setLateParticipant(false);
    String corporationName = "CORP " + participantPin + " TEST";
    data.setCorporationName(corporationName);
    data.setFirstNameCorporateContact("CORPFIRST");
    data.setLastNameCorporateContact("CORPLAST");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_CORPORATION);
    farmType.setLabel("Corporation");
    data.setFarmType(farmType);
    
    data.setEmail("ADMIN@FARMER.CA");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setTelephone("(648) 452-4357");
    data.setPostalCode("T5Y 4R4");
    data.setBusinessTaxNumberBn("9999 28888");
    data.setAddress("1234 HOME ROAD");
    data.setTownCity("PENTICTON");
    data.setProvince("BC");
    data.setFirstYearReporting("2022");
    data.setAccountingCode("cash");
    data.setLateEntry(false);

    data.setMunicipalityCode("37");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    data.setAuthorizeThirdParty("yes");
    data.setThirdPartyFirstName("PAUL");
    data.setThirdPartyLastName("BUNYAN");
    data.setThirdPartyBusinessName("LUMBER INC");
    data.setThirdPartyEmail("PAUL@LUMBER.INC");
    data.setThirdPartyAddress("345 BUSINESS STREET");
    data.setThirdPartyTownCity("KELOWNA");
    data.setThirdPartyProvince("BC");
    data.setThirdPartyPostalCode("V4N 0C0");
    data.setThirdPartyTelephone("(604) 555-5555");
    data.setThirdPartyFax("(604) 125-9338");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setAccountingCode("cash");
    data.setWhatIsYourMainFarmingActivity("treefruit");
    data.setBlueberry36YearProductionAcres_5062(12.0);
    data.setCranberry4thYearProductionAcres_4994(60.0);
    data.setBredCow_104(123.0);
    data.setHogsFarrowing_145(141.0);
    data.setHoneybees_126(126.8);
    data.setLeafCutterBees_129(129.6);
    data.setBroilersTurkeys_144(12.4);
    data.setBroilersChickens_143(13.0);
    data.setLayersEggsForConsumption_109(32.2);
    data.setLayersEggsForHatching_108(80.0);
    data.setFeederCattleFedUpTo900Lbs_105(34.0);
    data.setFeederCattleFedOver900Lbs_106(90.0);
    data.setNumberOfCustomFedCattle_141(41.0);
    data.setNumberOfCustomFedHogs_142(42.0);
    data.setFeederHogsFedOver50Lbs_124(2.0);
    data.setFeederHogsFedUpTo50Lbs_125(125.0);
    data.setDairyOfButterfatPerDay_113(113.0);
    data.setGala5YearProductionAcres_4826(22.0);
    data.setGala24YearProductionAcres_4824(12.0);
    data.setGala1stYearProductionAcres_4822(1.0);
    data.setDidYouCompleteAProductionCycle("yes");
    data.setDidYouStartFarmingWithinTheLastSixMonths("yes");
    data.setWhatIsYourMainFarmingActivity("treefruit");
    data.setDoYouHaveMultipleOperations("no");
    NppCropGrid fbg1 = new NppCropGrid();
    fbg1.setCrop("5572HayClover2");
    fbg1.setAcres(5.0);
    NppCropGrid fbg2 = new NppCropGrid();
    fbg2.setCrop("5564HayAlfalfa");
    fbg2.setAcres(6.0);
    data.setForageBasketGrid(Arrays.asList(fbg1, fbg2));
    
    NppCropGrid cbt = new NppCropGrid();
    cbt.setCrop("6970BeansAdzuki");
    cbt.setAcres(697.0);
    data.setCropBasketTypeGrid(Arrays.asList(cbt));
    
    data.setBredCow_104(2.0);
    data.setChristmasTreesEstablishmentAcres(0.0);
    data.setChristmasTreesEstablishmentAcres1(1.1);
    data.setChristmasTreesEstablishmentAcres2(2.0);

    data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setFiscalYearStart(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setSignDate(new Date());
    data.setThirdPartySignDate(new Date().toString());

    PartnershipInformation p1 = new PartnershipInformation("345345", "partner one", 10.0);
    PartnershipInformation p2 = new PartnershipInformation("122222", "partner two", 20.0);
    PartnershipInformation p3 = new PartnershipInformation("431444", "Tri-Partner Inc", 30.0);
    data.setPartnershipInformation(Arrays.asList(p1, p2, p3));
    data.setCommoditiesFarmed(Arrays.asList("treefruitGrapes", "grainLivestock", "nurseriesGreenhouses"));

    TreeFruitsFarmed tff = new TreeFruitsFarmed();
    tff.setApples(true);
    tff.setCherries(false);
    tff.setGrapes(false);
    tff.setTreefruit(false);
    data.setTreefruitsFarmed(tff);

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
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    resultData.setSubmissionGuid(submissionGuid);
    logger.debug("submissionGuid: " + submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertTrue(programYearMetadata.isEmpty());

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

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
    Client client = scenario.getClient();
    assertNotNull(client);
    assertEquals(participantPin, client.getParticipantPin());
    assertEquals("999928888" + BUSINESS_NUMBER_SUFFIX, client.getBusinessNumber());
    assertEquals(programYear, scenario.getYear());
    assertEquals(nppScenarioNumber, scenario.getScenarioNumber());

    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationByNumber(1);
    List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();

    HashMap<String, Double> productiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : pucs) {
      logger.debug("getProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      productiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    assertEquals(Double.valueOf(12.0), productiveUnitsMap.get("5062"));
    assertEquals(Double.valueOf(12.4), productiveUnitsMap.get("144"));
    assertEquals(Double.valueOf(60.0), productiveUnitsMap.get("4994"));
    assertEquals(Double.valueOf(90.0), productiveUnitsMap.get("106"));
    assertEquals(Double.valueOf(22.0), productiveUnitsMap.get("4826"));
    
    CrmAccountResource crmAccount = null;
    try {
      crmAccount = crmDao.getAccountByPin(participantPin);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(crmAccount);
    
    assertEquals(participantPin.toString(), crmAccount.getVsi_pin());
    assertEquals("999928888", crmAccount.getVsi_businessnumber());
    assertEquals(corporationName, crmAccount.getName());
  }
  
  @Test
  public void individualLateParticipantHappyPath() {

    Integer participantPin = getUnusedParticpantPin();
    Integer programYear = 2024;

    // NPP IDIR formId and formVersionId
    String formId = "cdffa52c-8995-4518-960c-0b14fa3077e8";
    String formVersionId = "b6c1d36a-f4ae-4023-aa50-c98fd8788f02";

    NppSubmissionDataResource data = new NppSubmissionDataResource();

    data.setExistingAccount(false);
    data.setLateParticipant(true);
    data.setFirstName("JOHNNY");
    data.setLastName("APPLESEED");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
    farmType.setLabel("Individual");
    data.setFarmType(farmType);
    
    data.setEmail("JOHNNY@FARMER.CA");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setTelephone("(648) 452-4357");
    data.setPostalCode("T5Y 4R4");
    data.setSinNumber("123456789");
    data.setAddress("12 HOME ROAD");
    data.setTownCity("PENTICTON");
    data.setProvince("BC");
    data.setFirstYearReporting("2022");
    data.setAccountingCode("cash");
    data.setLateEntry(false);
    
    data.setMunicipalityCode("35");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setAccountingCode("cash");
    data.setWhatIsYourMainFarmingActivity("nursery");
    data.setBlueberryPlantingYearAcres_5059(55.0);
    data.setChristmasTreesEstablishmentAcres(90.0);
    data.setDidYouCompleteAProductionCycle("yes");
    data.setDidYouStartFarmingWithinTheLastSixMonths("yes");
    data.setWhatIsYourMainFarmingActivity("treefruit");
    data.setDoYouHaveMultipleOperations("no");
    NppCropGrid fbg1 = new NppCropGrid();
    fbg1.setCrop("5572HayClover");
    fbg1.setAcres(5.0);
    NppCropGrid fbg2 = new NppCropGrid();
    fbg2.setCrop("5564HayAlfalfa");
    fbg2.setAcres(6.0);
    data.setForageBasketGrid(Arrays.asList(fbg1, fbg2));
    
    NppCropGrid cbt = new NppCropGrid();
    cbt.setCrop("6970BeansAdzuki");
    cbt.setAcres(697.0);
    data.setCropBasketTypeGrid(Arrays.asList(cbt));
    
    data.setFiscalYearEnd(Date.from(LocalDate.of(programYear, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setFiscalYearStart(Date.from(LocalDate.of(programYear, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setSignDate(new Date());

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
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    resultData.setSubmissionGuid(submissionGuid);
    logger.debug("submissionGuid: " + submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertTrue(programYearMetadata.isEmpty());

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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
    assertNotNull(task);
    assertNotNull(task.getAccountId());
    assertEquals(programYear + " LATE NPP " + participantPin, task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        "Primary Farming Activity: treefruit",
        task.getDescription());

    assertEquals(submissionGuid, submissionRec.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec.getSubmissionStatusCode());
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

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
    assertTrue(scenario.getFarmingYear().getIsLateParticipant());
    assertEquals(participantPin, scenario.getClient().getParticipantPin());
    assertEquals(programYear, scenario.getYear());
    assertEquals(nppScenarioNumber, scenario.getScenarioNumber());

    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationByNumber(1);
    List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();

    HashMap<String, Double> productiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : pucs) {
      logger.debug("getProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      productiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    assertEquals(Double.valueOf(55.0), productiveUnitsMap.get("5059"));
    assertEquals(Double.valueOf(90.0), productiveUnitsMap.get("6960"));
    assertEquals(Double.valueOf(5.0), productiveUnitsMap.get("5572"));
    assertEquals(Double.valueOf(6.0), productiveUnitsMap.get("5564"));
    assertEquals(Double.valueOf(697.0), productiveUnitsMap.get("6970"));
  }

  @Test
  public void individualExistingParticipantPinHappyPath() {

    Integer participantPin = 197623465;
    Integer programYear = 2023;

    // NPP IDIR formId and formVersionId
    String formId = "cdffa52c-8995-4518-960c-0b14fa3077e8";
    String formVersionId = "2d891d0b-865b-4efc-9044-5dd37d0c49ca";

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

    PartnershipInformation p1 = new PartnershipInformation("345345", "partner one", 10.0);
    PartnershipInformation p2 = new PartnershipInformation("122222", "partner two", 20.0);
    PartnershipInformation p3 = new PartnershipInformation("431444", "Tri-Partner Inc", 30.0);
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
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    resultData.setSubmissionGuid(submissionGuid);
    logger.debug("submissionGuid: " + submissionGuid);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);

    // Delete the submission if it exists, from a previous test run.
    deleteSubmission(submissionGuid);

    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);

    // Process the submission data
    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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
    assertNull(submissionRec.getValidationTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(2, submissionRec.getRevisionCount().intValue());

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

    deleteSubmission(submissionGuid);
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
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.NPP);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555000");
      submissionRec.setMainTaskGuid(null);
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[1]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.NPP);
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setValidationTaskGuid("55555555-5555-5555-5555-555555555001");
      submissionRec.setMainTaskGuid("66666666-6666-6666-6666-666666666001");
      submissionRecords.add(submissionRec);
    }
    {
      ChefsSubmission submissionRec = new ChefsSubmission();
      submissionRec.setSubmissionGuid(submissionGuidArray[2]);
      submissionRec.setFormTypeCode(ChefsFormTypeCodes.NPP);
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
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
      assertEquals(ChefsFormTypeCodes.NPP, submissionRec.getFormTypeCode());
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
  public void readNppSubmissionsInFARM() {

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, ChefsFormTypeCodes.NPP);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(chefsSubmissions);

    for (ChefsSubmission submission : chefsSubmissions) {
      assertEquals(ChefsFormTypeCodes.NPP, submission.getFormTypeCode());
      assertNotNull(submission.getSubmissionId());
      assertNotNull(submission.getSubmissionGuid());
      assertNotNull(submission.getSubmissionStatusCode());
      assertNotNull(submission.getRevisionCount());
    }
  }

  @Test
  public void submissionAlreadyProcessed() {
    String submissionGuid = "db4e9969-c2cf-44f1-b565-93343aba0e2c";

    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
    processor.setUser(user);
    try {
      processor.loadSubmissionsFromChefs();

      List<SubmissionListItemResource> submissionItems = processor.getSubmissionItems();

      List<SubmissionListItemResource> newSubmissionItems = submissionItems.stream().filter(s -> s.getSubmissionGuid().equals(submissionGuid))
          .collect(Collectors.toList());
      processor.setSubmissionItems(newSubmissionItems);

      processor.loadSubmissionsFromDatabase();

      Map<String, ChefsSubmission> submissionRecordMap = processor.getSubmissionRecordMap();

      assertTrue(submissionRecordMap.size() > 0);
      assertEquals(1, processor.getSubmissionItems().size());

      ChefsSubmission processedSubmission = submissionRecordMap.get(submissionGuid);
      assertEquals(submissionGuid, processedSubmission.getSubmissionGuid());
      assertEquals(ChefsFormTypeCodes.NPP, processedSubmission.getFormTypeCode());
      assertEquals("PROCESSED", processedSubmission.getSubmissionStatusCode());

      processor.processSubmissions();

    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

  @Test
  public void sinMismatch() {

    String submissionGuid = "00000000-0000-0001-0003-000000000002";

    deleteSubmission(submissionGuid);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NppSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setFirstName("Jon");
    data.setLastName("Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
    farmType.setLabel("Individual");
    data.setFarmType(farmType);
    
    data.setSinNumber("123456780");
    data.setAgriStabilityAgriInvestPin(314155940);
    data.setBusinessTaxNumberBn("");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setPostalCode("w0w 3e3");
    data.setProvince("BC");
    data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setEnvironment("DEV");
    data.setWhatIsYourMainFarmingActivity("otherPleaseSpecify");
    data.setSpecifyOther("salmon");

    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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
    assertEquals("2023 NPP 314155940", validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), validationTask.getStatusCode());
    assertEquals(formUserType + " NEW PARTICIPANT PLAN form was submitted but has validation errors:\n" + "\n"
        + "- Field \"SIN Number\" with value \"123456780\" does not match BCFARMS: \"123456789\".\n" + "\n" + "Environment: DEV\n" + "\n"
        + "First Name: Jon\n" + "Last Name: Snow\n" + "Corporate Name: \n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n"
        + "Participant Type: individual\n" + "SIN Number: 123456780\n", validationTask.getDescription());

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
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(validationTask.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    validationTask = completeAndGetValidationErrorTask(validationTask);

    assertNotNull(validationTask);
    assertNotNull(validationTask.getAccountId());
    assertEquals("2023 NPP 314155940", validationTask.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED), validationTask.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATUS_CODE_NOT_STARTED), validationTask.getStatusCode());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void businessNumberMismatch() {

    String submissionGuid = "00000000-0000-0001-0004-000000000001";

    deleteSubmission(submissionGuid);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NppSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setCorporationName("Targaryen Kingdom");
    data.setTelephone("(250) 555-5555");
    data.setEmail("targaryen@game.of.thrones");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_LIMITED_PARTNERSHIP);
    farmType.setLabel("Limited partnership");
    data.setFarmType(farmType);
    
    data.setSinNumber(null);
    data.setAgriStabilityAgriInvestPin(5070370);
    data.setBusinessTaxNumberBn("1234 56780");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setPostalCode("w0w 3e3");
    data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setEnvironment("DEV");
    data.setWhatIsYourMainFarmingActivity("otherPleaseSpecify");
    data.setSpecifyOther("salmon");

    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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

    assertNotNull(task.getAccountId()); // This PIN does not exist in CRM
    assertEquals("2023 NPP 5070370", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(formUserType + " NEW PARTICIPANT PLAN form was submitted but has validation errors:\n" + "\n"
        + "- Field \"Business Number\" with value \"123456780RC0001\" does not match BCFARMS: \"999999999RC0001\". Note that only the first nine digits are compared.\n"
        + "\n" + "Environment: DEV\n" + "\n" + "First Name: \n" + "Last Name: \n" + "Corporate Name: Targaryen Kingdom\n"
        + "Telephone: (250) 555-5555\n" + "Email: targaryen@game.of.thrones\n" + "Participant Type: corporation\n"
        + "Business Number: 123456780RC0001\n", task.getDescription());

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
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }
  
  @Test
  public void fiscalYearEndMissing() {

    String submissionGuid = "00000000-YEAR-0001-0003-000000000002";

    deleteValidationErrorTasksBySubmissionId(submissionGuid);

    deleteSubmission(submissionGuid);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = buildSubmissionMetaData();
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    NppSubmissionDataResource data = submission.getData();

    submissionMetaData.setSubmissionGuid(submissionGuid);

    data.setFirstName("Jon");
    data.setLastName("Snow");
    data.setTelephone("(250) 555-5555");
    data.setEmail("jsnow@game.of.thrones");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_INDIVIDUAL);
    farmType.setLabel("Individual");
    data.setFarmType(farmType);
    
    data.setAgriStabilityAgriInvestPin(3693470);
    data.setSinNumber("999999999");
    data.setBusinessTaxNumberBn(null);
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setWhatIsYourMainFarmingActivity("otherPleaseSpecify");
    data.setSpecifyOther("salmon");

    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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
    Integer programYear = DateUtils.getYearFromDate(new Date());
    assertEquals(programYear + " NPP 3693470", task.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task.getStatusCode());
    assertEquals(
        formUserType + " NEW PARTICIPANT PLAN form was submitted but has validation errors:\n\n"
            + "- Required field is blank: Fiscal Year End\n" + "\n"
            + "Environment: DEV\n" + "\n"
            + "First Name: Jon\n" + "Last Name: Snow\n" + "Corporate Name: \n" + "Telephone: (250) 555-5555\n" + "Email: jsnow@game.of.thrones\n"
            + "Participant Type: individual\n" + "SIN Number: 999999999\n", task.getDescription());

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
    assertEquals(ChefsSubmissionStatusCodes.INVALID, submissionRec.getSubmissionStatusCode());
    assertEquals(task.getActivityId(), submissionRec.getValidationTaskGuid());
    assertNull(submissionRec.getMainTaskGuid());
    assertNotNull(submissionRec.getSubmissionId());
    assertNotNull(submissionRec.getRevisionCount());
    assertEquals(1, submissionRec.getRevisionCount().intValue());

    deleteSubmission(submissionGuid);
  }

  @Test
  public void duplicateSubmission() {

    Integer participantPin = getUnusedParticpantPin();
    Integer programYear = 2023;
    
    assertNotNull(participantPin);

    // NPP IDIR formId and formVersionId
    String formId = "cdffa52c-8995-4518-960c-0b14fa3077e8";
    String formVersionId = "2d891d0b-865b-4efc-9044-5dd37d0c49ca";

    NppSubmissionDataResource data = new NppSubmissionDataResource();

    data.setExistingAccount(false);
    data.setLateParticipant(false);
    String corporationName = "CORP " + participantPin + " TEST";
    data.setCorporationName(corporationName);
    data.setFirstNameCorporateContact("CORPFIRST");
    data.setLastNameCorporateContact("CORPLAST");
    
    LabelValue farmType = new LabelValue();
    farmType.setValue(FIELD_VALUE_FARM_TYPE_CORPORATION);
    farmType.setLabel("Corporation");
    data.setFarmType(farmType);
    
    data.setEmail("ADMIN@FARMER.CA");
    data.setAgriStabilityAgriInvestPin(participantPin);
    data.setTelephone("(648) 452-4357");
    data.setPostalCode("T5Y 4R4");
    data.setBusinessTaxNumberBn("9999 28888");
    data.setAddress("1234 HOME ROAD");
    data.setTownCity("PENTICTON");
    data.setProvince("BC");
    data.setFirstYearReporting("2022");
    data.setAccountingCode("cash");
    data.setLateEntry(false);

    data.setMunicipalityCode("37");
    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");

    data.setAuthorizeThirdParty("yes");
    data.setThirdPartyFirstName("PAUL");
    data.setThirdPartyLastName("BUNYAN");
    data.setThirdPartyBusinessName("LUMBER INC");
    data.setThirdPartyEmail("PAUL@LUMBER.INC");
    data.setThirdPartyAddress("345 BUSINESS STREET");
    data.setThirdPartyTownCity("KELOWNA");
    data.setThirdPartyProvince("BC");
    data.setThirdPartyPostalCode("V4N 0C0");
    data.setThirdPartyTelephone("(604) 555-5555");
    data.setThirdPartyFax("(604) 125-9338");

    data.setOrigin("external");
    data.setExternalMethod("chefsForm");
    data.setEnvironment("DEV");
    data.setAccountingCode("cash");
    data.setWhatIsYourMainFarmingActivity("treefruit");
    data.setBlueberry36YearProductionAcres_5062(12.0);
    data.setCranberry4thYearProductionAcres_4994(60.0);
    data.setBredCow_104(123.0);
    data.setHogsFarrowing_145(141.0);
    data.setHoneybees_126(126.8);
    data.setLeafCutterBees_129(129.6);
    data.setBroilersTurkeys_144(12.4);
    data.setBroilersChickens_143(13.0);
    data.setLayersEggsForConsumption_109(32.2);
    data.setLayersEggsForHatching_108(80.0);
    data.setFeederCattleFedUpTo900Lbs_105(34.0);
    data.setFeederCattleFedOver900Lbs_106(90.0);
    data.setNumberOfCustomFedCattle_141(41.0);
    data.setNumberOfCustomFedHogs_142(42.0);
    data.setFeederHogsFedOver50Lbs_124(2.0);
    data.setFeederHogsFedUpTo50Lbs_125(125.0);
    data.setDairyOfButterfatPerDay_113(113.0);
    data.setGala5YearProductionAcres_4826(22.0);
    data.setGala24YearProductionAcres_4824(12.0);
    data.setGala1stYearProductionAcres_4822(1.0);
    data.setDidYouCompleteAProductionCycle("yes");
    data.setDidYouStartFarmingWithinTheLastSixMonths("yes");
    data.setWhatIsYourMainFarmingActivity("treefruit");
    data.setDoYouHaveMultipleOperations("no");
    NppCropGrid fbg1 = new NppCropGrid();
    fbg1.setCrop("5572HayClover2");
    fbg1.setAcres(5.0);
    NppCropGrid fbg2 = new NppCropGrid();
    fbg2.setCrop("5564HayAlfalfa");
    fbg2.setAcres(6.0);
    data.setForageBasketGrid(Arrays.asList(fbg1, fbg2));
    
    NppCropGrid cbt = new NppCropGrid();
    cbt.setCrop("6970BeansAdzuki");
    cbt.setAcres(697.0);
    data.setCropBasketTypeGrid(Arrays.asList(cbt));
    
    data.setBredCow_104(2.0);
    data.setChristmasTreesEstablishmentAcres(0.0);
    data.setChristmasTreesEstablishmentAcres1(1.1);
    data.setChristmasTreesEstablishmentAcres2(2.0);

    data.setFiscalYearEnd(Date.from(LocalDate.of(2023, 12, 31).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setFiscalYearStart(Date.from(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    data.setSignDate(new Date());
    data.setThirdPartySignDate(new Date().toString());

    PartnershipInformation p1 = new PartnershipInformation("345345", "partner one", 10.0);
    PartnershipInformation p2 = new PartnershipInformation("122222", "partner two", 20.0);
    PartnershipInformation p3 = new PartnershipInformation("431444", "Tri-Partner Inc", 30.0);
    data.setPartnershipInformation(Arrays.asList(p1, p2, p3));
    data.setCommoditiesFarmed(Arrays.asList("treefruitGrapes", "grainLivestock", "nurseriesGreenhouses"));

    TreeFruitsFarmed tff = new TreeFruitsFarmed();
    tff.setApples(true);
    tff.setCherries(false);
    tff.setGrapes(false);
    tff.setTreefruit(false);
    data.setTreefruitsFarmed(tff);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData1 = buildSubmissionMetaData();

    SubmissionResource<NppSubmissionDataResource> submission1 = new SubmissionResource<>();

    submission1.setData(data);
    submissionMetaData1.setSubmission(submission1);
    submissionMetaData1.setSubmissionGuid(null);

    NppSubmissionRequestDataResource<NppSubmissionDataResource> request = new NppSubmissionRequestDataResource<>();
    request.setDraft(false);
    request.setCreatedBy(user);
    request.setCreatedAt(new Date().toString());
    request.setUpdatedBy(user);
    request.setUpdatedAt(new Date().toString());
    request.setSubmission(submission1);

    String postSubmissionUrl = chefsConfig.postSubmissionUrl(formId, formVersionId);
    assertNotNull(postSubmissionUrl);
    try {
      submissionMetaData1 = chefsApiDao.postNppSubmission(postSubmissionUrl, request);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    NppSubmissionDataResource resultData1 = submissionMetaData1.getSubmission().getData();
    String submissionGuid1 = submissionMetaData1.getSubmissionGuid();
    resultData1.setSubmissionGuid(submissionGuid1);
    logger.debug("submissionGuid1: " + submissionGuid1);

    List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    assertTrue(programYearMetadata.isEmpty());

    Map<String, SubmissionListItemResource> itemResourceMap1 = buildSubmissionItemResourceMap(submissionGuid1);

    // Process the submission data
    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
    processor.setUser(user);
    processor.setItemResourceMap(itemResourceMap1);

    CrmTaskResource task1 = null;
    try {
      processor.loadSubmissionsFromDatabase();
      task1 = processor.processSubmission(submissionMetaData1);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(task1);
    assertNotNull(task1.getAccountId());
    assertEquals(programYear + " NPP " + participantPin, task1.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task1.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task1.getStatusCode());
    assertEquals(
        "Primary Farming Activity: treefruit",
        task1.getDescription());

    // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
    // to track the status of the submission.
    ChefsSubmission submissionRec1 = null;
    try {
      submissionRec1 = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid1);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec1);

    assertEquals(submissionGuid1, submissionRec1.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NPP, submissionRec1.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.PROCESSED, submissionRec1.getSubmissionStatusCode());
    assertNull(submissionRec1.getValidationTaskGuid());
    assertNotNull(submissionRec1.getSubmissionId());
    assertNotNull(submissionRec1.getRevisionCount());
    assertEquals(2, submissionRec1.getRevisionCount().intValue());

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
    Client client = scenario.getClient();
    assertNotNull(client);
    assertEquals(participantPin, client.getParticipantPin());
    assertEquals("999928888" + BUSINESS_NUMBER_SUFFIX, client.getBusinessNumber());
    assertEquals(programYear, scenario.getYear());
    assertEquals(nppScenarioNumber, scenario.getScenarioNumber());

    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationByNumber(1);
    List<ProductiveUnitCapacity> pucs = fo.getProductiveUnitCapacities();

    HashMap<String, Double> productiveUnitsMap = new HashMap<>();
    for (ProductiveUnitCapacity puc : pucs) {
      logger.debug("getProductiveUnitCapacities " + puc.getCode() + " reportedAmount " + puc.getReportedAmount());
      productiveUnitsMap.put(puc.getCode(), puc.getReportedAmount());
    }
    assertEquals(Double.valueOf(12.0), productiveUnitsMap.get("5062"));
    assertEquals(Double.valueOf(12.4), productiveUnitsMap.get("144"));
    assertEquals(Double.valueOf(60.0), productiveUnitsMap.get("4994"));
    assertEquals(Double.valueOf(90.0), productiveUnitsMap.get("106"));
    assertEquals(Double.valueOf(22.0), productiveUnitsMap.get("4826"));
    
    CrmAccountResource crmAccount = null;
    try {
      crmAccount = crmDao.getAccountByPin(participantPin);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(crmAccount);
    
    assertEquals(participantPin.toString(), crmAccount.getVsi_pin());
    assertEquals("999928888", crmAccount.getVsi_businessnumber());
    assertEquals(corporationName, crmAccount.getName());
    
    long scenarioCountAfterSubmission1 = programYearMetadata.stream()
        .filter(s -> s.getProgramYear().equals(programYear))
        .count();
    assertEquals(1, scenarioCountAfterSubmission1);
    
    // Second submission 
    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData2 = buildSubmissionMetaData();

    SubmissionResource<NppSubmissionDataResource> submission2 = new SubmissionResource<>();

    submission2.setData(data);
    submissionMetaData2.setSubmission(submission2);
    submissionMetaData2.setSubmissionGuid(null);
    
    try {
      submissionMetaData2 = chefsApiDao.postNppSubmission(postSubmissionUrl, request);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    NppSubmissionDataResource resultData2 = submissionMetaData2.getSubmission().getData();
    String submissionGuid2 = submissionMetaData2.getSubmissionGuid();
    resultData2.setSubmissionGuid(submissionGuid2);
    logger.debug("submissionGuid: " + submissionGuid2);
    

    Map<String, SubmissionListItemResource> itemResourceMap2 = buildSubmissionItemResourceMap(submissionGuid2);

    // Process the submission data
    processor.setItemResourceMap(itemResourceMap2);

    CrmTaskResource task2 = null;
    try {
      processor.loadSubmissionsFromDatabase();
      task2 = processor.processSubmission(submissionMetaData2);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(task2);
    assertNotNull(task2.getAccountId());
    assertEquals("Duplicate form: " + programYear + " " + processor.getFormShortName() + " " + participantPin, task2.getSubject());
    assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task2.getStateCode());
    assertEquals(Integer.valueOf(CrmConstants.STATUS_CODE_OPEN), task2.getStatusCode());
    assertEquals(
        processor.getFormUserType() + " " + processor.getFormLongName()
        + " form was submitted but has previous submissions for this PIN and program year:\n"
        + "\n"
        + "Form submissions of this type have been previously submitted for this PIN and program year: " + submissionGuid1
        + "\n\n"
        + "Environment: DEV\n",
        task2.getDescription());

    // Get the record from FARM_CHEF_SUBMISSIONS, created by the processor
    // to track the status of the submission.
    ChefsSubmission submissionRec2 = null;
    try {
      submissionRec2 = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid2);
    } catch (DataAccessException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionRec2);

    assertEquals(submissionGuid2, submissionRec2.getSubmissionGuid());
    assertEquals(ChefsFormTypeCodes.NPP, submissionRec2.getFormTypeCode());
    assertEquals(ChefsSubmissionStatusCodes.DUPLICATE, submissionRec2.getSubmissionStatusCode());
    assertNull(submissionRec2.getValidationTaskGuid());
    assertNotNull(submissionRec2.getSubmissionId());
    assertNotNull(submissionRec2.getRevisionCount());
    assertEquals(1, submissionRec2.getRevisionCount().intValue());

    programYearMetadata = getProgramYearMetadata(participantPin, programYear);
    assertNotNull(programYearMetadata);
    
    long scenarioCountAfterSubmission2 = programYearMetadata.stream()
        .filter(s -> s.getProgramYear().equals(programYear))
        .count();
    
    assertEquals(1, scenarioCountAfterSubmission2);
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
  
    Integer participantPin = 23508567;
    Integer programYear = 2023;
    String submissionGuid = "803b23c7-34e6-494b-8034-891a60b8d328";
    assertNotNull(submissionGuid);
  
    // Set to true if this submission has been processed before
    // and you want the Interim USER scenario and submission record
    // to be deleted before processing the submission again.
    boolean reprocess = true;

    if(reprocess) {
      List<ScenarioMetaData> programYearMetadata = getProgramYearMetadata(participantPin, programYear);
      assertNotNull(programYearMetadata);
      assertFalse(programYearMetadata.isEmpty());
  
      deleteSubmission(submissionGuid);
    }


    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);
  
    SubmissionWrapperResource<NppSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NppSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);
  
    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    assertNotNull(submissionMetaData);
  
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  
    NppSubmissionDataResource data = submission.getData();
    assertNotNull(data);
  
    Map<String, SubmissionListItemResource> itemResourceMap = buildSubmissionItemResourceMap(submissionGuid);
  
    // Process the submission data
    NppSubmissionProcessor processor = new NppSubmissionProcessor(conn, formUserType);
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

}
