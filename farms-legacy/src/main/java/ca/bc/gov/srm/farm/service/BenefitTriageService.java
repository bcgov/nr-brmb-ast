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

  List<BenefitTriageStatus> getBenefitTriageStatusByYear(int year) throws ServiceException;

  BenefitTriageResults processBenefitTriage(Connection connection, Integer importVersionId, String userId) throws ServiceException;
  
  BenefitTriageResults processBenefitTriageItems(Connection connection, Integer importVersionId,
      List<BenefitTriageCalculationItem> triageItems, String userId) throws ServiceException;

  void calculateTriageBenefits(Connection connection, List<BenefitTriageCalculationItem> tirageItems, List<BenefitTriageItemResult> results, StagingDAO sdao,
      Integer importVersionId, String userId) throws Exception;

  
  Integer queueBenefitTriage(String triageJobDescription, Connection connection, String userId) throws ServiceException;

}
