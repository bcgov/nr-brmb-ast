package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportJobTypes;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.dataimport.ImportSearchForm;

public class TipReportBatchSearchAction extends SecureAction {

  /**
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   * @return  The return value
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward doExecute(final ActionMapping mapping,
    final ActionForm actionForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    ImportSearchForm form = (ImportSearchForm) actionForm;
    ImportService service = ServiceFactory.getImportService();
    
    form.setJobType(ImportJobTypes.TIP_REPORT);

    List<String> importTypes = new ArrayList<>();
    importTypes.add(ImportClassCodes.TIP_REPORT);
    List<ImportSearchResult> searchResults = service.searchImports(importTypes);
    form.setSearchResults(searchResults);

    return mapping.findForward(ActionConstants.SUCCESS);
  }

}
