create or replace procedure farms_codes_write_pkg.update_inventory_item_detail(
   in in_inventory_item_detail_id farms.inventory_item_detail.inventory_item_detail_id%type,
   in in_eligibility_indicator farms.inventory_item_detail.eligibility_indicator%type,
   in in_revision_count farms.inventory_item_detail.revision_count%type,
   in in_fruit_vegetable_type_code farms.inventory_item_detail.fruit_vegetable_type_code%type,
   in in_line_item farms.inventory_item_detail.line_item%type,
   in in_insurable_value farms.inventory_item_detail.insurable_value%type,
   in in_premium_rate farms.inventory_item_detail.premium_rate%type,
   in in_commodity_type_code farms.inventory_item_detail.commodity_type_code%type,
   in in_user farms.inventory_item_detail.update_user%type
)
language plpgsql
as $$
begin
    update farms.inventory_item_detail t
    set t.eligibility_indicator = in_eligibility_indicator,
        t.revision_count = t.revision_count + 1,
        t.update_user = in_user,
        t.update_date = current_timestamp,
        t.fruit_vegetable_type_code = in_fruit_vegetable_type_code,
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
