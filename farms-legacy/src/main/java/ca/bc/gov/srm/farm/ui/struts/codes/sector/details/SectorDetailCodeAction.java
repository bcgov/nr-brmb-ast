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
package ca.bc.gov.srm.farm.ui.struts.codes.sector.details;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.SectorDetailCode;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.DateUtils;

public abstract class SectorDetailCodeAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  protected void populateForm(SectorDetailCodesForm form, boolean setUserInputValues) throws Exception {
    
    syncFilterContextWithForm(form);
    
    if(setUserInputValues) {
      CodesService service = ServiceFactory.getCodesService();
      SectorDetailCode code = service.getSectorDetailCode(form.getSectorDetailCode());
      
      if(code != null) {
       form.setSectorCode(code.getSectorCode());
       form.setSectorCodeDescription(code.getSectorCodeDescription());
       form.setSectorDetailCode(code.getSectorDetailCode());
       form.setSectorDetailCodeDescription(code.getSectorDetailCodeDescription());
       form.setRevisionCount(code.getRevisionCount());
       form.setMixed(code.isMixed());
      }      
    }      
  }
  
  protected SectorDetailCode getSectorDetailCodeFromForm(SectorDetailCodesForm form) {
    SectorDetailCode code = new SectorDetailCode();
    code.setSectorDetailCode(form.getSectorDetailCode());
    code.setSectorDetailCodeDescription(form.getSectorDetailCodeDescription());
    code.setExpiryDate(DateUtils.getNeverExpiresDate());
    code.setRevisionCount(form.getRevisionCount());
    
    if( ! code.isMixed() ) {
      code.setSectorCode(form.getSectorCode());
    }
    
    return code;
  }  


  protected void syncFilterContextWithForm(SectorDetailCodesForm form) {
    String cacheKey = CacheKeys.SECTOR_DETAIL_CODES_FILTER_CONTEXT;
    SectorDetailCodesFilterContext filterContext =
      (SectorDetailCodesFilterContext) CacheFactory.getUserCache().getItem(cacheKey);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Sector Detail Codes filter context does not exist. Creating...");
      filterContext = new SectorDetailCodesFilterContext();
      CacheFactory.getUserCache().addItem(cacheKey, filterContext);
    }
    
    if(form.isSetFilterContext()) {
      filterContext.setSectorCodeDescriptionFilter(form.getSectorCodeDescriptionFilter());
      filterContext.setSectorDetailCodeFilter(form.getSectorDetailCodeFilter());
      filterContext.setSectorDetailCodeDescriptionFilter(form.getSectorDetailCodeDescriptionFilter());
    } else {
      form.setSectorCodeDescriptionFilter(filterContext.getSectorCodeDescriptionFilter());
      form.setSectorDetailCodeFilter(filterContext.getSectorDetailCodeFilter());
      form.setSectorDetailCodeDescriptionFilter(filterContext.getSectorDetailCodeDescriptionFilter());
    }
  }
}
