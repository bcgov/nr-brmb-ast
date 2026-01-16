create or replace function farms_codes_write_pkg.in_use_municipality_code(
    in in_municipality_code farms.farm_municipality_codes.municipality_code%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin
    select (case
        when exists(
            select null
            from farms.farm_benchmark_per_units t
            where t.municipality_code = in_municipality_code
        )
        or exists(
            select null
            from farms.farm_fair_market_values t
            where t.municipality_code = in_municipality_code
        )
        or exists(
            select null
            from farms.farm_program_year_versions t
            where t.municipality_code = in_municipality_code
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
