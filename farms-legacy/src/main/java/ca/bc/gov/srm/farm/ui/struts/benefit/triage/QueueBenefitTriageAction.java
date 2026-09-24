package ca.bc.gov.srm.farm.ui.struts.benefit.triage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

public class QueueBenefitTriageAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Queueing Benefit Triage job...");

    BenefitTriageService service = ServiceFactory.getBenefitTriageService();

    String triageJobDescription = String.format("Benefit Triage Calculation requested by user %s", getUserAccountName());

    Integer importVersionId = service.queueBenefitTriage(triageJobDescription, getUserId());
    logger.debug("Queued Benefit Triage job importVersionId: " + importVersionId);

    return mapping.findForward(ActionConstants.SUCCESS);
  }
}
