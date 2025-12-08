create or replace function farms_types_pkg.in_prog_scenario_not_exist_num()
returns integer
language sql
immutable
as
$$
    select -20012;
$$;
