create or replace function farms_types_pkg.pin_does_not_exist_num()
returns integer
language sql
immutable
as
$$
    select -20015;
$$;
