create or replace function farms_codes_write_pkg.in_use_structure_group_code(
    in in_structure_group_code farms.farm_structure_group_codes.structure_group_code%type
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
            from farms.farm_productve_unit_capacities t
            where t.structure_group_code = in_structure_group_code
        )
        or exists(
            select null
            from farms.farm_benchmark_per_units t
            where t.structure_group_code = in_structure_group_code
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
