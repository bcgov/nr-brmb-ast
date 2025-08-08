create or replace function farms_codes_read_pkg.read_bpu(
    in in_program_year farms.farm_benchmark_per_units.program_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select bpu.benchmark_per_unit_id,
               bpu.program_year,
               coalesce(bpu.inventory_item_code, bpu.structure_group_code) inv_sg_code,
               coalesce(iic.description, sgc.description) inv_sg_description,
               (case
                   when bpu.inventory_item_code is not null then 'INV'
                   else 'SG'
               end) inv_sg_type,
               bpu.municipality_code,
               mc.description municipality_code_description,
               bpuy.benchmark_year,
               bpuy.average_margin,
               bpuy.average_expense,
               bpuy.revision_count
        from farms.farm_benchmark_per_units bpu
        join farms.farm_benchmark_years bpuy on bpuy.benchmark_per_unit_id = bpu.benchmark_per_unit_id
        join farms.farm_municipality_codes mc on mc.municipality_code = bpu.municipality_code
        left join farms.farm_inventory_item_codes iic on iic.inventory_item_code = bpu.inventory_item_code
        left join farms.farm_structure_group_codes sgc on sgc.structure_group_code = bpu.structure_group_code
        where bpu.program_year = in_program_year
        and (bpu.expiry_date is null or bpu.expiry_date > current_date)
        /* read relies on this ordering to construct objects. */
        order by lower(coalesce(iic.description, sgc.description)),
                 coalesce(bpu.inventory_item_code, bpu.structure_group_code),
                 lower(mc.description),
                 bpuy.benchmark_year;
    return cur;
end;
$$;
