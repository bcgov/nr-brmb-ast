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
package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 * @author awilkinson
 */
public abstract class BPUAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(BPUsForm form) {
    BPUFilterContext filterContext =
      (BPUFilterContext) CacheFactory.getUserCache().getItem(CacheKeys.BPU_FILTER_CONTEXT);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("BPU filter context does not exist. Creating...");
      filterContext = new BPUFilterContext();
      CacheFactory.getUserCache().addItem(CacheKeys.BPU_FILTER_CONTEXT, filterContext);
    }
    
    Integer programYear = form.getYearFilter();
    if(programYear == null) {
      // if year is not set assume no form fields are set and set them from the context
      
      if(filterContext.getYearFilter() == null) {
        // if context fields are not set, then set the defaults
        filterContext.setYearFilter(ProgramYearUtils.getCurrentProgramYear());
      }
      programYear = filterContext.getYearFilter();
      form.setYearFilter(programYear);
      form.setInventoryCodeFilter(filterContext.getInventoryCodeFilter());
      form.setInventoryDescFilter(filterContext.getInventoryDescFilter());
      form.setMunicipalityFilter(filterContext.getMunicipalityFilter());
      form.setMarginExpenseFilter(filterContext.getMarginExpenseFilter());
    } else {
      // if year is set assume form fields are set and update the context from the form
      filterContext.setYearFilter(programYear);
      filterContext.setInventoryCodeFilter(form.getInventoryCodeFilter());
      filterContext.setInventoryDescFilter(form.getInventoryDescFilter());
      filterContext.setMunicipalityFilter(form.getMunicipalityFilter());
      filterContext.setMarginExpenseFilter(form.getMarginExpenseFilter());
    }
    
  }

}
