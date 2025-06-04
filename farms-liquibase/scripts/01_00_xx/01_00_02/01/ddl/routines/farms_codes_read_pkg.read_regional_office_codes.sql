create or replace function farms_codes_read_pkg.read_regional_office_codes(
    in in_code farms.regional_office_code.regional_office_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.regional_office_code code,
               t.description,
               t.effective_date,
               t.expiry_date,
               t.revision_count
        from farms.regional_office_code t
        where (in_code is null or t.regional_office_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
