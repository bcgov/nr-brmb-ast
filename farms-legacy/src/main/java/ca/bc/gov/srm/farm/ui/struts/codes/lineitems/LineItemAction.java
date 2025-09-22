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
package ca.bc.gov.srm.farm.ui.struts.codes.lineitems;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;

/**
 * @author awilkinson
 */
public abstract class LineItemAction extends SecureAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(LineItemsForm form) {
    LineItemsFilterContext filterContext =
      (LineItemsFilterContext) CacheFactory.getUserCache().getItem(CacheKeys.LINE_ITEMS_FILTER_CONTEXT);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Line Items filter context does not exist. Creating...");
      filterContext = new LineItemsFilterContext();
      CacheFactory.getUserCache().addItem(CacheKeys.LINE_ITEMS_FILTER_CONTEXT, filterContext);
    }
    
    Integer programYear = form.getYearFilter();
    if(programYear == null) {
      // if year is not set assume no form fields are set and set them from the context
      
      if(filterContext.getYearFilter() == null) {
        // if context fields are not set, then set the defaults
        filterContext.setYearFilter(ProgramYearUtils.getCurrentProgramYear());
      }

      programYear = filterContext.getYearFilter();
      form.setYearFilter(programYear);
      form.setCodeFilter(filterContext.getCodeFilter());
      form.setDescriptionFilter(filterContext.getDescriptionFilter());
      form.setSectorFilter(filterContext.getSectorFilter());
      form.setIsEligibleFilter(filterContext.getIsEligibleFilter());
      form.setIsEligibleRefYearsFilter(filterContext.getIsEligibleRefYearsFilter());
      form.setIsYardageFilter(filterContext.getIsYardageFilter());
      form.setIsProgramPaymentFilter(filterContext.getIsProgramPaymentFilter());
      form.setIsContractWorkFilter(filterContext.getIsContractWorkFilter());
      form.setIsSupplyManagedCommodityFilter(filterContext.getIsSupplyManagedCommodityFilter());
      form.setIsExcludeFromRevenueCalculationFilter(filterContext.getIsExcludeFromRevenueCalculationFilter());
      form.setIsIndustryAverageExpenseFilter(filterContext.getIsIndustryAverageExpenseFilter());
      form.setFruitVegCodeFilter(filterContext.getFruitVegCodeFilter());
    } else {
      // if year is set assume form fields are set and update the context from the form
      filterContext.setYearFilter(programYear);
      filterContext.setCodeFilter(form.getCodeFilter());
      filterContext.setDescriptionFilter(form.getDescriptionFilter());
      filterContext.setSectorFilter(form.getSectorFilter());
      filterContext.setIsEligibleFilter(form.getIsEligibleFilter());
      filterContext.setIsEligibleRefYearsFilter(form.getIsEligibleRefYearsFilter());
      filterContext.setIsYardageFilter(form.getIsYardageFilter());
      filterContext.setIsProgramPaymentFilter(form.getIsProgramPaymentFilter());
      filterContext.setIsContractWorkFilter(form.getIsContractWorkFilter());
      filterContext.setIsSupplyManagedCommodityFilter(form.getIsSupplyManagedCommodityFilter());
      filterContext.setIsExcludeFromRevenueCalculationFilter(form.getIsExcludeFromRevenueCalculationFilter());
      filterContext.setIsIndustryAverageExpenseFilter(form.getIsIndustryAverageExpenseFilter());
      filterContext.setFruitVegCodeFilter(form.getFruitVegCodeFilter());
    }
    
  }

  protected LineItemCode getLineItemFromForm(LineItemsForm form)
  throws Exception {
    
    LineItemCode lineItem = new LineItemCode();
    lineItem.setLineItemId(form.getLineItemId());
    lineItem.setLineItem(DataParseUtils.parseIntegerObject(form.getLineItem()));
    lineItem.setProgramYear(form.getLineItemYear());
    lineItem.setDescription(form.getDescription());
    lineItem.setIsEligible(Boolean.valueOf(form.isEligible()));
    lineItem.setIsEligibleRefYears(Boolean.valueOf(form.isEligibleRefYears()));
    lineItem.setIsYardage(Boolean.valueOf(form.isYardage()));
    lineItem.setIsProgramPayment(Boolean.valueOf(form.isProgramPayment()));
    lineItem.setIsContractWork(Boolean.valueOf(form.isContractWork()));
    lineItem.setIsSupplyManagedCommodity(Boolean.valueOf(form.isSupplyManagedCommodity()));
    lineItem.setIsExcludeFromRevenueCalculation(Boolean.valueOf(form.isExcludeFromRevenueCalculation()));
    lineItem.setIsIndustryAverageExpense(Boolean.valueOf(form.isIndustryAverageExpense()));
    lineItem.setEstablishedDate(new Date());
    lineItem.setExpiryDate(DataParseUtils.parseDate(DateUtils.NEVER_EXPIRES_DATE_STRING));
    lineItem.setRevisionCount(form.getRevisionCount());
    lineItem.setSectorDetailLineItemId(DataParseUtils.parseIntegerObject(form.getSectorDetailLineItemId()));
    lineItem.setSectorCode(form.getSectorCode());
    lineItem.setSectorDetailCode(form.getSectorDetailCode());
    lineItem.setFruitVegCodeName(form.getFruitVegTypeCode());
    lineItem.setCommodityTypeCode(form.getCommodityTypeCode());
    
    return lineItem;
  }

}
