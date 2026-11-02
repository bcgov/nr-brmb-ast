/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.processor;

import static ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes.*;
import static ca.bc.gov.srm.farm.chefs.forms.OptOutFormConstants.*;
import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.resource.optOut.OptOutSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.dao.OptOutSubmissionDAO;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.exception.ServiceException;

public class OptOutSubmissionProcessor extends ChefsSubmissionProcessor<OptOutSubmissionDataResource> {
  protected OptOutSubmissionDAO optOutDao = new OptOutSubmissionDAO();
  private String validationQueueId;

  public OptOutSubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.OPT_OUT, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }

  @Override
  protected void processSubmission(String submissionGuid, String response) throws ServiceException {
    processSubmission(getSubmissionMetaData(response, OptOutSubmissionDataResource.class));
  }

  public CrmTaskResource processSubmission(SubmissionParentResource<OptOutSubmissionDataResource> metadata) throws ServiceException {
    if (!checkEnvironment(metadata)) return null;
    OptOutSubmissionDataResource data = metadata.getSubmission().getData();
    String guid = metadata.getSubmissionGuid();
    data.setSubmissionGuid(guid);
    data.setParsedProgramYear(data.getYear());
    ChefsSubmissionProcessData decision = shouldProcessSubmission(guid, data, submissionRecordMap.get(guid));
    if (!Boolean.TRUE.equals(decision.getProcess())) return decision.getValidationErrorTask();

    ChefsSubmission record = decision.getChefsSubmission();
    if (record == null) record = newSubmissionRecord(guid);
    Client client = readClient(data.getParticipantPin());
    CrmAccountResource account = data.getParticipantPin() == null ? null : getCrmAccountByParticipantPin(data.getParticipantPin());
    List<String> errors = OptOutSubmissionValidator.validate(data, client);
    if (account == null) errors.add("PIN not found in CORE.");
    if (!errors.isEmpty()) return recordValidationFailure(record, account, data, errors, INVALID);

    CrmProgramYearResource year = crmDao.getProgramYear(data.getYear());
    CrmEnrolmentResource enrolment = year == null ? null : crmDao.getEnrolment(year.getVsi_programyearid(), account.getAccountid());
    if (enrolment == null || enrolment.getVsi_participantprogramyearid() == null) {
      errors.add("No CORE enrolment exists for the selected participant and program year.");
      return recordValidationFailure(record, account, data, errors, INVALID);
    }
    String enrolmentId = UUID.fromString(enrolment.getVsi_participantprogramyearid()).toString();
    String owner;
    try {
      String previous = optOutDao.readEnrolmentForSubmission(connection, record.getSubmissionId());
      if (previous != null && !previous.equals(enrolmentId)) {
        errors.add("This request already started processing a different participant or program year. Staff review is required.");
        return recordValidationFailure(record, account, data, errors, INVALID);
      }
      owner = optOutDao.claimEnrolment(connection, enrolmentId, record.getSubmissionId(), guid, user);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    if (!guid.equals(owner)) {
      errors.add("An Opt Out request already exists for this participant and program year: " + owner);
      return recordValidationFailure(record, account, data, errors, DUPLICATE);
    }

    // Repeating this PATCH after a timeout is safe; an already-applied status is left alone.
    if (!Integer.valueOf(CrmConstants.ENROLMENT_STATUS_CODE_OPTED_OUT).equals(enrolment.getEnrolmentStatusCode())) {
      crmDao.updateEnrolmentStatus(enrolmentId, CrmConstants.ENROLMENT_STATUS_CODE_OPTED_OUT);
    }
    record.setSubmissionStatusCode(PROCESSED);
    createOrUpdateSubmission(record);
    return null;
  }

  @Override
  public boolean checkEnvironment(SubmissionParentResource<? extends ChefsSubmissionDataResource> metadata)
      throws ServiceException {
    if (Boolean.TRUE.equals(metadata.getDraft()) || Boolean.TRUE.equals(metadata.getDeleted())
        || !configUtil.getBoolean(ConfigurationKeys.CHEFS_OPT_OUT_ENABLED)) return false;
    // The supplied schema has no Admin Use section. An explicit per-environment
    // activation setting chooses the consumer for this external form (DEV/DLVR share CHEFS).
    ChefsSubmissionDataResource data = metadata.getSubmission().getData();
    if (ChefsConstants.USER_TYPE_IDIR.equals(formUserType) && !hasAdminValues(data)) return true;
    return super.checkEnvironment(metadata);
  }

  @Override
  protected ChefsSubmissionProcessData handleAdminSection(String guid,
      ChefsSubmissionDataResource data, ChefsSubmission record) throws ServiceException {
    if (ChefsConstants.USER_TYPE_IDIR.equals(formUserType) && !hasAdminValues(data)) {
      data.setOrigin(FIELD_VALUE_ORIGIN_EXTERNAL);
      data.setExternalMethod(FIELD_VALUE_METHOD_CHEFS_FORM);
      data.setEnvironment(configUtil.getEnvironment());
    }
    return super.handleAdminSection(guid, data, record);
  }

  private boolean hasAdminValues(ChefsSubmissionDataResource data) {
    return nonBlank(data.getOrigin()) || nonBlank(data.getInternalMethod())
        || nonBlank(data.getExternalMethod()) || nonBlank(data.getEnvironment());
  }

  private boolean nonBlank(String value) {
    return value != null && !value.trim().isEmpty();
  }

  protected Client readClient(Integer pin) throws ServiceException {
    if (pin == null) return null;
    try {
      return readDAO.readClient(pin);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  private CrmTaskResource recordValidationFailure(ChefsSubmission record, CrmAccountResource account,
      OptOutSubmissionDataResource data, List<String> errors, String status) throws ServiceException {
    CrmTaskResource task = crmDao.getValidationErrorBySubmissionGuid(data.getSubmissionGuid());
    if (task == null || Integer.valueOf(CrmConstants.TASK_STATE_CODE_COMPLETED).equals(task.getStateCode())) {
      CrmValidationErrorResource error = new CrmValidationErrorResource();
      error.setAccountIdParameter(account == null ? null : account.getAccountid());
      error.setSubject(String.format("%s %s %s", data.getYear(), FORM_SHORT_NAME, data.getParticipantPin()));
      error.setDescription(String.join("\n", errors));
      error.setCr4dd_origin(data.getOrigin());
      error.setCr4dd_method(getMethod(data));
      error.setCr4dd_chefsurl(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL)
          + ChefsConstants.URL_VIEW_CHEFS_SUBMISSION_FORM + data.getSubmissionGuid());
      task = crmDao.createValidationErrorTask(error, getValidationQueueId());
    }
    record.setSubmissionStatusCode(status);
    record.setValidationTaskGuid(task.getActivityId());
    createOrUpdateSubmission(record);
    return task;
  }

  @Override
  protected String getSystemErrorQueueId() throws ServiceException {
    return getValidationQueueId();
  }

  @Override
  protected String getValidationQueueId() throws ServiceException {
    if (validationQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_OPT_OUT_VALIDATION);
      if (queueName == null || queueName.trim().isEmpty()) throw new ServiceException("Opt Out validation queue is not configured.");
      CrmQueueResource queue = crmDao.getQueueByName(queueName);
      if (queue == null) throw new ServiceException("Opt Out validation queue was not found.");
      validationQueueId = queue.getQueueId();
    }
    return validationQueueId;
  }
}
