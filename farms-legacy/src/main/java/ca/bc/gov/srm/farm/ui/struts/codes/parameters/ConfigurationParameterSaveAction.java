package ca.bc.gov.srm.farm.ui.struts.codes.parameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class ConfigurationParameterSaveAction extends ConfigurationParameterAction {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String PARAM_TYPE_DECIMAL = "DECIMAL";
  private static final String PARAM_TYPE_INTEGER = "INTEGER";

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Configuration Parameter...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ConfigurationParametersForm form = (ConfigurationParametersForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      if(ConfigurationKeys.isTipsConfigurationParameter(form.getName())) {
        checkAuthorization(request, SecurityActionConstants.EDIT_TIP_CODES);
      } else {
        checkAuthorization(request, SecurityActionConstants.EDIT_CODES);
      }

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();
                  
      // Update existing config param                   
      if (checkConfigParamValProvided(form, errorMessages)) {
        logger.debug("Updating an existing config param.");
        codesService.updateConfigParam(getConfigParamFromForm(form), user);
      }
    }    
        
    return forward;
  }
  
  private boolean checkTypeMatchesValue(String type, String value) {
    boolean typeAndValueMatch = true;
    
    if (type.equals(PARAM_TYPE_DECIMAL)) {
      try {
        Double.parseDouble(value);
      } catch (NumberFormatException e) {
        typeAndValueMatch = false;
      }      
    }
    
    if (type.equals(PARAM_TYPE_INTEGER)) {
      try {
        Integer.parseInt(value);
      } catch (NumberFormatException e) {
        typeAndValueMatch = false;
      }      
    }    
    
    return typeAndValueMatch;
  }
  
  private boolean checkConfigParamValProvided(ConfigurationParametersForm form, ActionMessages errorMessages) {
    boolean valueProvided = true;
    
    if (form.getValue() == null || form.getValue().isEmpty()) {
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CONFIG_PARAM_VALUE_BLANK));
      valueProvided = false;
    }
        
    if (valueProvided) {
      valueProvided = checkTypeMatchesValue(form.getType(), form.getValue());
      if (!valueProvided) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CONFIG_PARAM_TYPE_VALUE_MISMATCH));
      }
    }
    
    return valueProvided;
  }
}
