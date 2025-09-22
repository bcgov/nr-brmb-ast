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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.EnrolmentUtils;

/**
 * @author awilkinson
 * @created Dec 3, 2010
 *
 */
public class EnrolmentsViewAction extends EnrolmentsAction {
  
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

    logger.debug("Viewing Enrolments...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    EnrolmentsForm form = (EnrolmentsForm) actionForm;
    
    populateForm(form);

    return forward;
  }

  /**
   * @param form EnrolmentsForm
   * @throws Exception On Exception
   */
  protected void populateForm(EnrolmentsForm form) throws Exception {
    
    syncFilterContextWithForm(form);

    ImportService service = ServiceFactory.getImportService();

    boolean allowedToGenerate;
    List<String> importTypes = new ArrayList<>(1);
    importTypes.add(ImportClassCodes.ENROL);
    List<ImportSearchResult> searchResults = service.searchImports(importTypes);
    if (searchResults.size() == 0) {
      allowedToGenerate = true;
    } else {
      // rely on the fact that the results are sorted newest to oldest.
      ImportSearchResult isr = searchResults.get(0);
      allowedToGenerate = !ImportStateCodes.isInProgress(isr.getStateCode());
    }
    form.setAllowedToGenerate(allowedToGenerate);

    setEnrolmentYearSelectOptions(form);
    setRegionSelectOptions(form);
  }

  /**
   * @param form form
   */
  private void setEnrolmentYearSelectOptions(EnrolmentsForm form) {
    List<ListView> enrolmentYearSelectOptions = new ArrayList<>();
    
    int currentYear = EnrolmentUtils.getCurrentEnrolmentYear().intValue();
    int startYear = EnrolmentUtils.getEnrolmentStartYear().intValue();
    int yearsAhead = EnrolmentUtils.getEnrolmentYearsAhead().intValue();
    int endYear = currentYear + yearsAhead;
    
    for(int ii = endYear; ii >= startYear; ii--) {
      String year = Integer.toString(ii);
      ListView lv = new CodeListView(year, year);
      enrolmentYearSelectOptions.add(lv);
    }
    
    form.setEnrolmentYearSelectOptions(enrolmentYearSelectOptions);
  }
  
  /**
   * @param form form
   */
  private void setRegionSelectOptions(EnrolmentsForm form) {
    List<ListView> regionSelectOptions = new ArrayList<>();

    ListView[] offices = (ListView[]) CacheFactory.getApplicationCache().getItem(ListService.SERVER_LIST + ListService.REGIONAL_OFFICE);
    for (int ii = 0; ii < offices.length; ii++) {
      regionSelectOptions.add(offices[ii]);
    }
    ListView allOption = new CodeListView(EnrolmentsForm.REGIONAL_OFFICE_CODE_ALL, EnrolmentsForm.REGIONAL_OFFICE_CODE_ALL_DISPLAY);
    regionSelectOptions.add(allOption);
    
    form.setRegionSelectOptions(regionSelectOptions);
  }

}
