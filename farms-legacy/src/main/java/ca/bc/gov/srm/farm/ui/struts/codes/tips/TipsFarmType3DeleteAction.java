package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
public class TipsFarmType3DeleteAction extends TipsFarmType3Action{
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

    logger.debug("Deleting Farm Type...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    TipsFarmType3Form form = (TipsFarmType3Form) actionForm;
    ActionMessages errors = form.validate(mapping, request);        
    
    if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();
      
      FarmType3 farmType = getFarmTypeCodeFromForm(form);
      if (codesService.isFarmType3InUse(farmType)) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERROR_FARM_TYPE_DELETE));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);            
      } else {
        // Can be deleted as the Farm Type isn't being referenced
        codesService.deleteFarmType3(farmType);
      }
    }
    
    return forward;
  }
}




