create or replace procedure farms_bpu_pkg.validate_inventory(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    c_check cursor for
        select line_number
        from farms.zbpu_benchmark_per_unit
        where inventory_item_code not in (
            select inv.inventory_item_code
            from farms.inventory_item_code inv
            join farms.agristabilty_commodity_xref comm on comm.inventory_item_code = inv.inventory_item_code
            where comm.inventory_class_code in ('1', '2')
            and now() between inv.effective_date and inv.expiry_date
        )
        and inventory_item_code not in (
            select structure_group_code
            from farms.structure_group_code
            where now() between effective_date and expiry_date
        );
    v_row record;

    v_msg varchar(200) := 'Invalid inventory code';
begin
    for v_row in c_check
    loop
        call farms_bpu_pkg.insert_error(in_import_version_id, v_row.line_number, v_msg);
    end loop;
end;
$$;
