create or replace function farms_types_pkg.scenario_not_found_msg()
returns text
language sql
immutable
as
$$
    select 'Scenario Id not found';
$$;
