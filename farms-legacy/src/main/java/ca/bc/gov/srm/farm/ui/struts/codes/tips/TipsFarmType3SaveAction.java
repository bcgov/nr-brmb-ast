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

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class TipsFarmType3SaveAction extends TipsFarmType3Action {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  /**
   * @param mapping    mapping
   * @param actionForm actionForm
   * @param request    request
   * @param response   response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Farm Type...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipsFarmType3Form form = (TipsFarmType3Form) actionForm;
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
          // creating new farm type

          boolean exists = checkFarmTypeExists(form.getId());
          boolean errorFree = true;
          
          if (exists) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FARM_TYPE_DUPLICATE_NAME));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
            errorFree = false;
          }

          if (form.getFarmTypeName() == null || form.getFarmTypeName().isEmpty()) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FARM_TYPE_BLANK));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
            errorFree = false;
          }

          if (errorFree) {
            // Create new farm type code
            logger.debug("Creating farm type.");
            Integer id = codesService.createFarmType3(getFarmTypeCodeFromForm(form), user);
            if (!form.getUsingDefaultRange()) {
              codesService.createFarmType3IncomeRange(
                  TipIncomeRangeFormDataUtil.getIncomeRangeList(form.getIncomeRangeJson(), jsonObjectMapper),
                  user,
                  id);
            }
          }
          
        } else {
          // Update existing farm type

          if (form.getFarmTypeName() == null || form.getFarmTypeName().isEmpty()) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FARM_TYPE_BLANK));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
            // Now update existing farm type
            logger.debug("Updating farm type.");
            codesService.updateFarmType3(getFarmTypeCodeFromForm(form), user);
            if (!form.getUsingDefaultRange()) {
              codesService.createFarmType3IncomeRange(
                  TipIncomeRangeFormDataUtil.getIncomeRangeList(form.getIncomeRangeJson(), jsonObjectMapper),
                  user,
                  form.getId());
            }
          }
        }

      } catch (Exception e) {
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }
  
  private boolean checkFarmTypeExists(Integer id) throws ServiceException {
    CodesService codesService = ServiceFactory.getCodesService();
    if (codesService.getFarmTypeCode(id) != null) {
      return true;
    }
    return false;
  }

}
