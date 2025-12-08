create or replace procedure farms_calculator_pkg.combined_farm_remove(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
declare

    v_scenario_count bigint;

    sc_rec record;

begin

    update farms.farm_agristability_scenarios
    set combined_farm_number = null,
        who_updated = in_user,
        when_updated = current_timestamp
    where agristability_scenario_id = in_scenario_id;

    for sc_rec in (
        select s2.agristability_scenario_id
        from farms.farm_scenarios_vw sv
        join farms.farm_scenarios_vw sv2 on sv2.program_year_id = sv.program_year_id
        join farms.farm_agristability_scenarios s2 on s2.agristability_scenario_id = sv2.agristability_scenario_id
        where s2.scenario_class_code = 'USER'
        and s2.scenario_state_code = 'IP'
        and sv.agristability_scenario_id = in_scenario_id
    )
    loop
        call farms_calculator_pkg.sc_log(sc_rec.agristability_scenario_id, 'Removed from Combined Farm', in_user);
    end loop;

    select count(*)
    into v_scenario_count
    from farms.farm_agristability_scenarios sc
    where sc.combined_farm_number = in_combined_farm_number;

    if v_scenario_count < 2 then

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
            update farms.farm_agristability_scenarios
            set combined_farm_number = null,
                who_updated = in_user,
                when_updated = current_timestamp
            where agristability_scenario_id = sc_rec.agristability_scenario_id;

            call farms_calculator_pkg.sc_log(sc_rec.agristability_scenario_id, 'Removed from Combined Farm', in_user);
        end loop;
    else
        call farms_calculator_pkg.log_combined_farm_update(in_combined_farm_number, in_user);
    end if;

end;
$$;
