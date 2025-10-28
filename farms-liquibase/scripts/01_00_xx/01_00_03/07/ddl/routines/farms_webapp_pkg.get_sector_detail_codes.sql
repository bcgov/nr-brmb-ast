create or replace function farms_webapp_pkg.get_sector_detail_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select sc.sector_code,
               sc.description sector_code_description,
               sdc.sector_detail_code,
               sdc.description sector_detail_code_description
        from farms.farm_sector_detail_codes sdc
        join farms.farm_sector_detail_xref sdx on sdx.sector_detail_code = sdc.sector_detail_code
        join farms.farm_sector_codes sc on sc.sector_code = sdx.sector_code
        where current_timestamp between sdc.established_date and sdc.expiry_date
        order by sdc.description asc;
    return v_cursor;
end;
$$;
