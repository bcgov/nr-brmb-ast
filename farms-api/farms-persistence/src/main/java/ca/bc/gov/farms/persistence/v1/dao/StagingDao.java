package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.sql.SQLException;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z02PartpntFarmInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z03StatementInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z04IncomeExpsDtl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z05PartnerInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z21ParticipantSuppl;

public interface StagingDao extends Serializable {

    void insert(final Z01ParticipantInfo obj, final String userId) throws SQLException;

    void insert(final Z02PartpntFarmInfo obj, final String userId) throws SQLException;

    void insert(final Z03StatementInfo obj, final String userId) throws SQLException;

    void insert(final Z04IncomeExpsDtl obj, final String userId) throws SQLException;

    void insert(final Z05PartnerInfo obj, final String userId) throws SQLException;

    void insert(final Z21ParticipantSuppl obj, final String userId) throws SQLException;
}
