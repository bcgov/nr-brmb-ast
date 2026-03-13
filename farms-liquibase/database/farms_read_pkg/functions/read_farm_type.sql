create or replace function farms_read_pkg.read_farm_type(
    in in_agristability_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type
)
returns table(
    farm_type_code  farms.farm_farm_type_codes.farm_type_code%type,
    description     farms.farm_farm_type_codes.description%type
)
language sql
as $$
    select ftc.farm_type_code,
           ftc.description
    from farms.farm_scenarios_vw sv
    join farms.farm_program_years py on sv.program_year_id = py.program_year_id
    join farms.farm_farm_type_codes ftc on py.farm_type_code = ftc.farm_type_code
    where sv.agristability_scenario_id = in_agristability_scenario_id;
$$;
