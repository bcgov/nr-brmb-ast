create or replace procedure farms_bpu_pkg.validate_municipality(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zbpu_benchmark_per_unit
        where municipality_code not in (
            select municipality_code
            from farms.municipality_code
            where current_date between effective_date and expiry_date
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
