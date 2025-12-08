package ca.bc.gov.srm.farm.ui.struts.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ClientAccountService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;


/**
 * Action for screen 400.
 */
public class AccountBenefitHistoryAction extends CalculatorAction {

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
    CalculatorForm form = (CalculatorForm) actionForm;
    Scenario scenario = getScenario(form);
    
    ClientAccountService service = ServiceFactory.getClientAccountService();
    service.getReferenceYearBenefits(scenario);

    return forward;
  }


}
