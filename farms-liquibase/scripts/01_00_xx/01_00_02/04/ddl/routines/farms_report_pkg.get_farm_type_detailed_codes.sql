create or replace function farms_report_pkg.get_farm_type_detailed_codes(
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
returns varchar
language plpgsql
as $$
declare
    cur refcursor;
    code_list varchar(50);
begin
    with a as (
        select puc.productive_unit_capacity_id,
               puc.productive_capacity_amount,
               puc.farming_operation_id,
               puc.structure_group_code,
               puc.agristability_scenario_id,
               puc.inventory_item_code
        from farms.scenarios_vw sv
        join farms.farm_farming_operations fo on fo.program_year_version_id = sv.program_year_version_id
        join farms.farm_productve_unit_capacities puc on puc.farming_operation_id = fo.farming_operation_id
        where sv.agristability_scenario_id = in_scenario_id
        and puc.agristability_scenario_id is null
    ), b as (
        select puc.productive_unit_capacity_id,
               puc.cra_productve_unit_capacity_id,
               puc.productive_capacity_amount,
               puc.farming_operation_id,
               puc.structure_group_code,
               puc.inventory_item_code
        from farms.farm_productve_unit_capacities puc
        where puc.agristability_scenario_id = in_scenario_id
    ), p as (
        select coalesce(sgc.structure_group_code, iic.inventory_item_code) puc_code,
               coalesce(a.productive_capacity_amount, 0) + coalesce(b.productive_capacity_amount, 0) productive_capacity_amount
        from a
        full outer join b on a.productive_unit_capacity_id = b.cra_productve_unit_capacity_id
        join farms.farm_farming_operations fo on fo.farming_operation_id = coalesce(a.farming_operation_id, b.farming_operation_id)
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
        join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
        left outer join farms.farm_inventory_item_codes iic on a.inventory_item_code = iic.inventory_item_code
                                                      and b.inventory_item_code = iic.inventory_item_code
        left outer join farms.farm_structure_group_codes sgc on a.structure_group_code = sgc.structure_group_code
                                                      and b.structure_group_code = sgc.structure_group_code
    ), i as (
        select p.puc_code,
               sum(p.productive_capacity_amount) amount
        from p
        group by p.puc_code
    ), j as (
        select i.puc_code,
               row_number() over (order by i.amount desc) rnk
        from i
    )
    select string_agg(j.puc_code, ',' order by j.rnk)
    into code_list
    from j
    where j.rnk <= 5;

    return code_list;
end;
$$;
