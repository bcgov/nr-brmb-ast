create or replace procedure farms_staging_pkg.insert_z29(
   in in_inventory_code farms.farm_z29_inventory_code_refs.inventory_code%type,
   in in_inventory_type_code farms.farm_z29_inventory_code_refs.inventory_type_code%type,
   in in_inventory_desc farms.farm_z29_inventory_code_refs.inventory_desc%type,
   in in_inventory_type_description farms.farm_z29_inventory_code_refs.inventory_type_description%type,
   in in_inventory_group_code farms.farm_z29_inventory_code_refs.inventory_group_code%type,
   in in_inventory_group_description farms.farm_z29_inventory_code_refs.inventory_group_description%type,
   in in_market_commodity_ind farms.farm_z29_inventory_code_refs.market_commodity_ind%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z29_inventory_code_refs (
        inventory_code,
        inventory_type_code,
        inventory_desc,
        inventory_type_description,
        inventory_group_code,
        inventory_group_description,
        market_commodity_ind,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        in_inventory_code,
        in_inventory_type_code,
        in_inventory_desc,
        in_inventory_type_description,
        in_inventory_group_code,
        in_inventory_group_description,
        in_market_commodity_ind,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
