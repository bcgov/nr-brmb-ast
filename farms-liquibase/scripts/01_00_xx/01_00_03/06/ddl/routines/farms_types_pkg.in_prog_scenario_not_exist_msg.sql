create or replace function farms_types_pkg.in_prog_scenario_not_exist_msg()
returns text
language sql
immutable
as
$$
    select 'In Progress scenario does not exist';
$$;
