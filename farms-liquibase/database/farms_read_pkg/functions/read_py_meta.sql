create or replace function farms_read_pkg.read_py_meta(
    in ppin farms.farm_agristability_clients.participant_pin%type,
    in pyear farms.farm_program_years.year%type
)
returns table(
    agristability_scenario_id       farms.farm_agristability_scenarios.agristability_scenario_id%type,
    benefits_calculator_version     farms.farm_agristability_scenarios.benefits_calculator_version%type,
    default_ind                     farms.farm_agristability_scenarios.default_ind%type,
    program_year                    farms.farm_agri_scenarios_vw.year%type,
    scenario_state_code             farms.farm_agristability_scenarios.scenario_state_code%type,
    scenario_state_desc             farms.farm_scenario_state_codes.description%type,
    program_year_version_number     farms.farm_agri_scenarios_vw.program_year_version_number%type,
    program_year_version_id         farms.farm_agri_scenarios_vw.program_year_version_id%type,
    revision_count                  farms.farm_agristability_scenarios.revision_count%type,
    scenario_creation_date          farms.farm_agristability_scenarios.scenario_creation_date%type,
    scenario_description            farms.farm_agristability_scenarios.description%type,
    scenario_created_by             farms.farm_agristability_scenarios.scenario_created_by%type,
    scenario_number                 farms.farm_agri_scenarios_vw.scenario_number%type,
    scenario_class_code             farms.farm_agristability_scenarios.scenario_class_code%type,
    scenario_class_desc             farms.farm_scenario_class_codes.description%type,
    scenario_category_code          farms.farm_scenario_category_codes.scenario_category_code%type,
    scenario_category_code_desc     farms.farm_scenario_category_codes.description%type,
    combined_farm_number            farms.farm_agristability_scenarios.combined_farm_number%type,
    chef_submission_id              farms.farm_agristability_scenarios.chef_submission_id%type,
    chef_submission_guid            farms.farm_chef_submissions.chef_submission_guid%type,
    participnt_data_src_code        farms.farm_agristability_scenarios.participnt_data_src_code%type,
    municipality_code               farms.farm_program_year_versions.municipality_code%type
)
language sql
as $$
    select sc.agristability_scenario_id,
           sc.benefits_calculator_version,
           sc.default_ind,
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
           sc.participnt_data_src_code,
           fpyv.municipality_code
    from farms.farm_agri_scenarios_vw m
    join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = m.agristability_scenario_id
    join farms.farm_scenario_state_codes ssc on ssc.scenario_state_code = sc.scenario_state_code
    join farms.farm_scenario_class_codes stc on stc.scenario_class_code = sc.scenario_class_code
    join farms.farm_scenario_category_codes scc on scc.scenario_category_code = sc.scenario_category_code
    join farms.farm_program_year_versions fpyv on fpyv.program_year_version_id = m.program_year_version_id
    left outer join farms.farm_chef_submissions csub on csub.chef_submission_id = sc.chef_submission_id
    where m.participant_pin = ppin
    and m.year between (pyear-5) and pyear
    order by m.year desc,
                m.program_year_version_number desc,
                m.scenario_number desc;
$$;
