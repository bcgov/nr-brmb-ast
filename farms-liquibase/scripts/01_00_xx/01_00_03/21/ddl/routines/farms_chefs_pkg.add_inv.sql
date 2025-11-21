create or replace procedure farms_chefs_pkg.add_inv(
    in in_farming_operation_id farms.farm_reported_inventories.farming_operation_id%type,
    in in_price_start farms.farm_reported_inventories.price_start%type,
    in in_price_end farms.farm_reported_inventories.price_end%type,
    in in_end_year_producer_price farms.farm_reported_inventories.end_year_producer_price%type,
    in in_quantity_start farms.farm_reported_inventories.quantity_start%type,
    in in_quantity_end farms.farm_reported_inventories.quantity_end%type,
    in in_start_of_year_amount farms.farm_reported_inventories.start_of_year_amount%type,
    in in_end_of_year_amount farms.farm_reported_inventories.end_of_year_amount%type,
    in in_quantity_produced farms.farm_reported_inventories.quantity_produced%type,
    in in_crop_unit_code farms.farm_reported_inventories.crop_unit_code%type,
    in in_on_farm_acres farms.farm_reported_inventories.on_farm_acres%type,
    in in_unseedable_acres farms.farm_reported_inventories.unseedable_acres%type,
    in in_cmmdty_xref_id farms.farm_reported_inventories.agristabilty_cmmdty_xref_id%type,
    in in_inventory_item_code farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    in in_inventory_class_code farms.farm_agristabilty_cmmdty_xref.inventory_class_code%type,
    in in_user farms.farm_reported_inventories.who_updated%type
)
language plpgsql
as
$$
declare

    xref_count bigint;
    xref_id farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id%type;

begin

    if (in_cmmdty_xref_id is not null and in_cmmdty_xref_id::text <> '') then
        xref_id := in_cmmdty_xref_id;
    else
        select count(x.agristabilty_cmmdty_xref_id)
        into xref_count
        from farms.farm_agristabilty_cmmdty_xref x
        where x.inventory_item_code = in_inventory_item_code
        and x.inventory_class_code = in_inventory_class_code;

        if xref_count = 1 then
            select x.agristabilty_cmmdty_xref_id
            into xref_id
            from farms.farm_agristabilty_cmmdty_xref x
            where x.inventory_item_code = in_inventory_item_code
            and x.inventory_class_code = in_inventory_class_code;

        elsif xref_count = 0 then
            raise exception '%', farms_types_pkg.xref_not_found_msg()
            using errcode = farms_types_pkg.xref_not_found_num()::text;
        else
            raise exception '%', farms_types_pkg.xref_more_than_one_found_msg()
            using errcode = farms_types_pkg.xref_more_than_one_found_num()::text;
        end if;

    end if;

    insert into farms.farm_reported_inventories (
        reported_inventory_id,
        price_start,
        price_end,
        end_year_producer_price,
        quantity_start,
        quantity_end,
        quantity_produced,
        start_of_year_amount,
        end_of_year_amount,
        agristabilty_cmmdty_xref_id,
        crop_unit_code,
        on_farm_acres,
        unseedable_acres,
        farming_operation_id,
        who_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_ri_seq'),
        in_price_start,
        in_price_end,
        in_end_year_producer_price,
        in_quantity_start,
        in_quantity_end,
        in_quantity_produced,
        in_start_of_year_amount,
        in_end_of_year_amount,
        xref_id,
        in_crop_unit_code,
        in_on_farm_acres,
        in_unseedable_acres,
        in_farming_operation_id,
        in_user,
        in_user,
        current_timestamp
    );

end;
$$;
