create or replace function farms_error_pkg.codify_productive_unit_capacity(
    in msg varchar,
    in farming_operation_id farms.productive_unit_capacity.farming_operation_id%type,
    in structure_group_code farms.productive_unit_capacity.structure_group_code%type,
    in inventory_item_code farms.productive_unit_capacity.inventory_item_code%type
)
returns varchar
language plpgsql
as $$
begin
    if msg like '%fk_puc_sgc%' then
        return 'The specified Livestock Inventory Code (' || structure_group_code || ') was not found for this Productive Unit Capacity';
    elsif msg like '%fk_puc_as%' then
        return 'The specified Scenario was not found for this Productive Unit Capacity';
    elsif msg like '%fk_puc_fo%' then
        return 'The specified Operation was not found for this Productive Unit Capacity';
    elsif msg like '%fk_puc_iic%' then
        return 'The specified Inventory Code (' || inventory_item_code || ') was not found for this Productive Unit Capacity';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
