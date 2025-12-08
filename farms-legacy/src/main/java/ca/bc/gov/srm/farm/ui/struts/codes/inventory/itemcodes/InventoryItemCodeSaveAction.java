/**
 *
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.itemcodes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang.ObjectUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemCode;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemDetail;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * 
 * @author awilkinson
 *
 */
public class InventoryItemCodeSaveAction extends InventoryItemCodeViewAction {

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

    logger.debug("Saving Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryItemCodesForm form = (InventoryItemCodesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    populateForm(form, false);

    ActionMessages errorMessages = new ActionMessages();
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        InventoryItemCode code = getInventoryItemCodeFromForm(form);
        List<InventoryItemDetail> detailsList = getInventoryItemDetailsFromForm(form);
        
        List<InventoryItemDetail> detailsAddList = new ArrayList<>();
        List<InventoryItemDetail> detailsUpdateList = new ArrayList<>();
        
        for(InventoryItemDetail detail : detailsList) {
          try {
            if (detail.getLineItem() != null && !detail.getLineItem().isEmpty()) {
              Integer lineItem = Integer.parseInt(detail.getLineItem());
              boolean exists = codesService.checkLineItemExistsForProgramYear(detail.getProgramYear(), lineItem);
              
              if (!exists) {
                errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_LINE_ITEM_DOES_NOT_EXIST_FOR_PROGRAM_YEAR, detail.getProgramYear().toString()));
                forward = mapping.findForward(ActionConstants.FAILURE);
                saveErrors(request, errorMessages);
              }
            }
          } catch (NumberFormatException e) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_LINE_ITEM_NOT_INTEGER, detail.getProgramYear().toString()));
            forward = mapping.findForward(ActionConstants.FAILURE);
            saveErrors(request, errorMessages);
          }
          if(detail.getInventoryItemDetailId() == null) {
            detailsAddList.add(detail);
          } else {
            detailsUpdateList.add(detail);
          }
        }
 
        if(form.isNew()) {
          if(detailsUpdateList.size() > 0) {
            throw new IllegalStateException("Logic error: detailsUpdateList is expected to be empty when adding a code.");
          }
          boolean exists = checkCodeExists(codesService, code.getCode());
          if(exists) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else if (errorMessages.isEmpty()) {
            codesService.createInventoryItemCode(code, detailsAddList, user);
            ServiceFactory.getListService().refreshCodeTableList(CodeTables.INVENTORY_ITEM);
          }
        } else if (errorMessages.isEmpty()) {
          codesService.updateInventoryItemCode(code, detailsAddList, detailsUpdateList, user);
          ServiceFactory.getListService().refreshCodeTableList(CodeTables.INVENTORY_ITEM);
        }
  
      } catch(InvalidRevisionCountException irce) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

  /**
   * @param codesService codesService
   * @param code code
   * @return true if the code already exists in the database
   * @throws Exception On Exception
   */
  private boolean checkCodeExists(CodesService codesService, String code)
      throws Exception {
    InventoryItemCode codeObj = codesService.getInventoryItemCode(code);
    boolean exists = codeObj != null;
    return exists;
  }

  /**
   * @param form form
   * @return InventoryItemCode
   * @throws Exception On Exception
   */
  private InventoryItemCode getInventoryItemCodeFromForm(InventoryItemCodesForm form)
  throws Exception {

    InventoryItemCode code = new InventoryItemCode();
    code.setCode(form.getCode());
    code.setDescription(form.getDescription());
    code.setRollupInventoryItemCode(form.getRollupInventoryItemCode());
    code.setRollupInventoryItemCodeDescription(form.getRollupInventoryItemCodeDescription());
    code.setEstablishedDate(new Date());
    code.setExpiryDate(DataParseUtils.parseDate(DateUtils.NEVER_EXPIRES_DATE_STRING));
    code.setRevisionCount(form.getRevisionCount());
    
    return code;
  }
  
  /**
   * @param form form
   * @return InventoryItemCode
   * @throws Exception On Exception
   */
  private List<InventoryItemDetail> getInventoryItemDetailsFromForm(InventoryItemCodesForm form)
      throws Exception {
    
    List<InventoryItemDetail> result = new ArrayList<>();
    
    Map<String, InventoryDetailFormData> detailsFormData = form.getDetails();   
    
    for(String yearString : detailsFormData.keySet()) {
      InventoryDetailFormData fd = detailsFormData.get(yearString);
      boolean addOrUpdate =
          StringUtils.isBlank(fd.getInventoryItemDetailId())
          || fd.isEligible() != fd.isEligibleOriginal()
          || StringUtils.notEqualNullSameAsEmpty(fd.getFruitVegCodeName(), fd.getFruitVegCodeNameOriginal())
          || StringUtils.notEqualNullSameAsEmpty(fd.getLineItem(), fd.getOriginalLineItem())
          || !ObjectUtils.equals(fd.getInsurableValue(), fd.getInsurableValueOriginal())
          || !ObjectUtils.equals(fd.getPremiumRate(), fd.getPremiumRateOriginal());
      
      if(addOrUpdate) {
      
        Integer year = Integer.valueOf(yearString);
        InventoryItemDetail detail = new InventoryItemDetail();
        result.add(detail);
        
        detail.setInventoryItemCode(form.getCode());
        detail.setProgramYear(year);
        detail.setIsEligible(Boolean.valueOf(fd.isEligible()));
        detail.setInventoryItemDetailId(DataParseUtils.parseIntegerObject(fd.getInventoryItemDetailId()));
        detail.setRevisionCount(DataParseUtils.parseIntegerObject(fd.getRevisionCount()));
        detail.setFruitVegCodeName(fd.getFruitVegCodeName());
        detail.setCommodityTypeCodeName(fd.getCommodityTypeCodeName());
        detail.setLineItem(fd.getLineItem());
        detail.setInsurableValue(fd.getInsurableValue());
        detail.setPremiumRate(fd.getPremiumRate());
        
      }
    }
    
    return result;
  }

}
