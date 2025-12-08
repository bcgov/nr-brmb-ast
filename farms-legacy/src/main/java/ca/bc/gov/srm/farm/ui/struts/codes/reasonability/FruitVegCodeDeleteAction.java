package ca.bc.gov.srm.farm.ui.struts.codes.reasonability;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCode;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class FruitVegCodeDeleteAction extends FruitVegCodeAction {
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

    logger.debug("Deleting Fruit veg code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FruitVegCodeForm form = (FruitVegCodeForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);        
    
    if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {

        CodesService codesService = ServiceFactory.getCodesService();
        ActionMessages errorMessages = new ActionMessages();
        
        try {
          FruitVegTypeCode fruitVegCode = getFruitVegCodeFromForm(form);
          if (codesService.checkFruitVegCodeInUse(fruitVegCode)) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERROR_FRUIT_VEG_CODE_DELETE));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);            
          } else {
            // Can be deleted as the Fruit Veggie code isn't being used elsewhere
            codesService.deleteFruitVegCode(fruitVegCode);
          }
        } catch(Exception e) {
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    
    return forward;
  }
}
