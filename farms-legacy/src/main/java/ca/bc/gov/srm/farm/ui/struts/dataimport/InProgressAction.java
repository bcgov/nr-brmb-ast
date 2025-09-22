package ca.bc.gov.srm.farm.ui.struts.dataimport;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Action for screen 205.
 */
public class InProgressAction extends SecureAction {

  /**
   * doExecute.
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
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ImportForm form = (ImportForm) actionForm;
    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ImportService service = ServiceFactory.getImportService();

    String importId = form.getImportVersionId();
    Integer importVersionId = new Integer(importId);
    ImportVersion iv = service.getImportVersion(importVersionId);
    CacheFactory.getUserCache().addItem(CacheKeys.IMPORT_VERSION, iv);

    String stateCode = iv.getImportStateCode();

    if (ImportStateCodes.isImportDone(stateCode)) {
      forward = getRedirect(mapping, "importResults", form);
    } else if (ImportStateCodes.isStagingDone(stateCode)) {
      forward = getRedirect(mapping, "stagingResults", form);
    }

    return forward;
  }

  
  private static ActionRedirect getRedirect(ActionMapping mapping, String forward, ImportForm form) {

    ActionRedirect redirect = new ActionRedirect(mapping.findForward(forward));
    redirect.addParameter("importVersionId", form.getImportVersionId());

    return redirect;
  }

}
