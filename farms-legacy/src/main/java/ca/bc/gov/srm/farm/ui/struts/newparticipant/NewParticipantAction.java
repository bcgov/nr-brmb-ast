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
package ca.bc.gov.srm.farm.ui.struts.newparticipant;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.participant.PersonFormData;
import ca.bc.gov.srm.farm.util.JsonUtils;

public class NewParticipantAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    
    logger.debug("New Participant...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    NewParticipantForm form = (NewParticipantForm) actionForm;
    populateForm(form);
    
    logger.debug("newParticipantJson: " + form.getNewParticipantJson());
    logger.debug("metaDataJson: " + form.getMetaDataJson());

    return forward;
  }

  private void populateForm(NewParticipantForm form) throws JsonProcessingException {
    
    NewParticipantFormData participant = getDefaultParticipant();
    NewParticipantFormMetaData metaData = getFormMetaData();
    
    form.setNewParticipantJson(jsonObjectMapper.writeValueAsString(participant));
    form.setMetaDataJson(jsonObjectMapper.writeValueAsString(metaData));
  }

  private NewParticipantFormData getDefaultParticipant() {
    
    NewParticipantFormData participant = new NewParticipantFormData();
    
    PersonFormData owner = getDefaultOwner();
    PersonFormData contact = new PersonFormData();
    List<OperationFormData> operations = getDefaultOperations();
    
    participant.setOwner(owner);
    participant.setContact(contact);
    participant.setOperations(operations);
    
    return participant;
  }

  private PersonFormData getDefaultOwner() {
    PersonFormData person = new PersonFormData();
    person.setProvinceState("BC");
    person.setCountry("CAN");
    
    return person;
  }

  private List<OperationFormData> getDefaultOperations() {
    List<OperationFormData> operations = new ArrayList<>();
    
    OperationFormData operation1 = new OperationFormData();
    operation1.setOperationNumber("1");
    
    operations.add(operation1);
    return operations;
  }

  private NewParticipantFormMetaData getFormMetaData() {
    NewParticipantFormMetaData metaData = new NewParticipantFormMetaData();
    
    ListService listService = ServiceFactory.getListService();
    
    List<ListView> municipalities = listService.getList(ListService.MUNICIPALITY);
    metaData.setMunicipalities(municipalities);
    
    List<ListView> accountingCodes = listService.getList(ListService.FEDERAL_ACCOUNTING);
    metaData.setAccountingCodes(accountingCodes);
    
    return metaData;
  }
}
