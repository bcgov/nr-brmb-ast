package ca.bc.gov.srm.farm.ui.struts;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * SecureForwardAction.
 *
 * @author   $author$
 * @version  $Revision: 2145 $, $Date: 2013-04-11 15:06:51 -0700 (Thu, 11 Apr 2013) $
 */
public class SecureForwardAction extends SecureAction {

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
  public ActionForward doExecute(final ActionMapping mapping,
    final ActionForm actionForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    return mapping.findForward(ActionConstants.SUCCESS);
  }
}
