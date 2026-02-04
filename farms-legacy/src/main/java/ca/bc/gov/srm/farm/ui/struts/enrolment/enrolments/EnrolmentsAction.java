/**
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.EnrolmentUtils;

/**
 * @author awilkinson
 */
public abstract class EnrolmentsAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String FILTER_CONTEXT_CACHE_KEY = CacheKeys.ENROLMENTS_FILTER_CONTEXT;

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(EnrolmentsForm form) {
    EnrolmentsFilterContext filterContext =
      (EnrolmentsFilterContext) CacheFactory.getUserCache().getItem(FILTER_CONTEXT_CACHE_KEY);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Enrolments filter context does not exist. Creating...");
      filterContext = new EnrolmentsFilterContext();
      CacheFactory.getUserCache().addItem(FILTER_CONTEXT_CACHE_KEY, filterContext);
    }

    if(form.getYear() == 0) {
      // if year is not set assume no form fields are set and set them from the context
      
      if(filterContext.getYear() == 0) {
        // if context fields are not set, then set the defaults
        filterContext.setYear(EnrolmentUtils.getCurrentEnrolmentYear().intValue());
        filterContext.setRegionalOfficeCode(EnrolmentsForm.REGIONAL_OFFICE_CODE_ALL);
        filterContext.setEnrolmentStatusFilter(EnrolmentsForm.ENROLMENT_STATUS_FILTER_UNGENERATED);
        filterContext.setScenarioStateFilter(EnrolmentsForm.SCENARIO_STATE_FILTER_UNGENERATED);
      }
      form.setYear(filterContext.getYear());
      form.setRegionalOfficeCode(filterContext.getRegionalOfficeCode());
      form.setEnrolmentStatusFilter(filterContext.getEnrolmentStatusFilter());
      form.setScenarioStateFilter(filterContext.getScenarioStateFilter());
      form.setStartDateFilter(filterContext.getStartDateFilter());
      form.setEndDateFilter(filterContext.getEndDateFilter());
      form.setPinFilter(filterContext.getPinFilter());
      form.setFailedReasonFilter(filterContext.getFailedReasonFilter());
    } else {
      // if year is set assume form fields are set and update the context from the form
      filterContext.setYear(form.getYear());
      filterContext.setRegionalOfficeCode(form.getRegionalOfficeCode());
      filterContext.setEnrolmentStatusFilter(form.getEnrolmentStatusFilter());
      filterContext.setScenarioStateFilter(form.getScenarioStateFilter());
      filterContext.setStartDateFilter(form.getStartDateFilter());
      filterContext.setEndDateFilter(form.getEndDateFilter());
      filterContext.setPinFilter(form.getPinFilter());
      filterContext.setFailedReasonFilter(form.getFailedReasonFilter());
    }
  }

}
