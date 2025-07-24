create or replace procedure farms_fmv_pkg.validate_variance(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zfmv_fair_market_value
        where percent_variance not between 0 and 100;
    v_check record;

    v_msg varchar(200) := 'Invalid variance (must be bwetween 0 and 100)';
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
