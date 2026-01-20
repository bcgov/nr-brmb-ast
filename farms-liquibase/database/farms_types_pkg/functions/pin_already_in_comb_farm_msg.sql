create or replace function farms_types_pkg.pin_already_in_comb_farm_msg()
returns text
language sql
immutable
as
$$
    select 'PIN is already in a combination';
$$;
