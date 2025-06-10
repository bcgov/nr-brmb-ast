create or replace function farms_read_pkg.read_bpu_all(
    in in_sc_id farms.agristability_scenario.agristability_scenario_id%type,
    in inv_cds numeric[],
    in str_cds numeric[],
    in in_base_year farms.program_year.year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select x.benchmark_per_unit_id,
               x.municipality_code,
               x.inventory_item_code,
               x.structure_group_code,
               x.unit_comment,
               x.bpu_revision_count,
               x.benchmark_year,
               x.average_margin,
               x.average_expense,
               x.bnch_revision_count
        from (
            select bpu.benchmark_per_unit_id,
                   bpu.municipality_code,
                   -- Municipality_Code for 'All Municipalities' is zero so if a matching municipality is found
                   -- that is not zero, MAX will return that code instead of 'All Municipalities' (the default).
                   max(bpu.municipality_code) over (partition by bpu.program_year, bpu.inventory_item_code, bpu.structure_group_code) mx_municipality_code,
                   bpu.inventory_item_code,
                   bpu.structure_group_code,
                   bpu.unit_comment,
                   bpu.revision_count bpu_revision_count,
                   bnch.benchmark_year,
                   bnch.average_margin,
                   bnch.average_expense,
                   bnch.revision_count as bnch_revision_count
            from farms.benchmark_per_unit bpu
            join farms.benchmark_year bnch on bpu.benchmark_per_unit_id = bnch.benchmark_per_unit_id
            join farms.agristability_scenario sc on sc.agristability_scenario_id = in_sc_id
            join farms.program_year_version pyv on sc.program_year_version_id = pyv.program_year_version_id
                                                and (bpu.municipality_code = pyv.municipality_code or bpu.municipality_code = '0')
            where bpu.program_year = in_base_year
            and (bpu.expiry_date is null or bpu.expiry_date >= current_date)
            and (bpu.inventory_item_code = any(inv_cds) or bpu.structure_group_code = any(str_cds))
        ) x
        where x.municipality_code = mx_municipality_code
        order by x.benchmark_per_unit_id,
                 x.benchmark_year desc;

    return cur;
end;
$$;
