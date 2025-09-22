package ca.bc.gov.srm.farm.ui.struts.dataimport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;


/**
 * ImportAction. Used by screen 200 to start an "import".
 */
public class ImportAction extends SecureAction {

  /**
   * execute.
   *
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   *
   * @return  The return value
   *
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
  	
  	if (!isTokenValid(request)) {
  		ActionMessages errors = new ActionMessages();
  		String key = MessageConstants.ERRORS_DUPLICATE_SUBMIT;
  		errors.add("", new ActionMessage(key));
      addErrors(request, errors);
      return mapping.findForward(ActionConstants.FAILURE);
  	}
    
    ImportForm form = (ImportForm) actionForm;
    ImportService service = ServiceFactory.getImportService();

    ActionMessages errors = actionForm.validate(mapping, request);
    if(errors == null) {
      errors = new ActionMessages();
    }
    
    if(ImportClassCodes.AARM.equals(form.getImportClassCode())
        || ImportClassCodes.BCCRA.equals(form.getImportClassCode())
        || ImportClassCodes.CRA.equals(form.getImportClassCode())) {
      
      boolean canImportCraData = canPerformAction(request, SecurityActionConstants.IMPORT_CRA);
      if(!canImportCraData) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_IMPORT_TYPE_NOT_ALLOWED));
      }
      
    } else if(ImportClassCodes.BPU.equals(form.getImportClassCode())
        || ImportClassCodes.FMV.equals(form.getImportClassCode())) {
      
      boolean canImportCodes = canPerformAction(request, SecurityActionConstants.IMPORT_CODES);
      if(!canImportCodes) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_IMPORT_TYPE_NOT_ALLOWED));
      }
      
    }
    
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return mapping.findForward(ActionConstants.FAILURE);
    }

    // if OK then reset the token
  	resetToken(request);

    // create a farm_import_versions entry, and save the file to a blob
    ImportVersion importVersion = service.createImportVersion(
  		form.getImportClassCode(),
  		ImportStateCodes.SCHEDULED_FOR_STAGING,
        form.getDescription(),
        form.getFile().getFileName(),
        form.getFile().getInputStream(),
        CurrentUser.getUser().getUserId());

    String key = CacheKeys.CURRENT_IMPORT;
    CacheFactory.getUserCache().addItem(key, importVersion);

    return mapping.findForward(ActionConstants.SUCCESS);
  }
}
