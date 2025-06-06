create or replace function farms_read_pkg.read_puc(
    in op_ids numeric[],
    in sc_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        with a as (
            select puc.productve_unit_capacity_id,
                   puc.productive_capacity_amount,
                   puc.farming_operation_id,
                   puc.structure_group_code,
                   puc.agristability_scenario_id,
                   puc.inventory_item_code,
                   puc.participant_data_source_code,
                   puc.revision_count
            from farms.productive_unit_capacity puc
            where puc.farming_operation_id = any(op_ids)
            and puc.agristability_scenario_id is null
        ), b as (
            select puc.productve_unit_capacity_id,
                   puc.cra_productive_unit_capacity_id,
                   puc.productive_capacity_amount,
                   puc.farming_operation_id,
                   puc.structure_group_code,
                   puc.agristability_scenario_id,
                   puc.inventory_item_code,
                   puc.participant_data_source_code,
                   puc.update_user,
                   puc.revision_count
            from farms.productive_unit_capacity puc
            where puc.agristability_scenario_id = any(sc_ids)
        ), p as (
            select coalesce(a.farming_operation_id, b.farming_operation_id) as farming_operation_id,
                   sgc.structure_group_code,
                   sgc.description as structure_group_description,
                   coalesce(sga.rollup_structure_group_code, sgc.structure_group_code) as rollup_structure_group_code,
                   (
                       select description
                       from farms.structure_group_code
                       where structure_group_code = coalesce(sga.rollup_structure_group_code, sgc.structure_group_code)
                       limit 1
                   ) as rollup_structure_group_description,
                   iic.inventory_item_code,
                   iic.description as inventory_item_description,
                   coalesce(iia.rollup_inventory_item_code, iic.inventory_item_code) as rollup_inventory_item_code,
                   (
                       select description
                       from farms.inventory_item_code
                       where inventory_item_code = coalesce(iia.rollup_inventory_item_code, iic.inventory_item_code)
                       limit 1
                   ) as rollup_inventory_item_description,
                   iid.commodity_type_code,
                   ctc.description commodity_type_code_description,
                   iid.fruit_vegetable_type_code,
                   fvtc.description fruit_vegetable_type_code_description,
                   iid.multiple_stage_commodity_code,
                   mscc.description multiple_stage_commodity_code_description,
                   coalesce(a.participant_data_source_code, b.participant_data_source_code) participant_data_source_code,
                   a.productve_unit_capacity_id as cra_productive_unit_capacity_id,
                   a.productive_capacity_amount as cra_productive_capacity_amount,
                   a.revision_count as cra_revision_count,
                   b.productive_unit_capacity_id as adj_productive_unit_capacity_id,
                   b.productive_capacity_amount as adj_productive_capacity_amount,
                   b.agristability_scenario_id as adj_agristability_scenario_id,
                   b.update_user as adjusted_by_user_id,
                   b.revision_count as adj_revision_count
            from a
            full outer join b on a.productve_unit_capacity_id = b.cra_productive_unit_capacity_id
            join farms.farming_operation fo on fo.farming_operation_id = coalesce(a.farming_operation_id, b.farming_operation_id)
            join farms.program_year_version pyv on pyv.program_year_version_id = fo.program_year_version_id
            join farms.program_year py on py.program_year_id = pyv.program_year_id
            left outer join farms.inventory_item_code iic on a.inventory_item_code = iic.inventory_item_code
                                                          or b.inventory_item_code = iic.inventory_item_code
            left outer join farms.inventory_item_attribute iia on iic.inventory_item_code = iia.inventory_item_code
            left outer join farms.inventory_item_detail iid on iic.inventory_item_code = iid.inventory_item_code
                                                            and iid.program_year = py.year
            left outer join farms.fruit_vegetable_type_code fvtc on fvtc.fruit_vegetable_type_code = iid.fruit_vegetable_type_code
            left outer join farms.commodity_type_code ctc on ctc.commodity_type_code = iid.commodity_type_code
            left outer join farms.multiple_stage_commodity_code mscc on mscc.multiple_stage_commodity_code = iid.multiple_stage_commodity_code
            left outer join farms.structure_group_code sgc on a.structure_group_code = sgc.structure_group_code
                                                           or b.structure_group_code = sgc.structure_group_code
            left outer join farms.structure_group_attribute sga on sgc.structure_group_code = sga.structure_group_code
        ), i as (
            select p.farming_operation_id,
                   p.structure_group_code,
                   p.structure_group_description,
                   p.rollup_structure_group_code,
                   p.rollup_structure_group_description,
                   p.inventory_item_code,
                   p.inventory_item_description,
                   p.rollup_inventory_item_code,
                   p.rollup_inventory_item_description,
                   p.commodity_type_code,
                   p.commodity_type_code_description,
                   p.fruit_vegetable_type_code,
                   p.fruit_vegetable_type_code_description,
                   p.multiple_stage_commodity_code,
                   p.multiple_stage_commodity_code_description,
                   ep.crop_unit_code expected_prod_crop_unit_code,
                   ep.expected_production_per_productive_unit,
                   p.cra_productive_unit_capacity_id,
                   p.cra_productive_capacity_amount,
                   p.cra_revision_count,
                   p.adj_productive_unit_capacity_id,
                   p.adj_productive_capacity_amount,
                   p.adj_agristability_scenario_id,
                   p.participant_data_source_code,
                   p.adjusted_by_user_id,
                   p.adj_revision_count,
                   sum(ri.on_farm_acres) over (partition by p.cra_productive_unit_capacity_id, p.adj_productive_unit_capacity_id) cra_on_farm_acres,
                   sum(ri.unseedable_acres) over (partition by p.cra_productive_unit_capacity_id, p.adj_productive_unit_capacity_id) cra_unseedable_acres,
                   row_number() over (partition by p.cra_productive_unit_capacity_id, p.adj_productive_unit_capacity_id order by ri.reported_inventory_id) row_num
            from p
            left outer join farms.agristabilty_commodity_xref x on x.inventory_item_code = p.inventory_item_code
                                                                and x.inventory_class_code = '1'
            left outer join farms.reported_inventory ri on ri.agristabilty_commodity_xref_id = x.agristabilty_commodity_xref_id
                                                        and ri.farming_operation_id = p.farming_operation_id
                                                        and ri.agristability_scenario_id is null
            left outer join farms.expected_production ep on ep.inventory_item_code = p.inventory_item_code
        )
        select *
        from i
        where i.row_num = 1;

    return cur;
end;
$$;
