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
package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;

/**
 * @author awilkinson
 */
public class ExportEnrolmentsCsvAction extends SecureAction {
  
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

    logger.debug("Generating Enrolment CSV...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    EnrolmentsForm form = (EnrolmentsForm) actionForm;

    @SuppressWarnings("unchecked")
    List<Integer> pins = (List<Integer>) CacheFactory.getUserCache().getItem(CacheKeys.CURRENT_ENROLMENT_PINS);
    
    Integer enrolmentYear = new Integer(form.getYear());
    String user = CurrentUser.getUser().getUserId();
    
    EnrolmentService enrolmentService = ServiceFactory.getEnrolmentService();
    File tempCsvFile = enrolmentService.generateCsv(enrolmentYear, pins, null, user);

    IOUtils.writeFileToResponse(response, tempCsvFile, IOUtils.CONTENT_TYPE_CSV);
    
    tempCsvFile.delete();

    return forward;
  }

}
