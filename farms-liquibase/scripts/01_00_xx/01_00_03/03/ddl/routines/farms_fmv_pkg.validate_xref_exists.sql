create or replace procedure farms_fmv_pkg.validate_xref_exists(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.farm_zfmv_fair_market_values z
        where not exists (
            select null
            from farms.farm_agristabilty_cmmdty_xref x
            where x.inventory_item_code = z.inventory_item_code
            and x.inventory_class_code in ('1', '2')
        );
    v_check record;

    v_msg varchar(200) := 'Inventory item code not found in Agristability_Commdty_Xref table';
begin
    for v_check in c_check
    loop
        call farms_fmv_pkg.insert_error(
            in_import_version_id,
            v_check.line_number,
            v_msg
        );
    end loop;
end;
$$;
