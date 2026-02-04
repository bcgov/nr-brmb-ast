/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.transfer;

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
import ca.bc.gov.srm.farm.domain.codes.ImportJobTypes;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.dataimport.ImportSearchForm;

/**
 * TransferSearchAction. Used by screen 252.
 */
public class TransferSearchAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

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

    logger.debug("Searching Transfers...");

    ImportSearchForm form = (ImportSearchForm) actionForm;
    ImportService service = ServiceFactory.getImportService();
    
    form.setJobType(ImportJobTypes.TRANSFER);

    List<String> importTypes = new ArrayList<>();
    importTypes.add(ImportClassCodes.XENROL);
    importTypes.add(ImportClassCodes.XCONTACT);
    importTypes.add(ImportClassCodes.XSTATE);
    List<ImportSearchResult> searchResults = service.searchImports(importTypes);
    form.setSearchResults(searchResults);

    return mapping.findForward(ActionConstants.SUCCESS);
  }

}
