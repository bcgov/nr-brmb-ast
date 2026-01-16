create or replace function farms_codes_read_pkg.read_crop_unit_codes(
    in in_code farms.farm_crop_unit_codes.crop_unit_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select t.crop_unit_code code,
               t.description,
               t.established_date,
               t.expiry_date,
               t.revision_count
        from farms.farm_crop_unit_codes t
        where (in_code is null or t.crop_unit_code = in_code)
        order by lower(t.description);
    return cur;
end;
$$;
