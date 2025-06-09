create or replace procedure farms_codes_write_pkg.delete_tip_line_item(
   in in_tip_line_item_id farms.tip_line_item.tip_line_item_id%type
)
language plpgsql
as $$
begin

    delete from farms.tip_line_item
    where tip_line_item_id = in_tip_line_item_id;

end;
$$;
