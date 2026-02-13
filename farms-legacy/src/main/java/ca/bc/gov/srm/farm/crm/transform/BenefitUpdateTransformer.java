/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.transform;

import static ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenefitCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitUpdateResource;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class BenefitUpdateTransformer {

  private static Logger logger = LoggerFactory.getLogger(BenefitUpdateTransformer.class);
  private static final char COMBINED_FARM_PINS_SCHEDULE_DELIMITER = ';';
  private static final char COMBINED_FARM_PINS_TRANSFER_DELIMITER = '\n';
  
  private static final String FARM_TYPE_DETAILED_SCHEDULE_DELIMITER = ";";
  private static final String FARM_TYPE_DETAILED_TRANSFER_DELIMITER = ",";

  private static final String DOLLAR_FORMAT_STRING = "0.00";

  public String generateCsv(
      Scenario scenario,
      Date stateChangeDate,
      String userEmail,
      String chefsFormNotes,
      String formUserType,
      String chefsFormType,
      String benefitTriageResultType) throws ServiceException {
    logMethodStart(logger);
    
    DecimalFormat dollarFormat = new DecimalFormat(DOLLAR_FORMAT_STRING);
    
    Integer pin = scenario.getClient().getParticipantPin();
    int programYear = scenario.getYear();
    Date fileStartDate = scenario.getProgramYearWhenCreatedTimestamp();
    
    String stateCode = scenario.getScenarioStateCode();
    String categoryCode = scenario.getScenarioCategoryCode();

    String stateChangeDateString = DateUtils.formatDateTime(stateChangeDate);
    Date craSupplementalReceivedDate = scenario.getCraSupplementalReceivedDate();
    String craSupplementalReceivedDateString = DateUtils.formatDateTime(craSupplementalReceivedDate);
    Date localSupplementalReceivedDate = scenario.getLocalSupplementalReceivedDate();
    String localSupplementalReceivedDateString = DateUtils.formatDateTime(localSupplementalReceivedDate);
    Date craStatementAReceivedDate = scenario.getCraStatementAReceivedDate();
    String craStatementAReceivedDateString = DateUtils.formatDateTime(craStatementAReceivedDate);
    Date localStatementAReceivedDate = scenario.getLocalStatementAReceivedDate();
    String localStatementAReceivedDateString = DateUtils.formatDateTime(localStatementAReceivedDate);
    String fileStartDateString = DateUtils.formatDateTime(fileStartDate);
    String submissionGuidString = scenario.getChefsSubmissionGuid();
    
    boolean lateParticipant = scenario.isLateParticipant();
    String lateParticipantString = getIndicatorString(lateParticipant);
    
    String benefitAmountString = "";
    String allocatedReferenceMarginString = "";
    String scenarioNumberString = "";
    String interimBenefitPercentString = "";
    String negativeMarginDeclineString = "";
    String negativeMarginBenefitString = "";
    String prodInsurDeemedBenefitString = "";
    String bcFundedBenefitAmountString = "";
    String combinedFarmsTotalString = "";
    String lateEnrolmentPenaltyAmountString = "";
    String verifierEmail = userEmail;
    
    if (scenario.getVerifiedByEmail() != null) {
      verifierEmail = scenario.getVerifiedByEmail();
    }
    
    if(scenario.stateIsOneOf(VERIFIED, PRE_VERIFIED, IN_PROGRESS)) {
      scenarioNumberString = scenario.getScenarioNumber().toString();
    }
    
    if(stateCode.equals(VERIFIED)) {
      Benefit benefit = scenario.getFarmingYear().getBenefit();
      Double benefitAmount;
      if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
        benefitAmount = benefit.getStandardBenefit();
        
        Double bcFundedBenefitAmount = benefit.getEnhancedAdditionalBenefit();
        bcFundedBenefitAmountString = StringUtils.formatDouble(bcFundedBenefitAmount, dollarFormat);
      } else {
        benefitAmount = benefit.getTotalBenefit();
      }
      
      if(programYear >= CalculatorConfig.GROWING_FORWARD_2018) {
        if(benefitAmount != null && benefitAmount.doubleValue() < CalculatorConfig.MIN_BENEFIT) {
          benefitAmount = Double.valueOf(0);
        }
        
        if(lateParticipant) {
          Double lateEnrolmentPenalty;
          
          if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
            if(scenario.isInCombinedFarm()) {
              lateEnrolmentPenalty = benefit.getEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent();
            } else {
              lateEnrolmentPenalty = benefit.getEnhancedLateEnrolmentPenalty();
            }
          } else {
            if(scenario.isInCombinedFarm()) {
              lateEnrolmentPenalty = benefit.getLateEnrolmentPenaltyAfterAppliedBenefitPercent();
            } else {
              lateEnrolmentPenalty = benefit.getLateEnrolmentPenalty();
            }
          }
          
          lateEnrolmentPenaltyAmountString = StringUtils.formatDouble(lateEnrolmentPenalty, dollarFormat);
        }
      }
      
      Double allocatedReferenceMargin = benefit.getAllocatedReferenceMargin();
      Double negativeMarginDecline = benefit.getNegativeMarginDecline();
      Double prodInsurDeemedBenefit = benefit.getProdInsurDeemedBenefit();
      Double enhancedRefMarginForBenefitCalc = benefit.getEnhancedReferenceMarginForBenefitCalculation();
      benefitAmountString = StringUtils.formatDouble(benefitAmount, dollarFormat);
      negativeMarginDeclineString = StringUtils.formatDouble(negativeMarginDecline, dollarFormat);
      prodInsurDeemedBenefitString = StringUtils.formatDouble(prodInsurDeemedBenefit, dollarFormat);
      
      Double negativeMarginBenefit = benefit.getNegativeMarginBenefit();
      if (prodInsurDeemedBenefit != null) {
        BenefitCalculator benefitCalculator = CalculatorFactory.getBenefitCalculator(scenario);
        double prodInsuranceDeduction = benefitCalculator.calculateProductionInsuranceDeduction(benefit, programYear, false);
        negativeMarginBenefit = Double.valueOf(negativeMarginBenefit.doubleValue() - prodInsuranceDeduction);
      }
      negativeMarginBenefitString = StringUtils.formatDouble(negativeMarginBenefit, dollarFormat);
      
      if(lateParticipant) {
        allocatedReferenceMarginString = StringUtils.formatDouble(enhancedRefMarginForBenefitCalc, dollarFormat);
      } else {
        allocatedReferenceMarginString = StringUtils.formatDouble(allocatedReferenceMargin, dollarFormat);
      }
      
      if(categoryCode.equals(ScenarioCategoryCodes.INTERIM)) {
        double ratio = benefit.getInterimBenefitPercent().doubleValue();
        final int hundred = 100;
        double percentage = ratio * hundred;
        interimBenefitPercentString = String.valueOf(percentage);
      }

      Double combinedFarmsTotal;
      if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
        combinedFarmsTotal = benefit.getEnhancedTotalBenefit();
      } else {
        combinedFarmsTotal = benefit.getTotalBenefit();
      }
      combinedFarmsTotalString = StringUtils.formatDouble(combinedFarmsTotal, dollarFormat);
    }
    
    boolean expectingPayment = false;
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    List<ScenarioMetaData> scenarioMetaDataList = scenario.getScenarioMetaDataList();
    List<ScenarioMetaData> triageScenariosMetaData = ScenarioUtils.findCompletedTriageScenarios(scenarioMetaDataList, programYear);
    
    for (ScenarioMetaData smd : triageScenariosMetaData) {
      Scenario triageScenario = calculatorService.loadScenario(pin, programYear, smd.getScenarioNumber());
      Benefit benefit = triageScenario.getFarmingYear().getBenefit();
      if (benefit != null && benefit.getTotalBenefit() > 1.0) {
        expectingPayment = true;
        break;
      }
    }
    String expectingPaymentString = getIndicatorString(expectingPayment);
    
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    String inPartnershipString = getIndicatorString(inPartnership);

    boolean bpuSetComplete = ScenarioUtils.isBpuSetComplete(scenario);
    String bpuSetCompleteString = getIndicatorString(bpuSetComplete);

    boolean fmvSetComplete = ScenarioUtils.isFmvSetComplete(scenario);
    String fmvSetCompleteString = getIndicatorString(fmvSetComplete);
    
    boolean inCombinedFarm = scenario.isInCombinedFarm();
    String inCombinedFarmString = getIndicatorString(inCombinedFarm);
    String combinedFarmPinsString = "";
    if(inCombinedFarm) {
      combinedFarmPinsString = getCombinedFarmPinsString(scenario);
    }
    
    String municipalityDescription = scenario.getFarmingYear().getMunicipalityCodeDescription();
    Boolean programYearNonParticipant = scenario.getFarmingYear().getIsNonParticipant();
    String programYearNonParticipantString = getIndicatorString(programYearNonParticipant);
    
    Boolean sendCopyToContactPerson = scenario.getFarmingYear().getIsCanSendCobToRep();
    String sendCopyToContactPersonString = getIndicatorString(sendCopyToContactPerson);
    
    boolean cashMarginsOptIn = scenario.getFarmingYear().getIsCashMargins();
    String cashMarginsOptInString = getIndicatorString(cashMarginsOptIn);
    
    Date cashMarginsOptInDate = scenario.getFarmingYear().getCashMarginsOptInDate();
    String cashMarginsOptInDateString = DateUtils.formatDateTime(cashMarginsOptInDate);
    
    String oldFarmTypeDetailCodeDescription = null;
    List<String> farmTypeDetailedCodeList = scenario.getFarmTypeDetailedCodes();
    String farmTypeDetailedCodesString = String.join(FARM_TYPE_DETAILED_SCHEDULE_DELIMITER, farmTypeDetailedCodeList);

    StringBuilder sb = new StringBuilder();
    sb.append(convertForCsv(pin));
    sb.append(",");
    sb.append(convertForCsv(scenario.getYear()));
    sb.append(",");
    sb.append(convertForCsv(stateCode));
    sb.append(",");
    sb.append(convertForCsv(stateChangeDateString));
    sb.append(",");
    sb.append(convertForCsv(verifierEmail));
    sb.append(",");
    sb.append(convertForCsv(craSupplementalReceivedDateString));
    sb.append(",");
    sb.append(convertForCsv(fileStartDateString));
    sb.append(",");
    sb.append(convertForCsv(scenario.getFarmTypeCodeDescription()));
    sb.append(",");
    sb.append(convertForCsv(oldFarmTypeDetailCodeDescription));
    sb.append(",");
    sb.append(convertForCsv(benefitAmountString));
    sb.append(",");
    sb.append(convertForCsv(scenarioNumberString));
    sb.append(",");
    sb.append(convertForCsv(inPartnershipString));
    sb.append(",");
    sb.append(convertForCsv(bpuSetCompleteString));
    sb.append(",");
    sb.append(convertForCsv(fmvSetCompleteString));
    sb.append(",");
    sb.append(convertForCsv(inCombinedFarmString));
    sb.append(",");
    sb.append(convertForCsv(combinedFarmPinsString));
    sb.append(",");
    sb.append(convertForCsv(municipalityDescription));
    sb.append(",");
    sb.append(convertForCsv(programYearNonParticipantString));
    sb.append(",");
    sb.append(convertForCsv(categoryCode));
    sb.append(",");
    sb.append(convertForCsv(interimBenefitPercentString));
    sb.append(",");
    sb.append(convertForCsv(allocatedReferenceMarginString));
    sb.append(",");
    sb.append(convertForCsv(negativeMarginDeclineString));
    sb.append(",");
    sb.append(convertForCsv(negativeMarginBenefitString));
    sb.append(",");
    sb.append(convertForCsv(lateParticipantString));
    sb.append(",");
    sb.append(convertForCsv(bcFundedBenefitAmountString));
    sb.append(",");
    sb.append(convertForCsv(prodInsurDeemedBenefitString));
    sb.append(",");
    sb.append(convertForCsv(lateEnrolmentPenaltyAmountString));
    sb.append(",");
    sb.append(convertForCsv(localSupplementalReceivedDateString));
    sb.append(",");
    sb.append(convertForCsv(localStatementAReceivedDateString));
    sb.append(",");
    sb.append(convertForCsv(craStatementAReceivedDateString));
    sb.append(",");
    sb.append(convertForCsv(sendCopyToContactPersonString));
    sb.append(",");
    sb.append(convertForCsv(chefsFormNotes));
    sb.append(",");
    sb.append(convertForCsv(formUserType));
    sb.append(",");
    sb.append(convertForCsv(submissionGuidString));
    sb.append(",");
    sb.append(convertForCsv(expectingPaymentString));
    sb.append(",");
    sb.append(convertForCsv(chefsFormType));
    sb.append(",");
    sb.append(convertForCsv(cashMarginsOptInString));
    sb.append(",");
    sb.append(convertForCsv(cashMarginsOptInDateString));
    sb.append(",");
    sb.append(convertForCsv(farmTypeDetailedCodesString));
    sb.append(",");
    sb.append(convertForCsv(benefitTriageResultType));
    sb.append(",");
    sb.append(convertForCsv(combinedFarmsTotalString));

    return sb.toString();
  }

  private String getCombinedFarmPinsString(Scenario scenario) {
    List<Scenario> scenarios = scenario.getCombinedFarm().getScenarios();
    String combinedFarmPinsString;
    StringBuilder builder = new StringBuilder();
    for(Iterator<Scenario> si = scenarios.iterator(); si.hasNext(); ) {
      Scenario curScenario = si.next();
      Integer curPin = curScenario.getClient().getParticipantPin();
      builder.append(curPin);
      if(si.hasNext()) {
        builder.append(COMBINED_FARM_PINS_SCHEDULE_DELIMITER);
      }
    }
    combinedFarmPinsString = builder.toString();
    return combinedFarmPinsString;
  }


  public CrmBenefitUpdateResource transformCsvToCrmResource(String[] fields) throws ParseException {
    logMethodStart(logger);
    
    int f = 0;
    String pinString = getFieldValue(fields, f++);
    String yearString = getFieldValue(fields, f++);
    String state = getFieldValue(fields, f++);
    String stateChangeDateString = getFieldValue(fields, f++);
    String verifierId = getFieldValue(fields, f++);
    String craSupplementalReceivedDateString = getFieldValue(fields, f++);
    String fileStartDateString = getFieldValue(fields, f++);
    String farmTypeDescription = getFieldValue(fields, f++);
    String oldFarmTypeDetail = getFieldValue(fields, f++);
    String benefitAmountString = getFieldValue(fields, f++);
    String scenarioNumberString = getFieldValue(fields, f++);
    String partnershipIndString = getFieldValue(fields, f++);
    String bpuSetCompleteIndString = getFieldValue(fields, f++);
    String fmvSetCompleteIndString = getFieldValue(fields, f++);
    String combinedFarmString = getFieldValue(fields, f++);
    String combinedFarmPinsString = getFieldValue(fields, f++);
    String municipalityDescription = getFieldValue(fields, f++);
    String programYearNonParticipantString = getFieldValue(fields, f++);
    String category = getFieldValue(fields, f++);
    String interimBenefitPercentString = getFieldValue(fields, f++);
    String allocatedReferenceMarginString = getFieldValue(fields, f++);
    String negativeMarginDeclineString = getFieldValue(fields, f++);
    String negativeMarginBenefitString = getFieldValue(fields, f++);
    String lateParticipantString = getFieldValue(fields, f++);
    String bcFundedBenefitAmountString = getFieldValue(fields, f++);
    String prodInsurDeemedBenefitString = getFieldValue(fields, f++);
    String lateEnrolmentPenaltyAmountString = getFieldValue(fields, f++);
    String localSupplementalReceivedDateString = getFieldValue(fields, f++);
    String localStatementAReceivedDateString = getFieldValue(fields, f++);
    String craStatementAReceivedDateString = getFieldValue(fields, f++);
    String sendCopyToContactPersonString = getFieldValue(fields, f++);
    String chefsFormNotes = getFieldValue(fields, f++);
    @SuppressWarnings("unused")
    String chefsFormUserType = getFieldValue(fields, f++);
    @SuppressWarnings("unused")
    String chefsFormSubmissionGuid = getFieldValue(fields, f++);
    String expectingPaymentString = getFieldValue(fields, f++);
    @SuppressWarnings("unused")
    String chefsFormType = getFieldValue(fields, f++);
    String cashMarginsOptInString = getFieldValue(fields, f++);
    String cashMarginsOptInDateString = getFieldValue(fields, f++);
    String farmTypeDetailedCodesString = getFieldValue(fields, f++);
    String benefitTriageResultType = getFieldValue(fields, f++);
    String combinedFarmsTotalString = getFieldValue(fields, f++);

    Double benefitAmountDouble = DataParseUtils.parseDoubleObject(benefitAmountString);
    Double allocatedReferenceMarginDouble = DataParseUtils.parseDoubleObject(allocatedReferenceMarginString);
    Boolean partnershipInd = getIndicator(partnershipIndString);
    
    Date stateChangeDate = DataParseUtils.parseDate(stateChangeDateString);
    Date fileStartDate = DataParseUtils.parseDate(fileStartDateString);
    Date craSupplementalReceivedDate = DataParseUtils.parseDate(craSupplementalReceivedDateString);
    Date localSupplementalReceivedDate = DataParseUtils.parseDate(localSupplementalReceivedDateString);
    Date localStatementAReceivedDate = DataParseUtils.parseDate(localStatementAReceivedDateString);
    Date craStatementAReceivedDate = DataParseUtils.parseDate(craStatementAReceivedDateString);
    
    // These fields are meaningless if supplementary data does not exist
    // so make them null if it does not exist (but not for USER scenarios).
    Boolean bpuSetCompleteInd = null;
    Boolean fmvSetCompleteInd = null;
    if( ! state.equals(ScenarioStateCodes.RECEIVED) || craSupplementalReceivedDate != null) {
      bpuSetCompleteInd = getIndicator(bpuSetCompleteIndString);
      fmvSetCompleteInd = getIndicator(fmvSetCompleteIndString);
    }
    
    Boolean combinedFarm = getIndicator(combinedFarmString);
    String combinedFarmPins = null;
    if(!StringUtils.isBlank(combinedFarmPinsString)) {
      char oldChar = COMBINED_FARM_PINS_SCHEDULE_DELIMITER;
      char newChar = COMBINED_FARM_PINS_TRANSFER_DELIMITER;
      combinedFarmPins = combinedFarmPinsString.replace(oldChar, newChar);
    }
    
    Boolean programYearNonParticipant = getIndicator(programYearNonParticipantString);
    Double interimBenefitPercentDouble = DataParseUtils.parseDoubleObject(interimBenefitPercentString);
    Double negativeMarginDecline = DataParseUtils.parseDoubleObject(negativeMarginDeclineString);
    Double negativeMarginBenefit = DataParseUtils.parseDoubleObject(negativeMarginBenefitString);
    Boolean lateParticipant = getIndicator(lateParticipantString);
    Double bcFundedBenefitAmount = DataParseUtils.parseDoubleObject(bcFundedBenefitAmountString);
    Double combinedFarmsTotal = DataParseUtils.parseDoubleObject(combinedFarmsTotalString);
    Double prodInsurDeemedBenefit = DataParseUtils.parseDoubleObject(prodInsurDeemedBenefitString);
    Double lateEnrolmentPenalty = DataParseUtils.parseDoubleObject(lateEnrolmentPenaltyAmountString);
    Boolean sendCopyToContactPerson = getIndicator(sendCopyToContactPersonString);
    
    Boolean expectingPayment = getIndicator(expectingPaymentString);
    
    String vsi_batchnumber = pinString + " " + yearString + " " + category;
    
    Boolean cashMarginsOptIn = getIndicator(cashMarginsOptInString);
    Date cashMarginsOptInDate = DataParseUtils.parseDate(cashMarginsOptInDateString);
    
    String farmTypeDetailedCodes = null;
    if(!StringUtils.isBlank(farmTypeDetailedCodesString)) {
      String oldDelimiter = FARM_TYPE_DETAILED_SCHEDULE_DELIMITER;
      String newDelimiter = FARM_TYPE_DETAILED_TRANSFER_DELIMITER;
      farmTypeDetailedCodes = farmTypeDetailedCodesString.replace(oldDelimiter, newDelimiter);
    }
    
    boolean zeroPass = BenefitTriageService.TRIAGE_RESULT_TYPE_ZERO_PASS.equals(benefitTriageResultType);
    
    CrmBenefitUpdateResource msg = new CrmBenefitUpdateResource();
    
    msg.setVsi_batchnumber(vsi_batchnumber);
    msg.setVsi_benefitamount(benefitAmountDouble);
    msg.setVsi_benefitcategory(category);
    msg.setVsi_bpusetcomplete(bpuSetCompleteInd);
    msg.setVsi_combinedfarm(combinedFarm);
    msg.setVsi_combinedfarmpins(combinedFarmPins);
    msg.setVsi_deemedproductioninsurancemargindeclineamo(prodInsurDeemedBenefit);
    msg.setVsi_farmsfilestate(state);
    msg.setVsi_farmtype(farmTypeDescription);
    msg.setVsi_farmtypedetailed(oldFarmTypeDetail);
    msg.setVsi_filestartdate(CrmTransferFormatUtil.formatDate(fileStartDate));
    msg.setVsi_fmvsetcomplete(fmvSetCompleteInd);
    msg.setVsi_fullyprovinciallyfunded(lateParticipant);
    msg.setVsi_interimbenefitpercent(interimBenefitPercentDouble);
    msg.setVsi_isprogramyearnonparticipant(programYearNonParticipant);
    msg.setVsi_lateparticipationpenaltyamount(lateEnrolmentPenalty);
    msg.setVsi_localstatementareceiveddate(CrmTransferFormatUtil.formatDate(localStatementAReceivedDate));
    msg.setVsi_municipality(municipalityDescription);
    msg.setVsi_negativemarginbenefitamount(negativeMarginBenefit);
    msg.setVsi_negativemarginmargindeclineamount(negativeMarginDecline);
    msg.setVsi_partnership(partnershipInd);
    msg.setVsi_pin(pinString);
    msg.setVsi_programyear(yearString);
    msg.setVsi_provinciallyfundedamount(bcFundedBenefitAmount);
    msg.setVsi_combinedfarmstotal(combinedFarmsTotal);
    msg.setVsi_provincialsupplementalreceiveddate(CrmTransferFormatUtil.formatDate(localSupplementalReceivedDate));
    msg.setVsi_referencemarginforbenefitcalculation(allocatedReferenceMarginDouble);
    msg.setVsi_scenarionumber(scenarioNumberString);
    msg.setVsi_statechangedate(CrmTransferFormatUtil.formatDate(stateChangeDate));
    msg.setVsi_supplementalreceiveddate(CrmTransferFormatUtil.formatDate(craSupplementalReceivedDate));
    msg.setVsi_verifieruserid(verifierId);
    msg.setCr4dd_crastatementareceiveddate(CrmTransferFormatUtil.formatDate(craStatementAReceivedDate));
    msg.setVsi_sendcopytocontact(sendCopyToContactPerson);
    msg.setVsi_chefsformsnotes(chefsFormNotes);
    msg.setVsi_expectingpayment(expectingPayment);
    msg.setVsi_cashmarginsoptin(cashMarginsOptIn);
    msg.setVsi_cashmarginsoptindate(CrmTransferFormatUtil.formatDate(cashMarginsOptInDate));
    msg.setVsi_farmtypedetailedexpanded(farmTypeDetailedCodes);
    msg.setVsi_fifozeropass(zeroPass);

    logMethodEnd(logger);

    return msg;
  }
}
