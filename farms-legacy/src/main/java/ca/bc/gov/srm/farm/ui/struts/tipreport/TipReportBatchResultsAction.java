package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportResults;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.dataimport.ResultsImportForm;

/**
 * Action for screen 230.
 */
public class TipReportBatchResultsAction extends SecureAction {

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
    // Avoid reloading the import version if viewing the same one
    // all the XML parsing if we can because it takes a long time.
    //
    if(results == null) {
    	results = service.getImportResults(importVersionId);
    } else {
    	if(!results.getImportVersion().getImportVersionId().equals(importVersionId)) {
    		results = service.getImportResults(importVersionId);
    	}
    }
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    Path tipReportBatchFile = tipReportService.getTipReportZipFile();
    form.setHasFileForDownload(Files.exists(tipReportBatchFile));
    
    cache.addItem(key, results);

    return forward;
  }

}
