create or replace function farms_types_pkg.data_not_current_code()
returns integer
language sql
immutable
as
$$
    select -20001;
$$;
