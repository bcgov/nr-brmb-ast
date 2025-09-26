/**
 *
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

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class CropUnitConversionSaveAction extends CropUnitConversionAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Crop Unit Conversion...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CropUnitConversionsForm form = (CropUnitConversionsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    if(errors == null) {
      errors = new ActionMessages();
    }

    CropUnitConversionFormData cropUnitConversionFormData = getCropUnitConversionFromForm(form);
    checkErrors(cropUnitConversionFormData, errors);

    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CropUnitConversion cropUnitConversion = convert(cropUnitConversionFormData);
      
      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        if(form.isNew()) {
          boolean exists = checkCropUnitConversionExists(
              codesService,
              form.getInventoryItemCode());
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_EXISTS));
            saveErrors(request, errorMessages);
            populateSelectOptions(form);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
            codesService.createCropUnitConversion(cropUnitConversion, user);
          }
        } else {
          codesService.updateCropUnitConversion(cropUnitConversion, user);
        }
  
      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        populateSelectOptions(form);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

  private void checkErrors(CropUnitConversionFormData cropUnitConversionFormData, ActionMessages errors) {
    final double conversionFactorMaxValue = 9999999d;
    final double conversionFactorMinValue = 0.0000000000001;
    
    if(StringUtils.isBlank(cropUnitConversionFormData.getInventoryItemCode())) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_INVENTORY_CODE_REQUIRED));
    }
    if(StringUtils.isBlank(cropUnitConversionFormData.getDefaultCropUnitCode())) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_DEFAULT_UNIT_REQUIRED));
    }
    
    Set<String> targetCropUnitCodeSet = new HashSet<>();
    Set<String> errorSet = new HashSet<>();
    for(CropUnitConversionItemFormData conversionItem : cropUnitConversionFormData.getConversionItems()) {
      
      String targetCropUnitCode = conversionItem.getTargetCropUnitCode();
      if(StringUtils.isBlank(targetCropUnitCode)) {
        errorSet.add(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_CODE);
      } else if (targetCropUnitCodeSet.contains(targetCropUnitCode)) {
        errorSet.add(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_DUPLICATE_CODE);
      } else if (targetCropUnitCode.equals(cropUnitConversionFormData.getDefaultCropUnitCode())) {
        errorSet.add(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_FACTOR_SAME_AS_DEFAULT);
      }
      targetCropUnitCodeSet.add(targetCropUnitCode);
      
      String conversionFactorString = conversionItem.getConversionFactor();
      if(StringUtils.isBlank(conversionFactorString)) {
        errorSet.add(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_FACTOR);
      } else {
        Double conversionFactor;
        try {
          conversionFactor = DataParseUtils.parseDoubleObject(conversionFactorString);
        } catch (ParseException e) {
          conversionFactor = null;
        }

        if(conversionFactor == null
            || conversionFactor.doubleValue() < conversionFactorMinValue
            || conversionFactor.doubleValue() > conversionFactorMaxValue) {
          errorSet.add(MessageConstants.ERRORS_CROP_UNIT_CONVERSION_FACTOR);
        }
      }
    }
    
    for (String error : errorSet) {
      errors.add("", new ActionMessage(error));
    }
  }

  private boolean checkCropUnitConversionExists(
      CodesService codesService,
      final String inventoryItemCode)
      throws Exception {
    CropUnitConversion cropUnitConversion =
      codesService.getCropUnitConversion(inventoryItemCode);
    boolean exists = cropUnitConversion != null;
    return exists;
  }

  private CropUnitConversionFormData getCropUnitConversionFromForm(CropUnitConversionsForm form)
  throws Exception {
    
    String cropUnitConversionJson = form.getCropUnitConversionJson();
    
    CropUnitConversionFormData cropUnitConversionFormData = jsonObjectMapper.readValue(cropUnitConversionJson, CropUnitConversionFormData.class);
    
    return cropUnitConversionFormData;
  }

  private CropUnitConversion convert(CropUnitConversionFormData cropUnitConversionFormData) throws ParseException {
    CropUnitConversion cropUnitConversion = new CropUnitConversion();
    
    cropUnitConversion.setDefaultCropUnitCode(cropUnitConversionFormData.getDefaultCropUnitCode());
    cropUnitConversion.setDefaultCropUnitCodeDescription(cropUnitConversionFormData.getDefaultCropUnitCodeDescription());
    cropUnitConversion.setInventoryItemCode(cropUnitConversionFormData.getInventoryItemCode());
    cropUnitConversion.setInventoryItemCodeDescription(cropUnitConversionFormData.getInventoryItemCodeDescription());
    cropUnitConversion.setRevisionCount(DataParseUtils.parseIntegerObject(cropUnitConversionFormData.getRevisionCount()));
    
    for (CropUnitConversionItemFormData cropUnitConversionItemFormData : cropUnitConversionFormData.getConversionItems()) {
      CropUnitConversionItem conversionItem = new CropUnitConversionItem();
      cropUnitConversion.getConversionItems().add(conversionItem);
      
      conversionItem.setConversionFactor(DataParseUtils.parseBigDecimal(cropUnitConversionItemFormData.getConversionFactor()));
      conversionItem.setTargetCropUnitCode(cropUnitConversionItemFormData.getTargetCropUnitCode());
      conversionItem.setTargetCropUnitCodeDescription(cropUnitConversionItemFormData.getTargetCropUnitCodeDescription());
    }
    return cropUnitConversion;
  }

}
