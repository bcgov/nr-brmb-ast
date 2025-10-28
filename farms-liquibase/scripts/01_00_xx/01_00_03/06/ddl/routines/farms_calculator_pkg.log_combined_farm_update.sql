create or replace procedure farms_calculator_pkg.log_combined_farm_update(
    in in_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
declare

    v_pins_in_combined_farm farms.farm_scenario_logs.log_message%type;

    sc_rec record;

begin

    select string_agg('[PIN: '||sv.participant_pin||', Scenario Number: '||sv.scenario_number||']', '; ' order by sv.participant_pin, sv.scenario_number)
    into v_pins_in_combined_farm
    from farms.farm_agristability_scenarios sc
    join farms.farm_scenarios_vw sv on sv.agristability_scenario_id = sc.agristability_scenario_id
    where sc.combined_farm_number = in_combined_farm_number;

    for sc_rec in (
        select s2.agristability_scenario_id
        from farms.farm_agristability_scenarios s
        join farms.farm_scenarios_vw sv on sv.agristability_scenario_id = s.agristability_scenario_id
        join farms.farm_scenarios_vw sv2 on sv2.program_year_id = sv.program_year_id
        join farms.farm_agristability_scenarios s2 on s2.agristability_scenario_id = sv2.agristability_scenario_id
        where s2.scenario_class_code = 'USER'
        and s2.scenario_state_code = 'IP'
        and s.combined_farm_number = in_combined_farm_number
    )
    loop
        call farms_calculator_pkg.sc_log(sc_rec.agristability_scenario_id, 'Combined Farm updated. ' || v_pins_in_combined_farm, in_user);
    end loop;

end;
$$;
