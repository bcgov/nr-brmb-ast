/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.structuregroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * @author hwang
 */
public abstract class StructureGroupCodeAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(StructureGroupCodesForm form) {
    String cacheKey = CacheKeys.STRUCTURE_GROUP_CODE_FILTER_CONTEXT;
    StructureGroupCodesFilterContext filterContext =
      (StructureGroupCodesFilterContext) CacheFactory.getUserCache().getItem(cacheKey);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Structure Group Codes filter context does not exist. Creating...");
      filterContext = new StructureGroupCodesFilterContext();
      CacheFactory.getUserCache().addItem(cacheKey, filterContext);
    }

    if(form.isSetFilterContext()) {
      filterContext.setCodeFilter(form.getCodeFilter());
      filterContext.setDescriptionFilter(form.getDescriptionFilter());
    } else {
      form.setCodeFilter(filterContext.getCodeFilter());
      form.setDescriptionFilter(filterContext.getDescriptionFilter());
    }
  }

}
