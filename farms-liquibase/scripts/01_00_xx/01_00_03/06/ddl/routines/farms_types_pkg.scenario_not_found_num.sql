create or replace function farms_types_pkg.scenario_not_found_num()
returns integer
language sql
immutable
as
$$
    select -20004;
$$;
