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
package ca.bc.gov.srm.farm.ui.struts.codes.municipalities;

import java.util.List;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * @author awilkinson
 */
public abstract class MunicipalityCodeAction extends SecureAction {
  
  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateRegionCodes(MunicipalityCodesForm form) throws Exception {
    
    CodesService codesService = ServiceFactory.getCodesService();
    List regionCodes = codesService.getCodes(CodeTables.REGIONAL_OFFICE);
    form.setRegionCodes(regionCodes);
  }

}
