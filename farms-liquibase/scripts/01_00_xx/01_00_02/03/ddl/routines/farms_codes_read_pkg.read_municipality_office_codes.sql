create or replace function farms_codes_read_pkg.read_municipality_office_codes(
    in in_code farms.municipality_code.municipality_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.municipality_code,
               t.regional_office_code
        from farms.office_municipality_xref t
        where (in_code is null or t.municipality_code = in_code)
        order by t.municipality_code;
    return cur;
end;
$$;
