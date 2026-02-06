/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.status;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class SaveDateReceivedAction extends CalculatorStatusViewAction {

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
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Save date received action...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    Scenario scenario = getScenario(form);

    Date oldLocalSuppDate = scenario.getLocalSupplementalReceivedDate();
    Date newLocalSuppDate = parseLocalSupplementalReceivedDate(form, errors, scenario);

    Date oldLocalStatementAReceivedDate = scenario.getLocalStatementAReceivedDate();
    Date newLocalStatementAReceivedDate = parseLocalStatementAReceivedDate(form, errors, scenario);

    checkReadOnly(request, form, scenario);

    if (scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {

      try {

        boolean localStatementAReceivedDateChanged = !DateUtils.equal(oldLocalStatementAReceivedDate, newLocalStatementAReceivedDate);
        boolean localSuppDateChanged = !DateUtils.equal(oldLocalSuppDate, newLocalSuppDate);

        if (localStatementAReceivedDateChanged || localSuppDateChanged) {

          CalculatorService calculatorService = ServiceFactory.getCalculatorService();
          calculatorService.updateProgramYearLocalReceivedDates(scenario, newLocalStatementAReceivedDate, newLocalSuppDate, getUserId(),
              getUserEmail());
        }

        scenario = refreshScenario(form);
        
        if(scenario.typeIsOneOf(ScenarioTypeCodes.USER)) {
          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());
        }

      } catch (InvalidRevisionCountException irce) {
        handleInvalidRevisionCount(request);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }

    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);

    return forward;
  }

  @Override
  protected boolean canModifyScenario(HttpServletRequest request, CalculatorForm form, Scenario scenario) throws Exception {
    boolean result;

    boolean authorizedAction = canPerformAction(request, SecurityActionConstants.EDIT_SCENARIO);

    if (authorizedAction) {
      result = true;
    } else {
      result = false;
    }
    return result;
  }

  private Date parseLocalSupplementalReceivedDate(CalculatorStatusForm form, ActionMessages errors, Scenario scenario) throws ParseException {
    Date newLocalSuppDate = DataParseUtils.parseDate(form.getLocalSupplementalReceivedDate());

    boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(scenario.getScenarioCategoryCode());

    if (isRealBenefit) {

      boolean isInterim = ScenarioCategoryCodes.INTERIM.equals(scenario.getScenarioCategoryCode());
      if (newLocalSuppDate != null && isInterim) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_INTERIM));

      } else if (scenario.getCraSupplementalReceivedDate() == null && newLocalSuppDate == null) {

        boolean hasSupplemental = ScenarioUtils.hasProgramYearSupplemental(scenario);

        if (hasSupplemental && !isInterim) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_REQUIRED));
        }
      }
    }

    return newLocalSuppDate;
  }

  private Date parseLocalStatementAReceivedDate(CalculatorStatusForm form, ActionMessages errors, Scenario scenario) throws ParseException {
    Date newLocalStmtADate = DataParseUtils.parseDate(form.getLocalStatementAReceivedDate());

    boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(scenario.getScenarioCategoryCode());

    if (isRealBenefit) {

      boolean isInterim = ScenarioCategoryCodes.INTERIM.equals(scenario.getScenarioCategoryCode());
      if (newLocalStmtADate != null && isInterim) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_STATEMENT_A_RECEIVED_DATE_INTERIM));

      } else if (scenario.getCraStatementAReceivedDate() == null && newLocalStmtADate == null) {

        boolean hasIncomeOrExpenses = ScenarioUtils.hasProgramYearIncomeExpenses(scenario);

        if (hasIncomeOrExpenses && !isInterim) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_STATEMENT_A_RECEIVED_DATE_REQUIRED));
        }
      }
    }

    return newLocalStmtADate;
  }

}
