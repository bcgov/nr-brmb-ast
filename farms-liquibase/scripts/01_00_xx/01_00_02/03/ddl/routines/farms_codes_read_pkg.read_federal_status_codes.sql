create or replace function farms_codes_read_pkg.read_federal_status_codes(
    in in_code farms.federal_status_code.federal_status_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.federal_status_code code,
               t.description,
               t.effective_date,
               t.expiry_date,
               t.revision_count
        from farms.federal_status_code t
        where (in_code is null or t.federal_status_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
