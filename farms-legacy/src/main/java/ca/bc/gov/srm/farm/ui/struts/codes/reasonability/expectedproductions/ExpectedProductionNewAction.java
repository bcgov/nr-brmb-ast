package ca.bc.gov.srm.farm.ui.struts.codes.reasonability.expectedproductions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class ExpectedProductionNewAction extends ExpectedProductionAction{
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

    logger.debug("Viewing Create New Expected Production Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ExpectedProductionsForm form = (ExpectedProductionsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  @Override
  protected void populateForm(ExpectedProductionsForm form) throws Exception {
    form.setNew(true);
  }
}
