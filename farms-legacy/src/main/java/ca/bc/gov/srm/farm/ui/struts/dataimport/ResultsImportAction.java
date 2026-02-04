package ca.bc.gov.srm.farm.ui.struts.dataimport;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportResults;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * Action for screen 230.
 */
public class ResultsImportAction extends SecureAction {

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
    ImportService service = ServiceFactory.getImportService();
    ResultsImportForm form = (ResultsImportForm) actionForm;
    Integer importVersionId = new Integer(form.getImportVersionId());
    
    String key = CacheKeys.IMPORT_RESULTS;
    Cache cache = CacheFactory.getUserCache();
    ImportResults results = (ImportResults) cache.getItem(key);
    
    //
    // This action gets called when the user switches tabs, so avoid
    // all the XML parsing if we can because it takes a long time.
    //
    if(results == null) {
    	results = service.getImportResults(importVersionId);
    } else {
    	if(!results.getImportVersion().getImportVersionId().equals(importVersionId)) {
    		results = service.getImportResults(importVersionId);
    	}
    }
    
    cache.addItem(key, results);

    return forward;
  }

}
