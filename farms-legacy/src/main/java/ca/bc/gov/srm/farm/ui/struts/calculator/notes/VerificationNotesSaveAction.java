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
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * @author awilkinson
 * @created Mar 2, 2011
 */
public class VerificationNotesSaveAction extends VerificationNotesViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Verification Notes...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    VerificationNotesForm form = (VerificationNotesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);
    
    ResponseObject responseObj = new ResponseObject();
    responseObj.setStatus("success");
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {
    	String verificationNotes = form.getNotes();
    	logger.debug("verificationNotes: " + verificationNotes);
    	
    	Integer programYearId = scenario.getFarmingYear().getProgramYearId();
    	CalculatorService service = ServiceFactory.getCalculatorService();
      
      try {
        
        if(VerificationNotesForm.NOTE_TYPE_INTERIM.equals(form.getNoteType())) {
          service.saveInterimVerificationNotes(
              scenario,
              verificationNotes,
              programYearId,
              getUserId());
        } else if(VerificationNotesForm.NOTE_TYPE_FINAL.equals(form.getNoteType())) {
          service.saveFinalVerificationNotes(
              scenario,
              verificationNotes,
              programYearId,
              getUserId());
        } else if(VerificationNotesForm.NOTE_TYPE_ADJUSTMENT.equals(form.getNoteType())) {
          service.saveAdjustmentVerificationNotes(
              scenario,
              verificationNotes,
              programYearId,
              getUserId());
        }
        
      	scenario = refreshScenario(form);
      	
      } catch(InvalidRevisionCountException irce) {
        handleInvalidRevisionCount(request);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } catch(Exception e) {
      	logger.error("Unexpected error: ", e);
        forward = mapping.findForward(ActionConstants.FAILURE);
        responseObj.setStatus("error");
      }
    }
    
    String responseJson = JsonUtils.getJsonObjectMapper().writeValueAsString(responseObj);
    
    IOUtils.writeJsonToResponse(response, responseJson);
    
    return forward;
  }
  
  class ResponseObject {
    String status;

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

}
