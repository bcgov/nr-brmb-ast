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
    update farms.farm_inventory_item_codes
    set description = in_description,
        established_date = in_effective_date,
        expiry_date = in_expiry_date,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where inventory_item_code = in_inventory_item_code
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;

    update farms.farm_inventory_item_attributes
    set rollup_inventory_item_code = in_rollup_inventory_item_code,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where inventory_item_code = in_inventory_item_code;

end;
$$;
