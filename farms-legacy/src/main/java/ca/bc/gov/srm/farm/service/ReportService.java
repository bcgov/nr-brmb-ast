/**
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.ui.struts.export.ExportForm;
import ca.bc.gov.srm.farm.ui.struts.report.BackgroundReportForm;
import ca.bc.gov.srm.farm.ui.struts.report.ReportForm;


/**
 * Used by the webapp to generate URLs for PDF reports.
 */
public interface ReportService {

  /** Report Types */

  /** Supplementary Info Provided */
  String REPORT_600 = "REPORT_600";

  /** Calculated Benefits */
  String REPORT_610 = "REPORT_610";

  /** Submissions by SIN/CTN/BN */
  String REPORT_620 = "REPORT_620";
  
  
  String REPORT_AAFM = "AAFM";
  
  String REPORT_AAFMA = "AAFMA";

  String REPORT_STA = "STA";
  
  String REPORT_NATIONAL_SURVEILLANCE_STRATEGY = "REPORT_NATIONAL_SURVEILLANCE_STRATEGY";
  
  String REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY = "REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY";
  
  
  String BACKGROUND_REPORT_STATUS_FILE_PREFIX = "backgroundReportStatus_";
  
  String BACKGROUND_REPORT_STATUS_FILE_SUFFIX = ".txt";

  /**
   * Validate the form for a screens 600 to 630.
   *
   * @param   mapping  mapping
   * @param   form     form
   * @param   request  request
   *
   * @return  ActionMessages
   */
  ActionMessages validateForm(
  	final ActionMapping mapping,
    final ReportForm form, 
    final HttpServletRequest request);
  
  /**
   * Validate the form for a screen 700.
   *
   * @param   mapping  mapping
   * @param   form     form
   * @param   request  request
   *
   * @return  ActionMessages
   */
  ActionMessages validateForm(
  	final ActionMapping mapping,
    final ExportForm form, 
    final HttpServletRequest request);
  
  /**
   * Get URL to call a report from screens 600 to 630.
   *
   * @param   form  form
   *
   * @return  the URL to call the report
   */
  String getReportUrl(final ReportForm form);
  
  
  /**
   * Save the COB report in a Blob in a table.
   * 
   * @throws Exception if it could not be saved
   */
  void saveCob(final Integer scenarioId, Integer programYear, String userId) throws Exception;
  
  /**
   * Regenerate the COB report and update the blob in a table.
   * 
   * @throws Exception if it could not be saved
   */
  void updateCob(final Integer scenarioId, Integer programYear, String userId) throws Exception;
  
  /**
   * Delete the COB report.
   * 
   * @param   scenarioId  scenarioId
   *
   * @throws Exception if it could not be deleted
   */
  void deleteBenefitDocument(final Integer scenarioId) throws Exception;
  
  
  /**
   * get the cob blob
   *
   * @param  scenarioId  scenarioId
   * @param  response HttpServletResponse
   * @throws Exception on error
   */
  void writeCobToResponse(final Integer scenarioId, final HttpServletResponse response, String fileName) 
  throws Exception;
  
  void generateNationalSurveillanceStrategy(Integer year) throws IOException, ServiceException;
  
  void generateAnalyticalSurveillanceStrategy(Integer year) throws IOException, ServiceException;
  
  String getBackgroundReportFileName(String reportType);
  
  void readReportStatusFile(BackgroundReportForm form) throws IOException;

  void writeReportStatusFile(String reportType) throws FileNotFoundException;
  
}
