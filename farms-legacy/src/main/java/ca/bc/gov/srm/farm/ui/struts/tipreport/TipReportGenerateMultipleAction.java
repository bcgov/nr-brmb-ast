package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.PropertyLoader;

public class TipReportGenerateMultipleAction extends SecureAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportForm form = (TipReportForm) actionForm;
    
    Integer year = form.getYear();
    String farmingOperationIdsString = form.getFarmingOperationIds();

    String appPropertiesPath = "config/applicationResources.properties";
    Properties props = PropertyLoader.loadProperties(appPropertiesPath);
    String reportState = null;
    if(form.getReportStatusFilter().equals("ungenerated")) {
      reportState = props.getProperty("Ungenerated");
    } else if(form.getReportStatusFilter().equals("generated")) {
      reportState = props.getProperty("Generated");
    } else if(form.getReportStatusFilter().equals("failed")) {
      reportState = props.getProperty("Unclassified");
    }
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    tipReportService.scheduleTipReportGeneration(farmingOperationIdsString, year, reportState);

    return forward;
  }
}
