create or replace function farms_read_pkg.read_pyv_sc(
    in sc_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
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
               py.local_statement_a_received_date,
               py.local_supplemntl_received_date,
               cs.chef_submission_id,
               cs.main_task_guid crm_task_guid,
               cs.chef_submission_guid,
               case when cs.combined_farm_number is not null then 'Y' else 'N' end is_in_combined_farm_indicator,
               (case
                   when sc.scenario_class_code = 'USER' and sc.scenario_state_code in ('COMP', 'AMEND', 'EN_COMP', 'PREVERIFID') then sc.combined_farm_number
                   when sc.scenario_class_code = 'USER' and sc.scenario_state_code = 'IP' then (
                       select min(s2.combined_farm_number)
                       from farms.farm_agri_scenarios_vw m
                       join farms.farm_program_year_versions pyv on pyv.program_year_version_id = m.program_year_version_id
                       join farms.farm_agri_scenarios_vw m2 on m2.program_year_id = m.program_year_id
                       join farms.farm_agristability_scenarios s2 on s2.agristability_scenario_id = m2.agristability_scenario_id
                       join farms.farm_program_year_versions pyv2 on pyv2.program_year_version_id = m2.program_year_version_id
                       where m.agristability_scenario_id = sc.agristability_scenario_id
                       and s2.scenario_class_code = 'USER'
                       and s2.scenario_state_code = 'IP'
                       and pyv2.municipality_code = pyv.municipality_code
                       and s2.combined_farm_number is not null
                   )
               end) combined_farm_number,
               cs.chef_form_type_code,
               sc.verified_user_id,
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
        left outer join farms.farm_users u on u.user_id = sc.verified_user_id
        where sc.agristability_scenario_id = any(sc_ids);
    return cur;
end;
$$;
