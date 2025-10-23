create or replace function farms_types_pkg.pin_does_not_exist_msg()
returns text
language sql
immutable
as
$$
    select 'PIN does not exist';
$$;
