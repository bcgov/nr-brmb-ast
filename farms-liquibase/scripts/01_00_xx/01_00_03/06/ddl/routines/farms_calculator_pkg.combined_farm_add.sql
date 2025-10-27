create or replace procedure farms_calculator_pkg.combined_farm_add(
    in in_cur_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_pin_to_add farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type,
    in in_user farms.farm_agristability_scenarios.who_updated%type
)
language plpgsql
as
$$
declare

    v_pin_exists bigint;
    v_ip_scenario_exists bigint;
    v_municipality_code farms.farm_program_year_versions.municipality_code%type;
    v_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type;
    v_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type;
    v_other_combined_farm_number farms.farm_agristability_scenarios.combined_farm_number%type;
    v_scenario_id_to_add farms.farm_agristability_scenarios.agristability_scenario_id%type;
    v_add_current_pin boolean := false;

begin

    v_pin_exists := farms_calculator_pkg.pin_exists(in_pin_to_add);
    if v_pin_exists != 1 then
        raise exception '%', farms_types_pkg.pin_does_not_exist_msg()
        using errcode = farms_types_pkg.pin_does_not_exist_num()::text;
    end if;

    select pyv.municipality_code,
           s.scenario_category_code
    into v_municipality_code,
         v_scenario_category_code
    from farms.farm_agristability_scenarios s
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
    where s.agristability_scenario_id = in_cur_scenario_id;

    v_ip_scenario_exists := farms_calculator_pkg.matching_sc_exists(in_pin_to_add, in_program_year, v_municipality_code, v_scenario_category_code);
    if v_ip_scenario_exists != 1 then
        raise exception '%', farms_types_pkg.in_prog_scenario_not_exist_msg()
        using errcode = farms_types_pkg.in_prog_scenario_not_exist_num()::text;
    end if;

    v_other_combined_farm_number := farms_webapp_pkg.get_ip_sc_combined_farm_number(in_pin_to_add, in_program_year);
    if (v_other_combined_farm_number is not null and v_other_combined_farm_number::text <> '') then
        raise exception '%', farms_types_pkg.pin_already_in_comb_farm_msg()
        using errcode = farms_types_pkg.pin_already_in_comb_farm_num()::text;
    end if;


    select min(s2.combined_farm_number)
    into v_combined_farm_number
    from farms.farm_scenarios_vw sv
    join farms.farm_scenarios_vw sv2 on sv2.program_year_id = sv.program_year_id
    join farms.farm_agristability_scenarios s2 on s2.agristability_scenario_id = sv2.agristability_scenario_id
    where sv.agristability_scenario_id = in_cur_scenario_id
    and s2.scenario_class_code = 'USER'
    and s2.scenario_state_code = 'IP'
    and (s2.combined_farm_number is not null and s2.combined_farm_number::text <> '');

    if coalesce(v_combined_farm_number::text, '') = '' then
        v_add_current_pin := true;

        select nextval('farms.farm_as_cfn_seq')
        into v_combined_farm_number;
    end if;

    v_scenario_id_to_add := farms_calculator_pkg.cf_get_sc_id_to_add(in_pin_to_add, in_program_year);

    if v_add_current_pin then
        update farms.farm_agristability_scenarios
        set combined_farm_number = v_combined_farm_number,
            who_updated = in_user,
            when_updated = current_timestamp
        where agristability_scenario_id = in_cur_scenario_id;
    end if;

    update farms.farm_agristability_scenarios
    set combined_farm_number = v_combined_farm_number,
        who_updated = in_user,
        when_updated = clock_timestamp()
    where agristability_scenario_id = v_scenario_id_to_add;

    call farms_calculator_pkg.log_combined_farm_update(v_combined_farm_number, in_user);

end;
$$;
