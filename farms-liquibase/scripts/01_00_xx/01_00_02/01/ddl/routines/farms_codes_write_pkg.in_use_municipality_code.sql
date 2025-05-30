create or replace function farms_codes_write_pkg.in_use_municipality_code(
    in in_municipality_code farms.municipality_code.municipality_code%type
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
            from farms.benchmark_per_unit t
            where t.municipality_code = in_municipality_code
        )
        or exists(
            select null
            from farms.fair_market_value t
            where t.municipality_code = in_municipality_code
        )
        or exists(
            select null
            from farms.program_year_version t
            where t.municipality_code = in_municipality_code
        )
        then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
