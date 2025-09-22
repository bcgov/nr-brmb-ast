package ca.bc.gov.srm.farm.ui.struts.codes.year.parameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.YearConfigurationParameter;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

public abstract class YearConfigurationParameterAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected void populateForm(YearConfigurationParametersForm form) throws Exception {
    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();
    YearConfigurationParameter parameter = service.getYearConfigurationParameter(form.getId());
    
    if(parameter != null) {
      form.setId(parameter.getId());
      form.setProgramYear(parameter.getProgramYear());
      form.setName(parameter.getName());
      form.setValue(parameter.getValue());
      form.setType(parameter.getType());
      form.setTypeDescription(parameter.getTypeDescription());
    }      
  }
  
  protected YearConfigurationParameter getYearConfigParamFromForm(YearConfigurationParametersForm form) {
    YearConfigurationParameter parameter = new YearConfigurationParameter();
    parameter.setId(form.getId());
    parameter.setProgramYear(form.getProgramYear());
    parameter.setName(form.getName());
    parameter.setType(form.getType());
    parameter.setValue(form.getValue());
    return parameter;
  }  

  protected void syncFilterContextWithForm(YearConfigurationParametersForm form) {
    YearConfigurationParameterFilterContext filterContext =
      (YearConfigurationParameterFilterContext) CacheFactory.getUserCache().getItem(CacheKeys.YEAR_CONFIGURATION_PARAMETER_FILTER_CONTEXT);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Year Configuration Parameter filter context does not exist. Creating...");
      filterContext = new YearConfigurationParameterFilterContext();
      CacheFactory.getUserCache().addItem(CacheKeys.YEAR_CONFIGURATION_PARAMETER_FILTER_CONTEXT, filterContext);
    }
    
    Integer programYear = form.getYearFilter();
    if(programYear == null) {
      // if year is not set assume no form fields are set and set them from the context
      
      if(filterContext.getYearFilter() == null) {
        // if context fields are not set, then set the defaults
        filterContext.setYearFilter(ProgramYearUtils.getCurrentProgramYear());
      }

      programYear = filterContext.getYearFilter();
      form.setYearFilter(programYear);
    } else {
      // if year is set assume form fields are set and update the context from the form
      filterContext.setYearFilter(programYear);
    }
    
  }
}
