package ca.bc.gov.farms.data.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.lang.NonNull;
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

    private final SimpleJdbcCall clearCall;
    private final SimpleJdbcCall insertZ01Call;
    private final SimpleJdbcCall insertZ02Call;
    private final SimpleJdbcCall insertZ03Call;
    private final SimpleJdbcCall insertZ04Call;
    private final SimpleJdbcCall insertZ05Call;
    private final SimpleJdbcCall insertZ21Call;
    private final SimpleJdbcCall insertZ22Call;
    private final SimpleJdbcCall insertZ23Call;
    private final SimpleJdbcCall insertZ28Call;
    private final SimpleJdbcCall insertZ29Call;
    private final SimpleJdbcCall insertZ40Call;
    private final SimpleJdbcCall insertZ42Call;
    private final SimpleJdbcCall insertZ50Call;
    private final SimpleJdbcCall insertZ51Call;
    private final SimpleJdbcCall insertZ99Call;
    private final SimpleJdbcCall statusCall;
    private final SimpleJdbcCall statusNonAutonomousCall;

    public StagingRepository(@NonNull JdbcTemplate jdbcTemplate) {
        this.clearCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("clear");

        this.insertZ01Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z01");

        this.insertZ02Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z02");

        this.insertZ03Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z03");

        this.insertZ04Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z04");

        this.insertZ05Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z05");

        this.insertZ21Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z21");

        this.insertZ22Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z22");

        this.insertZ23Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z23");

        this.insertZ28Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z28");

        this.insertZ29Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z29");

        this.insertZ40Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z40");

        this.insertZ42Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z42");

        this.insertZ50Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z50");

        this.insertZ51Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z51");

        this.insertZ99Call = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_staging_pkg")
                .withProcedureName("insert_z99");

        this.statusCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_import_pkg")
                .withProcedureName("update_status");

        this.statusNonAutonomousCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_import_pkg")
                .withProcedureName("update_status_non_autonomous");
    }

    public void clear() {

        clearCall.execute();
    }

    public void insert(final Z01ParticipantInfo obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_participant_pin", obj.getParticipantPin());
                put("in_sin_ctn_bn", obj.getSinCtnBn());
                put("in_sin", obj.getSin());
                put("in_business_number", obj.getBusinessNumber());
                put("in_trust_number", obj.getTrustNumber());
                put("in_participant_type_code", DbTypeConverters.toShort(obj.getParticipantTypeCode()));
                put("in_participant_language", DbTypeConverters.toShort(obj.getParticipantLanguage()));
                put("in_first_name", obj.getFirstName());
                put("in_last_name", obj.getLastName());
                put("in_corp_name", obj.getCorpName());
                put("in_address_1", obj.getAddress1());
                put("in_address_2", obj.getAddress2());
                put("in_city", obj.getCity());
                put("in_province", obj.getProvince());
                put("in_postal_code", obj.getPostalCode());
                put("in_country", obj.getCountry());
                put("in_participant_fax", obj.getParticipantFax());
                put("in_participant_phone_day", obj.getParticipantPhoneDay());
                put("in_participant_phone_evening", obj.getParticipantPhoneEvening());
                put("in_participant_cell_number", obj.getParticipantPhoneCell());
                put("in_participant_email_address", obj.getParticipantEmail());
                put("in_contact_first_name", obj.getContactFirstName());
                put("in_contact_last_name", obj.getContactLastName());
                put("in_contact_business_name", obj.getContactBusinessName());
                put("in_contact_address_1", obj.getContactAddress1());
                put("in_contact_address_2", obj.getContactAddress2());
                put("in_contact_city", obj.getContactCity());
                put("in_contact_province", obj.getContactProvince());
                put("in_contact_postal_code", obj.getContactPostalCode());
                put("in_contact_phone_day", obj.getContactPhoneDay());
                put("in_contact_fax_number", obj.getContactFaxNumber());
                put("in_contact_cell_number", obj.getContactPhoneCell());
                put("in_public_office_ind", DbTypeConverters.toShort(obj.getPublicOffice()));
                put("in_ident_effective_date", obj.getIdentEffectiveDate());
                put("in_user", userId);
            }
        };

        insertZ01Call.execute(params);
    }

    public void insert(final Z02PartpntFarmInfo obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_participant_pin", obj.getParticipantPin());
                put("in_program_year", DbTypeConverters.toShort(obj.getProgramYear()));
                put("in_form_version_number", DbTypeConverters.toShort(obj.getFormVersionNumber()));

                put("in_province_of_residence", obj.getProvinceOfResidence());
                put("in_province_of_main_farmstead", obj.getProvinceOfMainFarmstead());
                put("in_postmark_date", obj.getPostmarkDate());
                put("in_received_date", obj.getReceivedDate());

                put("in_sole_proprietor_ind", DbTypeConverters.toYN(obj.isSoleProprietor()));
                put("in_partnership_member_ind", DbTypeConverters.toYN(obj.isPartnershipMember()));
                put("in_corporate_shareholder_ind", DbTypeConverters.toYN(obj.isCorporateShareholder()));
                put("in_coop_member_ind", DbTypeConverters.toYN(obj.isCoopMember()));

                put("in_common_share_total", obj.getCommonShareTotal());
                put("in_farm_years", DbTypeConverters.toShort(obj.getFarmYears()));
                put("in_last_year_farming_ind", DbTypeConverters.toYN(obj.isLastYearFarming()));
                put("in_form_origin_code", DbTypeConverters.toShort(obj.getFormOriginCode()));
                put("in_industry_code", obj.getIndustryCode());
                put("in_participant_profile_code", DbTypeConverters.toShort(obj.getParticipantProfileCode()));

                put("in_accrual_cash_conversion_ind", DbTypeConverters.toYN(obj.isAccrualCashConversion()));
                put("in_perishable_commodities_ind", DbTypeConverters.toYN(obj.isPerishableCommodities()));
                put("in_receipts_ind", DbTypeConverters.toYN(obj.isReceipts()));
                put("in_other_text_ind", DbTypeConverters.toYN(obj.isOtherText()));
                put("in_other_text", obj.getOtherText());

                put("in_accrual_worksheet_ind", DbTypeConverters.toYN(obj.isAccrualWorksheet()));
                put("in_cwb_worksheet_ind", DbTypeConverters.toYN(obj.isCwbWorksheet()));
                put("in_combined_this_year_ind", DbTypeConverters.toYN(obj.isCombinedThisYear()));
                put("in_completed_production_cycle_ind", DbTypeConverters.toYN(obj.isCompletedProdCycle()));
                put("in_disaster_ind", DbTypeConverters.toYN(obj.isDisaster()));
                put("in_copy_cob_to_contact_ind", DbTypeConverters.toYN(obj.isCopyCobToContact()));

                put("in_municipality_code", DbTypeConverters.toShort(obj.getMunicipalityCode()));
                put("in_form_version_effective_date", obj.getFormVersionEffectiveDate());
                put("in_user", userId);
            }
        };

        insertZ02Call.execute(params);
    }

    public void insert(final Z03StatementInfo obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_operation_number", DbTypeConverters.toShort(obj.getOperationNumber()));
                put("in_participant_pin", obj.getParticipantPin());
                put("in_program_year", DbTypeConverters.toShort(obj.getProgramYear()));

                put("in_partnership_pin", obj.getPartnershipPin());
                put("in_partnership_name", obj.getPartnershipName());
                put("in_partnership_percent", DbTypeConverters.toBigDecimal(obj.getPartnershipPercent()));
                put("in_fiscal_year_start", obj.getFiscalYearStart());
                put("in_fiscal_year_end", obj.getFiscalYearEnd());
                put("in_accounting_code", DbTypeConverters.toBigDecimal(obj.getAccountingCode()));

                put("in_landlord_ind", DbTypeConverters.toYN(obj.isLandlord()));
                put("in_crop_share_ind", DbTypeConverters.toYN(obj.isCropShare()));
                put("in_feeder_member_ind", DbTypeConverters.toYN(obj.isFeederMember()));
                put("in_gross_income", DbTypeConverters.toBigDecimal(obj.getGrossIncome()));
                put("in_expenses", DbTypeConverters.toBigDecimal(obj.getExpenses()));
                put("in_net_income_before_adjustments", DbTypeConverters.toBigDecimal(obj.getNetIncomeBeforeAdj()));
                put("in_other_deductions", DbTypeConverters.toBigDecimal(obj.getOtherDeductions()));
                put("in_inventory_adjustments", DbTypeConverters.toBigDecimal(obj.getInventoryAdjustments()));
                put("in_net_income_after_adjustments", DbTypeConverters.toBigDecimal(obj.getNetIncomeAfterAdj()));
                put("in_business_use_of_home_expenses",
                        DbTypeConverters.toBigDecimal(obj.getBusinessUseOfHomeExpenses()));
                put("in_net_farm_income", DbTypeConverters.toBigDecimal(obj.getNetFarmIncome()));
                put("in_crop_disaster_ind", DbTypeConverters.toYN(obj.isCropDisaster()));
                put("in_livestock_disaster_ind", DbTypeConverters.toYN(obj.isLivestockDisaster()));
                put("in_user", userId);
            }
        };

        insertZ03Call.execute(params);
    }

    public void insert(final Z04IncomeExpsDtl obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_income_expense_key", DbTypeConverters.toLong(obj.getIncomeExpenseKey()));
                put("in_participant_pin", obj.getParticipantPin());
                put("in_program_year", DbTypeConverters.toShort(obj.getProgramYear()));
                put("in_operation_number", DbTypeConverters.toShort(obj.getOperationNumber()));
                put("in_line_code", DbTypeConverters.toShort(obj.getLineCode()));
                put("in_ie_ind", obj.getIe());
                put("in_amount", DbTypeConverters.toBigDecimal(obj.getAmount()));
                put("in_user", userId);
            }
        };

        insertZ04Call.execute(params);
    }

    public void insert(final Z05PartnerInfo obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_partner_info_key", DbTypeConverters.toLong(obj.getPartnerInfoKey()));
                put("in_participant_pin", obj.getParticipantPin());
                put("in_program_year", DbTypeConverters.toShort(obj.getProgramYear()));
                put("in_operation_number", DbTypeConverters.toShort(obj.getOperationNumber()));

                put("in_partnership_pin", obj.getPartnershipPin());
                put("in_partner_first_name", obj.getPartnerFirstName());
                put("in_partner_last_name", obj.getPartnerLastName());
                put("in_partner_corp_name", obj.getPartnerCorpName());
                put("in_partner_sin_ctn_bn", obj.getPartnerSinCtnBn());
                put("in_partner_percent", DbTypeConverters.toBigDecimal(obj.getPartnerPercent()));
                put("in_partner_pin", obj.getPartnerPin());
                put("in_user", userId);
            }
        };

        insertZ05Call.execute(params);
    }

    public void insert(final Z21ParticipantSuppl obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_inventory_key", DbTypeConverters.toLong(obj.getInventoryKey()));
                put("in_participant_pin", obj.getParticipantPin());
                put("in_program_year", DbTypeConverters.toShort(obj.getProgramYear()));
                put("in_operation_number", DbTypeConverters.toShort(obj.getOperationNumber()));
                put("in_inventory_type_code", DbTypeConverters.toShort(obj.getInventoryTypeCode()));
                put("in_inventory_code", obj.getInventoryCode());
                put("in_crop_unit_type", DbTypeConverters.toShort(obj.getCropUnitType()));
                put("in_crop_on_farm_acres", DbTypeConverters.toBigDecimal(obj.getCropOnFarmAcres()));
                put("in_crop_qty_produced", DbTypeConverters.toBigDecimal(obj.getCropQtyProduced()));
                put("in_quantity_end", DbTypeConverters.toBigDecimal(obj.getQuantityEnd()));
                put("in_end_of_year_price", DbTypeConverters.toBigDecimal(obj.getEndOfYearPrice()));
                put("in_end_of_year_amount", DbTypeConverters.toBigDecimal(obj.getEndOfYearAmount()));
                put("in_crop_unseedable_acres", DbTypeConverters.toBigDecimal(obj.getCropUnseedableAcres()));
                put("in_user", userId);
            }
        };

        insertZ21Call.execute(params);
    }

    public void insert(final Z22ProductionInsurance obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_production_insurance_key", DbTypeConverters.toBigDecimal(obj.getProductionInsuranceKey()));
                put("in_participant_pin", DbTypeConverters.toBigDecimal(obj.getParticipantPin()));
                put("in_program_year", DbTypeConverters.toBigDecimal(obj.getProgramYear()));
                put("in_operation_number", DbTypeConverters.toBigDecimal(obj.getOperationNumber()));
                put("in_production_insurance_number", obj.getProductionInsuranceNumber());
                put("in_user", userId);
            }
        };

        insertZ22Call.execute(params);
    }

    public void insert(final Z23LivestockProdCpct obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_productive_capacity_key", DbTypeConverters.toBigDecimal(obj.getProductiveCapacityKey()));
                put("in_participant_pin", DbTypeConverters.toBigDecimal(obj.getParticipantPin()));
                put("in_program_year", DbTypeConverters.toBigDecimal(obj.getProgramYear()));
                put("in_operation_number", DbTypeConverters.toBigDecimal(obj.getOperationNumber()));
                put("in_inventory_code", DbTypeConverters.toBigDecimal(obj.getInventoryCode()));
                put("in_productive_capacity_amount", DbTypeConverters.toBigDecimal(obj.getProductiveCapacityAmount()));
                put("in_user", userId);
            }
        };

        insertZ23Call.execute(params);
    }

    public void insert(final Z28ProdInsuranceRef obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_production_unit", DbTypeConverters.toShort(obj.getProductionUnit()));
                put("in_production_unit_description", obj.getProductionUnitDescription());
                put("in_user", userId);
            }
        };

        insertZ28Call.execute(params);
    }

    public void insert(final Z29InventoryRef obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_inventory_code", DbTypeConverters.toBigDecimal(obj.getInventoryCode()));
                put("in_inventory_type_code", DbTypeConverters.toBigDecimal(obj.getInventoryTypeCode()));
                put("in_inventory_desc", obj.getInventoryDesc());
                put("in_inventory_type_description", obj.getInventoryTypeDescription());
                put("in_inventory_group_code", DbTypeConverters.toBigDecimal(obj.getInventoryGroupCode()));
                put("in_inventory_group_description", obj.getInventoryGroupDescription());
                put("in_market_commodity_ind", DbTypeConverters.toYN(obj.getMarketCommodityInd()));
                put("in_user", userId);
            }
        };

        insertZ29Call.execute(params);
    }

    public void insert(final Z40PrtcpntRefSuplDtl obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_prior_year_supplemental_key", DbTypeConverters.toBigDecimal(obj.getPriorYearSupplementalKey()));
                put("in_participant_pin", DbTypeConverters.toBigDecimal(obj.getParticipantPin()));
                put("in_program_year", DbTypeConverters.toBigDecimal(obj.getProgramYear()));
                put("in_operation_number", DbTypeConverters.toBigDecimal(obj.getOperationNumber()));
                put("in_production_unit", DbTypeConverters.toBigDecimal(obj.getProductionUnit()));
                put("in_inventory_type_code", DbTypeConverters.toBigDecimal(obj.getInventoryTypeCode()));
                put("in_inventory_code", DbTypeConverters.toBigDecimal(obj.getInventoryCode()));
                put("in_quantity_start", DbTypeConverters.toBigDecimal(obj.getQuantityStart()));
                put("in_starting_price", DbTypeConverters.toBigDecimal(obj.getStartingPrice()));
                put("in_crop_on_farm_acres", DbTypeConverters.toBigDecimal(obj.getCropOnFarmAcres()));
                put("in_crop_qty_produced", DbTypeConverters.toBigDecimal(obj.getCropQtyProduced()));
                put("in_quantity_end", DbTypeConverters.toBigDecimal(obj.getQuantityEnd()));
                put("in_end_year_producer_price", DbTypeConverters.toBigDecimal(obj.getEndYearProducerPrice()));
                put("in_accept_producer_price_ind", DbTypeConverters.toYN(obj.isAcceptProducerPrice()));
                put("in_end_year_price", DbTypeConverters.toBigDecimal(obj.getEndYearPrice()));
                put("in_aarm_reference_p1_price", DbTypeConverters.toBigDecimal(obj.getAarmReferenceP1Price()));
                put("in_aarm_reference_p2_price", DbTypeConverters.toBigDecimal(obj.getAarmReferenceP2Price()));
                put("in_user", userId);
            }
        };

        insertZ40Call.execute(params);
    }

    public void insert(final Z42ParticipantRefYear obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_productive_capacity_key", DbTypeConverters.toBigDecimal(obj.getProductiveCapacityKey()));
                put("in_ref_operation_number", DbTypeConverters.toBigDecimal(obj.getRefOperationNumber()));
                put("in_participant_pin", DbTypeConverters.toBigDecimal(obj.getParticipantPin()));
                put("in_program_year", DbTypeConverters.toBigDecimal(obj.getProgramYear()));
                put("in_productive_type_code", DbTypeConverters.toBigDecimal(obj.getProductiveTypeCode()));
                put("in_productive_code", DbTypeConverters.toBigDecimal(obj.getProductiveCode()));
                put("in_productive_capacity_units", DbTypeConverters.toBigDecimal(obj.getProductiveCapacityUnits()));
                put("in_user", userId);
            }
        };

        insertZ42Call.execute(params);
    }

    public void insert(final Z50ParticipntBnftCalc obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_benefit_calc_key", DbTypeConverters.toBigDecimal(obj.getBenefitCalcKey()));
                put("in_participant_pin", DbTypeConverters.toBigDecimal(obj.getParticipantPin()));
                put("in_program_year", DbTypeConverters.toBigDecimal(obj.getProgramYear()));
                put("in_agristability_status", DbTypeConverters.toBigDecimal(obj.getAgristabilityStatus()));
                put("in_unadjusted_reference_margin",
                        DbTypeConverters.toBigDecimal(obj.getUnadjustedReferenceMargin()));
                put("in_adjusted_reference_margin", DbTypeConverters.toBigDecimal(obj.getAdjustedReferenceMargin()));
                put("in_program_margin", DbTypeConverters.toBigDecimal(obj.getProgramMargin()));
                put("in_whole_farm_ind", DbTypeConverters.toYN(obj.isWholeFarm()));
                put("in_structure_change_ind", DbTypeConverters.toYN(obj.isStructureChange()));
                put("in_structure_change_adj_amount", DbTypeConverters.toBigDecimal(obj.getStructureChangeAdjAmount()));
                put("in_user", userId);
            }
        };

        insertZ50Call.execute(params);
    }

    public void insert(final Z51ParticipantContrib obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_contribution_key", DbTypeConverters.toBigDecimal(obj.getContributionKey()));
                put("in_participant_pin", DbTypeConverters.toBigDecimal(obj.getParticipantPin()));
                put("in_program_year", DbTypeConverters.toBigDecimal(obj.getProgramYear()));
                put("in_provincial_contributions", DbTypeConverters.toBigDecimal(obj.getProvincialContributions()));
                put("in_federal_contributions", DbTypeConverters.toBigDecimal(obj.getFederalContributions()));
                put("in_interim_contributions", DbTypeConverters.toBigDecimal(obj.getInterimContributions()));
                put("in_producer_share", DbTypeConverters.toBigDecimal(obj.getProducerShare()));
                put("in_user", userId);
            }
        };

        insertZ51Call.execute(params);
    }

    public void insert(final Z99ExtractFileCtl obj, final String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_extract_file_number", DbTypeConverters.toShort(obj.getExtractFileNumber()));
                put("in_extract_date", obj.getExtractDate());
                put("in_row_count", obj.getRowCount());
                put("in_user", userId);
            }
        };

        insertZ99Call.execute(params);
    }

    public void status(Long pImportVersionId, String pString) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", pImportVersionId);
                put("in_msg", pString);
            }
        };

        statusCall.execute(params);
    }

    public void statusNonAutonomous(Long pImportVersionId, String pString) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", pImportVersionId);
                put("in_msg", pString);
            }
        };

        statusNonAutonomousCall.execute(params);
    }

}
