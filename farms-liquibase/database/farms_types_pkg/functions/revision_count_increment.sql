create or replace function farms_types_pkg.revision_count_increment()
returns integer
language sql
immutable
as
$$
    select 1;
$$;
