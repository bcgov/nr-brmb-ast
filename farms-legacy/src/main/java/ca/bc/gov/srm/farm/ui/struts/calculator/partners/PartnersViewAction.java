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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class PartnersViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  @Override
  protected ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    
    logger.debug("View Partners...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    PartnersForm form = (PartnersForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);
    
    logger.debug("formDataJson: " + form.getFormDataJson());
    logger.debug("metaDataJson: " + form.getMetaDataJson());

    return forward;
  }

  protected void populateForm(PartnersForm form, Scenario scenario) throws JsonProcessingException {
    
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    
    PartnersFormData participant = getFormData(scenario);
    PartnersFormMetaData metaData = getFormMetaData(scenario);
    
    form.setFormDataJson(jsonObjectMapper.writeValueAsString(participant));
    form.setMetaDataJson(jsonObjectMapper.writeValueAsString(metaData));
  }

  private PartnersFormData getFormData(Scenario scenario) {
    
    PartnersFormData formData = new PartnersFormData();
    
    List<PartnerFormData> partners = new ArrayList<>();
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    
    for (FarmingOperation farmingOperation : farmingOperations) {
      List<FarmingOperationPartner> farmingOperationPartners = farmingOperation.getFarmingOperationPartners();
      for (FarmingOperationPartner p : farmingOperationPartners) {
        PartnerFormData d = new PartnerFormData();
        d.setPartnerId(p.getFarmingOperationPartnerId().toString());
        d.setCorpName(p.getCorpName());
        d.setFirstName(p.getFirstName());
        d.setLastName(p.getLastName());
        d.setOperationSchedule(farmingOperation.getSchedule());
        d.setPartnerPercent(StringUtils.formatPercentTwoDecimalPlacesNoSymbol(p.getPartnerPercent()));
        d.setPartnerSin(p.getPartnerSin());
        d.setParticipantPin(p.getParticipantPin() == null ? null : p.getParticipantPin().toString());
        d.setRevisionCount(p.getRevisionCount().toString());
        partners.add(d);
      }
    }
    formData.setPartners(partners);
    
    return formData;
  }

  private PartnersFormMetaData getFormMetaData(Scenario scenario) {
    PartnersFormMetaData metaData = new PartnersFormMetaData();
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();

//  TODO use Lambda expression after upgrading to Java 8
//    List<String> operationSchedules = farmingOperations.stream()
//        .map(FarmingOperation::getSchedule)
//        .collect(Collectors.toList());
    
    List<String> operationSchedules = new ArrayList<>();
    for (FarmingOperation farmingOperation : farmingOperations) {
      operationSchedules.add(farmingOperation.getSchedule());
    }
    metaData.setOperationSchedules(operationSchedules);
    
    return metaData;
  }
}
