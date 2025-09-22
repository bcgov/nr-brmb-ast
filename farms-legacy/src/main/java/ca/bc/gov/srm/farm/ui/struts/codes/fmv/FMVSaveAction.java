/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.fmv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.FMV;
import ca.bc.gov.srm.farm.domain.codes.FMVPeriod;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
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
public class FMVSaveAction extends FMVAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving FMV...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FMVsForm form = (FMVsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    if(errors == null) {
      errors = new ActionMessages();
    }

    checkErrors(form, errors);

    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        FMV fmv = getFMVFromForm(form);
        if(form.isNew()) {
          boolean exists = checkFMVExists(
              codesService,
              form.getFmvYear(),
              form.getInventoryItemCode(),
              form.getMunicipalityCode(),
              form.getCropUnitCode(),
              form.getInventoryType());
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FMV_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
            codesService.createFMV(fmv, user);
          }
        } else {
          codesService.updateFMV(fmv, user);
        }
  
      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

  /**
   * @param form form
   * @param errors errors
   * @throws Exception On Exception
   */
  private void checkErrors(FMVsForm form, ActionMessages errors) throws Exception {
    final double priceMaxValue = 99999999999d;
    final double priceMinValue = 0;
    final double varianceMaxValue = 999d;
    final double varianceMinValue = 0;
    
    for(int ii = 0; ii < FMV.NUMBER_OF_PERIODS; ii++) {
      boolean hasPrice = !StringUtils.isBlank(form.getPrice(ii));
      boolean hasVariance = !StringUtils.isBlank(form.getPercentVariance(ii));
      if( (hasPrice && !hasVariance)
          || (!hasPrice && hasVariance) ) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_FMV_BOTH_PRICE_AND_VARIANCE));
        break;
      }
    }
    
    boolean priceOutOfRange = false;
    boolean varianceOutOfRange = false;
    for(int ii = 0; ii < FMV.NUMBER_OF_PERIODS; ii++) {
      if(!priceOutOfRange && !StringUtils.isBlank(form.getPrice(ii))) {
        double price = DataParseUtils.parseDouble(form.getPrice(ii));
        if(price < priceMinValue || price > priceMaxValue) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_FMV_PRICE));
        }
      }
      if(!varianceOutOfRange && !StringUtils.isBlank(form.getPercentVariance(ii))) {
        double variance = DataParseUtils.parseDouble(form.getPercentVariance(ii));
        if(variance < varianceMinValue || variance > varianceMaxValue) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_FMV_VARIANCE));
        }
      }
    }
  }

  /**
   * @param codesService codesService
   * @param year year
   * @param inventoryItemCode inventoryItemCode
   * @param municipalityCode municipalityCode
   * @param cropUnitCode cropUnitCode
   * @param inventoryType inventoryType
   * @return boolean
   * @throws Exception On Exception
   */
  private boolean checkFMVExists(
      CodesService codesService,
      Integer year,
      final String inventoryItemCode,
      final String municipalityCode,
      final String cropUnitCode,
      final String inventoryType)
      throws Exception {
    String cropUnitSearchParam = null;
    if(InventoryClassCodes.CROP.equals(inventoryType)) {
      cropUnitSearchParam = cropUnitCode;
    } else {
      cropUnitSearchParam = CropUnitCodes.getLivestockUnitCode(inventoryItemCode);
    }
    FMV fmv =
      codesService.getFMV(
          year,
          inventoryItemCode,
          municipalityCode,
          cropUnitSearchParam);
    boolean exists = fmv != null;
    return exists;
  }

  /**
   * @param form form
   * @return FMV
   * @throws Exception On Exception
   */
  private FMV getFMVFromForm(FMVsForm form)
  throws Exception {
    
    FMV fmv = new FMV();
    fmv.setProgramYear(form.getFmvYear());
    fmv.setInventoryItemCode(form.getInventoryItemCode());
    fmv.setMunicipalityCode(form.getMunicipalityCode());
    if(InventoryClassCodes.LIVESTOCK.equals(form.getInventoryType())) {
      fmv.setCropUnitCode(CropUnitCodes.getLivestockUnitCode(form.getInventoryItemCode()));
    } else {
      fmv.setCropUnitCode(form.getCropUnitCode());
    }
    
    FMVPeriod[] periods = new FMVPeriod[FMV.NUMBER_OF_PERIODS];
    for(int ii = 0; ii < FMV.NUMBER_OF_PERIODS; ii++) {
      FMVPeriod period = new FMVPeriod();
      period.setPeriod(new Integer(ii + 1));
      period.setFairMarketValueId(DataParseUtils.parseIntegerObject(form.getFairMarketValueId(ii)));
      period.setPrice(DataParseUtils.parseDoubleObject(form.getPrice(ii)));
      period.setPercentVariance(DataParseUtils.parseDoubleObject(form.getPercentVariance(ii)));
      period.setRevisionCount(DataParseUtils.parseIntegerObject(form.getRevisionCount(ii)));
      periods[ii] = period;
    }
    fmv.setPeriods(periods);
    
    return fmv;
  }

}
