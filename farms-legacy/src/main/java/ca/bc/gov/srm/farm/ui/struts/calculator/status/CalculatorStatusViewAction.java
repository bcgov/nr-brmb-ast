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
package ca.bc.gov.srm.farm.ui.struts.calculator.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 21, 2010
 */
public class CalculatorStatusViewAction extends CalculatorAction {
  
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

    logger.debug("Viewing Calculator Status...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    Scenario scenario = getScenario(form);
    
    /** retain inbox filter selections */
    syncInboxContextWithForm(form);

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);
    
    if(ScenarioUtils.hasAccrualAccountingOperations(scenario)) {
      ActionMessages errorMessages = new ActionMessages();
      errorMessages.add("",
          new ActionMessage(MessageConstants.ERRORS_SCENARIO_HAS_ACCRUAL_ACCOUNTING_OPERATIONS));
      saveErrors(request, errorMessages);
    }

    return forward;
  }

  /**
   * Fill the form fields from the account
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception Exception
   */
  protected void populateForm(CalculatorStatusForm form, Scenario scenario) throws Exception {
    
    CalculatorService calcService = ServiceFactory.getCalculatorService();
    Integer inProgressCombinedFarmNumber = calcService.getInProgressCombinedFarmNumber(
        scenario.getClient().getParticipantPin(),
        scenario.getYear());
    
    form.setInProgressCombinedFarmNumber(StringUtils.toString(inProgressCombinedFarmNumber));
    
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    form.setAssignedToCurrentUser(isAssignedToCurrentUser(scenario));
    populateFormRefYears(form, scenario);

    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getIsNonParticipant() != null) {
      form.setNonParticipant(scenario.getFarmingYear().getIsNonParticipant().booleanValue());
    }
    
    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getIsLateParticipant() != null) {
      form.setLateParticipant(scenario.getFarmingYear().getIsLateParticipant().booleanValue());
    }
    
    if (scenario.getFarmingYear() != null) {
      if (scenario.getFarmingYear().getCashMarginsOptInDate() != null) {
        form.setCashMarginsOptInDate(scenario.getFarmingYear().getCashMarginsOptInDate());
      }

      if (scenario.getFarmingYear().getIsCashMargins() != null) {
        form.setCashMargins(scenario.getFarmingYear().getIsCashMargins().booleanValue());
      }
    }
    
    boolean lateParticipantEnabled = CalculatorConfig.lateParticipantEnabled(scenario.getYear());
    form.setLateParticipantEnabled(lateParticipantEnabled);
    
    populateYearScenarioMap(form, scenario);
    
    Map<Integer, Boolean> taxYearDataGenerated = new HashMap<>();
    form.setTaxYearDataGenerated(taxYearDataGenerated);
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      taxYearDataGenerated.put(refScenario.getYear(), isTaxDataGenerated(refScenario));
    }
    
    
    Map<Integer, Boolean> newBaseDataArrived = new HashMap<>();
    form.setNewBaseDataArrived(newBaseDataArrived);
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      newBaseDataArrived.put(refScenario.getYear(), ScenarioUtils.hasNewBaseDataArrived(refScenario));
    }
    
    
    boolean hasProgramYearSupplemental = ScenarioUtils.hasProgramYearSupplemental(scenario);
    form.setHasProgramYearSupplemental(hasProgramYearSupplemental);
    
    boolean hasCraProgramYearSupplemental = ScenarioUtils.hasCraProgramYearSupplemental(scenario);
    form.setHasCraProgramYearSupplemental(hasCraProgramYearSupplemental);
    
    int prevYear = scenario.getYear().intValue() - 1;
    
    boolean previousYearVerified = false;
    for(ScenarioMetaData meta : scenario.getScenarioMetaDataList()) {
      if(meta.getProgramYear().intValue() == prevYear
          && meta.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)
          && meta.getScenarioStateCode().equals(ScenarioStateCodes.VERIFIED)) {
        previousYearVerified = true;
      }
    }
    form.setPreviousYearVerified(previousYearVerified);

    
    boolean previousYearCombinedWholeFarm = false;
    for(ReferenceScenario refScenario : scenario.getReferenceScenarios()) {
      if(refScenario.getYear().intValue() == prevYear) {
        previousYearCombinedWholeFarm = refScenario.getFarmingYear().getIsCombinedFarm().booleanValue();
        break;
      }
    }
    form.setPreviousYearCombinedWholeFarm(previousYearCombinedWholeFarm);
    
    
    boolean bpuSetComplete = ScenarioUtils.isBpuSetComplete(scenario);
    form.setBpuSetComplete(bpuSetComplete);

    boolean fmvSetComplete = ScenarioUtils.isFmvSetComplete(scenario);
    form.setFmvSetComplete(fmvSetComplete);
    
    form.setLocalStatementAReceivedDate(DateUtils.formatDateForDatePicker(scenario.getLocalStatementAReceivedDate()));
    form.setLocalSupplementalReceivedDate(DateUtils.formatDateForDatePicker(scenario.getLocalSupplementalReceivedDate()));
    
  }

  
  /**
   * @param refScenario ReferenceScenario
   * @return Boolean
   */
  private Boolean isTaxDataGenerated(ReferenceScenario refScenario) {
    Boolean taxDataGenerated = Boolean.FALSE;
    if(refScenario.getFarmingYear() != null
        && refScenario.getFarmingYear().getProgramYearVersionId() != null) {
      Integer year = refScenario.getYear();
      Integer pyv = refScenario.getFarmingYear().getProgramYearVersionNumber();
      List<ScenarioMetaData> scenarioMetaDataList = refScenario.getParentScenario().getScenarioMetaDataList();
      for(ScenarioMetaData meta : scenarioMetaDataList) {
        if(year.equals(meta.getProgramYear())
            && pyv.equals(meta.getProgramYearVersion())
            && (meta.getScenarioTypeCode().equals(ScenarioTypeCodes.GEN) || meta.getScenarioTypeCode().equals(ScenarioTypeCodes.LOCAL))) {
          taxDataGenerated = Boolean.TRUE;
          break;
        }
      }
    }
    return taxDataGenerated;
  }

  /**
   * @param form form
   * @param scenario Scenario
   */
  private void populateFormRefYears(CalculatorStatusForm form, Scenario scenario) {
    final int numRefYears = 5;
    List<Integer> refYears = new ArrayList<>(numRefYears);
    int year = scenario.getYear().intValue();
    for(int ii = 0; ii < numRefYears; ii++) {
      refYears.add(new Integer(--year));
    }
    form.setRefYears(refYears);
  }

  /**
   * @param request request
   * @param form form
   * @param scenario Scenario
   * @return boolean
   * @throws Exception On Exception
   */
  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean readOnly;

    if(scenario.getFarmingYear() == null
        || scenario.getFarmingYear().getProgramYearVersionId() == null) {
      readOnly = false;
    } else {
      readOnly = super.isReadOnly(request, form, scenario);
    }
    
    return readOnly;
  }

}
