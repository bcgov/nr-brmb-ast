create or replace procedure farms_fmv_pkg.validate_defaults(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select z.line_number,
               z.inventory_item_code,
               d.crop_unit_code default_crop_unit_code,
               cuc.description default_crop_unit_code_desc
        from farms.zfmv_fair_market_value z
        join farms.crop_unit_default d on d.inventory_item_code = z.inventory_item_code
        join farms.crop_unit_code cuc on cuc.crop_unit_code = d.crop_unit_code
        where d.crop_unit_code != z.crop_unit_code;
    v_check record;

    v_msg varchar(200) := 'Crop unit does not match the default. Default for ';
begin
    for v_check in c_check
    loop
        call farms_fmv_pkg.insert_error(
            in_import_version_id,
            v_check.line_number,
            v_msg || v_check.inventory_item_code || ' is ' || v_check.default_crop_unit_code || ' - ' || v_check.default_crop_unit_code_desc
        );
    end loop;
end;
$$;
