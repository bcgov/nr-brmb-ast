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
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 * @created Dec 23, 2010
 */
public class ProductiveUnitsAddNewAction extends ProductiveUnitsViewAction {
  
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

    logger.debug("Adding New Productive Units...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ProductiveUnitsForm form = (ProductiveUnitsForm) actionForm;
    Scenario scenario = getScenario(form);
    form.setParticipantDataSrcCode(scenario.getParticipantDataSrcCode());
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, false);
    addNew(form, scenario);

    return forward;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   */
  protected void addNew(ProductiveUnitsForm form, Scenario scenario) {
    populateRequiredYears(form, scenario);
    
    DecimalFormat df = new DecimalFormat("000");

    final int numberOfNewPucLines = 10;
    int pucNum = 0;

    // New puc ParticipantDataSrcCodes.CRA
    for (pucNum = 0; pucNum < numberOfNewPucLines; pucNum++) {
      String lineKey = ProductiveUnitFormLine.TYPE_NEW + df.format(pucNum);
      ProductiveUnitFormLine item = form.getItem(lineKey);
      item.setNew(true);
      
      for (String participantDataSrcCode : ParticipantDataSrcCodes.getCodeList()) {
        // getting the record object instantiates one if it does not exist
        item.getRecord(participantDataSrcCode);
      }
    }

    form.setAddedNew(true);
  }

}
