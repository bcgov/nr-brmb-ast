package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cdogs.CdogsConfigurationUtil;
import ca.bc.gov.srm.farm.cdogs.CdogsRestApiDao;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountAnnotationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitAnnotationResource;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.StringUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmUploadFileToNoteTest {

  private static Logger logger = LoggerFactory.getLogger(CrmUploadFileToNoteTest.class);

  private static CrmRestApiDao crmDao;
  private static CdogsRestApiDao cdogsDao;
  private static CdogsService cdogsService;
  private CdogsConfigurationUtil cdogsConfig = CdogsConfigurationUtil.getInstance();

  private static final String PDF_FILENAME_FORMAT = "%s_%s_%s_Form.pdf";

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();

    crmDao = new CrmRestApiDao();
    cdogsDao = new CdogsRestApiDao();
    cdogsService = ServiceFactory.getCdogsService();
  }

  @Test
  public void uploadAdjustmentFileToUpdateBenefitNote() {

    Integer programYear = 2023;
    Integer participantPin = 3778842;
    String submissionGuid = "1bfa7b2b-342c-4e79-9d62-71bfffdaf83f";
    String updateBenefitId = "77078b24-25b7-ee11-a568-002248b3411a";
    String formUserType = ChefsConstants.USER_TYPE_IDIR;    

    Map<Integer,File> participantPinFileMap = null;
    try {
      participantPinFileMap = cdogsService.createCdogsAdjustmentFormDocument(submissionGuid, formUserType);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(participantPinFileMap);
    
    File file = participantPinFileMap.get(participantPin);

    String expectedFileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, programYear, "Adjustment",
        participantPin);
    String expectedFilePath = IOUtils.getTempDirPath() + "/" + expectedFileName;
    assertNotNull(file);
    assertTrue(file.exists());
    assertEquals(expectedFileName, file.getName());
    assertEquals(expectedFilePath, file.getAbsolutePath());

    CrmBenefitAnnotationResource benefitEntity = new CrmBenefitAnnotationResource();
    benefitEntity.setSubject("Adjustment Form");
    benefitEntity.setNotetext("testing upload for Adjustment to Benefit");
    benefitEntity.setFilename(file.getName());
    benefitEntity.setIsdocument(true);
    benefitEntity.setEntityId(updateBenefitId);
    try {
      benefitEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      CrmBenefitAnnotationResource result = crmDao.uploadFileToNote(benefitEntity);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

  @Test
  public void uploadInterimFileToUpdateBenefitNote() {

    Integer programYear = 2023;
    Integer participantPin = 3778842;
    String submissionGuid = "8f58a615-26c2-4ca5-a718-6411fdef261e";
    String updateBenefitId = "d697bece-28b7-ee11-a568-6045bd5d6395";
    String formUserType = ChefsConstants.USER_TYPE_IDIR;

    Map<Integer,File> participantPinFileMap = null;
    try {
      participantPinFileMap = cdogsService.createCdogsInterimFormDocument(submissionGuid, formUserType);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(participantPinFileMap);
    
    File file = participantPinFileMap.get(participantPin);

    String expectedFileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, programYear, "Interim",
        participantPin);
    String expectedFilePath = IOUtils.getTempDirPath() + "/" + expectedFileName;
    assertNotNull(file);
    assertTrue(file.exists());
    assertEquals(expectedFileName, file.getName());
    assertEquals(expectedFilePath, file.getAbsolutePath());

    CrmBenefitAnnotationResource benefitEntity = new CrmBenefitAnnotationResource();
    benefitEntity.setSubject("Interim Form");
    benefitEntity.setNotetext("testing upload for Interim to Benefit");
    benefitEntity.setFilename(file.getName());
    benefitEntity.setIsdocument(true);
    benefitEntity.setEntityId(updateBenefitId);
    try {
      benefitEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      CrmBenefitAnnotationResource result = crmDao.uploadFileToNote(benefitEntity);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

  @Test
  public void uploadNOLFileToAccountNote() {

    String accountId = null;
    Integer programYear = 2024;
    Integer participantPin = 230000004;
    String submissionGuid = "1504b824-aadd-476f-8e7f-a748d0182641";
    String formUserType = ChefsConstants.USER_TYPE_IDIR;

    Map<Integer,File> participantPinFileMap = null;
    try {
      participantPinFileMap = cdogsService.createCdogsNolFormDocument(submissionGuid, formUserType);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    assertNotNull(participantPinFileMap);
    
    File file = participantPinFileMap.get(participantPin);

    String expectedFileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, programYear, "NOL",
        participantPin);
    String expectedFilePath = IOUtils.getTempDirPath() + "/" + expectedFileName;
    assertNotNull(file);
    assertTrue(file.exists());
    assertEquals(expectedFileName, file.getName());
    assertEquals(expectedFilePath, file.getAbsolutePath());

    try {
      CrmAccountResource account = crmDao.getAccountByPin(participantPin);
      assertEquals(participantPin.toString(), account.getVsi_pin());
      accountId = account.getAccountid();
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    CrmAccountAnnotationResource accountEntity = new CrmAccountAnnotationResource();
    accountEntity.setSubject("NOL Form");
    accountEntity.setNotetext("testing upload for NOL form to account");
    accountEntity.setFilename(file.getName());
    accountEntity.setIsdocument(true);
    accountEntity.setEntityId(accountId);

    try {
      accountEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      CrmAccountAnnotationResource result = crmDao.uploadFileToNote(accountEntity);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

  @Test
  public void uploadNppFormToAccountNote() {

    String nppTemplateUid = cdogsConfig.getNppTemplateGuid(null);

    String jsonInputString = TestUtils.loadFileAsString("data/chefs/npp_form_data.json");

    String accountId = null;
    Integer programYear = 2023;
    Integer participantPin = 3778842;
    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, programYear, "Enrolment",
        participantPin);

    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    // delete file if if exist
    if (file.delete()) {
      logger.debug("File deleted successfully: " + saveFilePath);
    } else {
      logger.debug("Failed to delete the file: " + saveFilePath);
    }

    try {
      String response = cdogsDao.generatePdfFromTemplateGuid(nppTemplateUid, saveFilePath, jsonInputString);
      assertNotNull(response);
      assertTrue(file.exists());
      if (file.exists() && !file.isDirectory()) {
        logger.debug("file exist: " + saveFilePath);
      }
    } catch (IOException | ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      CrmAccountResource account = crmDao.getAccountByPin(participantPin);
      assertEquals(participantPin.toString(), account.getVsi_pin());
      accountId = account.getAccountid();
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    CrmAccountAnnotationResource accountEntity = new CrmAccountAnnotationResource();
    accountEntity.setSubject("NPP Form");
    accountEntity.setNotetext("testing upload for npp form to account");
    accountEntity.setFilename(fileName);
    accountEntity.setIsdocument(true);
    accountEntity.setEntityId(accountId);
    try {
      accountEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

    try {
      CrmAccountAnnotationResource result = crmDao.uploadFileToNote(accountEntity);
      assertNotNull(result);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

  @Test
  public void createNoteInBenefitUpdateNoFileAttached() {

    String updateBenefitId = "111e8866-f37f-ee11-8179-0022483cb0d4";

    CrmBenefitAnnotationResource benefitEntity = new CrmBenefitAnnotationResource();
    benefitEntity.setSubject("Test Benefit Update Note");
    benefitEntity.setNotetext("testing note, no file attached");
    benefitEntity.setIsdocument(true);
    benefitEntity.setEntityId(updateBenefitId);

    try {
      CrmBenefitAnnotationResource result = crmDao.uploadFileToNote(benefitEntity);
      assertNotNull(result);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }

  }

}
