create or replace function farms_codes_read_pkg.read_program_years()
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select distinct py.year program_year
        from farms.program_year py
        order by program_year desc;
    return cur;
end;
$$;
