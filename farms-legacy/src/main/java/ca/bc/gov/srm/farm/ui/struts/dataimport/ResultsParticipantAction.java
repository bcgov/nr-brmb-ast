package ca.bc.gov.srm.farm.ui.struts.dataimport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * Action for screen 240.
 */
public class ResultsParticipantAction extends SecureAction {

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

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    //
    // Used to use XSL but it was too slow.
    // See also GetImportParticipantLogTag
    //
    
    return forward;
  }

}
