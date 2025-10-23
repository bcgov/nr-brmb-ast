create or replace function farms_types_pkg.invalid_ref_scenario_count_msg()
returns text
language sql
immutable
as
$$
    select 'Cannot create Reference Scenario: one already exists';
$$;
