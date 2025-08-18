package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

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
import ca.bc.gov.farms.persistence.v1.dto.staging.Z99ExtractFileCtl;

public class StagingDaoImpl extends BaseDaoImpl implements StagingDao {

    private static final long serialVersionUID = 1L;

    private Connection conn;

    public StagingDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void clear() throws SQLException {
        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_staging_pkg.clear()")) {

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z01ParticipantInfo obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z01(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            callableStatement.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
            callableStatement.setString(i++, obj.getSinCtnBn());
            callableStatement.setString(i++, obj.getSin());
            callableStatement.setString(i++, obj.getBusinessNumber());
            callableStatement.setString(i++, obj.getTrustNumber());
            callableStatement.setObject(i++, toShort(obj.getParticipantTypeCode()), Types.SMALLINT);
            callableStatement.setObject(i++, toShort(obj.getParticipantLanguage()), Types.SMALLINT);
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
            callableStatement.setObject(i++, toShort(obj.getPublicOffice()), Types.SMALLINT);
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

            callableStatement.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
            callableStatement.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);
            callableStatement.setObject(i++, toShort(obj.getFormVersionNumber()), Types.SMALLINT);

            callableStatement.setString(i++, obj.getProvinceOfResidence());
            callableStatement.setString(i++, obj.getProvinceOfMainFarmstead());
            callableStatement.setString(i++, obj.getPostmarkDate());
            callableStatement.setString(i++, obj.getReceivedDate());

            callableStatement.setString(i++, toString(obj.isSoleProprietor()));
            callableStatement.setString(i++, toString(obj.isPartnershipMember()));
            callableStatement.setString(i++, toString(obj.isCorporateShareholder()));
            callableStatement.setString(i++, toString(obj.isCoopMember()));

            callableStatement.setObject(i++, obj.getCommonShareTotal(), Types.INTEGER);
            callableStatement.setObject(i++, toShort(obj.getFarmYears()), Types.SMALLINT);
            callableStatement.setString(i++, toString(obj.isLastYearFarming()));
            callableStatement.setObject(i++, toShort(obj.getFormOriginCode()), Types.SMALLINT);
            callableStatement.setObject(i++, obj.getIndustryCode(), Types.INTEGER);
            callableStatement.setObject(i++, toShort(obj.getParticipantProfileCode()), Types.SMALLINT);

            callableStatement.setString(i++, toString(obj.isAccrualCashConversion()));
            callableStatement.setString(i++, toString(obj.isPerishableCommodities()));
            callableStatement.setString(i++, toString(obj.isReceipts()));
            callableStatement.setString(i++, toString(obj.isOtherText()));
            callableStatement.setString(i++, obj.getOtherText());

            callableStatement.setString(i++, toString(obj.isAccrualWorksheet()));
            callableStatement.setString(i++, toString(obj.isCwbWorksheet()));
            callableStatement.setString(i++, toString(obj.isCombinedThisYear()));
            callableStatement.setString(i++, toString(obj.isCompletedProdCycle()));
            callableStatement.setString(i++, toString(obj.isDisaster()));
            callableStatement.setString(i++, toString(obj.isCopyCobToContact()));

            callableStatement.setObject(i++, toShort(obj.getMunicipalityCode()), Types.SMALLINT);
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

            callableStatement.setObject(i++, toShort(obj.getOperationNumber()), Types.SMALLINT);
            callableStatement.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
            callableStatement.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);

            callableStatement.setObject(i++, obj.getPartnershipPin(), Types.INTEGER);
            callableStatement.setString(i++, obj.getPartnershipName());
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getPartnershipPercent()));
            callableStatement.setString(i++, obj.getFiscalYearStart());
            callableStatement.setString(i++, obj.getFiscalYearEnd());
            callableStatement.setObject(i++, toBigDecimal(obj.getAccountingCode()), Types.SMALLINT);

            callableStatement.setString(i++, toString(obj.isLandlord()));
            callableStatement.setString(i++, toString(obj.isCropShare()));
            callableStatement.setString(i++, toString(obj.isFeederMember()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getGrossIncome()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getExpenses()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getNetIncomeBeforeAdj()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOtherDeductions()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryAdjustments()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getNetIncomeAfterAdj()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getBusinessUseOfHomeExpenses()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getNetFarmIncome()));
            callableStatement.setString(i++, toString(obj.isCropDisaster()));
            callableStatement.setString(i++, toString(obj.isLivestockDisaster()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getIncomeExpenseKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getLineCode()));
            callableStatement.setString(i++, obj.getIe());
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getAmount()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getPartnerInfoKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getPartnershipPin()));
            callableStatement.setString(i++, obj.getPartnerFirstName());
            callableStatement.setString(i++, obj.getPartnerLastName());
            callableStatement.setString(i++, obj.getPartnerCorpName());
            callableStatement.setString(i++, obj.getPartnerSinCtnBn());
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getPartnerPercent()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getPartnerPin()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryTypeCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getCropUnitType()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getCropOnFarmAcres()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getCropQtyProduced()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getQuantityEnd()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getEndOfYearPrice()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getEndOfYearAmount()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getCropUnseedableAcres()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductionInsuranceKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityAmount()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductionUnit()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryTypeCode()));
            callableStatement.setString(i++, obj.getInventoryDesc());
            callableStatement.setString(i++, obj.getInventoryTypeDescription());
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryGroupCode()));
            callableStatement.setString(i++, obj.getInventoryGroupDescription());
            callableStatement.setString(i++, toString(obj.getMarketCommodityInd()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getPriorYearSupplementalKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductionUnit()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryTypeCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getQuantityStart()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getStartingPrice()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getCropOnFarmAcres()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getCropQtyProduced()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getQuantityEnd()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getEndYearProducerPrice()));
            callableStatement.setString(i++, toString(obj.isAcceptProducerPrice()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getEndYearPrice()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getAarmReferenceP1Price()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getAarmReferenceP2Price()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getRefOperationNumber()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductiveTypeCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductiveCode()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityUnits()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getBenefitCalcKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getAgristabilityStatus()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getUnadjustedReferenceMargin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getAdjustedReferenceMargin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramMargin()));
            callableStatement.setString(i++, toString(obj.isWholeFarm()));
            callableStatement.setString(i++, toString(obj.isStructureChange()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getStructureChangeAdjAmount()));
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

            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getContributionKey()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProvincialContributions()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getFederalContributions()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getInterimContributions()));
            callableStatement.setBigDecimal(i++, toBigDecimal(obj.getProducerShare()));
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void insert(final Z99ExtractFileCtl obj, final String userId) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_staging_pkg.insert_z99(?, ?, ?, ?)")) {

            callableStatement.setObject(i++, toShort(obj.getExtractFileNumber()), Types.SMALLINT);
            callableStatement.setString(i++, obj.getExtractDate());
            callableStatement.setObject(i++, obj.getRowCount(), Types.INTEGER);
            callableStatement.setString(i++, userId);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void status(Long pImportVersionId, String pString) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_import_pkg.update_status(?, ?)")) {

            callableStatement.setLong(i++, pImportVersionId);
            callableStatement.setString(i++, pString);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void statusNonAutonomous(Long pImportVersionId, String pString) throws SQLException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_import_pkg.update_status_non_autonomous(?, ?)")) {

            callableStatement.setLong(i++, pImportVersionId);
            callableStatement.setString(i++, pString);

            callableStatement.execute();
        } catch (SQLException e) {
            throw e;
        }
    }

}
