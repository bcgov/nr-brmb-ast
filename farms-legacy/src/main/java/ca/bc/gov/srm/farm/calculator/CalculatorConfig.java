package ca.bc.gov.srm.farm.calculator;

import static ca.bc.gov.srm.farm.configuration.ConfigurationKeys.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * There are a lot of magic numbers in the benefits calculations,
 * and it is likely that some will change in  future years (for
 * example the STRUCTURAL_CHANGE_LEAST_AMOUNT). But for now, simply
 * use a constants class - in the future this may need to be refactored
 * to use a factory that returns a object for a given program year.
 */
public final class CalculatorConfig {
  
  /**
   * For program year 2024:
   * - AgriStability (federal) Compensation Rate (positive & negative) is 80%.
   * - BC Enhanced Benefit Compensation Rate (positive & negative) is 90%.
   * - AgriStability (federal) Max Benefit remains at $3,000,000.
   * - BC Enhanced Max Benefit is increased to $6,200,000.
   */
  public static final int GROWING_FORWARD_2024 = 2024;
  
  /**
   * For program year 2023:
   * - AgriStability (federal) Compensation Rate (positive & negative) is 80%.
   * - BC Enhanced Benefit is disabled.
   * - A Payment Cap is applied to the benefit (70% of total margin decline).
   * - Payment Cap was relabeled to Payment Limitation.
   */
  public static final int GROWING_FORWARD_2023 = 2023;
  
  public static final int GROWING_FORWARD_2021 = 2021;
  
  /**
   * For program year 2020 onward, there is updated logic:
   * - Calculate the BC Enhanced Benefit the same as 2019.
   * - For the AgriStability Benefit (federal) no longer calculate
   *   or use the Reference Margin Limit.
   * - The Reference Margin Limit has been eliminated entirely from the calculation.
   */
  public static final int GROWING_FORWARD_2020 = 2020;
  
  /**
   * For program year 2019 onward, there is updated logic:
   * - Calculate the Agristability Benefit the same as 2018.
   * - Also calculate BC Enhanced Benefit and display separately, similar to but not the same as 2017.
   * - For BC Enhanced Benefit, do not apply the Reference Margin Limit.
   * - For BC Enhanced Benefit, increase compensation rate by 10% on the positive and negative margins (80% for both).
   */
  public static final int GROWING_FORWARD_2019 = 2019;
  
  /**
   * For program year 2018 onward, there is updated logic:
   * - Capped Reference Margin Limit (minimum 70% of Reference Margin)
   * - Minimum benefit of $250 (if lower, amount sent to CRM will be $0)
   * - Late Participation - 20% Benefit Reduction (Late Participation Penalty) 
   */
  public static final int GROWING_FORWARD_2018 = 2018;
  
  /**
   * For program year 2017, there is updated calculation logic
   * for BC Enhanced Benefits.
   */
  public static final int GROWING_FORWARD_2017 = 2017;
  
  /**
   * "Growing Forward 2013" changes affect program years 2013 and later.
   * For program year 2013 onward, tier 2 is no longer calculated.
   * Note: for GF, Tier 3 is called "Positive Margin".
   * There are various other changes as well.
   */
  public static final int GROWING_FORWARD_2013 = 2013;
  
  /**
   * For program year 2013 onward, tier 2 is no longer calculated.
   * There are various other changes as well.
   */
  public static final int INCOME_EXPENSE_SUMMARY_NOT_STORED_2011 = 2011;
  
  /**
   * Only 5 percent of yardage income or expenses are considered during
   * the benefit calculation.
   */
  public static final double YARDAGE_FACTOR = 0.05;
  
  /**
   * Only 30 percent of contract work expenses are considered during
   * the benefit calculation.
   */
  public static final double CONTRACT_WORK_FACTOR = 0.30;
  
  /**
   * For the structural change to be "notable" it must be at least this amount.
   * Notable is now labeled in the UI as "Material".
   * TODO Rename notable to material
   */
  public static final double STRUCTURAL_CHANGE_LEAST_AMOUNT = 5000;
  
  /**
   * For the structural change to be "notable" it must be at least this 
   * percentage of the olympic average of the production margin
   */
  public static final double STRUCTURAL_CHANGE_LEAST_PERCENT = 0.10;
  
  /**
   * For some reason this is always hard coded to 100% (i.e 1.000)
   */
  public static final double WHOLE_FARM_ALLOCATION = 1.00;
  
  public static final double TIER_2_TRIGGER_FACTOR = 0.85;
  
  public static final double TIER_2_BENEFIT_FACTOR = 0.70;
  
  public static final double TIER_2_MARGIN_DECLINE_FACTOR = 0.15;
  
  public static final double MIN_TIER_2_BENEFIT = 0.0;
  
  private static final double PAYMENT_TRIGGER_FACTOR = 0.70;
  
  public static final double TIER_3_MARGIN_DECLINE_FACTOR = 0.70;
  
  public static final double MIN_TIER_3_BENEFIT = 0.0;
  
  public static final double INTERIM_BENEFIT_FACTOR = 0.50;
  
  public static final double PROD_INSURANCE_FACTOR = 0.60;
  
  public static final double PROD_INSURANCE_ENHANCED_BENEFIT_FACTOR_2017_FORWARD = 0.70;
  
  /**
   * "Growing Forward" changes affect program years 2013 and later.
   */
  public static final double PROD_INSURANCE_FACTOR_2013_FORWARD = 0.70;
  
  public static final int MIN_NUM_REF_YEARS = 3;
  
  /**
   * FARM-795. I don't know why 3 million was chosen as the maximum payout.
   */
  public static final double AGRISTABILITY_MAX_BENEFIT_AMOUNT = 3000000;
  
  /**
   * As of Program Year 2018, the AgriStability Program does not pay benefits of less than $250.
   */
  public static final double MIN_BENEFIT = 250;
  
  public static final double MIN_MARGIN_DECLINE = 0.0;
  
  public static final int DOLLAR_AMOUNT_DECIMAL_PLACES = 2;
  
  
  public static final String CALC_TYPE_MARGIN = "MARGIN";
  public static final String CALC_TYPE_EXPENSE = "EXPENSE";
  
  public static final String STRUCTURAL_CHANGE_METHOD_RATIO = "RATIO";
  public static final String STRUCTURAL_CHANGE_METHOD_ADDITIVE = "ADDITIVE";
  public static final String STRUCTURAL_CHANGE_METHOD_SELECTED = "SELECTED";
  
  public static final double MIN_ENROLMENT_FEE = 45;
  public static final double ENROLMENT_FEE_FACTOR_2013_FORWARD = 0.0045 * 0.7;
  public static final double ENROLMENT_FEE_FACTOR_2012_PREVIOUS = 0.0045 * 0.85;
  public static final double LATE_ENROLMENT_FEE = 245;
  
  public static final double REFERENCE_MARGIN_LIMIT_CAP_FACTOR = 0.70;
  
  public static final double LATE_ENROLMENT_PENALTY_PERCENTAGE = 0.20;
  
  public static final int BPU_YEARS_IN_A_SET = 6;
  
  public static final int COVERAGE_NOTICE_ADDITIONAL_BPU_YEARS = 4;

  /** private constructor */
  private CalculatorConfig() {}
  
  //
  // For 2020 forward, the Reference Margin Limit has been eliminated.
  //
  public static final boolean useReferenceMarginLimit(int programYear) {
    boolean result = programYear >= GROWING_FORWARD_2013 && programYear <= GROWING_FORWARD_2019;
    return result;
  }

  /**
   * Enhanced Benefits were done as a test in 2017. In 2018 they were not done. For 2019 they are being done.
   */
  public static final boolean hasEnhancedBenefits(int programYear) {
    // Enhanced Benefits were introduced in 2017
    return getBooleanParameter(programYear, BC_ENHANCED_BENEFIT_ENABLED);
  }
  
  public static boolean incomeExpensesSummaryDataNotStored(int programYear) {
    return programYear <= INCOME_EXPENSE_SUMMARY_NOT_STORED_2011;
  }
  
  public static boolean lateParticipantEnabled(int programYear) {
    boolean enabled = programYear == GROWING_FORWARD_2017
        || programYear == GROWING_FORWARD_2018
        || programYear >= GROWING_FORWARD_2021;
    return enabled;
  }
  
  public static boolean lateParticipantPenaltyEnabled(int programYear) {
    boolean enabled = programYear == GROWING_FORWARD_2017
        || programYear >= GROWING_FORWARD_2021;
        return enabled;
  }
  
  public static Double getLateParticipantPenaltyPercent(int programYear) {
    return getPercentParameter(programYear, LATE_PARTICIPANT_PENALTY_PERCENTAGE);
  }
  
  public static double getEnrolmentFeeFactor(int enrolmentYear) {
    double result;
    if(enrolmentYear >= GROWING_FORWARD_2013) {
      result = ENROLMENT_FEE_FACTOR_2013_FORWARD;
    } else {
      result = ENROLMENT_FEE_FACTOR_2012_PREVIOUS;
    }
    return result;
  }

  /**
   * 2009-2012 - 0.6
   * 2013+     - 0.7
   */
  public static double getStandardNegativeMarginCompensationRate(int programYear) {
    return getPercentParameter(programYear, STANDARD_BENEFIT_NEGATIVE_MARGIN_COMPENSATION_RATE);
  }
  
  /**
   * 2009-2012 - 0.8
   * 2013+     - 0.7
   */
  public static double getStandardPositiveMarginCompensationRate(int programYear) {
    return getPercentParameter(programYear, STANDARD_BENEFIT_POSITIVE_MARGIN_COMPENSATION_RATE);
  }

  /**
   * 2017 - 0.7
   * 2018 - no enhanced benefits
   * 2019 - 0.8
   */
  public static double getEnhancedNegativeMarginCompensationRate(int programYear) {
    return getPercentParameter(programYear, ENHANCED_BENEFIT_NEGATIVE_MARGIN_COMPENSATION_RATE);
  }
  
  /**
   *  introduced in 2017 at 0.70
   */
  public static double getEnhancedPositiveMarginCompensationRate(int programYear) {
    return getPercentParameter(programYear, ENHANCED_BENEFIT_POSITIVE_MARGIN_COMPENSATION_RATE);
  }
  
  public static boolean reasonabilityTestsRequired(int programYear, String scenarioCategoryCode) {
    return programYear >= GROWING_FORWARD_2013 && ! COVERAGE_NOTICE.equals(scenarioCategoryCode);
  }

  public static double getPreVerificationPaymentAmountRequiringASpecialist() {
    return getDoubleParameter(PRE_VERIFICATION_PAYMENT_AMOUNT_REQUIRING_A_SPECIALIST);
  }
  
  public static double getPreVerificationIncomeAmountRequiringASpecialist() {
    return getDoubleParameter(PRE_VERIFICATION_INCOME_AMOUNT_REQUIRING_A_SPECIALIST);
  }
  
  public static List<String> getPreVerificationFarmTypeDetailedCodesRequiringSpecialist() {
    return getListFromCsvParameter(PRE_VERIFICATION_FARM_TYPE_DETAILED_CODES_REQUIRING_SPECIALIST);
  }
  
  public static List<String> getPreVerificationFarmTypeDetailedCodesForAbbotsford() {
    return getListFromCsvParameter(PRE_VERIFICATION_FARM_TYPE_DETAILED_CODES_FOR_ABBOTSFORD);
  }
  
  public static List<String> getPreVerificationFarmTypeDetailedCodesForKelowna() {
    return getListFromCsvParameter(PRE_VERIFICATION_FARM_TYPE_DETAILED_CODES_FOR_KELOWNA);
  }
  
  public static Double getPaymentCapPercentageOfTotalMarginDecline(int programYear) {
    return getPercentParameter(programYear, PAYMENT_LIMITATION_PERCENTAGE_OF_TOTAL_MARGIN_DECLINE);
  }
  
  public static boolean isPaymentCapEnabled(int programYear) {
    return getBooleanParameter(programYear, PAYMENT_LIMITATION_ENABLED);
  }
  
  public static Double getAgriStabilityMaxBenefit(int programYear) {
    Double result;
    if(programYear >= GROWING_FORWARD_2024) {
      result = getDoubleParameter(programYear, AGRISTABILITY_MAX_BENEFIT);
    } else {
      result = AGRISTABILITY_MAX_BENEFIT_AMOUNT;
    }
    return result;
  }
  
  public static Double getBCEnhancedMaxBenefit(int programYear) {
    Double result;
    if(programYear < GROWING_FORWARD_2024) {
      result = AGRISTABILITY_MAX_BENEFIT_AMOUNT;
    } else {
      result = getDoubleParameter(programYear, BC_ENHANCED_MAX_BENEFIT);
    }
    return result;
  }

  private static List<String> getListFromCsvParameter(String parameterName) {
    List<String> values = new ArrayList<>();
    String parameter = getParameter(parameterName);
    String[] array = parameter.split(",");
    for (String value : array) {
      String trimmedValue = value.trim();
      if(StringUtils.isNotBlank(trimmedValue)) {
        values.add(trimmedValue);
      }
    }
    return values;
  }
  
  private static Double getDoubleParameter(String parameterName) {
    return ConfigurationUtility.getInstance().getDouble(parameterName);
  }
  
  private static String getParameter(String parameterName) {
    return ConfigurationUtility.getInstance().getValue(parameterName);
  }
  
  @SuppressWarnings("unused")
  private static String getParameter(int programYear, String parameterName) {
    return getParameter(programYear, parameterName, true);
  }
  
  private static String getParameter(int programYear, String parameterName, boolean errorOnNotFound) {
    return ServiceFactory.getConfigurationService().getValue(programYear, parameterName, errorOnNotFound);
  }
  
  private static double getDoubleParameter(int programYear, String parameterName) {
    return ServiceFactory.getConfigurationService().getDouble(programYear, parameterName);
  }
  
  private static double getPercentParameter(int programYear, String parameterName) {
    return ServiceFactory.getConfigurationService().getPercent(programYear, parameterName);
  }
  
  private static BigDecimal getBigDecimalParameter(int programYear, String parameterName) {
    return ServiceFactory.getConfigurationService().getBigDecimal(programYear, parameterName);
  }
  
  private static boolean getBooleanParameter(int programYear, String parameterName) {
    String value = getParameter(programYear, parameterName, false);
    return "Y".equalsIgnoreCase(value)
        || "Yes".equalsIgnoreCase(value)
        || "true".equalsIgnoreCase(value);
  }
  
  public static Map<String, String> getParameters(int programYear) {
    Map<String, String> result = new HashMap<>();
    for (String parameterName : ConfigurationKeys.BENEFIT_CALCULATION_PARAMETERS) {
      String value = getParameter(programYear, parameterName, false);
      // if no value found for parameter then the parameter does not apply to this year so ignore it
      if(value != null) {
        result.put(parameterName, value);
      }
    }
    
    return result;
  }

  public static double getProductionInsuranceFactor(int programYear) {
    double result;
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
      result = CalculatorConfig.PROD_INSURANCE_FACTOR_2013_FORWARD;
    } else {
      result = CalculatorConfig.PROD_INSURANCE_FACTOR;
    }
    return result;
  }

  public static double getEnhancedProductionInsuranceFactor(@SuppressWarnings("unused") int programYear) {
    double prodInsuranceFactor;
    prodInsuranceFactor = CalculatorConfig.PROD_INSURANCE_ENHANCED_BENEFIT_FACTOR_2017_FORWARD;
    return prodInsuranceFactor;
  }

  public static double getPaymentTriggerFactor() {
    return PAYMENT_TRIGGER_FACTOR;
  }
  
  public static boolean isProdutiveUnitsRollupEnabled(int programYear) {
    return programYear >= GROWING_FORWARD_2024;
  }
  
  public static BigDecimal getNegativeMarginPaymentPercentage(int programYear) {
    BigDecimal result =
        getBigDecimalParameter(programYear, NEGATIVE_MARGIN_PAYMENT_PERCENTAGE)
        .divide(BigDecimal.valueOf(100));
    return result;
  }
  
  public static boolean isNegativeMarginCalculationEnabled(int programYear) {
    return programYear >= GROWING_FORWARD_2024;
  }
}
