create or replace procedure farms_staging_pkg.insert_z29(
   in in_inventory_code farms.z29_inventory_code_reference.inventory_code%type,
   in in_inventory_type_code farms.z29_inventory_code_reference.inventory_type_code%type,
   in in_inventory_desc farms.z29_inventory_code_reference.inventory_description%type,
   in in_inventory_type_description farms.z29_inventory_code_reference.inventory_type_description%type,
   in in_inventory_group_code farms.z29_inventory_code_reference.inventory_group_code%type,
   in in_inventory_group_description farms.z29_inventory_code_reference.inventory_group_description%type,
   in in_market_commodity_ind farms.z29_inventory_code_reference.market_commodity_indicator%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z29_inventory_code_reference (
        inventory_code,
        inventory_type_code,
        inventory_description,
        inventory_type_description,
        inventory_group_code,
        inventory_group_description,
        market_commodity_indicator,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
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
