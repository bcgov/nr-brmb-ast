create or replace function farms_types_pkg.xref_not_found_msg()
returns text
language sql
immutable
as
$$
    select 'Inventory Item, Class combination not found';
$$;
