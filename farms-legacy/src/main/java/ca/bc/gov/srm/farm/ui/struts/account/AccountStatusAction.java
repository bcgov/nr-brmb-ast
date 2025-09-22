package ca.bc.gov.srm.farm.ui.struts.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.SubscriptionService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.status.CalculatorStatusForm;
import ca.bc.gov.srm.farm.ui.struts.calculator.status.CalculatorStatusViewAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;


/**
 * AccountStatusAction - Action for screen 300.
 */
public class AccountStatusAction extends CalculatorStatusViewAction {

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
    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    int pin = form.getPin();
    int year;
    
    if(form.getYear() != 0) {
    	year = form.getYear();
    } else {
      year = ProgramYearUtils.getCurrentProgramYear().intValue();
      form.setYear(year);
    }

    Scenario scenario = getScenario(form);
    
    populateForm(form, scenario);
    
    // also get authorized users
    SubscriptionService service = ServiceFactory.getSubscriptionService();
    List users = service.getAuthorizedUsers(new Integer(pin));
    request.setAttribute("authorizedUsers", users);

    return forward;
  }


}
