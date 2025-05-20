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
    v_line_number numeric := 0;

    v_msg varchar(200) := 'Invalid municipality';
begin
    open c_check;
    loop
        fetch next from c_check into v_line_number;
        if not found then
            exit;
        end if;

        call farms_bpu_pkg.insert_error(in_import_version_id, v_line_number, v_msg);
    end loop;
    close c_check;
end;
$$;
