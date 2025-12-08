create or replace function farms_types_pkg.xref_more_than_one_found_num()
returns integer
language sql
immutable
as
$$
    select -20008;
$$;
