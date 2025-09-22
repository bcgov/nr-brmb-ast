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
package ca.bc.gov.srm.farm.service;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.domain.dataimport.StagingResults;
import ca.bc.gov.srm.farm.ui.domain.resultsstaging.STAGINGLOG;

/**
 * @author awilkinson
 * @created Dec 3, 2010
 */
public interface EnrolmentService {

  List<Enrolment> getEnrolments(Integer enrolmentYear, String regionCode) 
  throws ServiceException;
  
  void generateEnrolmentsStaging(Connection connection,
      Integer importVersionId,
      File enrolmentFile,
      String user)
  throws ServiceException;

  void setStagingResults(Transaction transaction,
      STAGINGLOG log,
      StagingResults details)
  throws ServiceException;

  void finishGeneration(Connection connection,
      Integer importVersionId,
      String user)
  throws ServiceException;
  
  File generateCsv(Integer enrolmentYear,
      List<Integer> pins, 
      File tempDir,
      String user)
  throws ServiceException;
  
  void transfer(Connection connection,
      Integer importVersionId,
      File enrolmentFile,
      String user)
  throws ServiceException;

  Scenario getScenario(int pin,
      int programYear)
  throws ServiceException;

  void processEnrolmentFromScenarioWorkflow(Scenario scenario, boolean verifyingLatePartipant,
      boolean completingEnrolmentNotice, String user, Transaction transaction)
  throws ServiceException;
  
  void processEnrolmentFromScenarioWorkflow(Scenario scenario, boolean verifyingLatePartipant,
      boolean completingEnrolmentNotice, String user, Connection connection)
  throws ServiceException;
}
