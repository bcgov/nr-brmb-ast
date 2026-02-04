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
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.codes.tips.TipIncomeRangeFormDataUtil;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class TipsFarmType2SaveAction extends TipsFarmSubtypeAction {
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

    logger.debug("Saving Farm Type 2...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipsFarmSubtypeForm form = (TipsFarmSubtypeForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();

      try {
        boolean exists = false;
        if (form.isNew()) {
          exists = doesFarmSubtypeExist(form.getName());
        } else {
          exists = isEnteredFarmSubtypeSameAsExisting(form.getId(), form.getName());
        }
        boolean errorFree = true;
        if (exists) {
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FARM_SUBTYPE_DUPLICATE_NAME));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
          errorFree = false;
        }
        
        if (form.getName() == null || form.getName().isEmpty()) {
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FARM_SUBTYPE_BLANK));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
          errorFree = false;
        }

        if (form.getParentName() == null || form.getParentName().isEmpty()) {
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_PARENT_FARM_TYPE_BLANK));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
          errorFree = false;
        }

        if (errorFree && form.isNew()) {
          // Create new farm subtype 
          logger.debug("Creating farm subtype.");
          Integer id = codesService.createFarmType2(getFarmSubtypeCodeFromForm(form), user);
          if (!form.getUsingDefaultRange() && !form.getIsInherited()) {
            codesService.createFarmType2IncomeRange(
                TipIncomeRangeFormDataUtil.getIncomeRangeList(form.getIncomeRangeJson(), jsonObjectMapper), 
                user, 
                id);
          }
        } else if (errorFree && !form.isNew()) {
          // update an existing farm subtype
          codesService.updateFarmType2(getFarmSubtypeCodeFromForm(form), user);
          if (!form.getUsingDefaultRange() && !form.getIsInherited()) {
            codesService.createFarmType2IncomeRange(
                TipIncomeRangeFormDataUtil.getIncomeRangeList(form.getIncomeRangeJson(), jsonObjectMapper), 
                user, 
                form.getId());
          }
        } else {
          form.setFarmTypeListViewItems(getListItemViews());
        }      
      } catch (Exception e) {
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
        form.setFarmTypeListViewItems(getListItemViews());
      }
    }

    return forward;
  }

  private boolean doesFarmSubtypeExist(String farmSubtypeName) throws ServiceException {
    CodesService codesService = ServiceFactory.getCodesService();
    if (codesService.getFarmSubtypeA(farmSubtypeName) != null) {
      return true;
    }
    return false;
  }
  
  private boolean isEnteredFarmSubtypeSameAsExisting(Integer id, String userEnteredFarmSubtypeName) throws ServiceException {
    CodesService codesService = ServiceFactory.getCodesService();
    FarmSubtype farmSubtype = codesService.getFarmSubtypeA(id);
    
    if (farmSubtype.getName().equals(userEnteredFarmSubtypeName)) {
      return false;
    }
    
    return doesFarmSubtypeExist(userEnteredFarmSubtypeName);
    
  }
}


