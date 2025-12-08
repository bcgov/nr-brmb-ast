package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;


/**
 * ViewPinsImportScreenAction. Used to load screen 361.
 */
public class ViewPinsImportScreenAction extends SecureAction {

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
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm,
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    saveToken(request);

    return mapping.findForward(ActionConstants.SUCCESS);
  }
}
