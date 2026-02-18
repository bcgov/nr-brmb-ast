package ca.bc.gov.srm.farm.ui.struts.chefs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.cdogs.CdogsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.forms.AdjustmentFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.CashMarginsFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.CoverageFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.InterimFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.NolFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.NppFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.StatementAFormConstants;
import ca.bc.gov.srm.farm.chefs.forms.SupplementalFormConstants;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 * ChefsSearchAction. Used by screen 256.
 */
public class ChefsSearchAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    logger.debug("List CHEFS form submission...");
    ChefsSubmissionSearchForm form = (ChefsSubmissionSearchForm) actionForm;
    List<ListView> formTypes = new ArrayList<>();
    formTypes.add(new CodeListView(ChefsFormTypeCodes.ADJ, "Adjustment"));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.CM, "Cash Margins"));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.CN, "Coverage"));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.INTERIM, "Interim"));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.NOL, ChefsFormTypeCodes.NOL));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.NPP, ChefsFormTypeCodes.NPP));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.STA, "Statement A"));
    formTypes.add(new CodeListView(ChefsFormTypeCodes.SUPP, "Supplemental"));
    form.setFormTypes(formTypes);
    
    String formType = form.getFormType();
    if (form.getFormTypes().stream().filter(o -> o.getValue().equals(form.getFormType())).findFirst().isPresent()) {
      CacheFactory.getUserCache().addItem(CacheKeys.CHEFS_SUBMISSION_FORM_TYPE_FILTER_CONTEXT, formType);
    } else {
      formType = (String) CacheFactory.getUserCache().getItem(CacheKeys.CHEFS_SUBMISSION_FORM_TYPE_FILTER_CONTEXT);
    }
    
    if (formType == null) {
      formType = ChefsFormTypeCodes.ADJ;
      CacheFactory.getUserCache().addItem(CacheKeys.CHEFS_SUBMISSION_FORM_TYPE_FILTER_CONTEXT, formType);
    }
    form.setFormType(formType);

    ChefsFormSubmissionService chefsFormSubmissionService = ServiceFactory.getChefsFormSubmissionService();
    List<ChefsSubmission> chefsSubmissions = chefsFormSubmissionService.getFormSubmissionsByType(form.getFormType());

    form.setSearchResults(chefsSubmissions);
    checkCdogsTemplateCached(form, formType);

    return mapping.findForward(ActionConstants.SUCCESS);
  }

  private void checkCdogsTemplateCached(ChefsSubmissionSearchForm form, String formType) {
    
    CdogsConfigurationUtil cdogsConfig = CdogsConfigurationUtil.getInstance();
    CdogsTemplate cdogsTemplate = null;
    List<CdogsTemplate> templates = new ArrayList<>();
    
    switch (formType) {
    case ChefsFormTypeCodes.ADJ:
      cdogsTemplate = new CdogsTemplate(AdjustmentFormConstants.FORM_SHORT_NAME, cdogsConfig.getAdjustmentTemplateGuid());
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.CM:
      cdogsTemplate = new CdogsTemplate(CashMarginsFormConstants.FORM_SHORT_NAME, cdogsConfig.getCashMarginsTemplateGuid());
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.CN:
      cdogsTemplate = new CdogsTemplate(CoverageFormConstants.FORM_SHORT_NAME, cdogsConfig.getCoverageTemplateGuid());
      templates.add(cdogsTemplate);
      cdogsTemplate = new CdogsTemplate(CoverageFormConstants.FORM_SHORT_NAME + " Report", cdogsConfig.getCoverageNoticeReportTemplateGuid());
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.INTERIM:
      cdogsTemplate = new CdogsTemplate(InterimFormConstants.FORM_SHORT_NAME, cdogsConfig.getInterimTemplateGuid(null));
      templates.add(cdogsTemplate);
      cdogsTemplate = new CdogsTemplate(InterimFormConstants.FORM_SHORT_NAME + " V2", cdogsConfig.getInterimTemplateGuid(2));
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.NOL:
      cdogsTemplate = new CdogsTemplate(NolFormConstants.FORM_SHORT_NAME, cdogsConfig.getNolTemplateGuid());
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.NPP:
      cdogsTemplate = new CdogsTemplate(NppFormConstants.FORM_SHORT_NAME, cdogsConfig.getNppTemplateGuid(null));
      templates.add(cdogsTemplate);
      cdogsTemplate = new CdogsTemplate(NppFormConstants.FORM_SHORT_NAME + " V2", cdogsConfig.getNppTemplateGuid(2));
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.STA:
      cdogsTemplate = new CdogsTemplate(StatementAFormConstants.FORM_SHORT_NAME, cdogsConfig.getStatementATemplateGuid());
      templates.add(cdogsTemplate);
      break;
    case ChefsFormTypeCodes.SUPP:
      cdogsTemplate = new CdogsTemplate(SupplementalFormConstants.FORM_SHORT_NAME, cdogsConfig.getSupplementalTemplateGuid());
      templates.add(cdogsTemplate);
      break;
    }
    
    form.setCdogsTemplates(templates);
  }
  
}
