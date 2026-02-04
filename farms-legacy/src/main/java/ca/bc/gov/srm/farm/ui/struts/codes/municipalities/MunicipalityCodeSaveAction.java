/**
 *
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.municipalities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import ca.bc.gov.srm.farm.domain.codes.MunicipalityCode;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;

/**
 * 
 * @author awilkinson
 *
 */
public class MunicipalityCodeSaveAction extends MunicipalityCodeAction {

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
    
    MunicipalityCodesForm form = (MunicipalityCodesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
      populateRegionCodes(form);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        MunicipalityCode code = getMunicipalityCodeFromForm(form);
        if(form.isNew()) {
          boolean exists = checkCodeExists(codesService, code.getCode());
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
            populateRegionCodes(form);
          } else {
            codesService.createMunicipalityCode(code, user);
            ServiceFactory.getListService().refreshCodeTableList(CodeTables.MUNICIPALITY);
          }
        } else {
          codesService.updateMunicipalityCode(code, user);
          ServiceFactory.getListService().refreshCodeTableList(CodeTables.MUNICIPALITY);
        }
  
      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
        populateRegionCodes(form);
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
    MunicipalityCode codeObj = codesService.getMunicipalityCode(code);
    boolean exists = codeObj != null;
    return exists;
  }

  /**
   * @param form form
   * @return MunicipalityCode
   * @throws Exception On Exception
   */
  private MunicipalityCode getMunicipalityCodeFromForm(MunicipalityCodesForm form)
  throws Exception {

    MunicipalityCode code = new MunicipalityCode();
    code.setCode(form.getCode());
    code.setDescription(form.getDescription());
    code.setEstablishedDate(new Date());
    code.setExpiryDate(DataParseUtils.parseDate(DateUtils.NEVER_EXPIRES_DATE_STRING));
    code.setRegionalOfficeCodes(new ArrayList());
    code.setRevisionCount(form.getRevisionCount());
    
    List regionCodes = new ArrayList();
    Map regionCodeSelections = form.getRegionCodeSelections();
    for(Iterator rci = regionCodeSelections.keySet().iterator(); rci.hasNext(); ) {
      String regionCode = (String) rci.next();
      Boolean selected = (Boolean) regionCodeSelections.get(regionCode);
      if(Boolean.TRUE.equals(selected)) {
        regionCodes.add(regionCode);
      }
    }
    code.setRegionalOfficeCodes(regionCodes);
    
    return code;
  }

}
