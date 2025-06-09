create or replace function farms_read_pkg.read_farm_type(
    in in_agristability_scenario_id farms.agristability_scenario.agristability_scenario_id%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select ftc.farm_type_code,
               ftc.description
        from farms.scenarios_vw sv
        join farms.program_year py on sv.program_year_id = py.program_year_id
        join farms.farm_type_code ftc on py.farm_type_code = ftc.farm_type_code
        where sv.agristability_scenario_id = in_agristability_scenario_id;

    return cur;

end;
$$;
