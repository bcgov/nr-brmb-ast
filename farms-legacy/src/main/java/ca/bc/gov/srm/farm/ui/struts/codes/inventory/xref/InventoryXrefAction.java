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
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.xref;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * @author awilkinson
 */
public abstract class InventoryXrefAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(InventoryXrefsForm form) {
    String cacheKey = getCacheKey(form.getInventoryClassCode());
    InventoryXrefsFilterContext filterContext =
      (InventoryXrefsFilterContext) CacheFactory.getUserCache().getItem(cacheKey);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Inventory Xref filter context does not exist. Creating...");
      filterContext = new InventoryXrefsFilterContext();
      CacheFactory.getUserCache().addItem(cacheKey, filterContext);
    }
    
    if(form.isSetFilterContext()) {
      filterContext.setCodeFilter(form.getCodeFilter());
      filterContext.setDescriptionFilter(form.getDescriptionFilter());
      filterContext.setClassFilter(form.getClassFilter());
      filterContext.setGroupFilter(form.getGroupFilter());
      filterContext.setIsMarketCommodityFilter(form.getIsMarketCommodityFilter());
    } else {
      form.setCodeFilter(filterContext.getCodeFilter());
      form.setDescriptionFilter(filterContext.getDescriptionFilter());
      form.setClassFilter(filterContext.getClassFilter());
      form.setGroupFilter(filterContext.getGroupFilter());
      form.setIsMarketCommodityFilter(filterContext.getIsMarketCommodityFilter());
    }
  }

  /**
   * @param inventoryClassCode inventoryClassCode
   * @return cacheKey
   */
  private String getCacheKey(String inventoryClassCode) {
    String cacheKey = null;
    if(inventoryClassCode.equals(InventoryClassCodes.CROP)) {
      cacheKey = CacheKeys.INVENTORY_CROP_XREF_FILTER_CONTEXT;
    }if(inventoryClassCode.equals(InventoryClassCodes.LIVESTOCK)) {
      cacheKey = CacheKeys.INVENTORY_LIVESTOCK_XREF_FILTER_CONTEXT;
    }if(inventoryClassCode.equals(InventoryClassCodes.INPUT)) {
      cacheKey = CacheKeys.INVENTORY_INPUT_XREF_FILTER_CONTEXT;
    } else if(inventoryClassCode.equals(InventoryClassCodes.RECEIVABLE)) {
      cacheKey = CacheKeys.INVENTORY_RECEIVABLE_XREF_FILTER_CONTEXT;
    } else if(inventoryClassCode.equals(InventoryClassCodes.PAYABLE)) {
      cacheKey = CacheKeys.INVENTORY_PAYABLE_XREF_FILTER_CONTEXT;
    }
    return cacheKey;
  }

}
