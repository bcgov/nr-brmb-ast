create or replace procedure farms_calculator_pkg.close_pyv_other_scenarios(
    in in_program_year_version_id farms.farm_farming_operations.program_year_version_id%type,
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_state_change_reason farms.farm_scenario_state_audits.state_change_reason%type,
    in in_user farms.farm_farming_operations.who_updated%type
)
language plpgsql
as
$$
declare

    active_scenarios_curs cursor for
        select sc.agristability_scenario_id
        from farms.farm_agristability_scenarios sc
        where sc.program_year_version_id = in_program_year_version_id
        and sc.scenario_class_code = 'USER'
        and sc.scenario_state_code = 'IP'
        and sc.scenario_category_code != 'NOL'
        and sc.agristability_scenario_id != in_scenario_id;

begin

    for active_scenario in active_scenarios_curs
    loop
        call farms_calculator_pkg.update_scenario_state(active_scenario.agristability_scenario_id, 'CLO', in_state_change_reason, in_user);
        call farms_calculator_pkg.sc_log(active_scenario.agristability_scenario_id, 'State changed to Closed. ' || in_state_change_reason, in_user);
    end loop;
end;
$$;
