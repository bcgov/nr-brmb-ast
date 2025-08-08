create or replace procedure farms_fmv_pkg.validate_one_unit_per_inventry(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select z.line_number,
               z.inventory_item_code,
               t.number_of_unit_codes
        from farms.farm_zfmv_fair_market_values z
        join (
            select z.crop_unit_code,
                   z.inventory_item_code,
                   count(z.crop_unit_code) over (partition by z.inventory_item_code) as number_of_unit_codes
            from farms.farm_zfmv_fair_market_values z
            group by z.crop_unit_code,
                     z.inventory_item_code
        ) t on z.crop_unit_code = t.crop_unit_code and z.inventory_item_code = t.inventory_item_code
        where t.number_of_unit_codes > 1;
    v_check record;

    v_msg varchar(200) := 'More than one crop unit for inventory code ';
begin
    for v_check in c_check
    loop
        call farms_fmv_pkg.insert_error(
            in_import_version_id,
            v_check.line_number,
            v_msg || v_check.inventory_item_code
        );
    end loop;
end;
$$;
