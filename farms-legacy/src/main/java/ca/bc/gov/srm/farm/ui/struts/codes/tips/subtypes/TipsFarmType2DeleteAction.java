package ca.bc.gov.srm.farm.ui.struts.codes.tips.subtypes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class TipsFarmType2DeleteAction extends TipsFarmSubtypeAction{
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
    
    TipsFarmSubtypeForm form = (TipsFarmSubtypeForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);        
    
    if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();
      
      FarmSubtype farmSubtype = getFarmSubtypeCodeFromForm(form);
      if (codesService.isFarmSubtypeAInUse(farmSubtype)) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERROR_FARM_SUBTYPE_DELETE));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);  
        form.setFarmTypeListViewItems(getListItemViews());
      } else {
        // Can be deleted as the Farm Subtype isn't being used by SubtypeB
        codesService.deleteFarmType2(farmSubtype);
        
      }
    }
    
    return forward;
  }
}





