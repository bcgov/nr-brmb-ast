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
package ca.bc.gov.srm.farm.ui.struts.calculator.inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Jan 27, 2011
 */
public class InventoryAddNewAction extends InventoryViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Adding New Inventory...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    InventoryForm form = (InventoryForm) actionForm;
    Scenario scenario = getScenario(form);
    setReadOnlyFlag(request, form, scenario);
    ActionMessages errors = new ActionMessages();
    
    boolean missingSupplementalDates = isMissingSupplementalDates(scenario, form);
    if(missingSupplementalDates) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_ADJUSTMENT_SCREENS));
    }

    populateForm(form, scenario, false);
    
    if(errors.isEmpty()) {
    
      form.setAddedNew(true);
      addNew(form, scenario);
  
      ActionMessages warnings = getDuplicateWarnings(scenario);
      warnings.add(getMissingFmvWarnings(scenario));
      warnings.add(getNonMarketCommodityMismatchedPricesWarnings(scenario, form));
      if(!warnings.isEmpty()) {
        saveMessages(request, warnings);
      }
    } else {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    return forward;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   */
  private void addNew(InventoryForm form, Scenario scenario) {
    
    DecimalFormat df = new DecimalFormat("00");
    
    final int numClassCodes = 3;
    List<String> classCodes = new ArrayList<>(numClassCodes);
    classCodes.add(InventoryClassCodes.CROP);
    classCodes.add(InventoryClassCodes.LIVESTOCK);
    
    final int numberOfNewLines = 10;
    int itemNum = 0;
    int numAddedThisView = 0;
    for(String classCode : classCodes) {
      while(numAddedThisView < numberOfNewLines) {
        String lineKey = InventoryFormData.TYPE_NEW + "_" + df.format(itemNum);
        InventoryFormData fd = form.getItem(lineKey);
        fd.setItemType(InventoryFormData.getItemType(classCode));
        fd.setNew(true);
        fd.setAdjQuantityProduced(StringUtils.formatTwoDecimalPlaces(null));
        fd.setAdjQuantityStart(StringUtils.formatTwoDecimalPlaces(null));
        fd.setAdjPriceStart(StringUtils.formatTwoDecimalPlaces(null));
        fd.setAdjQuantityEnd(StringUtils.formatTwoDecimalPlaces(null));
        fd.setAdjPriceEnd(StringUtils.formatTwoDecimalPlaces(null));
        itemNum++;
        numAddedThisView++;
      }
      numAddedThisView = 0;
    }

  }

}
