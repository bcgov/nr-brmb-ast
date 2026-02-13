package ca.bc.gov.srm.farm.ui.struts.benefit.triage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageStatus;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.UiUtils;

public class BenefitTriageStatusAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Benefit Triage Status...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    BenefitTriageStatusForm form = (BenefitTriageStatusForm) actionForm;

    populateForm(form);

    return forward;
  }

  private void populateForm(BenefitTriageStatusForm form) throws Exception {

    if (form.getYear() == null) {
      form.setYear(ProgramYearUtils.getCurrentProgramYear());
    }

    BenefitTriageService service = ServiceFactory.getBenefitTriageService();
    List<BenefitTriageStatus> triageStatusList = service.getBenefitTriageStatusByYear(form.getYear());

    form.setStatusResults(triageStatusList);

    setProgramYearSelectOptions(form);
    setScenarioStateOptions(form);
  }

  private void setScenarioStateOptions(BenefitTriageStatusForm form) throws Exception {
    List<ListView> states = new ArrayList<>();

    states.add(new CodeListView(ScenarioStateCodes.COMPLETED, "Completed"));
    states.add(new CodeListView(ScenarioStateCodes.FAILED, "Failed"));
    states.add(new CodeListView(ScenarioStateCodes.IN_PROGRESS, "In Progress"));
    form.setScenarioStateSelectOptions(states);
  }

  private void setProgramYearSelectOptions(BenefitTriageStatusForm form) {
     
    int startYear = 2023; 
    int endYear = ProgramYearUtils.getCurrentCalendarYear() + 1; 
    int[] yearsArr = IntStream.rangeClosed(startYear, endYear).toArray();
    List<ListView> options = UiUtils.getSelectOptions(yearsArr);
    
    form.setProgramYearSelectOptions(options);
  }
}
