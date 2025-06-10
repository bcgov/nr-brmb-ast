create or replace procedure farms_import_pkg.inventory_details(
    in in_user farms.program_year.create_user%type
)
language plpgsql
as $$
declare
    year_cursor cursor for
        with years as (
            select distinct z02.program_year
            from farms.z02_participant_farm_information z02
        )
        select years.program_year
        from years
        where not exists (
            select *
            from farms.inventory_item_detail iid
            where iid.program_year = years.program_year
        )
        order by years.program_year;
    year_rec record;

    v_rows_created numeric := 0;
begin

    for year_rec in year_cursor
    loop
        v_rows_created := farms_codes_write_pkg.copy_year_inventory_details(year_rec.program_year, in_user);
    end loop;

end;
$$;
