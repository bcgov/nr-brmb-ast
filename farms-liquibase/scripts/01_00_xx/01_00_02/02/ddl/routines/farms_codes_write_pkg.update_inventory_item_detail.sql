create or replace procedure farms_codes_write_pkg.update_inventory_item_detail(
   in in_inventory_item_detail_id farms.farm_inventory_item_details.inventory_item_detail_id%type,
   in in_eligibility_indicator farms.farm_inventory_item_details.eligibility_ind%type,
   in in_revision_count farms.farm_inventory_item_details.revision_count%type,
   in in_fruit_vegetable_type_code farms.farm_inventory_item_details.fruit_veg_type_code%type,
   in in_line_item farms.farm_inventory_item_details.line_item%type,
   in in_insurable_value farms.farm_inventory_item_details.insurable_value%type,
   in in_premium_rate farms.farm_inventory_item_details.premium_rate%type,
   in in_commodity_type_code farms.farm_inventory_item_details.commodity_type_code%type,
   in in_user farms.farm_inventory_item_details.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_inventory_item_details t
    set t.eligibility_ind = in_eligibility_indicator,
        t.revision_count = t.revision_count + 1,
        t.who_updated = in_user,
        t.when_updated = current_timestamp,
        t.fruit_veg_type_code = in_fruit_vegetable_type_code,
        t.line_item = in_line_item,
        t.insurable_value = in_insurable_value,
        t.premium_rate = in_premium_rate,
        t.commodity_type_code = in_commodity_type_code
    where t.inventory_item_detail_id = in_inventory_item_detail_id
    and t.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
