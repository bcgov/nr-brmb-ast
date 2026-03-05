create or replace function farms_read_pkg.read_inv(
    in op_ids bigint[],
    in sc_ids bigint[],
    in in_verified_date date default null
)
returns table(
    farming_operation_id                farms.farm_reported_inventories.farming_operation_id%type,
    agristabilty_cmmdty_xref_id         farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type,
    inventory_class_code                farms.farm_agristabilty_cmmdty_xref.inventory_class_code%type,
    inventory_class_description         farms.farm_inventory_item_codes.description%type,
    inventory_item_code                 farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    inventory_item_description          farms.farm_inventory_item_codes.description%type,
    rollup_inventory_item_code          farms.farm_inventory_item_codes.inventory_item_code%type,
    rollup_inventory_item_description   farms.farm_inventory_item_codes.description%type,
    inventory_group_code                farms.farm_agristabilty_cmmdty_xref.inventory_group_code%type,
    inventory_group_description         farms.farm_inventory_group_codes.description%type,
    eligibility_ind                     farms.farm_inventory_item_details.eligibility_ind%type,
    commodity_type_code                 farms.farm_inventory_item_details.commodity_type_code%type,
    commodity_type_code_description     farms.farm_commodity_type_codes.description%type,
    fruit_veg_type_code                 farms.farm_inventory_item_details.fruit_veg_type_code%type,
    fruit_veg_type_code_description     farms.farm_fruit_veg_type_codes.description%type,
    line_item                           farms.farm_inventory_item_details.line_item%type,
    line_item_description               farms.farm_line_items.description%type,
    multi_stage_commdty_code            farms.farm_inventory_item_details.multi_stage_commdty_code%type,
    multi_stage_commdty_code_desc       farms.farm_multi_stage_commdty_codes.description%type,
    revenue_variance_limit              farms.farm_fruit_veg_type_details.revenue_variance_limit%type,
    crop_unit_code                      farms.farm_crop_unit_codes.crop_unit_code%type,
    crop_unit_description               farms.farm_crop_unit_codes.description%type,
    market_commodity_ind                farms.farm_agristabilty_cmmdty_xref.market_commodity_ind%type,
    cra_reported_inventory_id           farms.farm_reported_inventories.reported_inventory_id%type,
    cra_price_start                     farms.farm_reported_inventories.price_start%type,
    cra_price_end                       farms.farm_reported_inventories.price_end%type,
    cra_end_year_producer_price         farms.farm_reported_inventories.end_year_producer_price%type,
    cra_quantity_end                    farms.farm_reported_inventories.quantity_end%type,
    cra_quantity_start                  farms.farm_reported_inventories.quantity_start%type,
    cra_start_of_year_amount            farms.farm_reported_inventories.start_of_year_amount%type,
    cra_end_of_year_amount              farms.farm_reported_inventories.end_of_year_amount%type,
    cra_quantity_produced               farms.farm_reported_inventories.quantity_produced%type,
    cra_on_farm_acres                   farms.farm_reported_inventories.on_farm_acres%type,
    cra_unseedable_acres                farms.farm_reported_inventories.unseedable_acres%type,
    cra_accept_producer_price_ind       farms.farm_reported_inventories.accept_producer_price_ind%type,
    cra_aarm_reference_p1_price         farms.farm_reported_inventories.aarm_reference_p1_price%type,
    cra_aarm_reference_p2_price         farms.farm_reported_inventories.aarm_reference_p2_price%type,
    cra_revision_count                  farms.farm_reported_inventories.revision_count%type,
    adj_reported_inventory_id           farms.farm_reported_inventories.reported_inventory_id%type,
    adj_price_start                     farms.farm_reported_inventories.price_start%type,
    adj_price_end                       farms.farm_reported_inventories.price_end%type,
    adj_end_year_producer_price         farms.farm_reported_inventories.end_year_producer_price%type,
    adj_quantity_end                    farms.farm_reported_inventories.quantity_end%type,
    adj_quantity_start                  farms.farm_reported_inventories.quantity_start%type,
    adj_start_of_year_amount            farms.farm_reported_inventories.start_of_year_amount%type,
    adj_end_of_year_amount              farms.farm_reported_inventories.end_of_year_amount%type,
    adj_quantity_produced               farms.farm_reported_inventories.quantity_produced%type,
    adj_on_farm_acres                   farms.farm_reported_inventories.on_farm_acres%type,
    adj_unseedable_acres                farms.farm_reported_inventories.unseedable_acres%type,
    adj_accept_producer_price_ind       farms.farm_reported_inventories.accept_producer_price_ind%type,
    adj_aarm_reference_p1_price         farms.farm_reported_inventories.aarm_reference_p1_price%type,
    adj_aarm_reference_p2_price         farms.farm_reported_inventories.aarm_reference_p2_price%type,
    adj_agristability_scenario_id       farms.farm_reported_inventories.agristability_scenario_id%type,
    adjusted_by_user_id                 farms.farm_reported_inventories.who_updated%type,
    adj_revision_count                  farms.farm_reported_inventories.revision_count%type
)
language sql
as $$
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
               i.accept_producer_price_ind,
               i.aarm_reference_p1_price,
               i.aarm_reference_p2_price,
               i.agristabilty_cmmdty_xref_id,
               i.crop_unit_code,
               i.farming_operation_id,
               i.agristability_scenario_id,
               i.revision_count
        from farms.farm_reported_inventories i
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
               i.accept_producer_price_ind,
               i.aarm_reference_p1_price,
               i.aarm_reference_p2_price,
               i.agristabilty_cmmdty_xref_id,
               i.crop_unit_code,
               i.farming_operation_id,
               i.agristability_scenario_id,
               i.who_updated,
               i.revision_count
        from farms.farm_reported_inventories i
        where i.agristability_scenario_id = any(sc_ids)
    )
    select coalesce(a.farming_operation_id, b.farming_operation_id) farming_operation_id,
           x.agristabilty_cmmdty_xref_id,
           x.inventory_class_code,
           iic.description inventory_class_description,
           x.inventory_item_code,
           iic.description inventory_item_description,
           coalesce(iia.rollup_inventory_item_code, iic.inventory_item_code) rollup_inventory_item_code,
           riic.description,
           x.inventory_group_code,
           igc.description inventory_group_description,
           iid.eligibility_ind,
           iid.commodity_type_code,
           ctc.description commodity_type_code_description,
           iid.fruit_veg_type_code,
           fvtc.description fruit_veg_type_code_description,
           iid.line_item,
           li.description line_item_description,
           iid.multi_stage_commdty_code,
           mscc.description multi_stage_commdty_code_desc,
           ffvtd.revenue_variance_limit,
           cuc.crop_unit_code,
           cuc.description crop_unit_description,
           x.market_commodity_ind,
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
           a.accept_producer_price_ind cra_accept_producer_price_ind,
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
           b.accept_producer_price_ind adj_accept_producer_price_ind,
           b.aarm_reference_p1_price adj_aarm_reference_p1_price,
           b.aarm_reference_p2_price adj_aarm_reference_p2_price,
           b.agristability_scenario_id adj_agristability_scenario_id,
           b.who_updated adjusted_by_user_id,
           b.revision_count adj_revision_count
    from a
    full outer join b on a.reported_inventory_id = b.cra_reported_inventory_id
    join farms.farm_agristabilty_cmmdty_xref x on (a.agristabilty_cmmdty_xref_id = x.agristabilty_cmmdty_xref_id
                                                or b.agristabilty_cmmdty_xref_id = x.agristabilty_cmmdty_xref_id)
    join farms.farm_inventory_item_codes iic on x.inventory_item_code = iic.inventory_item_code
    join farms.farm_inventory_class_codes icc on x.inventory_class_code = icc.inventory_class_code
    join farms.farm_farming_operations fo on fo.farming_operation_id = coalesce(a.farming_operation_id, b.farming_operation_id)
    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = fo.program_year_version_id
    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
    join farms.farm_inventory_item_details iid on x.inventory_item_code = iid.inventory_item_code
                                            and iid.program_year = py.year
    left outer join farms.farm_inventory_group_codes igc on x.inventory_group_code = igc.inventory_group_code
    left outer join farms.farm_crop_unit_codes cuc on (a.crop_unit_code = cuc.crop_unit_code
                                                or b.crop_unit_code = cuc.crop_unit_code)
    left outer join farms.farm_fruit_veg_type_codes fvtc on fvtc.fruit_veg_type_code = iid.fruit_veg_type_code
    left outer join farms.farm_fruit_veg_type_details ffvtd on ffvtd.fruit_veg_type_code = iid.fruit_veg_type_code
    left outer join farms.farm_line_items li on li.line_item = iid.line_item
                                        and py.year = li.program_year
                                        and li.expiry_date >= coalesce(in_verified_date, current_date)
                                        and li.established_date < coalesce(in_verified_date, current_date)
    left outer join farms.farm_commodity_type_codes ctc on ctc.commodity_type_code = iid.commodity_type_code
    left outer join farms.farm_multi_stage_commdty_codes mscc on mscc.multi_stage_commdty_code = iid.multi_stage_commdty_code
    left outer join farms.farm_inventory_item_attributes iia on iic.inventory_item_code = iia.inventory_item_code
    left outer join farms.farm_inventory_item_codes riic on riic.inventory_item_code = coalesce(iia.rollup_inventory_item_code, iic.inventory_item_code);
$$;
