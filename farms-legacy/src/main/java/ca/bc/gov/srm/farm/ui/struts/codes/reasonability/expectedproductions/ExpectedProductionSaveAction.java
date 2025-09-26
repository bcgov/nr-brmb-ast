package ca.bc.gov.srm.farm.ui.struts.codes.reasonability.expectedproductions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.ExpectedProduction;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class ExpectedProductionSaveAction extends ExpectedProductionAction {
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

    logger.debug("Saving Expected Production Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    ExpectedProductionsForm form = (ExpectedProductionsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    
    syncFilterContextWithForm(form);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {
      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();
      
      ExpectedProduction item = getExpectedProductionItemFromForm(form);
      
      try {
        if (form.isNew()) {
          if (codesService.checkExpectedProductionItemExists(item)) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERROR_EXPECTED_PROUCTION_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
            return forward;
          } 
                      
          boolean isValid = true;
          if (form.getInventoryItemCode() == null || form.getInventoryItemCode().isEmpty()) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_INVENTORY_CODE_REQUIRED));
            isValid = false;
          }
          
          if (form.getCropUnitCode() == null || form.getCropUnitCode().isEmpty()) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERROR_EXPECTED_PRODUCTION_CROP_UNIT_BLANK));
            isValid = false;
          }         
          
          if (validateExpectedProdVal(form.getExpectedProductionPerAcre(), errorMessages) && isValid) {
            // save the expected prod item here
            codesService.createExpectedProductionItem(item, user);
          } else {
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          }          
        } else {
          // Update existing expected prod item        
          if (validateExpectedProdVal(form.getExpectedProductionPerAcre(), errorMessages)) {
            logger.debug("Updating an existing expected prod item.");
            codesService.updateExpectedProductionItem(item, user);            
          } else {
            forward = mapping.findForward(ActionConstants.FAILURE);
            saveErrors(request, errorMessages);
          }
        }

      } catch (Exception e) {
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }      
    }
    return forward;
  }
  
  private boolean validateExpectedProdVal(String expectedProductionVal, ActionMessages errorMessages) {
    if (expectedProductionVal == null || expectedProductionVal.isEmpty()) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERROR_EXPECTED_PRODUCTION_BLANK));
      return false;
    }
    
    // check if a number was entered
    try {
      Double.valueOf(expectedProductionVal);
    } catch (NumberFormatException e) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERROR_EXPECTED_PRODUCTION_INVALID));
      return false;
    }
    
    return true;
  }
  
  private boolean validateProvidedVals(ExpectedProductionsForm form, ActionMessages errorMessages) {
    boolean isValid = true;
    if (form.getInventoryItemCode() == null || form.getInventoryItemCode().isEmpty()) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_INVENTORY_CODE_REQUIRED));
      isValid = false;
    }
    
    if (form.getCropUnitCode() == null || form.getCropUnitCode().isEmpty()) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERROR_EXPECTED_PRODUCTION_CROP_UNIT_BLANK));
      isValid = false;
    }    
    
    return isValid;
  }
}
