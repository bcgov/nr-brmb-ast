package ca.bc.gov.srm.farm.cdogs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.cdogs.resource.CdogsOptionsResource;
import ca.bc.gov.srm.farm.cdogs.resource.CdogsTemplateDataResource;
import ca.bc.gov.srm.farm.chefs.ChefsAuthenticationHandler;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.ChefsRestApiDao;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.cashMargin.CashMarginsReportDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageRefScenarioDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageReportDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.nol.NolSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalSubmissionDataResource;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CdogsRestApiTest {

  private final static Logger logger = LoggerFactory.getLogger(CdogsRestApiTest.class);
  protected static final String HTTP_METHOD_DELETE = "DELETE";
  protected static final String HTTP_METHOD_GET = "GET";

  private static CdogsRestApiDao cdogsDao;
  private static ChefsRestApiDao chefsApiDao;
  private static CdogsConfigurationUtil cdogsConfig;
  private static ChefsConfigurationUtil chefsConfig;
  private static ChefsFormCredentials formCredentials;

  @BeforeAll
  protected static void set() throws Exception {
    TestUtils.standardTestSetUp();

    cdogsDao = new CdogsRestApiDao();
    chefsConfig = ChefsConfigurationUtil.getInstance();
    cdogsConfig = CdogsConfigurationUtil.getInstance();

    System.setProperty("generate.cob.enabled", "N");
  }

  @Test
  public void healthCheckTest() {

    String response = null;
    try {
      response = cdogsDao.getHealthCheck();
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
  }
  
  @Test
  public void checkTemplateCacheTest() {
    
    String templateGuid = cdogsConfig.getInterimTemplateGuid(null);
    String response = null;
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getInterimTemplateGuid(2);
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getAdjustmentTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);

    templateGuid = cdogsConfig.getCashMarginsTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);

    templateGuid = cdogsConfig.getCoverageTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getCoverageNoticeReportTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getNolTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getNppTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getSupplementalTemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
    
    templateGuid = cdogsConfig.getStatementATemplateGuid();
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertEquals("OK", response);
  }

  @Test
  public void fileTypesTest() {

    String response = null;
    try {
      response = cdogsDao.getFileTypes();
      assertNotNull(response);
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    String fileTypes = "{\"dictionary\":{\"csv\":[\"csv\",\"doc\",\"docx\",\"html\",\"odt\",\"pdf\",\"rtf\",\"txt\"],\"docx\":[\"doc\",\"docx\",\"html\",\"odt\",\"pdf\",\"rtf\",\"txt\"],\"html\":[\"html\",\"odt\",\"pdf\",\"rtf\",\"txt\"],\"ods\":[\"csv\",\"ods\",\"pdf\",\"txt\",\"xls\",\"xlsx\"],\"odt\":[\"doc\",\"docx\",\"html\",\"odt\",\"pdf\",\"rtf\",\"txt\"],\"pptx\":[\"odt\",\"pdf\",\"ppt\",\"pptx\"],\"rtf\":[\"docx\",\"pdf\"],\"txt\":[\"doc\",\"docx\",\"html\",\"odt\",\"pdf\",\"rtf\",\"txt\"],\"xlsx\":[\"csv\",\"ods\",\"pdf\",\"rtf\",\"txt\",\"xls\",\"xlsx\"]}}";
    assertEquals(fileTypes, response);
  }

  @Test
  public void httpResponseCode401NoTokenTest() throws Exception {

    HttpURLConnection conn;

    try {
      conn = getHttpURLConnection(cdogsConfig.getFileTypeUrl(), HTTP_METHOD_GET);

      int httpResponseCode = conn.getResponseCode();
      assertEquals(401, httpResponseCode);

    } catch (ServiceException | IOException e) {
      throw e;
    }

  }

  @Test
  public void generatePdfFromAdjustmentTemplateGuidTest() {

    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.ADJ, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String adjustmentTemplateGuid = cdogsConfig.getAdjustmentTemplateGuid();
    String submissionGuid = "328a8ecd-e292-46cd-bbdd-0dafdb532233";

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<AdjustmentSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          AdjustmentSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    AdjustmentSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JOHNNY APPLESEED", data.getParticipantName());
    assertEquals("individual", data.getBusinessStructure());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityPin());
    assertEquals("999999999", data.getSinNumber());
    assertEquals("qwerty", data.getOtherDetails());
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
    assertFalse(data.getYearsToAdjust().get("2024"));

    String fileName = "2022_Adjustment_3778842_Form.pdf";
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(adjustmentTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }
  
  @Test
  public void generatePdfFromCashMarginsTemplateGuidTest() {

    int participantPin = 31415945;
    int programYear = 2024;
    int scenarioNumber = 26;
    
    Scenario scenario = null;
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(scenario);
    
    Person owner = scenario.getClient().getOwner();

    CashMarginsReportDataResource cashMarginsReportDataResource = new CashMarginsReportDataResource();
    cashMarginsReportDataResource.setParticipantName(owner.getFullName());
    cashMarginsReportDataResource.setAgriStabilityPin(scenario.getClient().getParticipantPin());
    cashMarginsReportDataResource.setEmail(owner.getEmailAddress());
    cashMarginsReportDataResource.setBusinessStructure("individual");
    cashMarginsReportDataResource.setConfirmationId("Confirm12345567");
    cashMarginsReportDataResource.setSubmissionGuid("d56649d9-ba3e-441b-b454-9e29c2bdf305");
    cashMarginsReportDataResource.setSinNumber("987654321");
    cashMarginsReportDataResource.setBusinessTaxNumberBn("9999 88888");
    cashMarginsReportDataResource.setPhoneNumber(owner.getDaytimePhone());
    cashMarginsReportDataResource.setSignatureDate(new Date());
    cashMarginsReportDataResource.setEnvironment("DEV");
    cashMarginsReportDataResource.setExternalMethod("chefsForm");
    cashMarginsReportDataResource.setCreatedAt(new Date());
    cashMarginsReportDataResource.setUpdatedAt(new Date());
    cashMarginsReportDataResource.setFormVersionId("123213123123");
    cashMarginsReportDataResource.setCreatedBy("TESTER");
    cashMarginsReportDataResource.setUpdatedBy("TESTERTOO");
    cashMarginsReportDataResource.setOrigin("external");
    
    
    String saveFilePath = IOUtils.getTempDirPath() + "/2024_Cash Margins_31415945_Report.pdf";
    File file = new File(saveFilePath);
    
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource(cashMarginsReportDataResource, file.getName());

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      logger.error("Error processing JSON when generating PDF from template: ", e);
      fail("Unexpected Exception");
    }
    
    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    String cashMarginsReportTemplateGuid = cdogsConfig.getCashMarginsTemplateGuid();
    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(cashMarginsReportTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      logger.debug("File path = " +file.getPath());
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }
  
  @Test
  public void generatePdfFromCoverageNoticeReportTemplateGuidTest() {

    int participantPin = 98765684;
    int programYear = 2023;
    int scenarioNumber = 3;
    
    Scenario scenario = null;
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    try {
      scenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    
    assertNotNull(scenario);
    
    Person owner = scenario.getClient().getOwner();

    CoverageReportDataResource coverageReportDataResource = new CoverageReportDataResource();
    coverageReportDataResource.setReportDate(new Date());
    coverageReportDataResource.setParticipantName(owner.getFullName());
    coverageReportDataResource.setAgriStabilityAgriInvestPin(scenario.getClient().getParticipantPin());
    coverageReportDataResource.setProgramYear(scenario.getYear());
    coverageReportDataResource.setAddress(owner.getAddressLine1() + " " + owner.getAddressLine2());
    coverageReportDataResource.setCity(owner.getCity());
    coverageReportDataResource.setProvince(owner.getProvinceState());
    coverageReportDataResource.setPostalCode(owner.getPostalCode());
    coverageReportDataResource.setPaymentCapPercentageOfTotalMarginDecline(
        CalculatorConfig.getPaymentCapPercentageOfTotalMarginDecline(programYear));
    coverageReportDataResource.setStandardPositiveMarginCompensationRate(
        CalculatorConfig.getStandardPositiveMarginCompensationRate(programYear));
    coverageReportDataResource.setStandardPositiveMarginCompensationRate(.80);
    
    Benefit benefit = scenario.getFarmingYear().getBenefit();
    if (benefit != null ) {
      coverageReportDataResource.setReferenceMargin(benefit.getAdjustedReferenceMargin());
      coverageReportDataResource.setReferenceMargin(1024855.67);
    }
    coverageReportDataResource.setTotalMarginDecline(
        coverageReportDataResource.getReferenceMargin() * coverageReportDataResource.getPaymentCapPercentageOfTotalMarginDecline());
    
    List<ReferenceScenario> refs = scenario.getReferenceScenarios(); 
    
    List<CoverageRefScenarioDataResource> refScenarios = new ArrayList<>();
    for (ReferenceScenario ref : refs) {
      MarginTotal marginTotal = ref.getFarmingYear().getMarginTotal();
      CoverageRefScenarioDataResource coverageRefScenario = new CoverageRefScenarioDataResource();

      coverageRefScenario.setYear(ref.getYear());
      coverageRefScenario.setUsedInCalc(ref.getUsedInCalc());
      coverageRefScenario.setStructureChangeApplied(marginTotal.getIsStructuralChangeNotable());
      coverageRefScenario.setTotalEligibleIncome(marginTotal.getTotalAllowableIncome());
      coverageRefScenario.setTotalEligibleExpenses(marginTotal.getTotalAllowableExpenses());
      coverageRefScenario.setProductionMargAccrAdjs(marginTotal.getProductionMargAccrAdjs());
      coverageRefScenario.setPercentageChange(marginTotal.getPercentageOfStructuralChange());
      coverageRefScenario.setProductionMargAftStrChanges(marginTotal.getProductionMargAftStrChangs());
      
      refScenarios.add(coverageRefScenario);
      logger.debug(coverageRefScenario.toString());
    }
    coverageReportDataResource.setRefs(refScenarios);
    
    String pdfFileName = String.format("/%d_Coverage_Notice_%d_Report.pdf", programYear, participantPin);
    String saveFilePath = IOUtils.getTempDirPath() + pdfFileName;
    File file = new File(saveFilePath);
    
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource(coverageReportDataResource, file.getName());

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      logger.error("Error processing JSON when generating PDF from template: ", e);
      fail("Unexpected Exception");
    }
    
    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    String coverageReportTemplateGuid = cdogsConfig.getCoverageNoticeReportTemplateGuid();
    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(coverageReportTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }

  @Test
  public void generatePdfFromCoverageNoticeTemplateGuidTest() {
  
    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.CN, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));
  
    String coverageNoticeTemplateGuid = cdogsConfig.getCoverageTemplateGuid();
    String submissionGuid = "71581b60-4706-48f5-907d-8e640c57d3b2";
    String participantPin = "4375666";
  
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);
  
    SubmissionWrapperResource<CoverageSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          CoverageSubmissionDataResource.class);
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
  
   
    String fileName = "2024_Coverage Notice_" + participantPin + "_Form.pdf";
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);
  
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  
    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);
  
    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }
  
    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(coverageNoticeTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }


  @Test
  public void generatePdfFromInterimTemplateGuidTest() {

    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.INTERIM, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String interimTemplateGuid = cdogsConfig.getInterimTemplateGuid(null);
    String submissionGuid = "7d93ad6f-37c3-4502-a037-fc0db3f4786d";

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          InterimSubmissionDataResource.class);
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


    String fileName = "2024_Interim_31415947_Form.pdf";
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(interimTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }
  
  @Test
  public void generatePdfFromInterimTemplateGuidV2Test() {

    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.INTERIM, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String interimTemplateGuid = cdogsConfig.getInterimTemplateGuid(2);
    String submissionGuid = "7d93ad6f-37c3-4502-a037-fc0db3f4786d";

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          InterimSubmissionDataResource.class);
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


    String fileName = "2024_Interim_v2_31415926_Form.pdf";
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(interimTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

  @Test
  public void generatePdfFromNolTemplateGuidTest() {

    String nppTemplateGuid = cdogsConfig.getNolTemplateGuid();

    String submissionGuid = "847e9d7e-0a73-495c-a444-baecf6b061fd";
    
    String fileName = "2023_NOL_3778842_Form.pdf";
    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }
    
    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.NOL, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<NolSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          NolSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NolSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(nppTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }

  @Test
  public void generatePdfFromNppTemplateGuidTest() {

    String nppTemplateGuid = cdogsConfig.getNppTemplateGuid();
    String submissionGuid = "bf693d24-6589-43f7-93ff-8f9bd851e8af";
    
    String fileName = "2023_NPP_23846074_Form.pdf";
    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }
    
    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.NPP, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<NppSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          NppSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    NppSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(nppTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }
  
  @Test
  public void generatePdfFromSupplementalTemplateGuidTest() {

    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.SUPP, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String supplementalTemplateGuid = cdogsConfig.getSupplementalTemplateGuid();
    String submissionGuid = "948dd6fa-6c13-48bc-ab9c-5849b9ed0eb2";

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);

    SubmissionWrapperResource<SupplementalSubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          SupplementalSubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);

    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);

    SupplementalSubmissionDataResource data = submission.getData();
    assertNotNull(data);

    assertEquals("JONNY APPLESEED", data.getParticipantName());
    assertNull(data.getEmail());
    assertEquals("individual", data.getBusinessStructure());
    assertEquals(Integer.valueOf(3778842), data.getAgriStabilityAgriInvestPin());
    assertNull(data.getTelephone());
    assertEquals("999999999", data.getSinNumber());
    assertEquals("", data.getBusinessTaxNumber());
    assertEquals("yes", data.getOnBehalfOfParticipant());
    assertEquals("Hong-Yi", data.getSignatureFirstName());
    assertEquals("Wang", data.getSignatureLastName());
    assertEquals("2025/01/21", data.getSignatureDate());
    assertEquals("N/A", data.getHowDoYouKnowTheParticipant());

    String fileName = "2023_Supplemental_3778842_Form.pdf";
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(supplementalTemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }

 

  @Test
  public void generatePdfFromStatementATemplateGuidTest() {
  
    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.STA, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));
  
    String statementATemplateGuid = cdogsConfig.getStatementATemplateGuid();
    String submissionGuid = "fde01732-945c-4d2a-b4eb-8b61250b3f1f";
    String participantPin = "98765703";
  
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    assertNotNull(submissionUrl);
  
    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper = null;
    try {
      submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl,
          StatementASubmissionDataResource.class);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(submissionWrapper);
  
    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    assertNotNull(submissionMetaData);
  
    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    assertNotNull(submission);
  
    StatementASubmissionDataResource data = submission.getData();
    assertNotNull(data);
  
    assertEquals("DIANE KEATON", data.getCorporationName());
    assertEquals("ADMIN@APPLES.CA", data.getEmail());
    assertEquals("Status Indian farming on a reserve", data.getFarmType().getLabel());
    assertEquals("statusIndianFarmingOnAReserve", data.getFarmType().getValue());
    assertEquals(Integer.valueOf(participantPin), data.getAgriStabilityAgriInvestPin());
    assertEquals("(640) 555-5555", data.getTelephone());
    assertNull(data.getTrustNumber());
    assertNull(data.getBusinessTaxNumber());
    assertNull(data.getTrustBusinessNumber());
    assertEquals("987654321", data.getSinNumber());
    assertEquals("cash", data.getAccountingMethod());
  
    String fileName = "2024_Statement_A_" + participantPin + "_Form.pdf";
    CdogsTemplateDataResource cdogsTemplateDataResource = new CdogsTemplateDataResource();
    CdogsOptionsResource cdogsOptionsResource = new CdogsOptionsResource(fileName);
    cdogsTemplateDataResource.setData(data);
    cdogsTemplateDataResource.setOptions(cdogsOptionsResource);
  
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      assertNotNull(cdogsDataJson);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  
    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);
  
    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }
  
    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(statementATemplateGuid, saveFilePath, cdogsDataJson);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
//        file.delete();
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
  }

  protected HttpURLConnection getHttpURLConnection(String endpointUrl, String method) throws ServiceException {

    HttpURLConnection conn;
    try {
      URL url = new URL(endpointUrl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.addRequestProperty("Accept", "application/json; charset=utf-8");
      conn.setRequestMethod(method);

    } catch (IOException e) {
      throw new ServiceException("Error getting CHEFS resource", e);
    }
    return conn;
  }

}
