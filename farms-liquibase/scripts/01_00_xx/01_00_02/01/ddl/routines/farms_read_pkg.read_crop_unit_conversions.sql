create or replace function farms_read_pkg.read_crop_unit_conversions(
    in op_ids numeric[],
    in sc_ids numeric[]
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        with xids as (
            select i.agristabilty_commodity_xref_id
            from farms.reported_inventory i
            where i.farming_operation_id = any(op_ids)
            and i.agristability_scenario_id is null
            union
            select i.agristabilty_commodity_xref_id
            from farms.reported_inventory i
            where i.agristability_scenario_id = any(sc_ids)
        )
        select x.inventory_item_code,
               dcuc.crop_unit_code default_crop_unit_code,
               dcuc.description default_crop_unit_code_description,
               cfcuc.crop_unit_code target_crop_unit_code,
               cfcuc.description target_crop_unit_code_description,
               cucf.conversion_factor
        from xids
        join farms.agristabilty_commodity_xref x on x.agristabilty_commodity_xref_id = xids.agristabilty_commodity_xref_id
        join farms.crop_unit_default cud on cud.inventory_item_code = x.inventory_item_code
        join farms.crop_unit_conversion_factor cucf on cucf.inventory_item_code = x.inventory_item_code
        join farms.crop_unit_code dcuc on dcuc.crop_unit_code = cud.crop_unit_code
        join farms.crop_unit_code cfcuc on cfcuc.crop_unit_code = cucf.target_crop_unit_code
        where x.inventory_class_code = '1';

    return cur;
end;
$$;
