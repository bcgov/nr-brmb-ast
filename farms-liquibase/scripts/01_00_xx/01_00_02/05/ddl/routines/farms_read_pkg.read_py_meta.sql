create or replace function farms_read_pkg.read_py_meta(
    in ppin farms.agristability_client.participant_pin%type,
    in pyear farms.program_year.year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select sc.agristability_scenario_id,
               sc.benefits_calculator_version,
               sc.default_indicator,
               m.year program_year,
               sc.scenario_state_code,
               ssc.description scenario_state_desc,
               m.program_year_version_number,
               m.program_year_version_id,
               sc.revision_count,
               sc.scenario_creation_date,
               sc.description scenario_description,
               sc.scenario_created_by,
               m.scenario_number,
               sc.scenario_class_code,
               stc.description scenario_class_desc,
               scc.scenario_category_code,
               scc.description scenario_category_code_desc,
               sc.combined_farm_number,
               sc.chef_submission_id,
               csub.chef_submission_guid,
               sc.participant_data_source_code,
               fpyv.municipality_code
        from farms.agri_scenarios_vw m
        join farms.agristability_scenario sc on sc.agristability_scenario_id = m.agristability_scenario_id
        join farms.scenario_state_code ssc on ssc.scenario_state_code = sc.scenario_state_code
        join farms.scenario_class_code stc on stc.scenario_class_code = sc.scenario_class_code
        join farms.scenario_category_code scc on scc.scenario_category_code = sc.scenario_category_code
        join farms.program_year_version fpyv on fpyv.program_year_version_id = m.program_year_version_id
        left outer join farms.chef_submission csub on csub.chef_submission_id = sc.chef_submission_id
        where m.participant_pin = ppin
        and m.year between (pyear-5) and pyear
        order by m.year desc,
                 m.program_year_version_number desc,
                 m.scenario_number desc;

    return cur;

end;
$$;
