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
            select i.agristabilty_cmmdty_xref_id
            from farms.farm_reported_inventories i
            where i.farming_operation_id = any(op_ids)
            and i.agristability_scenario_id is null
            union
            select i.agristabilty_cmmdty_xref_id
            from farms.farm_reported_inventories i
            where i.agristability_scenario_id = any(sc_ids)
        )
        select x.inventory_item_code,
               dcuc.crop_unit_code default_crop_unit_code,
               dcuc.description default_crop_unit_code_description,
               cfcuc.crop_unit_code target_crop_unit_code,
               cfcuc.description target_crop_unit_code_description,
               cucf.conversion_factor
        from xids
        join farms.farm_agristabilty_cmmdty_xref x on x.agristabilty_cmmdty_xref_id = xids.agristabilty_cmmdty_xref_id
        join farms.farm_crop_unit_defaults cud on cud.inventory_item_code = x.inventory_item_code
        join farms.farm_crop_unit_conversn_fctrs cucf on cucf.inventory_item_code = x.inventory_item_code
        join farms.farm_crop_unit_codes dcuc on dcuc.crop_unit_code = cud.crop_unit_code
        join farms.farm_crop_unit_codes cfcuc on cfcuc.crop_unit_code = cucf.target_crop_unit_code
        where x.inventory_class_code = '1';

    return cur;
end;
$$;
