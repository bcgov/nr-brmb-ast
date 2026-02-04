package ca.bc.gov.srm.farm.ui.struts.chefs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * ChefsSearchAction. Used by screen 256.
 */
public class ChefsUploadFormAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());
  CdogsService cdogsService = ServiceFactory.getCdogsService();
  CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();

  @Override
  protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    logger.debug("ChefsUploadFormAction...");

    ChefsSubmissionResultForm form = (ChefsSubmissionResultForm) actionForm;
    String submissionGuid = form.getSubmissionGuid();
    String formUserType = form.getUserFormType();
    String formType = form.getFormType();

    Map<Integer, File> participantPinFileMap = null;
    participantPinFileMap = cdogsService.createCdogsDocumentByFormType(submissionGuid, formUserType, formType);
    if (participantPinFileMap == null) {
      logger.error("participantPinFileMap is null");
      return mapping.findForward(ActionConstants.FAILURE);
    }
    Map.Entry<Integer, File> entry = participantPinFileMap.entrySet().iterator().next();
    Integer participantPin = entry.getKey();
    File file = entry.getValue();

    try {
      switch (formType) {
      case ChefsFormTypeCodes.ADJ:
      case ChefsFormTypeCodes.CM:
      case ChefsFormTypeCodes.INTERIM:
      case ChefsFormTypeCodes.STA:
      case ChefsFormTypeCodes.SUPP:
        List<String> crmEntityGuids = getCrmEntityGuids(submissionGuid);
        if (crmEntityGuids.size() > 0) {
          // get the first crmEntityGuid from list
          crmTransferService.uploadFileToNote(file, crmEntityGuids.get(0), formType);
        } else {
          logger.error("crmEntityGuids is empty with submissionGuid = {}, can not uploadFileToNote.", submissionGuid);
          return mapping.findForward(ActionConstants.FAILURE);
        }
        break;
      case ChefsFormTypeCodes.CN:
      case ChefsFormTypeCodes.NOL:
      case ChefsFormTypeCodes.NPP:
        crmTransferService.uploadFileToAccount(file, participantPin, formType);
        break;
      default:
        logger.error("Unknown ChefsFormTypeCode = {}, faile to upload", formType);
        return mapping.findForward(ActionConstants.FAILURE);
      }
    } catch (ServiceException e) {
      logger.debug(e.getMessage());
      return mapping.findForward(ActionConstants.FAILURE);
    }

    return mapping.findForward(ActionConstants.SUCCESS);
  }

  private List<String> getCrmEntityGuids(String submissionGuid) throws ServiceException {

    ChefsFormSubmissionService chefsFormSubmissionService = ServiceFactory.getChefsFormSubmissionService();
    List<ChefsSubmssnCrmEntity> crmEntities = chefsFormSubmissionService.getSubmissionCrmEntityGuidsBySubmissionGuid(submissionGuid);

    List<String> crmEntityGuids = new ArrayList<>();
    for (ChefsSubmssnCrmEntity csc : crmEntities) {
      crmEntityGuids.add(csc.getCrmEntityGuid().toString());
    }
    return crmEntityGuids;
  }

}
