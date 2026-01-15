create or replace function farms_calculator_pkg.create_pyv(
    in in_py_id farms.farm_program_year_versions.program_year_id%type,
    in in_municipality_code farms.farm_program_year_versions.municipality_code%type,
    in in_user farms.farm_program_year_versions.who_created%type
) returns farms.farm_program_years.program_year_id%type
language plpgsql
as
$$
declare

    new_pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    new_pyv_number farms.farm_program_year_versions.program_year_version_number%type;

begin

    select nextval('farms.farm_pyv_seq')
    into new_pyv_id;

    select coalesce(max(pyv.program_year_version_number) + 1, 1)
    into new_pyv_number
    from farms.farm_program_year_versions pyv
    where pyv.program_year_id = in_py_id;

    insert into farms.farm_program_year_versions (
        program_year_version_id,
        program_year_version_number,
        form_version_number,
        form_version_effective_date,
        common_share_total,
        farm_years,
        accrual_worksheet_ind,
        completed_prod_cycle_ind,
        cwb_worksheet_ind,
        perishable_commodities_ind,
        receipts_ind,
        accrual_cash_conversion_ind,
        combined_farm_ind,
        coop_member_ind,
        corporate_shareholder_ind,
        disaster_ind,
        partnership_member_ind,
        sole_proprietor_ind,
        other_text,
        post_mark_date,
        province_of_residence,
        received_date,
        last_year_farming_ind,
        can_send_cob_to_rep_ind,
        province_of_main_farmstead,
        locally_updated_ind,
        program_year_id,
        participant_profile_code,
        municipality_code,
        import_version_id,
        federal_status_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select new_pyv_id as program_year_version_id,
           new_pyv_number as program_year_version_number,
           99 form_version_number,
           current_timestamp::date form_version_effective_date,
           null as common_share_total,
           null farm_years,
           'N' accrual_worksheet_ind,
           'N' completed_prod_cycle_ind,
           'N' cwb_worksheet_ind,
           'N' perishable_commodities_ind,
           'N' receipts_ind,
           'N' accrual_cash_conversion_ind,
           'N' combined_farm_ind,
           'N' coop_member_ind,
           'N' corporate_shareholder_ind,
           'N' disaster_ind,
           'N' partnership_member_ind,
           'N' sole_proprietor_ind,
           null as other_text,
           null as post_mark_date,
           null province_of_residence,
           null as received_date,
           'N' last_year_farming_ind,
           'N' as can_send_cob_to_rep_ind,
           null province_of_main_farmstead,
           'N' as locally_updated_ind,
           in_py_id as program_year_id,
           3 participant_profile_code,
           in_municipality_code,
           null import_version_id,
           3 federal_status_code,
           1 revision_count,
           in_user as who_created,
           current_timestamp as when_created,
           in_user as who_updated,
           current_timestamp as when_updated;

    return new_pyv_id;
end;
$$;
