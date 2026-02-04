package ca.bc.gov.srm.farm.ui.struts.dataimport;

import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ConfirmImportAction.
 */
public class ConfirmImportAction extends SecureAction {

  /**
   * execute.
   *
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   *
   * @return  The return value
   *
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward doExecute(final ActionMapping mapping,
    final ActionForm actionForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ImportService service = ServiceFactory.getImportService();
    String importId = request.getParameter("importVersionId");
    Integer importVersionId = new Integer(importId);

    service.confirmImport(importVersionId);

    return forward;
  }

}
