create or replace function farms_types_pkg.xref_not_found_num()
returns integer
language sql
immutable
as
$$
    select -20009;
$$;
