create or replace procedure farms_write_pkg.write_calc_version(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_version farms.farm_agristability_scenarios.benefits_calculator_version%type,
    in in_user_id farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin
    update farms.farm_agristability_scenarios
    set benefits_calculator_version = in_version,
        when_updated = current_timestamp,
        who_updated = in_user_id
    where agristability_scenario_id = in_scenario_id
    or agristability_scenario_id in (
        select agristability_scenario_id
        from farms.farm_reference_scenarios
        where for_agristability_scenario_id = in_scenario_id
    );
end;
$$;
