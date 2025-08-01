create or replace function farms_read_pkg.read_inv(
    in op_ids numeric[],
    in sc_ids numeric[],
    in in_verified_date date default null
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        with a as (
            select i.reported_inventory_id,
                   i.price_start,
                   i.price_end,
                   i.end_year_producer_price,
                   i.quantity_end,
                   i.quantity_start,
                   i.start_of_year_amount,
                   i.end_of_year_amount,
                   i.quantity_produced,
                   i.on_farm_acres,
                   i.unseedable_acres,
                   i.accept_producer_price_indicator,
                   i.aarm_reference_p1_price,
                   i.aarm_reference_p2_price,
                   i.agristabilty_commodity_xref_id,
                   i.crop_unit_code,
                   i.farming_operation_id,
                   i.agristability_scenario_id,
                   i.revision_count
            from farms.reported_inventory i
            where i.farming_operation_id = any(op_ids)
            and i.agristability_scenario_id is null
        ), b as (
            select i.reported_inventory_id,
                   i.cra_reported_inventory_id,
                   i.price_start,
                   i.price_end,
                   i.end_year_producer_price,
                   i.quantity_end,
                   i.quantity_start,
                   i.start_of_year_amount,
                   i.end_of_year_amount,
                   i.quantity_produced,
                   i.on_farm_acres,
                   i.unseedable_acres,
                   i.accept_producer_price_indicator,
                   i.aarm_reference_p1_price,
                   i.aarm_reference_p2_price,
                   i.agristabilty_commodity_xref_id,
                   i.crop_unit_code,
                   i.farming_operation_id,
                   i.agristability_scenario_id,
                   i.update_user,
                   i.revision_count
            from farms.reported_inventory i
            where i.agristability_scenario_id = any(sc_ids)
        )
        select coalesce(a.farming_operation_id, b.farming_operation_id) farming_operation_id,
               x.agristabilty_commodity_xref_id,
               x.inventory_class_code,
               iic.description inventory_class_description,
               x.inventory_item_code,
               iic.description inventory_item_description,
               coalesce(iia.rollup_inventory_item_code, iic.inventory_item_code) rollup_inventory_item_code,
               (
                   select description
                   from farms.inventory_item_code
                   where inventory_item_code = coalesce(iia.rollup_inventory_item_code, iic.inventory_item_code)
                   limit 1
               ) rollup_inventory_item_description,
               x.inventory_group_code,
               igc.description inventory_group_description,
               iid.eligibility_indicator,
               iid.commodity_type_code,
               ctc.description commodity_type_code_description,
               iid.fruit_vegetable_type_code,
               fvtc.description fruit_vegetable_type_code_description,
               iid.line_item,
               li.description line_item_description,
               iid.multiple_stage_commodity_code,
               mscc.description multiple_stage_commodity_code_description,
               ffvtd.revenue_variance_limit,
               cuc.crop_unit_code,
               cuc.description crop_unit_description,
               x.market_commodity_indicator,
               a.reported_inventory_id cra_reported_inventory_id,
               a.price_start cra_price_start,
               a.price_end cra_price_end,
               a.end_year_producer_price cra_end_year_producer_price,
               a.quantity_end cra_quantity_end,
               a.quantity_start cra_quantity_start,
               a.start_of_year_amount cra_start_of_year_amount,
               a.end_of_year_amount cra_end_of_year_amount,
               a.quantity_produced cra_quantity_produced,
               a.on_farm_acres cra_on_farm_acres,
               a.unseedable_acres cra_unseedable_acres,
               a.accept_producer_price_indicator cra_accept_producer_price_indicator,
               a.aarm_reference_p1_price cra_aarm_reference_p1_price,
               a.aarm_reference_p2_price cra_aarm_reference_p2_price,
               a.revision_count cra_revision_count,
               b.reported_inventory_id adj_reported_inventory_id,
               b.price_start adj_price_start,
               b.price_end adj_price_end,
               b.end_year_producer_price adj_end_year_producer_price,
               b.quantity_end adj_quantity_end,
               b.quantity_start adj_quantity_start,
               b.start_of_year_amount adj_start_of_year_amount,
               b.end_of_year_amount adj_end_of_year_amount,
               b.quantity_produced adj_quantity_produced,
               b.on_farm_acres adj_on_farm_acres,
               b.unseedable_acres adj_unseedable_acres,
               b.accept_producer_price_indicator adj_accept_producer_price_indicator,
               b.aarm_reference_p1_price adj_aarm_reference_p1_price,
               b.aarm_reference_p2_price adj_aarm_reference_p2_price,
               b.agristability_scenario_id adj_agristability_scenario_id,
               b.update_user adjusted_by_user_id,
               b.revision_count adj_revision_count
        from a
        full outer join b on a.reported_inventory_id = b.cra_reported_inventory_id
        join farms.agristabilty_commodity_xref x on (a.agristabilty_commodity_xref_id = x.agristabilty_commodity_xref_id
                                                    or b.agristabilty_commodity_xref_id = x.agristabilty_commodity_xref_id)
        join farms.inventory_item_code iic on x.inventory_item_code = iic.inventory_item_code
        join farms.inventory_class_code icc on iic.inventory_class_code = icc.inventory_class_code
        join farms.farming_operation fo on fo.farming_operation_id = coalesce(a.farming_operation_id, b.farming_operation_id)
        join farms.program_year_version pyv on pyv.program_year_version_id = fo.program_year_version_id
        join farms.program_year py on py.program_year_id = pyv.program_year_id
        join farms.inventory_item_detail iid on x.inventory_item_code = iid.inventory_item_code
                                             and iid.program_year = py.year
        left outer join farms.inventory_group_code igc on x.inventory_group_code = igc.inventory_group_code
        left outer join farms.crop_unit_code cuc on (a.crop_unit_code = cuc.crop_unit_code
                                                    or b.crop_unit_code = cuc.crop_unit_code)
        left outer join farms.fruit_vegetable_type_code fvtc on fvtc.fruit_vegetable_type_code = iid.fruit_vegetable_type_code
        left outer join farms.fruit_vegetable_type_detail ffvtd on ffvtd.fruit_vegetable_type_code = iid.fruit_vegetable_type_code
        left outer join farms.line_item li on li.line_item = iid.line_item
                                           and py.year = li.program_year
                                           and li.expiry_date >= coalesce(in_verified_date, current_date)
                                           and li.effective_date < coalesce(in_verified_date, current_date)
        left outer join farms.commodity_type_code ctc on ctc.commodity_type_code = iid.commodity_type_code
        left outer join farms.multiple_stage_commodity_code mscc on mscc.multiple_stage_commodity_code = iid.multiple_stage_commodity_code
        left outer join farms.inventory_item_attribute iia on iic.inventory_item_code = iia.inventory_item_code;

    return cur;
end;
$$;
