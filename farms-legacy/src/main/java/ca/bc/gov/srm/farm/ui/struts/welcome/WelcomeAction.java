package ca.bc.gov.srm.farm.ui.struts.welcome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.service.ClientAccountService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;


/**
 * WelcomeAction - Action to "view" screens 600 to 620.
 */
public class WelcomeAction extends SecureAction {

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

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ClientAccountService service = ServiceFactory.getClientAccountService();

    Client[] clients = service.getClientsForCurrentUser();
    CacheFactory.getUserCache().addItem(CacheKeys.CURRENT_CLIENTS, clients);

    return forward;
  }
}
