create or replace procedure farms_fmv_pkg.validate_uniqueness(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select t.line_number,
               t.inventory_class_code
        from (
            select z.line_number,
                   x.inventory_class_code,
                   row_number() over (partition by z.program_year, z.period, z.inventory_item_code, z.municipality_code, z.crop_unit_code order by z.line_number) unique_crop_num,
                   row_number() over (partition by z.program_year, z.period, z.inventory_item_code, z.municipality_code order by z.line_number) unique_livestock_num
            from farms.farm_zfmv_fair_market_values z
            join farms.farm_agristabilty_cmmdty_xref x on x.inventory_item_code = z.inventory_item_code
            where x.inventory_class_code in ('1', '2')
        ) t
        where (t.inventory_class_code = '1' and t.unique_crop_num > 1)
        or (t.inventory_class_code = '2' and t.unique_livestock_num > 1)
        order by t.line_number;
    v_check record;

    v_msg varchar(200);
    v_crop_msg varchar(200) := 'Duplicate combination of year, period, item code, municipality, and crop unit code found for a crop item';
    v_livestock_msg varchar(200) := 'Duplicate combination of year, period, item code, and municipality found for a livestock item';
begin
    for v_check in c_check
    loop
        if v_check.inventory_class_code = '1' then
            v_msg := v_crop_msg;
        elsif v_check.inventory_class_code = '2' then
            v_msg := v_livestock_msg;
        else
            v_msg := null;
        end if;

        call farms_fmv_pkg.insert_error(
            in_import_version_id,
            v_check.line_number,
            v_msg
        );
    end loop;
end;
$$;
