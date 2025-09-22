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

import java.util.List;

import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface ChefsFormSubmissionService {

  List<ChefsSubmission> getFormSubmissionsByType(String formType) throws ServiceException;

  ChefsSubmission getSubmissionByGuid(String submissionGuid) throws ServiceException;

  SubmissionWrapperResource<?> getSubmissionWrapperResource(String submissionGuid, String formType, String userType) throws ServiceException;

  String getUserFormType(ChefsSubmission submission);

  String getUserFormTypeFromPreflight(String submissionGuid) throws ServiceException;

  void updateSubmission(ChefsSubmission submission) throws ServiceException;

  List<ChefsSubmssnCrmEntity> getSubmissionCrmEntityGuidsBySubmissionGuid(String submissionGuid) throws ServiceException;

}
