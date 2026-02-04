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
package ca.bc.gov.srm.farm.ui.struts.codes.fmv;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.FMV;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 * @author awilkinson
 */
public class FMVListViewAction extends FMVAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing FMVs...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FMVsForm form = (FMVsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }


  protected void populateForm(FMVsForm form) throws Exception {
    
    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();
    Integer selectedYear = form.getYearFilter();
    if(selectedYear == null) {
      selectedYear = ProgramYearUtils.getCurrentProgramYear();
      form.setYearFilter(selectedYear);
    }
    List<FMV> fmvs = service.getFMVs(selectedYear);
    form.setFmvs(fmvs);
    form.setNumFMVs(fmvs.size());

    setProgramYearSelectOptions(form);
  }


  private void setProgramYearSelectOptions(FMVsForm form) {
    List<ListView> programYearSelectOptions = new ArrayList<>();
    
    //
    // We should be getting the min year from the database, but this is
    // a quick fix for 2.1.1. The current min FMV year is 2003.
    //
    final int firstYear = 2000;
    int latestYear = ProgramYearUtils.getLatestAdminYear();
    
    for(int ii = latestYear; ii >= firstYear; ii--) {
      String year = Integer.toString(ii);
      ListView lv = new CodeListView(year, year);
      programYearSelectOptions.add(lv);
    }
    
    form.setProgramYearSelectOptions(programYearSelectOptions);
  }

}
