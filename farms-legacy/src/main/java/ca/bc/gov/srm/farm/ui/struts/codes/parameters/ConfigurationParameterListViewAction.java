package ca.bc.gov.srm.farm.ui.struts.codes.parameters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.ConfigurationParameter;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class ConfigurationParameterListViewAction extends ConfigurationParameterAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Year Configuration Parameters...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ConfigurationParametersForm form = (ConfigurationParametersForm) actionForm;
    
    populateForm(form, request);
    
    return forward;
  }

  @Override
  protected void populateForm(ConfigurationParametersForm form, HttpServletRequest request) throws Exception {
    CodesService service = ServiceFactory.getCodesService();
    List<ConfigurationParameter> parameters = service.getConfigurationParameters();
    form.setConfigurationParameters(parameters);
    form.setNumConfigurationParameters(parameters.size());
  }
}
