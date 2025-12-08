create or replace procedure farms_chefs_pkg.add_puc(
    in in_reported_amount farms.farm_productve_unit_capacities.productive_capacity_amount%type,
    in in_farming_operation_id farms.farm_productve_unit_capacities.farming_operation_id%type,
    in in_structure_group_code farms.farm_productve_unit_capacities.structure_group_code%type,
    in in_inventory_item_code farms.farm_productve_unit_capacities.inventory_item_code%type,
    in in_participnt_data_src_code farms.farm_productve_unit_capacities.participnt_data_src_code%type,
    in in_user farms.farm_productve_unit_capacities.who_updated%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_productve_unit_capacities (
        productve_unit_capacity_id,
        productive_capacity_amount,
        farming_operation_id,
        structure_group_code,
        inventory_item_code,
        participnt_data_src_code,
        who_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_puc_seq'),
        in_reported_amount,
        in_farming_operation_id,
        in_structure_group_code,
        in_inventory_item_code,
        in_participnt_data_src_code,
        in_user,
        in_user,
        current_timestamp
    );

end;
$$;
