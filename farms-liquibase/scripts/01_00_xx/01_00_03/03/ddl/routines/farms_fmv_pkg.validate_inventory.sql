create or replace procedure farms_fmv_pkg.validate_inventory(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zfmv_fair_market_value
        where inventory_item_code not in (
            select inventory_item_code
            from farms.inventory_item_code
            where current_date between effective_date and expiry_date
        );
    v_check record;

    v_msg varchar(200) := 'Invalid inventory code';
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
