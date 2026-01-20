create or replace function farms_codes_read_pkg.read_sector_detail_codes(
    in in_code farms.farm_sector_detail_codes.sector_detail_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select sc.sector_code,
               sc.description sector_code_desc,
               sdc.sector_detail_code,
               sdc.description sector_detail_code_desc,
               sdc.established_date,
               sdc.expiry_date,
               sdc.revision_count
        from farms.farm_sector_detail_codes sdc
        join farms.farm_sector_detail_xref sdx on sdx.sector_detail_code = sdc.sector_detail_code
        join farms.farm_sector_codes sc on sc.sector_code = sdx.sector_code
        where (in_code is null or sdc.sector_detail_code = in_code)
        order by lower(sc.description),
                 lower(sdc.description);
    return cur;

end;
$$;
