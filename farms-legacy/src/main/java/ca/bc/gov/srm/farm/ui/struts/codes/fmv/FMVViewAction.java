/**
 * Copyright (c) 2006, 
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

import ca.bc.gov.srm.farm.domain.codes.FMV;
import ca.bc.gov.srm.farm.domain.codes.FMVPeriod;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 *
 * @author awilkinson
 */
public class FMVViewAction extends FMVAction {
  
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

    logger.debug("Viewing FMV...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FMVsForm form = (FMVsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(FMVsForm form) throws Exception {

    syncFilterContextWithForm(form);

    CodesService service = ServiceFactory.getCodesService();
    FMV fmv =
      service.getFMV(
          form.getYearFilter(),
          form.getInventoryItemCode(),
          form.getMunicipalityCode(),
          form.getCropUnitCode());
    if(fmv != null) {
      form.setFmvYear(fmv.getProgramYear());
      form.setInventoryItemCode(fmv.getInventoryItemCode());
      form.setInventoryItemCodeDescription(fmv.getInventoryItemCodeDescription());
      form.setMunicipalityCode(fmv.getMunicipalityCode());
      form.setMunicipalityCodeDescription(fmv.getMunicipalityCodeDescription());
      form.setCropUnitCode(fmv.getCropUnitCode());
      form.setCropUnitCodeDescription(fmv.getCropUnitCodeDescription());
      form.setDefaultCropUnitCode(fmv.getDefaultCropUnitCode());
      form.setDefaultCropUnitCodeDescription(fmv.getDefaultCropUnitCodeDescription());
      
      FMVPeriod[] periods = fmv.getPeriods();
      for(int ii = 0; ii < periods.length; ii++) {
        FMVPeriod period = periods[ii];

        if(period != null) {
          form.setFairMarketValueId(ii, StringUtils.toString(period.getFairMarketValueId()));
          form.setPrice(ii, StringUtils.toString(period.getPrice()));
          form.setPercentVariance(ii, StringUtils.toString(period.getPercentVariance()));
          form.setRevisionCount(ii, StringUtils.toString(period.getRevisionCount()));
        }
      }
    }
  }

}
