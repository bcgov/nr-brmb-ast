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
package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 *
 */
public class BPUSaveAction extends BPUAction {

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

    logger.debug("Saving BPU...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    BPUsForm form = (BPUsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    if(errors == null) {
      errors = new ActionMessages();
    }

    checkErrors(form, errors);

    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      // repopulate form otherwise some fields are missing
      BpuUtils.populateFormForOneBPU(form, false);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {
      String user = CurrentUser.getUser().getUserId();
      CodesService service = ServiceFactory.getCodesService();
      List<BPU> bpus = service.getBPUs(form.getYearFilter());
      
      try {
        BPU bpu = getBPUFromForm(form);
        
        if(form.getNew()) {
          boolean exists = BpuUtils.checkBpuExists(
          		bpus,
              form.getInvSgCode(),
              form.getMunicipalityCode());
          
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_BPU_EXISTS));
            saveErrors(request, errorMessages);
            form.setNew(true);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
          	service.createBPU(bpu, user);
          }
        } else {
        	BPU existingBPU = BpuUtils.findById(bpus, form.getBpuId());
        	List<BPUYear> updatedYears = getUpdatedYears(existingBPU, bpu);
        	
        	if(updatedYears.size() > 0) {
        		service.updateBPUYears(updatedYears, user);
        	}
        }
      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        BpuUtils.populateFormForOneBPU(form, true);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }
  
  
  /**
   * 
   * @param oldBPU oldBPU
   * @param newBPU newBPU
   * @return updates
   */
  private List<BPUYear> getUpdatedYears(BPU oldBPU, BPU newBPU) {
  	List<BPUYear> updates = new ArrayList<>();
  	
  	for(int ii = 0; ii < BPU.NUMBER_OF_YEARS; ii++) {
  		Double oldMargin = oldBPU.getYears()[ii].getAverageMargin();
  		Double newMargin = newBPU.getYears()[ii].getAverageMargin();
  		Double oldExpense = oldBPU.getYears()[ii].getAverageExpense();
  		Double newExpense = newBPU.getYears()[ii].getAverageExpense();
  		
  		boolean marginChanged = !oldMargin.equals(newMargin);
      boolean expenseChanged = oldExpense != null && !oldExpense.equals(newExpense);
      
      if(marginChanged || expenseChanged) {
  			updates.add(newBPU.getYears()[ii]);
  		}
  	}
  	
  	return updates;
  }
  
  

  /**
   * @param form form
   * @param errors errors
   * @throws Exception On Exception
   */
  private void checkErrors(BPUsForm form, ActionMessages errors) throws Exception {
	  // Checking for Margin row errors
	  checkMarginExpenseErrors(form, errors, true);
	  
	  // Checking for Expense row errors (only when yearFilter is 2013 or later)
	  if (form.getYearFilter().intValue() >= CalculatorConfig.GROWING_FORWARD_2013) {
		  checkMarginExpenseErrors(form, errors, false);
	  }
  }
  
  /**
   * @param form form
   * @param errors errors
   * @param isMargin isMargin
   * @throws Exception On Exception
   */
  private void checkMarginExpenseErrors(BPUsForm form, ActionMessages errors, boolean isMargin) throws Exception {
	final double maxValue = 99999999999d;
    final double minValue = -maxValue;
    
    for (int ii = 0; ii < BPU.NUMBER_OF_YEARS; ii++) {
      String strAverage;
      String requiredMessage;
      String outOfRangeMessage;
      
      if (isMargin) {
        strAverage = form.getAverageMargins()[ii];
        requiredMessage = MessageConstants.ERRORS_BPU_MARGINS_REQUIRED;
        outOfRangeMessage = MessageConstants.ERRORS_BPU_MARGIN;
      } else {
        strAverage = form.getAverageExpenses()[ii];
        requiredMessage = MessageConstants.ERRORS_BPU_EXPENSES_REQUIRED;
        outOfRangeMessage = MessageConstants.ERRORS_BPU_EXPENSE;
      }

      if (StringUtils.isBlank(strAverage)) {
        errors.add("", new ActionMessage(requiredMessage));
        break;
      } else {
        double price = DataParseUtils.parseDouble(strAverage);
        
        if(price < minValue || price > maxValue) {
          errors.add("", new ActionMessage(outOfRangeMessage));
        }
      }
      
    }
    
  }
  
  
  /**
   * @param form form
   * @return BPU
   * @throws Exception On Exception
   */
  private BPU getBPUFromForm(BPUsForm form) throws Exception {
    BPU bpu = new BPU();
    
    bpu.setBpuId(form.getBpuId());
    bpu.setProgramYear(form.getYearFilter());
    bpu.setInvSgCode(form.getInvSgCode());
    bpu.setMunicipalityCode(form.getMunicipalityCode());
    
    BPUYear[] years = new BPUYear[BPU.NUMBER_OF_YEARS];
    for(int ii = 0; ii < BPU.NUMBER_OF_YEARS; ii++) {
    	years[ii] = new BPUYear();
      
    	years[ii].setBpuId(form.getBpuId());
    	years[ii].setYear(new Integer(form.getYears()[ii]));
      years[ii].setAverageMargin(DataParseUtils.parseDoubleObject(form.getAverageMargins()[ii]));
      years[ii].setAverageExpense(DataParseUtils.parseDoubleObject(form.getAverageExpenses()[ii]));
      years[ii].setRevisionCount(DataParseUtils.parseIntegerObject(form.getRevisionCounts()[ii]));
    }
    bpu.setYears(years);
    
    return bpu;
  }

}
