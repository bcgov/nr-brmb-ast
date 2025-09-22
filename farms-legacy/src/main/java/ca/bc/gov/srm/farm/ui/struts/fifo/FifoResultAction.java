package ca.bc.gov.srm.farm.ui.struts.fifo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.fifo.FifoResults;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportResults;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

public class FifoResultAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("FIFO result...");

    FifoResultForm form = (FifoResultForm) actionForm;
    logger.debug("importVersionId " + form.toString());
    Integer importVersionId = new Integer(form.getImportVersionId());
    String key = CacheKeys.IMPORT_RESULTS;
    Cache cache = CacheFactory.getUserCache();
    ImportResults results = (ImportResults) cache.getItem(key);
    
    ImportService service = ServiceFactory.getImportService();
    if(results == null) {
      results = service.getImportResults(importVersionId);
    } else {
      if(!results.getImportVersion().getImportVersionId().equals(importVersionId)) {
        results = service.getImportResults(importVersionId);
      }
    }
    
    cache.addItem(key, results);
    
    ImportVersion iv = service.getImportVersion(importVersionId);
    CacheFactory.getUserCache().addItem(CacheKeys.IMPORT_VERSION, iv);

    FifoResults result = service.getFifoResults(iv);
    form.setFifoResults(result);

    return mapping.findForward(ActionConstants.SUCCESS);
  }

}
