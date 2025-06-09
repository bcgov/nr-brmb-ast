create or replace function farms_codes_write_pkg.in_use_farm_type_code(
    in in_farm_type_code farms.farm_type_code.farm_type_code%type
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
            from farms.program_year t
            where t.farm_type_code = in_farm_type_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
