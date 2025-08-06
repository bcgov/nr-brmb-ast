create or replace procedure farms_bpu_pkg.validate_municipality(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.farm_zbpu_benchmark_per_units
        where municipality_code not in (
            select municipality_code
            from farms.farm_municipality_codes
            where current_date between established_date and expiry_date
        );
    v_row record;

    v_msg varchar(200) := 'Invalid municipality';
begin
    for v_row in c_check
    loop
        call farms_bpu_pkg.insert_error(in_import_version_id, v_row.line_number, v_msg);
    end loop;
end;
$$;
