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
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.xref;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.InventoryXref;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class InventoryXrefSaveAction extends InventoryXrefAction {

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

    logger.debug("Saving Inventory Xref...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryXrefsForm form = (InventoryXrefsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        InventoryXref xref = getInventoryXrefFromForm(form);
        if(form.isNew()) {
          boolean exists = checkCodeExists(codesService,
              xref.getInventoryClassCode(), xref.getInventoryItemCode());
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
            codesService.createInventoryXref(xref, user);
            ServiceFactory.getListService().refreshCodeTableList(CodeTables.INVENTORY_ITEM);
          }
        } else {
          codesService.updateInventoryXref(xref, user);
          ServiceFactory.getListService().refreshCodeTableList(CodeTables.INVENTORY_ITEM);
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
   * @param codesService codesService
   * @param inventoryClassCode inventoryClassCode
   * @param inventoryItemCode inventoryItemCode
   * @return true if the code already exists in the database
   * @throws Exception On Exception
   */
  private boolean checkCodeExists(CodesService codesService,
      String inventoryClassCode,
      String inventoryItemCode)
      throws Exception {
    InventoryXref codeObj = codesService.getInventoryXref(inventoryClassCode, inventoryItemCode);
    boolean exists = codeObj != null;
    return exists;
  }

  /**
   * @param form form
   * @return InventoryXref
   * @throws Exception On Exception
   */
  private InventoryXref getInventoryXrefFromForm(InventoryXrefsForm form)
  throws Exception {

    InventoryXref xref = new InventoryXref();
    xref.setCommodityXrefId(form.getCommodityXrefId());
    xref.setInventoryItemCode(form.getInventoryItemCode());
    xref.setInventoryItemCodeDescription(form.getInventoryItemCodeDescription());
    xref.setInventoryClassCode(form.getInventoryClassCode());
    xref.setInventoryClassCodeDescription(form.getInventoryClassCodeDescription());
    xref.setInventoryGroupCode(form.getInventoryGroupCode());
    xref.setInventoryGroupCodeDescription(form.getInventoryGroupCodeDescription());
    xref.setIsMarketCommodity(Boolean.valueOf(form.isMarketCommodity()));
    xref.setRevisionCount(form.getRevisionCount());
    
    return xref;
  }

}
