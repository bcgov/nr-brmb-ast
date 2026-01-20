create or replace function farms_bpu_pkg.margin_data_differences(
   in in_margin_data numeric[],
   in in_benchmark_per_unit_id numeric,
   in in_program_year numeric
)
returns integer
language plpgsql
as $$
declare
    c_benchmark_year cursor (p_year numeric, p_margin numeric) for
        select average_margin
        from farms.farm_benchmark_years
        where benchmark_per_unit_id = in_benchmark_per_unit_id
        and benchmark_year = p_year
        and average_margin <> p_margin;

    v_number_differences integer := 0;
    v_margin numeric;
    v_year numeric;
    v_benchmark_year record;
begin
    for ref_year_index in 1..6
    loop
        v_margin := in_margin_data[ref_year_index];
        v_year := in_program_year - ref_year_index;

        open c_benchmark_year(v_year, v_margin);
        fetch c_benchmark_year into v_benchmark_year;

        if found then
            v_number_differences := v_number_differences + 1;
        end if;

        close c_benchmark_year;
    end loop;

    return v_number_differences;
end;
$$;
