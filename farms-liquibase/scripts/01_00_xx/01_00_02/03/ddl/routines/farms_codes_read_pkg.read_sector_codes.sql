create or replace function farms_codes_read_pkg.read_sector_codes(
    in in_code farms.farm_sector_codes.sector_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select t.sector_code,
               t.description,
               t.established_date,
               t.expiration_date,
               t.revision_count
        from farms.farm_sector_codes t
        where (in_code is null or t.sector_code = in_code)
        order by lower(t.description);
    return cur;

end;
$$;
