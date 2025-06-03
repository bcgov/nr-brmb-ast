create or replace procedure farms_codes_write_pkg.update_crop_unit_default(
   in in_inventory_item_code farms.crop_unit_default.inventory_item_code%type,
   in in_crop_unit_code farms.crop_unit_default.crop_unit_code%type,
   in in_revision_count farms.crop_unit_default.revision_count%type,
   in in_user farms.crop_unit_default.update_user%type
)
language plpgsql
as $$
begin

    update farms.crop_unit_default cud
    set cud.crop_unit_code = in_crop_unit_code,
        cud.revision_count = cud.revision_count + 1,
        cud.update_user = in_user,
        cud.update_date = current_timestamp
    where cud.inventory_item_code = in_inventory_item_code
    and cud.revision_count = in_revision_count;

    if sql%rowcount <> 1 then
        raise exception 'Invalid revision count';
    end if;
end;
$$;
