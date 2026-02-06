package ca.bc.gov.srm.farm.ui.struts.chefs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * ChefsSearchAction. Used by screen 256.
 */
public class ChefsResultAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    logger.debug("ChefsResultAction...");
    ChefsSubmissionResultForm form = (ChefsSubmissionResultForm) actionForm;
    String submissionGuid = form.getSubmissionGuid();

    ChefsFormSubmissionService chefsFormSubmissionService = ServiceFactory.getChefsFormSubmissionService();
    CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();
    
    ChefsSubmission submission = chefsFormSubmissionService.getSubmissionByGuid(submissionGuid);

    String mainTaskGuid = submission.getMainTaskGuid();
    String validationTaskGuid = submission.getValidationTaskGuid();

    String mainTaskUrl = null;
    if (mainTaskGuid != null) {
      mainTaskUrl = crmConfig.getMainTaskAppUrl(submission.getFormTypeCode(), mainTaskGuid);
    }
    form.setMainTaskUrl(mainTaskUrl);
    
    String validationTaskUrl = null;
    if (validationTaskGuid != null) {
      validationTaskUrl = crmConfig.getValidationErrorTaskAppUrl(validationTaskGuid);
    }
    form.setValidationTaskUrl(validationTaskUrl);

    String userFormType = chefsFormSubmissionService.getUserFormType(submission);

    form.setSubmission(submission);
    form.setSubmissionStatusCode(form.getSubmission().getSubmissionStatusCode());

    ChefsConfigurationUtil chefsConfigUtil = ChefsConfigurationUtil.getInstance();
    String chefsSubmissionUrl = chefsConfigUtil.getViewSubmissionUrl(submissionGuid);
    form.setSubmissionUrl(chefsSubmissionUrl);

    if (userFormType == null) {
      userFormType = chefsFormSubmissionService.getUserFormTypeFromPreflight(submissionGuid);
      if (userFormType == null) {
        return mapping.findForward(ActionConstants.FAILURE);
      } else {
        submission.setBceidFormInd(getBceidFormIndFromUserFormType(userFormType));
        chefsFormSubmissionService.updateSubmission(submission);
      }
    }
    form.setUserFormType(userFormType);

    SubmissionWrapperResource<?> submissionWrapper = null;
    try {
      submissionWrapper = chefsFormSubmissionService.getSubmissionWrapperResource(submissionGuid, form.getFormType(), userFormType);
      if (submissionWrapper != null) {
        form.setUserFormType(userFormType);
      }
    } catch (ServiceException e) {
      logger.debug(e.getMessage());
      return mapping.findForward(ActionConstants.FAILURE);
    }
    ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
    String jsonResource = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(submissionWrapper);
    form.setResourceJson(jsonResource);

    return mapping.findForward(ActionConstants.SUCCESS);
  }

  private String getBceidFormIndFromUserFormType(String userFormType) {
    if (userFormType.equals(ChefsConstants.USER_TYPE_BASIC_BCEID)) {
      return "Y";
    }
    if (userFormType.equals(ChefsConstants.USER_TYPE_IDIR)) {
      return "N";
    }
    return null;
  }

}
