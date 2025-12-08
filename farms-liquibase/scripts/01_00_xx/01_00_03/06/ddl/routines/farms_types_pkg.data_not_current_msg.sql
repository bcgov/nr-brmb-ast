create or replace function farms_types_pkg.data_not_current_msg()
returns text
language sql
immutable
as
$$
    select 'Data is not current.  Please refresh your data.';
$$;
