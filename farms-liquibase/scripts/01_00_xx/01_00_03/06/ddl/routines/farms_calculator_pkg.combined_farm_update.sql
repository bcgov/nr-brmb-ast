create or replace procedure farms_calculator_pkg.combined_farm_update(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type,
    in in_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type,
    in in_scenario_number farms.farm_agristability_scenarios.scenario_number%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios
    set combined_farm_number = null,
        who_updated = in_user,
        when_updated = current_timestamp
    where scenario_class_code = 'USER'
    and (combined_farm_number is not null and combined_farm_number::text <> '')
    and agristability_scenario_id in (
        select sv.agristability_scenario_id
        from farms.farm_scenarios_vw sv
        where sv.participant_pin = in_participant_pin
        and sv.year = in_program_year
    );

    update farms.farm_agristability_scenarios
    set combined_farm_number = in_combined_farm_number,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = (
        select sv.agristability_scenario_id
        from farms.farm_scenarios_vw sv
        where sv.participant_pin = in_participant_pin
        and sv.year = in_program_year
        and sv.scenario_number = in_scenario_number
    );

    call farms_calculator_pkg.log_combined_farm_update(in_combined_farm_number, in_user);

end;
$$;
