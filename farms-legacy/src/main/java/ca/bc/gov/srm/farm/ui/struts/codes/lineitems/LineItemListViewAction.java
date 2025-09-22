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
package ca.bc.gov.srm.farm.ui.struts.codes.lineitems;

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
import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 *
 * @author awilkinson
 */
public class LineItemListViewAction extends LineItemAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Line Items...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    LineItemsForm form = (LineItemsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(LineItemsForm form) throws Exception {
    
    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();
    Integer selectedYear = form.getYearFilter();
    if(selectedYear == null) {
      selectedYear = ProgramYearUtils.getCurrentProgramYear();
      form.setYearFilter(selectedYear);
    }
    List<LineItemCode> lineItems = service.getLineItems(selectedYear);
    form.setLineItems(lineItems);
    form.setNumLineItems(lineItems.size());

    setProgramYearSelectOptions(form);

    ListView[] sectors =
      (ListView[]) CacheFactory.getApplicationCache().getItem(ListService.SERVER_LIST + ListService.SECTOR);
    form.setSectors(sectors);
    
    form.setFruitVegListViewItems(service.getFruitVegListItems());
  }

  
  /**
   * @param form form
   */
  private void setProgramYearSelectOptions(LineItemsForm form) {
    List<ListView> programYearSelectOptions = new ArrayList<>();
    
    //
    // We should be getting the min year from the database, but this is
    // a quick fix for 2.1.1. The current min "line item" year is 2002.
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
