create or replace procedure farms_fmv_pkg.validate_municipality(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zfmv_fair_market_value
        where municipality_code not in (
            select municipality_code
            from farms.municipality_code
            where current_date between effective_date and expiry_date
        );
    v_check record;

    v_msg varchar(200) := 'Invalid municipality';
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
