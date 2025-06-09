create or replace function farms_codes_read_pkg.check_tip_line_item_exists(
    in in_line_item_id farms.tip_line_item.line_item%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select tli.line_item
        from farms.tip_line_item tli
        where tli.line_item = in_line_item_id;
    return cur;

end;
$$;
