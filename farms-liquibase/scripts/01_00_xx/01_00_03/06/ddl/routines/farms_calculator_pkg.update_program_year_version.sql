create or replace procedure farms_calculator_pkg.update_program_year_version(
    in in_program_year_version_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_farm_years farms.farm_program_year_versions.farm_years%type,
    in in_common_share_total farms.farm_program_year_versions.common_share_total%type,
    in in_federal_status_code farms.farm_program_year_versions.federal_status_code%type,
    in in_municipality_code farms.farm_program_year_versions.municipality_code%type,
    in in_participant_profile_code farms.farm_program_year_versions.participant_profile_code%type,
    in in_province_of_residence farms.farm_program_year_versions.province_of_residence%type,
    in in_province_of_main_farmstead farms.farm_program_year_versions.province_of_main_farmstead%type,
    in in_other_text farms.farm_program_year_versions.other_text%type,
    in in_accrual_cash_conversion_ind farms.farm_program_year_versions.accrual_cash_conversion_ind%type,
    in in_accrual_worksheet_ind farms.farm_program_year_versions.accrual_worksheet_ind%type,
    in in_can_send_cob_to_rep_ind farms.farm_program_year_versions.can_send_cob_to_rep_ind%type,
    in in_combined_farm_ind farms.farm_program_year_versions.combined_farm_ind%type,
    in in_completed_prod_cycle_ind farms.farm_program_year_versions.completed_prod_cycle_ind%type,
    in in_coop_member_ind farms.farm_program_year_versions.coop_member_ind%type,
    in in_corporate_shareholder_ind farms.farm_program_year_versions.corporate_shareholder_ind%type,
    in in_cwb_worksheet_ind farms.farm_program_year_versions.cwb_worksheet_ind%type,
    in in_disaster_ind farms.farm_program_year_versions.disaster_ind%type,
    in in_last_year_farming_ind farms.farm_program_year_versions.last_year_farming_ind%type,
    in in_partnership_member_ind farms.farm_program_year_versions.partnership_member_ind%type,
    in in_perishable_commodities_ind farms.farm_program_year_versions.perishable_commodities_ind%type,
    in in_receipts_ind farms.farm_program_year_versions.receipts_ind%type,
    in in_sole_proprietor_ind farms.farm_program_year_versions.sole_proprietor_ind%type,
    in in_post_mark_date farms.farm_program_year_versions.post_mark_date%type,
    in in_received_date farms.farm_program_year_versions.received_date%type,
    in in_revision_count farms.farm_program_year_versions.revision_count%type,
    in in_user farms.farm_program_year_versions.who_updated%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin
    update farms.farm_program_year_versions pyv
    set pyv.municipality_code = in_municipality_code,
        pyv.province_of_residence = in_province_of_residence,
        pyv.province_of_main_farmstead = in_province_of_main_farmstead,
        pyv.sole_proprietor_ind = in_sole_proprietor_ind,
        pyv.corporate_shareholder_ind = in_corporate_shareholder_ind,
        pyv.coop_member_ind = in_coop_member_ind,
        pyv.partnership_member_ind = in_partnership_member_ind,
        pyv.accrual_cash_conversion_ind = in_accrual_cash_conversion_ind,
        pyv.combined_farm_ind = in_combined_farm_ind,
        pyv.completed_prod_cycle_ind = in_completed_prod_cycle_ind,
        pyv.disaster_ind = in_disaster_ind,
        pyv.farm_years = in_farm_years,
        pyv.last_year_farming_ind = in_last_year_farming_ind,
        pyv.federal_status_code = in_federal_status_code,
        pyv.common_share_total = in_common_share_total,
        pyv.accrual_worksheet_ind = in_accrual_worksheet_ind,
        pyv.cwb_worksheet_ind = in_cwb_worksheet_ind,
        pyv.perishable_commodities_ind = in_perishable_commodities_ind,
        pyv.receipts_ind = in_receipts_ind,
        pyv.other_text = in_other_text,
        pyv.can_send_cob_to_rep_ind = in_can_send_cob_to_rep_ind,
        pyv.participant_profile_code = in_participant_profile_code,
        pyv.post_mark_date = in_post_mark_date,
        pyv.received_date = in_received_date,
        pyv.locally_updated_ind = 'Y',
        pyv.revision_count = pyv.revision_count + 1,
        pyv.who_updated = in_user,
        pyv.when_updated = current_timestamp
    where pyv.program_year_version_id = in_program_year_version_id
    and pyv.revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;
end;
$$;
