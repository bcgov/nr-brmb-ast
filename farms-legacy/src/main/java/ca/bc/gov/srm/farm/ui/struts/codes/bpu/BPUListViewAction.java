/**
 * Copyright (c) 2011, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.UiUtils;

/**
 * 
 */
public class BPUListViewAction extends BPUAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing BPUs...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    BPUsForm form = (BPUsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }
  

  protected void populateForm(BPUsForm form) throws Exception {
    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();
    Integer selectedYear = form.getYearFilter();
    
    if(selectedYear == null) {
      selectedYear = ProgramYearUtils.getCurrentProgramYear();
      form.setYearFilter(selectedYear);
    }
    
    if(form.getMarginExpenseFilter() == null) {
    	form.setMarginExpenseFilter("M");
    }
    
    List<BPU> bpus = service.getBPUs(selectedYear);
    form.setBpus(bpus);
    form.setNumBPUs(bpus.size());

    setProgramYearSelectOptions(form);
  }
  

  private void setProgramYearSelectOptions(BPUsForm form) {
    List<ListView> options = UiUtils.getAdminYearsSelectOptions();
    form.setProgramYearSelectOptions(options);
  }

}
