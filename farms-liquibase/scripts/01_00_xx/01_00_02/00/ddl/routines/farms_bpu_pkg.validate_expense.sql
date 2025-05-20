create or replace procedure farms_bpu_pkg.validate_expense(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select b.line_number,
            b.program_year,
            b.year_minus_6_expense,
            b.year_minus_5_expense,
            b.year_minus_4_expense,
            b.year_minus_3_expense,
            b.year_minus_2_expense,
            b.year_minus_1_expense
        from farms.zbpu_benchmark_per_unit b;
    v_row record;

    v_growing_forward_2013 numeric := 2013;
begin
    for v_row in c_check
    loop
        if v_row.program_year >= v_growing_forward_2013
            and (v_row.year_minus_6_expense is null
                or v_row.year_minus_5_expense is null
                or v_row.year_minus_4_expense is null
                or v_row.year_minus_3_expense is null
                or v_row.year_minus_2_expense is null
                or v_row.year_minus_1_expense is null) then
            call farms_bpu_pkg.insert_error(in_import_version_id, v_row.line_number, 'Expense values are required for 2013 forward.');
        elsif v_row.program_year < v_growing_forward_2013
            and (v_row.year_minus_6_expense is not null
                or v_row.year_minus_5_expense is not null
                or v_row.year_minus_4_expense is not null
                or v_row.year_minus_3_expense is not null
                or v_row.year_minus_2_expense is not null
                or v_row.year_minus_1_expense is not null) then
            call farms_bpu_pkg.insert_error(in_import_version_id, v_row.line_number, 'Expense values are not valid for years preceding 2013.');
        end if;
    end loop;
end;
$$;
