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
package ca.bc.gov.srm.farm.service;

import java.io.File;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface CdogsService {

  Map<Integer,File> createCdogsAdjustmentFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer, File> createCdogsCashMarginsFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer, File> createCdogsCoverageFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer,File> createCdogsInterimFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer,File> createCdogsNolFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer,File> createCdogsNppFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer, File> createCdogsDocumentByFormType(String submissionGuid, String formUserType, String formType) throws ServiceException;

  Map<Integer, File> createCdogsStatementAFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  Map<Integer, File> createCdogsSupplementalFormDocument(String submissionGuid, String formUserType) throws ServiceException;

  void createCdogsCoverageNoticeReport(Scenario scenario, String userId)
      throws ServiceException;
  
}