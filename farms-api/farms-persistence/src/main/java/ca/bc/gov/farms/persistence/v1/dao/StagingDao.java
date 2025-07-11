package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.sql.SQLException;

import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z02PartpntFarmInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z03StatementInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z04IncomeExpsDtl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z05PartnerInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z21ParticipantSuppl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z22ProductionInsurance;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z23LivestockProdCpct;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z28ProdInsuranceRef;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z29InventoryRef;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z40PrtcpntRefSuplDtl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z42ParticipantRefYear;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z50ParticipntBnftCalc;

public interface StagingDao extends Serializable {

    void insert(final Z01ParticipantInfo obj, final String userId) throws SQLException;

    void insert(final Z02PartpntFarmInfo obj, final String userId) throws SQLException;

    void insert(final Z03StatementInfo obj, final String userId) throws SQLException;

    void insert(final Z04IncomeExpsDtl obj, final String userId) throws SQLException;

    void insert(final Z05PartnerInfo obj, final String userId) throws SQLException;

    void insert(final Z21ParticipantSuppl obj, final String userId) throws SQLException;

    void insert(final Z22ProductionInsurance obj, final String userId) throws SQLException;

    void insert(final Z23LivestockProdCpct obj, final String userId) throws SQLException;

    void insert(final Z28ProdInsuranceRef obj, final String userId) throws SQLException;

    void insert(final Z29InventoryRef obj, final String userId) throws SQLException;

    void insert(final Z40PrtcpntRefSuplDtl obj, final String userId) throws SQLException;

    void insert(final Z42ParticipantRefYear obj, final String userId) throws SQLException;

    void insert(final Z50ParticipntBnftCalc obj, final String userId) throws SQLException;
}
