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
               py.who_created py_create_user,

               pyv.program_year_version_id,
               pyv.program_year_version_number,
               pyv.form_version_number,
               pyv.common_share_total,
               pyv.farm_years,
               pyv.accrual_worksheet_ind,
               pyv.completed_prod_cycle_ind,
               pyv.cwb_worksheet_ind,
               pyv.perishable_commodities_ind,
               pyv.receipts_ind,
               pyv.accrual_cash_conversion_ind,
               pyv.combined_farm_ind,
               pyv.coop_member_ind,
               pyv.corporate_shareholder_ind,
               pyv.disaster_ind,
               pyv.partnership_member_ind,
               pyv.sole_proprietor_ind,
               pyv.other_text,
               pyv.post_mark_date,
               pyv.province_of_residence,
               pyv.received_date,
               pyv.last_year_farming_ind,
               null description_of_change,
               pyv.can_send_cob_to_rep_ind,
               pyv.province_of_main_farmstead,
               pyv.locally_updated_ind,
               pyv.participant_profile_code,
               ppc.description participant_profile_desc,
               pyv.municipality_code,
               mmc.description municipality_code_description,
               pyv.federal_status_code,
               fsc.description federal_status_description,
               py.non_participant_ind,
               py.late_participant_ind,
               py.cash_margins_ind,
               py.cash_margins_opt_in_date,
               pyv.revision_count pyv_revision_count,

               iv.import_version_id,
               iv.imported_by_user,
               iv.description,
               iv.staging_audit_info,
               iv.import_file_name,
               iv.import_control_file_date,
               iv.import_control_file_info,
               iv.import_date,
               iv.import_state_code,
               iv.revision_count iv_revision_count

        from farms.farm_program_years py
        join farms.farm_agristability_clients ac on py.agristability_client_id = ac.agristability_client_id
        -- will always have at least one version
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_federal_status_codes fsc on fsc.federal_status_code = pyv.federal_status_code
        join farms.farm_participant_profile_codes ppc on pyv.participant_profile_code = ppc.participant_profile_code
        left outer join farms.farm_import_versions iv on pyv.import_version_id = iv.import_version_id
        left outer join farms.farm_municipality_codes mmc on mmc.municipality_code = pyv.municipality_code
        -- want this year + 6 historical
        where pyv.program_year_version_id = any(pyv_ids)
        order by py.year desc;

    return cur;

end;
$$;
