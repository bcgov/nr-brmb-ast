create or replace procedure farms_calculator_pkg.update_farm_type(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_farm_type_code farms.farm_farm_type_codes.farm_type_code%type,
    in in_user farms.farm_program_years.who_updated%type
)
language plpgsql
as
$$
declare

    v_old_type farms.farm_farm_type_codes.description%type;
    v_new_type farms.farm_farm_type_codes.description%type;

begin

    begin
        select description
        into strict v_old_type
        from farms.farm_farm_type_codes
        where farm_type_code = (
            select farm_type_code
            from farms.farm_program_years
            where program_year_id = in_program_year_id
        );
    exception
        when no_data_found then
            v_old_type := null;
    end;

    update farms.farm_program_years
    set farm_type_code = in_farm_type_code,
        when_updated = current_timestamp,
        who_updated = in_user
    where program_year_id = in_program_year_id;

    select description
    into v_new_type
    from farms.farm_farm_type_codes
    where farm_type_code = in_farm_type_code;

    call farms_calculator_pkg.sc_log(in_agristability_scenario_id,
            'Farm Type updated from ''' || v_old_type || ''' to ''' || v_new_type || '''',
            in_user);

end;
$$;
