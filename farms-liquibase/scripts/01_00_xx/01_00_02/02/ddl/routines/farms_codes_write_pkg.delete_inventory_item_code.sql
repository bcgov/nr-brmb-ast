create or replace procedure farms_codes_write_pkg.delete_inventory_item_code(
   in in_inventory_item_code farms.farm_inventory_item_codes.inventory_item_code%type,
   in in_revision_count farms.farm_inventory_item_codes.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_inventory_item_code(in_inventory_item_code);

    if v_in_use = 0 then

        delete from farms.farm_inventory_item_details d
        where d.inventory_item_code = in_inventory_item_code;

        delete from farms.farm_inventory_item_attributes a
        where a.inventory_item_code = in_inventory_item_code;

        delete from farms.farm_inventory_item_codes c
        where c.inventory_item_code = in_inventory_item_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;

    end if;

end;
$$;
