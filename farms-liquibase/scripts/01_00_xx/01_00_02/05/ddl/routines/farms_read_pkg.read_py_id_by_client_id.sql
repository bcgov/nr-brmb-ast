create or replace function farms_read_pkg.read_py_id_by_client_id(
    in pclientid farms.program_year.agristability_client_id%type,
    in pyear farms.program_year.year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select fpy.program_year_id
        from farms.program_year fpy
        where fpy.agristability_client_id = pclientid
        and fpy.year = pyear;

    return cur;

end;
$$;
