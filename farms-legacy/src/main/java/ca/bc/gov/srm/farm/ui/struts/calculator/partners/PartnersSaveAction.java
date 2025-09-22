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
package ca.bc.gov.srm.farm.ui.struts.calculator.partners;

import static ca.bc.gov.srm.farm.util.DataParseUtils.*;
import static ca.bc.gov.srm.farm.util.StringUtils.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 */
public class PartnersSaveAction extends PartnersViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Partners...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    PartnersForm form = (PartnersForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    Scenario scenario = getScenario(form);

    checkReadOnly(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {

      if (errors == null || errors.isEmpty()) {
        errors = new ActionMessages();
        checkErrors(form, scenario, errors);
      }

      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
  
        try {
          updatePartnersFromForm(form, scenario);

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


  private void updatePartnersFromForm(PartnersForm form, Scenario scenario) throws Exception {
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();

    List<FarmingOperationPartner> updatedPartners = new ArrayList<>();
    List<FarmingOperationPartner> addedPartners = new ArrayList<>();
    List<FarmingOperationPartner> removedPartners = new ArrayList<>();

    for (PartnerFormData d : form.getFormData().getPartners()) {
      FarmingOperationPartner partner = new FarmingOperationPartner(); 
      
      String schedule = d.getOperationSchedule();
      
//    TODO use Lambda expression after upgrading to Java 8
//    FarmingOperation farmingOperation = farmingOperations.stream().filter(o -> o.getSchedule().equals(schedule))
//        .findFirst().orElse(null);
      
      FarmingOperation farmingOperation = null;
      for (FarmingOperation curOp : farmingOperations) {
        if(curOp.getSchedule().equals(schedule)) {
          farmingOperation = curOp;
          break;
        }
      }
      
      if(isBlank(d.getPartnerId())) {
        addedPartners.add(partner);
      } else {
        updatedPartners.add(partner);
      }
      partner.setFarmingOperation(farmingOperation);
      
      populateFromFormData(partner, d);
    }
    
    for (FarmingOperation farmingOperation : farmingOperations) {
      List<FarmingOperationPartner> farmingOperationPartners = farmingOperation.getFarmingOperationPartners();
      for (FarmingOperationPartner partner : farmingOperationPartners) {
        Integer partnerId = partner.getFarmingOperationPartnerId();
        boolean found = false;
        for(PartnerFormData partnerFormData : form.getFormData().getPartners()) {
          found |= isNotBlank(partnerFormData.getPartnerId())
              && partnerId.equals(Integer.valueOf(partnerFormData.getPartnerId()));
        }
        if( ! found ) {
          removedPartners.add(partner); 
        }
      }
    }
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    calculatorService.updateFarmingOperationPartners(scenario, updatedPartners, addedPartners, removedPartners, getUserId());
  }
  
  private void populateFromFormData(FarmingOperationPartner p, PartnerFormData d) throws ParseException {
    
    p.setFarmingOperationPartnerId(parseIntegerObject(d.getPartnerId()));
    p.setParticipantPin(parseIntegerObject(d.getParticipantPin()));
    p.setPartnerPercent(parsePercent(d.getPartnerPercent()));
    
    p.setFirstName(d.getFirstName());
    p.setLastName(d.getLastName());
    p.setCorpName(d.getCorpName());
    p.setPartnerSin(d.getPartnerSin());
    p.setRevisionCount(parseIntegerObject(d.getRevisionCount()));
  }

  private BigDecimal parsePercent(String percentString) throws ParseException {
    BigDecimal percent = parseBigDecimal(percentString);
    percent = percent.divide(BigDecimal.valueOf(100));
    return percent;
  }


  @SuppressWarnings("unused")
  private void checkErrors(PartnersForm form, Scenario scenario, ActionMessages errors) {
    // no checks needed
  }

}
