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
package ca.bc.gov.srm.farm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.domain.codes.CrmEntityTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;

/** Uses the existing unique CRM-entity link to reserve one request per CORE enrolment. */
public class OptOutSubmissionDAO extends ChefsDatabaseDAO {

  public String readEnrolmentForSubmission(Connection connection, Integer submissionId) throws DataAccessException {
    String sql = "SELECT crm_entity_guid FROM farm_chef_submssn_crm_entities"
        + " WHERE chef_submission_id = ? AND crm_entity_type_code = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, submissionId);
      statement.setString(2, CrmEntityTypeCodes.ENROLMENT);
      try (ResultSet result = statement.executeQuery()) {
        return result.next() ? result.getString(1) : null;
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  private String readOwner(Connection connection, String enrolmentId) throws DataAccessException {
    String sql = "SELECT s.chef_submission_guid FROM farm_chef_submssn_crm_entities e"
        + " JOIN farm_chef_submissions s ON s.chef_submission_id = e.chef_submission_id"
        + " WHERE e.crm_entity_guid = ? AND e.crm_entity_type_code = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, enrolmentId);
      statement.setString(2, CrmEntityTypeCodes.ENROLMENT);
      try (ResultSet result = statement.executeQuery()) {
        return result.next() ? result.getString(1) : null;
      }
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  /**
   * Commit the reservation before calling CORE. The CRM entity GUID primary key
   * also protects against competing IDIR/BCeID requests and concurrent pollers.
   * A failed CORE update retains its reservation so the same request can retry.
   * Returns the owning submission GUID (which may be a previous request).
   */
  public String claimEnrolment(Connection connection, String enrolmentId, Integer submissionId,
      String submissionGuid, String user) throws DataAccessException {
    try {
      // Serialize edits/retries of this submission, including changes to its year/PIN.
      try (PreparedStatement lock = connection.prepareStatement(
          "SELECT chef_submission_id FROM farm_chef_submissions WHERE chef_submission_id = ? FOR UPDATE")) {
        lock.setInt(1, submissionId);
        try (ResultSet result = lock.executeQuery()) {
          if (!result.next()) throw new DataAccessException("Opt Out submission not found.");
        }
      }
      String previousEnrolment = readEnrolmentForSubmission(connection, submissionId);
      if (previousEnrolment != null && !previousEnrolment.equals(enrolmentId)) {
        throw new DataAccessException("An Opt Out request cannot change enrolment after processing has started.");
      }
      String owner = readOwner(connection, enrolmentId);
      if (owner == null) {
        ChefsSubmssnCrmEntity entity = new ChefsSubmssnCrmEntity();
        entity.setCrmEntityGuid(enrolmentId);
        entity.setCrmEntityTypeCode(CrmEntityTypeCodes.ENROLMENT);
        entity.setChefSubmissionId(submissionId);
        createCrmEntity(connection, entity, user);
        owner = submissionGuid;
      }
      connection.commit();
      return owner;
    } catch (SQLException | DataAccessException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackError) {
        e.addSuppressed(rollbackError);
        throw new DataAccessException(e);
      }
      // A competing submission may have won the unique enrolment reservation.
      String owner = readOwner(connection, enrolmentId);
      if (owner != null && !owner.equals(submissionGuid)) return owner;
      throw new DataAccessException(e);
    }
  }
}
