package ca.bc.gov.srm.farm.ui.struts.tipreport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class TipReportGenerateBenchmarksAction extends TipReportsAction {
  
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

    logger.debug("Generating Tip Report Benchmarks...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportForm form = (TipReportForm) actionForm;
    
    syncFilterContextWithForm(form);
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    
    tipReportService.generateBenchmarkData(form.getYear(), CurrentUser.getUser().getUserId());
    
    return forward;
  }
}
