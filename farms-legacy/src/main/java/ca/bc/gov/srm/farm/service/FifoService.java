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

import java.io.File;
import java.sql.Connection;
import java.util.List;

import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.domain.fifo.FifoCalculationItem;
import ca.bc.gov.srm.farm.domain.fifo.FifoItemResult;
import ca.bc.gov.srm.farm.domain.fifo.FifoStatus;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface FifoService {
  
  public static final String FIFO_RESULT_TYPE_ZERO_PASS = "ZERO_PASS";

  List<FifoStatus> getFifoStatusByYear(int year) throws ServiceException;

  void processFifoCalculations(Connection connection, File file, Integer importVersionId, String userId) throws ServiceException;

  void calculateFifoBenefits(Connection connection, List<FifoCalculationItem> fifoItems, List<FifoItemResult> results, StagingDAO sdao,
      Integer importVersionId, String userId) throws Exception;

}
