create or replace function farms_calculator_pkg.cf_get_sc_id_to_add(
    in in_pin_to_add farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type
) returns farms.farm_agristability_scenarios.combined_farm_number%type
language plpgsql
as
$$
declare

    v_scenario_id_to_add farms.farm_agristability_scenarios.agristability_scenario_id%type;

begin

    select scenario_id
    into v_scenario_id_to_add
    from (
        select max(sv.agristability_scenario_id) over (order by sc.default_ind desc, sv.scenario_number desc) scenario_id,
               row_number() over () as row_num
        from farms.farm_scenarios_vw sv
        join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = sv.agristability_scenario_id
        where sv.participant_pin = in_pin_to_add
        and sv.year = in_program_year
        and sc.scenario_class_code = 'USER'
        and sc.scenario_state_code = 'IP'
    ) alias2
    where row_num = 1;

    return v_scenario_id_to_add;
end;
$$;
