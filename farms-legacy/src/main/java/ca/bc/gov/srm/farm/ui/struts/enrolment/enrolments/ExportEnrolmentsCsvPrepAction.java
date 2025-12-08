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
import java.util.StringTokenizer;

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
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 14, 2010
 */
public class ExportEnrolmentsCsvPrepAction extends EnrolmentsViewAction {
  
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

    logger.debug("Preparing to Export Enrolments...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    EnrolmentsForm form = (EnrolmentsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {
      
      List<Integer> pins = new ArrayList<>();
      String pinsString = form.getPins();
      StringTokenizer st = new StringTokenizer(pinsString, ",");
      
      while(st.hasMoreTokens()) {
        String curPinString = st.nextToken();
        if( ! StringUtils.isBlank(curPinString) ) {
          Integer curPin = DataParseUtils.parseIntegerObject(curPinString);
          pins.add(curPin);
        }
      }

      CacheFactory.getUserCache().addItem(CacheKeys.CURRENT_ENROLMENT_PINS, pins);
      
      form.setExportUrl("exportEnrolmentCsv.do?year=" + form.getYear());
    }
    
    populateForm(form);

    return forward;
  }

}
