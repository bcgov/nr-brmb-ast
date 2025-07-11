package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.sql.SQLException;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z02PartpntFarmInfo;

public interface StagingDao extends Serializable {

    void insert(final Z01ParticipantInfo obj, final String userId) throws SQLException;

    void insert(final Z02PartpntFarmInfo obj, final String userId) throws SQLException;
}
