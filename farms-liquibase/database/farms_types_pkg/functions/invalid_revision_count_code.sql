create or replace function farms_types_pkg.invalid_revision_count_code()
returns integer
language sql
immutable
as
$$
    select -20010;
$$;
