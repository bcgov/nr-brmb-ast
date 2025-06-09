create or replace function farms_read_pkg.read_pyv(
    in pyv_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select py.program_year_id,
               py.year program_year,
               py.assigned_to_user_guid,
               py.assigned_to_userid,
               py.revision_count py_revision_count,
               py.create_user py_create_user,

               pyv.program_year_version_id,
               pyv.program_year_version_number,
               pyv.form_version_number,
               pyv.common_share_total,
               pyv.farm_years,
               pyv.accrual_worksheet_indicator,
               pyv.completed_production_cycle_indicator,
               pyv.cwb_worksheet_indicator,
               pyv.perishable_commodities_indicator,
               pyv.receipts_indicator,
               pyv.accrual_cash_conversion_indicator,
               pyv.combined_farm_indicator,
               pyv.coop_member_indicator,
               pyv.corporate_shareholder_indicator,
               pyv.disaster_indicator,
               pyv.partnership_member_indicator,
               pyv.sole_proprietor_indicator,
               pyv.other_text,
               pyv.post_mark_date,
               pyv.province_of_residence,
               pyv.received_date,
               pyv.last_year_farming_indicator,
               null description_of_change,
               pyv.can_send_cob_to_representative_indicator,
               pyv.province_of_main_farmstead,
               pyv.locally_updated_indicator,
               pyv.participant_profile_code,
               ppc.description participant_profile_desc,
               pyv.municipality_code,
               mmc.description municipality_code_description,
               pyv.federal_status_code,
               fsc.description federal_status_description,
               py.non_participant_indicator,
               py.late_participant_indicator,
               py.cash_margins_indicator,
               py.cash_margins_opt_in_date,
               pyv.revision_count pyv_revision_count,

               iv.import_version_id,
               iv.imported_by_user,
               iv.description,
               iv.staging_audit_information,
               iv.import_file_name,
               iv.import_control_file_date,
               iv.import_control_file_information,
               iv.import_date,
               iv.import_state_code,
               iv.revision_count iv_revision_count

        from farms.program_year py
        join farms.agristability_client ac on py.agristability_client_id = ac.agristability_client_id
        -- will always have at least one version
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.federal_status_code fsc on fsc.federal_status_code = pyv.federal_status_code
        join farms.participant_profile_code ppc on pyv.participant_profile_code = ppc.participant_profile_code
        left outer join farms.import_version iv on pyv.import_version_id = iv.import_version_id
        left outer join farms.municipality_code mmc on mmc.municipality_code = pyv.municipality_code
        -- want this year + 6 historical
        where pyv.program_year_version_id = any(pyv_ids)
        order by py.year desc;

    return cur;

end;
$$;
