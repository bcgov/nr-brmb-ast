create or replace procedure farms_calculator_pkg.update_scenario_category(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios
    set scenario_category_code = in_scenario_category_code
    where agristability_scenario_id = in_agristability_scenario_id;

    update farms.farm_agristability_scenarios
    set scenario_category_code = in_scenario_category_code,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id in (
        select rs.agristability_scenario_id
        from farms.farm_reference_scenarios rs
        where rs.for_agristability_scenario_id = in_agristability_scenario_id
    );

end;
$$;
