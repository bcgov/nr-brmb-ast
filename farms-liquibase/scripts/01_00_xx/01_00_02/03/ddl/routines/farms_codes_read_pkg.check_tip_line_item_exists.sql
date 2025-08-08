create or replace function farms_codes_read_pkg.check_tip_line_item_exists(
    in in_line_item_id farms.farm_tip_line_items.line_item%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select tli.line_item
        from farms.farm_tip_line_items tli
        where tli.line_item = in_line_item_id;
    return cur;

end;
$$;
