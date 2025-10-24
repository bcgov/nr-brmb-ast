create or replace function farms_calculator_pkg.get_new_op_schedule(
    in in_agristability_client_id farms.farm_agristability_clients.agristability_client_id%type
) returns farms.farm_farming_operations.alignment_key%type
language plpgsql
as
$$
declare

    v_schedule farms.farm_farming_operations.alignment_key%type;

begin

    select chr(max(ascii(fo.alignment_key)) + 1)
    into v_schedule
    from farms.farm_program_years py
    join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
    join farms.farm_farming_operations fo on fo.program_year_version_id = pyv.program_year_version_id
    where py.agristability_client_id = in_agristability_client_id;

    return v_schedule;
end;
$$;
