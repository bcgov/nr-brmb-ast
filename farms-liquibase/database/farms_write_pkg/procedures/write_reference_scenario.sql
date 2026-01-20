create or replace procedure farms_write_pkg.write_reference_scenario(
    in in_for_scenario_id farms.farm_reference_scenarios.for_agristability_scenario_id%type,
    in in_scenario_id farms.farm_reference_scenarios.agristability_scenario_id%type,
    in in_used_in_calc_ind farms.farm_reference_scenarios.used_in_calc_ind%type,
    in in_deemed_farming_year_ind farms.farm_reference_scenarios.deemed_farming_year_ind%type,
    in in_user_id farms.farm_reference_scenarios.who_updated%type
)
language plpgsql
as
$$
begin
    update farms.farm_reference_scenarios
    set used_in_calc_ind = in_used_in_calc_ind,
        deemed_farming_year_ind = in_deemed_farming_year_ind,
        when_updated = current_timestamp,
        who_updated = in_user_id
    where for_agristability_scenario_id = in_for_scenario_id
    and agristability_scenario_id = in_scenario_id;
end;
$$;
