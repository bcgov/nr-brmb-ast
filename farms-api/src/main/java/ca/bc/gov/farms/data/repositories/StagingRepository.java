package ca.bc.gov.farms.data.repositories;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.data.entities.staging.Z02PartpntFarmInfo;
import ca.bc.gov.farms.data.entities.staging.Z03StatementInfo;
import ca.bc.gov.farms.data.entities.staging.Z04IncomeExpsDtl;
import ca.bc.gov.farms.data.entities.staging.Z05PartnerInfo;
import ca.bc.gov.farms.data.entities.staging.Z21ParticipantSuppl;
import ca.bc.gov.farms.data.entities.staging.Z22ProductionInsurance;
import ca.bc.gov.farms.data.entities.staging.Z23LivestockProdCpct;
import ca.bc.gov.farms.data.entities.staging.Z28ProdInsuranceRef;
import ca.bc.gov.farms.data.entities.staging.Z29InventoryRef;
import ca.bc.gov.farms.data.entities.staging.Z40PrtcpntRefSuplDtl;
import ca.bc.gov.farms.data.entities.staging.Z42ParticipantRefYear;
import ca.bc.gov.farms.data.entities.staging.Z50ParticipntBnftCalc;
import ca.bc.gov.farms.data.entities.staging.Z51ParticipantContrib;
import ca.bc.gov.farms.data.entities.staging.Z99ExtractFileCtl;

@Repository
public class StagingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clear() {

        jdbcTemplate.execute("call farms_staging_pkg.clear()", (CallableStatement cs) -> {

            cs.execute();

            return null;
        });
    }

    public void insert(final Z01ParticipantInfo obj, final String userId) {

        jdbcTemplate.execute(
                "call farms_staging_pkg.insert_z01(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
                    cs.setString(i++, obj.getSinCtnBn());
                    cs.setString(i++, obj.getSin());
                    cs.setString(i++, obj.getBusinessNumber());
                    cs.setString(i++, obj.getTrustNumber());
                    cs.setObject(i++, toShort(obj.getParticipantTypeCode()), Types.SMALLINT);
                    cs.setObject(i++, toShort(obj.getParticipantLanguage()), Types.SMALLINT);
                    cs.setString(i++, obj.getFirstName());
                    cs.setString(i++, obj.getLastName());
                    cs.setString(i++, obj.getCorpName());
                    cs.setString(i++, obj.getAddress1());
                    cs.setString(i++, obj.getAddress2());
                    cs.setString(i++, obj.getCity());
                    cs.setString(i++, obj.getProvince());
                    cs.setString(i++, obj.getPostalCode());
                    cs.setString(i++, obj.getCountry());
                    cs.setString(i++, obj.getParticipantFax());
                    cs.setString(i++, obj.getParticipantPhoneDay());
                    cs.setString(i++, obj.getParticipantPhoneEvening());
                    cs.setString(i++, obj.getParticipantPhoneCell());
                    cs.setString(i++, obj.getParticipantEmail());
                    cs.setString(i++, obj.getContactFirstName());
                    cs.setString(i++, obj.getContactLastName());
                    cs.setString(i++, obj.getContactBusinessName());
                    cs.setString(i++, obj.getContactAddress1());
                    cs.setString(i++, obj.getContactAddress2());
                    cs.setString(i++, obj.getContactCity());
                    cs.setString(i++, obj.getContactProvince());
                    cs.setString(i++, obj.getContactPostalCode());
                    cs.setString(i++, obj.getContactPhoneDay());
                    cs.setString(i++, obj.getContactFaxNumber());
                    cs.setString(i++, obj.getContactPhoneCell());
                    cs.setObject(i++, toShort(obj.getPublicOffice()), Types.SMALLINT);
                    cs.setString(i++, obj.getIdentEffectiveDate());
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z02PartpntFarmInfo obj, final String userId) {

        jdbcTemplate.execute(
                "call farms_staging_pkg.insert_z02(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);
                    cs.setObject(i++, toShort(obj.getFormVersionNumber()), Types.SMALLINT);

                    cs.setString(i++, obj.getProvinceOfResidence());
                    cs.setString(i++, obj.getProvinceOfMainFarmstead());
                    cs.setString(i++, obj.getPostmarkDate());
                    cs.setString(i++, obj.getReceivedDate());

                    cs.setString(i++, toString(obj.isSoleProprietor()));
                    cs.setString(i++, toString(obj.isPartnershipMember()));
                    cs.setString(i++, toString(obj.isCorporateShareholder()));
                    cs.setString(i++, toString(obj.isCoopMember()));

                    cs.setObject(i++, obj.getCommonShareTotal(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getFarmYears()), Types.SMALLINT);
                    cs.setString(i++, toString(obj.isLastYearFarming()));
                    cs.setObject(i++, toShort(obj.getFormOriginCode()), Types.SMALLINT);
                    cs.setObject(i++, obj.getIndustryCode(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getParticipantProfileCode()), Types.SMALLINT);

                    cs.setString(i++, toString(obj.isAccrualCashConversion()));
                    cs.setString(i++, toString(obj.isPerishableCommodities()));
                    cs.setString(i++, toString(obj.isReceipts()));
                    cs.setString(i++, toString(obj.isOtherText()));
                    cs.setString(i++, obj.getOtherText());

                    cs.setString(i++, toString(obj.isAccrualWorksheet()));
                    cs.setString(i++, toString(obj.isCwbWorksheet()));
                    cs.setString(i++, toString(obj.isCombinedThisYear()));
                    cs.setString(i++, toString(obj.isCompletedProdCycle()));
                    cs.setString(i++, toString(obj.isDisaster()));
                    cs.setString(i++, toString(obj.isCopyCobToContact()));

                    cs.setObject(i++, toShort(obj.getMunicipalityCode()), Types.SMALLINT);
                    cs.setString(i++, obj.getFormVersionEffectiveDate());
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z03StatementInfo obj, final String userId) {

        jdbcTemplate.execute(
                "call farms_staging_pkg.insert_z03(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setObject(i++, toShort(obj.getOperationNumber()), Types.SMALLINT);
                    cs.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);

                    cs.setObject(i++, obj.getPartnershipPin(), Types.INTEGER);
                    cs.setString(i++, obj.getPartnershipName());
                    cs.setBigDecimal(i++, toBigDecimal(obj.getPartnershipPercent()));
                    cs.setString(i++, obj.getFiscalYearStart());
                    cs.setString(i++, obj.getFiscalYearEnd());
                    cs.setObject(i++, toBigDecimal(obj.getAccountingCode()), Types.SMALLINT);

                    cs.setString(i++, toString(obj.isLandlord()));
                    cs.setString(i++, toString(obj.isCropShare()));
                    cs.setString(i++, toString(obj.isFeederMember()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getGrossIncome()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getExpenses()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getNetIncomeBeforeAdj()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getOtherDeductions()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryAdjustments()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getNetIncomeAfterAdj()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getBusinessUseOfHomeExpenses()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getNetFarmIncome()));
                    cs.setString(i++, toString(obj.isCropDisaster()));
                    cs.setString(i++, toString(obj.isLivestockDisaster()));
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z04IncomeExpsDtl obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z04(?, ?, ?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setObject(i++, toLong(obj.getIncomeExpenseKey()), Types.BIGINT);
            cs.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
            cs.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);
            cs.setObject(i++, toShort(obj.getOperationNumber()), Types.SMALLINT);
            cs.setObject(i++, toShort(obj.getLineCode()), Types.SMALLINT);
            cs.setString(i++, obj.getIe());
            cs.setBigDecimal(i++, toBigDecimal(obj.getAmount()));
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z05PartnerInfo obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z05(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setObject(i++, toLong(obj.getPartnerInfoKey()), Types.BIGINT);
                    cs.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);
                    cs.setObject(i++, toShort(obj.getOperationNumber()), Types.SMALLINT);

                    cs.setObject(i++, obj.getPartnershipPin(), Types.INTEGER);
                    cs.setString(i++, obj.getPartnerFirstName());
                    cs.setString(i++, obj.getPartnerLastName());
                    cs.setString(i++, obj.getPartnerCorpName());
                    cs.setString(i++, obj.getPartnerSinCtnBn());
                    cs.setBigDecimal(i++, toBigDecimal(obj.getPartnerPercent()));
                    cs.setObject(i++, obj.getPartnerPin(), Types.INTEGER);
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z21ParticipantSuppl obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z21(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setObject(i++, toLong(obj.getInventoryKey()), Types.BIGINT);
                    cs.setObject(i++, obj.getParticipantPin(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getProgramYear()), Types.SMALLINT);
                    cs.setObject(i++, toShort(obj.getOperationNumber()), Types.SMALLINT);
                    cs.setObject(i++, toShort(obj.getInventoryTypeCode()), Types.SMALLINT);
                    cs.setObject(i++, obj.getInventoryCode(), Types.INTEGER);
                    cs.setObject(i++, toShort(obj.getCropUnitType()), Types.SMALLINT);
                    cs.setBigDecimal(i++, toBigDecimal(obj.getCropOnFarmAcres()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getCropQtyProduced()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getQuantityEnd()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getEndOfYearPrice()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getEndOfYearAmount()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getCropUnseedableAcres()));
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z22ProductionInsurance obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z22(?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductionInsuranceKey()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
            cs.setString(i++, obj.getProductionInsuranceNumber());
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z23LivestockProdCpct obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z23(?, ?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityKey()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityAmount()));
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z28ProdInsuranceRef obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z28(?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setObject(i++, toShort(obj.getProductionUnit()), Types.SMALLINT);
            cs.setString(i++, obj.getProductionUnitDescription());
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z29InventoryRef obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z29(?, ?, ?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryTypeCode()));
            cs.setString(i++, obj.getInventoryDesc());
            cs.setString(i++, obj.getInventoryTypeDescription());
            cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryGroupCode()));
            cs.setString(i++, obj.getInventoryGroupDescription());
            cs.setString(i++, toString(obj.getMarketCommodityInd()));
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z40PrtcpntRefSuplDtl obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z40(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setBigDecimal(i++, toBigDecimal(obj.getPriorYearSupplementalKey()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getOperationNumber()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getProductionUnit()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryTypeCode()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getInventoryCode()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getQuantityStart()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getStartingPrice()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getCropOnFarmAcres()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getCropQtyProduced()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getQuantityEnd()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getEndYearProducerPrice()));
                    cs.setString(i++, toString(obj.isAcceptProducerPrice()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getEndYearPrice()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getAarmReferenceP1Price()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getAarmReferenceP2Price()));
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z42ParticipantRefYear obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z42(?, ?, ?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityKey()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getRefOperationNumber()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductiveTypeCode()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductiveCode()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProductiveCapacityUnits()));
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z50ParticipntBnftCalc obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z50(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    int i = 1;
                    cs.setBigDecimal(i++, toBigDecimal(obj.getBenefitCalcKey()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getAgristabilityStatus()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getUnadjustedReferenceMargin()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getAdjustedReferenceMargin()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getProgramMargin()));
                    cs.setString(i++, toString(obj.isWholeFarm()));
                    cs.setString(i++, toString(obj.isStructureChange()));
                    cs.setBigDecimal(i++, toBigDecimal(obj.getStructureChangeAdjAmount()));
                    cs.setString(i++, userId);

                    cs.execute();

                    return null;
                });
    }

    public void insert(final Z51ParticipantContrib obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z51(?, ?, ?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setBigDecimal(i++, toBigDecimal(obj.getContributionKey()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getParticipantPin()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProgramYear()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProvincialContributions()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getFederalContributions()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getInterimContributions()));
            cs.setBigDecimal(i++, toBigDecimal(obj.getProducerShare()));
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void insert(final Z99ExtractFileCtl obj, final String userId) {

        jdbcTemplate.execute("call farms_staging_pkg.insert_z99(?, ?, ?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setObject(i++, toShort(obj.getExtractFileNumber()), Types.SMALLINT);
            cs.setString(i++, obj.getExtractDate());
            cs.setObject(i++, obj.getRowCount(), Types.INTEGER);
            cs.setString(i++, userId);

            cs.execute();

            return null;
        });
    }

    public void status(Long pImportVersionId, String pString) {

        jdbcTemplate.execute("call farms_import_pkg.update_status(?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setLong(i++, pImportVersionId);
            cs.setString(i++, pString);

            cs.execute();

            return null;
        });
    }

    public void statusNonAutonomous(Long pImportVersionId, String pString) {

        jdbcTemplate.execute("call farms_import_pkg.update_status_non_autonomous(?, ?)", (CallableStatement cs) -> {

            int i = 1;
            cs.setLong(i++, pImportVersionId);
            cs.setString(i++, pString);

            cs.execute();

            return null;
        });
    }

    private Short toShort(Integer i) {
        return i == null ? null : i.shortValue();
    }

    private String toString(Boolean b) {
        return b == null ? null : b ? "Y" : "N";
    }

    private BigDecimal toBigDecimal(Double d) {
        return d == null ? null : BigDecimal.valueOf(d);
    }

    private BigDecimal toBigDecimal(Integer i) {
        return i == null ? null : BigDecimal.valueOf(i);
    }

    private Long toLong(Integer i) {
        return i == null ? null : i.longValue();
    }

}
