/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.partners;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

public class PartnersJavaScriptListAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  @Override
  protected ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    
    logger.debug("JavaScript Partners List...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    PartnersJavaScriptListForm form = (PartnersJavaScriptListForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  protected void populateForm(PartnersJavaScriptListForm form) throws ServiceException {
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    List<FarmingOperationPartner> allPartners = calculatorService.getAllPartners();
    form.setPartners(allPartners);
  }
}
