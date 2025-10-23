create or replace function farms_types_pkg.user_scenario_only_code()
returns integer
language sql
immutable
as
$$
    select -20016;
$$;
