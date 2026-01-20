create or replace function farms_types_pkg.invalid_revision_count_msg()
returns text
language sql
immutable
as
$$
    select 'Invalid revision count';
$$;
