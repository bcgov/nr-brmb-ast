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
package ca.bc.gov.srm.farm.ui.struts.tipreport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 * @author awilkinson
 */
public abstract class TipReportsAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String FILTER_CONTEXT_CACHE_KEY = CacheKeys.TIP_REPORTS_FILTER_CONTEXT;

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(TipReportForm form) {
    TipReportsFilterContext filterContext =
      (TipReportsFilterContext) CacheFactory.getUserCache().getItem(FILTER_CONTEXT_CACHE_KEY);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("TipReports filter context does not exist. Creating...");
      filterContext = new TipReportsFilterContext();
      CacheFactory.getUserCache().addItem(FILTER_CONTEXT_CACHE_KEY, filterContext);
    }

    if(form.getYear() == null) {
      // if year is not set assume no form fields are set and set them from the context
      
      if(filterContext.getYear() == 0) {
        // if context fields are not set, then set the defaults
        filterContext.setYear(ProgramYearUtils.getCurrentProgramYear());
        filterContext.setReportStatusFilter(TipReportForm.TIP_REPORT_STATUS_FILTER_GENERATED);
      }
      form.setYear(filterContext.getYear());
      form.setReportStatusFilter(filterContext.getReportStatusFilter());
      form.setPinFilter(filterContext.getPinFilter());
      form.setTipParticipantIndFilter(filterContext.getTipParticipantIndFilter());
      form.setAgriStabilityParticipantIndFilter(filterContext.getAgriStabilityParticipantIndFilter());
    } else {
      // if year is set assume form fields are set and update the context from the form
      filterContext.setYear(form.getYear());
      filterContext.setReportStatusFilter(form.getReportStatusFilter());
      filterContext.setPinFilter(form.getPinFilter());
      filterContext.setTipParticipantIndFilter(form.getTipParticipantIndFilter());
      filterContext.setAgriStabilityParticipantIndFilter(form.getAgriStabilityParticipantIndFilter());
    }
  }

}
