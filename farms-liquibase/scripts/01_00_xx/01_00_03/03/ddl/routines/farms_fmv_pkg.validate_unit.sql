create or replace procedure farms_fmv_pkg.validate_unit(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zfmv_fair_market_value
        where crop_unit_code not in (
            select crop_unit_code
            from farms.crop_unit_code
            where current_date between effective_date and expiry_date
        );
    v_check record;

    v_msg varchar(200) := 'Invalid crop unit code';
begin
    for v_check in c_check
    loop
        call farms_fmv_pkg.insert_error(
            in_import_version_id,
            v_check.line_number,
            v_msg
        );
    end loop;
end;
$$;
