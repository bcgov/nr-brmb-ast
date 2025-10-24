create or replace function farms_calculator_pkg.create_year(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_year_to_create farms.farm_program_years.year%type,
    in in_num_operations bigint,
    in in_scenario_class_code farms.farm_scenario_class_codes.scenario_class_code%type,
    in in_scenario_category_code farms.farm_scenario_category_codes.scenario_category_code%type,
    in in_user farms.farm_agristability_clients.who_created%type
) returns farms.farm_program_year_versions.program_year_version_id%type
language plpgsql
as
$$
declare

    src_ac_id farms.farm_agristability_clients.agristability_client_id%type;

    new_py_id farms.farm_program_years.program_year_id%type;
    new_pyv_id farms.farm_program_year_versions.program_year_version_id%type;
    v_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;

    cnt bigint;

begin

    select count(*)
    into cnt
    from farms.farm_program_years py
    join farms.farm_agristability_clients ac on ac.agristability_client_id = py.agristability_client_id
    where ac.participant_pin = in_participant_pin
    and py.year = in_year_to_create;

    if (cnt > 0) then
        raise exception '%', 'The Year to Create already exists.'
        using errcode = '45000';
    end if;

    -- figure out what id's we use to read the data
    select ac.agristability_client_id as src_ac_id
    into src_ac_id
    from farms.farm_agristability_clients ac
    where ac.participant_pin = in_participant_pin;

    new_py_id := farms_calculator_pkg.create_py(src_ac_id, in_year_to_create, in_user);
    new_pyv_id := farms_calculator_pkg.create_pyv(new_py_id, null, in_user);
    call farms_calculator_pkg.create_ops(new_pyv_id, in_num_operations, in_user);
    v_scenario_id := farms_calculator_pkg.create_scenario(new_pyv_id, in_scenario_class_code, in_scenario_category_code, in_user);

    return new_pyv_id;
end;
$$;
