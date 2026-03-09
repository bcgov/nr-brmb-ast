create or replace function farms_read_pkg.read_py_id_by_client_id(
    in pclientid farms.farm_program_years.agristability_client_id%type,
    in pyear farms.farm_program_years.year%type
)
returns table(
    program_year_id farms.farm_program_years.program_year_id%type
)
language sql
as $$
    select fpy.program_year_id
    from farms.farm_program_years fpy
    where fpy.agristability_client_id = pclientid
    and fpy.year = pyear;
$$;
