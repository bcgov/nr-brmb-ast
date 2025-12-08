/**
 * Copyright (c) 2018, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.crop.unit.conversion;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author awilkinson
 */
public abstract class CropUnitConversionAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  protected void syncFilterContextWithForm(CropUnitConversionsForm form) {
    CropUnitConversionFilterContext filterContext =
      (CropUnitConversionFilterContext) CacheFactory.getUserCache().getItem(CacheKeys.CROP_UNIT_CONVERSION_FILTER_CONTEXT);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Crop Unit Conversion filter context does not exist. Creating...");
      filterContext = new CropUnitConversionFilterContext();
      CacheFactory.getUserCache().addItem(CacheKeys.CROP_UNIT_CONVERSION_FILTER_CONTEXT, filterContext);
    }
    
    if(form.isUpdateFilterContext()) {
      filterContext.setInventoryCodeFilter(form.getInventoryCodeFilter());
      filterContext.setInventoryDescFilter(form.getInventoryDescFilter());
    } else {
      form.setInventoryCodeFilter(filterContext.getInventoryCodeFilter());
      form.setInventoryDescFilter(filterContext.getInventoryDescFilter());
    }
  }

  protected void populateCropUnitConversionFormData(CropUnitConversionsForm form, CropUnitConversion cropUnitConversion) throws JsonProcessingException {
    CropUnitConversionFormData conversionFormData = new CropUnitConversionFormData();
    
    conversionFormData.setDefaultCropUnitCode(cropUnitConversion.getDefaultCropUnitCode());
    conversionFormData.setDefaultCropUnitCodeDescription(cropUnitConversion.getDefaultCropUnitCodeDescription());
    conversionFormData.setInventoryItemCode(cropUnitConversion.getInventoryItemCode());
    conversionFormData.setInventoryItemCodeDescription(cropUnitConversion.getInventoryItemCodeDescription());
    conversionFormData.setRevisionCount(StringUtils.toString(cropUnitConversion.getRevisionCount()));
    
    for (CropUnitConversionItem cropUnitConversionItem : cropUnitConversion.getConversionItems()) {
      CropUnitConversionItemFormData conversionItemFormData = new CropUnitConversionItemFormData();
      conversionFormData.getConversionItems().add(conversionItemFormData);
      
      conversionItemFormData.setConversionFactor(StringUtils.toString(cropUnitConversionItem.getConversionFactor()));
      conversionItemFormData.setTargetCropUnitCode(cropUnitConversionItem.getTargetCropUnitCode());
      conversionItemFormData.setTargetCropUnitCodeDescription(cropUnitConversionItem.getTargetCropUnitCodeDescription());
    }
    
    String cropUnitConversionJson = jsonObjectMapper.writeValueAsString(conversionFormData);
    form.setCropUnitConversionJson(cropUnitConversionJson);
  }
  
  protected void populateSelectOptions(CropUnitConversionsForm form) throws JsonProcessingException {
    List<ListView> cropUnitSelectOptions = new ArrayList<>();

    ListView[] cropUnits = (ListView[]) CacheFactory.getApplicationCache().getItem(ListService.SERVER_LIST + ListService.CROP_UNIT);
    for (ListView cropUnit : cropUnits) {
      cropUnitSelectOptions.add(cropUnit);
    }
    
    String cropUnitSelectOptionsJson = jsonObjectMapper.writeValueAsString(cropUnits);
    
    form.setCropUnitSelectOptionsJson(cropUnitSelectOptionsJson);
  }

}
