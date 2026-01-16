create or replace function farms_codes_read_pkg.check_line_item_exists_for_program_year(
    in in_line_item farms.farm_line_items.line_item%type,
    in in_program_year farms.farm_line_items.program_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select li.line_item
        from farms.farm_line_items li
        where li.line_item = in_line_item
        and li.program_year = in_program_year;
    return cur;

end;
$$;
