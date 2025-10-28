create or replace function farms_types_pkg.user_scenario_only_msg()
returns text
language sql
immutable
as
$$
    select 'Must be a USER scenario';
$$;
