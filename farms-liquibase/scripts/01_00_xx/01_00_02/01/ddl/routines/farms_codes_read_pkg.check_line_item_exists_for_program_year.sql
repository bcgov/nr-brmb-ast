create or replace function farms_codes_read_pkg.check_line_item_exists_for_program_year(
    in in_line_item farms.line_item.line_item%type,
    in in_program_year farms.line_item.program_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select li.line_item
        from farms.line_item li
        where li.line_item = in_line_item
        and li.program_year = in_program_year;
    return cur;

end;
$$;
