create or replace function farms_codes_read_pkg.read_farm_type_codes(
    in in_code farms.farm_type_code.farm_type_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.farm_type_code code,
               t.description,
               t.effective_date,
               t.expiry_date,
               t.revision_count
        from farms.farm_type_code t
        where (in_code is null or t.farm_type_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
