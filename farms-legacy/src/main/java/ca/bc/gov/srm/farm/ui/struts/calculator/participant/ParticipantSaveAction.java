/**
 *
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class ParticipantSaveAction extends ParticipantViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Farm Detail...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ParticipantForm form = (ParticipantForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    Scenario scenario = getScenario(form);

    checkReadOnly(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {

      if ((errors == null || errors.isEmpty()) && MathUtils.valuesNotEqual(form.getNewPin(), form.getPin())) {
        ParticipantValidator validator = ParticipantValidator.getInstance();
        errors = validator.validate(form.getNewPin());
      }

      if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
  
        Client client = scenario.getClient();
        populateFromForm(client, form);

        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        
        try {
          calculatorService.updateClient(client, getUserId());
          form.setPin(form.getNewPin());

          scenario = refreshScenario(form);
          populateForm(form, scenario);
    
        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }


  @SuppressWarnings("unused")
  private void checkErrors(ParticipantForm form, Scenario scenario, ActionMessages errors) {
    // no checks needed
  }

  private void populateFromForm(Client client, ParticipantForm form) {
    
    client.setSin(form.getSin());
    client.setBusinessNumber(form.getBusinessNumber());
    client.setTrustNumber(form.getTrustNumber());
    client.setParticipantClassCode(form.getParticipantClassCode());
    client.setRevisionCount(Integer.valueOf(form.getClientRevisionCount()));
    
    if (form.getNewPin() != client.getParticipantPin()) {
      client.setParticipantPin(form.getNewPin());
    }
    
    populateFromFormData(client.getOwner(), form.getOwner());
    populateFromFormData(client.getContact(), form.getContact());
  }
  
  private void populateFromFormData(Person person, PersonFormData personFormData) {
    person.setFirstName(personFormData.getFirstName());
    person.setLastName(personFormData.getLastName());
    person.setCorpName(personFormData.getCorpName());
    person.setAddressLine1(personFormData.getAddressLine1());
    person.setAddressLine2(personFormData.getAddressLine2());
    person.setCity(personFormData.getCity());
    person.setPostalCode(personFormData.getPostalCode());
    person.setProvinceState(personFormData.getProvinceState());
    person.setDaytimePhone(personFormData.getDaytimePhone());
    person.setEveningPhone(personFormData.getEveningPhone());
    person.setFaxNumber(personFormData.getFaxNumber());
    person.setCellNumber(personFormData.getCellNumber());
    person.setEmailAddress(personFormData.getEmailAddress());
    person.setRevisionCount(Integer.valueOf(personFormData.getRevisionCount()));
  }

}
