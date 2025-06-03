create or replace procedure farms_codes_write_pkg.update_inventory_item_code(
   in in_inventory_item_code farms.inventory_item_code.inventory_item_code%type,
   in in_description farms.inventory_item_code.description%type,
   in in_rollup_inventory_item_code farms.inventory_item_attribute.rollup_inventory_item_code%type,
   in in_effective_date farms.inventory_item_code.effective_date%type,
   in in_expiry_date farms.inventory_item_code.expiry_date%type,
   in in_revision_count farms.inventory_item_code.revision_count%type,
   in in_user farms.inventory_item_code.update_user%type
)
language plpgsql
as $$
begin
    update farms.inventory_item_code c
    set c.description = in_description,
        c.effective_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.update_user = in_user,
        c.update_date = current_timestamp
    where c.inventory_item_code = in_inventory_item_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;

    update farms.inventory_item_attribute a
    set a.rollup_inventory_item_code = in_rollup_inventory_item_code,
        a.revision_count = a.revision_count + 1,
        a.update_user = in_user,
        a.update_date = current_timestamp
    where a.inventory_item_code = in_inventory_item_code;

end;
$$;
