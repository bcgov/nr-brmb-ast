create or replace function farms_webapp_pkg.get_ip_sc_combined_farm_number(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type
) returns farms.farm_agristability_scenarios.combined_farm_number%type
language plpgsql
as
$$
declare

    v_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type;

begin
    select min(sc.combined_farm_number)
    into v_combined_farm_number
    from farms.farm_scenarios_vw sv
    join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = sv.agristability_scenario_id
    where sv.participant_pin = in_participant_pin
    and sv.year = in_program_year
    and sc.scenario_class_code = 'USER'
    and sc.scenario_state_code = 'IP'
    and (sc.combined_farm_number is not null and sc.combined_farm_number::text <> '');
    return v_combined_farm_number;
end;
$$;
