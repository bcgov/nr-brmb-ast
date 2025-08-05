create or replace function farms_error_pkg.codify_reported_inventory(
    in msg varchar,
    in inventory_item_code farms.farm_agristabilty_cmmdty_xref.inventory_item_code%type,
    in inventory_class_code farms.farm_agristabilty_cmmdty_xref.inventory_class_code%type,
    in crop_unit_code farms.farm_reported_inventories.crop_unit_code%type,
    in farming_operation_id farms.farm_reported_inventories.farming_operation_id%type
)
returns varchar
language plpgsql
as $$
begin
    if msg like '%farm_ri_farm_cuc_fk%' then
        return 'The specified Crop Unit Code (' || crop_unit_code || ') was not found for this Reported Inventory';
    elsif msg like '%farm_ri_farm_as_fk%' then
        return 'The specified Scenario was not found for this Reported Inventory';
    elsif msg like '%farm_ri_farm_acx_fk%' then
        return 'The specified Commodity cross reference was not found for this Reported Inventory (' || inventory_item_code || ',' || inventory_class_code || ')';
    elsif msg like '%farm_ri_farm_fo_fk%' then
        return 'The specified Operation was not found for this Reported Inventory';
    elsif msg like 'INV_CLS_CD' then
        return 'The specified Inventory Class Code ('||inventory_class_code||') was not found';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
