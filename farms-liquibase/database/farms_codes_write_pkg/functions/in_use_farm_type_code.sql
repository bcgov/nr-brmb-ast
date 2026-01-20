create or replace function farms_codes_write_pkg.in_use_farm_type_code(
    in in_farm_type_code farms.farm_farm_type_codes.farm_type_code%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    select (case
        when exists(
            select null
            from farms.farm_program_years t
            where t.farm_type_code = in_farm_type_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
