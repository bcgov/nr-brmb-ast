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
package ca.bc.gov.srm.farm.ui.struts.calculator.participant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class ParticipantViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Participant...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ParticipantForm form = (ParticipantForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);

    return forward;
  }


  protected void populateForm(ParticipantForm form, Scenario scenario) {
    
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    
    // Scenario number is not specified when viewing the calculator.
    // The scenario to view is determined when loading the data.
    form.setScenarioNumber(scenario.getScenarioNumber());
    logger.debug("scenario number=[" + scenario.getScenarioNumber() + "]");

    Client client = scenario.getClient();
    Person owner = client.getOwner();
    Person contact = client.getContact();

    populatePersonFormData(form.getOwner(), owner);
    populatePersonFormData(form.getContact(), contact);
    
    form.setParticipantClassCode(client.getParticipantClassCode());
    form.setSin(client.getSin());
    form.setNewPin(client.getParticipantPin());
    form.setBusinessNumber(client.getBusinessNumber());
    form.setTrustNumber(client.getTrustNumber());
    form.setClientRevisionCount(String.valueOf(client.getRevisionCount()));
  }


  private void populatePersonFormData(PersonFormData personData, Person person) {

    personData.setAddressLine1(person.getAddressLine1());
    personData.setAddressLine2(person.getAddressLine2());
    personData.setCity(person.getCity());
    personData.setCorpName(person.getCorpName());
    personData.setCountry(person.getCountry());
    personData.setDaytimePhone(person.getDaytimePhone());
    personData.setEveningPhone(person.getEveningPhone());
    personData.setFaxNumber(person.getFaxNumber());
    personData.setCellNumber(person.getCellNumber());
    personData.setFirstName(person.getFirstName());
    personData.setLastName(person.getLastName());
    personData.setPostalCode(person.getPostalCode());
    personData.setProvinceState(person.getProvinceState());
    personData.setEmailAddress(person.getEmailAddress());
    personData.setRevisionCount(StringUtils.toString(person.getRevisionCount()));
  }
  
}
