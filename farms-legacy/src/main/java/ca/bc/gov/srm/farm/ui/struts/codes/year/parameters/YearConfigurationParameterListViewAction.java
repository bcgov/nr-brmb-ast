package ca.bc.gov.srm.farm.ui.struts.codes.year.parameters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.YearConfigurationParameter;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.UiUtils;

public class YearConfigurationParameterListViewAction extends YearConfigurationParameterAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Configuration Parameters...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    YearConfigurationParametersForm form = (YearConfigurationParametersForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  @Override
  protected void populateForm(YearConfigurationParametersForm form) throws Exception {
    
    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();

    Integer selectedYear = form.getYearFilter();
    if(selectedYear == null) {
      selectedYear = ProgramYearUtils.getCurrentProgramYear();
      form.setYearFilter(selectedYear);
    }
    
    setProgramYearSelectOptions(form);
    
    List<YearConfigurationParameter> parameters = service.getYearConfigurationParameters(selectedYear);
    form.setYearConfigurationParameters(parameters);
    form.setNumYearConfigurationParameters(parameters.size());
  }


  protected void setProgramYearSelectOptions(YearConfigurationParametersForm form) {
    List<ListView> options = UiUtils.getAdminYearsSelectOptions();
    form.setProgramYearSelectOptions(options);
  }
}
