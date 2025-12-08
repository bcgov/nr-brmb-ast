create or replace function farms_webapp_pkg.get_inventory_valid_items()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select x.agristabilty_cmmdty_xref_id,
               x.inventory_item_code,
               iic.description item_code_desc,
               x.inventory_class_code,
               icc.description item_class_desc,
               x.market_commodity_ind,
               iid.program_year,
               iid.eligibility_ind,
               iid.commodity_type_code,
               iid.line_item,
               iid.multi_stage_commdty_code,
               cud.crop_unit_code default_crop_unit_code
        from farms.farm_agristabilty_cmmdty_xref x
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = x.inventory_item_code
        join farms.farm_inventory_class_codes icc on icc.inventory_class_code = x.inventory_class_code
        join farms.farm_inventory_item_details iid on iid.inventory_item_code = x.inventory_item_code
        left outer join farms.farm_crop_unit_defaults cud on cud.inventory_item_code = x.inventory_item_code
        where x.inventory_item_code != '-1'
        and x.inventory_class_code != '-1'
        order by x.inventory_class_code,
                 x.inventory_item_code;
    return v_cursor;
end;
$$;
