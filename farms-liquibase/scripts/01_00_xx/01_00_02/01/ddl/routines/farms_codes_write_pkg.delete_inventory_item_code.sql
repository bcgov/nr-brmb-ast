create or replace procedure farms_codes_write_pkg.delete_inventory_item_code(
   in in_inventory_item_code farms.inventory_item_code.inventory_item_code%type,
   in in_revision_count farms.inventory_item_code.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_inventory_item_code(in_inventory_item_code);

    if v_in_use = 0 then

        delete from farms.inventory_item_detail d
        where d.inventory_item_code = in_inventory_item_code;

        delete from farms.inventory_item_attribute a
        where a.inventory_item_code = in_inventory_item_code;

        delete from farms.inventory_item_code c
        where c.inventory_item_code = in_inventory_item_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;

    end if;

end;
$$;
