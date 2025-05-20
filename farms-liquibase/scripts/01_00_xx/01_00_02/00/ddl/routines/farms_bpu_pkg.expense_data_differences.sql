create or replace function farms_bpu_pkg.expense_data_differences(
   in in_expense_data numeric[],
   in in_benchmark_per_unit_id numeric,
   in in_program_year numeric
)
returns integer
language plpgsql
as $$
declare
    v_number_differences integer := 0;
    v_expense numeric;
    v_year numeric;
    v_exists boolean;
begin
    for ref_year_index in 1..6
    loop
        v_expense := in_expense_data[ref_year_index];
        v_year := in_program_year - ref_year_index;

        select exists (
            select 1
            from farms.benchmark_year
            where benchmark_per_unit_id = in_benchmark_per_unit_id
            and benchmark_year = v_year
            and (average_expense <> v_expense
                or (average_expense is null and v_expense is not null)
                or (average_expense is not null and v_expense is null)
            )
        ) into v_exists;

        if v_exists then
            v_number_differences := v_number_differences + 1;
        end if;
    end loop;

    return v_number_differences;
end;
$$;
