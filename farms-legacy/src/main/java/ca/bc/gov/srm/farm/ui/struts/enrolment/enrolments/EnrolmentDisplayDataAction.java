/**
 *
 * Copyright (c) 2013,
 * Government of British Columbia,
 * Canada
 * 
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * @author awilkinson
 * @created Dec 3, 2010
 *
 */
public class EnrolmentDisplayDataAction extends SecureAction {
  
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

    logger.debug("Loading Enrolment Display Data...");

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
    
    EnrolmentService enrolmentService = ServiceFactory.getEnrolmentService();
    List<Enrolment> enrolments = enrolmentService.getEnrolments(
        new Integer(form.getYear()),
        form.getRegionalOfficeCode());

    form.setEnrolments(enrolments);
  }

}
