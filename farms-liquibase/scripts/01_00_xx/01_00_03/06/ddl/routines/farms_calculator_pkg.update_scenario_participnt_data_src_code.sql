create or replace procedure farms_calculator_pkg.update_scenario_participnt_data_src_code(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_participnt_data_src_code farms.farm_agristability_scenarios.participnt_data_src_code%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios
    set participnt_data_src_code = in_participnt_data_src_code,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = in_agristability_scenario_id;

end;
$$;
