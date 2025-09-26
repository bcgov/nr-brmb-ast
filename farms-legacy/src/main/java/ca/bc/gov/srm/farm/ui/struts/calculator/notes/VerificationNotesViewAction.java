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
package ca.bc.gov.srm.farm.ui.struts.calculator.notes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author awilkinson
 * @created Mar 2, 2011
 */
public class VerificationNotesViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Verification Notes...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    VerificationNotesForm form = (VerificationNotesForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);

    return forward;
  }

  /**
   * Fill the form fields from the account
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   */
  protected void populateForm(VerificationNotesForm form, Scenario scenario) {
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    
  	String notes = null;
  	
  	if(VerificationNotesForm.NOTE_TYPE_INTERIM.equals(form.getNoteType())) {
  	  notes = scenario.getInterimVerificationNotes();
  	} else if(VerificationNotesForm.NOTE_TYPE_FINAL.equals(form.getNoteType())) {
  	  notes = scenario.getFinalVerificationNotes();
  	} else if(VerificationNotesForm.NOTE_TYPE_ADJUSTMENT.equals(form.getNoteType())) {
  	  notes = scenario.getAdjustmentVerificationNotes();
  	}
  	
  	form.setNotes(notes);
  }

  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean result;

    boolean authorizedAction = canPerformAction(request, SecurityActionConstants.EDIT_SCENARIO);
    boolean assignedTo = isAssignedToCurrentUser(scenario);

    if(authorizedAction && assignedTo) {
      result = false;
    } else {
      result = true;
    }
    
    return result;
  }

}
