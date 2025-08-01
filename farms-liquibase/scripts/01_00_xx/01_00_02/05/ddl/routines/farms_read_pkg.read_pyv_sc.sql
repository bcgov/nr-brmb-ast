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
               sc.default_indicator,
               sc.scenario_creation_date,
               sc.program_year_version_id,
               refs.used_in_calculation_indicator,
               -- For CRA scenario calculations. REF scenarios created from CRA
               -- are defaulted to Y so we can safely default a CRA scenario to Y.
               coalesce(refs.deemed_farming_year_indicator, 'Y') deemed_farming_year_indicator,
               sc.scenario_class_code,
               stc.description scenario_type_description,
               sc.participant_data_source_code,
               sc.scenario_state_code,
               ssc.description scenario_state_description,
               scc.scenario_category_code,
               scc.description scenario_category_code_desc,
               sc.revision_count,
               sc.update_date,
               sc.cra_income_expense_received_date,
               sc.cra_supplemental_received_date,
               py.local_statement_a_received_date,
               py.local_supplemental_received_date,
               cs.chef_submission_id,
               cs.main_task_guid crm_task_guid,
               cs.chef_submission_guid,
               case when cs.combined_farm_number is not null then 'Y' else 'N' end is_in_combined_farm_indicator,
               (case
                   when sc.scenario_class_code = 'USER' and sc.scenario_state_code in ('COMP', 'AMEND', 'EN_COMP', 'PREVERIFID') then sc.combined_farm_number
                   when sc.scenario_class_code = 'USER' and sc.scenario_state_code = 'IP' then (
                       select min(s2.combined_farm_number)
                       from farms.agri_scenarios_vw m
                       join farms.program_year_version pyv on pyv.program_year_version_id = m.program_year_version_id
                       join farms.agri_scenarios_vw m2 on m2.program_year_id = m.program_year_id
                       join farms.agristability_scenario s2 on s2.agristability_scenario_id = m2.agristability_scenario_id
                       join farms.program_year_version pyv2 on pyv2.program_year_version_id = m2.program_year_version_id
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
        from farms.agristability_scenario sc
        join farms.program_year_version pyv on pyv.program_year_version_id = sc.program_year_version_id
        join farms.program_year py on py.program_year_id = pyv.program_year_id
        join farms.scenario_class_code stc on stc.scenario_class_code = sc.scenario_class_code
        join farms.scenario_state_code ssc on ssc.scenario_state_code = sc.scenario_state_code
        join farms.scenario_category_code scc on scc.scenario_category_code = sc.scenario_category_code
        -- this join is never going to result in multiple rows
        left join farms.reference_scenario refs on refs.agristability_scenario_id = sc.agristability_scenario_id
        left outer join farms.chef_submission cs on cs.chef_submission_id = sc.chef_submission_id
        left outer join farms.usr u on u.user_id = sc.verified_user_id
        where sc.agristability_scenario_id = any(sc_ids);
    return cur;
end;
$$;
