create or replace function farms_codes_write_pkg.in_use_structure_group_code(
    in in_structure_group_code farms.structure_group_code.structure_group_code%type
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
            from farms.productive_unit_capacity t
            where t.structure_group_code = in_structure_group_code
        )
        or exists(
            select null
            from farms.benchmark_per_unit t
            where t.structure_group_code = in_structure_group_code
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
