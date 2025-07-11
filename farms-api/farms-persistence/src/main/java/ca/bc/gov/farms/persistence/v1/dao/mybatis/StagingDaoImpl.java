package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.StagingDao;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z02PartpntFarmInfo;

public class StagingDaoImpl extends BaseDao implements StagingDao {

    private static final long serialVersionUID = 1L;

    private Connection conn;

    public StagingDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void insert(final Z01ParticipantInfo obj, final String userId) throws SQLException {
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

    @Override
    public void insert(final Z02PartpntFarmInfo obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z02(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getFormVersionNumber());

            callableStatement.setString(i++, obj.getProvinceOfResidence());
            callableStatement.setString(i++, obj.getProvinceOfMainFarmstead());
            callableStatement.setString(i++, obj.getPostmarkDate());
            callableStatement.setString(i++, obj.getReceivedDate());

            callableStatement.setString(i++,
                    obj.isSoleProprietor() == null ? null : obj.isSoleProprietor() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isPartnershipMember() == null ? null : obj.isPartnershipMember() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isCorporateShareholder() == null ? null : obj.isCorporateShareholder() ? "Y" : "N");
            callableStatement.setString(i++, obj.isCoopMember() == null ? null : obj.isCoopMember() ? "Y" : "N");

            callableStatement.setInt(i++, obj.getCommonShareTotal());
            callableStatement.setInt(i++, obj.getFarmYears());
            callableStatement.setString(i++,
                    obj.isLastYearFarming() == null ? null : obj.isLastYearFarming() ? "Y" : "N");
            callableStatement.setInt(i++, obj.getFormOriginCode());
            callableStatement.setInt(i++, obj.getIndustryCode());
            callableStatement.setInt(i++, obj.getParticipantProfileCode());

            callableStatement.setString(i++,
                    obj.isAccrualCashConversion() == null ? null : obj.isAccrualCashConversion() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isPerishableCommodities() == null ? null : obj.isPerishableCommodities() ? "Y" : "N");
            callableStatement.setString(i++, obj.isReceipts() == null ? null : obj.isReceipts() ? "Y" : "N");
            callableStatement.setString(i++, obj.isOtherText() == null ? null : obj.isOtherText() ? "Y" : "N");
            callableStatement.setString(i++, obj.getOtherText());

            callableStatement.setString(i++,
                    obj.isAccrualWorksheet() == null ? null : obj.isAccrualWorksheet() ? "Y" : "N");
            callableStatement.setString(i++, obj.isCwbWorksheet() == null ? null : obj.isCwbWorksheet() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isCombinedThisYear() == null ? null : obj.isCombinedThisYear() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isCompletedProdCycle() == null ? null : obj.isCompletedProdCycle() ? "Y" : "N");
            callableStatement.setString(i++, obj.isDisaster() == null ? null : obj.isDisaster() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isCopyCobToContact() == null ? null : obj.isCopyCobToContact() ? "Y" : "N");

            callableStatement.setInt(i++, obj.getMunicipalityCode());
            callableStatement.setString(i++, obj.getFormVersionEffectiveDate());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

}
