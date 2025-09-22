package ca.bc.gov.srm.farm.ui.struts.fifo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

public class FifoJobsAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("FIFO Jobs...");

    FifoJobsForm form = (FifoJobsForm) actionForm;
    ImportService service = ServiceFactory.getImportService();
    
    List<String> importTypes = new ArrayList<>();
    importTypes.add(ImportClassCodes.FIFO);
    List<ImportSearchResult> searchResults = service.searchImports(importTypes);
    form.setSearchResults(searchResults);

    return mapping.findForward(ActionConstants.SUCCESS);
  }
}
