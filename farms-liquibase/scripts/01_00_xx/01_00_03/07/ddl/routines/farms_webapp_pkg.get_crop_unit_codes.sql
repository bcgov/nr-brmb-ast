create or replace function farms_webapp_pkg.get_crop_unit_codes()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select crop_unit_code code,
               description
        from farms.farm_crop_unit_codes
        where current_timestamp between established_date and expiry_date
        /* Do not include code 32 for Head (Livestock)
           since we only want those for INVENTORY_CLASS_CODE 1 - Crop.
           Also exclude code 0 for Unknown since users should not select this. */
        and crop_unit_code not in ('0', '32')
        order by description asc;
    return v_cursor;
end;
$$;
