create or replace procedure farms_codes_write_pkg.delete_line_item(
   in in_line_item_id farms.farm_line_items.line_item_id%type,
   in in_program_year farms.farm_line_items.program_year%type,
   in in_line_item farms.farm_line_items.line_item%type,
   in in_revision_count farms.farm_line_items.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin
    v_in_use := farms_codes_write_pkg.in_use_line_item(in_program_year, in_line_item);

    if v_in_use = 0 then

        delete from farms.farm_sector_detail_line_items sdli
        where sdli.line_item = in_line_item;

        -- Make sure no one has changed the line item
        delete from farms.farm_line_items c
        where c.line_item = in_line_item
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;

        -- Since the line item is not in use for this year
        -- we can delete any expired copies of it too.
        delete from farms.farm_line_items c
        where c.line_item = in_line_item
        and c.program_year = in_program_year;
    end if;
end;
$$;
