create or replace function farms_read_pkg.read_pyv_sc(
    in sc_ids bigint[]
)
returns table(
    agristability_scenario_id       farms.farm_agristability_scenarios.agristability_scenario_id%type,
    scenario_number                 farms.farm_agristability_scenarios.scenario_number%type,
    benefits_calculator_version     farms.farm_agristability_scenarios.benefits_calculator_version%type,
    scenario_created_by             farms.farm_agristability_scenarios.scenario_created_by%type,
    description                     farms.farm_agristability_scenarios.description%type,
    default_ind                     farms.farm_agristability_scenarios.default_ind%type,
    scenario_creation_date          farms.farm_agristability_scenarios.scenario_creation_date%type,
    program_year_version_id         farms.farm_agristability_scenarios.program_year_version_id%type,
    used_in_calc_ind                farms.farm_reference_scenarios.used_in_calc_ind%type,
    -- For CRA scenario calculations. REF scenarios created from CRA
    -- are defaulted to Y so we can safely default a CRA scenario to Y.
    deemed_farming_year_ind         farms.farm_reference_scenarios.deemed_farming_year_ind%type,
    scenario_class_code             farms.farm_agristability_scenarios.scenario_class_code%type,
    scenario_type_description       farms.farm_scenario_class_codes.description%type,
    participnt_data_src_code        farms.farm_agristability_scenarios.participnt_data_src_code%type,
    scenario_state_code             farms.farm_agristability_scenarios.scenario_state_code%type,
    scenario_state_description      farms.farm_scenario_state_codes.description%type,
    scenario_category_code          farms.farm_scenario_category_codes.scenario_category_code%type,
    scenario_category_code_desc     farms.farm_scenario_category_codes.description%type,
    revision_count                  farms.farm_agristability_scenarios.revision_count%type,
    when_updated                    farms.farm_agristability_scenarios.when_updated%type,
    cra_income_expns_received_date  farms.farm_agristability_scenarios.cra_income_expns_received_date%type,
    cra_supplemental_received_date  farms.farm_agristability_scenarios.cra_supplemental_received_date%type,
    local_statement_a_receivd_date  farms.farm_program_years.local_statement_a_receivd_date%type,
    local_supplemntl_received_date  farms.farm_program_years.local_supplemntl_received_date%type,
    chef_submission_id              farms.farm_chef_submissions.chef_submission_id%type,
    crm_task_guid                   farms.farm_chef_submissions.main_task_guid%type,
    chef_submission_guid            farms.farm_chef_submissions.chef_submission_guid%type,
    is_in_combined_farm_ind         varchar,
    combined_farm_number            farms.farm_agristability_scenarios.combined_farm_number%type,
    chef_form_type_code             farms.farm_chef_submissions.chef_form_type_code%type,
    verifier_user_id                farms.farm_agristability_scenarios.verifier_user_id%type,
    verified_by_email               farms.farm_users.email_address%type,
    verifier_account_name           farms.farm_users.account_name%type
)
language sql
as $$
    with scenarios as(
        select sc.agristability_scenario_id,
               pyv.program_year_version_id,
               py.program_year_id
        from farms.farm_agristability_scenarios sc
        join farms.farm_program_year_versions pyv on sc.program_year_version_id = pyv.program_year_version_id
        join farms.farm_program_years py on pyv.program_year_id = py.program_year_id
        where sc.agristability_scenario_id = any(sc_ids)
    )
    select sc.agristability_scenario_id,
           sc.scenario_number,
           sc.benefits_calculator_version,
           sc.scenario_created_by,
           sc.description,
           sc.default_ind,
           sc.scenario_creation_date,
           sc.program_year_version_id,
           refs.used_in_calc_ind,
           -- For CRA scenario calculations. REF scenarios created from CRA
           -- are defaulted to Y so we can safely default a CRA scenario to Y.
           coalesce(refs.deemed_farming_year_ind, 'Y') deemed_farming_year_ind,
           sc.scenario_class_code,
           stc.description scenario_type_description,
           sc.participnt_data_src_code,
           sc.scenario_state_code,
           ssc.description scenario_state_description,
           scc.scenario_category_code,
           scc.description scenario_category_code_desc,
           sc.revision_count,
           sc.when_updated,
           sc.cra_income_expns_received_date,
           sc.cra_supplemental_received_date,
           py.local_statement_a_receivd_date,
           py.local_supplemntl_received_date,
           cs.chef_submission_id,
           cs.main_task_guid crm_task_guid,
           cs.chef_submission_guid,
           case when sc.combined_farm_number is not null then 'Y' else 'N' end is_in_combined_farm_ind,
           (case
               when sc.scenario_class_code = 'USER' and sc.scenario_state_code in ('COMP', 'AMEND', 'EN_COMP', 'PREVERIFID') then sc.combined_farm_number
               when sc.scenario_class_code = 'USER' and sc.scenario_state_code = 'IP' then (
                   select min(s2.combined_farm_number)
                   from scenarios sv
                   join farms.farm_program_year_versions pyv on pyv.program_year_version_id = sv.program_year_version_id
                   join scenarios sv2 on sv2.program_year_id = sv.program_year_id
                   join farms.farm_agristability_scenarios s2 on s2.agristability_scenario_id = sv2.agristability_scenario_id
                   join farms.farm_program_year_versions pyv2 on pyv2.program_year_version_id = sv2.program_year_version_id
                   where sv.agristability_scenario_id = sc.agristability_scenario_id
                   and s2.scenario_class_code = 'USER'
                   and s2.scenario_state_code = 'IP'
                   and pyv2.municipality_code = pyv.municipality_code
                   and s2.combined_farm_number is not null
               )
           end) combined_farm_number,
           cs.chef_form_type_code,
           sc.verifier_user_id,
           u.email_address verified_by_email,
           u.account_name verifier_account_name
    from farms.farm_agristability_scenarios sc
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = sc.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    join farms.farm_scenario_class_codes stc on stc.scenario_class_code = sc.scenario_class_code
    join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = sc.scenario_state_code
    join farms.farm_scenario_category_codes scc on scc.scenario_category_code = sc.scenario_category_code
    -- this join is never going to result in multiple rows
    left join farms.farm_reference_scenarios refs on refs.agristability_scenario_id = sc.agristability_scenario_id
    left outer join farms.farm_chef_submissions cs on cs.chef_submission_id = sc.chef_submission_id
    left outer join farms.farm_users u on u.user_id = sc.verifier_user_id
    where sc.agristability_scenario_id = any(sc_ids);
$$;
