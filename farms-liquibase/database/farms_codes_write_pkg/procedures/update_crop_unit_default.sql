create or replace procedure farms_codes_write_pkg.update_crop_unit_default(
   in in_inventory_item_code farms.farm_crop_unit_defaults.inventory_item_code%type,
   in in_crop_unit_code farms.farm_crop_unit_defaults.crop_unit_code%type,
   in in_revision_count farms.farm_crop_unit_defaults.revision_count%type,
   in in_user farms.farm_crop_unit_defaults.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_crop_unit_defaults
    set crop_unit_code = in_crop_unit_code,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where inventory_item_code = in_inventory_item_code
    and revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
