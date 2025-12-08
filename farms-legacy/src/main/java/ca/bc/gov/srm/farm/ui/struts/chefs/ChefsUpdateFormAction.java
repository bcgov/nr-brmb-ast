package ca.bc.gov.srm.farm.ui.struts.chefs;

import static ca.bc.gov.srm.farm.chefs.ChefsConstants.URL_VIEW_CHEFS_SUBMISSION_FORM;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * ChefsSearchAction. Used by screen 257.
 */
public class ChefsUpdateFormAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private ConfigurationUtility configUtil = ConfigurationUtility.getInstance();

  @Override
  protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    logger.debug("ChefsUpdateFormAction... ");
    CrmRestApiDao crmDao = new CrmRestApiDao();
    CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();
    ChefsSubmissionResultForm form = (ChefsSubmissionResultForm) actionForm;
    String submissionGuid = form.getSubmissionGuid();
    String submissionStatusCode = form.getSubmissionStatusCode();

    ChefsFormSubmissionService chefsFormSubmissionService = ServiceFactory.getChefsFormSubmissionService();
    ChefsSubmission submission = chefsFormSubmissionService.getSubmissionByGuid(submissionGuid);
    submission.setSubmissionStatusCode(submissionStatusCode);

    chefsFormSubmissionService.updateSubmission(submission);

    String mainTaskGuid = submission.getMainTaskGuid();
    if (mainTaskGuid != null && submission.getFormTypeCode().equals(ChefsFormTypeCodes.NOL)
        && (submissionStatusCode.equals(ChefsSubmissionStatusCodes.CANCELLED) || submissionStatusCode.equals(ChefsSubmissionStatusCodes.SUBMITTED))) {
      crmDao.completeTask(crmConfig.getNolTaskUrl(), mainTaskGuid);
    }
    String validationTaskGuid = submission.getValidationTaskGuid();
    if (validationTaskGuid != null
        && (submissionStatusCode.equals(ChefsSubmissionStatusCodes.CANCELLED) || submissionStatusCode.equals(ChefsSubmissionStatusCodes.SUBMITTED))) {
      crmDao.completeTask(crmConfig.getValidationErrorUrl(), validationTaskGuid);
    }

    form.setSubmission(submission);
    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(submissionGuid);
    form.setSubmissionUrl(chefsSubmissionUrl.toString());

    String userFormType = chefsFormSubmissionService.getUserFormTypeFromPreflight(submissionGuid);

    if (userFormType == null) {
      logger.error("getUserFormTypeFromPreflight returned null");
      return mapping.findForward(ActionConstants.FAILURE);
    }
    form.setUserFormType(userFormType);

    SubmissionWrapperResource<?> submissionWrapper = null;
    try {
      submissionWrapper = chefsFormSubmissionService.getSubmissionWrapperResource(submissionGuid, form.getFormType(), userFormType);
      if (submissionWrapper != null) {
        form.setUserFormType(userFormType);
      }
    } catch (ServiceException e) {
      logger.error(e.getMessage());
      return mapping.findForward(ActionConstants.FAILURE);
    }
    ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
    String jsonResource = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(submissionWrapper);
    form.setResourceJson(jsonResource);

    return mapping.findForward(ActionConstants.SUCCESS);
  }

}
