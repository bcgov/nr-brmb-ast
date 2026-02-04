package ca.bc.gov.srm.farm.ui.struts.tipreport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;

public class TipReportGenerateSingleAction extends SecureAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportForm form = (TipReportForm) actionForm;
    String userId = CurrentUser.getUser().getUserId();
    
    Integer farmingOperationId = form.getFarmingOperationId();
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();

    tipReportService.generateReports(farmingOperationId.toString(), null, userId);
    Integer tipReportDocumentId = tipReportService.getTipReportDocumentId(farmingOperationId);
    
    String responseJson = "{\"tipReportDocId\": " + tipReportDocumentId + "}";

    IOUtils.writeJsonToResponse(response, responseJson);

    return forward;
  }
}
