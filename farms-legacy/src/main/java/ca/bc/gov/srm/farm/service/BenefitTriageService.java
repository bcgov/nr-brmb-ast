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
package ca.bc.gov.srm.farm.service;

import java.sql.Connection;
import java.util.List;

import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageCalculationItem;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageItemResult;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageResults;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageStatus;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface BenefitTriageService {
  
  public static final String TRIAGE_RESULT_TYPE_ZERO_PASS = "ZERO_PASS";
  public static final String TRIAGE_RESULT_TYPE_PAYMENT_PASS = "PAYMENT_PASS";
  
  public static final String MESSAGE_FAIL_STRUCTURE_CHANGE_NOT_ENABLED =
      "Fail: Structure Change is not enabled because BPUs are missing.";
  
  public static final String MESSAGE_FAIL_REFERENCE_MARGIN_FAILED_AT_LOW_END =
      "Fail: The Reference Margin Test failed at the low end.";
  
  public static final String MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED =
      "Fail: Structural Change Additive Division Test failed.";
  
  public static final String MESSAGE_FAIL_LESS_THAN_5_YEARS_OF_DATA =
      "Fail: Less than 5 reference years.";
  
  public static final String MESSAGE_FAIL_MISSING_INCOME =
      "Fail: Missing income for one or more years.";
  
  public static final String MESSAGE_FAIL_MISSING_EXPENSES =
      "Fail: Missing expenses for one or more years.";
  
  public static final String MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED =
      "Fail: Fiscal Year End date changed.";
  
  public static final String MESSAGE_FAIL_COMBINED_FARM =
      "Fail: Last year this producer was part of a Combined Farm.";
  
  public static final String MESSAGE_FAIL_ACCOUNTING_METHOD_CHANGED =
      "Fail: Accounting Method changed.";
  
  public static final String MESSAGE_FAIL_MUNICIPALITY_CHANGED =
      "Fail: Municipality changed.";
  
  public static final String MESSAGE_FAIL_BENEFIT_RISK_FAILED_AT_HIGH_END =
      "Fail: Benefit Risk Test failed at the high end.";
  
  public static final String MESSAGE_FAIL_PAYMENT_TOO_LARGE =
      "Fail: Payment too large.";

  List<BenefitTriageStatus> getBenefitTriageStatusByYear(int year) throws ServiceException;

  BenefitTriageResults processBenefitTriage(Connection connection, Integer importVersionId, String userId) throws ServiceException;
  
  BenefitTriageResults processBenefitTriageItems(Connection connection, Integer importVersionId,
      List<BenefitTriageCalculationItem> triageItems, String userId) throws ServiceException;

  void calculateTriageBenefits(Connection connection, List<BenefitTriageCalculationItem> tirageItems, List<BenefitTriageItemResult> results, StagingDAO sdao,
      Integer importVersionId, String userId) throws Exception;

  
  Integer queueBenefitTriage(String triageJobDescription, Connection connection, String userId) throws ServiceException;

}
