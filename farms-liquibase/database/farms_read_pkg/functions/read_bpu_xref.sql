create or replace function farms_read_pkg.read_bpu_xref(
    in in_sc_ids bigint[],
    in in_scenario_bpu_purpose_code farms.farm_scenario_bpu_purpos_codes.scenario_bpu_purpose_code%type
)
returns table(
    benchmark_per_unit_id   farms.farm_benchmark_per_units.benchmark_per_unit_id%type,
    municipality_code       farms.farm_benchmark_per_units.municipality_code%type,
    inventory_item_code     farms.farm_benchmark_per_units.inventory_item_code%type,
    structure_group_code    farms.farm_benchmark_per_units.structure_group_code%type,
    unit_comment            farms.farm_benchmark_per_units.unit_comment%type,
    bpu_revision_count      farms.farm_benchmark_per_units.revision_count%type,
    benchmark_year          farms.farm_benchmark_years.benchmark_year%type,
    average_margin          farms.farm_benchmark_years.average_margin%type,
    average_expense         farms.farm_benchmark_years.average_expense%type,
    bnch_revision_count     farms.farm_benchmark_years.revision_count%type
)
language sql
as $$
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
        select distinct bpu.benchmark_per_unit_id,
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
               bnch.revision_count bnch_revision_count
        from farms.farm_scenario_bpu_xref x
        join farms.farm_benchmark_per_units bpu on x.benchmark_per_unit_id = bpu.benchmark_per_unit_id
        join farms.farm_benchmark_years bnch on bpu.benchmark_per_unit_id = bnch.benchmark_per_unit_id
        where x.agristability_scenario_id = any(in_sc_ids)
        and x.scenario_bpu_purpose_code = in_scenario_bpu_purpose_code
    ) x
    where x.municipality_code = mx_municipality_code
    order by x.benchmark_per_unit_id,
             x.benchmark_year desc;
$$;
