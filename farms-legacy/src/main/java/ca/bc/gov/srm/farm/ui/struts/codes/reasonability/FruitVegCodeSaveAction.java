package ca.bc.gov.srm.farm.ui.struts.codes.reasonability;

import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class FruitVegCodeSaveAction extends FruitVegCodeAction {
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

    logger.debug("Saving Fruit Veggie Code Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    FruitVegCodeForm form = (FruitVegCodeForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();
            
      try {

        if (form.isNew()) {
          // creating new fruit veg code

          boolean exists = fruitVegCodeExists(form.getName());
          if (exists) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FRUIT_VEGGIE_CODE_NAME_DUPLICATE));
          } 
          
          boolean nameProvided = true;
          if (form.getName() == null || form.getName().isEmpty()) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FRUIT_VEGGIE_CODE_NAME_BLANK));
            nameProvided = false;
          }

          if (!exists && nameProvided && codeValsProvided(form, errorMessages)) {
            // Create new Fruit veggie code
            logger.debug("Creating new Fruit veggie code.");
            codesService.createFruitVegCode(getFruitVegCodeFromForm(form), user);
          } else {
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          }
          
        } else {
          // Update existing fruit veggie code item          
          if (codeValsProvided(form, errorMessages)) {
            // Now update existing farm type
            logger.debug("Updating an existing fruit veg code.");
            codesService.updateFruitVegCode(getFruitVegCodeFromForm(form), user);            
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
  
  private boolean codeValsProvided(FruitVegCodeForm form, ActionMessages errorMessages) {
    boolean errorFree = true;
    
    if (form.getDescription() == null || form.getDescription().isEmpty()) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FRUIT_VEGGIE_CODE_DESCRIPTION_BLANK));
      errorFree = false;
    }
    
    if (form.getVarianceLimit() == null) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FRUIT_VEGGIE_CODE_VARIANCE_BLANK));
      errorFree = false;
    }
    
    try {
      NumberFormat.getInstance().parse(form.getVarianceLimit());
    } catch (Exception e) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FRUIT_VEGGIE_CODE_VARIANCE_INVALID));
      errorFree = false;            
    }
    
    return errorFree;
  }
  
  private boolean fruitVegCodeExists(String name) throws ServiceException {
    CodesService codesService = ServiceFactory.getCodesService();
    if (codesService.getFruitVegCode(name) != null) {
      return true;
    }
    return false;
  }  
}


