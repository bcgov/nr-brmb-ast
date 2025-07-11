package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.StagingDao;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;

public class StagingDaoImpl extends BaseDao implements StagingDao {

    private static final long serialVersionUID = 1L;

    private Connection conn;

    public StagingDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void insert(Z01ParticipantInfo obj, String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z01(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setString(i++, obj.getSinCtnBn());
            callableStatement.setString(i++, obj.getSin());
            callableStatement.setString(i++, obj.getBusinessNumber());
            callableStatement.setString(i++, obj.getTrustNumber());
            callableStatement.setInt(i++, obj.getParticipantTypeCode());
            callableStatement.setInt(i++, obj.getParticipantLanguage());
            callableStatement.setString(i++, obj.getFirstName());
            callableStatement.setString(i++, obj.getLastName());
            callableStatement.setString(i++, obj.getCorpName());
            callableStatement.setString(i++, obj.getAddress1());
            callableStatement.setString(i++, obj.getAddress2());
            callableStatement.setString(i++, obj.getCity());
            callableStatement.setString(i++, obj.getProvince());
            callableStatement.setString(i++, obj.getPostalCode());
            callableStatement.setString(i++, obj.getCountry());
            callableStatement.setString(i++, obj.getParticipantFax());
            callableStatement.setString(i++, obj.getParticipantPhoneDay());
            callableStatement.setString(i++, obj.getParticipantPhoneEvening());
            callableStatement.setString(i++, obj.getParticipantPhoneCell());
            callableStatement.setString(i++, obj.getParticipantEmail());
            callableStatement.setString(i++, obj.getContactFirstName());
            callableStatement.setString(i++, obj.getContactLastName());
            callableStatement.setString(i++, obj.getContactBusinessName());
            callableStatement.setString(i++, obj.getContactAddress1());
            callableStatement.setString(i++, obj.getContactAddress2());
            callableStatement.setString(i++, obj.getContactCity());
            callableStatement.setString(i++, obj.getContactProvince());
            callableStatement.setString(i++, obj.getContactPostalCode());
            callableStatement.setString(i++, obj.getContactPhoneDay());
            callableStatement.setString(i++, obj.getContactFaxNumber());
            callableStatement.setString(i++, obj.getContactPhoneCell());
            callableStatement.setInt(i++, obj.getPublicOffice());
            callableStatement.setString(i++, obj.getIdentEffectiveDate());
            callableStatement.setString(i++, userId);
            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

}
