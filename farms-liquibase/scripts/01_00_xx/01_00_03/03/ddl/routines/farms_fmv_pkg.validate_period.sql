create or replace procedure farms_fmv_pkg.validate_period(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zfmv_fair_market_value
        where period not between 1 and 12;
    v_check record;

    v_msg varchar(200) := 'Invalid period (must be bwetween 1 and 12)';
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
