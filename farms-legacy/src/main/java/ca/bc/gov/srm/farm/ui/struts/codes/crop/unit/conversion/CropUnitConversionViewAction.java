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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 */
public class CropUnitConversionViewAction extends CropUnitConversionAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Crop Unit Conversion...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CropUnitConversionsForm form = (CropUnitConversionsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  protected void populateForm(CropUnitConversionsForm form) throws Exception {

    syncFilterContextWithForm(form);
    populateSelectOptions(form);

    CodesService service = ServiceFactory.getCodesService();
    CropUnitConversion cropUnitConversion = service.getCropUnitConversion(form.getInventoryItemCode());
    
    if(cropUnitConversion != null) {
      form.setInventoryItemCode(cropUnitConversion.getInventoryItemCode());
      form.setInventoryItemCodeDescription(cropUnitConversion.getInventoryItemCodeDescription());
      form.setDefaultCropUnitCode(cropUnitConversion.getDefaultCropUnitCode());
      form.setDefaultCropUnitCodeDescription(cropUnitConversion.getDefaultCropUnitCodeDescription());
      
      populateCropUnitConversionFormData(form, cropUnitConversion);
    }
  }

}
