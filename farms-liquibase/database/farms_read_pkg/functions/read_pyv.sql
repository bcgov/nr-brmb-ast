create or replace function farms_read_pkg.read_pyv(
    in pyv_ids bigint[]
)
returns table(
    program_year_id                 farms.farm_program_years.program_year_id%type,
    program_year                    farms.farm_program_years.year%type,
    assigned_to_user_guid           farms.farm_program_years.assigned_to_user_guid%type,
    assigned_to_userid              farms.farm_program_years.assigned_to_userid%type,
    py_revision_count               farms.farm_program_years.revision_count%type,
    py_when_created                 farms.farm_program_years.when_created%type,

    program_year_version_id         farms.farm_program_year_versions.program_year_version_id%type,
    program_year_version_number     farms.farm_program_year_versions.program_year_version_number%type,
    form_version_number             farms.farm_program_year_versions.form_version_number%type,
    common_share_total              farms.farm_program_year_versions.common_share_total%type,
    farm_years                      farms.farm_program_year_versions.farm_years%type,
    accrual_worksheet_ind           farms.farm_program_year_versions.accrual_worksheet_ind%type,
    completed_prod_cycle_ind        farms.farm_program_year_versions.completed_prod_cycle_ind%type,
    cwb_worksheet_ind               farms.farm_program_year_versions.cwb_worksheet_ind%type,
    perishable_commodities_ind      farms.farm_program_year_versions.perishable_commodities_ind%type,
    receipts_ind                    farms.farm_program_year_versions.receipts_ind%type,
    accrual_cash_conversion_ind     farms.farm_program_year_versions.accrual_cash_conversion_ind%type,
    combined_farm_ind               farms.farm_program_year_versions.combined_farm_ind%type,
    coop_member_ind                 farms.farm_program_year_versions.coop_member_ind%type,
    corporate_shareholder_ind       farms.farm_program_year_versions.corporate_shareholder_ind%type,
    disaster_ind                    farms.farm_program_year_versions.disaster_ind%type,
    partnership_member_ind          farms.farm_program_year_versions.partnership_member_ind%type,
    sole_proprietor_ind             farms.farm_program_year_versions.sole_proprietor_ind%type,
    other_text                      farms.farm_program_year_versions.other_text%type,
    post_mark_date                  farms.farm_program_year_versions.post_mark_date%type,
    province_of_residence           farms.farm_program_year_versions.province_of_residence%type,
    received_date                   farms.farm_program_year_versions.received_date%type,
    last_year_farming_ind           farms.farm_program_year_versions.last_year_farming_ind%type,
    description_of_change           text,
    can_send_cob_to_rep_ind         farms.farm_program_year_versions.can_send_cob_to_rep_ind%type,
    province_of_main_farmstead      farms.farm_program_year_versions.province_of_main_farmstead%type,
    locally_updated_ind             farms.farm_program_year_versions.locally_updated_ind%type,
    participant_profile_code        farms.farm_program_year_versions.participant_profile_code%type,
    participant_profile_desc        farms.farm_participant_profile_codes.description%type,
    municipality_code               farms.farm_program_year_versions.municipality_code%type,
    municipality_code_description   farms.farm_municipality_codes.description%type,
    federal_status_code             farms.farm_program_year_versions.federal_status_code%type,
    federal_status_description      farms.farm_federal_status_codes.description%type,
    non_participant_ind             farms.farm_program_years.non_participant_ind%type,
    late_participant_ind            farms.farm_program_years.late_participant_ind%type,
    cash_margins_ind                farms.farm_program_years.cash_margins_ind%type,
    cash_margins_opt_in_date        farms.farm_program_years.cash_margins_opt_in_date%type,
    pyv_revision_count              farms.farm_program_year_versions.revision_count%type,

    import_version_id               farms.farm_import_versions.import_version_id%type,
    imported_by_user                farms.farm_import_versions.imported_by_user%type,
    description                     farms.farm_import_versions.description%type,
    staging_audit_info              farms.farm_import_versions.staging_audit_info%type,
    import_file_name                farms.farm_import_versions.import_file_name%type,
    import_control_file_date        farms.farm_import_versions.import_control_file_date%type,
    import_control_file_info        farms.farm_import_versions.import_control_file_info%type,
    import_date                     farms.farm_import_versions.import_date%type,
    import_state_code               farms.farm_import_versions.import_state_code%type,
    iv_revision_count               farms.farm_import_versions.revision_count%type
)
language sql
as $$
    select py.program_year_id,
           py.year program_year,
           py.assigned_to_user_guid,
           py.assigned_to_userid,
           py.revision_count py_revision_count,
           py.when_created py_when_created,

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
$$;
