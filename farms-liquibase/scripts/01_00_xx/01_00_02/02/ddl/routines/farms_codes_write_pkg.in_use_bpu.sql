create or replace function farms_codes_write_pkg.in_use_bpu(
    in in_benchmark_per_unit_id farms.farm_benchmark_years.benchmark_per_unit_id%type
)
returns numeric
language plpgsql
as $$
declare
    c_xref cursor for
        select agristability_scenario_id
        from farms.farm_scenario_bpu_xref
        where benchmark_per_unit_id = in_benchmark_per_unit_id;

    c_bpu cursor for
        select *
        from farms.farm_benchmark_per_units
        where benchmark_per_unit_id = in_benchmark_per_unit_id;

    v_xref record;
    v_bpu record;
    v_result numeric := 0; -- 0 = false, 1 = true
begin
    open c_xref;
    fetch c_xref into v_xref;
    if c_xref%found then
        v_result := 1;
    end if;
    close c_xref;

    if v_result = 0 then
        open c_bpu;
        fetch c_bpu into v_bpu;
        close c_bpu;

        select (case
            when exists(
                select null
                from farms.farm_program_year_versions pyv
                join farms.farm_farming_operations fo on fo.program_year_version_id = pyv.program_year_version_id
                join farms.farm_productve_unit_capacities puc on puc.farming_operation_id = fo.farming_operation_id
                where (v_bpu.municipality_code = '0' or pyv.municipality_code = v_bpu.municipality_code)
                and (
                    (to_number(to_char(fo.fiscal_year_start, 'YYYY')) = v_bpu.program_year)
                    or
                    (to_number(to_char(fo.fiscal_year_end, 'YYYY')) = v_bpu.program_year)
                )
                and (
                    (puc.inventory_item_code is not null and puc.inventory_item_code = v_bpu.inventory_item_code)
                    or
                    (puc.structure_group_code is not null and puc.structure_group_code = v_bpu.structure_group_code)
                )
            ) then 1
            else 0
        end) into v_result;
    end if;

    return v_result;
end;
$$;
