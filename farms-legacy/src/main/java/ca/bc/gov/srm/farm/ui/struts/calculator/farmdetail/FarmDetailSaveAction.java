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
package ca.bc.gov.srm.farm.ui.struts.calculator.farmdetail;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * 
 * @author awilkinson
 *
 */
public class FarmDetailSaveAction extends FarmDetailViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Farm Detail...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FarmDetailForm form = (FarmDetailForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    Scenario scenario = getScenario(form);
    
    Date oldLocalSuppDate = scenario.getLocalSupplementalReceivedDate();
    Date newLocalSuppDate = parseLocalSupplementalReceivedDate(form, errors, scenario);
    
    Date oldLocalStatementAReceivedDate = scenario.getLocalStatementAReceivedDate();
    Date newLocalStatementAReceivedDate = parseLocalStatementAReceivedDate(form, errors, scenario);
    
    Boolean newCashMargins = form.isCashMargins();
    Date newCashMarginsOptInDate = parseCashMarginsOptInDate(form);

    Boolean oldCashMargins = null;
    Date oldCashMarginsOptInDate = null;

    if(scenario.getFarmingYear() != null) {
      oldCashMargins = scenario.getFarmingYear().getIsCashMargins();
      oldCashMarginsOptInDate = scenario.getFarmingYear().getCashMarginsOptInDate();
    }

    checkReadOnly(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {

      if (errors == null || errors.isEmpty()) {
        errors = new ActionMessages();
        checkErrors(form, scenario, errors);
      }

      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
        
        FarmingYear fy = scenario.getFarmingYear();
        populateFarmingYearFromForm(form, fy);

        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        
        try {
          calculatorService.updateFarmingYear(fy, getUserId());
          
          boolean localStatementAReceivedDateChanged = ! DateUtils.equal(oldLocalStatementAReceivedDate, newLocalStatementAReceivedDate);
          boolean localSuppDateChanged = ! DateUtils.equal(oldLocalSuppDate, newLocalSuppDate);

          if (localStatementAReceivedDateChanged || localSuppDateChanged) {

            calculatorService.updateProgramYearLocalReceivedDates(scenario, newLocalStatementAReceivedDate, newLocalSuppDate, getUserId(),
                getUserEmail());
          }
          
          if (!newCashMargins.equals(oldCashMargins) || 
              !Objects.equals(newCashMarginsOptInDate, oldCashMarginsOptInDate)) {
            
            if (!newCashMargins) {
              // Cash Margins Opt In is false set cashMarginsOptInDate to null
              newCashMarginsOptInDate = null;
              form.setCashMarginsOptInDate(null);
            }

            updateAccountCashMargins(form.getPin(), newCashMargins, newCashMarginsOptInDate);

            calculatorService.saveCashMarginsInd(scenario, newCashMargins, newCashMarginsOptInDate, getUserId());
          }

          scenario = refreshScenario(form);
          populateForm(form, scenario, request);
          
          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());
          
        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

  private void updateAccountCashMargins(Integer participantPin, boolean isCashMargins, Date cashMarginsOptInDate) throws ServiceException {
    CrmRestApiDao crmRestApiDao = new CrmRestApiDao();
    
    CrmAccountResource crmAccount = null;
    if (participantPin != null) {
      crmAccount = crmRestApiDao.getAccountByPin(participantPin);
    
      crmAccount.setVsi_cashmarginsoptin(isCashMargins);
      crmAccount.setVsi_cashmarginsoptindate(cashMarginsOptInDate);
      crmRestApiDao.updateAccount(crmAccount);
    }

  }


  private Date parseLocalSupplementalReceivedDate(FarmDetailForm form, ActionMessages errors, Scenario scenario) throws ParseException {
    Date newLocalSuppDate = DataParseUtils.parseDate(form.getLocalSupplementalReceivedDate());
    
    boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(scenario.getScenarioCategoryCode());
    
    if(isRealBenefit) {
      
      boolean isInterim = ScenarioCategoryCodes.INTERIM.equals(scenario.getScenarioCategoryCode());
      if(newLocalSuppDate != null && isInterim) { 
        errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_INTERIM));
        
      } else if(scenario.getCraSupplementalReceivedDate() == null && newLocalSuppDate == null) {
        
        boolean hasSupplemental = ScenarioUtils.hasProgramYearSupplemental(scenario);
        
        if(hasSupplemental && !isInterim) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_REQUIRED));
        }
      }
    }
    
    return newLocalSuppDate;
  }


  private Date parseLocalStatementAReceivedDate(FarmDetailForm form, ActionMessages errors, Scenario scenario)
      throws ParseException {
    Date newLocalStmtADate = DataParseUtils.parseDate(form.getLocalStatementAReceivedDate());

    boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(scenario.getScenarioCategoryCode());

    if(isRealBenefit) {

      boolean isInterim = ScenarioCategoryCodes.INTERIM.equals(scenario.getScenarioCategoryCode());
      if(newLocalStmtADate != null && isInterim) {
        errors.add("",
            new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_STATEMENT_A_RECEIVED_DATE_INTERIM));

      } else if(scenario.getCraStatementAReceivedDate() == null && newLocalStmtADate == null) {

        boolean hasIncomeOrExpenses = ScenarioUtils.hasProgramYearIncomeExpenses(scenario);

        if(hasIncomeOrExpenses && !isInterim) {
          errors.add("",
              new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_PROVINCIAL_STATEMENT_A_RECEIVED_DATE_REQUIRED));
        }
      }
    }

    return newLocalStmtADate;
  }
  
  private Date parseCashMarginsOptInDate(FarmDetailForm form) throws ParseException{
    
    if (form.getCashMarginsOptInDate() == null) {
      return null;
    }
    Date cashMarinsOptInDate = DataParseUtils.parseDate(form.getCashMarginsOptInDate());

    return cashMarinsOptInDate;
  }

  private void checkErrors(FarmDetailForm form, Scenario scenario, ActionMessages errors) {
    
    boolean programYearVersionHasVerifiedBenefit = ScenarioUtils.programYearVersionHasVerifiedBenefit(scenario);
    
    if(programYearVersionHasVerifiedBenefit || scenario.isInCombinedFarm()) {
      FarmingYear farmingYear = scenario.getFarmingYear();
      String oldMunicipalityCode = farmingYear.getMunicipalityCode();
      String newMunicipalityCode = form.getMunicipalityCode();
      
      if(!StringUtils.isBlank(oldMunicipalityCode)
          && !newMunicipalityCode.equals(oldMunicipalityCode)) {
        
        if(programYearVersionHasVerifiedBenefit) {
          errors.add("", new ActionMessage(MessageConstants.INFO_FARM_DETAIL_SAVE_VERSION_HAS_VERIFIED_BENEFIT));
        } else if(scenario.isInCombinedFarm()) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_COMBINED_FARM_MUNICIPALITY_CHANGE));
        }
      }
    }
  }


  private void populateFarmingYearFromForm(FarmDetailForm form, FarmingYear fy)
      throws Exception {

    // Integers
    fy.setAgristabFedStsCode(DataParseUtils.parseIntegerObject(form.getAgristabFedStsCode()));
    fy.setCommonShareTotal(DataParseUtils.parseIntegerObject(form.getCommonShareTotal()));
    fy.setFarmYears(DataParseUtils.parseIntegerObject(form.getFarmYears()));
    fy.setRevisionCount(DataParseUtils.parseIntegerObject(form.getProgramYearVersionRevisionCount()));
    
    // Strings
    fy.setMunicipalityCode(form.getMunicipalityCode());
    fy.setParticipantProfileCode(form.getParticipantProfileCode());
    fy.setProvinceOfMainFarmstead(form.getProvinceOfMainFarmstead());
    fy.setProvinceOfResidence(form.getProvinceOfResidence());
    fy.setOtherText(form.getOtherText());
    
    // Boolean Indicators
    fy.setIsAccrualCashConversion(Boolean.valueOf(form.isAccrualCashConversion()));
    fy.setIsAccrualWorksheet(Boolean.valueOf(form.isAccrualWorksheet()));
    fy.setIsCanSendCobToRep(Boolean.valueOf(form.isCanSendCobToRep()));
    fy.setIsCombinedFarm(Boolean.valueOf(form.isCombinedFarm()));
    fy.setIsCompletedProdCycle(Boolean.valueOf(form.isCompletedProdCycle()));
    fy.setIsCoopMember(Boolean.valueOf(form.isCoopMember()));
    fy.setIsCorporateShareholder(Boolean.valueOf(form.isCorporateShareholder()));
    fy.setIsCwbWorksheet(Boolean.valueOf(form.isCwbWorksheet()));
    fy.setIsDisaster(Boolean.valueOf(form.isDisaster()));
    fy.setIsLastYearFarming(Boolean.valueOf(form.isLastYearFarming()));
    fy.setIsPartnershipMember(Boolean.valueOf(form.isPartnershipMember()));
    fy.setIsPerishableCommodities(Boolean.valueOf(form.isPerishableCommodities()));
    fy.setIsReceipts(Boolean.valueOf(form.isReceipts()));
    fy.setIsSoleProprietor(Boolean.valueOf(form.isSoleProprietor()));
    fy.setIsCashMargins(Boolean.valueOf(form.isCashMargins()));

  }

}
