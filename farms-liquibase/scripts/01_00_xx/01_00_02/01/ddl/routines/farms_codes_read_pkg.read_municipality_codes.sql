create or replace function farms_codes_read_pkg.read_municipality_codes(
    in in_code farms.municipality_code.municipality_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.municipality_code code,
               t.description,
               t.effective_date,
               t.expiry_date,
               t.revision_count
        from farms.municipality_code t
        where (in_code is null or t.municipality_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
