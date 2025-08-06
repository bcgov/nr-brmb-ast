create or replace procedure farms_codes_write_pkg.update_inventory_item_code(
   in in_inventory_item_code farms.farm_inventory_item_codes.inventory_item_code%type,
   in in_description farms.farm_inventory_item_codes.description%type,
   in in_rollup_inventory_item_code farms.farm_inventory_item_attributes.rollup_inventory_item_code%type,
   in in_effective_date farms.farm_inventory_item_codes.established_date%type,
   in in_expiry_date farms.farm_inventory_item_codes.expiry_date%type,
   in in_revision_count farms.farm_inventory_item_codes.revision_count%type,
   in in_user farms.farm_inventory_item_codes.who_updated%type
)
language plpgsql
as $$
begin
    update farms.farm_inventory_item_codes c
    set c.description = in_description,
        c.established_date = in_effective_date,
        c.expiry_date = in_expiry_date,
        c.revision_count = c.revision_count + 1,
        c.who_updated = in_user,
        c.when_updated = current_timestamp
    where c.inventory_item_code = in_inventory_item_code
    and c.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;

    update farms.farm_inventory_item_attributes a
    set a.rollup_inventory_item_code = in_rollup_inventory_item_code,
        a.revision_count = a.revision_count + 1,
        a.who_updated = in_user,
        a.when_updated = current_timestamp
    where a.inventory_item_code = in_inventory_item_code;

end;
$$;
