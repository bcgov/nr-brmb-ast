/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.dao.BlobReaderWriter;
import ca.bc.gov.srm.farm.dao.CobDAO;
import ca.bc.gov.srm.farm.dao.ReportDAO;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.export.ExportForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.ui.struts.report.BackgroundReportForm;
import ca.bc.gov.srm.farm.ui.struts.report.ReportForm;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.IOUtils;

final class ReportServiceImpl extends BaseService implements ReportService {
  
  private Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

  //
  // These keys are found in the Oracle reports config file
  //
  private static final String KEY_BENEFITS = "farm_benefits";
  
  // The CoB for 2023 forward
  private static final String KEY_BENEFIT_NOTICE_2023 = "farm_benefit_notice_2023";
  
  // The CoB for 2021 forward, if BC Enhanced Benefit is enabled
  private static final String KEY_BENEFIT_NOTICE_2021_BC_ENHANCED = "farm_benefit_notice_2021bc";
  
  // The CoB for 2021 forward, if BC Enhanced Benefit is disabled
  private static final String KEY_BENEFIT_NOTICE_2021 = "farm_benefit_notice_2021";
  
  // The CoB for 2020
  private static final String KEY_BENEFIT_NOTICE_2020 = "farm_benefit_notice_2020";
  
  // The CoB for 2019
  private static final String KEY_BENEFIT_NOTICE_2019 = "farm_benefit_notice_2019";
  
  // The CoB for 2018
  private static final String KEY_BENEFIT_NOTICE_2018 = "farm_benefit_notice_2018";
  
  // The CoB / Benefit Notice for 2017
  private static final String KEY_BENEFIT_NOTICE_2017 = "farm_benefit_notice_2017";
  
  // The CoB / Benefit Notice for 2013 to 2016
  private static final String KEY_BENEFIT_NOTICE_2013_TO_2016 = "farm_benefit_notice_2013-2016";
  
  // The CoB / Benefit Notice for 2012 and previous years
  private static final String KEY_BENEFIT_NOTICE_2012 = "farm_benefit_notice_2012";

  private static final String KEY_SUBMISSIONS = "farm_submissions";
  
  public static final String CSV_FILE_SUFFIX = ".csv";
  
  public static final String NATIONAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_BASE_NAME = "national_surveillance_strategy";
  public static final String NATIONAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_NAME = NATIONAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_BASE_NAME + CSV_FILE_SUFFIX;
  
  public static final String ANALYTICAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_BASE_NAME = "analytical_surveillance_strategy";
  public static final String ANALYTICAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_NAME = ANALYTICAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_BASE_NAME + CSV_FILE_SUFFIX;

  
  /**
   * Perform additional validation on top of the struts validator.
   *
   * @param   mapping  WebADEActionMapping
   * @param   form     form to validate
   * @param   request  HttpServletRequest
   *
   * @return  ActionMessages messages
   */
  @Override
  public ActionMessages validateForm(
    final ActionMapping mapping,
    final ReportForm form, 
    final HttpServletRequest request) {
    ActionMessages errors = form.validate(mapping, request);
    String reportType = form.getReportType();

    if (REPORT_620.equals(reportType)) {
      if (StringUtils.isEmpty(form.getName())
          && StringUtils.isEmpty(form.getPin())
          && StringUtils.isEmpty(form.getSin())) {
        errors.add("",
          new ActionMessage(MessageConstants.ERRORS_620_NO_CRITERIA));
      }
    } else if(REPORT_NATIONAL_SURVEILLANCE_STRATEGY.equals(reportType)
        || REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY.equals(reportType)) {
      
      int year = Integer.valueOf(form.getYear()).intValue();
      if (year < CalculatorConfig.GROWING_FORWARD_2013) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_SURVEILLANCE_STRATEGY_2013_FORWARD));
      }
    }

    return errors;
  }
  
  /**
   * Perform additional validation on top of the struts validator.
   *
   * @param   mapping  WebADEActionMapping
   * @param   form     form to validate
   * @param   request  HttpServletRequest
   *
   * @return  ActionMessages messages
   */
  @Override
  public ActionMessages validateForm(
    final ActionMapping mapping,
    final ExportForm form, 
    final HttpServletRequest request) {
    ActionMessages errors = form.validate(mapping, request);

    final char separator = ',';
    String codes = form.getInventoryCodes();
    if(StringUtils.isNotEmpty(codes)) {
      String[] values = StringUtils.split(codes, separator);
      
      for(int ii = 0; ii < values.length; ii++) {
        if(!StringUtils.isNumeric(values[ii].trim())) {
          errors.add("",
            new ActionMessage(MessageConstants.ERRORS_700_INVALID_CODE));
          break;
        }
      }
    }
    
    return errors;
  }
  
  
  
  /**
   * @param   form  form
   *
   * @return  the URL to call the report
   */
  @Override
  public String getReportUrl(final ReportForm form) {
    String url = null;

    String reportType = form.getReportType();
    if(REPORT_NATIONAL_SURVEILLANCE_STRATEGY.equals(reportType)
        || REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY.equals(reportType)) {
        // it's a struts action
        url = "backgroundReportGenerate.do?reportType="+reportType+"&year="+form.getYear();
    } else {
      // it's an Oracle report
      String reportKey = getReportKey(reportType);
      Map<String, String> params = getReportParameters(form);
      url = createUrl(reportKey, params);
    }
    
    logger.debug(url);
    return url;
  }
  
 


  /**
   * @param   form  form
   *
   * @return  map of parameters
   */
  private Map<String, String> getReportParameters(final ReportForm form) {
    Map<String, String> parameters = new HashMap<>();

    // all reports use these parameters
    parameters.put("IN_YEAR", form.getYear());
    parameters.put("IN_USER", CurrentUser.getUser().getUserId());

    // the 620 report has more parameters
    if (REPORT_620.equals(form.getReportType())) {
      parameters.put("IN_NAME", form.getName());
      parameters.put("IN_PARTNER_PIN", form.getPin());
      parameters.put("IN_BUSINESS_NUM", form.getSin());
    }

    return parameters;
  }
  

  /**
   * @param   reportType  from screen menu
   *
   * @return  the report's key in the report server config file
   */
  private String getReportKey(final String reportType) {
    String key = null;

    if (REPORT_610.equals(reportType)) {
      key = KEY_BENEFITS;
    } else if (REPORT_620.equals(reportType)) {
      key = KEY_SUBMISSIONS;
    }

    return key;
  }


  /**
   * @param   reportKey  key in report config file
   * @param   params     report parameters
   *
   * @return  a URL
   */
  private String createUrl(String reportKey, Map<String, String> params) {
    ConfigurationUtility cu = ConfigurationUtility.getInstance();
    String server = cu.getValue(ConfigurationKeys.REPORTS_SERVER);
    String href = server + "?" + reportKey;

    for (String name : params.keySet()) {
      String value = params.get(name);
      
      href += ("+" + name + "=" + "'" + value + "'");
    }
    
    logger.info(href); 
    
    href = StringEscapeUtils.escapeJavaScript(href);
    logger.info(href);
    
    return href;
  }
  
  
  /**
   * Save the COB report in a Blob in a table.
   *
   * @param   scenarioId  scenarioId
   * @param   programYear programYear
   *
   * @throws Exception if it could not be saved
   */
  @Override
  public void saveCob(final Integer scenarioId, Integer programYear, String userId) throws Exception {
    generateCob(scenarioId, programYear, true, userId);
  }
  
  
  /**
   * Save the COB report in a Blob in a table.
   *
   * @param   scenarioId  scenarioId
   * @param   programYear programYear
   *
   * @throws Exception if it could not be saved
   */
  @Override
  public void updateCob(final Integer scenarioId, Integer programYear, String userId) throws Exception {
    generateCob(scenarioId, programYear, false, userId);
  }
  
  
  /**
   * Save the COB report in a Blob in a table.
   * 
   * @throws Exception if it could not be saved
   */
  private void generateCob(final Integer scenarioId, Integer programYear, boolean isInsert, String userId) throws Exception {
    Transaction transaction = null;

    String reportKey;
    
    if(programYear.intValue() >= CalculatorConfig.GROWING_FORWARD_2023) {
      reportKey = KEY_BENEFIT_NOTICE_2023;
    } else if(programYear.intValue() >= CalculatorConfig.GROWING_FORWARD_2021) {
      if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
        reportKey = KEY_BENEFIT_NOTICE_2021_BC_ENHANCED;
      } else {
        reportKey = KEY_BENEFIT_NOTICE_2021;
      }
    } else if(programYear.intValue() == CalculatorConfig.GROWING_FORWARD_2020) {
      reportKey = KEY_BENEFIT_NOTICE_2020;
    } else if(programYear.intValue() == CalculatorConfig.GROWING_FORWARD_2019) {
      reportKey = KEY_BENEFIT_NOTICE_2019;
    } else if(programYear.intValue() == CalculatorConfig.GROWING_FORWARD_2018) {
      reportKey = KEY_BENEFIT_NOTICE_2018;
    } else if(programYear.intValue() == CalculatorConfig.GROWING_FORWARD_2017) {
      reportKey = KEY_BENEFIT_NOTICE_2017;
    } else if(programYear.intValue() >= CalculatorConfig.GROWING_FORWARD_2013) {
      reportKey = KEY_BENEFIT_NOTICE_2013_TO_2016;
    } else {
      reportKey = KEY_BENEFIT_NOTICE_2012;
    }

    try {
      transaction = openTransaction();
      
      transaction.begin();
      
      //
      // Insert an entry into the Blob table.
      //
      CobDAO dao = new CobDAO();
      
      if(isInsert) {
        dao.insertCob(transaction, scenarioId, userId);
      } else {
        dao.updateCob(transaction, scenarioId, userId);
      }
      
      //
      // Create the URL. note that this is a special direct URL to the report 
      // server so we don't need to worry about proxy server redirects and whatnot.
      //
      ConfigurationUtility cu = ConfigurationUtility.getInstance();
      String server = cu.getValue(ConfigurationKeys.DIRECT_REPORTS_SERVER);
      String href = server + "?" + reportKey;
      href += ("+IN_SCENARIO_ID" + "=" + "\"" + scenarioId.toString() + "\"");
      logger.debug("generateCob URL: " + href);
      
      //
      // call the URL directly
      //
      URL url = new URL(href);
      HttpURLConnection reportConnection = (HttpURLConnection) url.openConnection();
      reportConnection.setRequestMethod("GET");
      reportConnection.setDoOutput(true);
      reportConnection.connect();
      
      int responseCode = reportConnection.getResponseCode();
      logger.debug("generateCob response code: " + responseCode);
      
      if(responseCode == HttpURLConnection.HTTP_MOVED_PERM
          || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
          || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
        
        // Redirect
        String locationHeader = reportConnection.getHeaderField("Location");
        logger.warn("Received response code " + responseCode + " with redirect URL: " + locationHeader);

        URL redirectUrl = new URL(locationHeader);
        reportConnection = (HttpURLConnection) redirectUrl.openConnection();
        reportConnection.setRequestMethod("GET");
        reportConnection.setDoOutput(true);
        reportConnection.connect();
        responseCode = reportConnection.getResponseCode();
        logger.warn("generateCob REDIRECT response code: " + responseCode);
      }
      
      
      if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
        
        try(InputStream inputStream = reportConnection.getInputStream();) {
          String errorOutput = org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
          if(StringUtils.isEmpty(errorOutput)) {
            try(InputStream errorStream = reportConnection.getErrorStream()) {
              errorOutput = org.apache.commons.io.IOUtils.toString(errorStream, StandardCharsets.UTF_8.name());
            }
          }
          throw new Exception(errorOutput);
        }
        
      } else {
      
        //
        // put the response into a Blob
        //
        BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
        @SuppressWarnings("resource")
        Connection dbConnection = (Connection) transaction.getDatastore();
        Blob blob = dao.getBlob(dbConnection, scenarioId, true);
        try(InputStream inStream = reportConnection.getInputStream();) {
          blobReaderWriter.writeBlob(blob, inStream);
        }
        
      }
      
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
  
  
  /**
   * get the cob blob
   *
   * @param  scenarioId  scenarioId
   * @param  response HttpServletResponse
   * @throws Exception on error
   */
  @Override
  public void writeCobToResponse(final Integer scenarioId, final HttpServletResponse response, String fileName) 
  throws Exception {
    Transaction transaction = null;
    Blob blob = null;
    
    try {
      transaction = openTransaction();

      CobDAO dao = new CobDAO();
      @SuppressWarnings("resource")
      Connection dbConnection = (Connection) transaction.getDatastore();
      blob = dao.getBlob(dbConnection, scenarioId, false);

      response.reset();
      response.addHeader("content-disposition", "inline;filename=" + fileName);
      response.setContentType(IOUtils.CONTENT_TYPE_PDF);
      response.setContentLength((int) blob.length());
      
      try(InputStream inputStream = blob.getBinaryStream();) {
        @SuppressWarnings("resource")
        OutputStream outputStream = response.getOutputStream();
        
        BlobReaderWriter blobReaderWriter = new BlobReaderWriter();
        blobReaderWriter.readBlob(blob, outputStream);
      }
    } finally {
      closeTransaction(transaction);
    }
    
  }
  
  
  /**
   * Delete the COB report.
   *
   * @param   scenarioId  scenarioId
   * @throws Exception if it could not be saved
   */
  @Override
  public void deleteBenefitDocument(final Integer scenarioId) throws Exception {
    logger.debug("<deleteBenefitDocument");
    Transaction transaction = null;

    try {
      transaction = openTransaction();
      
      transaction.begin();
      
      CobDAO dao = new CobDAO();
      
      dao.deleteCob(transaction, scenarioId);
      
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }

      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
    logger.debug(">deleteBenefitDocument");
  }
  
  
  @Override
  public void generateNationalSurveillanceStrategy(Integer year) throws IOException, ServiceException {
    LoggingUtils.logMethodStart(logger);
    
    String reportType = REPORT_NATIONAL_SURVEILLANCE_STRATEGY;
    String[] headerLines = getNationalSurveillanceStrategyHeaderLines(year);
    String[] columnHeadings = getNationalSurveillanceStrategyColumnHeadings();
    Format[] columnFormats = getNationalSurveillanceStrategyColumnFormats();
    
    String reportTempFilePrefix = NATIONAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_BASE_NAME + "_";
    String outputFile = NATIONAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_NAME;
    
    generateSurveillanceStrategy(year, reportType, headerLines, columnHeadings, columnFormats, reportTempFilePrefix, outputFile);
    
    LoggingUtils.logMethodEnd(logger);
  }

  @Override
  public void generateAnalyticalSurveillanceStrategy(Integer year) throws IOException, ServiceException {
    LoggingUtils.logMethodStart(logger);
    
    String reportType = REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY;
    String[] headerLines = getAnalyticalSurveillanceStrategyHeaderLines(year);
    String[] columnHeadings = getAnalyticalSurveillanceStrategyColumnHeadings();
    Format[] columnFormats = getAnalyticalSurveillanceStrategyColumnFormats();
    
    String reportTempFilePrefix = ANALYTICAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_BASE_NAME + "_";
    String outputFile = ANALYTICAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_NAME;
    
    generateSurveillanceStrategy(year, reportType, headerLines, columnHeadings, columnFormats, reportTempFilePrefix, outputFile);
    
    LoggingUtils.logMethodEnd(logger);
  }
  
  private void generateSurveillanceStrategy(
      Integer year,
      String reportType,
      String[] headerLines,
      String[] columnHeadings,
      Format[] columnFormats,
      String reportTempFilePrefix,
      String outputFile) throws ProviderException, IOException, DataAccessException {
    LoggingUtils.logMethodStart(logger);
    
    Transaction transaction = null;
    transaction = openTransaction();
    ReportDAO dao = new ReportDAO();
    
    File tempDir = IOUtils.getTempDir();
    File csvFile = File.createTempFile(reportTempFilePrefix, CSV_FILE_SUFFIX, tempDir);
    
    dao.surveillanceStrategy(transaction, reportType, year, headerLines, columnHeadings, columnFormats, csvFile);    
    
    File finalCsv = new File(tempDir, outputFile);
    if(finalCsv.exists()) {
      finalCsv.delete();
    }
    csvFile.renameTo(finalCsv);
    
    LoggingUtils.logMethodEnd(logger);
  }

  /**
   * @return header lines
   */
  private String[] getNationalSurveillanceStrategyHeaderLines(Integer year) {
    DateFormat df = new SimpleDateFormat("MMMM dd YYYY");
    // Need quotes around lines that have commas in them.
    // Quoted them all just in case they are modified later.
    return new String[]{
        "\"British Columbia Ministry of Agriculture\"",
        "\"National Surveillance Strategy\"",
        "\"For the " + year + " Program Year\"",
        "\"Report Date: " + df.format(new Date()) + "\"",
        "",
    };
  }
  
  /**
   * @return column headings
   */
  private String[] getNationalSurveillanceStrategyColumnHeadings() {
    return new String[]{
        "PIN",
        "Province/Territory",
        "Program Year",
        "Farm Type",
        "Farm Type Detailed",
        "PY Allowable Revenue",
        "Accrued PY Allowable Revenue",
        "Accrued PY Allowable Expenses",
        "PY Margin",
        "Imputed AgriInsurance",
        "PY Overhead Expenses",
        "PY Finance and Capital Expenses",
        "PY Depreciation",
        "PY Non-Arms Length Salaries",
        "Reference Margin (before limit)",
        "Average Allowable Expenses (Reference Margin Limit)",
        "Combined Farm %",
    };
  }
  
  /**
   * @return column formats
   */
  private Format[] getNationalSurveillanceStrategyColumnFormats() {
    DecimalFormat currencyFormat = new DecimalFormat("#0.##");
    DecimalFormat integerFormat = new DecimalFormat("#");
    DecimalFormat percentFormat = new DecimalFormat("#.###");
    
    return new Format[] {
        integerFormat,   // PIN
        null,            // Province/Territory
        integerFormat,   // Program Year
        null,            // Farm Type
        null,            // Farm Type Detailed
        currencyFormat,  // PY Allowable Revenue
        currencyFormat,  // Accrued PY Allowable Revenue
        currencyFormat,  // Accrued PY Allowable Expenses
        currencyFormat,  // PY Margin
        currencyFormat,  // Imputed AgriInsurance
        currencyFormat,  // PY Overhead Expenses
        currencyFormat,  // PY Finance and Capital Expenses
        currencyFormat,  // PY Depreciation
        currencyFormat,  // PY Non-Arms Length Salaries
        currencyFormat,  // Reference Margin (before limit)
        currencyFormat,  // Average Allowable Expenses (Reference Margin Limit)    
        percentFormat,   // Combined Farm %    
    };
  }
  
  /**
   * @return header lines
   */
  private String[] getAnalyticalSurveillanceStrategyHeaderLines(Integer year) {
    DateFormat df = new SimpleDateFormat("MMMM dd YYYY");
    // Need quotes around lines that have commas in them.
    // Quoted them all just in case they are modified later.
    return new String[]{
        "\"British Columbia Ministry of Agriculture\"",
        "\"Analytical Surveillance Strategy\"",
        "\"For the " + year + " Program Year\"",
        "\"Report Date: " + df.format(new Date()) + "\"",
        "",
    };
  }
  
  /**
   * @return column headings
   */
  private String[] getAnalyticalSurveillanceStrategyColumnHeadings() {
    return new String[]{
        "PIN",
        "Province/Territory",
        "Program Year",
        "Farm Type",
        "Farm Type Detailed",
        "PY Allowable Revenue",
        "PY Allowable Expenses",
        "Accrued PY Allowable Revenue",
        "Accrued PY Allowable Expenses",
        "PY Margin",
        "Imputed AgriInsurance",
        "PY Overhead Expenses",
        "PY Finance and Capital Expenses",
        "PY Depreciation",
        "PY Non-Arms Length Salaries",
        "Reference Margin (before structural change adjustment)",
        "Reference Margin (before limit)",
        "Average Allowable Expenses (Reference Margin Limit)",
        "Viability Test Failure? (Y/N)",
        "Combined Farm %",
        "Late Participant? (Y/N)",
    };
  }
  
  /**
   * @return column formats
   */
  private Format[] getAnalyticalSurveillanceStrategyColumnFormats() {
    DecimalFormat currencyFormat = new DecimalFormat("#0.##");
    DecimalFormat integerFormat = new DecimalFormat("#");
    DecimalFormat percentFormat = new DecimalFormat("#.###");
    
    return new Format[] {
        integerFormat,   // PIN
        null,            // Province/Territory
        integerFormat,   // Program Year
        null,            // Farm Type
        null,            // Farm Type Detailed
        currencyFormat,  // PY Allowable Revenue
        currencyFormat,  // PY Allowable Expense
        currencyFormat,  // Accrued PY Allowable Revenue
        currencyFormat,  // Accrued PY Allowable Expenses
        currencyFormat,  // PY Margin
        currencyFormat,  // Imputed AgriInsurance
        currencyFormat,  // PY Overhead Expenses
        currencyFormat,  // PY Finance and Capital Expenses
        currencyFormat,  // PY Depreciation
        currencyFormat,  // PY Non-Arms Length Salaries
        currencyFormat,  // Reference Margin (before structural change adjustment)
        currencyFormat,  // Reference Margin (before limit)
        currencyFormat,  // Average Allowable Expenses (Reference Margin Limit)    
        null,            // Viability Test Failure? (Y/N)
        percentFormat,   // Combined Farm %    
        null,            // Late Participant? (Y/N) 
    };
  }
  
  /**
   * @param reportType reportType
   * @return report file name
   */
  @Override
  public String getBackgroundReportFileName(String reportType) {
    String result = null;
    
    if(reportType.equals(REPORT_NATIONAL_SURVEILLANCE_STRATEGY)) {
      result = NATIONAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_NAME;
    } else if(reportType.equals(REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY)) {
      result = ANALYTICAL_SURVEILLANCE_STRATEGY_OUTPUT_FILE_NAME;
    }
    
    return result;
  }


  private String getBackgroundReportStatusFileName(String reportType) {
    return BACKGROUND_REPORT_STATUS_FILE_PREFIX +
        reportType + BACKGROUND_REPORT_STATUS_FILE_SUFFIX;
  }


  @Override
  public void readReportStatusFile(BackgroundReportForm form) throws IOException,
  FileNotFoundException {
    String reportType = form.getReportType();
    File tempDir = IOUtils.getTempDir(); 
    String reportStatusFileName = getBackgroundReportStatusFileName(reportType);
    File reportStatusFile = new File(tempDir, reportStatusFileName);
    
    if(reportStatusFile.exists()) {
      try(FileReader fr = new FileReader(reportStatusFile);
          BufferedReader br = new BufferedReader(fr)) {
        String requestorAccountName = br.readLine();
        String reportRequestDate = br.readLine();
        form.setRequestorAccountName(requestorAccountName);
        form.setReportRequestDate(reportRequestDate);
      }
    }
  }


  @Override
  public void writeReportStatusFile(String reportType) throws FileNotFoundException {
    String reportStatusFileName = getBackgroundReportStatusFileName(reportType);
    File tempDir = IOUtils.getTempDir(); 
    File reportStatusFile = new File(tempDir, reportStatusFileName);
    
    if(reportStatusFile.exists()) {
      reportStatusFile.delete();
    }

    try(PrintWriter pw = new PrintWriter(reportStatusFile)) {
      String requestorAccountName = CurrentUser.getUser().getAccountName();
      String reportRequestDate = DateUtils.formatDateTime(new Date());
      
      pw.println(requestorAccountName);
      pw.println(reportRequestDate);
    }
  }
}
