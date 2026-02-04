package ca.bc.gov.srm.farm.ui.struts.export;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;


/**
 * ExportCriteriaAction - Action to "view" screens 700.
 */
public class ExportCriteriaAction extends SecureAction {

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
    ExportForm form = (ExportForm) actionForm;

    setYearSelectOptions(form);
    request.setAttribute("yearSelectOptions", form.getYearSelectOptions());
    
    form.setExportUrl(null);

    saveToken(request);

    return forward;
  }
  
  private void setYearSelectOptions(ExportForm form) {
    List<ListView> yearSelectOptions = new ArrayList<>();
    Integer latestAdminYear = ProgramYearUtils.getLatestAdminYear();
    final Integer earliestYear = 2023;
    
    for (Integer year = latestAdminYear; year >= earliestYear; year--) {
      ListView lv = new CodeListView(year.toString(), year.toString());
      yearSelectOptions.add(lv);
    }
   
    form.setYearSelectOptions(yearSelectOptions);
  }
  
}
