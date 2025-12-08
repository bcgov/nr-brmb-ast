/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 * 
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.EnrolmentUtils;
import ca.bc.gov.srm.farm.util.PropertyLoader;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 7, 2010
 */
public class GenerateEnrolmentsAction extends EnrolmentsViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Generating Enrolments...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    EnrolmentsForm form = (EnrolmentsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {
      
      String pinsString = form.getPins();

      Integer enrolmentYear = new Integer(form.getYear());

      String regionalOfficeCode = form.getRegionalOfficeCode();
      String regionalOfficeDescription = getRegionalOfficeDescription(regionalOfficeCode);
      String enrolmentStatusFilter = form.getEnrolmentStatusFilter();
      String scenarioStateFilter = form.getScenarioStateFilter();
      String startDateFilter = form.getStartDateFilter();
      String endDateFilter = form.getEndDateFilter();
      String failedReasonFilter = form.getFailedReasonFilter();
      
      String appPropertiesPath = "config/applicationResources.properties";
      Properties props = PropertyLoader.loadProperties(appPropertiesPath);
      String enrolmentStatus = null;
      if(enrolmentStatusFilter.equals("ungenerated")) {
        enrolmentStatus = props.getProperty("Ungenerated");
      } else if(enrolmentStatusFilter.equals("generated")) {
        enrolmentStatus = props.getProperty("Generated");
      } else if(enrolmentStatusFilter.equals("failed")) {
        enrolmentStatus = props.getProperty("Failed");
      }
      
      String scenarioState = null;
      if(scenarioStateFilter.equals("all")) {
        scenarioState = props.getProperty("All.Scenario.States");
      } else if(scenarioStateFilter.equals("verified")) {
        scenarioState = props.getProperty("Verified");
      } else if(scenarioStateFilter.equals("unverified")) {
        scenarioState = props.getProperty("Unverified");
      }
      
      String failedReason = "";
      if(!StringUtils.isBlank(failedReasonFilter)) {
        if(failedReasonFilter.equals(Enrolment.REASON_OVERSIZE_MARGIN)) {
          failedReason = props.getProperty("Excessive.Margin");
        } else if(failedReasonFilter.equals(Enrolment.REASON_INSUFF_REF_MARGIN_DATA)) {
          failedReason = props.getProperty("Insufficient.Reference.Margin");
        } else if(failedReasonFilter.equals(Enrolment.REASON_BPU_SET_INCOMPLETE)) {
          failedReason = props.getProperty("Missing.BPUs");
        }
        failedReason = " - " + failedReason;
      }
      
      String dateRangeString = "";
      if(enrolmentStatusFilter.equals("generated") || enrolmentStatusFilter.equals("failed")) {
        boolean hasStartDate = !StringUtils.isBlank(startDateFilter);
        boolean hasEndDate = !StringUtils.isBlank(endDateFilter);
        if(hasStartDate || hasEndDate) {
          String startDate = startDateFilter;
          if(!hasStartDate) {
            startDate = "*";
          }
          String endDate = endDateFilter;
          if(!hasEndDate) {
            endDate = "*";
          }
          
          dateRangeString = " - " + enrolmentStatus + " from " + startDate + " to " + endDate;
        }
      }
      
      String description =
        enrolmentYear + " - " +
        regionalOfficeDescription + " - " +
        enrolmentStatus + " - " +
        scenarioState +
        failedReason +
        dateRangeString;
      
      File outFile = EnrolmentUtils.createEnrolmentFile(pinsString, enrolmentYear, form.isCreateTaskInBarn());

      ImportService service = ServiceFactory.getImportService();

      // create a farm_import_versions entry, and save the file to a blob
      try (FileInputStream importFileInputStream = new FileInputStream(outFile);) {
        ImportVersion importVersion = service.createImportVersion(
            ImportClassCodes.ENROL,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            description,
            outFile.getPath(),
            importFileInputStream,
            CurrentUser.getUser().getUserId());
        
        String key = CacheKeys.CURRENT_IMPORT;
        CacheFactory.getUserCache().addItem(key, importVersion);
      }
    }

    return forward;
  }

  /**
   * @param regionalOfficeCode regionalOfficeCode
   * @return regionalOfficeDescription
   */
  private String getRegionalOfficeDescription(String regionalOfficeCode) {
    ListView[] offices = (ListView[]) CacheFactory
    .getApplicationCache().getItem(ListService.SERVER_LIST + ListService.REGIONAL_OFFICE);
    String result = null;
    if(regionalOfficeCode.equals(EnrolmentsForm.REGIONAL_OFFICE_CODE_ALL)) {
      result = EnrolmentsForm.REGIONAL_OFFICE_CODE_ALL_DISPLAY;
    } else {
      for(int ii = 0; ii < offices.length; ii++) {
        if(offices[ii].getValue().equals(regionalOfficeCode)) {
          result = offices[ii].getLabel();
          break;
        }
      }
    }
    return result;
  }

}
