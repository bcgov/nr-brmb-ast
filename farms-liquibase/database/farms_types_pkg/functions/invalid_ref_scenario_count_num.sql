create or replace function farms_types_pkg.invalid_ref_scenario_count_num()
returns integer
language sql
immutable
as
$$
    select -20014;
$$;
