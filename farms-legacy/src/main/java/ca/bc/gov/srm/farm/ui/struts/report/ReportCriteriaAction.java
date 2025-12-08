package ca.bc.gov.srm.farm.ui.struts.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 * ReportCriteriaAction - Action to "view" screens 600 to 620.
 */
public class ReportCriteriaAction extends SecureAction {

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
    ReportForm form = (ReportForm) actionForm;

    form.setReportUrl(null);
    
    if(StringUtils.isEmpty(form.getYear())) {
    	// default to program year
    	Integer py = ProgramYearUtils.getCurrentProgramYear();
    	form.setYear(py.toString());
    }

    saveToken(request);

    return forward;
  }
}
