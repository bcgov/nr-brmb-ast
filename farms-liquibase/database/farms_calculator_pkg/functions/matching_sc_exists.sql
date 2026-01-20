create or replace function farms_calculator_pkg.matching_sc_exists(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type,
    in in_municipality_code farms.farm_program_year_versions.municipality_code%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type
) returns bigint
language plpgsql
as
$$
declare

    v_ip_scenario_exists bigint;

begin
    select case when exists (
        select null
        from farms.farm_scenarios_vw sv
        join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = sv.agristability_scenario_id
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = sc.program_year_version_id
        where sv.participant_pin = in_participant_pin
        and sv.year = in_program_year
        and sc.scenario_class_code = 'USER'
        and sc.scenario_state_code = 'IP'
        and sc.scenario_category_code != 'UNK'
        /* Per FARM-949. Temporarily remove the condition that the category must match while the logic is reconsidered.
                         Exception: If it is an Interim the category must match.
        and (sc.scenario_category_code = in_scenario_category_code
             or (in_scenario_category_code != 'INT' and sc.scenario_category_code != 'INT'))*/
        and pyv.municipality_code = in_municipality_code
    ) then 1 else 0 end
    into v_ip_scenario_exists;

    return v_ip_scenario_exists;
end;
$$;
