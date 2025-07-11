package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.StagingDao;
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
import ca.bc.gov.farms.persistence.v1.dto.staging.Z51ParticipantContrib;

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

    @Override
    public void insert(final Z03StatementInfo obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z03(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getOperationNumber());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());

            callableStatement.setInt(i++, obj.getPartnershipPin());
            callableStatement.setString(i++, obj.getPartnershipName());
            callableStatement.setDouble(i++, obj.getPartnershipPercent());
            callableStatement.setString(i++, obj.getFiscalYearStart());
            callableStatement.setString(i++, obj.getFiscalYearEnd());
            callableStatement.setInt(i++, obj.getAccountingCode());

            callableStatement.setString(i++, obj.isLandlord() == null ? null : obj.isLandlord() ? "Y" : "N");
            callableStatement.setString(i++, obj.isCropShare() == null ? null : obj.isCropShare() ? "Y" : "N");
            callableStatement.setString(i++, obj.isFeederMember() == null ? null : obj.isFeederMember() ? "Y" : "N");
            callableStatement.setDouble(i++, obj.getGrossIncome());
            callableStatement.setDouble(i++, obj.getExpenses());
            callableStatement.setDouble(i++, obj.getNetIncomeBeforeAdj());
            callableStatement.setDouble(i++, obj.getOtherDeductions());
            callableStatement.setDouble(i++, obj.getInventoryAdjustments());
            callableStatement.setDouble(i++, obj.getNetIncomeAfterAdj());
            callableStatement.setDouble(i++, obj.getBusinessUseOfHomeExpenses());
            callableStatement.setDouble(i++, obj.getNetFarmIncome());
            callableStatement.setString(i++, obj.isCropDisaster() == null ? null : obj.isCropDisaster() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isLivestockDisaster() == null ? null : obj.isLivestockDisaster() ? "Y" : "N");
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z04IncomeExpsDtl obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z04(?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getIncomeExpenseKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getOperationNumber());
            callableStatement.setInt(i++, obj.getLineCode());
            callableStatement.setString(i++, obj.getIe());
            callableStatement.setDouble(i++, obj.getAmount());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z05PartnerInfo obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z05(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getPartnerInfoKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getOperationNumber());

            callableStatement.setInt(i++, obj.getPartnershipPin());
            callableStatement.setString(i++, obj.getPartnerFirstName());
            callableStatement.setString(i++, obj.getPartnerLastName());
            callableStatement.setString(i++, obj.getPartnerCorpName());
            callableStatement.setString(i++, obj.getPartnerSinCtnBn());
            callableStatement.setDouble(i++, obj.getPartnerPercent());
            callableStatement.setInt(i++, obj.getPartnerPin());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z21ParticipantSuppl obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z21(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getInventoryKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getOperationNumber());
            callableStatement.setInt(i++, obj.getInventoryTypeCode());
            callableStatement.setInt(i++, obj.getInventoryCode());
            callableStatement.setInt(i++, obj.getCropUnitType());
            callableStatement.setDouble(i++, obj.getCropOnFarmAcres());
            callableStatement.setDouble(i++, obj.getCropQtyProduced());
            callableStatement.setDouble(i++, obj.getQuantityEnd());
            callableStatement.setDouble(i++, obj.getEndOfYearPrice());
            callableStatement.setDouble(i++, obj.getEndOfYearAmount());
            callableStatement.setDouble(i++, obj.getCropUnseedableAcres());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z22ProductionInsurance obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z22(?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getProductionInsuranceKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getOperationNumber());
            callableStatement.setString(i++, obj.getProductionInsuranceNumber());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z23LivestockProdCpct obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z23(?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getProductiveCapacityKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getOperationNumber());
            callableStatement.setInt(i++, obj.getInventoryCode());
            callableStatement.setDouble(i++, obj.getProductiveCapacityAmount());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z28ProdInsuranceRef obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z28(?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getProductionUnit());
            callableStatement.setString(i++, obj.getProductionUnitDescription());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z29InventoryRef obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z29(?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getInventoryCode());
            callableStatement.setInt(i++, obj.getInventoryTypeCode());
            callableStatement.setString(i++, obj.getInventoryDesc());
            callableStatement.setString(i++, obj.getInventoryTypeDescription());
            callableStatement.setInt(i++, obj.getInventoryGroupCode());
            callableStatement.setString(i++, obj.getInventoryGroupDescription());
            callableStatement.setString(i++,
                    obj.getMarketCommodityInd() == null ? null : obj.getMarketCommodityInd() ? "Y" : "N");
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z40PrtcpntRefSuplDtl obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z40(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getPriorYearSupplementalKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getOperationNumber());
            callableStatement.setInt(i++, obj.getProductionUnit());
            callableStatement.setInt(i++, obj.getInventoryTypeCode());
            callableStatement.setInt(i++, obj.getInventoryCode());
            callableStatement.setDouble(i++, obj.getQuantityStart());
            callableStatement.setDouble(i++, obj.getStartingPrice());
            callableStatement.setDouble(i++, obj.getCropOnFarmAcres());
            callableStatement.setDouble(i++, obj.getCropQtyProduced());
            callableStatement.setDouble(i++, obj.getQuantityEnd());
            callableStatement.setDouble(i++, obj.getEndYearProducerPrice());
            callableStatement.setString(i++,
                    obj.isAcceptProducerPrice() == null ? null : obj.isAcceptProducerPrice() ? "Y" : "N");
            callableStatement.setDouble(i++, obj.getEndYearPrice());
            callableStatement.setDouble(i++, obj.getAarmReferenceP1Price());
            callableStatement.setDouble(i++, obj.getAarmReferenceP2Price());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z42ParticipantRefYear obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z42(?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getProductiveCapacityKey());
            callableStatement.setInt(i++, obj.getRefOperationNumber());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getProductiveTypeCode());
            callableStatement.setInt(i++, obj.getProductiveCode());
            callableStatement.setDouble(i++, obj.getProductiveCapacityUnits());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z50ParticipntBnftCalc obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z50(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getBenefitCalcKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setInt(i++, obj.getAgristabilityStatus());
            callableStatement.setDouble(i++, obj.getUnadjustedReferenceMargin());
            callableStatement.setDouble(i++, obj.getAdjustedReferenceMargin());
            callableStatement.setDouble(i++, obj.getProgramMargin());
            callableStatement.setString(i++, obj.isWholeFarm() == null ? null : obj.isWholeFarm() ? "Y" : "N");
            callableStatement.setString(i++,
                    obj.isStructureChange() == null ? null : obj.isStructureChange() ? "Y" : "N");
            callableStatement.setDouble(i++, obj.getStructureChangeAdjAmount());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z51ParticipantContrib obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z51(?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setInt(i++, obj.getContributionKey());
            callableStatement.setInt(i++, obj.getParticipantPin());
            callableStatement.setInt(i++, obj.getProgramYear());
            callableStatement.setDouble(i++, obj.getProvincialContributions());
            callableStatement.setDouble(i++, obj.getFederalContributions());
            callableStatement.setDouble(i++, obj.getInterimContributions());
            callableStatement.setDouble(i++, obj.getProducerShare());
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

}
