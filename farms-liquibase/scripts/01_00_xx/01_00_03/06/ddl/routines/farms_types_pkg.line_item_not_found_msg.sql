create or replace function farms_types_pkg.line_item_not_found_msg()
returns text
language sql
immutable
as
$$
    select 'Line Item, Year, Eligibility combination not found: ';
$$;
