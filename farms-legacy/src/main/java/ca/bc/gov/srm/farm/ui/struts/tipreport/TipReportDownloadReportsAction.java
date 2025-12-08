package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;

public class TipReportDownloadReportsAction extends SecureAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportForm form = (TipReportForm) actionForm;
    
    String farmingOperationIdsString = form.getFarmingOperationIds();
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    Path tipReportsZipFile = tipReportService.downloadReports(farmingOperationIdsString);
    IOUtils.writeFileToResponse(response, tipReportsZipFile, IOUtils.CONTENT_TYPE_ZIP);

    return forward;
  }
}
