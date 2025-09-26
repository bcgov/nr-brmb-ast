/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.sectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.SectorCode;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.codes.inventory.itemcodes.InventoryItemCodesFilterContext;
import ca.bc.gov.srm.farm.util.DateUtils;

public abstract class SectorCodeAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  protected void populateForm(SectorCodesForm form) throws Exception {
    
    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();
    SectorCode code = service.getSectorCode(form.getCode());
    
    if(code != null) {
     form.setDescription(code.getDescription());
     form.setRevisionCount(code.getRevisionCount());
    }      
  }
  
  protected SectorCode getSectorCodeFromForm(SectorCodesForm form) {
    SectorCode code = new SectorCode();
    code.setCode(form.getCode());
    code.setDescription(form.getDescription());
    code.setExpiryDate(DateUtils.getNeverExpiresDate());
    code.setRevisionCount(form.getRevisionCount());
    return code;
  }  


  protected void syncFilterContextWithForm(SectorCodesForm form) {
    String cacheKey = CacheKeys.SECTOR_CODES_FILTER_CONTEXT;
    InventoryItemCodesFilterContext filterContext =
      (InventoryItemCodesFilterContext) CacheFactory.getUserCache().getItem(cacheKey);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Sector Codes filter context does not exist. Creating...");
      filterContext = new InventoryItemCodesFilterContext();
      CacheFactory.getUserCache().addItem(cacheKey, filterContext);
    }
    
    if(form.isSetFilterContext()) {
      filterContext.setCodeFilter(form.getSectorCodeFilter());
      filterContext.setDescriptionFilter(form.getSectorCodeDescriptionFilter());
    } else {
      form.setSectorCodeFilter(filterContext.getCodeFilter());
      form.setSectorCodeDescriptionFilter(filterContext.getDescriptionFilter());
    }
  }
}
