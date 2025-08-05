create or replace function farms_bpu_pkg.expense_data_differences(
   in in_expense_data numeric[],
   in in_benchmark_per_unit_id numeric,
   in in_program_year numeric
)
returns integer
language plpgsql
as $$
declare
    c_benchmark_year cursor (p_year numeric, p_expense numeric) for
        select average_expense
        from farms.farm_benchmark_years
        where benchmark_per_unit_id = in_benchmark_per_unit_id
        and benchmark_year = p_year
        and (average_expense <> p_expense
            or (average_expense is null and p_expense is not null)
            or (average_expense is not null and p_expense is null)
        );

    v_number_differences integer := 0;
    v_expense numeric;
    v_year numeric;
    v_benchmark_year record;
begin
    for ref_year_index in 1..6
    loop
        v_expense := in_expense_data[ref_year_index];
        v_year := in_program_year - ref_year_index;

        open c_benchmark_year(v_year, v_expense);
        fetch c_benchmark_year into v_benchmark_year;

        if found then
            v_number_differences := v_number_differences + 1;
        end if;

        close c_benchmark_year;
    end loop;

    return v_number_differences;
end;
$$;
