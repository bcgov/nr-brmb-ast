create or replace function farms_types_pkg.xref_more_than_one_found_msg()
returns text
language sql
immutable
as
$$
    select 'More than one Inventory Item, Class combination was found';
$$;
