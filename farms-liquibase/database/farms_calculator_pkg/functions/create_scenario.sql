create or replace function farms_calculator_pkg.create_scenario(
    in in_pyv_id farms.farm_agristability_scenarios.program_year_version_id%type,
    in in_scenario_class_code farms.farm_agristability_scenarios.scenario_class_code%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_user farms.farm_agristability_scenarios.who_created%type
) returns farms.farm_agristability_scenarios.agristability_scenario_id%type
language plpgsql
as
$$
declare

    new_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;

begin
    select nextval('farms.farm_as_seq')
    into new_agristability_scenario_id;

    insert into farms.farm_agristability_scenarios (
        agristability_scenario_id,
        scenario_number,
        scenario_created_by,
        default_ind,
        scenario_creation_date,
        program_year_version_id,
        scenario_class_code,
        scenario_state_code,
        scenario_category_code,
        participnt_data_src_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated)
    select new_agristability_scenario_id agristability_scenario_id,
           (
               select coalesce(max(sv.scenario_number), 0) + 1
               from farms.farm_program_year_versions pyv
               join farms.farm_scenarios_vw sv on sv.program_year_id = pyv.program_year_id
               where pyv.program_year_version_id = in_pyv_id
           ) scenario_number,
           in_user scenario_created_by,
           'N' default_ind,
           current_timestamp scenario_creation_date,
           in_pyv_id program_year_version_id,
           in_scenario_class_code scenario_class_code,
           'REC' scenario_state_code,
           in_scenario_category_code scenario_category_code,
           'LOCAL' participnt_data_src_code,
           1 revision_count,
           in_user who_created,
           current_timestamp when_created,
           in_user who_updated,
           current_timestamp when_updated;

    return new_agristability_scenario_id;
end;
$$;
