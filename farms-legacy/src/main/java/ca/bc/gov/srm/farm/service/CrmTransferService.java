/**
 * Copyright (c) 2011,
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

import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 */
public interface CrmTransferService {
  
  void scheduleBenefitTransfer(Scenario scenario, String userEmail, String userId)
  throws Exception;
  
  void scheduleBenefitTransfer(Scenario scenario, String userEmail, String userId, Transaction transaction)
      throws Exception;
  
  void scheduleBenefitTransfer(Scenario scenario, String userEmail, String userId, String chefsFormNotes, String formUserType,
      String chefsFormType, String fifoResultType, Transaction transaction)
  throws Exception;

  void transferBenefitUpdate(final Connection connection,
      final Integer importVersionId,
      final File enrolmentFile,
      final String user) throws ServiceException;

  void transferAccountUpdate(final Connection connection,
      final Integer importVersionId,
      final File enrolmentFile,
      final String user) throws ServiceException;

  void clearSuccessfulTransfers() throws ServiceException;

  void postEnrolment(Enrolment e, Integer importVersionId, String user)
      throws Exception;

  void scheduleAccountUpdate(Integer clientId, String description, String userId, Transaction transaction) throws Exception;
  
  void scheduleAccountUpdate(Integer clientId, String description, String userId, String submissionGuid, String formUserType, Transaction transaction) throws Exception;

  void uploadFileToNote(File file, String crmEntityGuid, String formType) throws ServiceException;

  void uploadFileToAccount(File file, Integer participantPin, String formType) throws ServiceException;

  CrmAccountResource accountUpdate(Connection connection, CrmAccountResource crmAccount, Client client, String submissionGuid, String formUserType,
      String chefsFormTypeCodes) throws ServiceException;

}
