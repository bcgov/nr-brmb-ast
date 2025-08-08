create or replace procedure farms_import_pkg.line_items(
    in in_user farms.farm_program_years.who_created%type
)
language plpgsql
as $$
declare
    year_cursor cursor for
        with years as (
            select distinct z02.program_year
            from farms.farm_z02_partpnt_farm_infos z02
        )
        select years.program_year
        from years
        where not exists (
            select *
            from farms.farm_line_items li
            where li.program_year = years.program_year
        )
        order by years.program_year;
    year_rec record;

    v_rows_created numeric := 0;
begin

    for year_rec in year_cursor
    loop
        v_rows_created := farms_codes_write_pkg.copy_year_line_items(year_rec.program_year, in_user);
    end loop;

end;
$$;
