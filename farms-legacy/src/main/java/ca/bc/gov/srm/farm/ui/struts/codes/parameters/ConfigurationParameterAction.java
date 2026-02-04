package ca.bc.gov.srm.farm.ui.struts.codes.parameters;

import javax.servlet.http.HttpServletRequest;

import ca.bc.gov.srm.farm.domain.codes.ConfigurationParameter;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.FarmSecurityUtils;

public abstract class ConfigurationParameterAction extends SecureAction {
  protected void populateForm(ConfigurationParametersForm form, HttpServletRequest request) throws Exception {
    CodesService service = ServiceFactory.getCodesService();
    ConfigurationParameter parameter = service.getConfigurationParameter(form.getId());
    
    
    if(parameter != null) {
      
      boolean canEdit = FarmSecurityUtils.canPerformAction(request, SecurityActionConstants.EDIT_CODES);
      if(!canEdit && parameter.getSensitiveDataInd()) {
        String maskedValue = FarmSecurityUtils.maskSecret(parameter.getValue());
        form.setValue(maskedValue);
      } else {
        form.setValue(parameter.getValue());
      }
      
      form.setId(parameter.getId());
      form.setName(parameter.getName());
      form.setSensitiveDataInd(parameter.getSensitiveDataInd());
      form.setType(parameter.getType());
      form.setTypeDescription(parameter.getTypeDescription());
    }
  }
  
  protected ConfigurationParameter getConfigParamFromForm(ConfigurationParametersForm form) {
    ConfigurationParameter configParam = new ConfigurationParameter();
    configParam.setId(form.getId());
    configParam.setName(form.getName());
    configParam.setType(form.getType());
    configParam.setValue(form.getValue());
    return configParam;
  }  
}