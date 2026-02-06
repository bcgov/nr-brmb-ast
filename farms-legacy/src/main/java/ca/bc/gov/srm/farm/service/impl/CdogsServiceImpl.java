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
package ca.bc.gov.srm.farm.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.cdogs.CdogsConfigurationUtil;
import ca.bc.gov.srm.farm.cdogs.CdogsRestApiDao;
import ca.bc.gov.srm.farm.cdogs.resource.CdogsTemplateDataResource;
import ca.bc.gov.srm.farm.chefs.ChefsAuthenticationHandler;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.ChefsRestApiDao;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.forms.AdjustmentFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.CashMarginsFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.CoverageFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.InterimFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.NolFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.NppFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.StatementAFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.SupplementalFormConstants;
import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.cashMargin.CashMarginsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageRefScenarioDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageReportDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.nol.NolSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalSubmissionDataResource;
import ca.bc.gov.srm.farm.dao.BlobReaderWriter;
import ca.bc.gov.srm.farm.dao.CobDAO;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class CdogsServiceImpl extends BaseService implements CdogsService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static ChefsRestApiDao chefsApiDao;
  private static final ChefsConfigurationUtil chefsConfig = ChefsConfigurationUtil.getInstance();
  private static final CdogsConfigurationUtil cdogsConfig = CdogsConfigurationUtil.getInstance();

  private static final String PDF_FILENAME_FORMAT = "%s_%s_%s_Form.pdf";
  private static final String PDF_REPORT_FILENAME_FORMAT = "%s_%s_%s_Report.pdf";

  @Override
  public Map<Integer,File> createCdogsAdjustmentFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.ADJ, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String templateGuid = cdogsConfig.getAdjustmentTemplateGuid();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<AdjustmentSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, AdjustmentSubmissionDataResource.class);

    SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();

    AdjustmentSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);
    
    Integer particpantPin = data.getAgriStabilityPin();

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, data.getProgramYear(), AdjustmentFormConstants.FORM_SHORT_NAME,
        data.getAgriStabilityPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(particpantPin, generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }

  @Override
  public Map<Integer, File> createCdogsCashMarginsFormDocument(String submissionGuid, String formUserType) throws ServiceException {
    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.CM, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String templateGuid = cdogsConfig.getCashMarginsTemplateGuid();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<CashMarginsSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CashMarginsSubmissionDataResource.class);

    SubmissionParentResource<CashMarginsSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    SubmissionResource<CashMarginsSubmissionDataResource> submission = submissionMetaData.getSubmission();

    CashMarginsSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT,
        ProgramYearUtils.getCurrentCalendarYear(), CashMarginsFormConstants.FORM_SHORT_NAME, data.getAgriStabilityPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }

  @Override
  public Map<Integer, File> createCdogsCoverageFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.CN, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String templateGuid = cdogsConfig.getCoverageTemplateGuid();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<CoverageSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, CoverageSubmissionDataResource.class);

    SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();

    CoverageSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, data.getProgramYear().getValue(), CoverageFormConstants.FORM_LONG_NAME,
        data.getAgriStabilityAgriInvestPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityAgriInvestPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }
  
  @SuppressWarnings("resource")
  @Override
  public void createCdogsCoverageNoticeReport(Scenario scenario, String userId)
      throws ServiceException {
    
    Integer scenarioId = scenario.getScenarioId();
    Integer programYear = scenario.getYear();
    Integer participantPin = scenario.getClient().getParticipantPin();

    CoverageReportDataResource coverageReportDataResource = createCoverageReportDataResource(scenario);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_REPORT_FILENAME_FORMAT, programYear, CoverageFormConstants.FORM_LONG_NAME,
        participantPin);

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(coverageReportDataResource, fileName);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson = null;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      logger.error("Error processing JSON when generating PDF from template: ", e);
    }
    
    String templateGuid = cdogsConfig.getCoverageNoticeReportTemplateGuid();
    InputStream inputStream = generatedInputStreamFromTemplate(templateGuid, cdogsTemplateDataResource);
    
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();
      
      transaction.begin();
      
      CobDAO dao = new CobDAO();
      Connection dbConnection = (Connection) transaction.getDatastore();
      
      Blob blob = dao.getBlob(dbConnection, scenarioId, false);
      
      if (blob == null) {
        dao.insertCob(transaction, scenarioId, userId);
      } else {
        dao.updateCob(transaction, scenarioId, userId);
      }
      
      BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
      blob = dao.getBlob(dbConnection, scenarioId, true);
      blobReaderWriter.writeBlob(blob, inputStream);
        
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  @Override
  public Map<Integer, File> createCdogsInterimFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.INTERIM, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<InterimSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, InterimSubmissionDataResource.class);

    SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();

    InterimSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);
    
    String templateGuid = cdogsConfig.getInterimTemplateGuid(data.getVersion());

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT,
        DateUtils.getYearFromDate(data.getFiscalYearEndDate()), InterimFormConstants.FORM_SHORT_NAME, data.getAgriStabilityAgriInvestPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityAgriInvestPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }

  @Override
  public Map<Integer, File> createCdogsNolFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.NOL, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String templateGuid = cdogsConfig.getNolTemplateGuid();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<NolSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NolSubmissionDataResource.class);

    SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();

    NolSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT, data.getProgramYear(), NolFormConstants.FORM_SHORT_NAME,
        data.getAgriStabilityPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }

  @Override
  public Map<Integer, File> createCdogsNppFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.NPP, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<NppSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, NppSubmissionDataResource.class);

    SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();

    NppSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);

    String templateGuid = cdogsConfig.getNppTemplateGuid(data.getNoPin() == null ? null : 2);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT,
        DateUtils.getYearFromDate(data.getFiscalYearEnd()), NppFormConstants.FORM_SHORT_NAME, data.getAgriStabilityAgriInvestPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityAgriInvestPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }
  
  @Override
  public Map<Integer, File> createCdogsStatementAFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.STA, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String templateGuid = cdogsConfig.getStatementATemplateGuid();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<StatementASubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);

    SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();

    StatementASubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT,
        DateUtils.getYearFromDate(data.getFiscalYearEndDate()), StatementAFormConstants.FORM_SHORT_NAME, data.getAgriStabilityAgriInvestPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityAgriInvestPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }
  
  @Override
  public Map<Integer, File> createCdogsSupplementalFormDocument(String submissionGuid, String formUserType) throws ServiceException {

    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.SUPP, formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));

    String templateGuid = cdogsConfig.getSupplementalTemplateGuid();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    SubmissionWrapperResource<SupplementalSubmissionDataResource> submissionWrapper;
    submissionWrapper = chefsApiDao.getSubmissionWrapperResource(submissionUrl, SupplementalSubmissionDataResource.class);

    SubmissionParentResource<SupplementalSubmissionDataResource> submissionMetaData = submissionWrapper
        .getSubmissionMetaData();
    SubmissionResource<SupplementalSubmissionDataResource> submission = submissionMetaData.getSubmission();

    SupplementalSubmissionDataResource data = submission.getData();
    populateSubmissionMetaData(submissionMetaData, data);

    String fileName = StringUtils.formatWithNullAsEmptyString(PDF_FILENAME_FORMAT,
        data.getProgramYear().getValue(), SupplementalFormConstants.FORM_SHORT_NAME, data.getAgriStabilityAgriInvestPin());

    CdogsTemplateDataResource cdogsTemplateDataResource = createCdogsTemplateDataResource(data, fileName);

    Map<Integer, File> map = new HashMap<>();
    map.put(data.getAgriStabilityAgriInvestPin(), generatedPdfFromTemplate(templateGuid, fileName, cdogsTemplateDataResource));
    return map;
  }
  
  @Override
  public Map<Integer, File> createCdogsDocumentByFormType(String submissionGuid, String formUserType, String formType) throws ServiceException {
    Map<Integer,File> participantPinFileMap = null;
    switch(formType) {
    case ChefsFormTypeCodes.ADJ:
      participantPinFileMap = createCdogsAdjustmentFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.CM:
      participantPinFileMap = createCdogsCashMarginsFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.CN:
      participantPinFileMap = createCdogsCoverageFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.INTERIM:
      participantPinFileMap = createCdogsInterimFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.NOL:
      participantPinFileMap = createCdogsNolFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.NPP:
      participantPinFileMap = createCdogsNppFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.STA:
      participantPinFileMap = createCdogsStatementAFormDocument(submissionGuid, formUserType);
      break;
    case ChefsFormTypeCodes.SUPP:
      participantPinFileMap = createCdogsSupplementalFormDocument(submissionGuid, formUserType);
      break;
    default:
      logger.error("ChefsFormTypeCodes not found.");
      break;
    }
    return participantPinFileMap;
  }

  private File generatedPdfFromTemplate(String templateGuid, String fileName,
      CdogsTemplateDataResource cdogsTemplateDataResource) throws ServiceException {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      logger.error("Error processing JSON when generating PDF from template: ", e);
      throw new ServiceException("Error writing json data: " + e);
    }

    String saveFilePath = IOUtils.getTempDirPath() + "/" + fileName;
    File file = new File(saveFilePath);

    try {
      CdogsRestApiDao cdogsDao = new CdogsRestApiDao();
      String response = cdogsDao.generatePdfFromTemplateGuid(templateGuid, saveFilePath, cdogsDataJson);
      logger.debug(response);
    } catch (IOException | ServiceException e) {
      logger.error("Error generating CDOGS document: ", e);
      throw new ServiceException("Error generating CDOGS document: " + e);
    }
    return file;
  }
  
  private InputStream generatedInputStreamFromTemplate(String templateGuid, CdogsTemplateDataResource cdogsTemplateDataResource)
      throws ServiceException {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String cdogsDataJson;
    try {
      cdogsDataJson = ow.writeValueAsString(cdogsTemplateDataResource);
      logger.debug(cdogsDataJson);
    } catch (JsonProcessingException e) {
      logger.error("Error processing JSON when generating PDF from template: ", e);
      throw new ServiceException("Error writing json data: " + e);
    }

    InputStream inputStream;
    try {
      CdogsRestApiDao cdogsDao = new CdogsRestApiDao();
      inputStream = cdogsDao.generateInputStreamFromTemplateGuid(templateGuid, cdogsDataJson);
    } catch (ServiceException e) {
      logger.error("Error generating CDOGS document: ", e);
      throw new ServiceException("Error generating CDOGS document: " + e);
    }
    return inputStream;
  }
  
  private CoverageReportDataResource createCoverageReportDataResource(Scenario scenario) {
    Person owner = scenario.getClient().getOwner();
    Integer programYear = scenario.getYear();
    Integer participantPin = scenario.getClient().getParticipantPin();

    CoverageReportDataResource coverageReportDataResource = new CoverageReportDataResource();
    coverageReportDataResource.setReportDate(new Date());
    coverageReportDataResource.setParticipantName(owner.getFullName());
    coverageReportDataResource.setAgriStabilityAgriInvestPin(participantPin
        );
    coverageReportDataResource.setProgramYear(programYear);
    coverageReportDataResource.setAddress(owner.getAddressLine1());
    coverageReportDataResource.setCity(owner.getCity());
    coverageReportDataResource.setProvince(owner.getProvinceState());
    coverageReportDataResource.setPostalCode(owner.getPostalCode());
    coverageReportDataResource.setPaymentCapPercentageOfTotalMarginDecline(
        CalculatorConfig.getPaymentCapPercentageOfTotalMarginDecline(programYear));
    coverageReportDataResource.setStandardPositiveMarginCompensationRate(
        CalculatorConfig.getStandardPositiveMarginCompensationRate(programYear));

    Benefit benefit = scenario.getFarmingYear().getBenefit();
    if (benefit != null ) {
      coverageReportDataResource.setReferenceMargin(benefit.getAdjustedReferenceMargin());
    }
    
    if (coverageReportDataResource.getReferenceMargin() != null &&
        coverageReportDataResource.getPaymentCapPercentageOfTotalMarginDecline() != null) {
      
      coverageReportDataResource.setTotalMarginDecline(
          coverageReportDataResource.getReferenceMargin() * coverageReportDataResource.getPaymentCapPercentageOfTotalMarginDecline());
    }
    
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
    return coverageReportDataResource;
  }

  private CdogsTemplateDataResource createCdogsTemplateDataResource(ChefsSubmissionDataResource data, String fileName) {

    return new CdogsTemplateDataResource(data, fileName);
  }

  private <T> void populateSubmissionMetaData(SubmissionParentResource<T> submissionMetaData,
      ChefsSubmissionDataResource data) {
    data.setCreatedAt(submissionMetaData.getCreatedAt());
    data.setCreatedBy(submissionMetaData.getCreatedBy());
    data.setUpdatedAt(submissionMetaData.getUpdatedAt());
    data.setUpdatedBy(submissionMetaData.getUpdatedBy());
    data.setConfirmationId(submissionMetaData.getConfirmationId());
    data.setFormVersionId(submissionMetaData.getFormVersionId());
  }

}
